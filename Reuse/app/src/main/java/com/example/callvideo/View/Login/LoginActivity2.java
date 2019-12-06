package com.example.callvideo.View.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callvideo.Sinch.BaseActivity;
import com.example.callvideo.Home2Activity;
import com.example.callvideo.Presenter.Login.ILoginPresenter;
import com.example.callvideo.Presenter.Login.LoginPresenter;
import com.example.callvideo.R;
import com.example.callvideo.SQliteDatabase.BaseResipistory;
import com.example.callvideo.View.SignUp.SignUpActivity;
import com.example.callvideo.Sinch.SinchService;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.CheckBox;
import com.sinch.android.rtc.SinchError;

import java.io.IOException;

import io.paperdb.Paper;


public class LoginActivity2 extends BaseActivity implements SinchService.StartFailedListener, ILoginView {
    private EditText username,password;
    private Button login;
    private TextView txtSignUp;
    private Cursor c=null;
    private DatabaseReference table_user=null;
    private FirebaseDatabase firebaseDatabase=null;
    private CheckBox ckbRemember;
    private ProgressDialog progressDialog;
    private ILoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        username = (EditText)findViewById(R.id.edtPhoneLogin);
        password= (EditText)findViewById(R.id.edtPassword);
        ckbRemember=(CheckBox) findViewById(R.id.ckbRememberUser);
        login= (Button)findViewById(R.id.btnLogin);
        txtSignUp=(TextView)findViewById(R.id.txtSignUpNewAc);
       // setupUI(findViewById(R.id.parent));
        firebaseDatabase=FirebaseDatabase.getInstance();
        table_user=firebaseDatabase.getReference("User");
        Firebase.setAndroidContext(LoginActivity2.this);
        SignUp();
        Paper.init(this);
        loginPresenter=new LoginPresenter(this,this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=getProgressDialog();
                loginPresenter.onLogin(username.getText().toString(),password.getText().toString());
            }
        });
    }

    private void SignUp() {
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity2.this, SignUpActivity.class));
               // finish();
            }
        });
    }


//
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//    public void setupUI(View view) {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(LoginActivity2.this);
//                    return false;
//                }
//            });
//        }
//
////        If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }



    @Override
    protected void onServiceConnected() {
        login.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onPause();
    }
    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStarted() {
//        openPlaceCallActivity();
    }


    @Override
    public void setDisplaySuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(LoginActivity2.this,msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity2.this, Home2Activity.class);
        intent.putExtra("phoneUser",username.getText().toString());
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(username.getText().toString());
        }
        startActivity(intent);
    }

    @Override
    public void setDisplayError(String msg) {
        progressDialog.cancel();
        Toast.makeText(LoginActivity2.this,msg,Toast.LENGTH_SHORT).show();
    }
}
