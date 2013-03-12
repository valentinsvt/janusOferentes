package janus

class Parametros implements Serializable {
    //int indicador
    //String password
    //String direccionObrasCiviles
    //String direccionVialidadConcesiones
    String factorReduccion
    String factorVelocidad
    String capacidadVolquete
    String factorVolumen
    String factorReduccionTiempo
    String factorPeso
    double indiceGastosGenerales
    double impreso
    double indiceUtilidad
    int contrato
    double totales
    double indiceCostosIndirectosObra
    double indiceCostosIndirectosMantenimiento
    double administracion
    double indiceCostosIndirectosGarantias
    double indiceCostosIndirectosCostosFinancieros
    double indiceCostosIndirectosVehiculos
    double indiceCostosIndirectosPromocion
    double indiceCostosIndirectosTimbresProvinciales
    Item chofer
    Item volquete


    static mapping = {
        table 'paux'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'paux__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'paux__id'
            //indicador column: 'pauxindi'
            //password column: 'pauxpass'
            //direccionObrasCiviles column: 'prsn_dic'
            //direccionVialidadConcesiones column: 'prsndvyc'
            factorReduccion column: 'trnpftrd'
            factorVelocidad column: 'trnpvlcd'
            capacidadVolquete column: 'trnpcpvl'
            factorVolumen column: 'trnpftvl'
            factorReduccionTiempo column: 'trnprdtp'
            factorPeso column: 'trnpftps'
            indiceGastosGenerales column: 'indignrl'
            impreso column: 'indiimpr'
            indiceUtilidad column: 'indiutil'
            contrato column: 'indicntr'
            totales column: 'inditotl'
            indiceCostosIndirectosObra column: 'indidrob'
            indiceCostosIndirectosMantenimiento column: 'indimntn'
            administracion column: 'indiadmn'
            indiceCostosIndirectosGarantias column: 'indtgrnt'
            indiceCostosIndirectosCostosFinancieros column: 'indicsfn'
            indiceCostosIndirectosVehiculos column: 'indivhcl'
            indiceCostosIndirectosPromocion column: 'indiprmo'
            indiceCostosIndirectosTimbresProvinciales column: 'inditmbr'
            chofer column: 'itemchfr'
            volquete column: 'itemvlqt'
        }
    }
    static constraints = {
        //indicador(blank: true, nullable: true, attributes: [title: 'indicador'])
        //password(size: 1..8, blank: true, nullable: true, attributes: [title: 'password'])
        //direccionObrasCiviles(size: 1..15, blank: true, nullable: true, attributes: [title: 'direccionObrasCiviles'])
        //direccionVialidadConcesiones(size: 1..15, blank: true, nullable: true, attributes: [title: 'direccionVialidadConcesiones'])
        factorReduccion(size: 1..6, blank: true, nullable: true, attributes: [title: 'factorReduccion'])
        factorVelocidad(size: 1..6, blank: true, nullable: true, attributes: [title: 'factorVelocidad'])
        capacidadVolquete(size: 1..6, blank: true, nullable: true, attributes: [title: 'capacidadVolquete'])
        factorVolumen(size: 1..6, blank: true, nullable: true, attributes: [title: 'factorVolumen'])
        factorReduccionTiempo(size: 1..6, blank: true, nullable: true, attributes: [title: 'factorReduccionTiempo'])
        factorPeso(size: 1..6, blank: true, nullable: true, attributes: [title: 'factorPeso'])
        impreso(blank: true, nullable: true, attributes: [title: 'impreso'])
        indiceUtilidad(blank: true, nullable: true, attributes: [title: 'indiceUtilidad'])
        contrato(blank: true, nullable: true, attributes: [title: 'contrato'])
        totales(blank: true, nullable: true, attributes: [title: 'totales'])
        indiceGastosGenerales(blank: true, nullable: true, attributes: [title: 'indiceGastosGenerales'])
        indiceCostosIndirectosObra(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosObra'])
        indiceCostosIndirectosMantenimiento(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosMantenimiento'])
        administracion(blank: true, nullable: true, attributes: [title: 'administracion'])
        indiceCostosIndirectosGarantias(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosGarantias'])
        indiceCostosIndirectosCostosFinancieros(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosCostosFinancieros'])
        indiceCostosIndirectosVehiculos(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosVehiculos'])
        indiceCostosIndirectosPromocion(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosPromocion'])
        indiceCostosIndirectosTimbresProvinciales(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosTimbresProvinciales'])
        chofer(nullable: true, blank: true)
        volquete(nullable: true, blank: true)
    }
}