package com.sds.xr.sns.lib;

public interface AndroidGalleryCallback {
    public void onImageArrived(String senderId, String bucketMsg);
    public void onUploadCompleted();
    public void onDownloadCompleted();
    public void onDisconnected();
}
