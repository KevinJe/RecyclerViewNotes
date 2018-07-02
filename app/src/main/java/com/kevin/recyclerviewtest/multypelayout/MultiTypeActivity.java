package com.kevin.recyclerviewtest.multypelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kevin.recyclerviewtest.R;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeActivity extends AppCompatActivity {
    private List<ChatBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);
        initData();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        MultiTypeAdapter adapter = new MultiTypeAdapter(this, mDatas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                mDatas.add(new ChatBean(0, "This is me!"));
            } else {
                mDatas.add(new ChatBean(1, "This is my friend!"));
            }
        }
    }
}
