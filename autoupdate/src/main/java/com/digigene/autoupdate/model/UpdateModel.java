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

public interface UpdateModel {
    int DEFAULT_DOWNLOAD_PROGRESS_INTERVAL = 200;  /* in kilobytes */
    int BUFFER_SIZE = 1024;

    interface DialogTextAttrs {
        String DEFAULT_FORCED_UPDATE_MESSAGE = "To continue using this product, you must update " +
                "it. " +
                "Would you" +
                " " + "like to do so?";
        String DEFAULT_POSITIVE_TEXT = "Yes";
        String DEFAULT_NEGATIVE_TEXT = "No";
        String DEFAULT_DOWNLOADING_TEXT = "Downloading ...";
        String DEFAULT_STATUS_TEXT = "Downloading the new version of";

        String getDownloadingText();

        String getNegativeText();

        String getPositiveText();

        String getStatusText();

        String getForcedUpdateMessage();

        void setUserDialogTextAttrs(UserDialogTextAttrs userDialogTextAttrs);

        void setForcedUpdateMessageFromServer(String updateMessage);
    }

    interface UpdateFileInfo {
        void extractDataFromJson(Response response, JsonKeys jsonKeys);

        String getDownloadUrl();

        String getFileName();

        int getVersionCode();

        boolean isForcedUpdate();
    }
}
