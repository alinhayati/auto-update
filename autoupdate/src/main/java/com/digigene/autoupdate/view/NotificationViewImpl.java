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

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.RemoteViews;

import com.digigene.autoupdate.R;
import com.digigene.autoupdate.presenter.NotificationPresenter;

public class NotificationViewImpl implements NotificationView {
    private final Context context;
    private NotificationPresenter notificationPresenter;
    private int NOTIFICATION_ID;
    private Notification notification;

    public NotificationViewImpl(Context context) {
        this.context = context;
    }

    @Override
    public void loadView(Context context) {
        if (notification == null) {
            notification = buildNotification();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context
                        .NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void setPresenter(NotificationPresenter presenter) {
        this.notificationPresenter = presenter;
    }

    @Override
    public void setProgressBarValue(int progressBarValue) {
        if (notification == null) {
            loadView(context);
        }
        notification.contentView.setProgressBar(R.id.aup_status_progress, 100, progressBarValue,
                false);
    }

    private Notification buildNotification() {
        Notification notification = new Notification(R.mipmap.ic_launcher, notificationPresenter
                .getDialogTextAttrs().getDownloadingText(), System.currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout
                .aup_downloading_layout);
        notification.contentView.setImageViewResource(R.id.aup_status_icon, R.drawable
                .ic_file_download_black_24dp);
        notification.contentView.setTextViewText(R.id.aup_status_text, notificationPresenter
                .getDialogTextAttrs().getStatusText() + context.getString(R.string.app_name));
        return notification;
    }
}
