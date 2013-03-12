package janus

class Obra implements Serializable {
    int idJanus
    Persona responsableObra
    Persona revisor
    Persona inspector
    Comunidad comunidad
    Parroquia parroquia
    TipoObra tipoObjetivo
    Programacion programacion
    EstadoObra estadoObra
    ClaseObra claseObra
    Departamento departamento
    Lugar lugar
    String barrio
    String codigo
    String nombre
    String descripcion
    Date fechaInicio
    Date fechaFin
    double distanciaPeso
    double distanciaVolumen
    double latitud
    double longitud
    int beneficiariosDirectos
    int benificiariosIndirectos
    int beneficiariosPotenciales
    String estado
    String referencia
    Date fechaPreciosRubros
    String oficioIngreso
    String oficioSalida
    int plazo
    String observaciones
    String tipo
    Date fechaCreacionObra
    Item chofer
    Item volquete
    String memoCantidadObra
    String memoSalida
    Date fechaOficioSalida
    int factorReduccion
    int factorVelocidad
    int capacidadVolquete
    double factorVolumen
    double factorPeso
    int factorReduccionTiempo
    String sitio
    int plazoEjecucionAnios
    int plazoEjecucionMeses
    int plazoEjecucionDias
    String formulaPolinomica
    int indicador
    double indiceGastosGenerales
    double impreso
    double indiceUtilidad
    int contrato
    double totales
    double valor
    Presupuesto partidaObra
    String memoCertificacionPartida
    String memoActualizacionPrefecto
    String memoPartidaPresupuestaria
    int porcentajeAnticipo
    int porcentajeReajuste
    double indiceCostosIndirectosObra
    double indiceCostosIndirectosMantenimiento
    double administracion
    double indiceCostosIndirectosGarantias
    double indiceCostosIndirectosCostosFinancieros
    double indiceCostosIndirectosVehiculos
    double indiceCostosIndirectosPromocion
    double indiceCostosIndirectosTimbresProvinciales
    double distanciaPesoEspecial
    double distanciaVolumenMejoramiento
    double distanciaVolumenCarpetaAsfaltica
    Lugar listaPeso1
    Lugar listaVolumen0
    Lugar listaVolumen1
    Lugar listaVolumen2
    Lugar listaManoObra

