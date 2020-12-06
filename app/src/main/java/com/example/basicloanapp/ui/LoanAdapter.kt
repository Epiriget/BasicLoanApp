package com.example.basicloanapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.basicloanapp.R
import com.example.basicloanapp.service.LoanBodyResponse

class LoanAdapter(private val onClick: (LoanBodyResponse) ->  Unit):
    ListAdapter<LoanBodyResponse, LoanViewHolder>(LoanDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        return LoanViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object LoanDiffCallback: DiffUtil.ItemCallback<LoanBodyResponse>() {
        override fun areItemsTheSame( oldItem: LoanBodyResponse, newItem: LoanBodyResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LoanBodyResponse, newItem: LoanBodyResponse): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class LoanViewHolder(view: View, val onClick: (LoanBodyResponse) -> Unit): RecyclerView.ViewHolder(view) {
    private var currLoan: LoanBodyResponse? = null
    private val amount = view.findViewById<TextView>(R.id.item_amount)
    private val date = view.findViewById<TextView>(R.id.item_date)

    init {
        view.setOnClickListener {
            currLoan?.let {
                onClick(it)
            }
        }
    }

    // Todo: Replace mock representation
    fun bind(loan: LoanBodyResponse) {
        amount.text = loan.amount.toString()
        date.text = loan.date
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (LoanBodyResponse) -> Unit): LoanViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.loan_view_item, parent, false)
            return LoanViewHolder(view, onClick)
        }
    }
}