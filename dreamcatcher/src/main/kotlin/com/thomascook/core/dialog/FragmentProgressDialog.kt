package com.thomascook.core.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog

private const val ARG_TITLE = "arg_title"
private const val ARG_MESSAGE = "arg_message"

/**
 * Fragment for displaying progress dialogue
 */
class FragmentProgressDialogue : DialogFragment() {
    companion object {
        fun newInstance(title: String, message: String): FragmentProgressDialogue {
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_TITLE, title)

            val fragmentProgressDialogue = FragmentProgressDialogue()
            fragmentProgressDialogue.arguments = args
            return fragmentProgressDialogue
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments ?:
                throw IllegalStateException("Create instance of FragmentProgressDialogue using newInstance method")

        val title = args.getString(ARG_TITLE, "")
        val message = args.getString(ARG_MESSAGE, "")

        val builder = MaterialDialog.Builder(activity)
                .title(title)
                .content(message)
                .canceledOnTouchOutside(isCancelable)
                .cancelable(isCancelable)
                .progress(true, 0)

        return builder.build()
    }
}