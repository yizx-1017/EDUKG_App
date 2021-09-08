package com.java.wangxingqi.ui.link;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.wangxingqi.EntityViewActivity;
import com.java.wangxingqi.Manager;
import com.java.wangxingqi.R;
import com.java.wangxingqi.bean.RecognitionBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    private Observer<List<RecognitionBean>> observer = null;
    private List<RecognizeString> recognizeStringList;
    private EditText editText;
    private TextView textView;
    private String course = "chinese";
    private String inputContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        editText = root.findViewById(R.id.dashboard_input);
        textView = root.findViewById(R.id.dashboard_output);
        Button button = root.findViewById(R.id.dashboard_button);
        Spinner courseSpinner = root.findViewById(R.id.course_spinner);
        initObserver();
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String courseCN = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "选择的学科是：" + courseCN, Toast.LENGTH_SHORT).show();
                switch (courseCN){
                    case "语文":
                        course = "chinese";
                        break;
                    case "数学":
                        course = "math";
                        break;
                    case "英语":
                        course = "english";
                        break;
                    case "物理":
                        course = "physics";
                        break;
                    case "化学":
                        course = "chemistry";
                        break;
                    case "生物":
                        course = "biology";
                        break;
                    case "历史":
                        course = "history";
                        break;
                    case "政治":
                        course = "politics";
                        break;
                    case "地理":
                        course = "geo";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        button.setOnClickListener(view -> {
            String content = editText.getText().toString();
            if (!content.equals("")) {
                Log.i(TAG, "send message:" + content);
                inputContent = content;
                Manager.recognizeEntity(course, content, observer);
            }
        });
        return root;
    }

    private void initObserver() {
        observer = new Observer<List<RecognitionBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG,"observer subscribed");

            }

            @Override
            public void onNext(@NonNull List<RecognitionBean> recognitionBeans) {
                Log.e(TAG,"getAnswer");
                int executeStartIndex = 0;
                recognizeStringList = new ArrayList<>();
                for (RecognitionBean r: recognitionBeans) {
                    recognizeStringList.add(new RecognizeString(inputContent.substring(executeStartIndex, r.getStart_index()),false));
                    recognizeStringList.add(new RecognizeString(inputContent.substring(r.getStart_index(), r.getEnd_index()+1),true, r.getEntity_url()));
                    executeStartIndex = r.getEnd_index()+1;
                }
                recognizeStringList.add(new RecognizeString(inputContent.substring(executeStartIndex), false));
                recognizeStringList.removeIf(r->r.getContent().equals(""));
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e(TAG,"Complete");
                textView.setText("");
                for (RecognizeString r: recognizeStringList) {
                    if (r.getIsEntity()) {
                        SpannableString recognizeSpannable = new SpannableString(r.getContent());
                        ClickableSpan recognizeSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                Log.e(TAG,"Item clicked!");
                                Intent intent = new Intent(getActivity(), EntityViewActivity.class);
                                intent.putExtra("entity_label", r.getContent());
                                intent.putExtra("entity_course", course);
                                intent.putExtra("entity_uri", r.getUri());
                                startActivity(intent);
                            }
                        };
                        recognizeSpannable.setSpan(recognizeSpan, 0, r.getContent().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        textView.append(recognizeSpannable);
                    } else {
                        textView.append(r.getContent());
                    }
                }
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        };
    }
}

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
class RecognizeString {
    @NonNull
    private String content;
    @NonNull
    private Boolean isEntity;
    private String uri;
}