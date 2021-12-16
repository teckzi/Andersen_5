package tck.example.andersen_5

import android.app.Application

class ContactApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        ContactRepository.initialize(this)
    }
}