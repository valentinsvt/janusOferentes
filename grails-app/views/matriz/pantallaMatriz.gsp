<%@ page import="janus.Grupo" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            MatrizFP
        </title>
        <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'jquery.validate.min.js')}"></script>
        <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'messages_es.js')}"></script>
        <script src="${resource(dir: 'js/jquery/plugins/', file: 'jquery.livequery.js')}"></script>
        <script src="${resource(dir: 'js/jquery/plugins/', file: 'jquery.onscreen.js')}"></script>
        <script src="${resource(dir: 'js/jquery/plugins/box/js', file: 'jquery.luz.box.js')}"></script>
        <link href="${resource(dir: 'js/jquery/plugins/box/css', file: 'jquery.luz.box.css')}" rel="stylesheet">
        <script src="${resource(dir: 'js/jquery/plugins/jQuery-contextMenu-gh-pages/src', file: 'jquery.ui.position.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js/jquery/plugins/jQuery-contextMenu-gh-pages/src', file: 'jquery.contextMenu.js')}" type="text/javascript"></script>
        <link href="${resource(dir: 'js/jquery/plugins/jQuery-contextMenu-gh-pages/src', file: 'jquery.contextMenu.css')}" rel="stylesheet" type="text/css"/>
        <style type="text/css">
        .gris {
            background-color : #ececec;
        }

        .activo {
            background-color : rgba(255, 172, 55, 0.8);
            font-weight      : bold;
        }

        .blanco {
            background-color : transparent;
        }

        .estaticas {
            background  : linear-gradient(to bottom, #FFFFFF, #E6E6E6);
            font-weight : bold;
        }

        tr {
            cursor : pointer !important;
        }

        th {
            padding-left  : 0px;
            padding-right : 0px;
        }

        td {
            line-height : 12px !important;
            padding     : 3px !important;
        }

        .selectedColumna {
            background-color : rgba(120, 220, 249, 0.4);
            font-weight      : bold;
        }
        </style>

        <script type="text/javascript">
            $("#dlgLoad").dialog("open");
        </script>
    </head>

    <body>
        <div class="span12">
            <g:if test="${flash.message}">
                <div class="alert ${flash.clase ?: 'alert-info'}" role="status">
                    <a class="close" data-dismiss="alert" href="#">×</a>
                    ${flash.message}
                </div>
            </g:if>
        </div>

        <div class="span12 btn-group" role="navigation" style="margin-left: 0px;">
            <a href="${g.createLink(controller: 'obra', action: 'registroObra', params: [obra: obra])}" class="btn btn-ajax btn-new" id="atras" title="Regresar a la obra">
                <i class="icon-arrow-left"></i>
                Regresar
            </a>
            <g:link controller="formulaPolinomica" action="insertarVolumenesItem" class="btn btn-ajax btn-new btnFormula" params="[obra: obra]" title="Coeficientes">
                <i class="icon-table"></i>
                Coeficientes fórmula polinómica
            </g:link>
            <a href="${g.createLink(controller: 'reportes', action: 'imprimeMatriz', id: "${obra}")}" class="btn btn-ajax btn-new" id="imprimir" title="Imprimir">
                <i class="icon-print"></i>
                Imprimir
            </a>
            <input type="text" style="width: 200px;margin-left: 20px;margin-top: 9px;" class="ui-corner-all" id="texto_busqueda">
            <a href="#" class="btn btn-ajax btn-new" id="buscar" title="Buscar">
                <i class="icon-search"></i>
                Buscar
            </a>
            <a href="#" class="btn btn-ajax btn-new" id="reset" title="Resetear">
                <i class="icon-refresh"></i>
                Limpiar selección
            </a>
        </div>

        <div id="list-grupo" class="span12" role="main" style="margin-top: 10px;margin-left: 0px;width: 100%;max-width: 100%;overflow-x: hidden">
            <div style="width: 1000px;overflow-x: auto;max-width: 1000px;" class="scroll-pane">
                <table class="table table-bordered table-condensed  " style="width: ${cols.size() * 150 - 140}px;max-width: ${cols.size() * 150 - 140}px;float:left">
                    <thead>
                        <tr style="font-size: 10px !important;">
                            <th style="width: 30px;max-width: 30px;font-size: 12px !important" class="h_0">#</th>
                            <th style="width: 100px;;font-size: 12px !important" class="h_1">Código</th>
                            <th style="width: 300px !important;;font-size: 12px !important">Rubro</th>
                            <th style="width: 40px;;font-size: 12px !important">Unidad</th>
                            <th style="width: 70px;;font-size: 12px !important">Cantidad</th>
                            <g:each in="${cols}" var="c" status="k">
                                <g:if test="${c[2] != 'R'}">
                                    <th style="width: 150px;font-size: 12px !important" class="col_${k}" col="${k}">${c[1]}</th>
                                </g:if>
                            </g:each>
                        </tr>
                    </thead>
                    <tbody id="tableBody" class="scroll-content">

                    </tbody>
                    <tfoot>
                        <tr id="bandera">
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>

        <div id="div_hidden" style="display: none">
            <table class="table table-bordered table-condensed  " style="width: ${cols.size() * 150 - 140}px;max-width: ${cols.size() * 150 - 140}px;float:left">
                <thead>
                    <tr style="font-size: 10px !important;">
                        <th style="width: 30px;max-width: 30px;font-size: 12px !important" class="h_0">#</th>
                        <th style="width: 100px;;font-size: 12px !important" class="h_1">Código</th>
                        <th style="width: 300px !important;;font-size: 12px !important">Rubro</th>
                        <th style="width: 40px;;font-size: 12px !important">Unidad</th>
                        <th style="width: 70px;;font-size: 12px !important">Cantidad</th>
                        <g:each in="${cols}" var="c">
                            <g:if test="${c[2] != 'R'}">
                                <th style="width: 150px;font-size: 12px !important">${c[1]}</th>
                            </g:if>
                        </g:each>
                    </tr>
                </thead>
                <tbody id="tableBody_hid">

                </tbody>
            </table>
        </div>
        <script type="text/javascript">
            function cargarDatos(inicio, interval, limite) {
                var band = false
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(action: 'matrizPolinomica',controller: 'matriz')}",
                    data    : "id=${obra}&inicio=" + inicio + "&limit=" + limite,
                    success : function (msg) {

                        $("#dlgLoad").dialog("close");
                        if (msg != "fin") {

                            if (inicio == 0) {
                                $("#tableBody").append(msg);
                                copiaTabla()

                            } else {

                                $("#tableBody_hid").append(msg);
                                appendTabla()

                            }

                        } else {
                            band = true
                        }

                    }
                });
