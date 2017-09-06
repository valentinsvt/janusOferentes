<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 11/27/12
  Time: 11:54 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
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
        font-weight: bolder;
    }

    .titulo {
        font-size: 20px;
    }

    .error {
        background: #c17474;
    }

    .bold {
        font-weight: bold;
    }
    </style>

    <title>Registro de Obras</title>
</head>

<body>
%{--Todo Por hacer: imprimir, Formula pol, Fp liquidacion, rubros , documentos, composicion, tramites--}%
<g:if test="${flash.message}">
    <div class="row">
        <div class="span12" style="height: 35px;margin-bottom: 10px;">
            <div class="alert ${flash.clase ?: 'alert-info'}" role="status">
                <a class="close" data-dismiss="alert" href="#">×</a>
                ${flash.message}
            </div>
        </div>
    </div>
</g:if>

<div class="row">
    <div class="span12 btn-group" role="navigation" style="margin-bottom: 15px;">
        <button class="btn" id="lista"><i class="icon-book"></i> Lista</button>
        <g:if test="${obra?.id != null}">
            <button class="btn" id="btnImprimir"><i class="icon-print"></i> Imprimir</button>

            <g:if test="${obra?.estado == 'N'}">

                <button class="btn" id="cambiarEstado"><i class="icon-refresh"></i> Cambiar Estado</button>

            </g:if>

        </g:if>
    </div>
</div>

<g:form class="form-horizontal" name="frmSave-Lugar" action="save">
<fieldset class="borde" style="padding: 10px;">
<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Código
                </span>
            </div>

            <div class="controls">
                ${obra?.codigo}
            </div>
        </div>
    </div>

    <div class="span6">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Nombre
                </span>
            </div>

            <div class="controls">
                ${obra?.nombre}
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span10">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Descripción
                </span>
            </div>

            <div class="controls">
                ${obra?.descripcion}
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Programa
                </span>
            </div>

            <div class="controls">
                ${obra?.programacion?.descripcion}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Tipo
                </span>
            </div>

            <div class="controls">
                ${obra?.tipoObjetivo?.descripcion}
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Clase
                </span>
            </div>

            <div class="controls">
                ${obra?.claseObra?.descripcion}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Referencias
                </span>
            </div>

            <div class="controls">
                ${obra?.referencia}
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Cantón
                </span>
            </div>

            <div class="controls">
                ${obra?.comunidad?.parroquia?.canton?.nombre}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Parroquia
                </span>
            </div>

            <div class="controls">
                ${obra?.comunidad?.parroquia?.nombre}
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Comunidad
                </span>
            </div>

            <div class="controls">
                ${obra?.comunidad?.nombre}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Sitio
                </span>
            </div>

            <div class="controls">
                ${obra?.sitio}
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Barrio
                </span>
            </div>

            <div class="controls">
                ${obra?.barrio}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Fecha
                </span>
            </div>

            <div class="controls">
                <g:formatDate date="${obra?.fechaPreciosRubros}" format="dd-MM-yyyy"/>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Plazo
                </span>
            </div>

            <div class="controls">
                ${obra?.plazoEjecucionMeses} Mes${obra?.plazoEjecucionMeses == 1 ? '' : 'es'}
                ${obra?.plazoEjecucionDias} Día${obra?.plazoEjecucionDias == 1 ? '' : 's'}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Anticipo
                </span>
            </div>

            <div class="controls">
                ${obra?.porcentajeAnticipo}%
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Coordenadas
                </span>
            </div>

            <div class="controls">
                ${obra?.coordenadas}
            </div>
        </div>
    </div>

    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Registrada:
                </span>
            </div>

            <div class="controls" style="font-weight:bold ">
                ${obra?.estado == "N" ? "Acepta modificaciones" : "Registrado"}
                %{--${obra?.estado}--}%
            </div>
        </div>
    </div>


</div>

%{--
<div class="row">
    <div class="span5">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Precios para MO y Equipos
                </span>
            </div>

            <div class="controls">
                ${obra?.listaManoObra?.descripcion}
            </div>
        </div>
    </div>
</div>
--}%


<div class="row">
    <div class="span10">
        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Observaciones
                </span>
            </div>

            <div class="controls">
                ${obra?.observaciones}
            </div>
        </div>
    </div>
</div>
</fieldset>
</g:form>


