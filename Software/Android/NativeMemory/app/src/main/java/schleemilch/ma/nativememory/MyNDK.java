package schleemilch.ma.nativememory;

public class MyNDK {
    static {
        System.loadLibrary("Memory");
    }
    native public void showSelfProc();
    native public void mallocFile(String path);
    native public void mmapFile(String path);
    native public void executeSomething();
    native public void memoryAccess();
    native public void mmapBinExec(String path);
    native public void writingOwnOAT();
}
