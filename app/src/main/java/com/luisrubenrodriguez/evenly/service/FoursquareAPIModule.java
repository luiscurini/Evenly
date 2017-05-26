package com.luisrubenrodriguez.evenly.service;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GamingMonster on 22.05.2017.
 * Modules for Dagger.
 */

@Module
public class FoursquareAPIModule {

    @Provides
    @Singleton
    Retrofit provideCall() {
        return new Retrofit.Builder()
                .baseUrl(FoursquareAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    FoursquareAPI providesFoursquareAPI(Retrofit retrofit) {
        return retrofit.create(FoursquareAPI.class);
    }

    @Provides
    @Singleton
    FoursquareService providesFoursquareService(FoursquareAPI foursquareAPI) {
        return new FoursquareService(foursquareAPI);
    }

}
