package com.example.callvideo.Model.MyCourseList;

import com.example.callvideo.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMyCourseListListener {
    void onLoadTutorMyCourse(HashMap<String,Object> tutorMap);
    void onLoadCourseMyCourse(HashMap<String,Object>courseMap);
    void offlineStatus(String msg);
    void onlineStatus(String msg);
    void onLoadDataToClick(ArrayList<String>list);
}
