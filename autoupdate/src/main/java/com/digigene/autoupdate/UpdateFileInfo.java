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

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateFileInfo {
    private int downloadProgressInterval = 500;  // in kilobytes
    private int bufferSize = 1024; // in bytes
    private String description;
    private String downloadUrl;
    private String fileName;
    private int versionNumber;
    private boolean isForcedUpdate;

    public UpdateFileInfo(Context context, Response response) {
        JsonKeys jsonKeys = new JsonKeys(context);
        JSONObject dataJSON = null;
        try {
//            dataJSON = new JSONObject(response.getResponseString()).getJSONObject(jsonKeys
//                    .receivedJsonDataKey);
            dataJSON = new JSONObject(response.getResponseString());
            this.downloadUrl = dataJSON.optString(jsonKeys.downloadUrlKey);
            this.fileName = dataJSON.optString(jsonKeys.fileNameKey);
            this.versionNumber = Integer.parseInt(dataJSON.optString(jsonKeys.versionNumberKey));
            this.description = dataJSON.optString(jsonKeys.descriptionKey);
            this.isForcedUpdate = Boolean.parseBoolean(dataJSON.optString(jsonKeys
                    .forcedUpdateKey));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDownloadProgressInterval() {
        return downloadProgressInterval;
    }

    public void setDownloadProgressInterval(int downloadProgressInterval) {
        if (isDownloadProgressIntervalOk(downloadProgressInterval)) {
            this.downloadProgressInterval = downloadProgressInterval;
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        if (isBufferSizeOk(bufferSize)) {
            this.bufferSize = bufferSize;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public boolean isForcedUpdate() {
        return isForcedUpdate;
    }

    private boolean isDownloadProgressIntervalOk(int downloadProgressInterval) {
        return (downloadProgressInterval > 0);
    }

    private boolean isBufferSizeOk(int bufferSize) {
        return (bufferSize > 0);
    }
}
