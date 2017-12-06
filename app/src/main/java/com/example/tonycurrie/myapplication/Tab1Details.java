package com.example.tonycurrie.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Tab1Details extends Fragment{

    SharedPreferences positions;
    String pos;
    RowItem rlist;
    ImageView im;
    TextView tname,tprice,description,color,tsize,sell;
    int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.productitemdetails, container, false);

        //Position of the product clicked in Home page is retireved
        positions=this.getActivity().getSharedPreferences("Position",0);
        String proname=positions.getString("Listpos",null);

        for(int i=0;i<HomeFragment.listitems1.size();i++)
        {
           RowItem r= HomeFragment.listitems1.get(i);
            if(r.getproduct().equals(proname))
            {
                position=i;
            }
        }
        //Details of the product clicked are displayed
        rlist=new RowItem();
        rlist=HomeFragment.listitems1.get(position);
        String imageurl=rlist.getImageURL();
        String name=rlist.getproduct();
        String price=rlist.getprice();
        String col=rlist.getColor();
        String size=rlist.getSize();
        String desc=rlist.getDescription();
        String seller=rlist.getSeller();
        im=(ImageView) rootView.findViewById(R.id.image);
        tname=(TextView)rootView.findViewById(R.id.name);
        tprice=(TextView)rootView.findViewById(R.id.price);
        description=(TextView)rootView.findViewById(R.id.desc);
        color=(TextView)rootView.findViewById(R.id.col);
        tsize=(TextView)rootView.findViewById(R.id.size);
        sell=(TextView)rootView.findViewById(R.id.sell);
        tname.setText(name);
        tprice.setText(price);
        description.setText(desc);
        color.setText(col);
        tsize.setText(size);
        sell.setText(seller);
        Picasso.with(this.getActivity()).load(imageurl).into(im);

        return rootView;
    }
}