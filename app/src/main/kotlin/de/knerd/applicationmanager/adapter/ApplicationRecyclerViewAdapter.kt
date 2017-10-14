package de.knerd.applicationmanager.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.knerd.applicationmanager.R
import de.knerd.applicationmanager.adapter.viewholder.ApplicationBindingViewHolder
import de.knerd.applicationmanager.databinding.FragmentApplicationBinding
import de.knerd.applicationmanager.listener.OnApplicationListFragmentInteractionListener
import de.knerd.applicationmanager.models.ApplicationModel

/**
 * [RecyclerView.Adapter] that can display a [ApplicationModel] and makes a call to the
 * specified [OnApplicationListFragmentInteractionListener].
 */
class ApplicationRecyclerViewAdapter(
        private val mValues: List<ApplicationModel>,
        private val mListenerApplication: OnApplicationListFragmentInteractionListener?,
        private val context: Context
) : RecyclerView.Adapter<ApplicationBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationBindingViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<FragmentApplicationBinding>(inflater, R.layout.fragment_application, parent, false)
        return ApplicationBindingViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ApplicationBindingViewHolder, position: Int) {
        val binding = holder.binding
        binding.item = mValues[position]
        binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            mListenerApplication!!.onApplicationListFragmentInteraction(holder.binding.item)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}