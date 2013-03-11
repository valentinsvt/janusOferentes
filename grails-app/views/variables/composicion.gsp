<%--
  Created by IntelliJ IDEA.
  User: luz
  Date: 12/19/12
  Time: 3:31 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <g:if test="${rend == 'screen'}">
            <meta name="layout" content="main">
            <script src="${resource(dir: 'js/jquery/plugins/DataTables-1.9.4/media/js', file: 'jquery.dataTables.min.js')}"></script>
            <link href="${resource(dir: 'js/jquery/plugins/DataTables-1.9.4/media/css', file: 'jquery.dataTables.css')}" rel="stylesheet">
        </g:if>
        <title>Composición de la obra</title>

        <g:if test="${rend == 'pdf'}">
            <style type="text/css">
            @page {
                size   : 21cm 29.7cm ;  /*width height */
                margin : 1.5cm;
            }

            html {
                font-family : Verdana, Arial, sans-serif;
                font-size   : 8px;
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

            .tituloChevere {
                color       : #0088CC;
                border      : 0px solid red;
                white-space : nowrap;
                display     : block;
                width       : 98%;
                height      : 30px;
                font-weight : bold;
                font-size   : 14px;
                text-shadow : -2px 2px 1px rgba(0, 0, 0, 0.25);
                margin-top  : 10px;
                line-height : 25px;
            }

            </style>
        </g:if>

    </head>

    <body>
        <div class="hoja">
            <div class="tituloChevere">Composición de ${obra?.descripcion}</div>

            <g:if test="${flash.message}">
                <div class="span12">
                    <div class="alert ${flash.clase ?: 'alert-info'}" role="status">
                        <a class="close" data-dismiss="alert" href="#">×</a>
                        ${flash.message}
                    </div>
                </div>
            </g:if>

            <g:if test="${rend == 'screen'}">
                <div class="btn-toolbar" style="margin-top: 15px;">
                    <div class="btn-group">
                        <a href="${g.createLink(controller: 'obra', action: 'registroObra', params: [obra: obra?.id])}" class="btn " title="Regresar a la obra">
                            <i class="icon-arrow-left"></i>
                            Regresar
                        </a>
                    </div>

                    <div class="btn-group" data-toggle="buttons-radio">
                        <g:link action="composicion" id="${obra?.id}" params="[tipo: -1]" class="btn btn-info toggle ${tipo.contains(',') ? 'active' : ''}">
                            <i class="icon-cogs"></i>
                            Todos
                        </g:link>
                        <g:link action="composicion" id="${obra?.id}" params="[tipo: 1]" class="btn btn-info toggle ${tipo == '1' ? 'active' : ''}">
                            <i class="icon-briefcase"></i>
                            Materiales
                        </g:link>
                        <g:link action="composicion" id="${obra?.id}" params="[tipo: 2]" class="btn btn-info toggle ${tipo == '2' ? 'active' : ''}">
                            <i class="icon-group"></i>
                            Mano de obra
                        </g:link>
                        <g:link action="composicion" id="${obra?.id}" params="[tipo: 3]" class="btn btn-info toggle ${tipo == '3' ? 'active' : ''}">
                            <i class="icon-truck"></i>
                            Equipos
                        </g:link>
                    </div>

                    <div class="btn-group">
                        <g:link action="composicion" id="${obra?.id}" params="[tipo: tipo, rend: 'pdf']" class="btn btn-print btnPdf">
                            <i class="icon-print"></i>
                            Pdf
                        </g:link>
                        %{--<g:link action="composicion" id="${obra.id}" params="[tipo: tipo, rend: 'xls']" class="btn btn-print btnExcel"> </g:link>--}%
                        <g:link controller="reportes2" action="reporteExcelComposicion" class="btn btn-print btnExcel" id="${obra?.id}"><i class="icon-table"></i>Excel</g:link>
                    </div>
                </div>
            </g:if>

            <div class="body">
                <table class="table table-bordered table-condensed table-hover table-striped" id="tbl">
                    <thead>
                        <tr>
                            <g:if test="${tipo.contains(",") || tipo == '1'}">
                                <th>Código</th>
                                <th>Item</th>
                                <th>U</th>
                                <th>Cantidad</th>
                                <th>P. Unitario</th>
                                <th>Transporte</th>
                                <th>Costo</th>
                                <th>Total</th>
                                <g:if test="${tipo.contains(",")}">
                                    <th>Tipo</th>
                                </g:if>
                            </g:if>
                            <g:elseif test="${tipo == '2'}">
                                <th>Código</th>
                                <th>Mano de obra</th>
                                <th>U</th>
                                <th>Horas hombre</th>
                                <th>Sal. / hora</th>
                                <th>Costo</th>
                                <th>Total</th>
                            </g:elseif>
                            <g:elseif test="${tipo == '3'}">
                                <th>Código</th>
                                <th>Equipo</th>
                                <th>U</th>
                                <th>Cantidad</th>
                                <th>Tarifa</th>
                                <th>Costo</th>
                                <th>Total</th>
                            </g:elseif>
                        </tr>
                    </thead>
                    <tbody>
                        <g:set var="totalEquipo" value="${0}"/>
                        <g:set var="totalMano" value="${0}"/>
                        <g:set var="totalMaterial" value="${0}"/>
                        <g:each in="${res}" var="r">
                            <tr>
                                <td>${r.codigo}</td>
                                <td>${r.item}</td>
                                <td>${r.unidad}</td>
                                <td class="numero">
                                    <g:formatNumber number="${r.cantidad}" minFractionDigits="3" maxFractionDigits="3" format="##,###0" locale="ec"/>
                                </td>
                                <td class="numero">
                                    <g:formatNumber number="${r.punitario}" minFractionDigits="3" maxFractionDigits="3" format="##,###0" locale="ec"/>
                                </td>
                                <g:if test="${tipo.contains(",") || tipo == '1'}">
                                    <td class="numero">
                                        <g:formatNumber number="${r.transporte}" minFractionDigits="4" maxFractionDigits="4" format="##,####0" locale="ec"/>
                                    </td>
                                </g:if>
                                <td class="numero">
                                    <g:formatNumber number="${r.costo}" minFractionDigits="4" maxFractionDigits="4" format="##,####0" locale="ec"/>
                                </td>
                                <td class="numero">
                                    <g:formatNumber number="${r.total}" minFractionDigits="2" maxFractionDigits="2" format="##,##0" locale="ec"/>

                                    <g:if test="${r.grid == 1}">
                                        <g:set var="totalMaterial" value="${totalMaterial + r.total}"/>
                                    </g:if>
                                    <g:elseif test="${r.grid == 2}">
                                        <g:set var="totalMano" value="${totalMano + r.total}"/>
                                    </g:elseif>
                                    <g:elseif test="${r.grid == 3}">
                                        <g:set var="totalEquipo" value="${totalEquipo + r.total}"/>
                                    </g:elseif>

                                </td>
                                <g:if test="${tipo.contains(",")}">
                                    <td>${r.grupo}</td>
                                </g:if>
                            </tr>
                        </g:each>
                    </tbody>
                </table>

                <div style="width:100%;">
                    <table class="table table-bordered table-condensed pull-right" style="width: 20%;">
                        <tr>
                            <th>Equipos</th>
                            <td class="numero"><g:formatNumber number="${totalEquipo}" minFractionDigits="2" maxFractionDigits="2" format="##,##0" locale="ec"/></td>
                        </tr>
                        <tr>
                            <th>Mano de obra</th>
                            <td class="numero"><g:formatNumber number="${totalMano}" minFractionDigits="2" maxFractionDigits="2" format="##,##0" locale="ec"/></td>
                        </tr>
                        <tr>
                            <th>Materiales</th>
                            <td class="numero"><g:formatNumber number="${totalMaterial}" minFractionDigits="2" maxFractionDigits="2" format="##,##0" locale="ec"/></td>
                        </tr>
                        <tr>
                            <th>TOTAL DIRECTO</th>
                            <td class="numero"><g:formatNumber number="${totalEquipo + totalMano + totalMaterial}" minFractionDigits="2" maxFractionDigits="2" format="##,##0" locale="ec"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <g:if test="${rend == 'screen'}">
            <script type="text/javascript">
                $(function () {
                    $('#tbl').dataTable({
                        sScrollY        : "600px",
                        bPaginate       : false,
                        bScrollCollapse : true,
                        bFilter         : false,
                        oLanguage       : {
                            sZeroRecords : "No se encontraron datos",
                            sInfo        : "",
                            sInfoEmpty   : ""
                        }
                    });

                    $(".btn").click(function () {
                        if ($(this).hasClass("active")) {
                            return false;
                        }
                    });

                    $(".btnPdf").click(function () {
                        var url = $(this).attr("href");
                        url = url.replace("&", "W");
//                        console.log(url);

                        var actionUrl = "${createLink(controller:'pdf',action:'pdfLink')}?filename=composicion.pdf&url=" + url;
//                        console.log(actionUrl);
                        location.href = actionUrl;

                        return false;
                    });




                });
            </script>
        </g:if>

    </body>
</html>