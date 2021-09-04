package com.example.gkude.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.gkude.R;
import com.example.gkude.server.UserDataSource;
import com.example.gkude.server.UserRepository;
import com.example.gkude.ui.login.LoginActivity;


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
        Button quit = root.findViewById(R.id.btn_quit);
        update.setOnClickListener(view -> {
            // TODO complete
        });
        favorite.setOnClickListener(view -> {
            // TODO complete
        });
        history.setOnClickListener(view->{
            // TODO complete
        });
        quit.setOnClickListener(view -> {
            userRepository.logout();
            Toast.makeText(getContext(), "退出登录", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        return root;
    }

}
