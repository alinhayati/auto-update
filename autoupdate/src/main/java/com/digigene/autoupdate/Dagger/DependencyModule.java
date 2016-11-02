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

package com.digigene.autoupdate.Dagger;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;

import com.digigene.autoupdate.model.DialogTextAttrsImpl;
import com.digigene.autoupdate.model.DownloadFileCommand;
import com.digigene.autoupdate.model.ServerConnection;
import com.digigene.autoupdate.model.UpdateFileInfoImpl;
import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.model.UpdateParams;
import com.digigene.autoupdate.presenter.AlertDialogPresenter;
import com.digigene.autoupdate.presenter.AlertDialogPresenterImpl;
import com.digigene.autoupdate.presenter.DownloadDialogPresenter;
import com.digigene.autoupdate.presenter.DownloadDialogPresenterImpl;
import com.digigene.autoupdate.presenter.NotificationPresenter;
import com.digigene.autoupdate.presenter.NotificationPresenterImpl;
import com.digigene.autoupdate.UpdateAction;
import com.digigene.autoupdate.view.AlertDialogView;
import com.digigene.autoupdate.view.AlertDialogViewImpl;
import com.digigene.autoupdate.view.DownloadDialogView;
import com.digigene.autoupdate.view.DownloadDialogViewImpl;
import com.digigene.autoupdate.view.NotificationView;
import com.digigene.autoupdate.view.NotificationViewImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class DependencyModule {

    private Activity activity;
    private UpdateParams updateParams;

    public DependencyModule(Activity activity, UpdateParams updateParams) {
        this.activity = activity;
        this.updateParams = updateParams;
    }

    @AutoUpdateScope
    @Provides
    Context provideContext() {
        return activity;
    }

    @AutoUpdateScope
    @Provides
    Activity provideActivity() {
        return activity;
    }

    @AutoUpdateScope
    @Provides
    UpdateParams provideUpdateParams() {
        return updateParams;
    }

    @AutoUpdateScope
    @Provides
    UpdateModel.DialogTextAttrs provideDialogTextAttrs() {
        return new DialogTextAttrsImpl();
    }

    @AutoUpdateScope
    @Provides
    AlertDialogPresenter provideAlertDialogPresenter(DownloadDialogView downloadDialogView,
                                                     UpdateModel.DialogTextAttrs dialogTextAttrs) {
        return new AlertDialogPresenterImpl(downloadDialogView, dialogTextAttrs);
    }

    @AutoUpdateScope
    @Provides
    DownloadDialogView provideDownloadDialogView(DownloadDialogPresenter downloadDialogPresenter) {
        return new DownloadDialogViewImpl(downloadDialogPresenter);
    }

    @AutoUpdateScope
    @Provides
    AlertDialogView provideAlertDialogView(AlertDialogPresenter alertDialogPresenter, Activity
            activity) {
        return new AlertDialogViewImpl(alertDialogPresenter, activity);
    }

    @AutoUpdateScope
    @Provides
    DownloadDialogPresenter provideDownloadDialogPresenter(Context context, Activity activity,
                                                           UpdateModel
                                                                   .DialogTextAttrs
                                                                   dialogTextAttrs, UpdateModel
                                                                   .UpdateFileInfo updateFileInfo,
                                                           DownloadFileCommand
                                                                   downloadFileCommand) {
        return new DownloadDialogPresenterImpl(context, activity, dialogTextAttrs, updateFileInfo,
                downloadFileCommand);
    }

    @AutoUpdateScope
    @Provides
    DownloadFileCommand provideDownloadFileCommand(
            UpdateModel.UpdateFileInfo
                    updateFileInfo, UpdateParams
                    updateParams) {
        return new DownloadFileCommand(updateFileInfo, updateParams);
    }

    @AutoUpdateScope
    @Provides
    ProgressBar provideProgressBar(DownloadDialogView downloadDialogView) {
        return downloadDialogView.getProgressBar();
    }

    @AutoUpdateScope
    @Provides
    ServerConnection provideServerConnection(Context context, Activity activity, UpdateParams
            updateParams) {
        return new ServerConnection(context, activity, updateParams);
    }

    @AutoUpdateScope
    @Provides
    UpdateAction provideUpdateAction(Context context, UpdateModel
            .UpdateFileInfo updateFileInfo, AlertDialogView
                                             alertDialogView, DownloadFileCommand
                                             downloadFileCommand) {
        return new UpdateAction(context, updateFileInfo, alertDialogView,
                downloadFileCommand);
    }

    @AutoUpdateScope
    @Provides
    UpdateModel.UpdateFileInfo provideUpdateFileInfo() {
        return new UpdateFileInfoImpl();
    }

    @AutoUpdateScope
    @Provides
    NotificationView provideNotificationView(Context context) {
        return new NotificationViewImpl(context);
    }

    @AutoUpdateScope
    @Provides
    NotificationPresenter provideNotificationPresenter(Context context, NotificationView
            notificationView, UpdateModel.UpdateFileInfo updateFileInfo, UpdateModel
                                                               .DialogTextAttrs dialogTextAttrs) {
        return new NotificationPresenterImpl(context, notificationView, updateFileInfo,
                dialogTextAttrs);
    }

}
