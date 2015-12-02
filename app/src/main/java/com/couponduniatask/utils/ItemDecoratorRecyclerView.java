package com.couponduniatask.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoratorRecyclerView extends RecyclerView.ItemDecoration {
    private int spaceTop;
    private int spaceBottom;
    private int spaceLeft;
    private int spaceRight;

    public ItemDecoratorRecyclerView(int spaceTop, int spaceBottom, int spaceLeft, int spaceRight) {
        this.spaceBottom = spaceBottom;
        this.spaceLeft = spaceLeft;
        this.spaceRight = spaceRight;
        this.spaceTop = spaceTop;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) return;
        outRect.top = spaceTop;
        outRect.left = spaceLeft;
        outRect.right = spaceRight;
        outRect.bottom = spaceBottom;

    }
}

