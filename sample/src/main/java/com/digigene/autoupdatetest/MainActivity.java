package com.digigene.autoupdatetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.digigene.autoupdate.model.JsonKeys;
import com.digigene.autoupdate.model.UpdateParams;
import com.digigene.autoupdate.presenter.UpdateRequest;
import com.digigene.autoupdate.model.UserDialogTextAttrs;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    public void update(View view) {
        String url = "http://192.168.13.20:8080/api/" + "app/ravitel/" + "100" +
                "/newer";
        JsonKeys jsonKeys = new JsonKeys("downloadUrl", "fileName",
                "versionNumber", "isForced");
        UserDialogTextAttrs userDialogTextAttrs = new UserDialogTextAttrs() {
            @Override
            public String provideDownloadingText() {
                return "در حال دانلود فایل";
            }

            @Override
            public String provideNegativeText() {
                return "خیر";
            }

            @Override
            public String providePositiveText() {
                return "بله";
            }

            @Override
            public String provideStatusText() {
                return "دانلود فایل جدید";
            }

            @Override
            public String provideForcedUpdateMessageByClient() {
                return "یرای ادامه استفاده باید بروزرسانی کنید. آیا مایل هستید؟";
            }
        };
        UpdateParams updateParams = new UpdateParams.Builder(url, jsonKeys).setBufferSize(1024)
                .setDownloadProgressInterval(500).setCustomDialogTextAttrs(userDialogTextAttrs)
                .build();
//        UpdateParams updateParams = new UpdateParams.Builder(url, jsonKeys).setBufferSize(1024)
//                .setDownloadProgressInterval(500)
//                .build();
        new UpdateRequest(this, updateParams).update();
    }
}
