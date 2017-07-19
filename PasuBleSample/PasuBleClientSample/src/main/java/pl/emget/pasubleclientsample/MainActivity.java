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
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private GattScannerWrapper mGattScannerWrapper;
    private TextView mStatusLabel;

    // Required Permissions
    private static final int REQUEST_REQUIRED_PERMISSIONS_CODE = 0x11; // Can only use lower 8 bits for requestCode
    private static String[] PERMISSIONS_FOR_DISCOVERY = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        setupUI();
        mHandler = new Handler();
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
        mHandler.post(new Runnable() {
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
        if(mGattScannerWrapper.isScanning()){
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

    private void scheduleAutoStopScan(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGattScannerWrapper.shutdown();
                        mStatusLabel.setText(getString(R.string.status_label_idle));
                    }
                });
            }
        }, 10000);
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     */
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
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                if (device == null) {
                    return;
                }
                //final Intent intent = new Intent(this, DeviceControlActivity.class);
                //                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                //                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                Log.d(TAG, "Using device: " + device.getName() + " : " + device.getAddress());
                mGattScannerWrapper.shutdown();
                mStatusLabel.setText(getString(R.string.status_label_idle));
                //                startActivity(intent);
            }
        });
    }
}
