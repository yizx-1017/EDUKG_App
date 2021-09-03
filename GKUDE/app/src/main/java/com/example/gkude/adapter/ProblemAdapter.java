package com.example.gkude.adapter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.EntityViewActivity;
import com.example.gkude.ProblemViewActivity;
import com.example.gkude.R;
import com.example.gkude.bean.EntityBean;
import com.bumptech.glide.Glide;
import com.example.gkude.bean.ProblemBean;
import com.example.gkude.bean.RelationBean;

import org.javatuples.Triplet;

import java.util.List;

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder> {

    public interface OnRelationSelectedListener {
        void onRelationSelected(RelationBean relation);
    }

    private List<ProblemBean> problems;

    public ProblemAdapter(List<ProblemBean> problems){
        this.problems = problems;
    }


    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.item_entity_problem, parent, false);
        return new ProblemViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(ProblemViewHolder holder, int position) {
        holder.bind(problems.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(20, problems.size());
    }


    class ProblemViewHolder extends RecyclerView.ViewHolder {
        TextView qBody;
        TextView qAnswer;

        public ProblemViewHolder(View itemView) {
            super(itemView);
            qBody = itemView.findViewById(R.id.entity_problem);
//            TODO(zhiyuxie): add link
//            qAnswer = itemView.findViewById(R.id.entity_relation);
        }

        public void bind(final ProblemBean problem) {
            qBody.setText(problem.getQBody());
            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Go to the detailed page
                    problem.save();
                    Intent intent = new Intent(view.getContext(), ProblemViewActivity.class);
                    intent.putExtra("problem_id", problem.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
