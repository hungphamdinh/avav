package com.example.callvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Course;
import com.example.callvideo.View.LoadMyCourse.CourseDetailActivity;
import com.example.callvideo.ViewHolder.CourseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference course;
    private FirebaseDatabase database;
    private String phoneUser;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        database = FirebaseDatabase.getInstance();
        course = database.getReference("Course");
        if (getIntent() != null)
            phoneUser = getIntent().getStringExtra("phoneUser");
        if (!phoneUser.isEmpty() && phoneUser != null) {
            if (Common.isConnectedToInternet(this)) {
            } else {
                Toast.makeText(CourseActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        recyclerMenu = (RecyclerView) findViewById(R.id.listCourse);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadListCourse();
    }
    private void setStatus(String status){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("User");
        userRef.child(phoneUser).updateChildren(map);
    }
    private void loadListCourse() {
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                (Course.class, R.layout.course_layout,
                        CourseViewHolder.class,
                        course.orderByChild("isBuy").equalTo("false")) {
            @Override
            protected void populateViewHolder(CourseViewHolder viewHolder, final Course model, int position) {
                viewHolder.txtName.setText(model.getCourseName());
                viewHolder.txtPrice.setText("Giá: " + model.getPrice());
                viewHolder.txtDescript.setText(model.getDescript());
                Glide.with(CourseActivity.this)
                        .load(model.getImage())
                        .centerCrop()
                        // .placeholder(R.drawable.loading_spinner)
                        .into(viewHolder.courseImage);
                //viewHolder.txtTutorName.setText("Giảng viên: " + model.getTutorPhone());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
                        ArrayList<String> listIntent = new ArrayList<>();
                        listIntent.add(viewHolder.txtName.getText().toString());
                        listIntent.add(viewHolder.txtPrice.getText().toString());
                        listIntent.add(viewHolder.txtTutorName.getText().toString());
                        listIntent.add(phoneUser);
                        listIntent.add(adapter.getRef(position).getKey());
                        intent.putStringArrayListExtra("DetailList", listIntent);
                        //intent.putExtra("DetailList", (Parcelable) listIntent);

                        startActivity(intent);
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
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


