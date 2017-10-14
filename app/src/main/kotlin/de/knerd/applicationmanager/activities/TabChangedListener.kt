package de.knerd.applicationmanager.activities

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.SectionsPagerAdapter

class TabChangedListener(private val mSectionsPagerAdapter: SectionsPagerAdapter?, private val fab: FloatingActionButton, private val context: Context) : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        fab.setImageDrawable(ContextCompat.getDrawable(context, when {
            mSectionsPagerAdapter!!.getAddActivity(tab!!.position) == AddAgencyActivity::class.java -> R.drawable.add_agency
            mSectionsPagerAdapter.getAddActivity(tab.position) == AddAgentActivity::class.java -> R.drawable.add_user
            mSectionsPagerAdapter.getAddActivity(tab.position) == AddApplicationActivity::class.java -> R.drawable.add_application
            else -> R.drawable.add
        }))
    }
}