package com.example.callvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Tutor;
import com.example.callvideo.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TutorDetailAcitivity extends AppCompatActivity {
    private DatabaseReference tutor;
    private FirebaseDatabase database;
    private TextView txtUsername,txtTitle,txtCountry,txtEmail,txtExp;
    private String tutorId;
    private String userId;
    private Button btnChat;
    private ArrayList<String> listChatID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail_acitivity);
        database=FirebaseDatabase.getInstance();
        tutor=database.getReference("Tutor");
        txtUsername=findViewById(R.id.txtUserNameTutor);
        txtEmail=findViewById(R.id.txtEmailTutor);
        txtTitle=findViewById(R.id.txtTitleTutorDetail);
        txtCountry=findViewById(R.id.txtCountryTutor);
        txtExp=findViewById(R.id.txtExpTutor);
        btnChat=findViewById(R.id.btnMessage);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                tutorId=listChatID.get(0);
                userId=listChatID.get(1);
                getDetailTutor(tutorId);
            } else {
                Toast.makeText(TutorDetailAcitivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TutorDetailAcitivity.this,MainChatActivity.class);
                intent.putStringArrayListExtra("ChatID",listChatID);
                startActivity(intent);
            }
        });
    }
    private void getDetailTutor(String tutorId) {
        tutor.child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
//                Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
//                collapsingToolbarLayout.setTitle(curentFood.getName());
                txtTitle.setText(tutor.getUsername());
                txtUsername.setText(tutor.getUsername());
                txtEmail.setText(tutor.getEmail());
                txtExp.setText(tutor.getExperience());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
