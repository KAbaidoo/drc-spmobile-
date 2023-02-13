package io.bewsys.spmobile.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import io.bewsys.spmobile.R

class RadioButtonQuestion @JvmOverloads
constructor(
    private val ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(ctx, attributeSet, defStyleAttr) {
    var inflater: LayoutInflater

    init {
        // get the inflater service from the android system
        inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflate the layout into "this" component
        inflater.inflate(R.layout.radio_button_question, this)
    }

    fun setRadioButtons(radioButtonStrings: List<String>) {
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        for (index in radioButtonStrings.indices) {
            val r = RadioButton(ctx).apply {
                text = radioButtonStrings[index]
            }
            radioGroup.addView(r)
        }
    }

    fun setQuestion(question: String?){
        findViewById<TextView>(R.id.text_field_question).text =question ?: "Error: title unavailable!"
    }

    fun setQuestionNumber(setQuestionNumber: Int?) {
        findViewById<TextView>(R.id.text_view_question_number).text = setQuestionNumber.toString() ?: "0"
    }



}