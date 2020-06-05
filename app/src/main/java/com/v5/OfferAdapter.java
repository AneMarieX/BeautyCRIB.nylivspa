package com.v5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferViewHolder>{

    private Context context;
    private ArrayList<Offer> offerList;
    private FirebaseDataListener listener;


    public OfferAdapter(Context context, ArrayList<Offer> offerList){
        this.context = context;
        this.offerList = offerList;
        this.listener = (FirebaseDataListener)context;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_view, parent, false);
        OfferViewHolder holder = new OfferViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, final int position)
    {

        holder.offerName.setText(offerList.get(position).getName());
        holder.offerDesc.setText(offerList.get(position).getDesc());
        holder.offerDate.setText(offerList.get(position).getDate());
        holder.offerTime.setText(offerList.get(position).getTime());
        holder.offerPrice.setText(offerList.get(position).getPrice()+" kr.");
        holder.offerLocation.setText(offerList.get(position).getLocation() + " Location");

        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.onDataClick(offerList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount()
    {

        return offerList.size();
    }


    //interface data listener
    public interface FirebaseDataListener {
        void onDataClick(Offer offer, int position);
    }
}
