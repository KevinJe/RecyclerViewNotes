package com.kevin.library;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Kevin on 2018/3/18.
 * Activity的生命周期回调，由于用不到这么多方法，所以实现系统的Application.ActivityLifecycleCallbacks
 * 然后再直接实现DefaultActivityLifecycleCallbacks就不用重写这么多的方法了
 */

public class DefaultActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
