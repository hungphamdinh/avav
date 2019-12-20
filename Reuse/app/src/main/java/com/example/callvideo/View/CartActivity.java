package com.example.callvideo.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Entities.Course;
import com.example.callvideo.Model.Entities.Order;
import com.example.callvideo.Model.Entities.Request;
import com.example.callvideo.R;
import com.example.callvideo.SQliteDatabase.BaseResipistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private TextView txtTotalCart,txtCourseName,txtPrice;
    private Button btnPlaceOrder;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference requestReference;
    private ArrayList<Order> cartList;
    private String courseId;
    private String userPhone;
    private ArrayList<String>listId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        requestReference = database.getReference("Requests");
        txtTotalCart = (TextView) findViewById(R.id.txtTotalCart);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        txtCourseName=(TextView)findViewById(R.id.txtCartItemName);
        txtPrice=(TextView)findViewById(R.id.txtCartItemPrice);
        if (getIntent() != null) {
            listId = getIntent().getStringArrayListExtra("listId");
            userPhone=listId.get(0);
            courseId=listId.get(1);

        }
        if (!listId.isEmpty() && listId != null) {
            if (Common.isConnectedToInternet(this)) {
                loadListCourse(courseId);
                onClickOrder();

            }
        }

    }

    private void onClickOrder() {
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(userPhone,courseId);
            }
        });
    }

    private void loadListCourse(String courseId) {
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course=dataSnapshot.getValue(Course.class);
                txtCourseName.setText(course.getCourseName());
                txtPrice.setText(course.getPrice());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void openDialog(String userPhone,String courseId) {
        LayoutInflater inflater = LayoutInflater.from(CartActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("Thanh toán");
        alertDialog.setMessage("Bạn có muốn thanh toán khóa học?");
        // final EditText inputValue = (EditText) subView.findViewById(R.id.edtValue);
        alertDialog.create();
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,Object>map=new HashMap<>();
                map.put("phone",userPhone);
                map.put("courseId",courseId);
                map.put("status",1);
                map.put("total",txtPrice.getText().toString());
                map.put("courseName",txtCourseName.getText().toString());
                requestReference.push().setValue(map);
                checkBuy(courseId);
                Toast.makeText(CartActivity.this, "Chúc bạn học thật tốt", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }

    private void checkBuy(String courseId){
        final DatabaseReference course = database.getReference("Course").child(courseId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("isBuy", "true");
        course.updateChildren(map);
    }

}
