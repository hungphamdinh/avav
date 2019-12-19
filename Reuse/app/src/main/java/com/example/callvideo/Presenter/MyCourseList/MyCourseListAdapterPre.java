package com.example.callvideo.Presenter.MyCourseList;

import com.example.callvideo.Model.Entities.Request;
import com.example.callvideo.View.MyCourseList.IMyCourseAdapterView;

import java.util.ArrayList;

public class MyCourseListAdapterPre implements IMyCourseListAdaperListener {
    private IMyCourseAdapterView myCourseAdapterView;
    private MyCourseListCallAdapter mainInterator;
    public MyCourseListAdapterPre(IMyCourseAdapterView myCourseAdapterView){
        this.myCourseAdapterView=myCourseAdapterView;
        this.mainInterator=new MyCourseListCallAdapter(this);
    }
    public void setAdapter(String userPhone){
        //ArrayList<Request>requests=new ArrayList<>();
        mainInterator.loadTutor(userPhone);
    }
    @Override
    public void callAdapter(ArrayList<Request> requestList) {
        myCourseAdapterView.callAdapter(requestList);
    }

    @Override
    public void onErrorLoadCourse(String msg) {
        myCourseAdapterView.onErrorLoadCourse(msg);
    }
}
