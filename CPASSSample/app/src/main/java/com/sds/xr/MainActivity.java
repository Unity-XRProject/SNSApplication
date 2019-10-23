package com.sds.xr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sds.sdk.Channel;
import com.sds.xr.Communicator.RestfulCommunicator;
import com.sds.xr.Communicator.SDKCommunicationListener;
import com.sds.xr.Communicator.SDKCommunicator;
import com.sds.xr.Communicator.ServiceScheme;
import com.sds.xr.entities.ActorInfo;
import com.sds.xr.entities.ContentsAuthInfo;
import com.sds.xr.entities.ContentsCreatedInfo;
import com.sds.xr.utils.Gatherer;
import com.sds.xr.utils.Logger;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int PERMISSION_GLP = 516;
    View loading;

    private ActorInfo actorInfo;
    private EditText userInput;
    private TextView userOutput;
    private Button upload;
    private Button download;
    private String recvMsgs = "";
    private String currentDownloadableID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> allImages = new ArrayList<String>();
        Gatherer.loadAllImagesPaths(Gatherer.DCIM_ROOT, allImages);
        setContentView(R.layout.activity_main);
        loading = findViewById(R.id.loading_splash);
        userOutput = findViewById(R.id.user_output);
        userInput = findViewById(R.id.user_input);
        upload = findViewById(R.id.upload_btn);
        download = findViewById(R.id.downloads_btn);
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEND ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!SDKCommunicator.getInstance().sendBucket(v.getText().toString())) {
                        Toast.makeText(MainActivity.this, R.string.send_fail, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    v.setText("");
                }
                return true;
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("download clicked");
                if(currentDownloadableID != null) {
                    File downloadFilePath = new File(Gatherer.DOWNLOAD_ROOT, currentDownloadableID + ".png");
                    getContentsAuth(currentDownloadableID,  null, downloadFilePath.toString());
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // contents 등록
                RestfulCommunicator.getInstance().createContents(actorInfo.getServiceId(),new RestfulCommunicator.ResultListener() {

                    @Override
                    public void onSuccess(Object obj) {
                        ContentsCreatedInfo info = (ContentsCreatedInfo)obj;
                        getContentsAuth(info.getContentsId(), allImages.get(0), null);
                    }
                    @Override
                    public void onFail(Throwable t) {

                    }
                });
            }
        });

        if (SDKCommunicator.getInstance().isConnected()) {
            loading.setVisibility(View.GONE);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        actorInfo = (ActorInfo)intent.getSerializableExtra("ACTOR_INFO");
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
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<String> requestPermissionList = new ArrayList();
        for(String permission: ServiceScheme.requiredPermissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }

        if(!requestPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, ServiceScheme.requiredPermissions, PERMISSION_GLP);
        } else {
            if (SDKCommunicator.getInstance().isConnected() == false) {
                if(SDKCommunicator.getInstance().connect(actorInfo, ServiceScheme.FEATURE_CODE, listener) == false) {
                    //connection failure dialog
                    finish();
                }
            } else {


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GLP) {
            for (int result: grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        SDKCommunicator.getInstance().release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (SDKCommunicator.getInstance().isConnected()) {
            showExitPopup();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void showExitPopup() {
        new AlertDialog.Builder(this).setTitle("EXIT").setMessage("Exit App?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (SDKCommunicator.getInstance().isConnected()) {
                    SDKCommunicator.getInstance().disconnect();
                } else {
                    finish();
                }
            }
        }).setNegativeButton("Cancel", null).create().show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    SDKCommunicationListener listener = new SDKCommunicationListener() {
        @Override
        public void onUploadCompleted(boolean result, String s) {
            SDKCommunicator.getInstance().sendBucket(String.format("file uploaded: %s", s));
        }

        @Override
        public void onDownloadCompleted(boolean result, String s, byte[] bytes) {
            recvMsgs += String.format("\n%s is downloaded(%d bytes)", s, bytes.length);
            userOutput.setText(recvMsgs);
        }

        @Override
        public void onConnected() {
            loading.setVisibility(View.GONE);
        }

        @Override
        public void onDisconnected(DisconnectionCause cause, int errorCode) {
            loading.setVisibility(View.GONE);
            if(cause == DisconnectionCause.EXPLICITLY_DISCONNECTED) {
                finish();
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
                new AlertDialog.Builder(MainActivity.this).setTitle(message).setMessage(message).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();
            }
        }

        @Override
        public void onBucketMessage(String senderId, String bucketMsg) {
            if(bucketMsg.contains("file uploaded: ")) {
                String[] segs = bucketMsg.split(" ");
                currentDownloadableID = segs[segs.length - 1];
            }
            recvMsgs += "\n" + bucketMsg;
            userOutput.setText(recvMsgs);
        }
    };
}
