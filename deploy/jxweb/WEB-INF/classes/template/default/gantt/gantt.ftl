<#--
/**
$id:gantt$
$author:wmzsoft@gmail.com
#date:2014.02
**/
-->
<div id="${parameters.id}" style='width:${parameters.width!'100%'}; height:${parameters.height!'500px'};' <#rt>
 scale="${parameters.scale!'week'}" <#rt>
 dataScale="${parameters.dateScale!''}" step="${parameters.step!''}" <#rt>
 columns="${parameters.columns!''}" <#rt>
 <#if (parameters.readonly??)>
    dReadonly="${parameters.readonly?string}" <#rt>
 </#if>
 ></div><#rt>
<#if (parameters.jboset??) && (parameters.jboset.jbolist??) >
    <script type="text/javascript">
        var tasks_${parameters.id} = {
            data:[
                <#list parameters.jboset.jbolist as jbo>
                    {id:"${jbo.getString(parameters.taskId!'')!''}",<#rt>
                     <#if (jbo.getString(parameters.duration!'')??)>
                        duration:"${jbo.getString(parameters.duration)!''}",<#t>
                     </#if>
                     <#if (jbo.getString(parameters.start!'')??)>
                        start_date:"${jbo.getString(parameters.start)!''}",<#t>
                     </#if>
                     <#if ((jbo.getString(parameters.finish!'')??) && (jbo.getString(parameters.start!'')??))>
                        end_date:"${jbo.getString(parameters.finish)!''}",<#t>
                     </#if>
                     <#if (jbo.getString(parameters.progress!'')??)>
                        progress:"${jbo.getString(parameters.progress)!''}",<#t>
                     </#if>
                     <#if (jbo.getString(parameters.parentId!'')??)>
                        parent:"${jbo.getString(parameters.parentId)!''}",<#t>
                     </#if>
                     <#if (jbo.getString(parameters.wbs!'')??)>
                        wbs:"${jbo.getString(parameters.wbs)!''}",<#t>
                     </#if>
                     text:"${jbo.getString(parameters.name!'')!''}"<#t>
                    }<#t>
                    <#if jbo_has_next>,<#t></#if>
                </#list>
            ],
            links:[]
        };
        $(function(){
        	var paramIdObj = $("#${parameters.id}");
    		dhtmlxGantt(paramIdObj, event);
        	gantt.parse(tasks_${parameters.id});
        });
    	
    </script>
<#else>
    没有发现数据。
</#if>