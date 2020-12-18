package com.ly.baselibrary.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.ly.baselibrary.R
import kotlinx.android.synthetic.main.layout_new_order_dialog.*

/**
 * 接单
 */
class NewOrderDialog : Dialog, View.OnClickListener {
    public interface CloseListener{
        fun agreen();
    }
    var listener:CloseListener?=null
    constructor(context: Context?,listener:CloseListener) : super(context, R.style.LoadingDialog) {
        init()
        this.listener=listener
    }

    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        init()
    }

    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        init()
    }

    private fun init() {
        setContentView(R.layout.layout_new_order_dialog)
        cancelBtn.setOnClickListener(this)
        okBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancelBtn ->{
                dismiss()
            }
            R.id.okBtn-> {
                dismiss()
                if(listener!=null){
                    listener!!.agreen()
                }
            }
        }
    }
}