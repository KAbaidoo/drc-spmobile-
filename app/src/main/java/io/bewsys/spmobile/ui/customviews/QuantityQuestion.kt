package io.bewsys.spmobile.ui.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.core.widget.addTextChangedListener
import io.bewsys.spmobile.R

class QuantityQuestion @JvmOverloads
constructor(
    private val ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CustomQuestionViews(ctx, attributeSet, defStyleAttr) {
    private var questionView: TextView
    private var autoCompleteTextView: AutoCompleteTextView
    private var typedArray: TypedArray? = null
    private var _answer: String? = ""

    override var title: String = ""
        get() = questionView.text.toString()

    override var answer: String = ""
        get() = _answer.toString()


    init {

        LayoutInflater.from(ctx).inflate(R.layout.quantity_question, this, true)
        orientation = VERTICAL
        gravity = Gravity.CENTER

        questionView = findViewById(R.id.text_field_question)
        autoCompleteTextView = findViewById(R.id.autocomplete_text_view)

        if (attributeSet != null) {
            typedArray = ctx.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.QuantityQuestion,
                defStyleAttr,
                0
            )
            try {
                questionView.text =
                    typedArray?.getString(R.styleable.QuantityQuestion_android_text).toString()

                val entries =
                    typedArray?.getTextArray(R.styleable.QuantityQuestion_android_entries)

                if (entries != null) {
                    setUpDropDown(entries)
                }
            } finally {
                typedArray!!.recycle()
            }

        }
    }

    private fun setUpDropDown(entries: Array<CharSequence>?) {

        autoCompleteTextView.apply {
            setAdapter(
                ArrayAdapter(ctx, R.layout.dropdown_item, entries as Array<out CharSequence>).also {
                    addTextChangedListener {
                        _answer = it.toString()
                    }
                }
            )
        }

    }
    override fun addTextChangedListener(action: (String?) -> Unit) {
        autoCompleteTextView.addTextChangedListener {
            action.invoke(it.toString())
        }
    }

}