//        console.log("return "+band)
                return band
            }
            function cargarDatosAsinc(inicio, interval, limite) {
                var band = false
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(action: 'matrizPolinomica',controller: 'matriz')}",
                    data    : "id=${obra}&inicio=" + inicio + "&limit=" + limite,
                    async   : false,
                    success : function (msg) {

                        $("#dlgLoad").dialog("close");
                        if (msg != "fin") {

                            if (inicio == 0) {
                                $("#tableBody").append(msg);
                                copiaTabla()

                            } else {

                                $("#tableBody_hid").append(msg);
                                appendTabla()

                            }

                        } else {
                            band = true
                        }

                    }
                });
//        console.log("return "+band)
                return band
            }
            function copiaTabla() {
                var tabla = $('<table class="table table-bordered  table-condensed " id="tablaHeaders" style="width:140px;max-width: 140px;float: left">')
                tabla.append('<thead><th style="width: 30px;max-width: 30px;font-size: 12px !important">#</th><th style="width: 100px;;font-size: 12px !important" >Código</th></thead>')
                var body = $('<tbody id="body_headers">')
                var cnt = 0;
                $(".item_row").each(function () {
                    var tr = $("<tr class='item_row fila_" + cnt + "' fila='fila_" + cnt + "'>")
                    tr.css({"height" : $(this).innerHeight()})
                    tr.attr("color", $(this).attr("color"))
                    $(this).css({"height" : $(this).innerHeight()})
                    var col0 = $(this).find(".col_0")
                    var col1 = $(this).find(".col_1")
                    var c0 = col0.clone()
                    var c1 = col1.clone()
                    c0.removeClass("col_0").addClass("estaticas")
                    c1.removeClass("col_1").addClass("estaticas codigos")
                    tr.append(c0)
                    tr.append(c1)
                    cnt++
                    body.append(tr)

                });
                tabla.append(body)
                $("#list-grupo").prepend(tabla)
                $(".h_0").remove()
                $(".h_1").remove()
                $(".col_0").remove()
                $(".col_1").remove()

            }
            function appendTabla() {
                var tabla = $("#body_headers")

                $("#tableBody_hid").find(".item_row").each(function () {
                    var col0 = $(this).find(".col_0")
                    var col1 = $(this).find(".col_1")
                    var c0 = col0.clone()
                    var c1 = col1.clone()
                    var num = $(this).attr("num")
                    col0.remove()
                    col1.remove()
                    $("#tableBody").append($(this))
                    var tr = $("<tr class='item_row fila_" + num + "' fila='fila_" + num + "'>")
                    tr.css({"height" : $(this).innerHeight()})
                    tr.attr("color", $(this).attr("color"))
                    $(this).css({"height" : $(this).innerHeight()})
//            console.log($(this),$(this).innerHeight())
                    c0.removeClass("col_0").addClass("estaticas")
                    c1.removeClass("col_1").addClass("estaticas")

                    tr.append(c0)
                    tr.append(c1)
                    tabla.append(tr)

                });
                $(".item_row").bind("click", function () {
                    if ($(this).hasClass("activo")) {
                        $("." + $(this).attr("fila")).addClass($(".activo").attr("color")).removeClass("activo")
                    } else {
                        $(this).addClass("activo")
                        $("." + $(this).attr("fila")).addClass("activo")
                        $("." + $(this).attr("fila")).removeClass("gris")
                        $("." + $(this).attr("fila")).removeClass("blanco")
                    }
                });

            }
            $(function () {
                $("#dlgLoad").dialog("open");
                var inicio = 0
                cargarDatosAsinc(inicio, "interval", 40)
                inicio = 2
                var fin = false
                var ultimo = 1
//        var interval=setInterval(function(){
////            console.log("interval" + inicio)
//            cargarDatos(inicio,interval,20)
//            inicio++
//
//        }, 4000);
                $(".item_row").click(function () {
                    if ($(this).hasClass("activo")) {
                        $("." + $(this).attr("fila")).addClass($(".activo").attr("color")).removeClass("activo")
                    } else {
                        $(this).addClass("activo")
                        $("." + $(this).attr("fila")).addClass("activo")
                        $("." + $(this).attr("fila")).removeClass("gris")
                        $("." + $(this).attr("fila")).removeClass("blanco")
                    }

                });
                var ctrl = 0
                $("body").keydown(function (ev) {
//            console.log(ev.keyCode)
                    if (ev.keyCode == 17)
                        ctrl = 600
                    if (ev.keyCode == 16)
                        ctrl = 60000
                });

                var interval = setInterval(function () {
                    if ($("#bandera:onScreen").attr("id")) {
                        if (!fin) {

                            if (inicio > ultimo) {
                                $("#dlgLoad").dialog("open");
                                fin = cargarDatosAsinc(inicio, "interval", 20)
                                ultimo = inicio
                                if (!fin)
                                    inicio++
                                $(document).scrollTop($(document).scrollTop() - 300)
                            }

                        } else {
//                    console.log("clear interval!")
                            clearInterval(interval)
                        }

//                console.log("scroll!!")
                    }

                }, 2000);

//        $(document).scroll(function(){
////            console.log($("#bandera:onScreen"))
//
//
//        })
                $("th").click(function () {
                    var clase = "col_" + $(this).attr("col")
                    if ($(this).hasClass("selectedColumna")) {
                        $("." + clase).removeClass("selectedColumna")
                    } else {
                        $("." + clase).addClass("selectedColumna")
                    }

//            console.log("click th"+clase)

                });

                $("body").keyup(function (ev) {
                    if (ev.keyCode == 17)
                        ctrl = 0
                    if (ev.keyCode == 16)
                        ctrl = 0
                    if (ev.keyCode == 37) {
                        var leftPos = $('.scroll-pane').scrollLeft();
                        $(".scroll-pane").animate({scrollLeft : leftPos - (300 + ctrl)}, 800);

                    }
                    if (ev.keyCode == 39) {
                        var leftPos = $('.scroll-pane').scrollLeft();
                        $(".scroll-pane").animate({scrollLeft : leftPos + 300 + ctrl}, 800);

                    }

                });
                $("#buscar").click(function () {

                    var par = $("#texto_busqueda").val()
                    var primero = null
                    if (par.length > 0) {
                        $("th").each(function () {
                            var mayus = par.toUpperCase()
                            if ($(this).html().match(mayus)) {
                                if (!$(this).hasClass("selectedColumna"))
                                    $(this).click();
                                if (!primero)
                                    primero = $(this)

                            }
                        });
                        $("#body_headers").find(".codigos").each(function () {
                            var mayus = par.toUpperCase()
                            if ($(this).html().toUpperCase().match(mayus)) {
                                if (!$(this).hasClass("activo"))
                                    $(this).click();
                            }
                        });

                        if (primero) {
                            var leftPos = $('.scroll-pane').scrollLeft() + 500;
                            var pos = primero.position().left - 500
//                console.log($('.scroll-pane').scrollLeft(),leftPos,primero.position().left,primero,primero.offsetParent())
                            $(".scroll-pane").animate({scrollLeft : leftPos + pos - 500}, 800);
                        }
                    }

                });
                $("#reset").click(function () {
                    $(".activo").addClass($(".activo").attr("color")).removeClass("activo")
                    $(".selectedColumna").removeClass("selectedColumna")

                });

                $(".btnFormula").click(function () {
                    var url = $(this).attr("href");
                    $("#dlgLoad").dialog("open");
                    $.ajax({
                        type    : "POST",
                        url     : url,
                        success : function (msg) {
                            if (msg == "ok" || msg == "OK") {
                                location.href = "${createLink(controller: 'formulaPolinomica', action: 'coeficientes', id:obra)}";
                            }
                        }
                    });

                    return false;
                });

            });

        </script>

    </body>
</html>