<div id="busqueda" class="hide">

    <fieldset class="borde">
        <div class="span7">

            <div class="span2">Buscar Por</div>

            <div class="span2">Criterio</div>

            <div class="span1">Ordenar</div>

        </div>

        <div>
            <div class="span2"><g:select name="buscarPor" class="buscarPor"
                                         from="['1': 'Provincia', '2': 'Cantón', '3': 'Parroquia', '4': 'Comunidad']"
                                         style="width: 120px" optionKey="key"
                                         optionValue="value"/></div>

            <div class="span2" style="margin-left: -20px"><g:textField name="criterio" class="criterio"/></div>

            <div class="span1"><g:select name="ordenar" class="ordenar" from="['1': 'Ascendente', '2': 'Descendente']"
                                         style="width: 120px; margin-left: 60px;" optionKey="key"
                                         optionValue="value"/></div>

            <div class="span2" style="margin-left: 140px"><button class="btn btn-info" id="btn-consultar"><i
                    class="icon-check"></i> Consultar
            </button></div>

        </div>
    </fieldset>

    <fieldset class="borde">

        <div id="divTabla" style="height: 460px; overflow-y:auto; overflow-x: auto;">

        </div>

    </fieldset>

</div>

<g:if test="${obra?.id}">
    <div class="navbar navbar-inverse" style="margin-top: 10px;padding-left: 5px;float: left; width: 100%;"
         align="center">

        <div class="navbar-inner">
            <div class="botones">

                <ul class="nav">
                    <li><a href="#" id="btnVar"><i class="icon-pencil"></i>Variables</a></li>
                    <li><a href="${g.createLink(controller: 'volumenObra', action: 'volObra', id: obra?.id)}"><i
                            class="icon-list-alt"></i>Vol. Obra
                    </a></li>
                    <li><a href="#" id="matriz"><i class="icon-th"></i>Matriz FP</a></li>
                    <li>
                        %{--<g:link controller="formulaPolinomica" action="coeficientes" id="${obra?.id}" class="btnFormula">--}%
                        <g:link controller="formulaPolinomica" action="insertarVolumenesItem" class="btnFormula"
                                params="[obra: obra?.id]" title="Coeficientes">
                            Fórmula Pol.
                        </g:link>
                    </li>
                    %{--<li><a href="#">FP Liquidación</a></li>--}%
                    <li><a href="#" id="btnRubros"><i class="icon-money"></i>Rubros</a></li>
                    %{--<li><a href="#" id="btnDocumentos"><i class="icon-file"></i>Documentos</a></li>--}%
                    %{--<li><a href="${g.createLink(controller: 'documentosObra', action: 'documentosObra', id: obra?.id)}" id="btnDocumentos"><i class="icon-file"></i>Documentos</a></li>--}%
                    <li><a href="${g.createLink(controller: 'cronograma', action: 'cronogramaObra', id: obra?.id)}"><i
                            class="icon-calendar"></i>Cronograma
                    </a></li>
                    <li>
                        <g:link controller="variables" action="composicion" id="${obra?.id}"><i
                                class="icon-paste"></i>Composición
                        </g:link>
                    </li>
                    %{--<li>--}%
                    %{--<g:link controller="documentoObra" action="list" id="${obra.id}">--}%
                    %{--<i class="icon-book"></i>Biblioteca--}%
                    %{--</g:link>--}%
                    %{--</li>--}%
                    <li>
                        <a href="#" id="btnMapa"><i class="icon-flag"></i>Mapa</a>
                    </li>

                </ul>

            </div>
        </div>

    </div>
</g:if>


<div class="modal hide fade" id="modal-var" style=";overflow: hidden;">
    <div class="modal-header btn-primary">
        <button type="button" class="close" data-dismiss="modal">×</button>

        <h3 id="modal_title_var">
        </h3>
    </div>

    <div class="modal-body" id="modal_body_var">

    </div>

    <div class="modal-footer" id="modal_footer_var">
    </div>
</div>


<div class="modal hide fade mediumModal" id="modal-TipoObra" style=";overflow: hidden;">
    <div class="modal-header btn-primary">
        <button type="button" class="close" data-dismiss="modal">×</button>

        <h3 id="modalTitle_tipo">
        </h3>
    </div>

    <div class="modal-body" id="modalBody_tipo">

    </div>

    <div class="modal-footer" id="modalFooter_tipo">
    </div>
