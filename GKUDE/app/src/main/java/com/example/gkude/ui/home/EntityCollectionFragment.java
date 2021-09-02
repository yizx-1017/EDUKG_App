package com.example.gkude.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gkude.EntityViewActivity;
import com.example.gkude.Manager;
import com.example.gkude.R;
import com.example.gkude.adapter.EntityCollectionAdapter;
import com.example.gkude.bean.EntityBean;
import com.orm.SugarContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class EntityCollectionFragment extends Fragment implements EntityCollectionAdapter.OnEntitySelectedListener {

    private final String TAG;
    private Observer<List<EntityBean>> observer = null;
    private List<EntityBean> entityList = new LinkedList<>();
    private EntityCollectionAdapter mAdapter;

    public EntityCollectionFragment(final String tag) {
        this.TAG = tag;
        Log.e("EntityCollectionFragment", TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("EntityCollectionFragmentonCreate", TAG);
        super.onCreate(savedInstanceState);
//        SugarContext.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_entity_collection, container, false);

        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set adapter for recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EntityCollectionAdapter(entityList, this);
        recyclerView.setAdapter(mAdapter);

        initObserver();

        return rootView;
    }

    private void initObserver() {
        observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG,"observer subscribed");
            }
            @Override
            public void onNext(List<EntityBean> entities) {

                Log.i(TAG,"getList");
                mAdapter.setEntityList(entities);

//                if(TAG.equals("news")|| TAG.equals("paper")) {
//                    if (refreshLayout != null) {
//                        if (isLoadingMore) {
//                            refreshLayout.finishLoadMore();
//                            if (TAG.equals("news")) {
//                                for (News n : news) {
//                                    if (News.find(News.class, "_id = ?", n.get_id()).size() > 0) {
//                                        n.setVisited(true);
//                                    } else {
//                                        n.setVisited(false);
//                                    }
//                                }
//                            }
//                            mAdapter.addNewsList(news);
//                        } else {
//                            refreshLayout.finishRefresh();
//                            if (TAG.equals("news")) {
//                                for (News n : news) {
//                                    if (News.find(News.class, "_id = ?", n.get_id()).size() > 0) {
//                                        n.setVisited(true);
//                                    } else {
//                                        n.setVisited(false);
//                                    }
//                                }
//                            }
//
//                            mAdapter.setNewsList(news);
//                        }
//                    }
//                }
//                else{
//                    mAdapter.setNewsList(news);
//                }
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
                Log.i(TAG,"Complete");
            }
        };
        // TODO(zixuanyi): 不同学科的默认列表页
        if (TAG.equals("语文")) {
            Log.i("EntityCollectionFragment", "search 语文");
            Manager.searchEntity("chinese", "中", observer);
        } else if(TAG.equals("英语")) {
            Log.i("EntityCollectionFragment", "search 英语");
            Manager.searchEntity("english", "语", observer);
        } else if (TAG.equals("数学")) {
            Log.i("EntityCollectionFragment", "search 数学");
               Manager.searchEntity("math", "", observer);
        } else if (TAG.equals("物理")) {
            Log.i("EntityCollectionFragment", "search 物理");
                Manager.searchEntity("physics", "力", observer);
        } else if (TAG.equals("化学")) {
            Log.i("EntityCollectionFragment", "search 化学");
                Manager.searchEntity("chemistry","反", observer);
        } else if (TAG.equals("生物")) {
            Log.i("EntityCollectionFragment", "search 生物");
                Manager.searchEntity("biology","胞", observer);
        } else if (TAG.equals("历史")) {
            Log.i("EntityCollectionFragment", "search 历史");
                Manager.searchEntity("history","史", observer);
        } else if(TAG.equals("地理")) {
            Log.i("EntityCollectionFragment", "search 地理");
            Manager.searchEntity("geo","国", observer);
        } else if (TAG.equals("政治")) {
            Log.i("EntityCollectionFragment", "search 政治");
                Manager.searchEntity("politics","法", observer);
        }
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        if(!entity.isVisited()){
            entity.save();
        }

        // Go to the detailed page
        Intent intent = new Intent(getActivity(), EntityViewActivity.class);
        intent.putExtra("entity_id", entity.getId());
        startActivity(intent);
    }
}