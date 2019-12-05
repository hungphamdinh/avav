package com.example.callvideo.Presenter.Login;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.callvideo.Model.Login.IUserLoginListener;
import com.example.callvideo.Model.Login.UserLogin;
import com.example.callvideo.View.Login.ILoginView;

public class LoginPresenter implements  ILoginPresenter, IUserLoginListener {
    private ILoginView loginView;
    private Context context;
    private ProgressDialog progressDialog;
    private UserLogin mainLogin;
    public LoginPresenter(ILoginView loginView,Context context){
        this.loginView=loginView;
        this.context=context;
        mainLogin=new UserLogin(this,context);
    }

    @Override
    public void onLogin(String phone,String password) {
            //User user = new User();
        progressDialog=getProgressDialog();
            mainLogin.isValidData(phone,password);

    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
    }

    @Override
    public void onLoginSucess(String status) {
        progressDialog.cancel();
        loginView.setDisplaySuccess(status);
    }

    @Override
    public void onLoginError(String status) {
        progressDialog.cancel();
        loginView.setDisplayError(status);
    }
}
