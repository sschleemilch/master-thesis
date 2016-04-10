package ma.schleemilch.nativestuff;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "NDK-logging";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, System.getProperty("os.arch"));

        MyNDK ndk = new MyNDK();
        //ndk.showProcSpace();


        File internalStoragePath = new File(getDir("dyn", Context.MODE_PRIVATE), "mul.so");

        Log.d(TAG, internalStoragePath.getAbsolutePath());

        BufferedInputStream bis = null;
        OutputStream soWriter = null;
        final int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(getAssets().open("mul32.so"));
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
        //System.load(internalStoragePath.getAbsolutePath());

        long startTime = System.currentTimeMillis();
        //ndk.libExe(internalStoragePath.getAbsolutePath());
        long difference = System.currentTimeMillis() - startTime;
        //Log.d("TAG", "Took: " + difference + "ms");


        internalStoragePath = new File(getDir("dyn", Context.MODE_PRIVATE), "toExec");


        Log.d(TAG, internalStoragePath.getAbsolutePath());

        bis = null;
        soWriter = null;
        try {
            bis = new BufferedInputStream(getAssets().open("toExec32"));
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
        internalStoragePath.setExecutable(true);

        /*
        try {
            startTime = System.currentTimeMillis();
            Process nativeExe = Runtime.getRuntime().exec(internalStoragePath.getAbsolutePath());

            BufferedReader reader = new BufferedReader(new InputStreamReader(nativeExe.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            // Waits for the command to finish.
            nativeExe.waitFor();
            String nativeOutput =  output.toString();

            Log.d(TAG, "nativeOut: " + nativeOutput);
            difference = System.currentTimeMillis() - startTime;
            Log.d("TAG", "Took: " + difference + "ms");

        } catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        */
        startTime = System.currentTimeMillis();
        ndk.binExe(internalStoragePath.getAbsolutePath());
        difference = System.currentTimeMillis() - startTime;
        Log.d(TAG, "Took: " + difference + "ms");

        //ndk.callNativeActivity(internalStoragePath.getAbsolutePath());
    }
}
