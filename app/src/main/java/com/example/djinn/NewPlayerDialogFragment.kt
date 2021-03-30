package com.example.djinn

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.lang.ClassCastException
import java.lang.IllegalStateException

class NewPlayerDialogFragment : DialogFragment() {
    internal lateinit var listener: NewPlayerDialogListener

    interface NewPlayerDialogListener {
        fun onDialogPositiveClick(
            dialog: DialogFragment,
            playerName: String,
        )

        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NewPlayerDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement PartialGameDialogListener"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.dialog_new_player, null)
            val playerName: EditText? = dialogView.findViewById(R.id.edit_text_player_name)

            builder.setView(dialogView).apply {
                setTitle(R.string.new_player_dialog_title)
                setMessage(R.string.new_player_dialog_description)
                setPositiveButton(
                    R.string.dialog_add
                ) { dialog, _ ->
                    if (dialogView.findViewById<EditText>(
                            R.id.edit_text_player_name
                        ).text.toString() != ""
                    ) {
                        listener.onDialogPositiveClick(
                            this@NewPlayerDialogFragment,
                            dialogView.findViewById<EditText>(
                                R.id.edit_text_player_name
                            ).text.toString()
                        )
                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "You must enter a name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                setNegativeButton(
                    R.string.dialog_cancel
                ) { dialog, _ ->
                    dialog.cancel()
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}