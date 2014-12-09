<%@ page import="janus.Item" %>
<style type="text/css">
.tab {
    height: 300px !important;
    overflow-x: hidden !important;
    overflow-y: hidden !important;
}

.inputVar {
    width: 65px;
}

    /*.sum1 {*/
    /*background : #adff2f !important;*/
    /*}*/

    /*.sum2 {*/
    /*border : solid 2px green !important;*/
    /*}*/
</style>

<g:if test="${flash.message}">
    <div class="span12" style="margin-bottom: 10px;">
        <div class="alert ${flash.clase ?: 'alert-info'}" role="status">
            <a class="close" data-dismiss="alert" href="#">×</a>
            ${flash.message}
        </div>
    </div>
</g:if>

<g:form controller="variables" action="saveVar_ajax" name="frmSave-var">
<div id="tabs" style="height: 360px">
<ul>
    %{--<li><a href="#tab-transporte">Variables de Transporte</a></li>--}%
    %{--<li><a href="#tab-factores">Factores</a></li>--}%
    <li><a href="#tab-indirecto">Costos Indirectos</a></li>
</ul>



<div id="tab-indirecto" class="tab">

    <g:hiddenField name="idObra" value="${obra?.id}"/>

%{--
    <div class="row-fluid">
        <div class="span10">
            Control y Administración (Fiscalización) - no se usa en obras nuevas
        </div>

        <div class="span2">
            <g:textField type="text" name="contrato" class="inputVar num"
                         value="${g.formatNumber(number: obra?.contrato, maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"/>
        </div>
    </div>
--}%

    <div class="row-fluid">
        <div class="span4">
            Dirección de obra
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosObra" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosObra), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="1"/>
        </div>

        <div class="span4">
            &nbsp;Promoción
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosPromocion" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosPromocion), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="7"/>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span4">
            Mantenimiento y gastos de oficina
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosMantenimiento" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosMantenimiento), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="2"/>
        </div>

        <div class="span4 bold">
            &nbsp;Gastos Generales (subtotal)
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceGastosGenerales" class="inputVar sum2 num"
                         value="${g.formatNumber(number: (obra?.indiceGastosGenerales), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         readonly=""/>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span4">
            Administrativos
        </div>

        <div class="span2">
            <g:textField type="text" name="administracion" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.administracion), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="3"/>
        </div>

        <div class="span4 bold">
            &nbsp;Imprevistos
        </div>

        <div class="span2">
            <g:textField type="text" name="impreso" class="inputVar  sum2 num"
                         value="${g.formatNumber(number: (obra?.impreso), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="8"/>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span4">
            Garantías
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosGarantias" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosGarantias), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="4"/>
        </div>

        <div class="span4 bold">
            &nbsp;Utilidad
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceUtilidad" class="inputVar sum2  num"
                         value="${g.formatNumber(number: (obra?.indiceUtilidad), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="9"/>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span4">
            Costos financieros
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosCostosFinancieros" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosCostosFinancieros), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="5"/>
        </div>

        <div class="span4 bold">
            &nbsp;Timbres provinciales
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosTimbresProvinciales" class="inputVar sum2 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosTimbresProvinciales?:2.5), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="10"  readonly=""/>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span4">
            Vehículos
        </div>

        <div class="span2">
            <g:textField type="text" name="indiceCostosIndirectosVehiculos" class="inputVar sum1 num"
                         value="${g.formatNumber(number: (obra?.indiceCostosIndirectosVehiculos), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         tabindex="6"/>
        </div>

        <div class="span4 bold" style="border-top: solid 1px #D3D3D3;">
            &nbsp;Total Costos Indirectos
        </div>

        <div class="span2">
            <g:textField type="text" name="totales" class="inputVar num"
                         value="${g.formatNumber(number: (obra?.totales) ?: 0, maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"
                         readonly=""/>
        </div>
    </div>

    <div>

    </div>

    <div style="border-top: solid 2px #D3D3D3">

    </div>

    <div class="row fluid">

        <div class="span2 bold" style="margin-top: 30px">

            Mecánico

        </div>

        <div class="span2" style="margin-top: 30px; margin-left: 40px">

            <g:textField name="mecanico" type="text" class="inputVar num"
                         value="${g.formatNumber(number: (janus.Precio.findByItemAndPersona(janus.Item.findByCodigo('009.001'), session.usuario)?.precio), maxFractionDigits: 2, minFractionDigits: 2, format: '##,##0', locale: 'ec')}"/>

        </div>

    </div>

</div>
</div>
</g:form>

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
                ev.keyCode == 190 || ev.keyCode == 110 ||
                ev.keyCode == 8 || ev.keyCode == 46 || ev.keyCode == 9 ||
                ev.keyCode == 37 || ev.keyCode == 39);
    }

    $(".sum1, .sum2, .num").keydown(function (ev) {
        if (ev.keyCode == 190 || ev.keyCode == 188) {
            if ($(this).val().indexOf(".") > -1) {
                return false
            }
        }
        return validarNum(ev);
    });

    function suma(items, update) {
        var sum1 = 0;
        items.each(function () {
            sum1 += parseFloat($(this).val());
        });
        update.val(number_format(sum1, 2, ".", ""));
    }





    $(function () {
        $(".sum1").keyup(function (ev) {
            suma($(".sum1"), $("#indiceGastosGenerales"));
            suma($(".sum2"), $("#totales"));
        }).blur(function () {
                    suma($(".sum1"), $("#indiceGastosGenerales"));
                    suma($(".sum2"), $("#totales"));
                });
        $(".sum2").keyup(function (ev) {
            suma($(".sum2"), $("#totales"));
        }).blur(function () {
                    suma($(".sum2"), $("#totales"));
                });
        $(".sum1").blur();
        $(".sum2").blur();
        $("#tabs").tabs({
            heightStyle: "fill"
        });

//        costoItem($("#cmb_vol"), $("#costo_volqueta"));
//        costoItem($("#cmb_chof"), $("#costo_chofer"));

/*
        $("#cmb_vol").change(function () {
            costoItem($(this), $("#costo_volqueta"));
        });
        $("#cmb_chof").change(function () {
            costoItem($(this), $("#costo_chofer"));
        });



*/

    });

</script>