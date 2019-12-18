package com.example.callvideo.View.Chat;


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
import com.example.callvideo.Model.Entities.Chat;

import com.example.callvideo.Presenter.Chat.ChatPresenter;
import com.example.callvideo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.HashMap;

public class MainChatActivity extends AppCompatActivity implements IUserChatView {
    private EditText editText;
    private TextView txtName;
    private MessageAdapter messageAdapter;
    private RecyclerView messagesView;
    private String tutorId;
    private String userId;
    private ImageButton btnSubmit;
    private ArrayList<String> listChatID;
    private ChatPresenter chatPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        editText = (EditText) findViewById(R.id.editText);
        txtName=(TextView)findViewById(R.id.txtNameChat);
        btnSubmit=(ImageButton) findViewById(R.id.btnSend);
        messagesView = (RecyclerView) findViewById(R.id.messages_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainChatActivity.this);
        messagesView.setHasFixedSize(true);
        messagesView.setLayoutManager(linearLayoutManager);
//        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        chatPresenter=new ChatPresenter(this);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                tutorId=listChatID.get(0);
                userId=listChatID.get(1);
                HashMap<String,Object>sendMap=new HashMap<>();
                HashMap<String,Object>idMap=new HashMap<>();
                idMap.put("senderId",userId);
                idMap.put("receiverId",tutorId);
                chatPresenter.loadChat(idMap);
                //accessToUser(userId,tutorId);
                onClickSend(sendMap);
            } else {
                Toast.makeText(MainChatActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void onClickSend(HashMap<String,Object>sendMap) {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMap.put("userId",userId);
                sendMap.put("tutorId",tutorId);
                sendMap.put("msg",editText.getText().toString());
                chatPresenter.clickSend(sendMap);
             //   editText.setText("");
            }

        });
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //chatPresenter.setStatus("online",userId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //chatPresenter.setStatus("offline",userId);
    }

    @Override
    public void onClickSendMsg(HashMap<String, Object> msgMap) {
        editText.setText("");
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(MainChatActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void readMsg(ArrayList<Chat> chats,ArrayList<String>keys) {
        HashMap<String,Object>idMap=new HashMap<>();
        idMap.put("userId",userId);
        idMap.put("tutorId",tutorId);
        messageAdapter=new MessageAdapter(MainChatActivity.this,chats,idMap,keys);
        messageAdapter.notifyDataSetChanged();
        messagesView.setAdapter(messageAdapter);
        messagesView.scrollToPosition(chats.size()-1);
    }

    @Override
    public void onAccesstoUser(String tutorName) {

    }

}

