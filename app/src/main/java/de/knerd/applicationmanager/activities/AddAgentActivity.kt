package de.knerd.applicationmanager.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.MainActivity
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.ActivityAddAgentBinding
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.getConnection
import java.sql.SQLException
import java.util.*


class AddAgentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddAgentBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val agencyId = intent.getIntExtra(AGENCY_ID, -1)
        setupBinding(agencyId)
        setupGreeting()
        setupSpinner(agencyId)
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding(agencyId: Int) {
        val agencyDao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityAddAgentBinding>(this, R.layout.activity_add_agent)
        binding.agent = AgentModel()
        if (agencyId != -1) {
            binding.agent.agency = agencyDao.find { item -> item.id == agencyId }
        }
    }

    private fun setupSpinner(agencyId: Int) {
        val spinner = findViewById(R.id.agency) as Spinner
        if (agencyId == -1) {
            val agencyDao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, agencyDao.toList())
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.agent.agency = null
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    binding.agent.agency = adapter.getItem(position)
                }
            }
        } else {
            spinner.visibility = View.GONE
        }
    }

    private fun setupGreeting() {
        val greeting = findViewById(R.id.greeting) as AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, resources.getStringArray(R.array.greetings))
        greeting.setAdapter(adapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuItemId = item.itemId
        return when {
            menuItemId == android.R.id.home -> {
                navigateBack()
                true
            }
            save() -> {
                val intent = Intent(this, AgentDetailActivity::class.java)
                intent.putExtra(AgentDetailActivity.ARG_ITEM_ID, binding.agent.id)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        navigateBack()
    }

    private fun navigateBack() {
        if (intent.getIntExtra(AGENCY_ID, -1) == -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, AgencyDetailActivity::class.java)
            intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, binding.agent.agency!!.id)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_agent, menu)
        return true
    }

    fun onPickApplicationDate(view: View?) {
        DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    binding.agent.lastContact = calendar.time
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun save(): Boolean {
        return try {
            val dao = DaoManager.createDao(getConnection(this), AgentModel::class.java)
            dao.create(binding.agent)
            dao.refresh(binding.agent)
            Toast.makeText(this, getString(R.string.agent_saved, binding.agent.name), Toast.LENGTH_SHORT).show()
            true
        } catch (ex: SQLException) {
            Toast.makeText(this, getString(R.string.error_saving_agent), Toast.LENGTH_LONG).show()
            Log.e("agent", ex.message, ex)
            false
        }
    }

    companion object {
        val AGENCY_ID = "agency_id"
    }
}
