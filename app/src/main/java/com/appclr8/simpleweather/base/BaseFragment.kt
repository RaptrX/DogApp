package com.appclr8.simpleweather.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.appclr8.simpleweather.App.Companion.appComponent
import com.appclr8.simpleweather.R
import com.appclr8.simpleweather.di.AppComponent
import com.appclr8.simpleweather.di.ViewModelFactory
import com.appclr8.simpleweather.extensions.hideKeyboard
import com.appclr8.simpleweather.utils.ProgressBarUtil
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


abstract class BaseFragment<VM : BaseViewModel> : Fragment(), ViewModelProvider.Factory {
    abstract val viewModelClass: Class<VM>
    val viewModel: VM by lazy { provideViewModel() }

    @Inject
    lateinit var mProvider: Provider<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(appComponent)
    }

    abstract fun inject(appComponent: AppComponent)

    open fun provideViewModel(): VM {
        return ViewModelProvider(
            this,
            ViewModelFactory(mProvider)
        ).get(viewModelClass)
    }

    /**
     * Function performs
     * 1. Connect the VM emitting SingleLiveEvent states to the Observer actioning the emitted events on the Fragment view
     * 2. Calls VM onViewCreated() fun allowing VM to initiate any required onLoad functionality
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveDataEvent.observe(
            viewLifecycleOwner,
            baseStateUIObserver
        )
        viewModel.load()
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Override ViewModelProvider create function to facilitate VM injection
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return mProvider.get() as T
    }

    /**
     * Live data observer to action BaseStateUI state requests
     */
    private val baseStateUIObserver = Observer<BaseStateUI> { state ->
        state?.let {
            when (state) {
                BaseStateUI.Back -> {
                    Timber.d("state Back")
                    findNavController().navigateUp()
                }
                is BaseStateUI.To -> {
                    Timber.d("state To: ${state.directions}")
                    findNavController().navigate(state.directions)
                }
                is BaseStateUI.ErrorDialog -> {
                    when (state.errorState) {
                        BaseStateUI.ErrorStates.NETWORK_ERROR -> {
                            if (state.negativeButtonResId != null)
                                MaterialDialog(this.context!!)
                                    .cancelOnTouchOutside(false)
                                    .title(R.string.text_network_error_title)
                                    .message(R.string.text_network_error_message)
                                    .show {
                                        icon(R.drawable.ic_network_error)
                                        positiveButton(state.positiveButtonResId) {
                                            state.listener.get()?.onPosClicked(state.refId)
                                        }
                                        negativeButton(state.negativeButtonResId) {
                                            state.listener.get()?.onNegClicked(state.refId)
                                        }
                                    }
                            else
                                MaterialDialog(this.context!!)
                                    .cancelOnTouchOutside(false)
                                    .title(R.string.text_network_error_title)
                                    .message(R.string.text_network_error_message)
                                    .show {
                                        icon(R.drawable.ic_network_error)
                                        positiveButton(state.positiveButtonResId) {
                                            state.listener.get()?.onPosClicked(state.refId)
                                        }
                                    }
                        }
                    }
                }

                is BaseStateUI.SuccessDialog -> {
                    if (state.negativeButtonResId != null)
                        MaterialDialog(this.context!!)
                            .cancelOnTouchOutside(false)
                            .message(state.messageTextId)
                            .show {
                                icon(R.drawable.ic_check_green)
                                positiveButton(state.positiveButtonResId) {
                                    state.listener.get()?.onPosClicked(state.refId)
                                }
                                negativeButton(state.negativeButtonResId) {
                                    state.listener.get()?.onNegClicked(state.refId)
                                }
                            }
                    else
                        MaterialDialog(this.context!!)
                            .cancelOnTouchOutside(false)
                            .message(state.messageTextId)
                            .show {
                                icon(R.drawable.ic_check_green)
                                positiveButton(state.positiveButtonResId) {
                                    state.listener.get()?.onPosClicked(state.refId)
                                }
                            }
                }

                is BaseStateUI.RadioDialog -> {
                    MaterialDialog(this.requireContext())
                        .title(R.string.pick_location)
                        .cancelOnTouchOutside(false)
                        .show {
                            listItemsSingleChoice(
                                items = state.list,
                                initialSelection = state.selection,
                                waitForPositiveButton = false
                            ) { _, index, _ ->
                                state.listener.get()?.onPosBtnClicked(index, state.refId)
                            }
                                .positiveButton(R.string.blank)
                        }
                }

                is BaseStateUI.InfoDialog -> {
                    if (state.negativeButtonResId != null)
                        MaterialDialog(this.context!!)
                            .cancelOnTouchOutside(false)
                            .message(state.messageTextId)
                            .show {
                                icon(R.drawable.ic_info_blue)
                                positiveButton(state.positiveButtonResId) {
                                    state.listener.get()?.onPosClicked(state.refId)
                                }
                                negativeButton(state.negativeButtonResId) {
                                    state.listener.get()?.onNegClicked(state.refId)
                                }
                            }
                    else
                        MaterialDialog(this.context!!)
                            .cancelOnTouchOutside(false)
                            .message(state.messageTextId)
                            .show {
                                icon(R.drawable.ic_info_blue)
                                positiveButton(state.positiveButtonResId) {
                                    state.listener.get()?.onPosClicked(state.refId)
                                }
                            }
                }

                is BaseStateUI.ProgressDialogShow -> {
                    Timber.d("stateObserver state fragment -> DialogProgressBarShowState")
                    ProgressBarUtil.showProgressDialog(
                        this@BaseFragment.requireContext(),
                        resources.getString(state.titleId),
                        resources.getString(state.messageId) + state.stringExtra,
                        state.cancelable
                    )
                }

                is BaseStateUI.ProgressDialogHide -> {
                    Timber.d("stateObserver state fragment -> DialogProgressBarHideState")
                    ProgressBarUtil.dismissProgressDialog()
                }

                is BaseStateUI.ProgressDialogUpdate -> {
                    Timber.d("stateObserver state -> DialogProgressBarUpdateMessageState")
                    ProgressBarUtil.updateText(resources.getString(state.messageId) + state.stringExtra)
                }


                is BaseStateUI.ToastMessage -> {
                    Toast.makeText(this.context!!, state.messageResId, Toast.LENGTH_SHORT).show()
                }

                is BaseStateUI.CloseKeyboard -> {
                    Timber.d("stateObserver state -> CloseKeyboard")
                    hideKeyboard()
                }
                else -> {}
            }
        }
    }

    interface DialogListItemListener {
        fun onPosBtnClicked(selection: Int, refId: Int)
    }

    interface DialogClickListener {
        fun onPosClicked(refId: Int)
        fun onNegClicked(refId: Int)
    }
}