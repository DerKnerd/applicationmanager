package de.knerd.applicationmanager.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.listener.OnListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.getConnection
import de.knerd.applicationmanager.viewadapter.AgencyRecyclerViewAdapter

class AgencyFragment : BaseFragment(R.layout.fragment_agency) {

    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_agency_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            val agencyDao = DaoManager.createDao(getConnection(context), AgencyModel::class.java)
            view.adapter = AgencyRecyclerViewAdapter(agencyDao.queryForAll(), mListener, context)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}