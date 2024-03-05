package it.crmnccgroup.crmncc.data.model

class Cliente(
    var id: String = "",
    var alias: String = "",
    var citta: String = "",
    var cognome: String = "",
    var creator: String = "",
    var email: String = "",
    var nome: String = "",
    var provincia: String = "",
    var telefono: String = ""
): Base()