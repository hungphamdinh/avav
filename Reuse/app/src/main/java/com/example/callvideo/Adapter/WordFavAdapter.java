package com.example.callvideo.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Entities.Word;
import com.example.callvideo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class WordFavAdapter extends RecyclerView.Adapter<WordFavAdapter.WordFavViewHolder>  {
    private Context context;
    private ArrayList<Word> words;
    private ArrayList<String>docKey;
    public WordFavAdapter(Context context, ArrayList<Word> worlds, ArrayList<String>docKey) {
        this.context = context;
        this.words = worlds;
        this.docKey=docKey;
    }
    public WordFavAdapter() {
    }
    @NonNull
    @Override
    public WordFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.favourite_item, parent, false);
        WordFavViewHolder holder = new WordFavViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WordFavViewHolder holder, final int position) {
        holder.txtAfTrans.setText(words.get(position).getAfTrans());
        holder.txtBeTrans.setText(words.get(position).getBeTrans());
        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteDialog(docKey.get(position),holder);

            }
        });

    }

    private void deleteDialog(final String key,WordFavViewHolder holder) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có chắc muốn xóa?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Word");
                if(words.size()==0||words==null){
                    holder.itemView.setVisibility(View.GONE);
                }
                else {
                    docRef.child(key).removeValue();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return words.size();
    }


    public class WordFavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtAfTrans,txtBeTrans;
        private ImageView imgBtnDelete;
        private ItemClickListener itemClickListener;

        public WordFavViewHolder(View itemView) {
            super(itemView);
            txtAfTrans =(TextView) itemView.findViewById(R.id.txtAfTrans);
            txtBeTrans=(TextView)itemView.findViewById(R.id.txtBeTrans);
            imgBtnDelete=(ImageView)itemView.findViewById(R.id.btnDeleteFavour);
           // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
         //   itemClickListener.onClick(view,getAdapterPosition(),false);
        }



    }

}


