package com.example.gkude;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.gkude.bean.EntityBean;

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

    @SuppressLint("CheckResult")
    public static void searchEntity(@NonNull String course, @NonNull String searchKey, Observer<List<EntityBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<EntityBean>>) emitter -> {
            List<EntityBean> list = fetch.fetchInstanceList(course, searchKey);
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @SuppressLint("CheckResult")
    public void getEntityInfo(@NonNull EntityBean entityBean, Observer<EntityBean> observer) {
        Observable.create((ObservableOnSubscribe<EntityBean>) emitter -> {
            fetch.fetchInfoByInstanceName(entityBean);
            entityBean.save();
            emitter.onNext(entityBean);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
