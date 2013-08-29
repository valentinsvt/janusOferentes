
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    %{--<g:if test="${subPre == -1}">--}%

        %{--<title>Todos los Subpresupuestos</title>--}%

    %{--</g:if>--}%
    %{--<g:else>--}%

        %{--<title>Sub presupuesto ${subPre}</title>--}%
    %{--</g:else>--}%

    <link href="../font/open/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="../css/font-awesome.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
    @page {
        size   : 21cm 29.7cm;  /*width height */
        margin : 2cm;
    }

    body {
        background : none !important;
    }

    .hoja {
        /*background  : #e6e6fa;*/
        height      : 24.7cm; /*29.7-(1.5*2)*/
        font-family : serif;
        font-size   : 10px;
        width       : 16cm;
    }

    .tituloPdf {
        height        : 100px;
        font-size     : 11px;
        /*font-weight   : bold;*/
        text-align    : center;
        margin-bottom : 5px;
        width         : 95%;
        /*font-family       : 'Tulpen One', cursive !important;*/
        /*font-family : "Open Sans Condensed" !important;*/
    }

    .totales {
        font-weight : bold;
    }

    .theader{

        border-bottom: 1px solid #000000 !important;
        border-top: 1px solid #000000 !important;

    }

    .theaderBot {

        /*border: 1px solid #000000;*/

        border-bottom: 1px solid #000000;

    }

    .theaderup {

        /*border: 1px solid #000000;*/
        border-top: 1px solid #000000;



    }

    .padTopBot{

        padding-top: 7px !important;
        padding-bottom: 7px !important;

    }

    thead th{

        background : #FFFFFF !important;
        color: #000000 !important;


    }


    .num {
        text-align : right;
    }

    .header {
        background : #333333 !important;
        color      : #AAAAAA;
    }



    .total {
        /*background : #000000 !important;*/
        /*color      : #FFFFFF !important;*/
    }

    thead tr {
        margin : 0px
    }

    th, td {
        font-size : 10px !important;
    }

    .row-fluid {
        width  : 100%;
        height : 20px;
    }

    .span3 {
        width  : 29%;
        float  : left;
        height : 100%;
    }

    .span8 {
        width  : 79%;
        float  : left;
        height : 100%;
    }

    .span7 {
        width  : 69%;
        float  : left;
        height : 100%;
    }


    </style>
</head>

