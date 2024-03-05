package it.crmnccgroup.crmncc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.data.model.Cliente

class ClientiRecyclerViewAdapter(
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ClientiRecyclerViewAdapter.ClientiViewHolder>() {

    interface OnItemClickListener {
        fun onClientiItemClick(clienti: Cliente)
    }

    private var clientiList: List<Cliente> = arrayListOf()
    private val onItemClickListener: OnItemClickListener

    init {
        this.onItemClickListener = onItemClickListener
    }

    fun updateList(list: MutableList<Cliente>) {
        this.clientiList = list
        notifyDataSetChanged()
    }

    //esegue l'inflate del file di layout che voglio associare alla cella
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cella_tabella_clienti, parent, false)
        return ClientiViewHolder(view)
    }

    //binding tra parte grafica e dati dell'oggetto
    override fun onBindViewHolder(holder: ClientiViewHolder, position: Int) {
        holder.bind(clientiList[position])
    }

    override fun getItemCount(): Int {
        return clientiList.size ?: 0
    }

    inner class ClientiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //definizione riferimenti agli elementi e associazione alle variabili con gli ID
        private val textAliasClienti: TextView
        private val textCognomeClienti: TextView
        private val textNumeroTelefonoClienti: TextView
        private val textNomeClienti: TextView

        init {
            textAliasClienti = itemView.findViewById(R.id.textAliasClienti)
            textCognomeClienti = itemView.findViewById(R.id.textCognomeClienti)
            textNumeroTelefonoClienti = itemView.findViewById(R.id.textNumeroTelefonoClienti)
            textNomeClienti = itemView.findViewById(R.id.textNomeClienti)
            itemView.setOnClickListener(this)
        }

        //binding permette di associare il dato alla cella del file di layout
        fun bind(clienti: Cliente) {
            textAliasClienti.text = clienti.alias
            textCognomeClienti.text = clienti.cognome
            textNumeroTelefonoClienti.text = clienti.telefono
            textNomeClienti.text = clienti.nome
        }

        override fun onClick(v: View?) { //da togliere
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onClientiItemClick(clientiList[position])
            }
        }
    }
}