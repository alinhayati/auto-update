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

package com.digigene.autoupdate.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.digigene.autoupdate.model.JsonKeys;
import com.digigene.autoupdate.model.Response;
import com.digigene.autoupdate.model.ResponseCallBack;
import com.digigene.autoupdate.model.Utils;
import com.digigene.autoupdate.view.DialogTextAttrs;
import com.digigene.autoupdate.view.DownloadDialog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class UpdateParams {
    private static final int BUFFER_SIZE = 1024;
    private String url;
    private JsonKeys jsonKeys;
    private int bufferSize;
    private ResponseCallBack.Unsuccessful resUnsuccessful;
    private DownloadDialog downloadDialog;
    private DialogTextAttrs dialogTextAttrs;
    private Map<String, String> headerParams;

    private UpdateParams(@NonNull String url, @NonNull JsonKeys jsonKeys,
                         Map<String, String> headerParams, int bufferSize, ResponseCallBack
                                 .Unsuccessful resUnsuccessful, DownloadDialog downloadDialog,
                         DialogTextAttrs dialogTextAttrs) {
        this.url = url;
        this.jsonKeys = jsonKeys;
        this.headerParams = headerParams;
        this.bufferSize = bufferSize;
        this.resUnsuccessful = resUnsuccessful;
        this.downloadDialog = downloadDialog;
        this.dialogTextAttrs = dialogTextAttrs;
    }

    public DialogTextAttrs getDialogTextAttrs() {
        return dialogTextAttrs;
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

    public DownloadDialog getDownloadDialog() {
        return downloadDialog;
    }

    public String getUrl() {
        return url;
    }

    public static class Builder {
        private String url;
        private JsonKeys jsonKeys;
        private Map<String, String> headerParams;
        private int bufferSize = BUFFER_SIZE;
        private DialogTextAttrs dialogTextAttrs = new DialogTextAttrs();
        private ResponseCallBack.Unsuccessful resCallbackWhenUnsuccessful = new ResponseCallBack
                .Unsuccessful() {
            @Override
            public Response responseCodeIsNot2xx(Context context, Activity activity,
                                                 HttpURLConnection httpURLConnection) {
                return genericDoResponse(context, activity, httpURLConnection);
            }
        };
        private DownloadDialog downloadDialog = new DownloadDialog();

        public Builder(@NonNull String url, @NonNull JsonKeys jsonKeys) {
            Utils.throwExceptionIfEmpty(url);
            this.url = url;
            this.jsonKeys = jsonKeys;
        }

        public UpdateParams build() {
            return new UpdateParams(url, jsonKeys, headerParams, bufferSize,
                    resCallbackWhenUnsuccessful, downloadDialog, dialogTextAttrs);
        }

        public void setDialogTextAttrs(@NonNull DialogTextAttrs dialogTextAttrs) {
            this.dialogTextAttrs = dialogTextAttrs;
            downloadDialog.setDialogTextAttrs(dialogTextAttrs);
        }

        public Builder setHeaderParams(@NonNull Map<String, String> headerParams) {
            this.headerParams = headerParams;
            return this;
        }

        public Builder setDownloadDialog(@NonNull DownloadDialog downloadDialog) {
            this.downloadDialog = downloadDialog;
            return this;
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
}
