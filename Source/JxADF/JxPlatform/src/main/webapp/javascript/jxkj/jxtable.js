function bindTableEvent(tableid) {
}

function beforeDataTableLoad(tableId) {
}
function afterLoadDataTable(tableId, dtTable) {
    var $table = $("table", "#div_" + tableId);
    if ($table.length > 1) {
        $table.addClass("table-layout-fixed");
    }
}
/**
 * 将table变成dataTable
 *
 * @param tableId
 * @options 参数配置
 */
function loadDataTable(tableId, options) {
    beforeDataTableLoad(tableId);

    var forceScorll = false;
    var table = $("#" + tableId);

    if (table.length > 0 && $.fn.DataTable.fnIsDataTable(table[0])) {
        return;
    }

    if (!table.is(":visible")) {
        //当表单处于不可见状态时，以目前的dom结构无法确定实际宽度。
        return;
    }
    var tHead = $("thead:first tr:first", table); // 表head
    // 比如设备看板，这些特殊的页面处理
    if (null == tHead || tHead.length == 0) {
        return;
    }

    var multiHeader = $("thead:first tr", table).length > 1;
    var pageScrollHeight = document.documentElement.scrollHeight;
    var bottomHeight = table.closest(".fragment-mode").find(".bottom").outerHeight();
    var tableHeight = table.outerHeight();
    var bodyHeight = $("tbody", table).height();
    var pageHeight = JxUtil.getClientHeight();
    var scroll = JxUtil.getScroll(document.documentElement).scrollY;
    var isZeroRecord = $("tbody tr", table).length == 0;

    // 可以指定是否需要scroll
    if (options) {
        if (options["scroll"] != undefined && !options["scroll"]) {
            scroll = false;
        }
    }

    // 补边
    //var paddingHeight = 0;
    //if (multiHeader) {
    //    paddingHeight = 80;
    //}
    //if (options && options["paddingHeight"]) {
    //    paddingHeight = options["paddingHeight"];
    //}
    var height = bodyHeight - (pageScrollHeight - pageHeight) - 2;
    debug(tableId + "表格的高度为：" + height);

    if (options && options["height"]) {
        scroll = true;
        height = options["height"];
    }

    //使用自定义属性来强行指定高度,优先级最高
    if (table.attr("sheight")) {
        scroll = true;
        forceScorll = true;
        height = table.attr("sheight");
    }

    if (height < 150) {
        if (isZeroRecord) {
            height = 42;
        } else {
            height = 150;
        }
    }

    var dataTableOption = {
        "aaSorting": [],
        "bAutoWidth": false,
        "bPaginate": false,
        "bLengthChange": false,
        "bFilter": false,
        "bSort": false,
        "bInfo": false,
        "bStateSave": true,
        "bDeferRender": false,
        "oLanguage": {
            "sEmptyTable": getLangString("table.empty")
        }
    };

    var uid = getUrlParam("uid");

    if (!forceScorll && uid && uid > 0) {
        scroll = false;
    }

    // 只有在列表页面下，才需要Y轴滚动
    if ((table.attr("inputmode") != "EDIT" || forceScorll) && table.attr("jboname") != 'TOP_ATTACHMENT' && scroll) {
        dataTableOption["sScrollY"] = height;
        if(table.closest(".fragment-mode").is(".fragment-mode-inline")){
            dataTableOption["sScrollX"] = "100%";
        }
        dataTableOption["sDom"] = "tS";
        dataTableOption["fnInitComplete"] = function (obj) {
            var timer;
            $(window).on("resize.resizeDatatable", function () {
                clearTimeout(timer);
                timer = setTimeout(function(){
                    resizeDataTable(table);
                },200);
            });
            var rowIdx = 0;
            if (jx_appType && jx_appType == "list") {
                var fromUid = getUrlParam("fromUid");
                if ("" != fromUid) {
                    var trs = $("tr[uid!='']", table);
                    for (var i = 0; i < trs.length; i++) {
                        if ($(trs[i]).attr("uid") == fromUid) {
                            rowIdx = i;
                            break;
                        }
                    }
                }
            } else {

            }
            // 定位到行
            obj.oScroller.fnScrollToRow(rowIdx);
        };
    }

    var fixedWidth = table.attr("fixedWidth");
    if (fixedWidth && fixedWidth > JxUtil.getClientWidth()) {
        table.css({
            "width": fixedWidth
        });
    }

    var dttable = table.dataTable(dataTableOption);

    if (top.window.dataTableCollection) {
        //用于帮助解决jquery.layout.js布局的情况下west open/close时列表头部因为datatable.js固定死了
        top.window.dataTableCollection[location.href + tableId] = table;
    }
    afterLoadDataTable(tableId, dttable);
}

