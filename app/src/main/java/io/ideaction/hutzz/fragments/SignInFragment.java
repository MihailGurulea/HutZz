package io.ideaction.hutzz.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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


public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";

    @BindView(R.id.et_email)
    EditText mETEmail;
    @BindView(R.id.et_password)
    EditText mETPassword;

    private LogInActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;
    private Call<UserCallModel> mCall;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initFragment();

        return mView;
    }

    private void initFragment() {
        mActivity = (LogInActivity) getActivity();
        mUnbinder = ButterKnife.bind(this, mView);

        if (!HutzzApplication.getInstance().getUserEmail().equals("")) {
            mETEmail.setText(HutzzApplication.getInstance().getUserEmail());
        }

        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @OnClick(R.id.btn_sign_in)
    void onClickSignIn() {
        String email = String.valueOf(mETEmail.getText());
        String password = String.valueOf(mETPassword.getText());

        mActivity.showProgressBar();
        if (!dataValidation(email, password)) {
            mActivity.hideProgressBar();
        } else {
            new UtilsNetwork(mActivity, isNetwork -> {
                if (isNetwork) {
                    signInCallBack(email, password);
                } else {
                    Toast.makeText(mActivity, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }).execute();
        }
    }

    private void signInCallBack(String email, String password) {
        mCall = HutzzApplication.apiInterface().signIn(new UserCallModel(email, password));

        mCall.enqueue(new Callback<UserCallModel>() {
            @Override
            public void onResponse(Call<UserCallModel> call, Response<UserCallModel> response) {
                mActivity.hideProgressBar();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        HutzzApplication.getInstance().setToken(response.body().getToken());
                        HutzzApplication.getInstance().setAvatar(response.body().getUser().getAvatar());
                        mActivity.startMainActivity();
                    } else {
                        Toast.makeText(mActivity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, getString(R.string.email_already_taken), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserCallModel> call, Throwable t) {
                mActivity.hideProgressBar();
                Log.d(TAG, t.getMessage(), t);
                Toast.makeText(mActivity, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean dataValidation(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            mETEmail.setError(getString(R.string.email_field_is_empty));
            return false;
        } else if (Validations.isNotAValidEmail(email)) {
            mETEmail.setError(getString(R.string.provide_valid_email));
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            mETPassword.setError(getString(R.string.pass_is_empty));
            return false;
        }
        return true;
    }

    @OnClick(R.id.iv_back_arrow)
    void onClickBackArrow() {
        mActivity.onBackPressed();
    }

    @OnClick(R.id.ll_sign_up)
    void onClickSignUp() {
        mActivity.onClickSignUp();
    }

    @OnClick(R.id.tv_forgot_password)
    void onClickForgotPass() {
        mActivity.onClickForgotPassword();
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
