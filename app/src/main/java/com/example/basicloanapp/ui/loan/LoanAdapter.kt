package com.example.basicloanapp.ui.loan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.basicloanapp.R
import com.example.basicloanapp.domain.entity.Loan

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
        val dateInDays = loan.toDate.split(" ")[0]
        amount.text = view.resources.getString(R.string.item_amount, loan.amount)
        date.text = view.resources.getString(R.string.item_date, dateInDays)
        percent.text = view.resources.getString(R.string.item_percent, loan.percent)

        val stateText = when(loan.state) {
            "APPROVED" -> {
                state.setTextColor(view.resources.getColor(R.color.itemApproved))
                view.resources.getString(R.string.item_approved)
            }
            "REGISTERED" -> {
                state.setTextColor(view.resources.getColor(R.color.itemRegistered))
                view.resources.getString(R.string.item_registered)
            }
            "REJECTED" -> {
                state.setTextColor(view.resources.getColor(R.color.itemRejected))
                view.resources.getString(R.string.item_rejected)
            }
            else -> ""
        }
        state.text = stateText
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (Int) -> Unit): LoanViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.loan_view_item, parent, false)
            return LoanViewHolder(view, onClick)
        }
    }
}