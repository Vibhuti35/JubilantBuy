package com.example.tonycurrie.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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

//This is login fragment

public class ConnectFragment extends Fragment{
SQLiteDatabase db;
SQLiteOpenHelper openHelper;
Cursor cursor,cursor1;
Button _bRegister,_bLogin;
EditText LUname, LPass;
public static String firstname =null;

public ConnectFragment() {
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
    LUname=rootView.findViewById(R.id.EnteredLUsername);
    LPass=rootView.findViewById(R.id.EnteredLPassword);
    _bLogin=rootView.findViewById(R.id._bLogin);
    _bRegister=rootView.findViewById(R.id._bRegister);
    openHelper=new DatabaseHelperLogin (this.getActivity());
    db = openHelper.getReadableDatabase();
    //Validating the user and extracting the firstname to display
    _bLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String LUsername = LUname.getText().toString();
            String LPassword = LPass.getText().toString();
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelperLogin.TABLE_NAME + " WHERE " + DatabaseHelperLogin.COL_4 + " =? AND " + DatabaseHelperLogin.COL_5 + "= ?", new String[]{LUsername, LPassword});
            //firstname = cursor.getString(cursor.getColumnIndex(DatabaseHelperLogin.COL_2));
            cursor.moveToFirst();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    firstname = cursor.getString(cursor.getColumnIndex(DatabaseHelperLogin.COL_2));
                     SharedPreferences login = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = login.edit();
                     editor1.putString("displayname",firstname);
                    editor1.commit();
                    //Displaying user's first name in toolbar
                    MainActivity.logintxt.setText("Hello! "+firstname);
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                     if(MainActivity.flag==1){
                         MainActivity.flag=0;
                         Fragment fragment = new Tab2Buy();
                         if (fragment != null) {
                             FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                             fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                         }
                     }

                    else {
                         Fragment fragment = new HomeFragment();
                         if (fragment != null) {
                             FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                             fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                         }
                     }
                }
                else {
                    Toast.makeText(getActivity(), "Please enter valid username and password!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    });
    return rootView;
}
}