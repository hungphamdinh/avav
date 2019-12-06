package com.example.callvideo.View.LoadDetailMyCourse;

import com.example.callvideo.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoadDetailMyCourseView {
    void onDisplayTutor(HashMap<String,Object> map);
    void onDisplayDoc(ArrayList<Doc>docList);
    void onDisplayOnline(String msg);
    void onDisplayOffline(String msg);
    void onUpdateToken(String msg);
}
