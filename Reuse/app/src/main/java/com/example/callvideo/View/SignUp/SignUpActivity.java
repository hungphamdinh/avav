package com.example.callvideo.View.SignUp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.callvideo.Presenter.SignUp.SignUpPresenter;
import com.example.callvideo.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements ISignUpView {
    private EditText edtPhone, password, edtUsername,checkPass;
    private Button btnSignUp;
    private EditText emailId;
    private SignUpPresenter signUpPresenter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtUsername = findViewById(R.id.edtUsernameSignUp);
        edtPhone = findViewById(R.id.edtPhoneNumberSignUp);
        password = findViewById(R.id.edtPasswordSignUp);
        checkPass=(EditText)findViewById(R.id.edtCheckPasswordSignUp);
        emailId = findViewById(R.id.edtEmailSignUp);
        btnSignUp = findViewById(R.id.btnSignUpSignIn);

        signUpPresenter=new SignUpPresenter(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=getProgressDialog();
                HashMap<String,Object>edt=new HashMap<>();
                edt.clear();
                edt.put("userName",edtUsername.getText().toString());
                edt.put("phone",edtPhone.getText().toString());
                edt.put("pass",password.getText().toString());
                edt.put("email",emailId.getText().toString());
                edt.put("checkPass",checkPass.getText().toString());
                signUpPresenter.onSignUp(edt, FirebaseInstanceId.getInstance().getToken());
            }
        });

    }

    @Override
    public void onSignUpSuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSignUpFailed(String msg) {
        progressDialog.cancel();
        Toast.makeText(SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();

    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
    }
}
