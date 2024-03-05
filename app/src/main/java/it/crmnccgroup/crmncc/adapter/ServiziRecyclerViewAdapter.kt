package it.crmnccgroup.crmncc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.data.model.Servizio
import java.text.SimpleDateFormat

class ServiziRecyclerViewAdapter(
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ServiziRecyclerViewAdapter.ServiziViewHolder>() {

    interface OnItemClickListener {
        fun onServiziItemClick(servizi: Servizio)
    }

    private var serviziList: List<Servizio> = arrayListOf()
    private val onItemClickListener: OnItemClickListener

    init {
        this.onItemClickListener = onItemClickListener
    }

    fun updateList(list: MutableList<Servizio>) {
        this.serviziList = list
        notifyDataSetChanged()
    }

    //esegue l'inflate del file di layout che voglio associare alla cella
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiziViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cella_tabella_servizi, parent, false)
        return ServiziViewHolder(view)
    }

    //binding tra parte grafica e dati dell'oggetto
    override fun onBindViewHolder(holder: ServiziViewHolder, position: Int) {
        holder.bind(serviziList[position])
    }

    override fun getItemCount(): Int {
        return serviziList.size
    }

    inner class ServiziViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //definizione riferimenti agli elementi e associazione alle variabili con gli ID
        private val textViewDataOra: TextView
        private val textViewPartenza: TextView
        private val textViewDestinazione: TextView
        private val textViewCliente: TextView
        private val textViewNPasseggeri: TextView
        private val textViewAuto: TextView
        private val textViewAutista: TextView
        //manca stato servizio

        init {
            textViewDataOra = itemView.findViewById(R.id.textDataOraServizi)
            textViewPartenza = itemView.findViewById(R.id.textPartenzaServizi)
            textViewDestinazione = itemView.findViewById(R.id.textDestinazioneServizi)
            textViewCliente = itemView.findViewById(R.id.textClienteServizi)
            textViewNPasseggeri = itemView.findViewById(R.id.textNPasseggeriServizi)
            textViewAuto = itemView.findViewById(R.id.textAutoServizi)
            textViewAutista = itemView.findViewById(R.id.textAutistaServizi)
            itemView.setOnClickListener(this)
        }

        //binding permette di associare il dato alla cella del file di layout
        fun bind(servizi: Servizio) {
            textViewDataOra.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(servizi.orario)
            textViewPartenza.text = servizi.partenza
            textViewDestinazione.text = servizi.destinazione
            textViewCliente.text = servizi.cliente
            textViewNPasseggeri.text = servizi.numero_passeggeri.toString()
            textViewAuto.text = servizi.mezzo
            textViewAutista.text = servizi.autista
        }

        override fun onClick(v: View?) { //da togliere
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onServiziItemClick(serviziList[position])
            }
        }
    }
}