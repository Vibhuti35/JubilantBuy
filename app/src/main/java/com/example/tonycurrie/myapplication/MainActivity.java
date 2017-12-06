package com.example.tonycurrie.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String[] drawer_titles;
    private DrawerLayout drawerlayout;
    private ListView drawer_list;
    Toolbar toolbar;
   // private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle drawer_toggle;
    String name;
    public static String searchtxt;
    public static TextView logintxt;
    public static int flag=0;
    EditText search;
    public static int searchflag=0;
    public static SQLiteHelper sqLiteHelper;

    //Setting the drawer items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logintxt = (TextView) findViewById(R.id.login);

        drawer_titles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_list = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();
        //Passing the image and and the name of the list item in navigation drawer
        DataModel[] drawerItem = new DataModel[7];
        drawerItem[0] = new DataModel(R.drawable.login, "Login");
        drawerItem[1] = new DataModel(R.drawable.man, "MenCategory");
        drawerItem[2] = new DataModel(R.drawable.woman, "WomenCategory");
        drawerItem[3] = new DataModel(R.drawable.pos, "PostAd");
        drawerItem[4] = new DataModel(R.drawable.up, "UsedProducts");
        drawerItem[5] = new DataModel(R.drawable.b, "Barcode");
        drawerItem[6] = new DataModel(R.drawable.logout, "Logout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        //Creating an object for custom adapter
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, drawerItem);
        drawer_list.setAdapter(adapter);
        drawer_list.setOnItemClickListener(new DrawerItemClickListener());
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerlayout.addDrawerListener(drawer_toggle);
        setupDrawerToggle();
        //Home fragment when all the products are seen is displayed when the app opens(oncreate)
        Fragment fragment = new HomeFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            drawerlayout.closeDrawer(drawer_list);

        }
        else {
            Log.e("MainActivity", "Error in creating fragment");
        }
        //Displaying the first name if the user session is not end
        SharedPreferences login1 = this.getSharedPreferences("login", 0);
        name = login1.getString("displayname", null);
        if(name!=null) {
            TextView txt = (TextView) findViewById(R.id.login);
            txt.setText("Hello! "+name);
        }
        //sqLiteHelper object is created in Oncreate of mainactivity to avoid null point exeption when used products is clicked
        sqLiteHelper=new SQLiteHelper(this,"PostAdDB.sqlite",null,1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS ClothesAd(Id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,price VARCHAR, details VARCHAR,image BLOG)");

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
        }
    }
   //Calling respective fragment according to the option choosed in the drawer list
    private void selectItem(int position) {
        Fragment fragment = null;
        SharedPreferences Category = this.getSharedPreferences("Category", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Category.edit();

        //Differentiating the category selected
        switch (position) {
            case 0:
                fragment = new ConnectFragment();
                break;
            case 1: {
                editor.putString("category_filter", "Men");
                editor.apply();
                fragment = new HomeFragment();
                break;
            }
            case 2:{
                editor.putString("category_filter", "Women");
                editor.apply();
                fragment = new HomeFragment();
                break;
        }

            case 3: {
                //Redirecting to the activity where user can upload image and Details of the ad
                Intent intent = new Intent(MainActivity.this, PostAd.class);
                startActivity(intent);
                break;

            }
            case 4:{
                //Redirecting to the activity in which Used products(itmes added through postad) are displayed,
                Intent intent = new Intent(MainActivity.this, ClothesList.class);
                startActivity(intent);
                break;
            }
            case 5:{
                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            }

            case 6: {
                SharedPreferences login = this.getSharedPreferences("login", 0);
                name = login.getString("displayname", null);
                //If user clicks on log out with out logging in
                if(name==null) {
                    Toast.makeText(this, "You are not logged in!", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences.Editor editor1 = login.edit();
                    editor1.clear();
                    editor1.commit();
                    logintxt.setText("");
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    fragment = new HomeFragment();
                }
                break;
            }

            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            drawer_list.setItemChecked(position, true);
            drawer_list.setSelection(position);
            drawerlayout.closeDrawer(drawer_list);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
   /* Fragments clicks are stored in stack so that
    when the back button is pressed, application is not closed*/
    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
        else{

            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawer_toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawer_toggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        drawer_toggle = new android.support.v7.app.ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        drawer_toggle.syncState();
    }
    //When user clicks on Jubilant Buy it is redirected to home fragment
    public void onClickName(View view){
        Fragment fragment = new HomeFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }


    }
    //Redirecting to Registration fragment when registration button is clicked
    public void onClickRegistration(View v) {
        Fragment fragment = new Registration();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        }

    }
    //user should login, if not logged in before buying
    public void onClickBuy(View v) {

        SharedPreferences login1 =getSharedPreferences("login", 0);
        String name = login1.getString("displayname", null);
        if(logintxt.getText()=="") {
            //flag is used to redirect to Buynow fragment after logging in. When flag is zero Home fragment is loaded after logging in
            flag=1;
            Fragment fragment = new ConnectFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            }

        }
        else {
            Fragment fragment = new Tab2Buy();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            }
        }

    }
    //Redirecting to Compare fragment when compare button is clicked on Prodct Details fragment
    public void onClickCompare(View v) {
        Fragment fragment = new Tab3Compare();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        }

    }
    //Redirecting to Search fragment when compare button is search is clicked on Home fragment
    public void onClickSearch(View v) {

        search= (EditText) findViewById(R.id.search_box);
        searchtxt=(search.getText()).toString();
        searchtxt.trim();
        searchflag=1;

        Fragment fragment = new HomeFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        }

    }

}
