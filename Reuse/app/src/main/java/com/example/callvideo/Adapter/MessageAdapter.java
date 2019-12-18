package com.example.callvideo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.callvideo.Interface.ItemClickListener;
import com.example.callvideo.Model.Entities.Chat;
import com.example.callvideo.Model.Entities.Tutor;
import com.example.callvideo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<Chat> chat;
    public static final int MSG_LEFT=0;
    public DatabaseReference chatRef;
    public FirebaseDatabase database;
    private HashMap<String,Object>id;
    private ArrayList<String>keys;
    public static final int MSG_RIGHT=1;
    public MessageAdapter(Context context, ArrayList<Chat> chat, HashMap<String,Object>id,ArrayList<String>keys) {
        this.context = context;
        this.chat = chat;
        this.id=id;
        this.keys=keys;
    }
    public MessageAdapter(){

    }
    @NonNull
    @Override
    public MessageAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ChatViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        database = FirebaseDatabase.getInstance();
        chatRef = database.getReference("Chat");
        if(chat.get(position).getSender().equals(id.get("userId").toString())) {
            return MSG_RIGHT;
        }
        else{
            return MSG_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chatItem=chat.get(position);

        if(chatItem.getReciever().equals(id.get("userId"))&&chatItem.getSender().equals(id.get("tutorId"))){
            HashMap<String, Object> map = new HashMap<>();
            map.put("seen", true);
            chatRef.child(keys.get(position)).updateChildren(map);

        }
        if(chatItem.isSeen()==true){
            holder.seen.setVisibility(View.VISIBLE);
        }
        else {
            holder.seen.setVisibility(View.GONE);
        }
        //onCheckSeen(holder);
        holder.showMessage.setText(chatItem.getMessage());
        onLoadData(holder);
    }
    private void onLoadData(@NonNull ChatViewHolder holder) {
        DatabaseReference receiverRef= FirebaseDatabase.getInstance().getReference("Tutor");
        receiverRef.child(id.get("tutorId").toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                if(tutor.getStatus().equals("offline")){
                    holder.status.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.status.setVisibility(View.VISIBLE);
                }
                Glide.with(context.getApplicationContext())
                        .load(tutor.getAvatar())
                        .centerCrop()
                        // .placeholder(R.drawable.loading_spinner)
                        .into(holder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                deleteDialog(keys.get(position),holder);
            }
        });
    }

    private void deleteDialog(final String key, ChatViewHolder holder) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có chắc muốn xóa tin nhắn?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Chat");
                if(chat.size()==0){
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
        return chat.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView showMessage,seen;
        private ItemClickListener itemClickListener;
        public ImageView profileImage,status;
        private ImageButton btnDelete;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=(TextView)itemView.findViewById(R.id.showMessage);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
            seen=(TextView)itemView.findViewById(R.id.txtSeen);
            status=(ImageView)itemView.findViewById(R.id.imgStatusChat);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }

}
