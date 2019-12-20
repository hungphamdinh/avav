package com.example.callvideo.Presenter.SignUp;

import com.example.callvideo.View.SignUp.ISignUpView;

import java.util.HashMap;

public class SignUpPresenter implements ISignUpListener {
    private ISignUpView signUpView;
    private UserSignUp userSignUp;
    public SignUpPresenter(ISignUpView signUpView){
        this.signUpView=signUpView;
        this.userSignUp=new UserSignUp(this);
    }
    public void onSignUp(HashMap<String,Object>edt,String token){

        userSignUp.isValidData(edt,token);
    }

    @Override
    public void onSuccess(String msg) {
        signUpView.onSignUpSuccess(msg);
    }

    @Override
    public void onError(String msg) {
        signUpView.onSignUpFailed(msg);
    }
}
