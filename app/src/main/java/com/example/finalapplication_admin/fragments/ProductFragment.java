package com.example.finalapplication_admin.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.activities.AddProductActivity;
import com.example.finalapplication_admin.adapters.ProductAdapter;
import com.example.finalapplication_admin.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements ProductAdapter.OnItemLongClickListener {
    View root;

    LinearLayout progressbar;
    RecyclerView rc_product;
    //Brand home recyclerview
    ProductAdapter productAdapter;
    List<ProductModel> productModelList;
    ImageView add;
    EditText search;
    String proSelect;
    String proSelectId;

    FirebaseFirestore db;
    FirebaseUser user;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_product, container, false);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        initView();

        search = root.findViewById(R.id.editSearch);
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                filterList.clear();
//
//                if(editable.toString().isEmpty() ) {
//
//                    rc_product.setAdapter(new ProductAdapter(getContext(), proList, this));
//                    productAdapter.notifyDataSetChanged();
//                }
//                else {
//                    Filter(editable.toString());
//                }
//
//            }
//        });

        //Load data
        loadData();

        return root;
    }

//    private void Filter(String text) {
//        for (ProductModel product:productModelList) {
//            if (product.getName().equals(text)) {
//                filterList.add(product);
//
//            }
//        }
//        rc_product.setAdapter(new ProductAdapter(getContext(), filterList, getActivity().));
//        productAdapter.notifyDataSetChanged();
//    }

    public void initView() {
        progressbar = root.findViewById(R.id.progressbar);
        rc_product = root.findViewById(R.id.rc_product);
        rc_product.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        productModelList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productModelList, this);
        rc_product.setAdapter(productAdapter);

        add = root.findViewById(R.id.img_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });

        //search
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                // Handle edit action
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.putExtra("isEdit", 1);
                intent.putExtra("product_id", proSelectId);
                startActivity(intent);
                return true;
            case R.id.menu_delete:
                // Handle delete action
                CollectionReference productsRef = db.collection("AllProducts");
                Query query = productsRef.whereEqualTo("id", proSelectId);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Warning: ");
                builder.setMessage("Bạn có chắc chắn xóa sản phẩm '"+proSelect+"' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                        Toast.makeText(getContext(), "Product successfully deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                    loadData();
                                } else {
                                    Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadData() {
        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(productModelList != null){
                                productModelList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel suggestProductModel = document.toObject(ProductModel.class);
                                productModelList.add(suggestProductModel);
                                productAdapter.notifyDataSetChanged();
                            }

                            progressbar.setVisibility(View.GONE);

                            productAdapter.setOnItemClickListener(new ProductAdapter.OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(ProductModel product) {
                                    proSelectId = product.getId();
                                    proSelect = product.getName();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemLongClick(ProductModel product) {

    }
}