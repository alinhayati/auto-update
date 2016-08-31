package com.digigene.autoupdatetest;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.digigene.autoupdate.Response;
import com.digigene.autoupdate.ResponseCallBack;
import com.digigene.autoupdate.ServerConnection;
import com.digigene.autoupdate.UpdateRequest;

import java.util.Map;

public class MainActivity extends Activity {
    ServerConnection.UpdateURLRequestParams updateURLRequestParams = new ServerConnection
            .UpdateURLRequestParams() {
        @Override
        public String getURL() {
            return "http://www.yourDomain.com/updateApp?currentVersion=" + BuildConfig.VERSION_CODE;
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
                                                        Map<String, String> headerParams, String
                                                                serviceURL, int responseCode) {
                    return genericDoResponse(responseCode);
                }

                @Override
                public Response doWhenResponseCodeIs3xx(Context context, Activity activity,
                                                        Map<String, String> headerParams, String
                                                                serviceURL, int responseCode) {
                    return genericDoResponse(responseCode);
                }

                @Override
                public Response doWhenResponseCodeIs4xx(Context context, Activity activity,
                                                        Map<String, String> headerParams, String
                                                                serviceURL, int responseCode) {
                    return genericDoResponse(responseCode);
                }

                @Override
                public Response doWhenResponseCodeIs5xx(Context context, Activity activity,
                                                        Map<String, String> headerParams, String
                                                                serviceURL, int responseCode) {
                    return genericDoResponse(responseCode);
                }

                @NonNull
                private Response genericDoResponse(int responseCode) {
                    Toast.makeText(MainActivity.this, "Error connecting to the server: Response " +
                            "code is " + responseCode, Toast
                            .LENGTH_SHORT).show();
                    return new Response(responseCode, "");
                }
            };
        }
    };

    public void update(View view) {
        new UpdateRequest.UpdateRequestBuilder(this, this, updateURLRequestParams).build().update();
    }
}
