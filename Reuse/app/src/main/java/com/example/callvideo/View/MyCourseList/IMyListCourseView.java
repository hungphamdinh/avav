package com.example.callvideo.View.MyCourseList;

import com.example.callvideo.Adapter.StaffAdapter;
import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.ViewHolder.StaffViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMyListCourseView {
    void onDisplayTutor(HashMap<String,Object> map, StaffAdapter.StaffViewHolder holder);
    void onDisplayCourse(HashMap<String,Object>map, StaffAdapter.StaffViewHolder holder);
    void onDisplayOnline(String msg, StaffAdapter.StaffViewHolder holder);
    void onDisplayOffline(String msg, StaffAdapter.StaffViewHolder holder);
    void onDisplayError(String msg, StaffAdapter.StaffViewHolder holder);
    void onLoadDataToClick(ArrayList<String>dataList, StaffAdapter.StaffViewHolder holder);
}
