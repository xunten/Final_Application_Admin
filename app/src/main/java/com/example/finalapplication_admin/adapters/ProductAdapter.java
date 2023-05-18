package com.example.finalapplication_admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.models.ProductModel;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    private List<ProductModel> array;
    private OnItemLongClickListener mListener;

    public void setOnItemClickListener(OnItemLongClickListener listener) {
        mListener = listener;
    }

    public ProductAdapter(Context context, List<ProductModel> array, OnItemLongClickListener mListener) {
        this.context = context;
        this.array = array;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(item, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel suggestProductModel = array.get(position);
        holder.proName.setText(suggestProductModel.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.proPrice.setText("đ"+decimalFormat.format(suggestProductModel.getPrice()));
        Glide.with(context).load(suggestProductModel.getImg_url()).into(holder.proImg);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    holder.itemView.showContextMenu();
                    mListener.onItemLongClick(suggestProductModel);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView proName, proPrice;
        ImageView proImg;

        public ViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            proName = itemView.findViewById(R.id.productName);
            proPrice = itemView.findViewById(R.id.productPrice);
            proImg = itemView.findViewById(R.id.productImg);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle("Options");
            menu.add(0, R.id.menu_edit, 0, "Edit");
            menu.add(0, R.id.menu_delete, 0, "Delete");
        }
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(ProductModel product);
    }
}
