package com.example.finalapplication_admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.models.OrderProductModel;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{
    Context context;
    List<OrderProductModel> list;

    public OrderDetailAdapter(Context context, List<OrderProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProductModel item = list.get(position);
        holder.item_name.setText(item.getProName() + " ");
        holder.item_qty.setText("Qty: "+item.getProQty());
        holder.item_price.setText("Price: "+item.getProPrice());
        Glide.with(context).load(item.getProImg()).into(holder.item_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_name, item_qty, item_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_proImgDetail);
            item_name = itemView.findViewById(R.id.item_proNameDetail);
            item_qty = itemView.findViewById(R.id.item_proQtyDetail);
            item_price = itemView.findViewById(R.id.item_proPriceDetail);
        }
    }
}
