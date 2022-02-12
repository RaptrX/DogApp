package com.appclr8.dogrecycler.base

import android.text.InputType
import androidx.navigation.NavDirections
import com.appclr8.dogrecycler.R
import com.appclr8.dogrecycler.constants.AppConstants.CODE_UNSET
import java.lang.ref.WeakReference

sealed class BaseStateUI {
    //NAVIGATION STATES (Ref https://medium.com/google-developer-experts/using-navigation-architecture-component-in-a-large-banking-app-ac84936a42c2)
    data class To(val directions: NavDirections): BaseStateUI()
    object Back: BaseStateUI()
    object CloseKeyboard: BaseStateUI()
    data class BackTo(val destinationId: Int): BaseStateUI()
    object ToRoot: BaseStateUI()
    data class ToastMessage(val messageResId: Int): BaseStateUI()

    data class CustomDialog(var refId: Int = CODE_UNSET, val listener: WeakReference<BaseFragment.DialogClickListener>, val positiveButtonResId: Int, val negativeButtonResId: Int?, val messageId: Int, val titleId: Int, val imageId : Int) : BaseStateUI()
    data class SuccessDialog(var refId: Int = CODE_UNSET, val listener : WeakReference<BaseFragment.DialogClickListener>, val positiveButtonResId: Int = R.string.dialog_text_ok, val negativeButtonResId: Int?=null, val messageTextId: Int): BaseStateUI()
    data class InfoDialog(var refId: Int = CODE_UNSET, val listener : WeakReference<BaseFragment.DialogClickListener>, val positiveButtonResId: Int = R.string.dialog_text_ok, val negativeButtonResId: Int?=null, val messageTextId: Int, val cancelable: Boolean): BaseStateUI()
    data class RadioDialog(var refId : Int = CODE_UNSET, val listener: WeakReference<BaseFragment.DialogListItemListener>, val list : List<String>, val selection : Int): BaseStateUI()
    data class InputDialog(val listener: WeakReference<BaseFragment.DialogTextGetListener>, val hint: Int, val inputType: Int = InputType.TYPE_CLASS_TEXT, val title: Int, val refID: Int = CODE_UNSET, val positiveButtonResId: Int) : BaseStateUI()
    data class DatePickerDialog(val listener: WeakReference<BaseFragment.DateSetListener>, val requireFuture: Boolean, val type: Int) : BaseStateUI()

    data class ProgressDialogShow(val titleId: Int, val messageId: Int, val stringExtra: String="", val cancelable: Boolean = false) : BaseStateUI()
    data class ProgressDialogHide(val stringError: String="") : BaseStateUI()
    data class ProgressDialogUpdate(val messageId: Int, val stringExtra: String="") : BaseStateUI()
//TODO OTHER UI STATES
}