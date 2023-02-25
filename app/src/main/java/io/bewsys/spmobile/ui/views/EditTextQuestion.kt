package io.bewsys.spmobile.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import app.cash.sqldelight.db.QueryResult.Unit.value
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import io.bewsys.spmobile.R
import org.koin.core.KoinApplication.Companion.init

class EditTextQuestion @JvmOverloads
constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CustomQuestionViews(ctx, attributeSet, defStyleAttr) {
    private var questionView: TextView
    private var answerView: TextInputLayout
    private var typedArray: TypedArray? = null

    override var answer: String
        get() = answerView.editText?.text.toString()
        set(value) {
            answerView.editText?.setText(value)
        }

    override var title: String = ""
        get() = questionView.text.toString()


    init {
        LayoutInflater.from(ctx).inflate(R.layout.edit_text_question, this, true)
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
                typedArray?.getInt(
                    R.styleable.EditTextQuestion_android_inputType,
                    InputType.TYPE_CLASS_TEXT
                ) as Int
//            answerView.editText?.setText("")

        }

    }

    override fun addTextChangedListener(action: (String?) -> Unit) {
        answerView.editText?.addTextChangedListener {
            action.invoke(it.toString())
        }
    }
}