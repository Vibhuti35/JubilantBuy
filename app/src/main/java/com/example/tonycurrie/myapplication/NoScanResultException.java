package com.example.tonycurrie.myapplication;

//Handling the exception in case barcode scan fails
public class NoScanResultException extends Exception {
    public NoScanResultException() {}
    public NoScanResultException(String msg) { super(msg); }
    public NoScanResultException(Throwable cause) { super(cause); }
    public NoScanResultException(String msg, Throwable cause) { super(msg, cause); }
}

/*Reference: http://blog.whomeninja.in/android-barcode-scanner/. */