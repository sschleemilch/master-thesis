package schleemilch.ma.nativememory;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "NDK-Logging";

    int egg = 0x11223344;
    static byte[] eggBytes = {0x11, 0x22, 0x33, 0x44};
    static byte[] cbytes = "toBeEncrypted".getBytes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyNDK ndk = new MyNDK();

        //Log.d(TAG,Arrays.toString(anyString.getBytes()));

        //File internalStoragePath = new File(getDir("dyn", Context.MODE_PRIVATE), "sl");
        //Log.d(TAG, internalStoragePath.getAbsolutePath());


        final TextView eggText = (TextView)findViewById(R.id.textView);
        eggText.setText(new String(cbytes, StandardCharsets.UTF_8));

        Button eggButton = (Button) findViewById(R.id.btn_change_egg);
        eggButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ndk.eggHunting();
                //eggText.setText(new String(cbytes, StandardCharsets.UTF_8));
                finish();
                startActivity(getIntent());
            }
        });



        /*
        BufferedInputStream bis = null;
        OutputStream soWriter = null;
        final int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(getAssets().open("sl"));
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
        */
        //ndk.mallocFile(internalStoragePath.getAbsolutePath());
        //ndk.mmapFile(internalStoragePath.getAbsolutePath());
        //ndk.showSelfProc();
        //ndk.executeSomething();
        //ndk.memoryAccess();
        //ndk.mmapBinExec(internalStoragePath.getAbsolutePath());
        //ndk.writingOwnOAT();
        //ndk.crashApp();

    }
}
