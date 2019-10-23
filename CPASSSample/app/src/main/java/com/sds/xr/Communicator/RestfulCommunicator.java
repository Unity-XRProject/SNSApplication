package com.sds.xr.Communicator;

import android.util.Log;

import com.sds.xr.Contract.RestfulContract;
import com.sds.xr.entities.ActorInfo;
import com.sds.xr.entities.ContentsAuthInfo;
import com.sds.xr.entities.ContentsCreatedInfo;
import com.sds.xr.entities.ContentsCreationAuthInfo;
import com.sds.xr.entities.ContentsCreationInfo;
import com.sds.xr.entities.CreateActorRequestBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulCommunicator {
    public interface ResultListener {
        void onSuccess(Object obj);
        void onFail(Throwable t);
    }

    private static RestfulCommunicator inst;
    RestfulContract svc;

    private RestfulCommunicator() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer ffe9452d-a335-3f99-93f6-e6aae1404422").build();
                return chain.proceed(request);
            }
        });

        Retrofit fit = new Retrofit.Builder()
                .baseUrl(RestfulContract.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        svc = fit.create(RestfulContract.class);

    }

    public static synchronized RestfulCommunicator getInstance() {
        if(inst == null) {
            inst = new RestfulCommunicator();
        }
        return inst;
    }

    public void createActor(final String serviceID, final String channelID, final String actorID,
                            final int featureCode, final ResultListener rl) {
        CreateActorRequestBody body = new CreateActorRequestBody();
        body.setFeatureCode(featureCode);
        try{
            svc.createActor(serviceID, channelID, actorID, body).enqueue(new Callback<ActorInfo>() {

                @Override
                public void onResponse(Call<ActorInfo> call, Response<ActorInfo> response) {
                    rl.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<ActorInfo> call, Throwable t) {
                    rl.onFail(t);
                }
            });
        } catch(Exception e) {
            Log.e("Strange", e.toString());
        }
    }

    public void createContents(final String serviceID, final ResultListener rl) {
        ContentsCreationInfo contents = new ContentsCreationInfo();
        contents.setTransformYn("N");
        try{
            svc.createContents(serviceID, contents).enqueue(new Callback<ContentsCreatedInfo>() {
                @Override
                public void onResponse(Call<ContentsCreatedInfo> call, Response<ContentsCreatedInfo> response) {
                    rl.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<ContentsCreatedInfo> call, Throwable t) {
                    rl.onFail(t);
                }
            });
        } catch(Exception e) {
            Log.e("Strange", e.toString());
        }
    }

    public void getContentsAuth(final String serviceID, final String actorID,
                                final String contentID,
                                final ResultListener rl) {
        ContentsCreationAuthInfo contentsCreationAuthInfo =
                new ContentsCreationAuthInfo();
        contentsCreationAuthInfo.setFeatureCode(9);
        try{
            svc.getContentsAuth(serviceID, contentID, actorID, contentsCreationAuthInfo).enqueue(
                    new Callback<ContentsAuthInfo>() {
                        @Override
                        public void onResponse(Call<ContentsAuthInfo> call, Response<ContentsAuthInfo> response) {
                            rl.onSuccess(response.body());
                        }

                        @Override
                        public void onFailure(Call<ContentsAuthInfo> call, Throwable t) {
                            rl.onFail(t);
                        }
                    }
            );
        } catch(Exception e) {
            Log.e("Strange", e.toString());
        }
    }
}
