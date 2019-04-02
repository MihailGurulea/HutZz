package io.ideaction.hutzz.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ideaction.hutzz.R;

public class ListingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FIRST_CELL = 1000;
    private static final int CELL = 1001;
    private static final int LAST_CELL = 1002;

    private List<Integer> mListings;
    private ListingAdapterListener mListingAdapterListener;

    public ListingsAdapter(List<Integer> listings, ListingAdapterListener listingAdapterListener) {
        mListings = listings;
        mListingAdapterListener = listingAdapterListener;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == FIRST_CELL) {
            return new FirstViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_first_listing, viewGroup, false));
        } else if (i == LAST_CELL) {
            return new LastViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_last_listing, viewGroup, false));
        } else {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_linsting, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == CELL) {
            Integer listing = mListings.get(i);

            if (listing != null) {
                ((ViewHolder) viewHolder).mIVPropertyImage.setImageResource(listing);
            }

        } else if (getItemViewType(i) == FIRST_CELL) {
            Integer listing = mListings.get(i);

            if (listing != null) {
                ((FirstViewHolder) viewHolder).mIVFirstPropertyImage.setImageResource(listing);
            }

        } else {
            Integer listing = mListings.get(i);

            if (listing != null) {
                ((LastViewHolder) viewHolder).mIVLastPropertyImage.setImageResource(listing);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListings.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST_CELL;
        } else if (position == mListings.size() - 1) {
            return LAST_CELL;
        } else {
            return CELL;
        }
    }

    public interface ListingAdapterListener {
        void onClickCell(int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        ImageView mIVPropertyImage;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.image_view)
        void onClickListing() {
            mListingAdapterListener.onClickCell(getAdapterPosition());
        }
    }

    class FirstViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        ImageView mIVFirstPropertyImage;


        FirstViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.image_view)
        void onClickFirstListing() {
            mListingAdapterListener.onClickCell(getAdapterPosition());
        }
    }

    class LastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        ImageView mIVLastPropertyImage;


        LastViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.image_view)
        void onClickLastListing() {
            mListingAdapterListener.onClickCell(getAdapterPosition());
        }
    }
}