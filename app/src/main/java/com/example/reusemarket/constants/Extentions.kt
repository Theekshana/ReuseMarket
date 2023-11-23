package com.example.reusemarket.constants

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.reusemarket.R

/**
 * Extension function to set the visibility of a View to VISIBLE.
 */
fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

/**
 * Display an alert dialog with Yes and No options.
 */
fun Context.showAlertYeNo(
    title: String, message: String, positiveClick: () -> Unit,
    negativeClick: (() -> Unit)? = null,
) {
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            positiveClick()
        }
        setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            negativeClick?.invoke()
        }
    }.show()
}

class NoInternetErrorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.error_dialog_fragment, null)
        val builder = android.app.AlertDialog.Builder(requireContext())

        val okButton = view.findViewById<Button>(R.id.btnRetry)
        okButton.setOnClickListener {
            dialog?.dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}

