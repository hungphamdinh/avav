package com.example.callvideo.View.Translate;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.callvideo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TranslateActivity extends AppCompatActivity {
    private   BottomNavigationView bottomNavigationView;
    private String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transl);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (getIntent() != null)
            userPhone = getIntent().getStringExtra("userPhone");
        if(savedInstanceState == null){
            changeToMainView();
        }
      bottomNavigationView= (BottomNavigationView)
                findViewById(R.id.bottom_navigationMain);
        bottomNavigationListener();
    }

    public void bottomNavigationListener() {


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_translate:
                                changeToMainView();
                                break;
                            case R.id.action_favourites:
                                changeToFavourite();
                                break;
                        }
                        return true;
                    }
                });
    }

    public void changeToFavourite() {
        // Change current fragment in activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FavouriteFragment favouriteFragment = new FavouriteFragment(userPhone,TranslateActivity.this);
        ft.replace(R.id.fragment, favouriteFragment);
        ft.commit();
    }

    public void changeToMainView() {
        // Change current fragment in activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new MainFragment(TranslateActivity.this,userPhone));
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    setStatus("offline");
    }
    private void setStatus(final String status) {
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("User").child(userPhone);
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        user.updateChildren(map);
        //  user.child(phone).setValue(map);
    }

}