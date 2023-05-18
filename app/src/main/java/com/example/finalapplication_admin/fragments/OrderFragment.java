package com.example.finalapplication_admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.adapters.OrderAdapter;
import com.example.finalapplication_admin.models.OrderModel;
import com.example.finalapplication_admin.models.OrderProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    View root;

    LinearLayout progressbar;
    RecyclerView rc_order;
    FirebaseFirestore firestore;
    List<OrderProductModel> productList;

    List<OrderModel> list;
    OrderAdapter orderAdapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_order, container, false);

        firestore = FirebaseFirestore.getInstance();

        productList = new ArrayList<>();
        list = new ArrayList<>();

        progressbar = root.findViewById(R.id.progressbar_order);

        rc_order = root.findViewById(R.id.rc_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rc_order.setLayoutManager(layoutManager);

        //Load data
        loadData();

        return root;
    }

    private void loadData() {
    firestore.collection("Orders").orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //Thành công
                    for (DocumentSnapshot document : task.getResult()) {
                        //Get information
                        String id = document.getId();
                        String name = (String) document.get("name");
                        String phone = (String) document.get("phone");
                        String email = (String) document.get("email");
                        String address = (String) document.get("address");
                        String date = (String) document.get("date");
                        String status = (String) document.get("status");
                        int total = Integer.parseInt(document.get("total").toString());
                        //Get order detail
                        document.getReference().collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    productList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        productList.add(document.toObject(OrderProductModel.class));
                                    }
                                    // Tạo đối tượng OrderModel và thêm vào danh sách list
                                    OrderModel orderModel = new OrderModel(id, name, phone, email,
                                            address, date, total, status, productList);
                                    list.add(orderModel);

                                    // Gán danh sách list cho OrderAdapter và hiển thị lên RecyclerView
                                    orderAdapter = new OrderAdapter(getContext(), list);
                                    rc_order.setAdapter(orderAdapter);

                                    progressbar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), task.getException() + " :Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}