package com.example.gkude;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.RecognitionBean;
import com.example.gkude.bean.ResultBean;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Manager {
    private static Fetch fetch = null;

    @SuppressLint("CheckResult")
    public static void searchEntity(@NonNull String course, @NonNull String searchKey, Comparator<? super EntityBean> comparator, Observer<List<EntityBean>> observer) {

        Observable.create((ObservableOnSubscribe<List<EntityBean>>) emitter -> {
            if (fetch == null) {
                fetch = new Fetch();
            }
            List<EntityBean> list = fetch.fetchInstanceList(course, searchKey);
            if (comparator != null) {
                list.sort(comparator);
            }
            if (list.isEmpty()) {
                Log.e("searchEntity", "fetchInstanceList missing");
                list = EntityBean.findWithQuery(EntityBean.class, "SELECT * FROM ENTITY_BEAN where COURSE = '"+ course + "'");
            }
            System.out.println("I got here searchEntity");
            System.out.println(list);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void getEntityInfo(@NonNull EntityBean entityBean, Observer<EntityBean> observer) {
        Observable.create((ObservableOnSubscribe<EntityBean>) emitter -> {
            if (fetch == null) {
                fetch = new Fetch();
            }
            List<EntityBean> list = EntityBean.findWithQuery(EntityBean.class, "SELECT * FROM ENTITY_BEAN WHERE uri = " + "'" + entityBean.getUri()+ "'");
            EntityBean privateEntityBean;
            if (list.isEmpty()) {
                EntityBean tmpEntityBean1 = fetch.fetchInfoByInstanceName(entityBean);
                EntityBean tmpEntityBean2 = fetch.fetchQuestionListByUriName(entityBean);
                // 只有成功获取全部详细信息的entity才会保存到数据库中
                if (tmpEntityBean1!=null&&tmpEntityBean2!=null) {
                    entityBean.setVisited(true);
                    Log.i("getEntityInfo", "save");
                    entityBean.save();
                }
                privateEntityBean = entityBean;
            } else {
                privateEntityBean = list.get(0);
            }
            emitter.onNext(privateEntityBean);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void answerInputQuestion(@NonNull String course, @NonNull String inputQuestion, Observer<List<ResultBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<ResultBean>>) emitter -> {
            if (fetch == null) {
                fetch = new Fetch();
            }
            System.out.println("I got here send question "+ inputQuestion);
            List<ResultBean> list = fetch.fetchInputQuestion(course, inputQuestion);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void recognizeEntity(@NonNull String course, @NonNull String context, Observer<List<RecognitionBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<RecognitionBean>>) emitter -> {
            if (fetch == null) {
                fetch = new Fetch();
            }
            List<RecognitionBean> list = fetch.fetchLinkInstance(course, context);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
