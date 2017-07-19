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

import java.util.PriorityQueue;
import java.util.Queue;

import static android.content.Context.BLUETOOTH_SERVICE;

public class GattConnectionWrapper {

    private static final String TAG = GattConnectionWrapper.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    private GattConnectionCallback mGattConnectionCallback;
    private Context mContext;
    private String mDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    private boolean mIsConnected;
    private boolean mReadInProgress;

    // queue makes sure the characteristic read events are processed one after another
    private Queue<BluetoothGattCharacteristic> mCharacteristicsQueue;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Connected to GATT server.");
                mIsConnected = true;
                mGattConnectionCallback.postStatusUpdate("Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.d(TAG, "Attempting to start service discovery");
                mBluetoothGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Disconnected from GATT server.");
                mIsConnected = false;
                mGattConnectionCallback.postStatusUpdate("Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered()");
                mGattConnectionCallback.onServicesDiscovered(gatt.getServices());
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
                mGattConnectionCallback.postStatusUpdate("onServicesDiscovered() status: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicRead()");
                mGattConnectionCallback.onCharacteristicRead(characteristic);
            } else {
                Log.w(TAG, "onCharacteristicRead() status: " + status);
                mGattConnectionCallback.postStatusUpdate("onCharacteristicRead() status: " + status);
            }
            synchronized (this) {
                mReadInProgress = false;
            }
            // process the remaining queue items if any
            processCharacteristicsQueue();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged()");
            mGattConnectionCallback.onCharacteristicNotification(characteristic);
        }
    };

    public GattConnectionWrapper(GattConnectionCallback callback, Context context, String deviceAddress) {
        mGattConnectionCallback = callback;
        mContext = context;
        mDeviceAddress = deviceAddress;
        mCharacteristicsQueue = new PriorityQueue<>();
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
            mGattConnectionCallback.postStatusUpdate("BluetoothAdapter not initialized");
            return;
        }
        if(!mIsConnected){
            Log.w(TAG, "Device is disconnected!");
            mGattConnectionCallback.postStatusUpdate("Device is disconnected!");
        }
        // add the read request to the queue and start processing
        mCharacteristicsQueue.add(characteristic);
        processCharacteristicsQueue();
    }

    /**
     * Enables or disables notification on a given characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        Log.d(TAG, "setCharacteristicNotification()");
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            mGattConnectionCallback.postStatusUpdate("BluetoothAdapter not initialized");
            return;
        }
        if(!mIsConnected){
            Log.w(TAG, "Device is disconnected!");
            mGattConnectionCallback.postStatusUpdate("Device is disconnected!");
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, String value){
        Log.d(TAG, "writeCharacteristic()");
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            mGattConnectionCallback.postStatusUpdate("BluetoothAdapter not initialized");
            return;
        }
        if(!mIsConnected){
            Log.w(TAG, "Device is disconnected!");
            mGattConnectionCallback.postStatusUpdate("Device is disconnected!");
        }
        characteristic.setValue(value);
        mBluetoothGatt.writeCharacteristic(characteristic);
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
        if (mBluetoothGatt != null) {
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

    /**
     * Processes characteristics read requests.
     * Synchronized method.
     */
    private synchronized void processCharacteristicsQueue() {
        if (mCharacteristicsQueue.isEmpty()) {
            return;
        }
        if (mReadInProgress) {
            return;
        }
        mReadInProgress = true;
        BluetoothGattCharacteristic charac = mCharacteristicsQueue.poll();
        mBluetoothGatt.readCharacteristic(charac);
    }

}
