package com.example.tonycurrie.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanFragment extends Fragment {
    private String codeFormat,codeContent;
    private final String noResultErrorMsg = "No scan data received!";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);
        integrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();
        if (scanningResult != null) {
            codeContent = scanningResult.getContents();
            // send received data
            parentActivity.scanResultData(codeFormat,codeContent);

        }else{
            // send exception
            parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }
}
/*Reference: http://blog.whomeninja.in/android-barcode-scanner/. */