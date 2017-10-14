package de.knerd.applicationmanager.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.FragmentAgentBinding
import de.knerd.applicationmanager.listener.OnAgentListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgentModel
import de.knerd.applicationmanager.adapter.viewholder.AgentBindingViewHolder

class AgentRecyclerViewAdapter(
        private val mValues: List<AgentModel>,
        private val mListenerAgent: OnAgentListFragmentInteractionListener?,
        private val context: Context
) : RecyclerView.Adapter<AgentBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentBindingViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<FragmentAgentBinding>(inflater, R.layout.fragment_agent, parent, false)
        return AgentBindingViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: AgentBindingViewHolder, position: Int) {
        val binding = holder.binding
        binding.item = mValues[position]
        binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            mListenerAgent!!.onAgentListFragmentInteraction(holder.binding.item)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}
