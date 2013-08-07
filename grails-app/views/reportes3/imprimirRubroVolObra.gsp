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
        margin : 2cm;
    }

    body {
        background : none !important;
    }

    .hoja {
        /*background  : #e6e6fa;*/
        height      : 24.7cm; /*29.7-(1.5*2)*/
        font-family : arial;
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
        <p style="font-size: 18px">
            <b>G.A.D. PROVINCIA DE PICHINCHA</b>
        </p>

        <p style="font-size: 14px">
            <b>${oferente?.nombre?.toUpperCase() + " " + oferente?.apellido?.toUpperCase()}</b>
        </p>

        <p style="font-size: 14px">
            <b>ANÁLISIS DE PRECIOS UNITARIOS DE PRESUPUESTO</b>
        </p>
    </div>

    <div style="margin-top: 20px">
        <div class="row-fluid">
            <div class="span3" style="margin-right: 195px !important;">
                %{--<b>Fecha:</b> ${new java.util.Date().format("dd-MM-yyyy")}--}%
                %{--<b>Fecha:</b> ${fechaSalida.format("dd-MM-yyyy")}--}%
            </div>

            <div class="span4">
                %{--<b>Fecha Act. P.U:</b> ${fechaPrecios.format("dd-MM-yyyy")}--}%
            </div>
        </div>

        <div class="row-fluid">
            <div class="span3" style="margin-right: 195px !important;">
                <b>Código:</b> ${rubro.codigo}
            </div>

            <div class="span4">
                <b>Unidad:</b> ${rubro.unidad.codigo}
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">

                <b>Doc. Referencia:</b> ${obra?.oficioIngreso}
            </div>
        </div>



        <div class="row-fluid">
            <div class="span12">

                <b>Nombre del Proyecto:</b> ${obra?.nombre}
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">

                <b># Concurso:</b>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">

                <b>Fecha presentación de la oferta:</b>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">
                <g:set var="nombre" value="${rubro.nombre.replaceAll('<', '(menor)')}"></g:set>
                <g:set var="nombre" value="${rubro.nombre.replaceAll('<', '(mayor)')}"></g:set>
                <b>Descripción:</b> ${nombre}
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
        %{--<g:if test="${band == 0 && bandTrans == '1'}">--}%

            %{--${tablaTrans2}--}%
        %{--</g:if>--}%
        ${tablaIndi}
        <table class="table table-bordered table-striped table-condensed table-hover" style="margin-top: 40px;width: 50%;float: right;  border-top: 1px solid #000000;  border-bottom: 1px solid #000000">
            <tbody>
            <tr>
                <td style="width: 350px; border-bottom: #000000">
                    <b>COSTO UNITARIO DIRECTO</b>
                </td>
                <td style="text-align: right">
                    <b> <g:formatNumber number="${totalRubro}" format="##,#####0" minFractionDigits="5" maxFractionDigits="7" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>COSTOS INDIRECTOS</b>
                </td>
                <td style="text-align: right">
                    <b> <g:formatNumber number="${totalIndi}" format="##,#####0" minFractionDigits="5" maxFractionDigits="7" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>COSTO TOTAL DEL RUBRO</b>
                </td>
                <td style="text-align: right">
                    <b>  <g:formatNumber number="${totalRubro + totalIndi}" format="##,#####0" minFractionDigits="5" maxFractionDigits="7" locale="ec"/></b>
                </td>
            </tr>
            <tr>
                <td>
                    <b>PRECIO UNITARIO $USD</b>
                </td>
                <td style="text-align: right">
                    <b><g:formatNumber number="${(totalRubro + totalIndi).toDouble().round(2)}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/></b>

                </td>
            </tr>


            </tbody>
        </table>


    </div>
    <table style="margin-top: 130px">
        <tbody>
        <div>
            <b>Nota:</b> Los cálculos se hacen con todos los decimales y el resultado final se lo redondea a dos decimales.
        </div>
        </tbody>
    </table>
</div>
</body>

</html>


%{--<%----}%
  %{--Created by IntelliJ IDEA.--}%
  %{--User: luz--}%
  %{--Date: 11/22/12--}%
  %{--Time: 12:59 PM--}%
  %{--To change this template use File | Settings | File Templates.--}%
%{----%>--}%

%{--<%@ page contentType="text/html;charset=UTF-8" %>--}%
%{--<html>--}%
%{--<head>--}%
    %{--<title>Rubro :${rubro.codigo}</title>--}%
    %{--<link href="../../css/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../css/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../font/open/stylesheet.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../css/custom.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../../css/font-awesome.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../css/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../font/open/stylesheet.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../font/tulpen/stylesheet.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../css/custom.css" rel="stylesheet" type="text/css"/>--}%
    %{--<link href="../css/font-awesome.css" rel="stylesheet" type="text/css"/>--}%
    %{--<style type="text/css">--}%
    %{--@page {--}%
        %{--size   : 21cm 29.7cm;  /*width height */--}%
        %{--margin : 2cm;--}%
    %{--}--}%
    %{--.hoja {--}%
        %{--/*background  : #e6e6fa;*/--}%
        %{--height      : 24.7cm; /*29.7-(1.5*2)*/--}%
        %{--font-family : arial;--}%
        %{--font-size   : 10px;--}%
        %{--width : 16cm;--}%
    %{--}--}%

    %{--.tituloPdf {--}%
        %{--height        : 100px;--}%
        %{--font-size     : 11px;--}%
        %{--/*font-weight   : bold;*/--}%
        %{--text-align    : center;--}%
        %{--margin-bottom : 5px;--}%
        %{--width: 95%;--}%
        %{--/*font-family       : 'Tulpen One', cursive !important;*/--}%
        %{--/*font-family : "Open Sans Condensed" !important;*/--}%
    %{--}--}%

    %{--.totales {--}%
        %{--font-weight : bold;--}%
    %{--}--}%

    %{--.num {--}%
        %{--text-align : right;--}%
    %{--}--}%

    %{--.header {--}%
        %{--background : #333333 !important;--}%
        %{--color      : #AAAAAA;--}%
    %{--}--}%

    %{--.total {--}%
        %{--background : #000000 !important;--}%
        %{--color      : #FFFFFF !important;--}%
    %{--}--}%
        %{--/*th {*/--}%
        %{--/*background : #cccccc;*/--}%
        %{--/*}*/--}%

        %{--/*tbody tr:nth-child(2n+1) {*/--}%
        %{--/*background : none repeat scroll 0 0 #E1F1F7;*/--}%
        %{--/*}*/--}%

        %{--/*tbody tr:nth-child(2n) {*/--}%
        %{--/*background : none repeat scroll 0 0 #F5F5F5;*/--}%
        %{--/*}*/--}%
    %{--thead tr{--}%
        %{--margin: 0px--}%
    %{--}--}%
    %{--th,td{--}%
        %{--font-size: 10px !important;--}%
    %{--}--}%
    %{--.row-fluid{--}%
        %{--width: 100%;--}%
        %{--height: 20px;--}%
    %{--}--}%
    %{--.span3{--}%
        %{--width: 29%;--}%
        %{--float: left;--}%
        %{--height: 100%;--}%
    %{--}--}%
    %{--.span8{--}%
        %{--width: 79%;--}%
        %{--float: left;--}%
        %{--height: 100%;--}%
    %{--}--}%
    %{--.span7{--}%
        %{--width: 69%;--}%
        %{--float: left;--}%
        %{--height: 100%;--}%
    %{--}--}%


    %{--</style>--}%
%{--</head>--}%

%{--<body>--}%
%{--<div class="hoja">--}%

    %{--<div class="tituloPdf">--}%
        %{--<p>--}%
            %{--<b>GOBIERNO DE LA PROVINCIA DE PICHINCHA </b>--}%
        %{--</p>--}%
        %{--<p>--}%
            %{--DEPARTAMENTO DE COSTOS--}%
        %{--</p>--}%
        %{--<p>--}%
            %{--ANALISIS DE PRECIOS UNITARIOS DE PRESUPUESTO--}%
        %{--</p>--}%
    %{--</div>--}%
    %{--<div style="margin-top: 20px">--}%
        %{--<div class="row-fluid">--}%
            %{--<div class="span7">--}%
                %{--<b>Fecha:</b> ${new java.util.Date().format("dd-MM-yyyy")}--}%
            %{--</div>--}%
            %{--<div class="span3">--}%
                %{--<b>Fecha Act. P.U:</b> ${ fechaPrecios?.format("dd-MM-yyyy")}--}%
            %{--</div>--}%
        %{--</div>--}%
        %{--<div class="row-fluid">--}%
            %{--<div class="span7">--}%
                %{--<b>Código:</b> ${rubro.codigo}--}%
            %{--</div>--}%
            %{--<div class="span3">--}%
                %{--<b>Unidad:</b> ${rubro.unidad.codigo}--}%
            %{--</div>--}%
        %{--</div>--}%
        %{--<div class="row-fluid">--}%
            %{--<div class="span8">--}%
                %{--<g:set var="nombre" value="${rubro.nombre.replaceAll('<','(menor)')}"></g:set>--}%
                %{--<g:set var="nombre" value="${rubro.nombre.replaceAll('<','(mayor)')}"></g:set>--}%
                %{--<b>Descripción:</b> ${nombre}--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</div>--}%
    %{--<div style="width: 100%">--}%

        %{--${tablaHer}--}%
        %{--${tablaMano}--}%
        %{--${tablaMat}--}%
        %{--${tablaTrans}--}%
        %{--${tablaIndi}--}%
        %{--<table class="table table-bordered table-striped table-condensed table-hover" style="margin-top: 40px;width: 50%;float: right;">--}%
            %{--<tbody>--}%
            %{--<tr>--}%
                %{--<td style="width: 350px;">--}%
                    %{--<b>Costo unitario directo </b>--}%
                %{--</td>--}%
                %{--<td style="text-align: right">--}%
                    %{--<g:formatNumber number="${totalRubro}" format="##,#####0" minFractionDigits="5" maxFractionDigits="7" locale="ec"/>--}%
                %{--</td>--}%
            %{--</tr>--}%
            %{--<tr>--}%
                %{--<td>--}%
                    %{--<b>Costos indirectos</b>--}%
                %{--</td>--}%
                %{--<td style="text-align: right">--}%
                    %{--<g:formatNumber number="${totalIndi}" format="##,#####0" minFractionDigits="5" maxFractionDigits="7" locale="ec"/>--}%
                %{--</td>--}%
            %{--</tr>--}%
            %{--<tr>--}%
                %{--<td>--}%
                    %{--<b>Costo total del rubro</b>--}%
                %{--</td>--}%
                %{--<td style="text-align: right">--}%
                    %{--<g:formatNumber number="${totalRubro+totalIndi}" format="##,#####0" minFractionDigits="5" maxFractionDigits="7" locale="ec"/>--}%
                %{--</td>--}%
            %{--</tr>--}%
            %{--<tr>--}%
                %{--<td>--}%
                    %{--<b>Precio unitario </b>--}%
                %{--</td>--}%
                %{--<td style="text-align: right">  <g:formatNumber number="${(totalRubro+totalIndi).toDouble().round(2)}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/>--}%

                %{--</td>--}%
            %{--</tr>--}%

            %{--</tbody>--}%
        %{--</table>--}%
    %{--</div>--}%


%{--</div>--}%
%{--</body>--}%
%{--</html>--}%