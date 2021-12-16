package tck.example.andersen_5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.ContactRepository
import java.util.*

class ContactViewModel:ViewModel() {
    private val contactRepository = ContactRepository.get()
    private val contactIdLiveData = MutableLiveData<UUID>()

    var contactLiveData: LiveData<Contact?> = Transformations.switchMap(contactIdLiveData) { contactId ->
        contactRepository.getContact(contactId)
    }
    fun addContact (contact: Contact){
        contactRepository.addContact(contact)
    }
    fun loadContact(contactId: UUID){
        contactIdLiveData.value = contactId
    }
    fun updateContact(contact:Contact){
        contactRepository.updateContact(contact)
    }
    fun deleteContact(contactId: UUID){
        contactRepository.deleteContact(contactId)
    }

}