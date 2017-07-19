package pl.emget.pasubleclientsample;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

interface MainActivityInterface {

    Context getContext();
    void postStatusUpdate(String status);
    void onDeviceFound(BluetoothDevice device);
}
