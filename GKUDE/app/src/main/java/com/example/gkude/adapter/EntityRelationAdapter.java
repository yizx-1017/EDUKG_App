package com.example.gkude.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.R;
import com.example.gkude.bean.EntityBean;
import com.bumptech.glide.Glide;

import org.javatuples.Triplet;

import java.util.List;

public class EntityRelationAdapter extends RecyclerView.Adapter<EntityRelationAdapter.EntityRelationViewHolder> {

    private List<Triplet<String, Boolean, EntityBean>> relations;

    public EntityRelationAdapter(List<Triplet<String, Boolean, EntityBean>> relations){
        this.relations = relations;
    }


    @NonNull
    @Override
    public EntityRelationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.item_entity_relation, parent, false);
        return new EntityRelationViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(EntityRelationViewHolder holder, int position) {
        holder.bind(relations.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(20, relations.size());
    }


    class EntityRelationViewHolder extends RecyclerView.ViewHolder {
        TextView mLabel;
        TextView mRelation;
        ImageView mImg;
        public EntityRelationViewHolder(View itemView) {
            super(itemView);
            mLabel = itemView.findViewById(R.id.entity_label2);
            mRelation = itemView.findViewById(R.id.entity_relation);
            mImg = itemView.findViewById(R.id.entity_forward);
        }

        public void bind(final Triplet<String, Boolean, EntityBean> entity) {

            // load relation info
            mLabel.setText(entity.getValue2().getLabel());
            mRelation.setText(entity.getValue0());

            if(entity.getValue1()){
                Glide.with(mImg.getContext())
                        .load(R.drawable.ic_arrow_circle_forward_24px)
                        .into(mImg);
            } else{
                Glide.with(mImg.getContext())
                        .load(R.drawable.ic_arrow_circle_backward_24px)
                        .into(mImg);
            }

        }
    }
}
