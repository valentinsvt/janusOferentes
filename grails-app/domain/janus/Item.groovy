package janus

class Item implements Serializable {

    Persona responsable

    Unidad unidad
    TipoItem tipoItem
    DepartamentoItem departamento
    String codigo
    String nombre
    double peso
    Date fecha = new Date()
    Date fechaRegistro
    Date fechaModificacion
    String estado
    double transportePeso
    double transporteVolumen
    String padre
    String inec
    String transporte
    double rendimiento = 0
    String tipo
    String campo
    String registro
    String combustible
    String observaciones
    String foto
    TipoLista tipoLista
    String especificaciones
    Persona persona
    String janusId=""
    String codigoEspecificacion
    double codigoComprasPublicas  = 0

    static mapping = {
        table 'item'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'item__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'item__id'
            unidad column: 'undd__id'
            tipoItem column: 'tpit__id'
            departamento column: 'dprt__id'
            codigo column: 'itemcdgo'
            nombre column: 'itemnmbr'
            peso column: 'itempeso'
            fecha column: 'itemfcha'
            fechaRegistro column: 'itemfcrg'
            fechaModificacion column: 'itemfcmd'
            estado column: 'itemetdo'
            transportePeso column: 'itemtrps'
            transporteVolumen column: 'itemtrvl'
            padre column: 'itempdre'
            inec column: 'iteminec'
            transporte column: 'itemtrnp'
            rendimiento column: 'itemrndm'
            tipo column: 'itemtipo'
            campo column: 'itemcmpo'
            registro column: 'itemrgst'
            combustible column: 'itemcmbs'
            observaciones column: 'itemobsr'
            foto column: 'itemfoto'
            tipoLista column: 'tpls__id'
            especificaciones column: 'itemespc'
            responsable column: 'prsn__id'
            persona column: 'ofrt__id'
            janusId column: 'itemjnid'
            codigoEspecificacion column: 'itemcdes'
            codigoComprasPublicas column: 'cpac__id'
        }
    }
    static constraints = {
        nombre(size: 1..160, blank: false, attributes: [title: 'nombre'])
        codigo(size: 1..20, blank: false, attributes: [title: 'numero'])
        unidad(blank: true, nullable: true, attributes: [title: 'unidad'])
        tipoItem(blank: true, nullable: true, attributes: [title: 'tipoItem'])
        peso(blank: true, nullable: true, attributes: [title: 'peso'])
        departamento(blank: true, nullable: true, attributes: [title: 'departamento'])
        estado(size: 1..1, blank: true, nullable: true, attributes: [title: 'estado'])
        fecha(blank: true, nullable: true, attributes: [title: 'fecha'])
        fechaRegistro(blank: true, nullable: true, attributes: [title: 'fecha de registro'])
        fechaModificacion(blank: true, nullable: true, attributes: [title: 'fecha de ultima modificacion'])
        transportePeso(blank: true, nullable: true, attributes: [title: 'transportePeso'])
        transporteVolumen(blank: true, nullable: true, attributes: [title: 'transporteVolumen'])
        padre(size: 1..15, blank: true, nullable: true, attributes: [title: 'padre'])
        inec(size: 1..1, blank: true, nullable: true, attributes: [title: 'inec'])
        rendimiento(blank: true, nullable: true, attributes: [title: 'rendimiento'])
        tipo(size: 1..1, blank: true, nullable: true, attributes: [title: 'tipo'])
        campo(size: 1..29, blank: true, unique: true, nullable: true, attributes: [title: 'nombre corto'])
        registro(size: 1..1, blank: true, nullable: true, attributes: [title: 'registro'])
        transporte(size: 1..2, blank: true, nullable: true, attributes: [title: 'transporte'])
        combustible(size: 1..1, blank: true, nullable: true, attributes: [title: 'combustible'])
        observaciones(size: 1..127, blank: true, nullable: true, attributes: [title: 'observaciones'])
        foto(size: 1..100, blank: true, nullable: true)
        tipoLista(blank: true, nullable: true)
        especificaciones(blank: true, nullable: true, size: 1..1024)
        responsable(nullable:true,blank:true)
        persona(nullable: true,blank:true)
        janusId(nullable: true,blank: true)
        codigoEspecificacion(blank: true, nullable: true, size: 1..30)
        codigoComprasPublicas(blank: true, nullable: true, attributes: [title: 'Código de Compras Públicas'])
    }

    String toString() {
        "${this.codigo} - ${this.nombre}"
    }
}