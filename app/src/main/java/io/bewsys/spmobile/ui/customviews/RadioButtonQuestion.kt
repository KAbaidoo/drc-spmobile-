package io.bewsys.spmobile.ui.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RadioButton

import android.widget.RadioGroup
import android.widget.TextView

import io.bewsys.spmobile.R

class RadioButtonQuestion @JvmOverloads
constructor(
    private val ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CustomQuestionViews(ctx, attributeSet, defStyleAttr) {
    private var questionView: TextView
    private var radioGroup: RadioGroup
    private var typedArray: TypedArray? = null
    private var _answer: String? = ""

    override var answer: String = ""
        get() = _answer.toString()

    override var title: String = ""
        get() = questionView.text.toString()

    init {

        LayoutInflater.from(ctx).inflate(R.layout.radio_button_question, this, true)
        orientation = VERTICAL
        gravity = Gravity.CENTER

        questionView = findViewById(R.id.text_field_question)
        radioGroup = findViewById(R.id.radio_group)

        if (attributeSet != null) {
            typedArray = ctx.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.RadioButtonQuestion,
                defStyleAttr,
                0
            )
            try {
                questionView.text =
                    typedArray?.getString(R.styleable.RadioButtonQuestion_android_text).toString()

                val entries =
                    typedArray?.getTextArray(R.styleable.RadioButtonQuestion_android_entries)

                if (entries != null) {
                    setUpRadioButtons(entries)
                }
            } finally {
                typedArray!!.recycle()
            }

        }
    }



    private fun setUpRadioButtons(entries: Array<CharSequence>?) {

        entries?.let {
            for (index in it.indices) {
                val r = RadioButton(ctx).apply {

                    text = it[index]
                    textAlignment = TEXT_ALIGNMENT_INHERIT
                    setPadding(0, 0, 28, 0)
                }
                radioGroup.addView(r)
            }

        }
    }
    override fun addTextChangedListener(action: (String?) -> Unit) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            _answer = findViewById<RadioButton>(checkedId).text.toString()
            action.invoke((_answer))
        }
//        autoCompleteTextView.addTextChangedListener {
//            action.invoke(it)
//        }
    }

}


