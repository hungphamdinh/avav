package com.example.callvideo.View.MyCourseList;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Adapter.StaffAdapter;
import com.example.callvideo.Model.Entities.Request;
import com.example.callvideo.Presenter.MyCourseList.MyCourseListAdapterPre;
import com.example.callvideo.R;
import com.example.callvideo.ViewHolder.StaffViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by User on 2/28/2017.
 */

public class MyCourseFragment extends Fragment implements IMyCourseAdapterView{
    private Context context;
    private String userPhone;
    public MyCourseFragment(){}
    public MyCourseFragment(Context context, String userPhone){
        this.context=context;
        this.userPhone=userPhone;
    }
    private static final String TAG = "MyCourseFragment";
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Request, StaffViewHolder> adapter;
    private StaffAdapter staffAdapter;
    private MyCourseListAdapterPre myCourseListAdapterPre;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course,container,false);
        recyclerMenu=(RecyclerView)view.findViewById(R.id.listOrderRecycler);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerMenu.setLayoutManager(layoutManager);
        myCourseListAdapterPre=new MyCourseListAdapterPre(this);
        myCourseListAdapterPre.setAdapter(userPhone);
        //loadTutor();
        return view;
    }
    @Override
    public void callAdapter(ArrayList<Request> requestArrayList) {
        staffAdapter=new StaffAdapter(context,requestArrayList,requestArrayList.get(0).getCourseId(),userPhone);
        staffAdapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(staffAdapter);
    }

    @Override
    public void onErrorLoadCourse(String msg) {
    }
}
