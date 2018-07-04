package com.yahoo.rahul.moviesdoughnut.api;

import android.util.Log;

import com.yahoo.rahul.moviesdoughnut.api.request.Discover;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TmdbService {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Scheduler IO_THREAD = Schedulers.io();
    private static Scheduler MAIN_THREAD = AndroidSchedulers.mainThread();
    public Discover discover;
    private OkHttpClient okHttpClient;
    private Endpoints endpoints;


    public TmdbService(final String api_key) {

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(7, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url();

                        Headers headers = request.headers().newBuilder()

                                .add("Connection", "keep-alive")
                                .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                                .add("Accept-Language", "en-US,en;q=0.9,en-GB;q=0.8")
                                .build();

                        HttpUrl newUrl = url.newBuilder().addQueryParameter("api_key", api_key).build();

                        request = request.newBuilder().build();

                        request = request.newBuilder().url(newUrl).headers(headers).build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        endpoints = retrofit.create(Endpoints.class);

        init();
    }

    private void init() {
        discover = new Discover(endpoints);
    }

    public class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            try {

                Log.d("OkHttp", String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            } catch (Exception e) {
            }
            try {
                Buffer requestBuffer = new Buffer();
                request.body().writeTo(requestBuffer);
                Log.d("OkHttp", requestBuffer.readUtf8());
            } catch (Exception e) {
            }
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            try {
                Log.d("OkHttp", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            } catch (Exception e) {
            }
            MediaType contentType = response.body().contentType();
            BufferedSource buffer = Okio.buffer(new GzipSource(response.body().source()));
            String content = buffer.readUtf8();
            Log.d("OkHttp", content);
            Log.d("OkHttp Response Body", response.body().string());

            ResponseBody wrappedBody = ResponseBody.create(contentType, content);
            return response.newBuilder().removeHeader("Content-Encoding").body(wrappedBody).build();
        }
    }
}
