package com.example.gkude.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gkude.EntityViewActivity;
import com.example.gkude.Manager;
import com.example.gkude.R;
import com.example.gkude.adapter.EntityCollectionAdapter;
import com.example.gkude.bean.EntityBean;
import com.orm.SugarContext;
import com.example.gkude.utils.CategoryUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

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
    private RefreshLayout refreshLayout;
    CategoryUtil cu = new CategoryUtil();
    Pair<String, String> p;

    public EntityCollectionFragment(final String tag) {
        this.TAG = tag;
        Log.e("EntityCollectionFragment", TAG);
        p = cu.getSearchKeyword(TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("EntityCollectionFragmentonCreate", TAG);
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

        initObserver();
        initSwipeRefresh(rootView);
        return rootView;
    }

    private void initObserver() {
        observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG,"observer subscribed");
            }
            @Override
            public void onNext(@NonNull List<EntityBean> entities) {
                Log.i(TAG,"getList");
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    for (EntityBean n : entities) {
                        if (!EntityBean.findWithQuery(EntityBean.class, "SELECT * FROM ENTITY_BEAN WHERE uri = " + "'" + n.getUri()+ "'").isEmpty()) {
                            n.setVisited(true);
                        }else {
                            n.setVisited(false);
                        }
                    }
                    mAdapter.setEntityList(entities);
                } else {
                    mAdapter.setEntityList(entities);
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.e("EntityCollection", e.getMessage());
            }
            @Override
            public void onComplete() {
                Log.i(TAG,"Complete");
            }
        };
        Manager.searchEntity(p.first, p.second, null,observer);
    }

    private void initSwipeRefresh(View rootView) {
        refreshLayout = rootView.findViewById(R.id.swipe_refresh);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this.getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this.getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e(TAG, "onRefresh: ");
                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.
                Manager.searchEntity(p.first, p.second, null,observer);
            }
        });
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        // Go to the detailed page
        Intent intent = new Intent(getActivity(), EntityViewActivity.class);
        intent.putExtra("entity_id", entity.getId());
        intent.putExtra("entity_label", entity.getLabel());
        intent.putExtra("entity_course", entity.getCourse());
        intent.putExtra("entity_category", entity.getCategory());
        intent.putExtra("entity_uri", entity.getUri());
        startActivity(intent);
    }
}