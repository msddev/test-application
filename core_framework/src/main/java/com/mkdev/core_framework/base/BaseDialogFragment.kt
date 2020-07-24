package com.mkdev.core_framework.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.mkdev.core_framework.common.arch.StatefulViewModel
import com.mkdev.core_framework.utils.FragmentInterface
import com.mkdev.core_framework.utils.ResultHandlerContainer
import com.mkdev.core_framework.viewmodel.CommunicationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class BaseDialogFragment(@LayoutRes val layoutResource: Int) : DialogFragment(),
    FragmentInterface,
    ResultHandlerContainer {

    private val communicationViewModel: CommunicationViewModel by sharedViewModel()

    private val job = SupervisorJob()
    val viewScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layoutResource, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
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

    override fun onResume() {
        dialog?.window?.apply {
            val params = attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = params
        }
        super.onResume()
    }

    @Deprecated(
        "It is deprecated due to it uses  fragment instead of lifeCycleOwner",
        ReplaceWith("subscribeWithView")
    )
    override fun <STATE : Any> StatefulViewModel<STATE>.subscribe(stateChange: (STATE) -> Unit) {
        this.observe(this@BaseDialogFragment, stateChange)
    }

    override fun <STATE : Any> StatefulViewModel<STATE>.subscribeOnView(stateChange: (STATE) -> Unit) {
        this.observe(viewLifecycleOwner, stateChange)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    override fun onResultProvided(request: Any, result: Any): Boolean {
        return false
    }

    protected fun setResult(request: Any, data: Any) {
        communicationViewModel.onResultProvided(request, data)
    }
}