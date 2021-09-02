package com.example.gkude.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.Manager;
import com.example.gkude.R;
import com.example.gkude.adapter.MessageAdapter;
import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.ResultBean;
import com.example.gkude.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";
    Observer<List<ResultBean>> observer = null;
    private List<Message> messageList;
    private RecyclerView msgRecyclerView;
    private EditText inputQuestion;
    private Button send;
    private LinearLayoutManager layoutManager;
    private MessageAdapter messageAdapter;
    private FragmentNotificationsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        msgRecyclerView = root.findViewById(R.id.msg_recycler_view);
        inputQuestion = root.findViewById(R.id.input_question);
        send = root.findViewById(R.id.send_question);
        layoutManager = new LinearLayoutManager(getActivity());
        messageAdapter = new MessageAdapter(messageList = getData());
        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(messageAdapter);

        send.setOnClickListener(view -> {
            String content = inputQuestion.getText().toString();
            if(!content.equals("")) {
                messageList.add(new Message(content,Message.TYPE_SEND));
                messageAdapter.notifyItemInserted(messageList.size()-1);
                msgRecyclerView.scrollToPosition(messageList.size()-1);
                inputQuestion.setText("");//清空输入框中的内容
                // TODO revise to complete
                Manager.answerInputQuestion("chinese", content, observer);
            }
        });
        return root;
    }

    @NonNull
    private List<Message> getData() {
        List<Message> list = new ArrayList<>();
        list.add(new Message("Hello! Welcome to knowledge Q&A!",Message.TYPE_RECEIVED));
        return list;
    }

    private void initObserver() {
         observer = new Observer<List<ResultBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG,"observer subscribed");
            }
            @Override
            public void onNext(@NonNull List<ResultBean> answerList) {
                Log.e(TAG,"getAnswer");
                String content = answerList.get(0).getValue();
                messageList.add(new Message(content,Message.TYPE_RECEIVED));
                messageAdapter.notifyItemInserted(messageList.size()-1);
                msgRecyclerView.scrollToPosition(messageList.size()-1);
            }
            @Override
            public void onError(@NonNull Throwable e) {
            }
            @Override
            public void onComplete() {
                Log.e(TAG,"Complete");
            }
        };
    }
}