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
import de.knerd.applicationmanager.MainActivity
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.ActivityAddAgencyBinding
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.getConnection
import java.sql.SQLException

class AddAgencyActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddAgencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityAddAgencyBinding>(this, R.layout.activity_add_agency)
        binding.agency = AgencyModel()

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
            val intent = Intent(this, MainActivity::class.java)
            navigateUpTo(intent)
            true
        } else if (save()) {
            val intent = Intent(this, AgencyDetailActivity::class.java)
            intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, binding.agency.id)
            navigateUpTo(intent)
            true
        } else {
            false
        }
    }

    private fun save(): Boolean {
        try {
            val dao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)
            dao.create(binding.agency)
            Toast.makeText(this, getString(R.string.agency_saved, binding.agency.name), Toast.LENGTH_SHORT).show()
            return true
        } catch (ex: SQLException) {
            Toast.makeText(this, getString(R.string.error_saving_agency), Toast.LENGTH_LONG).show()
            Log.e("agency", ex.message, ex)
            return false
        }
    }
}
