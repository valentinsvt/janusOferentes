<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 1/14/13
  Time: 11:49 AM
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
        font-weight : bolder;
    }

    </style>


    <title>Registro de Contratos</title>
</head>
<body>

<div class="span12 btn-group" role="navigation" style="margin-left: 0px;width: 100%;float: left;height: 35px;">
    <button class="btn" id="btn-lista"><i class="icon-book"></i> Lista</button>
    <button class="btn" id="btn-nuevo"><i class="icon-plus"></i> Nuevo</button>
    <button class="btn" id="btn-aceptar" disabled="true"><i class="icon-ok"></i> Aceptar</button>
    <button class="btn" id="btn-cancelar"><i class="icon-undo"></i> Cancelar</button>
    <button class="btn" id="btn-borrar"><i class="icon-remove"></i> Eliminar Contrato</button>
    <button class="btn" id="btn-salir"><i class="icon-ban-circle"></i> Salir</button>

</div>




<fieldset class="" style="position: relative; height: 50px; float: left;border-bottom: 1px solid black;">

<g:form class="registroContrato" name="frm-registroContrato" action="save">

<div class="span12" style="margin-top: 10px" align="center">

    <g:if test="${contrato?.codigo != null}">

        <div class="span2 formato">Contrato N°</div>

        <div class="span3"><g:textField name="contratoNumero" class="cotratoNumero" value="${contrato?.codigo}" disabled="true"/></div>

        <div class="span2 formato">Memo de Distribución</div>

        <div class="span3"><g:textField name="memoDistribucion" class="memo" value="${contrato?.memo}" disabled="true"/></div>


        </div>

    </g:if>

    <g:else>

        <div class="span2 formato">Contrato N°</div>

        <div class="span3"><g:textField name="contratoNumero" class="cotratoNumero" value="${contrato?.codigo}" /></div>

        <div class="span2 formato">Memo de Distribución</div>

        <div class="span3"><g:textField name="memoDistribucion" class="memo" value="${contrato?.memo}" /></div>


    </g:else>


</fieldset>


<fieldset class="" style="position: relative; padding: 10px;border-bottom: 1px solid black;">




    <p class="css-vertical-text">Contratación</p>

    <div class="linea" style="height: 85%;"></div>



    <g:if test="${contrato?.codigo != null}">

        <div class="span12" align="center">

            <div class="span2 formato">Obra</div>

            <div class="span3"><g:textField name="obra" id ="obraCodigo" class="obraCodigo" value="${contrato?.oferta?.concurso?.obra?.codigo}" disabled="true"/></div>

            <div class="span1 formato">Nombre</div>

            <div class="span3"><g:textField name="nombre" class="nombreObra" value="${contrato?.oferta?.concurso?.obra?.nombre}" style="width: 400px" disabled="true"/></div>

        </div>

        <div class="span12" style="margin-top: 5px" align="center">

            <div class="span2 formato">Parroquia</div>

            <div class="span3"><g:textField name="parroquia" class="parroquia" value="${contrato?.oferta?.concurso?.obra?.parroquia?.nombre}" disabled="true"/> </div>

            <div class="span1 formato">Cantón</div>

            <div class="span2"><g:textField name="canton" class="canton" value="${contrato?.oferta?.concurso?.obra?.parroquia?.canton?.nombre}" disabled="true"/> </div>

        </div>

        <div class="span12" style="margin-top: 5px" align="center">

            <div class="span2 formato">Clase Obra</div>

            <div class="span3"><g:textField name="claseObra" class="claseObra" value="${contrato?.oferta?.concurso?.obra?.claseObra?.descripcion}" disabled="true"/> </div>


        </div>

        <div class="span12" style="margin-top: 5px" align="center">

            <div class="span2 formato">Contratista</div>

            <div class="span3"><g:textField name="contratista" class="contratista" value="${contrato?.oferta?.proveedor?.nombre}" disabled="true"/> </div>

        </div>

    </g:if>

    <g:else>


        <div class="span12" style="margin-top: 5px" align="center">



            <div class="span2 formato">Obra</div>

            <div class="span3">
                <input type="hidden" id="obraId" value="${contrato?.oferta?.concurso?.obra?.codigo}" name="obra.id" >
                <g:textField name="obra" id ="obraCodigo" class="obraCodigo txtBusqueda" value="${contrato?.oferta?.concurso?.obra?.codigo}"  />
            </div>

            <div class="span1 formato">Nombre</div>

            <div class="span5">
                <g:textField name="nombre" class="nombreObra" id="nombreObra" style="width: 400px" disabled="true"/>
            </div>

        </div>
        <div class="span12" style="margin-top: 5px" align="center">
            <div class="span2 formato">Oferta</div>

            <div class="span3" id="div_ofertas">
                <g:select name="oferta.id" from="" noSelection="['-1':'Seleccione una obra']" id="oferta"> </g:select>
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

            <div class="span3"><g:textField name="parroquia" class="parroquia" id="parr"/> </div>

            <div class="span1 formato">Cantón</div>

            <div class="span2"><g:textField name="canton" class="canton" id="canton"/> </div>

        </div>

        <div class="span12" style="margin-top: 5px" align="center">

            <div class="span2 formato">Clase Obra</div>

            <div class="span3"><g:textField name="claseObra" class="claseObra" id="clase"/> </div>


        </div>

        <div class="span12" style="margin-top: 5px" align="center">



        </div>



    </g:else>


