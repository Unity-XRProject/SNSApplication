package com.sds.xr.sns.lib;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.sds.xr.Communicator.RestfulCommunicator;
import com.sds.xr.Communicator.SDKCommunicationListener;
import com.sds.xr.Communicator.SDKCommunicator;
import com.sds.xr.Communicator.ServiceScheme;
import com.sds.xr.MainActivity;
import com.sds.xr.R;
import com.sds.xr.TestActivity;
import com.sds.xr.entities.ActorInfo;
import com.sds.xr.entities.ContentsAuthInfo;
import com.sds.xr.entities.ContentsCreatedInfo;
import com.sds.xr.utils.Gatherer;
import com.sds.xr.utils.Logger;

import java.io.File;

public class AndroidGallary {

    protected static AndroidGallary instance = null;
    private AndroidGalleryCallback callback = null;
    private static final String TAG = "VRSNS";
    private Context ctx;
    private String[] imgs;
    private ActorInfo actorInfo;
    private boolean isLoggedIn = false;
    private String currentDownloadableID = null;

    /**
     * 
     * @param ctx
     * @param actorId
     * @param capacity The initial capacity. Meaningless when already initialized.
     * @return
     */
    public static AndroidGallary getInstance(Context ctx, String actorId, int capacity) {
        synchronized (AndroidGallary.class) {
            if (instance == null) {
                instance = new AndroidGallary(ctx, actorId, capacity);
            }
        }
        return instance;
    }

    protected AndroidGallary(Context ctx, String actorId, int capacity) {
        this.ctx = ctx;
        StringBuilder sb = new StringBuilder();
        ContentResolver cr = ctx.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA }, null, null, "date_modified desc");
        imgs = new String[capacity];
        for (int i = 0; i < capacity; i++) if (cursor.moveToNext()) {
            String img = cursor.getString(0);
            imgs[i] = img;
            Log.i(TAG, img);
        }

        RestfulCommunicator.getInstance().createActor(ServiceScheme.SERVICE_ID,
                ServiceScheme.CHANNEL_ID, /*"VRActor1"*/ actorId,
                ServiceScheme.FEATURE_CODE, new RestfulCommunicator.ResultListener() {
                    @Override
                    public void onSuccess(Object obj) {
                        Log.i(TAG, actorId + ": Actor login success!");
                        actorInfo = (ActorInfo) obj;
                        isLoggedIn = true;
                    }

                    @Override
                    public void onFail(Throwable t) {
                        final String msg = actorId + ": Actor login failed!";
                        Log.i(TAG, msg);
                        new AlertDialog.Builder(ctx)
                                .setMessage(msg)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                });

        if (SDKCommunicator.getInstance().isConnected() == false) {
            if(SDKCommunicator.getInstance().connect(actorInfo, ServiceScheme.FEATURE_CODE, listener) == false) {
                new AlertDialog.Builder(ctx)
                        .setMessage("Server connection failed!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        } else {

        }

    }

    public String[] getImageList() {
        return imgs;
    }

    public void registerCallback(AndroidGalleryCallback callback) {
        this.callback = callback;
    }

    public void sendImage(int index) {
        sendImage(imgs[index]);
    }
    public void sendImage(String path) {
        if (isLoggedIn) {
            RestfulCommunicator.getInstance().createContents(actorInfo.getServiceId(),new RestfulCommunicator.ResultListener() {
                @Override
                public void onSuccess(Object obj) {
                    ContentsCreatedInfo info = (ContentsCreatedInfo)obj;
                    getContentsAuth(info.getContentsId(), path, null);
                }
                @Override
                public void onFail(Throwable t) {
                    Log.e(TAG, "Content creation failed!");
                }
            });
        } else {
            Log.e(TAG, "sendImage: Not logged in!");
        }
    }

    public void receiveImage() {
        if (isLoggedIn) {
            if (currentDownloadableID != null) {
                File downloadFilePath = new File(Gatherer.DOWNLOAD_ROOT, currentDownloadableID + ".png");
                getContentsAuth(currentDownloadableID,  null, downloadFilePath.toString());
            }
        } else {
            Log.e(TAG, "receiveImage: Not logged in!");
        }
    }

    public void release() {
        SDKCommunicator.getInstance().release();
    }

    private void getContentsAuth(String contentsId, final String upfilePath, final String downFilePath) {
        RestfulCommunicator.getInstance().getContentsAuth(actorInfo.getServiceId(), actorInfo.getActorId(), contentsId,
                new RestfulCommunicator.ResultListener() {
                    @Override
                    public void onSuccess(Object obj) {
                        SDKCommunicator.getInstance().TransferImage((ContentsAuthInfo) obj, upfilePath, downFilePath);
                    }
                    @Override
                    public void onFail(Throwable t) {
                        Log.e(TAG, "Upload failed!");
                    }
                });
    }

    SDKCommunicationListener listener = new SDKCommunicationListener() {
        @Override
        public void onUploadCompleted(boolean result, String s) {
            SDKCommunicator.getInstance().sendBucket(String.format("file uploaded: %s", s));
            Log.i(TAG, String.format("%s is uploaded", s));
            if (callback != null) {
                callback.onUploadCompleted();
            }
        }

        @Override
        public void onDownloadCompleted(boolean result, String s, byte[] bytes) {
            Log.i(TAG, String.format("%s is downloaded(%d bytes)", s, bytes.length));
            if (callback != null) {
                callback.onDownloadCompleted();
            }
        }

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(DisconnectionCause cause, int errorCode) {
            if(cause == DisconnectionCause.EXPLICITLY_DISCONNECTED) {
            } else {
                String message = "";
                switch(cause) {
                    case NETWORK_ERROR: message =  "Network Error";
                        break;
                    case FORCE_DISCONNECTED: message =  "DisConnected by force";
                        break;
                    case LOCAL_ERROR: message =  "Local Error";
                        break;
                    case CONNECTION_FAILED: message =  "Failed to connection";
                        break;
                    case DISCONNECTION_FAILED: message =  "Incomplete disconnection";
                        break;
                }
                message = String.format(message + "(%d)", errorCode);
                Log.e(TAG, message);
                if (callback != null) {
                    callback.onDisconnected();
                }
            }
        }

        @Override
        public void onBucketMessage(String senderId, String bucketMsg) {
            if(bucketMsg.contains("file uploaded: ")) {
                String[] segs = bucketMsg.split(" ");
                currentDownloadableID = segs[segs.length - 1];
            }
            if (callback != null) {
                callback.onImageArrived(senderId, bucketMsg);
            }
        }
    };
}
