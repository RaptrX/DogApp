package com.appclr8.dogrecycler.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.appclr8.dogrecycler.App.Companion.appComponent
import com.appclr8.dogrecycler.R
import com.appclr8.dogrecycler.di.AppComponent
import com.appclr8.dogrecycler.di.ViewModelFactory
import com.appclr8.dogrecycler.extensions.hideKeyboard
import com.appclr8.dogrecycler.utils.ProgressBarUtil
import timber.log.Timber
import java.util.*
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
                is BaseStateUI.CustomDialog -> {
                    showCustomDialog(oneButton = state.negativeButtonResId == null, state = state)
                }

                is BaseStateUI.SuccessDialog -> {
                    showSuccessDialog(oneButton = state.negativeButtonResId == null, state = state)
                }

                is BaseStateUI.RadioDialog -> {
                    showRadioDialog(state = state)
                }

                is BaseStateUI.InfoDialog -> {
                    showInfoDialog(oneButton = state.negativeButtonResId == null, state = state)
                }

                is BaseStateUI.InputDialog -> {
                    showInputDialog(state = state)
                }

                is BaseStateUI.DatePickerDialog -> {
                    showDatePickerDialog(state = state)
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

    @SuppressLint("CheckResult")
    private fun showInputDialog(state: BaseStateUI.InputDialog) {
        MaterialDialog(this.requireContext())
            .title(res = state.title)
            .cancelOnTouchOutside(cancelable = false)
            .show {
                input(
                    allowEmpty = false,
                    waitForPositiveButton = true,
                    hintRes = state.hint,
                    inputType = state.inputType
                ) { dialog, text ->
                    state.listener.get()?.onTextSubmitted(dialog = dialog, text = text.toString(), refId = state.refID)
                }
                positiveButton(res = state.positiveButtonResId)
            }
    }

    private fun showCustomDialog(oneButton: Boolean, state: BaseStateUI.CustomDialog) {
        if (oneButton) {
            MaterialDialog(this.context!!)
                .cancelOnTouchOutside(cancelable = false)
                .title(res = state.titleId)
                .message(res = state.messageId)
                .show {
                    icon(res = state.imageId)
                    positiveButton(res = state.positiveButtonResId) {
                        state.listener.get()?.onPosClicked(refId = state.refId)
                    }
                }
        } else {
            MaterialDialog(this.context!!)
                .cancelOnTouchOutside(cancelable = false)
                .title(res = state.titleId)
                .message(res = state.messageId)
                .show {
                    icon(res = state.imageId)
                    positiveButton(state.positiveButtonResId) {
                        state.listener.get()?.onPosClicked(refId = state.refId)
                    }
                    negativeButton(state.negativeButtonResId) {
                        state.listener.get()?.onNegClicked(refId = state.refId)
                    }
                }
        }
    }

    private fun showSuccessDialog(oneButton: Boolean, state: BaseStateUI.SuccessDialog) {
        if (oneButton) {
            MaterialDialog(this.context!!)
                .cancelOnTouchOutside(cancelable = false)
                .message(res = state.messageTextId)
                .show {
                    icon(res = R.drawable.ic_check_green)
                    positiveButton(res = state.positiveButtonResId) {
                        state.listener.get()?.onPosClicked(refId = state.refId)
                    }
                }
        } else {
            MaterialDialog(this.context!!)
                .cancelOnTouchOutside(cancelable = false)
                .message(res = state.messageTextId)
                .show {
                    icon(res = R.drawable.ic_check_green)
                    positiveButton(res = state.positiveButtonResId) {
                        state.listener.get()?.onPosClicked(refId = state.refId)
                    }
                    negativeButton(state.negativeButtonResId) {
                        state.listener.get()?.onNegClicked(refId = state.refId)
                    }
                }
        }
    }

    private fun showRadioDialog(state: BaseStateUI.RadioDialog) {
        MaterialDialog(this.requireContext())
            .title(res = state.title)
            .cancelOnTouchOutside(cancelable = false)
            .show {
                listItemsSingleChoice(
                    items = state.list,
                    initialSelection = state.selection,
                    waitForPositiveButton = false
                ) { _, index, _ ->
                    state.listener.get()?.onPosBtnClicked(selection = index, refId = state.refId)
                }
                    .positiveButton(res = R.string.blank)
            }
    }

    private fun showInfoDialog(oneButton: Boolean, state: BaseStateUI.InfoDialog) {
        if (oneButton) {
            MaterialDialog(this.context!!)
                .cancelOnTouchOutside(cancelable = state.cancelable)
                .message(text = state.messageText)
                .show {
                    icon(res = R.drawable.ic_info_blue)
                    positiveButton(res = state.positiveButtonResId) {
                        state.listener.get()?.onPosClicked(refId = state.refId)
                    }
                }
        } else {
            MaterialDialog(this.context!!)
                .cancelOnTouchOutside(cancelable = state.cancelable)
                .message(text = state.messageText)
                .show {
                    icon(res = R.drawable.ic_info_blue)
                    positiveButton(res = state.positiveButtonResId) {
                        state.listener.get()?.onPosClicked(refId = state.refId)
                    }
                    negativeButton(res = state.negativeButtonResId) {
                        state.listener.get()?.onNegClicked(refId = state.refId)
                    }
                }
        }
    }

    private fun showDatePickerDialog(state: BaseStateUI.DatePickerDialog) {
        MaterialDialog(this.requireContext())
            .cancelOnTouchOutside(false)
            .show {
                datePicker(requireFutureDate = state.requireFuture) { _, datetime ->
                    state.listener.get()?.onDateSet(dateTime = datetime, type = state.type)
                }
            }
    }

    /**INTERFACES FOR HANDLING DIALOGS*/

    interface DialogListItemListener {
        fun onPosBtnClicked(selection: Int, refId: Int)
    }

    interface DialogTextGetListener {
        fun onTextSubmitted(dialog: MaterialDialog, text: String, refId: Int)
    }

    interface DateSetListener {
        fun onDateSet(dateTime: Calendar, type: Int)
    }

    interface DialogClickListener {
        fun onPosClicked(refId: Int)
        fun onNegClicked(refId: Int)
    }
}