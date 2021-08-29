package com.example.gkude;

import androidx.annotation.NonNull;

import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.RecognitionBean;
import com.example.gkude.bean.ResultBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Manager {
    private static Fetch fetch = new Fetch();

    public static void searchEntity(Observer<List<EntityBean>> observer, String keyword) {
    }

    public static void searchEntity(@NonNull String course, @NonNull String searchKey, Observer<List<EntityBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<EntityBean>>) emitter -> {
            List<EntityBean> list = fetch.fetchInstanceList(course, searchKey);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getEntityInfo(@NonNull EntityBean entityBean, Observer<EntityBean> observer) {
        Observable.create((ObservableOnSubscribe<EntityBean>) emitter -> {
            fetch.fetchInfoByInstanceName(entityBean);
            fetch.fetchQuestionListByUriName(entityBean);
            entityBean.setVisited(true);
            entityBean.save();
            emitter.onNext(entityBean);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void answerInputQuestion(@NonNull String course, @NonNull String inputQuestion, Observer<List<ResultBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<ResultBean>>) emitter -> {
            List<ResultBean> list = fetch.fetchInputQuestion(course, inputQuestion);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void recognizeEntity(@NonNull String course, @NonNull String context, Observer<List<RecognitionBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<RecognitionBean>>) emitter -> {
            List<RecognitionBean> list = fetch.fetchLinkInstance(course, context);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
