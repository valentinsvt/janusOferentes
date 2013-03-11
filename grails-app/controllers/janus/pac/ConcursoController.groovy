package janus.pac

import janus.Administracion
import janus.Comunidad
import janus.Obra
import janus.Parroquia
import org.springframework.dao.DataIntegrityViolationException

class ConcursoController extends janus.seguridad.Shield {

    def buscadorService
    def obraService


    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def generaCodigo(concursoInstance) {
        def codigo = ""
//        def conc = Concurso.count()
        def sec = 1
        def lst = Concurso.list([sort: "id", order: "desc"])
//        println "________________________________________________________"
//        println lst
        if (lst.size() > 0) {
            def last = lst[1].codigo?.split("-")
//            println last
            if (last?.size() > 2) {
                def cod = last[2].toInteger()
                sec = cod + 1
//                println cod
//                println sec
            }
        }

        codigo += concursoInstance.pac.tipoProcedimiento.sigla + "-"
        codigo += "GADPP" + "-"
        codigo += sec + "-"
        codigo += new Date().format("yyyy")
//        println codigo
//        println "________________________________________________________"
        return codigo
    }

    def index() {
        redirect(action: "list", params: params)
    } //index

    def list() {
        def campos = ["descripcion": ["Descripción", "string"]]
        if (!params.sort){
            params.sort="id"
            params.order="desc"
        }
        return [concursoInstanceList: Concurso.list(params), params: params, campos: campos]
    } //list

    def nuevoProceso() {
        def pac = Pac.get(params.id)
        def admin = Administracion.findAllByFechaInicioLessThanEqualsAndFechaFinGreaterThanEquals(new Date(), new Date())
        def concurso = new Concurso()
        concurso.pac = pac
        if (admin.size() == 1) {
            concurso.administracion = admin[0]
        } else if (admin.size() > 1) {
            println "hay mas de una admin: " + admin
        } else {
            println "no hay admin q asignar"
        }
        concurso.costoBases = 0
        concurso.objeto = pac.descripcion

        if (concurso.save(flush: true)) {
            println "saved ok"
            def codigo = generaCodigo(concurso)
            concurso.codigo = codigo
            if (!concurso.save(flush: true)) {
                println "error al guarda el codigo " + codigo + " en el concurso " + concurso.id
                println concurso.errors
            }

            flash.clase = "alert-success"
            flash.message = "Proceso creado"
            redirect(action: 'list')
        } else {
            println "not saved"
            flash.clase = "alert-error"
            flash.message = "Ha ocurrido un error al crear el proceso"
            redirect(action: 'list')
        }
    }

    def show() {
        def concursoInstance = Concurso.get(params.id)
        if (!concursoInstance) {
            flash.clase = "alert-error"
            flash.message = "No se encontró Concurso con id " + params.id
            redirect(action: "list")
            return
        }
        def of = Oferta.findAllByConcurso(concursoInstance).size()
        [concursoInstance: concursoInstance, of: of]
    }

    def buscaPac() {
        def listaTitulos = ["Descripción", "Departamento", "Presupuesto"]
        def listaCampos = ["descripcion", "departamento", "presupuesto"]
        def funciones = [null, null]
        def url = g.createLink(action: "buscaPac", controller: "concurso")
//        def funcionJs = ""
        def funcionJs = "function(){"
        funcionJs += '$("#modal-pac").modal("hide");'
        funcionJs += 'var id=$(this).attr("regId");'
//        funcionJs += 'console.log(id);'
        funcionJs += 'var url = "' + createLink(controller: 'concurso', action: 'nuevoProceso') + '/"+id;'
        funcionJs += 'location.href = url;'
        funcionJs += '}'
        def numRegistros = 20
        def extras = ""
        if (!params.reporte) {
            def lista2 = buscadorService.buscar(Pac, "Pac", "excluyente", params, true, extras) /* Dominio, nombre del dominio , excluyente o incluyente ,params tal cual llegan de la interfaz del buscador, ignore case */
            lista2.pop()
            def lista = []
            lista2.each { l ->
                if (Concurso.countByPac(l) == 0) {
                    lista.add(l)
                }
            }

            render(view: '../tablaBuscadorColDer', model: [listaTitulos: listaTitulos, listaCampos: listaCampos, lista: lista, funciones: funciones, url: url, controller: "llamada", numRegistros: numRegistros, funcionJs: funcionJs])
        } else {
            println "entro reporte"
            /*De esto solo cambiar el dominio, el parametro tabla, el paramtero titulo y el tamaño de las columnas (anchos)*/
            session.dominio = Pac
            session.funciones = funciones
            def anchos = [20, 80] /*el ancho de las columnas en porcentajes... solo enteros*/
            redirect(controller: "reportes", action: "reporteBuscador", params: [listaCampos: listaCampos, listaTitulos: listaTitulos, tabla: "Pac", orden: params.orden, ordenado: params.ordenado, criterios: params.criterios, operadores: params.operadores, campos: params.campos, titulo: "Rubros", anchos: anchos, extras: extras, landscape: true])
        }
    }

