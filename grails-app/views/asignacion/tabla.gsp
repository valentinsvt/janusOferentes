<div class="tituloTree" style="width: 800px;">
    Asignaciones del año   <g:select name="anios" id="tabla_anio" from="${janus.pac.Anio.list(sort: 'anio')}" value="${actual.anio}" optionKey="anio" optionValue="anio"></g:select>
</div>
<table class="table table-bordered table-striped table-condensed table-hover">
    <thead>
    <tr>
        <th>Partida</th>
        <th>Año</th>
        <th>Valor</th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${asignaciones}" var="asg">
        <tr>
            <td>${asg.prespuesto.descripcion} (${asg.prespuesto.numero})</td>
            <td style="text-align: center">${asg.anio.anio}</td>
            <td style="text-align: right">
                <g:formatNumber number="${asg.valor}" format="##,##0" minFractionDigits="2" maxFractionDigits="2" locale="ec"/>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>

<script type="text/javascript">
    $("#tabla_anio").change(function(){
        $.ajax({type : "POST", url : "${g.createLink(controller: 'asignacion',action:'tabla')}",
            data     :   "anio="+$("#tabla_anio").val(),
            success  : function (msg) {
                $("#list-Asignacion").html(msg)
            }
        });
    })
</script>