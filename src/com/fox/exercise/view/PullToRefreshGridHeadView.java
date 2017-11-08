package com.fox.exercise.view;

import com.fox.exercise.R;
import com.fox.exercise.newversion.view.GridViewWithHeaderAndFooter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.GridView;


public class PullToRefreshGridHeadView extends PullToRefreshAdapterViewBase<GridViewWithHeaderAndFooter> {

    class InternalGridView extends GridViewWithHeaderAndFooter implements EmptyViewMethodAccessor {

        public InternalGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshGridHeadView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        @Override
        public ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }

    }

    public PullToRefreshGridHeadView(Context context) {
        super(context);
    }

    public PullToRefreshGridHeadView(Context context, int mode) {
        super(context, mode);
    }

    public PullToRefreshGridHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected final GridViewWithHeaderAndFooter createRefreshableView(Context context, AttributeSet attrs) {
        GridViewWithHeaderAndFooter gv = new InternalGridView(context, attrs);
        // Use Generated ID (from res/values/ids.xml)
        gv.setId(R.id.gridview);
        return gv;
    }

    @Override
    public ContextMenuInfo getContextMenuInfo() {
        return ((InternalGridView) getRefreshableView()).getContextMenuInfo();
    }
}
