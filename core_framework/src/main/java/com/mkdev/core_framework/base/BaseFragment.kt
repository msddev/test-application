package com.mkdev.core_framework.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.mkdev.core_framework.utils.FragmentInterface
import com.mkdev.core_framework.utils.ResultHandlerContainer
import com.mkdev.core_framework.viewmodel.CommunicationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class BaseFragment(@LayoutRes val layoutResource: Int) : CoreFragment(), FragmentInterface,
    ResultHandlerContainer {

    private val communicationViewModel: CommunicationViewModel by sharedViewModel()


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
        communicationViewModel.getEventsLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (onResultProvided(it.first, it.second)) {
                    communicationViewModel.onResultConsumed(it.first, it.second)
                }
            }
        })
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

    /**»
     * Will be called on every fragment of the activity with a specific request code, when a fragment calls setResult(requestCode, data)
     * The returned value indicates that this fragment has handled the result and no other fragment should handle it.
     */
    override fun onResultProvided(request: Any, result: Any): Boolean {
        return false
    }

    protected fun setResult(request: Any, data: Any) {
        communicationViewModel.onResultProvided(request, data)
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