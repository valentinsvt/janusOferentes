<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 1/24/13
  Time: 3:55 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Subir archivo de Indices</title>
    </head>

    <body>
        <div class="span4" style="margin-left: 200px; margin-top: 20px">
            <fieldset class="span4" style="position: relative; height: 120px; float: left;padding: 10px;border-bottom: 1px solid black; border-top: 1px solid black">

                <g:uploadForm action="uploadFile" method="post" name="frmUpload" enctype="multipart/form-data">
                    <fieldset class="form" name="form-envio">
                        <div class="fieldcontain required">
                            %{--<label  for="file">--}%
                            %{--Archivo--}%
                            %{--<span class="required-indicator">*</span>--}%
                            %{--</label>--}%
                            <input type="file" id="file" name="file"/>
                        </div>
                        <g:select name="periodo" from="${janus.pac.PeriodoValidez.list([order: 'descripcion'])}" optionKey="id" optionValue="descripcion"/>

                    </fieldset>
                </g:uploadForm>
                <div class="span4 btn-group" role="navigation" style="margin-left: 0px;width: 100%;float: left;height: 35px;">
                    <button class="btn" id="aceptar"><i class="icon-check"></i> Aceptar</button>
                    <button class="btn" id="regresar"><i class="icon-undo"></i> Regresar</button>
                </div>

            </fieldset>
        </div>

        <script type="text/javascript">


            $("#aceptar").click(function () {

                $("#frmUpload").submit();

                %{--var file = $("#file").val()--}%

                %{--//        console.log("---->>>"+file)--}%

                %{--$.ajax({--}%
                %{--type    : "POST",--}%
                %{--url     : "${createLink(action: 'uploadFile')}",--}%
                %{--data    :  {file: file},--}%
                %{--success : function (msg) {--}%


                %{--}--}%
                %{--});--}%

            });



        </script>

    </body>
</html>