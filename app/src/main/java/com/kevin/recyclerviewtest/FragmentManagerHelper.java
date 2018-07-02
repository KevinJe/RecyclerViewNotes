package com.kevin.recyclerviewtest;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Kevin Jern on 2018/7/2 21:28.
 * Fragment管理类
 */
public class FragmentManagerHelper {
    // FragmentManager
    private FragmentManager mFragmentManager;
    // 容器布局id containerViewId
    private int mContainerViewId;

    public FragmentManagerHelper(@Nullable FragmentManager fragmentManager, int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

    /**
     * 添加Fragment
     *
     * @param fragment 要添加的Fragment
     */
    public void add(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(mContainerViewId, fragment);
        transaction.commit();
    }

    /**
     * 切换Fragment，上一个Fragment隐藏，这一个Fragment显示
     *
     * @param fragment 要切换的Fragment
     */
    public void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        // 先隐藏所有的Fragment
        for (Fragment childFragment : fragments) {
            transaction.hide(childFragment);
        }
        // 如果此时不包含当前的Fragment，则先添加当前的Fragment
        if (!fragments.contains(fragment)) {
            transaction.add(mContainerViewId, fragment);
        } else {
            // 否则 就显示当前的Fragment
            transaction.show(fragment);
        }
        // 提交
        transaction.commit();

    }

}
