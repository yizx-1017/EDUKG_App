package com.example.gkude.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.R;
import com.example.gkude.server.UserDataSource;
import com.example.gkude.server.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private List<UserItem> userItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user,container,false);
        mRecyclerView = root.findViewById(R.id.user_recycler_view);
        initItemData();
        return root;
    }

    private void initItemData() {
        userItems = new ArrayList<>();
        UserItem userItem = new UserItem("修改密码");
        userItems.add(userItem);
        userItem = new UserItem("收藏");
        userItems.add(userItem);
        userItem = new UserItem("历史记录");
        userItems.add(userItem);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private View getHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.item_user_header, (ViewGroup) mRecyclerView.getParent(), false);
        TextView userName = headerView.findViewById(R.id.my_header_name);
        userName.setText(UserRepository.getInstance(new UserDataSource()).getUser().getUsername());
        return headerView;
    }
}
