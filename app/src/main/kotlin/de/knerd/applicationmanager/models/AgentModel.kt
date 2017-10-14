package de.knerd.applicationmanager.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable
import de.knerd.applicationmanager.BR
import java.text.SimpleDateFormat
import java.util.*

@DatabaseTable(tableName = "contact_person")
class AgentModel : BaseObservable() {
    @DatabaseField(canBeNull = false)
    @get:Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @DatabaseField(generatedId = true)
    var id: Int = 0

    @DatabaseField(canBeNull = false)
    @get:Bindable
    var lastContact: Date? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastContact)
            notifyPropertyChanged(BR.lastContactFormatted)
        }

    @DatabaseField(canBeNull = false)
    @get:Bindable
    var greeting: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.greeting)
        }

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    var agency: AgencyModel? = null

    @ForeignCollectionField(eager = false)
    var applications: ForeignCollection<ApplicationModel>? = null

    @get:Bindable
    val lastContactFormatted: String
        get() {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
            return if (lastContact != null) {
                formatter.format(lastContact)
            } else {
                ""
            }
        }

    override fun toString(): String {
        return if (name != null) {
            name!!
        } else {
            ""
        }
    }
}
