package com.example.tonycurrie.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OrderConfirmation extends Fragment {
    TextView amount;
    public OrderConfirmation() {
    }
    //Order confirmation to user
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_confirmation, container, false);
        amount=rootView.findViewById(R.id.amount);
        amount.setText("Amount : "+Tab2Buy.price);

        return rootView;
    }
}
