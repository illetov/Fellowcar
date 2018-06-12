package com.fellowcar.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fellowcar.android.R;
import com.fellowcar.android.data.adapters.TripAdapter;
import com.fellowcar.android.data.realm.TripRealmObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class ModelTripsFragment extends Fragment {
    private Realm realm;
    private RealmResults<TripRealmObject> results;

    public ModelTripsFragment() {
    }

    public static ModelTripsFragment newInstance() {
        return new ModelTripsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();
        results = realm.where(TripRealmObject.class).isNotNull("driverName").findAll();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_all_trip, container,
                false);
        TripAdapter adapter = new TripAdapter(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.list_all_trips);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new OnTripClickListener(getContext(), recyclerView,
                new OnTripClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), results.get(position).getDriverName(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getContext(), results.get(position).getPrice() + " P",
                                Toast.LENGTH_SHORT).show();
                    }
                }));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.d("ModelTripsFragment", "onCreateView: has been host fragment");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        this.getActivity().getFragmentManager().beginTransaction().addToBackStack(null);
    }

}
