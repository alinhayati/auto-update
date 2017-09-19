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

package com.digigene.autoupdate.model;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ResponseActionFactory implements ResponseCallBack.Successful {
    private ResponseCallBack.Unsuccessful responseUnsuccessful;

    public ResponseActionFactory(ResponseCallBack.Unsuccessful responseUnsuccessful) {
        this.responseUnsuccessful = responseUnsuccessful;
    }

    @Override
    public Response responseCodeIs2xx(HttpURLConnection httpURLConnection) {
        BufferedReader br = null;
        try {
            int responseCode = httpURLConnection.getResponseCode();
            br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return new Response(responseCode, null, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response makeResponse(HttpURLConnection httpURLConnection, Context context, Activity
            activity) throws IOException {
        int responseCode = 0;
        try {
            responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException", Toast.LENGTH_SHORT).show();
            return new Response(0, "Error in makeResponse:" + e.getMessage(),
                    null);
        }
        int responseCodeCharacteristicNumber = Math.abs(responseCode / 100);
        return selectResponse(httpURLConnection, context, activity, responseCodeCharacteristicNumber);
    }

    private Response selectResponse(HttpURLConnection httpURLConnection, Context context, Activity
            activity, int responseCodeCharacteristicNumber) {
        if (responseCodeCharacteristicNumber == 2) {
            return responseCodeIs2xx(httpURLConnection);
        } else {
            return responseUnsuccessful.responseCodeIsNot2xx(context, activity,
                    httpURLConnection);
        }
    }
}
