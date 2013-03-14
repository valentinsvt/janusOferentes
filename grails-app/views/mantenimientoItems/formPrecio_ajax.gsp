<%@ page import="janus.PrecioRubrosItems" %>

<div id="create-precioRubrosItemsInstance" class="span" role="main">
    <g:form class="form-horizontal" name="frmSave" action="savePrecio_ajax">
        <g:hiddenField name="id" value="${precio?.id}"/>
        <g:hiddenField name="item.id" value="${precio?.itemId}"/>

        <div class="tituloTree">
            Nuevo precio de ${precio?.item.nombre}
        </div>

        <div class="control-group">
            <div>
                <span class="control-label label label-inverse">
                    Precio Unitario
                </span>
            </div>

            <div class="controls">
                <div class="input-append">
                    <g:field type="number" name="precio" class=" required input-small" value="${fieldValue(bean: precio, field: 'precio')}"/>
                    <span class="add-on" id="spanPeso">
                        $
                    </span>
                </div>
                por ${precio.item.unidad.descripcion}
                <span class="mandatory">*</span>

                <p class="help-block ui-helper-hidden"></p>
            </div>
        </div>

    </g:form>
</div>

<script type="text/javascript">
    $("#frmSave").validate({
        rules          : {
            fecha : {
                remote : {
                    url  : "${createLink(action:'checkFcPr_ajax')}",
                    type : "post",
                    data : {
                        item : "${precio.itemId}"
                    }
                }
            }
        },
        messages       : {
            fecha : {
                remote : "Ya existe un precio para esta fecha"
            }
        },
        errorPlacement : function (error, element) {
            element.parent().find(".help-block").html(error).show();
        },
        success        : function (label) {
            label.parent().hide();
        },
        errorClass     : "label label-important"
    });
</script>
