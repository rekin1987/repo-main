package pl.emget.pasubleclientsample;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

/**
 * Created by psuszek on 2017-07-18.
 */

public class GattConnectionWrapper {

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
//    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            String intentAction;
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                Log.i(TAG, "Connected to GATT server.");
//                broadcastUpdate(intentAction);
//                // Attempts to discover services after successful connection.
//                Log.i(TAG, "Attempting to start service discovery");
//                mBluetoothGatt.discoverServices();
//
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                Log.i(TAG, "Disconnected from GATT server.");
//                broadcastUpdate(intentAction);
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.i(TAG, "onServicesDiscovered()");
//                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
//            } else {
//                Log.w(TAG, "onServicesDiscovered received: " + status);
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt,
//                                         BluetoothGattCharacteristic characteristic,
//                                         int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.i(TAG, "onCharacteristicRead()");
//                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//            } else {
//                Log.i(TAG, "onCharacteristicRead() status: " + status);
//                broadcastUpdate(ACTION_GATT_DISCONNECTED);
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt,
//                                            BluetoothGattCharacteristic characteristic) {
//            Log.i(TAG, "onCharacteristicChanged()");
//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//        }
//    };
}
