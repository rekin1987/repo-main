package pl.emget.pasubleserversample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

import pl.emget.pasubleserverprofile.ServerProfile;

import static android.content.Context.BLUETOOTH_SERVICE;

public class GattServerWrapper {

    private static final String TAG = GattServerWrapper.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BluetoothGattServer mGattServer;
    private BluetoothDevice mConnectedDevice;

    private MainActivityInterface mParentInterface;

    /*
     * Callback handles all incoming requests from GATT clients.
     * From connections to read/write requests.
     */
    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.d(TAG, "onConnectionStateChange " + ServerProfile.getStatusDescription(status) + " " + ServerProfile.getStateDescription(newState));
            String devName = device.getName() == null ? "" : device.getName() + " : ";
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mParentInterface.postStatusUpdate("Connected device: " + devName + device.getAddress());
                mConnectedDevice = device;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mParentInterface.postStatusUpdate("Disconnected device: " + devName + device.getAddress());
                mConnectedDevice = null;
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            UUID charUUID = characteristic.getUuid();
            Log.d(TAG, "onCharacteristicReadRequest " + ServerProfile.getAttributeNameFromUUID(charUUID));

            if (ServerProfile.DEVICE_INFO_CHARACTERISTIC_MANUFACTURER_NAME_UUID.equals(charUUID)) {
                String stringVal = mParentInterface.getManufacturer();
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, stringVal.getBytes());
            } else if (ServerProfile.DEVICE_INFO_CHARACTERISTIC_FIRMWARE_VERSION_UUID.equals(charUUID)) {
                String stringVal = mParentInterface.getFirmwareVersion();
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, stringVal.getBytes());
            } else if (ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID.equals(charUUID)) {
                String stringVal = mParentInterface.getTemperatureValue();
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, stringVal.getBytes());
            } else if (ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID.equals(charUUID)) {
                String stringVal = mParentInterface.getVoltageValue();
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, stringVal.getBytes());
            } else if (ServerProfile.FLIR_METERLINK_CHARACTERISTIC_UUID.equals(charUUID)) {
                String stringVal = mParentInterface.getMeterlinkData();
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, stringVal.getBytes());
            } else {
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, 0, null);
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean
                preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            UUID charUUID = characteristic.getUuid();
            Log.d(TAG, "onCharacteristicWriteRequest " + ServerProfile.getAttributeNameFromUUID(charUUID));

            if (ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_COLOR_UUID.equals(charUUID)) {
                mParentInterface.setColorValue(Integer.parseInt(new String(value)));
                if (responseNeeded) {
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, value);
                }
            }
        }
    };

    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i(TAG, "Peripheral Advertise Started.");
            mParentInterface.postStatusUpdate("GATT Server Ready");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.w(TAG, "Peripheral Advertise Failed: " + errorCode);
            mParentInterface.postStatusUpdate("GATT Server Error " + errorCode);
        }
    };

    /**
     * Constructor.
     *
     * @param parentActivityInterface
     */
    public GattServerWrapper(MainActivityInterface parentActivityInterface) {
        mParentInterface = parentActivityInterface;
    }

    public void init() {
        setupBluetooth();
        setupServicesAndCharacteristics();
    }

    public void startAdvertising() {
        if (mBluetoothLeAdvertiser != null) {
            AdvertiseSettings settings = new AdvertiseSettings.Builder().setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED).setConnectable
                    (true).setTimeout(0).setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM).build();

            AdvertiseData data = new AdvertiseData.Builder().setIncludeDeviceName(true).addServiceUuid(new ParcelUuid(ServerProfile
                    .CUSTOM_METRICS_SERVICE_UUID)).build();
            mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
        }
    }

    public void notifyValuesChanged() {
        if (mConnectedDevice != null) {
            // temperature characteristic
            BluetoothGattCharacteristic tempCharacteristic = mGattServer.getService(ServerProfile.CUSTOM_METRICS_SERVICE_UUID).getCharacteristic
                    (ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID);
            tempCharacteristic.setValue(mParentInterface.getTemperatureValue());
            mGattServer.notifyCharacteristicChanged(mConnectedDevice, tempCharacteristic, false);
            // voltage characteristic
            BluetoothGattCharacteristic voltCharacteristic = mGattServer.getService(ServerProfile.CUSTOM_METRICS_SERVICE_UUID).getCharacteristic
                    (ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID);
            voltCharacteristic.setValue(mParentInterface.getVoltageValue());
            mGattServer.notifyCharacteristicChanged(mConnectedDevice, voltCharacteristic, false);
        }
    }

    public void shutdown() {
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mBluetoothLeAdvertiser = null;
        }
        if (mGattServer != null) {
            mGattServer.close();
            mGattServer = null;
        }
        mBluetoothManager = null;
        mBluetoothAdapter = null;
    }

    private void setupBluetooth() {
        mBluetoothManager = (BluetoothManager) mParentInterface.getContext().getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            mParentInterface.postStatusUpdate("Bluetooth disabled. Turn it on and restart app.");
            return;
        }

        if (!mParentInterface.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mParentInterface.postStatusUpdate("No LE Support. App will not work.");
            return;
        }

        if (!mBluetoothAdapter.isMultipleAdvertisementSupported()) {
            mParentInterface.postStatusUpdate("No Advertising Support. App will not work.");
            return;
        }

        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mGattServer = mBluetoothManager.openGattServer(mParentInterface.getContext(), mGattServerCallback);
    }

    private void setupServicesAndCharacteristics() {
        BluetoothGattService deviceInfoService = new BluetoothGattService(ServerProfile.DEVICE_INFO_SERVICE_UUID, BluetoothGattService
                .SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic charManufacturer = new BluetoothGattCharacteristic(ServerProfile
                .DEVICE_INFO_CHARACTERISTIC_MANUFACTURER_NAME_UUID, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic
                .PERMISSION_READ);
        BluetoothGattCharacteristic charFirmware = new BluetoothGattCharacteristic(ServerProfile.DEVICE_INFO_CHARACTERISTIC_FIRMWARE_VERSION_UUID,
                BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);

        BluetoothGattService customMetricsService = new BluetoothGattService(ServerProfile.CUSTOM_METRICS_SERVICE_UUID, BluetoothGattService
                .SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic charTemp = new BluetoothGattCharacteristic(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ);
        BluetoothGattCharacteristic charVoltage = new BluetoothGattCharacteristic(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ);
        BluetoothGattCharacteristic charColor = new BluetoothGattCharacteristic(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_COLOR_UUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE);

        deviceInfoService.addCharacteristic(charManufacturer);
        deviceInfoService.addCharacteristic(charFirmware);

        customMetricsService.addCharacteristic(charTemp);
        customMetricsService.addCharacteristic(charVoltage);
        customMetricsService.addCharacteristic(charColor);

        mGattServer.addService(deviceInfoService);
        // app crashes when adding two services without delay
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        mGattServer.addService(customMetricsService);

        addMeterlinkService();
    }

    private void addMeterlinkService() {
        BluetoothGattService customMetricsService = new BluetoothGattService(ServerProfile.FLIR_METERLINK_SERVICE_UUID, BluetoothGattService
                .SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic dataChar = new BluetoothGattCharacteristic(ServerProfile.FLIR_METERLINK_CHARACTERISTIC_UUID,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ);
        customMetricsService.addCharacteristic(dataChar);
        // app crashes when adding two services without delay
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        mGattServer.addService(customMetricsService);
    }

    public void notifyMeterlinkValuesChanged() {
        if (mConnectedDevice != null) {
            // data characteristic
            BluetoothGattCharacteristic dataCharacteristic = mGattServer.getService(ServerProfile.FLIR_METERLINK_SERVICE_UUID).getCharacteristic
                    (ServerProfile.FLIR_METERLINK_CHARACTERISTIC_UUID);
            dataCharacteristic.setValue(String.valueOf(mParentInterface.getMeterlinkData()));
            mGattServer.notifyCharacteristicChanged(mConnectedDevice, dataCharacteristic, false);
        }
    }
}
