package de.knerd.applicationmanager.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.MainActivity
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.SectionsPagerAdapter
import de.knerd.applicationmanager.databinding.ActivityAgencyDetailBinding
import de.knerd.applicationmanager.fragments.AgentFragment
import de.knerd.applicationmanager.listener.OnAgentListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.getConnection

class AgencyDetailActivity : AppCompatActivity(), OnAgentListFragmentInteractionListener {

    lateinit var binding: ActivityAgencyDetailBinding

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupTabs()
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        val agencyDao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)
        val agency = agencyDao.findLast { item -> item.id == intent.getIntExtra(ARG_ITEM_ID, -1) }

        binding = DataBindingUtil.setContentView<ActivityAgencyDetailBinding>(this, R.layout.activity_agency_detail)
        binding.agency = agency
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_agency_detail, menu)
        return true
    }

    override fun onBackPressed() {
        navigateBack()
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
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun onAddAgentClick(view: View) {
        val intent = Intent(this, AddAgentActivity::class.java)
        intent.putExtra(AddAgentActivity.AGENCY_ID, binding.agency.id)
        startActivity(intent)
    }

    private fun setupTabs() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mSectionsPagerAdapter!!.addFragment(AgentFragment(binding.agency.id), getString(R.string.agent_fragment_title), AddAgentActivity::class.java)

        mViewPager = findViewById(R.id.container) as ViewPager

        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
    }

    override fun onAgentListFragmentInteraction(item: AgentModel) {
        val intent = Intent(this, AgentDetailActivity::class.java)
        intent.putExtra(AgentDetailActivity.ARG_ITEM_ID, item.id)
        startActivity(intent)
    }

    companion object {
        val ARG_ITEM_ID = "item_id"
    }
}
