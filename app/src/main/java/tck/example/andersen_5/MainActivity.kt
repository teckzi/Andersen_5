package tck.example.andersen_5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import tck.example.andersen_5.fragments.ContactFragment
import tck.example.andersen_5.fragments.ContactListFragment
import java.util.*

class MainActivity : AppCompatActivity(),ContactListFragment.Callbacks,ContactFragment.Callback {
    private val listFragment = ContactListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,listFragment).commit()
        }
    }

    override fun onContactSelected(contactId: UUID) {
        val fragment = ContactFragment.newInstance(contactId)
        val container = findViewById<View>(R.id.fragment_container_detail)
        if (container != null) supportFragmentManager.beginTransaction().replace(R.id.fragment_container_detail,fragment).commit()
        else supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }

    override fun onContactReplaced() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,listFragment).addToBackStack(null)
            .commit()
        val fragmentTwo = ContactFragment()
        val container = findViewById<View>(R.id.fragment_container_detail)
        if (container != null) supportFragmentManager.beginTransaction().remove(fragmentTwo).commit()
    }
}