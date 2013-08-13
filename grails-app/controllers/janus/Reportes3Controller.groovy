package janus


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

class Reportes3Controller {

    def preciosService


    def meses = ['', "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]

    private String printFecha(Date fecha) {
        if (fecha) {
            return (fecha.format("dd")+' de '+ meses[fecha.format("MM").toInteger()]+' de '+fecha.format("yyyy")).toUpperCase()
        } else {
            return "Error: no hay fecha que mostrar"
        }
    }


    def index() { }

    def test() {
        return [params: params]
    }

    def imprimirTablaSub(){
//        println "imprimir tabla sub "+params
        def obra = Obra.get(params.obra)
        def detalle
        def subPre

        def fechaHoy = printFecha(new Date())

        def oferente = Persona.get(params.oferente)

        if (params.sub && params.sub != "-1"){

            detalle= VolumenesObra.findAllByObraAndSubPresupuesto(obra,SubPresupuesto.get(params.sub),[sort:"orden"])
        }

        else {


            detalle= VolumenesObra.findAllByObra(obra,[sort:"orden"])


        }
        def subPres = VolumenesObra.findAllByObra(obra,[sort:"orden"]).subPresupuesto.unique()

        def precios = [:]

        if (params.sub != '-1'){

            subPre= SubPresupuesto.get(params.sub).descripcion

        }else {

            subPre= -1

        }

        def indirecto = obra.totales/100
        preciosService.ac_rbroObra(obra.id)

        detalle.each{

            def res = preciosService.presioUnitarioVolumenObra("sum(parcial)+sum(parcial_t) precio ",it.item.id,params.oferente)

            def precio = 0
            if(res["precio"][0]!=null && res["precio"][0]!="null" )
                precio = res["precio"][0]
            precios.put(it.id.toString(),(precio+precio*indirecto).toDouble().round(2))
        }


//        println "precios "+precios


//        [detalle:detalle,precios:precios,subPres:subPres,subPre:SubPresupuesto.get(params.sub).descripcion,obra: obra,indirectos:indirecto*100]
        [detalle:detalle,precios:precios,subPres:subPres,subPre:subPre,obra: obra,indirectos:indirecto*100, oferente: oferente, fechaHoy: fechaHoy]


        //2

//
//                println "imprimir tabla sub "+params
//        def obra = Obra.get(params.obra)
//
//        def detalle
//        def valores
//        def subPre
//        def fechaNueva = obra?.fechaCreacionObra.format("dd-MM-yyyy");
//        def fechaPU = (obra?.fechaPreciosRubros.format("dd-MM-yyyy"));
//
//        if (params.sub != '-1'){
//
//            subPre= SubPresupuesto.get(params.sub).descripcion
//
//        }else {
//
//            subPre= -1
//
//        }
//
//
//        if (params.sub)
//            if (params.sub == '-1'){
//                valores = preciosService.rbro_pcun_v2(obra.id)
//            }else {
//
//                valores = preciosService.rbro_pcun_v3(obra.id, params.sub)
//            }
//        else
//            valores = preciosService.rbro_pcun_v2(obra.id)
//
//        def subPres = VolumenesObra.findAllByObra(obra, [sort: "orden"]).subPresupuesto.unique()
//
//        def precios = [:]
//
//        def indirecto = obra.totales / 100
//        preciosService.ac_rbroObra(obra.id)
//
//
//        [detalle: detalle, precios: precios, subPres: subPres, subPre: subPre, obra: obra, indirectos: indirecto * 100, valores: valores, fechaNueva: fechaNueva, fechaPU: fechaPU]

    }
    def imprimirRubroVolObra(){
//        println "imprimir rubro "+params
        def rubro =Item.get(params.id)
        def obra=Obra.get(params.obra)

        def fechaOferta = printFecha(obra?.fechaOferta)

        def oferente = Persona.get(params.oferente)
//        def fecha = new Date().parse("dd-MM-yyyy",params.fecha)
        def indi = obra.totales
        try{
            indi=indi.toDouble()
        } catch (e){
            println "error parse "+e
            indi=21.5
        }

        preciosService.ac_rbroObra(obra.id)
        def res = preciosService.presioUnitarioVolumenObra("*",rubro.id,params.oferente)

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
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalHer += r["parcial"]
                tablaHer += "</tr>"
            }
            if (r["grpocdgo"] == 2) {
                tablaMano += "<tr>"
                tablaMano += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaMano += "<td>" + r["itemnmbr"] + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
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
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
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
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["rbpcpcun"] + r["parcial_t"] / r["rbrocntd"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["parcial"] + r["parcial_t"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
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
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["itempeso"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["distancia"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"] / (r["itempeso"] * r["rbrocntd"] * r["distancia"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["tarifa"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                total += r["parcial_t"]
                tablaTrans += "</tr>"
            }
            else {

            }

        }
        tablaTrans += "<tr><td></td><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: total, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaTrans += "</tbody></table>"
        tablaHer += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalHer, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaHer += "</tbody></table>"
        tablaMano += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMan, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMano += "</tbody></table>"
        tablaMat += "<tr><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMat, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMat += "</tbody></table>"
        tablaTrans2 += "</tbody></table>"
        tablaMat2 += "</tbody></table>"


        def totalRubro = total + totalHer + totalMan + totalMat
        totalRubro = totalRubro.toDouble().round(5)

        band = total

        def totalIndi = totalRubro * indi / 100
        totalIndi = totalIndi.toDouble().round(5)
        tablaIndi += "<thead><tr><th class='tituloHeader'>COSTOS INDIRECTOS</th></tr><tr><th colspan='3' class='theader'></th></tr><tr><th style='width:550px' class='padTopBot'>DESCRIPCIÓN</th><th style='width:130px'>PORCENTAJE</th><th>VALOR</th></tr>    <tr><th colspan='3' class='theaderup'></th></tr>  </thead>"
        tablaIndi += "<tbody><tr><td>COSTOS INDIRECTOS</td><td style='text-align:center'>${indi}%</td><td style='text-align:right'>${g.formatNumber(number: totalIndi, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5")}</td></tr></tbody>"
        tablaIndi += "</table>"


        if (total == 0)
            tablaTrans = ""
        if (totalHer == 0)
            tablaHer = ""
        if (totalMan == 0)
            tablaMano = ""
        if (totalMat == 0)
            tablaMat = ""
//        println "fin reporte rubro"
        [rubro: rubro, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band:  band, tablaMat2: tablaMat2, bandMat: bandMat,
                bandTrans: bandTrans, tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat, tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, obra: obra, oferente: oferente, fechaOferta: fechaOferta]



    }

    def imprimirRubroExcel(){
        println "imprimir rubro  excel "+params
        def rubro = Item.get(params.id)
//        def fecha = new Date().parse("dd-MM-yyyy",params.fecha)
//        def lugar = params.lugar
        def indi = params.indi
//        def listas = params.listas

        try{
            indi=indi.toDouble()
        } catch (e){
            println "error parse "+e
            indi=21.5
        }


//        def parametros = ""+params.id+","+params.lugar+",'"+fecha.format("yyyy-MM-dd")+"',"+params.dsps.toDouble()+","+params.dsvs.toDouble()+","+rendimientos["rdps"]+","+rendimientos["rdvl"]
//        def parametros = ""+rubro.id+","+params.oferente
//        preciosService.ac_rbroV2(params.id,params.oferente)
//        def res = preciosService.rb_precios(parametros,"order by grpocdgo desc")

        def parametros = ""+rubro.id+","+params.oferente
        preciosService.ac_rbroObra(params.id)
        def res = preciosService.rb_precios(parametros,"")


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

//        sheet.setColumnView(4, 30)
//        sheet.setColumnView(8, 20)
        def label = new Label(0, 1,"GOBIERNO  AUTÓNOMO DESCENTRALIZADO DE LA PROVINCIA DE PICHINCHA".toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(0,2, "GESTIÓN DE PRESUPUESTOS".toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(0, 3, "ANÁLISIS DE PRECIOS UNITARIOS".toUpperCase(), times16format); sheet.addCell(label);

        sheet.mergeCells(0,1, 1, 1)
        sheet.mergeCells(0,2, 1,2)
        sheet.mergeCells(0,3, 1, 3)
        label = new Label(0, 5, "Fecha: "+new Date().format("dd-MM-yyyy"), times16format); sheet.addCell(label);
        sheet.mergeCells(0,5, 1, 5)
        label = new Label(0, 6, "Código: "+rubro.codigo, times16format); sheet.addCell(label);
        sheet.mergeCells(0,6, 1, 6)
        label = new Label(0, 7, "Descripción: "+rubro.nombre, times16format); sheet.addCell(label);
        sheet.mergeCells(0,7, 1, 7)
//        label = new Label(5, 5, "Fecha Act. P.U: "+fecha?.format("dd-MM-yyyy"), times16format); sheet.addCell(label);
//        sheet.mergeCells(5,5, 6, 5)
        label = new Label(5, 6, "Unidad: "+rubro.unidad?.codigo, times16format); sheet.addCell(label);
        sheet.mergeCells(5,6, 6, 6)

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
        println "imprimir rubro "+params
        def rubro = Item.get(params.id)

        def oferente = Persona.get(params.oferente)

        def obraOferente = Obra.findByOferente(oferente)


        def fechaOferta = printFecha(obraOferente?.fechaOferta)

        println(obraOferente)

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


        def parametros = ""+rubro.id+","+params.oferente
        preciosService.ac_rbroObra(params.id)
        def res = preciosService.rb_precios(parametros,"")




        def tablaHer = '<table class=""> '
        def tablaMano = '<table class=""> '
        def tablaMat = '<table class=""> '
        def tablaMat2 = '<table class="marginTop"> '
        def tablaTrans = '<table class=""> '
        def tablaTrans2 = '<table class="marginTop"> '
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
        tablaTrans2 += "<thead><tr><th colspan='8' class='tituloHeader'>TRANSPORTE</th></tr><tr><th colspan='8' class='theader'></th></tr><tr><th style='width: 80px;' class='padTopBot'>CÓDIGO</th><th style='width:610px'>DESCRIPCIÓN</th><th>UNIDAD</th><th>PES/VOL</th><th>CANTIDAD</th><th>DISTANCIA</th><th>TARIFA</th><th>C.TOTAL(\$)</th></tr>  <tr><th colspan='8' class='theaderup'></th></tr> </thead><tbody>"

//        println "rends "+rendimientos

//        println "res "+res

        res.each { r ->
//            println "res "+res
            if (r["grpocdgo"] == 3) {
                tablaHer += "<tr>"
                tablaHer += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaHer += "<td>" + r["itemnmbr"] + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaHer += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalHer += r["parcial"]
                tablaHer += "</tr>"
            }
            if (r["grpocdgo"] == 2) {
                tablaMano += "<tr>"
                tablaMano += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaMano += "<td>" + r["itemnmbr"] + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"] * r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rndm"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaMano += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                totalMan += r["parcial"]
                tablaMano += "</tr>"
            }
            if (r["grpocdgo"] == 1) {

                bandMat = 1

                tablaMat += "<tr>"
                if (params.trans != 'no') {
                    tablaMat += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                    tablaMat += "<td>" + r["itemnmbr"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbpcpcun"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
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
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["rbpcpcun"] + r["parcial_t"] / r["rbrocntd"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
//                    tablaMat+="<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>${r['itempeso']}</td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'></td>"
//                    tablaMat += "<td style='width: 50px;text-align: right'>" + r["parcial"] + "</td>"
                    tablaMat += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: (r["parcial"] + r["parcial_t"]), format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"

                    totalMat += r["parcial"] + r["parcial_t"]
                }
                tablaMat += "</tr>"
            }
            if (r["grpocdgo"]== 1 && params.trans != 'no') {
                tablaTrans += "<tr>"
                tablaTrans += "<td style='width: 80px;'>" + r["itemcdgo"] + "</td>"
                tablaTrans += "<td>" + r["itemnmbr"] + "</td>"
                println("entro")
//                if(r["tplscdgo"].trim() =='P' || r["tplscdgo"].trim() =='P1' ){
//                    println("entro2")
//                    tablaTrans += "<td style='width: 50px;text-align: right'>" + "ton-km" + "</td>"
//                } else{
//
//                    if(r["tplscdgo"].trim() =='V' || r["tplscdgo"].trim() =='V1' || r["tplscdgo"].trim() =='V2'){
//
//                        tablaTrans += "<td style='width: 50px;text-align: right'>" + "m3-km" + "</td>"
//                    }
//                    else {

//                        tablaTrans += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
//                    }
//
//                }
//                tablaTrans += "<td style='width: 50px;text-align: right'>" + r["unddcdgo"] + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["itempeso"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["rbrocntd"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["distancia"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["tarifa"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                tablaTrans += "<td style='width: 50px;text-align: right'>" + g.formatNumber(number: r["parcial_t"], format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec") + "</td>"
                total += r["parcial_t"]
                tablaTrans += "</tr>"
            }
            else {

            }


        }
        tablaTrans += "<tr><td></td><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: total, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaTrans += "</tbody></table>"
        tablaHer += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalHer, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaHer += "</tbody></table>"
        tablaMano += "<tr><td></td><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMan, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMano += "</tbody></table>"
        tablaMat += "<tr><td></td><td></td><td></td><td></td><td style='text-align: right'><b>TOTAL</b></td><td style='width: 50px;text-align: right'><b>${g.formatNumber(number: totalMat, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5", locale: "ec")}</b></td></tr>"
        tablaMat += "</tbody></table>"
        tablaTrans2 += "</tbody></table>"
        tablaMat2 += "</tbody></table>"

        def totalRubro = 0
        if (!params.trans) {
            totalRubro = total + totalHer + totalMan + totalMat
        } else {
            totalRubro = totalHer + totalMan + totalMat
        }

        band = total

        def totalIndi = totalRubro * indi / 100
        tablaIndi += "<thead><tr><th class='tituloHeader'>COSTOS INDIRECTOS</th></tr><tr><th colspan='3' class='theader'></th></tr><tr><th style='width:550px' class='padTopBot'>DESCRIPCIÓN</th><th style='width:130px'>PORCENTAJE</th><th>VALOR</th></tr>    <tr><th colspan='3' class='theaderup'></th></tr>  </thead>"
        tablaIndi += "<tbody><tr><td>COSTOS INDIRECTOS</td><td style='text-align:center'>${indi}%</td><td style='text-align:right'>${g.formatNumber(number: totalIndi, format: "##,#####0", minFractionDigits: "5", maxFractionDigits: "5")}</td></tr></tbody>"
        tablaIndi += "</table>"

        if (total == 0 || params.trans == "no")
            tablaTrans = ""
        if (totalHer == 0)
            tablaHer = ""
        if (totalMan == 0)
            tablaMano = ""
        if (totalMat == 0)
            tablaMat = ""
//        println "fin reporte rubro"
//        [rubro: rubro, fechaPrecios: fecha, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band: band, tablaMat2: tablaMat2, bandMat: bandMat, bandTrans: bandTrans , tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat,
//                tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, obra: obra, fechaPala: fecha1]

        [rubro: rubro, tablaTrans: tablaTrans, tablaTrans2: tablaTrans2, band: band, tablaMat2: tablaMat2, bandMat: bandMat, bandTrans: bandTrans , tablaHer: tablaHer, tablaMano: tablaMano, tablaMat: tablaMat,
                tablaIndi: tablaIndi, totalRubro: totalRubro, totalIndi: totalIndi, obra: obraOferente, oferente: oferente, fechaOferta: fechaOferta, obraOferente: obraOferente]
    }




//    def imprimirRubro(){
//        println "imprimir rubro "+params
//        def rubro = Item.get(params.id)
////        def fecha = new Date().parse("dd-MM-yyyy",params.fecha)
////        def lugar = params.lugar
//        def indi = params.indi
////        def listas = params.listas
//        try{
//            indi=indi.toDouble()
//        } catch (e){
//            println "error parse "+e
//            indi=21.5
//        }
//        def obra
//        if (params.obra){
//            obra = Obra.get(params.obra)
//        }
//
//        def parametros = ""+rubro.id+","+params.oferente
//        preciosService.ac_rbroObra(params.id)
//        def res = preciosService.rb_precios(parametros,"")
//        def tablaHer='<table class=""> '
//        def tablaMano='<table class=""> '
//        def tablaMat='<table class=""> '
//        def tablaTrans='<table class=""> '
//        def tablaIndi='<table class=""> '
//        def total = 0,totalHer=0,totalMan=0,totalMat=0
//        tablaTrans+="<thead><tr><th colspan='7'>Transporte</th></tr><tr><th style='width: 80px;'>Código</th><th style='width:610px'>Descripción</th><th>Pes/Vol</th><th>Cantidad</th><th>Distancia</th><th>Unitario(\$)</th><th>C.Total(\$)</th></tr></thead><tbody>"
//
//        tablaHer+="<thead><tr><th colspan='7'>Herramienta</th></tr><tr><th style='width: 80px'>Código</th><th style='width:610px'>Descripción</th><th>Cantidad</th><th>Tarifa<br/> (\$/hora)</th><th>Costo(\$)</th><th>Rendimiento</th><th>C.Total(\$)</th></tr></thead><tbody>"
//        tablaMano+="<thead><tr><th colspan='7'>Mano de obra</th></tr><tr><th style='width: 80px;'>Código</th><th style='width:610px'>Descripción</th><th>Cantidad</th><th>Jornal<br/>(\$/hora)</th><th>Costo(\$)</th><th>Rendimiento</th><th>C.Total(\$)</th></tr></thead><tbody>"
//        tablaMat+="<thead><tr><th colspan='7'>Materiales</th></tr><tr><th style='width: 80px;'>Código</th><th style='width:610px'>Descripción</th><th>Cantidad</th><th>Unitario(\$)</th><th>Unidad</th><th>Peso/Vol</th><th>C.Total(\$)</th></tr></thead><tbody>"
////        println "rends "+rendimientos
//
////        println "res "+res
//
//        res.each {r->
////            println "res "+res
//            if(r["grpocdgo"]==3){
//                tablaHer+="<tr>"
//                tablaHer+="<td style='width: 80px;'>"+r["itemcdgo"]+"</td>"
//                tablaHer+="<td>"+r["itemnmbr"]+"</td>"
//                tablaHer+="<td style='width: 50px;text-align: right'>"+ g.formatNumber(number:r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaHer+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbpcpcun"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaHer+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbpcpcun"]*r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaHer+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rndm"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaHer+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["parcial"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                totalHer+=r["parcial"]
//                tablaHer+="</tr>"
//            }
//            if(r["grpocdgo"]==2){
//                tablaMano+="<tr>"
//                tablaMano+="<td style='width: 80px;'>"+r["itemcdgo"]+"</td>"
//                tablaMano+="<td>"+r["itemnmbr"]+"</td>"
//                tablaMano+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaMano+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbpcpcun"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaMano+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbpcpcun"]*r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaMano+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rndm"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaMano+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["parcial"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                totalMan+=r["parcial"]
//                tablaMano+="</tr>"
//            }
//            if(r["grpocdgo"]==1){
//                tablaMat+="<tr>"
//                if(!params.trans){
//                    tablaMat+="<td style='width: 80px;'>"+r["itemcdgo"]+"</td>"
//                    tablaMat+="<td>"+r["itemnmbr"]+"</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbpcpcun"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                    tablaMat+="<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>${r['itempeso']}</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>"+r["parcial"]+"</td>"
//                    totalMat+=r["parcial"]
//                }else{
//                    tablaMat+="<td style='width: 80px;'>"+r["itemcdgo"]+"</td>"
//                    tablaMat+="<td>"+r["itemnmbr"]+"</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbpcpcun"]+r["parcial_t"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                    tablaMat+="<td style='width: 50px;text-align: center'>${r['unddcdgo']}</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>${r['itempeso']}</td>"
//                    tablaMat+="<td style='width: 50px;text-align: right'>"+r["parcial"]+"</td>"
//                    totalMat+=r["parcial"]+r["parcial_t"]
//                }
//                tablaMat+="</tr>"
//            }
//            if(r["parcial_t"]>0){
//                tablaTrans+="<tr>"
//                tablaTrans+="<td style='width: 80px;'>"+r["itemcdgo"]+"</td>"
//                tablaTrans+="<td>"+r["itemnmbr"]+"</td>"
//                tablaTrans+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["itempeso"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaTrans+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["rbrocntd"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaTrans+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["distancia"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaTrans+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["tarifa"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                tablaTrans+="<td style='width: 50px;text-align: right'>"+g.formatNumber(number:r["parcial_t"] ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")+"</td>"
//                total+=r["parcial_t"]
//                tablaTrans+="</tr>"
//            }
//
//        }
//        tablaTrans+="<tr><td><b>SUBTOTAL</b></td><td></td><td></td><td></td><td></td><td></td><td style='width: 50px;text-align: right'>${g.formatNumber(number:total ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")}</td></tr>"
//        tablaTrans+="</tbody></table>"
//
//        tablaHer+="<tr><td><b>SUBTOTAL</b></td><td></td><td></td><td></td><td></td><td></td><td style='width: 50px;text-align: right'>${g.formatNumber(number:totalHer,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")}</td></tr>"
//        tablaHer+="</tbody></table>"
//        tablaMano+="<tr><td><b>SUBTOTAL</b></td><td></td><td></td><td></td><td></td><td></td><td style='width: 50px;text-align: right'>${g.formatNumber(number:totalMan ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")}</td></tr>"
//        tablaMano+="</tbody></table>"
//        tablaMat+="<tr><td><b>SUBTOTAL</b></td><td></td><td></td><td></td><td></td><td></td><td style='width: 50px;text-align: right'>${g.formatNumber(number:totalMat ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5", locale: "ec")}</td></tr>"
//        tablaMat+="</tbody></table>"
//        def totalRubro=0
//        if (!params.trans){
//            totalRubro=total+totalHer+totalMan+totalMat
//        }else{
//            totalRubro=totalHer+totalMan+totalMat
//        }
//        def totalIndi=totalRubro*indi/100
//        tablaIndi+="<thead><tr><th colspan='3'>Costos indirectos</th></tr><tr><th style='width:550px'>Descripción</th><th>Porcentaje</th><th>Valor</th></tr></thead>"
//        tablaIndi+="<tbody><tr><td>Costos indirectos</td><td style='text-align:right'>${indi}%</td><td style='text-align:right'>${g.formatNumber(number:totalIndi ,format:"##,#####0", minFractionDigits:"5", maxFractionDigits:"5")}</td></tr></tbody>"
//        tablaIndi+="</table>"
//
//        if (total==0 || params.trans=="no")
//            tablaTrans=""
//        if(totalHer==0)
//            tablaHer=""
//        if(totalMan==0)
//            tablaMano=""
//        if(totalMat==0)
//            tablaMat=""
//        println "fin reporte rubro"
//        [rubro:rubro,tablaTrans:tablaTrans,tablaHer:tablaHer,tablaMano:tablaMano,tablaMat:tablaMat,tablaIndi:tablaIndi,totalRubro:totalRubro,totalIndi:totalIndi,obra: obra]
//
//
//
//    }
}
