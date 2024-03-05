package it.crmnccgroup.crmncc.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class Servizio(
   var id: String = "",
   var creator: String? = "",
   var autista: String? = "",
   var cliente: String? = "",
   var destinazione: String? = "",
   var importo: Int = 0,
   var mezzo: String? = "",
   var passeggero: String? = "",
   var numero_passeggeri: Int = 0,
   var orario: Date? = null,
   var partenza: String? = ""
): Base()