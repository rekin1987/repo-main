package pl.emget.pasubleserverprofile;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;

import java.util.HashMap;
import java.util.UUID;

public class ServerProfile {

    private static HashMap<UUID, String> mProfileAttributes = new HashMap<>(7);
    // SERVICE
    public static UUID DEVICE_INFO_SERVICE_UUID = UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB");
    // read
    public static UUID DEVICE_INFO_CHARACTERISTIC_MANUFACTURER_NAME_UUID = UUID.fromString("00002A29-0000-1000-8000-00805F9B34FB");
    // read
    public static UUID DEVICE_INFO_CHARACTERISTIC_FIRMWARE_VERSION_UUID = UUID.fromString("00002A26-0000-1000-8000-00805F9B34FB");
    // SERVICE
    public static UUID CUSTOM_METRICS_SERVICE_UUID = UUID.fromString("1706BBC0-88AB-4B8D-877E-2237916EE929");
    // read, notify
    public static UUID CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID = UUID.fromString("275348FB-C14D-4FD5-B434-7C3F351DEA5F");
    // read, notify
    public static UUID CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID = UUID.fromString("BD28E457-4026-4270-A99F-F9BC20182E15");
    // write
    public static UUID CUSTOM_METRICS_CHARACTERISTIC_COLOR_UUID = UUID.fromString("AD28E558-1055-4270-BB77F-F9BC20182E16");

    // Meterlink SERVICE
    public static UUID  FLIR_METERLINK_SERVICE_UUID =  UUID.fromString("D813BF66-5E61-188C-3D47-2487320A8B6E");
    // read, notify
    public static UUID  FLIR_METERLINK_CHARACTERISTIC_UUID = UUID.fromString("E9A8B8C1-B91E-10A1-5241-C4D951378343");

    static {
        mProfileAttributes.put(DEVICE_INFO_SERVICE_UUID, "Device Information Service");
        mProfileAttributes.put(DEVICE_INFO_CHARACTERISTIC_MANUFACTURER_NAME_UUID, "Manufacturer Name String");
        mProfileAttributes.put(DEVICE_INFO_CHARACTERISTIC_FIRMWARE_VERSION_UUID, "Firmware Revision String");
        mProfileAttributes.put(CUSTOM_METRICS_SERVICE_UUID, "Custom Metrics Service");
        mProfileAttributes.put(CUSTOM_METRICS_CHARACTERISTIC_TEMPERATURE_UUID, "Temperature");
        mProfileAttributes.put(CUSTOM_METRICS_CHARACTERISTIC_VOLTAGE_UUID, "Voltage");
        mProfileAttributes.put(CUSTOM_METRICS_CHARACTERISTIC_COLOR_UUID, "Display color");
        mProfileAttributes.put(FLIR_METERLINK_SERVICE_UUID, "Meterlink Service");
        mProfileAttributes.put(FLIR_METERLINK_CHARACTERISTIC_UUID, "Meterlink Data");
    }

    public static String getStateDescription(int state) {
        switch (state) {
            case BluetoothProfile.STATE_CONNECTED:
                return "Connected";
            case BluetoothProfile.STATE_CONNECTING:
                return "Connecting";
            case BluetoothProfile.STATE_DISCONNECTED:
                return "Disconnected";
            case BluetoothProfile.STATE_DISCONNECTING:
                return "Disconnecting";
            default:
                return "Unknown State " + state;
        }
    }

    public static String getStatusDescription(int status) {
        switch (status) {
            case BluetoothGatt.GATT_SUCCESS:
                return "SUCCESS";
            default:
                return "Unknown Status " + status;
        }
    }

    public static String getAttributeNameFromUUID(UUID inputUUID) {
        return mProfileAttributes.get(inputUUID);
    }

    public static String lookup(UUID uuid, String defaultName) {
        String name = mProfileAttributes.get(uuid);
        return name == null ? defaultName : name;
    }

}
