package com.java.wangxingqi.ui.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.java.wangxingqi.EntityListViewActivity;
import com.java.wangxingqi.ProblemListViewActivity;
import com.java.wangxingqi.ProblemRecommendationActivity;
import com.java.wangxingqi.R;
import com.java.wangxingqi.StatsAnalyzeActivity;
import com.java.wangxingqi.server.Result;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;
import com.java.wangxingqi.ui.login.LoginActivity;


public class UserFragment extends Fragment{
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user,container,false);
        userRepository = UserRepository.getInstance(new UserDataSource());
        TextView userName = root.findViewById(R.id.user_name);
        userName.setText(userRepository.getUser().getUsername());
        LinearLayout update = root.findViewById(R.id.update);
        LinearLayout favorite = root.findViewById(R.id.favorite);
        LinearLayout history = root.findViewById(R.id.history);
        LinearLayout wrongProblems = root.findViewById(R.id.wrongProblem);
        LinearLayout recommendationProblems = root.findViewById(R.id.recommendationProblem);
        LinearLayout statisticData = root.findViewById(R.id.statisticData);
        Button quit = root.findViewById(R.id.btn_quit);
        update.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("更改密码");
            final View v = getLayoutInflater().inflate(R.layout.change_password, null);
            builder.setView(v);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText old_password = v.findViewById(R.id.old_password);
                    EditText new_password = v.findViewById(R.id.new_password);
                    String s_old = old_password.getText().toString();
                    String s_new = new_password.getText().toString();
                    Result<String> result = userRepository.updatePassword(s_old, s_new);
                    if (result.getStatus().equals(200)) {
                        Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        });
        favorite.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EntityListViewActivity.class);
            intent.putExtra("isFavorite", true);
            startActivity(intent);
        });
        history.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), EntityListViewActivity.class);
            intent.putExtra("isFavorite", false);
            startActivity(intent);
        });
        wrongProblems.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProblemListViewActivity.class);
            startActivity(intent);
        });
        recommendationProblems.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProblemRecommendationActivity.class);
            startActivity(intent);
        });
        statisticData.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), StatsAnalyzeActivity.class);
            startActivity(intent);
        });
        quit.setOnClickListener(view -> {
            userRepository.logout();
            Toast.makeText(getContext(), "退出登录", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        return root;
    }

}
