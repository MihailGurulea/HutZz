package io.ideaction.hutzz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.ideaction.hutzz.HutzzApplication;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.LogInActivity;
import io.ideaction.hutzz.activities.MainActivity;
import io.ideaction.hutzz.models.FacebookUserModel;
import io.ideaction.hutzz.utils.FacebookSignInHelper;
import io.ideaction.hutzz.utils.UtilsNetwork;

public class LogInFragment extends Fragment {

    private static final String TAG = "LogInFragment";
    private static final String EMAIL = "email";

    @BindView(R.id.btn_facebook)
    Button mBTNFaceBook;

    private LogInActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;
    private FacebookSignInHelper mFacebookSignInHelper;

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_log_in, container, false);

        initFragment();

        return mView;
    }

    private void initFragment() {
        mActivity = (LogInActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);
    }

    @OnClick(R.id.btn_email)
    void onClickRegisterWithEmail() {
        mActivity.onClickSignUp();
    }

    @OnClick(R.id.btn_facebook)
    void onClickFaceBook() {
        new UtilsNetwork(mActivity, isNetwork -> {
            if (isNetwork) {
                mActivity.blockUI();
                mFacebookSignInHelper = FacebookSignInHelper.newInstance(
                        mActivity,
                        token -> {
                            mActivity.unblockUI();
                            mActivity.startMainActivity();
                            HutzzApplication.getInstance().setToken(token);
                        },
                        () -> {
                            mActivity.unblockUI();
                            Toast.makeText(mActivity, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                        },
                        error -> {
                            mActivity.unblockUI();
                            Log.e(TAG, error.getMessage(), error);
                            Toast.makeText(mActivity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                );
                mFacebookSignInHelper.loginUser();
            } else {
                Toast.makeText(mActivity, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    @OnClick(R.id.ll_sign_in)
    void onClickSignIn() {
        mActivity.onClickSignIn();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookSignInHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