</fieldset>

<fieldset class="" style="position: relative; height: 150px; float: left;border-bottom: 1px solid black;padding: 10px;">



    <div class="span12" style="margin-top: 10px" align="center">

        <div class="span2 formato">Tipo</div>

        <div class="span3"><g:select from="${janus.pac.TipoContrato.list().descripcion}" name="tipoContrato" class="tipoContrato activo" value="${contrato?.tipoContrato?.descripcion}" /></div>

        <div class="span2 formato">Fecha de Suscripción</div>

        <div class="span2"><elm:datepicker name="fechaSuscripcion" class="fechaSuscripcion datepicker input-small activo" value="${contrato?.fechaSubscripcion}" /> </div>

    </div>

    <div class="span12" style="margin-top: 5px" align="center">

        <div class="span2 formato">Objeto del Contrato</div>

        <div class="span3"><g:textArea name="objetoContrato" class="activo" rows="5" cols="5" style="height: 79px; width: 800px; resize: none" value="${contrato?.objeto}"/></div>



    </div>


</fieldset>

<fieldset class="" style="position: relative; height: 160px; float: left;padding: 10px;border-bottom: 1px solid black;">

    <div class="span12" style="margin-top: 10px">

        <div class="span2 formato">Monto</div>

        <div class="span3"><g:textField name="monto" class="monto activo" value="${contrato?.monto}"/></div>

        <div class="span2 formato">Plazo</div>

        <div class="span3"><g:textField name="plazo" class="plazo activo" value="${contrato?.oferta?.concurso?.obra?.plazo}" style="width: 50px" maxlength="4"/> Días</div>

    </div>

    <div class="span12" style="margin-top: 10px">

        <div class="span2 formato">Anticipo</div>

        <div class="span1"><g:textField name="anticipo" class="anticipo activo" value="${contrato?.oferta?.concurso?.obra?.porcentajeAnticipo}" style="width: 30px; text-align: right"/> %</div>

        <div class="span2"><g:textField name="anticipoValor" class="anticipoValor activo" value="" style="width: 105px; text-align: right"/></div>

        <div class="span2 formato">Indices de la Oferta</div>

        <div class="span3"><g:select name="indiceOferta" from="${janus.pac.PeriodoValidez.list().descripcion}" class="indiceOferta activo" value="${contrato?.periodoValidez}"/> </div>

    </div>


    <div class="span12" style="margin-top: 10px">

        <div class="span2 formato">Financiamiento</div>

        <div class="span3"><g:textField name="financiamiento" class="financiamiento activo" value="${contrato?.financiamiento}"/></div>

        <div class="span2 formato">Financiado Por</div>

        <div class="span3"><g:textField name="financiadoPor" class="financiadoPor activo"/> </div>


    </div>

</fieldset>

</g:form>




