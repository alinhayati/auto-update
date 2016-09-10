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

import android.support.annotation.NonNull;

import java.util.Map;

public class UpdateRequestParams {
    private final String url;
    private final JsonKeys jsonKeys;
    private final ResponseCallBack.Unsuccessful resUnsuccessful;
    private Map<String, String> headerParams;

    public UpdateRequestParams(@NonNull String url, @NonNull JsonKeys jsonKeys,
                               Map<String, String>
                                       headerParams, ResponseCallBack
                                       .Unsuccessful resUnsuccessful) {
        Utils.throwExceptionIfEmpty(url);
        this.url = url;
        this.jsonKeys = jsonKeys;
        this.headerParams = headerParams;
        this.resUnsuccessful = resUnsuccessful;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public JsonKeys getJsonKeys() {
        return jsonKeys;
    }

    public ResponseCallBack.Unsuccessful getResponseCallbackWhenUnsuccessful() {
        return resUnsuccessful;
    }

    public String getUrl() {
        return url;
    }
}
