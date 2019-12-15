package com.example.callvideo.Presenter.LoadCourse;

import android.util.Log;

import com.example.callvideo.Model.Entities.Course;
import com.example.callvideo.Model.Entities.Tutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoadCourse  {
    private ILoadCourseListener loadCourseListener;
    public LoadCourse(ILoadCourseListener loadCourseListener){
        this.loadCourseListener=loadCourseListener;
    }
    public void loadDetailCourse(String courseId,HashMap<String,Object>tutorMap,HashMap<String,Object> courseMap) {
        //Firebase.setAndroidContext(context);
        DatabaseReference courseReference= FirebaseDatabase.getInstance().getReference("Course");
        courseReference.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Course course = dataSnapshot.getValue(Course.class);
                    courseMap.put("courseId",courseId);
                    courseMap.put("courseName", course.getCourseName());
                    courseMap.put("coursePrice", course.getPrice());
                    courseMap.put("courseDescript", course.getDescript());
                    courseMap.put("courseDiscount", course.getDiscount());
                    courseMap.put("courseSchedule", course.getSchedule());
                    courseMap.put("courseImage",course.getImage());
                    courseMap.put("tutorPhone",course.getTutorPhone());
                    loadCourseListener.onLoadDataCourse(courseMap);
//                loadView.LoadCourseDetailSuccess(courseMap);
                    String tutorPhone = course.getTutorPhone();
                    loadDetailTutor(tutorPhone, tutorMap);
                }
                else {
                    Log.e("Error","Data not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void loadDetailTutor(String tutorId,HashMap<String,Object>tutorMap) {
        DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                tutorMap.put("Exp",tutor.getExperience());
                tutorMap.put("Gmail",tutor.getEmail());
                tutorMap.put("Name",tutor.getUsername());
                tutorMap.put("Image",tutor.getAvatar());
                loadCourseListener.onLoadDataTutor(tutorMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void  setStatus(String status,String userPhone){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("User");
        userRef.child(userPhone).updateChildren(map);
    }
}
