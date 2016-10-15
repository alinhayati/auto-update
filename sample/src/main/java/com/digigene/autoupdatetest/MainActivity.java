package com.digigene.autoupdatetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.digigene.autoupdate.model.JsonKeys;
import com.digigene.autoupdate.presenter.UpdateParams;
import com.digigene.autoupdate.presenter.UpdateRequest;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    public void update(View view) {
        String url = "http://www.yourWhateverDomain.com/updateApp?currentVersion=" + BuildConfig
                .VERSION_CODE;
        JsonKeys jsonKeys = new JsonKeys("downloadJsonKey", "fileNameJsonKey",
                "versionCodeJsonKey", "isForcedJsonKey");
        UpdateParams updateParams = new UpdateParams.Builder(url, jsonKeys).build();
        new UpdateRequest(MainActivity.this, this, updateParams).update();
    }
}
