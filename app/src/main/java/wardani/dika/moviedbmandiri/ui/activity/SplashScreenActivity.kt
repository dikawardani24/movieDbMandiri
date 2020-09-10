package wardani.dika.moviedbmandiri.ui.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ActivitySplashScreenBinding
import wardani.dika.moviedbmandiri.util.startActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var handler: Handler? = null
    private val loadingRunnable = LoadingRunnable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        loadingRunnable.updateProgress()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler?.removeCallbacks(loadingRunnable)
    }

    private class LoadingRunnable(private val activity: SplashScreenActivity) : Runnable {

        fun updateProgress() {
            @Suppress("DEPRECATION") val handler = Handler()
            handler.postDelayed(activity.loadingRunnable, 300)
            activity.handler = handler
        }

        override fun run() {
            val progressBar = activity.binding.progressHorizontal
            var currentProgress: Int = progressBar.progress
            currentProgress += 10
            progressBar.progress = currentProgress
            if (currentProgress > 100) {
                activity.startActivity(MainActivity::class)
                activity.finish()
            } else {
                updateProgress()
            }
        }
    }
}