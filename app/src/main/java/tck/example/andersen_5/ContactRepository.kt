package tck.example.andersen_5

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.contactsDatabase.ContactDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "contact-database"
class ContactRepository private constructor(context: Context) {


    private val database: ContactDatabase = Room.databaseBuilder(
        context.applicationContext,
        ContactDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val contactDao = database.contactDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getContacts() : LiveData<List<Contact>> = contactDao.getContacts()

    fun getContact(id: UUID): LiveData<Contact?> = contactDao.getContact(id)

    fun updateContact(contact: Contact){
        executor.execute{
            contactDao.updateContact(contact)
        }
    }

    fun addContact(contact: Contact){
        executor.execute {
            contactDao.addContact(contact)
        }
    }

    fun deleteContact(id: UUID){
        executor.execute {
            contactDao.deleteContact(id)
        }
    }

    companion object {
        private var INSTANCE: ContactRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = ContactRepository(context)
        }

        fun get(): ContactRepository {
            return INSTANCE ?: throw IllegalStateException("ContactRepository must be initialized")
        }
    }
}