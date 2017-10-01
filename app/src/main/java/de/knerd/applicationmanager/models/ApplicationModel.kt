package de.knerd.applicationmanager.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import de.knerd.applicationmanager.BR
import java.text.SimpleDateFormat
import java.util.*

@DatabaseTable(tableName = "application")
class ApplicationModel : BaseObservable() {
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    @get:Bindable
    var agent: AgentModel? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.agent)
        }
    @DatabaseField(canBeNull = false)
    @get:Bindable
    var companyName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.companyName)
        }
    @DatabaseField(generatedId = true)
    var id: Int = 0
    @DatabaseField(canBeNull = false)
    @get:Bindable
    var applicationDate: Date? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.applicationDate)
            notifyPropertyChanged(BR.applicationDateFormatted)
        }
    @DatabaseField(canBeNull = false)
    @get:Bindable
    var state: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.state)
        }
    @get:Bindable
    val applicationDateFormatted: String
        get() {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
            return if (applicationDate != null) {
                formatter.format(applicationDate)
            } else {
                ""
            }
        }
}