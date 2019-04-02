package io.ideaction.hutzz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.ideaction.hutzz.HutzzApplication;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.fragments.BookmarksFragment;
import io.ideaction.hutzz.fragments.EditProfileFragment;
import io.ideaction.hutzz.fragments.ProfileFragment;
import io.ideaction.hutzz.fragments.RequestsFragment;
import io.ideaction.hutzz.fragments.SearchFragment;
import io.ideaction.hutzz.utils.FragmentEntranceSide;

import static io.ideaction.hutzz.utils.Constants.ERROR_DIALOG_REQUEST;
import static io.ideaction.hutzz.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static io.ideaction.hutzz.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;
    @BindView(R.id.bottom_nav_menu)
    BottomNavigationView mBottomNavigationView;

    private boolean mLocationPermissionGranted = false;

    public static Intent startActivity(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static boolean isProfileDataSaved() {
        return !HutzzApplication.getInstance().getUserFirstName().equals("") &&
                !HutzzApplication.getInstance().getUserLastName().equals("") &&
                !HutzzApplication.getInstance().getUserLocation().equals("") &&
                !HutzzApplication.getInstance().getUserPhone().equals("") &&
                !HutzzApplication.getInstance().getUserEmail().equals("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setBottomNavigationView();
    }

    private void setBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

            switch (menuItem.getItemId()) {
                case R.id.nav_search:
                    if (!(fragment instanceof SearchFragment)) {
                        MainActivity.this.setFragmentAddToStack(SearchFragment.newInstance(), FragmentEntranceSide.STANDARD);
                    }
                    break;
                case R.id.nav_bookmarks:
                    if (!(fragment instanceof BookmarksFragment)) {
                        MainActivity.this.setFragmentAddToStack(BookmarksFragment.newInstance(), FragmentEntranceSide.STANDARD);
                    }
                    break;
                case R.id.nav_requests:
                    if (!(fragment instanceof RequestsFragment)) {
                        MainActivity.this.setFragmentAddToStack(RequestsFragment.newInstance(), FragmentEntranceSide.STANDARD);
                    }
                    break;
                case R.id.nav_profile:
                    if (!(fragment instanceof EditProfileFragment || fragment instanceof ProfileFragment)) {
                        if (isProfileDataSaved()) {
                            MainActivity.this.setFragmentAddToStack(ProfileFragment.newInstance(), FragmentEntranceSide.STANDARD);
                        } else {
                            MainActivity.this.setFragmentAddToStack(EditProfileFragment.newInstance(), FragmentEntranceSide.STANDARD);
                        }
                    }
                    break;
            }
            return true;
        });
    }

    public void setFragmentAddToStack(Fragment fragment, FragmentEntranceSide fragmentEntranceSide) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        switch (fragmentEntranceSide) {
            case RIGHT:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
                break;
            case LEFT:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_left);
                break;
            case BOTTOM:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom, R.anim.exit_from_bottom, R.anim.enter_from_bottom);
                break;
        }
        fragmentTransaction.replace(R.id.main_activity_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName().toUpperCase());
        fragmentTransaction.commit();
    }

    public void startCreateListingActivity(){
        startActivity(CreateListingActivity.startActivity(MainActivity.this));
    }

    public void showProgressBar() {
        if (mProgressBar != null && mProgressBar.getVisibility() == View.GONE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void onClickSignOut() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do you really want to log out?")
                .setConfirmText("Yes, I do!")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    startActivity(LogInActivity.startActivity(MainActivity.this));
                    MainActivity.this.finishAffinity();
                })
                .setCancelButton("Nope", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

        if (fragment instanceof SearchFragment || fragment == null) {

            if (checkMapServices()) {

                if (mLocationPermissionGranted) {
                    mBottomNavigationView.setSelectedItemId(R.id.nav_search);
                } else {
                    getLocationPermission();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

        if (fragment instanceof BookmarksFragment || fragment instanceof RequestsFragment || fragment instanceof ProfileFragment) {
            mBottomNavigationView.setSelectedItemId(R.id.nav_search);
        } else {
            finishAffinity();
            System.exit(0);
        }
    }

    boolean checkMapServices() {
        if (isServicesOK()) {
            return isMapsEnabled();
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mBottomNavigationView.setSelectedItemId(R.id.nav_search);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

        if (fragment instanceof EditProfileFragment) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

        if (fragment instanceof EditProfileFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    mBottomNavigationView.setSelectedItemId(R.id.nav_search);
                } else {
                    getLocationPermission();
                }
            }
        }

    }
}