<body>
<div class="hoja" style="margin-bottom: 40px">

    <div class="tituloPdf" style="margin-bottom: 100px !important">


        <p style="font-size: 14px">
            <b> Formulario N°. 2 </b>
        </p>

        <p style="font-size: 14px; text-align: left">
            <b> NOMBRE DEL OFERENTE: </b> ${oferente?.nombre?.toUpperCase() + " " + oferente?.apellido?.toUpperCase()}
        </p>
        <p style="font-size: 14px; text-align: left">
            <b>PROCESO: </b> ${concurso?.codigo}
        </p>
        <p style="font-size: 14px; text-align: left">
            <b> TABLA DE DESCRIPCIÓN DE RUBROS, UNIDADES, CANTIDADES Y PRECIOS </b>
        </p>
        <p style="font-size: 14px; text-align: left">
            <b>GOBIERNO AUTÓNOMO DESCENTRALIZADO DE LA PROVINCIA DE PICHINCHA</b>
        </p>
        <p style="font-size: 14px; text-align: left; margin-bottom: 80px">
            <b>NOMBRE DEL PROYECTO:</b> ${obra?.nombre.toUpperCase()}
        </p>

    </div>



    <g:set var="total1" value="${0}"></g:set>
    <g:set var="total2" value="${0}"></g:set>
    <g:set var="totalPrueba" value="${0}"></g:set>
    <g:set var="totales" value="${0}"></g:set>
    <g:set var="totalPresupuesto" value="${0}"></g:set>
    <g:if test="${subPre == -1}">
        <g:each in="${subPres}" var="sp" status="sub">
            <div style="font-size: 12px; font-weight: bold">${sp.descripcion}</div>
            <table class="table table-bordered table-striped table-condensed table-hover">
                <thead>
                <tr class="theaderBot thederup padTopBot">
                    <th style="width: 20px;" class="theaderBot theaderup padTopBot">
                        Código (cpc)
                    </th>
                    <th style="width: 20px;" class="theaderBot theaderup padTopBot">
                        N°
                    </th>
                    <th style="width: 80px;" class="theaderBot theaderup padTopBot">
                        Rubro
                    </th>
                    <th style="width: 550px;" class="theaderBot theaderup padTopBot">
                        Componente del proyecto/Item
                    </th>
                    <th style="width: 35px;" class="col_unidad theaderBot theaderup padTopBot">
                        Unidad
                    </th>
                    <th style="width: 80px;" class="theaderBot theaderup padTopBot">
                        Cantidad
                    </th>
                    <th class="col_precio theaderBot theaderup padTopBot" style="width:110px ;">P. U.</th>
                    <th class="col_total  theaderBot theaderup padTopBot" style="width:110px;">C.Total</th>
                </tr>
                </thead>
                <tbody id="tabla_material">
                <g:set var="total" value="${0}"></g:set>
                <g:each in="${detalle}" var="vol" status="j">

                <g:if test="${vol.subPresupuesto.id == sp.id}">
                    <tr class="item_row" id="${vol.id}" item="${vol.item.id}" sub="${vol.subPresupuesto.id}">
                        <td style="width: 20px" class="cpc"></td>
                        <td style="width: 20px" class="orden">${vol.orden}</td>
                        <td class="cdgo">${vol.item.codigo}</td>
                        <td class="nombre">${vol.item.nombre.replaceAll("<","(Menor)").replaceAll(">","(Mayor)")}</td>
                        <td style="width: 60px !important;text-align: center" class="col_unidad">${vol.item.unidad.codigo}</td>
                        <td style="text-align: right" class="cant">
                            <g:formatNumber number="${vol.cantidad}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/>
                        </td>
                        <td class="col_precio" style=";text-align: right" id="j_${vol.item.id}"><g:formatNumber number="${precios[vol.id.toString()]}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                        <td class="col_total total" style=";text-align: right"><g:formatNumber number="${precios[vol.id.toString()]*vol.cantidad}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                        <g:set var="total" value="${total.toDouble()+(precios[vol.id.toString()]*vol.cantidad)}"></g:set>

                        <g:hiddenField name="totales" value="${totales = precios[vol.id.toString()]*vol.cantidad}"/>
                        <g:hiddenField name="totalPrueba" value="${totalPrueba = total2+=totales}"/>
                        <g:hiddenField name="totalPresupuesto" value="${totalPresupuesto = total1 += totales}"/>




                    </tr>
                </g:if>
                </g:each>



                <tr>
                    <td colspan="5"></td>
                    <td><b>Total:</b></td>
                    <td style="text-align: right"><g:formatNumber number="${total}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                </tr>
                </tbody>
            </table>

        </g:each>

    <table style="margin-top: 10px; margin-left:430px; font-size: 12px !important;">
    <thead>

    </thead>
    <tbody>
    <tr>

    <td style="text-align: right"><b style="font-size: 10px">Total Presupuesto:</b></td>
    <td style="text-align: right; font-size: 12px"><b><g:formatNumber number="${totalPresupuesto}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b></td>
    </tr>


    </tbody>
    </table>

            <div class="tituloPdf">


                <p style="font-size: 14px; text-align: left">
                    NOTA: ESTOS PRECIOS NO INCLUYEN IVA
                </p>

                <p style="font-size: 14px; text-align: left">
                    <g:set var="valor" value="${totalPresupuesto.toDouble().round(2)}"/>
                    PRECIO TOTAL DE LA OFERTA USD <b><elm:numberToLetter numero="${valor}" dolares="true"/></b>, MÁS IVA.

                </p>


                <p style="font-size: 14px; text-align: left; margin-top: 40px; margin-bottom: 60px">
                    <b>QUITO, ${fechaOferta}</b>
                </p>

                <p style="text-align: left">
                    _____________________________________
                </p>

                <p style="font-size: 14px; text-align: left; margin-bottom: 50px">
                    <g:if test="${firma}">
                       <b> ${firma.toUpperCase()}</b>
                    </g:if>
                    <g:else>
                        FIRMA DEL RESPONSABLE
                    </g:else>

                </p>

            </div>


    </g:if>

    <g:else>

        <table class="table table-bordered table-striped table-condensed table-hover">
            <thead>
            <tr class="theaderBot thederup padTopBot">
                <th style="width: 20px;" class="theaderBot theaderup padTopBot">
                    Código (cpc)
                </th>
                <th style="width: 20px;" class="theaderBot theaderup padTopBot">
                    N°
                </th>
                <th style="width: 80px;" class="theaderBot theaderup padTopBot">
                    Rubro
                </th>
                <th style="width: 550px;" class="theaderBot theaderup padTopBot">
                    Descripción
                </th>
                <th style="width: 35px;" class="col_unidad theaderBot theaderup padTopBot">
                    Unidad
                </th>
                <th style="width: 80px;" class="theaderBot theaderup padTopBot">
                    Cantidad
                </th>
                <th class="col_precio theaderBot theaderup padTopBot" style="width:110px ;">P. U.</th>
                <th class="col_total  theaderBot theaderup padTopBot" style="width:110px;">C.Total</th>
            </tr>
            </thead>
            <tbody id="tabla_material">
            <g:set var="total" value="${0}"></g:set>
            <g:each in="${detalle}" var="vol" status="i">

                <tr class="item_row" id="${vol.id}" item="${vol.item.id}" sub="${vol.subPresupuesto.id}">
                    <td style="width: 20px" class="cpc"></td>
                    <td style="width: 20px" class="orden">${vol.orden}</td>
                    <td class="cdgo">${vol.item.codigo}</td>
                    <td class="nombre">${vol.item.nombre.replaceAll("<","(Menor)").replaceAll(">","(Mayor)")}</td>
                    <td style="width: 60px !important;text-align: center" class="col_unidad">${vol.item.unidad.codigo}</td>
                    <td style="text-align: right" class="cant">
                        <g:formatNumber number="${vol.cantidad}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/>
                    </td>
                    <td class="col_precio" style=";text-align: right" id="i_${vol.item.id}"><g:formatNumber number="${precios[vol.id.toString()]}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                    <td class="col_total total" style=";text-align: right"><g:formatNumber number="${precios[vol.id.toString()]*vol.cantidad}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                    <g:set var="total" value="${total.toDouble()+(precios[vol.id.toString()]*vol.cantidad)}"></g:set>
                </tr>

            </g:each>
            <tr>
                <td colspan="5"></td>
                <td><b>Total:</b></td>
                <td style="text-align: right"><g:formatNumber number="${total}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
            </tr>
            </tbody>
        </table>


        <div class="tituloPdf">


            <p style="font-size: 14px; text-align: left">
                NOTA: ESTOS PRECIOS NO INCLUYEN IVA
            </p>

            <p style="font-size: 14px; text-align: left">
                <g:set var="valor" value="${total.toDouble().round(2)}"/>
                PRECIO TOTAL DE LA OFERTA USD <b><elm:numberToLetter numero="${valor}" dolares="true"/></b>, MÁS IVA.

            </p>


            <p style="font-size: 14px; text-align: left; margin-top: 40px; margin-bottom: 60px">
                QUITO,
            </p>

            <p style="text-align: left">
                _____________________________________
            </p>


            <p style="font-size: 14px; text-align: left; margin-bottom: 50px">
                FIRMA DEL RESPONSABLE.
            </p>

        </div>
    </g:else>
</div>



</body>
</html>

