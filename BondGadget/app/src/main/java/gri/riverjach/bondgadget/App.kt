package gri.riverjach.bondgadget

import android.app.Application
import gri.riverjach.bondgadget.repository.Repo
import timber.log.Timber

class App : Application() {

    companion object {
        val repo = Repo()
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}