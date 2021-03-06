package de.knerd.applicationmanager.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.databinding.FragmentAgencyBinding
import de.knerd.applicationmanager.listener.OnAgencyListFragmentInteractionListener
import de.knerd.applicationmanager.models.AgencyModel
import de.knerd.applicationmanager.adapter.viewholder.AgencyBindingViewHolder

/**
 * [RecyclerView.Adapter] that can display a [AgencyModel] and makes a call to the
 * specified [OnAgencyListFragmentInteractionListener].
 */
class AgencyRecyclerViewAdapter(
        private val mValues: List<AgencyModel>,
        private val mListenerAgency: OnAgencyListFragmentInteractionListener?,
        private val context: Context
) : RecyclerView.Adapter<AgencyBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgencyBindingViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<FragmentAgencyBinding>(inflater, R.layout.fragment_agency, parent, false)
        return AgencyBindingViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: AgencyBindingViewHolder, position: Int) {
        val binding = holder.binding
        binding.item = mValues[position]
        binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            mListenerAgency!!.onAgencyListFragmentInteraction(holder.binding.item)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}
