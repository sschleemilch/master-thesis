package ma.schleemilch.dynlibjava;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "NDK-Stuff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File internalStoragePath = new File(getDir("dyn", Context.MODE_PRIVATE), "myso.so");
        Log.d(TAG, internalStoragePath.getAbsolutePath());

        BufferedInputStream bis = null;
        OutputStream soWriter = null;
        final int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(getAssets().open("myso.so"));
            soWriter = new BufferedOutputStream(new FileOutputStream(internalStoragePath));
            byte [] buf = new byte[BUF_SIZE];

            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0){
                soWriter.write(buf, 0, len);
            }
            soWriter.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        MyNdk ndk = new MyNdk();
        System.load(internalStoragePath.getAbsolutePath());
        ndk.printSuccess();
        long difference = System.currentTimeMillis() - startTime;
        Log.d(TAG, "Took: " + difference + "ms");
    }
}
