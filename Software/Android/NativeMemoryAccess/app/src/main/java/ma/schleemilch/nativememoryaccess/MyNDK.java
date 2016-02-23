package ma.schleemilch.nativememoryaccess;

/**
 * Created by basti on 22.02.2016.
 */
public class MyNDK {
    static {
        System.loadLibrary("MyLib");
    }
    public native void showProcSpace();
    public native void libExe(String libpath);
}
