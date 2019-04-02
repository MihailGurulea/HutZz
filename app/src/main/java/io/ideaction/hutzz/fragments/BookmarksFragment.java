package io.ideaction.hutzz.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.MainActivity;
import io.ideaction.hutzz.adapters.BookmarksAdapter;
import io.ideaction.hutzz.models.Bookmark;

public class BookmarksFragment extends Fragment implements BookmarksAdapter.BookmarksAdapterListener {

    private static final String TAG = "BookmarksFragment";

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    private MainActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Bookmark> mBookmarks;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        initFragment();
        initRecyclerView();

        return mView;
    }

    private void initFragment() {
        mActivity = (MainActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);

        mBookmarks = new ArrayList<>();
        mBookmarks.add(new Bookmark("1", R.drawable.jpg_room1, "Park Capitol", "8165 Eucalyptus Avenue, LA, USA", "$1,599/mo"));
        mBookmarks.add(new Bookmark("2", R.drawable.jpg_room2, "New York Central Park", "1155 Europa Avenue, LU, USA", "$3,599/mo"));
        mBookmarks.add(new Bookmark("3", R.drawable.jpg_room1, "Park Capitol", "8165 Eucalyptus Avenue, LA, USA", "$1,599/mo"));
        mBookmarks.add(new Bookmark("4", R.drawable.jpg_room2, "New York Central Park", "1155 Europa Avenue, LU, USA", "$3,599/mo"));
        mBookmarks.add(new Bookmark("5", R.drawable.jpg_room1, "Park Capitol", "8165 Eucalyptus Avenue, LA, USA", "$1,599/mo"));
        mBookmarks.add(new Bookmark("6", R.drawable.jpg_room2, "New York Central Park", "1155 Europa Avenue, LU, USA", "$3,599/mo"));
        mBookmarks.add(new Bookmark("7", R.drawable.jpg_room1, "Park Capitol", "8165 Eucalyptus Avenue, LA, USA", "$1,599/mo"));
        mBookmarks.add(new Bookmark("8", R.drawable.jpg_room2, "New York Central Park", "1155 Europa Avenue, LU, USA", "$3,599/mo"));
    }

    private void initRecyclerView() {
        mAdapter = new BookmarksAdapter(mBookmarks, this);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        if (mView != null) {
            mView = null;
        }

        if (mActivity != null) {
            mActivity = null;
        }
    }

    @Override
    public void onClickCell(int position) {

    }
}
