package com.fellowcar.android.data.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fellowcar.android.R;
import com.fellowcar.android.data.network.model.main.GeoPointResponce;

import java.util.List;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.GeoholderItem> {

    private List<GeoPointResponce> geopointList;

    public RecycleViewAdapter(List<GeoPointResponce> geopointList) {
        this.geopointList = geopointList;
    }

    class GeoholderItem extends RecyclerView.ViewHolder {

        private TextView city;
        private TextView meta;

        public GeoholderItem(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.cityName);
            meta = itemView.findViewById(R.id.cityRegionInfo);
        }
    }

    @Override
    public GeoholderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_geo_template, parent, false);
        return new GeoholderItem(view);
    }

    @Override
    public void onBindViewHolder(GeoholderItem holder, int position) {
        GeoPointResponce geopoint = geopointList.get(position);
        holder.city.setText(geopoint.getCity());
        holder.meta.setText(geopoint.getMetaInfo());
    }

    @Override
    public int getItemCount() {
        return geopointList.size();
    }
}
