package com.org.coffeeshop.ApiCall;

import android.util.Log;

import com.org.coffeeshop.Utils.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit initilization
 */
public class ZomatoApiClient {

    private static Retrofit mRetrofit = null;

    public static synchronized Retrofit getInstance() {
        if (mRetrofit == null) {
            mRetrofit = buildRetrofit();
        }
        return mRetrofit;
    }

    private static Retrofit buildRetrofit() {
        OkHttpClient.Builder httpClient;
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(provideHeaderInterceptor())
                .addInterceptor(provideHttpLoggingInterceptor());

        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private static Interceptor provideHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader(Constant.USER_KEY, Constant.API_KEY)
                        .method(original.method(), original.body()).build();

                return chain.proceed(request);
            }
        };
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.v("HTTP: ", message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
