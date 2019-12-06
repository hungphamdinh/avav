package com.example.callvideo.Model.LoadDetailMyCourse;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Adapter.DocAdapter;
import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.Model.Entities.Tutor;
import com.example.callvideo.Notification.Token;
import com.example.callvideo.View.LoadDetailMyCourse.TutorDetailAcitivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

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
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Doc>docList=new ArrayList<>();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    docList.add(doc);
                    loadCourseListener.onLoadDocMyCourse(docList);
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
}
