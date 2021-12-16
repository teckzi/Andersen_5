package tck.example.andersen_5.contactsDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tck.example.andersen_5.classes.Contact


@Database(entities = [Contact::class], version=1)
@TypeConverters(ContactTypeConverter::class)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao():ContactDao
}