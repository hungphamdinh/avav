package com.example.callvideo.Presenter.MyCourseList;

import android.widget.Toast;

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
    public void loadCourse(HashMap<String,Object>posMap){
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        int pos= (int) posMap.get("pos");
        courseRef.child(requests.get(pos).getCourseId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //requests=new ArrayList<>();
                Course course=dataSnapshot.getValue(Course.class);
                if(course.getStatus()==0){
//                    map.put("courseName", "");
                    myCourseListListener.onErrorLoadData("Cập nhật khóa học");
                }
                else {
                    HashMap<String,Object>tutorMap=new HashMap<>();
                    HashMap<String,Object>map=new HashMap<>();
                    map.put("courseName", course.getCourseName());
                    map.put("courseSchedule", course.getSchedule());
                    map.put("courseImage", course.getImage());
                    myCourseListListener.onLoadCourseMyCourse(map);
                    onClickItem(course, posMap.get("userId").toString(), (Integer) posMap.get("pos"));
                    loadTutor(course.getTutorPhone(), tutorMap);
                }
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
