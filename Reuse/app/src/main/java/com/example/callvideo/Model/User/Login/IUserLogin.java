package com.example.callvideo.Model.User.Login;

import com.google.firebase.database.DatabaseReference;

public interface IUserLogin {
    int isValidData(String password,boolean phoneExist);
}
