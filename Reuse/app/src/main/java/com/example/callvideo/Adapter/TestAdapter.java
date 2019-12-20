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
import com.example.callvideo.Model.Entities.Doc;
import com.example.callvideo.R;

import java.util.ArrayList;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder>  {
    private Context context;
    private ArrayList<Doc> doc;
    private ArrayList<String> docKey;
    public TestAdapter(Context context, ArrayList<Doc> doc, ArrayList<String>docKey) {
        this.context = context;
        this.doc = doc;
        this.docKey =docKey;
    }
    public TestAdapter() {
    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.test_layout, parent, false);
        TestViewHolder holder = new TestViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {

        holder.txtDocName.setText(doc.get(position).getDocName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Uri uri = Uri.parse(doc.get(position).getDocUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return doc.size();
    }


    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView txtDocName;
        private ItemClickListener itemClickListener;

        public TestViewHolder(View itemView) {
            super(itemView);
            txtDocName=(TextView)itemView.findViewById(R.id.txtTest);
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












