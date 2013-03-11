
<%@ page import="janus.TipoPeriodo" %>

<div id="show-tipoPeriodo" class="span5" role="main">

    <form class="form-horizontal">
    
    <g:if test="${tipoPeriodoInstance?.codigo}">
        <div class="control-group">
            <div>
                <span id="codigo-label" class="control-label label label-inverse">
                    Codigo
                </span>
            </div>
            <div class="controls">
        
                <span aria-labelledby="codigo-label">
                    <g:fieldValue bean="${tipoPeriodoInstance}" field="codigo"/>
                </span>
        
            </div>
        </div>
    </g:if>
    
    <g:if test="${tipoPeriodoInstance?.descripcion}">
        <div class="control-group">
            <div>
                <span id="descripcion-label" class="control-label label label-inverse">
                    Descripcion
                </span>
            </div>
            <div class="controls">
        
                <span aria-labelledby="descripcion-label">
                    <g:fieldValue bean="${tipoPeriodoInstance}" field="descripcion"/>
                </span>
        
            </div>
        </div>
    </g:if>
    
    </form>
</div>
