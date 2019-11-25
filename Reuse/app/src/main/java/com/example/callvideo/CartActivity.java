package com.example.callvideo;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Course;
import com.example.callvideo.Model.Order;
import com.example.callvideo.Model.Request;
import com.example.callvideo.SQliteDatabase.BaseResipistory;
import com.example.callvideo.ViewHolder.CartAdapter;
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
    private TextView txtTotalCart;
    private RecyclerView recyclerList;
    private Button btnPlaceOrder;
    private RecyclerView.LayoutManager layoutManager;
    private CartAdapter cartAdapter;
    private FirebaseDatabase database;
    private DatabaseReference requestReference;
    private ArrayList<Order> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        requestReference = database.getReference("Requests");
        txtTotalCart = (TextView) findViewById(R.id.txtTotalCart);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerlistCart);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        recyclerList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerList.setLayoutManager(layoutManager);
        loadListCourse();
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartList.size() > 0)
                    openDialog();
                else
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadListCourse() {
        cartList = new BaseResipistory(this).getinform();
        cartAdapter = new CartAdapter(cartList, this);
        recyclerList.setAdapter(cartAdapter);
        int total = 0;
        for (Order order : cartList)
            total += Integer.parseInt(order.getPrice());
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalCart.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        } else {
            deleteCart(item.getOrder());
        }
        return super.onContextItemSelected(item);

    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(CartActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("Thanh toán");
        alertDialog.setMessage("Bạn có muốn thanh toán khóa học?");
        // final EditText inputValue = (EditText) subView.findViewById(R.id.edtValue);
        alertDialog.create();
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  String key=inputKey.getText().toString();
                Request request = new Request(
                );
                request.setCourse(cartList);
                request.setName(Common.currentUser.getUsername());
                request.setPhone(Common.currentUser.getPhone());
                request.setTotal(txtTotalCart.getText().toString());
                for(int i=0;i<cartList.size();i++) {
                    request.setCourseId(cartList.get(i).getCourseId());
                    checkBuy(cartList.get(i).getCourseId());
                }
                //     Firebase mRefchild=mRef.child(key);
                //     mRefchild.setValue(data);
                requestReference.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                new BaseResipistory(getBaseContext()).cleanCart();
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

    private void deleteCart(int position) {
        cartList.remove(position);
        new BaseResipistory(this).cleanCart();//Clean old data from Sqlite
        for (Order item : cartList) {
            new BaseResipistory(this).insert(item);//add new Data from castList to Sqlite
        }
        loadListCourse();//Refresh

    }
    private void checkBuy(String courseId){
        final DatabaseReference course = database.getReference("Course").child(courseId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("isBuy", "true");
        course.updateChildren(map);
    }

    @Override
    protected void onStop() {
        super.onStop();
        new BaseResipistory(getBaseContext()).cleanCart();
    }
}
