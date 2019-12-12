package com.example.callvideo.Presenter.LoadCourse;
import com.example.callvideo.View.LoadCourse.LoadCourseDetailView;

import java.util.HashMap;

public class DetailCoursePresenter implements ILoadCourseListener{
    private LoadCourseDetailView loadView;
    private LoadCourse mainInterator;
    public DetailCoursePresenter(LoadCourseDetailView loadView){
        this.loadView=loadView;
        this.mainInterator =new LoadCourse(this);
    }
    public void loadDetailPresenter(String courseId) {
        HashMap<String, Object> courseMap = new HashMap<>();
        HashMap<String, Object> tutorMap = new HashMap<>();
        mainInterator.loadDetailCourse(courseId,courseMap, tutorMap);

    }
    public void setStatus(String status,String phoneKey){
        mainInterator.setStatus(status,phoneKey);
    }

    @Override
    public void onLoadDataCourse(HashMap<String, Object> courseMap) {
        loadView.onDisplayCourse(courseMap);
    }

    @Override
    public void onLoadDataTutor(HashMap<String, Object> tutorMap) {
        loadView.onDisplayTutor(tutorMap);
    }

    @Override
    public void onLoadDataFailer(String msg) {

    }
}
