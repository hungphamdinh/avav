package com.example.callvideo.Presenter.Cart;

import java.util.HashMap;

public interface ICartListener {
    void onSuccess(String msg);
    void onError(String msg);
    void onDisplayCourse(HashMap<String,Object>map);
}
