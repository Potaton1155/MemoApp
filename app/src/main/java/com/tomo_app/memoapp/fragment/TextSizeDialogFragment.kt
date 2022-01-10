package com.tomo_app.memoapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.tomo_app.memoapp.databinding.FragmentTextSizeDialogBinding


class TextSizeDialogFragment : DialogFragment() {
    private var _binding: FragmentTextSizeDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: DialogListener

    interface DialogListener {
        fun clickPositiveButton(size: Float)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement DialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentTextSizeDialogBinding.inflate(LayoutInflater.from(context))
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val builder = AlertDialog.Builder(context)
        var textSize = 16.0f

        binding.seekBar.min = 0
        binding.seekBar.max = 10
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                textSize = (p1 * 5 + 16).toFloat()
                binding.textView.textSize = textSize
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        builder.apply {
            setTitle("Set text size")
            setView(_binding!!.root)
            setNegativeButton("Cancel") { _, _ ->
            }
            setPositiveButton("OK") { _, _ ->
                listener.clickPositiveButton(textSize)
                pref.edit {
                    putInt("curposition", binding.seekBar.progress)
                }
            }
        }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        binding.seekBar.progress = pref.getInt("curposition", binding.seekBar.progress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}