package io.ideaction.hutzz.network;

import io.ideaction.hutzz.models.UserCallModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HutzzAPI {

    //-------------------------------Authorization--------------------------------------------------

    /**
     * Register an account
     *
     * @param body - email, password, username
     * @return "email": "test@gmail.com"
     */
    @POST("auth/register/")
    @Headers("Content-Type: application/json")
    Call<Void> register(@Body UserCallModel body);

    /**
     * Sign In with a existing account
     *
     * @param body - email, password
     * @return "email": "test@gmail.com"
     */
    @POST("auth/login/")
    @Headers("Content-Type: application/json")
    Call<UserCallModel> signIn(@Body UserCallModel body);

    /**
     * Forgot Password Call
     *
     * @param body - email
     * @return "message": "Check your email"
     */
    @POST("auth/request-password/")
    @Headers("Content-Type: application/json")
    Call<Void> forgotPassword(@Body UserCallModel body);

    /**
     * Reset password
     *
     * @param body - {
     * 	"old_password": "Aa1234",
     * 	"password": "Aa12345",
     * 	"password_c": "Aa12345"
     * }
     *
     * @return {
     *     "errors": {
     *         "old_password": [
     *             "old_password is incorrect"
     *         ]
     *     }
     * }
     */
    @POST("auth/reset-password/")
    @Headers("Content-Type: application/json")
    Call<Void> resetPassword(@Body UserCallModel body);
}
