package com.java.wangxingqi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wangxingqi.R;
import com.java.wangxingqi.ui.chat.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> list;
    public MessageAdapter(List<Message> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout leftLayout;
        TextView left_msg;

        RelativeLayout rightLayout;
        TextView right_msg;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);

            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = list.get(position);
        if(msg.getType() == Message.TYPE_RECEIVED){
            //如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());

            //注意此处隐藏右面的消息布局用的是 View.GONE
            holder.rightLayout.setVisibility(View.GONE);
        }else if(msg.getType() == Message.TYPE_SEND){
            //如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(msg.getContent());

            //同样使用View.GONE
            holder.leftLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
