package com.example.callvideo.Presenter.MyCourseList;

import com.example.callvideo.Model.Entities.Request;

import java.util.ArrayList;

public interface IMyCourseListAdaperListener {
    void callAdapter(ArrayList<Request>requestList);
    void onErrorLoadCourse(String msg);
}
