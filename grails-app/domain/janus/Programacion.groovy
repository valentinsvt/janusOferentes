package janus

class Programacion implements Serializable {
    String descripcion
    Date fechaInicio
    Date fechaFin
    static mapping = {
        table 'prog'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'prog__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'prog__id'
            descripcion column: 'progdscr'
            fechaInicio column: 'progfcin'
            fechaFin column: 'progfcfn'
        }
    }
    static constraints = {
        descripcion(size: 1..40, blank: false, nullable: false, attributes: [title: 'descripcion'])
        fechaInicio(blank: true, nullable: true, attributes: [title: 'fechaInicio'])
        fechaFin(blank: true, nullable: true, attributes: [title: 'fechaFin'])
    }
}