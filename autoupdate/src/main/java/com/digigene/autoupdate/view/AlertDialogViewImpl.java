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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.digigene.autoupdate.model.UpdateModel;
import com.digigene.autoupdate.presenter.AlertDialogPresenter;

public class AlertDialogViewImpl implements AlertDialogView {
    private AlertDialogPresenter alertDialogPresenter;
    private Activity activity;

    public AlertDialogViewImpl(AlertDialogPresenter alertDialogPresenter, Activity activity) {
        this.alertDialogPresenter = alertDialogPresenter;
        this.activity = activity;
    }

    @Override
    public void loadView(Context context) {
        UpdateModel.DialogTextAttrs dialogTextAttrs = alertDialogPresenter.getDialogTextAttrs();
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context)
                .setMessage(dialogTextAttrs.getForcedUpdateMessage()).setPositiveButton(dialogTextAttrs
                        .getPositiveText(), getPositiveOnClickListener()).setNegativeButton
                        (dialogTextAttrs.getNegativeText(), getNegativeOnClickListener())
                .setCancelable(false).create();
        alertDialog.show();
        int accentColor = alertDialogPresenter.getAppAccentColor(context);
        setButtonsColor(alertDialog, accentColor);
    }

    private DialogInterface.OnClickListener getPositiveOnClickListener() {
        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialogPresenter.onPositiveButtonClicked(activity);
            }
        };
        return positiveOnClickListener;
    }

    private DialogInterface.OnClickListener getNegativeOnClickListener() {
        DialogInterface.OnClickListener negativeOnClickListener = new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialogPresenter.onNegativeButtonClicked(activity);
            }
        };
        return negativeOnClickListener;
    }

    private void setButtonsColor(AlertDialog alertDialog, int accentColor) {
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(accentColor);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(accentColor);
    }
}