<div class="navbar navbar-inverse" style="margin-top: 20px;padding-left: 5px;float: left" align="center">

    <div class="navbar-inner">
        <div class="botones">

            <ul class="nav">
                <li>
                    <g:link controller="garantia" action="garantiasContrato" id="">
                        <i class="icon-pencil"></i>Garantías
                    </g:link>
                </li>
                %{--<li><a href="${g.createLink(controller: 'volumenObra', action: 'volObra', id: obra?.id)}"><i class="icon-list-alt"></i>Vol. Obra--}%
                %{--</a></li>--}%
                <li><a href="#" id="btnCronograma"><i class="icon-th"></i>Cronograma</a></li>
                %{--<li>--}%
                %{--<g:link controller="formulaPolinomica" action="coeficientes" id="${obra?.id}">--}%
                %{--Fórmula Pol.--}%
                %{--</g:link>--}%
                %{--</li>--}%
                %{--<li><a href="#" id="btnFormula"><i class="icon-file"></i>F. Polinómica</a></li>--}%
                <li><a href="${g.createLink(controller: 'contrato', action: 'polinomicaContrato', id: contrato?.id)}"><i class="icon-calendar"></i> F. Polinómica
                </a></li>


            </ul>

        </div>
    </div>

</div>


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

    $("#plazo").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function (){

                var enteros = $(this).val();

            });


    $("#monto").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function (){

                var enteros = $(this).val();

            });




    $("#anticipo").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function (){

                var enteros = $(this).val();

                if(parseFloat(enteros) > 100){

                    $(this).val(100)

                }



            });

    $("#anticipoValor").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function (){

                var enteros = $(this).val();

                var porcentaje = $("#anticipo").val()

                var monto = $("#monto").val()

                var anticipoValor = (porcentaje*(monto))/100;

                $("#anticipoValor").val(number_format(anticipoValor,2,".",""))


            }).click(function () {

                var porcentaje = $("#anticipo").val()

                var monto = $("#monto").val()

                var anticipoValor = (porcentaje*(monto))/100;

                $("#anticipoValor").val(number_format(anticipoValor,2,".",","))



            });


    $("#financiamiento").keydown(function (ev) {

        return validarNum(ev);

    }).keyup(function (){

                var enteros = $(this).val();

            });


    $("#contratoNumero").click (function () {

            $("#btn-aceptar").attr("disabled", false)

    });

    $("#objetoContrato").click (function () {

       $("#btn-aceptar").attr("disabled",false)

    });

    $("#tipoContrato").change(function () {

        $("#btn-aceptar").attr("disabled",false)

    });

    $("#monto").click(function () {

        $("#btn-aceptar").attr("disabled",false)

    });

    $("#financiamiento").click(function () {

        $("#btn-aceptar").attr("disabled",false)

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

    function  cargarCombo(){
        if($("#obraId").val()*1>0) {
            $.ajax({
                type : "POST", url : "${g.createLink(controller: 'contrato',action:'cargarOfertas')}",
                data     : "id="+$("#obraId").val(),
                success  : function (msg) {
                    $("#div_ofertas").html(msg)
                }
            });

        }

    }

    function cargarCanton(){
        if($("#obraId").val()*1>0) {
            $.ajax({
                type : "POST", url : "${g.createLink(controller: 'contrato',action:'cargarCanton')}",
                data     : "id="+$("#obraId").val(),
                success  : function (msg) {
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

        $("#btn-aceptar").attr("disabled",false);

        location.href="${createLink(action: 'registroContrato')}"
    });


    $("#obraCodigo").focus(function () {

        var btnOk = $('<a href="#" data-dismiss="modal" class="btn">Cerrar</a>');
        $("#modalTitle_busquedaOferta").html("Lista de Obras");
        $("#modalFooter_busquedaOferta").html("").append(btnOk);
        $("#modal-busquedaOferta").modal("show");

    });


    if(${contrato?.codigo != null}) {


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



    $("#btn-cancelar").click( function () {

        if(${contrato?.id == null} ){

            location.href = "${g.createLink(action: 'registroContrato')}";

        }  else {

            location.href = "${g.createLink(action: 'registroContrato')}" + "?contrato=" + "${contrato?.id}";

        }



    } )

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