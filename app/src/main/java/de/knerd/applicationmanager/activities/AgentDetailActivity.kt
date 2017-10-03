package de.knerd.applicationmanager.activities

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
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
import java.util.*


/**
 * An activity representing a single Agency detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [AgencyDetailActivity].
 */
class AgentDetailActivity : AppCompatActivity(), OnApplicationListFragmentInteractionListener, ActivityCompat.OnRequestPermissionsResultCallback {

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
        setupBinding()
        setupToolbar()
        setupPermissions()
        setupTabs()
        super.onCreate(savedInstanceState)
    }

    private fun setupPermissions() {
        phoneEnabled = if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            false
        } else {
            setupContacts()
            true
        }
    }

    private var phoneEnabled: Boolean = false

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when {
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> setupContacts()
            grantResults[1] == PackageManager.PERMISSION_GRANTED -> phoneEnabled = true
            else -> phoneEnabled = false
        }
        setupTabs()
    }

    private fun setupContacts() {
        val numberCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", arrayOf(getAgent()!!.name), null)
        numberCursor.use {
            if (it != null) {
                while (it.moveToNext()) {
                    phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                }
            }
        }

        val emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", arrayOf(getAgent()!!.name), null)
        emailCursor.use {
            if (it != null) {
                while (it.moveToNext()) {
                    email = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                }
            }
        }
    }

    lateinit var phoneNumber: String
    lateinit var email: String

    private fun setupBinding() {
        val agent = getAgent()

        binding = DataBindingUtil.setContentView<ActivityAgentDetailBinding>(this, R.layout.activity_agent_detail)
        binding.agent = agent
    }

    private fun getAgent(): AgentModel? {
        val agentDao = DaoManager.createDao(getConnection(this), AgentModel::class.java)
        return agentDao.findLast { item -> item.id == intent.getIntExtra(ARG_ITEM_ID, -1) }
    }

    fun callAgent(view: View?) {
        val intent = if (!phoneEnabled) {
            Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
        } else {
            Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null))
        }
        startActivity(intent)

    }

    private fun setupTabs() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mSectionsPagerAdapter!!.addFragment(AgentDetailsFragment.newInstance(binding.agent, phoneNumber, email), getString(R.string.agent_detail_fragment_title), AddApplicationActivity::class.java)
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
        return when (menuItemId) {
            android.R.id.home -> {
                navigateBack()
                true
            }
            R.id.action_edit_last_contact -> {
                changeLastContact()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeLastContact() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    binding.agent.lastContact = calendar.time
                    val agentDao = DaoManager.createDao(getConnection(this), AgentModel::class.java)
                    agentDao.update(binding.agent)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onApplicationListFragmentInteraction(item: ApplicationModel) {
        val intent = Intent(this, ApplicationDetailActivity::class.java)
        intent.putExtra(ApplicationDetailActivity.ARG_ITEM_ID, item.id)
        startActivity(intent)
    }

    override fun onBackPressed() {
        navigateBack()
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