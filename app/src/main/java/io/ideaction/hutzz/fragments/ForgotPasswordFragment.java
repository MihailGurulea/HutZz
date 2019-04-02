package io.ideaction.hutzz.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.ideaction.hutzz.HutzzApplication;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.activities.LogInActivity;
import io.ideaction.hutzz.models.UserCallModel;
import io.ideaction.hutzz.utils.UtilsNetwork;
import io.ideaction.hutzz.utils.Validations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = "ForgotPasswordFragment";

    @BindView(R.id.et_email)
    EditText mETEmail;

    private LogInActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;
    private Call<Void> mCall;

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        initFragment();

        return mView;
    }

    private void initFragment() {
        mActivity = (LogInActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);
    }


    @OnClick(R.id.iv_back_arrow)
    void onClickBackArrow() {
        mActivity.onBackPressed();
    }

    @OnClick(R.id.btn_submit)
    void onClickSubmit() {
        String email = String.valueOf(mETEmail.getText());

        mActivity.showProgressBar();
        if (!dataValidation(email)) {
            mActivity.hideProgressBar();
        } else {
            new UtilsNetwork(mActivity, isNetwork -> {
                if (isNetwork) {
                    forgotPasswordCall(email);
                } else {
                    Toast.makeText(mActivity, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }).execute();
        }
    }

    private void forgotPasswordCall(String email) {
        mCall = HutzzApplication.apiInterface().forgotPassword(new UserCallModel(email));

        mCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                mActivity.hideProgressBar();
                if (response.isSuccessful()) {
                    Toast.makeText(mActivity, getString(R.string.we_sent_a_new_password), Toast.LENGTH_SHORT).show();
                    mActivity.onClickSignIn();
                } else {
                    Toast.makeText(mActivity, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }


    private boolean dataValidation(String email) {
        if (TextUtils.isEmpty(email)) {
            mETEmail.setError(getString(R.string.email_field_is_empty));
            return false;
        } else if (Validations.isNotAValidEmail(email)) {
            mETEmail.setError(getString(R.string.provide_valid_email));
            return false;
        }
        return true;
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

        if (mCall != null) {

            if (mCall.isExecuted()) {
                mCall.cancel();
            }

            if (mCall.isCanceled()) {
                mCall = null;
            }
        }
    }
}
