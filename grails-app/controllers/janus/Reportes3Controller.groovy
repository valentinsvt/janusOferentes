package janus

import com.lowagie.text.Document
import com.lowagie.text.Element
import com.lowagie.text.Font
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import jxl.write.Number
import jxl.write.WritableCellFormat
import jxl.write.WritableFont
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import jxl.LabelCell.*
import jxl.*

import java.awt.Color

class Reportes3Controller {

    def preciosService
    def dbConnectionService


    def meses = ['', "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]

    private String printFecha(Date fecha) {
        if (fecha) {
            return (fecha.format("dd")+' de '+ meses[fecha.format("MM").toInteger()]+' de '+fecha.format("yyyy")).toLowerCase()
        } else {
            return "Error: no hay fecha que mostrar"
        }
    }


    def index() { }

    def test() {
        return [params: params]
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    def imprimirTablaSub(){
//        println "imprimir tabla sub "+params
        def obra = Obra.get(params.obra)
        def detalle
        def subPre
        def orden

        def fechaHoy = printFecha(new Date())

        def oferente = Persona.get(params.oferente)

        def sql = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

        def cn = dbConnectionService.getConnection()

        def conc = cn.rows(sql.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)


        def fechaOferta = printFecha(concurso?.fechaLimiteEntregaOfertas);

        def firma = Persona.get(params.oferente).firma

//        if (params.sub && params.sub != "-1"){
//
//            detalle= VolumenesObra.findAllByObraAndSubPresupuesto(obra,SubPresupuesto.get(params.sub),[sort:"orden"])
//        }
//
//        else {
//
//
//            detalle= VolumenesObra.findAllByObra(obra,[sort:"orden"])
//
//
//        }

        if (params.ord == '1') {
            orden = 'asc'
        } else {
            orden = 'desc'
        }


        preciosService.ac_rbroObra(obra.id)
        if (params.sub && params.sub != "-1") {
            detalle = preciosService.rbro_pcun_v5(obra.id, params.sub, orden)
              } else {
            detalle = preciosService.rbro_pcun_v4(obra.id, orden)
        }


        def subPres = VolumenesObra.findAllByObra(obra,[sort:"orden"]).subPresupuesto.unique()

        def precios = [:]

        if (params.sub != '-1'){

            subPre= SubPresupuesto.get(params.sub).descripcion

        }else {

            subPre= -1

        }

        def indirecto = obra.totales/100


//        detalle.each{
//
//            def res = preciosService.presioUnitarioVolumenObra("sum(parcial)+sum(parcial_t) precio ",it.item.id,obra?.id)
//
//            def precio = 0
//            if(res["precio"][0]!=null && res["precio"][0]!="null" )
//                precio = res["precio"][0]
//            precios.put(it.id.toString(),(precio+precio*indirecto).toDouble().round(2))
//        }

        [detalle:detalle,precios:precios,subPres:subPres,subPre:subPre,obra: obra,indirectos:indirecto*100, oferente: oferente, fechaHoy: fechaHoy, concurso: concurso, fechaOferta: fechaOferta, firma: firma]

    }



//    def imprimirRubroVolObra() {
//        println "----->>>>" + params
////        def rubro = Item.get(params.id)
//        def obra = Obra.get(params.obra)
//
//        def fecha1
//        def fecha2
//
//        if(params.fecha){
//
//            fecha1 = new Date().parse("dd-MM-yyyy", params.fecha)
//        }else {
//
//        }
//
//        if(params.fechaSalida){
//
//            fecha2 = new Date().parse("dd-MM-yyyy", params.fechaSalida)
//        }else {
//        }
//
//
////        def fechaSalida = printFecha(fecha2)
////        def fecha = printFecha(fecha1)
//
//        def fechaPal = printFecha(new Date());
//
//
//        def vol1 = VolumenesObra.get(params.id)
//        def rubro = Item.get(vol1.item.id)
//
//
//        def indi = obra.totales
//
//        try {
//            indi = indi.toDouble()
//        } catch (e) {
//            println "error parse " + e
//            indi = 21.5
//        }
//
//        preciosService.ac_rbroObra(obra.id)
//        def res = preciosService.precioUnitarioVolumenObraAsc("*", obra.id, rubro.id)
//
//        def tablaHer = '<table class=""> '
//        def tablaMano = '<table class=""> '
//        def tablaMat = '<table class=""> '
//        def tablaTrans = '<table class=""> '
//        def tablaIndi = '<table class="marginTop"> '
//
//        def tablaMat2 = '<table class="marginTop"> '
//        def tablaTrans2 = '<table class="marginTop"> '
//
//
//        def total = 0, totalHer = 0, totalMan = 0, totalMat = 0
//        def band = 0
//        def bandMat = 0
//        def bandTrans = params.desglose
//
//        tablaHer += "<thead><tr><th colspan='7' class='tituloHeader'>EQUIPOS</th></tr><tr><th colspan='7' class='theader'></th></tr><tr><th style='width: 80px' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>CANTIDAD</th><th style='width:70px'>TARIFA(\$/H)</th><th>COSTO(\$)</th><th>RENDIMIENTO</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='7' class='theaderup'></th></tr> </thead><tbody>"
//        tablaMano += "<thead><tr><th colspan='7' class='tituloHeader'>MANO DE OBRA</th></tr><tr><th colspan='7' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>CANTIDAD</th><th style='width:70px'>JORNAL(\$/H)</th><th>COSTO(\$)</th><th>RENDIMIENTO</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='7' class='theaderup'></th></tr> </thead><tbody>"
//
//        if(params.desglose == '1'){
//            tablaMat += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"
//
//        } else {
//            tablaMat += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES INCLUIDO TRANSPORTE</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"
//
//
//        }
//        tablaTrans += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"
//        tablaTrans2 += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"
//        tablaMat2 += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"
//
//
////        println "rends "+rendimientos
//
////        println "res "+res
//
//        res.each { r ->
//            if (r["grpocdgo"] == 3) {
//                tablaHer += "<tr>"
//                tablaHer += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
//                tablaHer += "<td>" + r["itemnmbr"] + "</td>"
//                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                totalHer += r["parcial"]
//                tablaHer += "</tr>"
//            }
//            if (r["grpocdgo"] == 2) {
//                tablaMano += "<tr>"
//                tablaMano += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
//                tablaMano += "<td>" + r["itemnmbr"] + "</td>"
//                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                totalMan += r["parcial"]
//                tablaMano += "</tr>"
//            }
//            if (r["grpocdgo"] == 1) {
//                bandMat=1
//                if (params.desglose == '1') {
//                    tablaMat += "<tr>"
//                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
//                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["parcial"] + "</td>"
//                    totalMat += r["parcial"]
//                    tablaMat += "</tr>"
//                }
////                if (params.desglose != '1') {
//                else{
//                    tablaMat += "<tr>"
//                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
//                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["rbpcpcun"] + r["parcial_t"] / r["rbrocntd"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["parcial"] + r["parcial_t"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    totalMat += (r["parcial"] + r["parcial_t"])
//                    tablaMat += "</tr>"
//                }
//            }
//            if (r["grpocdgo"] == 1 && params.desglose == "1") {
//
//                tablaTrans += "<tr>"
//                tablaTrans += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
//                tablaTrans += "<td>" + r["itemnmbr"] + "</td>"
//                if(r["tplscdgo"].trim() =='P' || r["tplscdgo"].trim() =='P1' ){
//                    tablaTrans += "<td style='width: 50px;text-align: right'>" + "ton-km" + "</td>"
//                } else{
//
//                    if(r["tplscdgo"].trim() =='V' || r["tplscdgo"].trim() =='V1' || r["tplscdgo"].trim() =='V2'){
//
//                        tablaTrans += "<td style='width: 50px;text-align: right'>" + "m3-km" + "</td>"
//                    }
//                    else {
//
//                        tablaTrans += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
//                    }
//
//                }
////                tablaTrans += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["itempeso"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["distancia"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
////                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"] / (r["itempeso"] * r["rbrocntd"] * r["distancia"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["tarifa"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                total += r["parcial_t"]
//                tablaTrans += "</tr>"
//            }
//            else {
//
//            }
//
//        }
//        tablaTrans += "<tr><td></td><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: total, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
//        tablaTrans += "</tbody></table>"
//        tablaHer += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalHer, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
//        tablaHer += "</tbody></table>"
//        tablaMano += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMan, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
//        tablaMano += "</tbody></table>"
//        tablaMat += "<tr><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMat, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
//        tablaMat += "</tbody></table>"
//        tablaTrans2 += "</tbody></table>"
//        tablaMat2 += "</tbody></table>"
//
//
//        def totalRubro = total + totalHer + totalMan + totalMat
//        totalRubro = totalRubro.toDouble().round(5)
//
//        band = total
//
//        def totalIndi = totalRubro * indi / 100
//        totalIndi = totalIndi.toDouble().round(5)
//        tablaIndi += "<thead><tr><th class='tituloHeader'>COSTOS INDIRECTOS</th></tr><tr><th colspan='3' class='theader'></th></tr><tr><th style='width:550px' class='padTopBot'>DESCRIPCIÓN</th><th style='width:130px'>PORCENTAJE</th><th>VALOR</th></tr>    <tr><th colspan='3' class='theaderup'></th></tr>  </thead>"
//        tablaIndi += "<tbody><tr><td>COSTOS INDIRECTOS</td><td style='text-align:center'>${indi}%</td><td style='text-align:right'>${g.formatNumber(number: totalIndi, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5")}</td></tr></tbody>"
//        tablaIndi += "</table>"
//
//
//        if (total == 0)
//            tablaTrans = ""
//        if (totalHer == 0)
//            tablaHer = ""
//        if (totalMan == 0)
//            tablaMano = ""
//        if (totalMat == 0)
//            tablaMat = ""
////        println "fin reporte rubro"
//        [rubro: rubro, fechaPrecios: fecha1, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band:  band, tablaMat2: tablaMat2, bandMat: bandMat,
//                bandTrans: bandTrans, tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat, tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, fechPal: fechaPal, fechaSalida: fecha2, obra: obra]
//
//
//    }





    def imprimirRubroVolObra(){

//        println("entro")

//        println "imprimir rubro "+params

        def rubro =Item.get(params.id)
        def obra=Obra.get(params.obra)

        def fechaOferta = printFecha(obra?.fechaOferta)

        def oferente = Persona.get(params.oferente)



        def obraOferente = Obra.findByOferente(oferente)

        def sql = "SELECT * FROM cncr WHERE obra__id=${obraOferente?.idJanus}"

//        println("sql:" + sql)

        def cn = dbConnectionService.getConnection()

        def conc = cn.rows(sql.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)


        def fechaEntregaOferta = printFecha(concurso?.fechaLimiteEntregaOfertas)

        def firma = Persona.get(params.oferente).firma

        def indi = obra.totales
        try{
            indi=indi.toDouble()
        } catch (e){
            println "error parse "+e
            indi=21.5
        }

//        preciosService.ac_rbroObra(Obra.findByOferente(oferente).id)
//
//        def res = preciosService.presioUnitarioVolumenObra("*",rubro.id,obra?.id)


        def parametros = ""+rubro.id+","+oferente.id
        preciosService.ac_rbroV2(rubro?.id, oferente?.id)
        def res = preciosService.rb_preciosV3(parametros)


        def tablaHer = '<table class=""> '
        def tablaMano = '<table class=""> '
        def tablaMat = '<table class=""> '
        def tablaTrans = '<table class=""> '
        def tablaIndi = '<table class="marginTop"> '

        def tablaMat2 = '<table class="marginTop"> '
        def tablaTrans2 = '<table class="marginTop"> '


        def total = 0, totalHer = 0, totalMan = 0, totalMat = 0
        def band = 0
        def bandMat = 0
        def bandTrans = params.desglose

        tablaHer += "<thead><tr><th colspan='7' class='tituloHeader'>EQUIPOS</th></tr><tr><th colspan='7' class='theader'></th></tr><tr><th style='width: 80px' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>CANTIDAD</th><th style='width:70px'>TARIFA(\$/H)</th><th>COSTO(\$)</th><th>RENDIMIENTO</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='7' class='theaderup'></th></tr> </thead><tbody>"
        tablaMano += "<thead><tr><th colspan='7' class='tituloHeader'>MANO DE OBRA</th></tr><tr><th colspan='7' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>CANTIDAD</th><th style='width:70px'>JORNAL(\$/H)</th><th>COSTO(\$)</th><th>RENDIMIENTO</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='7' class='theaderup'></th></tr> </thead><tbody>"
        tablaMat += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES INCLUYE TRANSPORTE</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"
        tablaTrans += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"
        tablaTrans2 += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"
        tablaMat2 += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES INCLUYE TRANSPORTE</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"

        res.each { r ->
            if (r["grpocdgo"] == 3) {
                tablaHer += "<tr>"
                tablaHer += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaHer += "<td>" + r["itemnmbr"] + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalHer += r["parcial"]
                tablaHer += "</tr>"
            }
            if (r["grpocdgo"] == 2) {
                tablaMano += "<tr>"
                tablaMano += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaMano += "<td>" + r["itemnmbr"] + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalMan += r["parcial"]
                tablaMano += "</tr>"
            }
            if (r["grpocdgo"] == 1) {
                bandMat=1
                if (params.desglose == '1') {
                    tablaMat += "<tr>"
                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["parcial"] + "</td>"
                    totalMat += r["parcial"]
                    tablaMat += "</tr>"
                }
//                if (params.desglose != '1') {
                else{
                    tablaMat += "<tr>"
                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["rbpcpcun"] + r["parcial_t"] / r["rbrocntd"]), format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["parcial"] + r["parcial_t"]), format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    totalMat += (r["parcial"] + r["parcial_t"])
                    tablaMat += "</tr>"
                }
            }
            if (r["grpocdgo"] == 1 && params.desglose == "1") {

                tablaTrans += "<tr>"
                tablaTrans += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaTrans += "<td>" + r["itemnmbr"] + "</td>"
//                if(r["tplscdgo"].trim() =='P' || r["tplscdgo"].trim() =='P1' ){
//                    tablaTrans += "<td style='width: 50px;text-align: right'>" + "ton-km" + "</td>"
//                } else{
//
//                    if(r["tplscdgo"].trim() =='V' || r["tplscdgo"].trim() =='V1' || r["tplscdgo"].trim() =='V2'){
//
//                        tablaTrans += "<td style='width: 50px;text-align: right'>" + "m3-km" + "</td>"
//                    }
//                    else {

                        tablaTrans += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
//                    }
//
//                }
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["itempeso"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["distancia"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"] / (r["itempeso"] * r["rbrocntd"] * r["distancia"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["tarifa"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                total += r["parcial_t"]
                tablaTrans += "</tr>"
            }
            else {

            }

        }
        tablaTrans += "<tr><td></td><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: total, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaTrans += "</tbody></table>"
        tablaHer += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalHer, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaHer += "</tbody></table>"
        tablaMano += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMan, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMano += "</tbody></table>"
        tablaMat += "<tr><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMat, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMat += "</tbody></table>"
        tablaTrans2 += "</tbody></table>"
        tablaMat2 += "</tbody></table>"


        def totalRubro = total + totalHer + totalMan + totalMat
        totalRubro = totalRubro.toDouble().round(5)

        band = total

        def totalIndi = totalRubro * indi / 100
        totalIndi = totalIndi.toDouble().round(5)
        tablaIndi += "<thead><tr><th class='tituloHeader'>COSTOS INDIRECTOS</th></tr><tr><th colspan='3' class='theader'></th></tr><tr><th style='width:550px' class='padTopBot'>DESCRIPCIÓN</th><th style='width:130px'>PORCENTAJE</th><th>VALOR</th></tr>    <tr><th colspan='3' class='theaderup'></th></tr>  </thead>"
        tablaIndi += "<tbody><tr><td>COSTOS INDIRECTOS</td><td style='text-align:center'>${indi}%</td><td style='text-align:right'>${g.formatNumber(number: totalIndi, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5")}</td></tr></tbody>"
        tablaIndi += "</table>"


        if (total == 0)
            tablaTrans = ""
        if (totalHer == 0)
            tablaHer = ""
        if (totalMan == 0)
            tablaMano = ""
        if (totalMat == 0)
            tablaMat = ""

        println(tablaMat)

//        println "fin reporte rubro"
        [rubro: rubro, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band:  band, tablaMat2: tablaMat2, bandMat: bandMat,
                bandTrans: bandTrans, tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat, tablaIndi: tablaIndi, totalRubro: totalRubro,
                totalIndi: totalIndi, obra: obra, oferente: oferente,
                fechaOferta: fechaOferta, concurso: concurso, fechaEntregaOferta: fechaEntregaOferta, firma: firma]



    }

