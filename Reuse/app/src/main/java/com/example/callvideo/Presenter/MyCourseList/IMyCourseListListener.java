package com.example.callvideo.Presenter.MyCourseList;

import com.example.callvideo.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMyCourseListListener {
    void onLoadTutorMyCourse(HashMap<String,Object> tutorMap);
    void onLoadCourseMyCourse(HashMap<String,Object>courseMap);
    void onErrorLoadData(String msg);
    void offlineStatus(String msg);
    void onlineStatus(String msg);
    void onLoadDataToClick(ArrayList<String>list);
}
