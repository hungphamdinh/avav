package com.example.callvideo.Model.SignUp;

public interface ISignUp {
    int onSignUp(boolean checkPhone,boolean checkMail);
    void setValueToFirebase();
}
