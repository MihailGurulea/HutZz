package io.ideaction.hutzz.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.models.Bookmark;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private List<Bookmark> mBookmarks;
    private BookmarksAdapterListener mBookmarksAdapterListener;

    public BookmarksAdapter(List<Bookmark> bookmarks, BookmarksAdapterListener bookmarksAdapterListener) {
        mBookmarks = bookmarks;
        mBookmarksAdapterListener = bookmarksAdapterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_bookmark, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (mBookmarks != null && mBookmarks.size() > 0) {
            Bookmark bookmark = mBookmarks.get(i);

            if (bookmark != null) {
                viewHolder.mIVMainImage.setImageResource(bookmark.getImage());
                viewHolder.mTVPropertyName.setText(bookmark.getProperty());
                viewHolder.mTVPropertyAddress.setText(bookmark.getPropertyAddress());
                viewHolder.mTVPrice.setText(bookmark.getPrice());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBookmarks.size();
    }


    public interface BookmarksAdapterListener {
        void onClickCell(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_main_image)
        ImageView mIVMainImage;
        @BindView(R.id.btn_saved)
        Button mBTNSaved;
        @BindView(R.id.tv_property)
        TextView mTVPropertyName;
        @BindView(R.id.tv_address)
        TextView mTVPropertyAddress;
        @BindView(R.id.tv_price)
        TextView mTVPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.rl_bookmark_cell)
        void onClickCell() {
            mBookmarksAdapterListener.onClickCell(getAdapterPosition());
        }
    }
}