    static mapping = {
        table 'obra'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'obra__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'obra__id'
            idJanus column: 'obrajnid'
            responsableObra column: 'prsn__id'
            revisor column: 'obrarvsr'
            inspector column: 'obrainsp'
            comunidad column: 'cmnd__id'
            parroquia column: 'parr__id'
            tipoObjetivo column: 'tpob__id'
            programacion column: 'prog__id'
            estadoObra column: 'edob__id'
            claseObra column: 'csob__id'
            departamento column: 'dpto__id'
            lugar column: 'lgar__id'
            codigo column: 'obracdgo'
            nombre column: 'obranmbr'
            descripcion column: 'obradscr'
            fechaInicio column: 'obrafcin'
            fechaFin column: 'obrafcfn'
            distanciaPeso column: 'obradsps'
            distanciaVolumen column: 'obradsvl'
            latitud column: 'obralatt'
            longitud column: 'obralong'
            beneficiariosDirectos column: 'obrabfdi'
            benificiariosIndirectos column: 'obrabfin'
            beneficiariosPotenciales column: 'obrabfpt'
            estado column: 'obraetdo'
            referencia column: 'obrarefe'
            fechaCreacionObra column: 'obrafcha'
            oficioIngreso column: 'obraofig'
            oficioSalida column: 'obraofsl'
            plazo column: 'obraplzo'
            observaciones column: 'obraobsr'
            tipo column: 'obratipo'
            fechaPreciosRubros column: 'rbpcfcha'
            chofer column: 'obrachfr'
            volquete column: 'obravlqt'
            memoCantidadObra column: 'obrammco'
            memoSalida column: 'obrammsl'
            fechaOficioSalida column: 'obrafcsl'
            factorReduccion column: 'obraftrd'
            factorVelocidad column: 'obravlcd'
            capacidadVolquete column: 'obracpvl'
            factorVolumen column: 'obraftvl'
            factorPeso column: 'obraftps'
            factorReduccionTiempo column: 'obrardtp'
            sitio column: 'obrasito'
            plazoEjecucionAnios column: 'obrapz_a'
            plazoEjecucionMeses column: 'obrapz_m'
            plazoEjecucionDias column: 'obrapz_d'
            formulaPolinomica column: 'obrafrpl'
            indicador column: 'obraindi'
            indiceGastosGenerales column: 'indignrl'
            impreso column: 'indiimpr'
            indiceUtilidad column: 'indiutil'
            contrato column: 'indicntr'
            totales column: 'inditotl'
            valor column: 'obravlor'
            partidaObra column: 'prsp__id'
            memoCertificacionPartida column: 'obrammpr'
            memoActualizacionPrefecto column: 'obraprft'
            memoPartidaPresupuestaria column: 'obrammfn'
            porcentajeAnticipo column: 'obraantc'
            porcentajeReajuste column: 'obrarjst'
            indiceCostosIndirectosObra column: 'indidrob'
            indiceCostosIndirectosMantenimiento column: 'indimntn'
            administracion column: 'indiadmn'
            indiceCostosIndirectosGarantias column: 'indigrnt'
            indiceCostosIndirectosCostosFinancieros column: 'indicsfn'
            indiceCostosIndirectosVehiculos column: 'indivhcl'
            indiceCostosIndirectosPromocion column: 'indiprmo'
            indiceCostosIndirectosTimbresProvinciales column: 'inditmbr'
            distanciaPesoEspecial column: 'obradses'
            distanciaVolumenMejoramiento column: 'obradsmj'
            distanciaVolumenCarpetaAsfaltica column: 'obradsca'
            barrio column: 'obrabarr'

            listaPeso1 column: 'lgarps01'
            listaVolumen0 column: 'lgarvl00'
            listaVolumen1 column: 'lgarvl01'
            listaVolumen2 column: 'lgarvl02'
            listaManoObra column: 'lgarlsmq'
        }
    }
    static constraints = {

        codigo(size: 1..20, blank: false, attributes: [title: 'numero'])
        nombre(size: 1..127, blank: true, nullable: true, attributes: [title: 'nombre'])
        responsableObra(blank: true, nullable: true, attributes: [title: 'responsableObra'])
        revisor(blank: true, nullable: true, attributes: [title: 'revisor'])
        lugar(blank: true, nullable: true, attributes: [title: 'lugar'])
        listaPeso1(blank: true, nullable: true, attributes: [title: 'Lista al peso 1'])
        listaVolumen0(blank: true, nullable: true, attributes: [title: 'Lista al volumen 1'])
        listaVolumen1(blank: true, nullable: true, attributes: [title: 'Lista al volumen 1'])
        listaVolumen2(blank: true, nullable: true, attributes: [title: 'Lista al volumen 1'])
        listaManoObra(blank: true, nullable: true, attributes: [title: 'Lista de MO y Equipos'])

        comunidad(blank: true, nullable: true, attributes: [title: 'comunidad'])
        parroquia(blank: true, nullable: true, attributes: [title: 'parroquia'])
        tipoObjetivo(blank: true, nullable: true, attributes: [title: 'tipoObjetivo'])
        claseObra(blank: true, nullable: true, attributes: [title: 'claseObra'])
        estadoObra(blank: true, nullable: true, attributes: [title: 'estadoObra'])
        programacion(blank: true, nullable: true, attributes: [title: 'programacion'])
        departamento(blank: true, nullable: true, attributes: [title: 'departamento'])
        descripcion(size: 1..511, blank: true, nullable: true, attributes: [title: 'descripcion'])
        fechaInicio(blank: true, nullable: true, attributes: [title: 'fechaInicio'])
        fechaFin(blank: true, nullable: true, attributes: [title: 'fechaFin'])
        distanciaPeso(blank: true, nullable: true, attributes: [title: 'distanciaPeso'])
        distanciaVolumen(blank: true, nullable: true, attributes: [title: 'distanciaVolumen'])
        latitud(blank: true, nullable: true, attributes: [title: 'latitud'])
        longitud(blank: true, nullable: true, attributes: [title: 'longitud'])
        beneficiariosDirectos(blank: true, nullable: true, attributes: [title: 'beneficiariosDirectos'])
        benificiariosIndirectos(blank: true, nullable: true, attributes: [title: 'benificiariosIndirectos'])
        beneficiariosPotenciales(blank: true, nullable: true, attributes: [title: 'beneficiariosPotenciales'])
        estado(size: 1..1, blank: true, nullable: true, attributes: [title: 'estado'])
        referencia(size: 1..127, blank: true, nullable: true, attributes: [title: 'referencia'])
        fechaCreacionObra(blank: true, nullable: true, attributes: [title: 'fecha'])
        oficioIngreso(size: 1..20, blank: true, nullable: true, attributes: [title: 'oficioIngreso'])
        oficioSalida(size: 1..20, blank: true, nullable: true, attributes: [title: 'oficioSalida'])
        plazo(blank: true, nullable: true, attributes: [title: 'plazo'])
        observaciones(size: 1..127, blank: true, nullable: true, attributes: [title: 'observaciones'])
        tipo(size: 1..1, blank: true, nullable: true, attributes: [title: 'tipo'])
        fechaPreciosRubros(blank: true, nullable: true, attributes: [title: 'fecha'])
        chofer(size: 1..31, blank: true, nullable: true, attributes: [title: 'itemChofer'])
        volquete(size: 1..31, blank: true, nullable: true, attributes: [title: 'item/volquete'])
        memoCantidadObra(size: 1..20, blank: true, nullable: true, attributes: [title: 'memoCantidadObra'])
        memoSalida(size: 1..20, blank: true, nullable: true, attributes: [title: 'memoSalida'])
        fechaOficioSalida(blank: true, nullable: true, attributes: [title: 'fechaOficioSalida'])
        factorReduccion(blank: true, nullable: true, attributes: [title: 'factorReduccion'])
        factorVelocidad(blank: true, nullable: true, attributes: [title: 'factorVelocidad'])
        capacidadVolquete(blank: true, nullable: true, attributes: [title: 'capacidadVolquete'])
        factorVolumen(blank: true, nullable: true, attributes: [title: 'factorVolumen'])
        factorPeso(blank: true, nullable: true, attributes: [title: 'factorPeso'])
        factorReduccionTiempo(blank: true, nullable: true, attributes: [title: 'factorReduccionTiempo'])
        sitio(size: 1..63, blank: true, nullable: true, attributes: [title: 'sitio'])
        plazoEjecucionAnios(blank: true, nullable: true, attributes: [title: 'plazoEjecucionAnios'])
        plazoEjecucionMeses(blank: true, nullable: true, attributes: [title: 'plazoEjecucionMeses'])
        plazoEjecucionDias(blank: true, nullable: true, attributes: [title: 'plazoEjecucionDias'])
        formulaPolinomica(size: 1..20, blank: true, nullable: true, attributes: [title: 'formulaPolinomica'])
        indicador(blank: true, nullable: true, attributes: [title: 'indicador'])
        indiceGastosGenerales(blank: true, nullable: true, attributes: [title: 'indiceGastosGenerales'])
        impreso(blank: true, nullable: true, attributes: [title: 'impreso'])
        indiceUtilidad(blank: true, nullable: true, attributes: [title: 'indiceUtilidad'])
        contrato(blank: true, nullable: true, attributes: [title: 'contrato'])
        totales(blank: true, nullable: true, attributes: [title: 'totales'])
        valor(size: 1..10, blank: true, nullable: true, attributes: [title: 'valor'])
        partidaObra(size: 1..10, blank: true, nullable: true, attributes: [title: 'partidaObra'])
        memoCertificacionPartida(size: 1..10, blank: true, nullable: true, attributes: [title: 'memoCertificacionPartida'])
        memoActualizacionPrefecto(size: 1..10, blank: true, nullable: true, attributes: [title: 'memoActualizacionPrefecto'])
        memoPartidaPresupuestaria(size: 1..10, blank: true, nullable: true, attributes: [title: 'memoPartidaPresupuestaria'])
        porcentajeAnticipo(size: 1..10, blank: true, nullable: true, attributes: [title: 'porcentajeAnticipo'])
        porcentajeReajuste(size: 1..10, blank: true, nullable: true, attributes: [title: 'porcentajeReajuste'])
        indiceCostosIndirectosObra(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosObra'])
        indiceCostosIndirectosMantenimiento(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosMantenimiento'])
        administracion(blank: true, nullable: true, attributes: [title: 'administracion'])
        indiceCostosIndirectosGarantias(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosGarantias'])
        indiceCostosIndirectosCostosFinancieros(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosCostosFinancieros'])
        indiceCostosIndirectosVehiculos(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosVehiculos'])
        indiceCostosIndirectosPromocion(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosPromocion'])
        indiceCostosIndirectosTimbresProvinciales(blank: true, nullable: true, attributes: [title: 'indiceCostosIndirectosTimbresProvinciales'])

        distanciaPesoEspecial(blank: true, nullable: true, attributes: [title: 'distanciaPesoEspecial'])
        distanciaVolumenMejoramiento(blank: true, nullable: true, attributes: [title: 'distanciaVolumenMejoramiento'])
        distanciaVolumenCarpetaAsfaltica(blank: true, nullable: true, attributes: [title: 'distanciaVolumenCarpetaAsfaltica'])
        barrio(size: 1..127, blank: true, nullable: true, attributes: [title: 'barrio'])


    }
}