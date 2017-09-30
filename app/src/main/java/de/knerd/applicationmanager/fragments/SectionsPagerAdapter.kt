package de.knerd.applicationmanager.fragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import de.knerd.applicationmanager.activities.AddAgencyActivity
import kotlin.reflect.KClass

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var sections: List<Fragment> = ArrayList<Fragment>()
    private var sectionTitles: List<String> = ArrayList<String>()
    private var addActivities: List<Class<out Any>> = ArrayList<Class<Any>>()

    override fun getItem(position: Int): Fragment {
        return sections[position]
    }

    override fun getCount(): Int {
        return sections.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return sectionTitles[position]
    }

    fun getAddActivity(position: Int): Class<out Any> {
        return addActivities[position]
    }

    fun addFragment(fragment: Fragment, title: String, addActivityClass: KClass<AddAgencyActivity>) {
        sections += fragment
        sectionTitles += title
        addActivities += addActivityClass.java
    }
}