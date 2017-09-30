package de.knerd.applicationmanager.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.ActivityAddAgentBinding
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.getConnection
import java.sql.SQLException

class AddAgentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val agencyId = intent.getIntExtra(AGENCY_ID, -1)
        val agencyDao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityAddAgentBinding>(this, R.layout.activity_add_agent)
        binding.agent = AgentModel()
        binding.agent.agency = agencyDao.findLast { item -> item.id == agencyId }

        setContentView(R.layout.activity_add_agent)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            val intent = Intent(this, AgencyDetailActivity::class.java)
            intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, binding.agent.agency!!.id)
            navigateUpTo(intent)
            true
        } else if (save()) {
            val intent = Intent(this, AgentDetailActivity::class.java)
            intent.putExtra(AgentDetailActivity.ARG_ITEM_ID, binding.agent.id)
            startActivity(intent)
            true
        } else {
            false
        }
    }

    private fun save(): Boolean {
        try {
            val dao = DaoManager.createDao(getConnection(this), AgentModel::class.java)
            dao.create(binding.agent)
            dao.refresh(binding.agent)
            Toast.makeText(this, getString(R.string.agent_saved, binding.agent.name), Toast.LENGTH_SHORT).show()
            return true
        } catch (ex: SQLException) {
            Toast.makeText(this, getString(R.string.error_saving_agent), Toast.LENGTH_LONG).show()
            Log.e("agent", ex.message, ex)
            return false
        }
    }

    companion object {
        val AGENCY_ID = "agency_id"
    }
}
