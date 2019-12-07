package com.example.callvideo.Presenter.MyCourseList;

import com.example.callvideo.Model.Entities.Course;
import com.example.callvideo.Model.Entities.Request;
import com.example.callvideo.Model.Entities.Tutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCourseList {
    IMyCourseListListener myCourseListListener;
    ArrayList<Request>requests;
    public MyCourseList(IMyCourseListListener myCourseListListener,ArrayList<Request>requests){
        this.myCourseListListener=myCourseListListener;
        this.requests=requests;
    }
    public void loadCourse(HashMap<String,Object>tutorMap,HashMap<String,Object>posMap,HashMap<String,Object>map){
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(requests.get((Integer) posMap.get("pos")).courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course=dataSnapshot.getValue(Course.class);
                map.put("courseName",course.getCourseName());
                map.put("courseSchedule",course.getSchedule());
                map.put("courseImage",course.getImage());
                myCourseListListener.onLoadCourseMyCourse(map);
                onClickItem(course,posMap.get("userId").toString(), (Integer) posMap.get("pos"));
                loadTutor(course.getTutorPhone(),tutorMap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickItem(Course course,String userId,int position) {
        String tutorID=course.getTutorPhone();
        String userID=userId;
        ArrayList<String> listIntent=new ArrayList<>();
        listIntent.add(tutorID);
        listIntent.add(userID);
        listIntent.add(requests.get(position).courseId);
        myCourseListListener.onLoadDataToClick(listIntent);
    }

    public void loadTutor(String tutorPhone, HashMap<String,Object>tutorMap){
        DatabaseReference tutorRef= FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.child(tutorPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                if(tutor.getStatus().equals("offline")){
                    myCourseListListener.offlineStatus("Giảng viên hiện không hoạt dộng");
                }
                else{
                    myCourseListListener.onlineStatus("Giảng viên hiện đang hoạt động");
                }
                tutorMap.put("tutorName",tutor.getUsername());
                tutorMap.put("tutorMail",tutor.getEmail());
                tutorMap.put("tutorImage",tutor.getAvatar());
                myCourseListListener.onLoadTutorMyCourse(tutorMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
