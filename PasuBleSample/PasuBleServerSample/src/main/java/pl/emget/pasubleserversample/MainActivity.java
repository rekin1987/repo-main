package pl.emget.pasubleserversample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements MainActivityInterface {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ToggleButton mToggleOn;
    private TextView mTemperatureLabel;
    private SeekBar mTemperatureBar;
    private TextView mVoltageLabel;
    private SeekBar mVoltageBar;
    private EditText mFirmwareEditText;
    private TextView mStatusLabel;
    private LinearLayout mColorLine;

    private Handler mHandler;
    private GattServerWrapper mGattServerWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        setupUI();
        mHandler = new Handler();
        mGattServerWrapper = new GattServerWrapper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGattServerWrapper.shutdown();
    }

    private void setupUI() {
        mToggleOn = (ToggleButton) findViewById(R.id.toggleButtonOnOff);
        mTemperatureLabel = (TextView) findViewById(R.id.temperatureLabel);
        mTemperatureBar = (SeekBar) findViewById(R.id.seekBarTemperature);
        mVoltageLabel = (TextView) findViewById(R.id.voltageLabel);
        mVoltageBar = (SeekBar) findViewById(R.id.seekBarVoltage);
        mFirmwareEditText = (EditText) findViewById(R.id.editTextFirmwareVersion);
        mStatusLabel = (TextView) findViewById(R.id.statusLabel);
        mColorLine = (LinearLayout) findViewById(R.id.colorLine);

        mToggleOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGattServerWrapper.init();
                    mGattServerWrapper.startAdvertising();
                } else {
                    mGattServerWrapper.shutdown();
                    mStatusLabel.setText(getString(R.string.status_idle));
                }
            }
        });

        mTemperatureBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTemperatureLabel.setText(String.format(getString(R.string.temperature_label), progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mVoltageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVoltageLabel.setText(String.format(getString(R.string.voltage_label), progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // set some initial values
        mTemperatureBar.setProgress(20);
        mVoltageBar.setProgress(9);
    }

    public void onNotifyButtonClick(View view) {
        mGattServerWrapper.notifyValuesChanged();
    }

    @Override
    public String getManufacturer() {
        return getString(R.string.manufacturer);
    }

    @Override
    public String getFirmwareVersion() {
        return mFirmwareEditText.getText().toString();
    }

    @Override
    public String getTemperatureValue() {
        return String.valueOf(mTemperatureBar.getProgress());
    }

    @Override
    public String getVoltageValue() {
        return String.valueOf(mVoltageBar.getProgress());
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
    public void setColorValue(final int color) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int colorResId;
                switch (color) {
                    case 0:
                        colorResId = Color.TRANSPARENT;
                        break;
                    case 1:
                        colorResId = getResources().getColor(pl.emget.pasubleserverprofile.R.color.colorRed);
                        break;
                    case 2:
                        colorResId = getResources().getColor(pl.emget.pasubleserverprofile.R.color.colorGreen);
                        break;
                    case 3:
                        colorResId = getResources().getColor(pl.emget.pasubleserverprofile.R.color.colorBlue);
                        break;
                    default:
                        colorResId = Color.TRANSPARENT;
                        break;
                }
                mColorLine.setBackgroundColor(colorResId);
            }
        });
    }

}
