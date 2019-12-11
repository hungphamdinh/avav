package com.example.callvideo.View.Translate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Adapter.WordFavAdapter;
import com.example.callvideo.Model.Entities.Word;
import com.example.callvideo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by almaz on 16.04.17.
 */

public class FavouriteFragment extends Fragment {
    private WordFavAdapter wordFavAdapter;
    private RecyclerView listWord;
    private View rootView;
    private ArrayList<Word>wordList;
    private ArrayList<String>key;
    private String userPhone;
    private Context context;
    public FavouriteFragment(String userPhone,Context context){
        this.context=context;
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
        listWord =(RecyclerView)rootView.findViewById(R.id.listView);
        listWord.setHasFixedSize(true);
        listWord.setLayoutManager(new LinearLayoutManager(context));
        DatabaseReference wordRef= FirebaseDatabase.getInstance().getReference("Word");
        wordRef.orderByChild("userPhone").equalTo(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key=new ArrayList<>();
                wordList=new ArrayList<>();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Word word=childSnap.getValue(Word.class);
                    key.add(childSnap.getKey());
                    wordList.add(word);
                    wordFavAdapter=new WordFavAdapter(context,wordList,key);
                    listWord.setAdapter(wordFavAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

//    public void setHintToSearch() {
//        EditText search = (EditText) rootView.findViewById(R.id.search);
//        if (nameOfDB.equals("History.db")) {
//            search.setHint(R.string.history_search_hint);
//        } else {
//            search.setHint(R.string.favourite_search_hint);
//        }
//    }



    @Override
    public void onDestroy() {
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
//        actionBar.setTitle(R.string.app_name);
        super.onDestroy();
    }
}
