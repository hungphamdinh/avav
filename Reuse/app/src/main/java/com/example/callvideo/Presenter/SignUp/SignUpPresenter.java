package com.example.callvideo.Presenter.SignUp;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.SignUp.ISignUpListener;
import com.example.callvideo.Model.SignUp.UserSignUp;
import com.example.callvideo.View.SignUp.ISignUpView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpPresenter implements ISignUpPresenter, ISignUpListener {
    private ISignUpView signUpView;
    private Context context;
    private UserSignUp userSignUp;
    public SignUpPresenter(ISignUpView signUpView,Context context){
        this.signUpView=signUpView;
        this.context=context;
        this.userSignUp=new UserSignUp(this,context);
    }
    @Override
    public void onSignUp(HashMap<String,Object>edt){
        userSignUp.isValidData(edt);
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
