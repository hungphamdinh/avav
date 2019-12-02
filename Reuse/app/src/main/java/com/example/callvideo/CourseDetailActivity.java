package com.example.callvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Course;
import com.example.callvideo.Model.Order;
import com.example.callvideo.Model.Tutor;
import com.example.callvideo.SQliteDatabase.BaseResipistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseDetailActivity extends AppCompatActivity {
    private TextView txtDCName, txtDCDiscount, txtDCDescript, txtDCPrice,txtExp,txtTutorName,txtGmail,txtSchedule,txtCourseDoc;
    private DatabaseReference courseReference;
    private FirebaseDatabase database;
    private ArrayList<String> courseDetailList;
    private Button btnAdd;
    private BaseResipistory baseResipistory;
    private ImageView imageCourse,profile;
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
        if (getIntent() != null) {
            courseDetailList = getIntent().getStringArrayListExtra("DetailList");

        }
        if (!courseDetailList.isEmpty() && courseDetailList != null) {
            if (Common.isConnectedToInternet(this)) {
                userPhone=courseDetailList.get(3);
                courseID=courseDetailList.get(4);
                loadDetaillCourse(courseID);
            }
        }


    }

    private void loadDetaillCourse(String courseId) {
        courseReference.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                course = dataSnapshot.getValue(Course.class);
                //Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
                txtDCName.setText(course.getCourseName());
                txtDCPrice.setText(course.getPrice());
                txtDCDescript.setText(course.getDescript());
                txtDCDiscount.setText(course.getDiscount());
                txtSchedule.setText(course.getSchedule());
                Glide.with(CourseDetailActivity.this)
                        .load(course.getImage())
                        .centerCrop()
                        // .placeholder(R.drawable.loading_spinner)
                        .into(imageCourse);
    //            txtCourseDoc.setText(course.getCourseDoc());
                String tutorPhone=course.getTutorPhone();
                loadDetailTutor(tutorPhone);
                HashMap<Object, Object> orderMap = new HashMap<>();
                orderMap.put("courseName", course.getCourseName());
                orderMap.put("courseId", courseDetailList.get(4));
                orderMap.put("discount", course.getDiscount());
                orderMap.put("price", course.getPrice());
                orderMap.put("schedule", course.getSchedule());
                orderMap.put("tutorPhone",course.getTutorPhone());
//                txtCourseDoc.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Uri uri = Uri.parse(course.getCourseDoc());
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    }
//                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseResipistory.insert(orderMap);
                        Toast.makeText(CourseDetailActivity.this, "Add to cart successed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CourseDetailActivity.this,CartActivity.class));
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadDetailTutor(String tutorId) {
        DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                //Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
                txtExp.setText(tutor.getExperience());
                txtGmail.setText(tutor.getEmail());
                txtTutorName.setText(tutor.getUsername());
                Glide.with(CourseDetailActivity.this)
                        .load(tutor.getAvatar())
                        .centerCrop()
                        // .placeholder(R.drawable.loading_spinner)
                        .into(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

}
