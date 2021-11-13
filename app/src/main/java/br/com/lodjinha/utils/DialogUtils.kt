package br.com.lodjinha.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import br.com.lodjinha.R

object DialogUtils {
    fun showDialog(context: Context?, message: String, onDismissListener: () -> Unit) {
        context?.let {
            buildDialog(it, message, context.getString(R.string.OK),
                DialogInterface.OnDismissListener { onDismissListener() })
        }
    }

    private fun buildDialog(context: Context, message: String, positiveButton: String, onDismissListener: DialogInterface.OnDismissListener?) {
        AlertDialog.Builder(context, R.style.Dialog)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ -> dialog.dismiss() }
            .setOnDismissListener(onDismissListener)
            .create()
            .show()
    }
}