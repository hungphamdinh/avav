package com.example.callvideo.Model.User.SignUp;

public interface ISignUp {
    int onSignUp(boolean checkPhone,boolean checkMail);
    void setValueToFirebase();
}
