package com.example.basicloanapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.basicloanapp.R
import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.LoanBodyResponse

class LoanAdapter(private val onClick: (Int) ->  Unit):
    ListAdapter<Loan, LoanViewHolder>(LoanDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        return LoanViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object LoanDiffCallback: DiffUtil.ItemCallback<Loan>() {
        override fun areItemsTheSame( oldItem: Loan, newItem: Loan): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Loan, newItem: Loan): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class LoanViewHolder(private val view: View, val onClick: (Int) -> Unit): RecyclerView.ViewHolder(view) {
    private var currLoan: Loan? = null
    private val amount = view.findViewById<TextView>(R.id.item_amount)
    private val date = view.findViewById<TextView>(R.id.item_date)
    private val percent = view.findViewById<TextView>(R.id.item_percent)
    private val period = view.findViewById<TextView>(R.id.item_period)
    private val state = view.findViewById<TextView>(R.id.item_state)

    init {
        view.setOnClickListener {
            currLoan?.let {
                onClick(it.id)
            }
        }
    }

    fun bind(loan: Loan) {
        currLoan = loan
        amount.text = view.resources.getString(R.string.item_amount, loan.amount)
        date.text = view.resources.getString(R.string.item_date, loan.date)
        percent.text = view.resources.getString(R.string.item_percent, loan.percent)
        period.text = view.resources.getString(R.string.item_period, loan.toDate)

        when(loan.state) {
            view.resources.getString(R.string.item_approved) -> {
                state.setTextColor(view.resources.getColor(R.color.itemApproved))
            }
            view.resources.getString(R.string.item_registered) -> {
                state.setTextColor(view.resources.getColor(R.color.itemRegistered))
            }
            view.resources.getString(R.string.item_rejected) -> {
                state.setTextColor(view.resources.getColor(R.color.itemRejected))
            }
        }
        state.text = loan.state
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (Int) -> Unit): LoanViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.loan_view_item, parent, false)
            return LoanViewHolder(view, onClick)
        }
    }
}