    def form_ajax() {
        println "aqui "
        def campos = ["codigo": ["Código", "string"], "nombre": ["Nombre", "string"], "descripcion": ["Descripción", "string"], "oficioIngreso": ["Memo ingreso", "string"], "oficioSalida": ["Memo salida", "string"], "sitio": ["Sitio", "string"], "plazo": ["Plazo", "int"], "parroquia": ["Parroquia", "string"], "comunidad": ["Comunidad", "string"], "canton": ["Canton", "string"]]
        def concursoInstance = new Concurso(params)
        if (params.id) {
            concursoInstance = Concurso.get(params.id.toLong())
            if (!concursoInstance) {
                flash.clase = "alert-error"
                flash.message = "No se encontró Concurso con id " + params.id
                redirect(action: "list")
                return
            } //no existe el objeto
        } //es edit
        return [concursoInstance: concursoInstance,campos:campos]
    } //form_ajax



    def buscarObra() {

        def extraParr = ""
        def extraCom = ""
        if (params.campos instanceof java.lang.String) {
            if (params.campos == "parroquia") {
                def parrs = Parroquia.findAll("from Parroquia where nombre like '%${params.criterios.toUpperCase()}%'")
                params.criterios = ""
                parrs.eachWithIndex { p, i ->
                    extraParr += "" + p.id
                    if (i < parrs.size() - 1)
                        extraParr += ","
                }
                if (extraParr.size() < 1)
                    extraParr = "-1"
                params.campos = ""
                params.operadores = ""
            }
            if (params.campos == "comunidad") {
                def coms = Comunidad.findAll("from Comunidad where nombre like '%${params.criterios.toUpperCase()}%'")
                params.criterios = ""
                coms.eachWithIndex { p, i ->
                    extraCom += "" + p.id
                    if (i < coms.size() - 1)
                        extraCom += ","
                }
                if (extraCom.size() < 1)
                    extraCom = "-1"
                params.campos = ""
                params.operadores = ""
            }
        } else {
            def remove = []
            params.campos.eachWithIndex { p, i ->
                if (p == "comunidad") {
                    def coms = Comunidad.findAll("from Comunidad where nombre like '%${params.criterios[i].toUpperCase()}%'")

                    coms.eachWithIndex { c, j ->
                        extraCom += "" + c.id
                        if (j < coms.size() - 1)
                            extraCom += ","
                    }
                    if (extraCom.size() < 1)
                        extraCom = "-1"
                    remove.add(i)
                }
                if (p == "parroquia") {
                    def parrs = Parroquia.findAll("from Parroquia where nombre like '%${params.criterios[i].toUpperCase()}%'")

                    parrs.eachWithIndex { c, j ->
                        extraParr += "" + c.id
                        if (j < parrs.size() - 1)
                            extraParr += ","
                    }
                    if (extraParr.size() < 1)
                        extraParr = "-1"
                    remove.add(i)
                }
            }
            remove.each {
                params.criterios[it] = null
                params.campos[it] = null
                params.operadores[it] = null
            }
        }


        def extras = " "
        if (extraParr.size() > 1)
            extras += " and parroquia in (${extraParr})"
        if (extraCom.size() > 1)
            extras += " and comunidad in (${extraCom})"

        extras+=" and estado='R' "

        def parr = { p ->
            return p.parroquia?.nombre
        }
        def comu = { c ->
            return c.comunidad?.nombre
        }
        def listaTitulos = ["Código", "Nombre", "Descripción", "Fecha Reg.", "M. ingreso", "M. salida", "Sitio", "Plazo", "Parroquia", "Comunidad", "Inspector", "Revisor", "Responsable", "Estado Obra"]
        def listaCampos = ["codigo", "nombre", "descripcion", "fechaCreacionObra", "oficioIngreso", "oficioSalida", "sitio", "plazo", "parroquia", "comunidad", "inspector", "revisor", "responsable", "estadoObra"]
        def funciones = [null, null, null, ["format": ["dd/MM/yyyy hh:mm"]], null, null, null, null, ["closure": [parr, "&"]], ["closure": [comu, "&"]], null, null, null, null]
        def url = g.createLink(action: "buscarObra", controller: "obra")
        def funcionJs = "function(){"
        funcionJs += '$("#modal-busqueda").modal("hide");'
        funcionJs += '$("#obra_id").val($(this).attr("regId"));'
        funcionJs += 'cargarDatos();'
        funcionJs += '}'
        def numRegistros = 20

        if (!params.reporte) {
            def lista = buscadorService.buscar(Obra, "Obra", "excluyente", params, true, extras) /* Dominio, nombre del dominio , excluyente o incluyente ,params tal cual llegan de la interfaz del buscador, ignore case */
            lista.pop()
            render(view: '../tablaBuscador', model: [listaTitulos: listaTitulos, listaCampos: listaCampos, lista: lista, funciones: funciones, url: url, controller: "llamada", numRegistros: numRegistros, funcionJs: funcionJs, width: 1800, paginas: 12])
        } else {
//            println "entro reporte"
            /*De esto solo cambiar el dominio, el parametro tabla, el paramtero titulo y el tamaño de las columnas (anchos)*/
            session.dominio = Obra
            session.funciones = funciones
            def anchos = [7, 10, 7, 7, 7, 7, 7, 4, 7, 7, 7, 7, 7, 7] /*el ancho de las columnas en porcentajes... solo enteros*/
            redirect(controller: "reportes", action: "reporteBuscador", params: [listaCampos: listaCampos, listaTitulos: listaTitulos, tabla: "Obra", orden: params.orden, ordenado: params.ordenado, criterios: params.criterios, operadores: params.operadores, campos: params.campos, titulo: "Obras", anchos: anchos, extras: extras, landscape: true])
        }
    }

