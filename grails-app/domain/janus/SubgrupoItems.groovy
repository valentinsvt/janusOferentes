package janus

class SubgrupoItems implements Serializable {
    Grupo grupo
    int codigo
    String descripcion
    static mapping = {
        table 'sbgr'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'sbgr__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'sbgr__id'
            grupo column: 'grpo__id'
            codigo column: 'sbgrcdgo'
            descripcion column: 'sbgrdscr'
        }
    }
    static constraints = {
        grupo(blank: false, attributes: [title: 'grupo'])
        codigo(blank: false, attributes: [title: 'numero'])
        descripcion(size: 1..63, blank: false, unique: true, attributes: [title: 'descripcion'])
    }
}