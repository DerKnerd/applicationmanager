package de.knerd.applicationmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import de.knerd.applicationmanager.activities.*
import de.knerd.applicationmanager.adapter.SectionsPagerAdapter
import de.knerd.applicationmanager.fragments.AgencyFragment
import de.knerd.applicationmanager.fragments.AgentFragment
import de.knerd.applicationmanager.fragments.ApplicationFragment
import de.knerd.applicationmanager.listener.OnAgencyListFragmentInteractionListener
import de.knerd.applicationmanager.listener.OnAgentListFragmentInteractionListener
import de.knerd.applicationmanager.listener.OnApplicationListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.ApplicationModel
import de.knerd.applicationmanager.models.setupDatabase

class MainActivity : AppCompatActivity(), OnAgencyListFragmentInteractionListener, OnAgentListFragmentInteractionListener, OnApplicationListFragmentInteractionListener {

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
        setContentView(R.layout.activity_main)

        setupTabs()
        setupToolbar()
        setupDatabase(this)
    }

    private var selectedAgent: AgentModel? = null

    override fun onAgentListFragmentInteraction(item: AgentModel) {
        selectedAgent = item
        askForContactDetailsPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.any()) {
            navigateToAgent(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
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

    override fun onAgencyListFragmentInteraction(item: AgencyModel) {
        val intent = Intent(this, AgencyDetailActivity::class.java)
        intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, item.id)
        startActivity(intent)
    }

    override fun onApplicationListFragmentInteraction(item: ApplicationModel) {
        val intent = Intent(this, ApplicationDetailActivity::class.java)
        intent.putExtra(ApplicationDetailActivity.ARG_ITEM_ID, item.id)
        startActivity(intent)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun setupTabs() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mSectionsPagerAdapter!!.addFragment(AgencyFragment.newInstance(), getString(R.string.agency_fragment_title), AddAgencyActivity::class.java)
        mSectionsPagerAdapter!!.addFragment(AgentFragment.newInstance(-1), getString(R.string.agent_fragment_title), AddAgentActivity::class.java)
        mSectionsPagerAdapter!!.addFragment(ApplicationFragment.newInstance(), getString(R.string.application_fragment_title), AddApplicationActivity::class.java)

        mViewPager = findViewById(R.id.container) as ViewPager

        mViewPager!!.adapter = mSectionsPagerAdapter


        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        tabLayout.addOnTabSelectedListener(TabChangedListener(mSectionsPagerAdapter, findViewById(R.id.fab) as FloatingActionButton, this))
    }

    fun addEntry(view: View) {
        val intent = Intent(this, mSectionsPagerAdapter!!.getAddActivity(mViewPager!!.currentItem))
        startActivity(intent)
    }
}
