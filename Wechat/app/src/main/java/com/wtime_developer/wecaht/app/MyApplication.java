package com.wtime_developer.wecaht.app;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: 2016/11/7    修改下面的两个参数    第一个appid  第二个appsecret
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        UMShareAPI.get(this);
    }


}
