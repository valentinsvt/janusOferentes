package janus

class ObraFPController {
    def dbConnectionService
    def rg_cmpo = []
    def numeroCampos = 0
    def id_equipo = 0
    def id_repuestos = 0
    def id_combustible = 0
    def id_saldo = 0
    def id_mecanico = 0
    def id_herramientas = 0
    def id_manoDeObra = 0


    def index() { }

    def pone_ids() {
        def cn = dbConnectionService.getConnection()
        def er = 0
        def cont = false
        cn.eachRow("select item__id from item where itemcdgo = 'EQPO'") { row ->
            id_equipo = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item EQUIPO: 'EQPO'"

        cn.eachRow("select item__id from item where itemcdgo = 'REP'") { row ->
            id_repuestos = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item REPUESTOS: 'REP'"

        cn.eachRow("select item__id from item where itemcdgo = 'COMB'") { row ->
            id_combustible = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item COMBUSTIBLE: 'COMB'"

        cn.eachRow("select item__id from item where itemcdgo = 'SLDO'") { row ->
            id_saldo = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item SALDO: 'SLDO'"

        cn.eachRow("select item__id from item where itemcdgo = '009.001'") { row ->
            id_mecanico = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item MECANICO: '009.001'"

        cn.eachRow("select item__id from item where itemcdgo = '103.001.009'") { row ->
            id_herramientas = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item HERRAMIENTAS: '103.001.009'"

        cn.eachRow("select item__id from item where itemcdgo = 'MO'") { row ->
            id_manoDeObra = row.item__id
            cont = true
        }
        if (!cont) er += "No se ha registrado el item MANO DE OBRA: 'MO'"

        cn.close()
        return er
    }


    def validaciones(){

        def obra__id = params.obra.toInteger()
        def sbpr = 0         /* no hay subpresupuestos */
        def res

        res = pone_ids()
/*
        println "equipo: ${id_equipo}, repuestos: ${id_repuestos}, combustible: ${id_combustible}, " +
                "saldo: ${id_saldo}, mecanico: ${id_mecanico}, herramientas: ${id_herramientas} " +
                "mano de obra: ${id_manoDeObra}"
*/

        if (res) {
            render "Error: " + res
            return
        }
        res = ejecutaSQL("select * from ac_rbro_hr_of(${obra__id})")
        if (!res){
            render "Error: no se pudo ejecutar ac_rbro_hr_of"
            return
        }
        res = ejecutaSQL("select * from sp_obra_of(${obra__id}, ${sbpr})")
        if (!res){
            render "Error: no se pudo ejecutar sp_obra_of"
            return
        }

        res = verificaMatriz(obra__id)
        if (res!=""){
            render res
            return
        }
        println "Validaciones .. pasa verificaMatriz"
        res = verifica_precios(obra__id)
        if (res.size()>0){
            def msg ="<span style='color:red'>Errores detectados</span><br> <span class='label-azul'>No se encontraron precios para los siguientes items:</span><br>"
            msg+= res.collect{ "<b>ITEM</b>: $it.key ${it.value.join(", <b>Lista</b>: ")}" }.join('<br>')
            render msg
            return
        }
//        println "Validaciones .. pasa verificaMatriz y verifica_precios "

        redirect(action: "matrizFP",params: ["obra":params.obra,"sub":params.sub,"trans": false])
        return
    }

    def matrizFP() {
        def res = ""
        println "matriz fp "+params
        /* --------------------- parámetros que se requieren para correr el proceso  --------------------- */
        def obra__id = params.obra.toInteger()         // obra de pruebas dos rubros: 550, varios 921. Pruebas 886
        def sbpr = 0   // todos los subpresupuestos
        def conTransporte = false   // parámetro leido de la interfaz
        def obra = Obra.get(obra__id)
        if (conTransporte)
            obra.desgloseTransporte = "S"
        else
            obra.desgloseTransporte = "N"
        obra.save(flush: true)

        pone_ids()
/*
        if(params.trans == "checked")
            conTransporte=true
*/
        /* ----------------------------------- FIN de parámetros  ---------------------------------------- */

        //def obra = Obra.get(obra__id)

        //ejecutaSQL("select * from ac_rbro_hr(${obra__id})")
        ejecutaSQL("select * from ac_rbro_hr_of(${obra__id})")
        println "ejecutó ac_rbro_hr"

        /* solo se debe correr sp_obra cuando esta no está registrada */
        //if (Obra.get(obra__id).estado == "N") ejecutaSQL("select * from sp_obra(${obra__id}, ${sbpr})")

        //ejecutaSQL("select * from sp_obra(${obra__id}, ${sbpr})")
        if (Obra.get(obra__id).estado == "N") {
            ejecutaSQL("select * from sp_obra_of(${obra__id}, ${sbpr})")
//            println "ejecutó sp_obra"
        }
//
//        println "verificaMatriz" + verificaMatriz(obra__id)
//        println "pasa verificaMatriz"
//        println "verifica_precios \n" + verifica_precios(obra__id)

        /* --------------------------------------- procesaMatriz --------------------------------
        * la pregunta de uno o todos los subpresupuestos se debe manejar en la interfaz         *
        * 1. Eliminar las tablas obxx_user si existen y crear nuevas                            *
        * 2. Se descomponen los items de la obra y se los inserta en vlobitem: sp_obra          *


        * ------------------------------------------------------------------------------------- */
        /* 1. Eliminar las tablas obxx_user si existen y crear nuevas                           */
        println "inicia creaTablas"
        creaTablas(obra__id, "S")  /* cambio obra__id */
        numeroCampos = 0

        /* 2. Se descomponen los items de la obra y se los inserta en vlobitem: sp_obra         */

        /* -------------------------------------------------------------------------------------
        * Verifica si existe Transporte y/o Equipos'                                          */

        println "inicia calculaTransporte"
        def transporte = calculaTransporte(obra__id)
        if(!transporte)
            transporte=0.0
        def equipos = calculaEquipos(obra__id)
        if (conTransporte)
            transporte += equipos
        else
            transporte = equipos
        def hayEquipos = (transporte > 0)

//        println "fin calculaTransporte"

        /*---- Fin de la consideración del DESGLOSE de transporte --------- */

        /* ------------------------------------------------------------------------------------- */
        /* Desglose de la Mano de Obra                                                           */

        creaCampo(obra__id, 'ORDEN', 'R')          /* cambio obra__id */
        creaCampo(obra__id, 'CODIGO', 'R')
        creaCampo(obra__id, 'RUBRO', 'R')
        creaCampo(obra__id, 'UNIDAD', 'R')
        creaCampo(obra__id, 'CANTIDAD', 'R')

        /* campos de Mano de Obra que figuran en la obra --------------------------------------- */

        manoDeObra(obra__id, sbpr, hayEquipos)
        materiales(obra__id, sbpr, hayEquipos)   // crea columnas de materiales
        if (hayEquipos) {
            creaCampo(obra__id, id_equipo + "_U", "D")
            creaCampo(obra__id, id_equipo + "_T", "D")
            creaCampo(obra__id, 'TRANSPORTE_U', 'D')
            creaCampo(obra__id, 'TRANSPORTE_T', 'D')
            creaCampo(obra__id, id_repuestos + "_U", "D")
            creaCampo(obra__id, id_repuestos + "_T", "D")
            creaCampo(obra__id, id_combustible + "_U", "D")
            creaCampo(obra__id, id_combustible + "_T", "D")
        }
        creaCampo(obra__id, 'TOTAL_U', 'T');
        creaCampo(obra__id, 'TOTAL_T', 'T');
        /* ---- Inserta los rubros y títulos de totales --------------------------------------- */
        insertaRubro("obra__id, codigo, rubro, orden", "${obra__id},'sS1', 'SUMAN', 10000")
        insertaRubro("obra__id, codigo, rubro, orden", "${obra__id},'sS2', 'TOTALES', 10001")
        insertaRubro("obra__id, codigo, rubro, orden", "${obra__id},'sS3', 'COEFICIENTES DE LA FORMULA', 10002")
        insertaRubro("obra__id, codigo, rubro, orden", "${obra__id},'sS4', 'TARIFA HORARIA', 10003")
        insertaRubro("obra__id, codigo, rubro, orden", "${obra__id},'sS6', 'HORAS HOMBRE POR COMPONENTE', 10004")
        insertaRubro("obra__id, codigo, rubro, orden", "${obra__id},'sS5', 'COEFICIENTES DE LA CUADRILLA TIPO',10005")

        /* ---- ejecuta Rubros(subPrsp) y Descomposicion(subPrsp) ----------------------------- */
        rubros(obra__id, sbpr)
        println "completa rubros"
        descomposicion(obra__id, sbpr)
        println "completa descomposicion"
        des_Materiales(obra__id, sbpr, conTransporte)
        println "completa des_Materiales"
        if (hayEquipos) {
            if (conTransporte) acTransporte(obra__id, sbpr)
            acEquipos(obra__id, sbpr)
        }
        println "completa hayEquipos"

        acManoDeObra(obra__id)                      /* cambio obra__id */
        println "completa acManoDeObra"
        acTotal(obra__id)                           /* cambio obra__id */
        println "completa acTotal"

        if (hayEquipos) desgloseTrnp(obra__id)      /* no se usa fracción por chofer, así queda un sólo parámetro */
        println "completa desgloseTrnp"

        completaTotalS2(obra__id, hayEquipos)
        println "completa completaTotalS2"
        acTotalS2(obra__id)                         /* cambio obra__id */
        println "completa acTotalS2"


        tarifaHoraria(obra__id)
        println "completa tarifaHoraria"
        res = cuadrillaTipo(obra__id)            /* cambio obra__id */
        if (!res){
            println "res"
        }

        println "completa cuadrillaTipo"

        formulaPolinomica(obra__id)                 /* cambio obra__id */
        println "fin matriz"
        render "ok"
        return

    }

    def verificaMatriz(id) {
        def obra = Obra.get(id)
        def errr = ""
        if (!VolumenesObra.findAllByObra(obra)) errr += "<br><span class='label-azul'>No se ha ingresado los volúmenes de Obra</span>"
/*
        if (!obra.lugar) errr += "<br><span class='label-azul'>No se ha definido la Lista precios:</span> \"Peso Capital de cantón\" para esta Obra"
        if (!obra.listaPeso1) errr += "<br><span class='label-azul'>No se ha definido la Lista precios:</span> \"Peso Especial\" para esta Obra"
        if (!obra.listaVolumen0) errr += "<br><span class='label-azul'>No se ha definido la Lista precios: </span>\"Materiales Pétreos Hormigones\" para esta Obra"
        if (!obra.listaVolumen1) errr += "<br><span class='label-azul'>No se ha definido la Lista precios: </span>\"Materiales Mejoramiento\" para esta Obra"
        if (!obra.listaVolumen2) errr += "<br><span class='label-azul'>No se ha definido la Lista precios:</span> \"Materiales Carpeta Asfáltica\" para esta Obra"
        if (!obra.listaManoObra) errr += "<br><span class='label-azul'>No se ha definido la Lista precios:</span> \"Mano de obra y equipos\" para esta Obra"

        if (!obra.distanciaPeso) errr += "<br> <span class='label-azul'> No se han ingresado las distancias al Peso</span>"
        if (!obra.distanciaVolumen) errr += "<br>  <span class='label-azul'>No se han ingresado las distancias al Volumen</span>"
*/
        if (rubrosSinCantidad(id) > 0) errr += "<br> <span class='label-azul'>Existen Rubros con cantidades Negativas o CERO</span>"

//        if (nombresCortos()) errr += "<br><span class='label-azul'>Existen Items con nombres cortos repetidos: </span>" + nombresCortos()

        if (valorMecanico(id) == 0) errr += "No hay precio unitario para Mecánico</br>"

        if (errr) errr = "<b><span style='color:red'>Errores detectados</span></b> " + errr
        else errr = ""
        return errr
    }

    def rubrosSinCantidad(id) {
        def cn = dbConnectionService.getConnection()
        def er = 0;
        def tx_sql = "select count(*) nada from vlob where obra__id = ${id} and vlobcntd <= 0"
        cn.eachRow(tx_sql.toString()) {row ->
            er = row.nada
        }
        cn.close()
        return er
    }

    def valorMecanico(id) {
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def er = 0;
        def tx_sql = "select count(*) nada from prco, item where item.item__id = prco.item__id and " +
                "itemcdgo = '009.001' and prco.prsn__id = ${obra.oferente.id} and prcoprco > 0"
        cn.eachRow(tx_sql.toString()) {row ->
            er = row.nada
        }
        cn.close()
//        println "mecanico: $er"
        return er
    }

    def nombresCortos() {
        // sería mejor limitarse a sólo los items de la obra
        def cn = dbConnectionService.getConnection()
        def errr = "";
        def tx_sql = "select count(*), itemcmpo from item where tpit__id = 1 group by itemcmpo having count(*) > 1"
        cn.eachRow(tx_sql) {row ->
            errr += ":" + row.itemcmpo
        }
/*
        cn.eachRow("select item__id, itemcmpo from item".toString()) {row ->
            (row.itemcmpo =~ /\W+/).findAll { p ->
                errr += "item: " + row.item__id + " tiene: /${p}/"
            }
        }
*/
        cn.close()
        return errr
    }

    def verifica_precios(id) {
        // usa funcion
        def cn = dbConnectionService.getConnection()
        def errr = [:];
        def tx_sql = "select itemcdgo, itemnmbr, tplsdscr from verifica_precios_of(${id}) order by itemcdgo "
        cn.eachRow(tx_sql.toString()) {row ->
            errr.put(row["itemcdgo"]?.trim(),[row["itemnmbr"]?.trim(),row["tplsdscr"]?.trim()])
//            errr += "Item: ${row.itemcdgo.trim()} ${row.itemnmbr.trim()} Lista: ${row.tplsdscr.trim()}\n"
//            println "r "+row
        }
        cn.close()
        return errr
    }

    def creaTablas(id, reprocesa) {
        // en lugar de crear las tablas solo se borran los datos si se quiere reprocesar.
        if (reprocesa == 'S') {
            def tx = ""
            def cn = dbConnectionService.getConnection()
            def errr = "";
/*
            cn.execute("drop table if exists mfcl, mfvl, mfrb".toString())

            tx = " create table mfcl (clmncdgo smallint not null, clmndscr varchar(60), clmntipo char(1),"
            tx += "clmnextn char(1), clmnitem varchar(20), clmngrpo char(1),"
            tx += "constraint pk_mfcl primary key (clmncdgo))"
            cn.execute(tx.toString())

            tx = "create table mfvl (clmncdgo smallint not null, codigo varchar(20) not null, "
            tx += "valor numeric(15,3), constraint pk_mfvl primary key (clmncdgo, codigo))"
            cn.execute(tx.toString())

            tx = "create table mfrb (codigo varchar(20) not null, rubro varchar(60), unidad varchar(5),"
            tx += "cantidad numeric(15,3), orden smallint, constraint pk_mfrb primary key (codigo))"
            cn.execute(tx.toString())
*/
            cn.execute("delete from mfvl where obra__id = ${id}".toString())
            cn.execute("delete from mfrb where obra__id = ${id}".toString())
            cn.execute("delete from mfcl where obra__id = ${id}".toString())
            cn.close()
        }
        return "<br>Tablas borradas.. reproceso <br>"
    }

    def ejecutaSQL(txSql) {
        def res
        def cn = dbConnectionService.getConnection()
        //println txSql
        res = cn.execute(txSql.toString())
        cn.close()
        return res
    }

    def calculaTransporte(id) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = "select sum(trnp) transporte from rbro_pcun_of (${id})"
        def trnp = 0.0
        cn.eachRow(tx_sql.toString()) {row ->
            trnp = row.transporte
        }
        cn.close()
        return trnp
    }

    def calculaEquipos(id) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = "select sum(voitcntd) equipos from vlobitem, item, dprt, sbgr "
        tx_sql += "where item.item__id = vlobitem.item__id and obra__id = ${id} and "
        tx_sql += "dprt.dprt__id = item.dprt__id and sbgr.sbgr__id = dprt.sbgr__id and grpo__id = 3"
        def eqpo = 0.0
//        println "calculaEquipos: " + tx_sql
        cn.eachRow(tx_sql) {row ->
            eqpo = row.equipos
        }
        cn.close()
        return eqpo
    }

    def creaCampo(id, campo, tipo) {
        numeroCampos++
        rg_cmpo.add(campo)
        ejecutaSQL("insert into mfcl values (${numeroCampos}, '${id}', '${campo}', '${tipo}', null, null, null)")
    }

    def manoDeObra(id, sbpr, hayEq) { //sólo una fórmula por todos los sbpr
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def tx_wh = ""
        if (sbpr != 0) tx_wh = "sbpr__id = ${sbpr} and "
        def tx_sql = "select item.item__id, grpo__id from vlobitem, item, dprt, sbgr "
        //tx_sql += "where item.item__id = vlobitem.item__id and obra__id = ${id} and sbpr__id = ${sbpr} and "
        tx_sql += "where item.item__id = vlobitem.item__id and obra__id = ${id} and ${tx_wh}"
        tx_sql += "dprt.dprt__id = item.dprt__id and sbgr.sbgr__id = dprt.sbgr__id and grpo__id = 2 "
        tx_sql += "group by item.item__id, grpo__id order by item.itemcdgo"
//        println "manoDeObra: $tx_sql"
        cn.eachRow(tx_sql.toString()) {row ->
            creaCampo(id, row.item__id + "_U", "O")
            creaCampo(id, row.item__id + "_T", "O")
        }
        if (hayEq) {
            //println rg_cmpo
            if (!rg_cmpo.contains(id_mecanico + "_U")) {
                //println "crea mecanico"
                creaCampo(id, "${id_mecanico}_U", "O")
                creaCampo(id, "${id_mecanico}_T", "O")
            }
            if (!rg_cmpo.contains(obra.chofer.id + "_U")) {
                creaCampo(id, "${obra.chofer.id}_U", "O")
                creaCampo(id, "${obra.chofer.id}_T", "O")
            }

        }
        creaCampo(id, "${id_manoDeObra}_U", "T")
        creaCampo(id, "${id_manoDeObra}_T", "T")
        cn.close()
    }

    def materiales(id, sbpr, hayEq) {
        def cn = dbConnectionService.getConnection()
        def tx_wh = ""
        if (sbpr != 0) tx_wh = "sbpr__id = ${sbpr} and "

        def tx_sql = "select item.item__id, grpo__id from vlobitem, item, dprt, sbgr "
        //tx_sql += "where item.item__id = vlobitem.item__id and obra__id = ${id} and sbpr__id = ${sbpr} and "
        tx_sql += "where item.item__id = vlobitem.item__id and obra__id = ${id} and ${tx_wh}"
        tx_sql += "dprt.dprt__id = item.dprt__id and sbgr.sbgr__id = dprt.sbgr__id and grpo__id = 1 "
        tx_sql += "group by item.item__id, grpo__id order by item.itemcdgo"  //gdo --
//        println "materiales: " + tx_sql
        cn.eachRow(tx_sql.toString()) {row ->
            creaCampo(id, row.item__id + "_U", "M")
            creaCampo(id, row.item__id + "_T", "M")
        }
        creaCampo(id, id_saldo + "_U", "M")
        creaCampo(id, id_saldo + "_T", "M")
        cn.close()

        if (hayEq) {
            if (!rg_cmpo.contains(id_herramientas + "_U")) {
                creaCampo(id, "${id_herramientas}_U", "M")
                creaCampo(id, "${id_herramientas}_T", "M")
            }
        }

    }

    def insertaRubro(campos, valores) {
        //println "insertaRubro: insert into mfrb (${campos}) values (${valores})"
        ejecutaSQL("insert into mfrb (${campos}) values (${valores})")
    }

    def rubros(id, sbpr) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = ""
        if (sbpr == 0) {
            tx_sql = "select itemcdgo, sum(vlobcntd) vlobcntd, itemnmbr, unddcdgo, min(vlobordn) ordn "
            tx_sql += "from vlob, item, undd "
            tx_sql += "where item.item__id = vlob.item__id and obra__id = ${id} and "
            tx_sql += "vlobcntd > 0 and undd.undd__id = item.undd__id "
            tx_sql += "group by itemcdgo, itemnmbr, unddcdgo order by ordn"
        } else {
            tx_sql = "select itemcdgo, sum(vlobcntd) vlobcntd, itemnmbr, unddcdgo, min(vlobordn) ordn "
            tx_sql += "from vlob, item, undd "
            tx_sql += "where item.item__id = vlob.item__id and obra__id = ${id} and "
            tx_sql += "vlobcntd > 0 and undd.undd__id = item.undd__id and sbpr__id = ${sbpr} "
            tx_sql += "group by itemcdgo, itemnmbr, unddcdgo order by ordn"
        }
        //println "rubros: " + tx_sql
        def contador = 1
        cn.eachRow(tx_sql.toString()) {row ->
            def nmbr = row.itemnmbr.replaceAll(/'/,"\'\'")        //maneja nombres con comillas simples
            def tx = "${id},'${row.itemcdgo}', '${nmbr.size() > 60 ? nmbr[0..59] : nmbr}'," +
                    "'${row.unddcdgo}', ${row.vlobcntd}, ${contador}"
            insertaRubro("obra__id, codigo, rubro, unidad, cantidad, orden", tx)
            ejecutaSQL("insert into mfvl (obra__id, clmncdgo, codigo, valor) values(${id},1," +
                    "'${row.itemcdgo}', ${contador} )")
            ejecutaSQL("insert into mfvl (obra__id, clmncdgo, codigo) values(${id},2, '${row.itemcdgo}')")
            contador++
        }
        tx_sql = "select distinct clmncdgo from mfcl where obra__id = ${id} and clmndscr like '%_T' or clmndscr like '%_U'"
        //println "2rubros: " + tx_sql
        cn.eachRow(tx_sql.toString()) {d ->
            //println "insert into mfvl (obra__id, clmncdgo, codigo, valor) select " +
            "obra__id, ${d.clmncdgo}, codigo, 0 from mfrb where obra__id = ${id}"
            ejecutaSQL("insert into mfvl (obra__id, clmncdgo, codigo, valor) select " +
                    "obra__id, ${d.clmncdgo}, codigo, 0 from mfrb where obra__id = ${id}")
        }
        cn.close()
    }

    def descomposicion(id, sbpr) {
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_sql = ""
        def tx_cr = ""
        if (sbpr == 0) {
            tx_sql = "select item.item__id, itemcdgo, sum(vlobcntd) vlobcntd, itemnmbr, unddcdgo "
            tx_sql += "from vlob, item, undd "
            tx_sql += "where item.item__id = vlob.item__id and obra__id = ${id} and "
            tx_sql += "vlobcntd > 0 and undd.undd__id = item.undd__id "
            tx_sql += "group by item.item__id, itemcdgo, itemnmbr, unddcdgo"
        } else {
            tx_sql = "select item.item__id, itemcdgo, vlobcntd, itemnmbr, unddcdgo "
            tx_sql += "from vlob, item, undd "
            tx_sql += "where item.item__id = vlob.item__id and obra__id = ${id} and "
            tx_sql += "vlobcntd > 0 and undd.undd__id = item.undd__id and sbpr__id = ${sbpr} "
        }
//        println "descomposicion: " + tx_sql
        def contador = 1
        cn.eachRow(tx_sql.toString()) {row ->
            /****
             * ya no hace falata invocar por separado a rb_precios_r,vlob_pcun_of maneja registrado o no
             */
            tx_cr = "select item__id, itemcdgo, parcial pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 2"  //v2
            //println "descomposicion: tx_cr: " + tx_cr
            cn1.eachRow(tx_cr.toString()) {cr ->
                poneValores(id, cr.item__id, cr.pcun, cr.pcun * row.vlobcntd, row.vlobcntd, row.itemcdgo)
            }
        }
        cn.close()
        cn1.close()
    }

    def columnaCdgo(id, cmpo) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = "select clmncdgo from mfcl where obra__id = ${id} and clmndscr = '${cmpo}'"
        def posicion = 0
        cn.eachRow(tx_sql.toString()) {row ->
            posicion = row.clmncdgo
        }
        cn.close()
        return posicion
    }

    def rubroCantidad(id, cdgo) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = "select cantidad from mfrb where obra__id = ${id} and codigo = '${cdgo}'"
        //println "...rubroCantidad:" + tx_sql
        def cntd = 0.0
        cn.eachRow(tx_sql.toString()) {row ->
            cntd = row.cantidad
        }
        cn.close()
        return cntd
    }

    def poneValores(id, cmpo, pcun, incr, cntd, rbro) {
        def clmn = columnaCdgo(id, cmpo + "_U")
//        println  "poneValores: update mfvl set valor = ${pcun} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = '${rbro}'"
        ejecutaSQL("update mfvl set valor = ${pcun} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = '${rbro}'")
        clmn = columnaCdgo(id, cmpo + "_T")
        ejecutaSQL("update mfvl set valor = ${incr} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = '${rbro}'")
        ejecutaSQL("update mfvl set valor = valor + ${pcun * cntd} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS1'")

        clmn = columnaCdgo(id, "TOTAL_T")
        //println "aumenta a TOTAL_T:  campo: $cmpo columna: $clmn incr:" +  incr
        //println "update mfvl set valor = valor + ${incr} where clmncdgo = ${clmn} and codigo = '${rbro}'"
        ejecutaSQL("update mfvl set valor = valor + ${incr} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = '${rbro}'")
    }

    def des_Materiales(id, sbpr, conTrnp) {
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_cr = ""
        def tx_sql = ""
/*
        def tx_sql = "select rdvl, rdps, dsps, dsvl from transporte(${id})"
        def rdvl = 0.0
        def rdps = 0.0
        def dsvl = 0.0
        def dsps = 0.0
        cn.eachRow(tx_sql.toString()) {row ->
            rdvl = row.rdvl
            rdps = row.rdps
            dsvl = row.dsvl
            dsps = row.dsps
        }
*/
        //println "dsps: $dsps, dsvl: $dsvl, rdps: $rdps, rdvl: $rdvl"
        if (sbpr == 0) {
            tx_sql = "select item.item__id, itemcdgo, sum(vlobcntd) vlobcntd, itemnmbr, unddcdgo "
            tx_sql += "from vlob, item, undd "
            tx_sql += "where item.item__id = vlob.item__id and obra__id = ${id} and "
            tx_sql += "vlobcntd > 0 and undd.undd__id = item.undd__id "
            tx_sql += "group by item.item__id, itemcdgo, itemnmbr, unddcdgo"
        } else {
            tx_sql = "select item.item__id, itemcdgo, vlobcntd, itemnmbr, unddcdgo "
            tx_sql += "from vlob, item, undd "
            tx_sql += "where item.item__id = vlob.item__id and obra__id = ${id} and "
            tx_sql += "vlobcntd > 0 and undd.undd__id = item.undd__id and sbpr__id = ${sbpr} "
        }
        println "des_Materiales: " + tx_sql

        cn.eachRow(tx_sql.toString()) {row ->
            if (conTrnp) {
                tx_cr = "select item__id, itemcdgo, parcial pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 1"  //v2
//                if (obra.estado == 'N') {
//                    tx_cr = "select item__id, itemcdgo, parcial pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 1"  //v2
//                } else {
//                    tx_cr = "select item__id, itemcdgo, parcial pcun from rb_precios_r(${id}, ${row.item__id}) where grpocdgo = 1"
//                }
            } else {
                tx_cr = "select item__id, itemcdgo, parcial + parcial_t pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 1"
//                if (obra.estado == 'N') {
//                    tx_cr = "select item__id, itemcdgo, parcial + parcial_t pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 1"
//                } else {
//                    tx_cr = "select item__id, itemcdgo, parcial + parcial_t pcun from rb_precios_r(${id}, ${row.item__id}) where grpocdgo = 1"
//                }
            }
            //println "des_Materiales: " + tx_cr
            cn1.eachRow(tx_cr.toString()) {cr ->
                if(row.itemcdgo == 'C-001-001') {
                    println "des_Materiales: id: $id, cmpo: ${cr.item__id}, pcun: ${cr.pcun}, " +
                            "incr: ${cr.pcun * row.vlobcntd}, cntd: ${row.vlobcntd}, rbro: ${row.itemcdgo}"
                }
                poneValores(id, cr.item__id, cr.pcun, cr.pcun * row.vlobcntd, row.vlobcntd, row.itemcdgo)
            }
        }
        cn.close()
        cn1.close()
    }

    def acTransporte(id, sbpr) {  /* la existencia de transporte se mane al llamar la función */
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def tx_sql = ""
        def clmn = ""
        def cntd = 0.0
        if (sbpr == 0) {
            if (obra.estado == 'N') {
                tx_sql = "select rbrocdgo, trnp from rbro_pcun_of(${id})"
            } else {
/*
                tx_sql  = "select itemcdgo rbrocdgo, coalesce(sum(rbrocntd * itemdstn * itemtrfa * obit.itempeso),0) trnp "
                tx_sql += "from obrb, obit, item "
                tx_sql += "where item.item__id = obrb.rbrocdgo and obrb.obra__id = ${id} and obit.obra__id = obrb.obra__id and "
                tx_sql += "obit.item__id = obrb.item__id group by item.item__id, itemcdgo"
*/
                tx_sql = "select distinct itemcdgo rbrocdgo, rbrotrnp trnp from obrb, item "
                tx_sql += "where obra__id = ${id} and item.item__id = obrb.rbrocdgo"
            }
        } else {
            if (obra.estado == 'N') {
                tx_sql = "select rbrocdgo, trnp from rbro_pcun_of(${id}) where sbpr__id = ${sbpr}"
            } else {    /* TODO --- seguir con proceso de genera matrizFP */
                tx_sql = "select distinct itemcdgo rbrocdgo, rbrotrnp trnp from obrb, item, vlob "
                tx_sql += "where obrb.obra__id = ${id} and item.item__id = obrb.rbrocdgo and "
                tx_sql += "vlob.item__id = obrb.rbrocdgo and sbpr__id = ${sbpr}"
            }
        }

        cn.eachRow(tx_sql.toString()) {row ->
            clmn = columnaCdgo(id, 'TRANSPORTE_T')
            cntd = rubroCantidad(id, row.rbrocdgo)
            ejecutaSQL("update mfvl set valor = ${row.trnp * cntd} where obra__id = ${id} and codigo = '${row.rbrocdgo}' and " +
                    "clmncdgo = ${clmn}")
            clmn = columnaCdgo(id, 'TRANSPORTE_U');
            ejecutaSQL("update mfvl set valor = ${row.trnp} where obra__id = ${id} and codigo = '${row.rbrocdgo}' and " +
                    "clmncdgo = ${clmn}")
            clmn = columnaCdgo(id, 'TOTAL_T');
            ejecutaSQL("update mfvl set valor = valor + ${row.trnp * cntd} where obra__id = ${id} and codigo = '${row.rbrocdgo}' and " +
                    "clmncdgo = ${clmn}")
        }
        cn.close()
        actualizaS1(id, "TRANSPORTE_T")
    }

    def actualizaS1(id, columna) {
        def cn = dbConnectionService.getConnection()
        def clmn = columnaCdgo(id, columna)
        def tx_sql = "select sum(valor) suma from mfvl where obra__id = ${id} and clmncdgo = '${clmn}' and codigo not like 'sS%'"
        def totl = 0.0
        cn.eachRow(tx_sql.toString()) {row ->
            totl = row.suma
        }
        //println "valor de total: $totl"
        //if (!totl) totl = 0.0
        ejecutaSQL("update mfvl set valor = ${totl} where obra__id = ${id} and clmncdgo = '${clmn}' and codigo = 'sS1'")
        cn.close()
    }

    def acEquipos(id, sbpr) {
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_sql = ""
        def tx_cr = ""
        def clmn = ""
        def cntd = 0.0
        def suma = 0
        if (sbpr == 0) {
            tx_sql = "select item.item__id, itemcdgo from vlob, item where obra__id = ${id} and vlobcntd > 0 and "
            tx_sql += "item.item__id = vlob.item__id"
        } else {
            tx_sql = "select item.item__id, itemcdgo from vlob, item where obra__id = ${id} and sbpr__id = ${sbpr} and "
            tx_sql += "vlobcntd > 0 and item.item__id = vlob.item__id"
        }
        println "acEquipos: " + tx_sql
        cn.eachRow(tx_sql.toString()) {row ->
            suma = 0
            tx_cr = "select sum(parcial) pcun from vlob_pcun_of (${id},${row.item__id}) where grpocdgo = 3 and cmbs = 'S'"  //v2
            cntd = rubroCantidad(id, row.itemcdgo)

//            if (obra.estado == 'N') {
//                tx_cr = "select sum(parcial) pcun from vlob_pcun_of (${id},${row.item__id}) where grpocdgo = 3 and cmbs = 'S'"  //v2
//            } else {
//                tx_cr = "select sum(parcial) pcun from rb_precios_r (${id}, ${row.item__id}) where grpocdgo = 3 and cmbs = 'S'"
//            }
            println "...-> $tx_cr"
            cn1.eachRow(tx_cr.toString()) {d ->
                if (d.pcun > 0) {
                    clmn = columnaCdgo(id, "${id_equipo}_T")
                    ejecutaSQL("update mfvl set valor = ${d.pcun * cntd} where obra__id = ${id} and codigo = '${row.itemcdgo}' " +
                            " and clmncdgo = ${clmn}")
                    clmn = columnaCdgo(id, "${id_equipo}_U")
                    ejecutaSQL("update mfvl set valor = ${d.pcun} where obra__id = ${id} and codigo = '${row.itemcdgo}' " +
                            " and clmncdgo = ${clmn}")
                    suma += d.pcun * cntd
                }
            }
            tx_cr = "select sum(parcial) pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 3 and cmbs = 'N'"   //v2
//            if (obra.estado == 'N') {
//                tx_cr = "select sum(parcial) pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 3 and cmbs = 'N'"   //v2
//            } else {
//                tx_cr = "select sum(parcial) pcun from rb_precios_r (${id}, ${row.item__id}) where grpocdgo = 3 and cmbs = 'N'"
//            }
            cn1.eachRow(tx_cr.toString()) {d ->
                if (d.pcun > 0) {
//                    clmn = columnaCdgo(id, "SALDO_T")
                    clmn = columnaCdgo(id, id_herramientas + "_T")
//                    cntd = rubroCantidad(id, row.itemcdgo)
                    ejecutaSQL("update mfvl set valor = ${d.pcun * cntd} where obra__id = ${id} and codigo = '${row.itemcdgo}' " +
                            " and clmncdgo = ${clmn}")

//                    clmn = columnaCdgo(id, "SALDO_U")
                    clmn = columnaCdgo(id, id_herramientas + "_U")
                    ejecutaSQL("update mfvl set valor = ${d.pcun} where obra__id = ${id} and codigo = '${row.itemcdgo}' " +
                            " and clmncdgo = ${clmn}")
                    suma += d.pcun * cntd
                }
            }

            tx_cr = "select sum(parcial) pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 3"   //v2
//            if (obra.estado == 'N') {
//                tx_cr = "select sum(parcial) pcun from vlob_pcun_of (${id}, ${row.item__id}) where grpocdgo = 3"   //v2
//            } else {
//                tx_cr = "select sum(parcial) pcun from rb_precios_r (${id}, ${row.item__id}) where grpocdgo = 3"
//            }
            cn1.eachRow(tx_cr.toString()) {d ->
                if (d.pcun > 0) {
                    clmn = columnaCdgo(id, "TOTAL_T")
//                    cntd = rubroCantidad(id, row.itemcdgo)
                    ejecutaSQL("update mfvl set valor = valor + ${d.pcun * cntd} where obra__id = ${id} and codigo = '${row.itemcdgo}' " +
                            " and clmncdgo = ${clmn}")
                    println "actualiza totales con: ${d.pcun * cntd} y debe ser: $suma"
                }
            }
        }
        cn.close()
        cn1.close()
        actualizaS1(id, "${id_equipo}_T")
        actualizaS1(id, "SALDO_T")
//        actualizaS1(id, "${id_saldo}_T")

        actualizaS1(id, "${id_herramientas}_T")
//        actualizaS1(id, "${id_herramientas}_T")

    }

    def acManoDeObra(id) {
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_sql = ""
        def tx_cr  = ""
        def clmn = ""
        def cntd = 0.0
        def valor = 0.0 /* gdo */
        ejecutaSQL("update mfcl set clmntipo = null where obra__id = ${id} and clmndscr like '%U'")
        tx_sql =  "select codigo from mfrb where obra__id = ${id} and codigo not like 'sS%'"
        cn.eachRow(tx_sql.toString()) {row ->
            tx_cr =  "select sum(valor) suma from mfvl v, mfcl c "
            tx_cr += "where c.obra__id = ${id} and c.obra__id = v.obra__id and codigo = '${row.codigo}' and c.clmncdgo = v.clmncdgo and clmntipo = 'O'"
//            println "acManoDeObra: " + tx_cr
            cn1.eachRow(tx_cr.toString()) {d ->
                clmn = columnaCdgo(id, "${id_manoDeObra}_T")
                ejecutaSQL("update mfvl set valor = ${d.suma} where obra__id = ${id} and codigo = '${row.codigo}' and " +
                        "clmncdgo = ${clmn}")
                clmn = columnaCdgo(id, "${id_manoDeObra}_U")
                cntd = rubroCantidad(id, row.codigo)
                /* gdo */
                if (!d.suma) valor = 0
                else valor = d.suma/cntd
//                ejecutaSQL("update mfvl set valor = ${d.suma / cntd} where obra__id = ${id} and codigo = '${row.codigo}' and " +
                ejecutaSQL("update mfvl set valor = ${valor} where obra__id = ${id} and codigo = '${row.codigo}' and " +
                        "clmncdgo = ${clmn}")
            }
        }
        cn.close()
        cn1.close()
        actualizaS1(id, "${id_manoDeObra}_T")
    }

    def acTotal(id) {
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_sql = ""
        def tx_cr  = ""
        def clmn = ""
        def cntd = 0.0
        tx_sql =  "select codigo from mfrb where obra__id = ${id} and codigo not like 'sS%'"
        cn.eachRow(tx_sql.toString()) {row ->
            clmn = columnaCdgo(id, "TOTAL_T")
            cntd = rubroCantidad(id, row.codigo)
            tx_cr =  "select valor from mfvl where obra__id = ${id} and codigo = '${row.codigo}' and clmncdgo = ${clmn}"
            println "acTotal...: " + tx_cr
            cn1.eachRow(tx_cr.toString()) {d ->
                clmn = columnaCdgo(id, "TOTAL_U")
                ejecutaSQL("update mfvl set valor = ${d.valor / cntd} where obra__id = ${id} and codigo = '${row.codigo}' and " +
                        "clmncdgo = ${clmn}")
            }
        }
        cn.close()
        cn1.close()
        actualizaS1(id, "TOTAL_T")
    }

    def totalSx(id, columna, sx) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = ""
        def clmn = columnaCdgo(id, columna)
        def total = 0.0
        tx_sql = "select valor from mfvl where obra__id = ${id} and clmncdgo = ${clmn} and codigo = '${sx}'"
        //println "totalSx: " + tx_sql
        cn.eachRow(tx_sql.toString()) {row ->
            total = row.valor
        }
        cn.close()
        return total
    }

    def actualizaS2(id, columna, valor) {
        def clmn = columnaCdgo(id, columna)
        ejecutaSQL("update mfvl set valor = ${valor} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'")
    }


    def desgloseTrnp(id) {
        def obra = Obra.get(id)
        def transporte = (totalSx(id, 'TRANSPORTE_T', 'sS1') + totalSx(id, "${id_equipo}_T", "sS1"))
        def saldo = totalSx(id, "${id_saldo}_T", "sS1")
        def mecanico = totalSx(id, "${id_mecanico}_T", "sS1")
        def repuestos = totalSx(id, "${id_repuestos}_T", "sS1")
        def combustible = totalSx(id, "${id_combustible}_T", "sS1")

        actualizaS2(id, "${id_equipo}_T", transporte * obra.desgloseEquipo)
        actualizaS2(id, "${id_repuestos}_T", transporte * obra.desgloseRepuestos + repuestos)
        actualizaS2(id, "${id_combustible}_T", transporte * obra.desgloseCombustible + combustible)
        actualizaS2(id, "${id_mecanico}_T", transporte * obra.desgloseMecanico + mecanico)
        actualizaS2(id, "${id_saldo}_T", transporte * obra.desgloseSaldo + saldo)
    }

    def completaTotalS2(id, hayEqpo) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = ""
        def clmn = ""
        ejecutaSQL("update mfvl c1 set valor = (select valor from mfcl c, " +
                " mfvl v where c.clmncdgo = v.clmncdgo and c.obra__id = v.obra__id and c.obra__id = ${id} and v.codigo = 'sS1' and " +
                "clmndscr like '%_T' and v.clmncdgo = c1.clmncdgo) where obra__id = ${id} and valor = 0 and " +
                "codigo = 'sS2' and clmncdgo in (select clmncdgo from mfcl where clmndscr like '%_T')")

        if (hayEqpo) {
            clmn = columnaCdgo(id, "${id_mecanico}_T")
            tx_sql = "select valor from mfvl where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'"
            cn.eachRow(tx_sql.toString()) {row ->
                clmn = columnaCdgo(id, "${id_manoDeObra}_T")
                ejecutaSQL("update mfvl set valor = valor + ${row.valor} " +
                        " where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'")
            }
            clmn = columnaCdgo(id, "TRANSPORTE_T")
            ejecutaSQL("update mfvl set valor = 0 where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'")
        }
        cn.close()
    }

