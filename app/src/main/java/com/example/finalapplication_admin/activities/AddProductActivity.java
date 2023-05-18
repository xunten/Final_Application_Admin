package com.example.finalapplication_admin.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.models.BrandModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinner;
    TextView proName, proPrice, proDes;
    ImageView proImg, imgCamera;
    AppCompatButton add;

    int loai = 0;
    String brand = "";
    int edit;
    boolean flag = false;
    String str_hinhanh = "";
    String product_edit_id = "";

    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edit = getIntent().getIntExtra("isEdit", 0);
        product_edit_id = getIntent().getStringExtra("product_id");

        initView();
        ActionToolbar();

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        //Get brand
        List<String> stringList = new ArrayList<>();
        stringList.add("Choose brand ...");
        firestore.collection("Brand")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BrandModel brandModel = document.toObject(BrandModel.class);
                                stringList.add(brandModel.getBrandName());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //Lay brand duoc chon
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
                brand = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (edit == 0 ) {
            //nếu null thì thêm
            flag = false;
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProduct();
                }
            });
        }
        else {
            flag = true;
            toolbar.setTitle("Edit product");
            add.setText("Edit product");

            //get data into edit form
            CollectionReference productsRef = firestore.collection("AllProducts");
            Query query = productsRef.whereEqualTo("id", product_edit_id);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            proName.setText(document.get("name").toString());
                            proPrice.setText(document.get("price").toString());
                            proDes.setText(document.get("description").toString());

                            Glide.with(getApplicationContext()).load(document.get("img_url")).into(proImg);
                            str_hinhanh = document.get("img_url").toString();

                            int position = stringList.indexOf(document.get("type").toString());
                            spinner.setSelection(position);
                        }
                    }
                }
            });

            //update new information
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CollectionReference productsRef = firestore.collection("AllProducts");
                    Query query = productsRef.whereEqualTo("id", product_edit_id);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().update("name", proName.getText().toString().trim());
                                    document.getReference().update("price", Integer.parseInt(proPrice.getText().toString()));
                                    document.getReference().update("description", proDes.getText().toString().trim());
                                    document.getReference().update("img_url", str_hinhanh);
                                    document.getReference().update("type", brand);
                                }
                                Toast.makeText(getApplicationContext(), "Edited Successfully ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarAdd);
        spinner = findViewById(R.id.spinner_brand);

        proName = findViewById(R.id.addName);
        proPrice = findViewById(R.id.addPrice);
        proDes = findViewById(R.id.addDes);
        proImg = findViewById(R.id.proImg);

        add = findViewById(R.id.btnAddProduct);

        imgCamera = findViewById(R.id.imgcamera);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddProductActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    //Xử lí nút thêm hình ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri filePath = data.getData();

            // Hiển thị ảnh lên ImageView
            proImg.setImageURI(filePath);

            // Tạo một tài liệu mới trong Firestore
            DocumentReference newDocRef = firestore.collection("images").document();
            String newDocId = newDocRef.getId();

            // Lưu trữ ảnh vào Firebase Storage
            StorageReference imageRef = storageRef.child("images/" + newDocId + ".jpg");
            UploadTask uploadTask = imageRef.putFile(filePath);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        // Lưu thông tin ảnh vào Firestore
                        str_hinhanh = downloadUri.toString();
                    }
                } else {
                    // Lưu trữ thất bại
                }
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    public void addProduct(){
        String str_ten = proName.getText().toString().trim();
        String str_gia = proPrice.getText().toString().trim();
        String str_mota = proDes.getText().toString().trim();


        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_hinhanh) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || (loai == 0)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
        else {
            // Add a new document with a generated id.
            final String docId;
            docId = firestore.collection("AllProducts").document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put("name", str_ten);
            data.put("price", Integer.parseInt(str_gia));
            data.put("description", str_mota);
            data.put("img_url", str_hinhanh);
            data.put("type", brand);
            data.put("id", docId);

            firestore.collection("AllProducts")
                    .document(docId).set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddProductActivity.this, "Added to database successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}