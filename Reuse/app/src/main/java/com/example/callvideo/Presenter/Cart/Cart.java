package com.example.callvideo.Presenter.Cart;

import com.example.callvideo.Model.Entities.Course;
import com.example.callvideo.Model.Entities.User;
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

public class Cart {
    private ICartListener iCartListener;
    public Cart(ICartListener iCartListener){
        this.iCartListener=iCartListener;
    }
    public void onPurchased(HashMap<String,Object>inputMap){
        DatabaseReference requestReference=FirebaseDatabase.getInstance().getReference("Requests");
        String courseId=inputMap.get("courseId").toString();
        String userPhone=inputMap.get("userPhone").toString();
        String price=inputMap.get("price").toString();
        String courseName=inputMap.get("courseName").toString();
        HashMap<String,Object>map=new HashMap<>();
        map.put("phone",userPhone);
        map.put("courseId",courseId);
        map.put("status",1);
        map.put("total",price);
        map.put("courseName",courseName);
        requestReference.push().setValue(map);
        iCartListener.onSuccess("Chúc bạn học thật tốt");
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("User");
        userRef.child(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                String userName=user.getUsername();
                HashMap<String,Object>dataNoti=new HashMap<>();
                dataNoti.put("reciever","0772223398");
                dataNoti.put("sender",userPhone);
                dataNoti.put("username","Học viên "+userName+" vừa mua khóa học");
                dataNoti.put("msg",courseName);
                checkStatusCourse(courseId);
                sendNotification(dataNoti);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void loadListCourse(HashMap<String,Object>edtMap) {
        String courseId=edtMap.get("courseId").toString();
//        String courseName=edtMap.get("courseName").toString();
//        String price=edtMap.get("price").toString();
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object>map=new HashMap<>();
                Course course=dataSnapshot.getValue(Course.class);
               map.put("courseName",course.getCourseName());
               map.put("price",course.getPrice());
               iCartListener.onDisplayCourse(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void sendNotification(HashMap<String,Object> listChat) {
        APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        String reciever=listChat.get("reciever").toString();
        String sender=listChat.get("sender").toString();
        //      int ckAccount= (int) listChat.get("ckAccount");
        String userName=listChat.get("username").toString();
        String msg=listChat.get("msg").toString();
        DatabaseReference tokenRef= FirebaseDatabase.getInstance().getReference("Tokens");
        tokenRef.orderByKey().equalTo(reciever).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Token token=childSnap.getValue(Token.class);
                    Data data=new Data(sender, R.mipmap.ic_launcher,userName+": "+msg,"Cập nhật khóa học",
                            reciever);
                    Sender send=new Sender(data,token.getToken());
                    apiService.sendNotification(send)
                            .enqueue(new Callback<MyRespone>() {
                                @Override
                                public void onResponse(Call<MyRespone> call, Response<MyRespone> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            iCartListener.onError("Failed");
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
    public void checkStatusCourse(String courseId) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("Course");
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",2);
        courseRef.child(courseId).updateChildren(map);
    }
}
