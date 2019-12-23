package com.example.callvideo.View;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.callvideo.Adapter.SectionsPageAdapter;
import com.example.callvideo.Common.Common;
import com.example.callvideo.Model.Entities.User;
import com.example.callvideo.R;
import com.example.callvideo.View.MyAccountView.MyAccountActivity;
import com.example.callvideo.View.CourseList.CourseActivity;
import com.example.callvideo.View.MyCourseList.MyCourseFragment;
import com.example.callvideo.View.Login.LoginActivity2;
import com.example.callvideo.View.Translate.TranslateActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String TAG = "TranslateActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String userPhone = "";
    private TextView nav_user,nav_usermane;
    private ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Category");
        Paper.init(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView)hView.findViewById(R.id.txtGmailProfile);
        profile=(CircleImageView) hView.findViewById(R.id.imageViewProfile);
        nav_usermane=(TextView)hView.findViewById(R.id.txtNameProfile);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        if (Common.isConnectedToInternet(this)) {
            // loadMenu();

        } else {
            Toast.makeText(Home2Activity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            return;
        }
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (getIntent() != null)
            userPhone = getIntent().getStringExtra("phoneUser");
        if (!userPhone.isEmpty() && userPhone != null) {
            if (Common.isConnectedToInternet(this)) {
                setupViewPager(mViewPager);
                setStatus();
                setProfileInform();
            } else {
                Toast.makeText(Home2Activity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            // loadMenu();
        } else if (id == R.id.accountInform) {
            Intent intent=new Intent(Home2Activity.this, MyAccountActivity.class);
            intent.putExtra("phoneKey",userPhone);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_menu) {
        } else if (id == R.id.nav_translate) {
            Intent intent=new Intent(Home2Activity.this, TranslateActivity.class);
            intent.putExtra("userPhone",userPhone);
            startActivity(intent);
        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(Home2Activity.this, CourseActivity.class);
            intent.putExtra("phoneUser",userPhone);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {
            Paper.book().destroy();
            Intent signIn = new Intent(Home2Activity.this, LoginActivity2.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
            DatabaseReference.goOffline();
            finish();
        } else if (id == R.id.nav_feedback) {
            Uri uri = Uri.parse("https://forms.gle/uKm5YsnwTdje7sFf6");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
                }
//         else if (id == R.id.nav_send) {
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openFeedBack() {


    }
    private void setProfileInform() {
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("User");
        userRef.child(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tutor=dataSnapshot.getValue(User.class);
                nav_user.setText(tutor.getEmail());
                Glide.with(getApplicationContext())
                        .load(tutor.getAvatar())
                        .centerCrop()
                        .into(profile);
                nav_usermane.setText(tutor.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(Home2Activity.this, userPhone), "Trang chủ");
        adapter.addFragment(new MyCourseFragment(Home2Activity.this,userPhone), "Khóa học đã đăng ký");
        adapter.addFragment(new Tab3Fragment(), "Về chúng tôi");
        viewPager.setAdapter(adapter);

    }

    private void setStatus() {
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "online");
        HashMap<String, Object> offMap = new HashMap<>();
        offMap.put("status","offline");
        user.child(userPhone).onDisconnect().updateChildren(offMap);
        user.child(userPhone).updateChildren(map);
        //  user.child(phone).setValue(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
        HashMap<String, Object> offMap = new HashMap<>();
        offMap.put("status","offline");
        user.child(userPhone).updateChildren(offMap);
    }
}
