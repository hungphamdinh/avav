package com.example.callvideo.Model.User.Login;

import com.google.firebase.database.DatabaseReference;

public interface IUserLoginListener {
    void onLoginSucess(String status);
    void onLoginError(String status);}
