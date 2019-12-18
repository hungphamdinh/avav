package com.example.callvideo.Presenter.SignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserSignUp  {
    private ISignUpListener signUpListener;
    public UserSignUp(ISignUpListener signUpListener){
        this.signUpListener=signUpListener;
    }
    public void isValidData(HashMap<String,Object>editText){
    DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("User");
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                String userName = editText.get("userName").toString();
                String pass = editText.get("pass").toString();
                String phone = editText.get("phone").toString();
                String email = editText.get("email").toString();
                String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
                if (userName.equals("") || pass.equals("") || phone.equals("") || email.equals("")) {
                    signUpListener.onError("Please check your inform");
                } else {
                    boolean check = dataSnapshot.child(phone).exists();
                    if (check == false) {
                        DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("User");
                        emailRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (email.trim().matches(emailPattern)) {
                                    if (check == true) {
                                        //progress.dismiss();
                                        signUpListener.onError("Số điện thoại này đã tồn tại");
                                    } else if (snapshot.exists()) {
                                        signUpListener.onError("Email đã tồn tại");
                                    } else {
                                        DatabaseReference table_user = FirebaseDatabase.getInstance().getReference("User");
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("username", userName);
                                        map.put("password", pass);
                                        map.put("email", email);//
                                        map.put("status", "offline");
                                        map.put("avatar", "default");
                                        table_user.child(phone).setValue(map);
                                        signUpListener.onSuccess("Đăng ký thành công");
                                    }
                                } else {
                                    signUpListener.onError("Email không hợp lệ");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else {
                        signUpListener.onError("This phone is exist");
                    }
                }
            }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}




}
