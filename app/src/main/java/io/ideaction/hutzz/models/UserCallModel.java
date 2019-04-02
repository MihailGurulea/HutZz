package io.ideaction.hutzz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCallModel {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("user")
    @Expose
    private User mUser;



    public UserCallModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserCallModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCallModel(String email) {
        this.email = email;
    }

    public UserCallModel() {
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
