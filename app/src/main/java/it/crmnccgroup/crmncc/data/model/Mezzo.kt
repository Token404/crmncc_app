package it.crmnccgroup.crmncc.data.model

class Mezzo(
    var id: String = "",
    var auto: String = "",
    var categoria: String = "",
    var colore: String = "",
    var creator: String = "",
    var posti: Int = 0,
    var targa: String = ""
): Base()