package io.ideaction.hutzz.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.MainActivity;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "SearchFragment";

    @BindView(R.id.map)
    MapView mMapFragment;

    private GoogleMap mGoogleMap;

    private MainActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        initFragment();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mMapFragment != null) {
            mMapFragment.onCreate(null);
            mMapFragment.getMapAsync(this);
        }
    }

    private void initFragment() {
        mActivity = (MainActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);

        // Adding custom markers
        drawMarker(47.065502, 28.865401, "First Marker");
        drawMarker(47.059098, 28.870939, "Second Marker");
        drawMarker(47.061366, 28.863286, "Third Marker");
        drawMarker(47.062973, 28.875216, "Fourth Marker");
        drawMarker(47.068355, 28.871584, "Fifth Marker");
        drawMarker(47.055767, 28.867720, "Sixth Marker");

        View locationButton = ((View) mMapFragment.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        locationButton.setVisibility(View.GONE);
        new Handler().postDelayed(locationButton::callOnClick, 2000);
    }

    private void drawMarker(double lat, double lon, String description) {
        if (mGoogleMap != null) {
            LatLng gps = new LatLng(lat, lon);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_geo_pin);
            mGoogleMap.addMarker(new MarkerOptions()
                    .icon(icon)
                    .position(gps)
                    .title(description));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapFragment.onPause();
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

        if(mGoogleMap != null){
            mGoogleMap = null;
        }
    }
}
