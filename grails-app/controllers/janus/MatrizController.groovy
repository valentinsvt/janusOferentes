package janus

class MatrizController extends janus.seguridad.Shield {

    def preciosService
    def dbConnectionService

    def index() {}


    def coeficientes(){
        println "coef "+params
        def obra = Obra.get(params.id)
        def fp = FormulaPolinomica.findAllByObra(obra,[order:"numero"])
        [obra:obra,fp:fp]
    }

    def pantallaMatriz(){
        def obra = params.id
        def cn = dbConnectionService.getConnection()
        def sql = "SELECT clmncdgo,clmndscr,clmntipo from mfcl where obra__id = ${obra} order by  1"
//        println "sql desc "+sql
        def columnas = []
        def filas = []
        cn.eachRow(sql.toString()){r->
            columnas.add([r[0],r[1],r[2]])
        }
        [obra:obra,cols:columnas]
    }

    def matrizPolinomica(){
        println "matriz "+params
        def obra = params.id
        def offset = params.inicio
        if (!offset)
            offset = 0
        else
            offset = offset.toInteger()
        def limit = params.limit.toInteger()
        offset = offset*limit
        def cn = dbConnectionService.getConnection()
        def cn2 = dbConnectionService.getConnection()
        def sql = "SELECT clmncdgo,clmndscr,clmntipo from mfcl where obra__id = ${obra} order by 1"

        def columnas = []
        def filas = []
        cn.eachRow(sql.toString()){r->
            columnas.add([r[0],r[1],r[2]])
        }
        sql ="SELECT * from mfrb where obra__id =${obra} order by orden limit ${limit} offset ${offset}"
        println "sql desc "+sql
        def cont = offset+1
        cn.eachRow(sql.toString()){r->
            def tmp = [cont,r[0].trim(),r[2],r[3],r[4]]
            def sq =""
            columnas.each {c->
                if(c[2]!="R"){
                    sq = "select valor from mfvl where clmncdgo=${c[0]} and codigo='${r[0].trim()}' and obra__id =${obra}"
                    cn2.eachRow(sq.toString()){v->
                        tmp.add(v[0])
                    }
                }

            }


            filas.add(tmp)
            cont++
        }
        if (filas.size()==0)
            render "fin"
        else
            [filas:filas,cols:columnas,obraId:params.id,offset:offset]


    }

    def insertarVolumenesItem(){
        println "insert vlobitem "+params
        def obra = Obra.get(params.obra)
        def cn = dbConnectionService.getConnection()
        def cn2 = dbConnectionService.getConnection()
        def updates = dbConnectionService.getConnection()
        def sql = "SELECT v.voit__id,v.obra__id,v.item__id,v.voitpcun,v.voitcntd,v.voitcoef,v.voitordn,v.voittrnp,v.voitrndm,i.itemnmbr,i.dprt__id,d.sbgr__id,s.grpo__id,o.clmndscr,o.clmncdgo from vlobitem v,dprt d,sbgr s,item i,mfcl o where v.item__id=i.item__id and i.dprt__id=d.dprt__id and d.sbgr__id=s.sbgr__id  and o.clmndscr = i.itemcmpo || '_T'  and  v.obra__id = ${params.obra} and o.obra__id=${params.obra} order by s.grpo__id"
//        println "sql "+sql
        cn.eachRow(sql.toString()){r->
//            println "r-> "+r
            def codigo = ""
            if (r['grpo__id']==1 || r['grpo__id']==3)
                codigo = "sS3"
            else
                codigo = "sS5"
            def select = "select valor from mfvl where clmncdgo=${r['clmncdgo']} and codigo='${codigo}' and obra__id =${params.obra} "
            def valor = 0
            cn2.eachRow(select.toString()){r2->
//                println "r2 "+r2
                valor = r2['valor']
                if(!valor)
                    valor=0
                def sqlUpdate ="update vlobitem set voitcoef= ${valor} where voit__id = ${r['voit__id']}"
                updates.execute(sqlUpdate.toString())
            }
        }

        def fp = FormulaPolinomica.findAllByObra(obra)
        if (fp.size()==0){
            def indice21 = Indice.findByCodigo("021")
            def indiSldo = Indice.findByCodigo("SLDO")
            def indiMano = Indice.findByCodigo("MO")
            def indiPeon = Indice.findByCodigo("C.1")
            11.times {
                def fpx = new FormulaPolinomica()
                fpx.obra=obra
                if (it<10){
                    fpx.numero="p0"+(it+1)
                    if (it==0){
                        fpx.indice=indiMano
                        def select = "select clmncdgo from mfcl where clmndscr = 'MANO_OBRA_T' and obra__id = ${params.obra} "
                        def columna
                        def valor = 0
                        println "sql it 0 mfcl "+select
                        cn.eachRow(select.toString()){r->
                            columna=r[0]
                        }
                        select = "select valor from mfvl where clmncdgo=${columna} and codigo='sS3' and obra__id =${params.obra} "
                        cn.eachRow(select.toString()){r->
                            valor=r[0]
                        }
                        if (!valor)
                            valor=0
                        fpx.valor=valor
                    }else{
                        if (it==9)
                            fpx.numero="p"+(it+1)
                        fpx.indice=indice21
                        fpx.valor=0
                    }
                }else{
                    fpx.numero="px"
                    fpx.indice=indiSldo
                    fpx.valor=0
                }
                if (!fpx.save(flush: true))
                    println "erroe save fpx "+fpx.errors

                if(it<10){
                    def cuadrilla = new FormulaPolinomica()
                    cuadrilla.obra=obra
                    cuadrilla.numero="c0"+(it+1)
                    if (it==9)
                        cuadrilla.numero="c"+(it+1)
                    cuadrilla.valor=0
                    cuadrilla.indice=indiPeon
                    if (!cuadrilla.save(flush: true))
                        println "error save cuadrilla "+cuadrilla.errors
                }


            }
        }


        render "ok"
    }

}
