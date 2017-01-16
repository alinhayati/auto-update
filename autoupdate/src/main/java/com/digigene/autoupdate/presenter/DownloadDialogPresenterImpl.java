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
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import com.digigene.autoupdate.BuildConfig;
import com.digigene.autoupdate.EventBus.DownloadEventMessage;
import com.digigene.autoupdate.R;
import com.digigene.autoupdate.model.DownloadFileCommand;
import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.view.DownloadDialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class DownloadDialogPresenterImpl implements DownloadDialogPresenter {
    private final Context context;
    private final DownloadFileCommand downloadFileCommand;
    private UpdateModel.UpdateFileInfo updateFileInfo;
    private Activity activity;
    private UpdateModel.DialogTextAttrs dialogTextAttrs;
    private DownloadDialogView downloadDialogView;

    public DownloadDialogPresenterImpl(Context context, Activity activity, UpdateModel
            .DialogTextAttrs dialogTextAttrs, UpdateModel.UpdateFileInfo updateFileInfo,
                                       DownloadFileCommand downloadFileCommand) {
        this.context = context;
        this.activity = activity;
        this.dialogTextAttrs = dialogTextAttrs;
        this.updateFileInfo = updateFileInfo;
        this.downloadFileCommand = downloadFileCommand;
        EventBus.getDefault().register(this);
    }

    @Override
    public void startDownloading() {
        downloadFileCommand.execute();
    }

    @Override
    public String getStatusText() {
        return dialogTextAttrs.getStatusText() + " " + context.getString(R.string
                .app_name);
    }

    @Override
    public int getImageResourceId() {
        return R.drawable.ic_file_download_black_24dp;
    }

    @Override
    public void setView(DownloadDialogView downloadDialogView) {
        this.downloadDialogView = downloadDialogView;
    }

    @Override
    @Subscribe
    public void onEvent(DownloadEventMessage downloadEventMessage) {
        if (downloadEventMessage.isDownloadingInForced()) {
            doWhenDownloadingInForcedMode(downloadEventMessage.getProgressBarPercent());
        }
        if (downloadEventMessage.isFinishedInForced()) {
            doWhenDownloadIsFinishedInForcedMode(downloadEventMessage.getFileName());
        }
    }

    private void doWhenDownloadingInForcedMode(int progressPercent) {
        downloadDialogView.setProgressBarValue(progressPercent);
    }

    private void doWhenDownloadIsFinishedInForcedMode(String fileName) {
        File directory = context.getExternalFilesDir(null);
        File file = new File(directory, fileName);
        Uri fileUri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context, context.getPackageName(),
                    file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
        activity.finish();
    }
}