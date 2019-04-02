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

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";

    @BindView(R.id.et_username)
    EditText mETUsername;
    @BindView(R.id.et_email)
    EditText mETEmail;
    @BindView(R.id.et_password)
    EditText mETPassword;
    @BindView(R.id.et_confirm_password)
    EditText mETConfirmPassword;

    private LogInActivity mActivity;
    private Unbinder mUnbinder;
    private View mView;

    private Call<Void> mCall;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initFragment();

        mETUsername.setText("Mihail");
        mETEmail.setText("asdfghj@asdfghj.com");
        mETPassword.setText("Aa1234");
        mETConfirmPassword.setText("Aa1234");

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

    @OnClick(R.id.ll_sign_in)
    void onClickSignInBottomText() {
        mActivity.onClickSignIn();
    }

    @OnClick(R.id.btn_sign_up)
    void onClickSignUp() {
        String username = String.valueOf(mETUsername.getText());
        String email = String.valueOf(mETEmail.getText());
        String password = String.valueOf(mETPassword.getText());
        String confirmPassword = String.valueOf(mETConfirmPassword.getText());

        mActivity.showProgressBar();
        if (!isRegistrationDataValid(username, email, password, confirmPassword)) {
            mActivity.hideProgressBar();
        } else {
            new UtilsNetwork(mActivity, isNetwork -> {
                if (isNetwork) {
                    signUpCallBack(username, email, password);
                } else {
                    Toast.makeText(mActivity, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }).execute();
        }
    }

    private void signUpCallBack(String username, String email, String password) {
        mCall = HutzzApplication.apiInterface().register(new UserCallModel(username, email, password));

        mCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mActivity.hideProgressBar();
                if (response.isSuccessful()) {
                    HutzzApplication.getInstance().setUserCredentials(new UserCallModel(
                            username,
                            email,
                            password
                    ));
                    mActivity.startMainActivity();
                } else {
                    Toast.makeText(mActivity, getString(R.string.email_already_taken), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mActivity.hideProgressBar();
                Log.d(TAG, t.getMessage(), t);
                Toast.makeText(mActivity, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isRegistrationDataValid(String username, String email, String password, String repeatPassword) {
        if (TextUtils.isEmpty(username)) {
            mETUsername.setError(getString(R.string.username_empty));
            return false;
        } else if (username.toCharArray().length < 3) {
            mETUsername.setError(getString(R.string.username_requirements));
            return false;
        }

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
        } else if (!Validations.isValidPassword(password)) {
            mETPassword.setError(getString(R.string.password_requirements));
            return false;
        }

        if (TextUtils.isEmpty(repeatPassword)) {
            mETConfirmPassword.setError(getString(R.string.confirm_pass_empty));
            return false;
        } else if (!Validations.isPasswordMatching(password, repeatPassword)) {
            mETConfirmPassword.setError(getString(R.string.pass_should_match));
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
    }
}
