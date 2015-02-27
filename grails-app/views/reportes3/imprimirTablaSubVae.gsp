<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 24/02/15
  Time: 03:26 PM
--%>


<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>

    <link href="../font/open/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="../css/font-awesome.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
    @page {
        /*size   : 21cm 29.7cm;  *//*width height */
        size   : 29.7cm 21cm;  /*width height */
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
        width       : 25cm;
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

        padding-top: 5px !important;
        padding-bottom: 5px !important;

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
            <b> Formulario N° 2 </b>
        </p>

        <p style="font-size: 14px; text-align: left">
            <b> NOMBRE DEL OFERENTE: </b> ${oferente?.nombre?.toUpperCase() + " " + oferente?.apellido?.toUpperCase()}
        </p>
        <p style="font-size: 14px; text-align: left">
            <b>PROCESO: </b> ${obra?.codigoConcurso}
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

    <g:set var="total1" value="${0}"/>
    <g:set var="total2" value="${0}"/>
    <g:set var="totalPrueba" value="${0}"/>
    <g:set var="totales" value="${0}"/>
    <g:set var="totalPresupuesto" value="${0}"/>
    <g:set var="totalRelativo1" value="${0}"/>
    <g:set var="finalRelativo" value="${0}"/>
    <g:set var="totalRelativo2" value="${0}"/>
    <g:set var="totalR1" value="${0}"/>
    <g:set var="totalR2" value="${0}"/>

    <g:set var="totalVae1" value="${0}"/>
    <g:set var="totalVae2" value="${0}"/>
    <g:set var="finalVae" value="${0}"/>
    <g:set var="totalV1" value="${0}"/>
    <g:set var="totalV2" value="${0}"/>



    <g:if test="${subPre == -1}">
        <g:each in="${subPres}" var="sp" status="sub">
            <div style="font-size: 12px; font-weight: bold">${sp.descripcion}</div>
            <table class="table table-bordered table-striped table-condensed table-hover">
                <thead>
                <tr>
                    <th colspan="11" class="theaderBot theaderup padTopBot">
                    <th style="width: 35px;"  >
                        Código (cpc)
                    </th>
                    <th style="width: 20px;" >
                        N°
                    </th>
                    <th style="width: 80px;" >
                        Rubro
                    </th>
                    <th style="width: 450px;" >
                        Componente del proyecto/Item
                    </th>
                    <th style="width: 60px;" class="col_unidad ">
                        Unidad
                    </th>
                    <th style="width: 80px;">
                        Cantidad
                    </th>
                    <th class="col_precio " style="width:80px ;">P. U.</th>
                    <th class="col_total  " style="width:80px;">C.Total</th>

                    <th style="width: 60px;" >
                        Peso Relativo
                    </th>
                    <th style="width: 40px;" >
                        VAE Rubro
                    </th>
                    <th style="width: 40px;">
                        VAE Total
                    </th>
                </th>

                </tr>
                </thead>
                <tbody id="tabla_material">
                <g:set var="total" value="${0}"/>

                <g:each in="${detalle}" var="vol" status="j">

                    <g:if test="${vol.sbpr__id == sp.id}">
                        <tr class="item_row" id="${vol.vlob__id}" item="${vol.item__id}" sub="${vol.sbpr__id}">
                            <td style="width: 50px" class="cpc"></td>
                            <td style="width: 20px" class="orden">${vol.vlobordn}</td>
                            <td style="width: 60px" class="cdgo">${vol.rbrocdgo.trim()}</td>
                            <td style="width: 450px" class="nombre">${vol.rbronmbr.trim()}</td>
                            <td style="width: 60px !important;text-align: center" class="col_unidad">${vol.unddcdgo.trim()}</td>
                            <td style="text-align: right; width: 80px" class="cant" >
                                <g:formatNumber number="${vol.vlobcntd}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/>
                            </td>
                            <td class="col_precio" style=" width:80px;text-align: right" id="j_${vol.item__id}"><g:formatNumber number="${vol.pcun}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                            <td class="col_total total" style="width:80px;text-align: right"><g:formatNumber number="${vol.totl}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                            <td style="width: 60px;text-align: right" class="relativo"><g:formatNumber number="${vol.relativo}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                            <td style="width: 50px;text-align: right" class="vae_rbro"><g:formatNumber number="${vol.vae_rbro}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                            <td style="width: 50px;text-align: right" class="vae_totl"><g:formatNumber number="${vol.vae_totl}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                            <g:set var="total" value="${total.toDouble() + vol.totl}"/>

                            <g:hiddenField name="totales" value="${totales = vol.totl}"/>
                            <g:hiddenField name="totalPrueba" value="${totalPrueba = total2+=totales}"/>
                            <g:hiddenField name="totalPresupuesto" value="${totalPresupuesto = total1 += totales}"/>

                            <g:hiddenField name="totalRelativo1" value="${totalRelativo1 = vol.relativo}"/>
                            <g:hiddenField name="totalRelativo2" value="${totalRelativo2 = totalR2+=totalRelativo1}"/>
                            <g:hiddenField name="finalRelativo" value="${finalRelativo = totalR1 += totalRelativo1}"/>

                            <g:hiddenField name="totalVae1" value="${totalVae1 = vol.vae_totl}"/>
                            <g:hiddenField name="totalVae2" value="${totalVae2 = totalV2 += totalVae1}"/>
                            <g:hiddenField name="finalVae" value="${finalVae = totalV1 += totalVae1}"/>




                        </tr>
                    </g:if>
                </g:each>
                <tr>
                    <td colspan="6"></td>
                    <td style="text-align: right"><b>Total:</b></td>
                    <td style="text-align: right"><g:formatNumber number="${total}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                </tr>
                </tbody>
            </table>

        </g:each>


        <table style="margin-top: 10px; font-size: 12px !important">
            <thead>
            </thead>
            <tbody>
            <tr>
                <td colspan="11" class="theaderBot theaderup padTopBot">
                <td style="text-align: right; width: 880px"><b>TOTAL PRESUPUESTO:  </b></td>
                <td style="text-align: right; width: 80px "><b><g:formatNumber number="${totalPresupuesto}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b></td>
                <td style="text-align: right; width: 70px "><b><g:formatNumber number="${finalRelativo}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b></td>
                <td style="text-align: right; width: 100px "><b><g:formatNumber number="${finalVae}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b></td>
            </td>
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
                <b>Quito, ${fechaOferta}</b>
            </p>

            <p style="text-align: left">
                _____________________________________
            </p>

            <p style="font-size: 14px; text-align: left; margin-bottom: 50px">
                <g:if test="${firma}">
                    <b> ${firma}</b>
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
                <th colspan="11" class="theaderBot theaderup padTopBot">
                <th style="width: 35px;" >
                    Código (cpc)
                </th>
                <th style="width: 20px;" >
                    N°
                </th>
                <th style="width: 80px;" >
                    Rubro
                </th>
                <th style="width: 450px;" >
                    Componente del proyecto/Item
                </th>
                <th style="width: 60px;" class="col_unidad ">
                    Unidad
                </th>
                <th style="width: 80px;">
                    Cantidad
                </th>
                <th class="col_precio " style="width:80px ;">P. U.</th>
                <th class="col_total  " style="width:80px;">C.Total</th>

                <th style="width: 60px;" >
                    Peso Relativo
                </th>
                <th style="width: 40px;" >
                    VAE Rubro
                </th>
                <th style="width: 40px;">
                    VAE Total
                </th>
            </th>

            </tr>
            </thead>
            <tbody id="tabla_material">
            <g:set var="total" value="${0}"/>
            <g:each in="${detalle}" var="vol" status="i">

                <tr class="item_row" id="${vol.id}" item="${vol.item.id}" sub="${vol.subPresupuesto.id}">
                    <td style="width: 50px" class="cpc"></td>
                    <td style="width: 20px" class="orden">${vol.orden}</td>
                    <td style="width: 60px" class="cdgo">${vol.item.codigo}</td>
                    <td style="width: 450px" class="nombre">${vol.item.nombre.replaceAll("<","(Menor)").replaceAll(">","(Mayor)")}</td>
                    <td style="width: 60px !important;text-align: center" class="col_unidad">${vol.item.unidad.codigo}</td>
                    <td style="text-align: right; width: 80px" class="cant">
                        <g:formatNumber number="${vol.cantidad}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/>
                    </td>
                    <td class="col_precio" style="width:80px;text-align: right" id="i_${vol.item.id}"><g:formatNumber number="${precios[vol.id.toString()]}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                    <td class="col_total total" style="width:80px;text-align: right"><g:formatNumber number="${precios[vol.id.toString()]*vol.cantidad}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                    <td style="width: 60px;text-align: right" class="relativo"><g:formatNumber number="${vol.relativo}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                    <td style="width: 50px;text-align: right" class="vae_rbro"><g:formatNumber number="${vol.vae_rbro}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>
                    <td style="width: 50px;text-align: right" class="vae_totl"><g:formatNumber number="${vol.vae_totl}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></td>

                    <g:set var="total" value="${total.toDouble()+(precios[vol.id.toString()]*vol.cantidad)}"/>
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
                Quito, <b>${fechaOferta}</b>
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

