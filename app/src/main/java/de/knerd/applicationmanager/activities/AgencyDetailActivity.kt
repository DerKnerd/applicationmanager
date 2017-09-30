package de.knerd.applicationmanager.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.MainActivity
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.ActivityAgencyDetailBinding
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.getConnection

/**
 * An activity representing a single Agency detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [AgencyListActivity].
 */
class AgencyDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityAgencyDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agency_detail)
        val toolbar = findViewById(R.id.detail_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val agencyDao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)
        val agency = agencyDao.findLast { item -> item.id == intent.getIntExtra(ARG_ITEM_ID, -1) }

        binding = DataBindingUtil.setContentView<ActivityAgencyDetailBinding>(this, R.layout.activity_agency_detail)
        binding.agency = agency
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    fun onAddAgentClick(view: View) {
        val intent = Intent(this, AddAgentActivity::class.java)
        intent.putExtra(AddAgentActivity.AGENCY_ID, binding.agency.id)
        startActivity(intent)
    }

    companion object {
        val ARG_ITEM_ID = "item_id"
    }
}
