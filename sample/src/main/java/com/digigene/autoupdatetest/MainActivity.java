package com.digigene.autoupdatetest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.digigene.autoupdate.Response;
import com.digigene.autoupdate.ResponseCallBack;
import com.digigene.autoupdate.ServerConnection;
import com.digigene.autoupdate.UpdateRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class MainActivity extends Activity {
    ServerConnection.UpdateURLRequestParams updateURLRequestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        updateURLRequestParams = new ServerConnection
                .UpdateURLRequestParams() {
            @Override
            public String getURL() {
                return "http://www.yourDomain.com/updateApp?currentVersion=" + BuildConfig
                        .VERSION_CODE;
            }

            @Override
            public Map<String, String> getHeaderParams() {
                return null;
            }

            @Override
            public ResponseCallBack.Unsuccessful getResponseCallBackWhenUnsuccessful() {
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
        };
    }

    public void update(View view) {
        new UpdateRequest.UpdateRequestBuilder(this, this, updateURLRequestParams).build().update();
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
