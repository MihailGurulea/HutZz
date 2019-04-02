package io.ideaction.hutzz.utils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.Collection;

import io.ideaction.hutzz.activities.LogInActivity;

public class FacebookSignInHelper {

    private static final String TAG = "FacebookSignInHelper";
    private static final Collection<String> PERMISSION_LOGIN = Arrays.asList("public_profile", "email");

    private static FacebookSignInHelper facebookSignInHelper;
    private static Success sSuccess;
    private static Cancel sCancel;
    private static Error sError;
    private static Listener sListener;

    private CallbackManager callbackManager;
    private LogInActivity mActivity;
    private FacebookCallback<LoginResult> loginCallback;

    private FacebookSignInHelper(LogInActivity mActivity) {
        try {
            this.mActivity = mActivity;
            // Initialize the SDK before executing any other operations,
            // especially, if you're using Facebook UI elements.
            callbackManager = CallbackManager.Factory.create();
            loginCallback = new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    if (sSuccess != null) {
                        sSuccess.onFacebookSuccess(loginResult.getAccessToken().getToken());
                    } else {
                        if (sListener != null) {
                            sListener.onFacebookSuccess(loginResult.getAccessToken().getToken());
                        }
                    }
                }

                @Override
                public void onCancel() {
                    if (sCancel != null) {
                        sCancel.onFacebookCancel();
                    } else {
                        if (sListener != null) {
                            sListener.onFacebookCancel();
                        }
                    }
                }

                @Override
                public void onError(FacebookException error) {
                    if (sError != null) {
                        sError.onFacebookError(error);
                    } else {
                        if (sListener != null) {
                            sListener.onFacebookError(error);
                        }
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FacebookSignInHelper newInstance(LogInActivity context, Success success, Cancel cancel, Error error) {
        sSuccess = success;
        sCancel = cancel;
        sError = error;
        if (facebookSignInHelper == null)
            facebookSignInHelper = new FacebookSignInHelper(context);
        return facebookSignInHelper;
    }

    public static FacebookSignInHelper newInstance(LogInActivity context, Listener listener) {
        sListener = listener;
        if (facebookSignInHelper == null)
            facebookSignInHelper = new FacebookSignInHelper(context);
        return facebookSignInHelper;
    }

    /**
     * To login user on facebook without default Facebook button
     */
    public void loginUser() {
        try {
            LoginManager.getInstance().registerCallback(callbackManager, loginCallback);
            LoginManager.getInstance().logInWithReadPermissions(this.mActivity, PERMISSION_LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To log out user from facebook
     */
    public void signOut() {
        // Facebook sign out
        LoginManager.getInstance().logOut();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public FacebookCallback<LoginResult> getLoginCallback() {
        return loginCallback;
    }

    public interface Success {
        void onFacebookSuccess(String token);
    }

    public interface Cancel {
        void onFacebookCancel();
    }

    public interface Error {
        void onFacebookError(FacebookException error);
    }

    public interface Listener {
        void onFacebookSuccess(String token);

        void onFacebookCancel();

        void onFacebookError(FacebookException error);
    }
}
