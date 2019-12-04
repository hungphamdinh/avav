package com.example.callvideo.Model.LoadCourse;

import java.util.HashMap;

public interface ILoadCourseListener {
    void onLoadDataCourse(HashMap<String,Object> courseMap);
    void onLoadDataTutor(HashMap<String,Object>tutorMap);
    void onLoadDataFailer(String msg);
}
