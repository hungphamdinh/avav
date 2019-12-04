package com.example.callvideo.Presenter.SignUp;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.User.SignUp.UserSignUp;
import com.example.callvideo.View.SignUp.ISignUpView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUpPresenter implements ISignUpPresenter {
    private ISignUpView signUpView;
    private Context context;
    public SignUpPresenter(ISignUpView signUpView,Context context){
        this.signUpView=signUpView;
        this.context=context;
    }
    @Override
    public void onSignUp(ArrayList<String>list){
        String usernameTemp=list.get(0);
        String phone=list.get(1);
        String passwordTemp=list.get(2);
        String emailTemp=list.get(3);
        UserSignUp userSignUp=new UserSignUp();
        userSignUp.setUsername(usernameTemp);
        userSignUp.setPhone(phone);
        userSignUp.setEmail(emailTemp);
        userSignUp.setPassword(passwordTemp);
        DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("User");
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                if (Common.isConnectedToInternet(context.getApplicationContext())) {

                    final ProgressDialog progress = new ProgressDialog(context);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                    boolean check = dataSnapshot.child(phone).exists();
                    if (check == false) {
                        DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("User");
                        emailRef.orderByChild("email").equalTo(emailTemp).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                int codeSignUpNot = userSignUp.onSignUp(false, snapshot.exists());
                                if (codeSignUpNot == 2) {
                                    progress.cancel();
                                    signUpView.onSignUpFailed("This email is exist");
                                } else {
                                    int codeSignUpEmail = userSignUp.onSignUp(false, false);
                                    progress.cancel();
                                    if (codeSignUpEmail == 0) {
                                        signUpView.onSignUpFailed("Please check your username. phone, password and email");
                                    } else if (codeSignUpEmail == 4) {
                                        signUpView.onSignUpFailed("Invalid email");
                                    } else {
                                        signUpView.onSignUpSuccess("Sign up success");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else {
                        int codeSignUpNot = userSignUp.onSignUp(true, dataSnapshot.exists());
                        if (codeSignUpNot == 1) {
                            progress.cancel();
                            signUpView.onSignUpFailed("This phone is exist");
                        }
                    }
                }
                else {
                    signUpView.onSignUpFailed("Please check your connected");
                }

                //      }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
