package com.example.tonycurrie.myapplication;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Tab2Buy extends Fragment{

    private RadioGroup radioGroup;
    EditText cardno,cvv;
    String method;
    String pos,msg,exp,month,year;
    SharedPreferences position;
    Button pay;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    ExpandableListView ev;
    RadioButton card;
    RadioButton cod;
    //Intent intent ;
    SQLiteDatabase db;
    Cursor cursor;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase database;
    String Message;
    RowItem rlist;
    String productName,PhoneNumber;
    public static String price;
    int done=0;
    int positions;
    Spinner spinner1,spinner2;
    String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2buy, container, false);
        //initialising default value
        exp="1/2017";
        //Retriving saved Product name
        position=this.getActivity().getSharedPreferences("Position",0);
        String proname=position.getString("Listpos",null);
        //Retriving position in arratlist where the product is stored
        for(int i=0;i<HomeFragment.listitems1.size();i++)
        {
            RowItem r= HomeFragment.listitems1.get(i);
            if(r.getproduct().equals(proname))
            {
                positions=i;
            }
        }

        //Initializing objects
        rlist=new RowItem();
        rlist=HomeFragment.listitems1.get(positions);
        productName=rlist.getproduct();
        price=rlist.getprice();
        cardno= rootView.findViewById(R.id.numb);
        cvv= rootView.findViewById(R.id.cvv);
        radioGroup = rootView.findViewById(R.id.radioGroup);
        pay= rootView.findViewById(R.id.pay);
        card= rootView.findViewById(R.id.card) ;
        cod= rootView.findViewById(R.id.cod) ;
        cardno.setEnabled(false);
        cvv.setEnabled(false);
        username = ConnectFragment.firstname;
        SharedPreferences login1 = this.getActivity().getSharedPreferences("login", 0);
        String name = login1.getString("displayname", null);
        openHelper=new DatabaseHelperLogin (this.getActivity());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHelperLogin.TABLE_NAME + " WHERE " + DatabaseHelperLogin.COL_2 + " =? ", new String[]{name});
        cursor.moveToFirst();
        cursor.moveToFirst();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                PhoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelperLogin.COL_10));
                //Toast.makeText(getActivity(), "Phonenmber:" + PhoneNumber, Toast.LENGTH_SHORT).show();
            }

        }
        spinner1 = rootView.findViewById(R.id.month);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month=String.valueOf(spinner1.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Selection of year for expiry detail
        spinner2= (Spinner) rootView.findViewById(R.id.year);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year=String.valueOf(spinner2.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Storing the payment method selected
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardno.setEnabled(true);
                cvv.setEnabled(true);
          //      exp.setEnabled(true);
                method="card";
            }
        });

        //Storing the payment method selected
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardno.setEnabled(false);
                cvv.setEnabled(false);
                method="cash";
            }
        });

        //Validation for length of card number
        cardno.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if( cardno.length() != 16 ) {
                    cardno.requestFocus();
                    cardno.setError("Please enter valid 16 digit card number.");
                }
                else {
                    cardno.setError(null);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        //Validating length of CVV number which should be 3 digit
        cvv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if( cvv.length() != 3 ) {
                    cvv.requestFocus();
                    cvv.setError("Please enter valid 3 digit CVV number.");
                }
                else {
                    cvv.setError(null);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        //Payment
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if any payment method is selected or not and if not forcing user to select
                if(radioGroup.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getActivity(), "Please select payment method!", Toast.LENGTH_SHORT).show();
                }

                else {
                    // Verifying if user has enters valid credit card details or not
                    exp = "" + month + "/" + year + "";
                    if (method.equals("card")) {
                        if (cardno.getText().toString().equals("")) {
                            if (exp.equals("1/2017")) {
                                if (cvv.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Please enter Valid credit card details before proceeding further.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }

                    //Prompting user before proceeding the payment
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirm your in-app purchase. \nDo you want to buy? \nProduct: " + productName + "\nPrice:" + price);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                //Verifying card details with already store card details n database
                                openHelper = new DatabaseHelper(getActivity());
                                database = openHelper.getWritableDatabase();
                                Cursor cursor = database.rawQuery("SELECT * FROM CARDINFO", null);
                                cursor.moveToFirst();
                                while (!cursor.isAfterLast()) {

                                    String card = cursor.getString(0);
                                    String expi = cursor.getString(1);
                                    String cvvv = cursor.getString(2);
                                    //Finalising the order and informing user about the details if cash on delivery option is selected
                                    if (method.equals("cash")) {
                                        done = 1;
                                        //Toast.makeText(getActivity(), "Your order has been placed.", Toast.LENGTH_SHORT).show();
                                        msg = "Your order for " + productName + " has been placed. Amount Payable:" + price + ". It will be delivered within 7 working days. Thank you for shopping Jubilant Buy.";
                                    }
                                    //Validating the card and finalising the order if corrct
                                    if (method.equals("card")) {
                                        if (cardno.getText().toString().equals(card)) {
                                            if (exp.equals(expi)) {
                                                if (cvv.getText().toString().equals(cvvv)) {
                                                    done = 1;
                                                    Toast.makeText(getActivity(), "Your order has been placed.", Toast.LENGTH_SHORT).show();
                                                    msg = "Your order for " + productName + "has been placed. Amount paid:" + price + ". It will be delivered within 7 working days. Thank you for shopping with us.";
                                                }
                                            }
                                        }

                                    }

                                    cursor.moveToNext();
                                }
                                cursor.close();

                                //  try {
                                //Sending user a confirmation message with details if payment is successful
                                if (done == 1) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(PhoneNumber, null, msg, null, null);
                                    done = 0;
                                    Fragment fragment = new OrderConfirmation();
                                    if (fragment != null) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                                    }
                                    else {
                                        Log.e("MainActivity", "Error in creating fragment");
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please enter Valid credit card details before proceeding further.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(getActivity(), "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            database.close();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }

            }
        });


        return rootView;
    }
}