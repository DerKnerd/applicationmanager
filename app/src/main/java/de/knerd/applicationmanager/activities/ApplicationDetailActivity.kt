package de.knerd.applicationmanager.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.ActivityApplicationDetailBinding
import de.knerd.applicationmanager.models.ApplicationModel
import de.knerd.applicationmanager.models.getConnection

class ApplicationDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityApplicationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_application_detail, menu)
        return true
    }

    override fun onBackPressed() {
        navigateBack()
    }

    private fun navigateBack() {
        val intent = Intent(this, AgentDetailActivity::class.java)
        intent.putExtra(AgentDetailActivity.ARG_ITEM_ID, binding.application.agent!!.id)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuItemId = item.itemId
        return if (menuItemId == android.R.id.home) {
            navigateBack()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun editState(view: View?) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
                .setTitle(R.string.edit_state)
                .setSingleChoiceItems(R.array.application_states, resources.getStringArray(R.array.application_states).indexOfFirst { s -> s == binding.application.state }, { _, i ->
                    binding.application.state = resources.getStringArray(R.array.application_states)[i]
                })
                .setPositiveButton(R.string.save, { _, i ->
                    val applicationDao = DaoManager.createDao(getConnection(this), ApplicationModel::class.java)
                    applicationDao.update(binding.application)
                    dialog!!.dismiss()
                })
                .setNegativeButton(android.R.string.cancel, { _, i ->
                    dialog!!.cancel()
                })
        dialog = builder.create()
        builder.show()
    }

    private fun setupBinding() {
        val applicationDao = DaoManager.createDao(getConnection(this), ApplicationModel::class.java)
        val application = applicationDao.findLast { item -> item.id == intent.getIntExtra(ApplicationDetailActivity.ARG_ITEM_ID, -1) }

        binding = DataBindingUtil.setContentView<ActivityApplicationDetailBinding>(this, R.layout.activity_application_detail)
        binding.application = application
    }

    companion object {
        val ARG_ITEM_ID = "item_id"
    }
}
