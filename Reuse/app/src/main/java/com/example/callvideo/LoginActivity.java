package com.example.callvideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.SinchError;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button mLoginButton;
    private EditText mLoginName;
    private ProgressDialog mSpinner;
    private DatabaseReference table_user=null;
    private FirebaseDatabase firebaseDatabase=null;
    private User userAccount;
    private String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        firebaseDatabase= FirebaseDatabase.getInstance();
        table_user=firebaseDatabase.getReference("User");
        mLoginName = (EditText) findViewById(R.id.loginName);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setEnabled(false);
        if(getIntent()!=null)
            userPhone =getIntent().getStringExtra("userPhone");
        if(!userPhone.isEmpty()&& userPhone !=null){
            if (Common.isConnectedToInternet(this)) {
                //  setStatus("online",userPhone);
                getDataFromAc(userPhone);
            }
            else {
                Toast.makeText(LoginActivity.this,"Check your connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked() {
        String userName = mLoginName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userName.equals(getSinchServiceInterface().getUserName())) {
            getSinchServiceInterface().stopClient();
        }
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
    private void getDataFromAc(String phoneNum) {
        table_user.child(phoneNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
            //    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    mLoginName.setText(user.getUsername());
            //    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

