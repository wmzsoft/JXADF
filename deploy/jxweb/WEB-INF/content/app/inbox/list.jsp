<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="{app.inbox.title}" />
<jxui:body appName="inbox" appType="list">
	<jxui:form id="inbox">
		<jxui:appbar/>
		<jxui:fragment id="listtable" type="list-table" />
	</jxui:form>
<script type='text/javascript'>
    function gotoAppFromInbox(me,e,myid){
        var mytr = $("[uid='"+myid+"']");
        if (mytr){
            var ownid = mytr.find("[ownerid]").attr("ownerid");
            var myappurl=mytr.find("[dataattribute='WFINBOXAPP.DESCRIPTION']").find("a");
            if (myappurl){
                var myurl = myappurl.attr("href");
                if (myurl){
                    if (myurl.indexOf('.action?')>0){
                        myurl = myurl.replace(".action?","_main.action?uid="+ownid);
                    }else if (myurl.indexOf(".action")>0){
                        myurl = myurl.replace(".action","_main.action?uid="+ownid);
                    }else{
                        return ;
                    }
                    window.location.href=myurl;
                }
            }
        }
    }
</script>
</jxui:body>

