package pl.emget.pasubleclientsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import static android.content.Context.BLUETOOTH_SERVICE;

public class GattConnectionWrapper {

    private static final String TAG = GattConnectionWrapper.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    private GattConnectionCallback mGattConnectionCallback;
    private Context mContext;
    private String mDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    private boolean mIsConnected;

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
                mIsConnected = true;
                mGattConnectionCallback.postStatusUpdate("Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery");
                mGattConnectionCallback.postStatusUpdate("Attempting to start service discovery.");
                mBluetoothGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
                mIsConnected = false;
                mGattConnectionCallback.postStatusUpdate("Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onServicesDiscovered()");
                mGattConnectionCallback.onServicesDiscovered(gatt.getServices());
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
                mGattConnectionCallback.postStatusUpdate("onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onCharacteristicRead()");
                mGattConnectionCallback.onCharacteristicRead(characteristic);
            } else {
                Log.w(TAG, "onCharacteristicRead() status: " + status);
                mGattConnectionCallback.postStatusUpdate("onCharacteristicRead() status: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i(TAG, "onCharacteristicChanged()");
            mGattConnectionCallback.onCharacteristicNotification(characteristic);
        }
    };

    public GattConnectionWrapper(GattConnectionCallback callback, Context context, String deviceAddress) {
        mGattConnectionCallback = callback;
        mContext = context;
        mDeviceAddress = deviceAddress;
    }

    public void init() {
        BluetoothManager aBluetoothManager = (BluetoothManager) mContext.getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = aBluetoothManager.getAdapter();
    }

    public void connect() {
        connect(mDeviceAddress);
    }

    public void shutdown() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        mBluetoothAdapter = null;
        mIsConnected = false;
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth
     * .BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "readCharacteristic()");
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        //        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes
        // .CLIENT_CHARACTERISTIC_CONFIG));
        //        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        //        mBluetoothGatt.writeDescriptor(descriptor);
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     */
    private void connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            mGattConnectionCallback.postStatusUpdate("BluetoothAdapter not initialized or unspecified address.");
            return;
        }

        // Previously connected device.  Try to reconnect.
        if (mDeviceAddress != null && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            mBluetoothGatt.connect();
            return;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            mGattConnectionCallback.postStatusUpdate("Device not found.  Unable to connect.");
            return;
        }
        // We want to directly connect to the device, so we are setting the autoConnect parameter to false.
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
    }

}
