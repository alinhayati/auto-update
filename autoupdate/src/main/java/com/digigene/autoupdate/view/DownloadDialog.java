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

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digigene.autoupdate.R;

public class DownloadDialog {
    private int downloadProgressInt = 200;
    private View downloadView;
    private ProgressBar progressBar;
    private ImageView downloadImageView;
    private TextView statusTextView;
    private DialogTextAttrs dialogTextAttrs = new DialogTextAttrs();

    public DownloadDialog() {
    }

    public View getDownloadView() {
        return downloadView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public DialogTextAttrs getDialogTextAttrs() {
        return dialogTextAttrs;
    }

    public void setDialogTextAttrs(@NonNull DialogTextAttrs dialogTextAttrs) {
        this.dialogTextAttrs = dialogTextAttrs;
    }

    public int getDownloadProgressInt() {
        return downloadProgressInt;
    }

    /**
     * @param downloadProgressInt Download progress interval in kilobytes
     */
    public void setDownloadProgressInt(int downloadProgressInt) {
        if (downloadProgressInt > 0) {
            this.downloadProgressInt = downloadProgressInt;
        } else
            throw new IllegalArgumentException("The downloadProgressInt must an integer greater " +
                    "than zero");
    }

    public void setViews(Context context) {
        initializeViews(context);
        setValues(context);
    }

    private void setValues(Context context) {
        statusTextView.setText(dialogTextAttrs.getStatusText() + " " + context.getString(R.string
                .app_name));
        downloadImageView.setImageResource(R.drawable.ic_file_download_black_24dp);
    }

    private void initializeViews(Context context) {
        downloadView = LayoutInflater.from(context).inflate(R.layout.aup_downloading_layout,
                null, false);
        progressBar = (ProgressBar) downloadView.findViewById(R.id.aup_status_progress);
        downloadImageView = (ImageView) downloadView.findViewById(R.id.aup_status_icon);
        statusTextView = (TextView) downloadView.findViewById(R.id.aup_status_text);
    }
}
