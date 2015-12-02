package com.couponduniatask.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.couponduniatask.R;
import com.couponduniatask.network.Response.GetResponse;
import com.couponduniatask.utils.ThreadHelper;
import com.couponduniatask.utils.ViewEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter for {@code RecyclerView} based widgets
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements BasicAdapter, View.OnClickListener {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.00");

    protected List listItems;
    protected ViewEventListener viewEventListener;
    private boolean autoDataSetChanged = true;
    private final double latitude;
    private final double longitude;


    public RecyclerAdapter(List listItems, double latitude, double longitude) {
        this.listItems = listItems;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public void setItems(List items) {
        ThreadHelper.crashIfBackgroundThread();
        listItems = items;
        if (autoDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItem(Object item) {
        ThreadHelper.crashIfBackgroundThread();
        listItems.add(item);
        if (autoDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void delItem(Object item) {
        ThreadHelper.crashIfBackgroundThread();
        listItems.remove(item);
        if (autoDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void delItem(int pos) {
        ThreadHelper.crashIfBackgroundThread();
        listItems.remove(pos);
        if (autoDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItems(List items) {
        ThreadHelper.crashIfBackgroundThread();
        listItems.addAll(items);
        if (autoDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void clearItems() {
        ThreadHelper.crashIfBackgroundThread();
        listItems.clear();
        if (autoDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewEventListener getViewEventListener() {
        return viewEventListener;
    }

    @Override
    public void setViewEventListener(ViewEventListener viewEventListener) {
        this.viewEventListener = viewEventListener;
    }


    @Override
    public void setAutoDataSetChanged(boolean enabled) {
        this.autoDataSetChanged = enabled;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        final GetResponse.Data data = (GetResponse.Data) (listItems.get(position));
        if (data != null) {
            viewHolder.tvProfileName.setText(data.BrandName + "");
            viewHolder.tvProfileOffer.setText(data.NumCoupons + " Offer");

//            float distance = Float.parseFloat(data.Distance);


            float[] result1 = new float[3];
            double lat_1 = Double.parseDouble(data.Latitude);
            double long_1 = Double.parseDouble(data.Longitude);

            android.location.Location.distanceBetween(latitude, longitude, lat_1, long_1, result1);

            Float distance = result1[0];
            String location = DECIMAL_FORMAT.format(distance);

            viewHolder.tvProfileLocation.setText(location + "m " + data.NeighbourhoodName);
            viewHolder.ivProfile.setImageBitmap(null);
            viewHolder.ll_item.removeAllViews();
            Context context = viewHolder.ll_item.getContext();
            for (GetResponse.Category category : data.Categories) {
                TextView text = new TextView(context);
                text.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
                text.setText("" + category.Name);
                text.setGravity(Gravity.CENTER_VERTICAL);
                text.setSingleLine();
                text.setCompoundDrawablePadding(8);
                text.setPadding(10, 10, 10, 10);
                viewHolder.ll_item.addView(text);
            }

            Picasso.with(context)
                    .load(data.LogoURL)
                    .into(viewHolder.ivProfile);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = (Integer) v.getTag();

        if (viewEventListener != null) {
            viewEventListener.onViewEvent(listItems.get(pos), pos, v);
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView ivProfile;
        TextView tvProfileName;
        TextView tvProfileOffer;
        TextView tvProfileLocation;
        LinearLayout ll_item;

        public ViewHolder(View convertView) {
            super(convertView);
            progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
            ivProfile = (ImageView) convertView.findViewById(R.id.iv_logo);
            tvProfileName = (TextView) convertView.findViewById(R.id.tv_profile_name);
            tvProfileOffer = (TextView) convertView.findViewById(R.id.tv_profile_offer);
            tvProfileLocation = (TextView) convertView.findViewById(R.id.tv_profile_location);
            ll_item = (LinearLayout) convertView.findViewById(R.id.ll_items);
        }
    }


}
