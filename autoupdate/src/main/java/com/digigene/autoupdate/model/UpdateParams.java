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

import android.support.annotation.NonNull;

import java.util.Map;

public class UpdateParams {
    private String url;
    private JsonKeys jsonKeys;
    private int bufferSize;
    private int downloadProgressInterval;
    private ResponseCallBack.Unsuccessful resUnsuccessful;
    private UserDialogTextAttrs userDialogTextAttrs;
    private Map<String, String> headerParams;

    private UpdateParams(@NonNull String url, @NonNull JsonKeys jsonKeys,
                         Map<String, String> headerParams, int bufferSize, int
                                 downloadProgressInterval, ResponseCallBack
                                 .Unsuccessful resUnsuccessful, UserDialogTextAttrs
                                 userDialogTextAttrs) {
        this.url = url;
        this.jsonKeys = jsonKeys;
        this.headerParams = headerParams;
        this.bufferSize = bufferSize;
        this.downloadProgressInterval = downloadProgressInterval;
        this.resUnsuccessful = resUnsuccessful;
        this.userDialogTextAttrs = userDialogTextAttrs;
    }

    public UserDialogTextAttrs getUserDialogTextAttrs() {
        return userDialogTextAttrs;
    }

    public int getDownloadProgressInterval() {
        return downloadProgressInterval;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public JsonKeys getJsonKeys() {
        return jsonKeys;
    }

    public ResponseCallBack.Unsuccessful getResUnsuccessful() {
        return resUnsuccessful;
    }

    public String getUrl() {
        return url;
    }

    public static class Builder {
        private String url;
        private JsonKeys jsonKeys;
        private Map<String, String> headerParams;
        private int bufferSize = UpdateModel.BUFFER_SIZE;
        private int downloadProgressInterval = UpdateModel.DEFAULT_DOWNLOAD_PROGRESS_INTERVAL;
        private UserDialogTextAttrs userDialogTextAttrs;
        private ResponseCallBack.Unsuccessful resCallbackWhenUnsuccessful;

        public Builder(@NonNull String url, @NonNull JsonKeys jsonKeys) {
            Utils.throwExceptionIfEmpty(url);
            this.url = url;
            this.jsonKeys = jsonKeys;
        }

        public UpdateParams build() {
            return new UpdateParams(url, jsonKeys, headerParams, bufferSize,
                    downloadProgressInterval, resCallbackWhenUnsuccessful, userDialogTextAttrs);
        }

        public Builder setCustomDialogTextAttrs(@NonNull UserDialogTextAttrs userDialogTextAttrs) {
            this.userDialogTextAttrs = userDialogTextAttrs;
            return this;
        }

        public Builder setHeaderParams(@NonNull Map<String, String> headerParams) {
            this.headerParams = headerParams;
            return this;
        }

        public Builder setDownloadProgressInterval(@NonNull int downloadProgressInterval) {
            if (downloadProgressInterval > 0) {
                this.downloadProgressInterval = downloadProgressInterval;
                return this;
            } else {
                throw new IllegalArgumentException("The download progress interval must be an " +
                        "integer greater " +
                        "than zero");
            }
        }

        public Builder setBufferSize(@NonNull int bufferSize) {
            if (bufferSize > 0) {
                this.bufferSize = bufferSize;
                return this;
            } else
                throw new IllegalArgumentException("The bufferSize must be an integer greater " +
                        "than zero");
        }

        public Builder setResponseCallbackWhenUnsuccessful(@NonNull ResponseCallBack.Unsuccessful
                                                                   resCallbackWhenUnsuccessful) {
            this.resCallbackWhenUnsuccessful = resCallbackWhenUnsuccessful;
            return this;
        }
    }
}
