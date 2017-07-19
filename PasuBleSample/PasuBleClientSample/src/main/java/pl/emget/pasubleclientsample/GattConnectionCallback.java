package pl.emget.pasubleclientsample;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.List;

public interface GattConnectionCallback {

    void postStatusUpdate(String status);

    void onServicesDiscovered(List<BluetoothGattService> services);

    void onCharacteristicRead(BluetoothGattCharacteristic characteristic);

    void onCharacteristicNotification(BluetoothGattCharacteristic characteristic);
}
