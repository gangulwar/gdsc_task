package gangulwar.gdsc

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider
import gangulwar.gdsc.databinding.ActivityMainBinding
import android.widget.TextView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var songSlider: Slider
    private lateinit var volumeSlider: Slider
    private lateinit var songStartTimeTextView: TextView
    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = window
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            val color = ContextCompat.getColor(this, R.color.dark_mode_background)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = color
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        updateAppTheme()

        binding.switchMode.setOnClickListener {
            toggleDarkMode()
        }

        songSlider = findViewById<Slider>(R.id.songSlider)
        volumeSlider = findViewById(R.id.volumeSlider)

        songStartTimeTextView = findViewById(R.id.song_start_time)

        songSlider.setValue(40F)
        volumeSlider.setValue(60F)

        songSlider.addOnChangeListener { slider, value, fromUser ->
            val minutes = value.toInt() / 60
            val seconds = value.toInt() % 60
            songStartTimeTextView.text =
                String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
        }

        songSlider.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue))
        volumeSlider.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue))

    }

    private fun toggleDarkMode() {
        isDarkMode = !isDarkMode

        val newMode =
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        if (newMode != AppCompatDelegate.getDefaultNightMode()) {
            sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply()
            AppCompatDelegate.setDefaultNightMode(newMode)

            recreate()
        }
    }

    private fun updateAppTheme() {

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}