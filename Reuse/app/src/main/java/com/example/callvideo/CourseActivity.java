package com.example.callvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Course;
import com.example.callvideo.ViewHolder.CourseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.ImageView;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference course;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        database = FirebaseDatabase.getInstance();
        course = database.getReference("Course");
        recyclerMenu = (RecyclerView) findViewById(R.id.listCourse);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadListCourse();
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
                        listIntent.add(viewHolder.txtDescript.getText().toString());
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
}


