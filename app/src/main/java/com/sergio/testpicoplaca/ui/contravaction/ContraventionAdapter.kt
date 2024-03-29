package com.sergio.testpicoplaca.ui.contravaction

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sergio.testpicoplaca.R
import com.sergio.testpicoplaca.data.db.Contravention
import com.sergio.testpicoplaca.utils.TimeHelper
import kotlinx.android.synthetic.main.item_history.view.*

class ContraventionAdapter(mContr:List<Contravention> = ArrayList() ): RecyclerView.Adapter<ContraventionAdapter.ContravactionHolder>() {
    var contraventionList:List<Contravention> = mContr
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContravactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history,parent,false)
        return ContravactionHolder(view)
    }

    override fun getItemCount() = contraventionList.size

    override fun onBindViewHolder(holder: ContravactionHolder, position: Int) {
        val contravenion = contraventionList[position]
        holder.bind(contravenion)
    }

    class ContravactionHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(contravaction:Contravention){
            itemView.labelLicensePlate.text = contravaction.licensePlate
            itemView.labelContravention.text= itemView.context.getString(if (contravaction.contravention) R.string.contravention_positive else R.string.contravention_negative)
            itemView.labelContravention.setTextColor(
                if (contravaction.contravention) Color.RED else Color.rgb(0,153,0)
            )
            itemView.labelTimeRegister.text = contravaction.dateRegister
            itemView.labelTime.text         = TimeHelper.setContext(itemView.context).formatDate(contravaction.date)
        }
    }
}