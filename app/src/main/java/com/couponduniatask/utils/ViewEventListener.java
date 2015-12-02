package com.couponduniatask.utils;

import android.view.View;

public interface ViewEventListener<T> {
    void onViewEvent(T item, int position, View view);
}
