package com.somesh.permissionmadeeasy.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;


public class DialogUtil {
    public static DialogUtil sDialogUtil;

    public static DialogUtil getInstance() {
        if (sDialogUtil == null) {
            sDialogUtil = new DialogUtil();
        }
        return sDialogUtil;
    }

    /**
     * @param context                     Activity/Fragment context
     * @param dialogTitle                 Title of dialog
     * @param titleIcon                   Title Icon
     * @param dialogMessage               Message Body of Dialog
     * @param positiveButtonText          Positive Button Text
     * @param dialogPositiveClickListener Positive Button click listener
     * @param negativeButtonText          Negative button text
     * @param dialogNegativeClickListener Negative Button click listner
     * @param isCancellable               Can dialog be canceled by clicking on outside
     * @return Created dialog instance/ object
     */
    public AlertDialog showAlertDialog(Context context, @Nullable String dialogTitle, int titleIcon, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, @Nullable String negativeButtonText, @Nullable DialogInterface.OnClickListener dialogNegativeClickListener, boolean isCancellable) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(isCancellable);
        builder.setMessage(dialogMessage);
        if (titleIcon != 0) {
            builder.setIcon(titleIcon);
        }

        builder.setPositiveButton(positiveButtonText, dialogPositiveClickListener);

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener);
        }

        if (dialogTitle != null) {
            builder.setTitle(dialogTitle);
        }

        alert = builder.create();
        alert.show();
        return alert;
    }

    public AlertDialog showAlertDialog(Context ctx, String dialogTitle, int titleIcon, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, String negativeButtonText, boolean isCancellable) {

        return showAlertDialog(ctx, dialogTitle, titleIcon, dialogMessage, positiveButtonText, dialogPositiveClickListener, negativeButtonText, (dialog, id) -> dialog.dismiss(), isCancellable);

    }

    public AlertDialog showAlertDialog(Context ctx, String dialogTitle, int titleIcon, String dialogMessage, String positiveButtonText, boolean isCancellable) {
        return showAlertDialog(ctx, dialogTitle, titleIcon, dialogMessage, positiveButtonText, (dialog, id) -> dialog.dismiss(), null, null, isCancellable);
    }


    public AlertDialog showAlertDialog(Context ctx, String dialogTitle, int titleIcon, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener onPositiveClickListener, boolean isCancellable) {
        return showAlertDialog(ctx, dialogTitle, titleIcon, dialogMessage, positiveButtonText, onPositiveClickListener, null, null, isCancellable);
    }


}
