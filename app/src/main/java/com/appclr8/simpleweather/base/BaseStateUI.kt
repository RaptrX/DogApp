package com.appclr8.simpleweather.base

import androidx.navigation.NavDirections
import com.appclr8.simpleweather.R
import com.appclr8.simpleweather.constants.AppConstants.CODE_UNSET
import com.appclr8.simpleweather.models.Place
import java.lang.ref.WeakReference

sealed class BaseStateUI {
    //NAVIGATION STATES (Ref https://medium.com/google-developer-experts/using-navigation-architecture-component-in-a-large-banking-app-ac84936a42c2)
    enum class ErrorStates {NETWORK_ERROR}
    data class To(val directions: NavDirections): BaseStateUI()
    object Back: BaseStateUI()
    object CloseKeyboard: BaseStateUI()
    data class BackTo(val destinationId: Int): BaseStateUI()
    object ToRoot: BaseStateUI()
    data class ToastMessage(val messageResId: Int): BaseStateUI()
    data class ErrorDialog(var refId: Int = CODE_UNSET, val errorState: ErrorStates, val listener : WeakReference<BaseFragment.DialogClickListener>, val positiveButtonResId: Int = R.string.dialog_text_ok, val negativeButtonResId: Int?=null): BaseStateUI()
    data class SuccessDialog(var refId: Int = CODE_UNSET, val listener : WeakReference<BaseFragment.DialogClickListener>, val positiveButtonResId: Int = R.string.dialog_text_ok, val negativeButtonResId: Int?=null, val messageTextId: Int): BaseStateUI()
    data class InfoDialog(var refId: Int = CODE_UNSET, val listener : WeakReference<BaseFragment.DialogClickListener>, val positiveButtonResId: Int = R.string.dialog_text_ok, val negativeButtonResId: Int?=null, val messageTextId: Int): BaseStateUI()
    data class RadioDialog(var refId : Int = CODE_UNSET, val listener: WeakReference<BaseFragment.DialogListItemListener>, val list : List<String>, val selection : Int): BaseStateUI()
    data class ProgressDialogShow(val titleId: Int, val messageId: Int, val stringExtra: String="", val cancelable: Boolean = false) : BaseStateUI()
    data class ProgressDialogHide(val stringError: String="") : BaseStateUI()
    data class ProgressDialogUpdate(val messageId: Int, val stringExtra: String="") : BaseStateUI()
//TODO OTHER UI STATES
}