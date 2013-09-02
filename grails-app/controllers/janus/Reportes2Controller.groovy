package janus

import com.lowagie.text.Document
import com.lowagie.text.Element
import com.lowagie.text.Font
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import janus.pac.CronogramaContrato
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.*

import java.awt.Color

class Reportes2Controller {

    def preciosService
    def dbConnectionService

    def index() {}



    def meses = ['', "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]

    private String printFecha(Date fecha) {
        if (fecha) {
            return (fecha.format("dd")+' de '+ meses[fecha.format("MM").toInteger()]+' de '+fecha.format("yyyy")).toLowerCase()
        } else {
            return "Error: no hay fecha que mostrar"
        }
    }

    private static int[] arregloEnteros(array) {
        int[] ia = new int[array.size()]
        array.eachWithIndex { it, i ->
            ia[i] = it.toInteger()
        }

        return ia
    }

//    private static void addCellTabla(PdfPTable table, paragraph, params) {
//        PdfPCell cell = new PdfPCell(paragraph);
//        if (params.height) {
//            cell.setFixedHeight(params.height.toFloat());
//        }
//        if (params.border) {
//            cell.setBorderColor(params.border);
//        }
//        if (params.bg) {
//            cell.setBackgroundColor(params.bg);
//        }
//        if (params.colspan) {
//            cell.setColspan(params.colspan);
//        }
//        if (params.align) {
//            cell.setHorizontalAlignment(params.align);
//        }
//        if (params.valign) {
//            cell.setVerticalAlignment(params.valign);
//        }
//        if (params.w) {
//            cell.setBorderWidth(params.w);
//            cell.setUseBorderPadding(true);
//        }
//        if (params.bwl) {
//            cell.setBorderWidthLeft(params.bwl.toFloat());
//            cell.setUseBorderPadding(true);
//        }
//        if (params.bwb) {
//            cell.setBorderWidthBottom(params.bwb.toFloat());
//            cell.setUseBorderPadding(true);
//        }
//        if (params.bwr) {
//            cell.setBorderWidthRight(params.bwr.toFloat());
//            cell.setUseBorderPadding(true);
//        }
//        if (params.bwt) {
//            cell.setBorderWidthTop(params.bwt.toFloat());
//            cell.setUseBorderPadding(true);
//        }
//        if (params.bcl) {
//            cell.setBorderColorLeft(params.bcl);
//        }
//        if (params.bcb) {
//            cell.setBorderColorBottom(params.bcb);
//        }
//        if (params.bcr) {
//            cell.setBorderColorRight(params.bcr);
//        }
//        if (params.bct) {
//            cell.setBorderColorTop(params.bct);
//        }
//        if (params.pl) {
//            cell.setPaddingLeft(params.pl.toFloat());
//        }
//        if (params.pr) {
//            cell.setPaddingRight(params.pr.toFloat());
//        }
//        if (params.pt) {
//            cell.setPaddingTop(params.pt.toFloat());
//        }
//        if (params.pb) {
//            cell.setPaddingBottom(params.pb.toFloat());
//        }
//        if (params.bordeTop) {
//            cell.setBorderWidthTop(1)
//            cell.setBorderWidthLeft(0)
//            cell.setBorderWidthRight(0)
//            cell.setBorderWidthBottom(0)
//            cell.setPaddingTop(7);
//
//        }
//        if (params.bordeBot) {
//            cell.setBorderWidthBottom(1)
//            cell.setBorderWidthLeft(0)
//            cell.setBorderWidthRight(0)
//            cell.setPaddingBottom(7)
//
//            if (!params.bordeTop) {
//                cell.setBorderWidthTop(0)
//            }
//        }
//
//        table.addCell(cell);
//    }


    def addCellTabla(table, paragraph, params) {
        PdfPCell cell = new PdfPCell(paragraph);
//        println "params "+params
        cell.setBorderColor(Color.BLACK);
        if (params.border) {
            if (!params.bordeBot)
                if (!params.bordeTop)
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


    private String numero(num, decimales, cero) {
        if (num == 0 && cero.toString().toLowerCase() == "hide") {
            return " ";
        }
        if (decimales == 0) {
            return formatNumber(number: num, minFractionDigits: decimales, maxFractionDigits: decimales, locale: "ec")
        } else {
            def format
            if (decimales == 2) {
                format = "##,##0"
            } else if (decimales == 3) {
                format = "##,###0"
            }
            return formatNumber(number: num, minFractionDigits: decimales, maxFractionDigits: decimales, locale: "ec", format: format)
        }
    }

    private String numero(num, decimales) {
        return numero(num, decimales, "show")
    }

    private String numero(num) {
        return numero(num, 3)
    }

    def test() {
        return [params: params]
    }

    def tablasPlanilla() {
        def planilla = Planilla.get(params.id)
        def obra = planilla.contrato.oferta.concurso.obra
        def contrato = planilla.contrato
        def oferta = contrato.oferta
        def planillas = Planilla.withCriteria {
            and {
                eq("contrato", contrato)
                or {
                    lt("fechaInicio", planilla.fechaFin)
                    isNull("fechaInicio")
                }
                order("id", "asc")
            }
        }
        def periodoOferta = PeriodosInec.findByFechaInicioLessThanEqualsAndFechaFinGreaterThanEquals(oferta.fechaEntrega, oferta.fechaEntrega)
        def periodos = []
        def data = [
                c: [:],
                p: [:]
        ]
        def pcs = FormulaPolinomicaContractual.withCriteria {
            and {
                eq("contrato", contrato)
                or {
                    ilike("numero", "c%")
                    and {
                        ne("numero", "P0")
                        ilike("numero", "p%")
                    }
                }
                order("numero", "asc")
            }
        }
        periodos.add(periodoOferta)
        planillas.each { pl ->
            if (pl.tipoPlanilla.codigo == 'A') {
                //si es anticipo: el periodo q corresponde a la fecha del anticipo
                def prin = PeriodosInec.findByFechaInicioLessThanEqualsAndFechaFinGreaterThanEquals(pl.fechaPresentacion, pl.fechaPresentacion)
                periodos.add(prin)
            } else {
                periodos.add(pl.periodoIndices)
            }
        }
        periodos.eachWithIndex { per, perNum ->
            def valRea = ValorReajuste.findAllByObraAndPeriodoIndice(obra, per)
            def tot = [c: 0, p: 0]
            valRea.each { v ->
                def c = pcs.find { it.indice == v.formulaPolinomica.indice }
                if (c) {
                    def pos = "p"
                    if (c.numero.contains("c")) {
                        pos = "c"
                    }
                    tot[pos] += (v.valor * c.valor).round(3)
//                        println "\t\t" + pos + "   " + (v.valor * c.valor)
                    if (!data[pos][per]) {
                        data[pos][per] = [valores: [], total: 0]
                    }
                    data[pos][per]["valores"].add([formulaPolinomica: c, valorReajuste: v])
                }
            }
            data["c"][per]["total"] = tot["c"]
            data["p"][per]["total"] = tot["p"]
        }//periodos.each

        def tbodyB0 = "<tbody>"
        def totC = 0
        pcs.findAll { it.numero.contains("c") }.each { c ->
            tbodyB0 += "<tr>"
            tbodyB0 += "<td>" + c.indice.descripcion + " (" + c.numero + ")</td>"
            tbodyB0 += "<td class='number'>" + elm.numero(number: c.valor, decimales: 3) + "</td>"
            totC += c.valor
            data.c.each { cp ->
                def act = cp.value.valores.find { it.formulaPolinomica.indice == c.indice }
                def val = act.valorReajuste.valor
                tbodyB0 += "<td class='number'>" + elm.numero(number: val) + "</td>"
                tbodyB0 += "<td class='number'>" + elm.numero(number: val * c.valor, decimales: 3) + "</td>"
            }
            tbodyB0 += "</tr>"
        }
        tbodyB0 += "<tr>"
        tbodyB0 += "<th>TOTALES</th>"
        tbodyB0 += "<td class='number'>" + elm.numero(number: totC, decimales: 3) + "</td>"
        data.c.each { cp ->
            tbodyB0 += "<td></td>"
            tbodyB0 += "<td class='number'>" + elm.numero(number: cp.value.total) + "</td>"
        }
        tbodyB0 += "</tr>"
        tbodyB0 += "</tbody>"

        def p0s = []
        def tbodyP0 = "<tbody>"
        def diasPlanilla = planilla.fechaFin - planilla.fechaInicio
        def valorPlanilla = planilla.valor

        def acumuladoCrono = 0, acumuladoPlan = 0

        def diasAll = 0

        periodos.eachWithIndex { per, i ->
            if (i > 0) {
                tbodyP0 += "<tr>"
                if (i == 1) {
                    tbodyP0 += "<th>ANTICIPO</th>"
                    tbodyP0 += "<th>"
                    def planillaAnticipo = Planilla.findByContratoAndTipoPlanilla(contrato, TipoPlanilla.findByCodigo("A"))
                    tbodyP0 += planillaAnticipo.fechaPresentacion.format("MMM-yy")
                    tbodyP0 += "</th>"
                    tbodyP0 += "<td colspan='4'></td>"
                    tbodyP0 += "<td class='number'>"
                    tbodyP0 += elm.numero(number: planillaAnticipo.valor)
                    tbodyP0 += "</td>"

                    def p0 = FormulaPolinomicaContractual.findByContratoAndNumero(contrato, "P0")
                    def vrP0 = ValorReajuste.findByPeriodoIndiceAndFormulaPolinomica(per, p0)
                    data["p"][per]["p0"] = vrP0.valor
                    p0s[i - 1] = vrP0.valor
                } else {
//                    def periodosEjecucion = PeriodoEjecucion.findAllByObra(obra)
                    def periodosEjecucion = PeriodoEjecucion.withCriteria {
                        and {
                            eq("obra", obra)
                            or {
                                between("fechaInicio", per.fechaInicio, per.fechaFin)
                                between("fechaFin", per.fechaInicio, per.fechaFin)
                            }
                            order("fechaInicio")
                        }
                    }
                    def diasTotal = 0, valorTotal = 0
//                    println per.fechaInicio.format("dd-MM-yyyy") + "  " + per.fechaFin.format("dd-MM-yyyy")
                    tbodyP0 += "<th>"
                    tbodyP0 += per.descripcion
                    tbodyP0 += "</th>"
                    periodosEjecucion.each { pe ->
//                        println "\t" + pe.tipo + "  " + pe.fechaInicio.format("dd-MM-yyyy") + "   " + pe.fechaFin.format("dd-MM-yyyy")
                        if (pe.tipo == "P") {
                            def diasUsados
                            def diasPeriodo = pe.fechaFin - pe.fechaInicio
//                            println "\t\tdias periodo: " + diasPeriodo
                            def crono = CronogramaEjecucion.findAllByPeriodo(pe)
                            def valorPeriodo = crono.sum { it.precio }
//                            println "\t\tvalor periodo: " + valorPeriodo
                            if (pe.fechaInicio <= per.fechaInicio) {
                                diasUsados = pe.fechaFin - per.fechaInicio
                                if (diasUsados == 0) diasUsados = 1
                                diasTotal += diasUsados
//                                println "\t\tdias usados: " + diasUsados
                            } else if (pe.fechaInicio > per.fechaInicio && pe.fechaFin < per.fechaFin) {
                                diasUsados = pe.fechaFin - pe.fechaInicio
                                if (diasUsados == 0) diasUsados = 1
                                diasTotal += diasUsados
//                                println "\t\tdias usados: " + diasUsados
                            } else if (pe.fechaFin >= per.fechaFin) {
                                diasUsados = per.fechaFin - pe.fechaInicio
                                if (diasUsados == 0) diasUsados = 1
                                diasTotal += diasUsados
//                                println "\t\tdias usados: " + diasUsados
                            }
                            def valorUsado = (valorPeriodo / diasPeriodo) * diasUsados
                            valorTotal += valorUsado
//                            println "\t\tvalor usado: " + valorUsado
                        }
                    }
                    acumuladoCrono += valorTotal
                    def planillado = (valorPlanilla / diasPlanilla) * diasTotal
                    acumuladoPlan += planillado
//                    println "TOTAL: " + diasTotal + " dias"
//                    println "PLANILLADO: " + planillado

                    def p0 = FormulaPolinomicaContractual.findByContratoAndNumero(contrato, "P0")
                    def vrP0 = ValorReajuste.findByPeriodoIndiceAndFormulaPolinomica(per, p0)
                    data["p"][per]["p0"] = vrP0.valor
                    p0s[i - 1] = vrP0.valor
                    diasAll += diasTotal
                    tbodyP0 += "<th>"
                    tbodyP0 += "(" + diasTotal + ")"
                    tbodyP0 += "</th>"
                    tbodyP0 += "<td class='number'>" + elm.numero(number: valorTotal) + "</td>"
                    tbodyP0 += "<td class='number'>" + elm.numero(number: acumuladoCrono) + "</td>"
                    tbodyP0 += "<td class='number'>" + elm.numero(number: planillado) + "</td>"
                    tbodyP0 += "<td class='number'>" + elm.numero(number: acumuladoPlan) + "</td>"
                    tbodyP0 += "<td class='number'>" + elm.numero(number: vrP0.valor) + "</td>"
                }
                tbodyP0 += "</tr>"
            }
        }
        tbodyP0 += "</tbody>"
        tbodyP0 += "<tfoot>"
        tbodyP0 += "<tr>"
        tbodyP0 += "<th>TOTAL</th>"
        tbodyP0 += "<th>(" + diasAll + ")</th>"
        tbodyP0 += "<td></td>"
        tbodyP0 += "<td class='number bold'>" + elm.numero(number: acumuladoCrono) + "</td>"
        tbodyP0 += "<td></td>"
        tbodyP0 += "<td class='number bold'>" + elm.numero(number: acumuladoPlan) + "</td>"
        tbodyP0 += "<td></td>"
        tbodyP0 += "</tr>"
        tbodyP0 += "</tfoot>"

        def a = 0, b = 0, c = 0, d = 0, tots = []
        def tbodyFr = "<tbody>"
        pcs.findAll { it.numero.contains('p') }.eachWithIndex { p, i ->
            tbodyFr += "<tr>"
            tbodyFr += "<td>" + p.indice.descripcion + " (" + p.numero + ")</td>"

            data.p.eachWithIndex { cp, j ->
                def act = cp.value.valores.find { it.formulaPolinomica.indice == p.indice }
                if (j == 0) {
                    c = act.formulaPolinomica.valor
                    b = act.valorReajuste.valor
                    tbodyFr += "<td class='number'>"
                    tbodyFr += "<div>"
                    tbodyFr += elm.numero(number: c, decimales: 3)
                    tbodyFr += "</div>"
                    tbodyFr += "<div class='bold'>"
                    tbodyFr += elm.numero(number: b, decimales: 3)
                    tbodyFr += "</div>"
                    tbodyFr += "</td>"
                } //j==0
                else {
                    a = act.valorReajuste.valor
                    d = (a / b) * c
                    tbodyFr += "<td class='number'>"
                    tbodyFr += "<div>"
                    tbodyFr += elm.numero(number: a, decimales: 3)
                    tbodyFr += "</div>"
                    tbodyFr += "<div class='bold'>"
                    tbodyFr += elm.numero(number: d, decimales: 3)
                    tbodyFr += "</div>"
                    tbodyFr += "</td>"
                    if (!tots[j - 1]) {
                        tots[j - 1] = [
                                per: cp.key,
                                total: 0
                        ]
                    }
                    tots[j - 1].total += d
                }
            } //data.p.each
            tbodyFr += "</tr>"
        } //pcs.p.each

        def filaFr = "", filaFr1 = "", filaP0 = "", filaPr = ""
//        println ">>>"
//        println p0s

        def totalReajuste = 0

        tots.eachWithIndex { t, i ->
            def pr = (t.total - 1) * p0s[i]
            totalReajuste += pr
            filaFr += "<td class='number'>" + elm.numero(number: t.total, decimales: 3) + "</td>"
            filaFr1 += "<td class='number'>" + elm.numero(number: t.total - 1, decimales: 3) + "</td>"
            filaP0 += "<td class='number'>" + elm.numero(number: p0s[i]) + "</td>"
            filaPr += "<td class='number'>" + elm.numero(number: pr) + "</td>"
        }
        tbodyFr += "</tbody>"
        tbodyFr += "<tfoot>"

        tbodyFr += "<tr>"
        tbodyFr += "<th rowspan='4'>1.000</th>"
        tbodyFr += "<th>F<sub>r</sub></th>"
        tbodyFr += filaFr
        tbodyFr += "</tr>"

        tbodyFr += "<tr>"
        tbodyFr += "<th>F<sub>r</sub>-1</th>"
        tbodyFr += filaFr1
        tbodyFr += "</tr>"

        tbodyFr += "<tr>"
        tbodyFr += "<th>P<sub>0</sub></th>"
        tbodyFr += filaP0
        tbodyFr += "</tr>"

        tbodyFr += "<tr>"
        tbodyFr += "<th>P<sub>r</sub>-P</th>"
        tbodyFr += filaPr
        tbodyFr += "</tr>"

        tbodyFr += "<tr>"
        tbodyFr += "<th colspan='2'>REAJUSTE TOTAL</th>"
        tbodyFr += "<td colspan='2' class='number bold'>"
        tbodyFr += elm.numero(number: totalReajuste)
        tbodyFr += "</td>"
        tbodyFr += "</tr>"

        tbodyFr += "</tfoot>"

        return [tbodyFr: tbodyFr, tbodyP0: tbodyP0, tbodyB0: tbodyB0, planilla: planilla, obra: obra, oferta: oferta, contrato: contrato, pcs: pcs, data: data, periodos: periodos]

    } //tablasPlanilla()


    def reportePrecios() {
//        params.orden = "a" //a,n    Alfabetico | Numerico
//        params.col = ["t", "u", "p", "f"] //t,u,p,f   Transporte | Unidad | Precio | Fecha de Act
//        params.fecha = "22-11-2012"
//        params.lugar = "4"
//        params.grupo = "1"
//
//        println params

        def orden = "itemnmbr"
        if (params.orden == "n") {
            orden = "itemcdgo"
        }
        def lugar = Lugar.get(params.lugar.toLong())
        def fecha = new Date().parse("dd-MM-yyyy", params.fecha)
        def items = ""
        def lista = Item.withCriteria {
            eq("tipoItem", TipoItem.findByCodigo("I"))
            departamento {
                subgrupo {
                    eq("grupo", Grupo.get(params.grupo.toLong()))
                }
            }
        }
        lista.id.each {
            if (items != "") {
                items += ","
            }
            items += it
        }
        def res = []
//        println items
        def tmp = preciosService.getPrecioRubroItemOrder(fecha, lugar, items, orden, "asc")
        tmp.each {
            res.add(PrecioRubrosItems.get(it))
        }

        return [lugar: lugar, cols: params.col, precios: res]
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    def reporteCronogramaPdf() {

        println("--Z" + params)

        def tipo = "obra"
//        def obra = null, contrato = null, lbl = ""
//        switch (tipo) {
//            case "obra":
//                obra = Obra.get(params.id.toLong())
//                lbl = " la obra"
//                break;
//            case "contrato":
//                contrato = Contrato.get(params.id)
//                obra = contrato.obra
//                lbl = "l contrato de la obra"
//                break;
//        }

        def contrato = null, lbl =""

        def obra = Obra.get(params.id)

        def sql = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

        def cn = dbConnectionService.getConnection()

        def conc = cn.rows(sql.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)


        def firma = Persona.get(session.usuario.id).firma


        def oferente = Persona.get(session.usuario.id)



        def meses = obra.plazoEjecucionMeses + (obra.plazoEjecucionDias > 0 ? 1 : 0)

        def detalle = VolumenesObra.findAllByObra(obra, [sort: "orden"])

        def precios = [:]

        def indirecto = obra.totales / 100

        preciosService.ac_rbroObra(obra.id)

        detalle.each {
            it.refresh()
            def res = preciosService.precioUnitarioVolumenObraSinOrderBy("sum(parcial)+sum(parcial_t) precio ", obra.id, it.item.id)
//            println("-->>" + res)

            precios.put(it.id.toString(), (res["precio"][0] + res["precio"][0] * indirecto).toDouble().round(2))
        }

        def baos = new ByteArrayOutputStream()

        def name = "cronograma${tipo.capitalize()}_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";

        com.lowagie.text.Font catFont = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font catFont2 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 14, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font catFont3 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font catFont4 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font info = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL)
        com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 9, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font fontTh = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font fontTh2 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font fontTd = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL);

        Document document
        document = new Document(PageSize.A4.rotate());
        document.setMargins(45.2, 30, 36.2, 56.2);
        def pdfw = PdfWriter.getInstance(document, baos);
        document.open();
        PdfContentByte cb = pdfw.getDirectContent();
        document.addTitle("Cronograma de${lbl} " + obra.nombre + " " + new Date().format("dd_MM_yyyy"));
        document.addSubject("Generado por el sistema Janus");
        document.addKeywords("reporte, janus, planillas");
        document.addAuthor("Janus");
        document.addCreator("Tedein SA");

        /* ***************************************************** Titulo del reporte *******************************************************/
//        Paragraph preface = new Paragraph();
//        addEmptyLine(preface, 1);
//        preface.setAlignment(Element.ALIGN_CENTER);
//
//        preface.add(new Paragraph("FORMULARIO N: 11", catFont3));
//        preface.add(new Paragraph("NOMBRE DEL OFERENTE: " +  session.usuario, catFont3));
//        preface.add(new Paragraph("PROCESO: " + concurso?.codigo, catFont3));
//        preface.add(new Paragraph("G.A.D. PROVINCIA DE PICHINCHA", catFont3));
//        preface.add(new Paragraph("CRONOGRAMA VALORADO DE TRABAJO", catFont3));
//        preface.add(new Paragraph("PROYECTO: " + obra?.nombre, catFont3));
//        addEmptyLine(preface, 1);
//        document.add(preface);





        Paragraph headers = new Paragraph();
        addEmptyLine(headers, 1);
        headers.setAlignment(Element.ALIGN_CENTER);
        headers.add(new Paragraph("FORMULARIO N° 11", catFont4));
//        Paragraph nombreOferente = new Paragraph();
//        addEmptyLine(nombreOferente, 1);
//        nombreOferente.setAlignment(Element.ALIGN_LEFT);
//        nombreOferente.setIndentationLeft(25)
//        nombreOferente.add(new Paragraph("NOMBRE DEL OFERENTE: " + oferente?.nombre?.toUpperCase() + " " + oferente?.apellido?.toUpperCase(), catFont4));
        Paragraph proceso = new Paragraph();
        addEmptyLine(proceso, 1);
        proceso.setAlignment(Element.ALIGN_CENTER);
        proceso.add(new Paragraph("PROCESO: " + concurso?.codigo, catFont4));
        Paragraph analisis = new Paragraph();
        addEmptyLine(analisis, 1);
        analisis.setAlignment(Element.ALIGN_LEFT);
        analisis.setIndentationLeft(25)
        analisis.add(new Paragraph("NOMBRE DEL OFERENTE: " + oferente?.nombre?.toUpperCase() + " " + oferente?.apellido?.toUpperCase(), catFont4));
        addEmptyLine(analisis, 1);
        analisis.add(new Paragraph("CRONOGRAMA VALORADO DE TRABAJO", catFont4));
        addEmptyLine(analisis, 1);
        analisis.add(new Paragraph("PROYECTO: " + obra?.nombre, catFont4));
        addEmptyLine(analisis, 1);

        document.add(headers);
//        document.add(nombreOferente)
        document.add(proceso)
        document.add(analisis)




        /* ***************************************************** Fin Titulo del reporte ***************************************************/
        /* ***************************************************** Tabla cronograma *********************************************************/
        def tams = [10, 40, 5, 6, 6, 6, 2]
        meses.times {
            tams.add(7)
        }
        tams.add(10)

        PdfPTable tabla = new PdfPTable(8 + meses);
        tabla.setWidthPercentage(100);
        tabla.setWidths(arregloEnteros(tams))
        tabla.setWidthPercentage(100);

        addCellTabla(tabla, new Paragraph("Código", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("Rubro", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("Unidad", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("Cantidad", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("P. Unitario", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("C. Total", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("T.", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        meses.times { i ->
            addCellTabla(tabla, new Paragraph("Mes " + (i + 1), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        }
        addCellTabla(tabla, new Paragraph("Total Rubro", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])

//        addCellTabla(tabla, new Paragraph("Código", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        addCellTabla(tabla, new Paragraph("Rubro", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        addCellTabla(tabla, new Paragraph("Unidad", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        addCellTabla(tabla, new Paragraph("Cantidad", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        addCellTabla(tabla, new Paragraph("P. Unitario", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        addCellTabla(tabla, new Paragraph("C. Total", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        addCellTabla(tabla, new Paragraph("T.", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        meses.times { i ->
//            addCellTabla(tabla, new Paragraph("Mes " + (i + 1), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
//        }
//        addCellTabla(tabla, new Paragraph("Total Rubro", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])

        tabla.setHeaderRows(1)
//        tabla.setHeaderRows(2)
//        tabla.setFooterRows(1)

        def totalMes = []
        def sum = 0
        def borderWidth = 2

        detalle.eachWithIndex { vol, s ->
            def cronos
            switch (tipo) {
                case "obra":
                    cronos = Cronograma.findAllByVolumenObra(vol)
                    break;
                case "contrato":
                    cronos = CronogramaContrato.findAllByVolumenObra(vol)
                    break;
            }
            def totalDolRow = 0, totalPrcRow = 0, totalCanRow = 0
            def parcial = precios[vol.id.toString()] * vol.cantidad
            sum += parcial

            addCellTabla(tabla, new Paragraph(vol.item.codigo, fontTd), [border: Color.BLACK, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE])
            addCellTabla(tabla, new Paragraph(vol.item.nombre, fontTd), [border: Color.BLACK, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE])
            addCellTabla(tabla, new Paragraph(vol.item.unidad.codigo, fontTd), [border: Color.BLACK, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
            addCellTabla(tabla, new Paragraph(numero(vol.cantidad, 2), fontTd), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
            addCellTabla(tabla, new Paragraph(numero(precios[vol.id.toString()], 2), fontTd), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
            addCellTabla(tabla, new Paragraph(numero(parcial, 2), fontTd), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
            addCellTabla(tabla, new Paragraph('$', fontTd), [border: Color.BLACK, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE])
            meses.times { i ->
                def prec = cronos.find { it.periodo == i + 1 }
                totalDolRow += (prec ? prec.precio : 0)
                if (!totalMes[i]) {
                    totalMes[i] = 0
                }
                totalMes[i] += (prec ? prec.precio : 0)
                addCellTabla(tabla, new Paragraph(numero(prec?.precio, 2), fontTd), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
            }
            addCellTabla(tabla, new Paragraph(numero(totalDolRow, 2) + ' $', fontTh), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])

            addCellTabla(tabla, new Paragraph(' ', fontTd), [border: Color.BLACK, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE, colspan: 6])
            addCellTabla(tabla, new Paragraph('%', fontTd), [border: Color.BLACK, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE])
            meses.times { i ->
                def porc = cronos.find { it.periodo == i + 1 }
                totalPrcRow += (porc ? porc.porcentaje : 0)
                addCellTabla(tabla, new Paragraph(numero(porc?.porcentaje, 2), fontTd), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
            }
            addCellTabla(tabla, new Paragraph(numero(totalPrcRow, 2) + ' %', fontTh), [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])

            addCellTabla(tabla, new Paragraph(' ', fontTd), [border: Color.BLACK, bwb: borderWidth, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE, colspan: 6])
            addCellTabla(tabla, new Paragraph('F', fontTd), [border: Color.BLACK, bwb: borderWidth, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE])
            meses.times { i ->
                def cant = cronos.find { it.periodo == i + 1 }
                totalCanRow += (cant ? cant.cantidad : 0)
                addCellTabla(tabla, new Paragraph(numero(cant?.cantidad, 2), fontTd), [border: Color.BLACK, bwb: borderWidth, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
            }
            addCellTabla(tabla, new Paragraph(numero(totalCanRow, 2) + ' F', fontTh), [border: Color.BLACK, bwb: borderWidth, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
        }

        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("TOTAL PARCIAL", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE, colspan: 4])
        addCellTabla(tabla, new Paragraph(numero(sum, 2), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("T", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        meses.times { i ->
            addCellTabla(tabla, new Paragraph(numero(totalMes[i], 2), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
        }
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])

        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("TOTAL ACUMULADO", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE, colspan: 4])
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("T", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        def acu = 0
        meses.times { i ->
            acu += totalMes[i]
            addCellTabla(tabla, new Paragraph(numero(acu, 2), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
        }
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])

        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("% PARCIAL", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE, colspan: 4])
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("T", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        meses.times { i ->
            def prc = 100 * totalMes[i] / sum
            addCellTabla(tabla, new Paragraph(numero(prc, 2), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
        }
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])

        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("% ACUMULADO", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE, colspan: 4])
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        addCellTabla(tabla, new Paragraph("T", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])
        acu = 0
        meses.times { i ->
            def prc = 100 * totalMes[i] / sum
            acu += prc
            addCellTabla(tabla, new Paragraph(numero(acu, 2), fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE])
        }
        addCellTabla(tabla, new Paragraph(" ", fontTh), [border: Color.BLACK, bg: Color.LIGHT_GRAY, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE])

        document.add(tabla)
        /* ***************************************************** Fin Tabla cronograma *****************************************************/

        PdfPTable pieTabla = new PdfPTable(2);
        pieTabla.setWidthPercentage(100);
        pieTabla.setWidths(arregloEnteros([99, 1]))

        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph("Quito, " + printFecha(concurso?.fechaLimiteEntregaOfertas), fontTh2), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph("________________________________________ ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        addCellTabla(pieTabla, new Paragraph(firma, fontTh2), [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])
        addCellTabla(pieTabla, new Paragraph(" ", fontTh),[border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT])

        document.add(pieTabla);

        document.close();

        pdfw.close()
        byte[] b = baos.toByteArray();
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=" + name)
        response.setContentLength(b.length)
        response.getOutputStream().write(b)

    }



    def reporteExcelComposicion1() {

        def obra = Obra.get(params.id)


        params.tipo = "1,2,3"

        def sql = "SELECT\n" +
                "  v.voit__id                            id,\n" +
                "  i.itemcdgo                            codigo,\n" +
                "  i.itemnmbr                            item,\n" +
                "  u.unddcdgo                            unidad,\n" +
                "  v.voitcntd                            cantidad,\n" +
                "  v.voitpcun                            punitario,\n" +
                "  v.voittrnp                            transporte,\n" +
                "  v.voitpcun                            costo,\n" +
                "  (v.voitpcun)*v.voitcntd               total,\n" +
                "  d.dprtdscr                            departamento,\n" +
                "  s.sbgrdscr                            subgrupo,\n" +
                "  g.grpodscr                            grupo,\n" +
                "  g.grpo__id                            grid\n" +
                "FROM vlobitem v\n" +
                "INNER JOIN item i ON v.item__id = i.item__id\n" +
                "INNER JOIN undd u ON i.undd__id = u.undd__id\n" +
                "INNER JOIN dprt d ON i.dprt__id = d.dprt__id\n" +
                "INNER JOIN sbgr s ON d.sbgr__id = s.sbgr__id\n" +
                "INNER JOIN grpo g ON s.grpo__id = g.grpo__id AND g.grpo__id IN (${params.tipo})\n" +
                "WHERE v.obra__id = ${params.id} \n" +
                "  ORDER BY grid ASC"

        def cn = dbConnectionService.getConnection()

        def res = cn.rows(sql.toString())

//        println(res)

        //excel
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
        sheet.setColumnView(0, 20)
        sheet.setColumnView(1, 60)
        sheet.setColumnView(2, 10)
        sheet.setColumnView(3, 20)
        sheet.setColumnView(4, 20)
        sheet.setColumnView(5, 20)
        sheet.setColumnView(6, 20)
        sheet.setColumnView(7, 25)
//        sheet.setColumnView(8, 25)

        def label
        def number
        def fila = 8;
        def totalE = 0;
        def totalM = 0;
        def totalMO = 0;
        def totalEquipo = 0;
        def totalManoObra = 0;
        def totalMaterial = 0;
        def totalDirecto = 0;
        def ultimaFila

        label = new Label(2, 4, "Composición de " + obra?.nombre, times16format); sheet.addCell(label);

        label = new Label(0, 6, "CODIGO", times16format); sheet.addCell(label);
        label = new Label(1, 6, "ITEM", times16format); sheet.addCell(label);
        label = new Label(2, 6, "UNIDAD", times16format); sheet.addCell(label);
        label = new Label(3, 6, "CANTIDAD", times16format); sheet.addCell(label);
        label = new Label(4, 6, "P.UNITARIO", times16format); sheet.addCell(label);
//        label = new Label(5, 6, "TRANSPORTE", times16format); sheet.addCell(label);
        label = new Label(5, 6, "COSTO", times16format); sheet.addCell(label);
        label = new Label(6, 6, "TOTAL", times16format); sheet.addCell(label);
        label = new Label(7, 6, "TIPO", times16format); sheet.addCell(label);

        res.each {


            label = new Label(0, fila, it?.codigo.toString()); sheet.addCell(label);
            label = new Label(1, fila, it?.item.toString()); sheet.addCell(label);
            label = new Label(2, fila, it?.unidad.toString()); sheet.addCell(label);
            number = new jxl.write.Number(3, fila, it?.cantidad); sheet.addCell(number);
            number = new jxl.write.Number(4, fila, it?.punitario); sheet.addCell(number);
//            number = new jxl.write.Number(5, fila, it?.transporte); sheet.addCell(number);
            number = new jxl.write.Number(5, fila, it?.costo); sheet.addCell(number);
            number = new jxl.write.Number(6, fila, it?.total); sheet.addCell(number);
            label = new Label(7, fila, it?.grupo.toString()); sheet.addCell(label);

            fila++

            if (it?.grid == 1) {
                totalMaterial = (totalM += it?.total)
            }
            if (it?.grid == 2) {
                totalManoObra = (totalMO += it?.total)
            }
            if (it?.grid == 3) {
                totalEquipo = (totalE += it?.total)
            }
            totalDirecto = totalEquipo + totalManoObra + totalMaterial;
            ultimaFila = fila
        }

        label = new Label(6, ultimaFila, "Total Materiales: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila, totalMaterial); sheet.addCell(number);

        label = new Label(6, ultimaFila + 1, "Total Mano de Obra: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila + 1, totalManoObra); sheet.addCell(number);

        label = new Label(6, ultimaFila + 2, "Total Equipos: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila + 2, totalEquipo); sheet.addCell(number);

        label = new Label(6, ultimaFila + 3, "TOTAL DIRECTO: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila + 3, totalDirecto); sheet.addCell(number);

        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "ComposicionExcel.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());


    }


    def reportePreciosExcel() {

        def orden = "itemnmbr"
        if (params.orden == "n") {
            orden = "itemcdgo"
        }
        def lugar = Lugar.get(params.lugar.toLong())
        def fecha = new Date().parse("dd-MM-yyyy", params.fecha)
        def items = ""
        def lista = Item.withCriteria {
            eq("tipoItem", TipoItem.findByCodigo("I"))
            departamento {
                subgrupo {
                    eq("grupo", Grupo.get(params.grupo.toLong()))
                }
            }
        }
        lista.id.each {
            if (items != "") {
                items += ","
            }
            items += it
        }
        def res = []
//        println items
        def tmp = preciosService.getPrecioRubroItemOrder(fecha, lugar, items, orden, "asc")
        tmp.each {
            res.add(PrecioRubrosItems.get(it))
        }

        //excel

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
        sheet.setColumnView(0, 20)
        sheet.setColumnView(1, 60)
        sheet.setColumnView(2, 15)
        sheet.setColumnView(3, 20)
        sheet.setColumnView(4, 20)
        sheet.setColumnView(5, 20)
        sheet.setColumnView(6, 25)


        def label
        def number
        def fila = 8;

        label = new Label(2, 1, "Gobierno Autónomo Descentralizado de la Provincia de Pichincha".toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(2, 2, "Reporte de Costos de Materiales", times16format); sheet.addCell(label);

        label = new Label(1, 4, lugar?.descripcion, times16format); sheet.addCell(label);
        label = new Label(4, 4, "Fecha Consulta: " + new Date().format("dd-MM-yyyy"), times16format); sheet.addCell(label);


        label = new Label(0, 6, "CODIGO", times16format); sheet.addCell(label);
        label = new Label(1, 6, "MATERIAL", times16format); sheet.addCell(label);
        label = new Label(2, 6, "UNIDAD", times16format); sheet.addCell(label);
        label = new Label(3, 6, "PESO/VOL", times16format); sheet.addCell(label);
        label = new Label(4, 6, "COSTO", times16format); sheet.addCell(label);
        label = new Label(5, 6, "FECHA ACT.", times16format); sheet.addCell(label);

        res.each {

            label = new Label(0, fila, it?.item?.codigo.toString()); sheet.addCell(label);
            label = new Label(1, fila, it?.item?.nombre.toString()); sheet.addCell(label);
            label = new Label(2, fila, it?.item?.unidad?.codigo.toString()); sheet.addCell(label);
            number = new jxl.write.Number(3, fila, it?.item?.peso); sheet.addCell(number);
            number = new jxl.write.Number(4, fila, it?.precioUnitario); sheet.addCell(number);
            label = new Label(5, fila, it?.fecha.format("dd-MM-yyyy")); sheet.addCell(label);

            fila++

        }


        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "MantenimientoPreciosExcel.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());


    }


    def reporteComposicion () {

        def obra = Obra.get(params.id)

        def totales

        def valorTotal

        def total1 = 0

        def totalesMano
        def valorTotalMano
        def total2 = 0

        def totalesEquipos
        def valorTotalEquipos
        def total3 = 0


        def sql4 = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

        def cn4 = dbConnectionService.getConnection()

        def conc = cn4.rows(sql4.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)



        if (!params.tipo) {
            params.tipo = "-1"
        }
        if (!params.rend) {
            params.rend = "screen"
        }
        if (!params.sp) {
            params.sp = '-1'
        }
        if (params.tipo == "-1") {
            params.tipo = "1,2,3"
        }
        def wsp = ""
        if (params.sp.toString() != "-1") {
            wsp = "      AND v.sbpr__id = ${params.sp} \n"
        }



        def sql = "SELECT i.itemcdgo codigo, i.itemnmbr item, u.unddcdgo unidad, sum(v.voitcntd) cantidad, \n" +
                "v.voitpcun punitario, v.voittrnp transporte, v.voitpcun + v.voittrnp  costo, \n" +
                "sum((v.voitpcun + v.voittrnp) * v.voitcntd)  total, g.grpodscr grupo, g.grpo__id grid \n" +
                "FROM vlobitem v INNER JOIN item i ON v.item__id = i.item__id\n" +
                "INNER JOIN undd u ON i.undd__id = u.undd__id\n" +
                "INNER JOIN dprt d ON i.dprt__id = d.dprt__id\n" +
                "INNER JOIN sbgr s ON d.sbgr__id = s.sbgr__id\n" +
                "INNER JOIN grpo g ON s.grpo__id = g.grpo__id AND g.grpo__id IN (${params.tipo}) \n" +
                "WHERE v.obra__id = ${params.id} and v.voitcntd >0 \n" + wsp +
                "group by i.itemcdgo, i.itemnmbr, u.unddcdgo, v.voitpcun, v.voittrnp, v.voitpcun, \n" +
                "g.grpo__id, g.grpodscr " +
                "ORDER BY g.grpo__id ASC, i.itemcdgo"


        def cn = dbConnectionService.getConnection()
        def res = cn.rows(sql.toString())


        def baos = new ByteArrayOutputStream()
        def name = "composicion_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
        Font times12bold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
        Font times10bold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
        Font times18bold = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
        Font times14bold = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
        Font times8bold = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
        Font times8normal = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL)
        Font times10boldWhite = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
        Font times8boldWhite = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
        times8boldWhite.setColor(Color.BLACK)
        times10boldWhite.setColor(Color.BLACK)
        def fonts = [times12bold: times12bold, times10bold: times10bold, times8bold: times8bold,
                times10boldWhite: times10boldWhite, times8boldWhite: times8boldWhite, times8normal: times8normal]

        Document document
        document = new Document(PageSize.A4);
        def pdfw = PdfWriter.getInstance(document, baos);
        document.open();
        document.addTitle("Composicion " + new Date().format("dd_MM_yyyy"));
        document.addSubject("Generado por el sistema Janus");
        document.addKeywords("reporte, janus, composicion");
        document.addAuthor("Janus");
        document.addCreator("Tedein SA");

        def prmsHeaderHoja = [border: Color.WHITE]
        def prmsHeader = [border: Color.WHITE, colspan: 7,
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsRight = [border: Color.WHITE, colspan: 7,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_RIGHT]
        def prmsHeader2 = [border: Color.WHITE, colspan: 3,
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellHead = [border: Color.WHITE,
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellHead3 = [border: Color.WHITE,
                align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT]
        def prmsCellHead2 = [border: Color.WHITE,
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE, bordeTop: "1", bordeBot: "1"]
        def prmsCellIzquierda = [border: Color.WHITE,
                align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT]
        def prmsCellDerecha = [border: Color.WHITE,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_RIGHT]
        def prmsCellDerecha2 = [border: Color.WHITE,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_RIGHT, bordeTop: "1", bordeBot: "1"]
        def prmsCellCenter = [border: Color.WHITE, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellLeft = [border: Color.WHITE, valign: Element.ALIGN_MIDDLE]
        def prmsSubtotal = [border: Color.WHITE, colspan: 6,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
        def prmsNum = [border: Color.WHITE, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]

        def prms = [prmsHeaderHoja: prmsHeaderHoja, prmsHeader: prmsHeader, prmsHeader2: prmsHeader2,
                prmsCellHead: prmsCellHead, prmsCell: prmsCellCenter, prmsCellLeft: prmsCellLeft, prmsSubtotal: prmsSubtotal, prmsNum: prmsNum, prmsRight: prmsRight,
                prmsCellDerecha: prmsCellDerecha, prmsCellIzquierda: prmsCellIzquierda]

        Paragraph headersTitulo = new Paragraph();
        addEmptyLine(headersTitulo, 1);
        headersTitulo.setAlignment(Element.ALIGN_CENTER);
        headersTitulo.add(new Paragraph("G.A.D. PROVINCIA DE PICHINCHA", times18bold));
        headersTitulo.add(new Paragraph("COMPOSICIÓN", times14bold));
//        headersTitulo.add(new Paragraph(obra?.departamento?.direccion?.nombre, times12bold));
        headersTitulo.add(new Paragraph("", times12bold));
        document.add(headersTitulo)

        PdfPTable header = new PdfPTable(3)
        header.setWidthPercentage(100)
        header.setWidths(arregloEnteros([10,2,65]))

        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)

        addCellTabla(header, new Paragraph("PROYECTO", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph(" : ", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph(obra?.nombre, times8bold), prmsCellHead3)

        addCellTabla(header, new Paragraph("PROCESO", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph(" : ", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph(concurso?.codigo, times8bold), prmsCellHead3)

        addCellTabla(header, new Paragraph("FECHA", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph(" : ", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph(printFecha(concurso?.fechaLimiteEntregaOfertas), times8bold), prmsCellHead3)

        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)

        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)
        addCellTabla(header, new Paragraph("", times8bold), prmsCellHead3)

        document.add(header);


        PdfPTable tablaHeader = new PdfPTable(8)
        tablaHeader.setWidthPercentage(100)
        tablaHeader.setWidths(arregloEnteros([12, 36, 5, 9, 9, 9, 10, 10]))

        PdfPTable tablaTitulo = new PdfPTable(2)
        tablaTitulo.setWidthPercentage(100)
        tablaTitulo.setWidths(arregloEnteros([90, 10]))

        PdfPTable tablaComposicion = new PdfPTable(8)
        tablaComposicion.setWidthPercentage(100)
        tablaComposicion.setWidths(arregloEnteros([12, 36, 5, 9, 9, 9, 10, 10]))

        PdfPTable tablaTotales = new PdfPTable(2)
        tablaTotales.setWidthPercentage(100)
        tablaTotales.setWidths(arregloEnteros([70, 30]))


        addCellTabla(tablaHeader, new Paragraph("Código", times8bold), prmsCellHead2)
        addCellTabla(tablaHeader, new Paragraph("Item", times8bold), prmsCellHead2)
        addCellTabla(tablaHeader, new Paragraph("U", times8bold), prmsCellHead2)
        addCellTabla(tablaHeader, new Paragraph("Cantidad", times8bold), prmsCellHead2)
        addCellTabla(tablaHeader, new Paragraph("Precio Unitario", times8bold), prmsCellDerecha2)
        addCellTabla(tablaHeader, new Paragraph("Transporte", times8bold), prmsCellDerecha2)
        addCellTabla(tablaHeader, new Paragraph("Costo", times8bold), prmsCellDerecha2)
        addCellTabla(tablaHeader, new Paragraph("Total", times8bold), prmsCellDerecha2)


        addCellTabla(tablaTitulo, new Paragraph("Materiales ", times14bold), prmsCellIzquierda)
        addCellTabla(tablaTitulo, new Paragraph(" ", times10bold), prmsCellIzquierda)



        res.each { r ->

            if (r?.grid == 1) {

                addCellTabla(tablaComposicion, new Paragraph(r?.codigo, times8normal), prmsCellIzquierda)
                addCellTabla(tablaComposicion, new Paragraph(r?.item, times8normal), prmsCellIzquierda)
                addCellTabla(tablaComposicion, new Paragraph(r?.unidad, times8normal), prmsCellHead)
                addCellTabla(tablaComposicion, new Paragraph(g.formatNumber(number: r?.cantidad, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion, new Paragraph(g.formatNumber(number: r?.punitario, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion, new Paragraph(g.formatNumber(number: r?.transporte, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion, new Paragraph(g.formatNumber(number: r?.costo, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion, new Paragraph(g.formatNumber(number: r?.total, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)

                totales = r?.total

                valorTotal = (total1 += totales)
            }

        }

        addCellTabla(tablaTotales, new Paragraph("Total Materiales", times10bold), prmsCellDerecha)
        addCellTabla(tablaTotales, new Paragraph(g.formatNumber(number: valorTotal, minFractionDigits:
                3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times10bold), prmsNum)

        addCellTabla(tablaTotales, new Paragraph(" ", times10bold), prmsNum)
        addCellTabla(tablaTotales, new Paragraph(" ", times10bold), prmsNum)




        PdfPTable tablaTitulo2 = new PdfPTable(2)
        tablaTitulo2.setWidthPercentage(100)
        tablaTitulo2.setWidths(arregloEnteros([90, 10]))

        PdfPTable tablaComposicion2 = new PdfPTable(8)
        tablaComposicion2.setWidthPercentage(100)
        tablaComposicion2.setWidths(arregloEnteros([12, 36, 5, 9, 9, 9, 10, 10]))

        PdfPTable tablaTotalesMano = new PdfPTable(2)
        tablaTotalesMano.setWidthPercentage(100)
        tablaTotalesMano.setWidths(arregloEnteros([70, 30]))
//
//        println("h:" + tablaTitulo2.getHeaderHeight())

        addCellTabla(tablaTitulo2, new Paragraph("Mano de obra ", times14bold), prmsCellIzquierda)
        addCellTabla(tablaTitulo2, new Paragraph(" ", times10bold), prmsCellIzquierda)



        res.each { j ->


            if (j?.grid == 2) {
                addCellTabla(tablaComposicion2, new Paragraph(j?.codigo, times8normal), prmsCellIzquierda)
                addCellTabla(tablaComposicion2, new Paragraph(j?.item, times8normal), prmsCellIzquierda)
                addCellTabla(tablaComposicion2, new Paragraph(j?.unidad, times8normal), prmsCellHead)
                addCellTabla(tablaComposicion2, new Paragraph(g.formatNumber(number: j?.cantidad, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion2, new Paragraph(g.formatNumber(number: j?.punitario, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion2, new Paragraph(g.formatNumber(number: j?.transporte, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion2, new Paragraph(g.formatNumber(number: j?.costo, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion2, new Paragraph(g.formatNumber(number: j?.total, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)

                totalesMano = j?.total
                valorTotalMano = (total2 += totalesMano)


            }

        }

        addCellTabla(tablaTotalesMano, new Paragraph("Total Mano de Obra:", times10bold), prmsCellDerecha)
        addCellTabla(tablaTotalesMano, new Paragraph(g.formatNumber(number: valorTotalMano, minFractionDigits:
                3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times10bold), prmsNum)

        addCellTabla(tablaTotalesMano, new Paragraph(" ", times10bold), prmsNum)
        addCellTabla(tablaTotalesMano, new Paragraph(" ", times10bold), prmsNum)


        PdfPTable tablaTitulo3 = new PdfPTable(2)
        tablaTitulo3.setWidthPercentage(100)
        tablaTitulo3.setWidths(arregloEnteros([90, 10]))

        PdfPTable tablaComposicion3 = new PdfPTable(8)
        tablaComposicion3.setWidthPercentage(100)
        tablaComposicion3.setWidths(arregloEnteros([12, 36, 5, 9, 9, 9, 10, 10]))

        PdfPTable tablaTotalesEquipos = new PdfPTable(2)
        tablaTotalesEquipos.setWidthPercentage(100)
        tablaTotalesEquipos.setWidths(arregloEnteros([70, 30]))


        addCellTabla(tablaTitulo3, new Paragraph("Equipos ", times14bold), prmsCellIzquierda)
        addCellTabla(tablaTitulo3, new Paragraph(" ", times10bold), prmsCellIzquierda)


        res.each { k ->

            if (k?.grid == 3) {
                addCellTabla(tablaComposicion3, new Paragraph(k?.codigo, times8normal), prmsCellIzquierda)
                addCellTabla(tablaComposicion3, new Paragraph(k?.item, times8normal), prmsCellIzquierda)
                addCellTabla(tablaComposicion3, new Paragraph(k?.unidad, times8normal), prmsCellHead)
                addCellTabla(tablaComposicion3, new Paragraph(g.formatNumber(number: k?.cantidad, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion3, new Paragraph(g.formatNumber(number: k?.punitario, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion3, new Paragraph(g.formatNumber(number: k?.transporte, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion3, new Paragraph(g.formatNumber(number: k?.costo, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)
                addCellTabla(tablaComposicion3, new Paragraph(g.formatNumber(number: k?.total, minFractionDigits:
                        3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times8normal), prmsNum)

                totalesEquipos = k?.total
                valorTotalEquipos = (total3 += totalesEquipos)


            }

        }

        addCellTabla(tablaTotalesEquipos, new Paragraph("Total Equipos:", times10bold), prmsCellDerecha)
        addCellTabla(tablaTotalesEquipos, new Paragraph(g.formatNumber(number: valorTotalEquipos, minFractionDigits:
                3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times10bold), prmsNum)

//
        PdfPTable tablaTotalGeneral = new PdfPTable(2)
        tablaTotalGeneral.setWidthPercentage(100)
        tablaTotalGeneral.setWidths(arregloEnteros([70, 30]))

        addCellTabla(tablaTotalGeneral, new Paragraph("Total General:", times10bold), prmsCellDerecha)
        addCellTabla(tablaTotalGeneral, new Paragraph(g.formatNumber(number: (valorTotal + valorTotalMano + valorTotalEquipos), minFractionDigits:
                3, maxFractionDigits: 3, format: "##,##0", locale: "ec"), times10bold), prmsNum)

//        println("size: " + document.pageSize.getHeight())

        document.add(tablaHeader);
        document.add(tablaTitulo);
        document.add(tablaComposicion);
        document.add(tablaTotales)
        document.add(tablaTitulo2)
        document.add(tablaComposicion2);
        document.add(tablaTotalesMano)
        document.add(tablaTitulo3)
        document.add(tablaComposicion3);
        document.add(tablaTotalesEquipos)
        document.add(tablaTotalGeneral)
        document.close();
        pdfw.close()
        byte[] b = baos.toByteArray();
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=" + name)
        response.setContentLength(b.length)
        response.getOutputStream().write(b)



    }



    def reporteExcelComposicion() {

//        println("!!!" + params)


        def obra = Obra.get(params.id)


        def sql4 = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

        def cn4 = dbConnectionService.getConnection()

        def conc = cn4.rows(sql4.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)


        if (!params.tipo) {
            params.tipo = "-1"
        }
        if (!params.sp) {

            params.sp = '-1'
        }
        if (params.tipo == "-1") {
            params.tipo = "1,2,3"
        }

        def wsp = ""

        if (params.sp.toString() != "-1") {

            wsp = "      AND v.sbpr__id = ${params.sp} \n"
        }

//        params.tipo = "1,2,3"

        def sql = "SELECT\n" +
                "  v.voit__id                            id,\n" +
                "  i.itemcdgo                            codigo,\n" +
                "  i.itemnmbr                            item,\n" +
                "  u.unddcdgo                            unidad,\n" +
                "  v.voitcntd                            cantidad,\n" +
                "  v.voitpcun                            punitario,\n" +
                "  v.voittrnp                            transporte,\n" +
                "  v.voitpcun + v.voittrnp               costo,\n" +
                "  (v.voitpcun + v.voittrnp)*v.voitcntd  total,\n" +
                "  d.dprtdscr                            departamento,\n" +
                "  s.sbgrdscr                            subgrupo,\n" +
                "  g.grpodscr                            grupo,\n" +
                "  g.grpo__id                            grid,\n" +
                "  v.sbpr__id                            sp,\n" +
                "  b.sbprdscr                            subpresupuesto\n" +
                "FROM vlobitem v\n" +
                "INNER JOIN item i ON v.item__id = i.item__id\n" +
                "INNER JOIN undd u ON i.undd__id = u.undd__id\n" +
                "INNER JOIN dprt d ON i.dprt__id = d.dprt__id\n" +
                "INNER JOIN sbgr s ON d.sbgr__id = s.sbgr__id\n" +
                "INNER JOIN sbpr b ON v.sbpr__id = b.sbpr__id\n" +
                "INNER JOIN grpo g ON s.grpo__id = g.grpo__id AND g.grpo__id IN (${params.tipo})\n" +
                "WHERE v.obra__id = ${params.id} \n" + wsp +
                "ORDER BY v.sbpr__id, grid ASC, i.itemnmbr"



        def cn = dbConnectionService.getConnection()

        def res = cn.rows(sql.toString())

//        println("--->>" + res)

        //excel
        WorkbookSettings workbookSettings = new WorkbookSettings()
        workbookSettings.locale = Locale.default

        def file = File.createTempFile('myExcelDocument', '.xls')
        file.deleteOnExit()
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings)

        WritableFont font = new WritableFont(WritableFont.ARIAL, 12)
        WritableCellFormat formatXls = new WritableCellFormat(font)

        def row = 0
        WritableSheet sheet = workbook.createSheet('Composicion', 0)

        WritableFont times16font = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false);
        WritableCellFormat times16format = new WritableCellFormat(times16font);
        sheet.setColumnView(0, 20)
        sheet.setColumnView(1, 60)
        sheet.setColumnView(2, 10)
        sheet.setColumnView(3, 20)
        sheet.setColumnView(4, 20)
        sheet.setColumnView(5, 20)
        sheet.setColumnView(6, 20)
        sheet.setColumnView(7, 20)
        sheet.setColumnView(8, 25)
        sheet.setColumnView(9, 20)

        def label
        def number
        def fila = 14;
        def totalE = 0;
        def totalM = 0;
        def totalMO = 0;
        def totalEquipo = 0;
        def totalManoObra = 0;
        def totalMaterial = 0;
        def totalDirecto = 0;
        def ultimaFila

        label = new jxl.write.Label(1, 2, " G.A.D. PROVINCIA DE PICHINCHA", times16format); sheet.addCell(label);
        label = new jxl.write.Label(1, 4, "COMPOSICIÓN: " + obra?.nombre, times16format); sheet.addCell(label);
        label = new jxl.write.Label(1, 6, "PROCESO: " + concurso?.codigo, times16format); sheet.addCell(label);
        label = new jxl.write.Label(1, 8, "FECHA: " + printFecha(concurso?.fechaLimiteEntregaOfertas), times16format); sheet.addCell(label);

        label = new jxl.write.Label(0, 12, "CODIGO", times16format); sheet.addCell(label);
        label = new jxl.write.Label(1, 12, "ITEM", times16format); sheet.addCell(label);
        label = new jxl.write.Label(2, 12, "UNIDAD", times16format); sheet.addCell(label);
        label = new jxl.write.Label(3, 12, "CANTIDAD", times16format); sheet.addCell(label);
        label = new jxl.write.Label(4, 12, "P.UNITARIO", times16format); sheet.addCell(label);
        label = new jxl.write.Label(5, 12, "TRANSPORTE", times16format); sheet.addCell(label);
        label = new jxl.write.Label(6, 12, "COSTO", times16format); sheet.addCell(label);
        label = new jxl.write.Label(7, 12, "TOTAL", times16format); sheet.addCell(label);
        label = new jxl.write.Label(8, 12, "TIPO", times16format); sheet.addCell(label);
        label = new jxl.write.Label(9, 12, "SUBPRESUPUESTO", times16format); sheet.addCell(label);

        res.each {

            if (it?.item == null) {

                it?.item = " "
            }

            if (it?.cantidad == null) {


                it?.cantidad = 0
            }
            if (it?.punitario == null) {

                it?.punitario = 0

            }
            if (it?.transporte == null) {

                it?.transporte = 0
            }
            if (it?.costo == null) {

                it?.costo = 0

            }
            if (it?.total == null) {

                it?.total = 0
            }

            label = new jxl.write.Label(0, fila, it?.codigo.toString()); sheet.addCell(label);
            label = new jxl.write.Label(1, fila, it?.item.toString()); sheet.addCell(label);
            label = new jxl.write.Label(2, fila, it?.unidad.toString()); sheet.addCell(label);
            number = new jxl.write.Number(3, fila, it?.cantidad.toDouble().round(2) ?: 0); sheet.addCell(number);
            number = new jxl.write.Number(4, fila, it?.punitario.toDouble().round(2) ?: 0); sheet.addCell(number);
            number = new jxl.write.Number(5, fila, it?.transporte.toDouble().round(2) ?: 0); sheet.addCell(number);
            number = new jxl.write.Number(6, fila, it?.costo.toDouble().round(2) ?: 0); sheet.addCell(number);
            number = new jxl.write.Number(7, fila, it?.total.toDouble().round(2) ?: 0); sheet.addCell(number);
            label = new jxl.write.Label(8, fila, it?.grupo.toString()); sheet.addCell(label);
            label = new jxl.write.Label(9, fila, it?.subpresupuesto.toString()); sheet.addCell(label);

            fila++

            if (it?.grid == 1) {
                totalMaterial = (totalM += it?.total)
            }
            if (it?.grid == 2) {
                totalManoObra = (totalMO += it?.total)
            }
            if (it?.grid == 3) {
                totalEquipo = (totalE += it?.total)
            }
            totalDirecto = totalEquipo + totalManoObra + totalMaterial;
            ultimaFila = fila
        }

        label = new jxl.write.Label(6, ultimaFila, "Total Materiales: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila, totalMaterial.toDouble()?.round(2) ?: 0); sheet.addCell(number);

        label = new jxl.write.Label(6, ultimaFila + 1, "Total Mano de Obra: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila + 1, totalManoObra.toDouble()?.round(2) ?: 0); sheet.addCell(number);

        label = new jxl.write.Label(6, ultimaFila + 2, "Total Equipos: ", times16format); sheet.addCell(label);
//        number = new jxl.write.Number(7, ultimaFila + 2, totalEquipo); sheet.addCell(number);
        number = new jxl.write.Number(7, ultimaFila + 2, totalEquipo.toDouble()?.round(2) ?: 0); sheet.addCell(number);

        label = new jxl.write.Label(6, ultimaFila + 3, "TOTAL DIRECTO: ", times16format); sheet.addCell(label);
        number = new jxl.write.Number(7, ultimaFila + 3, totalDirecto.toDouble()?.round(2) ?: 0); sheet.addCell(number);

        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "ComposicionExcel.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());
    }


}
