package com.example.callvideo.Presenter.MyAccount;

import java.util.HashMap;

interface IMyAcountPresenter {
    void loadData(String phoneKey, HashMap<String,Object> map);
    void setOnClick(String phoneKey,HashMap<String,Object>map);
}
