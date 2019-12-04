package com.example.callvideo.View.LoadMyCourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.callvideo.CartActivity;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Course;
import com.example.callvideo.Model.Order;
import com.example.callvideo.Presenter.LoadCourse.DetailCoursePresenter;
import com.example.callvideo.Presenter.LoadCourse.IDetailCoursePresenter;
import com.example.callvideo.R;
import com.example.callvideo.SQliteDatabase.BaseResipistory;
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
    private BaseResipistory baseResipistory;
    private ImageView imageCourse,profile;
    private IDetailCoursePresenter detailCoursePresenter;
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
        baseResipistory = new BaseResipistory(this);
        btnAdd = (Button) findViewById(R.id.btnAddCart);
        detailCoursePresenter=new DetailCoursePresenter(this,this);

        if (getIntent() != null) {
            courseDetailList = getIntent().getStringArrayListExtra("DetailList");

        }
        if (!courseDetailList.isEmpty() && courseDetailList != null) {
            if (Common.isConnectedToInternet(this)) {
                userPhone=courseDetailList.get(3);
                courseID=courseDetailList.get(4);
                detailCoursePresenter.loadDetailPresenter(courseID);
            }
        }


    }



    private void setStatus(String status){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("User");
        userRef.child(userPhone).updateChildren(map);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("offline");
    }

 
    @Override
    public void onDisplayCourse(HashMap<String, Object> map) {
        txtDCName.setText(map.get("courseName").toString());
        txtDCPrice.setText(map.get("coursePrice").toString());
        txtDCDescript.setText(map.get("courseDescript").toString());
        txtDCDiscount.setText(map.get("courseDiscount").toString());
        txtSchedule.setText(map.get("courseSchedule").toString());
        Glide.with(CourseDetailActivity.this)
                .load(map.get("courseImage"))
                .centerCrop()
                .into(imageCourse);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseResipistory.insert(map);
                Toast.makeText(CourseDetailActivity.this, "Add to cart successed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CourseDetailActivity.this, CartActivity.class));
            }
        });
    }

    @Override
    public void onDisplayTutor(HashMap<String, Object> map) {
        txtExp.setText(map.get("Exp").toString());
        txtGmail.setText(map.get("Gmail").toString());
        txtTutorName.setText(map.get("Name").toString());
        Glide.with(CourseDetailActivity.this)
                .load(map.get("Image").toString())
                .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .into(profile);

    }
}
