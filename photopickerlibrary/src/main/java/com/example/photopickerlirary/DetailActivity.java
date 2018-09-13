package com.example.photopickerlirary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.photopickerlirary.utils.ShareElementUtils;
import com.example.photopickerlirary.utils.StatusBarUtil;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView detail_img;
    private LinearLayout detail_container;
    private String urlExtra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.transprent));
        ShareElementUtils.applyShareElement(this);
        initIntent();
        initView();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null)return;
        urlExtra = intent.getStringExtra("urlExtra");
    }

    private void initView() {
        detail_container = findViewById(R.id.detail_container);
        detail_container.setOnClickListener(this);
        detail_img = findViewById(R.id.detail_img);
        if (!TextUtils.isEmpty(urlExtra)){
            Glide.with(this).load(urlExtra).into(detail_img);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            ShareElementUtils.closeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.detail_container) {
            ShareElementUtils.closeActivity(this);
        }
    }
}
