package tck.example.andersen_5.contactsDatabase

import androidx.room.TypeConverter
import java.util.*

class ContactTypeConverter {

    @TypeConverter
    fun fromUUID(uuid: UUID?):String?{
        return uuid?.toString()
    }
    @TypeConverter
    fun toUUID(uuid:String?): UUID?{
        return UUID.fromString(uuid)
    }
}