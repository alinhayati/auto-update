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
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.ProgressBar;

import com.digigene.autoupdate.model.DownloadFileCommand;
import com.digigene.autoupdate.R;
import com.digigene.autoupdate.model.UpdateFileInfo;

import java.io.File;

public class DownloadCallBackFactory {
    private final Context context;
    private final Activity activity;
    private final ProgressBar progressBar;
    private final UpdateFileInfo updateFileInfo;
    private Notification notification;
    private DownloadFileCommand.DownloadCallback forcedDownloadCallBack = new DownloadFileCommand.DownloadCallback() {

        @Override
        public void onDownloading(int progressPercent) {
            doWhenDownloadingInForcedMode(progressPercent);
        }

        @Override
        public void onDownloadFinished() {
            doWhenDownloadIsFinishedInForcedMode();
        }
    };
    private DownloadFileCommand.DownloadCallback notForcedDownloadCallBack = new DownloadFileCommand.DownloadCallback() {
        @Override
        public void onDownloading(int progressPercent) {
            doWhenDownloadingInNonForcedMode(progressPercent);
        }

        @Override
        public void onDownloadFinished() {
            doWhenDownloadIsFinishedInNonForcedMode();
        }
    };

    public DownloadCallBackFactory(Context context, Activity activity, ProgressBar progressBar, Notification notification, UpdateFileInfo updateFileInfo) {
        this.context = context;
        this.activity = activity;
        this.progressBar = progressBar;
        this.notification = notification;
        this.updateFileInfo = updateFileInfo;
    }

    public DownloadFileCommand.DownloadCallback getDownloadCallback(CallBackType callBackType) {
        if (callBackType == CallBackType.forced) {
            return forcedDownloadCallBack;
        }
        if (callBackType == CallBackType.notForced) {
            return notForcedDownloadCallBack;
        }
        return null;
    }

    private void doWhenDownloadingInNonForcedMode(int progressPercent) {
        notification.contentView.setProgressBar(R.id.aup_status_progress, 100, progressPercent, false);
    }

    private void doWhenDownloadingInForcedMode(int progressPercent) {
        progressBar.setProgress(progressPercent);
    }

    private void doWhenDownloadIsFinishedInNonForcedMode() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + updateFileInfo.getFileName())), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void doWhenDownloadIsFinishedInForcedMode() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + updateFileInfo.getFileName())), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        activity.finish();
    }

    public enum CallBackType {
        forced, notForced
    }
}
