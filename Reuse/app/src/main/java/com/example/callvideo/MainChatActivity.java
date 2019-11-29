package com.example.callvideo;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Adapter.MessageAdapter;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Chat;
import com.example.callvideo.Model.Tutor;
import com.example.callvideo.Model.User;

import com.example.callvideo.Notification.Client;
import com.example.callvideo.Notification.Data;
import com.example.callvideo.Notification.MyRespone;
import com.example.callvideo.Notification.Sender;
import com.example.callvideo.Notification.Token;
import com.example.callvideo.Service.APIService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scaledrone.lib.Scaledrone;


import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainChatActivity extends AppCompatActivity {
    private EditText editText;
    private TextView txtName;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private DatabaseReference reference;
    private RecyclerView messagesView;
    private FirebaseDatabase database;
    //private FirebaseUser fuser;
    private String tutorId;
    private String userId;
    private ImageButton btnSubmit;
    private ArrayList<Chat>chats;
    private ArrayList<String> listChatID;
    private APIService apiService;
    private boolean notify=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        //fuser= FirebaseAuth.getInstance().getCurrentUser();
        editText = (EditText) findViewById(R.id.editText);
        txtName=(TextView)findViewById(R.id.txtNameChat);
        btnSubmit=(ImageButton) findViewById(R.id.btnSend);
        //  Log.d("UserId",fuser.getUid());
        messagesView = (RecyclerView) findViewById(R.id.messages_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainChatActivity.this);
        //linearLayoutManager.setStackFromEnd(true);
        messagesView.setHasFixedSize(true);
        messagesView.setLayoutManager(linearLayoutManager);
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                tutorId=listChatID.get(0);
                userId=listChatID.get(1);
                accessToUser(userId,tutorId);
            } else {
                Toast.makeText(MainChatActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
//        if (getIntent() != null)
//            userId = getIntent().getStringExtra("userid");
        onClickSend();
        //messageAdapter = new MessageAdapter(this);

//        messagesView.setAdapter(messageAdapter);

    }

    private void accessToUser(String senderId,String reciverId) {
        database=FirebaseDatabase.getInstance();
        reference= database.getReference("Tutor");
        reference.child(reciverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                txtName.setText(tutor.getUsername());
//                if(user.getImage.equals("default")){
//
//                }
                readMessage(senderId,reciverId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickSend() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg=editText.getText().toString();
                if(!msg.equals("")) {
                    sendMessage(userId, tutorId, editText.getText().toString());
                }
                else {
                    Toast.makeText(MainChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }

        });
    }

    public void sendMessage(String sender, String reciever, String message){
        reference= FirebaseDatabase.getInstance().getReference();
        HashMap<Object,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        reference.child("Chat").push().setValue(hashMap);
        final  String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("User").child(sender);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                ArrayList<String>listChat=new ArrayList<>();
                listChat.add(reciever);
                listChat.add(sender);
                listChat.add(user.getUsername());
                listChat.add(msg);
                if(notify){
                    sendNotification(listChat);
                }
                notify=false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(ArrayList<String> listChat) {
        String reciever=listChat.get(0);
        String sender=listChat.get(1);
        String userName=listChat.get(2);
        String msg=listChat.get(3);
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        tokenRef.orderByKey().equalTo(reciever).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Token token=childSnap.getValue(Token.class);
                    Data data=new Data(sender,R.mipmap.ic_launcher,userName+": "+msg,"Tin nhắn mới",
                    reciever);
                    Sender send=new Sender(data,token.getToken());
                    apiService.sendNotification(send)
                            .enqueue(new Callback<MyRespone>() {
                                @Override
                                public void onResponse(Call<MyRespone> call, Response<MyRespone> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            Toast.makeText(MainChatActivity.this,"Failed",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void readMessage(final String myId, final String turId){
        chats=new ArrayList<>();
        DatabaseReference chatRef;
        chatRef=FirebaseDatabase.getInstance().getReference("Chat");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Chat chatItem=childSnap.getValue(Chat.class);
//                    Log.d("reciever",chatItem.getReciever());
                    if(chatItem.getReciever().equals(myId)&&chatItem.getSender().equals(turId)
                            ||chatItem.getReciever().equals(turId)&&chatItem.getSender().equals(myId)){
                        chats.add(chatItem);

//                        Log.d("Size", String.valueOf(chats.size()));
                    }
                    messageAdapter=new MessageAdapter(MainChatActivity.this,chats,myId);
                    messageAdapter.notifyDataSetChanged();
                    messagesView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

