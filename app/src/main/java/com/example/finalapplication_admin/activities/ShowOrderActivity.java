package com.example.finalapplication_admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.example.finalapplication_admin.R;
import com.example.finalapplication_admin.models.OrderModel;
import java.text.DecimalFormat;

public class ShowOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView name, total, phone, address, email, date, qtyPro, id;
    //Order
    OrderModel orderModel = null;
    int qty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        initView();
        ActionToolbar();

        final Object obj = getIntent().getSerializableExtra("detailed");
        if(obj instanceof OrderModel){
            orderModel = (OrderModel) obj;
        }

        if(orderModel != null){
            id.setText(orderModel.getId());
            name.setText(orderModel.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            total.setText(decimalFormat.format(orderModel.getTotal()) + " VND");
            phone.setText(orderModel.getPhone());
            address.setText(orderModel.getAddress());
            email.setText(orderModel.getEmail());
            date.setText(orderModel.getDate());
            for(int i = 0; i < orderModel.getProducts().size();i++){
                qty = qty + orderModel.getProducts().get(i).getProQty();
            }
            qtyPro.setText(qty+"");
        }
    }

    private void initView() {
        id = findViewById(R.id.textId);
        name = findViewById(R.id.textName);
        total = findViewById(R.id.textTotal);
        phone = findViewById(R.id.textPhone);
        address = findViewById(R.id.textAddress);
        email = findViewById(R.id.textEmail);
        date = findViewById(R.id.textDate);
        qtyPro = findViewById(R.id.textQtyProduct);
        toolbar = findViewById(R.id.toolbarOrder);

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