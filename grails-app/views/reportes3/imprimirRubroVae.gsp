<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 20/02/15
  Time: 03:52 PM
--%>

<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 29/01/15
  Time: 04:15 PM
--%>

<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 06/01/15
  Time: 04:58 PM
--%>

<%--
  Created by IntelliJ IDEA.
  User: luz
  Date: 11/22/12
  Time: 12:59 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Rubro :${rubro.codigo}</title>
    <link href="../../../web-app/font/open/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../../../web-app/font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../../../web-app/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="../../../web-app/css/font-awesome.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
    @page {
        /*size   : 21cm 29.7cm;  *//*width height */
        size   : 29.7cm 21cm;  /*width height */
        margin : 2cm;
        margin-left: 2.0cm;
    ;
    }

    body {
        background : none !important;
    }

    .hoja {
        /*background  : #e6e6fa;*/
        height      : 24.7cm; /*29.7-(1.5*2)*/
        font-family : arial;
        font-size   : 10px;
        width       : 25cm;
    }

    .tituloPdf {
        height        : 60px;
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

    .num {
        text-align : right;
    }

    .header {
        background : #333333 !important;
        color      : #AAAAAA;
    }

    .total {
        background : #000000 !important;
        color      : #FFFFFF !important;
    }


    thead tr {
        margin : 0px;


    }

    th, td {
        font-size : 10px !important;


    }

    .theader {

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

    .marginTop{

        margin-top:20px !important;
    }

    .tituloHeader{
        font-size: 14px !important;
    }




    thead th{

        background : #FFFFFF !important;
        color: #000000 !important;


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
<div class="hoja">

    <div class="tituloPdf tituloHeader">
        <p style="font-size: 12pt; text-align: center">
            <b>Formulario N°4</b>
        </p>
        <p style="font-size: 12pt; text-align: left">
            <b>NOMBRE DEL OFERENTE: ${oferente?.nombre.toUpperCase() + " " + oferente?.apellido.toUpperCase()}</b>
        </p>
        <p style="font-size: 12pt; text-align: center">
            <b>PROCESO: ${obra?.codigoConcurso}</b>
        </p>
        <p style="font-size: 12pt; text-align: left">
            <b>ANÁLISIS DE PRECIOS UNITARIOS</b>
        </p>
    </div>

    <div style="margin-top: 100px">
        <div class="row-fluid">
            <div class="span12" style="margin-right: 195px !important; margin-top: 50px !important">
                <b>Proyecto:    </b>${obra?.nombre.toUpperCase()}
            </div>
        </div>
        <div class="row-fluid" style="margin-top: 5px">
            <div>
                <b style="margin-top: 5px">Rubro:   </b> ${rubro.nombre}
            </div>
        </div>
        <div class="row-fluid" style="margin-top: 5px">
            <div>
                <b style="margin-top: 5px">Unidad:  </b> ${rubro.unidad.codigo}
            </div>
        </div>
    </div>



    <div style="width: 100%">

        ${tablaHer}
        ${tablaMano}
        ${tablaMat}
        <g:if test="${bandMat != 1}">
            ${tablaMat2}
        </g:if>
        ${tablaTrans}
        <g:if test="${band == 0 && bandTrans == '1'}">

            ${tablaTrans2}
        </g:if>
        ${tablaIndi}
        <table class="table table-bordered table-striped table-condensed table-hover" style="margin-top: 25px; width: 600px;float: right;  border-top: 1px solid #000000;  border-bottom: 1px solid #000000;">
            <tbody>
            <tr>
                <td style="width: 240px; border-bottom: #000000">
                    <b>COSTO UNITARIO DIRECTO</b>
                </td>
                <td style="text-align: right; width: 50px;">
                    <b> <g:formatNumber number="${totalRubro}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>
                </td>
                <td style="width: 120px; text-align: center">
                    <b> <g:formatNumber number="${totalRelativo}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>
                </td>
                <td style="width: 100px"> </td>
                <td style="text-align: right; width: 40px;">
                    <b> <g:formatNumber number="${totalVae}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>COSTOS INDIRECTOS</b>
                </td>
                <td style="text-align: right;">
                    <b> <g:formatNumber number="${totalIndi}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>
                </td>
                <td style="text-align: center"><b>TOTAL</b></td>
                <td> </td>
                <td style="text-align: center">
                    <b>TOTAL</b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>COSTO TOTAL DEL RUBRO</b>
                </td>
                <td style="text-align: right">
                    <b>  <g:formatNumber number="${totalRubro + totalIndi}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>
                </td>
                <td style="text-align: center">
                    <b>PESO</b>
                </td>
                <td> </td>
                <td style="text-align: center">
                    <b>VAE</b>
                </td>

            </tr>
            <tr>
                <td>
                    <b>PRECIO UNITARIO $USD</b>
                </td>
                <td style="text-align: right">
                    <b><g:formatNumber number="${(totalRubro + totalIndi).toDouble().round(2)}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>

                </td>
                <td style="text-align: center">
                    <b>RELATIVO (%)</b>
                </td>
                <td> </td>
                <td style="text-align: center">
                    <b>(%)</b>
                </td>
            </tr>


            </tbody>
        </table>

    </div>
    %{--<table style="margin-top: 130px">--}%
        %{--<tbody>--}%
        %{--<div>--}%
            %{--<b>Nota:</b> Los cálculos se hacen con todos los decimales y el resultado final se lo redondea a dos decimales, estos precios no incluyen IVA.--}%
        %{--</div>--}%
        %{--</tbody>--}%
    %{--</table>--}%


    <div style="width: 100%;float: left;height: 20px;margin-top: 10px;text-align: left">
        <b>Nota:</b> Los cálculos se hacen con todos los decimales y el resultado final se lo redondea a dos decimales, estos precios no incluyen IVA.

        <p style="font-size: 11pt; text-align: left">
            <b>Quito, ${fechaEntregaOFerta}</b>
        </p>
        <p style="font-size: 11pt; text-align: left; margin-top: 60px">
            <b>__________________________</b>
        </p>
        <p style="font-size: 11pt; text-align: left">
            <b>${firma}</b>
        </p>
    </div>



</div>

</body>

</html>