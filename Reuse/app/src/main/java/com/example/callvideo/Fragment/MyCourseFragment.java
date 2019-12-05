package com.example.callvideo.Fragment;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Adapter.StaffAdapter;
import com.example.callvideo.Model.Request;
import com.example.callvideo.R;
import com.example.callvideo.ViewHolder.StaffViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by User on 2/28/2017.
 */

public class MyCourseFragment extends Fragment {
    private Context context;
    private String userPhone;
    public MyCourseFragment(){}
    public MyCourseFragment(Context context, String userPhone){
        this.context=context;
        this.userPhone=userPhone;
    }
    private static final String TAG = "MyCourseFragment";
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference request;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Request, StaffViewHolder> adapter;
    private StaffAdapter staffAdapter;
    private ArrayList<Request>requestList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course,container,false);
        database= FirebaseDatabase.getInstance();
        request =database.getReference("Requests");
        recyclerMenu=(RecyclerView)view.findViewById(R.id.listOrderRecycler);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerMenu.setLayoutManager(layoutManager);
        loadTutor();
        return view;
    }
    private void loadTutor(){
        request.orderByChild("phone").equalTo(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList=new ArrayList<>();
                for (DataSnapshot childSnap :dataSnapshot.getChildren()){
                    Request requestCk=childSnap.getValue(Request.class);
                    requestList.add(requestCk);
                    staffAdapter=new StaffAdapter(context,requestList,requestCk.getCourseId(),userPhone);
                    staffAdapter.notifyDataSetChanged();
                    recyclerMenu.setAdapter(staffAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    private void loadListTutor() {
//        adapter=new FirebaseRecyclerAdapter<Request, StaffViewHolder>
//                (Request.class,R.layout.my_course_layout,
//                        StaffViewHolder.class,
//                        request.orderByChild("phone").equalTo(userPhone)) {
//            @Override
//            protected void populateViewHolder(StaffViewHolder viewHolder, final Request model, int position) {
//
//                    viewHolder.txtName.setText(model.getName());
//                    viewHolder.txtEmail.setText(model.getPhone());
//                    loadCourse(model.getCourseId(),viewHolder);
//
//            }
//
//        };
//        adapter.notifyDataSetChanged();
//        recyclerMenu.setAdapter(adapter);
//    }
//    private void loadCourse(String courseID,StaffViewHolder viewHolder){
//        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
//        courseRef.child(courseID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Course course=dataSnapshot.getValue(Course.class);
//                viewHolder.txtCourseName.setText(course.getCourseName());
//                viewHolder.txtSchedule.setText(course.getSchedule());
//                viewHolder.txtDescript.setText(course.getDescript());
//                loadTutor(course.getTutorPhone(),viewHolder);
//                onClickItem(course, viewHolder);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void onClickItem(Course course, StaffViewHolder viewHolder) {
//        viewHolder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                Intent intent=new Intent(context, TutorDetailAcitivity.class);
//                String tutorID=course.getTutorPhone();
//                String userID=userPhone;
//                ArrayList<String> listIntent=new ArrayList<>();
//                listIntent.add(tutorID);
//                listIntent.add(userID);
//                intent.putStringArrayListExtra("ChatID",listIntent);
//                //intent.putExtra("tutorID",adapter.getRef(position).getKey());
//                startActivity(intent);                    }
//        });
//    }
//
//    private void loadTutor(String tutorPhone, StaffViewHolder viewHolder){
//        DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
//        tutorRef.child(tutorPhone).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Tutor tutor=dataSnapshot.getValue(Tutor.class);
//                viewHolder.txtName.setText(tutor.getUsername());
//                viewHolder.txtEmail.setText(tutor.getEmail());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
