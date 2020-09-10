package wardani.dika.moviedbmandiri.util

import android.app.Activity
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.bottomsheet.BottomSheetDialog
import wardani.dika.moviedbmandiri.R
import kotlin.reflect.KClass


fun Activity.startActivity(kClass: KClass<*>, block: Intent.() -> Unit = {}) {
    val intent = Intent(this, kClass.java)
    block(intent)
    startActivity(intent)
}

fun Activity.showWarning(message: String?) {
    val bottomSheetDialog = BottomSheetDialog(this)
    bottomSheetDialog.setContentView(R.layout.error_dialog)
    bottomSheetDialog.findViewById<TextView>(R.id.messageError)?.text = message
    bottomSheetDialog.show()
}

fun Activity.updateAndroidSecurityProvider() {
    try {
        ProviderInstaller.installIfNeeded(this)
    } catch (e: GooglePlayServicesRepairableException) {
        showWarning("${e.message}")
    } catch (e: GooglePlayServicesNotAvailableException) {
        showWarning("Google Play Services not available.")
    }
}