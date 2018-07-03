package com.example.mobileapp.directionapipb5.directions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DirectionService {
    @GET
    Call<DirectionResponse>getDirectionResponse(@Url String urlString);
}
