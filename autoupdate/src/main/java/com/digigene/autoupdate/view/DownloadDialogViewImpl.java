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

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digigene.autoupdate.R;
import com.digigene.autoupdate.presenter.DownloadDialogPresenter;

public class DownloadDialogViewImpl implements DownloadDialogView {
    private View downloadView;
    private ProgressBar progressBar;
    private ImageView downloadImageView;
    private TextView statusTextView;
    private DownloadDialogPresenter downloadDialogPresenter;

    public DownloadDialogViewImpl(DownloadDialogPresenter downloadDialogPresenter) {
        this.downloadDialogPresenter = downloadDialogPresenter;
        downloadDialogPresenter.setView(this);
    }

    @Override
    public void loadView(Context context) {
        findElements(context);
        initialize(context);
        downloadDialogPresenter.startDownloading();
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void setProgressBarValue(int progressBarValue) {
        progressBar.setProgress(progressBarValue);
    }

    private void initialize(Context context) {
        statusTextView.setText(downloadDialogPresenter.getStatusText());
        downloadImageView.setImageResource(downloadDialogPresenter.getImageResourceId());
        new AlertDialog.Builder(context).setCustomTitle(downloadView).setCancelable(false).show();
    }

    private void findElements(Context context) {
        downloadView = LayoutInflater.from(context).inflate(R.layout.aup_downloading_layout,
                null, false);
        progressBar = (ProgressBar) downloadView.findViewById(R.id.aup_status_progress);
        downloadImageView = (ImageView) downloadView.findViewById(R.id.aup_status_icon);
        statusTextView = (TextView) downloadView.findViewById(R.id.aup_status_text);
    }
}
