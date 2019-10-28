package com.sds.xr.Communicator;

import android.app.Application;

import com.sds.sdk.Channel;
import com.sds.sdk.ChannelInterface;
import com.sds.sdk.FileTransfer;
import com.sds.sdk.IcActor;
import com.sds.sdk.PaasManager;
import com.sds.sdk.Participant;
import com.sds.sdk.data.BaseParams;
import com.sds.sdk.data.ConnectParams;
import com.sds.sdk.data.FileParams;
import com.sds.sdk.data.PaaSResultCode;
import com.sds.sdk.listener.ChannelListener;
import com.sds.sdk.listener.ConnectListener;
import com.sds.sdk.listener.FileTransferListener;
import com.sds.xr.entities.ActorInfo;
import com.sds.xr.entities.ContentsAuthInfo;
import com.sds.xr.utils.Logger;

import java.util.ArrayList;

import static com.sds.cpaas.common.util.Log.LOG_LEVEL_DEBUG;

public class SDKCommunicator {
    private SDKCommunicator() {

    }

    private static SDKCommunicator instance;
    private SDKCommunicationListener listener;

    public static void init(Application application) {
        // SDK초기화(Application 객체 등록, 로그 출력 설정)
        PaasManager.initApplication(application, true, true);

        com.sds.cpaas.common.util.Log.IS_WRITE_LOG_FILE_EXTERNAL = true; // 파일 쓰기({sdcard}/PaasLogs)
        com.sds.cpaas.common.util.Log.mLogLevel = LOG_LEVEL_DEBUG; // SDK 로그 레벨
        com.sds.cpaas.common.util.Log.LOG_LEVEL_BIZLOGIC = LOG_LEVEL_DEBUG; // BizLogic 로그 레벨
        com.sds.cpaas.common.util.Log.LOG_LEVEL_MEDIAENGINE = LOG_LEVEL_DEBUG; // MediaEngine 로그 레벨
    }

    public static void deinit() {
        PaasManager.destroyApplication();
    }

    public synchronized static SDKCommunicator getInstance() {
        if(instance == null) {
            instance = new SDKCommunicator();
        }
        return instance;
    }

    public boolean connect(ActorInfo actorInfo, int featureCode, SDKCommunicationListener l) {
        ConnectParams params = SDKCommunicator.getInstance().makeConnectionParam(actorInfo, featureCode);
        if(params == null) {
            return false;
        }
        listener = l;

        SDKCommunicator.getInstance().setListeners();
        // PaaSLog.setListener(LogCatcher.mLogListener);
        if (Channel.getInstance().connect(params) != PaaSResultCode.CL_SUCCESS) {
            return false;
        }
        return true;
    }

    public void disconnect() {
        Channel.getInstance().unregisterPhoneState();
        Channel.getInstance().disconnect();
    }

    public boolean sendBucket(String msg) {
        return ( Channel.getInstance().sendBucketMessage(msg, null, null) == 0 ) ? true : false;
    }

    public void TransferImage(ContentsAuthInfo info, String upfilePath, String downFilePath) {
        FileParams fp = new FileParams();
        fp.fileFormat = FileParams.FILETRANSFER_FILE_FORMAT.FORMAT_DEFAULT;
        fp.contentsId = info.getContentsInfo().getContentsId();
        fp.serviceId= info.getServiceId();
        fp.actorId= info.getActorId();
        fp.joinAuthOtp=info.getOtp();
        fp.frontManagerAuthKey=info.getFrontManagerAuthKey();
        fp.privateFrontManagerList = info.getPrivateFrontManagerList();
        fp.publicFrontManagerList = info.getPublicFrontManagerList();
        if(downFilePath == null) {
            FileTransfer.getInstance().startUpload(fp, upfilePath);
        } else if(upfilePath == null) {
            FileTransfer.getInstance().startDownload(fp, downFilePath);
        } else {

        }
    }

    public boolean isConnected() {
        return Channel.getInstance().getConnectedState() == ChannelInterface.ConnectState.CONNECTED;
    }

    public void release() {
        clearListeners();
        Channel.getInstance().release();
    }

    private void setListeners() {
        clearListeners();
        Channel.getInstance().addChannelListener(chListener);
        Channel.getInstance().addConnectListener(connListener);
        FileTransfer.getInstance().addListener(transferListener);
    }

    private void clearListeners() {
        Channel.getInstance().clearConnectListener();
        Channel.getInstance().clearChannelListener();
        FileTransfer.getInstance().clearListner();
    }

