package com.example.callvideo.Model.MyAccount;

import java.util.HashMap;

public interface MyAccountListener {
    void updateInform(HashMap<String,Object>informMap);
    void onSuccess(String msg);
    void onError(String msg);
    void onLoading(int percent);
}
