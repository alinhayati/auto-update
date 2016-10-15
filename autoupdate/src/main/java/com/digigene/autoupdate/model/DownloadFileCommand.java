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

package com.digigene.autoupdate.model;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.digigene.autoupdate.presenter.UpdateParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileCommand extends AsyncTask<Void, Integer, Void> {

    private int downloadProgressInterval;
    private int bufferSize;
    private String downloadURL, fileName;
    private DownloadCallback downloadCallback;

    public DownloadFileCommand(UpdateFileInfo updateFileInfo, UpdateParams updateParams,
                               DownloadCallback downloadCallback) {
        this.downloadURL = updateFileInfo.getDownloadUrl();
        this.fileName = updateFileInfo.getFileName();
        this.bufferSize = updateParams.getBufferSize();
        this.downloadProgressInterval = updateParams.getDownloadDialog().getDownloadProgressInt();
        this.downloadCallback = downloadCallback;
    }

    @Override
    protected Void doInBackground(Void... aVoid) {
        try {
            URL downloadURL = new URL(this.downloadURL);
            HttpURLConnection httpURLConnection = ServerConnection.buildURLConnection(downloadURL);
            httpURLConnection = ServerConnection.setURLConnectionGetParams(httpURLConnection, null);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (!ServerConnection.isResponseSuccessful(responseCode)) {
                doWhenResponseIsUnsuccessful(httpURLConnection);
            } else {
                doWhenResponseIsSuccessful(httpURLConnection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        downloadCallback.onDownloadFinished();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        downloadCallback.onDownloading(progress[0]);
    }

    @NonNull
    private static FileOutputStream makeFileOutputStream(File directory, String fileName) throws
            FileNotFoundException {
        File outputFile = new File(directory, fileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        return new FileOutputStream(outputFile);
    }

    private void doWhenResponseIsSuccessful(HttpURLConnection httpURLConnection) throws
            IOException {
        InputStream is;
        File directory = Environment.getExternalStorageDirectory();
        directory.mkdirs();
        if (directory.isDirectory()) {
            is = httpURLConnection.getInputStream();
            downloadFile(is, httpURLConnection, directory);
            is.close();
        }
    }

    private void doWhenResponseIsUnsuccessful(HttpURLConnection httpURLConnection) {
        InputStream is;
        is = httpURLConnection.getErrorStream();
    }

    private void downloadFile(InputStream is, HttpURLConnection httpURLConnection, File
            directory) throws IOException {
        int fileLength = httpURLConnection.getContentLength();
        byte[] buffer = new byte[bufferSize];
        int bytesRead = 0, downloadedData = 0, requestFrequency = 0;
        FileOutputStream fos = makeFileOutputStream(directory, fileName);
        while ((bytesRead = is.read(buffer)) > 0) {
            requestFrequency++;
            fos.write(buffer, 0, bytesRead);
            downloadedData += bytesRead;
            int progress = (int) ((downloadedData / (float) fileLength) * 100);
            if (requestFrequency % downloadProgressInterval == 0)
                publishProgress(progress);
        }
        publishProgress(100);
        fos.close();
    }


    public interface DownloadCallback {
        void onDownloading(int progressPercent);

        void onDownloadFinished();
    }
}
