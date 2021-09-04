package com.example.gkude.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.EntityViewActivity;
import com.example.gkude.R;
import com.example.gkude.bean.EntityBean;
import com.bumptech.glide.Glide;
import com.example.gkude.bean.RelationBean;

import org.javatuples.Triplet;

import java.util.List;

public class EntityRelationAdapter extends RecyclerView.Adapter<EntityRelationAdapter.EntityRelationViewHolder> {


    private List<RelationBean> relations;
    private String course;

    public EntityRelationAdapter(List<RelationBean> relations, String course){
        Log.i("EntityRelation category", course);
        this.relations = relations;
        this.course = course;
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

        public void bind(final RelationBean relation) {
            boolean forward = true;
            String entity_name = relation.getObjectName();
            if(entity_name == null) {
                entity_name = relation.getSubjectName();
                forward = false;
            }
            // load relation info
            mLabel.setText(entity_name);
            mRelation.setText(relation.getRelationName());
            // TODO(zhiyuxie): add link

            if(forward){
                Glide.with(mImg.getContext())
                        .load(R.drawable.ic_arrow_circle_forward_24px)
                        .into(mImg);
            } else{
                Glide.with(mImg.getContext())
                        .load(R.drawable.ic_arrow_circle_backward_24px)
                        .into(mImg);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Go to the detailed page
                    Intent intent = new Intent(view.getContext(), EntityViewActivity.class);
                    intent.putExtra("entity_label", relation.getName());

                    intent.putExtra("entity_course",course);
                    intent.putExtra("entity_uri", relation.getEntityUri());
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}
