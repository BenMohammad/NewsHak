package com.newshak.di.components;

import android.content.SharedPreferences;

import com.newshak.di.modules.AppModule;
import com.newshak.di.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    Retrofit retrofit();

    SharedPreferences sharedPreferences();
}
