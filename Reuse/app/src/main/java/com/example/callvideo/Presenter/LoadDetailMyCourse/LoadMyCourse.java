package com.example.callvideo.Presenter.LoadDetailMyCourse;

import android.util.Log;

import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.Model.Entities.Tutor;
import com.example.callvideo.Notification.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadMyCourse {
    private ILoadMyCourseListener loadCourseListener;
    public LoadMyCourse(ILoadMyCourseListener loadCourseListener){
        this.loadCourseListener=loadCourseListener;
    }
    public void getDetailTutor(String tutorId, HashMap<String,Object>tutorMap) {
        DatabaseReference tutorRef= FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                if(tutor.getStatus().equals("offline")){
                    loadCourseListener.offlineStatus("Giảng viên hiện không hoạt động");
                }
                else {
                    loadCourseListener.onlineStatus("Giảng viên hiện đang hoạt động");

                }
                tutorMap.put("title",tutor.getUsername());
                tutorMap.put("tutorName",tutor.getUsername());
                tutorMap.put("tutorEmail",tutor.getEmail());
                tutorMap.put("tutorExp",tutor.getExperience());
                tutorMap.put("tutorImage",tutor.getAvatar());
                loadCourseListener.onLoadTutorMyCourse(tutorMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadCourseDoc(String courseId){
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        ArrayList<Doc>docList=new ArrayList<>();
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                docList.clear();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    if(doc.getType().compareTo("tutorTest")!=0&&doc.getStatus()==1) {
                        docList.add(doc);
                        loadCourseListener.onLoadDocMyCourse(docList);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadTutorTest(String courseId){
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        ArrayList<Doc>docList=new ArrayList<>();
        ArrayList<String>docKey=new ArrayList<>();
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                docList.clear();
                docKey.clear();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {

                    Doc doc = childSnap.getValue(Doc.class);
                    if(doc.getType().equals("tutorTest")&&doc.getStatus()==1) {
                        docList.add(doc);
                        docKey.add(childSnap.getKey());
                        loadCourseListener.onLoadTutorTest(docList,docKey);
                    }
                    else {
                        //loadCourseListener.onError("Tải lại bài test");
                        Log.e("NullTest","Check your test status");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateToken(String userId,String token){
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        Token newToken=new Token(token);
        tokenRef.child(userId).setValue(newToken);
        loadCourseListener.updateToken("Token was updated");
    }
    public void  setStatus(String status,String userPhone){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("User");
        userRef.child(userPhone).updateChildren(map);
    }
}
