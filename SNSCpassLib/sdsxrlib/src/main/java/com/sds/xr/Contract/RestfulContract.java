package com.sds.xr.Contract;

import com.sds.xr.entities.ActorInfo;
import com.sds.xr.entities.ContentsAuthInfo;
import com.sds.xr.entities.ContentsCreatedInfo;
import com.sds.xr.entities.ContentsCreationAuthInfo;
import com.sds.xr.entities.ContentsCreationInfo;
import com.sds.xr.entities.CreateActorRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestfulContract {
    String URL = "https://apimdev.meeting.samsungsquare.com";

    @POST("/channels/v1/services/{serviceId}/channels/{channelId}/actors/{actorId}")
    Call<ActorInfo> createActor(@Path("serviceId") String serviceId, @Path("channelId") String channelId, @Path("actorId") String actorId, @Body CreateActorRequestBody updateActorRequest);

    @POST("/contents/v1/services/{serviceId}/contents")
    Call<ContentsCreatedInfo> createContents(@Path("serviceId") String serviceId, @Body ContentsCreationInfo contents);

    @POST("/contents/v1/services/{serviceId}/contents/{contentsId}/actors/{actorId}")
    Call<ContentsAuthInfo> getContentsAuth(@Path("serviceId") String serviceId, @Path("contentsId") String contentsId, @Path("actorId") String actorId, @Body ContentsCreationAuthInfo contents);
}
