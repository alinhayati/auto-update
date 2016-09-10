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
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class UpdateRequest {
    private Notification notification;
    private NotificationManager notificationManager;
    private int NOTIFICATION_ID, downloadProgressInt, bufferSize;
    private Context context;
    private Activity activity;
    private UpdateRequestParams updateRequestParams;
    private View downloadView;
    private ProgressBar progressBar;
    private ServerConnection serverConnection;
    private DialogTextAttrs dialogTextAttrs;

    private UpdateRequest(Builder builder) {
        context = builder.context;
        activity = builder.activity;
        updateRequestParams = builder
                .updateRequestParams;
        downloadProgressInt = builder.downloadProgressInt;
        bufferSize = builder.bufferSize;
        dialogTextAttrs = builder.dialogTextAttrs;
        serverConnection = new ServerConnection(context, activity,
                updateRequestParams);
    }

    public void update() {
        new GetUpdateInfoFromServer().execute();
    }

    @NonNull
    private DialogInterface.OnClickListener getNegativeOnClickListener() {
        return new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        };
    }

    private void doWhenUpdateIsNotForced(UpdateFileInfo updateFileInfo) {
        showDownloadingInNotification();
        startDownloading(updateFileInfo);
    }

    private void doWhenUpdateIsForced(final UpdateFileInfo updateFileInfo) {
        DialogInterface.OnClickListener positiveOnClickListener = getOnClickListener
                (updateFileInfo);
        DialogInterface.OnClickListener negativeOnClickListener = getNegativeOnClickListener();
        showAlertDialog(positiveOnClickListener, negativeOnClickListener);
    }

    private void startDownloading(UpdateFileInfo updateFileInfo) {
        new DownloadFile(updateFileInfo, new DownloadCallBackFactory(context, activity,
                progressBar, notification, updateFileInfo).getDownloadCallback
                (DownloadCallBackFactory.CallBackType.notForced)).execute();
    }

    @NonNull
    private DialogInterface.OnClickListener getOnClickListener(final UpdateFileInfo
                                                                       updateFileInfo) {
        return new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDownloadingDialogInApp();
                new DownloadFile(updateFileInfo, new DownloadCallBackFactory(context, activity,
                        progressBar, null, updateFileInfo).getDownloadCallback
                        (DownloadCallBackFactory.CallBackType.forced)).execute();
            }
        };
    }

    private void showAlertDialog(DialogInterface.OnClickListener positiveOnClickListener,
                                 DialogInterface.OnClickListener negativeOnClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(dialogTextAttrs
                .getUpdateMessage()).setPositiveButton(dialogTextAttrs.getPositiveText(),
                positiveOnClickListener).setNegativeButton(dialogTextAttrs.getNegativeText(),
                negativeOnClickListener).setCancelable(false).create();
        alertDialog.show();
        int accentColor = getAppAccentColor(context);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(accentColor);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(accentColor);
    }

    private void showDownloadingDialogInApp() {
        downloadView = LayoutInflater.from(context).inflate(R.layout.aup_downloading_layout,
                null, false);
        progressBar = (ProgressBar) downloadView.findViewById(R.id.aup_status_progress);
        ImageView imageView = (ImageView) downloadView.findViewById(R.id.aup_status_icon);
        imageView.setImageResource(R.drawable.ic_file_download_black_24dp);
        TextView textView = (TextView) downloadView.findViewById(R.id.aup_status_text);
        textView.setText(dialogTextAttrs.getStatusText() + " " + context.getString(R
                .string
                .app_name));
        new AlertDialog.Builder(context).setCustomTitle(downloadView).setCancelable(false).show();
    }

    private void showDownloadingInNotification() {
        notification = new Notification(R.mipmap.ic_launcher, dialogTextAttrs.getDownloadingText(),
                System.currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout
                .aup_downloading_layout);
        notification.contentView.setImageViewResource(R.id.aup_status_icon, R.drawable
                .ic_file_download_black_24dp);
        notification.contentView.setTextViewText(R.id.aup_status_text, dialogTextAttrs
                .getStatusText() + context.getString(R.string.app_name));
        notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private int getAppAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public static class Builder {
        private Context context;
        private Activity activity;
        private UpdateRequestParams updateRequestParams;
        private int downloadProgressInt = 200;
        private int bufferSize = 1024;
        private DialogTextAttrs dialogTextAttrs = new DialogTextAttrs();

        public Builder(@NonNull Context context, @NonNull Activity activity, @NonNull
        UpdateRequestParams
                updateRequestParams) {
            this.context = context;
            this.activity = activity;
            this.updateRequestParams = updateRequestParams;
        }

        /**
         * @param downloadProgressInt This is in kilobytes, and denotes the size interval at the
         *                            end of which the download progress bar
         *                            shows progress
         */
        public Builder withDownloadProgressInterval(@NonNull int downloadProgressInt) {
            if (downloadProgressInt == 0)
                throw new IllegalArgumentException("downloadProgressInt must be larger than zero");
            this.downloadProgressInt = downloadProgressInt;
            return this;
        }

        /**
         * @param bufferSize This is the buffer size (in bytes) used when downloading the file
         *                   from server
         */
        public Builder withBufferSize(@NonNull int bufferSize) {
            if (bufferSize == 0)
                throw new IllegalArgumentException("bufferSize must be larger than zero");
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder withDialogTextAttrs(@NonNull DialogTextAttrs dialogTextAttrs) {
            this.dialogTextAttrs = dialogTextAttrs;
            return this;
        }

        public UpdateRequest build() {
            return new UpdateRequest(this);
        }
    }

    private class GetUpdateInfoFromServer extends AsyncTask<Void, Void, Void> {
        Response response;
        private HttpURLConnection httpURLConnection;
        private int versionCode;

        @Override
        protected Void doInBackground(Void... aVoid) {
            httpURLConnection = serverConnection.makeGetRequest();
            response = new ResponseActionFactory(updateRequestParams
                    .getResponseCallbackWhenUnsuccessful())
                    .doWhenThisResponseCodeIsReturned(httpURLConnection, context, activity);
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (ServerConnection.isResponseSuccessful(response.getResponseCode())) {
                UpdateFileInfo updateFileInfo = new UpdateFileInfo(response,
                        updateRequestParams.getJsonKeys(), dialogTextAttrs, downloadProgressInt,
                        bufferSize);
                updateDialogTextsAttrs();
                try {
                    versionCode = context.getPackageManager().getPackageInfo(context.getPackageName
                            (), 0).versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (updateFileInfo.getVersionCode() > versionCode) {
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

        private void updateDialogTextsAttrs() {
            String updateMessageKey = updateRequestParams.getJsonKeys().getUpdateMessageKey();
            if (updateMessageKey != null) {
                JSONObject dataJSON = null;
                try {
                    dataJSON = new JSONObject(response.getResponseString());
                    String updateMessageFromServer = dataJSON.optString(updateMessageKey);
                    if (updateMessageFromServer != null || !updateMessageFromServer.trim()
                            .isEmpty()) {
                        dialogTextAttrs.setUpdateMessage(updateMessageFromServer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