    private ConnectParams makeConnectionParam(ActorInfo actorInfo, int featureCode) {
        ConnectParams params = new ConnectParams();

        if(actorInfo == null) {
            return null;
        } else {
            // Service ID
            params.serviceId = actorInfo.getServiceId();
            // Channel ID(Room No.)
            params.channelId = actorInfo.getChannelId();
            // Actor  ID(사용자 ID)
            params.actorId = actorInfo.getActorId();

            // 입장 P/W
            params.joinAuthOtp = actorInfo.getJoinAuthOtp();
            params.frontManagerAuthKey = actorInfo.getFrontManagerAuthKey();

            // Server 정보 설정
            BaseParams.Server pv1 = new BaseParams.Server();
            pv1.setProtocolType(actorInfo.getPrivateFrontManagerList().get(0).getConnectionType());
            pv1.setIp(actorInfo.getPrivateFrontManagerList().get(0).getIp());
            pv1.setPort(actorInfo.getPrivateFrontManagerList().get(0).getPort());
            BaseParams.Server pb1 = new BaseParams.Server();
            pb1.setProtocolType(actorInfo.getPublicFrontManagerList().get(0).getConnectionType());
            pb1.setIp(actorInfo.getPublicFrontManagerList().get(0).getIp());
            pb1.setPort(actorInfo.getPublicFrontManagerList().get(0).getPort());
            BaseParams.Server pb2 = new BaseParams.Server();
            pb2.setProtocolType(actorInfo.getPublicFrontManagerList().get(1).getConnectionType());
            pb2.setIp(actorInfo.getPublicFrontManagerList().get(1).getIp());
            pb2.setPort(actorInfo.getPublicFrontManagerList().get(1).getPort());

            params.privateFrontManagerList.add(pv1);
            params.publicFrontManagerList.add(pb1);
            params.publicFrontManagerList.add(pb2);
            params.featureCode = featureCode;

            return params;
        }
    }

    ChannelListener chListener = new ChannelListener() {

        @Override
        public void onAudioPathChanged(int i) {

        }

        @Override
        public void onParticipantConnected(Participant participant) {

        }

        @Override
        public void onParticipantDisconnected(Participant participant, int i, String s) {

        }

        @Override
        public void onNetworkError(int i) {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.NETWORK_ERROR, i);
            }
        }

        @Override
        public void onCallStateChanged(int i, String s) {

        }

        @Override
        public void onForceDisconnected(int i, String s) {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.FORCE_DISCONNECTED, i);
            }
        }

        @Override
        public void onPushBucketMessage(String s, String s1) {
            if(listener != null) {
                listener.onBucketMessage(s, s1);
            }
        }

        @Override
        public void onScreenOrientationChanged(int i) {

        }

        @Override
        public void onScreenDpiChanged(int i) {

        }

        @Override
        public void onLocalError(int i) {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.LOCAL_ERROR, i);
            }
        }

        @Override
        public void onActorDisconnectAlarm(ArrayList<IcActor> arrayList) {

        }

        @Override
        public void onConferenceDisconnectAlarm() {

        }

        @Override
        public void onParticipantHandOver(String s) {

        }

        @Override
        public void onOverPerformance() {

        }

        @Override
        public void onP2PConnectionEvent(int i) {

        }

        @Override
        public void onPushFeedbackMessage(String s, String s1, String s2, String s3) {

        }
    };

    ConnectListener connListener = new ConnectListener() {

        @Override
        public void onConnected(ChannelInterface channelInterface) {
            if(listener != null) {
                listener.onConnected();
            }
        }

        @Override
        public void onConnectFailure(int i) {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.CONNECTION_FAILED, i);
            }
        }

        @Override
        public void onDisconnected(ChannelInterface channelInterface, int i) {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.EXPLICITLY_DISCONNECTED, i);
            }
        }

        @Override
        public void onDisconnectFailure() {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.DISCONNECTION_FAILED, 0);
            }
        }

        @Override
        public void onRegistered() {

        }

        @Override
        public void onRegisterFailure(int i) {

        }
    };

    FileTransferListener transferListener = new FileTransferListener() {

        @Override
        public void onUploadStart(String s) {

        }

        @Override
        public void onDownloadStart(String s) {
            Logger.i("onDownloadStart");
        }

        @Override
        public void onUploadCompleted(String s) {
            if(listener != null) {
                listener.onUploadCompleted(true, s);
            }
        }

        @Override
        public void onDownloadCompleted(String s, boolean b, byte[] bytes) {
            Logger.i("onDownloadCompleted");
            if(listener != null) {
                listener.onDownloadCompleted(true, s, bytes);
            }
        }

        @Override
        public void onUploadProgress(String s, int i, long l, long l1) {

        }

        @Override
        public void onDownloadProgress(String s, int i, long l, long l1) {
            Logger.i("onDownloadProgress");
        }

        @Override
        public void onDownloadCanceled(String s) {
            if(listener != null) {
                listener.onDownloadCompleted(false, s, null);
            }
        }

        @Override
        public void onUploadCanceled(String s) {
            if(listener != null) {
                listener.onUploadCompleted(false, s);
            }
        }

        @Override
        public void onUploadFailed(String s, int i) {
            if(listener != null) {
                listener.onUploadCompleted(false, s);
            }
        }

        @Override
        public void onDownloadFailed(String s, int i) {
            if(listener != null) {
                listener.onDownloadCompleted(false, s, null);
            }
        }

        @Override
        public void onNetworkError(int i) {
            if(listener != null) {
                listener.onDisconnected(SDKCommunicationListener.DisconnectionCause.NETWORK_ERROR, i);
            }
        }
    };
}
