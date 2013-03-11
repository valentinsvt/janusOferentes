<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 1/14/13
  Time: 11:49 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="janus.ejecucion.TipoPlanilla" contentType="text/html;charset=UTF-8" %>
<html>
    <head>

        <meta name="layout" content="main">
        <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'jquery.validate.min.js')}"></script>
        <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'messages_es.js')}"></script>

        <script src="${resource(dir: 'js/jquery/plugins/', file: 'jquery.livequery.js')}"></script>
        <script src="${resource(dir: 'js/jquery/plugins/box/js', file: 'jquery.luz.box.js')}"></script>
        <link href="${resource(dir: 'js/jquery/plugins/box/css', file: 'jquery.luz.box.css')}" rel="stylesheet">



        <style type="text/css">

        .formato {
            font-weight : bolder;
        }

        .titulo {
            font-size : 20px;
        }

        .error {
            background : #c17474;
        }
        </style>


        <title>Registro de Contratos</title>
    </head>

    <body>

        <div class="row">
            <div class="span12 btn-group" role="navigation" style="margin-left: 0px;width: 100%;height: 35px;">
                <button class="btn" id="btn-lista"><i class="icon-book"></i> Lista</button>
                <button class="btn" id="btn-nuevo"><i class="icon-plus"></i> Nuevo</button>
                <button class="btn" id="btn-aceptar" disabled="true"><i class="icon-ok"></i> Aceptar</button>
                <button class="btn" id="btn-cancelar"><i class="icon-undo"></i> Cancelar</button>
                <button class="btn" id="btn-borrar"><i class="icon-remove"></i> Eliminar Contrato</button>
                <button class="btn" id="btn-salir"><i class="icon-ban-circle"></i> Salir</button>
            </div>
        </div>


        <g:form class="registroContrato" name="frm-registroContrato" action="save">

            <g:hiddenField name="id" value="${contrato?.id}"/>

            <fieldset class="" style="position: relative; height: 50px; border-bottom: 1px solid black; width: 100%;">

                <div class="span12" style="margin-top: 10px" align="center">

                    <g:if test="${contrato?.codigo != null}">

                        <div class="span2 formato">Contrato N°</div>

                        <div class="span3"><g:textField name="codigo" class="codigo required" value="${contrato?.codigo}"/></div>

                        <div class="span2 formato">Memo de Distribución</div>

                        <div class="span3"><g:textField name="memo" class="memo" value="${contrato?.memo}"/></div>


                    %{--</div>--}%

                    </g:if>

                    <g:else>

                        <div class="span2 formato">Contrato N°</div>

                        <div class="span3"><g:textField name="codigo" class="codigo required" value="${contrato?.codigo}"/></div>

                        <div class="span2 formato">Memo de Distribución</div>

                        <div class="span3"><g:textField name="memo" class="memo" value="${contrato?.memo}"/></div>

                    </g:else>

                </div> <!--DSAFSD-->
            </fieldset>


            <fieldset class="" style="position: relative; padding: 10px;border-bottom: 1px solid black;">

                <p class="css-vertical-text">Contratación</p>

                <div class="linea" style="height: 85%;"></div>

            %{--<g:hiddenField name="oferta" class="oferta" value="${contrato?.oferta?.id}"/>--}%

                <g:if test="${contrato?.codigo != null}">

                    <div class="span12" align="center">

                        <div class="span2 formato">Obra</div>

                        <div class="span3"><g:textField name="obra" id="obraCodigo" class="obraCodigo required" value="${contrato?.oferta?.concurso?.obra?.codigo}" disabled="true"/></div>

                        <div class="span1 formato">Nombre</div>

                        <div class="span3"><g:textField name="nombre" class="nombreObra" value="${contrato?.oferta?.concurso?.obra?.nombre}" style="width: 400px" disabled="true"/></div>

                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">

                        <div class="span2 formato">Parroquia</div>

                        <div class="span3"><g:textField name="parroquia" class="parroquia" value="${contrato?.oferta?.concurso?.obra?.parroquia?.nombre}" disabled="true"/></div>

                        <div class="span1 formato">Cantón</div>

                        <div class="span2"><g:textField name="canton" class="canton" value="${contrato?.oferta?.concurso?.obra?.parroquia?.canton?.nombre}" disabled="true"/></div>

                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">

                        <div class="span2 formato">Clase Obra</div>

                        <div class="span3"><g:textField name="claseObra" class="claseObra" value="${contrato?.oferta?.concurso?.obra?.claseObra?.descripcion}" disabled="true"/></div>

                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">

                        <div class="span2 formato">Contratista</div>

                        <div class="span3"><g:textField name="contratista" class="contratista" value="${contrato?.oferta?.proveedor?.nombre}" disabled="true"/></div>

                    </div>

                </g:if>

                <g:else>

                    <div class="span12" style="margin-top: 5px" align="center">

                        <div class="span2 formato">Obra</div>

                        <div class="span3">
                            <input type="hidden" id="obraId" value="${contrato?.oferta?.concurso?.obra?.codigo}" name="obra.id">
                            <g:textField name="obra" id="obraCodigo" class="obraCodigo required txtBusqueda" value="${contrato?.oferta?.concurso?.obra?.codigo}"/>
                        </div>

                        <div class="span1 formato">Nombre</div>

                        <div class="span5">
                            <g:textField name="nombre" class="nombreObra" id="nombreObra" style="width: 400px" disabled="true"/>
                        </div>

                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">
                        <div class="span2 formato">Oferta</div>

                        <div class="span3" id="div_ofertas">
                            <g:select name="oferta.id" from="" noSelection="['-1': 'Seleccione una obra']" id="oferta" optionKey="id"></g:select>
                        </div>
                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">
                        <div class="span2 formato">Contratista</div>

                        <div class="span3">
                            <g:textField name="contratista" class="contratista" id="contratista" disabled="true"/>
                        </div>
                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">

                        <div class="span2 formato">Parroquia</div>

                        <div class="span3"><g:textField name="parroquia" class="parroquia" id="parr"/></div>

                        <div class="span1 formato">Cantón</div>

                        <div class="span2"><g:textField name="canton" class="canton" id="canton"/></div>

                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">

                        <div class="span2 formato">Clase Obra</div>

                        <div class="span3"><g:textField name="claseObra" class="claseObra" id="clase"/></div>

                    </div>

                    <div class="span12" style="margin-top: 5px" align="center">

                    </div>

                </g:else>

            </fieldset>

            <fieldset class="" style="position: relative; height: 150px; border-bottom: 1px solid black;padding: 10px;">

                <div class="span12" style="margin-top: 10px" align="center">

                    <div class="span2 formato">Tipo</div>

                    <div class="span3"><g:select from="${janus.pac.TipoContrato.list()}" name="tipoContrato.id" class="tipoContrato activo" value="${contrato?.tipoContratoId}" optionKey="id" optionValue="descripcion"/></div>

                    <div class="span2 formato">Fecha de Suscripción</div>

                    <div class="span2"><elm:datepicker name="fechaSuscripcion" class="fechaSuscripcion datepicker input-small activo" value="${contrato?.fechaSubscripcion}"/></div>

                </div>

                <div class="span12" style="margin-top: 5px" align="center">

                    <div class="span2 formato">Objeto del Contrato</div>

                    <div class="span3"><g:textArea name="objeto" class="activo" rows="5" cols="5" style="height: 79px; width: 800px; resize: none" value="${contrato?.objeto}"/></div>

                </div>

            </fieldset>

            <fieldset class="" style="position: relative; height: 190px; padding: 10px;border-bottom: 1px solid black;">

                <div class="span12" style="margin-top: 10px">

                    <div class="span2 formato">Multa por retraso</div>

                    <div class="span3">
                        <g:textField name="multaRetraso" class="number" style="width: 50px"
                                     value="${g.formatNumber(number: contrato?.multaRetraso, maxFractionDigits: 0, minFractionDigits: 0, format: '##,##0', locale: 'ec')}"/>&#8240;
                    </div>

                    <div class="span2 formato">Multa por no presentación de planilla</div>

                    <div class="span3">
                        <g:textField name="multaPlanilla" class="number" style="width: 50px"
                                     value="${g.formatNumber(number: contrato?.multaPlanilla, maxFractionDigits: 0, minFractionDigits: 0, format: '##,##0', locale: 'ec')}"/>&#8240;
                    </div>

                </div>

                <div class="span12" style="margin-top: 10px">

                    <div class="span2 formato">Monto</div>

                    <div class="span3"><g:textField name="monto" class="monto activo"
                                                    value="${g.formatNumber(number: contrato?.monto, maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"/></div>

                    <div class="span2 formato">Plazo</div>

                    <div class="span3"><g:textField name="plazo" class="plazo activo" style="width: 50px" maxlength="4"
                                                    value="${g.formatNumber(number: contrato?.plazo, maxFractionDigits: 0, minFractionDigits: 0, format: '##,##0', locale: 'ec')}"/> Días</div>

                </div>

                <div class="span12" style="margin-top: 10px">

                    <div class="span2 formato">Anticipo</div>

                    <div class="span1">
                        <g:textField name="porcentajeAnticipo" class="anticipo activo"
                                     value="${g.formatNumber(number: contrato?.porcentajeAnticipo, maxFractionDigits: 0, minFractionDigits: 0, format: '##,##0', locale: 'ec')}"
                                     style="width: 30px; text-align: right"/> %
                    </div>

                    <div class="span2">
                        <g:textField name="anticipo" class="anticipoValor activo" style="width: 105px; text-align: right"
                                     value="${g.formatNumber(number: contrato?.anticipo, maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"/>
                    </div>

                    <div class="span2 formato">Indices de la Oferta</div>

                    <div class="span3"><g:select name="periodoValidez.id" from="${janus.pac.PeriodoValidez.list()}" class="indiceOferta activo" value="${contrato?.periodoValidez?.id}" optionValue="descripcion" optionKey="id"/></div>

                </div>


                <div class="span12" style="margin-top: 10px">

                    <div class="span2 formato">Financiamiento</div>

                    <div class="span3"><g:textField name="financiamiento" class="financiamiento activo" value="${contrato?.financiamiento}"/></div>

                    <div class="span2 formato">Financiado Por</div>

                    <div class="span3"><g:textField name="financiadoPor" class="financiadoPor activo"/></div>

                </div>

            </fieldset>

        </g:form>



        <g:if test="${contrato}">
            <div class="navbar navbar-inverse" style="margin-top: 20px;padding-left: 5px;" align="center">

                <div class="navbar-inner">
                    <div class="botones">

                        <ul class="nav">
                            <li>
                            %{--<g:link controller="garantia" action="garantiasContrato" id="">--}%
                            %{--<i class="icon-pencil"></i>Garantías--}%
                            %{--</g:link>--}%
                            %{--<a href="#"><i class="icon-pencil"></i> Garantías</a>--}%
                                <g:link controller="garantia" action="garantiasContrato" id="${contrato?.id}">
                                    <i class="icon-pencil"></i> Garantías
                                </g:link>

                            </li>
                            %{--<li><a href="${g.createLink(controller: 'volumenObra', action: 'volObra', id: obra?.id)}"><i class="icon-list-alt"></i>Vol. Obra--}%
                            %{--</a></li>--}%
                            <li>
                            %{--<a href="#" id="btnCronograma">--}%
                                <g:link controller="cronogramaContrato" action="index" id="${contrato?.id}">
                                    <i class="icon-th"></i>Cronograma contrato
                                </g:link>
                            %{--</a>--}%
                            </li>
                            <g:if test="${janus.ejecucion.Planilla.countByContratoAndTipoPlanilla(contrato, TipoPlanilla.findByCodigo('A')) > 0 && contrato.oferta.concurso.obra.fechaInicio}">
                                <li>
                                    <g:link controller="cronogramaEjecucion" action="index" id="${contrato?.id}">
                                        <i class="icon-th"></i>Cronograma ejecucion
                                    </g:link>
                                </li>
                            </g:if>
                        %{--<li>--}%
                        %{--<g:link controller="formulaPolinomica" action="coeficientes" id="${obra?.id}">--}%
                        %{--Fórmula Pol.--}%
                        %{--</g:link>--}%
                        %{--</li>--}%
                        %{--<li><a href="#" id="btnFormula"><i class="icon-file"></i>F. Polinómica</a></li>--}%
                            <li>
                                <a href="${g.createLink(controller: 'contrato', action: 'polinomicaContrato', id: contrato?.id)}">
                                    <i class="icon-calendar"></i> F. Polinómica
                                </a>
                            </li>

                            <li>
                                <g:link controller="documentoProceso" action="list" id="${contrato?.oferta?.concursoId}" params="[contrato: contrato?.id]">
                                    <i class="icon-book"></i>Biblioteca
                                </g:link>
                            </li>

                            <li>
                                <g:link controller="planilla" action="list" id="${contrato?.id}">
                                    <i class=" icon-file-alt"></i>Planillas
                                </g:link>
                            </li>

                        </ul>

                    </div>
                </div>

            </div>
        </g:if>


        <div class="modal hide fade mediumModal" id="modal-var" style="overflow: hidden">
            <div class="modal-header btn-primary">
                <button type="button" class="close" data-dismiss="modal">x</button>

                <h3 id="modal_tittle_var">

                </h3>

            </div>

            <div class="modal-body" id="modal_body_var">

            </div>

            <div class="modal-footer" id="modal_footer_var">

            </div>

        </div>

        <div class="modal grandote hide fade" id="modal-busqueda" style="overflow: hidden">
            <div class="modal-header btn-info">
                <button type="button" class="close" data-dismiss="modal">x</button>

                <h3 id="modalTitle_busqueda"></h3>

            </div>

            <div class="modal-body" id="modalBody">
                <bsc:buscador name="contratos" value="" accion="buscarContrato" controlador="contrato" campos="${campos}" label="Contrato" tipo="lista"/>

            </div>

            <div class="modal-footer" id="modalFooter_busqueda">

            </div>

        </div>



    </div>


        <div id="borrarContrato">

            <fieldset>
                <div class="span3">
                    Está seguro de que desea borrar el contrato: <div style="font-weight: bold;">${contrato?.codigo} ?</div>

                </div>
            </fieldset>
        </div>

        <script type="text/javascript">

            function updateAnticipo() {
                var porcentaje = $("#porcentajeAnticipo").val();
                var monto = $("#monto").val().replace(",", "");
                var anticipoValor = (porcentaje * (monto)) / 100;
                $("#anticipo").val(number_format(anticipoValor, 2, ".", ","));
            }

            $("#frm-registroContrato").validate();

            function validarNum(ev) {
                /*
                 48-57      -> numeros
                 96-105     -> teclado numerico
                 188        -> , (coma)
                 190        -> . (punto) teclado
                 110        -> . (punto) teclado numerico
                 8          -> backspace
                 46         -> delete
                 9          -> tab
                 37         -> flecha izq
                 39         -> flecha der
                 */
                return ((ev.keyCode >= 48 && ev.keyCode <= 57) ||
                        (ev.keyCode >= 96 && ev.keyCode <= 105) ||
                        ev.keyCode == 190 || ev.keyCode == 110 ||
                        ev.keyCode == 8 || ev.keyCode == 46 || ev.keyCode == 9 ||
                        ev.keyCode == 37 || ev.keyCode == 39);
            }
            function validarInt(ev) {
                /*
                 48-57      -> numeros
                 96-105     -> teclado numerico
                 188        -> , (coma)
                 190        -> . (punto) teclado
                 110        -> . (punto) teclado numerico
                 8          -> backspace
                 46         -> delete
                 9          -> tab
                 37         -> flecha izq
                 39         -> flecha der
                 */
                return ((ev.keyCode >= 48 && ev.keyCode <= 57) ||
                        (ev.keyCode >= 96 && ev.keyCode <= 105) ||
                        ev.keyCode == 8 || ev.keyCode == 46 || ev.keyCode == 9 ||
                        ev.keyCode == 37 || ev.keyCode == 39);
            }

            $(".number").keydown(function (ev) {
                return validarInt(ev);
            });

            $("#plazo").keydown(function (ev) {

                return validarInt(ev);

            }).keyup(function () {

                        var enteros = $(this).val();

                    });

            $("#monto").keydown(function (ev) {

                return validarNum(ev);

            }).keyup(function () {

                        var enteros = $(this).val();

                    });

            $("#porcentajeAnticipo").keydown(function (ev) {

                return validarNum(ev);

            }).keyup(function () {

                        var enteros = $(this).val();

                        if (parseFloat(enteros) > 100) {

                            $(this).val(100)

                        }
                        updateAnticipo();

                    });

            $("#anticipo").keydown(function (ev) {

                return validarNum(ev);

            }).keyup(function () {

                        var enteros = $(this).val();
                        updateAnticipo();
//                        var porcentaje = $("#porcentajeAnticipo").val();
//
//                        var monto = $("#monto").val();
//
//                        var anticipoValor = (porcentaje * (monto)) / 100;
//
//                        $("#anticipo").val(number_format(anticipoValor, 2, ".", ""));

                    }).click(function () {
                        updateAnticipo();
//                        var porcentaje = $("#porcentajeAnticipo").val();
//
//                        var monto = $("#monto").val();
//
//                        var anticipoValor = (porcentaje * (monto)) / 100;
//
//                        $("#anticipo").val(number_format(anticipoValor, 2, ".", ","));

                    });

            $("#financiamiento").keydown(function (ev) {

                return validarNum(ev);

            }).keyup(function () {

                        var enteros = $(this).val();

                    });

            $("#codigo").click(function () {

                $("#btn-aceptar").attr("disabled", false)

            });

            $("#objeto").click(function () {

                $("#btn-aceptar").attr("disabled", false)

            });

            $("#tipoContrato").change(function () {

                $("#btn-aceptar").attr("disabled", false)

            });

            $("#monto").click(function () {

                $("#btn-aceptar").attr("disabled", false)

            });

            $("#financiamiento").click(function () {

                $("#btn-aceptar").attr("disabled", false)

            });

            function enviarObra() {
                var data = "";
                $("#buscarDialog").hide();
                $("#spinner").show();
                $(".crit").each(function () {
                    data += "&campos=" + $(this).attr("campo");
                    data += "&operadores=" + $(this).attr("operador");
                    data += "&criterios=" + $(this).attr("criterio");
                });
                if (data.length < 2) {
                    data = "tc=" + $("#tipoCampo").val() + "&campos=" + $("#campo :selected").val() + "&operadores=" + $("#operador :selected").val() + "&criterios=" + $("#criterio").val()
                }
                data += "&ordenado=" + $("#campoOrdn :selected").val() + "&orden=" + $("#orden :selected").val();
                $.ajax({type : "POST", url : "${g.createLink(controller: 'contrato',action:'buscarObra')}",
                    data     : data,
                    success  : function (msg) {
                        $("#spinner").hide();
                        $("#buscarDialog").show();
                        $(".contenidoBuscador").html(msg).show("slide");
                    }
                });

            }

            function cargarCombo() {
                if ($("#obraId").val() * 1 > 0) {
                    $.ajax({
                        type    : "POST", url : "${g.createLink(controller: 'contrato',action:'cargarOfertas')}",
                        data    : "id=" + $("#obraId").val(),
                        success : function (msg) {
                            $("#div_ofertas").html(msg)
                        }
                    });

                }

            }

            function cargarCanton() {
                if ($("#obraId").val() * 1 > 0) {
                    $.ajax({
                        type    : "POST", url : "${g.createLink(controller: 'contrato',action:'cargarCanton')}",
                        data    : "id=" + $("#obraId").val(),
                        success : function (msg) {
                            $("#canton").val(msg)
                        }
                    });

                }
            }

            $("#obraCodigo").dblclick(function () {
                var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cerrar</a>');
                $("#modalTitle_busqueda").html("Lista de obras");
                $("#modalFooter_busqueda").html("").append(btnOk);
                $("#modal-busqueda").modal("show");
                $("#contenidoBuscador").html("")
                $("#buscarDialog").unbind("click")
                $("#buscarDialog").bind("click", enviarObra)

            });

            $("#btn-lista").click(function () {

                $("#btn-cancelar").attr("disabled", true);
//        $("#btn-aceptar").attr("disabled", true);

                var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cerrar</a>');
                $("#modalTitle_busqueda").html("Lista de Contratos");
                $("#modalFooter_busqueda").html("").append(btnOk);
                $("#buscarDialog").unbind("click")
                $("#buscarDialog").bind("click", enviar)
                $("#contenidoBuscador").html("")
                $("#modal-busqueda").modal("show");

            });

            $("#btn-nuevo").click(function () {

                location.href = "${createLink(action: 'registroContrato')}"
            });

            $("#obraCodigo").focus(function () {

                var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cerrar</a>');
                $("#modalTitle_busquedaOferta").html("Lista de Obras");
                $("#modalFooter_busquedaOferta").html("").append(btnOk);
                $("#modal-busquedaOferta").modal("show");

            });

            if (${contrato?.codigo != null}) {

                $(".activo").focus(function () {

                    $("#btn-aceptar").attr("disabled", false)

                })

            }

            $("#btn-salir").click(function () {

                location.href = "${g.createLink(action: 'index', controller: "inicio")}";

            });

            $("#btn-aceptar").click(function () {

                $("#frm-registroContrato").submit();

            });

            $("#btn-cancelar").click(function () {

                if (${contrato?.id == null}) {

                    location.href = "${g.createLink(action: 'registroContrato')}";

                } else {

                    location.href = "${g.createLink(action: 'registroContrato')}" + "?contrato=" + "${contrato?.id}";

                }

            })

            $("#btn-borrar").click(function () {

                if (${contrato?.codigo != null}) {

                    $("#borrarContrato").dialog("open")

                }

            });

            $("#borrarContrato").dialog({

                autoOpen  : false,
                resizable : false,
                modal     : true,
                draggable : false,
                width     : 350,
                height    : 220,
                position  : 'center',
                title     : 'Eliminar Contrato',
                buttons   : {
                    "Aceptar"  : function () {

                        $.ajax({
                            type    : "POST",
                            url     : "${createLink(action: 'delete')}",
                            data    : "id=${contrato?.id}",
                            success : function (msg) {

                                $("#borrarContrato").dialog("close");
                                location.href = "${g.createLink(action: 'registroContrato')}";
                            }
                        });
//

//
                    },
                    "Cancelar" : function () {
                        $("#borrarContrato").dialog("close");
                    }
                }

            });


        </script>

    </body>
</html>