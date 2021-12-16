package tck.example.andersen_5.viewModel

import androidx.lifecycle.ViewModel
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.ContactRepository

class ContactListViewModel: ViewModel() {

    private val contactRepository = ContactRepository.get()
    val contactListLiveData = contactRepository.getContacts()

    fun addContact (contact: Contact){
        contactRepository.addContact(contact)
    }
}