package io.ideaction.hutzz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.ideaction.hutzz.HutzzApplication;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.MainActivity;
import io.ideaction.hutzz.adapters.ListingsAdapter;
import io.ideaction.hutzz.utils.FragmentEntranceSide;
import io.ideaction.hutzz.utils.Utils;

public class ProfileFragment extends Fragment implements ListingsAdapter.ListingAdapterListener {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.civ_profile_pic)
    CircleImageView mCIVProfilePic;
    @BindView(R.id.tv_name)
    TextView mTVName;
    @BindView(R.id.tv_location)
    TextView mTVLocation;
    @BindView(R.id.tv_phone)
    TextView mTVPhone;
    @BindView(R.id.tv_email)
    TextView mTVEmail;
    @BindView(R.id.tv_placeholder)
    TextView mTVPlaceholder;
    @BindView(R.id.tv_listing)
    TextView mTVListing;
    @BindView(R.id.rv_listings)
    RecyclerView mRecyclerView;

    private MainActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Integer> mListings;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        initFragment();

        return mView;
    }

    private void initFragment() {
        mActivity = (MainActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);

        initTextViews();
        initProfilePicture();
        initList();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new ListingsAdapter(mListings, this);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initList() {
        mListings = new ArrayList<>();
        mListings.add(R.drawable.jpg_listing_example1);
        mListings.add(R.drawable.jpg_listing_example2);
        mListings.add(R.drawable.jpg_listing_example3);
        mListings.add(R.drawable.jpg_listing_example1);
        mListings.add(R.drawable.jpg_listing_example2);
        mListings.add(R.drawable.jpg_listing_example3);
        mListings.add(R.drawable.jpg_listing_example1);
        mListings.add(R.drawable.jpg_listing_example2);
        mListings.add(R.drawable.jpg_listing_example3);
    }

    @SuppressLint("SetTextI18n")
    private void initTextViews() {
        mTVName.setText(HutzzApplication.getInstance().getUserFirstName() + " " + HutzzApplication.getInstance().getUserLastName());
        mTVLocation.setText(HutzzApplication.getInstance().getUserLocation());
        mTVPhone.setText(HutzzApplication.getInstance().getUserPhone());
        mTVEmail.setText(HutzzApplication.getInstance().getUserEmail());

        String listings = String.valueOf("9 Listings");
        SpannableString spannedString = Utils.setCustomFontTypeAndSize(mActivity, listings, 0,
                1, R.font.sf_ui_text_bold, 1.5f);
        mTVListing.setText(spannedString);
    }

    private void initProfilePicture() {
        if (HutzzApplication.getInstance().getAvatar().equals("")) {
            if (!HutzzApplication.getInstance().getUserFirstName().equals("")) {
                mTVPlaceholder.setText(String.valueOf(HutzzApplication.getInstance().getUserFirstName().charAt(0)).toUpperCase());
            } else {
                mTVPlaceholder.setText("H");
            }
        }
    }

    @Override
    public void onClickCell(int position) {
        Toast.makeText(mActivity, "Opening the selected listing", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_edit)
    void onClickEdit() {
        mActivity.setFragmentAddToStack(EditProfileFragment.newInstance(), FragmentEntranceSide.STANDARD);
    }

    @OnClick(R.id.tv_log_out)
    void onClickLogOut() {
        mActivity.onClickSignOut();
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
