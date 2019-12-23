package com.example.callvideo.View.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Entities.Order;
import com.example.callvideo.Presenter.Cart.CartPresenter;
import com.example.callvideo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements ICartView {
    private TextView txtTotalCart,txtCourseName,txtPrice;
    private Button btnPlaceOrder;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Order> cartList;
    private String courseId;
    private String userPhone;
    private ArrayList<String>listId;
    private CartPresenter cartPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtTotalCart = (TextView) findViewById(R.id.txtTotalCart);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        txtCourseName=(TextView)findViewById(R.id.txtCartItemName);
        txtPrice=(TextView)findViewById(R.id.txtCartItemPrice);
        cartPresenter=new CartPresenter(this);
        if (getIntent() != null) {
            listId = getIntent().getStringArrayListExtra("listId");
            userPhone=listId.get(0);
            courseId=listId.get(1);

        }
        if (!listId.isEmpty() && listId != null) {
            if (Common.isConnectedToInternet(this)) {
                HashMap<String,Object>map=new HashMap<>();
                map.put("courseName",txtCourseName.getText().toString());
                map.put("courseId",courseId);
                map.put("price",txtPrice.getText().toString());
                map.put("userPhone",userPhone);
                cartPresenter.loadListCourse(map);
                onClickOrder();

            }
        }

    }

    private void onClickOrder() {
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
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
                HashMap<String,Object>map=new HashMap<>();
                map.put("courseName",txtCourseName.getText().toString());
                map.put("courseId",courseId);
                map.put("price",txtPrice.getText().toString());
                map.put("userPhone",userPhone);
                cartPresenter.onPurchased(map);
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


    @Override
    public void onDisplaySuccess(String msg) {
        Toast.makeText(CartActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisplayError(String msg) {
        Toast.makeText(CartActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisplayCourse(HashMap<String, Object> map) {
        String courseName=map.get("courseName").toString();
        String price=map.get("price").toString();
        txtCourseName.setText(courseName);
        txtPrice.setText(price+"vnđ");
        txtTotalCart.setText(price+"vnđ");
    }
}
