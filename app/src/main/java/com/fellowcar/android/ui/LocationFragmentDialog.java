package com.fellowcar.android.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.fellowcar.android.R;
import com.fellowcar.android.data.adapters.RecycleViewAdapter;
import com.fellowcar.android.data.network.model.main.GeoPointResponce;

import java.util.ArrayList;
import java.util.List;


public class LocationFragmentDialog extends DialogFragment {

    SearchView mSearchViewLocation;
    RecyclerView mRecycleView;
    List<GeoPointResponce> geopointList;
    RecycleViewAdapter recycleViewAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_dialog_view);

        mSearchViewLocation = dialog.findViewById(R.id.searchView);
        mSearchViewLocation.setQueryHint("Enter your location");

        mRecycleView = dialog.findViewById(R.id.recycleResultGeocode);
        geopointList = new ArrayList<>();
        recycleViewAdapter = new RecycleViewAdapter(geopointList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(dialog.getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(dialog.getContext(), mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                TextView viewById = getF.findViewById(R.id.textView);
//                viewById.setText(geopointList.get(position).getCity());
                dialog.dismiss();
            }
        }));
        mRecycleView.setAdapter(recycleViewAdapter);
        applyDataSet();
        //set full screen mode
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }


    private void applyDataSet() {
        GeoPointResponce geopoint = new GeoPointResponce("Gelendjik", "RU FR 255458 Russia Kr krai");
        GeoPointResponce geopoint2 = new GeoPointResponce("Anapa", "RU FR 8958 Russia Kr krai");
        GeoPointResponce geopoint3 = new GeoPointResponce("Sochi", "RU FR 54458 Russia Kr krai");

        geopointList.add(geopoint);
        geopointList.add(geopoint2);
        geopointList.add(geopoint3);
        recycleViewAdapter.notifyDataSetChanged();
    }

}