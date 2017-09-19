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
import android.content.Context;
import android.os.AsyncTask;

import com.digigene.autoupdate.Dagger.DaggerWrapper;
import com.digigene.autoupdate.model.Response;
import com.digigene.autoupdate.model.ResponseActionFactory;
import com.digigene.autoupdate.model.ServerConnection;
import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.model.UpdateParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

public class UpdateRequest {

    @Inject
    UpdateAction updateAction;
    @Inject
    UpdateModel.UpdateFileInfo updateFileInfo;
    @Inject
    ServerConnection serverConnection;
    @Inject
    Context context;
    @Inject
    UpdateModel.DialogTextAttrs dialogTextAttrs;
    private Activity activity;
    private UpdateParams updateParams;

    public UpdateRequest(Activity activity, UpdateParams updateParams) {
        this.activity = activity;
        this.updateParams = updateParams;
        DaggerWrapper.getDependencyComponent(activity, updateParams).inject(this);
        if (updateParams.getUserDialogTextAttrs() != null) {
            dialogTextAttrs.setUserDialogTextAttrs(updateParams.getUserDialogTextAttrs());
        }
    }

    public void update() {
        new UpdateCommand().execute();
    }

    private class UpdateCommand extends AsyncTask<Void, Void, Response> {
        private Response response;
        private HttpURLConnection httpURLConnection;

        @Override
        protected Response doInBackground(Void... aVoid) {
            httpURLConnection = serverConnection.makeGetRequest();
            try {
                response = new ResponseActionFactory(updateParams.getResUnsuccessful())
                        .makeResponse(httpURLConnection, context, activity);
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response != null) {
                if (ServerConnection.isResponseSuccessful(response.getResponseCode())) {
                    updateFileInfo.extractDataFromJson(response, updateParams.getJsonKeys());
                    updateDialogTextsAttrs();
                    updateAction.update();
                } else {
                    return;
                }
            } else {
                return;
            }
        }

        private void updateDialogTextsAttrs() {
            String updateMessageKey = updateParams.getJsonKeys().getUpdateMessageKey();
            if (updateMessageKey != null) {
                JSONObject dataJSON = null;
                try {
                    dataJSON = new JSONObject(response.getResponseString());
                    String updateMessageFromServer = dataJSON.optString(updateMessageKey);
                    if (updateMessageFromServer != null || !updateMessageFromServer.trim()
                            .isEmpty()) {
                        dialogTextAttrs.setForcedUpdateMessageFromServer
                                (updateMessageFromServer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
