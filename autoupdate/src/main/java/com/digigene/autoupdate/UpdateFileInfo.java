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

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateFileInfo {
    private int downloadProgressInterval;
    private int bufferSize;
//    private String updateMessage;
    private String downloadUrl;
    private String fileName;
    private int versionCode;
    private boolean isForcedUpdate;

    public UpdateFileInfo(Response response, JsonKeys jsonKeys, DialogTextAttrs
            dialogTextAttrs, int downloadProgressInterval, int bufferSize) {
        JSONObject dataJSON = null;
        try {
            dataJSON = new JSONObject(response.getResponseString());
            extractDataFromJson(dataJSON, jsonKeys);
            this.downloadProgressInterval = downloadProgressInterval;
            this.bufferSize = bufferSize;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDownloadProgressInterval() {
        return downloadProgressInterval;
    }

    public int getBufferSize() {
        return bufferSize;
    }

//    public String getUpdateMessage() {
//        return updateMessage;
//    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public boolean isForcedUpdate() {
        return isForcedUpdate;
    }

    private void extractDataFromJson(JSONObject dataJSON, JsonKeys jsonKeys) {
        this.downloadUrl = dataJSON.optString(jsonKeys.getDownloadUrlKey());
        this.fileName = dataJSON.optString(jsonKeys.getFileNameKey());
        this.versionCode = Integer.parseInt(dataJSON.optString(jsonKeys.getVersionCodeKey()));
//        if (jsonKeys.getUpdateMessageKey() != null) {
//            this.updateMessage = dataJSON.optString(jsonKeys.getUpdateMessageKey());
//        }
        this.isForcedUpdate = Boolean.parseBoolean(dataJSON.optString(jsonKeys
                .getIsForcedKey()));
    }

    private boolean isDownloadProgressIntervalOk(int downloadProgressInterval) {
        return (downloadProgressInterval > 0);
    }

    private boolean isBufferSizeOk(int bufferSize) {
        return (bufferSize > 0);
    }
}
