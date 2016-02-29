package ma.schleemilch.dynlibjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.load("myso");
        MyNdk ndk = new MyNdk();
        ndk.printSuccess();
    }
}
