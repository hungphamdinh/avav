package com.example.callvideo.Presenter.LoadDetailMyCourse;

import com.example.callvideo.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoadMyCourseListener {
    void onLoadTutorMyCourse(HashMap<String,Object>tutorMap);
    void onLoadDocMyCourse(ArrayList<Doc> docList);
    void onLoadTutorTest(ArrayList<Doc>docArrayList,ArrayList<String>key);
    void offlineStatus(String msg);
    void onError(String msg);
    void onlineStatus(String msg);
    void updateToken(String msg);
}
