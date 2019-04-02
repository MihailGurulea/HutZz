package io.ideaction.hutzz.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ideaction.hutzz.R;

public class CreateListingActivity extends AppCompatActivity {

    private static final String TAG = "CreateListingActivity";

    @BindView(R.id.s_address)
    Spinner mAddressSpinner;
    @BindView(R.id.s_state)
    Spinner mStateSpinner;
    @BindView(R.id.s_city)
    Spinner mCitySpinner;
    @BindView(R.id.tv_apartment)
    TextView mTVApartment;
    @BindView(R.id.tv_condo)
    TextView mTVCondo;
    @BindView(R.id.tv_house)
    TextView mTVHouse;
    @BindView(R.id.tv_penthouse)
    TextView mTVPenthouse;

    public static Intent startActivity(Context context) {
        return new Intent(context, CreateListingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        ButterKnife.bind(this);

        initSpinners();
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> countrySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, R.layout.country_spiner_listings);
        countrySpinnerAdapter.setDropDownViewResource(R.layout.country_spinner_dropdown);
        mAddressSpinner.setAdapter(countrySpinnerAdapter);

        ArrayAdapter<CharSequence> stateSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.state_spinner_array, R.layout.state_spiner_listings);
        stateSpinnerAdapter.setDropDownViewResource(R.layout.state_spinner_dropdown);
        mStateSpinner.setAdapter(stateSpinnerAdapter);

        ArrayAdapter<CharSequence> citySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.city_spinner_array, R.layout.city_spiner_listings);
        citySpinnerAdapter.setDropDownViewResource(R.layout.city_spinner_dropdown);
        mCitySpinner.setAdapter(citySpinnerAdapter);
    }

    @OnClick({R.id.tv_apartment, R.id.tv_condo, R.id.tv_house, R.id.tv_penthouse})
    void onClickPropertyType(View view){
        switch (view.getId()){
            case R.id.tv_apartment:
                changeUIButtons(mTVApartment, true);
                changeUIButtons(mTVCondo, false);
                changeUIButtons(mTVHouse, false);
                changeUIButtons(mTVPenthouse, false);
                break;
            case R.id.tv_condo:
                changeUIButtons(mTVApartment, false);
                changeUIButtons(mTVCondo, true);
                changeUIButtons(mTVHouse, false);
                changeUIButtons(mTVPenthouse, false);
                break;
            case R.id.tv_house:
                changeUIButtons(mTVApartment, false);
                changeUIButtons(mTVCondo, false);
                changeUIButtons(mTVHouse, true);
                changeUIButtons(mTVPenthouse, false);
                break;
            case R.id.tv_penthouse:
                changeUIButtons(mTVApartment, false);
                changeUIButtons(mTVCondo, false);
                changeUIButtons(mTVHouse, false);
                changeUIButtons(mTVPenthouse, true);
                break;
            default:
                changeUIButtons(mTVApartment, false);
                changeUIButtons(mTVCondo, false);
                changeUIButtons(mTVHouse, false);
                changeUIButtons(mTVPenthouse, false);
        }
    }

    private void changeUIButtons(TextView button, boolean pressed) {
        if (pressed) {
            button.setBackgroundResource(R.drawable.green_bg_round_corners_19dp_radius);
            button.setTextColor(Color.WHITE);
        } else {
            button.setBackgroundResource(R.drawable.grey_bg_round_corners_19dp_radius);
            button.setTextColor(Color.BLACK);
        }
    }
}
