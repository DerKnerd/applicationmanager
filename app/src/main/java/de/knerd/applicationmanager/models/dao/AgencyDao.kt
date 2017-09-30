package de.knerd.applicationmanager.models.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import de.knerd.applicationmanager.models.AgencyModel

class AgencyDao(connectionSource: ConnectionSource?) : BaseDaoImpl<AgencyModel, Int>(connectionSource, AgencyModel::class.java), Dao<AgencyModel, Int> {
}