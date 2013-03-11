<%@ page import="janus.pac.Asignacion" %>
<%@ page import="janus.Grupo" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <title>
        P.A.C.
    </title>
    <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'jquery.validate.min.js')}"></script>
    <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'messages_es.js')}"></script>
    <script src="${resource(dir: 'js/jquery/plugins/', file: 'jquery.livequery.js')}"></script>
    <script src="${resource(dir: 'js/jquery/plugins/box/js', file: 'jquery.luz.box.js')}"></script>
    <link href="${resource(dir: 'js/jquery/plugins/box/css', file: 'jquery.luz.box.css')}" rel="stylesheet">
    <script src="${resource(dir: 'js/jquery/plugins/jQuery-contextMenu-gh-pages/src',file: 'jquery.ui.position.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js/jquery/plugins/jQuery-contextMenu-gh-pages/src',file: 'jquery.contextMenu.js')}" type="text/javascript"></script>
    <link href="${resource(dir: 'js/jquery/plugins/jQuery-contextMenu-gh-pages/src',file: 'jquery.contextMenu.css')}" rel="stylesheet" type="text/css" />
    <style type="text/css">
    td{
        font-size: 10px !important;
    }
    th{
        font-size: 11px !important;
    }
    </style>
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
<div class="tituloTree" style="width: 800px;">
    Asignacion de techos anuales a partidas presupuestarias
</div>
<div id="list-grupo" class="span12" role="main" style="margin-top: 10px;margin-left: 0px">

    <div id="create-Asignacion"  style="border-bottom: 1px solid black;width: 800px;margin-bottom: 10px" >
        <g:form class="form-horizontal frm_asgn" name="frmSave-Asignacion" action="save" >
            <g:hiddenField name="id" value="${asignacionInstance?.id}"/>

            <div class="control-group">
                <div>
                    <span class="control-label label label-inverse">
                        Partida
                    </span>
                </div>

                <div class="controls">

                    <input type="text" style="width: 190px;;font-size: 10px" id="item_presupuesto">

                    <input type="hidden" style="width: 60px" id="item_prsp" name="prespuesto.id">
                    <a href="#" class="btn btn-warning" title="Crear nueva partida" style="margin-top: -10px" id="item_agregar_prsp">
                        <i class="icon-edit"></i>
                        Crear nueva partida
                    </a>
                    <br>
                    <input type="text" style="width: 370px;;font-size: 10px;margin-top: 5px;" id="item_desc" disabled="true">

                </div>
            </div>

            <div class="control-group">
                <div>
                    <span class="control-label label label-inverse">
                        Anio
                    </span>
                </div>

                <div class="controls">
                    <g:select id="anio" name="anio.id" from="${janus.pac.Anio.list()}" optionKey="id" optionValue="anio" class="many-to-one  required" value="${actual}"/>
                    <span class="mandatory">*</span>
                    <p class="help-block ui-helper-hidden"></p>
                </div>
            </div>

            <div class="control-group">
                <div>
                    <span class="control-label label label-inverse">
                        Valor
                    </span>
                </div>

                <div class="controls">
                    <g:field type="number" name="valor" id="valor" class=" required" value="0.00"/>
                    <span class="mandatory">*</span>
                    <p class="help-block ui-helper-hidden"></p>
                </div>
            </div>
            <div class="control-group">
                <div>
                    <a href="#"  id="guardar"  class="btn btn-primary">Guardar </a>
                </div>
            </div>

        </g:form>
    </div>

    <div id="list-Asignacion" style="width: 800px;">

    </div>
</div>


<div class="modal grande hide fade" id="modal-ccp" style="overflow: hidden;">
    <div class="modal-header btn-info">
        <button type="button" class="close" data-dismiss="modal">×</button>

        <h3 id="modalTitle"></h3>
    </div>

    <div class="modal-body" id="modalBody">
        <bsc:buscador name="pac.buscador.id" value="" accion="buscaPrsp" controlador="asignacion" campos="${campos}" label="cpac" tipo="lista"/>
    </div>

    <div class="modal-footer" id="modalFooter">
    </div>
