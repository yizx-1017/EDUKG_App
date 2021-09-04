package com.example.gkude.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.R;
import com.example.gkude.bean.EntityBean;

import java.util.List;

public class EntityCollectionAdapter extends RecyclerView.Adapter<EntityCollectionAdapter.EntityInfoViewHolder> {

    public interface OnEntitySelectedListener {
        void onEntitySelected(EntityBean entity);
    }

    private List<EntityBean> entityList;
    private final OnEntitySelectedListener mListener;

    public EntityCollectionAdapter(List<EntityBean> entityList, OnEntitySelectedListener listener){
        this.entityList = entityList;
        this.mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEntityList(List<EntityBean> entities) {
        this.entityList = entities;
        Log.i("EntityCollectionAdapter", "setEntityList");
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EntityInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.item_entity, parent, false);
        return new EntityInfoViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(EntityInfoViewHolder holder, int position) {
        holder.bind(entityList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }


    class EntityInfoViewHolder extends RecyclerView.ViewHolder {
        TextView mLabel;
        TextView mInfo;
        EntityInfoViewHolder(View itemView) {
            super(itemView);
            mLabel = itemView.findViewById(R.id.entity_label);
            mInfo = itemView.findViewById(R.id.entity_description);

            mInfo.setMaxLines(2);
            mInfo.setEllipsize(TextUtils.TruncateAt.END);
        }

        void bind(final EntityBean entity, final OnEntitySelectedListener listener) {

            // load entity info
            mLabel.setText(entity.getLabel());
            mInfo.setText(entity.getDescription());
            Log.i("EntityCollectionAdapter", String.valueOf(entity.isVisited()));
            if(entity.isVisited()){
                mLabel.setTextColor(itemView.getResources().getColor(R.color.clickedNews2));
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onEntitySelected(entity);
                    }
                }
            });

        }
    }
}
