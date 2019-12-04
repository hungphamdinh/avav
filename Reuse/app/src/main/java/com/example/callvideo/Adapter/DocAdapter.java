package com.example.callvideo.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callvideo.Common.Common;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Doc;
import com.example.callvideo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DocAdapter extends RecyclerView.Adapter<DocAdapter.DocViewHolder>  {
    private Context context;
    private ArrayList<Doc> doc;
    public static final int MSG_LEFT = 0;
    public DatabaseReference courseRef;
    public FirebaseDatabase database;
    private String userId;
    public static final int MSG_RIGHT = 1;
    private String courseId;
    //   private String status;
    //private String imgUrl;
    public DocAdapter(Context context, ArrayList<Doc> doc) {
        this.context = context;
        this.doc = doc;
//        this.courseId=courseId;
//        this.userId=userId;
    }

    public DocAdapter() {

    }

    @NonNull
    @Override
    public DocAdapter.DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doc_layout, parent, false);
        DocViewHolder holder = new DocViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DocViewHolder holder, int position) {
        holder.txtDocName.setText(doc.get(position).getDocName());
        openCourseDoc(doc.get(position).getDocUrl(),holder);
    }
    private void openCourseDoc(String doc,DocViewHolder holder) {
        holder.txtDocName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(doc);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return doc.size();
    }


    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView txtDocName;
        private ItemClickListener itemClickListener;

        public DocViewHolder(View itemView) {
            super(itemView);
            txtDocName=(TextView)itemView.findViewById(R.id.txtCourseDocDetail);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select this action");
            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);

        }

    }

}

