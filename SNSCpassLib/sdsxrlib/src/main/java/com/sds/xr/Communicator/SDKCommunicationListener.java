package com.sds.xr.Communicator;

public interface SDKCommunicationListener {
    enum DisconnectionCause {
        EXPLICITLY_DISCONNECTED,
        NETWORK_ERROR,
        FORCE_DISCONNECTED,
        LOCAL_ERROR,
        CONNECTION_FAILED,
        DISCONNECTION_FAILED
    }

    void onUploadCompleted(boolean result, String s);

    void onDownloadCompleted(boolean result, String s, byte[] bytes);

    void onConnected();

    void onDisconnected(DisconnectionCause cause, int errorCode);

    void onBucketMessage(String senderId, String bucketMsg);
}
