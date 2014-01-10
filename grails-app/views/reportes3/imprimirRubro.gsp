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
    %{--<link href="../../css/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../css/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../font/open/stylesheet.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../css/custom.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../css/font-awesome.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../css/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>--}%
    <link href="../font/open/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>
    <link href="../css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="../css/font-awesome.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
    @page {
        size   : 21cm 29.7cm;  /*width height */
        margin-left : 2cm;
        margin-top: 1cm;
    }

    body {
        background : none !important;
    }

    .hoja {
        /*background  : #e6e6fa;*/
        height      : 20.7cm !important; /*29.7-(1.5*2)*/
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

    .grande{

        font-size: 18px;
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

        /*th {*/
        /*background : #cccccc;*/
        /*}*/

        /*tbody tr:nth-child(2n+1) {*/
        /*background : none repeat scroll 0 0 #E1F1F7;*/
        /*}*/

        /*tbody tr:nth-child(2n) {*/
        /*background : none repeat scroll 0 0 #F5F5F5;*/
        /*}*/
    thead tr {
        margin : 0px
    }

    th, td {
        font-size : 10px !important;
    }

    .theader {

        /*border: 1px solid #000000;*/
        /*margin-bottom: 30px !important;*/
        border-bottom: 1px solid #000000;

    }

    .theaderup {
        /*margin-top: 30px !important;*/
        /*border: 1px solid #000000;*/
        border-top: 1px solid #000000;



    }

    .marginTop{

        margin-top:20px !important;
    }


    .tituloHeader{

        font-size: 14px !important;

    }

    .padTopBot{

        padding-top: 7px !important;
        padding-bottom: 7px !important;

    }

    .row-fluid {
        width  : 100%;
        height : 10px;
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

<body  style="margin-top: 56pt !important">
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

    <div style="margin-top: 20px">

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

    <div style="width: 100%;margin-top: 10px;">

        ${tablaHer}
        ${tablaMano}
        ${tablaMat}
        %{--<g:if test="${bandMat != 1}">--}%
            %{--${tablaMat2}--}%
        %{--</g:if>--}%
        %{--${tablaTrans}--}%
        ${tablaIndi}
        <table class="table table-bordered table-striped table-condensed table-hover" style="margin-top: 40px;width: 50%;float: right; border-top: 1px solid #000000;  border-bottom: 1px solid #000000">
            <tbody>
            <tr style="">
                <td style="width: 350px;">
                    <b>COSTO UNITARIO DIRECTO</b>
                </td>
                <td style="text-align: right">
                    <b><g:formatNumber number="${totalRubro}" format="##,#####0" minFractionDigits="5" maxFractionDigits="5" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>COSTOS INDIRECTOS</b>
                </td>
                <td style="text-align: right">
                    <b> <g:formatNumber number="${totalIndi}" format="##,#####0" minFractionDigits="5" maxFractionDigits="5" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>COSTO TOTAL DEL RUBRO</b>
                </td>
                <td style="text-align: right">
                    <b><g:formatNumber number="${totalRubro + totalIndi}" format="##,#####0" minFractionDigits="5" maxFractionDigits="5" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>PRECIO UNITARIO ($USD)</b>
                </td>
                <td style="text-align: right">
                    <b><g:formatNumber number="${totalRubro + totalIndi}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>

                </td>
            </tr>

            </tbody>
        </table>

    </div>

    <div style="width: 100%;float: left;height: 20px;margin-top: 10px;text-align: left">
        <b>Nota:</b> Los cálculos se hacen con todos los decimales y el resultado final se lo redondea a dos decimales, estos precios no incluyen IVA.

        <p style="font-size: 12pt; text-align: left">
            <b>Quito, ${fechaEntregaOFerta}</b>
        </p>
         <p style="font-size: 12pt; text-align: left; margin-top: 60px">
            <b>__________________________</b>
        </p>
        <p style="font-size: 12pt; text-align: left">
            <b>${firma}</b>
        </p>
    </div>













    <div>

    </div>


</div>
</body>
</html>

