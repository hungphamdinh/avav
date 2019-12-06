package com.example.callvideo.Model.MyCourseList;

import com.example.callvideo.Adapter.StaffAdapter;
import com.example.callvideo.Model.Entities.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class MyCourseListCallAdapter {
    private IMyCourseListAdaperListener myCourseListAdaperListener;
    public MyCourseListCallAdapter(IMyCourseListAdaperListener myCourseListAdaperListener){
        this.myCourseListAdaperListener=myCourseListAdaperListener;
    }
    public void loadTutor(String userPhone){
        DatabaseReference request= FirebaseDatabase.getInstance().getReference("Requests");
        request.orderByChild("phone").equalTo(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Request>requestList=new ArrayList<>();
                for (DataSnapshot childSnap :dataSnapshot.getChildren()){
                    Request requestCk=childSnap.getValue(Request.class);
                    requestList.add(requestCk);
                    myCourseListAdaperListener.callAdapter(requestList);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
