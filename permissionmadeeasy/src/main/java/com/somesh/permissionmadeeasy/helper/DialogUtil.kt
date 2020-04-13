package com.somesh.permissionmadeeasy.helper

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object DialogUtil {

    /**
     * @param context                     Activity/Fragment context
     * @param dialogTitle                 Title of dialog
     * @param titleIcon                   Title Icon
     * @param dialogMessage               Message Body of Dialog
     * @param positiveButtonText          Positive Button Text
     * @param dialogPositiveClickListener Positive Button click listener
     * @param negativeButtonText          Negative button text
     * @param dialogNegativeClickListener Negative Button click listener
     * @param isCancellable               Can dialog be canceled by clicking on outside
     * @return Created dialog instance/ object
     */
    private fun showAlertDialog(context: Context, dialogTitle: String?, titleIcon: Int, dialogMessage: String, positiveButtonText: String, dialogPositiveClickListener: DialogInterface.OnClickListener, negativeButtonText: String?, dialogNegativeClickListener: DialogInterface.OnClickListener?, isCancellable: Boolean): AlertDialog {
        val alert: AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(isCancellable)
        builder.setMessage(dialogMessage)
        if (titleIcon != 0) {
            builder.setIcon(titleIcon)
        }
        builder.setPositiveButton(positiveButtonText, dialogPositiveClickListener)
        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener)
        }
        if (dialogTitle != null) {
            builder.setTitle(dialogTitle)
        }
        alert = builder.create()
        alert.show()
        return alert
    }

    @JvmStatic
    fun showAlertDialog(ctx: Context, dialogTitle: String?, titleIcon: Int, dialogMessage: String, positiveButtonText: String, dialogPositiveClickListener: DialogInterface.OnClickListener, negativeButtonText: String?, isCancellable: Boolean): AlertDialog {
        return showAlertDialog(ctx, dialogTitle, titleIcon, dialogMessage, positiveButtonText, dialogPositiveClickListener, negativeButtonText, DialogInterface.OnClickListener { dialog: DialogInterface, id: Int -> dialog.dismiss() }, isCancellable)
    }

    @JvmStatic
    fun showAlertDialog(ctx: Context, dialogTitle: String?, titleIcon: Int, dialogMessage: String, positiveButtonText: String, isCancellable: Boolean): AlertDialog {
        return showAlertDialog(ctx, dialogTitle, titleIcon, dialogMessage, positiveButtonText, DialogInterface.OnClickListener { dialog: DialogInterface, id: Int -> dialog.dismiss() }, null, null, isCancellable)
    }

    @JvmStatic
    fun showAlertDialog(ctx: Context, dialogTitle: String?, titleIcon: Int, dialogMessage: String, positiveButtonText: String, onPositiveClickListener: DialogInterface.OnClickListener, isCancellable: Boolean): AlertDialog {
        return showAlertDialog(ctx, dialogTitle, titleIcon, dialogMessage, positiveButtonText, onPositiveClickListener, null, null, isCancellable)
    }
}