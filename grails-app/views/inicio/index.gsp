<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sistema SEP-Oferentes</title>
    <meta name="layout" content="main"/>
    <style type="text/css">
    @page {
        size: 8.5in 11in;  /* width height */
        margin: 0.25in;
    }
    .item{
        width: 260px;height: 220px;float: left;margin: 4px;
        font-family: 'open sans condensed';
        border: none;



    }
    .imagen{
        width: 167px;
        height: 100px;
        margin: auto;
        margin-top: 10px;
    }
    .desactivado{
        color: #bbc;
    }
    .titl {
        font-family: 'open sans condensed';
        font-weight: bold;
        text-shadow: -2px 2px 1px rgba(0, 0, 0, 0.25);
        color:#0088CC;
        margin-top: 20px;
    }

    </style>
</head>

<body>
<div class="dialog">
    <div style="text-align: center;"><h1 class="titl" style="font-size: 20px;">SEGUIMIENTO Y EJECUCIÓN DE PROYECTOS DE OBRAS Y CONSULTORÍAS<br>
            GOBIERNO AUTÓNOMO DESCENTRALIZADO PROVINCIA DE PICHINCHA</h1>
        <h1 class="titl" style="font-size: 24px; color: #06a">Registro de Ofertas en Línea</h1></div>
        <h3 style="font-size: 16px; color: #06a; text-align: center">Incluye Módulo VAE</h3>
    <div class="body ui-corner-all" style="width: 600px;position: relative;margin: auto;margin-top: 0px;height: 383px;
    background: #2080b0;">
        <img src="${resource(dir: 'images', file: 'oferentes2.jpeg')}"/>

                        %{--<g:link  controller="proyecto" action="list" title="Gestión de proyectos">--}%
%{--
        <div  class="ui-corner-all  item fuera">
            <div  class="ui-corner-all ui-widget-content item">
                        <div class="imagen">
                            <img src="${resource(dir: 'images', file: 'apu1.png')}" width="100%" height="100%"/>
                        </div>
                        <div class="texto"><b>Precios unitarios y análisis de precios</b>: registro y mantenimiento de
                        ítems y rubros. Análisis de precios, rendimientos y listas de precios...</div>
                    </div>
                </div>
</g:link>

<g:link  controller="asignacion" action="asignacionesCorrientesv2"  id="${session.unidad.id}" title="Programación del gasto corriente">

                <div  class="ui-corner-all item fuera">
                    <div  class="ui-corner-all ui-widget-content item">
                        <div class="imagen">
                            <img src="${resource(dir: 'images', file: 'obra100.png')}" width="100%" height="100%"/>
                        </div>
                        <div class="texto"><b>Obras</b>: registro de Obras, georeferenciación, los volúmenes de obra,
                        variables de transporte y costos indirectos ...</div>
                    </div>
                </div>
</g:link>


<g:link  controller="entidad" action="arbol_asg"  id="${session.unidad.id}" title="Plan Anual de Compras - gasto corriente ">

                <div  class="ui-corner-all item fuera">
                   <div  class="ui-corner-all ui-widget-content item">
                       <div class="imagen">
                            <img src="${resource(dir: 'images', file: 'compras.png')}" width="100%" height="100%"/>
                       </div>
                        <div class="texto"><b>Compras Públicas</b>: plan anual de contrataciones, gestión de pliegos y
                        control y seguimiento del PAC de obras ...</div>
                    </div>
                </div>
</g:link>


<g:link  controller="documento" action="list" title="Documentos de los Proyectos">

               <div  class="ui-corner-all  item fuera">
                   <div  class="ui-corner-all ui-widget-content item">
                       <div class="imagen">
                           <img src="${resource(dir: 'images', file: 'fiscalizar.png')}" width="100%" height="100%"/>
                       </div>
                       <div class="texto"><b>Fiscalización</b>: seguimiento a la ejecución de las obras: incio de obra,
                       planillas, reajuste de precios, cronograma ...</div>
                   </div>
               </div>
</g:link>

<g:link  controller="documento" action="list" title="Documentos de los Proyectos">

            <div  class="ui-corner-all  item fuera">
                <div  class="ui-corner-all ui-widget-content item">
                    <div class="imagen">
                        <img src="${resource(dir: 'images', file: 'reporte.png')}" width="100%" height="100%"/>
                    </div>
                    <div class="texto"><b>Reportes</b>: formatos pdf, hoja de cálculo, texto plano y html.
                    obras, concursos, contratos, contratistas, avance de obra...</div>
                </div>
            </div>
</g:link>

<g:link  controller="documento" action="list" title="Documentos de los Proyectos">

            <div  class="ui-corner-all  item fuera">
                <div  class="ui-corner-all ui-widget-content item">
                    <div class="imagen">
                        <img src="${resource(dir: 'images', file: 'oferta.png')}" width="100%" height="100%"/>
                    </div>
                    <div class="texto"><b>Documentos de la Oferta</b>: generación de los documentos para respaldo de la oferta
                    a entregar: rubros, volúmenes de obra y cronograma </div>
                </div>
            </div>
</g:link>



    </div>
--}%
    <div style="height: 25px;width: 100%;text-align: right;float: right;">&copy; TEDEIN S.A. Versión ${message(code: 'version', default: '0.1.0x')}</div>
</div>
<script type="text/javascript">
    $(".fuera").hover(function(){
        var d =  $(this).find(".imagen")
        d.width(d.width()+10)
        d.height(d.height()+10)
//        $.each($(this).children(),function(){
//            $(this).width( $(this).width()+10)
//        });
    },function(){
        var d =  $(this).find(".imagen")
        d.width(d.width()-10)
        d.height(d.height()-10)
    })
</script>
</body>
</html>

%{--
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <title></title>
    </head>
    <body>
    </body>
</html>--}%
