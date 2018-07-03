package com.example.mobileapp.directionapipb5;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.mobileapp.directionapipb5.directions.DirectionCallback;
import com.example.mobileapp.directionapipb5.directions.DirectionResponse;
import com.example.mobileapp.directionapipb5.directions.DirectionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {
    private DirectionResponse directionResponse;
    private Retrofit retrofit;
    private DirectionService service;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    public MainViewModel(@NonNull Application application) {
        super(application);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DirectionService.class);
    }
    public DirectionResponse getDirections(String origin, String destination, String key, final DirectionCallback callback){
        String endUrl = String.format("directions/json?origin=%s&destination=%s&alternatives=true&key=%s",origin,destination,key);
        service.getDirectionResponse(endUrl).enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                directionResponse = response.body();
                callback.onResponse(directionResponse);
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {

            }
        });
        return directionResponse;
    }
}
