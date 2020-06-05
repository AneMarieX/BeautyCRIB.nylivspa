package com.v5;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class OfferViewHolder extends RecyclerView.ViewHolder {


    public TextView offerName;
    public TextView offerDesc;
    public TextView offerDate;
    public TextView offerTime;
    public TextView offerPrice;
    public TextView offerLocation;
    public View view;




    public OfferViewHolder(View view){
        super(view);

        offerName = view.findViewById(R.id.offer_name);
        offerDesc = view.findViewById(R.id.offer_desc);
        offerDate = view.findViewById(R.id.offer_date);
        offerTime = view.findViewById(R.id.offer_time);
        offerPrice = view.findViewById(R.id.offer_price);
        offerLocation = view.findViewById(R.id.offer_location);


        this.view = view;
    }

}
