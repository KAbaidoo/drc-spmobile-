package io.bewsys.spmobile.ui.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import io.bewsys.spmobile.R
import java.text.SimpleDateFormat
import java.util.*

class DatePickertQuestion @JvmOverloads
constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CustomQuestionViews(ctx, attributeSet, defStyleAttr) {
    private var questionView: TextView
    private var answerView: TextInputLayout
    private var typedArray: TypedArray? = null

    override var title: String = ""
        get() = questionView.text.toString()

    override var answer: String
        set(value) {
            answerView.editText?.setText(value)
        }
        get() = answerView.editText?.text.toString()


    init {
        LayoutInflater.from(ctx).inflate(R.layout.date_picker_question, this, true)
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER

        questionView = findViewById(R.id.text_field_question)
        answerView = findViewById(R.id.edit_text_answer)

        if (attributeSet != null) {
            typedArray = ctx.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.EditTextQuestion,
                defStyleAttr,
                0
            )
            questionView.text =
                typedArray?.getString(R.styleable.EditTextQuestion_android_text).toString()
            answerView.editText?.isEnabled =
                typedArray?.getBoolean(R.styleable.EditTextQuestion_android_enabled, true) ?: true
            answerView.editText?.inputType =
                typedArray?.getType(R.styleable.EditTextQuestion_android_inputType) as Int
            answerView.editText?.setText("")
        }

        answerView.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText("Select date of birth")
                .build()

            datePicker.show(findFragment<Fragment>().parentFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(it)
                answer = date
            }
        }


    }

    override fun addTextChangedListener(action: (String?) -> Unit) {
        answerView.editText?.addTextChangedListener {
            action.invoke(it.toString())
        }
    }
}