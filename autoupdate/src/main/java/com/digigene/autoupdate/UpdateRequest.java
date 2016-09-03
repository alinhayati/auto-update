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

package com.digigene.autoupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.net.HttpURLConnection;

public class UpdateRequest {
    private Notification notification;
    private NotificationManager notificationManager;
    private int NOTIFICATION_ID = 10;
    private Context context;
    private Activity activity;
    private ServerConnection.UpdateURLRequestParams serverConnectionUpdateURLRequestParams;
    private View downloadView;
    private ProgressBar progressBar;
    private ServerConnection serverConnection;

    private UpdateRequest(UpdateRequestBuilder updateRequestBuilder) {
        context = updateRequestBuilder.context;
        activity = updateRequestBuilder.activity;
        serverConnectionUpdateURLRequestParams = updateRequestBuilder
                .serverConnectionUpdateURLRequestParams;
        serverConnection = new ServerConnection(context, activity,
                serverConnectionUpdateURLRequestParams);
    }

    public void update() {
        new GetUpdateInfoFromServer().execute();
    }

    private void doWhenUpdateIsNotForced(UpdateFileInfo updateFileInfo) {
        showDownloadingInNotification();
        new DownloadFile(updateFileInfo, new DownloadCallBackFactory(context, activity,
                progressBar, notification, updateFileInfo).getDownloadCallback
                (DownloadCallBackFactory.CallBackType.notForced)).execute();
    }

    private void doWhenUpdateIsForced(final UpdateFileInfo updateFileInfo) {
        String updateMessage = context.getString(R.string.aup_forced_update_message);
        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDownloadingDialogInApp();
                new DownloadFile(updateFileInfo, new DownloadCallBackFactory(context, activity,
                        progressBar, null, updateFileInfo).getDownloadCallback
                        (DownloadCallBackFactory.CallBackType.forced)).execute();
            }
        };
        DialogInterface.OnClickListener negativeOnClickListener = new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        };
        new AlertDialog.Builder(context).setMessage(updateMessage)
                .setPositiveButton(context.getString(R.string.aup_positive_text_in_alert_dialog),
                        positiveOnClickListener).setNegativeButton(context.getString(R.string
                        .aup_negative_text_in_alert_dialog),
                negativeOnClickListener).setCancelable(false).show();
    }

    private void showDownloadingDialogInApp() {
        downloadView = LayoutInflater.from(context).inflate(R.layout.aup_downloading_layout,
                null, false);
        progressBar = (ProgressBar) downloadView.findViewById(R.id.aup_status_progress);
        ImageView imageView = (ImageView) downloadView.findViewById(R.id.aup_status_icon);
        imageView.setImageResource(R.drawable.ic_file_download_black_24dp);
        TextView textView = (TextView) downloadView.findViewById(R.id.aup_status_text);
        textView.setText(context.getString(R.string.aup_status_text) + context.getString(R.string
                .app_name));
        new AlertDialog.Builder(context).setCustomTitle(downloadView).setCancelable(false).show();
    }

    private void showDownloadingInNotification() {
        notification = new Notification(R.mipmap.ic_launcher, context.getString(R.string
                .aup_downloading_text),
                System.currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout
                .aup_downloading_layout);
        notification.contentView.setImageViewResource(R.id.aup_status_icon, R.drawable
                .ic_file_download_black_24dp);
        notification.contentView.setTextViewText(R.id.aup_status_text, context.getString(R.string
                .aup_status_text) + context.getString(R.string.app_name));
        notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static class UpdateRequestBuilder {
        private Context context;
        private Activity activity;
        private ServerConnection.UpdateURLRequestParams serverConnectionUpdateURLRequestParams;
        private int downloadProgressInt, bufferSize;

        public UpdateRequestBuilder(Context context, Activity activity, ServerConnection
                .UpdateURLRequestParams

                serverConnectionUpdateURLRequestParams) {
            this.context = context;
            this.activity = activity;
            this.serverConnectionUpdateURLRequestParams = serverConnectionUpdateURLRequestParams;
        }

        /**
         * @param downloadProgressInt This is in kilobytes, and denotes the size interval at the
         *                            end of which the download progress bar
         *                            shows progress
         */
        public UpdateRequestBuilder withDownloadProgressInterval(int downloadProgressInt) {
            this.downloadProgressInt = downloadProgressInt;
            return this;
        }

        /**
         * @param bufferSize This is the buffer size (in bytes) used when downloading the file
         *                   from server
         */
        public UpdateRequestBuilder withBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public UpdateRequest build() {
            return new UpdateRequest(this);
        }
    }

    private class GetUpdateInfoFromServer extends AsyncTask<Void, Void, Void> {
        private HttpURLConnection httpURLConnection;

        @Override
        protected Void doInBackground(Void... aVoid) {
            httpURLConnection = serverConnection.makeGetRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Response response = new ResponseActionFactory(serverConnectionUpdateURLRequestParams
                    .getResponseCallBackWhenUnsuccessful())
                    .doWhenThisResponseCodeIsReturned(httpURLConnection, context, activity);
            if (ServerConnection.isResponseSuccessful(response.getResponseCode())) {
                UpdateFileInfo updateFileInfo = new UpdateFileInfo(context, response);
                if (updateFileInfo.getVersionNumber() > BuildConfig.VERSION_CODE) {
                    if (updateFileInfo.isForcedUpdate()) {
                        doWhenUpdateIsForced(updateFileInfo);
                    } else {
                        doWhenUpdateIsNotForced(updateFileInfo);
                    }
                }
            } else {
                return;
            }
        }
    }
}
