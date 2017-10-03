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
import de.knerd.applicationmanager.databinding.ActivityAddApplicationBinding
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.ApplicationModel
import de.knerd.applicationmanager.models.getConnection
import java.sql.SQLException
import java.util.*

class AddApplicationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddApplicationBinding

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val agencyId = intent.getIntExtra(AddApplicationActivity.AGENCY_ID, -1)
        val agentId = intent.getIntExtra(AddApplicationActivity.AGENT_ID, -1)
        setupBinding(agentId)
        setupState()
        setupSpinner(agencyId, agentId)
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_application, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuItemId = item.itemId
        return when {
            menuItemId == android.R.id.home -> {
                navigateBack()
                true
            }
            save() -> {
                val intent = Intent(this, ApplicationDetailActivity::class.java)
                intent.putExtra(ApplicationDetailActivity.ARG_ITEM_ID, binding.application.id)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        navigateBack()
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding(agentId: Int) {
        val agentDao = DaoManager.createDao(getConnection(this), AgentModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityAddApplicationBinding>(this, R.layout.activity_add_application)
        binding.application = ApplicationModel()
        if (agentId != -1) {
            binding.application.agent = agentDao.find { item -> item.id == agentId }
        }
    }

    private fun setupSpinner(agencyId: Int, agentId: Int) {
        val spinner = findViewById(R.id.agent) as Spinner
        spinner.apply {
            when {
                agentId != -1 -> visibility = View.GONE
                agencyId != -1 -> {
                    val agencyDao = DaoManager.createDao(getConnection(this.context), AgencyModel::class.java)
                    val agencyAdapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, agencyDao.find { item -> item.id == agencyId }!!.agents!!.toList())
                    adapter = agencyAdapter
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            binding.application.agent = null
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            binding.application.agent = agencyAdapter.getItem(position)
                        }
                    }
                }
                else -> {
                    val agentDao = DaoManager.createDao(getConnection(this.context), AgentModel::class.java)
                    val agentAdapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, agentDao.toList())
                    adapter = agentAdapter
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            binding.application.agent = null
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            binding.application.agent = agentAdapter.getItem(position)
                        }
                    }
                }
            }
        }
    }

    private fun setupState() {
        val state = findViewById(R.id.state) as AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, resources.getStringArray(R.array.application_states))
        state.setAdapter(adapter)
    }

    private fun navigateBack() {
        val agencyId = intent.getIntExtra(AddApplicationActivity.AGENCY_ID, -1)
        val agentId = intent.getIntExtra(AddApplicationActivity.AGENT_ID, -1)
        val intent = when {
            agentId != -1 -> {
                val intent = Intent(this, AgentDetailActivity::class.java)
                intent.putExtra(AgentDetailActivity.ARG_ITEM_ID, agentId)
                intent
            }
            agencyId != -1 -> {
                val intent = Intent(this, AgencyDetailActivity::class.java)
                intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, agencyId)
                intent
            }
            else -> Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
    }

    private fun save(): Boolean {
        return try {
            val dao = DaoManager.createDao(getConnection(this), ApplicationModel::class.java)
            dao.create(binding.application)
            dao.refresh(binding.application)
            Toast.makeText(this, getString(R.string.application_saved, binding.application.companyName), Toast.LENGTH_SHORT).show()
            true
        } catch (ex: SQLException) {
            Toast.makeText(this, getString(R.string.error_saving_application), Toast.LENGTH_LONG).show()
            Log.e("application", ex.message, ex)
            false
        }
    }

    fun onPickApplicationDate(view: View?) {
        val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    binding.application.applicationDate = calendar.time
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    companion object {
        val AGENT_ID = "agent_id"
        val AGENCY_ID = "agency_id"
    }
}
