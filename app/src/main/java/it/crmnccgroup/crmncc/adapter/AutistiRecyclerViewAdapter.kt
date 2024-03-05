package it.crmnccgroup.crmncc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.data.model.Autista

class AutistiRecyclerViewAdapter(
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AutistiRecyclerViewAdapter.AutistiViewHolder>() {

    interface OnItemClickListener {
        fun onAutistiItemClick(autisti: Autista)
    }

    private var autistiList: List<Autista> = arrayListOf()
    private val onItemClickListener: OnItemClickListener

    init {
        this.onItemClickListener = onItemClickListener
    }

    fun updateList(list: MutableList<Autista>) {
        this.autistiList = list
        notifyDataSetChanged()
    }

    //esegue l'inflate del file di layout che voglio associare alla cella
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutistiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cella_tabella_autisti, parent, false)
        return AutistiViewHolder(view)
    }

    //binding tra parte grafica e dati dell'oggetto
    override fun onBindViewHolder(holder: AutistiViewHolder, position: Int) {
        holder.bind(autistiList[position])
    }

    override fun getItemCount(): Int {
        return autistiList.size ?: 0
    }

    inner class AutistiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //definizione riferimenti agli elementi e associazione alle variabili con gli ID
        private val textAliasAutisti: TextView
        private val textCognomeAutisti: TextView
        private val textNomeAutisti: TextView
        private val textNumeroTelefonoAutisti: TextView

        init {
            textAliasAutisti = itemView.findViewById(R.id.textAliasAutisti)
            textCognomeAutisti = itemView.findViewById(R.id.textCognomeAutisti)
            textNomeAutisti = itemView.findViewById(R.id.textNomeAutisti)
            textNumeroTelefonoAutisti = itemView.findViewById(R.id.textNumeroTelefonoAutisti)
            itemView.setOnClickListener(this)
        }

        //binding permette di associare il dato alla cella del file di layout
        fun bind(autisti: Autista) {
            textAliasAutisti.text = autisti.alias
            textCognomeAutisti.text = autisti.cognome
            textNomeAutisti.text = autisti.nome
            textNumeroTelefonoAutisti.text = autisti.telefono
        }

        override fun onClick(v: View?) { //da togliere
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onAutistiItemClick(autistiList[position])
            }
        }
    }
}