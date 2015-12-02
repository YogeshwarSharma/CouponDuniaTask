package com.couponduniatask.adapters;


import com.couponduniatask.utils.ViewEventListener;

import java.util.List;

/**
 * Common methods for adapters added by the library
 */
public interface BasicAdapter {
    void setItems(List items);

    void addItem(Object item);

    void delItem(Object item);

    void delItem(int pos);

    void addItems(List items);

    void clearItems();

    ViewEventListener getViewEventListener();

    void setViewEventListener(ViewEventListener viewEventListener);

    void setAutoDataSetChanged(boolean enabled);
}
