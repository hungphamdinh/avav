package com.example.callvideo.View.LoadDetailMyCourse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.callvideo.Adapter.DocAdapter;
import com.example.callvideo.Adapter.TestAdapter;
import com.example.callvideo.Adapter.WordFavAdapter;
import com.example.callvideo.Sinch.BaseActivity;
import com.example.callvideo.Sinch.CallScreenActivity;
import com.example.callvideo.Common.Common;
import com.example.callvideo.View.Chat.MainChatActivity;
import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.Presenter.LoadDetailMyCourse.DetailMyCoursePresenter;
import com.example.callvideo.R;
import com.example.callvideo.Sinch.SinchService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.calling.Call;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorDetailAcitivity extends BaseActivity implements ILoadDetailMyCourseView {
    private TextView txtUsername,txtTitle,txtStatus,txtEmail,txtExp,txtCourseDoc;
    private String tutorId;
    private String userId;
    private String courseId;
    private CircleImageView profileImage,imgStatus;
    private Button btnChat;
    private Button btnCall;
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DocAdapter docAdapter;
    private RecyclerView testList;
    private TestAdapter testAdapter;
    private ArrayList<String> listChatID;
    private DetailMyCoursePresenter detailMyCoursePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail_acitivity);
        txtUsername=(TextView)findViewById(R.id.txtUserNameTutor);
        txtEmail=(TextView)findViewById(R.id.txtEmailTutor);
        txtTitle=(TextView)findViewById(R.id.txtTitleTutorDetail);
        txtStatus=(TextView)findViewById(R.id.txtTutorStatusDe);
        txtExp=(TextView)findViewById(R.id.txtExpTutor);
        imgStatus=(CircleImageView)findViewById(R.id.imgStatusTutorDe);
        recyclerMenu = (RecyclerView) findViewById(R.id.listDoc);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        profileImage=(CircleImageView)findViewById(R.id.imgProfileDetail);
        testList=(RecyclerView)findViewById(R.id.listTest);
        testList.setHasFixedSize(true);
        testList.setLayoutManager(new LinearLayoutManager(this));
        btnChat=(Button)findViewById(R.id.btnMessage);
        btnCall=(Button)findViewById(R.id.btnCallTutor);
        detailMyCoursePresenter=new DetailMyCoursePresenter(this);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                tutorId=listChatID.get(0);
                userId=listChatID.get(1);
                courseId=listChatID.get(2);
                detailMyCoursePresenter.loadDetailMyCourse(tutorId,courseId);
                detailMyCoursePresenter.setToken(FirebaseInstanceId.getInstance().getToken(),userId);
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

    @Override
    protected void onServiceConnected() {

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
        //detailMyCoursePresenter.setStatus("online",userId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDisplayTutor(HashMap<String, Object> map) {
                txtTitle.setText(map.get("title").toString());
                txtUsername.setText(map.get("tutorName").toString());
                txtEmail.setText(map.get("tutorEmail").toString());
                txtExp.setText(map.get("tutorExp").toString());
                Glide.with(getApplicationContext())
            .load(map.get("tutorImage").toString())
            .centerCrop()
                        .into(profileImage);
    }

    @Override
    public void onDisplayDoc(ArrayList<Doc> docList) {
        docAdapter=new DocAdapter(TutorDetailAcitivity.this,docList);
        docAdapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(docAdapter);
    }

    @Override
    public void onDisplayTutorTest(ArrayList<Doc> docArrayList, ArrayList<String> docKey) {
        testAdapter=new TestAdapter(TutorDetailAcitivity.this,docArrayList,docKey);
        testAdapter.notifyDataSetChanged();
        if(docArrayList==null){
            testList.setVisibility(View.INVISIBLE);
        }
        testList.setAdapter(testAdapter);
    }

    @Override
    public void onDisplayOnline(String msg) {
        txtStatus.setText(msg);
        txtStatus.setTextColor(Color.parseColor("#00FF00"));
        imgStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(TutorDetailAcitivity.this,msg,Toast.LENGTH_SHORT).show();
        //testList.setVisibility(View.GONE);
    }

    @Override
    public void onDisplayOffline(String msg) {
        txtStatus.setTextColor(Color.parseColor("#FF0000"));
        txtStatus.setText(msg);
        imgStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUpdateToken(String msg) {
        Toast.makeText(TutorDetailAcitivity.this,msg,Toast.LENGTH_SHORT).show();

    }
}
