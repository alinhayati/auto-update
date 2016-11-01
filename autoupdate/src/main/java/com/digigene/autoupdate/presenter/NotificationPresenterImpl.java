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

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.digigene.autoupdate.EventBus.DownloadEventMessage;
import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.view.NotificationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class NotificationPresenterImpl implements NotificationPresenter {
    private Context context;
    private NotificationView notificationView;
    private UpdateModel.UpdateFileInfo updateFileInfo;
    private UpdateModel.DialogTextAttrs dialogTextAttrs;

    public NotificationPresenterImpl(Context context, NotificationView notificationView,
                                     UpdateModel.UpdateFileInfo updateFileInfo, UpdateModel
                                             .DialogTextAttrs dialogTextAttrs) {
        this.context = context;
        this.notificationView = notificationView;
        this.updateFileInfo = updateFileInfo;
        this.dialogTextAttrs = dialogTextAttrs;
        notificationView.setPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public UpdateModel.DialogTextAttrs getDialogTextAttrs() {
        return dialogTextAttrs;
    }


    @Override
    @Subscribe
    public void onEvent(DownloadEventMessage downloadEventMessage) {
        if (downloadEventMessage.isDownloadingInNonForced()) {
            doWhenDownloadingInNonForcedMode(downloadEventMessage.getProgressBarPercent());
        }

        if (downloadEventMessage.isFinishedInNonForced()) {
            doWhenDownloadIsFinishedInNonForcedMode();
        }
    }

    private void doWhenDownloadingInNonForcedMode(int progressPercent) {
        notificationView.setProgressBarValue(progressPercent);
    }

    private void doWhenDownloadIsFinishedInNonForcedMode() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() +
                "/" + updateFileInfo.getFileName())), "application/vnd.android" +
                ".package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
