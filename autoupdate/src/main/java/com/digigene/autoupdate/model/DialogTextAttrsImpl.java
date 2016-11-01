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

public class DialogTextAttrsImpl implements UpdateModel.DialogTextAttrs {
    private String downloadingText;
    private String negativeText;
    private String positiveText;
    private String statusText;
    private String updateMessage;

    public DialogTextAttrsImpl() {
    }

    @Override
    public String getDownloadingText() {
        return (downloadingText != null) ? downloadingText : DEFAULT_DOWNLOADING_TEXT;
    }

    @Override
    public String getNegativeText() {
        return (negativeText != null) ? negativeText : DEFAULT_NEGATIVE_TEXT;
    }

    @Override
    public String getPositiveText() {
        return (positiveText != null) ? positiveText : DEFAULT_POSITIVE_TEXT;
    }

    @Override
    public String getStatusText() {
        return (statusText != null) ? statusText : DEFAULT_STATUS_TEXT;
    }

    @Override
    public String getForcedUpdateMessage() {
        return (updateMessage != null) ? updateMessage : DEFAULT_FORCED_UPDATE_MESSAGE;
    }

    @Override
    public void setUserDialogTextAttrs(UserDialogTextAttrs userDialogTextAttrs) {
        this.downloadingText = userDialogTextAttrs.provideDownloadingText();
        this.negativeText = userDialogTextAttrs.provideNegativeText();
        this.positiveText = userDialogTextAttrs.providePositiveText();
        this.statusText = userDialogTextAttrs.provideStatusText();
        this.updateMessage = userDialogTextAttrs.provideForcedUpdateMessageByClient();
    }

    @Override
    public void setForcedUpdateMessageFromServer(String updateMessage) {
        this.updateMessage = updateMessage;
    }

}
