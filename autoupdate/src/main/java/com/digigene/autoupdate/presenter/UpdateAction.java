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

import android.content.Context;
import android.content.pm.PackageManager;

import com.digigene.autoupdate.model.DownloadFileCommandImpl;
import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.view.AlertDialogView;

public class UpdateAction {
    private AlertDialogView alertDialogView;
    private DownloadFileCommandImpl downloadFileCommand;
    private Context context;
    private UpdateModel.UpdateFileInfo updateFileInfo;

    public UpdateAction(Context context, UpdateModel.UpdateFileInfo
            updateFileInfo, AlertDialogView alertDialogView,
                        DownloadFileCommandImpl downloadFileCommand) {
        this.context = context;
        this.updateFileInfo = updateFileInfo;
        this.alertDialogView = alertDialogView;
        this.downloadFileCommand = downloadFileCommand;
    }

    public void update() {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName
                    (), 0).versionCode;
            if (updateFileInfo.getVersionCode() > versionCode) {
                if (updateFileInfo.isForcedUpdate()) {
                    downloadFileCommand.setDownloadType(DownloadFileCommandImpl.DownloadType
                            .forced);
                    doForcedUpdate();
                } else {
                    downloadFileCommand.setDownloadType(DownloadFileCommandImpl.DownloadType
                            .notForced);
                    doNonForcedUpdate();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doNonForcedUpdate() {
        startDownloading();
    }

    private void doForcedUpdate() {
        alertDialogView.loadView(context);
    }

    private void startDownloading() {
        downloadFileCommand.execute();
    }
}
