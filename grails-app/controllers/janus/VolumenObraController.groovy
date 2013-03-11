package janus

class VolumenObraController extends janus.seguridad.Shield{
    def buscadorService
    def preciosService
    def volObra(){

        def obra = Obra.get(params.id)
        def volumenes = VolumenesObra.findAllByObra(obra)

        def campos = ["codigo": ["Código", "string"], "nombre": ["Descripción", "string"]]

        [obra:obra,volumenes:volumenes,campos:campos]



    }
    def buscarRubroCodigo(){
        def rubro = Item.findByCodigoAndTipoItem(params.codigo?.trim(),TipoItem.get(2))
        if (rubro){
            render ""+rubro.id+"&&"+rubro.tipoLista?.id+"&&"+rubro.nombre+"&&"+rubro.unidad?.codigo
            return
        } else{
            render "-1"
            return
        }
    }


    def addItem(){
//        println "addItem "+params
        def obra= Obra.get(params.obra)
        def rubro = Item.get(params.rubro)
        def volumen
        if (params.id)
            volumen=VolumenesObra.get(params.id)
        else{
            volumen=VolumenesObra.findByObraAndItem(obra,rubro)
            if(!volumen)
                volumen=new VolumenesObra()
        }
        volumen.cantidad=params.cantidad.toDouble()
        volumen.orden=params.orden.toInteger()
        volumen.subPresupuesto=SubPresupuesto.get(params.sub)
        volumen.obra=obra
        volumen.item=rubro
        if (!volumen.save(flush: true)){
            println "error volumen obra "+volumen.errors
            render "error"
        }else{
            preciosService.actualizaOrden(volumen,"insert")
            redirect(action: "tabla",params: [obra:obra.id,sub:volumen.subPresupuesto.id])
        }
    }

    def tabla(){
//        println "paramms "+params
        def obra = Obra.get(params.obra)
        def detalle
        if (params.sub && params.sub !="null")
            detalle= VolumenesObra.findAllByObraAndSubPresupuesto(obra,SubPresupuesto.get(params.sub),[sort:"orden"])
        else
            detalle= VolumenesObra.findAllByObra(obra,[sort:"orden"])
        def subPres = VolumenesObra.findAllByObra(obra,[sort:"orden"]).subPresupuesto.unique()

        def precios = [:]
        def fecha = obra.fechaPreciosRubros
        def dsps = obra.distanciaPeso
        def dsvl = obra.distanciaVolumen
        def lugar = obra.lugar
        def prch = 0
        def prvl = 0

//        /*Todo ver como mismo es esta suma*/
        def indirecto = obra.totales/100
//        println "indirecto "+indirecto
        preciosService.ac_rbroObra(obra.id)

        detalle.each{

            def res = preciosService.presioUnitarioVolumenObra("sum(parcial)+sum(parcial_t) precio ",obra.id,it.item.id)
//            println "res "+res+" "+it.item.id+"  "+obra.id
//            println "r->" + (res["precio"][0]+res["precio"][0]*indirecto)+"   <<<>>> "+res
            precios.put(it.id.toString(),(res["precio"][0]+res["precio"][0]*indirecto).toDouble().round(2))
        }
//
//        println "precios "+precios


        [detalle:detalle,precios:precios,subPres:subPres,subPre:params.sub,obra: obra,precioVol:prch,precioChof:prvl,indirectos:indirecto*100]

    }

    def eliminarRubro(){
        def vol = VolumenesObra.get(params.id)
        def obra = vol.obra
        def orden = vol.orden
        preciosService.actualizaOrden(vol,"delete")
        vol.delete()
        redirect(action: "tabla",params: [obra:obra.id])

    }

    def buscaRubro() {

        def listaTitulos = ["Código", "Descripción","Unidad"]
        def listaCampos = ["codigo", "nombre","unidad"]
        def funciones = [null, null]
        def url = g.createLink(action: "buscaRubro", controller: "rubro")
        def funcionJs = "function(){"
        funcionJs += '$("#modal-rubro").modal("hide");'
        funcionJs += '$("#item_id").val($(this).attr("regId"));$("#item_codigo").val($(this).attr("prop_codigo"));$("#item_nombre").val($(this).attr("prop_nombre"))'
        funcionJs += '}'
        def numRegistros = 20
        def extras = " and tipoItem = 2"
        if (!params.reporte) {
            def lista = buscadorService.buscar(Item, "Item", "excluyente", params, true, extras) /* Dominio, nombre del dominio , excluyente o incluyente ,params tal cual llegan de la interfaz del buscador, ignore case */
            lista.pop()
            render(view: '../tablaBuscadorColDer', model: [listaTitulos: listaTitulos, listaCampos: listaCampos, lista: lista, funciones: funciones, url: url, controller: "llamada", numRegistros: numRegistros, funcionJs: funcionJs])
        } else {
            println "entro reporte"
            /*De esto solo cambiar el dominio, el parametro tabla, el paramtero titulo y el tamaño de las columnas (anchos)*/
            session.dominio = Item
            session.funciones = funciones
            def anchos = [20, 80] /*el ancho de las columnas en porcentajes... solo enteros*/
            redirect(controller: "reportes", action: "reporteBuscador", params: [listaCampos: listaCampos, listaTitulos: listaTitulos, tabla: "Item", orden: params.orden, ordenado: params.ordenado, criterios: params.criterios, operadores: params.operadores, campos: params.campos, titulo: "Rubros", anchos: anchos, extras: extras, landscape: true])
        }
    }
}
