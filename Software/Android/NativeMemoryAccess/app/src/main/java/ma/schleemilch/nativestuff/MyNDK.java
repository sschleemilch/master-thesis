package ma.schleemilch.nativestuff;

/**
 * Created by basti on 22.02.2016.
 */
public class MyNDK {
    static {
        System.loadLibrary("MyLib");
    }
    public native void showProcSpace();
    public native void libExe(String libpath);
    public native void binExe(String binpath);

    public native void callNativeActivity(String actpath);
}
