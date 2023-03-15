package io.bewsys.spmobile.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

open class CustomQuestionViews @JvmOverloads
constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(ctx, attributeSet, defStyleAttr) {
//    private var questionView: TextView
//    private var answerView: TextInputLayout
//    private var typedArray: TypedArray? = null

    open var title: String = ""
//        get() = questionView.text.toString()

    open var answer: String = ""
//        set(value) {
//            answerView.editText?.setText(value)
//        }
//        get() = answerView.editText?.text.toString()


//    init {
//        LayoutInflater.from(ctx).inflate(R.layout.date_picker_question, this, true)
//        orientation = LinearLayout.VERTICAL
//        gravity = Gravity.CENTER
//
//        questionView = findViewById(R.id.text_field_question)
//        answerView = findViewById(R.id.edit_text_answer)
//
//
//
//
//        if (attributeSet != null) {
//            typedArray = ctx.theme.obtainStyledAttributes(
//                attributeSet,
//                R.styleable.EditTextQuestion,
//                defStyleAttr,
//                0
//            )
//            questionView.text =
//                typedArray?.getString(R.styleable.EditTextQuestion_android_text).toString()
//            answerView.editText?.isEnabled =
//                typedArray?.getBoolean(R.styleable.EditTextQuestion_android_enabled, true) ?: true
//            answerView.editText?.inputType =
//                typedArray?.getType(R.styleable.EditTextQuestion_android_inputType) as Int
//            answerView.editText?.setText("")
//        }
//    }

    open fun addTextChangedListener(action: (String?) -> Unit){

    }

}