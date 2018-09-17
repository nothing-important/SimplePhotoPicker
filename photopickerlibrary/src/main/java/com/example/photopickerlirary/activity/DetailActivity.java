package com.example.photopickerlirary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.photopickerlirary.BaseActivity;
import com.example.photopickerlirary.R;
import com.example.photopickerlirary.adapter.DetailAdapter;
import com.example.photopickerlirary.entity.PhotoBean;
import com.example.photopickerlirary.utils.ShareElementUtils;
import com.example.photopickerlirary.utils.StatusBarUtil;
import com.example.photopickerlirary.widget.ViewPagerExtend;

import java.util.List;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private ViewPagerExtend detail_vp;
    private List<PhotoBean> urlExtra;
    public static int currentPosition;

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
        urlExtra = (List<PhotoBean>) intent.getSerializableExtra("urlExtra");
        currentPosition = intent.getIntExtra("currentPosition" , 0);
    }

    private void initView() {
        detail_vp = findViewById(R.id.detail_vp);
        DetailAdapter detailAdapter = new DetailAdapter(urlExtra , this);
        detail_vp.setAdapter(detailAdapter);
        detail_vp.setCurrentItem(currentPosition);
        detail_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position ;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    }

}
