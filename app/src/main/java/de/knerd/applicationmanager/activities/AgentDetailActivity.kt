package de.knerd.applicationmanager.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.ActivityAgentDetailBinding
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.getConnection

/**
 * An activity representing a single Agency detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [AgencyDetailActivity].
 */
class AgentDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityAgentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
    }

    private fun setupBinding() {
        val agentDao = DaoManager.createDao(getConnection(this), AgentModel::class.java)
        val agent = agentDao.findLast { item -> item.id == intent.getIntExtra(ARG_ITEM_ID, -1) }

        binding = DataBindingUtil.setContentView<ActivityAgentDetailBinding>(this, R.layout.activity_agent_detail)
        binding.agent = agent
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.detail_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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

    private fun navigateBack() {
        val intent = Intent(this, AgencyDetailActivity::class.java)
        intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, binding.agent.agency!!.id)
        startActivity(intent)
    }

    fun onAddApplicationClick(view: View?) {

    }

    companion object {
        val ARG_ITEM_ID = "item_id"
    }
}