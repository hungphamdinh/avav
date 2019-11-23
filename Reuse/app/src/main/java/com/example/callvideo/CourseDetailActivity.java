package com.example.callvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView txtDCName, txtDCDiscount, txtDCDescript, txtDCPrice,txtExp,txtTutorName,txtGmail,txtSchedule;
    private DatabaseReference courseReference;
    private FirebaseDatabase database;
    private ArrayList<String> courseDetailList;
    private Button btnAdd;
    private BaseResipistory baseResipistory;
    private Course course;
    private Order order;
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
        txtGmail=(TextView)findViewById(R.id.txtEmailTutorCourse);
        txtSchedule=(TextView)findViewById(R.id.txtScheduleTutor);
        baseResipistory = new BaseResipistory(this);
        btnAdd = (Button) findViewById(R.id.btnAddCart);
        if (getIntent() != null) {
            courseDetailList = getIntent().getStringArrayListExtra("DetailList");

        }
        if (!courseDetailList.isEmpty() && courseDetailList != null) {
            if (Common.isConnectedToInternet(this)) {
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
                txtDCPrice.setText("Price: " + course.getPrice());
                txtDCDescript.setText(course.getDescript());
                txtDCDiscount.setText("Discount" + course.getDiscount());
                txtSchedule.setText(course.getSchedule());
                String tutorPhone=course.getTutorPhone();
                loadDetailTutor(tutorPhone);
                HashMap<Object, Object> orderMap = new HashMap<>();
                orderMap.put("courseName", course.getCourseName());
                orderMap.put("courseId", courseDetailList.get(4));
                orderMap.put("discount", course.getDiscount());
                orderMap.put("price", course.getPrice());
                orderMap.put("schedule", course.getSchedule());
                orderMap.put("tutorPhone",course.getTutorPhone());
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseResipistory.insert(orderMap);
                        Toast.makeText(CourseDetailActivity.this, "Add to cart successed", Toast.LENGTH_SHORT).show();
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
