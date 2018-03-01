package com.bindinglistview.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;


public class ReactListManager extends ViewGroupManager<RecyclerView> {

    private int poolSize;
    private float height;
    private ReactListAdapter adapter;
    private ReadableMap bindings;

    @Override
    public String getName() {
        return "RCTBindingListView";
    }

    @Override
    protected RecyclerView createViewInstance(ThemedReactContext reactContext) {
        adapter = new ReactListAdapter();
        RecyclerView list = new RecyclerView(reactContext);
        list.setLayoutManager(new LinearLayoutManager(reactContext));
        list.setItemViewCacheSize(poolSize);
        return list;
    }

    @ReactProp(name = "rowHeight")
    public void setRowHeight(RecyclerView view, float height) {
        this.height = height;
    }

    @ReactProp(name = "poolSize")
    public void setPoolSize(RecyclerView view, int poolSize) {
        this.poolSize = poolSize;
    }

    @ReactProp(name = "binding")
    public void setBinding(RecyclerView view, ReadableMap binding) {
        Log.i("NIGA", "list = " + binding.toString());
        this.bindings = binding;
        adapter.setBindings(binding);
    }

    @ReactProp(name = "rows")
    public void setRows(RecyclerView view, ReadableArray rows) {
        adapter.setData(rows);
    }

    @Override
    public void addView(RecyclerView parent, View child, int index) {
        ReactCell cell = (ReactCell) child;
        cell.setRootBindings(this.bindings);
        cell.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, parent.getResources().getDisplayMetrics()));
        adapter.addCell(cell);
        if (poolSize == adapter.getPoolSize()) {
            parent.setAdapter(adapter);
        }
    }
}
