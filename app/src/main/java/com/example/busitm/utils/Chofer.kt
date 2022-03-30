package com.example.busitm.utils

data class Chofer(
    var nombre: String? = null,
    var apellido: String? = null,
    var codigo_ruta: String? = null,
    var nombre_ruta: String? = null
) {
    override fun toString(): String {
        return "\nNombre: $nombre\nApellido: $apellido\n" +
                "CódigoRuta: $codigo_ruta\nNombreRuta: $nombre_ruta"
    }
}
