package schleemilch.ma.nativememory;

public class MyNDK {
    static {
        System.loadLibrary("Memory");
    }
    native public void showSelfProc();
    native public void mallocFile(String path);
    native public void mmapFile(String path);
    native public void executeSomething();
}
