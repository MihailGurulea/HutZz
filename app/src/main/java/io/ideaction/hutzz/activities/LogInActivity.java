package io.ideaction.hutzz.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ideaction.hutzz.R;
import io.ideaction.hutzz.fragments.ForgotPasswordFragment;
import io.ideaction.hutzz.fragments.LogInFragment;
import io.ideaction.hutzz.fragments.SignInFragment;
import io.ideaction.hutzz.fragments.SignUpFragment;
import io.ideaction.hutzz.utils.FragmentEntranceSide;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    public static Intent startActivity(Context context) {
        return new Intent(context, LogInActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initActivity();
    }

    private void initActivity() {
        ButterKnife.bind(this);

        setFragmentAddToStack(LogInFragment.newInstance(), FragmentEntranceSide.STANDARD);
    }

    public void setFragmentAddToStack(Fragment fragment, FragmentEntranceSide fragmentEntranceSide) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
        fragmentTransaction.replace(R.id.log_in_activity_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName().toUpperCase());
        fragmentTransaction.commit();
    }

    public void onClickSignIn() {
        setFragmentAddToStack(SignInFragment.newInstance(), FragmentEntranceSide.STANDARD);
    }

    public void onClickSignUp() {
        setFragmentAddToStack(SignUpFragment.newInstance(), FragmentEntranceSide.STANDARD);
    }

    public void onClickForgotPassword() {
        setFragmentAddToStack(ForgotPasswordFragment.newInstance(), FragmentEntranceSide.RIGHT);
    }

    public void startMainActivity(){
        startActivity(MainActivity.startActivity(LogInActivity.this));
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

    public void blockUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void unblockUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.log_in_activity_container) instanceof LogInFragment) {
            finishAffinity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.log_in_activity_container);
        if (fragment instanceof LogInFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
