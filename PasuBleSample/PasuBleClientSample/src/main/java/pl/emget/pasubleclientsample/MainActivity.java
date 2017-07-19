package pl.emget.pasubleclientsample;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements MainActivityInterface {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private GattScannerWrapper mGattScannerWrapper;
    private TextView mStatusLabel;
    private Handler mUiHandler;
    private Handler mAutoStopDiscoveryHandler;
    private Runnable mAutoStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            if (mGattScannerWrapper.isScanning()) {
                mGattScannerWrapper.shutdown();
                mStatusLabel.setText(getString(R.string.status_label_idle));
            }
        }
    };

    // Required Permissions
    private static final int REQUEST_REQUIRED_PERMISSIONS_CODE = 0x11; // Can only use lower 8 bits for requestCode
    private static String[] PERMISSIONS_FOR_DISCOVERY = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        setupUI();
        mUiHandler = new Handler();
        mAutoStopDiscoveryHandler = new Handler();
        mGattScannerWrapper = new GattScannerWrapper(this);
        verifyRequiredPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGattScannerWrapper.shutdown();
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void postStatusUpdate(final String status) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mStatusLabel.setText(status);
            }
        });
    }

    @Override
    public void onDeviceFound(final BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLeDeviceListAdapter.addDevice(device);
                mLeDeviceListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onScanButtonClick(View view) {
        if (mGattScannerWrapper.isScanning()) {
            mGattScannerWrapper.shutdown();
            mStatusLabel.setText(getString(R.string.status_label_idle));
        } else {
            mLeDeviceListAdapter.clear();
            mLeDeviceListAdapter.notifyDataSetChanged();
            mGattScannerWrapper.init();
            mGattScannerWrapper.startScan();
            mStatusLabel.setText(getString(R.string.status_label_scanning));
            scheduleAutoStopScan();
        }
    }

    private void scheduleAutoStopScan() {
        mAutoStopDiscoveryHandler.removeCallbacks(mAutoStopScanRunnable);
        mAutoStopDiscoveryHandler.postDelayed(mAutoStopScanRunnable, 10000);
    }

    private void verifyRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if we have permissions
            for (int i = 0; i < PERMISSIONS_FOR_DISCOVERY.length; ++i) {
                int permission = checkSelfPermission(PERMISSIONS_FOR_DISCOVERY[i]);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    requestPermissions(PERMISSIONS_FOR_DISCOVERY, REQUEST_REQUIRED_PERMISSIONS_CODE);
                    break;
                }
            }
        }
    }

    private void setupUI() {
        mStatusLabel = (TextView) findViewById(R.id.statusLabel);
        mLeDeviceListAdapter = new LeDeviceListAdapter(getLayoutInflater());
        ListView devicesListView = (ListView) findViewById(R.id.devicesListView);
        devicesListView.setAdapter(mLeDeviceListAdapter);
        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startPeripheralConnectionActivity(CustomPeripheralActivity.class, mLeDeviceListAdapter.getDevice(position));
            }
        });
        devicesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startPeripheralConnectionActivity(GeneralPeripheralActivity.class, mLeDeviceListAdapter.getDevice(position));
                return false;
            }
        });
    }

    private void startPeripheralConnectionActivity(Class intentClass, BluetoothDevice device) {
        if (device == null) {
            return;
        }
        Log.d(TAG, "startPeripheralConnectionActivity() using device: " + device.getName() + " : " + device.getAddress());

        final Intent intent = new Intent(MainActivity.this, intentClass);
        intent.putExtra(CustomPeripheralActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(CustomPeripheralActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        mGattScannerWrapper.shutdown();
        mStatusLabel.setText(getString(R.string.status_label_idle));
        startActivity(intent);
    }
}
