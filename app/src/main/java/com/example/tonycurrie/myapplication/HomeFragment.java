package com.example.tonycurrie.myapplication;

/**
 * Created by priyanka on 18/11/17.
 */

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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String TAG = HomeFragment.class.getSimpleName();
    private ListView lv;
    String productName,price,imageURL,color,gender,size,description,quantity,category,seller,age;
    String s = " ";
    ArrayList<RowItem> listitems=new ArrayList<RowItem>();
    public static ArrayList<RowItem> listitems1=new ArrayList<RowItem>();
    RowItem rlist;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    String filter;
    SharedPreferences catf;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        lv = rootView.findViewById(R.id.listview);

        catf = this.getActivity().getSharedPreferences("Category", 0);
        filter = catf.getString("category_filter", null);


        this.openHelper = new DatabaseHelper(this.getActivity());
        this.database = openHelper.getWritableDatabase();
        Cursor cursor ;
        if(filter!=null){
            //Displaying the products according to Men Category and Women Category
            cursor = database.rawQuery("SELECT * FROM PRODUCT WHERE GENDER = '" + filter + "'", null);
            filter=null;
            SharedPreferences.Editor editor1 = catf.edit();
            editor1.clear();
            editor1.commit();
        }
        //Displaying the products which match to the string in searchbar
        else if(MainActivity.searchflag==1){
            cursor = database.rawQuery("SELECT * FROM PRODUCT WHERE ITEM_NAME Like '%" + MainActivity.searchtxt + "%' COLLATE NOCASE",null);
            MainActivity.searchflag=0;
        }
        else {
            //Fetching the product details from database
            cursor = database.rawQuery("SELECT * FROM PRODUCT", null);
        }


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            rlist=new RowItem();
            productName=cursor.getString(1);
            price=cursor.getString(5);
            color=cursor.getString(2);
            gender=cursor.getString(3);
            size=cursor.getString(4);
            price=cursor.getString(5);
            description=cursor.getString(6);
            quantity=cursor.getString(7);
            category=cursor.getString(8);
            seller=cursor.getString(9);
            age=cursor.getString(10);
            imageURL=cursor.getString(11);
            rlist.setproduct(productName);
            rlist.setColor(color);
            rlist.setGender(gender);
            rlist.setSize(size);
            rlist.setDescription(description);
            rlist.setQuantity(quantity);
            rlist.setCategory(category);
            rlist.setSeller(seller);
            rlist.setAge(age);
            rlist.setprice(price);
            rlist.setimageURL(imageURL);
            rlist.setprice(price);
            listitems.add(rlist);
            listitems1.add(rlist);
            cursor.moveToNext();
        }
        cursor.close();
        this.database.close();
        //Stting up the customised listview
        CustomListViewAdapter adapter=new CustomListViewAdapter(HomeFragment.this.getContext(),R.layout.list,listitems);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int pos=position;
                rlist= listitems.get(pos);
                String proname=rlist.getproduct();
                SharedPreferences Position = getActivity().getSharedPreferences("Position", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = Position.edit();
                //Passing the postion of the product to display the details of the selected product (To use by other classes)
                editor.putString("Listpos", ""+proname);
                editor.commit();
                Fragment fragment = new Tab1Details();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                }
            }
        });

        return rootView;
    }
}
