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
    static byte[] eggBytes = {0x11, 0x22, 0x33, 0x44};
    byte[] cbytes = ndk.encrypt("toBeEncrypted","secretkeyxxxxxxx");
    //static byte[] encString = new byte[eggBytes.length + cbytes.length];

    private static byte[] encrypt(String toEncrypt, String seckey, String iv){
        byte[] strBytes = toEncrypt.getBytes();
        byte[] keyBytes = seckey.getBytes(); // 16 Bytes
        byte[] ivBytes = iv.getBytes(); // 16 Bytes
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte [] encrypted = new byte[cipher.getOutputSize(strBytes.length)];
            int enc_len = cipher.update(strBytes, 0, strBytes.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);
            byte[] eggEnc = new byte[encrypted.length + eggBytes.length + 1];
            int i;
            for (i = 0; i < eggBytes.length; i++){
                eggEnc[i] = eggBytes[i];
            }
            eggEnc[i++] = (byte) strBytes.length;
            for (int n = 0; n < encrypted.length; n++){
                eggEnc[i+n] = encrypted[n];
            }
            return eggEnc;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | ShortBufferException | IllegalBlockSizeException
                | BadPaddingException e){
            e.getMessage();
        }
        return null;
    }
    //static byte[] encrypted = encrypt("toBeEncrypted","secretkeyxxxxxxx","abcdefghijklmnop");
    //static byte[] getEncrypted = enc("toBeEncrypted","secretkeyxxxxxxx");


    private static byte[] enc(String toEnc, String sKey){
        SecretKeySpec secretKeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encrypted = cipher.doFinal(toEnc.getBytes());
            byte[] res = new byte[eggBytes.length + 1 + encrypted.length];
            int i;
            for (i = 0; i < eggBytes.length; i++){
                res[i] = eggBytes[i];
            }
            res[i++] = (byte)encrypted.length;
            for (int n = 0; n < encrypted.length; n++){
                res[i+n] = encrypted[n];
            }
            return encrypted;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e){
            e.getMessage();
        }
        return null;
    }
    private static String dec(byte[] encrypted, String sKey){
        String res = "";
        SecretKeySpec secretKeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            res = new String(decrypted,"UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | IllegalBlockSizeException |
                BadPaddingException | UnsupportedEncodingException e){
            e.getMessage();
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Log.d(TAG,Arrays.toString(anyString.getBytes()));
        //File internalStoragePath = new File(getDir("dyn", Context.MODE_PRIVATE), "sl");
        //Log.d(TAG, internalStoragePath.getAbsolutePath());


        //Log.d(TAG, "Clear: " + Arrays.toString("toBeEncrypted".getBytes()));
        //Log.d(TAG, "Enc: " + Arrays.toString(encrypted));
        final TextView eggText = (TextView)findViewById(R.id.textView);
        try{

            eggText.setText(new String(cbytes,"UTF-8"));
        } catch (UnsupportedEncodingException e){
            e.getMessage();
        }

        //byte[] strToPrint = new byte[encrypted[4]];
        //System.arraycopy(encrypted,6,strToPrint,0,strToPrint.length);
        //eggText.setText(new String(strToPrint, StandardCharsets.UTF_8));

        Button eggButton = (Button) findViewById(R.id.btn_change_egg);
        eggButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               eggText.setText(new String(ndk.decrypt(cbytes,"secretkeyxxxxxxx")));
                ndk.eggHunting();
                //ndk.eggDecryption();
                //finish();
                //startActivity(getIntent());
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
