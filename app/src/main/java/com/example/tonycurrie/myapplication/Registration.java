package com.example.tonycurrie.myapplication;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends Fragment{

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _rRegister;
    EditText _FName,_LName,_UName,_Password,_AL1,_AL2,_City,_PC,_PH;
    Fragment fragment = null;

    public Registration() {
    }

    //Fetching the data user has entered and storing it to database after applying required validations

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_registration, container, false);
        openHelper = new DatabaseHelperLogin(this.getActivity());
        _FName = rootView.findViewById(R.id.EnteredFName);
        _LName = rootView.findViewById(R.id.EnteredLName);
        _UName = rootView.findViewById(R.id.EnteredUsername);
        _Password = rootView.findViewById(R.id.EnteredPassword);
        _AL1 = rootView.findViewById(R.id.EnteredAL1);
        _AL2 = rootView.findViewById(R.id.EnteredAL2);
        _City = rootView.findViewById(R.id.EnteredCity);
        _PC = rootView.findViewById(R.id.EnteredPC);
        _PH=rootView.findViewById(R.id.EnteredPH);
        _rRegister = rootView.findViewById(R.id.button_register);
        _rRegister.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              db = openHelper.getWritableDatabase();
                                              String F_Name = _FName.getText().toString();
                                              String L_Name = _LName.getText().toString();
                                              String U_name = _UName.getText().toString();
                                              String Pass = _Password.getText().toString();
                                              String AL_1 = _AL1.getText().toString();
                                              String AL_2 = _AL2.getText().toString();
                                              String City = _City.getText().toString();
                                              String P_Code = _PC.getText().toString();
                                              String PH_No = _PH.getText().toString();

                                              //Validations
                                              if (F_Name.length() == 0) {
                                                  _FName.requestFocus();
                                                  _FName.setError("Field cannot be empty");
                                              }
                                              else if (!F_Name.matches("[a-zA-Z]+")) {
                                                  _FName.requestFocus();
                                                  _FName.setError("Invalid characters(only Alphanumeric characters allowed");
                                              }
                                              else if (L_Name.length() == 0) {
                                                  _LName.requestFocus();
                                                  _LName.setError("Field cannot be empty");
                                              }
                                              else if (!L_Name.matches("[a-zA-Z]+")) {
                                                  _LName.requestFocus();
                                                  _LName.setError("Invalid characters(only Alphanumeric characters allowed");
                                              }
                                              else if (U_name.length() == 0) {
                                                  _UName.requestFocus();
                                                  _UName.setError("Field Cannot Be Empty");
                                              }
                                              else if (Pass.length() == 0) {
                                                  _Password.requestFocus();
                                                  _Password.setError("Password cannot be left blank");
                                              }
                                              else if (AL_1.length() == 0) {
                                                  _AL1.requestFocus();
                                                  _AL1.setError("Address cannot be left blank");
                                              }

                                              else if (City.length() == 0) {
                                                  _City.requestFocus();
                                                  _City.setError("City cannot be left blank");
                                              }
                                              else if (P_Code.length() == 0) {
                                                  _PC.requestFocus();
                                                  _PC.setError("Postalcode cannot be left blank");
                                              }
                                              else if (PH_No.length() != 10) {
                                                  _PH.requestFocus();
                                                  _PH.setError("Enter valid 10 digit phone number ");
                                              }

                                              else{
                                              insertdata(F_Name, L_Name, U_name, Pass, AL_1, AL_2, City, P_Code,PH_No);
                                              Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                                              fragment = new ConnectFragment();
                                              if (fragment != null) {
                                                  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                  fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                                              }
                                          }

                                          }
                                      }
        );

        return rootView;
    }
    //Inserting data to database
    public void insertdata(String F_Name, String L_Name, String U_Name, String Pass, String AL_1,String AL_2,String City, String P_Code, String PH_No)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperLogin.COL_2, F_Name);
        contentValues.put(DatabaseHelperLogin.COL_3, L_Name);
        contentValues.put(DatabaseHelperLogin.COL_4, U_Name);
        contentValues.put(DatabaseHelperLogin.COL_5, Pass);
        contentValues.put(DatabaseHelperLogin.COL_6, AL_1);
        contentValues.put(DatabaseHelperLogin.COL_7, AL_2);
        contentValues.put(DatabaseHelperLogin.COL_8, City);
        contentValues.put(DatabaseHelperLogin.COL_9, P_Code);
        contentValues.put(DatabaseHelperLogin.COL_10, PH_No );
        long id = db.insert(DatabaseHelperLogin.TABLE_NAME, null, contentValues);
    }
}