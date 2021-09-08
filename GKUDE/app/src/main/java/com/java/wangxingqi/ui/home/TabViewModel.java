package com.java.wangxingqi.ui.home;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TabViewModel extends ViewModel {

    private MutableLiveData<List<String>> category, delCategory;

    public TabViewModel() {
        category = new MutableLiveData<>();
        delCategory = new MutableLiveData<>();
        ArrayList<String> temp = new ArrayList<>();
        temp.add("语文");
        temp.add("英语");
        temp.add("数学");
        temp.add("物理");
        temp.add("化学");
        temp.add("生物");
        temp.add("历史");
        temp.add("地理");
        temp.add("政治");
        category.setValue(temp);
        delCategory.setValue(new ArrayList<String>());
    }

    public LiveData<List<String>> getCategory(){
        return category;
    }

    public LiveData<List<String>> getDelCategory(){
        return delCategory;
    }

    public void setCategory(List<String> cat){
        category.setValue(cat);
    }

    public void setDelCategory(List<String> delCat){
        delCategory.setValue(delCat);
    }
}