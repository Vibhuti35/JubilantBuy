package com.example.tonycurrie.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BarcodeActivity extends AppCompatActivity implements ScanResultReceiver {

    private TextView contentTxt,productnameTxt;
    public static String barcode_id;
    private SQLiteOpenHelper openHelper1;
    private SQLiteDatabase database1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        productnameTxt= (TextView)findViewById(R.id.scan_product);

    }
    /*When Scan Now button is pressed new fragment is opened and
     one prerequisite is that user should give permission to access the camera for this app*/

    public void scanNow(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scan_fragment,scanFragment);
        fragmentTransaction.commit();
    }
    //Fetching the content of the barcode and the corresponding productname if exists from database
    @Override
    public void scanResultData(String codeFormat, String codeContent) {

        contentTxt.setText("CONTENT: " + codeContent);
        barcode_id=codeContent;
        try {
            openHelper1 = new DatabaseHelper(this);
            database1 = openHelper1.getWritableDatabase();
            Cursor cursor = database1.rawQuery("SELECT * FROM BARCODE_PRODUCT WHERE BARCODE_NO = '" + codeContent + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String productname=cursor.getString(1);
                productnameTxt.setText("Product Name :" + productname);
                cursor.moveToNext();
            }
            cursor.close();
            this.database1.close();

        }
        catch (Exception e)
        {
            Toast.makeText(this, "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //Result when the barcode is not read or no data fetched
    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();

    }

    //Redirecting to locate activity once the locate button is pressed
    public void locateNow(View view){
        Intent intent = new Intent(BarcodeActivity.this, LocationActivity.class);
        startActivity(intent);

    }
}
