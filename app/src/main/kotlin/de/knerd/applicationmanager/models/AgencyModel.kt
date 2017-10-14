package de.knerd.applicationmanager.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable
import de.knerd.applicationmanager.BR

@DatabaseTable(tableName = "agency")
class AgencyModel : BaseObservable() {
    @DatabaseField(unique = true, canBeNull = false)
    @get:Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @DatabaseField(generatedId = true)
    @get:Bindable
    var id: Int = 0

    @ForeignCollectionField(eager = false)
    @get:Bindable
    var agents: ForeignCollection<AgentModel>? = null

    override fun toString(): String {
        return if (name != null) {
            name!!
        } else {
            ""
        }
    }
}