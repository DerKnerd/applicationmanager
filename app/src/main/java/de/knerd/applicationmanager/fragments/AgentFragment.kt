package de.knerd.applicationmanager.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.j256.ormlite.dao.DaoManager
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.AgentRecyclerViewAdapter
import de.knerd.applicationmanager.listener.OnAgentListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.models.getConnection

@SuppressLint("ValidFragment")
class AgentFragment(private val agencyId: Int) : BaseFragment(R.layout.fragment_agent) {

    private var mListenerAgent: OnAgentListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_agent_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            val agencyDao = DaoManager.createDao(getConnection(context), AgencyModel::class.java)
            val agentDao = DaoManager.createDao(getConnection(context), AgentModel::class.java)
            val agents: List<AgentModel>
            agents = if (agencyId == -1) {
                agentDao.toList()
            } else {
                agencyDao.find { item -> item.id == agencyId }!!.agents!!.toList()
            }
            view.adapter = AgentRecyclerViewAdapter(agents, mListenerAgent, context)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAgentListFragmentInteractionListener) {
            mListenerAgent = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnAgentListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListenerAgent = null
    }
}