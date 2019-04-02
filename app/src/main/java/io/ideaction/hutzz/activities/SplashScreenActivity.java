package io.ideaction.hutzz.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ideaction.hutzz.HutzzApplication;
import io.ideaction.hutzz.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (TextUtils.isEmpty(HutzzApplication.getInstance().getToken())) {
            startLogInActivity();
        } else {
            new Handler().postDelayed(this::startMainActivity, 1000);
        }
    }

    private void startLogInActivity() {
        startActivity(LogInActivity.startActivity(SplashScreenActivity.this));
    }

    private void startMainActivity() {
        startActivity(MainActivity.startActivity(SplashScreenActivity.this));
    }
}
