package com.java.wangxingqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.wangxingqi.bean.EntityBean;
import com.java.wangxingqi.bean.ProblemBean;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

public class ShareActivity extends Activity implements WbShareCallback {
    //在微博开发平台为应用申请的App Key
    private static final String APP_KY = "2045436852";
    //在微博开放平台设置的授权回调页
    private static final String REDIRECT_URL = "http://www.sina.com";
    //在微博开放平台为应用申请的高级权限
    private static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    private IWBAPI mWBAPI;
    private String shareText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        generateText();
        TextView shareTextView = findViewById(R.id.shareActivityText);
        shareTextView.setText(shareText);
        Button mCommit = findViewById(R.id.shareActivityButton);
        Button mCancel = findViewById(R.id.cancelShareActivity);
        mCancel.setOnClickListener(view -> {
            finish();
        });
        initSdk();
        mCommit.setOnClickListener(view -> {
            Log.i("ShareActivity", "commit cliked!");
            doWeiboShare();
        });

    }

    private void initSdk() {
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
    }

    private void generateText() {
        boolean isEntity = getIntent().getBooleanExtra("isEntity", true);
        Long id = getIntent().getLongExtra("id", 1L);
        if (isEntity){
            EntityBean entityBean = EntityBean.findById(EntityBean.class, id);
            shareText = "【基础教育知识图谱：实体分享】\n";
            shareText += ("实体名称：" + entityBean.getLabel() + '\n');
            shareText += ("所属学科：" + entityBean.getCourse() + '\n');
            shareText += ("所属目录：" + entityBean.getCategory() + '\n');
        } else {
            ProblemBean problemBean = ProblemBean.findById(ProblemBean.class, id);
            String qBody = problemBean.getQBody();
            shareText = "【基础教育知识图谱：试题分享】\n";
            try {
                int A = qBody.indexOf("A.");
                int B = qBody.indexOf("B.");
                int C = qBody.indexOf("C.");
                int D = qBody.indexOf("D.");
                String tmp = qBody.substring(0, A) + "\n" + qBody.substring(A, B) + "\n" + qBody.substring(B, C) + '\n' + qBody.substring(C, D) + "\n" + qBody.substring(D);
                if (A>=0&&B>=0&&C>=0&&D>=0) {
                    shareText += ("问题：" + tmp + '\n');
                } else {
                    shareText += ("问题：" + problemBean.getQBody() + '\n');
                }
            } catch (Exception e) {
                shareText += ("问题：" + problemBean.getQBody() + '\n');
            }
            shareText += ("答案：" + problemBean.getQAnswer() + '\n');
        }

    }

    private void doWeiboShare() {
        WeiboMultiMessage message = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = shareText;
        message.textObject = textObject;
        mWBAPI.shareMessage(message, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWBAPI.doResultIntent(data, this);
    }

    @Override
    public void onComplete() {
        Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(@NonNull UiError uiError) {
        Toast.makeText(ShareActivity.this, "分享失败:" + uiError.errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(ShareActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
    }
}
