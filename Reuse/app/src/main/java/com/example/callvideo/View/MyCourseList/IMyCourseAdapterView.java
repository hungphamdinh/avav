package com.example.callvideo.View.MyCourseList;

import com.example.callvideo.Model.Entities.Request;

import java.util.ArrayList;

public interface IMyCourseAdapterView {
    void callAdapter(ArrayList<Request>requestArrayList);
    void onErrorLoadCourse(String msg);
}
