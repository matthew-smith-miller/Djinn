package com.example.djinn

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException
import java.lang.IllegalStateException

class PartialGameDialogFragment : DialogFragment() {
    internal lateinit var listener: PartialGameDialogListener

    interface PartialGameDialogListener {
        fun onDialogPositiveClick(
            dialog: DialogFragment,
            player: Int,
            type: String,
            rawScore: Int
        )

        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as PartialGameDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement PartialGameDialogListener"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val dialogView = inflater.inflate(R.layout.dialog_partial_game, null)
            val spinner: Spinner? = dialogView.findViewById(R.id.spinner_partial_game_types)
            context?.let { el ->
                ArrayAdapter.createFromResource(
                    el,
                    R.array.dialog_partial_game_types,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner?.adapter = adapter
                }
            }
            builder.setView(dialogView).apply {
                setTitle(R.string.dialog_title)
                setMessage(R.string.dialog_description)
                setPositiveButton(
                    R.string.dialog_add,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(
                            this@PartialGameDialogFragment,
                            player = 0,
                            type = spinner?.selectedItem.toString(),
                            rawScore = dialogView.findViewById<EditText>(
                                R.id.edit_text_raw_score
                            ).text.toString().toInt()
                        )
                    })
                setNegativeButton(
                    R.string.dialog_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}