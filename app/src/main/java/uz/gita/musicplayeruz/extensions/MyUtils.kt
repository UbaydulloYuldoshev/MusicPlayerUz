package uz.gita.musicplayeruz.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import timber.log.Timber

fun Fragment.showToast(message : String) {
    Toast.makeText(this.requireContext(),message,Toast.LENGTH_SHORT).show()
}

fun <T : ViewBinding> T.scope(block : T.() -> Unit) {
    block(this)
}


fun timber(message : String, tag : String = "TTT"){
    Timber.tag(tag).d(message)
}