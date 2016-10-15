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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import com.digigene.autoupdate.model.DownloadFileCommand;
import com.digigene.autoupdate.model.UpdateFileInfo;
import com.digigene.autoupdate.view.DownloadDialog;
import com.digigene.autoupdate.view.NotificationView;

public class UpdateAction {
    private Context context;
    private Activity activity;
    private UpdateFileInfo updateFileInfo;
    private UpdateParams updateParams;

    public UpdateAction(Context context, Activity activity, UpdateFileInfo updateFileInfo,
                        UpdateParams updateParams) {
        this.context = context;
        this.activity = activity;
        this.updateFileInfo = updateFileInfo;
        this.updateParams = updateParams;
    }

    public void update() {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName
                    (), 0).versionCode;
            if (updateFileInfo.getVersionCode() > versionCode) {
                if (updateFileInfo.isForcedUpdate()) {
                    doForcedUpdate();
                } else {
                    doNonForcedUpdate();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DialogInterface.OnClickListener getNegativeOnClickListener() {
        return new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        };
    }

    private DialogInterface.OnClickListener getPositiveOnClickListener() {
        return new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DownloadDialog downloadDialog = updateParams.getDownloadDialog();
                showDownloadingDialogInApp(downloadDialog);
                new DownloadFileCommand(updateFileInfo, updateParams, new DownloadCallBackFactory
                        (context, activity, updateParams.getDownloadDialog().getProgressBar(), null,
                                updateFileInfo).getDownloadCallback(DownloadCallBackFactory
                        .CallBackType.forced)).execute();
            }
        };
    }

    private void doNonForcedUpdate() {
        DownloadDialog downloadDialog = updateParams.getDownloadDialog();
        NotificationView notificationView = new NotificationView(context, updateParams
                .getDialogTextAttrs());
        notificationView.showDownloadingInNotification();
        startDownloading(downloadDialog, notificationView);
    }

    private void doForcedUpdate() {
        DialogInterface.OnClickListener positiveOnClickListener = getPositiveOnClickListener
                ();
        DialogInterface.OnClickListener negativeOnClickListener = getNegativeOnClickListener();
        new com.digigene.autoupdate.view.AlertDialog(context, updateParams.getDialogTextAttrs())
                .showAlertDialog(positiveOnClickListener, negativeOnClickListener);
    }

    private void showDownloadingDialogInApp(DownloadDialog downloadDialog) {
        downloadDialog.setViews(context);
        new AlertDialog.Builder(context).setCustomTitle(downloadDialog.getDownloadView())
                .setCancelable(false).show();
    }

    private void startDownloading(DownloadDialog downloadDialog, NotificationView
            notificationView) {
        new DownloadFileCommand(updateFileInfo, updateParams, new DownloadCallBackFactory
                (context, activity,
                        downloadDialog.getProgressBar(), notificationView.getNotification(),
                        updateFileInfo).getDownloadCallback
                (DownloadCallBackFactory.CallBackType.notForced)).execute();
    }
}
