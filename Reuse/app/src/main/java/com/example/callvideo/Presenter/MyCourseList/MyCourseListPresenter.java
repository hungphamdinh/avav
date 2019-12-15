package com.example.callvideo.Presenter.MyCourseList;

import com.example.callvideo.Adapter.StaffAdapter;
import com.example.callvideo.Model.Entities.Request;
import com.example.callvideo.View.MyCourseList.IMyListCourseView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCourseListPresenter implements IMyCourseListListener {
    private MyCourseList mainInterator;
    private IMyListCourseView courseView;
    private StaffAdapter.StaffViewHolder holder;
    private ArrayList<Request>requests;
    public MyCourseListPresenter(IMyListCourseView courseView, StaffAdapter.StaffViewHolder holder,ArrayList<Request>requests){
        this.requests=requests;
        mainInterator=new MyCourseList(this,requests);
        this.courseView=courseView;
        this.holder=holder;
    }
    public void setCourseList(int pos,String userId){
        HashMap<String,Object>posMap=new HashMap<>();
        posMap.put("pos",pos);
        posMap.put("userId",userId);
        mainInterator.loadCourse(posMap);
    }
    @Override
    public void onLoadTutorMyCourse(HashMap<String, Object> tutorMap) {
        courseView.onDisplayTutor(tutorMap,holder);
    }

    @Override
    public void onLoadCourseMyCourse(HashMap<String, Object> courseMap) {
        courseView.onDisplayCourse(courseMap,holder);
    }

    @Override
    public void onErrorLoadData(String msg) {
        courseView.onDisplayError(msg,holder);
    }

    @Override
    public void offlineStatus(String msg) {
        courseView.onDisplayOffline(msg,holder);
    }

    @Override
    public void onlineStatus(String msg) {
        courseView.onDisplayOnline(msg,holder);

    }

    @Override
    public void onLoadDataToClick(ArrayList<String> list) {
        courseView.onLoadDataToClick(list,holder);
    }
}
