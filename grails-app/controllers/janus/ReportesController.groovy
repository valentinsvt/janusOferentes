package janus

import com.itextpdf.text.BadElementException
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import janus.pac.Garantia
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.*

import java.awt.Color

//import java.awt.Label

class ReportesController {

    def index() {}

    def meses = ['', "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]


    private String printFecha(Date fecha) {
        if (fecha) {
            return (fecha.format("dd") + ' de ' + meses[fecha.format("MM").toInteger()] + ' de ' + fecha.format("yyyy")).toLowerCase()
        } else {
            return "Error: no hay fecha que mostrar"
        }
    }

    def buscadorService
    def preciosService
    def dbConnectionService

    def garantiasContrato() {
        def contrato = Contrato.get(params.id)
        def garantias = Garantia.findAllByContrato(contrato)
        return [contrato: contrato, garantias: garantias]
    }


    def rubro = {
        println "rep!!!  rubro " + params
//        def rubro
//        def grupos = []
//        def volquetes = []
//        def choferes = []
//        def grupoTransporte=DepartamentoItem.findAllByTransporteIsNotNull()
//        grupoTransporte.each {
//            if(it.transporte.codigo=="H")
//                choferes=Item.findAllByDepartamento(it)
//            if(it.transporte.codigo=="T")
//                volquetes=Item.findAllByDepartamento(it)
//        }
//        grupos.add(Grupo.get(4))
//        grupos.add(Grupo.get(5))
//        grupos.add(Grupo.get(6))
//
//        rubro = Item.get(params.id)
//        def items=Rubro.findAllByRubro(rubro)
//        items.sort{it.item.codigo}
//        [ rubro: rubro, grupos: grupos,items:items,choferes:choferes,volquetes:volquetes]
//        render "<html><head></head><body>Hola</body></html>"
        return [algo: "algo"]
    }

    def imprimeMatriz() {

        //nuevo


        def obra = Obra.get(params.id)

        def oferente = Persona.get(session.usuario.id)


        def sql1 = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

        def cn1 = dbConnectionService.getConnection()

        def conc = cn1.rows(sql1.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)

//        println "imprime matriz"
        def cn = buscadorService.dbConnectionService.getConnection()
        def cn2 = buscadorService.dbConnectionService.getConnection()
        def sql = "SELECT clmncdgo,clmndscr,clmntipo from mfcl where obra__id=${params.id} order by 1"
        def columnas = []
        def filas = []
        cn.eachRow(sql.toString()) { r ->
            def col = r[1]
            if (r[2] != "R") {
                def parts = col.split("_")
                //println "parts "+parts
                try {
                    col = parts[0].toLong()
                    col = Item.get(col).nombre

                } catch (e) {
                    col = parts[0]
                }

                col += parts[1]?.replaceAll("T", " Total")?.replaceAll("U", " Unitario")
            }

            columnas.add([r[0], col, r[2]])
        }
        sql = "SELECT * from mfrb where obra__id=${params.id} order by orden"
        def cont = 1
        cn.eachRow(sql.toString()) { r ->
            def tmp = [cont, r[0].trim(), r[2], r[3], r[4]]
            def sq = ""
            columnas.each { c ->
                if (c[2] != "R") {
                    sq = "select valor from mfvl where obra__id=${params.id} and clmncdgo=${c[0]} and codigo='${r[0].trim()}'"
                    cn2.eachRow(sq.toString()) { v ->
                        tmp.add(v[0])
                    }
                }

            }
//            println "fila  "+tmp
//            println("col" + columnas)
            filas.add(tmp)
            cont++
        }

        def baos = new ByteArrayOutputStream()
        def name = "matriz_polinomica_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
//            println "name "+name
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 14, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font titleFont3 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font titleFont2 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times8bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD)
        com.lowagie.text.Font times8normal = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font catFont = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font info = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL)

        def prmsHeaderHoja = [border: Color.WHITE]

