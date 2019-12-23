package com.example.callvideo.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Entities.Course;
import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.Model.Entities.Request;
import com.example.callvideo.Model.Entities.Tutor;
import com.example.callvideo.Presenter.MyCourseList.MyCourseListPresenter;
import com.example.callvideo.R;
import com.example.callvideo.View.LoadDetailMyCourse.TutorDetailAcitivity;
import com.example.callvideo.View.MyCourseList.IMyListCourseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> implements IMyListCourseView {
    private Context context;
    private ArrayList<Request> requests;
    private String userId;
    private String courseId;
    private MyCourseListPresenter courseListPresenter;
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
            courseListPresenter = new MyCourseListPresenter(this, holder, requests);
            courseListPresenter.setCourseList(position, userId);
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }
    @Override
    public void onDisplayTutor(HashMap<String, Object> map, StaffViewHolder holder) {
        holder.txtName.setText(map.get("tutorName").toString());
        holder.txtEmail.setText(map.get("tutorMail").toString());
        Glide.with(context.getApplicationContext())
                .load(map.get("tutorImage"))
                .centerCrop()
                .into(holder.profileImage);
    }
    @Override
    public void onDisplayCourse(HashMap<String, Object> map, StaffViewHolder holder) {
        holder.txtCourseName.setText(map.get("courseName").toString());
        holder.txtSchedule.setText(map.get("courseSchedule").toString());
        Glide.with(context.getApplicationContext())
                .load(map.get("courseImage").toString())
                .centerCrop()
                .into(holder.imgCourse);
    }
    @Override
    public void onDisplayOnline(String msg, StaffViewHolder holder) {
        holder.txtStatus.setTextColor(Color.parseColor("#00FF00"));
        holder.txtStatus.setText(msg);
        holder.imgStatus.setVisibility(View.INVISIBLE);

    }
    @Override
    public void onDisplayOffline(String msg, StaffViewHolder holder) {
        holder.txtStatus.setTextColor(Color.parseColor("#FF0000"));
        holder.txtStatus.setText(msg);
        holder.imgStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDisplayError(String msg,StaffViewHolder holder) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        holder.itemView.setVisibility(View.GONE);
    }

    @Override
    public void onLoadDataToClick(ArrayList<String> dataList, StaffViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TutorDetailAcitivity.class);
                intent.putStringArrayListExtra("ChatID",dataList);
                context.startActivity(intent);
            }
        });

    }


    public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtName,txtCourseName,txtStatus,txtEmail,txtSchedule;
        private CircleImageView profileImage,imgStatus;
        private ImageView imgCourse;
        private ItemClickListener itemClickListener;

        public StaffViewHolder(View itemView) {
            super(itemView);
            txtName=(TextView)itemView.findViewById(R.id.txtUserNameMyCourse);
            txtEmail=(TextView)itemView.findViewById(R.id.txtEmailMyCourse);
            txtCourseName=(TextView)itemView.findViewById(R.id.txtTitleMyCourse);
            txtStatus=(TextView)itemView.findViewById(R.id.txtTutorStatus);
            txtSchedule=(TextView)itemView.findViewById(R.id.txtScheduleMyCourse);
            imgStatus=(CircleImageView)itemView.findViewById(R.id.imgStatusMyCourse);
            profileImage=(CircleImageView)itemView.findViewById(R.id.imgProfileMyCourse);
            imgCourse=(ImageView)itemView.findViewById(R.id.imgMyCourse);
            itemView.setOnClickListener(this);
       //     itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("Select this action");
//            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
//            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
//
//        }

    }

}


