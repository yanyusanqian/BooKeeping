package com.wyk.bookeeping.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingending.popuplayout.PopupLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.FullyGridLayoutManager;
import com.wyk.bookeeping.adpter.GridImageAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddArticleActivity extends AppCompatActivity {
    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private RecyclerView mRecyclerView;
    private PopupWindow pop;
    private PopupLayout popupLayout;
    private EditText article_edit;
    private FrameLayout article_return;
    private TextView article_done;
    private String userid;
    private String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addarticle);

        mRecyclerView = findViewById(R.id.recycler);
        article_edit = findViewById(R.id.article_edit);
        article_return = findViewById(R.id.article_return);
        article_done = findViewById(R.id.article_done);

        initWidget();

        article_return.setOnClickListener(v -> {
            finish();
        });

        article_done.setOnClickListener(donelistener);
    }

    private void initWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(AddArticleActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            Toast.makeText(AddArticleActivity.this, "暂不支持视频", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            // 预览音频
                            Toast.makeText(AddArticleActivity.this, "暂不支持音频", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }


    @SuppressLint("CheckResult")
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
        //获取写的权限
        RxPermissions rxPermission = new RxPermissions(AddArticleActivity.this);
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {// 用户已经同意该权限
                        //弹出选择和拍照的dialog
                        showPop();
                    } else {
                        Toast.makeText(AddArticleActivity.this, "拒绝后无法使用该功能~", Toast.LENGTH_SHORT).show();
                    }
                });
    };


    private void showPop() {
        View bottomView = View.inflate(AddArticleActivity.this, R.layout.layout_bottom_dialog, null);
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
                    PictureSelector.create(AddArticleActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(maxSelectNum)
                            .minSelectNum(1)
                            .imageSpanCount(4)
                            .compress(true)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .forResult(PictureConfig.CHOOSE_REQUEST);

                    break;
                case R.id.tv_camera:
                    //拍照
                    PictureSelector.create(AddArticleActivity.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);

                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    View.OnClickListener donelistener = v -> {
        String text = article_edit.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(AddArticleActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            MultipartBody.Builder multipartBodyBuilder = null;
            if (selectList.size() == 0) {
                multipartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name", userid)
                        .addFormDataPart("content", text);
            } else {
                multipartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name", userid)
                        .addFormDataPart("content", text);
                for (int i = 0; i < selectList.size(); i++) {
                    File f = null;
                    LocalMedia media = selectList.get(i);
                    if (media.isCompressed()) {
                        f = new File(media.getCompressPath());
                    }
                    if (media.isCut()) {
                        f = new File(media.getCutPath());
                    }
                    if (!media.isCompressed() && !media.isCut()) {
                        f = new File(media.getPath());
                    }
                    if (f != null)
                        multipartBodyBuilder.addFormDataPart("img", f.getName(), RequestBody.create(f, MediaType.parse("image/*")));
                }
            }
            RequestBody requestBody = multipartBodyBuilder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }
            });

        }
    };

}