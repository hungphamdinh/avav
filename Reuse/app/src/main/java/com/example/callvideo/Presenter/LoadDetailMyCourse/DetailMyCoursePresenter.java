package com.example.callvideo.Presenter.LoadDetailMyCourse;


import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.View.LoadDetailMyCourse.ILoadDetailMyCourseView;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailMyCoursePresenter implements ILoadMyCourseListener {
    private ILoadDetailMyCourseView loadView;
    private LoadMyCourse mainInterator;
    public DetailMyCoursePresenter(ILoadDetailMyCourseView loadView){
        this.loadView=loadView;
        mainInterator=new LoadMyCourse(this);

    }
    public void loadDetailMyCourse(String tutorId,String courseId){
        HashMap<String,Object>tutorMap=new HashMap<>();
        mainInterator.getDetailTutor(tutorId,tutorMap);
        mainInterator.loadCourseDoc(courseId);
    }
    public void setToken(String token,String userId){
        mainInterator.updateToken(userId,token);
    }

    @Override
    public void onLoadTutorMyCourse(HashMap<String, Object> tutorMap) {
        loadView.onDisplayTutor(tutorMap);
    }

    @Override
    public void onLoadDocMyCourse(ArrayList<Doc> docList) {
        loadView.onDisplayDoc(docList);
    }

    @Override
    public void offlineStatus(String msg) {
        loadView.onDisplayOffline(msg);
    }

    @Override
    public void onlineStatus(String msg) {
        loadView.onDisplayOnline(msg);
    }

    @Override
    public void updateToken(String msg) {
        loadView.onUpdateToken(msg);
    }
}
