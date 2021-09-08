package com.java.wangxingqi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wangxingqi.R;
import com.java.wangxingqi.bean.PropertyBean;

import java.util.List;

public class EntityPropertyAdapter extends RecyclerView.Adapter<EntityPropertyAdapter.EntityPropertyViewHolder> {

    private List<PropertyBean> properties;

    public EntityPropertyAdapter(List<PropertyBean> properties){
        this.properties = properties;
    }


    @NonNull
    @Override
    public EntityPropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.item_entity_property, parent, false);
        return new EntityPropertyViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(EntityPropertyViewHolder holder, int position) {
        holder.bind(properties.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(20, properties.size());
    }


    class EntityPropertyViewHolder extends RecyclerView.ViewHolder {
        TextView predicateLabel;
        TextView object;

        public EntityPropertyViewHolder(View itemView) {
            super(itemView);
            predicateLabel = itemView.findViewById(R.id.entity_definition);
            object = itemView.findViewById(R.id.entity_definition_content);
        }

        public void bind(final PropertyBean property) {
            // load property info
            predicateLabel.setText(property.getPredicateLabel());
            object.setText(property.getObject());
        }
    }
}
