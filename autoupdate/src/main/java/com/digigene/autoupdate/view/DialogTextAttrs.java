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

package com.digigene.autoupdate.view;

public class DialogTextAttrs {
    private String updateMessage = "To continue using this product, you must update it. Would you" +
            " " + "like to do so?";
    private String positiveText = "Yes";
    private String negativeText = "No";
    private String downloadingText = "Downloading ...";
    private String statusText = "Downloading the new version of";

    public DialogTextAttrs(String downloadingText, String negativeText, String positiveText, String
            statusText, String updateMessage) {
        this.downloadingText = downloadingText;
        this.negativeText = negativeText;
        this.positiveText = positiveText;
        this.statusText = statusText;
        this.updateMessage = updateMessage;
    }

    public DialogTextAttrs() {
    }

    public String getDownloadingText() {
        return downloadingText;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }
}
