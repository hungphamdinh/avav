package com.example.callvideo.View.LoadCourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.callvideo.View.Cart.CartActivity;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Entities.Course;
import com.example.callvideo.Model.Entities.Order;
import com.example.callvideo.Presenter.LoadCourse.DetailCoursePresenter;
import com.example.callvideo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseDetailActivity extends AppCompatActivity implements LoadCourseDetailView {
    private TextView txtDCName, txtDCDiscount, txtDCDescript, txtDCPrice,txtExp,txtTutorName,txtGmail,txtSchedule,txtCourseDoc;
    private DatabaseReference courseReference;
    private FirebaseDatabase database;
    private ArrayList<String> courseDetailList;
    private Button btnAdd;
    private ImageView imageCourse,profile;
    private DetailCoursePresenter detailCoursePresenter;
    private Course course;
    private Order order;
    private String userPhone;
    private String courseID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        database = FirebaseDatabase.getInstance();
        courseReference = database.getReference("Course");
        txtDCName = (TextView) findViewById(R.id.txtcourseNameDetail);
        txtDCPrice = (TextView) findViewById(R.id.txtCoursePriceDetail);
        txtDCDiscount = (TextView) findViewById(R.id.txtCourseDiscountDetai);
        txtDCDescript = (TextView) findViewById(R.id.txtCourseDiscriptDetail);
        txtExp=(TextView)findViewById(R.id.txtExpTutorCourse);
        txtTutorName=(TextView)findViewById(R.id.txtUserNameTutorCourse);
        txtCourseDoc=(TextView)findViewById(R.id.txtCourseDoc);
        txtGmail=(TextView)findViewById(R.id.txtEmailTutorCourse);
        imageCourse=(ImageView)findViewById(R.id.imgDetailCourse);
        txtSchedule=(TextView)findViewById(R.id.txtScheduleTutor);
        profile=(ImageView) findViewById(R.id.imgProfileCourseDetail);
        btnAdd = (Button) findViewById(R.id.btnAddCart);
        detailCoursePresenter=new DetailCoursePresenter(this);

        if (getIntent() != null) {
            courseDetailList = getIntent().getStringArrayListExtra("DetailList");
            userPhone=courseDetailList.get(3);
            courseID=courseDetailList.get(4);

        }
        if (!courseDetailList.isEmpty() && courseDetailList != null) {
            if (Common.isConnectedToInternet(this)) {
                detailCoursePresenter.loadDetailPresenter(courseID);
            }
        }


    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        detailCoursePresenter.setStatus("online",userPhone);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        detailCoursePresenter.setStatus("offline",userPhone);
//    }

 
    @Override
    public void onDisplayCourse(HashMap<String, Object> map) {
        txtDCName.setText(map.get("courseName").toString());
        txtDCPrice.setText(map.get("coursePrice").toString());
        txtDCDescript.setText(map.get("courseDescript").toString());
        txtDCDiscount.setText(map.get("courseDiscount").toString());
        txtSchedule.setText(map.get("courseSchedule").toString());
        Glide.with(getApplicationContext())
                .load(map.get("courseImage"))
                .centerCrop()
                .into(imageCourse);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String>listId=new ArrayList<>();
                listId.add(userPhone);
                listId.add(courseID);
                Intent intent=new Intent(CourseDetailActivity.this, CartActivity.class);
                intent.putStringArrayListExtra("listId",listId);
                Toast.makeText(CourseDetailActivity.this, "Add to cart successed", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onDisplayTutor(HashMap<String, Object> map) {
        txtExp.setText(map.get("Exp").toString());
        txtGmail.setText(map.get("Gmail").toString());
        txtTutorName.setText(map.get("Name").toString());
        Glide.with(getApplicationContext())
                .load(map.get("Image").toString())
                .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .into(profile);

    }
}
