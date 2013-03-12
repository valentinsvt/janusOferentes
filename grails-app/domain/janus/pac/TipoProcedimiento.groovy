package janus.pac

class TipoProcedimiento {

    String descripcion
    String sigla
    double bases = 0
    double techo = 0

    static mapping = {
        table 'tppc'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'tppc__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'tppc__id'
            descripcion column: 'tppcdscr'
            sigla column: 'tppcsgla'
            bases column: 'tppcbase'
            techo column: 'tppctcho'
        }
    }
    static constraints = {
        descripcion(nullable: true, blank: true, size: 1..64)
        sigla(nullable: false, blank: false, size: 1..5)
    }

}