    def acTotalS2(id) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = ""
        def clmn = ""
        tx_sql =  "select sum(valor) suma from mfcl c, mfvl v "
        tx_sql += "where c.clmncdgo = v.clmncdgo and c.obra__id = v.obra__id and c.obra__id = ${id} and codigo = 'sS2' and clmndscr like '%_T' and "
        tx_sql += "clmntipo in ('O', 'M', 'D')"
        //println "acTotal S2: sql: " + tx_sql
        cn.eachRow(tx_sql.toString()) {row ->
            clmn = columnaCdgo(id, "TOTAL_T")
            ejecutaSQL("update mfvl set valor = ${row.suma} " +
                    " where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'")
        }
        cn.close()
    }

    def tarifaHoraria(id) {
        def obra = Obra.get(id)
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_sql = ""
        def tx_cr = ""
        def errr = ""
        def clmn = ""
        def item__id = 0
        def item = ""
        def pcun = 0.0
        def tx = ""
        tx_sql =  "select clmndscr from mfcl where obra__id = ${id} and clmntipo = 'O'"
        cn.eachRow(tx_sql.toString()) {row ->
            tx_cr = "select item__id, itemcdgo, itemnmbr from item where item__id = '${row.clmndscr[0..-3]}'"
            println "tx_cr..... campo:" + tx_cr
            cn1.eachRow(tx_cr.toString()) {d ->
                item__id = d.item__id
                item     = d.itemcdgo
                tx       = d.itemnmbr
            }
            if (obra.estado == 'N') {
/*
                tx_cr = "select rbpcpcun pcun from item_pcun (${item__id}, ${obra.lugarId}, '${obra.fechaPreciosRubros}')"
*/
                tx_cr = "select rbpcpcun pcun from item_pcun_of (${item__id}, ${obra.oferente.id})"
                //println "tarifaHoraria:" + tx_cr
            } else {
                tx_cr = "select itempcun pcun from obit where item__id = ${item__id} and obra__id = ${id}"
            }

            println "...... segunda: " + tx_cr

            cn1.eachRow(tx_cr.toString()) {d ->
                if (!d.pcun) {
                    println "No existe precio para el item ${item}: ${tx}"
                    pcun = 0
                }
//                if (d.pcun == 0) errr = "No existe precio para el item ${item}: ${tx}"
                pcun = d.pcun
            }

            clmn = columnaCdgo(id, row.clmndscr)
            tx_cr = "select valor from mfvl where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'"
            cn1.eachRow(tx_cr.toString()) {d ->
                ejecutaSQL("update mfvl set valor = ${pcun} " +
                        " where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS4'")
                if (pcun > 0){
                    ejecutaSQL("update mfvl set valor = ${d.valor / pcun} " +
                            " where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS6'")
                }
            }
        }
        cn.close()
        cn1.close()
        return errr
    }

    def cuadrillaTipo(id) {
        def cn = dbConnectionService.getConnection()
        def cn1 = dbConnectionService.getConnection()
        def tx_sql = ""
        def tx_cr = ""
        def errr = ""
        def clmn = ""
        def total = 0.0
        def granTotal = 0.0
        def totalS6 = 0.0
        def totalS2 = 0.0
        def suma = 0.0

        tx_sql =  "select sum(valor) suma from mfcl c, mfvl v "
        tx_sql += "where c.clmncdgo = v.clmncdgo and c.obra__id = v.obra__id and c.obra__id = ${id} and codigo = 'sS2' and clmntipo = 'O'"
        println "total: " + tx_sql
        cn.eachRow(tx_sql.toString()) {row ->
            total = row.suma
        }
        clmn = columnaCdgo(id, 'TOTAL_T')
        tx_sql =  "select valor from mfvl where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS2'"
        println "granTotal" + tx_sql
        cn.eachRow(tx_sql.toString()) {row ->
            granTotal = row.valor
        }
        tx_sql =  "select sum(valor) suma from mfcl c, mfvl v "
        tx_sql += "where c.clmncdgo = v.clmncdgo and c.obra__id = v.obra__id and c.obra__id = ${id} and codigo = 'sS6' and clmntipo = 'O'"
        println "suma totalS6: $tx_sql"
        cn.eachRow(tx_sql.toString()) {row ->
            totalS6 = row.suma
        }

        tx_sql = "select sum(valor) suma from mfcl c, mfvl v "
        tx_sql += "where c.clmncdgo = v.clmncdgo and c.obra__id = v.obra__id and c.obra__id = ${id} and " +
                "codigo = 'sS2' and clmntipo = 'O'"
        cn.eachRow(tx_sql.toString()) { row ->
            totalS2 = row.suma
        }

        println  "total" + total + ", granTotal" + granTotal
        if (totalS6.toBigInteger() == 0) errr = "Error: La suma de componentes de Mano de Obra da CERO," +
                "revise los parámetros de Precios"
        else {
            clmn = columnaCdgo(id, "${id_manoDeObra}_T")
            ejecutaSQL("update mfvl set valor = ${totalS6} " +
                    " where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS6'")

            tx_sql =  "select clmncdgo, clmndscr from mfcl where obra__id = ${id} and clmntipo = 'O'"
            suma = 0
            cn.eachRow(tx_sql.toString()) {row ->
                tx_cr = "select valor from mfvl where obra__id = ${id} and clmncdgo = ${row.clmncdgo} and codigo = 'sS2'"
                cn1.eachRow(tx_cr.toString()) {d ->
                    ejecutaSQL("update mfvl set valor = ${d.valor / totalS2} " +
                            " where obra__id = ${id} and clmncdgo = ${row.clmncdgo} and codigo = 'sS5'")
                    suma += d.valor / totalS2
                }
            }
            clmn = columnaCdgo(id, "${id_manoDeObra}_T")
            ejecutaSQL("update mfvl set valor = ${suma} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS5'")
            ejecutaSQL("update mfvl set valor = ${total/granTotal} where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS3'")
        }
        cn.close()
        cn1.close()
        return errr
    }

    def formulaPolinomica(id) {
        def cn = dbConnectionService.getConnection()
        def tx_sql = ""
        def clmn = ""
        def granTotal = 0.0
        def parcial = 0.0
        def valor = 0.0
        def suma = 0.0

        granTotal = totalSx(id, 'TOTAL_T', 'sS2')
        //println ".....1 el gran total es: $granTotal"
        tx_sql =  "select clmncdgo, clmndscr from mfcl where obra__id = ${id} and clmntipo in ('M', 'D')"
        suma = 0
        cn.eachRow(tx_sql.toString()) {row ->
            parcial = totalSx(id, row.clmndscr, 'sS2')
            if (parcial > 0) valor = parcial / granTotal
            else valor = 0
            ejecutaSQL("update mfvl set valor = ${valor} " +
                    " where obra__id = ${id} and clmncdgo = ${row.clmncdgo} and codigo = 'sS3'")
            suma += parcial / granTotal
        }
        parcial = totalSx(id, "${id_manoDeObra}_T", 'sS3')
        clmn = columnaCdgo(id, 'TOTAL_T')
        ejecutaSQL("update mfvl set valor = ${suma + parcial} " +
                " where obra__id = ${id} and clmncdgo = ${clmn} and codigo = 'sS3'")
        cn.close()
    }

}