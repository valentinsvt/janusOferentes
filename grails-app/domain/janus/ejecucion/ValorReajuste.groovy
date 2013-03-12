package janus.ejecucion

//import janus.Indice
//import janus.pac.PeriodoValidez

class ValorReajuste {

    janus.Obra obra
    Planilla planilla
    PeriodosInec periodoIndice
//    Indice indice
    FormulaPolinomicaContractual formulaPolinomica

    double valor

    static mapping = {
        table 'vlrj'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'vlrj__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'vlrj__id'
            planilla column: 'plnl__id'
            periodoIndice column: 'prin__id'
//            indice column: 'indc__id'
            formulaPolinomica column: 'frpl__id'
            valor column: 'vlrjvlor'
            obra column: 'obra__id'
        }
    }
    static constraints = {
        planilla(blank: true, nullable: true)
        periodoIndice(blank: true, nullable: true)
//        indice(blank: true, nullable: true)
        formulaPolinomica(blank: true, nullable: true)
        valor(blank: true, nullable: true)
        obra(blank: false, nullable: false)
    }
}
