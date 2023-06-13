package gri.riverjach.theendormap

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Class de test pour écouter tous les changement et de les stocker
 */
class TestObserver<T> : Observer<T> {
    val observeValues = mutableListOf<T?>()

    override fun onChanged(value: T) {
        observeValues.add(value)
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().apply {
    observeForever(this)
}