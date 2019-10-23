package com.sds.xr.entities;

import java.io.Serializable;
import java.util.List;

public class ActorInfo implements Serializable {
    private String actorId;
    private String joinAuthOtp;
    private String frontManagerAuthKey;
    private List<ServerInfo> privateFrontManagerList;
    private List<ServerInfo> publicFrontManagerList;
    private Integer featureCode;
    private String serviceId;
    private String channelId;

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getJoinAuthOtp() {
        return joinAuthOtp;
    }

    public void setJoinAuthOtp(String joinAuthOtp) {
        this.joinAuthOtp = joinAuthOtp;
    }

    public String getFrontManagerAuthKey() {
        return frontManagerAuthKey;
    }

    public void setFrontManagerAuthKey(String frontManagerAuthKey) {
        this.frontManagerAuthKey = frontManagerAuthKey;
    }

    public List<ServerInfo> getPrivateFrontManagerList() {
        return privateFrontManagerList;
    }

    public void setPrivateFrontManagerList(List<ServerInfo> privateFrontManagerList) {
        this.privateFrontManagerList = privateFrontManagerList;
    }

    public List<ServerInfo> getPublicFrontManagerList() {
        return publicFrontManagerList;
    }

    public void setPublicFrontManagerList(List<ServerInfo> publicFrontManagerList) {
        this.publicFrontManagerList = publicFrontManagerList;
    }

    public Integer getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(Integer featureCode) {
        this.featureCode = featureCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public class ServerInfo implements Serializable{
        private String ip;
        private Integer port;
        private String connectionType;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getConnectionType() {
            return connectionType;
        }

        public void setConnectionType(String connectionType) {
            this.connectionType = connectionType;
        }
    }
}
