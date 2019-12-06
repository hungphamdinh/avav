package com.example.callvideo.Presenter.Login;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.callvideo.Model.Login.IUserLoginListener;
import com.example.callvideo.Model.Login.UserLogin;
import com.example.callvideo.View.Login.ILoginView;

public class LoginPresenter implements  ILoginPresenter, IUserLoginListener {
    private ILoginView loginView;
    private Context context;
    private UserLogin mainLogin;
    public LoginPresenter(ILoginView loginView,Context context){
        this.loginView=loginView;
        this.context=context;
        mainLogin=new UserLogin(this,context);
    }

    @Override
    public void onLogin(String phone,String password) {
            //User user = new User();
        mainLogin.isValidData(phone,password);

    }


    @Override
    public void onLoginSucess(String status) {
        loginView.setDisplaySuccess(status);
    }

    @Override
    public void onLoginError(String status) {
        loginView.setDisplayError(status);
    }
}