        Document document
        document = new Document(PageSize.A4.rotate());
        // margins: left, right, top, bottom
        // 1 in = 72, 1cm=28.1, 3cm = 86.4
        document.setMargins(45.2, 30, 56.2, 56.2);
        def pdfw = PdfWriter.getInstance(document, baos);
        document.open();
        document.addTitle("Matriz Polinómica " + new Date().format("dd_MM_yyyy"));
        document.addSubject("Generado por el sistema Janus");
        document.addKeywords("reporte, janus,matriz");
        document.addAuthor("Janus");
        document.addCreator("Tedein SA");
        com.lowagie.text.Font small = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL);
        Paragraph headersTitulo = new Paragraph();
        addEmptyLine(headersTitulo, 1)
        headersTitulo.setAlignment(Element.ALIGN_CENTER);
        headersTitulo.add(new Paragraph("G.A.D. PROVINCIA DE PICHINCHA", titleFont));
        addEmptyLine(headersTitulo, 1);
        headersTitulo.add(new Paragraph("MATRIZ DE LA FÓRMULA POLINÓMICA - " + oferente?.nombre.toUpperCase() + " " + oferente?.apellido.toUpperCase(), titleFont));
        addEmptyLine(headersTitulo, 1);

        document.add(headersTitulo)

        PdfPTable tablaHeader = new PdfPTable(3);
        tablaHeader.setWidthPercentage(100);
        tablaHeader.setWidths(arregloEnteros([15, 2, 70]))

        addCellTabla(tablaHeader, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" ", times8bold), prmsHeaderHoja)


        addCellTabla(tablaHeader, new Paragraph("PROYECTO", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" : ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(obra?.nombre, times8normal), prmsHeaderHoja)

        addCellTabla(tablaHeader, new Paragraph("PROCESO", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" : ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(concurso?.codigo, times8normal), prmsHeaderHoja)

        addCellTabla(tablaHeader, new Paragraph("FECHA PRESENTACIÓN", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" : ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(printFecha(concurso?.fechaLimiteEntregaOfertas).toUpperCase(), times8normal), prmsHeaderHoja)


        addCellTabla(tablaHeader, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(tablaHeader, new Paragraph(" ", times8bold), prmsHeaderHoja)

        document.add(tablaHeader);

        /*table*/

        def parcial = []
        def anchos = [5, 6, 35, 5, 8, 8, 8, 8, 8, 8]     // , 9
        def anchos2 = [5, 6, 35, 5, 8, 8, 8, 8, 8]     // , 9

        def inicio = 0
        def fin = 10

        def inicioCab = 1
        def finCab = 10

//        println "size "+columnas.size()
        while (fin <= columnas.size() + 1) {  //gdo  <= antes

//            println "inicio "+inicio+"  fin  "+fin
//            println "iniciocab "+inicioCab+"  fincab  "+finCab
//
//


            if (inicio != 0) {

                anchos = [10, 10, 10, 10, 10, 10, 10, 10, 10, 10]
                anchos2 = [10, 10, 10, 10, 10, 10, 10, 10, 10]
            }

            if (fin - inicio < 10) {
                anchos = []
                (fin - inicio).toInteger().times { i ->
                    anchos.add((100 / (fin - inicio)).toInteger())
                }

                anchos2 = []
                ((fin - inicio).toInteger() - 1).times { i ->
                    anchos2.add((100 / (((fin - inicio).toInteger()) - 1)).toInteger())
                }

            }
            def parrafo = new Paragraph("")
/*
            if (inicio == fin)
               inicio -= 2       //gdo
*/

//            println "anchos "+anchos
//            println "anchos2 "+anchos2


            PdfPTable table = new PdfPTable((fin - inicio).toInteger());       //gdo

//            println("-->>" + (fin-inicio))

            PdfPTable table2 = new PdfPTable(((fin - inicio).toInteger()) - 1);





            table.setWidthPercentage(100);
            table.setWidths(arregloEnteros(anchos))

            table2.setWidthPercentage(100);
            table2.setWidths(arregloEnteros(anchos2))



            if (inicio == 0) {


                (finCab - inicioCab).toInteger().times { i ->

//                if(inicio != 0){
//
//                    println("entro" + i)
//                    println("--->>>"  + i)
//                    println("%%%%"  + inicio)
//                    PdfPCell c1 = new PdfPCell(new Phrase(columnas[((inicio+i)-1)][1], small));
//                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    table.addCell(c1);
//
//
//                }
//                if(inicio == 0){
//
//                    PdfPCell c1 = new PdfPCell(new Phrase(columnas[inicio+i][1], small));
//                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    table.addCell(c1);
//
//                }

//                    println columnas
                    PdfPCell c0 = new PdfPCell(new Phrase(columnas[(inicioCab + i) - 1][1], small));
                    c0.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table2.addCell(c0);


                }
                table2.setHeaderRows(1);
                filas.each { f ->
                    (finCab - inicioCab).toInteger().times { i ->

//                    if(inicio != 0) {
//
//                        def dato = f[(inicio + i)-1]
//                        if (!dato)
//                            dato = "0.00"
//                        else
//                            dato = dato.toString()
//                        def cell = new PdfPCell(new Phrase(dato, small))
//                        cell.setFixedHeight(16f);
//                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT)
//                        table2.addCell(cell);
//
//                    }
//
//                    if(inicio == 0){
//
//
//
//                        def dato = f[(inicio + i)]
//                        if (!dato)
//                            dato = "0.00"
//                        else
//                            dato = dato.toString()
//                        def cell = new PdfPCell(new Phrase(dato, small))
//                        cell.setFixedHeight(16f);
////                        if (i > 3) cell.setHorizontalAlignment(Element.ALIGN_RIGHT)
//                        table2.addCell(cell);
//
//                    }
                        def fuente = small
                        def borde = 1.5
                        if (f[1] =~ "sS") {
                            fuente = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD);
                        }


                        def dato = f[(inicio + i)]
                        if (!dato)
                            dato = "0.00"
                        else
                            dato = dato.toString()
                        def cell = new PdfPCell(new Phrase(dato, fuente));
                        cell.setFixedHeight(16f);
                        if (f[1] == "sS1")
                            cell.setBorderWidthTop(borde)
                        if (f[1] == "sS2")
                            cell.setBorderWidthBottom(borde)
                        table2.addCell(cell);
                    }
                }


            } else {


                (finCab - inicioCab).toInteger().times { i ->

//                println "columnas "+columnas[(inicioCab + i)-1][1]
                    PdfPCell c1 = new PdfPCell(new Phrase(columnas[(inicioCab + i) - 1][1], small));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);


                }


                table.setHeaderRows(1);
                filas.each { f ->
//                    println "f "+f[1]
                    def fuente = small
                    def borde = 1.5
                    if (f[1] =~ "sS") {
                        fuente = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD);
                    }
                    if (f[1] == "sS1" || f[1] == "sS2")
                        borde = 1.5
                    (fin - inicio).toInteger().times { i ->

                        def dato = f[(inicio + i) - 1]
                        if (!dato)
                            dato = "0.00"
                        else
                            dato = dato.toString()
                        def cell = new PdfPCell(new Phrase(dato, fuente));
                        cell.setFixedHeight(16f);
                        if (f[1] == "sS1")
                            cell.setBorderWidthTop(borde)
                        if (f[1] == "sS2")
                            cell.setBorderWidthBottom(borde)
                        table.addCell(cell);
                    }
                }


            }



            parrafo.add(table2)
            parrafo.add(table);
            document.add(parrafo);
            document.newPage();
//            inicio = fin + 1
            inicio = fin
            fin = inicio + 10

            inicioCab = finCab
            finCab = inicioCab + 10


            if (fin > columnas.size() + 1) {
                fin = columnas.size() + 1
            }
            if (finCab > columnas.size() + 1) {
                finCab = columnas.size() + 1
            }
            if (inicio > columnas.size())
                break;

        }

        /*table*/

        document.close();
        pdfw.close()
        byte[] b = baos.toByteArray();
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=" + name)
        response.setContentLength(b.length)
        response.getOutputStream().write(b)

        //old

//        println "imprime matriz"
//        def cn = buscadorService.dbConnectionService.getConnection()
//        def cn2 = buscadorService.dbConnectionService.getConnection()
//        def sql = "SELECT clmncdgo,clmndscr,clmntipo from mfcl where obra__id=${params.id} order by 1"
//        def columnas = []
//        def filas = []
//        cn.eachRow(sql.toString()) { r ->
//            columnas.add([r[0], r[1], r[2]])
//        }
//        sql = "SELECT * from mfrb where obra__id=${params.id} order by orden"
//        def cont = 1
//        cn.eachRow(sql.toString()) { r ->
//            def tmp = [cont, r[0].trim(), r[2], r[3], r[4]]
//            def sq = ""
//            columnas.each { c ->
//                if (c[2] != "R") {
//                    sq = "select valor from mfvl where obra__id=${params.id} and clmncdgo=${c[0]} and codigo='${r[0].trim()}'"
//                    cn2.eachRow(sq.toString()) { v ->
//                        tmp.add(v[0])
//                    }
//                }
//
//            }
////            println "fila  "+tmp
//            filas.add(tmp)
//            cont++
//        }
//
//        def baos = new ByteArrayOutputStream()
//        def name = "matriz_polinomica_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
////            println "name "+name
//        Font catFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
//        Font info = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL)
//        Document document
//        document = new Document(PageSize.A4.rotate());
//        def pdfw = PdfWriter.getInstance(document, baos);
//        document.open();
//        document.addTitle("Matriz Polinómica " + new Date().format("dd_MM_yyyy"));
//        document.addSubject("Generado por el sistema Janus");
//        document.addKeywords("reporte, janus,matriz");
//        document.addAuthor("Janus");
//        document.addCreator("Tedein SA");
////        Paragraph preface = new Paragraph();
////        addEmptyLine(preface, 1);
////        preface.add(new Paragraph("Matriz polinómica", catFont));
////        preface.add(new Paragraph("Generado por el usuario: " + session.usuario + "   el: " + new Date().format("dd/MM/yyyy hh:mm"), info))
////        addEmptyLine(preface, 1);
////        document.add(preface);
//        Font small = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL);
//
//        /*table*/
//
//        def parcial = []
//        def anchos = [5, 10, 30, 5, 10, 10, 10, 10]
//        def inicio = 0
//        def fin = 8
////        println "size "+columnas.size()
//        while (fin <= columnas.size()) {
////            println "inicio "+inicio+"  fin  "+fin
//            if (inicio != 0)
//                anchos = [12, 12, 12, 12, 12, 12, 12, 12]
//            if (fin - inicio < 8) {
//                anchos = []
//                (fin - inicio).toInteger().times { i ->
//                    anchos.add((100 / (fin - inicio)).toInteger())
//                }
//            }
//            def parrafo = new Paragraph("")
//            PdfPTable table = new PdfPTable((fin - inicio).toInteger());
//            table.setWidthPercentage(100);
//            table.setWidths(arregloEnteros(anchos))
//            (fin - inicio).toInteger().times { i ->
//                PdfPCell c1 = new PdfPCell(new Phrase(columnas[inicio + i][1], small));
//                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(c1);
//            }
//            table.setHeaderRows(1);
//            filas.each { f ->
//                (fin - inicio).toInteger().times { i ->
//                    def dato = f[inicio + i]
//                    if (!dato)
//                        dato = "0.00"
//                    else
//                        dato = dato.toString()
//                    def cell = new PdfPCell(new Phrase(dato, small));
//                    cell.setFixedHeight(28f);
//                    table.addCell(cell);
//                }
//            }
//            parrafo.add(table);
//            document.add(parrafo);
//            document.newPage();
//            inicio = fin + 1
//            fin = inicio + 8
//            if (fin > columnas.size())
//                fin = columnas.size()
//            if (inicio > columnas.size())
//                break;
//
//        }
//
//        /*table*/
//
//        document.close();
//        pdfw.close()
//        byte[] b = baos.toByteArray();
//        response.setContentType("application/pdf")
//        response.setHeader("Content-disposition", "attachment; filename=" + name)
//        response.setContentLength(b.length)
//        response.getOutputStream().write(b)


    }


    def matrizExcel() {


//        println("-->" + params)

        def obra = Obra.get(params.id)

        def oferente = Persona.get(session.usuario.id)


        def sql1 = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"

//        println("sql:" + sql)

        def cn3 = dbConnectionService.getConnection()

        def conc = cn3.rows(sql1.toString())

        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)


        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def cn2 = dbConnectionService.getConnection()
//        def obra = Obra.get(params.id)
        def lugar = obra.lugar
        def fecha = obra.fechaPreciosRubros
        def itemsChofer = [obra.chofer]
        def itemsVolquete = [obra.volquete]
        def indi = obra.totales
        WorkbookSettings workbookSettings = new WorkbookSettings()
        workbookSettings.locale = Locale.default
        def file = File.createTempFile('matrizFP' + obra.codigo, '.xls')
        file.deleteOnExit()
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings)

        WritableFont times10Font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);
        WritableCellFormat times10format = new WritableCellFormat(times10Font);
        WritableFont times10Normal = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);
        WritableCellFormat times10formatNormal = new WritableCellFormat(times10Normal);

        WritableFont times08font = new WritableFont(WritableFont.TIMES, 8, WritableFont.NO_BOLD, false);
        WritableCellFormat times08format = new WritableCellFormat(times08font);

        def fila = 12

        WritableSheet sheet = workbook.createSheet("PAC", 0)

        sheet.setColumnView(0, 8)
        sheet.setColumnView(1, 12)
        sheet.setColumnView(2, 50)
        sheet.setColumnView(3, 8)
        sheet.setColumnView(4, 12)
        sheet.setColumnView(5, 15)
        sheet.setColumnView(6, 15)
        sheet.setColumnView(7, 15)
        sheet.setColumnView(8, 15)
        sheet.setColumnView(9, 15)
        sheet.setColumnView(10, 15)
        sheet.setColumnView(11, 15)
        sheet.setColumnView(12, 15)  // el resto por defecto..

        def label = new jxl.write.Label(2, 2, "G.A.D. PROVINCIA DE PICHINCHA".toUpperCase(), times10format); sheet.addCell(label);
        label = new jxl.write.Label(2, 3, "MATRIZ DE LA FÓRMULA POLINÓMICA - " + oferente?.nombre.toUpperCase() + " " + oferente?.apellido.toUpperCase(), times10format); sheet.addCell(label);
        label = new jxl.write.Label(2, 5, "PROYECTO: " + obra?.nombre, times10format); sheet.addCell(label);
        label = new jxl.write.Label(2, 6, "PROCESO: " + concurso?.codigo, times10format); sheet.addCell(label);
        label = new jxl.write.Label(2, 7, "FECHA PRESENTACIÓN: " + printFecha(concurso?.fechaLimiteEntregaOfertas), times10format); sheet.addCell(label);

        // crea columnas

        def sql = "SELECT clmncdgo,clmndscr,clmntipo from mfcl where obra__id = ${obra.id} order by  1"
//        println "sql desc " + sql
        def subSql = ""
        def sqlVl = ""
        def clmn = 0
        def col = ""
        cn.eachRow(sql.toString()) { r ->
            col = r[1]
            if (r[2] != "R") {
                def parts = r[1].split("_")
                try {
                    col = Item.get(parts[0].toLong()).nombre
                } catch (e) {
                    println "error: " + e
                    col = parts[0]
                }
                col += " " + parts[1]?.replaceAll("T", " Total")?.replaceAll("U", " Unitario")
            }
            label = new jxl.write.Label(clmn++, fila, "${col}", times10formatNormal); sheet.addCell(label);
        }
        fila++
        def sqlRb = "SELECT orden, codigo, rubro, unidad, cantidad from mfrb where obra__id = ${obra.id} order by orden"
//        println "sql desc " + sqlRb
        def number
        cn.eachRow(sqlRb.toString()) { r ->
            4.times {
                label = new jxl.write.Label(it, fila, r[it]?.toString() ?: "", times08format); sheet.addCell(label);
            }
            number = new Number(4, fila, r.cantidad?.toDouble()?.round(3) ?: 0, times08format); sheet.addCell(number);

            fila++
        }

        fila = 13
        clmn = 5

        sql = "SELECT clmncdgo, clmntipo from mfcl where obra__id = ${obra.id} order by  1"
        cn.eachRow(sqlRb.toString()) { rb ->
            cn1.eachRow(sql.toString()) { r ->
                if (r.clmntipo != "R") {
                    subSql = "select valor from mfvl where clmncdgo = ${r.clmncdgo} and codigo='${rb.codigo.trim()}' and " +
                            "obra__id = ${obra.id}"
                    //println subSql
                    cn2.eachRow(subSql.toString()) { v ->
//                        label = new Label(clmn++, fila, v.valor.toString(), times08format); sheet.addCell(label);
                        number = new Number(clmn++, fila, v.valor?.toDouble()?.round(5) ?: 0.00000, times08format); sheet.addCell(number);
                    }
                }
            }
            clmn = 5
            fila++
        }

        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "matriz.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());

        // crea columnas


    }


    def reporteBuscador = {

        // println "reporte buscador params !! "+params
        if (!session.dominio)
            response.sendError(403)
        else {
            def listaTitulos = params.listaTitulos
            def listaCampos = params.listaCampos
            def lista = buscadorService.buscar(session.dominio, params.tabla, "excluyente", params, true, params.extras)
            def funciones = session.funciones
            session.dominio = null
            session.funciones = null
            lista.pop()

            def baos = new ByteArrayOutputStream()
            def name = "reporte_de_" + params.titulo.replaceAll(" ", "_") + "_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
//            println "name "+name
            Font catFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
            Font info = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL);
            Document document
            if (params.landscape)
                document = new Document(PageSize.A4.rotate());
            else
                document = new Document();

            def pdfw = PdfWriter.getInstance(document, baos);

            document.open();
            document.addTitle("Reporte de " + params.titulo + " " + new Date().format("dd_MM_yyyy"));
            document.addSubject("Generado por el sistema Janus");
            document.addKeywords("reporte, elyon," + params.titulo);
            document.addAuthor("Janus");
            document.addCreator("Tedein SA");
            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 1);
            preface.add(new Paragraph("Reporte de " + params.titulo, catFont));
            preface.add(new Paragraph("Generado por el usuario: " + session.usuario + "   el: " + new Date().format("dd/MM/yyyy hh:mm"), info))
            addEmptyLine(preface, 1);
            document.add(preface);
