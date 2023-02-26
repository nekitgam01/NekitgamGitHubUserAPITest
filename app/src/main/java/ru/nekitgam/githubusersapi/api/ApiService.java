package ru.nekitgam.githubusersapi.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search/users")
    Call<String> getSearchUsers(@Query("q") String q);

    @GET("users/{user}/followers")
    Call<String> getFollowersForUser(@Query("user") String user);

    @GET("users/{user}/repos")
    Call<String> getReposForUser(@Query("user") String user);
}
