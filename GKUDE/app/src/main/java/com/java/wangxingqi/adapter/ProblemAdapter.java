package com.java.wangxingqi.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wangxingqi.ProblemViewActivity;
import com.java.wangxingqi.R;
import com.java.wangxingqi.bean.ProblemBean;

import java.util.List;

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder> {

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
        return problems.size();
    }


    class ProblemViewHolder extends RecyclerView.ViewHolder {
        TextView qBody;
        TextView qAnswer;

        public ProblemViewHolder(View itemView) {
            super(itemView);
            qBody = itemView.findViewById(R.id.entity_problem);
        }

        public void bind(final ProblemBean problem) {
            String text = problem.getQBody();
            qBody.setText(text);
            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Go to the detailed page
                    ProblemBean privateProblem = problem;
                    List<ProblemBean> list = ProblemBean.find(ProblemBean.class, "q_id = ?", privateProblem.getQID().toString());
                    if (list.isEmpty()) {
                        privateProblem.save();
                    } else {
                        privateProblem = list.get(0);
                    }
                    Intent intent = new Intent(view.getContext(), ProblemViewActivity.class);
                    intent.putExtra("problem_id", privateProblem.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
