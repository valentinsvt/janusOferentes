package janus

class TipoInstitucion implements Serializable {
    String codigo
    String descripcion
    static mapping = {
        table 'tpin'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'tpin__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'tpin__id'
            codigo column: 'tpincdgo'
            descripcion column: 'tpindscr'
        }
    }
    static constraints = {
        codigo(size: 1..1, blank: false, attributes: [title: 'numero'])
        descripcion(size: 1..31, blank: false, attributes: [title: 'descripcion'])
    }

    String toString() {
        "${descripcion}"
    }
}