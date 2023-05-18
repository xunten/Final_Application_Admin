package com.example.finalapplication_admin.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.activities.ShowOrderActivity;
import com.example.finalapplication_admin.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<OrderModel> array;

    public OrderAdapter(Context context, List<OrderModel> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderModel order = array.get(position);
        holder.idOrder.setText("Order #" + order.getId());
        if(order.getStatus().equals("Đơn hàng đã được thanh toán")) {
            holder.status.setTextColor(Color.parseColor("#FF0D4A10"));
            holder.status.setText(order.getStatus());
        } else if(order.getStatus().equals("Đơn hàng đang được giao")){
            holder.status.setTextColor(Color.parseColor("#008eff"));
            holder.status.setText(order.getStatus());
        } else if(order.getStatus().equals("Đơn hàng đang được xử lí")) {
            holder.status.setTextColor(Color.parseColor("#FF1100"));
            holder.status.setText(order.getStatus());
        } else if(order.getStatus().equals("Đã hủy")) {
            holder.status.setTextColor(Color.parseColor("#FF000000"));
            holder.status.setText(order.getStatus());
        }
        holder.created_at.setText("Order at " + order.getDate());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.rc_history_detail.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        linearLayoutManager.setInitialPrefetchItemCount(order.getProducts().size());

        //Adapter detail order
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(context, order.getProducts());
        holder.rc_history_detail.setLayoutManager(linearLayoutManager);
        holder.rc_history_detail.setAdapter(detailAdapter);
        holder.rc_history_detail.setRecycledViewPool(viewPool);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowOrderActivity.class);
                intent.putExtra("detailed", order);
                context.startActivity(intent);
            }
        });

        holder.status.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_status_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Xử lý sự kiện khi người dùng nhấn OK
                builder.setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lấy đối tượng RadioGroup và Button từ dialog layout
                                RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

                                int selectedId = radioGroup.getCheckedRadioButtonId();
                                if (selectedId != -1) {
                                    RadioButton radioButton = dialogView.findViewById(selectedId);
                                    // Lấy trạng thái đã chọn từ dialog
                                    String selectedStatus = radioButton.getText().toString();

                                    // Cập nhật vào cơ sở dữ liệu (Firestore)
                                    // TODO: Cập nhật dữ liệu vào Firestore với trạng thái đã chọn
                                    firestore.collection("Orders").document(order.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                task.getResult().getReference().update("status", selectedStatus);
                                                Toast.makeText(builder.getContext(), "Update Status Successfully ", Toast.LENGTH_SHORT).show();
                                                order.setStatus(selectedStatus);
                                                notifyItemChanged(position);
                                            } else {
                                                Toast.makeText(builder.getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(builder.getContext(), "Please select a status!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                // Set radio button selected based on the current status
                RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
                String currentStatus = holder.status.getText().toString();
                int radioIndex = -1;
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.getText().toString().equals(currentStatus)) {
                        radioIndex = i;
                        break;
                    }
                }

                if (radioIndex != -1) {
                    radioGroup.check(radioGroup.getChildAt(radioIndex).getId());
                }

                builder.show();
                return true; // Đánh dấu đã xử lý sự kiện nhấn giữ
            }
        });
    }


    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView idOrder, created_at;
        RecyclerView rc_history_detail;
        Button status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            created_at = itemView.findViewById(R.id.dateOrder);
            status = itemView.findViewById(R.id.status);
            rc_history_detail = itemView.findViewById(R.id.rc_history_detail);
        }
    }
}
