package com.example.callvideo.Presenter.LoadCourse;

import android.content.Context;

import com.example.callvideo.Model.LoadCourse.ILoadCourseListener;
import com.example.callvideo.Model.LoadCourse.LoadCourse;
import com.example.callvideo.View.LoadCourse.LoadCourseDetailView;

import java.util.HashMap;

public class DetailCoursePresenter implements IDetailCoursePresenter,ILoadCourseListener{
    private LoadCourseDetailView loadView;
    private LoadCourse mainInterator;
    private Context context;
    public DetailCoursePresenter(LoadCourseDetailView loadView, Context context){
        this.loadView=loadView;
        this.context=context;
        this.mainInterator =new LoadCourse(this);
    }
    public void loadDetailPresenter(String courseId) {
        HashMap<String, Object> courseMap = new HashMap<>();
        HashMap<String, Object> tutorMap = new HashMap<>();
        mainInterator.loadDetailCourse(courseId,courseMap, tutorMap);

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
