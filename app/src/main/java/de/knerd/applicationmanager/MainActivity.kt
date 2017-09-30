package de.knerd.applicationmanager

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import de.knerd.applicationmanager.activities.AddAgencyActivity
import de.knerd.applicationmanager.activities.AgencyDetailActivity
import de.knerd.applicationmanager.fragments.AgencyFragment
import de.knerd.applicationmanager.fragments.SectionsPagerAdapter
import de.knerd.applicationmanager.listener.OnListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.setupDatabase

class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {
    override fun onAgencyListFragmentInteraction(item: AgencyModel) {
        val intent = Intent(this, AgencyDetailActivity::class.java)
        intent.putExtra(AgencyDetailActivity.ARG_ITEM_ID, item.id)
        startActivity(intent)
    }

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

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Set up the ViewPager with the sections adapter.
        setupViewPager()

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        setupDatabase(this)
    }

    private fun setupViewPager() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mSectionsPagerAdapter!!.addFragment(AgencyFragment(), getString(R.string.agency_fragment_title), AddAgencyActivity::class)

        mViewPager = findViewById(R.id.container) as ViewPager

        mViewPager!!.adapter = mSectionsPagerAdapter
    }

    fun addEntry(view: View) {
        val intent = Intent(this, mSectionsPagerAdapter!!.getAddActivity(mViewPager!!.currentItem))
        startActivity(intent)
    }
}
