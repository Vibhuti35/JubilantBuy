package com.example.tonycurrie.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Tab3Compare extends Fragment {
    SharedPreferences position;
    RowItem rlist;
    TextView tname;
    Button btncompare;
    StringBuffer buffer1;
    int positions;
    String name,result;
    String imageurl;
    ImageView im;
    private ListView lv;
    public static ArrayList<CompareItems> listitems=new ArrayList<CompareItems>();
    CompareItems comp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3compare, container, false);
        position=this.getActivity().getSharedPreferences("Position",0);
        String proname=position.getString("Listpos",null);
        //fetching the position of the selected product from database
        for(int i=0;i<HomeFragment.listitems1.size();i++)
        {
            RowItem r= HomeFragment.listitems1.get(i);
            if(r.getproduct().equals(proname))
            {
                positions=i;
            }
        }

        lv = rootView.findViewById(R.id.listview);
        rlist=new RowItem();
        rlist=HomeFragment.listitems1.get(positions);
        name = rlist.getproduct();
        imageurl=rlist.getImageURL();
        tname =  rootView.findViewById(R.id.name);
        btncompare=rootView.findViewById(R.id.compare);
        im=rootView.findViewById(R.id.image);
        tname.setText(name);

        Picasso.with(this.getActivity()).load(imageurl).into(im);
        btncompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String json;

                 result="No result found";
                 buffer1= new StringBuffer();
                int count=0;
                //fetching the data from json and if the product name matches displaying the data from different websites
                try {
                    InputStream is = getActivity().getAssets().open("sample.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer,"UTF-8");
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray indexTitles = jsonObj.getJSONArray("indexTitles");
                    JSONArray data = jsonObj.getJSONArray("data");
                    int datalength = data.length();
                    for (int z = 0; z < datalength; z++) {
                        JSONArray dataItems = data.getJSONArray(z);
                        if( dataItems.getString(0).contains(name)){
                            comp=new CompareItems();
                            count++;
                            buffer1.append("Store:"+dataItems.getString(1)+"      "+"Price: "+dataItems.getString(2)+"   "+"URL:"+ dataItems.getString(3)+" \n"+" \n");
                            comp.setName(dataItems.getString(1));
                            comp.setPrice(dataItems.getString(2));
                            comp.setUrl(dataItems.getString(3));
                            listitems.add(comp);
                        }
                    }

                    if (count!=0) {
                        result = buffer1.toString();
                    }
                    CustomListViewAdapterCompare adapter=new CustomListViewAdapterCompare(Tab3Compare.this.getContext(),R.layout.listcompare,listitems);
                    lv.setAdapter(adapter);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),"not found" , Toast.LENGTH_LONG).show();

                }
                catch (JSONException e) {
                    Toast.makeText(getActivity(),"not found" , Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }


}