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

public class NotificationView {
    private final Context context;
    private final DialogTextAttrs dialogTextAttrs;
    private int NOTIFICATION_ID;
    private Notification notification;

    public NotificationView(Context context, DialogTextAttrs dialogTextAttrs) {
        this.context = context;
        this.dialogTextAttrs = dialogTextAttrs;
    }

    public void showDownloadingInNotification() {
        notification = new Notification(R.mipmap.ic_launcher, dialogTextAttrs.getDownloadingText(),
                System.currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout
                .aup_downloading_layout);
        notification.contentView.setImageViewResource(R.id.aup_status_icon, R.drawable
                .ic_file_download_black_24dp);
        notification.contentView.setTextViewText(R.id.aup_status_text, dialogTextAttrs
                .getStatusText() + context.getString(R.string.app_name));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context
                        .NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public Notification getNotification() {
        return notification;
    }
}
