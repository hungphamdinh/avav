package com.example.callvideo.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Course;
import com.example.callvideo.Model.Request;
import com.example.callvideo.Model.Tutor;
import com.example.callvideo.R;
import com.example.callvideo.TutorDetailAcitivity;
import com.example.callvideo.ViewHolder.StaffViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder>  {
    private Context context;
    private ArrayList<Request> requests;
    public static final int MSG_LEFT = 0;
    public DatabaseReference courseRef;
    public FirebaseDatabase database;
    private String userId;
    public static final int MSG_RIGHT = 1;
    private String courseId;
    //private String imgUrl;
    public StaffAdapter(Context context, ArrayList<Request> requests, String courseId,String userId) {
        this.context = context;
        this.requests = requests;
        this.courseId=courseId;
        this.userId=userId;
    }

    public StaffAdapter() {

    }

    @NonNull
    @Override
    public StaffAdapter.StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_course_layout, parent, false);
        StaffViewHolder holder = new StaffViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        holder.txtName.setText(requests.get(position).getName());
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(requests.get(position).courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course=dataSnapshot.getValue(Course.class);
                holder.txtCourseName.setText(course.getCourseName());
                holder.txtSchedule.setText(course.getSchedule());
                holder.txtDescript.setText(course.getDescript());
                loadTutor(course.getTutorPhone(),holder);
                onClickItem(course, holder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadTutor(String tutorPhone, StaffViewHolder viewHolder){
        DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.child(tutorPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                viewHolder.txtName.setText(tutor.getUsername());
                viewHolder.txtEmail.setText(tutor.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void onClickItem(Course course, StaffViewHolder viewHolder) {
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent=new Intent(context, TutorDetailAcitivity.class);
                String tutorID=course.getTutorPhone();
                String userID=userId;
                ArrayList<String> listIntent=new ArrayList<>();
                listIntent.add(tutorID);
                listIntent.add(userID);
                intent.putStringArrayListExtra("ChatID",listIntent);
                //intent.putExtra("tutorID",adapter.getRef(position).getKey());
                context.startActivity(intent);                    }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }


    public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        public TextView txtName,txtCourseName,txtDescript,txtEmail,txtSchedule;
        private ItemClickListener itemClickListener;

        public StaffViewHolder(View itemView) {
            super(itemView);
            txtName=(TextView)itemView.findViewById(R.id.txtUserNameMyCourse);
            txtEmail=(TextView)itemView.findViewById(R.id.txtEmailMyCourse);
            txtCourseName=(TextView)itemView.findViewById(R.id.txtTitleMyCourse);
            txtDescript=(TextView)itemView.findViewById(R.id.txtCourseDescriptMyCourse);
            txtSchedule=(TextView)itemView.findViewById(R.id.txtScheduleMyCourse);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select this action");
            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);

        }

    }

}

