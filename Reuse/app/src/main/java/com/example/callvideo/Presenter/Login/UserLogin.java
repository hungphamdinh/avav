package com.example.callvideo.Presenter.Login;

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
    public UserLogin(IUserLoginListener userLoginListener){
        this.userLoginListener=userLoginListener;
    }
    public void isValidData(String phone,String password) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (phone.equals("") || password.equals("")) {
                        userLoginListener.onLoginError("Vui lòng kiểm tra lại thông tin");
                    } else {
                        if (dataSnapshot.child(phone).exists()) {
                            User uUser = dataSnapshot.child(phone).getValue(User.class);
                            uUser.setPhone(phone);
                            //uUser.setUsername(uUser.getUsername());
                            if (password.equals(uUser.getPassword())&&uUser.getCkAccount()==1) {
                                Common.currentUser = uUser;
                                userLoginListener.onLoginSucess("Đăng nhập thành công");
                            }
                            else if(uUser.getCkAccount()!=1){
                                userLoginListener.onLoginError("Tài khoản của bạn hiện chưa được xác thực");
                            }
                            else {
                                userLoginListener.onLoginError("Vui lòng kiểm tra lại mật khẩu");
                            }

                        } else {
                            userLoginListener.onLoginError("Số điện thoại này không tồn tại");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



}
