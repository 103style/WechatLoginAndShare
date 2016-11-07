package com.wtime_developer.wecaht.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.wtime_developer.wecaht.R;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Activity acticity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        /**
         * 1.修改app/MyApplication里的appid 和 appsecret
         * 2.修改umeng module里manifest里面的umengappkey
         * 3.在app.build 弄好相应的签名信息
         * 4.下载微信的签名生成工具生成签名  下载地址：https://res.wx.qq.com/open/zh_CN/htmledition/res/dev/download/sdk/Gen_Signature_Android2.apk
         * 5. umeng的混淆文件已经复制到 proguard-rules.pro
         */
        context = this;
        acticity = this;
    }

    @OnClick({R.id.login, R.id.share_wx, R.id.share_wx_circle})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.share_wx:
                share(SHARE_MEDIA.WEIXIN);//微信
                break;
            case R.id.share_wx_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);//朋友圈
                break;

        }
    }

    private void login() {
        UMShareAPI mShareAPI = UMShareAPI.get(context);
        if (mShareAPI.isInstall(acticity, SHARE_MEDIA.WEIXIN)) {//判断是否安装微信
            mShareAPI.doOauthVerify(acticity, SHARE_MEDIA.WEIXIN, umAuthListener);//授权
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //授权成功
            UMShareAPI mShareAPI = UMShareAPI.get(context);
            mShareAPI.getPlatformInfo(acticity, SHARE_MEDIA.WEIXIN, umAuthListener1);//获取用户信息
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

    private UMAuthListener umAuthListener1 = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            //把友盟返回的信息 转化为微信的实体类
            String json = "{\"openid\":\"" + map.get("openid") + "\","
                    + "\"nickname\":\"" + map.get("screen_name") + "\","
                    + "\"sex\":\"" + map.get("gender") + "\","
                    + "\"province\":\"" + map.get("province") + "\","
                    + "\"city\":\"" + map.get("city") + "\","
                    + "\"country\":\"" + map.get("country") + "\","
                    + "\"headimgur\":\"" + map.get("profile_image_url") + "\","
                    + "\"privilege\":[],"
                    + "\"unionid\":\"" + map.get("unionid") + "\"}";
            //再调用你们后台写的微信登录相关的接口，
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context, "获取用户信息失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context, "取消获取用户信息", Toast.LENGTH_SHORT).show();
        }
    };


    private void share(SHARE_MEDIA media) {
        new ShareAction(acticity).setPlatform(media)
                .withTitle("Title")
                .withText("content")
                .withMedia(new UMImage(context, R.mipmap.ic_launcher))//第二个参数也可以是网络图片地址
                .withTargetUrl("url")//修改为你要分享的链接
                .setCallback(umShareListener)
                .share();
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
            finish();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
            finish();
        }
    };


}
