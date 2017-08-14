package pl.emget.pasubleserversample;

import android.content.Context;

public interface MainActivityInterface {

    String getManufacturer();
    String getFirmwareVersion();
    String getTemperatureValue();
    String getVoltageValue();
    void setColorValue(int color);
    Context getContext();
    void postStatusUpdate(String status);
    String getMeterlinkData();
}
