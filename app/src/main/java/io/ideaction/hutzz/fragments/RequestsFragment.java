package io.ideaction.hutzz.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.MainActivity;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";
    private static final int APPLIED = 1000;
    private static final int MY_LISTING = 1001;

    @BindView(R.id.btn_create)
    Button mBTNCreate;
    @BindView(R.id.tv_applied)
    TextView mTVApplied;
    @BindView(R.id.tv_my_listing)
    TextView mTVMyListing;
    @BindView(R.id.tv_regular_font_statement)
    TextView mTVRegularStatement;
    @BindView(R.id.iv_image)
    ImageView mIVImage;

    private MainActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_requests, container, false);

        initFragment();

        return mView;
    }

    private void initFragment() {
        mActivity = (MainActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);

        changeUIOnClick(APPLIED);
    }

    @OnClick(R.id.tv_applied)
    void onClickApplied() {
        changeUIOnClick(APPLIED);
    }

    @OnClick(R.id.tv_my_listing)
    void onClickMyListing() {
        changeUIOnClick(MY_LISTING);
    }

    /**
     * This is a setter for the UI of the two buttons: APPLIED and MY LISTING
     *
     * @param type = one of the two constants: APPLIED or MY_LISTING
     */
    private void changeUIOnClick(int type) {
        switch (type) {
            case MY_LISTING:
                mBTNCreate.setVisibility(View.VISIBLE);
                mTVApplied.setTextColor(getResources().getColor(R.color.white_opacity_70));
                mTVApplied.setBackgroundResource(0);
                mTVMyListing.setTextColor(Color.WHITE);
                mTVMyListing.setBackgroundResource(R.drawable.tv_bottom_line);
                mIVImage.setImageResource(R.drawable.ic_mountains);
                break;

            default:
                mBTNCreate.setVisibility(View.GONE);
                mTVApplied.setTextColor(Color.WHITE);
                mTVApplied.setBackgroundResource(R.drawable.tv_bottom_line);
                mTVMyListing.setTextColor(getResources().getColor(R.color.white_opacity_70));
                mTVMyListing.setBackgroundResource(0);
                mIVImage.setImageResource(R.drawable.ic_building);
                break;
        }
    }

    @OnClick(R.id.btn_create)
    void onClickCreateListing(){
        mActivity.startCreateListingActivity();
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
}
