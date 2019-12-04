package com.example.callvideo.View.SignUp;

import android.app.Activity;
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

public class SignUpActivity extends AppCompatActivity implements ISignUpView {
    private EditText edtPhone, password, edtUsername;
    private Button btnSignUp;
    private EditText emailId;
    private String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
    private ISignUpPresenter signUpPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtUsername = findViewById(R.id.edtUsernameSignUp);
        edtPhone = findViewById(R.id.edtPhoneNumberSignUp);
        password = findViewById(R.id.edtPasswordSignUp);
        emailId = findViewById(R.id.edtEmailSignUp);
        btnSignUp = findViewById(R.id.btnSignUpSignIn);
        ArrayList<String> list=new ArrayList<>();

        signUpPresenter=new SignUpPresenter(this,this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list.add(edtUsername.getText().toString());
                list.add(edtPhone.getText().toString());
                list.add(password.getText().toString());
                list.add(emailId.getText().toString());
                signUpPresenter.onSignUp(list);
            }
        });
        setupUI(findViewById(R.id.parentSignUp));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");
        Firebase.setAndroidContext(SignUpActivity.this);


    }
//
//    public void SignUp(final DatabaseReference table_user) {
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View view) {
//                if (Common.isConnectedToInternet(getBaseContext())) {
//                    final ProgressDialog progress = new ProgressDialog(SignUpActivity.this);
//                    progress.setTitle("Loading");
//                    progress.setMessage("Wait while loading...");
//                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//                    progress.show();
//                    String usernameTemp = edtUsername.getText().toString();
//                    String passwordTemp = password.getText().toString();
//                    String emailTemp = emailId.getText().toString();
//                    table_user.orderByChild("email").equalTo(emailTemp).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                        //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
//                                    if (usernameTemp.equals("") || passwordTemp.equals("") || edtPhone.getText().toString().equals("") || emailId.getText().toString().isEmpty()) {
//                                        progress.dismiss();
//                                        Toast.makeText(SignUpActivity.this, "Please check your username. phone, password and email", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        if (emailTemp.trim().matches(emailPattern)) {
//                                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
//                                                progress.dismiss();
//                                                Toast.makeText(SignUpActivity.this, "This phone number is exist", Toast.LENGTH_SHORT).show();
//                                            }
//                                            if(dataSnapshot.exists()){
//                                                progress.dismiss();
//                                                Toast.makeText(SignUpActivity.this, "This email is exist, please check your email", Toast.LENGTH_SHORT).show();                                            }
//                                            else {
//                                                progress.dismiss();
//                                                HashMap<String, String> map = new HashMap<>();
//                                                //User user = new User(usernameTemp, passwordTemp,"");
//                                                map.put("username", usernameTemp);
//                                                map.put("password", passwordTemp);
//                                                map.put("email", emailTemp);//
//                                                map.put("status","offline");
//                                                map.put("avatar","default");
//                                                table_user.child(edtPhone.getText().toString()).setValue(map);
//                                                Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
//                                                //finish();
//
//
//                                            }
//                                        } else {
//                                            progress.dismiss();
//                                            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    }
//                          //      }
//                            }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                } else {
//                    Toast.makeText(SignUpActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        });
//    }

    @Override
    public void onSignUpSuccess(String msg) {
        Toast.makeText(SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpFailed(String msg) {
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
}
