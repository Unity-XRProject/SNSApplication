package com.sds.xr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sds.xr.Communicator.RestfulCommunicator;
import com.sds.xr.Communicator.ServiceScheme;
import com.sds.xr.entities.ActorInfo;
import com.sds.xr.widget.CustomClearableEditText;

public class LoginActivity extends AppCompatActivity {

    private static String PREFERENCE_USER_ID = "PREFERENCE_USER_ID";

    Button btnSignIn;
    protected CustomClearableEditText textID;
    private View IDUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textID = (CustomClearableEditText) findViewById(R.id.id_field).findViewById(R.id.edit);
        textID.setMaxLength(70);
        textID.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);

        IDUnderline = findViewById(R.id.id_field).findViewById(R.id.edit_underline);
        IDUnderline.setBackgroundColor(getColor(R.color.skin_lockscreen_edit_underline_focused_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try{
                WindowManager.LayoutParams lpWindow =  getWindow().getAttributes();
                lpWindow.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
                getWindow().setAttributes(lpWindow);
            }catch(Exception e){
                //if(AppLog.E()){
                //	AppLog.e("set flags error", "SSE");
                //}
            }
            getWindow().setStatusBarColor(getColor(R.color.skin_list_item_title_text_color));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("ActorInfo", MODE_PRIVATE);
        textID.setText(sharedPreferences.getString(PREFERENCE_USER_ID, ""));
        textID.setTextColor(getColor(R.color.skin_list_item_title_text_color));
        textID.setHintTextColor(getColor(R.color.skin_search_hint_text_color));
        textID.setHintWithoutIamge(getResources().getString(R.string.id_hint));
        textID.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    textID.setTextColor(getColor(R.color.skin_list_item_title_text_color));
                    if(textID.getText() != null && textID.getText().length() > 0){
                        textID.setImageGone(false);
                    }
                    IDUnderline.setBackgroundColor(getColor(R.color.skin_lockscreen_edit_underline_focused_color));
                } else {
                    textID.setTextColor(getColor(R.color.skin_list_item_title_text_color));
                    textID.setHintWithoutIamge(getResources().getString(R.string.id_hint));
                    textID.setImageGone(true);
                    IDUnderline.setBackgroundColor(getColor(R.color.skin_lockscreen_edit_underline_unfocused_color));
                }
            }
        });

        btnSignIn = findViewById(R.id.sign_in);
        btnSignIn.setOnClickListener((view) -> {
            RestfulCommunicator.getInstance().createActor(ServiceScheme.SERVICE_ID,
                    ServiceScheme.CHANNEL_ID, textID.getText().toString(),
                    ServiceScheme.FEATURE_CODE, new RestfulCommunicator.ResultListener() {
                        @Override
                        public void onSuccess(Object obj) {
                            ActorInfo actorInfo = (ActorInfo) obj;
                            SharedPreferences sharedPreferences = getSharedPreferences("ActorInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(PREFERENCE_USER_ID, textID.getText().toString());
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("ACTOR_INFO", actorInfo);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(Throwable t) {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage(getString(R.string.invalid_id))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    }).create().show();
                        }
                    });
        });
    }

    @Override
    protected void onDestroy() {
        textID.setText("");
        textID.destroyDrawingCache();
        super.onDestroy();
    }
}
