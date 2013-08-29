<%@ page import="janus.Unidad" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>

<head>
    <meta name="layout" content="main">
    <title>Janus -Ingreso-</title>

    <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'jquery.validate.min.js')}"></script>
    <script src="${resource(dir: 'js/jquery/plugins/jquery-validation-1.9.0', file: 'messages_es.js')}"></script>
    %{--<link href='http://fonts.googleapis.com/css?family=Tulpen+One' rel='stylesheet' type='text/css'>--}%
    %{--<link href='http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>--}%
    <link href='${resource(dir: "css", file: "custom.css")}' rel='stylesheet' type='text/css'>
    <link href='${resource(dir: "font/open", file: "stylesheet.css")}' rel='stylesheet' type='text/css'>
    <link href='${resource(dir: "font/tulpen", file: "stylesheet.css")}' rel='stylesheet' type='text/css'>
    <style type="text/css">
    @page {
        size   : 8.5in 11in;  /* width height */
        margin : 0.25in;
    }

    .item {
        width       : 260px;
        height      : 220px;
        float       : left;
        margin      : 4px;
        font-family : 'open sans condensed';
        border      : none;

    }

    .imagen {
        width      : 167px;
        height     : 100px;
        margin     : auto;
        margin-top : 10px;
    }

    .texto {
        width       : 90%;
        height      : 50px;
        padding-top : 0px;
        /*border: solid 1px black;*/
        margin      : auto;
        margin      : 8px;
        /*font-family: fantasy; */
        font-size   : 16px;

        /*
                font-weight: bolder;
        */
        font-style  : oblique;
        /*text-align: justify;*/
    }

    .fuera {
        margin-left      : 15px;
        margin-top       : 10px;
        /*background-color: #317fbf; */
        background-color : rgba(200, 200, 200, 0.9);
        border           : none;

    }

    .desactivado {
        color : #bbc;
    }

    .titl {
        font-family: 'open sans condensed';
        font-weight: bold;
        text-shadow: -2px 2px 1px rgba(0, 0, 0, 0.25);
        color:#0088CC;
        margin-top: 20px;
    }

    .archivo {
        width: 100%;
        float: left;
        margin-top: 30px;
        text-align: center;
    }
    </style>
</head>

<body>
<g:if test="${flash.message}">
    <div class="alert alert-info" role="status">
        <a class="close" data-dismiss="alert" href="#">×</a>
        ${flash.message}
    </div>
</g:if>

<div class="alert hide" id="divError" role="status">
    <a class="close" data-dismiss="alert" href="#">×</a>
    <span id="spanError"></span>
</div>

    <div style="text-align: center;"><h1 class="titl" style="font-size: 26px;">SEP - GADPP</h1>
        <h1 class="titl" style="font-size: 20px;">SEGUIMIENTO Y EJECUCIÓN DE PROYECTOS DE OBRAS Y CONSULTORÍAS<br>
        GOBIERNO AUTÓNOMO DESCENTRALIZADO PROVINCIA DE PICHINCHA</h1>
        <h1 class="titl" style="font-size: 24px; color: #06a">Registro de Ofertas en Línea</h1></div>

<div class="dialog ui-corner-all" style="height: 595px;padding: 10px;width: 910px;margin: auto;margin-top: 5px">
    <div style="text-align: center; margin-top: 10px; color: #810;">
        <img src="${resource(dir: 'images', file: 'logo_gpp3.png')}"/>
    </div>

    <div style="width: 100%;height: 20px;float: left;margin-top: 20px;text-align: center">
        <a href="#" id="ingresar" class="btn btn-inverse">
            <i class="icon-off"></i>
            Ingresar
        </a>
    </div>

    <div class="archivo">
        Le recomendamos descargar y leer el
        <a  href="${createLink(uri: '/Manual sep-oferentes.pdf')}"><img src="${resource(dir: 'images', file: 'pdf_pq.png')}"/>manual de usuario </a>
        antes de ingresar a nuestro sistema.
    </div>


    <div style="text-align: center ; color:#004060; margin-top:120px; font-size: 10px;">Desarrollado por: TEDEIN S.A. Versión ${message(code: 'version', default: '1.1.0x')}</div>

</div>






<div class="modal login hide fade " id="modal-ingreso" style=";overflow: hidden;">
    <div class="modal-body" id="modalBody" style="padding: 0px">

        <g:form class="well form-horizontal span " action="validar" name="frmLogin" style="border: 5px solid #2080B0;background:#C7C7C5 /*#a7a7a5*/;color: #939Aa2;width: 300px;position: relative;padding-left: 60px;margin: 0px">
            <p class="css-vertical-text tituloGrande" style="left: 12px;;font-family: 'Tulpen One',cursive;font-weight: bold;font-size: 35px; color: #334">Sistema SEP-Ofenretes</p>

            <div class="linea" style="height: 95%;left: 45px; border-left-color: #334"></div>
            <button type="button" class="close" data-dismiss="modal" style="color: white;opacity: 1;">×</button>
            <fieldset style="">
                <legend style="color: #334;border:none;font-family: 'Open Sans Condensed', serif;font-weight: bolder;font-size: 25px">Ingreso</legend>

                <g:if test="${flash.message}">
                    <div class="alert alert-info" role="status">
                        <a class="close" data-dismiss="alert" href="#">×</a>
                        ${flash.message}
                    </div>
                </g:if>

                <div class="control-group" style="margin-top: 0">

                    <label class="control-label" for="login" style="width: 100%;text-align: left;font-size: 25px;font-family: 'Tulpen One',cursive;font-weight: bolder; color: #334">Usuario:</label>

                    <div class="controls" style="width: 100%;margin-left: 5px">
                        <g:textField name="login" class="span2" style="width: 90%; border: 1px solid #000000" />
                        <p class="help-block ui-helper-hidden"></p>
                    </div>
                </div>

                <div class="control-group">

                    <label class="control-label" for="login" style="width: 100%;text-align: left;font-size: 25px;font-family: 'Tulpen One',cursive;font-weight: bolder; color:#334">Password:</label>

                    <div class="controls" style="width: 100%;margin-left: 5px">
                        <g:passwordField name="pass" class="span2" style="width: 90%;border: 1px solid #000000"/>
                        <p class="help-block ui-helper-hidden"></p>
                    </div>
                </div>

                <div class="control-group">

                    <a href="#" class="btn btn-primary" id="btnLogin">Continuar</a>
                    <a href="#" id="btnOlvidoPass" style="color: #223;margin-left: 70px;text-decoration: none;font-family: 'Open Sans Condensed', serif;font-weight: bold">
                        Olvidó su contraseña?
                    </a>
                </div>
            </fieldset>
        </g:form>
    </div>
</div>







</div>


<div id="recuperarPass-dialog" class="dialog ui-corner-all" style="height: 595px;background: #d0d0d0;;padding: 10px;width: 910px;margin: auto;margin-top: 5px">

    %{--<fieldset>--}%


    <div style="text-align: center;">
        <h1 style="font-family: 'open sans condensed';font-weight: bold;font-size: 12px;text-shadow: -2px 2px 1px rgba(0, 0, 0, 0.25);color:#fff;">

        </h1>
    </div>

    <div class="span3">
        Ingrese el email registrado a su usuario y se le enviará una nueva contraseña para ingresar al sistema.

    </div>

    <div class="span3" style="margin-top: 20px">

        <g:textField name="mailPass" type="text" id="email" class="span2" placeholder="Email" style="width: 300px; border: 1px solid #000000"/>

    </div>
    %{--</fieldset>--}%
</div>



<script type="text/javascript">
    $(function () {

        $("#btnOlvidoPass").click(function () {


            $("#recuperarPass-dialog").dialog("open");
            $("#modal-ingreso").modal("hide");

        });


        $("#recuperarPass-dialog").dialog({

            autoOpen: false,
            resizable: false,
            modal: true,
            draggable: false,
            width: 400,
            height: 250,
            position: 'center',
            title: 'Recuperar Password',
            buttons: {
                "Aceptar": function () {

//                    var email = $.trim(mail.val());


                    var emailPass = $.trim(email.value)

//                    console.log(emailPass)

                    if (email != "") {
//                        btnSend.replaceWith(spinner);
                        $.ajax({
                            type: "POST",
                            url: "${createLink(action:'olvidoPass')}",
                            data: {
                                email: emailPass
                            },
                            success: function (msg) {
                                var parts = msg.split("*");
                                if (parts[0] == "OK") {
                                    $("#divError").removeClass("alert-error").addClass("alert-success").show();
                                    $("#spanError").html(parts[1]);
                                    $("#recuperarPass-dialog").dialog("close");

                                } else {
                                    $("#divError").addClass("alert-error").removeClass("alert-success").show();
                                    $("#spanError").html(parts[1]);
                                    $("#recuperarPass-dialog").dialog("close")
                                }
//                                $("#modalBody").html(parts[1]);
//                                spinner.remove();
                            }
                        });
                    } else {
                        $("#divMail").addClass("error");
                    }


                },

                "Cancelar": function () {

                    $("#recuperarPass-dialog").dialog("close")

                }


            }

        });


        $("input").keypress(function (ev) {
            if (ev.keyCode == 13) {
                $("#frmLogin").submit();
            }
        });

        $("#btnLogin").click(function () {
            $("#frmLogin").submit();
            return false;
        });

        $("#frmLogin").validate({
            errorPlacement: function (error, element) {
                element.parent().find(".help-block").html(error).show();
            },
            success: function (label) {
                label.parent().hide();
            },
            errorClass: "label label-important",
            submitHandler: function (form) {
                $("#btnLogin").replaceWith(spinnerLogin);
                form.submit();
            }
        });

        $(".fuera").hover(function () {
            var d = $(this).find(".imagen")
            d.width(d.width() + 10)
            d.height(d.height() + 10)
//        $.each($(this).children(),function(){
//            $(this).width( $(this).width()+10)
//        });
        }, function () {
            var d = $(this).find(".imagen")
            d.width(d.width() - 10)
            d.height(d.height() - 10)
        })


    });
</script>

</body>
</html>