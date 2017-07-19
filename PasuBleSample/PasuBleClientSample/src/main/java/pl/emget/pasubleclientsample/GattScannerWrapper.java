package pl.emget.pasubleclientsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BLUETOOTH_SERVICE;

class GattScannerWrapper {

    private static final String TAG = GattScannerWrapper.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mIsScanning;

    private MainActivityInterface mParentInterface;

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, "onScanResult");
            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAG, "onBatchScanResults: " + results.size() + " results");
            for (ScanResult result : results) {
                processResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.w(TAG, "LE Scan Failed: " + errorCode);
            mParentInterface.postStatusUpdate("LE Scan Failed: " + errorCode);
        }

        private void processResult(ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.i(TAG, "New LE Device: " + device.getName() + " @ " + result.getRssi());
            mParentInterface.onDeviceFound(device);
        }
    };

    public GattScannerWrapper(MainActivityInterface activityInterface) {
        mParentInterface = activityInterface;
    }

    public void init() {
        setupBluetooth();
    }

    public void startScan() {
        if (mBluetoothAdapter != null) {
            ScanFilter scanFilter = new ScanFilter.Builder().build();
            ArrayList<ScanFilter> filters = new ArrayList<>();
            filters.add(scanFilter);
            ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
            mIsScanning = true;
            mBluetoothAdapter.getBluetoothLeScanner().startScan(filters, settings, mScanCallback);
        }
    }

    public void shutdown() {
        stopScan();
        mBluetoothAdapter = null;
    }

    public boolean isScanning() {
        return mIsScanning;
    }

    private void setupBluetooth() {
        BluetoothManager aBluetoothManager = (BluetoothManager) mParentInterface.getContext().getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = aBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            mParentInterface.postStatusUpdate("Bluetooth disabled. Turn it on and restart app.");
        }

        if (!mParentInterface.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mParentInterface.postStatusUpdate("No LE Support. App will not work.");
        }
    }

    private void stopScan() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
        }
        mIsScanning = false;
    }
}
