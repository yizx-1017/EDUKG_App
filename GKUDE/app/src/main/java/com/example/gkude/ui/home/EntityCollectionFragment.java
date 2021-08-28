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
import com.example.gkude.R;
import com.example.gkude.adapter.EntityCollectionAdapter;
import com.example.gkude.bean.EntityBean;
import com.orm.SugarContext;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class EntityCollectionFragment extends Fragment implements EntityCollectionAdapter.OnEntitySelectedListener {

    private static String TAG = new String();
    private Observer<List<EntityBean>> observer = null;
    private List<EntityBean> entityList = new LinkedList<>();
    private EntityCollectionAdapter mAdapter;

    public EntityCollectionFragment(final String tag) {
        this.TAG = tag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(getContext());
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

        return rootView;
    }

    private void initObserver(boolean getNew) {
        observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG,"observer subscribed");
            }
            @Override
            public void onNext(List<EntityBean> news) {
                Log.e(TAG,"getList");
                mAdapter.setEntityList(news);

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
                Log.e(TAG,"Complete");
            }
        };
        // TODO(zixuanyi): Manage.refresh_n
        // Get information according to the TAG
        //Manager.refresh_n(TAG, observer);
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        if(!entity.isVisited()){
            entity.save();
        }

        // Go to the detailed page
        Intent intent = new Intent(getActivity(), EntityViewActivity.class);
        intent.putExtra("label", entity.getLabel());
        intent.putExtra("description", entity.getDescription());
        intent.putExtra("properties", (Parcelable) entity.getProperties());
        intent.putExtra("relations", (Parcelable) entity.getRelations());
        intent.putExtra("problems", (Parcelable) entity.getProblems());
        startActivity(intent);
    }
}