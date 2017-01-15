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

package com.digigene.autoupdate.EventBus;

public class DownloadEventMessage {
    private int progressBarPercent;
    private boolean isFinishedInNonForced, isFinishedInForced;
    private boolean isDownloadingInNonForced, isDownloadingInForced;
    private String fileName;

    public boolean isDownloadingInForced() {
        return isDownloadingInForced;
    }

    public void setDownloadingInForced(boolean downloadingInForced) {
        isDownloadingInForced = downloadingInForced;
    }

    public boolean isDownloadingInNonForced() {
        return isDownloadingInNonForced;
    }

    public void setDownloadingInNonForced(boolean downloadingInNonForced) {
        isDownloadingInNonForced = downloadingInNonForced;
    }

    public boolean isFinishedInForced() {
        return isFinishedInForced;
    }

    public void setFinishedInForced(boolean finishedInForced) {
        isFinishedInForced = finishedInForced;
    }

    public boolean isFinishedInNonForced() {
        return isFinishedInNonForced;
    }

    public void setFinishedInNonForced(boolean finishedInNonForced) {
        isFinishedInNonForced = finishedInNonForced;
    }

    public int getProgressBarPercent() {
        return progressBarPercent;
    }

    public void setProgressBarPercent(int progressBarPercent) {
        this.progressBarPercent = progressBarPercent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
