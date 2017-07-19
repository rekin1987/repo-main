package pl.emget.pasubleclientsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import pl.emget.pasubleserverprofile.ServerProfile;

public class CustomPeripheralActivity extends Activity implements GattConnectionCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "CONNECTION_ACTIVITY_EXTRAS_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "CONNECTION_ACTIVITY_EXTRAS_DEVICE_ADDRESS";

    private Handler mUiHandler;
    private TextView mStatusLabel;
    private TextView mManufacturerLabel;
    private TextView mTemperatureLabel;
    private TextView mVoltageLabel;

    private String mDeviceName;
    private String mDeviceAddress;
    private String mManufacturerName;
    private String mFirmwareVersion;

    private GattConnectionWrapper mGattConnectionWrapper;
    private BluetoothGattCharacteristic mColorSetCharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_custom_peripheral);

        mUiHandler = new Handler();
        mDeviceName = getIntent().getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mGattConnectionWrapper = new GattConnectionWrapper(this, this, mDeviceAddress);
        mGattConnectionWrapper.init();
        setupUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGattConnectionWrapper.shutdown();
    }

    private void setupUI() {
        ((TextView) findViewById(R.id.deviceDetails)).setText(String.format(getString(R.string.device_details_label), mDeviceName, mDeviceAddress));
        mStatusLabel = (TextView) findViewById(R.id.statusLabel);
        mManufacturerLabel = (TextView) findViewById(R.id.manufacturerLabel);
        mTemperatureLabel = (TextView) findViewById(R.id.temperatureLabel);
        mVoltageLabel = (TextView) findViewById(R.id.voltageLabel);
        getActionBar().setTitle(mDeviceName);
    }

    @Override
    public void postStatusUpdate(final String status) {
        Log.d(TAG, "postStatusUpdate()");
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mStatusLabel.setText(status);
            }
        });
    }

    @Override
    public void onServicesDiscovered(List<BluetoothGattService> services) {
        Log.d(TAG, "onServicesDiscovered()");
        for (BluetoothGattService service : services) {
            if (service.getUuid().equals(ServerProfile.DEVICE_INFO_SERVICE_UUID)) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic ch : characteristics) {
                    mGattConnectionWrapper.readCharacteristic(ch);
                }
            } else if (service.getUuid().equals(ServerProfile.CUSTOM_METRICS_SERVICE_UUID)) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic ch : characteristics) {
                    if (ch.getUuid().equals(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID)) {
                        mGattConnectionWrapper.setCharacteristicNotification(ch, true);
                    } else if (ch.getUuid().equals(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID)) {
                        mGattConnectionWrapper.setCharacteristicNotification(ch, true);
                    } else if (ch.getUuid().equals(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_COLOR_UUID)) {
                        mColorSetCharacteristic = ch;
                    }
                }
            }
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "onCharacteristicRead()");
        if (characteristic.getUuid().equals(ServerProfile.DEVICE_INFO_CHARACTERISTIC_MANUFACTURER_NAME_UUID)) {
            mManufacturerName = new String(characteristic.getValue());
        } else if (characteristic.getUuid().equals(ServerProfile.DEVICE_INFO_CHARACTERISTIC_FIRMWARE_VERSION_UUID)) {
            mFirmwareVersion = new String(characteristic.getValue());
        }
        if (mManufacturerName != null && mFirmwareVersion != null) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mManufacturerLabel.setText(String.format(getString(R.string.firmware_label), mManufacturerName, mFirmwareVersion));
                }
            });
        }
    }

    @Override
    public void onCharacteristicNotification(final BluetoothGattCharacteristic ch) {
        Log.d(TAG, "onCharacteristicNotification()");
        if (ch.getUuid().equals(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID)) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTemperatureLabel.setText(String.format(getString(R.string.temperature_label), new String(ch.getValue())));
                }
            });
        } else if (ch.getUuid().equals(ServerProfile.CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID)) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mVoltageLabel.setText(String.format(getString(R.string.voltage_label), new String(ch.getValue())));
                }
            });
        }
    }

    public void onConnectClick(View view) {
        if (!mGattConnectionWrapper.isConnected()) {
            mGattConnectionWrapper.connect();
        }
    }

    public void onColorChangeClick(View view) {
        CharSequence[] array = {"Transparent", "Red", "Green", "Blue"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set lamp color")
                .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGattConnectionWrapper.writeCharacteristic(mColorSetCharacteristic, String.valueOf(which));
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
