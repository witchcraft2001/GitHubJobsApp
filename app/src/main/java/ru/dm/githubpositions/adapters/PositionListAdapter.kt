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
import kotlinx.android.synthetic.main.layout_position_list_item.view.textTitle
import kotlinx.android.synthetic.main.layout_position_list_section.view.*
import ru.dm.githubpositions.R
import ru.dm.githubpositions.adapters.listeners.AdapterOnClickListener
import ru.dm.githubpositions.data.models.Item
import ru.dm.githubpositions.data.models.PositionItem
import ru.dm.githubpositions.data.models.SectionItem
import ru.dm.githubpositions.data.models.State

class PositionListAdapter(
    private val retry: () -> Unit,
    private val clickListener: AdapterOnClickListener<PositionItem>?
    ) : PagedListAdapter<Item, RecyclerView.ViewHolder>(PositionDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val SECTION_VIEW_TYPE = 2
    private val FOOTER_VIEW_TYPE = 3

    private var state = State.LOADING

    companion object {
        val PositionDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return (oldItem is PositionItem && newItem is PositionItem && oldItem.id == newItem.id) ||
                        (oldItem is SectionItem && newItem is SectionItem && oldItem.createdAt == newItem.createdAt)
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return (oldItem is PositionItem && newItem is PositionItem && oldItem.equals(newItem)) ||
                        (oldItem is SectionItem && newItem is SectionItem && oldItem.equals(newItem))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            DATA_VIEW_TYPE -> PositionViewHolder.create(parent)
            SECTION_VIEW_TYPE -> SectionViewHolder.create(parent)
            else -> ListFooterViewHolder.create(retry, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            DATA_VIEW_TYPE -> (holder as PositionViewHolder).bind(getItem(position) as PositionItem, clickListener)
            SECTION_VIEW_TYPE -> (holder as SectionViewHolder).bind(getItem(position))
            else -> (holder as ListFooterViewHolder).bind(state)
        }
//        if (getItemViewType(position) == DATA_VIEW_TYPE)
//            (holder as PositionViewHolder).bind(getItem(position), clickListener)
//        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) {
            if (getItem(position) is PositionItem) {
                DATA_VIEW_TYPE
            } else {
                SECTION_VIEW_TYPE
            }
        } else FOOTER_VIEW_TYPE
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

    fun bind(position: PositionItem?, clickListener: AdapterOnClickListener<PositionItem>?) {
        position?.let {
            itemView.textTitle.text = position.title
            itemView.textCompany.text = position.company
            itemView.textHowToApply.text = position.howToApply
            itemView.setOnClickListener { clickListener?.onClickItem(position) }
        }
    }
}

class SectionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): SectionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_position_list_section, parent, false)
            return SectionViewHolder(view)
        }
    }

    fun bind(section: Item?) {
        section?.let {
            itemView.text_title.text = section.createdAt
        }
    }
}

class ListFooterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_position_list_footer, parent, false)
            view.text_error.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }

    fun bind(state: State?) {
        state?.let {
            itemView.text_error.visibility = if (state == State.ERROR) VISIBLE else INVISIBLE
            itemView.progress_bar.visibility = if (state == State.LOADING) VISIBLE else INVISIBLE
        }
    }
}