    def imprimirRubroExcel(){
//        println "imprimir rubro  excel "+params
        def rubro = Item.get(params.id)
        def indi = params.indi
        def obra = Obra.get(params.obra)
        def obra2 = Obra.get(params.obra.toLong())

//        println(obra)

        def oferente = Persona.get(params.oferente)

        def obraOferente = Obra.findByOferente(oferente)

//        def sql = "SELECT * FROM cncr WHERE obra__id=${obraOferente?.idJanus}"
        def sql = "SELECT * FROM cncr WHERE obra__id=${obra2?.idJanus}"

//        println("sql:" + sql)

        def cn = dbConnectionService.getConnection()

        def conc = cn.rows(sql.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id
        }
        def concurso = janus.pac.Concurso.get(cncrId)

//        println("concurso" + concurso)


        try{
            indi=indi.toDouble()
        } catch (e){
            println "error parse "+e
            indi=21.5
        }



//        def parametros = ""+rubro.id+","+params.oferente
//        preciosService.ac_rbroObra(params.id)
//        def res = preciosService.rb_precios(parametros,"")


        def parametros = ""+rubro.id+","+oferente.id
        preciosService.ac_rbroV2(rubro?.id, oferente?.id)
        def res = preciosService.rb_preciosV3(parametros)



        WorkbookSettings workbookSettings = new WorkbookSettings()
        workbookSettings.locale = Locale.default
        def file = File.createTempFile('myExcelDocument', '.xls')
        file.deleteOnExit()
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings)
        WritableFont font = new WritableFont(WritableFont.ARIAL, 12)
        WritableCellFormat formatXls = new WritableCellFormat(font)
        def row = 0
        WritableSheet sheet = workbook.createSheet('MySheet', 0)
        WritableFont times16font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
        WritableCellFormat times16format = new WritableCellFormat(times16font);
        WritableFont times10Font = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, true);
        WritableCellFormat times10 = new WritableCellFormat(times10Font);
        sheet.setColumnView(0, 20)
        sheet.setColumnView(1, 50)
        sheet.setColumnView(2, 15)
        sheet.setColumnView(3, 15)
        sheet.setColumnView(4, 15)
        sheet.setColumnView(5, 15)
        sheet.setColumnView(6, 15)

        def label = new Label(1, 2, "NOMBRE DEL OFERENTE: " + oferente?.nombre.toUpperCase() + " " + oferente?.apellido.toUpperCase(), times16format); sheet.addCell(label);

        if (concurso != null){

            label = new Label(1, 3, "PROCESO:" + concurso?.codigo.toUpperCase(), times16format); sheet.addCell(label);
        } else {
            label = new Label(1, 3, "PROCESO:", times16format); sheet.addCell(label);

        }

        label = new Label(1, 4, "Análisis de precios unitarios".toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(1, 6, "PROYECTO: " + obra?.nombre.toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(1, 7, "RUBRO: " + rubro?.nombre, times16format); sheet.addCell(label);
        label = new Label(1, 8, "UNIDAD:" + rubro?.unidad?.codigo, times16format); sheet.addCell(label);

        def fila = 9
        label = new Label(0, fila,"Herramientas", times16format); sheet.addCell(label);
        sheet.mergeCells(0,fila, 1, fila)
        fila++
        def number
        def totalHer=0
        def totalMan=0
        def totalMat=0
        def total = 0
        def band=0
        def rowsTrans=[]
        res.each {r->
//            println "r "+r
            if(r["grpocdgo"]==3){
                if(band==0){
                    label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
                    label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
                    label = new Label(2, fila, "Cantidad", times16format); sheet.addCell(label);
                    label = new Label(3, fila, "Tarifa(\$/hora)", times16format); sheet.addCell(label);
                    label = new Label(4, fila, "Costo(\$)", times16format); sheet.addCell(label);
                    label = new Label(5, fila, "Rendimiento", times16format); sheet.addCell(label);
                    label = new Label(6, fila, "C.Total(\$)", times16format); sheet.addCell(label);
                    fila++
                }
                band=1
                label = new Label(0, fila,r["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila,r["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, r["rbrocntd"]);sheet.addCell(number);
                number = new Number(3, fila, r["rbpcpcun"]);sheet.addCell(number);
                number = new Number(4, fila, r["rbpcpcun"]*r["rbrocntd"]);sheet.addCell(number);
                number = new Number(5, fila, r["rndm"]);sheet.addCell(number);
                number = new Number(6, fila, r["parcial"]);sheet.addCell(number);
                totalHer+=r["parcial"]
                fila++
            }
            if(r["grpocdgo"]==2){
                if(band==1){
                    label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
                    number = new Number(6, fila, totalHer);sheet.addCell(number);
                    fila++
                }
                if(band!=2){
                    fila++
                    label = new Label(0, fila,"Mano de obra", times16format); sheet.addCell(label);
                    sheet.mergeCells(0,fila, 1, fila)
                    fila++
                    label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
                    label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
                    label = new Label(2, fila, "Cantidad", times16format); sheet.addCell(label);
                    label = new Label(3, fila, "Jornal(\$/hora)", times16format); sheet.addCell(label);
                    label = new Label(4, fila, "Costo(\$)", times16format); sheet.addCell(label);
                    label = new Label(5, fila, "Rendimiento", times16format); sheet.addCell(label);
                    label = new Label(6, fila, "C.Total(\$)", times16format); sheet.addCell(label);
                    fila++
                }
                band=2
                label = new Label(0, fila,r["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila,r["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, r["rbrocntd"]);sheet.addCell(number);
                number = new Number(3, fila, r["rbpcpcun"]);sheet.addCell(number);
                number = new Number(4, fila, r["rbpcpcun"]*r["rbrocntd"]);sheet.addCell(number);
                number = new Number(5, fila, r["rndm"]);sheet.addCell(number);
                number = new Number(6, fila, r["parcial"]);sheet.addCell(number);
                totalMan+=r["parcial"]
                fila++
            }
            if(r["grpocdgo"]==1){
                if(band==2){
                    label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
                    number = new Number(6, fila, totalMan);sheet.addCell(number);
                    fila++
                }
                if(band!=3){
                    fila++
                    label = new Label(0, fila,"Materiales", times16format); sheet.addCell(label);
                    sheet.mergeCells(0,fila, 1, fila)
                    fila++
                    label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
                    label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
                    label = new Label(2, fila, "Cantidad", times16format); sheet.addCell(label);
                    label = new Label(3, fila, "Unitario", times16format); sheet.addCell(label);
                    label = new Label(4, fila, "Unidad", times16format); sheet.addCell(label);
                    label = new Label(6, fila, "C.Total(\$)", times16format); sheet.addCell(label);
                    fila++
                }
                band=3
                label = new Label(0, fila,r["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila,r["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, r["rbrocntd"]);sheet.addCell(number);
                number = new Number(3, fila, r["rbpcpcun"]);sheet.addCell(number);
                label = new Label(4, fila,r["unddcdgo"], times10); sheet.addCell(label);
                number = new Number(6, fila, r["parcial"]);sheet.addCell(number);
                totalMat+=r["parcial"]
                fila++

            }
            if(r["parcial_t"]>0){
                rowsTrans.add(r)
                total+=r["parcial_t"]
            }

        }
        if(band==3){
            label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
            number = new Number(6, fila, totalMat);sheet.addCell(number);
            fila++
        }

        /*Tranporte*/
        if (rowsTrans.size()>0){
            fila++
            label = new Label(0, fila,"Transporte", times16format); sheet.addCell(label);
            sheet.mergeCells(0,fila, 1, fila)
            fila++
            label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
            label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
            label = new Label(2, fila, "Peso/Vol", times16format); sheet.addCell(label);
            label = new Label(3, fila, "Cantidad", times16format); sheet.addCell(label);
            label = new Label(4, fila, "Distancia", times16format); sheet.addCell(label);
            label = new Label(5, fila, "Unitario", times16format); sheet.addCell(label);
            label = new Label(6, fila, "C.Total(\$)", times16format); sheet.addCell(label);
            fila++
            rowsTrans.each {rt->
                label = new Label(0, fila,rt["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila,rt["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, rt["itempeso"]);sheet.addCell(number);
                number = new Number(3, fila, rt["rbrocntd"]);sheet.addCell(number);
                number = new Number(4, fila, rt["distancia"]);sheet.addCell(number);
                number = new Number(5, fila, rt["tarifa"]);sheet.addCell(number);
                number = new Number(6, fila, rt["parcial_t"]);sheet.addCell(number);
                fila++
            }
            label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
            number = new Number(6, fila, total);sheet.addCell(number);
            fila++
            fila++
        }

        /*indirectos */

        label = new Label(0, fila,"Costos Indirectos", times16format); sheet.addCell(label);
        sheet.mergeCells(0,fila, 1, fila)
        fila++

        label = new Label(0, fila, "Descripción", times16format); sheet.addCell(label);
        sheet.mergeCells(0,fila, 1, fila)
        label = new Label(5, fila, "Porcentaje", times16format); sheet.addCell(label);
        label = new Label(6, fila, "Valor", times16format); sheet.addCell(label);
        fila++
        def totalRubro=total+totalHer+totalMan+totalMat
        def totalIndi=totalRubro*indi/100
        label = new Label(0, fila, "Costos indirectos", times10); sheet.addCell(label);
        sheet.mergeCells(0,fila, 1, fila)
        number = new Number(5, fila, indi);sheet.addCell(number);
        number = new Number(6, fila, totalIndi);sheet.addCell(number);


        /*Totales*/
        fila+=4
        label = new Label(4, fila,"Costo unitario directo", times16format); sheet.addCell(label);
        sheet.mergeCells(4,fila, 5, fila)
        label = new Label(4, fila+1,"Costos indirectos", times16format); sheet.addCell(label);
        sheet.mergeCells(4,fila+1, 5, fila+1)
        label = new Label(4, fila+2,"Costo total del rubro", times16format); sheet.addCell(label);
        sheet.mergeCells(4,fila+2, 5, fila+2)
        label = new Label(4, fila+3,"Precio unitario(\$USD)", times16format); sheet.addCell(label);
        sheet.mergeCells(4,fila+3, 5, fila+3)
        number = new Number(6, fila, totalRubro.toDouble().round(5));sheet.addCell(number);
        number = new Number(6, fila+1, (totalIndi).toDouble().round(5));sheet.addCell(number);
        number = new Number(6, fila+2, (totalRubro+totalIndi).toDouble().round(5));sheet.addCell(number);
        number = new Number(6, fila+3, (totalRubro+totalIndi).toDouble().round(2));sheet.addCell(number);


        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "rubro.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());


    }



    def imprimirRubro() {
//        println "imprimir rubro "+params
        def rubro = Item.get(params.id)

        def oferente = Persona.get(params.oferente)

        def obraOferente = Obra.findByOferente(oferente)

        def obra2 = Obra.get(params.obra2.toLong())

//        println("--->>" + obra2?.idJanus)

//        def sql = "SELECT * FROM cncr WHERE obra__id=${obraOferente?.idJanus}"
        def sql = "SELECT * FROM cncr WHERE obra__id=${obra2?.idJanus}"

//        println("sql:" + sql)

        def cn = dbConnectionService.getConnection()

        def conc = cn.rows(sql.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)

//        println("con" + concurso)


        def fechaOferta = printFecha(obraOferente?.fechaOferta)

        def fechaEntregaOferta = printFecha(concurso?.fechaLimiteEntregaOfertas)

        def firma = Persona.get(params.oferente).firma

//        println(obraOferente)

        def lugar = params.lugar
        def indi = params.indi
        def listas = params.listas

        try {
            indi = indi.toDouble()
        } catch (e) {
            println "error parse " + e
            indi = 21.5
        }
        def obra
        if (params.obra) {
            obra = Obra.get(params.obra)
        }


//        def parametros = ""+rubro.id+","+params.oferente
//        preciosService.ac_rbroV2(rubro?.id, oferente?.id)
//        def res = preciosService.rb_precios(parametros,"")

                def parametros = ""+rubro.id+","+oferente.id
        preciosService.ac_rbroV2(rubro?.id, oferente?.id)
        def res = preciosService.rb_preciosV3(parametros)


        def tablaHer = '<table class=""> '
        def tablaMano = '<table class=""> '
        def tablaMat = '<table class=""> '
        def tablaMat2 = '<table class="marginTop"> '
        def tablaTrans = '<table class=""> '
//        def tablaTrans2 = '<table class="marginTop"> '
        def tablaIndi = '<table class="marginTop"> '
        def total = 0, totalHer = 0, totalMan = 0, totalMat = 0
        def band = 0
        def bandMat = 0
        def bandTrans = params.trans

        tablaTrans += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot' >CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"
        tablaHer += "<thead><tr><th colspan='7' class='tituloHeader'>EQUIPOS</th></tr><tr><th colspan='7' class='theader'></th></tr><tr><th style='width: 80px' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>CANTIDAD</th><th style='width:70px'>TARIFA(\$/H)</th><th>COSTO(\$)</th><th>RENDIMIENTO</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='7' class='theaderup'></th></tr> </thead><tbody>"
        tablaMano += "<thead><tr><th colspan='7' class='tituloHeader'>MANO DE OBRA</th></tr><tr><th colspan='7' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>CANTIDAD</th><th style='width:70px'>JORNAL(\$/H)</th><th>COSTO(\$)</th><th>RENDIMIENTO</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='7' class='theaderup'></th></tr> </thead><tbody>"
        tablaMat += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES INCLUYE TRANSPORTE</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"
        tablaMat2 += "<thead><tr><th colspan='6' class='tituloHeader'>MATERIALES INCLUYE TRANSPORTE</th></tr><tr><th colspan='6' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>CANTIDAD</th><th>UNITARIO(\$)</th><th>C.TOTAL(\$)</th></tr> <tr><th colspan='6' class='theaderup'></th></tr> </thead><tbody>"
//        tablaTrans2 += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"







        res.each { r ->
//            println "res zzzzz "+r

            if (r["grpocdgo"] == 3) {

//                println("entro" + r['itemnmbr'])

                tablaHer += "<tr>"
                tablaHer += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaHer += "<td>" + r["itemnmbr"] + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalHer += r["parcial"]
                tablaHer += "</tr>"
            }
            else if (r["grpocdgo"] == 2) {
                tablaMano += "<tr>"
                tablaMano += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaMano += "<td>" + r["itemnmbr"] + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalMan += r["parcial"]
                tablaMano += "</tr>"
            }
            else if (r["grpocdgo"] == 1) {

                bandMat = 1

                tablaMat += "<tr>"
                if (params.trans != 'no') {
                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat+="<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>${r['itempeso']}</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'></td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["parcial"] + "</td>"
                    totalMat += r["parcial"]
                } else {

                }
                if(params.trans == 'no'){

                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["rbpcpcun"] + r["parcial_t"] / r["rbrocntd"]), format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat+="<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>${r['itempeso']}</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'></td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["parcial"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["parcial"] + r["parcial_t"]), format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"

                    totalMat += r["parcial"] + r["parcial_t"]
                }
                tablaMat += "</tr>"
            }
           else if (r["grpocdgo"]== 1 && params.trans != 'no') {
                tablaTrans += "<tr>"
                tablaTrans += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaTrans += "<td>" + r["itemnmbr"] + "</td>"
                println("entro")


                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["itempeso"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["distancia"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["tarifa"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"], format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                total += r["parcial_t"]
                tablaTrans += "</tr>"
            }
            else {

            }


        }
        tablaTrans += "<tr><td></td><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: total, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaTrans += "</tbody></table>"
        tablaHer += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalHer, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaHer += "</tbody></table>"
        tablaMano += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMan, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMano += "</tbody></table>"
        tablaMat += "<tr><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMat, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMat += "</tbody></table>"
//        tablaTrans2 += "</tbody></table>"
        tablaMat2 += "</tbody></table>"

            def totalRubro = 0
//        if (!params.trans) {
//            totalRubro = total + totalHer + totalMan + totalMat
//        } else {
            totalRubro = totalHer + totalMan + totalMat
//        }

        band = total

        def totalIndi = totalRubro * indi / 100
        tablaIndi += "<thead><tr><th class='tituloHeader'>COSTOS INDIRECTOS</th></tr><tr><th colspan='3' class='theader'></th></tr><tr><th style='width:550px' class='padTopBot'>DESCRIPCIÓN</th><th style='width:130px'>PORCENTAJE</th><th>VALOR</th></tr>    <tr><th colspan='3' class='theaderup'></th></tr>  </thead>"
        tablaIndi += "<tbody><tr><td>COSTOS INDIRECTOS</td><td style='text-align:center'>${indi}%</td><td style='text-align:right'>${g.formatNumber(number: totalIndi, format: "##,##0", minFractionDigits: "5", maxFractionDigits: "5")}</td></tr></tbody>"
        tablaIndi += "</table>"

//        if (total == 0 || params.trans == "no")
//            tablaTrans = ""
//        if (totalHer == 0)
//            tablaHer = ""
//        if (totalMan == 0)
//            tablaMano = ""
//        if (totalMat == 0)
//            tablaMat = ""
//        println "fin reporte rubro"
//        [rubro: rubro, fechaPrecios: fecha, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band: band, tablaMat2: tablaMat2, bandMat: bandMat, bandTrans: bandTrans , tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat,
//                tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, obra: obra, fechaPala: fecha1]
//
//        [rubro: rubro, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band: band, tablaMat2: tablaMat2, bandMat: bandMat, bandTrans: bandTrans , tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat,
//                tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, obra: obraOferente, oferente: oferente, fechaOferta: fechaOferta, obraOferente: obraOferente, concurso: concurso, fechaEntregaOFerta: fechaEntregaOferta, firma: firma]

        [rubro: rubro, tablaTrans: tablaTrans, band: band, bandMat: bandMat, tablaMat2: tablaMat2, bandTrans: bandTrans , tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat,
                tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, obra: obraOferente, oferente: oferente, fechaOferta: fechaOferta, obraOferente: obraOferente, concurso: concurso, fechaEntregaOFerta: fechaEntregaOferta, firma: firma]
    }




    private static int[] arregloEnteros(array) {
        int[] ia = new int[array.size()]
        array.eachWithIndex { it, i ->
            ia[i] = it.toInteger()
        }

        return ia
    }

    private static void addCellTabla(PdfPTable table, paragraph, params) {
        PdfPCell cell = new PdfPCell(paragraph);
        if (params.height) {
            cell.setFixedHeight(params.height.toFloat());
        }
        if (params.border) {
            cell.setBorderColor(params.border);
        }
        if (params.bg) {
            cell.setBackgroundColor(params.bg);
        }
        if (params.colspan) {
            cell.setColspan(params.colspan);
        }
        if (params.align) {
            cell.setHorizontalAlignment(params.align);
        }
        if (params.valign) {
            cell.setVerticalAlignment(params.valign);
        }
        if (params.w) {
            cell.setBorderWidth(params.w);
            cell.setUseBorderPadding(true);
        }
        if (params.bwl) {
            cell.setBorderWidthLeft(params.bwl.toFloat());
            cell.setUseBorderPadding(true);
        }
        if (params.bwb) {
            cell.setBorderWidthBottom(params.bwb.toFloat());
            cell.setUseBorderPadding(true);
        }
        if (params.bwr) {
            cell.setBorderWidthRight(params.bwr.toFloat());
            cell.setUseBorderPadding(true);
        }
        if (params.bwt) {
            cell.setBorderWidthTop(params.bwt.toFloat());
            cell.setUseBorderPadding(true);
        }
        if (params.bcl) {
            cell.setBorderColorLeft(params.bcl);
        }
        if (params.bcb) {
            cell.setBorderColorBottom(params.bcb);
        }
        if (params.bcr) {
            cell.setBorderColorRight(params.bcr);
        }
        if (params.bct) {
            cell.setBorderColorTop(params.bct);
        }
        if (params.pl) {
            cell.setPaddingLeft(params.pl.toFloat());
        }
        if (params.pr) {
            cell.setPaddingRight(params.pr.toFloat());
        }
        if (params.pt) {
            cell.setPaddingTop(params.pt.toFloat());
        }
        if (params.pb) {
            cell.setPaddingBottom(params.pb.toFloat());
        }
        if (params.bordeTop) {
            cell.setBorderWidthTop(1)
            cell.setBorderWidthLeft(0)
            cell.setBorderWidthRight(0)
            cell.setBorderWidthBottom(0)
            cell.setPaddingTop(7);

        }
        if (params.bordeBot) {
            cell.setBorderWidthBottom(1)
            cell.setBorderWidthLeft(0)
            cell.setBorderWidthRight(0)
            cell.setPaddingBottom(7)

            if (!params.bordeTop) {
                cell.setBorderWidthTop(0)
            }
        }

        table.addCell(cell);
    }


def reporteFormula () {


//            println("paramsf" + params)

    def auxiliar = Auxiliar.get(1)

//        println(auxiliar)

    def auxiliarFijo = Auxiliar.get(1)

    def oferente = Persona.get(session.usuario.id)

    def obra = Obra.get(params.obra)

    def sql1 = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

    def cn1 = dbConnectionService.getConnection()

    def conc = cn1.rows(sql1.toString())

    def cncrId

    conc.each {

        cncrId = it?.cncr__id

    }

    def concurso = janus.pac.Concurso.get(cncrId)

    def firmaOferente = Persona.get(session.usuario.id).firma



    def firma

    def firmas

    def firmaFijaFormu

    def cuenta = 0;


    def formula = FormulaPolinomica.findAllByObra(obra)

//    println("-->>" + formula)

    def ps = FormulaPolinomica.findAllByObraAndNumeroIlike(obra, 'p%', [sort: 'numero'])



    def cuadrilla = FormulaPolinomica.findAllByObraAndNumeroIlike(obra, 'c%', [sort: 'numero'])
//
//        println("---->>>>>"+ps)

    def c

    def z = []

    def banderafp = 0

    if (obra?.formulaPolinomica == null) {

        obra?.formulaPolinomica = ""

    }


    def prmsHeaderHoja = [border: Color.WHITE]
    def prmsHeaderHoja4 = [border: Color.WHITE, bordeTop:  "1"]


    def prmsHeaderHoja2 = [border: Color.WHITE, colspan: 9]
    def prmsHeaderHoja3 = [border: Color.WHITE, colspan: 2]


    def prmsHeader = [border: Color.WHITE, colspan: 7, bg: new Color(73, 175, 205),
            align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
    def prmsCellHead2 = [border: Color.WHITE,
            align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE,bordeBot: "1"]
    def prmsHeader2 = [border: Color.WHITE, colspan: 3, bg: new Color(73, 175, 205),
            align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
    def prmsCellHead = [border: Color.WHITE, bg: new Color(73, 175, 205),
            align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
    def prmsCellCenter = [border: Color.BLACK, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
    def prmsCellRight = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_RIGHT]
    def prmsHeaderHojaLeft = [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT]
    def prmsCellLeft = [border: Color.BLACK, valign: Element.ALIGN_MIDDLE]
    def prmsSubtotal = [border: Color.BLACK, colspan: 6,
            align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
    def prmsNum = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]

    def prms = [prmsHeaderHoja: prmsHeaderHoja, prmsHeader: prmsHeader, prmsHeader2: prmsHeader2,
            prmsCellHead: prmsCellHead, prmsCell: prmsCellCenter, prmsCellLeft: prmsCellLeft, prmsSubtotal: prmsSubtotal, prmsNum: prmsNum, prmsHeaderHoja2: prmsHeaderHoja2, prmsCellRight: prmsCellRight]



    def baos = new ByteArrayOutputStream()
    def name = "formulaPolinomica_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
    Font times12bold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    Font times18bold = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    Font times10bold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
    Font times10normal = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
    Font times8bold = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
    Font times8normal = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL)
    Font times10boldWhite = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
    Font times8boldWhite = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
    times8boldWhite.setColor(Color.WHITE)
    times10boldWhite.setColor(Color.WHITE)
    def fonts = [times12bold: times12bold, times10bold: times10bold, times8bold: times8bold,
            times10boldWhite: times10boldWhite, times8boldWhite: times8boldWhite, times8normal: times8normal, times10normal: times10normal, times18bold: times18bold]

    Document document
    document = new Document(PageSize.A4);
    def pdfw = PdfWriter.getInstance(document, baos);
    document.open();
    document.addTitle("Formula " + new Date().format("dd_MM_yyyy"));
    document.addSubject("Generado por el sistema Janus");
    document.addKeywords("documentosObra, janus, presupuesto");
    document.addAuthor("Janus");
    document.addCreator("Tedein SA")
    document.setMargins(40, 20, 20, 20)



    Paragraph headers = new Paragraph();

    headers.setAlignment(Element.ALIGN_CENTER);
    headers.add(new Paragraph("G.A.D. PROVINCIA PICHINCHA", times12bold));
    addEmptyLine(headers, 1);
    headers.add(new Paragraph("PROCESO: " + concurso?.codigo, times12bold));
    addEmptyLine(headers, 1);
    headers.add(new Paragraph("FÓRMULA POLINÓMICA", times12bold));
    addEmptyLine(headers, 1);
    document.add(headers);

    PdfPTable tablaHeader = new PdfPTable(3);
    tablaHeader.setWidthPercentage(90);
    tablaHeader.setWidths(arregloEnteros([20, 2, 60]))

    addCellTabla(tablaHeader, new Paragraph("Nombre del Oferente", times10bold), prmsHeaderHoja)
    addCellTabla(tablaHeader, new Paragraph(" : ", times10bold), prmsHeaderHoja)
    addCellTabla(tablaHeader, new Paragraph(oferente?.nombre.toUpperCase() + " " + oferente?.apellido.toUpperCase(), times10normal), prmsHeaderHoja2)

    addCellTabla(tablaHeader, new Paragraph("Proyecto", times10bold), prmsHeaderHoja)
    addCellTabla(tablaHeader, new Paragraph(" : ", times10bold), prmsHeaderHoja)
    addCellTabla(tablaHeader, new Paragraph(obra?.nombre, times10normal), prmsHeaderHoja2)

    document.add(tablaHeader)

    Paragraph txtIzq = new Paragraph();
    txtIzq.setAlignment(Element.ALIGN_LEFT);
    txtIzq.setIndentationLeft(20)
    txtIzq.add(new Paragraph("De existir variaciones en los costos de los componentes de precios unitarios estipulados en el contrato para la contrucción de Proyecto: " + obra?.nombre, times10normal));
    document.add(txtIzq);

    Paragraph txtIzqHeader = new Paragraph();
    txtIzqHeader.setAlignment(Element.ALIGN_LEFT);
    txtIzqHeader.setIndentationLeft(20)
    txtIzqHeader.add(new Paragraph("Los costos se reajustarán para efecto de pago, mediante la fórmula general: ", times10normal));

    txtIzqHeader.add(new Paragraph("Pr = Po (p01B1/Bo + p02C1/Co + p03D1/Do + p04E1/Eo + p05F1/Fo + p06G1/Go + p07H1/Ho + p08I1/Io + p09J1/Jo + p10K1/Ko + pxX1/Xo) ", times10normal));

    def textoFormula = "Pr=Po(";
    def txInicio = "Pr = Po (";
    def txFin = ")";
    def txSuma = " + "
    def txExtra = ""
    def tx = []
    def valores = []
    def formulaCompleta

    def valorP

    ps.each { j ->

        if (j.valor != 0.0 || j.valor != 0) {
            if (j.numero == 'p01') {
                tx[0] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "B1/Bo"
                valores[0] = j
            }
            if (j.numero == 'p02') {
                tx[1] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "C1/Co"
                valores[1] = j
            }
            if (j.numero == 'p03') {
                tx[2] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "D1/Do"
                valores[2] = j
            }
            if (j.numero == 'p04') {
                tx[3] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "E1/Eo"
                valores[3] = j
            }
            if (j.numero == 'p05') {

                tx[4] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "F1/Fo"
                valores[4] = j
            }
            if (j.numero == 'p06') {
                tx[5] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "G1/Go"
                valores[5] = j
            }
            if (j.numero == 'p07') {

                def p07valores =
                        tx[6] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "H1/Ho"
                valores[6] = j
            }
            if (j.numero == 'p08') {

                tx[7] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "I1/Io"
                valores[7] = j
            }
            if (j.numero == 'p09') {

                tx[8] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "J1/Jo"
                valores[8] = j

            }
            if (j.numero == 'p10') {

                tx[9] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "K1/Ko"
                valores[9] = j
            }
            if (j.numero.trim() == 'px') {
                tx[10] = g.formatNumber(number: j.valor, maxFractionDigits: 3, minFractionDigits: 3) + "X1/Xo"
                valores[10] = j
            }

        }

    }
    def formulaStr = txInicio
    tx.eachWithIndex { linea, k ->
        if (linea) {
            formulaStr += linea
            if (k < tx.size() - 1)
                formulaStr += " + "
        }

    }
    formulaStr += txFin
//        println "forstr "+formulaStr
    txtIzqHeader.add(new Paragraph(formulaStr, times10bold));
    txtIzqHeader.add(new Paragraph(" ", times10bold));

    document.add(txtIzqHeader)

    PdfPTable tablaCoeficiente = new PdfPTable(4);
    tablaCoeficiente.setWidthPercentage(90);
    tablaCoeficiente.setWidths(arregloEnteros([10, 8, 25, 53]))


    def valorTotal = 0

//        println "valores " +valores

    valores.each { i ->
        if (i) {
            if (i.valor != 0.0 || i.valor != 0) {

//                         addCellTabla(tablaCoeficiente, new Paragraph(" ", times10bold), prmsHeaderHoja)
                addCellTabla(tablaCoeficiente, new Paragraph(i.numero + " = ", times10normal), prmsHeaderHoja)
                addCellTabla(tablaCoeficiente, new Paragraph(g.formatNumber(number: i.valor, format: "#.###", minFractionDigits: 3, locale: "ec"), times10normal), prmsHeaderHoja)
                addCellTabla(tablaCoeficiente, new Paragraph("Coeficiente del Componente ", times10normal), prmsHeaderHoja)
                addCellTabla(tablaCoeficiente, new Paragraph(i?.indice?.descripcion.toUpperCase(), times10normal), prmsHeaderHoja)

                valorTotal = i.valor + valorTotal

            }
        }


    }

    addCellTabla(tablaCoeficiente, new Paragraph("SUMAN : ", times10bold), prmsHeaderHoja4)
    addCellTabla(tablaCoeficiente, new Paragraph(g.formatNumber(number: valorTotal, format: "##,##0", minFractionDigits: 3, maxFractionDigits: 3, locale: "ec"), times10bold), prmsHeaderHoja4)
    addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
    addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)

    addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
    addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
    addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
    addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)




    PdfPTable tablaCuadrillaHeader = new PdfPTable(2);
    tablaCuadrillaHeader.setWidthPercentage(90);
    tablaCuadrillaHeader.setWidths(arregloEnteros([30, 70]))

//        addCellTabla(tablaCuadrillaHeader, new Paragraph(" ", times10bold), prmsHeaderHoja)
    addCellTabla(tablaCuadrillaHeader, new Paragraph("CUADRILLA TIPO ", times10bold), prmsHeaderHoja)

//        addCellTabla(tablaCuadrillaHeader, new Paragraph(" ", times10bold), prmsHeaderHoja)
    addCellTabla(tablaCuadrillaHeader, new Paragraph("CLASE OBRERO ", times10bold), prmsHeaderHoja)



    PdfPTable tablaCuadrilla = new PdfPTable(3);
    tablaCuadrilla.setWidthPercentage(90);
    tablaCuadrilla.setWidths(arregloEnteros([10, 10, 70]))

    def valorTotalCuadrilla = 0;

    cuadrilla.each { i ->


        if (i.valor != 0.0 || i.valor != 0) {

//                addCellTabla(tablaCuadrilla, new Paragraph(" ", times10normal), prmsHeaderHoja)
            addCellTabla(tablaCuadrilla, new Paragraph(i?.numero, times10normal), prmsHeaderHoja)
            addCellTabla(tablaCuadrilla, new Paragraph(g.formatNumber(number: i?.valor, format: "##.####", locale: "ec"), times10normal), prmsHeaderHoja)

            addCellTabla(tablaCuadrilla, new Paragraph(i?.indice?.descripcion, times10normal), prmsHeaderHoja)


            valorTotalCuadrilla = i.valor + valorTotalCuadrilla

        } else {


        }

    }

    addCellTabla(tablaCuadrilla, new Paragraph("SUMAN : ", times10bold), prmsHeaderHoja4)
    addCellTabla(tablaCuadrilla, new Paragraph(g.formatNumber(number: valorTotalCuadrilla, format: "##,##0", minFractionDigits: 3, maxFractionDigits: 3, locale: "ec"), times10bold), prmsHeaderHoja4)
    addCellTabla(tablaCuadrilla, new Paragraph(" ", times10normal), prmsHeaderHoja)

    document.add(tablaCoeficiente)
    document.add(tablaCuadrillaHeader)
    document.add(tablaCuadrilla)

    Paragraph txtIzqPie = new Paragraph();
    txtIzqPie.setAlignment(Element.ALIGN_LEFT);
    txtIzqPie.setIndentationLeft(28);
    txtIzqPie.add(new Paragraph(auxiliarFijo?.notaFormula, times10normal));
    txtIzqPie.add(new Paragraph(" ", times10bold));
    document.add(txtIzqPie)

    PdfPTable pieTabla = new PdfPTable(2);
    pieTabla.setWidthPercentage(90);
    pieTabla.setWidths(arregloEnteros([99, 1]))

    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)

    addCellTabla(pieTabla, new Paragraph("Quito, " + printFecha(concurso?.fechaLimiteEntregaOfertas), fonts.times10bold), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)

    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)

    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)

    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)

    addCellTabla(pieTabla, new Paragraph("_____________________________", fonts.times10bold), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)

    addCellTabla(pieTabla, new Paragraph(firmaOferente, fonts.times10bold), prmsHeaderHojaLeft)
    addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)


    document.add(pieTabla)


    document.close();
    pdfw.close()
    byte[] b = baos.toByteArray();
    response.setContentType("application/pdf")
    response.setHeader("Content-disposition", "attachment; filename=" + name)
    response.setContentLength(b.length)
    response.getOutputStream().write(b)


}


}
