package konjetic.sofanbaapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import konjetic.sofanbaapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backArrow.setOnClickListener {
            this.finish()
        }
    }
}
