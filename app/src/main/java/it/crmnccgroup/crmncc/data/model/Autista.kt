package it.crmnccgroup.crmncc.data.model



class Autista(
    var id: String = "",
    var alias: String = "",
    var creator: String = "",
    var nome: String = "",
    var cognome: String = "",
    var telefono: String = "",
    var email: String = "",
    var citta: String = "",
    var provincia: String = ""
): Base()