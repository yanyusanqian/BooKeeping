package com.wyk.bookeeping.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.TipDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.GridImageAdapter;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;
import com.wyk.bookeeping.utils.okhttpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeUserMsgActivity extends AppCompatActivity {
    private FrameLayout personal_return;
    private RelativeLayout personal_image, personal_nikename;
    private ImageView personal_im_image;
    private TextView personal_tv_nikename;
    private Button personal_logout;
    private String userPhone, response, userNikename, response2, imgPath;
    private Dialog globalDialog;
    private PopupWindow pop;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeusermsg);

        personal_return = findViewById(R.id.personal_return);
        personal_image = findViewById(R.id.personal_image);
        personal_nikename = findViewById(R.id.personal_nikename);
        personal_im_image = findViewById(R.id.personal_im_image);
        personal_tv_nikename = findViewById(R.id.personal_tv_nikename);
        personal_logout = findViewById(R.id.personal_logout);

        userPhone = SpUtils.getString(ChangeUserMsgActivity.this, "USERPHONE", "");
        personal_tv_nikename.setText(SpUtils.getString(this, "USERNAME", "未命名"));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.headportrait)
                .centerCrop();
        Glide.with(this)
                .load("http://"+getString(R.string.localhost)+"/pic_file/"+SpUtils.getString(this,"USERIMAGE",""))
                .apply(requestOptions).into(personal_im_image);

        personal_return.setOnClickListener(v -> {
            finish();
        });

        // 更改头像
        personal_image.setOnClickListener(v -> {
            //获取写的权限
            RxPermissions rxPermission = new RxPermissions(ChangeUserMsgActivity.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(permission -> {
                        if (permission.granted) {// 用户已经同意该权限
                            //弹出选择和拍照的dialog
                            showPop();
                        } else {
                            Toast.makeText(ChangeUserMsgActivity.this, "拒绝后无法使用该功能~", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 更改用户名
        personal_nikename.setOnClickListener(v -> {
            InputDialog.show(this, "设置昵称", "设置一个好听的名字吧", new InputDialogOkButtonClickListener() {
                @Override
                public void onClick(Dialog dialog, String inputText) {
                    globalDialog = dialog;
                    userNikename = inputText;
                    new Thread() {
                        @Override
                        public void run() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("user_phone", userPhone);
                            params.put("user_nikename", inputText);
                            try {
                                String url = "http://" + getString(R.string.localhost) + "/Bookeeping/UpdateUserNikename";
                                response = okhttpUtils.getInstance().Connection(url, params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.obj = response;
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                }
            });
        });

        // 退出登陆
        personal_logout.setOnClickListener(v -> {
            SpUtils.remove(ChangeUserMsgActivity.this, "USERPHONE");
            SpUtils.remove(ChangeUserMsgActivity.this, "USERNAME");
            SpUtils.remove(ChangeUserMsgActivity.this, "USERIMAGE");
            SpUtils.remove(ChangeUserMsgActivity.this, "USERSTATUS");
            DBHelper.getInstance(ChangeUserMsgActivity.this).deleteAccount(ChangeUserMsgActivity.this);
            finish();
        });

    }

    /**
     * 更改用户名消息处理
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("ChangeUserNikeName", " Response" + response);
            if ("Failed".equals(response)) {
                Toast.makeText(ChangeUserMsgActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            }
            if ("1".equals(response)) {
                Toast.makeText(ChangeUserMsgActivity.this, "更改成功", Toast.LENGTH_LONG).show();
                SpUtils.putString(ChangeUserMsgActivity.this, "USERNAME", userNikename);
                personal_tv_nikename.setText(userNikename);
                globalDialog.dismiss();
            }
            if ("0".equals(response)) {
                Toast.makeText(ChangeUserMsgActivity.this, "更改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 相册/拍照 选择弹窗
     */
    private void showPop() {
        View bottomView = View.inflate(ChangeUserMsgActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = view -> {
            switch (view.getId()) {
                case R.id.tv_album:
                    //相册
                    PictureSelector.create(ChangeUserMsgActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1)
                            .minSelectNum(1)
                            .imageSpanCount(4)
                            .enableCrop(true)// 是否裁剪
                            .circleDimmedLayer(true)
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                            .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                            .compress(true)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                    break;
                case R.id.tv_camera:
                    //拍照
                    PictureSelector.create(ChangeUserMsgActivity.this)
                            .openCamera(PictureMimeType.ofImage())
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                    break;
                case R.id.tv_cancel:
                    //取消
                    closePopupWindow();
                    break;
            }
            closePopupWindow();
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    /**
     * 头像选择器返回选择结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data);
                LocalMedia media = images.get(0);

                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_phone", userPhone);
                File f = null;
                if (media.isCompressed()) {
                    imgPath = media.getCompressPath();
                    f = new File(imgPath);
                }
                if (media.isCut()) {
                    imgPath = media.getCutPath();
                    f = new File(imgPath);
                }
                if (!media.isCompressed() && !media.isCut()) {
                    imgPath = media.getPath();
                    f = new File(imgPath);
                }
                if (f != null)
                    multipartBodyBuilder.addFormDataPart("img", f.getName(), RequestBody.create(f, MediaType.parse("image/*")));

                String url = "http://" + getString(R.string.localhost) + "/Bookeeping/ChangeUserImage";

                RequestBody requestBody = multipartBodyBuilder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = "Failed";
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response1) throws IOException {
                        Message msg = mHandler2.obtainMessage();
                        msg.obj = response1;
                        response2 = Objects.requireNonNull(response1.body()).string();
                        mHandler2.sendMessage(msg);
                    }
                });
            }
        }
    }

    /**
     * 更改头像消息处理
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if ("0".equals(response2)) {
                Toast.makeText(ChangeUserMsgActivity.this, "更改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if ("Failed".equals(response)) {
                Toast.makeText(ChangeUserMsgActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangeUserMsgActivity.this, "更改成功", Toast.LENGTH_LONG).show();
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.color_f6)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(ChangeUserMsgActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(personal_im_image);

                SpUtils.putString(ChangeUserMsgActivity.this, "USERIMAGE", response2);
            }
        }
    };
}
