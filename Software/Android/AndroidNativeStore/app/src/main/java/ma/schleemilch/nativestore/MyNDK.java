package ma.schleemilch.nativestore;

/**
 * Created by Basti on 25.03.2016.
 */
public class MyNDK {
    static {
        System.loadLibrary("Memory");
    }
    native void showProcSpace();
}
