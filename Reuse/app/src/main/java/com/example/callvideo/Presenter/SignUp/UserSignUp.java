package com.example.callvideo.Presenter.SignUp;
import com.example.callvideo.Notification.Data;
import com.example.callvideo.Notification.MyRespone;
import com.example.callvideo.Notification.Sender;
import com.example.callvideo.Notification.Token;
import com.example.callvideo.R;
import com.example.callvideo.Service.APIService;
import com.example.callvideo.Service.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignUp  {
    private ISignUpListener signUpListener;
    private boolean notify=true;
    public UserSignUp(ISignUpListener signUpListener){
        this.signUpListener=signUpListener;
    }
    public void isValidData(HashMap<String,Object>editText,String token){
    DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("User");
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                String userName = editText.get("userName").toString();
                String pass = editText.get("pass").toString();
                String phone = editText.get("phone").toString();
                String email = editText.get("email").toString();
                String checkPass=editText.get("checkPass").toString();
                String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
                if (userName.equals("") || pass.equals("") || phone.equals("")
                        || email.equals("")||checkPass.equals("")||checkPass.compareTo(pass)!=0||phone.length()!=10) {
                    signUpListener.onError("Vui lòng kiểm tra lại tài khoản của bạn");
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
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("username", userName);
                                        map.put("password", pass);
                                        map.put("email", email);//
                                        map.put("status", "offline");
                                        map.put("avatar", "default");
                                        map.put("ckAccount",0);
                                        table_user.child(phone).setValue(map);
                                        updateToken(phone,token);
                                        HashMap<String,Object> listChat=new HashMap<>();
                                        listChat.put("reciever","0772223398");
                                        listChat.put("sender",phone);
                                        listChat.put("username","Tài khoản mới: "+phone);
//                                        listChat.put("ckAccount",2);
                                        listChat.put("msg","Yêu cầu kích hoạt tài khoản");
                                        if(notify){
                                            sendNotification(listChat);
                                        }
                                        notify=false;
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
    public void updateToken(String userId,String token){
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        Token newToken=new Token(token);
        tokenRef.child(userId).setValue(newToken);
    }

    private void sendNotification(HashMap<String,Object>listChat) {
        APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        String reciever=listChat.get("reciever").toString();
        String sender=listChat.get("sender").toString();
  //      int ckAccount= (int) listChat.get("ckAccount");
        String userName=listChat.get("username").toString();
        String msg=listChat.get("msg").toString();
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        tokenRef.orderByKey().equalTo(reciever).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Token token=childSnap.getValue(Token.class);
                    Data data=new Data(sender, R.mipmap.ic_launcher,userName+": "+msg,"Thông báo",
                            reciever);
                    Sender send=new Sender(data,token.getToken());
                    apiService.sendNotification(send)
                            .enqueue(new Callback<MyRespone>() {
                                @Override
                                public void onResponse(Call<MyRespone> call, Response<MyRespone> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            signUpListener.onError("Failed");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyRespone> call, Throwable t) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
