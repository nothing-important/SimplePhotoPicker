package com.example.administrator.simplephotopicker;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.photopickerlirary.AlbumHelper;
import com.example.photopickerlirary.SelectPhotoResult;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_one , btn_two;
    private RecyclerView recycler_one , recycler_two;
    private DisplayAdapter adapter_one , adapter_two;
    private List<String> list_one = new ArrayList<>();
    private List<String> list_two = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_one = findViewById(R.id.btn_one);
        btn_two = findViewById(R.id.btn_two);
        recycler_one = findViewById(R.id.recyler_one);
        recycler_two = findViewById(R.id.recyler_two);
        LinearLayoutManager manager_one = new LinearLayoutManager(this);
        manager_one.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager_two = new LinearLayoutManager(this);
        manager_two.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_one.setLayoutManager(manager_one);
        recycler_two.setLayoutManager(manager_two);
        adapter_one = new DisplayAdapter(list_one , this);
        adapter_two = new DisplayAdapter(list_two , this);
        recycler_one.setAdapter(adapter_one);
        recycler_two.setAdapter(adapter_two);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_one:
                new AlbumHelper()
                        .with(MainActivity.this)
                        .setPickPhotoNums(5)
                        .setResultPhotoList((ArrayList<String>) list_one)
                        .setSelectImg(R.mipmap.ic_launcher)
                        .setOnReceiveResultListener(new SelectPhotoResult() {
                            @Override
                            public void onReceivePhotoResult(ArrayList<String> resultList) {
                                list_one.clear();
                                ((ArrayList<String>) list_one).addAll(resultList);
                                adapter_one.notifyDataSetChanged();
                            }
                        }).start();
                break;
            case R.id.btn_two:
                new AlbumHelper()
                        .with(MainActivity.this)
                        .setPickPhotoNums(3)
                        .setResultPhotoList((ArrayList<String>) list_two)
                        .setOnReceiveResultListener(new SelectPhotoResult() {
                            @Override
                            public void onReceivePhotoResult(ArrayList<String> resultList) {
                                list_two.clear();
                                ((ArrayList<String>) list_two).addAll(resultList);
                                adapter_two.notifyDataSetChanged();
                            }
                        }).start();
                break;
        }
    }
}