    def datosObra(){
        def obra = Obra.get(params.obra)
        def monto = obraService.montoObra(obra)
        def plazo = obra.plazoEjecucionMeses*30+obra.plazoEjecucionDias
        render ""+obra.codigo+"&&"+obra.nombre+"&&"+plazo+"&&"+monto
    }



    def save() {
        println "save concurso "+params
        def concursoInstance
        if (params.id) {
            concursoInstance = Concurso.get(params.id)
            if (!concursoInstance) {
                flash.clase = "alert-error"
                flash.message = "No se encontró Concurso con id " + params.id
                redirect(action: 'list')
                return
            }//no existe el objeto
            concursoInstance.properties = params
        }//es edit
        else {
            concursoInstance = new Concurso(params)
        } //es create


        if (!concursoInstance.save(flush: true)) {
            println "errores concurso "+concursoInstance.errors
            flash.clase = "alert-error"
            def str = "<h4>No se pudo guardar Concurso " + (concursoInstance.id ? concursoInstance.id : "") + "</h4>"

            str += "<ul>"
            concursoInstance.errors.allErrors.each { err ->
                def msg = err.defaultMessage
                err.arguments.eachWithIndex { arg, i ->
                    msg = msg.replaceAll("\\{" + i + "}", arg.toString())
                }
                str += "<li>" + msg + "</li>"
            }
            str += "</ul>"

            flash.message = str
            redirect(action: 'list')
            return
        }

        if (!concursoInstance.codigo) {
            def codigo = generaCodigo(concursoInstance)
            concursoInstance.codigo = codigo
            if (!concursoInstance.save(flush: true)) {
                println "error al guarda el codigo " + codigo + " en el concurso " + concursoInstance.id
                println concursoInstance.errors
            }
        }

        if (params.id) {
            flash.clase = "alert-success"
            flash.message = "Se ha actualizado correctamente Concurso " + concursoInstance.id
        } else {
            flash.clase = "alert-success"
            flash.message = "Se ha creado correctamente Concurso " + concursoInstance.id
        }
        redirect(action: 'list')
    } //save

    def show_ajax() {
        def concursoInstance = Concurso.get(params.id)
        if (!concursoInstance) {
            flash.clase = "alert-error"
            flash.message = "No se encontró Concurso con id " + params.id
            redirect(action: "list")
            return
        }
        [concursoInstance: concursoInstance]
    } //show

    def delete() {
        def concursoInstance = Concurso.get(params.id)
        if (!concursoInstance) {
            flash.clase = "alert-error"
            flash.message = "No se encontró Concurso con id " + params.id
            redirect(action: "list")
            return
        }

        try {
            concursoInstance.delete(flush: true)
            flash.clase = "alert-success"
            flash.message = "Se ha eliminado correctamente Concurso " + concursoInstance.id
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.clase = "alert-error"
            flash.message = "No se pudo eliminar Concurso " + (concursoInstance.id ? concursoInstance.id : "")
            redirect(action: "list")
        }
    } //delete

    def registrar() {
        println "registrar " + params
        def con = Concurso.get(params.id)
        con.estado = params.estado
        if (!con.save(flush: true))
            render "error"
        else
            render "ok"

    }


} //fin controller
