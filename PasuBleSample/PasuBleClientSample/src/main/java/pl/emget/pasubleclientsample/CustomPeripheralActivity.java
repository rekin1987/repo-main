package pl.emget.pasubleclientsample;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class CustomPeripheralActivity extends Activity implements GattConnectionCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "CONNECTION_ACTIVITY_EXTRAS_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "CONNECTION_ACTIVITY_EXTRAS_DEVICE_ADDRESS";
    private Handler mHandler;
    private TextView mStatusLabel;

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;

    private GattConnectionWrapper mGattConnectionWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_custom_peripheral);

        mHandler = new Handler();
        mDeviceName = getIntent().getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mGattConnectionWrapper = new GattConnectionWrapper(this, this, mDeviceAddress);
        mGattConnectionWrapper.init();
        setupUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupUI() {
        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        //        mConnectionState = (TextView) findViewById(R.id.connection_state);
        //        mDataField = (TextView) findViewById(R.id.data_value);

        getActionBar().setTitle(mDeviceName);
    }

    @Override
    public void postStatusUpdate(String status) {
        Log.d(TAG, "postStatusUpdate()");
    }

    @Override
    public void onServicesDiscovered(List<BluetoothGattService> services) {
        Log.d(TAG, "onServicesDiscovered()");
    }

    @Override
    public void onCharacteristicRead(BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "onCharacteristicRead()");
    }

    @Override
    public void onCharacteristicNotification(BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "onCharacteristicNotification()");
    }
}
