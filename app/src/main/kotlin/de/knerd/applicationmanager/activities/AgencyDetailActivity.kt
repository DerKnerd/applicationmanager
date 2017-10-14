package de.knerd.applicationmanager.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.MainActivity
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.SectionsPagerAdapter
import de.knerd.applicationmanager.databinding.ActivityAgencyDetailBinding
import de.knerd.applicationmanager.fragments.AgentFragment
import de.knerd.applicationmanager.fragments.ApplicationFragment
import de.knerd.applicationmanager.fragments.ApplicationSource
import de.knerd.applicationmanager.listener.OnAgentListFragmentInteractionListener
import de.knerd.applicationmanager.listener.OnApplicationListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.ApplicationModel
import de.knerd.applicationmanager.models.getConnection

class AgencyDetailActivity : AppCompatActivity(), OnAgentListFragmentInteractionListener, OnApplicationListFragmentInteractionListener {

    lateinit var binding: ActivityAgencyDetailBinding
    private var selectedAgent: AgentModel? = null

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

    override fun onAgentListFragmentInteraction(item: AgentModel) {
        selectedAgent = item
        askForContactDetailsPermission()
    }

    override fun onApplicationListFragmentInteraction(item: ApplicationModel) {
        val intent = Intent(this, ApplicationDetailActivity::class.java)
        intent.putExtra(ApplicationDetailActivity.ARG_ITEM_ID, item.id)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.any()) {
            navigateToAgent(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onBackPressed() {
        navigateBack()
    }

    private fun setupBinding() {
        val agencyDao = DaoManager.createDao(getConnection(this), AgencyModel::class.java)
        val agency = agencyDao.findLast { item -> item.id == intent.getIntExtra(ARG_ITEM_ID, -1) }

        binding = DataBindingUtil.setContentView<ActivityAgencyDetailBinding>(this, R.layout.activity_agency_detail)
        binding.agency = agency
    }

    private fun setupTabs() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mSectionsPagerAdapter!!.addFragment(AgentFragment.newInstance(binding.agency.id), getString(R.string.agent_fragment_title), AddAgentActivity::class.java)
        mSectionsPagerAdapter!!.addFragment(ApplicationFragment.newInstance(ApplicationSource.Agency, binding.agency.id), getString(R.string.application_fragment_title), AddApplicationActivity::class.java)

        mViewPager = findViewById(R.id.container) as ViewPager

        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        tabLayout.addOnTabSelectedListener(TabChangedListener(mSectionsPagerAdapter, findViewById(R.id.fab) as FloatingActionButton, this))
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun navigateBack() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun onFabClick(view: View) {
        val intent = Intent(this, mSectionsPagerAdapter!!.getAddActivity(mViewPager!!.currentItem))
        intent.putExtra(AddAgentActivity.AGENCY_ID, binding.agency.id)
        startActivity(intent)
    }

    private fun askForContactDetailsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        } else {
            navigateToAgent(true)
        }
    }

    private fun navigateToAgent(allowed: Boolean) {
        val intent = Intent(this, AgentDetailActivity::class.java)
        intent.putExtra(AgentDetailActivity.ARG_ITEM_ID, selectedAgent!!.id)
        intent.putExtra(AgentDetailActivity.ARG_CONTACT_DETAILS, allowed)
        startActivity(intent)
    }

    companion object {
        val ARG_ITEM_ID = "item_id"
    }
}
