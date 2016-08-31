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

import java.net.HttpURLConnection;
import java.util.Map;

public interface ResponseCallBack {
    interface Successful {
        Response doWhenResponseCodeIs2xx(HttpURLConnection httpURLConnection, int responseCode);
    }

    interface Unsuccessful {
        Response doWhenResponseCodeIs1xx(Context context, Activity activity, Map<String, String>
                headerParams, String serviceURL, int responseCode);

        Response doWhenResponseCodeIs3xx(Context context, Activity activity, Map<String, String>
                headerParams, String serviceURL, int responseCode);

        Response doWhenResponseCodeIs4xx(Context context, Activity activity, Map<String, String>
                headerParams, String serviceURL, int responseCode);

        Response doWhenResponseCodeIs5xx(Context context, Activity activity, Map<String, String>
                headerParams, String serviceURL, int responseCode);
    }
}
