package com.abhiket.transactions.ui.view

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.abhiket.transactions.R
import com.abhiket.transactions.databinding.DialogLoadingBinding

class LoadingDialog @JvmOverloads constructor(
    context: Context,
    @StyleRes themeResId: Int = 0,
    private val isCancelable: Boolean = true,
    @StringRes private val loadingText: Int = 0
) : Dialog(context, themeResId),
    LifecycleOwner,
    OnBackPressedDispatcherOwner,
    SavedStateRegistryOwner {

    private var _lifecycleRegistry: LifecycleRegistry? = null
    private val lifecycleRegistry: LifecycleRegistry
        get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
            _lifecycleRegistry = it
        }

    private val savedStateRegistryController: SavedStateRegistryController =
        SavedStateRegistryController.create(this)
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override fun onSaveInstanceState(): Bundle {
        val bundle = super.onSaveInstanceState()
        savedStateRegistryController.performSave(bundle)
        return bundle
    }

    @Suppress("ClassVerificationFailure") // needed for onBackInvokedDispatcher call
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) {
            onBackPressedDispatcher.setOnBackInvokedDispatcher(onBackInvokedDispatcher)
        }
        savedStateRegistryController.performRestore(savedInstanceState)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        setCancelable(isCancelable)
        bindDialogView()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @CallSuper
    override fun onStop() {
        // This is the closest thing to onDestroy that a Dialog has
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        _lifecycleRegistry = null
        super.onStop()
    }

    @Suppress("DEPRECATION")
    override val onBackPressedDispatcher = OnBackPressedDispatcher {
        super.onBackPressed()
    }

    @CallSuper
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun setContentView(layoutResID: Int) {
        initializeViewTreeOwners()
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        initializeViewTreeOwners()
        super.setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        initializeViewTreeOwners()
        super.setContentView(view, params)
    }

    override fun addContentView(view: View, params: ViewGroup.LayoutParams?) {
        initializeViewTreeOwners()
        super.addContentView(view, params)
    }

    /**
     * Sets the view tree owners before setting the content view so that the
     * inflation process and attach listeners will see them already present.
     */
    @CallSuper
    private fun initializeViewTreeOwners() {
        val dialogWindow = window
        if (dialogWindow != null) {
            dialogWindow.decorView.setViewTreeLifecycleOwner(this)
            dialogWindow.decorView.setViewTreeOnBackPressedDispatcherOwner(this)
            dialogWindow.decorView.setViewTreeSavedStateRegistryOwner(this)
            dialogWindow.setBackgroundDrawableResource(R.drawable.base_dialog_bg)
        } else {
            dismiss()
        }
    }

    private fun bindDialogView() {
        val dialogBinding = DialogLoadingBinding.inflate(layoutInflater)
        if (loadingText != Resources.ID_NULL) {
            dialogBinding.loadingText.setText(loadingText)
        }
        setContentView(dialogBinding.root)
    }

}
