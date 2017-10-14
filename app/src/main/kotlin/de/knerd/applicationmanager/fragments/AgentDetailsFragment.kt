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

    lateinit var phoneNumber: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_agent_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)
    }

    private fun setupBinding(view: View?) {
        binding = DataBindingUtil.bind<FragmentAgentDetailsBinding>(view)
        binding.agent = agent
        binding.phoneNumber = phoneNumber
        binding.phoneNumberVisibility = when (phoneNumber) {
            "" -> View.GONE
            else -> View.VISIBLE
        }
    }

    companion object {
        fun newInstance(agent: AgentModel, phoneNumber: String): AgentDetailsFragment {
            val fragment = AgentDetailsFragment()
            fragment.agent = agent
            fragment.phoneNumber = phoneNumber
            return fragment
        }
    }
}
