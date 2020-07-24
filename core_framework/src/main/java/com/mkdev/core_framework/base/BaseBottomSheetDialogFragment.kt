package com.mkdev.core_framework.base

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mkdev.core_framework.common.arch.StatefulViewModel
import com.mkdev.core_framework.viewmodel.CommunicationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val layoutId: Int,
    @StyleRes private val themeId: Int? = null
) : BottomSheetDialogFragment() {

    @BottomSheetBehavior.State
    open val defaultState: Int = BottomSheetBehavior.STATE_SETTLING

    private val communicationViewModel: CommunicationViewModel by sharedViewModel()

    private val job = SupervisorJob()
    val viewScope = CoroutineScope(Dispatchers.Main + job)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        view.isClickable = true
        view.isFocusable = true
        view.isEnabled = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).also {
            it.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            it.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    onBackPressed()
                } else
                    onKeyDown(keyCode, event)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return themeId?.let { BottomSheetDialog(requireContext(), theme) } ?: (super.onCreateDialog(
            savedInstanceState
        ) as BottomSheetDialog).apply {
            behavior.state = defaultState
        }

    }

    @Deprecated(
        "Pleae use subscriveOnView, since this one subscrivbes the ViewModel on fragment itself which is a bad practice",
        ReplaceWith("subscribeOnView")
    )
    protected fun <STATE : Any> StatefulViewModel<STATE>.subscribe(stateChange: (STATE) -> Unit) {
        this.observe(this@BaseBottomSheetDialogFragment, stateChange)
    }

    protected fun <STATE : Any> StatefulViewModel<STATE>.subscribeOnView(stateChange: (STATE) -> Unit) {
        this.observe(viewLifecycleOwner, stateChange)
    }

    @Deprecated(
        "Pleae use subscriveOnView, since this one subscrivbes the LiveData on fragment itself which is a bad practice",
        ReplaceWith("subscribeOnView")
    )
    inline fun <T> LiveData<T>.subscribe(crossinline onNext: T.() -> Unit) {
        observe(this@BaseBottomSheetDialogFragment, Observer {
            it?.let(onNext)
        })
    }

    inline fun <T> LiveData<T>.subscribeOnView(crossinline onNext: T.() -> Unit) {
        observe(viewLifecycleOwner, Observer {
            it?.let(onNext)
        })
    }

    protected fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    protected open fun onBackPressed(): Boolean {
        return false
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    override fun getTheme(): Int = themeId ?: super.getTheme()
}