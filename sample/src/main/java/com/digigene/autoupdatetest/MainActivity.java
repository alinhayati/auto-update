package com.digigene.autoupdatetest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.digigene.autoupdate.JsonKeys;
import com.digigene.autoupdate.Response;
import com.digigene.autoupdate.ResponseCallBack;
import com.digigene.autoupdate.UpdateRequest;
import com.digigene.autoupdate.UpdateRequestParams;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MainActivity extends Activity {
    UpdateRequestParams updateRequestParams;

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
        jsonKeys.setUpdateMessageKey("updateMessageJsonKey");
        UpdateRequestParams updateRequestParams = new UpdateRequestParams(url, jsonKeys, null,
                getResponseCallbackWhenUnsuccessful());
        new UpdateRequest.Builder(this, this, updateRequestParams).build().update();
    }

    private ResponseCallBack.Unsuccessful getResponseCallbackWhenUnsuccessful() {
        return new ResponseCallBack.Unsuccessful() {
            @Override
            public Response doWhenResponseCodeIs1xx(Context context, Activity activity,
                                                    HttpURLConnection httpURLConnection) {
                return genericDoResponse(context, activity, httpURLConnection);
            }

            @Override
            public Response doWhenResponseCodeIs3xx(Context context, Activity activity,
                                                    HttpURLConnection httpURLConnection) {
                return genericDoResponse(context, activity, httpURLConnection);
            }

            @Override
            public Response doWhenResponseCodeIs4xx(Context context, Activity activity,
                                                    HttpURLConnection httpURLConnection) {
                return genericDoResponse(context, activity, httpURLConnection);
            }

            @Override
            public Response doWhenResponseCodeIs5xx(Context context, Activity activity,
                                                    HttpURLConnection httpURLConnection) {
                return genericDoResponse(context, activity, httpURLConnection);
            }
        };
    }

    @NonNull
    private Response genericDoResponse(Context context, Activity activity, HttpURLConnection
            httpURLConnection) {
        int responseCode = 0;
        try {
            responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "Error connecting to the server: Response code is " +
                responseCode, Toast
                .LENGTH_SHORT).show();
        return new Response(responseCode, "Error:" + responseCode, null);
    }
}
