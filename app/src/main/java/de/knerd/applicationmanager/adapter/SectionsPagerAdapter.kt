package de.knerd.applicationmanager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

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

    fun <T> addFragment(fragment: Fragment, title: String, addActivityClass: Class<T>) {
        sections += fragment
        sectionTitles += title
        addActivities += addActivityClass
    }

    fun <T> getFragment(fragment: Class<T>): T? where T : Fragment {
        return sections.find { f -> f.javaClass == fragment } as T
    }
}