package ru.nekitgam.githubusersapi.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @GET("search/users")
    Call<String> getSearchUsers(@Query("q") String q, @Query("per_page") Integer per_page);

    @GET("users/{user}/followers?per_page=9999")
    Call<String> getFollowersForUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<String> getReposForUser(@Path("user") String user);
}
