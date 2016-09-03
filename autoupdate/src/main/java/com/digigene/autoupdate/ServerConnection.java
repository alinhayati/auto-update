//        Copyright (C) 2016 DigiGene, (www.DigiGene.com)(alinhayati[at]gmail[dot]com)
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

package com.digigene.autoupdate;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ServerConnection {

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    private final Context context;
    private final Activity activity;
    private final UpdateURLRequestParams updateURLRequestParams;
    private String updateRequestURL;
    private Map<String, String> headerParams;
    private ResponseCallBack.Unsuccessful responseUnsuccessful;

    public ServerConnection(Context context, Activity activity, UpdateURLRequestParams
            updateURLRequestParams) {
        this.context = context;
        this.activity = activity;
        this.updateURLRequestParams = updateURLRequestParams;
        headerParams = updateURLRequestParams.getHeaderParams();
        updateRequestURL = updateURLRequestParams.getURL();
        if ((updateRequestURL == null) || (updateRequestURL.trim() == "")) {
            throw new RuntimeException("Update URL address is invalid");
        }
        responseUnsuccessful = updateURLRequestParams.getResponseCallBackWhenUnsuccessful();
    }

    public static HttpURLConnection setURLConnectionGetParams(HttpURLConnection myURLConnection,
                                                              Map<String, String> headerParams)
            throws ProtocolException {
        setHeaderParams(headerParams, myURLConnection);
        myURLConnection.setRequestMethod("GET");
        myURLConnection.setUseCaches(false);
        return myURLConnection;
    }

    public static HttpURLConnection buildURLConnection(URL myURL) throws IOException {
        HttpsURLConnection https;
        HttpURLConnection httpURLConnection;
        if (myURL.getProtocol().toLowerCase().equalsIgnoreCase("https")) {
            trustAllHosts();
            https = (HttpsURLConnection) myURL.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            httpURLConnection = https;
        } else {
            httpURLConnection = (HttpURLConnection) myURL.openConnection();
        }
        return httpURLConnection;
    }

    public static int responseCodeCharacteristicNumber(int responseCode) {
        return Math.abs(responseCode / 100);
    }

    public static boolean isResponseSuccessful(int responseCode) {
        return (responseCodeCharacteristicNumber(responseCode) == 2);
    }

    public HttpURLConnection makeGetRequest() {
        String serviceURL = updateRequestURL;
        URL myURL;
        HttpURLConnection myURLConnection = null;
        try {
            myURL = new URL(serviceURL);
            myURLConnection = buildURLConnection(myURL);
            myURLConnection = setURLConnectionGetParams(myURLConnection, headerParams);
            myURLConnection.connect();
            return myURLConnection;
//            responseCode = myURLConnection.getResponseCode();
//            Response response = new Response(responseCode, );
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (myURLConnection != null) {
                try {
                    myURLConnection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        throw new RuntimeException("Error getting the download URL from the server");
    }

    /**
     * To get around certificate checking, all servers are trusted. This shall be changed for
     * HTTPS request that are intended to be secure
     */
    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setHeaderParams(Map<String, String> headerParams, HttpURLConnection
            myURLConnection) {
        if (headerParams != null) {
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                myURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    public interface UpdateURLRequestParams {
        String getURL();

        Map<String, String> getHeaderParams();

        ResponseCallBack.Unsuccessful getResponseCallBackWhenUnsuccessful();
    }
}
