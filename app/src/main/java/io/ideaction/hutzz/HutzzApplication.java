package io.ideaction.hutzz;

import android.app.Application;
import android.graphics.Typeface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.grandcentrix.tray.AppPreferences;

import io.ideaction.hutzz.models.UserCallModel;
import io.ideaction.hutzz.network.HutzzAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HutzzApplication extends Application {

    private static final String TAG = "HutzzApplication";
    private static final String USER_EMAIL = "io.ideaction.hutzz.userEmail";
    private static final String USER_PASSWORD = "io.ideaction.hutzz.userPassword";
    private static final String USER_FIRST_NAME = "io.ideaction.hutzz.userFirstName";
    private static final String USER_LAST_NAME = "io.ideaction.hutzz.userLastName";
    private static final String USER_LOCATION = "io.ideaction.hutzz.userLocation";
    private static final String USER_PHONE = "io.ideaction.hutzz.userPhone";
    private static final String TOKEN = "io.ideaction.hutzz.token";
    private static final String AVATAR = "io.ideaction.hutzz.avatar";
    private static final String BASE_URL = "http://04ee96f3.ngrok.io/api/";

    private static HutzzApplication sHutzzApplication;
    private static AppPreferences sAppPreferences;
    private static Retrofit sRetrofit;

    private static Typeface SfProDisplayBold;
    private static Typeface SfProDisplaySemiBold;
    private static Typeface SfProDisplayRegular;

    public static HutzzApplication getInstance() {
        return sHutzzApplication;
    }

    public static HutzzAPI apiInterface() {
        return sRetrofit.create(HutzzAPI.class);
    }

    public static Typeface getSfProDisplayBold() {
        return SfProDisplayBold;
    }

    public static Typeface getSfProDisplaySemiBold() {
        return SfProDisplaySemiBold;
    }

    public static Typeface getSfProDisplayRegular() {
        return SfProDisplayRegular;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sHutzzApplication = this;
        sAppPreferences = new AppPreferences(this);

        Gson gson = new GsonBuilder().create();

        sRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SfProDisplayBold = Typeface.createFromAsset(getAssets(), "fonts/sf_pro_display_bold.ttf");
        SfProDisplaySemiBold = Typeface.createFromAsset(getAssets(), "fonts/sf_pro_display_semi_bold.ttf");
        SfProDisplayRegular = Typeface.createFromAsset(getAssets(), "fonts/sf_pro_display_regular.ttf");
    }

    public String getUserEmail() {
        return sAppPreferences.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        sAppPreferences.put(USER_EMAIL, userEmail);
    }

    public String getUserPassword() {
        return sAppPreferences.getString(USER_PASSWORD, "");
    }

    public void setUserPassword(String userPassword) {
        sAppPreferences.put(USER_PASSWORD, userPassword);
    }

    public String getUserFirstName() {
        return sAppPreferences.getString(USER_FIRST_NAME, "");
    }

    public void setUserFirstName(String userFirstName) {
        sAppPreferences.put(USER_FIRST_NAME, userFirstName);
    }

    public String getUserLastName() {
        return sAppPreferences.getString(USER_LAST_NAME, "");
    }

    public void setUserLastName(String userLastName) {
        sAppPreferences.put(USER_LAST_NAME, userLastName);
    }

    public String getUserLocation() {
        return sAppPreferences.getString(USER_LOCATION, "");
    }

    public void setUserLocation(String userLocation) {
        sAppPreferences.put(USER_LOCATION, userLocation);
    }

    public String getUserPhone() {
        return sAppPreferences.getString(USER_PHONE, "");
    }

    public void setUserPhone(String userPhone) {
        sAppPreferences.put(USER_PHONE, userPhone);
    }

    public String getToken() {
        return sAppPreferences.getString(TOKEN, "");
    }

    public void setToken(String token) {
        sAppPreferences.put(TOKEN, token);
    }

    public String getAvatar() {
        return sAppPreferences.getString(AVATAR, "");
    }

    public void setAvatar(String avatar) {
        sAppPreferences.put(AVATAR, avatar);
    }

    public UserCallModel getUserCredentials() {
        UserCallModel userCallModel = new UserCallModel();
        userCallModel.setEmail(sAppPreferences.getString(USER_EMAIL, ""));
        userCallModel.setPassword(sAppPreferences.getString(USER_PASSWORD, ""));

        return userCallModel;
    }

    public void setUserCredentials(UserCallModel userCallModel) {
        sAppPreferences.put(USER_EMAIL, userCallModel.getEmail());
        sAppPreferences.put(USER_PASSWORD, userCallModel.getPassword());
    }
}
