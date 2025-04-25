package com.example.android_interview.base

import android.annotation.SuppressLint
import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.CallSuper
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.android_interview.extension.closeSoftKeyBoard
import com.example.android_interview.ui.MainActivity

abstract class BaseFragment<T> : Fragment() where T : ViewBinding {

    open val mainActivity get() = requireActivity() as? MainActivity

    abstract val binding: T

    open fun T.initView() {
        // Not do something
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initView()
        setClearFocus(binding.root)
    }

    private fun setClearFocus(view: View) {
        if (view is AppCompatEditText) {
            hideKeyboardOutside(binding.root)
            view.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus()
                    v.closeSoftKeyBoard()
                }
                false
            })
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setClearFocus(innerView)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    open fun hideKeyboardOutside(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideKeyboardOutside(innerView)
            }
        }
    }

    private fun hideSoftKeyboard() {
        val activity = activity ?: return
        val imm: InputMethodManager =
            activity.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        activity.currentFocus?.requestFocus()
    }

}