</div>
<div class="modal hide fade" id="modal-presupuesto">
    <div class="modal-header btn-warning">
        <button type="button" class="close" data-dismiss="modal">×</button>

        <h3 id="modalTitle-presupuesto"></h3>
    </div>

    <div class="modal-body" id="modalBody-presupuesto">
    </div>

    <div class="modal-footer" id="modalFooter-presupuesto">
    </div>
</div>
<script type="text/javascript">
    function cargarTecho(){
        if($("#item_prsp").val()*1>0){
            $.ajax({type : "POST", url : "${g.createLink(controller: 'asignacion',action:'cargarTecho')}",
                data     :  "id="+$("#item_prsp").val()+"&anio="+$("#anio").val(),
                success  : function (msg) {
                    $("#valor").val(number_format(msg, 2, ".", ""))
                }
            });
        }else{
            $.box({
                imageClass : "box_info",
                text       : "Por favor escoja una partida presupuestaria, dando doble click en el campo de texto",
                title      : "Alerta",
                iconClose  : false,
                dialog     : {
                    resizable : false,
                    draggable : false,
                    buttons   : {
                        "Aceptar" : function () {
                        }
                    },
                    width     : 500
                }
            });
        }

    }

    $("#guardar").click(function(){
        var msn =""
        var valor = $("#valor").val()
        if($("#item_prsp").val()*1<1){
            msn+="<br>Error: Escoja una partida presupuestaria, dando doble click en el campo de texto"
        }
        if(isNaN(valor)){
            msn+="<br>Error: El valor debe ser un número positivo"
        }else{
            if(valor*1<0){
                msn+="<br>Error: El valor debe ser un número positivo"
            }
        }
        if(msn=="")
            $(".frm_asgn").submit()
        else{
            $.box({
                imageClass : "box_info",
                text       : msn,
                title      : "Errores",
                iconClose  : false,
                dialog     : {
                    resizable : false,
                    draggable : false,
                    buttons   : {
                        "Aceptar" : function () {
                        }
                    },
                    width     : 500
                }
            });
        }

    })


    $("#anio").change(cargarTecho)
    $("#item_agregar_prsp").click(function () {
        $.ajax({
            type    : "POST",
            url     : "${createLink(action:'form_ajax',controller: 'presupuesto')}",
            success : function (msg) {
                var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cancelar</a>');
                var btnSave = $('<a href="#"  class="btn btn-success"><i class="icon-ok"></i> Guardar</a>');

                btnSave.click(function () {
                    if ($("#frmSave-presupuestoInstance").valid()) {
                        btnSave.replaceWith(spinner);
                    }
                    $.ajax({type : "POST", url : "${g.createLink(controller: 'presupuesto',action:'saveAjax')}",
                        data     :   $("#frmSave-presupuestoInstance").serialize(),
                        success  : function (msg) {
//                            console.log(msg)
                            var parts = msg.split("&")
                            $("#item_prsp").val(parts[0])
                            $("#item_presupuesto").val(parts[1])
                            $("#item_presupuesto").attr("title",parts[2])
                            $("#item_desc").val(parts[2])
                            $("#modal-presupuesto").modal("hide");
                        }
                    });

                    return false;
                });

                $("#modalTitle-presupuesto").html("Crear Presupuesto");
                $("#modalBody-presupuesto").html(msg);
                $("#modalFooter-presupuesto").html("").append(btnOk).append(btnSave);
                $("#modal-presupuesto").modal("show");
            }
        });
        return false;
    });
    $("#item_presupuesto").dblclick(function () {
        var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cerrar</a>');
        $("#modalTitle").html("Partidas presupuestarias");
        $("#modalFooter").html("").append(btnOk);
        $(".contenidoBuscador").html("")
        $("#modal-ccp").modal("show");

    });
    %{--list-Asignacion--}%
    $.ajax({type : "POST", url : "${g.createLink(controller: 'asignacion',action:'tabla')}",
        data     :   "",
        success  : function (msg) {
           $("#list-Asignacion").html(msg)
        }
    });


</script>

</body>
</html>

