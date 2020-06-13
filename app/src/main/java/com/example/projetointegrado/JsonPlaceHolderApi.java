package com.example.projetointegrado;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @POST(Constants.CREATE_USER)
    Call<JsonObject> createPostCreateUser(@Body JsonObject body);

    @FormUrlEncoded
    @POST(Constants.LOGIN_USER)
    Call<JsonObject> createPostLogin(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST(Constants.USER_DATA)
    Call<JsonObject> createPostUserData(@Field("id") String id);
}
