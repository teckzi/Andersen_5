package tck.example.andersen_5.contactsDatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import tck.example.andersen_5.classes.Contact
import java.util.*

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun getContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE id=(:id)")
    fun getContact(id: UUID): LiveData<Contact?>

    @Update
    fun updateContact(contact: Contact)

    @Insert
    fun addContact(contact: Contact)

    @Query("DELETE FROM contact WHERE id=(:id)")
    fun deleteContact(id:UUID)
}