</div>


<div class="modal grandote hide fade " id="modal-busqueda" style=";overflow: hidden;">
    <div class="modal-header btn-info">
        <button type="button" class="close" data-dismiss="modal">×</button>

        <h3 id="modalTitle_busqueda"></h3>
    </div>

    <div class="modal-body" id="modalBody">
        <bsc:buscador name="obra?.buscador.id" value="" accion="buscarObra" controlador="obra" campos="${campos}"
                      label="Obra" tipo="lista"/>
    </div>

    <div class="modal-footer" id="modalFooter_busqueda">
    </div>
</div>


<div id="estadoDialog">

    <fieldset>
        <div class="span4">
            Está seguro de querer cambiar el estado de la obra:<div style="font-weight: bold;">${obra?.nombre} ?

        </div>
            <br>
            <span style="color: red">
                Una vez registrada los datos de la obra no podrán ser editados.
            </span>

        </div>
    </fieldset>
</div>



<g:if test="${obra}">
    <div class="modal hide fade mediumModal" id="modal-matriz" style=";overflow: hidden;">
        <div class="modal-header btn-primary">
            <button type="button" class="close" data-dismiss="modal">×</button>

            <h3 id="modal_title_matriz">
            </h3>
        </div>

        <div class="modal-body" id="modal_body_matriz">
            <div id="msg_matriz">
                <g:if test="${obra.desgloseTransporte == 'S'}">
                    <p style="font-size: 14px; text-align: center;">Ya existe una matriz generada
                    </p>
                </g:if>
                <g:else>
                    <g:if test="${obra.desgloseTransporte == 'N'}">
                        <p style="font-size: 14px; text-align: center;">Ya existe una matriz generada
                        </p>
                    </g:if>
                </g:else>
                <p>Desea generar la matriz? Esta acción podria tomar varios minutos</p>
                <a href="#" class="btn btn-info" id="no">No</a>
                <a href="#" class="btn btn-danger" id="si">Si</a>
                <a href="#" class="btn btn-info" data-dismiss="modal" id="cancela" style="margin-left: 400px;">Cancelar</a>

            </div>

            <div id="datos_matriz" style="text-align: center">
%{--
                <span>Seleccione el subpresupuesto:</span>
                <g:select name="mtariz_sub" from="${subs}" noSelection="['0': 'Todos los subpresupuestos']" optionKey="id" optionValue="descripcion" style="margin-right: 20px"></g:select>
                <p>Generar con desglose de Transporte <input type="checkbox" id="si_trans" style="margin-top: -3px" checked="true">
                </p>
--}%
                <p>Haga clic en el botón Generar para iniciar el proceso</p>
                <a href="#" class="btn btn-success" id="ok_matiz">Generar</a>
            </div>
        </div>

    </div>
</g:if>

