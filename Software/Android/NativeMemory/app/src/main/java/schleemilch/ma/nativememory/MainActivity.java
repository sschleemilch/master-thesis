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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "NDK-Logging";
    static final MyNDK ndk = new MyNDK();
    //byte[] cbytes = ndk.encrypt("toBeEncrypted","secretkeyxxxxxxx");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d(TAG,Arrays.toString(anyString.getBytes()));
        //File internalStoragePath = new File(getDir("dyn", Context.MODE_PRIVATE), "sl");
        //Log.d(TAG, internalStoragePath.getAbsolutePath());
        //Log.d(TAG, "Clear: " + Arrays.toString("toBeEncrypted".getBytes()));
        //Log.d(TAG, "Enc: " + Arrays.toString(encrypted));
        /*
        final TextView eggText = (TextView)findViewById(R.id.textView);
        try{

            eggText.setText(new String(cbytes,"UTF-8"));
        } catch (UnsupportedEncodingException e){
            e.getMessage();
        }
        */
        //byte[] strToPrint = new byte[encrypted[4]];
        //System.arraycopy(encrypted,6,strToPrint,0,strToPrint.length);
        //eggText.setText(new String(strToPrint, StandardCharsets.UTF_8));
        /*
        Button eggButton = (Button) findViewById(R.id.btn_change_egg);
        eggButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               eggText.setText(new String(ndk.decrypt(cbytes,"secretkeyxxxxxxx")));
                //ndk.eggHunting();
                //ndk.eggDecryption();
                //finish();
                //startActivity(getIntent());
            }
        });
        */
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
        ndk.memoryAccess();
        //ndk.mmapBinExec(internalStoragePath.getAbsolutePath());
        //ndk.writingOwnOAT();
        //ndk.crashApp();

    }
}
