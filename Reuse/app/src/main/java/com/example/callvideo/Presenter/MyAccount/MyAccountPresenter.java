package com.example.callvideo.Presenter.MyAccount;

import android.content.Context;

import com.example.callvideo.Model.MyAccount.MyAccount;
import com.example.callvideo.Model.MyAccount.MyAccountListener;
import com.example.callvideo.View.MyAccountView.MyAccountView;

import java.util.HashMap;

public class MyAccountPresenter implements MyAccountListener,IMyAcountPresenter {
    private MyAccountView myAccountView;
    private Context context;
    private MyAccount mainInterator;
    public MyAccountPresenter(MyAccountView myAccountView,Context context){
        this.myAccountView=myAccountView;
        this.context=context;
        this.mainInterator=new MyAccount(this);
    }
    @Override
    public void loadData(String phoneKey,HashMap<String,Object>map){
        mainInterator.updateTutor(phoneKey,map);
    }
    @Override
    public void setOnClick(String phoneKey,HashMap<String,Object>map){
        mainInterator.setOnClick(phoneKey,map);
    }
    @Override
    public void updateInform(HashMap<String, Object> informMap) {
        myAccountView.updateInform(informMap);
    }

    @Override
    public void onSuccess(String msg) {
        myAccountView.onSuccess(msg);
    }

    @Override
    public void onError(String msg) {
        myAccountView.onError(msg);
    }

    @Override
    public void onLoading(int percent) {
        myAccountView.onLoading(percent);
    }
}
