package com.example.callvideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.User;
import com.example.callvideo.SQliteDatabase.BaseResipistory;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import java.io.IOException;

import io.paperdb.Paper;


public class LoginActivity2 extends AppCompatActivity {
    private EditText edntPhone,password;
    private Button login;
    private TextView txtSignUp;
    private Cursor c=null;
    private DatabaseReference table_user=null;
    private FirebaseDatabase firebaseDatabase=null;
    private CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        edntPhone = (EditText)findViewById(R.id.edtPhoneLogin);
        password= (EditText)findViewById(R.id.edtPassword);
        ckbRemember=(CheckBox) findViewById(R.id.ckbRememberUser);
        login= (Button)findViewById(R.id.btnLogin);
        txtSignUp=(TextView)findViewById(R.id.txtSignUpNewAc);
        setupUI(findViewById(R.id.parent));
        firebaseDatabase=FirebaseDatabase.getInstance();
        table_user=firebaseDatabase.getReference("User");
        Firebase.setAndroidContext(LoginActivity2.this);
        SignUp();
      ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
//        readFromAssets();
        Paper.init(this);
        Login(table_user);
//        String userRemember=Paper.book().read(Common.USER_KEY);
//        String passRemember=Paper.book().read(Common.PWD_KEY);
//        if(userRemember!=null&&passRemember!=null) {
//            if (!userRemember.isEmpty() && !passRemember.isEmpty())
//                loginRemember(userRemember, passRemember);
//        }
        readFromAssets();
    }

    private void loginRemember(final String userRemember, final String passRemember) {
        if (Common.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog progress = new ProgressDialog(LoginActivity2.this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (userRemember.equals("") || passRemember.equals("")) {
                        progress.dismiss();
                        Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dataSnapshot.child(userRemember).exists()) {
                            User uUser = dataSnapshot.child(userRemember).getValue(User.class);
                            uUser.setEmail(userRemember);
                            if (uUser.getPassword().equals(passRemember)) {
                                progress.dismiss();
                                Intent intent = new Intent(LoginActivity2.this, Home2Activity.class);
                                intent.putExtra("phoneUser",userRemember);
//                                Intent intentPhoneNumber=new Intent(LoginActivity2.this,MyAccountActivity.class);
//                                intentPhoneNumber.putExtra("phoneNum",userRemember);
                                Common.currentUser = uUser;
                                startActivity(intent);
                                finish();
                            } else {
                                progress.dismiss();
                                Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                                //  edntPhone.setText(uUser.getPassword());
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(LoginActivity2.this, "This account is not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(LoginActivity2.this,"Check your connection",Toast.LENGTH_SHORT).show();
            return;
        }
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


    public void Login(final DatabaseReference table_user) {
        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    if(ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY, edntPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY,password.getText().toString());
                    }
                    final ProgressDialog progress = new ProgressDialog(LoginActivity2.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                    final String usernameTemp = edntPhone.getText().toString();
                    final String passwordTemp = password.getText().toString();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (usernameTemp.equals("") || passwordTemp.equals("")) {
                                progress.dismiss();
                                Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                            } else {
                                if (dataSnapshot.child(edntPhone.getText().toString()).exists()) {
                                    User uUser = dataSnapshot.child(edntPhone.getText().toString()).getValue(User.class);
                                    uUser.setPhone(edntPhone.getText().toString());
                                    if (uUser.getPassword().equals(password.getText().toString())) {
                                        progress.dismiss();

                                        Intent intent = new Intent(LoginActivity2.this, Home2Activity.class);
                                        intent.putExtra("phoneUser",usernameTemp);

                                        Common.currentUser = uUser;
                                 //       Intent intentPhoneNumber=new Intent(LoginActivity2.this,MyAccountActivity.class);
                                 //       intentPhoneNumber.putExtra("phoneNum",usernameTemp);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(LoginActivity2.this,"Log in successfully",Toast.LENGTH_LONG).show();
                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                                        //  edntPhone.setText(uUser.getPassword());
                                    }
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(LoginActivity2.this, "This account is not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity2.this,"Check your connection",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
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
                    hideSoftKeyboard(LoginActivity2.this);
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
    private void readFromAssets() {
        BaseResipistory myDbHelper = new BaseResipistory(LoginActivity2.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();
        //     Toast.makeText(LoginActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();
        c = myDbHelper.query("OrderDetail", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
//                Toast.makeText(LoginActivity.this,
//                        "ID"+c.getInt(0)+"\n"+
//                                "ItemId: " + c.getString(1) + "\n" +
//                                "ItemName: " + c.getString(2) + "\n" +
//                                "Quantity: " + c.getString(3) + "\n" +
//                                "Price:  " + c.getString(4)+"\n"+
//                                "Discount: "+c.getString(5),
//                        Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
        }
    }
}
