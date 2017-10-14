package de.knerd.applicationmanager.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.ApplicationRecyclerViewAdapter
import de.knerd.applicationmanager.listener.OnApplicationListFragmentInteractionListener
import de.knerd.applicationmanager.models.ApplicationModel
import de.knerd.applicationmanager.models.getConnection

open class ApplicationFragment : Fragment() {

    var source = ApplicationSource.Main
    var objectId: Int = -1

    private var mListenerApplication: OnApplicationListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_application_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            val applicationDao = DaoManager.createDao(getConnection(context), ApplicationModel::class.java)
            view.adapter = when (source) {
                ApplicationSource.Main -> ApplicationRecyclerViewAdapter(applicationDao.queryForAll(), mListenerApplication, context)
                ApplicationSource.Agent -> ApplicationRecyclerViewAdapter(applicationDao.filter { item -> item.agent!!.id == objectId }, mListenerApplication, context)
                ApplicationSource.Agency -> ApplicationRecyclerViewAdapter(applicationDao.filter { item -> item.agent!!.agency!!.id == objectId }, mListenerApplication, context)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnApplicationListFragmentInteractionListener) {
            mListenerApplication = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnApplicationListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListenerApplication = null
    }

    companion object {
        fun newInstance(source: ApplicationSource, id: Int): ApplicationFragment {
            val fragment = ApplicationFragment()
            fragment.source = source
            fragment.objectId = id
            return fragment
        }

        fun newInstance(): ApplicationFragment {
            return ApplicationFragment()
        }
    }
}