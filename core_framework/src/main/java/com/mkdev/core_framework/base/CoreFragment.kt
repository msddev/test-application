package com.mkdev.core_framework.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.mkdev.core_framework.common.arch.StatefulViewModel
import com.mkdev.core_framework.utils.FragmentInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class CoreFragment() : Fragment(), FragmentInterface {

    private val job = SupervisorJob()
    val viewScope = CoroutineScope(Dispatchers.Main + job)
    open fun onViewInitialized(view: View) {}

    @Deprecated(
        "Pleae use subscriveOnView, since this one subscrivbes the ViewModel on fragment itself which is a bad practice",
        ReplaceWith("subscribeOnView")
    )
    override fun <STATE : Any> StatefulViewModel<STATE>.subscribe(stateChange: (STATE) -> Unit) {
        this.observe(this@CoreFragment, stateChange)
    }

    override fun <STATE : Any> StatefulViewModel<STATE>.subscribeOnView(stateChange: (STATE) -> Unit) {
        this.observe(viewLifecycleOwner, stateChange)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val isOnBackPressedHandled = onBackPressed()
            isEnabled = isOnBackPressedHandled
            if (!isOnBackPressedHandled) {
                requireActivity().onBackPressed()
            }
        }

    }

    open fun pressBackOnActivity() {
        activity?.onBackPressed()
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
}