function resizeDataTable(table){
    if(!table.closest(".fragment-mode").is(".fragment-mode-inline")){
        table.fnAdjustColumnSizing();
    }
}

function tableFilterQuickSearch(me, e) {
    e = $.event.fix(e || window.event);
    if (e.keyCode == "13") {
        $(me).blur();
    }
}

/**
 * appbar 中的quicksearch功能
 * @param me
 * @param e
 */
function tableQuickSearch(me, e) {
    e = $.event.fix(e || window.event);
    var value = $(me).val();
    if (e.keyCode == "13") {
        dwr.engine.setAsync(false);
        WebClientBean.tableQuickSearch(jx_appNameType, value, {
            callback: function () {
                var appbar = $(me).closest("table");
                var div = appbar.siblings("div");
                if (div.length > 0) {
                    var id = div.attr("id");
                    getTableData(id);
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
        dwr.engine.setAsync(true);
    } else {
        if ("" == value) {
            $(me).siblings(".placeholder").css("display", "");
        } else {
            $(me).siblings(".placeholder").css("display", "none");
        }
    }
}
/**
 * 展开行更多页面
 *
 * @param me
 * @param type
 *            路径
 */
function expandRow(me, type) {
    // 获取tbody,tr,和tr内部td的个数
    var $button = $(me);
    var tbody = $button.closest("tbody");
    var tr = $button.closest("tr");
    var tdlen = $("td", tr).length;
    var uid = tr.attr("uid");

    // 是否已经打开的扩展编辑界面
    var expandTr = $("tr[expand=1]");
    if (expandTr.length > 0) {
        // 切换按钮状态
        $(".btn_expand.expanded").removeClass("expanded");
        // 移除扩展编辑界面，重置扩展标记
        $(".table_expand_tr").remove();
        expandTr.removeAttr("expand");
        // 如果是关闭的扩展编辑界面属于当前行，则只进行关闭动作。
        if (uid == expandTr.attr("uid")) {
            return;
        }
    }

    var expHtml = '<tr id="exp_tr_' + uid + '" class="table_expand_tr"><td colspan="' + tdlen + '" id="exp_t' + uid + '"><iframe scrolling="no" frameborder="0" src="index_' + type + '.action?uid=' + uid + '" onload="expandFrameLoaded(this)" /></td></tr>';
    tr.after(expHtml).attr("expand", "1");
    $button.addClass("expanded");
}

function expandFrameLoaded(frame) {
    var $frame = $(frame);
    var $body = $("body", frame.contentDocument);
    frame.height = $body.height();
    frame.contentWindow.JxUtil.isExpandContext = true;
    frame.contentWindow.JxUtil.expandSourceRow = $frame.closest("tr").prev();
    $body.addClass("expandContext");
}

function setExpandSourceRowText($source, text) {
    if (!JxUtil.expandSourceRow) return;
    var attr = $source.attr("dataattribute");
    var displayName = $source.attr("displayname");
    var $target = JxUtil.expandSourceRow.find("[dataattribute*='" + (displayName ? "." + displayName : attr) + "']");
    if ($target.length) {
        var $children = $target.children();
        if ($children.length) {
            if ($children.is("input")) {
                $children.val(text)
            } else if ($children.is("span")) {
                $children.attr("title", text).text(text);
            }
        }
    }
}

/**
 * 从收件箱链接跳转到应用程序
 * @param obj
 * @param event
 * @param trUid
 */
function goWfApp(obj, event, trUid) {

}

/**
 * *处理分页 将剪贴板内的内容向文本域粘贴时，会触发 onpaste 事件。
 */
function pagePaste() {
    return false;
}

/**
 * *处理分页 触发 onblur 事件。
 */
function pageBlur(dom, tableId) {
    var $dom = $(dom);
    var value = $dom.val();
    var origin = $dom.attr("originvalue");
    if (value != origin) {
        pageGoto(tableId);
    }
}

function pageKeypress(dom, e) {
    e = $.event.fix(e || window.event);
    if (e.keyCode == 13) {
        dom.blur();
    }
    return (e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode == 8);
}

function pageKeydown(tableId, e) {
    e = $.event.fix(e || window.event);
    if (e.keyCode == 13) {
        pageGoto(tableId);
    }
}

function pageSpanMouseover(me) {
    /*    me.style.color = '#F00';
     me.style.cursor = 'hand';*/
}

function pageSpanMouseout(me) {
    /*    me.style.color = '#000';
     me.style.cursor = 'default';*/
}

function pageGotoKeypress(tableId, e) {
    var event = e || window.event;
    if (event.keyCode == 13) {
        pageGoto(tableId);
    }
    return (event.keyCode >= 48 && event.keyCode <= 57 || event.keyCode == 46);
}

function spanSetPageSize(tableId, pSize) {
    var psid = document.getElementById(tableId + "_pagesize");
    if (psid) {
        psid.value = pSize;
    }
    document.getElementById(tableId + "_topage").value = '1';
    pageGoto(tableId);// 去到第一页
}

function pageGoto(tableId) {
    getTableData('div_' + tableId, null, null);
}

function pageNext(tableId) {
    var pnid = document.getElementById(tableId + "_pagenum");// 当前页
    var tpid = document.getElementById(tableId + "_topage");// 去到第几页
    if (typeof (pnid) != 'undefined') {
        tpid.value = pnid.value - (-1);
    }
    pageGoto(tableId);
}

function pagePre(tableId) {
    var pnid = document.getElementById(tableId + "_pagenum");// 当前页
    var tpid = document.getElementById(tableId + "_topage");// 去到第几页
    if (typeof (pnid) != 'undefined') {
        tpid.value = pnid.value - 1;
    }
    pageGoto(tableId);
}

function pageFirst(tableId) {
    var pnid = document.getElementById(tableId + "_pagenum");// 当前页
    var tpid = document.getElementById(tableId + "_topage");// 去到第几页
    if (typeof (pnid) != 'undefined') {
        tpid.value = 1;
    }
    pageGoto(tableId);
}

function pageLast(tableId) {
    var pnid = document.getElementById(tableId + "_pagenum");// 当前页
    var pcid = document.getElementById(tableId + "_pagecount");// 总页数
    var tpid = document.getElementById(tableId + "_topage");// 去到第几页
    if (typeof (pnid) != 'undefined') {
        tpid.value = pcid.value;
    }
    pageGoto(tableId);
}

function pageSort(me, e) {
    var tableId = $(me).attr("tid");
    var attributename = $(me).attr("dataattribute");
    var columnName = $(me).attr("sortname");
    if (tableId == null || tableId == '') {
        return;
    }
    if (columnName == null || columnName == '') {
        columnName = attributename;
    }
    if (columnName == null || columnName == '') {
        return;
    }
    var ob = document.getElementById(tableId + "_pagesort");// 排序信息
    if (typeof (ob) != 'undefined') {
        var obs = ob.value;// 原来的排序信息
        var ad = "asc";
        if (obs.indexOf(' asc') > 0) {
            ad = "desc";
        }
        ob.value = columnName + " " + ad;
    }
    pageGoto(tableId);
}

// ----------------------------------------------------

function addComtop(me, e) {
    var xUrl = comtopUrl + $(me).attr("mxevent_desc");
    window.location.href = xUrl;
}

// ----------------------COMTOP------------------------
/*
 * 选择当前页中所有记录项时的显示效果 param checkbox checkbox对象引用 param name 列表中的checkbox的name属性值
 * param oddBgColor 奇行记录项的背景颜色 param evenBgColor 偶行记录项的背景颜色 param highLightcolor
 * 高亮时的背景颜色 history 2005-11-20 胡文杰 新建
 */
function ckPageSelectHandler(checkbox, name, oddBgColor, evenBgColor, highLightcolor) {
    var form = checkbox.form;
    if (!oddBgColor)
        oddBgColor = '#ffffff';
    if (!evenBgColor)
        evenBgColor = '#ffffff';
    if (!highLightcolor)
        highLightcolor = 'lightblue';
    var prefix = 'tr_' + name.split("_")[1] + "_";
    var objs = document.getElementsByName(name);
    for (var i = 0; i < objs.length; i++) {
        var el = objs[i];
        // if(el.name!='allbox' && !el.disabled && el.name!='selectall' ) {
        if (el.disabled || el.type != "checkbox") {
            continue;
        }
        var objTRArray = []; // 支持多行变色
        var trArray = document.all[prefix + i];
        if (trArray) {
            if (trArray.length == undefined) {
                objTRArray.push(trArray);
            } else {
                objTRArray = trArray;
            }
        }
        el.checked = checkbox.checked;
        // 得到row对象并设定颜色
        if (el.checked) {
            for (var k = 0; k < objTRArray.length; k++) {
                objTRArray[k].bgColor = highLightcolor;
            }
        } else {
            for (var k = 0; k < objTRArray.length; k++) {
                objTRArray[k].bgColor = ((i + 1) % 2 == 0) ? evenBgColor
                    : oddBgColor;
            }
        }
    }
}

/*
 * 当列表中,每条记录前为checkbox时，选中列表中某条记录时的处理代码 param checkbox checkbox对象引用 param index
 * 行数的索引,即logic:iterate标签indexId属性值(注：比如3行,其index分别为0,1,2) param oddBgColor
 * 奇数行的背景颜色 param evenBgColor 偶数行的背景颜色 param highLightcolor 高亮背景颜色 history
 * 2005-7-27 胡文杰 新建
 */
function ckOneSelect(checkbox, e, index, oddBgColor, evenBgColor, highLightcolor, allboxName) {
    e = $.event.fix(e || window.event);
    e.stopPropagation();
    ckOneSelectHandler(checkbox, index, oddBgColor, evenBgColor, highLightcolor, allboxName);
}

function ckOneSelectHandler(checkbox, index, oddBgColor, evenBgColor, highLightcolor, allboxName) {
    // 改变相应checkbox状态（checked or unchecked）
    ckChangeCheckboxState(checkbox, allboxName);
    // 改变显示效果
    ckChangeSelectedRowView(checkbox, index, oddBgColor, evenBgColor,
        highLightcolor);
}

/*
 * 改变checkbox的状态，选中一条记录时，复选框联动(即当前页列表选择checkbox全选完后，选择当前页的checkbox也自动选择) param
 * checkbox checkbox对象引用 history 2005-11-20 胡文杰 新建
 */
function ckChangeCheckboxState(checkbox, allboxName) {
    var form = checkbox.form;
    if (!checkbox.checked) {
        if (!allboxName && !form.allbox) {
            return;
        }
        if (!form.allbox) {
            document.getElementsByName(allboxName)[0].checked = false;
        } else {
            form.allbox.checked = false;
        }

        if (form.selectall) {
            form.selectall.checked = false;
        }
    } else {
        var i = 0;
        var objs = document.getElementsByName(checkbox.name);
        var iLength = objs.length;
        var result = true;
        for (i < 0; i < iLength; i++) {
            result = result && objs[i].checked;
        }

        if (form.allbox == undefined) {
            if (allboxName != undefined) {
                document.getElementsByName(allboxName)[0].checked = result;
            }
        } else {
            form.allbox.checked = result;
        }
        if (form.selectall != null) {
            form.selectall.checked = result;
        }
    }
}

/*
 * 改变当前记录对应的行的显示效果，奇偶行颜色可定制、前端为checkbox多选版本 param checkbox checkbox对象引用 param
 * index 行数的索引,即logic:iterate标签indexId属性值(注：比如3行,其index分别为0,1,2) param
 * oddBgColor 奇数行的背景颜色 param evenBgColor 偶数行的背景颜色 param highLightcolor 高亮背景颜色
 * history 2005-7-27 胡文杰 新建
 */
function ckChangeSelectedRowView(checkbox, index, oddBgColor, evenBgColor, highLightcolor) {
    if (!oddBgColor)
        oddBgColor = '#ffffff';
    if (!evenBgColor)
        evenBgColor = '#ffffff';
    if (!highLightcolor)
        highLightcolor = 'lightblue';
    var objs = document.getElementsByName(checkbox.name);
    var objTR = null;
    // tr页面对象的id前缀 <tr id='tr_0'>.... 其与listTableRow中编码要对应，而页面开发人员不需要关注此事
    var table = checkbox.parentElement;
    while (table.tagName != "TABLE") {
        table = table.parentElement;
        if (table.tagName == "BODY") {
            break;
        }
    }
    var prefix = 'tr_' + table.id + "_";
    // 是否为全选但前页面所有纪录
    var flag = index;
    if (objs == null) {
        return;
    }
    if (objs.length == null) {
        return;
    }
    for (var j = 0; j < objs.length; j++) {
        var objTRArray = []; // 支持多行变色
        var trArray = document.all[prefix + j];
        if (trArray) {
            if (trArray.length == undefined) {
                objTRArray.push(trArray);
            } else {
                objTRArray = trArray;
            }
        }
        for (var k = 0; k < objTRArray.length; k++) {
            var objTR = objTRArray[k];
            // 找到当前记录，改变其背景颜色
            if (flag == -1)
                index = j;
            if (j == index && checkbox.checked) {
                objTR.bgColor = highLightcolor;
                // objTR.style.color="#0066ff";

            } else {
                // 如果不是当前记录，则以序号的不同设置不同的背景颜色
                if (!objs[j].checked) {
                    objTR.bgColor = ((j + 1) % 2 == 0) ? evenBgColor
                        : oddBgColor;
                    // objTR.style.color="#000000";
                }
            }
        }
    }
}