<script type="text/javascript">

    $("#frm-registroObra").validate();

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
                ev.keyCode == 8 || ev.keyCode == 46 || ev.keyCode == 9 ||
                ev.keyCode == 37 || ev.keyCode == 39);
    }

    $("#porcentajeAnticipo").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function () {

                var enteros = $(this).val();

                if (parseFloat(enteros) > 100) {

                    $(this).val(100)

                }

            });

    $("#plazo").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function () {

                var enteros = $(this).val();

            });

    $("#latitud").bind({
        keydown: function (ev) {
            // esta parte valida el punto: si empieza con punto le pone un 0 delante, si ya hay un punto lo ignora
            if (ev.keyCode == 190 || ev.keyCode == 110) {
                var val = $(this).val();
                if (val.length == 0) {
                    $(this).val("0");
                }
                return val.indexOf(".") == -1;
            } else {
                // esta parte valida q sean solo numeros, punto, tab, backspace, delete o flechas izq/der
                return validarNum(ev);
            }
        }, //keydown
        keyup: function () {
            var val = $(this).val();
            // esta parte valida q no ingrese mas de 2 decimales
            var parts = val.split(".");
            if (parts.length > 1) {
                if (parts[1].length > 5) {
                    parts[1] = parts[1].substring(0, 5);
                    val = parts[0] + "." + parts[1];
                    $(this).val(val);
                }
            }

        }

    });

    $("#longitud").bind({
        keydown: function (ev) {
            // esta parte valida el punto: si empieza con punto le pone un 0 delante, si ya hay un punto lo ignora
            if (ev.keyCode == 190 || ev.keyCode == 110) {
                var val = $(this).val();
                if (val.length == 0) {
                    $(this).val("0");
                }
                return val.indexOf(".") == -1;
            } else {
                // esta parte valida q sean solo numeros, punto, tab, backspace, delete o flechas izq/der
                return validarNum(ev);
            }
        }, //keydown
        keyup: function () {
            var val = $(this).val();
            // esta parte valida q no ingrese mas de 2 decimales
            var parts = val.split(".");
            if (parts.length > 1) {
                if (parts[1].length > 5) {
                    parts[1] = parts[1].substring(0, 5);
                    val = parts[0] + "." + parts[1];
                    $(this).val(val);
                }
            }

        }

    });

    $(function () {
        <g:if test="${obra}">

        $(".plazo").blur(function () {
            var $m = $("#plazoEjecucionMeses");
            var $d = $("#plazoEjecucionDias");

            var valM = $m.val();
            var oriM = $m.data("original");

            var valD = $d.val();
            var oriD = $d.data("original");

            if (parseFloat(valM) == parseFloat(oriM) && parseFloat(valD) == parseFloat(oriD)) {
                $("#crono").val(0);
            } else {
                $.box({
                    imageClass: "box_info",
                    text: "Si cambia el plazo de la obra y guarda se eliminará el cronograma.<br/>Desea continuar?",
                    title: "Confirmación",
                    iconClose: false,
                    dialog: {
                        resizable: false,
                        draggable: false,
                        buttons: {
                            "Cancelar": function () {
                                $m.val(oriM);
                                $d.val(oriD);
                            },
                            "Sí": function () {
                                $("#crono").val(1);
                            },
                            "No": function () {
                                $m.val(oriM);
                                $d.val(oriD);
                            }
                        }
                    }
                });
            }
        });

        $("#matriz").click(function () {
            $("#modal_title_matriz").html("Generar matriz");
            $("#datos_matriz").hide();
            $("#msg_matriz").show();
            $("#modal-matriz").modal("show")

        });
        $("#no").click(function () {
            location.href = "${g.createLink(controller: 'matriz',action: 'pantallaMatriz',id: obra?.id)}"
        });
        $("#si").click(function () {
            $("#datos_matriz").show();
            $("#msg_matriz").hide()
        });

        $("#ok_matiz").click(function () {
            var sp = $("#mtariz_sub").val();
            var tr = $("#si_trans").attr("checked");
//            console.log(sp,tr)
//                    if (sp != "-1")

            $("#dlgLoad").dialog("open");

            $.ajax({
                type: "POST",
                url: "${createLink(action: 'validaciones',controller: 'obraFP')}",
                data: "obra=${obra.id}&sub=" + sp + "&trans=" + tr,
                success: function (msg) {
                    $("#dlgLoad").dialog("close");
                    $("#modal-matriz").modal("hide")
                    if (msg != "ok") {
                        $.box({
                            imageClass: "box_info",
                            text: msg,
                            title: "Errores",
                            iconClose: false,
                            dialog: {
                                resizable: false,
                                draggable: false,
                                width: 900,
                                buttons: {
                                    "Aceptar": function () {
                                    }
                                }
                            }
                        });
                    } else {
                        location.href = "${g.createLink(controller: 'matriz',action: 'pantallaMatriz',params:[id:obra.id,inicio:0,limit:40])}"
                    }
                },
                error: function () {
                    $("#dlgLoad").dialog("close");
                    $("#modal-matriz").modal("hide");
                    $.box({
                        imageClass: "box_info",
                        text: "Ha ocurrido un error interno, comuniquese con el administrador del sistema.",
                        title: "Errores",
                        iconClose: false,
                        dialog: {
                            resizable: false,
                            draggable: false,
                            width: 700,
                            buttons: {
                                "Aceptar": function () {
                                }
                            }
                        }
                    });
                }
            });
            %{--$.ajax({--}%
            %{--type    : "POST",--}%
            %{--url     : "${createLink(action: 'matrizFP',controller: 'obraFP')}",--}%
            %{--data    : "obra=${obra.id}&sub=" + sp + "&trans=" + tr,--}%
            %{--success : function (msg) {--}%
            %{--$("#dlgLoad").dialog("close");--}%
            %{--location.href = "${g.createLink(controller: 'matriz',action: 'pantallaMatriz',params:[id:obra.id,inicio:0,limit:40])}"--}%
            %{--}--}%
            %{--});--}%
        });
        </g:if>
        $("#lista").click(function () {
            var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cerrar</a>');
            $("#modalTitle_busqueda").html("Lista de obras");
//        $("#modalBody").html($("#buscador_rubro").html());
            $("#modalFooter_busqueda").html("").append(btnOk);
            $(".contenidoBuscador").html("");
            $("#modal-busqueda").modal("show");
        });

        $("#nuevo").click(function () {
            location.href = "${g.createLink(action: 'registroObra')}";
        });

        $("#cancelarObra").click(function () {
            location.href = "${g.createLink(action: 'registroObra')}" + "?obra=" + "${obra?.id}";
        });

        $("#eliminarObra").click(function () {
            if (${obra?.id != null}) {
                $("#eliminarObraDialog").dialog("open");
            }

        });

        $("#cambiarEstado").click(function () {
            if (${obra?.id != null}) {
                $("#estadoDialog").dialog("open")
            }
        });

        $("#btnDocumentos").click(function () {

            if (${obra?.estado == 'R'}) {
                $("#dlgLoad").dialog("open");
                location.href = "${g.createLink(controller: 'documentosObra', action: 'documentosObra', id: obra?.id)}"
            }
            else {
                $("#documentosDialog").dialog("open")
            }
        });

        $("#btnMapa").click(function () {
            location.href = "${g.createLink(action: 'mapaObra', id: obra?.id)}"
        });

        $("#btn-aceptar").click(function () {
            $("#frm-registroObra").submit();
        });

        $("#btn-buscar").click(function () {
            $("#dlgLoad").dialog("close");
            $("#busqueda").dialog("open");
            return false;
//
        });
        $("#copiarObra").click(function () {
            $("#copiarDialog").dialog("open");
        });

        $("#btnRubros").click(function () {
            var url = "${createLink(controller:'reportes', action:'imprimirRubros')}?oferente=${session.usuario.id}&obra=${obra?.id}&transporte=";
            var urlVae= "${createLink(controller:'reportes', action:'imprimirRubrosVae')}?oferente=${session.usuario.id}&obra=${obra?.id}&transporte=";


            $.box({
                imageClass: "box_info",
                text: "Como desea imprimir los rubros de la obra?",
                title: "Impresión",
                iconClose: false,
                dialog: {
                    width: 400,
                    resizable: false,
                    draggable: false,
                    buttons: {

                        "Pdf": function () {
                            url += "0";
                            location.href = url;
                            %{--location.href = "${g.createLink(controller: 'pdf',action: 'pdfLink')}?url=" + url--}%
                        },
                        "Excel": function () {
                            var url = "${createLink(controller:'reportes', action:'imprimirRubrosExcel')}?oferente=${session.usuario.id}&obra=${obra?.id}&transporte=";
                            url += "1";
                            location.href = url;
                        },
                        "Pdf Vae": function () {
                            url += "0";
                            location.href = urlVae;
                         },
                        "Excel Vae": function () {
                            var urlExcelVae = "${createLink(controller:'reportes', action:'imprimirRubrosExcelVae')}?oferente=${session.usuario.id}&obra=${obra?.id}&transporte=";
                            urlExcelVae += "1";
                            location.href = urlExcelVae;

                        },
                        "Cancelar": function () {

                        }
                    }
                }
            });
            return false;
        });

        $("#btn-consultar").click(function () {
            $("#dlgLoad").dialog("open");
            busqueda();
        });

        $("#btnImprimir").click(function () {

            $("#dlgLoad").dialog("open");
            location.href = "${g.createLink(controller: 'reportes', action: 'reporteRegistro', id: obra?.id)}"
            $("#dlgLoad").dialog("close")

        });

        $("#btnVar").click(function () {
            $.ajax({
                type: "POST",
                url: "${createLink(controller: 'variables', action:'variables_ajax')}",
                data: {
                    obra: "${obra?.id}"
                },
                success: function (msg) {
                    var btnCancel = $('<a href="#" data-dismiss="modal" class="btn">Cancelar</a>');
                    var btnSave = $('<a href="#"  class="btn btn-success"><i class="icon-ok"></i> Guardar</a>');

                    btnSave.click(function () {
                        if ($("#frmSave-var").valid()) {
                            btnSave.replaceWith(spinner);
                        }
                        var data = $("#frmSave-var").serialize() + "&id=" + $("#id").val()+"&lang=en_US";
                        var url = $("#frmSave-var").attr("action");

//                                console.log(url);
//                                console.log(data);

                        $.ajax({
                            type: "POST",
                            url: url,
                            data: data,
                            success: function (msg) {
//                                console.log("Data Saved: " + msg);
                                if(msg == 'flash'){
                                    location.reload(true)
                                }else{
                                    location.reload(true)
                                }
                                $("#modal-var").modal("hide");
                            }
                        });

                        return false;
                    });

                    $("#modal_title_var").html("Variables");
                    $("#modal_body_var").html(msg);
                    $("#modal_footer_var").html("").append(btnCancel);

                    <g:if test="${obra?.estado !='R'}">
                    $("#modal_footer_var").html("").append(btnSave);
                    </g:if>
                    <g:else>
                    $("#modal_footer_var").html("").append(btnCancel);
                    </g:else>




//                    $("#modal_footer_var").html("").append(btnSave);
                    $("#modal-var").modal("show");
                }
            });
            return false;
        });

        $("#copiarDialog").dialog({


            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 380,
            height: 280,
            position: 'center',
            title: 'Copiar la obra',
            buttons: {
                "Aceptar": function () {
                    %{--var data = $("#frm-registroObra").serialize();--}%
                    %{--data+="&nuevoCodigo="+ $.trim($("#nuevoCodigo").val());--}%

                    %{--var url = "${createLink(action: 'saveCopia')}?"+data;--}%

                    %{--console.log(url);--}%

                    //location.href = url;

                    var originalId = "${obra?.id}";
                    var nuevoCodigo = $.trim($("#nuevoCodigo").val());

                    $.ajax({
                        type: "POST",
                        url: "${createLink(action: 'saveCopia')}",
                        data: {
                            id: originalId,
                            nuevoCodigo: nuevoCodigo

                        },
                        success: function (msg) {

                            $("#copiarDialog").dialog("close")
                            var parts = msg.split('_')
                            if (parts[0] == 'NO') {

                                $("#spanError").html(parts[1]);
                                $("#divError").show()
                            } else {

                                $("#divError").hide()
                                $("#spanOk").html(parts[1]);
                                $("#divOk").show()

                            }

                        }
                    });

                },
                "Cancelar": function () {

                    $("#copiarDialog").dialog("close");

                }
            }


        });

        $("#busqueda").dialog({

            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 800,
            height: 600,
            position: 'center',
            title: 'Datos de Situación Geográfica'

        });

        $("#estadoDialog").dialog({

            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 450,
            height: 260,
            position: 'center',
            title: 'Cambiar estado de la Obra',
            buttons: {
                "Aceptar": function () {
//
                    var estadoCambiado = $("#estado").val();

                    %{--var estadoCambiado1 = ${obra?.estado}--}%

                    %{--console.log(${obra?.estado})--}%

                    if (${obra?.estado == 'N'}) {
//                        estadoCambiado = 'R';
                        $.ajax({
                            type: "POST",
                            url: "${g.createLink(action: 'regitrarObra')}",
                            data: "id=${obra?.id}",
                            success: function (msg) {
                                console.log(msg)
                                if (msg != "ok") {
                                    $.box({
                                        imageClass: "box_info",
                                        text: msg,
                                        title: "Errores",
                                        iconClose: false,
                                        dialog: {
                                            resizable: false,
                                            draggable: false,
                                            width: 900,
                                            buttons: {
                                                "Aceptar": function () {
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    location.reload(true)
                                }
                            }
                        });
//
                    } else {
                        estadoCambiado = 'N';
                        $.ajax({
                            type: "POST",
                            url: "${g.createLink(action: 'desregitrarObra')}",
                            data: "id=${obra?.id}",
                            success: function (msg) {
                                if (msg != "ok") {
                                    $.box({
                                        imageClass: "box_info",
                                        text: msg,
                                        title: "Errores",
                                        iconClose: false,
                                        dialog: {
                                            resizable: false,
                                            draggable: false,
                                            width: 900,
                                            buttons: {
                                                "Aceptar": function () {
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    location.reload(true)
                                }
                            }
                        });
//
                    }
//                            $(".estado").val(estadoCambiado);
                    $("#estadoDialog").dialog("close");
//
                },
                "Cancelar": function () {
                    $("#estadoDialog").dialog("close");
                }
            }

        });

        $("#documentosDialog").dialog({

            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 350,
            height: 180,
            position: 'center',
            title: 'Imprimir Documentos de la Obra',
            buttons: {
                "Aceptar": function () {

                    $("#documentosDialog").dialog("close");

                }
            }

        });

        $(".btnFormula").click(function () {
            var url = $(this).attr("href");
            $("#dlgLoad").dialog("open");
            $.ajax({
                type: "POST",
                url: url,
                success: function (msg) {
                    if (msg == "ok" || msg == "OK") {
                        location.href = "${createLink(controller: 'formulaPolinomica', action: 'coeficientes', id:obra?.id)}";
                    }
                }
            });

            return false;
        });

        var url = "${resource(dir:'images', file:'spinner_24.gif')}";
        var spinner = $("<img style='margin-left:15px;' src='" + url + "' alt='Cargando...'/>");

        function submitForm(btn) {
            if ($("#frmSave-TipoObra").valid()) {
                btn.replaceWith(spinner);
            }
            $("#frmSave-TipoObra").submit();
        }

        $("#btnCrearTipoObra").click(function () {

            $.ajax({
                type: "POST",
                url: "${createLink(action:'crearTipoObra')}",
                success: function (msg) {
                    var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cancelar</a>');
                    var btnSave = $('<a href="#"  class="btn btn-success"><i class="icon-save"></i> Guardar</a>');

                    btnSave.click(function () {
//                            submitForm(btnSave);
                        $(this).replaceWith(spinner);
                        $.ajax({
                            type: "POST",
                            url: "${createLink(controller: 'tipoObra', action:'saveTipoObra')}",
                            data: $("#frmSave-TipoObra").serialize(),
                            success: function (msg) {
                                if (msg != 'error') {
                                    $("#divTipoObra").html(msg);
                                }

                                $("#modal-TipoObra").modal("hide");
                            }
                        });

                        return false;
                    });

                    $("#modalHeader_tipo").removeClass("btn-edit btn-show btn-delete");
                    $("#modalTitle_tipo").html("Crear Tipo de Obra");
                    $("#modalBody_tipo").html(msg);
                    $("#modalFooter_tipo").html("").append(btnOk).append(btnSave);
                    $("#modal-TipoObra").modal("show");
                }
            });
            return false;

        });

        $("#eliminarObraDialog").dialog({

            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 350,
            height: 220,
            position: 'center',
            title: 'Eliminar Obra',
            buttons: {
                "Aceptar": function () {

                    if (${volumen?.id != null || formula?.id != null}) {

                        $("#noEliminarDialog").dialog("open")

                    }

                    else {

                        $.ajax({
                            type: "POST",
                            url: "${createLink(action: 'delete')}",
                            data: "id=${obra?.id}",
                            success: function (msg) {

                                if (msg == 'ok') {

                                    location.href = "${createLink(action: 'registroObra')}"

                                } else {

                                }

                            }
                        });

                    }

                    $("#eliminarObraDialog").dialog("close")

                },
                "Cancelar": function () {

                    $("#eliminarObraDialog").dialog("close")

                }

            }

        });

        $("#noEliminarDialog").dialog({

            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 350,
            height: 220,
            position: 'center',
            title: 'No se puede Eliminar la Obra!',
            buttons: {
                "Aceptar": function () {

                    $("#eliminarObraDialog").dialog("close");
                    $("#noEliminarDialog").dialog("close");

                }
            }


        });

        function busqueda() {

            var buscarPor = $("#buscarPor").val();
            var criterio = $(".criterio").val();

            var ordenar = $("#ordenar").val();

//                   console.log("buscar" + buscarPor)
//                    console.log("criterio" + criterio)
//                    console.log("ordenar" + ordenar)

            $.ajax({
                type: "POST",
                url: "${createLink(action:'situacionGeografica')}",
                data: {
                    buscarPor: buscarPor,
                    criterio: criterio,
                    ordenar: ordenar

                },
                success: function (msg) {

                    $("#divTabla").html(msg);
                    $("#dlgLoad").dialog("close");
                }
            });

        }

    });
</script>

</body>
</html>