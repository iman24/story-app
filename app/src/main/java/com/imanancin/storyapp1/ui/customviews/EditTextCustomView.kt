package com.imanancin.storyapp1.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.imanancin.storyapp1.R

class EditTextCustomView : TextInputEditText, TextWatcher {
    var isValid: Boolean = false

    constructor(context: Context) : super(context) {
        addTextChangedListener(this)
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        addTextChangedListener(this)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        addTextChangedListener(this)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

    }

    override fun afterTextChanged(editable: Editable?) {

        if (text != null && text?.isNotEmpty() == true) {
            when(inputType) {
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT-> {
                    if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches() && text!!.isNotEmpty()) {
                        error = resources.getString(R.string.email_not_valid)
                        isValid = false
                    } else {
                        isValid = true
                    }
                }
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT-> {
                    if (text!!.toString().length < 6 && text?.isNotEmpty()!!) {
                        error = resources.getString(R.string.weak_password)
                        isValid = false
                    } else {
                        isValid = true
                    }
                }
            }
        }else{
            error = resources.getString(R.string.form_required)
            isValid = false
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}


}