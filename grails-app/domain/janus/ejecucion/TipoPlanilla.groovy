package janus.ejecucion

class TipoPlanilla implements Serializable{

     String codigo
    String nombre

    static mapping = {

        table 'tppl'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'tppl__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'tppl__id'
            codigo column: 'tpplcdgo'
            nombre column: 'tppldscr'
        }

    }

    static constraints = {

        codigo(blank: true, nullable: true)
        nombre(blank: true, nullable: true)
    }
}
