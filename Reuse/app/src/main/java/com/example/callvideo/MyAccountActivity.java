package com.example.callvideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountActivity extends AppCompatActivity {
    private EditText edtPass, edtUsername, edtProfile;
    private Button btnUpdate,btnChooseFile;
    private EditText edtEmail;
    private CircleImageView profile;
    private String phoneKey;
    private String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
    private Uri imageUri;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        edtUsername = (EditText)findViewById(R.id.edtNameU);
        edtPass = (EditText)findViewById(R.id.edtPassU);
        edtEmail = (EditText)findViewById(R.id.edtEmailtU);
        edtProfile=(EditText)findViewById(R.id.edtProfileU);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        profile=(CircleImageView)findViewById(R.id.imgProfileMyAccount);
        btnChooseFile=(Button)findViewById(R.id.btnUpdateFile);
        btnUpdate.setEnabled(false);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        if (getIntent() != null)
            phoneKey = getIntent().getStringExtra("phoneKey");
        if (!phoneKey.isEmpty() && phoneKey != null) {
            if (Common.isConnectedToInternet(this)) {
                updateTutor(table_user,phoneKey);

            } else {
                Toast.makeText(MyAccountActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        onClickChooseImage();

    }

    private void onClickChooseImage() {
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        if(ContextCompat.checkSelfPermission(MyAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                chooseImage();
                btnUpdate.setEnabled(true);
       //         }
//            else{
//                ActivityCompat.requestPermissions(MyAccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//
//            }

            }
        });
    }

    public void updateTutor(final DatabaseReference table_user, final String phoneKey) {

        table_user.child(phoneKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                edtUsername.setText(user.getUsername());
                edtEmail.setText(user.getEmail());
                edtPass.setText(user.getPassword());
                Glide.with(MyAccountActivity.this)
                        .load(user.getAvatar())
                        .centerCrop()
                        .into(profile);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        if (Common.isConnectedToInternet(getBaseContext())) {
                            final ProgressDialog progress = new ProgressDialog(MyAccountActivity.this);
                            progress.setTitle("Loading");
                            progress.setMessage("Wait while loading...");
                            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                            progress.show();
                            final String usernameTemp = edtUsername.getText().toString();
                            final String passwordTemp = edtPass.getText().toString();
                            final String emailTemp = edtEmail.getText().toString();
                            //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (usernameTemp.equals("") || passwordTemp.equals("") || emailTemp.isEmpty()) {
                                progress.dismiss();
                                Toast.makeText(MyAccountActivity.this, "Please fill your inform", Toast.LENGTH_SHORT).show();
                            } else {
                                if (emailTemp.trim().matches(emailPattern)) {
                                    progress.dismiss();
                                    updateStorage(phoneKey);
                                    //User user = new User(usernameTemp, passwordTemp,"");
                                    //finish();
                                }
                                else {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //      }

                        } else {
                            Toast.makeText(MyAccountActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateStorage(String phoneKey) {
        final String usernameTemp = edtUsername.getText().toString();
        final String passwordTemp = edtPass.getText().toString();
        final String emailTemp = edtEmail.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        progressDialog=new ProgressDialog(MyAccountActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Đang tải...");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("/Image/").child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                HashMap<String, Object> map = new HashMap<>();
                String url=taskSnapshot.getDownloadUrl().toString();
                // map.put("courseId", key);
                map.put("avatar", url);
                map.put("username", usernameTemp);
                map.put("password", passwordTemp);
                map.put("email", emailTemp);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
                reference.child(phoneKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(MyAccountActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(MyAccountActivity.this,"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyAccountActivity.this,"Tải file lên thất bại",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
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
                    hideSoftKeyboard(MyAccountActivity.this);
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
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
//        {
            chooseImage();
//        }
//        else
//            Toast.makeText(MyAccountActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            edtProfile.setText(data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(MyAccountActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
        }
    }
}
