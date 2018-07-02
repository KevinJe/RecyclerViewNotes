package com.kevin.recyclerviewtest.commonadpteruse;

import com.kevin.recyclerviewtest.api.NetCallback;
import com.kevin.recyclerviewtest.mvp.view.IListData;

/**
 * Created by Kevin Jern on 2018/7/1 17:32.
 */
public interface HomeContract {
    interface IHomePresenter {
        void getHomeData(NetCallback callback);
    }

    interface IHomeView extends IListData {

    }
}
