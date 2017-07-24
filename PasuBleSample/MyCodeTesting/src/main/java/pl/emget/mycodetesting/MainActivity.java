package pl.emget.mycodetesting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s1 = "Code - String %1$s is number %2$d ?";
        String s2 = "Code - String %1s is number %2d ?";
        String s3 = "Code - String %s is number %d ?";

        TextView t1 = (TextView) findViewById(R.id.text1);
        TextView t2 = (TextView) findViewById(R.id.text2);
        TextView t3 = (TextView) findViewById(R.id.text3);
        TextView t4 = (TextView) findViewById(R.id.text4);
        TextView t5 = (TextView) findViewById(R.id.text5);
        TextView t6 = (TextView) findViewById(R.id.text6);

        s1 = getString(R.string.formatted_str_1);
        s2 = getString(R.string.formatted_str_2);
        s3 = getString(R.string.formatted_str_3);

//        t1.setText(String.format(s1, "aaa", 1));
//        t2.setText(String.format(s2, "aaa", 2));
//        t3.setText(String.format(s3, "aaa", 3));

//        t1.setText(String.format(getString(R.string.formatted_str_1), "aaa", 1));
//        t2.setText(String.format(getString(R.string.formatted_str_2), "aaa", 2));
//        t3.setText(String.format(getString(R.string.formatted_str_3), "aaa", 3));
//
//        t4.setText(getString(R.string.formatted_str_1, "aaa", 1));
//        t5.setText(getString(R.string.formatted_str_2, "aaa", 2));
//        t6.setText(getString(R.string.formatted_str_3, "aaa", 3));

        t1.setText(String.format(s1, "bbb", 1));
        t2.setText(String.format(s2, "bbb", 2));
        t3.setText(String.format(s3, "bbb", 3));

        t4.setText(String.format(Locale.getDefault(), s1, "bbb", 1));
        t5.setText(String.format(Locale.getDefault(), s2, "bbb", 2));
        t6.setText(String.format(Locale.getDefault(), s3, "bbb", 3));
    }
}
