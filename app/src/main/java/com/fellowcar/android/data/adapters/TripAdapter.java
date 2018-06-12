package com.fellowcar.android.data.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fellowcar.android.R;
import com.fellowcar.android.data.realm.RealmController;
import com.fellowcar.android.data.realm.TripRealmObject;
import com.fellowcar.android.ui.custom.RoundTransform;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;


//THIS ADAPTER WORK WITH LIST OF INCOMING/OUTGOING ROUTES
// EXECUTE THINGS LIKE AS SHOW PHOTO DRIVER, DISTANCE AND NAME
// THIS ADAPTER IS COMPLETED

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private Context mContext;
    private Realm realm;

    public TripAdapter(Context context) {
        this.mContext = context;
        realm = Realm.getDefaultInstance();
    }

    class TripHolder extends RecyclerView.ViewHolder {

        public TextView name, age, price, from, to, distance, nearby;
        public ImageView photo;

        public TripHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_driver);
            age = view.findViewById(R.id.age_driver);
            price = view.findViewById(R.id.price_way);
            from = view.findViewById(R.id.first_way_driver);
            to = view.findViewById(R.id.last_way_driver);
            distance = view.findViewById(R.id.distance_way);
            nearby = view.findViewById(R.id.nearby_info);

            photo = view.findViewById(R.id.photo_driver);
        }
    }


    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_trip, parent, false);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {


        RealmResults<TripRealmObject> results = realm.where(TripRealmObject.class).isNotNull("driverName").findAll();
        TripRealmObject element = results.get(position);

        holder.distance.setText(String.valueOf(element.getDistanceWay()));
        holder.name.setText(element.getDriverName());
        holder.from.setText(element.getFrom());
        holder.to.setText(element.getTo());
        holder.price.setText(String.valueOf(element.getPrice()));
        holder.nearby.setText(String.valueOf(element.getDistanceNearby()));


        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        Picasso.get()
                .load(element.getDriverPhotoURL())
                .transform(new RoundTransform(100, 0))
                .error(R.drawable.ic_person_black_24dp)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return (int) realm.where(TripRealmObject.class).count();
    }
}
