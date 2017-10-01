package de.knerd.applicationmanager.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.FragmentAgentDetailsBinding
import de.knerd.applicationmanager.models.AgentModel

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AgentDetailsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AgentDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgentDetailsFragment : Fragment() {
    lateinit var binding: FragmentAgentDetailsBinding

    lateinit var agent: AgentModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_agent_details, container, false)
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(activity, R.layout.fragment_agent_details)
        binding.agent = agent
    }

    companion object {
        fun newInstance(agent: AgentModel): AgentDetailsFragment {
            val fragment = AgentDetailsFragment()
            fragment.agent = agent
            return fragment
        }
    }
}
