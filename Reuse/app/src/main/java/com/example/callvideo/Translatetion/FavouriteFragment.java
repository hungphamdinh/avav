package com.example.callvideo.Translatetion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.callvideo.R;

import java.util.ArrayList;

/**
 * Created by almaz on 16.04.17.
 */

public class FavouriteFragment extends Fragment {

    private ActionBar actionBar;
    private String nameOfDB;
    private View rootView;
    private TextView apiInfo;
    private TextView link;
    private String userPhone;
    public FavouriteFragment(String userPhone){
        this.userPhone=userPhone;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favourite_fragment, container, false);

//        setHintToSearch();
        return rootView;
    }

    public void setHintToSearch() {
        EditText search = (EditText) rootView.findViewById(R.id.search);
        if (nameOfDB.equals("History.db")) {
            search.setHint(R.string.history_search_hint);
        } else {
            search.setHint(R.string.favourite_search_hint);
        }
    }

//    public void setCustomActionBar() {
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.custom_action_bar);
//
//        if (nameOfDB.equals("History.db")) {
//            actionBar.setTitle(R.string.text_history);
//        } else {
//            actionBar.setTitle(R.string.text_favourites);
//        }
//    }



    @Override
    public void onDestroy() {
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
//        actionBar.setTitle(R.string.app_name);
        super.onDestroy();
    }
}
