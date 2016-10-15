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
import android.content.DialogInterface;
import android.util.TypedValue;

import com.digigene.autoupdate.R;

public class AlertDialog {
    private Context context;
    private DialogTextAttrs dialogTextAttrs;

    public AlertDialog(Context context, DialogTextAttrs dialogTextAttrs) {
        this.context = context;
        this.dialogTextAttrs = dialogTextAttrs;
    }

    public void showAlertDialog(DialogInterface.OnClickListener positiveOnClickListener,
                                DialogInterface.OnClickListener negativeOnClickListener) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context)
                .setMessage(dialogTextAttrs.getUpdateMessage()).setPositiveButton(dialogTextAttrs
                        .getPositiveText(), positiveOnClickListener).setNegativeButton
                        (dialogTextAttrs.getNegativeText(), negativeOnClickListener)
                .setCancelable(false).create();
        alertDialog.show();
        int accentColor = getAppAccentColor(context);
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(accentColor);
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(accentColor);
    }

    private int getAppAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }
}
