package com.mkdev.core_framework.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import com.mkdev.core_framework.utils.FragmentInterface

open class BaseFragment(@LayoutRes val layoutResource: Int) : CoreFragment(), FragmentInterface {

    private var savedView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        savedView ?: inflater.inflate(layoutResource, container, false).also {
            savedView = it
            onViewInitialized(it!!)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (savedView?.parent as? ViewGroup)?.removeView(savedView!!)
    }

    override fun getView(): View? {
        return super.getView() ?: savedView
    }

    override fun onDetach() {
        super.onDetach()
        savedView = null
    }

    protected fun hideSoftInput(view: View) {
        val inputMethodManager = context?.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    protected fun clearSavedView() {
        savedView = null
    }
}