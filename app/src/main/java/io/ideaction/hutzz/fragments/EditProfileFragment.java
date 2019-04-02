package io.ideaction.hutzz.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.ideaction.hutzz.HutzzApplication;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.MainActivity;
import io.ideaction.hutzz.utils.FragmentEntranceSide;
import io.ideaction.hutzz.utils.Validations;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "EditProfileFragment";
    private static final int RC_FILE_PICKER_PERM = 10001;
    private static final int PICK_IMAGE = 10000;
    private static final String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @BindView(R.id.et_first_name)
    EditText mETFirstName;
    @BindView(R.id.et_last_name)
    EditText mETLastName;
    @BindView(R.id.et_location)
    EditText mETLocation;
    @BindView(R.id.et_phone)
    EditText mETPhone;
    @BindView(R.id.et_email)
    EditText mETEmail;
    @BindView(R.id.civ_profile_pic)
    CircleImageView mCIVProfilePic;
    @BindView(R.id.tv_placeholder)
    TextView mTVPlaceholder;

    private MainActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initFragment();

        return mView;
    }

    private void initFragment() {
        mActivity = (MainActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);

        if (HutzzApplication.getInstance().getAvatar().equals("")) {

            if (!HutzzApplication.getInstance().getUserFirstName().equals("")) {
                mTVPlaceholder.setText(String.valueOf(HutzzApplication.getInstance().getUserFirstName().charAt(0)).toUpperCase());
            } else {
                mTVPlaceholder.setText("H");
            }
        }

        if (MainActivity.isProfileDataSaved()) {
            mETFirstName.setText(HutzzApplication.getInstance().getUserFirstName());
            mETLastName.setText(HutzzApplication.getInstance().getUserLastName());
            mETLocation.setText(HutzzApplication.getInstance().getUserLocation());
            mETPhone.setText(HutzzApplication.getInstance().getUserPhone());
            mETEmail.setText(HutzzApplication.getInstance().getUserEmail());
        }

    }


    @OnClick(R.id.tv_done)
    void onClickDone() {
        if (isNewDataValid(mETFirstName.getText().toString(),
                mETLastName.getText().toString(),
                mETLocation.getText().toString(),
                mETPhone.getText().toString(),
                mETEmail.getText().toString())) {
            HutzzApplication.getInstance().setUserFirstName(mETFirstName.getText().toString());
            HutzzApplication.getInstance().setUserLastName(mETLastName.getText().toString());
            HutzzApplication.getInstance().setUserLocation(mETLocation.getText().toString());
            HutzzApplication.getInstance().setUserPhone(mETPhone.getText().toString());
            HutzzApplication.getInstance().setUserEmail(mETEmail.getText().toString());

            mActivity.setFragmentAddToStack(ProfileFragment.newInstance(), FragmentEntranceSide.STANDARD);
        }
    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    @OnClick({R.id.civ_profile_pic, R.id.iv_camera})
    void onClickOnProfilePic() {
        if (EasyPermissions.hasPermissions(mActivity, PERMS)) {
            openGallery();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.ratiolate_gallery), RC_FILE_PICKER_PERM, PERMS);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private boolean isNewDataValid(String firstName, String lastName, String location, String phone, String email) {
        if (TextUtils.isEmpty(firstName)) {
            mETFirstName.setError("First name field is empty");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            mETLastName.setError("Last name field is empty");
            return false;
        }

        if (TextUtils.isEmpty(location)) {
            mETLocation.setError("Location field is empty");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            mETPhone.setError("Phone field is empty");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            mETEmail.setError("Email field is empty");
            return false;
        } else if (Validations.isNotAValidEmail(email)) {
            mETEmail.setError("You need to provide a valid E-mail");
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedPicture = data.getData();
            Glide.with(this).load(selectedPicture).into(mCIVProfilePic);
            mTVPlaceholder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
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
