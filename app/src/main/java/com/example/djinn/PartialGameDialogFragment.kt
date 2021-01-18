package com.example.djinn

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import java.lang.ClassCastException
import java.lang.IllegalStateException

class PartialGameDialogFragment : DialogFragment() {
    internal lateinit var listener: PartialGameDialogListener

    private val playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

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
        val playerIds = arguments?.getIntegerArrayList(PLAYER_IDS)
        var playerIdsAndNames: List<DataClasses.IdNameTuple>? = null
        if (playerIds != null) {
            playerViewModel.getPlayerIdsAndNames(playerIds).observe(owner = this) { returnedList ->
                playerIdsAndNames = returnedList
            }
        }
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.dialog_partial_game, null)
            val spinnerPlayers: Spinner? = dialogView.findViewById(R.id.spinner_players)
            val spinnerTypes: Spinner? = dialogView.findViewById(R.id.spinner_partial_game_types)

            if (playerIdsAndNames != null) {
                context?.let { el ->
                    ArrayAdapter(
                        el,
                        android.R.layout.simple_spinner_item,
                        playerIdsAndNames!!
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerPlayers?.adapter = adapter
                    }
                }
            }
            context?.let { el ->
                ArrayAdapter.createFromResource(
                    el,
                    R.array.dialog_partial_game_types,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTypes?.adapter = adapter
                }
            }
            builder.setView(dialogView).apply {
                setTitle(R.string.dialog_title)
                setMessage(R.string.dialog_description)
                setPositiveButton(
                    R.string.dialog_add
                ) { dialog, id ->
                    if (dialogView.findViewById<EditText>(
                            R.id.edit_text_raw_score
                        ).text.toString() != ""
                    ) {
                        listener.onDialogPositiveClick(
                            this@PartialGameDialogFragment,
                            (spinnerPlayers?.selectedItem as DataClasses.IdNameTuple).id,
                            spinnerTypes?.selectedItem.toString(),
                            dialogView.findViewById<EditText>(
                                R.id.edit_text_raw_score
                            ).text.toString().toInt()
                        )
                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "You must enter a raw score",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                setNegativeButton(
                    R.string.dialog_cancel
                ) { dialog, id ->
                    dialog.cancel()
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}