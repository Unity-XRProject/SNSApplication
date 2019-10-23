package com.sds.xr.entities;

import com.sds.sdk.data.BaseParams;

import java.util.ArrayList;

public class ContentsAuthInfo {
    private String actorId;
    private ContentsCreatedInfo contentsInfo;
    private int featureCode;
    private String frontManagerAuthKey;
    private String otp;
    private ArrayList<BaseParams.Server> privateFrontManagerList;
    private ArrayList<BaseParams.Server> publicFrontManagerList;
    private String serviceId;

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public ContentsCreatedInfo getContentsInfo() {
        return contentsInfo;
    }

    public void setContentsInfo(ContentsCreatedInfo contentsInfo) {
        this.contentsInfo = contentsInfo;
    }

    public int getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(int featureCode) {
        this.featureCode = featureCode;
    }

    public ArrayList<BaseParams.Server> getPrivateFrontManagerList() {
        return privateFrontManagerList;
    }

    public void setPrivateFrontManagerList(ArrayList<BaseParams.Server> privateFrontManagerList) {
        this.privateFrontManagerList = privateFrontManagerList;
    }

    public ArrayList<BaseParams.Server> getPublicFrontManagerList() {
        return publicFrontManagerList;
    }

    public void setPublicFrontManagerList(ArrayList<BaseParams.Server> publicFrontManagerList) {
        this.publicFrontManagerList = publicFrontManagerList;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public String getFrontManagerAuthKey() {
        return frontManagerAuthKey;
    }

    public void setFrontManagerAuthKey(String frontManagerAuthKey) {
        this.frontManagerAuthKey = frontManagerAuthKey;
    }
}
