package io.bewsys.spmobile.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import io.bewsys.spmobile.R
import org.koin.core.KoinApplication.Companion.init

class EditTextQuestion @JvmOverloads
constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(ctx, attributeSet, defStyleAttr) {
    private var questionView: TextView
    private var answerView: TextInputLayout
    private var typedArray: TypedArray? = null

    var answer: String
        set(value) {
            answerView.editText?.setText(value)
        }
            get() = answerView.editText?.text.toString()


    init {
        LayoutInflater.from(ctx).inflate(R.layout.edit_text_question,this,true)
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
    }

//    fun addTextChangedListener(action: (Editable?) -> Unit) {
//        answerView.editText?.addTextChangedListener {
//            action.invoke(it)
//        }
//    }


}