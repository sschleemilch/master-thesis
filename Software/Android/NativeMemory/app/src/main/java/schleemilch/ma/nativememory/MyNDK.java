package schleemilch.ma.nativememory;

public class MyNDK {
    static {
        System.loadLibrary("Memory");
    }
    native public void showSelfProc();

}
