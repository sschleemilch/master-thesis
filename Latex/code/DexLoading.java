package ma.schleemilch.dexclassload;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File dexInternalStoragePath = new File(getDir("dex", Context.MODE_PRIVATE),
                "toload.dex");
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;
        final int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(getAssets().open("toload.dex"));
            dexWriter = new BufferedOutputStream(new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0){
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        final File optimizedDexOutputPath = getDir("outdex", Context.MODE_PRIVATE);
        DexClassLoader dcl = new DexClassLoader(dexInternalStoragePath.getAbsolutePath(),
                optimizedDexOutputPath.getAbsolutePath(), null, getClassLoader());
        Class classToLoad = null;
        Method m;
        try {
            classToLoad = dcl.loadClass("ToLoad");
            m = classToLoad.getDeclaredMethod("exampleMulMethod", int.class, int.class);
            TextView textView = (TextView) findViewById(R.id.invokeResult);
            textView.setText("8*9=" + m.invoke(classToLoad.newInstance(), 8, 9));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        } catch (InvocationTargetException e){
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
