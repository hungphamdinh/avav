package com.example.callvideo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Doc;
import com.example.callvideo.Model.Tutor;
import com.example.callvideo.Notification.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorDetailAcitivity extends BaseActivity {
    private DatabaseReference tutorRef;
    private FirebaseDatabase database;
    private TextView txtUsername,txtTitle,txtStatus,txtEmail,txtExp,txtCourseDoc;
    private String tutorId;
    private String userId;
    private String courseId;
    private CircleImageView profileImage,imgStatus;
    private Button btnChat;
    private Button btnCall;
    //private String status;
    private ArrayList<String> listChatID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail_acitivity);
        database=FirebaseDatabase.getInstance();
        tutorRef =database.getReference("Tutor");
        txtUsername=(TextView)findViewById(R.id.txtUserNameTutor);
        txtEmail=(TextView)findViewById(R.id.txtEmailTutor);
        txtTitle=(TextView)findViewById(R.id.txtTitleTutorDetail);
        txtStatus=(TextView)findViewById(R.id.txtTutorStatusDe);
        txtExp=(TextView)findViewById(R.id.txtExpTutor);
        imgStatus=(CircleImageView)findViewById(R.id.imgStatusTutorDe);

        profileImage=(CircleImageView)findViewById(R.id.imgProfileDetail);
        txtCourseDoc=(TextView)findViewById(R.id.txtCourseDocDetail);
        btnChat=(Button)findViewById(R.id.btnMessage);
        btnCall=(Button)findViewById(R.id.btnCallTutor);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                tutorId=listChatID.get(0);
                userId=listChatID.get(1);
                courseId=listChatID.get(2);
                //status=listChatID.get(3);
                getDetailTutor(tutorId);
                getCourseDoc(courseId);
                updateToken(FirebaseInstanceId.getInstance().getToken());
            } else {
                Toast.makeText(TutorDetailAcitivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        onClickChat();
        onClickCall();
    }

    private void onClickCall() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callButtonClicked(tutorId);
            }
        });
    }
    private void updateToken(String token){
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        Token newToken=new Token(token);
        tokenRef.child(userId).setValue(newToken);
    }
    private void onClickChat() {
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TutorDetailAcitivity.this, MainChatActivity.class);
                intent.putStringArrayListExtra("ChatID",listChatID);
                startActivity(intent);
            }
        });
    }

    private void callButtonClicked(String userName) {
        //String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a tutorRef to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }
    private void getCourseDoc(String courseId){
      DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
      docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
                  Doc doc = childSnap.getValue(Doc.class);
                  txtCourseDoc.setText(doc.getDocName());
                  openCourseDoc(doc);
              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
    }

    private void openCourseDoc(Doc doc) {
        txtCourseDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(doc.getDocUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    private void setStatus(String status){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("User");
        userRef.child(userId).updateChildren(map);
    }
    private void getDetailTutor(String tutorId) {
        tutorRef.child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);

//                Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
//                collapsingToolbarLayout.setTitle(curentFood.getName());
                if(tutor.getStatus().equals("offline")){
                    txtStatus.setTextColor(Color.parseColor("#FF0000"));
                    txtStatus.setText("Giảng viên hiện không hoạt động");
                    imgStatus.setVisibility(View.INVISIBLE);
                }
                else {
                    txtStatus.setText("Giảng viên hiện đang hoạt động");
                    txtStatus.setTextColor(Color.parseColor("#00FF00"));
                    imgStatus.setVisibility(View.VISIBLE);

                }
                txtTitle.setText(tutor.getUsername());
                txtUsername.setText(tutor.getUsername());
                txtEmail.setText(tutor.getEmail());
                txtExp.setText(tutor.getExperience());
                Glide.with(TutorDetailAcitivity.this)
                        .load(tutor.getAvatar())
                        .centerCrop()
                        .into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onServiceConnected() {
//        TextView userName = (TextView) findViewById(R.id.loggedInName);
//        userName.setText(getSinchServiceInterface().getUserName());
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("offline");
    }
}
