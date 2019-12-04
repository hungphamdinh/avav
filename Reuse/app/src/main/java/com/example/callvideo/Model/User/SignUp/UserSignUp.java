package com.example.callvideo.Model.User.SignUp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserSignUp implements ISignUp{
    private String username;
    private String password;
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public UserSignUp(){

    }
    @Override
    public int onSignUp(boolean checkPhone, boolean checkEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
        if (getUsername().equals("") || getPassword().equals("") || getPhone().equals("") || getEmail().equals("")) {
            return 0;
          //  Toast.makeText(SignUpActivity.this, "Please check your username. phone, password and email", Toast.LENGTH_SHORT).show();
        } else {
            if (getEmail().trim().matches(emailPattern)) {
                if (checkPhone==true) {
                    //progress.dismiss();
                        return 1;
                }
                else if(checkEmail==true){
                    return 2;
                }
                else {
//                    progress.dismiss();
                    setValueToFirebase();
//                    Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                    return 3;
                    //finish();
                }
            } else {
               // progress.dismiss();
               // Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                return 4;
            }
        }
        //      }
    }

    public void setValueToFirebase() {
        DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("User");
        HashMap<String, String> map = new HashMap<>();
        //User user = new User(usernameTemp, passwordTemp,"");
        map.put("username", getUsername());
        map.put("password", getPassword());
        map.put("email", getEmail());//
        map.put("status","offline");
        map.put("avatar","default");
        table_user.child(getPhone()).setValue(map);
    }


}
