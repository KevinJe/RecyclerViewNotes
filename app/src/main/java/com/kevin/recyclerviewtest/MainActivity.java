package com.kevin.recyclerviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kevin.recyclerviewtest.basicuse.BasicUseActivity;
import com.kevin.recyclerviewtest.commonadpteruse.CommonAdapterActivity;
import com.kevin.recyclerviewtest.dragitemanimatoruse.DragItemAnimatorActivity;
import com.kevin.recyclerviewtest.multypelayout.MultiTypeActivity;
import com.kevin.recyclerviewtest.refreshload.RefreshLoadActivity;
import com.kevin.recyclerviewtest.wrap.HeaderFooterListActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void dragItemAnimator(View view) {
        startActivity(new Intent(this, DragItemAnimatorActivity.class));
    }

    public void basicUse(View view) {
        startActivity(new Intent(this, BasicUseActivity.class));
    }

    public void list(View view) {
        startActivity(new Intent(this, CommonAdapterActivity.class));
    }

    public void headerFooter(View view) {
        startActivity(new Intent(this, HeaderFooterListActivity.class));
    }

    public void refreshLoad(View view) {
        startActivity(new Intent(this, RefreshLoadActivity.class));
    }

    public void multiType(View view) {
        startActivity(new Intent(this, MultiTypeActivity.class));
    }
}
