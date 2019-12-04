package com.example.callvideo.Model.User.Login;

import com.example.callvideo.Model.User.User;

public class UserLogin implements IUserLogin{
    private String phone;
    private String passWord;

    public UserLogin(String phone,String passWord){
        this.phone=phone;
        this.passWord=passWord;
    }
    public int isValidData(String password,boolean phoneExist) {

        User user=new User(phone,password);
        if (user.getPhone().equals("") || user.getPassword().equals("")) {
            return 0;
        }
        else {
            if (phoneExist==true&&password!=null) {
//                        User uUser = dataSnapshot.child(getPhone()).getValue(User.class);
                if (password.equals(user.getPassword())) {
                    return 1;

                }
                else {
                    return 2;
                }
            }
            else {
                return 3;
            }
        }
    }

}
