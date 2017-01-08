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

import android.app.Activity;
import android.content.Context;
import android.opengl.EGLExt;
import android.os.Process;
import android.util.TypedValue;

import com.digigene.autoupdate.R;
import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.view.DownloadDialogView;

public class AlertDialogPresenterImpl implements AlertDialogPresenter {

    private DownloadDialogView downloadDialogView;
    private UpdateModel.DialogTextAttrs dialogTextAttrs;

    public AlertDialogPresenterImpl(DownloadDialogView downloadDialogView, UpdateModel
            .DialogTextAttrs
            dialogTextAttrs) {
        this.downloadDialogView = downloadDialogView;
        this.dialogTextAttrs = dialogTextAttrs;
    }

    @Override
    public void onPositiveButtonClicked(Context context) {
        downloadDialogView.loadView(context);
    }

    @Override
    public void onNegativeButtonClicked(Activity activity) {
        System.exit(0);
    }

    @Override
    public int getAppAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public UpdateModel.DialogTextAttrs getDialogTextAttrs() {
        return dialogTextAttrs;
    }

}
