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

import com.example.callvideo.Presenter.SignUp.ISignUpPresenter;
import com.example.callvideo.Presenter.SignUp.SignUpPresenter;
import com.example.callvideo.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements ISignUpView {
    private EditText edtPhone, password, edtUsername;
    private Button btnSignUp;
    private EditText emailId;
    private ISignUpPresenter signUpPresenter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtUsername = findViewById(R.id.edtUsernameSignUp);
        edtPhone = findViewById(R.id.edtPhoneNumberSignUp);
        password = findViewById(R.id.edtPasswordSignUp);
        emailId = findViewById(R.id.edtEmailSignUp);
        btnSignUp = findViewById(R.id.btnSignUpSignIn);

        signUpPresenter=new SignUpPresenter(this,this);
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

                signUpPresenter.onSignUp(edt);
            }
        });
        setupUI(findViewById(R.id.parentSignUp));

    }

    @Override
    public void onSignUpSuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpFailed(String msg) {
        progressDialog.cancel();
        Toast.makeText(SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SignUpActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
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
