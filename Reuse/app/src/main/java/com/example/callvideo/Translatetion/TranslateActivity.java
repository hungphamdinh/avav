package com.example.callvideo.Translatetion;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.callvideo.R;
import com.example.callvideo.View.Translate.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                            case R.id.action_history:
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
        FavouriteFragment favouriteFragment = new FavouriteFragment(userPhone);
        ft.replace(R.id.fragment, favouriteFragment);
        ft.commit();
    }

    public void changeToMainView() {
        // Change current fragment in activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new MainFragment(TranslateActivity.this,userPhone));
        ft.commit();
    }


}