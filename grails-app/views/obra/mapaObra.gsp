<%--
  Created by IntelliJ IDEA.
  User: fabricio
  Date: 1/31/13
  Time: 11:21 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <link href='${resource(dir: "css", file: "print.css")}' rel='stylesheet' type='text/css' media="print"/>

        <script type="text/javascript"
                src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBpasnhIQUsHfgCvC3qeJpEgcB9_ppWQI0&sensor=true"></script>

        <style>

        #mapaPichincha img {

            max-width : none;;
        }

        </style>

        <title>Localización Geográfica de la Obra</title>
    </head>

    <body>

        <div class="datosObra span12" style="margin-bottom: 20px">

            <div class="span3" style="margin-left: -30px; margin-right: 30px;">
                <span id="nombreObra" class="control-label label label-inverse">
                    Nombre de la Obra:
                </span>
            </div>

            <div class="span5" style="margin-left: -50px; font-size: large">${obra?.nombre}</div>

        </div>


        <div id="mapaPichincha" style="width: 700px; height: 500px">

        </div>


        <div class="" style="margin-top: 20px">

            <div class="coordenadasOriginales span12">

                <div class="span3" style="margin-left: -30px; margin-right: 30px;">
                    <span id="coordOrig" class="control-label label label-inverse">
                        Coordenadas de la Obra:
                    </span>
                </div>

                <div class="span2" style="margin-left: -50px">
                    Latitud:
                    ${formatNumber(number: obra?.latitud, format: '####.##', minFractionDigits: 5, maxFractionDigits: 8, locale: 'ec')}
                </div>


                <div class="span3">
                    Longitud:
                    ${formatNumber(number: obra?.longitud, format: '####.##', minFractionDigits: 5, maxFractionDigits: 8, locale: 'ec')}
                </div>
            </div>


            <div class="btn-group" style="margin-top: 20px; margin-left: 250px">
                <button class="btn noprint" id="btnVolver"><i class="icon-arrow-left"></i> Regresar</button>
                <button class="btn noprint" id="btnImprimir" onClick="window.print()"><i class="icon-print"></i> Imprimir
                </button>
            </div>

            <script type="text/javascript">

                var map;
                var lat;
                var longitud;
                var latorigen;
                var longorigen;
                var lastValidCenter;
                //    var allowedBounds;

                var countryCenter = new google.maps.LatLng(-0.15, -78.35);

                var allowedBounds = new google.maps.LatLngBounds(
                        new google.maps.LatLng(-0.41, -79.56),
                        new google.maps.LatLng(-0.50, -76.44),
                        new google.maps.LatLng(-0.28690, -76.59190)
                );

                var marker = new google.maps.Marker({
                    position  : countryCenter,
                    draggable : true

                });

                function initialize() {

                    var latitudObra = ${obra?.latitud};

                    var longitudObra = ${obra?.longitud};

                    var myOptions = {
                        center             : countryCenter,
                        zoom               : 7,
                        maxZoom            : 16,
                        minZoom            : 8,
                        panControl         : false,
                        zoomControl        : true,
                        mapTypeControl     : false,
                        scaleControl       : false,
                        streetViewControl  : false,
                        overviewMapControl : false,

                        mapTypeId : google.maps.MapTypeId.ROADMAP //SATELLITE, ROADMAP, HYBRID, TERRAIN
                    };

                    map = new google.maps.Map(document.getElementById('mapaPichincha'), myOptions);

                    limites2();

                    var posicion;

                    if (latitudObra == 0 || longitudObra == 0) {
                        posicion = new google.maps.LatLng(-0.21, -78.52)
                    } else {
                        posicion = new google.maps.LatLng(latitudObra, longitudObra)
                    }

                    var marker2 = new google.maps.Marker({
                        map       : map,
                        position  : posicion,
                        draggable : false
                    });
                }

                function limites2() {

                    google.maps.event.addListenerOnce(map, 'idle', function () {
                        allowedBounds = map.getBounds();
                    });
                    google.maps.event.addListener(map, 'drag', function () {
                        checkBounds();
                    });
                }

                function checkBounds() {
                    if (!allowedBounds.contains(map.getCenter())) {
                        var C = map.getCenter();
                        var X = C.lng();
                        var Y = C.lat();
                        var AmaxX = allowedBounds.getNorthEast().lng();
                        var AmaxY = allowedBounds.getNorthEast().lat();
                        var AminX = allowedBounds.getSouthWest().lng();
                        var AminY = allowedBounds.getSouthWest().lat();
                        if (X < AminX) {
                            X = AminX;
                        }
                        if (X > AmaxX) {
                            X = AmaxX;
                        }
                        if (Y < AminY) {
                            Y = AminY;
                        }
                        if (Y > AmaxY) {
                            Y = AmaxY;
                        }
                        map.panTo(new google.maps.LatLng(Y, X));
                    }
                }

                $(function () {
                    initialize();

                    $("#btnVolver").click(function () {
                        location.href = "${g.createLink(controller: 'obra', action: 'registroObra')}" + "?obra=" + "${obra?.id}";
                    });
                });

            </script>

    </body>
</html>