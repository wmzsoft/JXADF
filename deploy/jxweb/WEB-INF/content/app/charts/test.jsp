<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="HighChart测试页面" >
<script src="<%=path%>/javascript/Highcharts-3.0.9/js/highcharts.js"></script>
<script src="<%=path%>/javascript/Highcharts-3.0.9/js/modules/exporting.js"></script>
</jxui:head>
<jxui:body appName="msplan" appType="list">
<jxui:highchart height="300px"
	chart="{type: 'line'}"
	title="{text: '销售情况表'}"
	xAxis="{categories:'select to_char(enddate\\u002C\\u0027yyyy-mm\\u0027) as name from ChartTest group by to_char(enddate\\u002C\\u0027yyyy-mm\\u0027)'}"
	yAxis="{title: {text: '元'},plotLines: [{value: 0,width: 1,color: '#808080'}]}"
	tooltip="{valueSuffix: '元'}"
	legend="{layout: 'vertical',align: 'right',verticalAlign: 'middle',borderWidth: 0}"
	series="[{
            name: '$SQLDATA_COLUMN',
            data: 'select sum(mcost) as 物料成本,sum(pcost) as 人工成本,sum(sales) as 销售,sum(tax) as 税 from charttest group by to_char(enddate\\u002C\\u0027yyyy-mm\\u0027) order by to_char(enddate\\u002C\\u0027yyyy-mm\\u0027)'
        }]"
	/>
</jxui:body>