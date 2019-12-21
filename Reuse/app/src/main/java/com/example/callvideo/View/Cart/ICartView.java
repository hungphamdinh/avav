package com.example.callvideo.View.Cart;

import java.util.HashMap;

public interface ICartView {
    void onDisplaySuccess(String msg);
    void onDisplayError(String msg);
    void onDisplayCourse(HashMap<String,Object>map);
}
