package com.unisonpharmaceuticals.utils;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class MitsAutoHeightListView extends LinearLayout {
    private static final int INVALID = -1;
    private BaseAdapter adapter;
    private DataSetObserver dataSetObserver;
    private final LayoutInflater layoutInflater;
    private int dividerViewResourceId;
    private View headerView;
    private View footerView;
    private MitsAutoHeightListView.OnItemClickListener itemClickListener;

    public MitsAutoHeightListView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MitsAutoHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.dividerViewResourceId = -1;
        this.layoutInflater = LayoutInflater.from(this.getContext());
        this.setOrientation(1);
    }

    public void setDividerView(int resourceId) {
        if (resourceId < 0) {
            throw new IllegalStateException("Resource Id cannot be negative");
        } else {
            this.dividerViewResourceId = resourceId;
        }
    }

    public void setOnItemClickListener(MitsAutoHeightListView.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setHeaderView(View view) {
        this.headerView = view;
    }

    public void setHeaderView(int resourceId) {
        this.headerView = this.layoutInflater.inflate(resourceId, this, false);
    }

    public void setFooterView(View view) {
        this.footerView = view;
    }

    public void setFooterView(int resourceId) {
        this.footerView = this.layoutInflater.inflate(resourceId, this, false);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter may not be null");
        } else {
            if (this.adapter != null && this.dataSetObserver != null) {
                this.adapter.unregisterDataSetObserver(this.dataSetObserver);
            }

            this.adapter = adapter;
            this.dataSetObserver = new MitsAutoHeightListView.AdapterDataSetObserver();
            this.adapter.registerDataSetObserver(this.dataSetObserver);
            this.resetList();
            this.refreshList();
        }
    }

    private void refreshList() {
        if (this.headerView != null) {
            this.addView(this.headerView);
        }

        int count = this.adapter.getCount();

        for(int i = 0; i < count; ++i) {
            final View view = this.adapter.getView(i, (View)null, this);
            int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (MitsAutoHeightListView.this.itemClickListener != null) {
                        MitsAutoHeightListView.this.itemClickListener.onItemClick(MitsAutoHeightListView.this.adapter.getItem(finalI), view, finalI);
                    }

                }
            });
            this.addView(view);
            if (this.dividerViewResourceId != -1 && i != count - 1) {
                this.addView(this.layoutInflater.inflate(this.dividerViewResourceId, this, false));
            }
        }

        if (this.footerView != null) {
            this.addView(this.footerView);
        }

    }

    private void resetList() {
        this.removeAllViews();
        this.invalidate();
    }

    class AdapterDataSetObserver extends DataSetObserver {
        AdapterDataSetObserver() {
        }

        public void onChanged() {
            super.onChanged();
            MitsAutoHeightListView.this.resetList();
            MitsAutoHeightListView.this.refreshList();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Object var1, View var2, int var3);
    }
}