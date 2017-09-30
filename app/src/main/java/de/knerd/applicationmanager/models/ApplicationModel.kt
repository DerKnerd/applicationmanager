package de.knerd.applicationmanager.models

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.misc.BaseDaoEnabled
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "application")
class ApplicationModel : BaseDaoEnabled<BaseDaoImpl<ApplicationModel, Int>, Int>() {
    @DatabaseField(foreign = true, canBeNull = false)
    val agent: AgentModel? = null
    @DatabaseField(canBeNull = false)
    val companyName: String? = null
    @DatabaseField(generatedId = true)
    val id: Int = 0
}