package com.example.clarence.corelibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.example.clarence.corelibrary.BaseHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class BaseListAdapter<T> extends BaseAdapter implements AbsListView.OnScrollListener {

    protected Collection<T> mDatas;
    protected final int mItemLayoutId;
    protected AbsListView mList;
    protected boolean isScrolling;
    protected Context mCxt;

    private AbsListView.OnScrollListener listener;

    public abstract void convert(BaseHolder helper, T item, boolean isScrolling);

    public BaseListAdapter(AbsListView view, Collection<T> mDatas, int itemLayoutId) {
        if (mDatas == null) {
            mDatas = new ArrayList<T>(0);
        }
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.mList = view;
        mCxt = view.getContext();
        mList.setOnScrollListener(this);
    }

    public void refresh(Collection<T> datas) {
        if (datas == null) {
            datas = new ArrayList<T>(0);
        }
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addOnScrollListener(AbsListView.OnScrollListener l) {
        this.listener = l;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatas instanceof List) {
            return ((List<T>) mDatas).get(position);
        } else if (mDatas instanceof Set) {
            return new ArrayList<T>(mDatas).get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position), isScrolling, position);
        return viewHolder.getConvertView();

    }

    private BaseHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return BaseHolder.get(convertView, parent, mItemLayoutId);
    }

    public void convert(BaseHolder helper, T item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 设置是否滚动的状态
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            isScrolling = false;
            this.notifyDataSetChanged();
        } else {
            isScrolling = true;
        }
        if (listener != null) {
            listener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (listener != null) {
            listener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }
}