//        Start a new page
//        document.newPage();
            //System.getProperty("user.name")
            addContent(document, catFont, listaCampos.size(), listaTitulos, params.anchos, listaCampos, funciones, lista);            // Los tamaños son porcentajes!!!!
            document.close();
            pdfw.close()
            byte[] b = baos.toByteArray();
            response.setContentType("application/pdf")
            response.setHeader("Content-disposition", "attachment; filename=" + name)
            response.setContentLength(b.length)
            response.getOutputStream().write(b)
        }
    }


    def analisisPrecios() {


    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private static void addContent(Document document, catFont, columnas, headers, anchos, campos, funciones, datos) throws DocumentException {
        Font small = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL);
        def parrafo = new Paragraph("")
        createTable(parrafo, columnas, headers, anchos, campos, funciones, datos);
        document.add(parrafo);


    }


    private static void createTable(Paragraph subCatPart, columnas, headers, anchos, campos, funciones, datos) throws BadElementException {
        PdfPTable table = new PdfPTable(columnas);
        table.setWidthPercentage(100);
        table.setWidths(arregloEnteros(anchos))
        Font small = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL);
        headers.eachWithIndex { h, i ->
            PdfPCell c1 = new PdfPCell(new Phrase(h, small));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        table.setHeaderRows(1);
        def tagLib = new BuscadorTagLib()
        datos.each { d ->
            campos.eachWithIndex { c, j ->
                def campo
                if (funciones) {
                    if (funciones[j])
                        campo = tagLib.operacion([propiedad: c, funcion: funciones[j], registro: d]).toString()
                    else
                        campo = d.properties[c].toString()
                } else {
                    campo = d.properties[c].toString()
                }

                table.addCell(new Phrase(campo, small));

            }

        }

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }


    static arregloEnteros(array) {
        int[] ia = new int[array.size()]
        array.eachWithIndex { it, i ->
            ia[i] = it.toInteger()
        }

        return ia
    }


    def imprimirRubrosExcel() {
        def obra = Obra.get(params.obra.toLong())
        def lugar = obra.lugar
        def fecha = obra.fechaPreciosRubros
        def itemsChofer = [obra.chofer]
        def itemsVolquete = [obra.volquete]
        def indi = obra.totales

        def oferente = Persona.get(params.oferente)

        def obraOferente = Obra.findByOferente(oferente)

//        def sql = "SELECT * FROM cncr WHERE obra__id=${obraOferente?.idJanus}"
        def sql = "SELECT * FROM cncr WHERE obra__id=${obra?.idJanus}"
//
        println("sql:" + sql)
//
        def cn = dbConnectionService.getConnection()

        def conc = cn.rows(sql.toString())


        def cncrId

        conc.each {

            cncrId = it?.cncr__id

        }

        def concurso = janus.pac.Concurso.get(cncrId)




        WorkbookSettings workbookSettings = new WorkbookSettings()
        workbookSettings.locale = Locale.default
        def file = File.createTempFile('myExcelDocument', '.xls')
        file.deleteOnExit()
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings)
        WritableFont font = new WritableFont(WritableFont.ARIAL, 12)
        WritableCellFormat formatXls = new WritableCellFormat(font)
        def row = 0

        preciosService.ac_rbroObra(obra.id)
        VolumenesObra.findAllByObra(obra, [sort: "orden"]).item.eachWithIndex { rubro, i ->
//            def res = preciosService.presioUnitarioVolumenObra("* ", rubro.id, params.oferente)
            def res = preciosService.presioUnitarioVolumenObra("* ", rubro.id,obra?.id)


            WritableSheet sheet = workbook.createSheet(rubro.codigo, i)
            rubroAExcel(sheet, res, rubro, fecha, indi, oferente, concurso, obra)
        }
        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "rubro.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());

    }


    def rubroAExcel(sheet, res, rubro, fecha, indi, oferente, concurso, obra) {


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
        label = new Label(1, 3, "PROCESO:" + concurso?.codigo.toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(1, 4, "Análisis de precios unitarios".toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(1, 6, "PROYECTO: " + obra?.nombre.toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(1, 7, "RUBRO: " + rubro?.nombre, times16format); sheet.addCell(label);
        label = new Label(1, 8, "UNIDAD:" + rubro?.unidad?.codigo, times16format); sheet.addCell(label);

        def fila = 9
        label = new Label(0, fila, "Herramientas", times16format); sheet.addCell(label);
        sheet.mergeCells(0, fila, 1, fila)
        fila++
        def number
        def totalHer = 0
        def totalMan = 0
        def totalMat = 0
        def total = 0
        def band = 0
        def rowsTrans = []
        res.each { r ->
//            println "r "+r
            if (r["grpocdgo"] == 3) {
                if (band == 0) {
                    label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
                    label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
                    label = new Label(2, fila, "Cantidad", times16format); sheet.addCell(label);
                    label = new Label(3, fila, "Tarifa", times16format); sheet.addCell(label);
                    label = new Label(4, fila, "Costo", times16format); sheet.addCell(label);
                    label = new Label(5, fila, "Rendimiento", times16format); sheet.addCell(label);
                    label = new Label(6, fila, "C.Total", times16format); sheet.addCell(label);
                    fila++
                }
                band = 1
                label = new Label(0, fila, r["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila, r["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, r["rbrocntd"]); sheet.addCell(number);
                number = new Number(3, fila, r["rbpcpcun"]); sheet.addCell(number);
                number = new Number(4, fila, r["rbpcpcun"] * r["rbrocntd"]); sheet.addCell(number);
                number = new Number(5, fila, r["rndm"]); sheet.addCell(number);
                number = new Number(6, fila, r["parcial"]); sheet.addCell(number);
                totalHer += r["parcial"]
                fila++
            }
            if (r["grpocdgo"] == 2) {
                if (band == 1) {
                    label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
                    number = new Number(6, fila, totalHer); sheet.addCell(number);
                    fila++
                }
                if (band != 2) {
                    fila++
                    label = new Label(0, fila, "Mano de obra", times16format); sheet.addCell(label);
                    sheet.mergeCells(0, fila, 1, fila)
                    fila++
                    label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
                    label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
                    label = new Label(2, fila, "Cantidad", times16format); sheet.addCell(label);
                    label = new Label(3, fila, "Jornal", times16format); sheet.addCell(label);
                    label = new Label(4, fila, "Costo", times16format); sheet.addCell(label);
                    label = new Label(5, fila, "Rendimiento", times16format); sheet.addCell(label);
                    label = new Label(6, fila, "C.Total", times16format); sheet.addCell(label);
                    fila++
                }
                band = 2
                label = new Label(0, fila, r["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila, r["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, r["rbrocntd"]); sheet.addCell(number);
                number = new Number(3, fila, r["rbpcpcun"]); sheet.addCell(number);
                number = new Number(4, fila, r["rbpcpcun"] * r["rbrocntd"]); sheet.addCell(number);
                number = new Number(5, fila, r["rndm"]); sheet.addCell(number);
                number = new Number(6, fila, r["parcial"]); sheet.addCell(number);
                totalMan += r["parcial"]
                fila++
            }
            if (r["grpocdgo"] == 1) {
                if (band == 2) {
                    label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
                    number = new Number(6, fila, totalMan); sheet.addCell(number);
                    fila++
                }
                if (band != 3) {
                    fila++
                    label = new Label(0, fila, "Materiales", times16format); sheet.addCell(label);
                    sheet.mergeCells(0, fila, 1, fila)
                    fila++
                    label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
                    label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
                    label = new Label(2, fila, "Cantidad", times16format); sheet.addCell(label);
                    label = new Label(3, fila, "Unitario", times16format); sheet.addCell(label);
                    label = new Label(6, fila, "C.Total", times16format); sheet.addCell(label);
                    fila++
                }
                band = 3
                label = new Label(0, fila, r["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila, r["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, r["rbrocntd"]); sheet.addCell(number);
                number = new Number(3, fila, r["rbpcpcun"]); sheet.addCell(number);
                number = new Number(6, fila, r["parcial"]); sheet.addCell(number);
                totalMat += r["parcial"]
                fila++

            }
            if (r["parcial_t"] > 0) {
                rowsTrans.add(r)
                total += r["parcial_t"]
            }

        }
        if (band == 3) {
            label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
            number = new Number(6, fila, totalMat); sheet.addCell(number);
            fila++
        }

        /*Tranporte*/
        if (rowsTrans.size() > 0) {
            fila++
            label = new Label(0, fila, "Transporte", times16format); sheet.addCell(label);
            sheet.mergeCells(0, fila, 1, fila)
            fila++
            label = new Label(0, fila, "Código", times16format); sheet.addCell(label);
            label = new Label(1, fila, "Descripción", times16format); sheet.addCell(label);
            label = new Label(2, fila, "Peso/Vol", times16format); sheet.addCell(label);
            label = new Label(3, fila, "Cantidad", times16format); sheet.addCell(label);
            label = new Label(4, fila, "Distancia", times16format); sheet.addCell(label);
            label = new Label(5, fila, "Unitario", times16format); sheet.addCell(label);
            label = new Label(6, fila, "C.Total", times16format); sheet.addCell(label);
            fila++
            rowsTrans.each { rt ->
                label = new Label(0, fila, rt["itemcdgo"], times10); sheet.addCell(label);
                label = new Label(1, fila, rt["itemnmbr"], times10); sheet.addCell(label);
                number = new Number(2, fila, rt["itempeso"]); sheet.addCell(number);
                number = new Number(3, fila, rt["rbrocntd"]); sheet.addCell(number);
                number = new Number(4, fila, rt["distancia"]); sheet.addCell(number);
                number = new Number(5, fila, rt["parcial_t"] / (rt["itempeso"] * rt["rbrocntd"] * rt["distancia"])); sheet.addCell(number);
                number = new Number(6, fila, rt["parcial_t"]); sheet.addCell(number);
                fila++
            }
            label = new Label(0, fila, "SUBTOTAL", times10); sheet.addCell(label);
            number = new Number(6, fila, total); sheet.addCell(number);
            fila++
            fila++
        }

        /*indirectos */

        label = new Label(0, fila, "Costos Indirectos", times16format); sheet.addCell(label);
        sheet.mergeCells(0, fila, 1, fila)
        fila++

        label = new Label(0, fila, "Descripción", times16format); sheet.addCell(label);
        sheet.mergeCells(0, fila, 1, fila)
        label = new Label(5, fila, "Porcentaje", times16format); sheet.addCell(label);
        label = new Label(6, fila, "Valor", times16format); sheet.addCell(label);
        fila++
        def totalRubro = total + totalHer + totalMan + totalMat
        def totalIndi = totalRubro * indi / 100
        label = new Label(0, fila, "Costos indirectos", times10); sheet.addCell(label);
        sheet.mergeCells(0, fila, 1, fila)
        number = new Number(5, fila, indi); sheet.addCell(number);
        number = new Number(6, fila, totalIndi); sheet.addCell(number);

        /*Totales*/
        fila += 4
        label = new Label(4, fila, "Costo unitario directo", times16format); sheet.addCell(label);
        sheet.mergeCells(4, fila, 5, fila)
        label = new Label(4, fila + 1, "Costos indirectos", times16format); sheet.addCell(label);
        sheet.mergeCells(4, fila + 1, 5, fila + 1)
        label = new Label(4, fila + 2, "Costo total del rubro", times16format); sheet.addCell(label);
        sheet.mergeCells(4, fila + 2, 5, fila + 2)
        label = new Label(4, fila + 3, "Precio unitario", times16format); sheet.addCell(label);
        sheet.mergeCells(4, fila + 3, 5, fila + 3)
        number = new Number(6, fila, totalRubro); sheet.addCell(number);
        number = new Number(6, fila + 1, totalIndi); sheet.addCell(number);
        number = new Number(6, fila + 2, totalRubro + totalIndi); sheet.addCell(number);
        number = new Number(6, fila + 3, (totalRubro + totalIndi).toDouble().round(2)); sheet.addCell(number);
        return sheet
    }



    def imprimirRubros() {

//                println("->>>" + params)

        def obra = Obra.get(params.obra.toLong())

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

        def firma = Persona.get(params.oferente).firma

        def lugar = obra.lugar
        def fecha = obra.fechaPreciosRubros
        def fechaIngreso = obra.fechaCreacionObra
        def itemsChofer = [obra.chofer]
        def itemsVolquete = [obra.volquete]

        def indi = obra.totales
//         println "aqui es el reporte"
//        preciosService.ac_rbroObra(obra.id)
        preciosService.ac_rbroObra(Obra.findByOferente(oferente).id)


        def baos = new ByteArrayOutputStream()
        def name = "rubros_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
        com.lowagie.text.Font times12bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times12normal = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font times18bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 18, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times16bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times14bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 14, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times10bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times10normal = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font times8bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD)
        com.lowagie.text.Font times8normal = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL)
        com.lowagie.text.Font times10boldWhite = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times8boldWhite = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD)
        times8boldWhite.setColor(Color.BLACK)
        times10boldWhite.setColor(Color.BLACK)
        def fonts = [times12bold: times12bold, times10bold: times10bold, times8bold: times8bold,
                times10boldWhite: times10boldWhite, times8boldWhite: times8boldWhite, times8normal: times8normal]

        Document document
        document = new Document(PageSize.A4);
        // margins: left, right, top, bottom
        // 1 in = 72, 1cm=28.1, 3cm = 86.4
        document.setMargins(45.2, 30, 56.2, 56.2);
        def pdfw = PdfWriter.getInstance(document, baos);
        document.open();
        document.addTitle("Rubros " + new Date().format("dd_MM_yyyy"));
        document.addSubject("Generado por el sistema Janus");
        document.addKeywords("reporte, janus, rubros");
        document.addAuthor("Janus");
        document.addCreator("Tedein SA");

        def prmsHeaderHoja = [border: Color.WHITE]
        def prmsHeaderHoja2 = [border: Color.WHITE, colspan: 3]
        def prmsHeaderHoja3 = [border: Color.WHITE, colspan: 2]
        def prmsHeaderHoja4 = [border: Color.WHITE, colspan: 1]
        def prmsHeaderHojaLeft = [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT]
        def prmsHeaderHojaLeft2 = [border: Color.WHITE, align: Element.ALIGN_LEFT, valign: Element.ALIGN_LEFT, bordeTop: "1"]
        def prmsHeader = [border: Color.WHITE, colspan: 8,
                align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE]
        def prmsHeader3 = [border: Color.WHITE, colspan: 8,
                align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE]
        def prmsHeader2 = [border: Color.WHITE, colspan: 3,
                align: Element.ALIGN_LEFT, valign: Element.ALIGN_MIDDLE]
        def prmsCellHead = [border: Color.WHITE,
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE, bordeTop: "1", bordeBot: "1"]
        def prmsCellCenter = [border: Color.WHITE, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellLeft = [border: Color.WHITE, valign: Element.ALIGN_MIDDLE]
        def prmsSubtotal = [border: Color.WHITE, colspan: 6,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
        def prmsSubtotalMat = [border: Color.WHITE, colspan: 5,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
        def prmsSubtotalTrans = [border: Color.WHITE, colspan: 7,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
        def prmsNum = [border: Color.WHITE, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]

        def prms = [prmsHeaderHoja: prmsHeaderHoja, prmsHeader: prmsHeader, prmsHeader2: prmsHeader2,
                prmsCellHead: prmsCellHead, prmsCell: prmsCellCenter, prmsCellLeft: prmsCellLeft, prmsSubtotal: prmsSubtotal, prmsNum: prmsNum,
                prmsHeaderHojaLeft: prmsHeaderHojaLeft, prmsSubtotalMat: prmsSubtotalMat, prmsHeader3: prmsHeader3, prmsSubtotalTrans: prmsSubtotalTrans]

        VolumenesObra.findAllByObra(obra, [sort: "orden"]).item.unique().each { rubro ->

            Paragraph headers = new Paragraph();
            addEmptyLine(headers, 1);
            headers.setAlignment(Element.ALIGN_CENTER);
            headers.add(new Paragraph("Formulario N° 4", times12bold));
            Paragraph nombreOferente = new Paragraph();
            addEmptyLine(nombreOferente, 1);
            nombreOferente.setAlignment(Element.ALIGN_LEFT);
            nombreOferente.setIndentationLeft(25)
            nombreOferente.add(new Paragraph("NOMBRE DEL OFERENTE: " + oferente?.nombre?.toUpperCase() + " " + oferente?.apellido?.toUpperCase(), times12bold));
            Paragraph proceso = new Paragraph();
            addEmptyLine(proceso, 1);
            proceso.setAlignment(Element.ALIGN_CENTER);
            proceso.add(new Paragraph("PROCESO: " + concurso?.codigo, times12bold));
            Paragraph analisis = new Paragraph();
            addEmptyLine(analisis, 1);
            analisis.setAlignment(Element.ALIGN_LEFT);
            analisis.setIndentationLeft(25)
            analisis.add(new Paragraph("ANÁLISIS DE PRECIOS UNITARIOS", times12bold));
            addEmptyLine(analisis, 1);
            addEmptyLine(analisis, 1);

            document.add(headers);
            document.add(nombreOferente)
            document.add(proceso)
            document.add(analisis)


            def id = rubro.id


//            def res = preciosService.presioUnitarioVolumenObra("* ", id, params.oferente)
            def res = preciosService.presioUnitarioVolumenObra("* ", id, obra?.id)

            PdfPTable headerRubroTabla = new PdfPTable(4); // 4 columns.
            headerRubroTabla.setWidthPercentage(90);
            headerRubroTabla.setWidths(arregloEnteros([12, 66, 12, 10]))

            addCellTabla(headerRubroTabla, new Paragraph("Proyecto:", times8bold), prmsHeaderHoja)
            addCellTabla(headerRubroTabla, new Paragraph(obra.nombre?.toUpperCase(), times8normal), prmsHeaderHoja2)
            addCellTabla(headerRubroTabla, new Paragraph("Rubro:", times8bold), prmsHeaderHoja)
            addCellTabla(headerRubroTabla, new Paragraph(rubro.nombre, times8normal), prmsHeaderHoja2)
            addCellTabla(headerRubroTabla, new Paragraph("Unidad:", times8bold), prmsHeaderHoja)
            addCellTabla(headerRubroTabla, new Paragraph(rubro.unidad.codigo, times8normal), prmsHeaderHoja2)
            PdfPTable tablaHerramientas = new PdfPTable(7);
            PdfPTable tablaManoObra = new PdfPTable(7);
            PdfPTable tablaMateriales = new PdfPTable(6);
            PdfPTable tablaTransporte = new PdfPTable(8);
            PdfPTable tablaIndirectos = new PdfPTable(3);
            PdfPTable tablaTotales = new PdfPTable(3);

            creaHeadersTabla(tablaHerramientas, fonts, prms, "EQUIPOS")
            creaHeadersTabla(tablaManoObra, fonts, prms, "MANO DE OBRA")
            creaHeadersTabla(tablaMateriales, fonts, prms, "MATERIALES INCLUYE TRANSPORTE")
            if (params.transporte == '1') {
                creaHeadersTabla(tablaTransporte, fonts, prms, "TRANSPORTE")
            }

            creaHeadersTabla(tablaIndirectos, fonts, prms, "COSTOS INDIRECTOS")

            def totalTrans = 0, totalHer = 0, totalMan = 0, totalMat = 0
            def totalRubro

            res.each { r ->
                if (r["grpocdgo"] == 3) {
                    llenaDatos(tablaHerramientas, r, fonts, prms, "H")
                    totalHer += r.parcial
//                    if (params.transporte != "1") {
//                        totalHer += r.parcial_t
//                    }
                }
                if (r["grpocdgo"] == 2) {
                    llenaDatos(tablaManoObra, r, fonts, prms, "O")
                    totalMan += r.parcial
//                    if (params.transporte != "1") {
//                        totalMan += r.parcial_t
//                    }
                }
                if (r["grpocdgo"] == 1) {
                    if (params.transporte == "1") {
                        llenaDatos(tablaMateriales, r, fonts, prms, "M")
                    } else {
                        llenaDatos(tablaMateriales, r, fonts, prms, "MNT")

                    }
                    totalMat += r.parcial
                    if (params.transporte != "1") {
                        totalMat += r.parcial_t
                    }
                }
                if (r["grpocdgo"] == 1 && params.transporte == "1") {
                    llenaDatos(tablaTransporte, r, fonts, prms, "T")
                    totalTrans += r.parcial_t
//                    println("-->>" + totalTrans)
                }
            }
            totalRubro = totalHer + totalMan + totalMat
            if (params.transporte == "1") {
                totalRubro += totalTrans
            }
            def totalIndi = totalRubro * (indi / 100)

            addSubtotal(tablaHerramientas, totalHer, fonts, prms)
            addSubtotal(tablaManoObra, totalMan, fonts, prms)
            addSubtotalMat(tablaMateriales, totalMat, fonts, prms)

            if (params.transporte == "1") {
                addSubtotalTrans(tablaTransporte, totalTrans, fonts, prms)


            }

            addCellTabla(tablaIndirectos, new Paragraph("Costos Indirectos", fonts.times8normal), prmsCellLeft)
            addCellTabla(tablaIndirectos, new Paragraph(g.formatNumber(number: indi, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec") + "%", fonts.times8normal), prmsNum)
            addCellTabla(tablaIndirectos, new Paragraph(g.formatNumber(number: totalIndi, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), prmsNum)

            tablaTotales.setWidthPercentage(90);
            tablaTotales.setWidths(arregloEnteros([80, 40, 40]))

            addCellTabla(tablaTotales, new Paragraph(" ", fonts.times8bold), prmsHeaderHoja)
            prmsCellLeft.put("bordeTop", "1")
            prmsNum.put("bordeTop", "1")
            addCellTabla(tablaTotales, new Paragraph("COSTO UNITARIO DIRECTO", fonts.times8bold), prmsCellLeft)
            addCellTabla(tablaTotales, new Paragraph(g.formatNumber(number: totalRubro, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8bold), prmsNum)
            prmsCellLeft.remove("bordeTop")
            prmsNum.remove("bordeTop")
            addCellTabla(tablaTotales, new Paragraph(" ", fonts.times8bold), prmsHeaderHoja)
            addCellTabla(tablaTotales, new Paragraph("COSTOS INDIRECTOS", fonts.times8bold), prmsCellLeft)
            addCellTabla(tablaTotales, new Paragraph(g.formatNumber(number: totalIndi, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8bold), prmsNum)

            addCellTabla(tablaTotales, new Paragraph(" ", fonts.times8bold), prmsHeaderHoja)
            addCellTabla(tablaTotales, new Paragraph("COSTO TOTAL DEL RUBRO", fonts.times8bold), prmsCellLeft)
            addCellTabla(tablaTotales, new Paragraph(g.formatNumber(number: totalRubro + totalIndi, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8bold), prmsNum)

            addCellTabla(tablaTotales, new Paragraph(" ", fonts.times8bold), prmsHeaderHoja)

            prmsCellLeft.put("bordeBot", "1")
            prmsNum.put("bordeBot", "1")
            addCellTabla(tablaTotales, new Paragraph("PRECIO UNITARIO (\$USD)", fonts.times8bold), prmsCellLeft)
            addCellTabla(tablaTotales, new Paragraph(g.formatNumber(number: totalRubro + totalIndi, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8bold), prmsNum)
            prmsCellLeft.remove("bordeBot")
            prmsNum.remove("bordeBot")

            PdfPTable pieTabla = new PdfPTable(2);
            pieTabla.setWidthPercentage(90);
            pieTabla.setWidths(arregloEnteros([99, 1]))

            addCellTabla(pieTabla, new Paragraph("Nota: Los cálculos se hacen con todos los decimales y el resultado final se lo redondea a dos decimales, estos precios no incluyen IVA.   ", fonts.times8normal), prmsHeaderHojaLeft)
            addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)


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

            addCellTabla(pieTabla, new Paragraph(firma, fonts.times10bold), prmsHeaderHojaLeft)
            addCellTabla(pieTabla, new Paragraph(" ", fonts.times8normal), prmsHeaderHojaLeft)



            addTablaHoja(document, headerRubroTabla, false)
            addTablaHoja(document, tablaHerramientas, false)
            addTablaHoja(document, tablaManoObra, false)
            addTablaHoja(document, tablaMateriales, false)
            addTablaHoja(document, tablaTransporte, false)
            addTablaHoja(document, tablaIndirectos, false)
            addTablaHoja(document, tablaTotales, true)
            addTablaHoja(document, pieTabla, false)

            document.newPage();
//            println res
        }

        document.close();
        pdfw.close()
        byte[] b = baos.toByteArray();
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=" + name)
        response.setContentLength(b.length)
        response.getOutputStream().write(b)

    }

    def llenaDatos(table, r, fonts, params, tipo) {
        addCellTabla(table, new Paragraph(r.itemcdgo, fonts.times8normal), params.prmsCellLeft)
        addCellTabla(table, new Paragraph(r.itemnmbr, fonts.times8normal), params.prmsCellLeft)
        switch (tipo) {
            case "H":
            case "O":
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun * r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rndm, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                break;
            case "M":



                addCellTabla(table, new Paragraph(r.unddcdgo, fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph("", fonts.times8normal), params.prmsCell)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                break;
            case "MNT":


                addCellTabla(table, new Paragraph(r.unddcdgo, fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: ((r.parcial + r.parcial_t) / r.rbrocntd), minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph("", fonts.times8normal), params.prmsCell)
                addCellTabla(table, new Paragraph(g.formatNumber(number: (r.parcial + r.parcial_t), minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                break;
            case "T":

                addCellTabla(table, new Paragraph(r.unddcdgo, fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.itempeso, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.distancia, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.tarifa, minfractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial_t, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8normal), params.prmsNum)
                break;
        }
    }

    def addSubtotal(table, subtotal, fonts, params) {
        addCellTabla(table, new Paragraph("TOTAL", fonts.times8bold), params.prmsSubtotal)
        addCellTabla(table, new Paragraph(g.formatNumber(number: subtotal, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8bold), params.prmsNum)
    }


    def addSubtotalMat(table, subtotal, fonts, params) {
        addCellTabla(table, new Paragraph("TOTAL", fonts.times8bold), params.prmsSubtotalMat)
        addCellTabla(table, new Paragraph(g.formatNumber(number: subtotal, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8bold), params.prmsNum)
    }

    def addSubtotalTrans(table, subtotal, fonts, params) {
        addCellTabla(table, new Paragraph("TOTAL", fonts.times8bold), params.prmsSubtotalTrans)
        addCellTabla(table, new Paragraph(g.formatNumber(number: subtotal, minFractionDigits: 5, maxFractionDigits: 5, format: "##,##0", locale: "ec"), fonts.times8bold), params.prmsNum)
    }



    def creaHeadersTabla(table, fonts, params, String tipo) {
        table.setWidthPercentage(90);
        if (tipo == "COSTOS INDIRECTOS") {


            addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader)
            table.setWidths(arregloEnteros([65, 15, 20]))
            addCellTabla(table, new Paragraph("DESCRIPCIÓN", fonts.times8boldWhite), params.prmsCellHead)
            addCellTabla(table, new Paragraph("PORCENTAJE", fonts.times8boldWhite), params.prmsCellHead)
            addCellTabla(table, new Paragraph("VALOR", fonts.times8boldWhite), params.prmsCellHead)


        } else {
            if (tipo == "MANO DE OBRA") {
                table.setWidths(arregloEnteros([10, 38, 12, 13, 10, 15, 13]))
//                table.setWidths(arregloEnteros([10, 30, 10, 10, 10, 10, 10]))

                addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader)
                addCellTabla(table, new Paragraph("CÓDIGO", fonts.times8boldWhite), params.prmsCellHead)
                addCellTabla(table, new Paragraph("DESCRIPCIÓN", fonts.times8boldWhite), params.prmsCellHead)
                addCellTabla(table, new Paragraph("CANTIDAD", fonts.times8boldWhite), params.prmsCellHead)
                addCellTabla(table, new Paragraph("JORNAL(\$/H)", fonts.times8boldWhite), params.prmsCellHead)
                addCellTabla(table, new Paragraph("COSTO(\$)", fonts.times8boldWhite), params.prmsCellHead)
                addCellTabla(table, new Paragraph("RENDIMIENTO", fonts.times8boldWhite), params.prmsCellHead)
                addCellTabla(table, new Paragraph("C.TOTAL(\$)", fonts.times8boldWhite), params.prmsCellHead)
            } else {
                if (tipo == "MATERIALES INCLUYE TRANSPORTE") {

                    table.setWidths(arregloEnteros([12, 38, 12, 13, 13, 15]))
                    addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader)
                    addCellTabla(table, new Paragraph("CÓDIGO", fonts.times8boldWhite), params.prmsCellHead)
                    addCellTabla(table, new Paragraph("DESCRIPCIÓN", fonts.times8boldWhite), params.prmsCellHead)
                    addCellTabla(table, new Paragraph("UNIDAD", fonts.times8boldWhite), params.prmsCellHead)
                    addCellTabla(table, new Paragraph("CANTIDAD", fonts.times8boldWhite), params.prmsCellHead)
                    addCellTabla(table, new Paragraph("UNITARIO(\$)", fonts.times8boldWhite), params.prmsCellHead)
//                addCellTabla(table, new Paragraph("", fonts.times8boldWhite), params.prmsCellHead)
                    addCellTabla(table, new Paragraph("C.TOTAL(\$)", fonts.times8boldWhite), params.prmsCellHead)

                } else {


                    if (tipo == "TRANSPORTE") {

                        table.setWidths(arregloEnteros([9, 24, 8, 10, 12, 11, 11, 11]))
                        addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader)
                        addCellTabla(table, new Paragraph("CÓDIGO", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("DESCRIPCIÓN", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("UNIDAD", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("PESO/VOL", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("CANTIDAD", fonts.times8boldWhite), params.prmsCellHead)


                        addCellTabla(table, new Paragraph("DISTANCIA", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("TARIFA", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("C.TOTAL(\$)", fonts.times8boldWhite), params.prmsCellHead)

                    } else {


                        table.setWidths(arregloEnteros([12, 35, 12, 13, 12, 17, 13]))
                        addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader)
                        addCellTabla(table, new Paragraph("CÓDIGO", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("DESCRIPCIÓN", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("CANTIDAD", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("TARIFA(\$/H)", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("COSTO(\$)", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("RENDIMIENTO", fonts.times8boldWhite), params.prmsCellHead)
                        addCellTabla(table, new Paragraph("C.TOTAL(\$)", fonts.times8boldWhite), params.prmsCellHead)


                    }


                }


            }


        }
    }

    def addTablaHoja(document, table, right) {
        Paragraph paragraph = new Paragraph()
        if (right) {
            paragraph.setAlignment(Element.ALIGN_RIGHT);
        }
        paragraph.setSpacingAfter(10);
//        addEmptyLine(paragraph, 1);
        paragraph.add(table);
        document.add(paragraph);
    }

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

//    def llenaDatos(PdfPTable table, r, fonts, params, tipo) {
//        addCellTabla(table, new Paragraph(r.itemcdgo, fonts.times8normal), params.prmsCellLeft)
//        addCellTabla(table, new Paragraph(r.itemnmbr, fonts.times8normal), params.prmsCellLeft)
//        switch (tipo) {
//            case "H":
//            case "O":
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun * r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rndm, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                break;
//            case "M":
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph("", fonts.times8normal), params.prmsCell)
//                addCellTabla(table, new Paragraph("", fonts.times8normal), params.prmsCell)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                break;
//            case "MNT":
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbpcpcun, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph("", fonts.times8normal), params.prmsCell)
//                addCellTabla(table, new Paragraph("", fonts.times8normal), params.prmsCell)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: (r.parcial + r.parcial_t), minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                break;
//            case "T":
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.itempeso, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.rbrocntd, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.distancia, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial_t / (r.itempeso * r.rbrocntd * r.distancia), minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                addCellTabla(table, new Paragraph(g.formatNumber(number: r.parcial_t, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8normal), params.prmsNum)
//                break;
//        }
//    }
//
//    def addSubtotal(PdfPTable table, subtotal, fonts, params) {
//        addCellTabla(table, new Paragraph("Subtotal", fonts.times8bold), params.prmsSubtotal)
//        addCellTabla(table, new Paragraph(g.formatNumber(number: subtotal, minFractionDigits: 5, maxFractionDigits: 5, format: "##,#####0", locale: "ec"), fonts.times8bold), params.prmsNum)
//    }
//
//    def creaHeadersTabla(PdfPTable table, fonts, params, String tipo) {
//        table.setWidthPercentage(100);
//        if (tipo == "Costos Indirectos") {
//            table.setWidths(arregloEnteros([70, 15, 15]))
//            addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader2)
//
//            addCellTabla(table, new Paragraph("Descripción", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Porcentaje", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Valor", fonts.times8boldWhite), params.prmsCellHead)
//        } else {
//            table.setWidths(arregloEnteros([10, 48, 8, 8, 8, 10, 8]))
//            addCellTabla(table, new Paragraph(tipo, fonts.times10boldWhite), params.prmsHeader)
//
//            addCellTabla(table, new Paragraph("Código", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Descripción", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Cantidad", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Tarifa", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Costo", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("Rendimiento", fonts.times8boldWhite), params.prmsCellHead)
//            addCellTabla(table, new Paragraph("C.Total", fonts.times8boldWhite), params.prmsCellHead)
//        }
//    }
//
//    def addTablaHoja(Document document, PdfPTable table, boolean right) {
//        Paragraph paragraph = new Paragraph()
//        if (right) {
//            paragraph.setAlignment(Element.ALIGN_RIGHT);
//        }
//        paragraph.setSpacingAfter(10);
////        addEmptyLine(paragraph, 1);
//        paragraph.add(table);
//        document.add(paragraph);
//    }
//
//    def addCellTabla(PdfPTable table, Paragraph paragraph, params) {
//        PdfPCell cell = new PdfPCell(paragraph);
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
//        }
//        table.addCell(cell);
//    }
//


    def reporteRegistro() {

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


        def auxiliar = Auxiliar.get(1)


        def prmsHeaderHoja = [border: Color.WHITE]
        def prmsHeaderHoja3 = [border: Color.WHITE, colspan: 2]


        def prmsHeaderHoja2 = [border: Color.WHITE, colspan: 9]


        def prmsHeader = [border: Color.WHITE, colspan: 7, bg: new Color(73, 175, 205),
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]



        def prmsHeader2 = [border: Color.WHITE, colspan: 3, bg: new Color(73, 175, 205),
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellHead = [border: Color.WHITE, bg: new Color(73, 175, 205),
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellCenter = [border: Color.BLACK, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellRight = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_RIGHT]
        def prmsCellLeft = [border: Color.BLACK, valign: Element.ALIGN_MIDDLE]
        def prmsSubtotal = [border: Color.BLACK, colspan: 6,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
        def prmsNum = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]

        def prms = [prmsHeaderHoja: prmsHeaderHoja, prmsHeader: prmsHeader, prmsHeader2: prmsHeader2,
                prmsCellHead: prmsCellHead, prmsCell: prmsCellCenter, prmsCellLeft: prmsCellLeft, prmsSubtotal: prmsSubtotal, prmsNum: prmsNum, prmsHeaderHoja2: prmsHeaderHoja2, prmsCellRight: prmsCellRight]



        def baos = new ByteArrayOutputStream()
        def name = "presupuesto_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
        com.lowagie.text.Font times12bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times14bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 14, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times10bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times10normal = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font times8bold = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD)
        com.lowagie.text.Font times8normal = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL)
        com.lowagie.text.Font times10boldWhite = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font times8boldWhite = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 8, com.lowagie.text.Font.BOLD)
        times8boldWhite.setColor(Color.WHITE)
        times10boldWhite.setColor(Color.WHITE)
        def fonts = [times12bold: times12bold, times10bold: times10bold, times8bold: times8bold,
                times10boldWhite: times10boldWhite, times8boldWhite: times8boldWhite, times8normal: times8normal, times10normal: times10normal]

        Document document
        document = new Document(PageSize.A4);
        document.setMargins(56.2, 30, 56.2, 56.2);
        def pdfw = PdfWriter.getInstance(document, baos);
        document.open();
        document.addTitle("Registro " + new Date().format("dd_MM_yyyy"));
        document.addSubject("Generado por el sistema Janus");
        document.addKeywords("documentosObra, janus, presupuesto");
        document.addAuthor("Janus");
        document.addCreator("Tedein SA");



        Paragraph headers = new Paragraph();
        addEmptyLine(headers, 1);
        headers.setAlignment(Element.ALIGN_CENTER);
        headers.add(new Paragraph("G.A.D. PROVINCIA PICHINCHA", times14bold));
        headers.add(new Paragraph(" ", times10bold));
        headers.add(new Paragraph("PROCESO: " + concurso?.codigo, times12bold));
        headers.add(new Paragraph(" ", times10bold));
        headers.add(new Paragraph("DATOS DE LA OBRA ", times12bold));
        headers.add(new Paragraph(" ", times10bold));
        document.add(headers)


        PdfPTable tablaCoeficiente = new PdfPTable(3);
        tablaCoeficiente.setWidthPercentage(100);
        tablaCoeficiente.setWidths(arregloEnteros([30, 20, 50]))

        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente, new Paragraph("Proyecto: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.nombre, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente, new Paragraph("Descripción: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.descripcion, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente, new Paragraph("Dirección: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.departamento?.descripcion, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente, new Paragraph("Programa: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.programacion?.descripcion, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente, new Paragraph("Clase de Obra: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.claseObra?.descripcion, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente, new Paragraph("Plazo: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.plazoEjecucionMeses + " Mes(es)" + " " + obra?.plazoEjecucionDias + " Días", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente, new Paragraph("Anticipo: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(obra?.porcentajeAnticipo + " %", times10normal), prmsHeaderHoja3)


        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)


        PdfPTable tablaDistancias = new PdfPTable(4);
        tablaDistancias.setWidthPercentage(100);
        tablaDistancias.setWidths(arregloEnteros([25, 25, 25, 25]))

        PdfPTable tablaCoeficiente2 = new PdfPTable(3);
        tablaCoeficiente2.setWidthPercentage(100);
        tablaCoeficiente2.setWidths(arregloEnteros([30, 40, 30]))

        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente2, new Paragraph("Ubicación", times12bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente2, new Paragraph("Cantón: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.parroquia?.canton?.nombre, times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente2, new Paragraph("Parroquia: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.parroquia?.nombre, times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente2, new Paragraph("Comunidad: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.comunidad?.nombre, times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente2, new Paragraph("Barrio: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.barrio, times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        addCellTabla(tablaCoeficiente2, new Paragraph("Sitio: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.sitio, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente2, new Paragraph("Coordenadas: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.coordenadas, times10normal), prmsHeaderHoja3)

        addCellTabla(tablaCoeficiente2, new Paragraph("Observaciones: ", times10bold), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(obra?.observaciones, times10normal), prmsHeaderHoja)
        addCellTabla(tablaCoeficiente2, new Paragraph(" ", times10normal), prmsHeaderHoja)

        document.add(tablaCoeficiente)
        document.add(tablaDistancias)
        document.add(tablaCoeficiente2)


        document.close();
        pdfw.close()
        byte[] b = baos.toByteArray();
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=" + name)
        response.setContentLength(b.length)
        response.getOutputStream().write(b)

//        def obra = Obra.get(params.id)
//
//        def auxiliar = Auxiliar.get(1)
//
//
//        def prmsHeaderHoja = [border: Color.WHITE]
//
//
//        def prmsHeaderHoja2 = [border: Color.WHITE, colspan: 9]
//
//
//        def prmsHeader = [border: Color.WHITE, colspan: 7, bg: new Color(73, 175, 205),
//                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
//
//
//
//        def prmsHeader2 = [border: Color.WHITE, colspan: 3, bg: new Color(73, 175, 205),
//                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
//        def prmsCellHead = [border: Color.WHITE, bg: new Color(73, 175, 205),
//                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
//        def prmsCellCenter = [border: Color.BLACK, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
//        def prmsCellRight = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_RIGHT]
//        def prmsCellLeft = [border: Color.BLACK, valign: Element.ALIGN_MIDDLE]
//        def prmsSubtotal = [border: Color.BLACK, colspan: 6,
//                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
//        def prmsNum = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
//
//        def prms = [prmsHeaderHoja: prmsHeaderHoja, prmsHeader: prmsHeader, prmsHeader2: prmsHeader2,
//                prmsCellHead: prmsCellHead, prmsCell: prmsCellCenter, prmsCellLeft: prmsCellLeft, prmsSubtotal: prmsSubtotal, prmsNum: prmsNum, prmsHeaderHoja2: prmsHeaderHoja2, prmsCellRight: prmsCellRight]
//
//
//
//        def baos = new ByteArrayOutputStream()
//        def name = "presupuesto_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
//        Font times12bold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
//        Font times10bold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
//        Font times10normal = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
//        Font times8bold = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
//        Font times8normal = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL)
//        Font times10boldWhite = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
//        Font times8boldWhite = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
//        times8boldWhite.setColor(Color.WHITE)
//        times10boldWhite.setColor(Color.WHITE)
//        def fonts = [times12bold: times12bold, times10bold: times10bold, times8bold: times8bold,
//                times10boldWhite: times10boldWhite, times8boldWhite: times8boldWhite, times8normal: times8normal, times10normal: times10normal]
//
//        Document document
//        document = new Document(PageSize.A4);
//        def pdfw = PdfWriter.getInstance(document, baos);
//        document.open();
//        document.addTitle("Registro " + new Date().format("dd_MM_yyyy"));
//        document.addSubject("Generado por el sistema Janus");
//        document.addKeywords("documentosObra, janus, presupuesto");
//        document.addAuthor("Janus");
//        document.addCreator("Tedein SA");
//
//
//
//        Paragraph headers = new Paragraph();
//        addEmptyLine(headers, 1);
//        headers.setAlignment(Element.ALIGN_CENTER);
//        headers.add(new Paragraph(auxiliar.titulo, times12bold));
//        headers.add(new Paragraph(" ", times10bold));
//        headers.add(new Paragraph(" ", times10bold));
//        headers.add(new Paragraph("DESCRIPCIÓN DE LA OBRA", times10bold));
//
//        document.add(headers)
//
//
//        PdfPTable tablaCoeficiente = new PdfPTable(3);
//        tablaCoeficiente.setWidthPercentage(100);
//        tablaCoeficiente.setWidths(arregloEnteros([30, 40, 30]))
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Código: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.oficioIngreso, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10bold), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Obra: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.nombre, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Descripción: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.descripcion, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Tipo de Obra: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.tipoObjetivo?.descripcion, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Programa: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.programacion?.descripcion, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Clase de Obra: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.claseObra?.descripcion, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Plazo: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.plazoEjecucionMeses + " Mes(es)" + " " + obra?.plazoEjecucionDias + " Días", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Oficio de Ingreso: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.oficioIngreso, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Oficio de Salida: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.oficioSalida, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Distancia Peso: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(g.formatNumber(number: obra?.distanciaPeso, format: "###.##", locale: "ec"), times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Distancia Volumen: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(g.formatNumber(number: obra?.distanciaVolumen, format: "###.##", locale: "ec"), times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Ubicación", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Cantón: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.parroquia?.canton?.nombre, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Parroquia: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.parroquia?.nombre, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Comunidad: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.comunidad?.nombre, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Barrio: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.barrio, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Sitio: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.sitio, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Lugar de Referencia de Precios: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.lugar?.descripcion, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Datos Generales", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Inspector: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.inspector?.nombre + " " + obra?.inspector?.apellido, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Revisor: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.revisor?.nombre + " " + obra?.revisor?.apellido, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Referencias: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.referencia, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Fecha de Registro: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(formatDate(date: obra?.fechaCreacionObra, format: "yyyy-MM-dd"), times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        addCellTabla(tablaCoeficiente, new Paragraph("Observaciones: ", times10bold), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(obra?.observaciones, times10normal), prmsHeaderHoja)
//        addCellTabla(tablaCoeficiente, new Paragraph(" ", times10normal), prmsHeaderHoja)
//
//        document.add(tablaCoeficiente)
//
//
//        document.close();
//        pdfw.close()
//        byte[] b = baos.toByteArray();
//        response.setContentType("application/pdf")
//        response.setHeader("Content-disposition", "attachment; filename=" + name)
//        response.setContentLength(b.length)
//        response.getOutputStream().write(b)


    }





    def documentosObraExcel() {

        def obra = Obra.get(params.id)


        def detalle

        detalle = VolumenesObra.findAllByObra(obra, [sort: "orden"])
        def subPres = VolumenesObra.findAllByObra(obra, [sort: "orden"]).subPresupuesto.unique()

        def precios = [:]
        def fecha = obra.fechaPreciosRubros
        def dsps = obra.distanciaPeso
        def dsvl = obra.distanciaVolumen
        def lugar = obra.lugar
        def prch = 0
        def prvl = 0
        if (obra.chofer) {
            prch = preciosService.getPrecioItems(fecha, lugar, [obra.chofer])
            prch = prch["${obra.chofer.id}"]
            prvl = preciosService.getPrecioItems(fecha, lugar, [obra.volquete])
            prvl = prvl["${obra.volquete.id}"]
        }
        def rendimientos = preciosService.rendimientoTranposrte(dsps, dsvl, prch, prvl)

        if (rendimientos["rdps"].toString() == "NaN")
            rendimientos["rdps"] = 0
        if (rendimientos["rdvl"].toString() == "NaN")
            rendimientos["rdvl"] = 0

        def indirecto = obra.totales / 100

        def c;

        def total1 = 0;

        def totales

        def totalPresupuesto;

        //excel
        WorkbookSettings workbookSettings = new WorkbookSettings()
        workbookSettings.locale = Locale.default

        def file = File.createTempFile('myExcelDocument', '.xls')
        file.deleteOnExit()
//        println "paso"
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings)

        WritableFont font = new WritableFont(WritableFont.ARIAL, 12)
        WritableCellFormat formatXls = new WritableCellFormat(font)

        def row = 0
        WritableSheet sheet = workbook.createSheet('MySheet', 0)
        // fija el ancho de la columna
        // sheet.setColumnView(1,40)


        params.id = params.id.split(",")
        if (params.id.class == java.lang.String) {
            params.id = [params.id]
        }
        WritableFont times16font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
        WritableCellFormat times16format = new WritableCellFormat(times16font);
        sheet.setColumnView(0, 60)
        sheet.setColumnView(1, 12)
        sheet.setColumnView(2, 25)
        sheet.setColumnView(3, 25)
        sheet.setColumnView(4, 30)
        sheet.setColumnView(8, 20)
        // inicia textos y numeros para asocias a columnas
//        def label = new Label(0, 1, "Texto", times16format);
//        def nmro = new Number(12, 1, 9999);

        def label
        def nmro

        def fila = 6;

        label = new Label(0, 2, "Presupuesto de la Obra: " + obra?.nombre.toString(), times16format); sheet.addCell(label);

        label = new Label(0, 4, "RUBRO", times16format); sheet.addCell(label);
        label = new Label(1, 4, "NOMBRE", times16format); sheet.addCell(label);
        label = new Label(2, 4, "UNDD", times16format); sheet.addCell(label);
        label = new Label(3, 4, "CANTIDAD", times16format); sheet.addCell(label);
        label = new Label(4, 4, "P_UNIT", times16format); sheet.addCell(label);
        label = new Label(5, 4, "SUBTOTAL", times16format); sheet.addCell(label);
//        label = new Label(6, 4, "SUBP", times16format); sheet.addCell(label);
//        label = new Label(7, 4, "SUBPRESUPUESTO", times16format); sheet.addCell(label);
//        label = new Label(8, 4, "ORDEN", times16format); sheet.addCell(label);


        detalle.each {

            def parametros = "" + it.item.id + "," + lugar.id + ",'" + fecha.format("yyyy-MM-dd") + "'," + dsps.toDouble() + "," + dsvl.toDouble() + "," + rendimientos["rdps"] + "," + rendimientos["rdvl"]
            preciosService.ac_rbro(it.item.id, lugar.id, fecha.format("yyyy-MM-dd"))
            def res = preciosService.rb_precios("sum(parcial)+sum(parcial_t) precio ", parametros, "")
            precios.put(it.id.toString(), res["precio"][0] + res["precio"][0] * indirecto)

            def precioUnitario = precios[it.id.toString()]

            def subtotal = (precios[it.id.toString()] * it.cantidad)

//            println(precioUnitario)

            label = new Label(0, fila, it?.item?.codigo.toString()); sheet.addCell(label);
            label = new Label(1, fila, it?.item?.nombre.toString()); sheet.addCell(label);
            label = new Label(2, fila, it?.item?.unidad?.codigo.toString()); sheet.addCell(label);
            label = new Label(3, fila, it?.cantidad.toString()); sheet.addCell(label);
            label = new Label(4, fila, precioUnitario.toString()); sheet.addCell(label);
            label = new Label(5, fila, subtotal.toString()); sheet.addCell(label);

            fila++

//            return totalPresupuesto

        }

        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "DocumentosObraExcel.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());
    }




    def reporteExcelVolObra() {

        def obra = Obra.get(params.id)

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



        def detalle

        detalle = VolumenesObra.findAllByObra(obra, [sort: "orden"])

        def subPres = VolumenesObra.findAllByObra(obra, [sort: "orden"]).subPresupuesto.unique()

        def precios = [:]



        def indirecto = obra.totales / 100

        def c;

        def total1 = 0;

        def totales

        def totalPresupuesto;

        //excel

        WorkbookSettings workbookSettings = new WorkbookSettings()
        workbookSettings.locale = Locale.default

        def file = File.createTempFile('myExcelDocument', '.xls')
        file.deleteOnExit()
//        println "paso"
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings)

        WritableFont font = new WritableFont(WritableFont.ARIAL, 12)
        WritableCellFormat formatXls = new WritableCellFormat(font)

        def row = 0
        WritableSheet sheet = workbook.createSheet('MySheet', 0)
        // fija el ancho de la columna
        // sheet.setColumnView(1,40)


        params.id = params.id.split(",")
        if (params.id.class == java.lang.String) {
            params.id = [params.id]
        }
        WritableFont times16font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
        WritableCellFormat times16format = new WritableCellFormat(times16font);
        sheet.setColumnView(0, 12)
        sheet.setColumnView(1, 25)
        sheet.setColumnView(2, 40)
        sheet.setColumnView(3, 50)
        sheet.setColumnView(4, 25)
        sheet.setColumnView(5, 25)
        sheet.setColumnView(6, 25)


        def label
        def number
        def nmro
        def numero = 1;

        def fila = 11;

        def ultimaFila





        label = new Label(2, 2, "NOMBRE DEL OFERENTE: " + oferente?.nombre.toUpperCase() + " " + oferente?.apellido.toUpperCase(), times16format); sheet.addCell(label);
        label = new Label(2, 3, "PROCESO: " + concurso?.codigo, times16format); sheet.addCell(label);
        label = new Label(2, 4, "TABLA DE DESCRIPCIÓN DE RUBROS, UNIDADES, CANTIDADES Y PRECIOS", times16format); sheet.addCell(label);
        label = new Label(2, 5, "GOBIERNO AUTÓNOMO DESCENTRALIZADO DE LA PROVINCIA DE PICHINCHA", times16format); sheet.addCell(label);
        label = new Label(2, 6, "NOMBRE DEL PROYECTO: " + obra?.nombre.toUpperCase(), times16format); sheet.addCell(label);

        label = new Label(0, 10, "N°", times16format); sheet.addCell(label);
        label = new Label(1, 10, "RUBRO", times16format); sheet.addCell(label);
        label = new Label(2, 10, "SUBPRESUPUESTO", times16format); sheet.addCell(label);
        label = new Label(3, 10, "COMPONENTE DEL PROYECTO/ITEM", times16format); sheet.addCell(label);
        label = new Label(4, 10, "UNIDAD", times16format); sheet.addCell(label);
        label = new Label(5, 10, "CANTIDAD", times16format); sheet.addCell(label);
        label = new Label(6, 10, "UNITARIO", times16format); sheet.addCell(label);
        label = new Label(7, 10, "C.TOTAL", times16format); sheet.addCell(label);

        detalle.each {

            def res = preciosService.presioUnitarioVolumenObra("sum(parcial)+sum(parcial_t) precio ", it.item.id, params.oferente)

            def precio = 0
            if (res["precio"][0] != null && res["precio"][0] != "null")
                precio = res["precio"][0]
            precios.put(it.id.toString(), (precio + precio * indirecto).toDouble().round(2))

            def precioUnitario = precios[it.id.toString()];
            def subtotal = precios[it.id.toString()] * it.cantidad;

            number = new Number(0, fila, numero++); sheet.addCell(number);
            label = new Label(1, fila, it?.item?.codigo.toString()); sheet.addCell(label);
            label = new Label(2, fila, it?.subPresupuesto?.descripcion.toString()); sheet.addCell(label);
            label = new Label(3, fila, it?.item?.nombre.toString()); sheet.addCell(label);
            label = new Label(4, fila, it?.item?.unidad?.codigo.toString()); sheet.addCell(label);
            number = new Number(5, fila, it?.cantidad); sheet.addCell(number);
            number = new Number(6, fila, precioUnitario.round(2)); sheet.addCell(number);
            number = new Number(7, fila, subtotal.round(2)); sheet.addCell(number);

            fila++

            totales = precios[it.id.toString()] * it.cantidad

            totalPresupuesto = (total1 += totales);

            ultimaFila = fila

        }

        label = new Label(6, ultimaFila, "TOTAL ", times16format); sheet.addCell(label);
        number = new Number(7, ultimaFila, totalPresupuesto.round(2)); sheet.addCell(number);



        workbook.write();
        workbook.close();
        def output = response.getOutputStream()
        def header = "attachment; filename=" + "VolObraExcel.xls";
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition", header);
        output.write(file.getBytes());


    }


    def dummyReportes() {

        return false

    }



    def pagarAnticipoPdf() {


        def baos = new ByteArrayOutputStream()
        def name = "pagarAnticipo_" + new Date().format("ddMMyyyy_hhmm") + ".pdf";
        Font times12bold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
        Font times10bold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
        Font times8bold = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
        Font times8normal = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL)
        Font times10boldWhite = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
        Font times8boldWhite = new Font(Font.TIMES_ROMAN, 8, Font.BOLD)
        times8boldWhite.setColor(Color.WHITE)
        times10boldWhite.setColor(Color.WHITE)
        def fonts = [times12bold: times12bold, times10bold: times10bold, times8bold: times8bold,
                times10boldWhite: times10boldWhite, times8boldWhite: times8boldWhite, times8normal: times8normal]

        Document document
        document = new Document(PageSize.A4);
        def pdfw = PdfWriter.getInstance(document, baos);
        document.open();
        document.addTitle("Pagar Anticipo " + new Date().format("dd_MM_yyyy"));
        document.addSubject("Generado por el sistema Janus");
        document.addKeywords("reporte, janus, rubros");
        document.addAuthor("Janus");
        document.addCreator("Tedein SA");


        def prmsHeaderHoja = [border: Color.WHITE]
        def prmsHeader = [border: Color.WHITE, colspan: 7, bg: new Color(73, 175, 205),
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsHeader2 = [border: Color.WHITE, colspan: 3, bg: new Color(73, 175, 205),
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellHead = [border: Color.WHITE, bg: new Color(73, 175, 205),
                align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellCenter = [border: Color.BLACK, align: Element.ALIGN_CENTER, valign: Element.ALIGN_MIDDLE]
        def prmsCellLeft = [border: Color.BLACK, valign: Element.ALIGN_MIDDLE]
        def prmsSubtotal = [border: Color.BLACK, colspan: 6,
                align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]
        def prmsNum = [border: Color.BLACK, align: Element.ALIGN_RIGHT, valign: Element.ALIGN_MIDDLE]

        def prms = [prmsHeaderHoja: prmsHeaderHoja, prmsHeader: prmsHeader, prmsHeader2: prmsHeader2,
                prmsCellHead: prmsCellHead, prmsCell: prmsCellCenter, prmsCellLeft: prmsCellLeft, prmsSubtotal: prmsSubtotal, prmsNum: prmsNum]

        def planilla = janus.ejecucion.Planilla.get(params.id)

        def contrato = Contrato.get(planilla?.contrato?.id)

        def obra = Obra.get(contrato?.oferta?.concurso?.obra?.id)

        def suma = (planilla?.reajuste + planilla?.valor)

        PdfPTable headerRubroTabla = new PdfPTable(4); // 4 columns.
        headerRubroTabla.setWidthPercentage(100);
        headerRubroTabla.setWidths(arregloEnteros([10, 40, 10, 40]))


        addCellTabla(headerRubroTabla, new Paragraph("Obra:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(obra?.nombre + " " + obra?.descripcion, times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(" ", times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Lugar:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(obra?.lugar?.descripcion, times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Planilla:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(g.formatNumber(number: planilla?.numero, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Ubicación:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(obra?.parroquia?.nombre + " " + obra?.parroquia?.canton?.nombre, times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Monto Contrato:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(g.formatNumber(number: contrato?.monto, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Contratista:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(contrato?.oferta?.proveedor?.nombre, times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Período:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph((planilla.tipoPlanilla.codigo == 'A' ? 'Anticipo' : 'del ' + planilla.fechaInicio.format('dd-MM-yyyy') + ' al ' + planilla.fechaFin.format('dd-MM-yyyy')), times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph("Plazo:", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(g.formatNumber(number: contrato?.plazo, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(" ", times8normal), prmsHeaderHoja)

        addCellTabla(headerRubroTabla, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(" ", times8normal), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(" ", times8bold), prmsHeaderHoja)
        addCellTabla(headerRubroTabla, new Paragraph(" ", times8normal), prmsHeaderHoja)


        PdfPTable anticipoTabla = new PdfPTable(2);
        anticipoTabla.setWidthPercentage(100);
        anticipoTabla.setWidths(arregloEnteros([50, 50]))

        if (planilla?.tipoPlanilla?.codigo == 'A') {

            addCellTabla(anticipoTabla, new Paragraph(contrato?.porcentajeAnticipo + " % de anticipo:", times8bold), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: planilla?.valor, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph("(+) Reajuste provisional del anticipo", times8bold), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: planilla?.reajuste, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph("SUMA:", times8bold), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: suma, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph("A FAVOR DEL CONTRATISTA:", times8bold), prmsHeaderHoja)
            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: suma, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)


        } else {

            addCellTabla(anticipoTabla, new Paragraph("Valor Planilla", times8bold), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: planilla?.valor, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph("(+) Reajuste provisional del anticipo", times8bold), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: planilla?.reajuste, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph("SUMA:", times8bold), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: suma, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)

            addCellTabla(anticipoTabla, new Paragraph("A FAVOR DEL CONTRATISTA:", times8bold), prmsHeaderHoja)
            addCellTabla(anticipoTabla, new Paragraph(g.formatNumber(number: suma, minFractionDigits: 2, maxFractionDigits: 2, format: "##,##0", locale: "ec"), fonts.times8normal), prmsHeaderHoja)


        }


        document.add(headerRubroTabla);
        document.add(anticipoTabla)

        document.close();
        pdfw.close()
        byte[] b = baos.toByteArray();
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=" + name)
        response.setContentLength(b.length)
        response.getOutputStream().write(b)


    }


    def anticipoReporte() {

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
        def diasPlanilla = 0

        if (planilla.tipoPlanilla.codigo != "A") {
            diasPlanilla = planilla.fechaFin - planilla.fechaInicio
        }
        def valorPlanilla = planilla.valor

        def acumuladoCrono = 0, acumuladoPlan = 0

        def diasAll = 0

        periodos.eachWithIndex { per, i ->
            if (i > 0) {
                def planillaActual = Planilla.findByPeriodoIndicesAndContrato(per, contrato)
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

        def detalle = VolumenesObra.findAllByObra(obra, [sort: "orden"])
        def precios = [:]
        def indirecto = obra.totales / 100

        preciosService.ac_rbroObra(obra.id)

        detalle.each {
            def res = preciosService.presioUnitarioVolumenObra("sum(parcial)+sum(parcial_t) precio ", obra.id, it.item.id)
            precios.put(it.id.toString(), (res["precio"][0] + res["precio"][0] * indirecto).toDouble().round(2))
        }

        def planillasAnteriores = Planilla.withCriteria {
            eq("contrato", contrato)
            lt("fechaFin", planilla.fechaInicio)
        }

        return [tbodyFr: tbodyFr, tbodyP0: tbodyP0, tbodyB0: tbodyB0, planilla: planilla, obra: obra, oferta: oferta, contrato: contrato, pcs: pcs, data: data, periodos: periodos, detalle: detalle, planillasAnteriores: planillasAnteriores, precios: precios]

    }


    def aseguradoras() {
        def asg = janus.pac.Aseguradora.list()
        [asg: asg]
    }


}
