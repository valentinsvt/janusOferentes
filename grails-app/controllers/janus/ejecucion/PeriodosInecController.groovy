package janus.ejecucion

import org.springframework.dao.DataIntegrityViolationException

class PeriodosInecController extends janus.seguridad.Shield {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def migracionService

    def index() {
        redirect(action: "list", params: params)
    } //index

    def list() {
        [periodosInecInstanceList: PeriodosInec.list(params), params: params]
    } //list

    def form_ajax() {
        def periodosInecInstance = new PeriodosInec(params)
        if(params.id) {
            periodosInecInstance = PeriodosInec.get(params.id)
            if(!periodosInecInstance) {
                flash.clase = "alert-error"
                flash.message =  "No se encontró Periodos Inec con id " + params.id
                redirect(action:  "list")
                return
            } //no existe el objeto
        } //es edit
        return [periodosInecInstance: periodosInecInstance]
    } //form_ajax

    def save() {
        println "save3 "+params
        def periodosInecInstance
        if (params.fechaInicio){
            params.fechaInicio=new Date().parse("dd-MM-yyyy",params.fechaInicio)
        }
        if (params.fechaFin){
            params.fechaFin=new Date().parse("dd-MM-yyyy",params.fechaFin)
        }
        if(params.id) {
            periodosInecInstance = PeriodosInec.get(params.id)
            if(!periodosInecInstance) {
                flash.clase = "alert-error"
                flash.message = "No se encontró Periodos Inec con id " + params.id
                redirect(action: 'list')
                return
            }//no existe el objeto
            periodosInecInstance.properties = params
        }//es edit
        else {
            periodosInecInstance = new PeriodosInec(params)

        } //es create

        if (!periodosInecInstance.save(flush: true)) {
            flash.clase = "alert-error"
            def str = "<h4>No se pudo guardar Periodos Inec " + (periodosInecInstance.id ? periodosInecInstance.id : "") + "</h4>"

            str += "<ul>"
            periodosInecInstance.errors.allErrors.each { err ->
                def msg = err.defaultMessage
                err.arguments.eachWithIndex {  arg, i ->
                    msg = msg.replaceAll("\\{" + i + "}", arg.toString())
                }
                str += "<li>" + msg + "</li>"
            }
            str += "</ul>"

            flash.message = str
            redirect(action: 'list')
            return
        }

        if(params.id) {
            flash.clase = "alert-success"
            flash.message = "Se ha actualizado correctamente Periodos Inec " + periodosInecInstance.id
        } else {
            flash.clase = "alert-success"
            flash.message = "Se ha creado correctamente Periodos Inec " + periodosInecInstance.id
        }
        redirect(action: 'list')
    } //save

    def show_ajax() {
        def periodosInecInstance = PeriodosInec.get(params.id)
        if (!periodosInecInstance) {
            flash.clase = "alert-error"
            flash.message =  "No se encontró Periodos Inec con id " + params.id
            redirect(action: "list")
            return
        }
        [periodosInecInstance: periodosInecInstance]
    } //show

    def delete() {
        def periodosInecInstance = PeriodosInec.get(params.id)
        if (!periodosInecInstance) {
            flash.clase = "alert-error"
            flash.message =  "No se encontró Periodos Inec con id " + params.id
            redirect(action: "list")
            return
        }

        try {
            periodosInecInstance.delete(flush: true)
            flash.clase = "alert-success"
            flash.message =  "Se ha eliminado correctamente Periodos Inec " + periodosInecInstance.id
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.clase = "alert-error"
            flash.message =  "No se pudo eliminar Periodos Inec " + (periodosInecInstance.id ? periodosInecInstance.id : "")
            redirect(action: "list")
        }
    } //delete

    def generarIndices(){
        render migracionService.insertRandomIndices()
    }

} //fin controller
