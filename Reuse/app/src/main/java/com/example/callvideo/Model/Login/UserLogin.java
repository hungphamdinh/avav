package com.example.callvideo.Model.Login;

import android.content.Context;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLogin {
    private Context context;
    public IUserLoginListener userLoginListener;
    public UserLogin(IUserLoginListener userLoginListener,Context context){
        this.userLoginListener=userLoginListener;
        this.context=context;
    }
    public void isValidData(String phone,String password) {
        if (Common.isConnectedToInternet(context)) {

//        User user=new User(phone,password);
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (phone.equals("") || password.equals("")) {
                        userLoginListener.onLoginError("Null");
                    } else {
                        if (dataSnapshot.child(phone).exists()) {
                            User uUser = dataSnapshot.child(phone).getValue(User.class);
                            uUser.setPhone(phone);
                            //uUser.setUsername(uUser.getUsername());
                            if (password.equals(uUser.getPassword())) {
                                Common.currentUser = uUser;
                                userLoginListener.onLoginSucess("Success");
                            } else {
                                userLoginListener.onLoginError("WrongPass");
                            }

                        } else {
                            userLoginListener.onLoginError("PhoneNotExist");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            userLoginListener.onLoginError("Please check your connection");
        }
    }


}