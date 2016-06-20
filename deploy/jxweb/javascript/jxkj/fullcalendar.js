
function myFullCalendar(myid){
	var md = $('#'+myid).attr('month');
	if (md=='' || md==null){
		var myDate = new Date();
		md = myDate.getFullYear()+'-'+(myDate.getMonth()+1);  
	}
	$('#'+myid).fullCalendar({
    	header: {
      	left: 'prev,next',
      	center: 'title',
        right: 'month,basicWeek,basicDay'
      },
      lang:userLangeCode,
      defaultDate: md+'-01',
      defaultView: 'month',
      weekNumbers:true,
      editable: false,
      fixedWeekCount:false,
      height: window.innerHeight-120,  
      dayClick: function(date, jsEvent, view){
    	  calendarDayClick($('#'+myid),date,jsEvent,view);
      },
      eventClick: function(calEvent, jsEvent, view){
    	  calendarEventClick($('#'+myid),calEvent,jsEvent,view);
      },
      windowResize: function(view) {
      	 $('#'+myid).fullCalendar('option', 'height', window.innerHeight-120);  
      }
    });		
	
	$('.fc-prev-button').click(function(event) {
		loadCalendarData($('#'+myid),event);
	});
	$('.fc-next-button').click(function(event) {
		loadCalendarData($('#'+myid),event);
	});
	loadCalendarData($('#'+myid));
}

/**
 * 点击日历空白处，新建事件。
 * @param me
 * @param date
 * @param jsEvent
 * @param view
 */
function calendarDayClick(me,date, jsEvent, view){
	if ($(me).attr('view')=='readonly'){
		return;
	}
	var eurl = $(me).attr('eventurl');
	if (eurl.indexOf("?")>0){
		eurl = eurl +"&";
	}else{
		eurl = eurl +"?";
	}
	eurl = eurl + "flag=add&day="+date.format();
	appDialog(jx_appName,jx_appType,$(me).attr('id'),eurl,800,550,refreshCalendar);
}

/**
 * 点击日历事件
 * @param me
 * @param calEvent
 * @param jsEvent
 * @param view
 */
function calendarEventClick(me,calEvent, jsEvent, view){
	var eurl = $(me).attr('eventurl');
	if (eurl==null || eurl==""){
		return;
	}
	if (calEvent.id==null || calEvent.id==""){
		return;
	}
	if (eurl.indexOf("?")>0){
		eurl = eurl +"&";
	}else{
		eurl = eurl +"?";
	}
	eurl = eurl + "uid="+calEvent.id+"&view="+$(me).attr('view');
	if ($(me).attr('view')=='readonly'){
		appDialog(jx_appName,jx_appType,$(me).attr('id'),eurl,800,550);      			
	}else{
		appDialog(jx_appName,jx_appType,$(me).attr('id'),eurl,800,550,refreshCalendar);   
	}   			
}

/**
 * 加载日历数据
 * @param me
 * @param e
 */
function loadCalendarData(me,e){	
	var source = $(me).attr("dataurl");	
	var i=0;
	while (true){
		if (source==null || source==""){
			return;
		}
		$(me).fullCalendar('removeEventSource',source);
		var moment =  $(me).fullCalendar('getDate');
		if (moment!="" && moment!=null){
			var m = getUrlParamNameValue(source,"month");
			if (m!="" && m!=null){
				source=source.replace(m,"month="+moment.format());
			}else{
				if (source.indexOf("?")>0){
					source = source + "&month="+moment.format();
				}else{
					source = source + "?month="+moment.format();
				}
			}
		}
		var vbgcolor=$(me).attr("backgroundColor"+i);
		if (vbgcolor==null || vbgcolor==""){
			$(me).fullCalendar('addEventSource', source);
		}else{
			var vtextColor=$(me).attr("textColor"+i);
			if (vtextColor==null || vtextColor==""){
				vtextColor="#FFFFFF";
			}
			$(me).fullCalendar('addEventSource', {
				url:source,
				backgroundColor:vbgcolor,
				textColor:vtextColor
			});
		}			
		if (i==0){
			$(me).attr("dataurl",source);
		}else{
			$(me).attr("dataurl"+i,source);
		}
		i++;
		source = $(me).attr("dataurl"+i);
	}
}

/**
** 刷新日历
**/
function refreshCalendar(me,e){
	var cid = $(me).attr('target').id;
	if (cid !=null && cid !=""){
		if (cid.length>3){
			var calendar= cid.substring(0,cid.length-3);
			$("#"+calendar).fullCalendar('refetchEvents');
		}
	}
}
