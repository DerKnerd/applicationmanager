package de.knerd.applicationmanager.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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

    lateinit var email: String
    lateinit var phoneNumber: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_agent_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE), 1)
        }
    }

    private fun setupBinding(view: View?) {
        binding = DataBindingUtil.bind<FragmentAgentDetailsBinding>(view)
        binding.agent = agent
        binding.phoneNumber = phoneNumber
        binding.email = email
    }

    companion object {
        fun newInstance(agent: AgentModel, phoneNumber: String, email: String): AgentDetailsFragment {
            val fragment = AgentDetailsFragment()
            fragment.agent = agent
            fragment.phoneNumber = phoneNumber
            fragment.email = email
            return fragment
        }
    }
}
