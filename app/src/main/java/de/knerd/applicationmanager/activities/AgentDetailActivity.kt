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
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.SectionsPagerAdapter
import de.knerd.applicationmanager.databinding.ActivityAgentDetailBinding
import de.knerd.applicationmanager.fragments.AgentDetailsFragment
import de.knerd.applicationmanager.fragments.ApplicationFragment
import de.knerd.applicationmanager.fragments.ApplicationSource
import de.knerd.applicationmanager.listener.OnApplicationListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.ApplicationModel
import de.knerd.applicationmanager.models.getConnection

/**
 * An activity representing a single Agency detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [AgencyDetailActivity].
 */
class AgentDetailActivity : AppCompatActivity(), OnApplicationListFragmentInteractionListener {

    lateinit var binding: ActivityAgentDetailBinding

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
        setupToolbar()
        setupTabs()
    }

    private fun setupBinding() {
        val agentDao = DaoManager.createDao(getConnection(this), AgentModel::class.java)
        val agent = agentDao.findLast { item -> item.id == intent.getIntExtra(ARG_ITEM_ID, -1) }

        binding = DataBindingUtil.setContentView<ActivityAgentDetailBinding>(this, R.layout.activity_agent_detail)
        binding.agent = agent
    }

    private fun setupTabs() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mSectionsPagerAdapter!!.addFragment(AgentDetailsFragment.newInstance(binding.agent), getString(R.string.agent_detail_fragment_title), AddApplicationActivity::class.java)
        mSectionsPagerAdapter!!.addFragment(ApplicationFragment.newInstance(ApplicationSource.Agent, binding.agent.id), getString(R.string.application_fragment_title), AddApplicationActivity::class.java)

        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_agent_detail, menu)
        return true
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

    override fun onApplicationListFragmentInteraction(item: ApplicationModel) {
//        val intent = Intent(this, ApplicationDetailActivity::class.java)
//        intent.putExtra(ApplicationDetailActivity.ARG_ITEM_ID, item.id)
//        startActivity(intent)
    }

    private fun navigateBack() {
        val intent = Intent(this, AgencyDetailActivity::class.java)
        intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, binding.agent.agency!!.id)
        startActivity(intent)
    }


    fun onFabClick(view: View) {
        val intent = Intent(this, mSectionsPagerAdapter!!.getAddActivity(mViewPager!!.currentItem))
        intent.putExtra(AddAgentActivity.AGENCY_ID, binding.agent.id)
        startActivity(intent)
    }

    companion object {
        val ARG_ITEM_ID = "item_id"
    }
}