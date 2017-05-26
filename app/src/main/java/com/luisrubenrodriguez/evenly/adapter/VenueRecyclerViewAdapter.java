package com.luisrubenrodriguez.evenly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luisrubenrodriguez.evenly.R;
import com.luisrubenrodriguez.evenly.model.Venue;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GamingMonster on 22.05.2017.
 * RecyclerView Adapter for the venues list.
 */

public class VenueRecyclerViewAdapter extends RecyclerView.Adapter<VenueRecyclerViewAdapter.VenueRecyclerViewHolder> {

    private List<Venue> mVenues;
    private Context mContext;
    private OnVenueItemClickListener mOnVenueItemClickListener;

    /**
     * Interface that MainActivity must implement, it will start the Venue Activity
     * Clearer delegation of responsibilities.
     */
    public interface OnVenueItemClickListener {
        void onItemClick(String venue_id);
    }

    public void setOnVenueItemClickListener(OnVenueItemClickListener onVenueItemClickListener) {
        mOnVenueItemClickListener = onVenueItemClickListener;
    }

    public VenueRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VenueRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.venue_listitem, parent, false);
        return new VenueRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenueRecyclerViewHolder holder, int position) {
        Venue venue = mVenues.get(position);
        holder.name.setText(venue.getName());
        if (venue.getLocation().getAddress() != null) {
            holder.address.setText(venue.getLocation().getAddress());
        } else {
            holder.address.setText(mContext.getString(R.string.empty_address));
        }
        holder.distance.setText(mContext.getString(R.string.distance, venue.getLocation().getDistance().toString()));
    }

    @Override
    public int getItemCount() {
        return ((mVenues != null) && (mVenues.size() != 0)) ? mVenues.size() : 0;
    }

    public void loadNewData(List<Venue> newVenues) {
        mVenues = newVenues;
        notifyDataSetChanged();
    }

    class VenueRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.venue_name)
        TextView name;
        @BindView(R.id.venue_address)
        TextView address;
        @BindView(R.id.venue_distance)
        TextView distance;

        private VenueRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnVenueItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mOnVenueItemClickListener.onItemClick(mVenues.get(position).getId());
                        }
                    }
                }
            });
        }
    }
}
