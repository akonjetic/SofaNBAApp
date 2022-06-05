package konjetic.sofanbaapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.AboutTextViewBinding

class AboutTextView(context: Context, attr: AttributeSet) : FrameLayout(context, attr) {

    private val binding: AboutTextViewBinding

    init {
        binding = AboutTextViewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.AboutTextView, 0, 0
        ).apply {
            try {
                getString(R.styleable.AboutTextView_titleText)?.let {
                    binding.title.text = it
                }
                getString(R.styleable.AboutTextView_valueText)?.let {
                    binding.text.text = it
                }
            } finally {
                recycle()
            }
        }
    }
}
