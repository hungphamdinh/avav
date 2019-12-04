package com.example.callvideo.Presenter.Login;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.User.User;
import com.example.callvideo.Model.User.Login.UserLogin;
import com.example.callvideo.View.Login.ILoginView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter implements  ILoginPresenter{
    ILoginView loginView;
    Context context;
    public LoginPresenter(ILoginView loginView,Context context){
        this.loginView=loginView;
        this.context=context;
    }

    @Override
    public void onLogin(String phone,String password) {
        if (Common.isConnectedToInternet(context)) {
            final ProgressDialog progress = getProgressDialog();
            //User user = new User();
            UserLogin userLogin = new UserLogin(phone, password);
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()) {
                        User uUser = dataSnapshot.child(phone).getValue(User.class);
                        int loginCode = userLogin.isValidData(uUser.getPassword(), dataSnapshot.child(phone).exists());

                        if (loginCode == 0) {
                            progress.cancel();
                            loginView.onLoginError("Please check your account");
                        } else if (loginCode == 2) {
                            progress.cancel();
                            loginView.onLoginError("Wrong password");
                        } else {
                            progress.cancel();
                            Common.currentUser = uUser;
                            loginView.onLoginSucess("Sign up Sucess");
                        }
                    } else {
                        int loginCodeNot = userLogin.isValidData("", dataSnapshot.child(phone).exists());
                        if (loginCodeNot == 3) {
                            progress.cancel();
                            loginView.onLoginError("Not exist");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            loginView.onLoginError("Please check your connection");
        }
    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
    }
}
