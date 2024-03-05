package it.crmnccgroup.crmncc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.data.model.Mezzo

class MezziRecyclerViewAdapter(
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MezziRecyclerViewAdapter.MezziViewHolder>() {

    interface OnItemClickListener {
        fun onMezziItemClick(mezzi: Mezzo)
    }

    private var mezziList: List<Mezzo> = arrayListOf()
    private val onItemClickListener: OnItemClickListener

    init {
        this.onItemClickListener = onItemClickListener
    }

    fun updateList(list: MutableList<Mezzo>) {
        this.mezziList = list
        notifyDataSetChanged()
    }

    //esegue l'inflate del file di layout che voglio associare alla cella
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MezziViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cella_tabella_mezzi, parent, false)
        return MezziViewHolder(view)
    }

    //binding tra parte grafica e dati dell'oggetto
    override fun onBindViewHolder(holder: MezziViewHolder, position: Int) {
        holder.bind(mezziList[position])
    }

    override fun getItemCount(): Int {
        return mezziList.size ?: 0
    }

    inner class MezziViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //definizione riferimenti agli elementi e associazione alle variabili con gli ID
        private val textAutoMezzi: TextView
        private val textTargaMezzi: TextView
        private val textColoreMezzi: TextView
        private val textCategoriaMezzi: TextView
        private val textNumeroPasseggeriMezzi: TextView

        init {
            textAutoMezzi = itemView.findViewById(R.id.textAutoMezzi)
            textTargaMezzi = itemView.findViewById(R.id.textTargaMezzi)
            textColoreMezzi = itemView.findViewById(R.id.textColoreMezzi)
            textCategoriaMezzi = itemView.findViewById(R.id.textCategoriaMezzi)
            textNumeroPasseggeriMezzi = itemView.findViewById(R.id.textNumeroPasseggeriMezzi)
            itemView.setOnClickListener(this)
        }

        //binding permette di associare il dato alla cella del file di layout
        fun bind(mezzi: Mezzo) {
            textAutoMezzi.text = mezzi.auto
            textTargaMezzi.text = mezzi.targa
            textColoreMezzi.text = mezzi.colore
            textCategoriaMezzi.text = mezzi.categoria
            textNumeroPasseggeriMezzi.text = mezzi.posti.toString()
        }

        override fun onClick(v: View?) { //da togliere
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onMezziItemClick(mezziList[position])
            }
        }
    }
}