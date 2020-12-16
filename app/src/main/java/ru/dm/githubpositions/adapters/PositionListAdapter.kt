package ru.dm.githubpositions.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_position_list_footer.view.*
import kotlinx.android.synthetic.main.layout_position_list_item.view.*
import ru.dm.githubpositions.R
import ru.dm.githubpositions.data.models.Position
import ru.dm.githubpositions.data.models.State

class PositionListAdapter(private val retry: () -> Unit) : PagedListAdapter<Position, RecyclerView.ViewHolder>(PositionDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        val PositionDiffCallback = object : DiffUtil.ItemCallback<Position>() {
            override fun areItemsTheSame(oldItem: Position, newItem: Position): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Position, newItem: Position): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) PositionViewHolder.create(parent) else ListFooterViewHolder.create(retry, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as PositionViewHolder).bind(getItem(position))
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}

class PositionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): PositionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_position_list_item, parent, false)
            return PositionViewHolder(view)
        }
    }

    fun bind(position: Position?) {
        position?.let {
            itemView.textTitle.text = position.title
            itemView.textCompany.text = position.company
            itemView.textHowToApply.text = position.howToApply
        }
    }
}

class ListFooterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_position_list_footer, parent, false)
            view.textError.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }

    fun bind(state: State?) {
        state?.let {
            itemView.textError.visibility = if (state == State.ERROR) VISIBLE else INVISIBLE
            itemView.progressBar.visibility = if (state == State.LOADING) VISIBLE else INVISIBLE
        }
    }
}
