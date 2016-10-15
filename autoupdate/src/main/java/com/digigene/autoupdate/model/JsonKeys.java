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

import com.digigene.autoupdate.model.Utils;

public class JsonKeys {
    private final String isForcedKey;
    private String downloadUrlKey;
    private String fileNameKey;
    private String versionCodeKey;
    private String updateMessageKey;

    public JsonKeys(@NonNull String downloadUrlKey, @NonNull String fileNameKey, @NonNull String
            versionCodeKey, @NonNull String isForcedKey) {
        String[] arguments = {downloadUrlKey, fileNameKey, versionCodeKey, isForcedKey};
        Utils.checkArguments(arguments);
        this.downloadUrlKey = downloadUrlKey;
        this.fileNameKey = fileNameKey;
        this.versionCodeKey = versionCodeKey;
        this.isForcedKey = isForcedKey;
    }

    public String getDownloadUrlKey() {
        return downloadUrlKey;
    }

    public String getFileNameKey() {
        return fileNameKey;
    }

    public String getIsForcedKey() {
        return isForcedKey;
    }

    public String getUpdateMessageKey() {
        return updateMessageKey;
    }

    public void setUpdateMessageKey(String updateMessageKey) {
        this.updateMessageKey = updateMessageKey;
    }

    public String getVersionCodeKey() {
        return versionCodeKey;
    }
}
