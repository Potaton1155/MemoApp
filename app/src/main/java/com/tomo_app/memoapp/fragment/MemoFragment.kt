package com.tomo_app.memoapp.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.tomo_app.memoapp.R
import com.tomo_app.memoapp.databinding.FragmentMemoBinding
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

class MemoFragment : Fragment(), TextSizeDialogFragment.DialogListener {
    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!
    private var inputMethodManager: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editMemo = pref.getString("MEMO", "")
        val currentTextColor = pref.getInt("color", -16777216)
        val currentTextSize = pref.getFloat("size", 64.0f)
        val total = getString(R.string.total)

        binding.textInputEdit.setText(editMemo)
        binding.textInputEdit.setTextColor(currentTextColor)
        binding.textInputEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize)

        characterCountTextView(total)
        characterCounter()
        setTouchListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.preference, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                deleteDialog()
                true
            }
            R.id.color -> {
                val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()
                    //  set initial (default) color
                    .setInitialColor(binding.textInputEdit.currentTextColor)
                    //  set Color Model. ARGB, RGB or HSV
                    .setColorModel(ColorModel.HSV)
                    //  set is user be able to switch color model
                    .setColorModelSwitchEnabled(true)
                    //  set your localized string resource for OK button
                    .setButtonOkText(android.R.string.ok)
                    //  set your localized string resource for Cancel button
                    .setButtonCancelText(android.R.string.cancel)
                    //  callback for picked color (required)
                    .onColorSelected { color: Int ->
                        binding.textInputEdit.setTextColor(color)
                    }
                    .create()
                //  show dialog from Fragment
                colorPicker.show(childFragmentManager, "color_picker")
                true
            }
            R.id.size -> {
                TextSizeDialogFragment().show(childFragmentManager, "text_size_dialog")
                true
            }
            R.id.privacy_policy -> {
                findNavController().navigate(R.id.action_memoFragment_to_privacyPolicyFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //テキストエリア外タップでキーボードを隠す
    private fun setTouchListener() {
        inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        binding.textInputEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                inputMethodManager!!.hideSoftInputFromWindow(
                    binding.textInputEdit.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    private fun characterCountTextView(total: String) {
        binding.charCountTextView.text =
            total + binding.textInputEdit.text.toString().replace("\n", "")
                .replace("\u0020", "").length.toString()
    }

    private fun characterCounter() {
        binding.textInputEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val total = getString(R.string.total)
                binding.charCountTextView.text = total +
                        s.toString().replace("\n", "").replace("\u0020", "").length.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    //アラートダイアログ生成
    private fun deleteDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("WARNING!")
            .setMessage("Delete all text?")
            .setPositiveButton("OK") { _, _ ->
                val restoreText: String = binding.textInputEdit.text.toString()
                binding.textInputEdit.setText("")
                Snackbar.make(binding.constraintLayoutMemo, "deleted", Snackbar.LENGTH_LONG)
                    .apply {
                        setDuration(5000).show()
                        setAction("UNDO") {
                            binding.textInputEdit.setText(restoreText)
                        }
                    }
            }
            .setNegativeButton("CANCEL", null)
            .create()
        builder.show()
    }

    override fun clickPositiveButton(size: Float) {
        binding.textInputEdit.textSize = size
    }

    //データ変更を保存する
    override fun onPause() {
        super.onPause()
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pref.edit {
            putString("MEMO", binding.textInputEdit.text.toString().trimEnd())
            putInt("color", binding.textInputEdit.currentTextColor)
            putFloat("size", binding.textInputEdit.textSize)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}