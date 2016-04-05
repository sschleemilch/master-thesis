package schleemilch.ma.nativememory;

public class MyNDK {
    static {
        System.loadLibrary("Memory");
        System.loadLibrary("crypto");
    }
    native public void showSelfProc();
    native public void mallocFile(String path);
    native public void mmapFile(String path);
    native public void executeSomething();
    native public void memoryAccess();
    native public void mmapBinExec(String path);
    native public void writingOwnOAT();
    native public void crashApp();
    native public void eggHunting();
    native public void eggDecryption();
    native public byte[] encrypt(String toEncrypt, String key);
    native public byte[] decrypt(byte[] encrypted, String key);
}
