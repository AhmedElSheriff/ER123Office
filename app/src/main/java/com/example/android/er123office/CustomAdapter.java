package com.example.android.er123office;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.er123office.data.Driver;

import java.util.List;

/**
 * Created by Abshafi on 3/20/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    List<Driver> dummyList;
    Context context;
    private LayoutInflater inflater;
    public CustomAdapter(List<Driver> dummyList, Context context) {
        this.dummyList = dummyList;
        this.context= context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.recycle_item,parent,false);
        CustomViewHolder customViewHolder= new CustomViewHolder(row);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if(dummyList.get(position).getDriverAvailable().equals("true")) {
            holder.nameText.setText(dummyList.get(position).getPlateChars() + " " + dummyList.get(position).getPlateNums());
            holder.imageView.setImageResource(R.drawable.goodambulance);
        }
    }

    @Override
    public int getItemCount() {
        return dummyList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        ImageView imageView;
        LinearLayout rootView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.text_ambulance);
            imageView = (ImageView) itemView.findViewById(R.id.image_ambulance);
            rootView = (LinearLayout)  itemView.findViewById(R.id.root_view);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Driver driver = dummyList.get(getAdapterPosition());
                    driver.setSelected(!driver.isSelected());
                    rootView.setSelected(driver.isSelected());
                    return true;
                }
            });
        }
    }
}