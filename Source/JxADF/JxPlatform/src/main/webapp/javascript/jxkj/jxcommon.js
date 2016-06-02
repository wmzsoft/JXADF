var sUserAgent = navigator.userAgent.toLowerCase();
var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
var bIsMidp = sUserAgent.match(/midp/i) == "midp";
var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
var bIsAndroid = sUserAgent.match(/android/i) == "android";
var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
var bIsIE = sUserAgent.match(/msie/i) == "msie";
var bIsChrome = sUserAgent.match(/chrome/i) == "chrome";
var bIsFireFox = sUserAgent.match(/firefox/i) == "firefox";
var bIsSafari = sUserAgent.match(/version\/([\d.]+).*safari/)!=null;

function debug(obj) {
    if (window.console && window.console.log) {
        window.console.info(obj);
    }
}

// 判断是否为移动设备浏览器
function isMobile() {
    if (/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))) {
        if (window.location.href.indexOf("?mobile") < 0) {
            try {
                if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
                    return true;
                } else {
                    return false;
                }
            } catch (e) {
            }
        }
    }
    return false;
}

function isBootstrapRender() {
    return top.location.search && top.location.search.indexOf("renderer=bootstrap") > -1;
}

function resizeDialogWhenFrame(isInit) {
    if (!JxUtil.isTopWindow() && window.$currentDialog) {
        if ($currentDialog.dialog("isOpen")) {
            if (isInit && window.resizeRequired) {
                if (window.resizeWidth && window.resizeWidth != window.initWidth) {
                    $currentDialog.dialog("option", "width", window.initWidth);
                }
                if (window.resizeWidth && window.resizeHeight != window.initHeight) {
                    $currentDialog.dialog("option", "height", window.initHeight);
                }
            } else {
                var $document = $(document);
                var $win = $(window);
                var docW = $document.width();
                var docH = $document.height();
                var winW = $win.width();
                var winH = $win.height();
                var $parentWin = $(parent.window);
                window.resizeRequired = (docW < $parentWin.width() - 100 ) || ( docH < $parentWin.height() - 10);
                if (window.resizeRequired) {
                    if (docW > winW) {
                        window.resizeWidth = docW > window.initWidth ? docW + 30 : window.initWidth;
                        $currentDialog.dialog("option", "width", window.resizeWidth);
                    }

                    if (docH > winH) {
                        window.resizeHeight = docH > window.initHeight ? docH + 60 : window.initHeight;
                        $currentDialog.dialog("option", "height", window.resizeHeight);
                    }
                }
            }
        }
    }
}

var JxUtil = {
    extend: function (baseFun, extFun, needSuper) {
        if (needSuper) {
            return function () {
                baseFun.apply(this, arguments);
                extFun.apply(this, arguments);
            };
        } else {
            return function () {
                extFun.apply(this, arguments);
            };
        }
    },
    formatDate: function (num, type) {
        // GMT : Wed 22 Jul 2009 16:24:33 GMT
        // UTC : Tue Oct 12 00:00:00 UTC 0800 2010
        num = num + "";
        var date = "";

        var month = {};
        month["Jan"] = "01";
        month["Feb"] = "02";
        month["Mar"] = "03";
        month["Apr"] = "04";
        month["May"] = "05";
        month["Jun"] = "06";
        month["Jul"] = "07";
        month["Aug"] = "08";
        month["Sep"] = "09";
        month["Oct"] = "10";
        month["Nov"] = "11";
        month["Dec"] = "12";

        var week = {};
        week["Mon"] = "一";
        week["Tue"] = "二";
        week["Wed"] = "三";
        week["Thu"] = "四";
        week["Fri"] = "五";
        week["Sat"] = "六";
        week["Sun"] = "日";

        var str = num.split(" ");
        if (num.indexOf("GMT") > 0) {
            date = str[3] + "-";
            date = date + month[str[1]] + "-" + str[2];
            if (type == "dateTime") {
                date += " " + str[4];
            }
        } else if (num.indexOf("UTC") > 0) {
            date = str[5] + "-";
            date = date + month[str[1]] + "-" + str[2];
            if (type == "dateTime") {
                date += " " + str[3];
            }
        }
        // date=date+" 周"+week[str[0]];
        return date;
    },
    getClientWidth: function () {
        var clientWidth = 0;
        if (document.body.clientWidth && document.documentElement.clientWidth) {
            clientWidth = (document.body.clientWidth < document.documentElement.clientWidth) ? document.body.clientWidth
                : document.documentElement.clientWidth;
        } else {
            clientWidth = (document.body.clientWidth > document.documentElement.clientWidth) ? document.body.clientWidth
                : document.documentElement.clientWidth;
        }
        return clientWidth;
    },
    getClientHeight: function () {
        var clientHeight = 0;
        if (document.body.clientHeight && document.documentElement.clientHeight) {
            clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight
                : document.documentElement.clientHeight;
        } else {
            clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight
                : document.documentElement.clientHeight;
        }
        return clientHeight;
    },

    getScroll: function (el) {
        if (el && el.length > 1) {
            debug("只能对指定的单一元素进行判断");
            return;
        }
        var $el = $(el);
        var isXScroll = false;
        var isYScroll = false;
        if ($el.is("html") || $el.is("body")) {
            isXScroll = $("body")[0].scrollWidth > $(window).width();
            isYScroll = $("body")[0].scrollHeight > $(window).height();
        } else {
            isXScroll = el.scrollWidth > el.clientWidth;
            isYScroll = el.scrollHeight > el.clientHeight;
        }

        return {
            scrollX: isXScroll,
            scrollY: isYScroll
        };
    },
    // 返回val的字节长度
    getByteLen: function (val) {
        val = val.replace(/\./g, "").replace(/\,/g, "");

        var len = 0;
        for (var i = 0; i < val.length; i++) {
            if (val[i].match(/[^\x00-\xff]/ig) != null) // 全角
                len += 2;
            else
                len += 1;
        }
        return len;
    },
    // 返回val在规定字节长度max内的值
    getByteVal: function (val, max) {
        val = val.replace(/\./g, "").replace(/\,/g, "");

        var returnValue = '';
        var byteValLen = 0;
        for (var i = 0; i < val.length; i++) {
            if (val[i].match(/[^\x00-\xff]/ig) != null)
                byteValLen += 2;
            else
                byteValLen += 1;

            if (byteValLen > max)
                break;

            returnValue += val[i];
        }
        return returnValue;
    },
    isTopWindow: function () {
        return window.top == window.self;
    },
    adjustNumToFixString: function (num, length) {
        var reg;
        num = num.toString();
        if (num.length > length) {
            reg = new RegExp("\\d{" + length + "}$");
            return num.match(reg)[0];
        } else if (num.length == length) {
            return num;
        } else {
            var fixlength = length - num.length;
            var fixStr = "";
            for (var i = 0; i < fixlength; i++) {
                fixStr += "0";
            }
            return fixStr + num;
        }
    },
    // 用于expandType
    isExpandContext: false,
    expandSourceRow: null
};
/**
 * * 动态加载JS文件
 */
function loadJs(filename) {
    if ((typeof(JsAndCssList) == "undefined") || (JsAndCssList.indexOf("[" + filename + "]") == -1)) {
        var fileref = document.createElement('script');
        fileref.setAttribute("type", "text/javascript");
        fileref.setAttribute("src", filename);
        if (typeof fileref != "undefined") {
            document.getElementsByTagName("head")[0].appendChild(fileref);
            JsAndCssList = window.JsAndCssList + "[" + filename + "]";
        }
    }
};
/**
 * * 动态加载CSS文件
 */
function loadCss(filename) {
    if (JsAndCssList.indexOf("[" + filename + "]") == -1) {
        var fileref = document.createElement_x("link");
        fileref.setAttribute("rel", "stylesheet");
        fileref.setAttribute("type", "text/css");
        fileref.setAttribute("href", filename);
        if (typeof fileref != "undefined") {
            document.getElementsByTagName_r("head")[0].appendChild(fileref);
            JsAndCssList += "[" + filename + "]";
        }
    }
}

/**
 * * DWR 捕获错误信息
 */
function errorHandler(errorString, exception) {
    if (errorString == null || errorString == '' || errorString == 'Error') {
        alert(getLangString("jxcommon.unknowerror"));
    } else if (errorString.indexOf("Incomplete reply from server") >= 0) {
        // DWRSessionService 抛出的错误不用捕捉！
    } else {
        alert(errorString);
    }
}

/**
 * * DWR 捕获异常
 */
function exceptionHandler(exceptionString, exception) {
    if (exceptionString == null || exceptionString == ''
        || exceptionString == 'Error') {
        alert(getLangString("jxcommon.unknowerror"));
    } else {
        alert(exceptionString);
    }
}

/**
 * * 在屏幕右下角显示5秒的提示信息 * text ：消息内容 * title: 消息标题
 */
function msgTip(text, title) {
    if (text == '' || text == 'null' || text == null) {
        return;
    }
    var timerMax = 10;
    var dd = $("<div class='dialog' id='dialog-message'></div>");
    var dialogIcon = $("<span class='ui-icon ui-icon-circle-check' style='float: left; margin: 0 7px 0 0;'></span>");
    var dialogMsg = $("<p></p>");
    dialogMsg.text(text);
    dd.append(dialogIcon).append(dialogMsg);

    dd[0].timerMax = timerMax || 3;
    return dd.dialog({
        resizable: false,
        modal: true,
        show: {
            effect: 'fade',
            duration: 300
        },
        open: function (e, ui) {
            var me = this, dlg = $(this), btn = dlg.parent().find(".ui-button-text").text(getLangString("jxcommon.confirm") + "(" + me.timerMax + ")");
            --me.timerMax;
            me.timer = window.setInterval(function () {
                btn.text(getLangString("jxcommon.confirm") + "(" + me.timerMax + ")");
                if (me.timerMax-- <= 0) {
                    dlg.dialog("close");
                    window.clearInterval(me.timer); // 时间到了清除计时器
                }
            }, 1000);
        },
        title: title || getLangString("jxcommon.tip"),
        buttons: [{
            text: getLangString("jxcommon.confirm"),
            click: function () {
                var dlg = $(this).dialog("close");
                window.clearInterval(this.timer); // 清除计时器
            }
        }],
        close: function () {
            dd.remove();
            window.clearInterval(this.timer); // 清除计时器
        }
    });
}

/**
 * * 获得当前页面URL的地址
 */
function getUrlParam(name) {
    var result = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    if (result == null || result.length < 1) {
        return "";
    }
    return result[1];
}
/**
 * 返回参数值 2014-01-01
 *
 * @param url
 *            http://a.b.com/a?month=2014-01-01&b=c
 * @param name
 *            month
 * @returns
 */
function getUrlParamValue(url, name) {
    if (url == null || name == null) {
        return "";
    }
    var result = url.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    if (result == null || result.length < 1) {
        return "";
    }
    return result[1];
}

function getUrlParamNameValue(url, name) {
    if (url == null || name == null) {
        return "";
    }
    var result = url.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    if (result == null || result.length < 1) {
        return "";
    }
    var rs = result[0];
    if (rs.length > 1) {
        if (rs.charAt(0) == "?" || rs.charAt(0) == "&") {
            rs = rs.substring(1);
        }
    }
    return rs;
}

/**
 * 获得ContextPath,例:/jxtech
 *
 * @returns
 */
function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0, index + 1);
    return result;
}


String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.ltrim = function () {
    return this.replace(/(^\s*)/g, "");
};
String.prototype.rtrim = function () {
    return this.replace(/(\s*$)/g, "");
};


/**
 * * 模拟鼠标点击事件。示例：<INPUT id="test" onkeypress="doClick("buttonId", event)"> *
 * linkId 需要点击的元素ID e event内置对象
 */
function doClick(linkId, e) {
    var fireOnThis = document.getElementById(linkId);
    if (fireOnThis == null) {
        fireOnThis = window.parent.document.getElementById(linkId);
    }
    if (fireOnThis == null) {
        return;
    }
    if (document.createEvent) {
        var evObj = document.createEvent('MouseEvents');
        evObj.initEvent('click', true, false);
        fireOnThis.dispatchEvent(evObj);
    } else if (document.createEventObject) {
        fireOnThis.fireEvent('onclick');
    }
}

// 显示日期格式
function showDate(me, e) {
    var inputId = $(me).attr('tid');
    var $input = $('#' + inputId);
    showDatePicker(me, e, "datepicker", {
        'showButtonPanel': true,
        'currentText': getLangString("jxcommon.datepicker.today"),
        'closeText': getLangString("jxcommon.datepicker.close"),
        'dateFormat': 'yy-mm-dd',
        'onClose': function (dateText) {
            if (dateText != '') {
                $input.attr("changed", "1");
                if ($input.attr("inputmode") != "QUERYIMMEDIATELY") {
                    $input.blur();
                }
            }
            resizeDialogWhenFrame(true);
        },
        beforeShow: function (input) {
            setTimeout(function () {
                var $buttonPane = $input
                    .datepicker("widget")
                    .find(".ui-datepicker-buttonpane");
                $buttonPane.html("");
                $("<span/>", {
                    "class": "toolbar_btn",
                    "style": "float:left;",
                    text: getLangString("jxcommon.datepicker.today"),
                    click: function () {
                        var date = new Date();
                        $input.val(date.getFullYear() + "-" + JxUtil.adjustNumToFixString(date.getMonth() + 1, 2) + "-" + JxUtil.adjustNumToFixString(date.getDate(), 2));
                        $input.datepicker("hide");
                    }
                }).appendTo($buttonPane);
                $("<span/>", {
                    "class": "toolbar_btn",
                    "style": "float:right;",
                    text: getLangString("jxcommon.datepicker.clear"),
                    click: function () {
                        $.datepicker._clearDate(input);
                    }
                }).appendTo($buttonPane);
            }, 1);
        }
    });
}

// 显示日期时间格式
function showDateTime(me, e) {
    showDatePicker(me, e, "datetimepicker", {
        'dateFormat': 'yy-mm-dd',
        'showSecond': true
    });
}

// 显示时间格式
function showTime(me, e) {
    showDatePicker(me, e, "timepicker");
}

function showDatePicker(me, e, method, options) {
    var inputId = $(me).attr('tid');
    var $input = $('#' + inputId);
    options = $.extend({}, {
        'showButtonPanel': false,
        'duration': 0,
        'onClose': function (dateText) {
            if (dateText != '') {
                $input.attr("changed", "1");
                $input.blur();
            }
            resizeDialogWhenFrame(true);
        }
    }, options);
    $input[method](options);
    $input.datepicker("show").unbind("focus").bind("focus", function () {
        $(this).datepicker("show");
        resizeDialogWhenFrame();
    });
    resizeDialogWhenFrame();
}

// 添加记录并转到某个应用
function addTo(me, e, appName, appType) {
    var url = "app.action?app=" + appName;
    if (appType != null && appType != '') {
        url = url + "&type=" + appType;
    }
    window.location.href = url;
}

function addToMe(me, e) {
    addTo(me, e, jx_appName, null);
}

// 在当前页面添加信息
function addMe(me, e) {
    dwr.engine.setAsync(false); // 设置成同步
    // 添加之前先对当前的数据进行检查，看是否需要保存当前的记录
// if(undefined != jx_appNameType && null != jx_appNameType ){
    WebClientBean.checkDataChange(jx_appNameType, {
        callback: function (data) {
            if (data) {
                alert(getLangString("jxcommon.unsave"));
                dwr.engine.setAsync(true); // 重新设置成异步
                return;
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });

    // 执行添加数据
    WebClientBean.add(jx_appNameType, {
        callback: function (data) {
            if (!data) {
                alert(getLangString("jxcommon.addFail"));
                dwr.engine.setAsync(true); // 重新设置成异步
                return;
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    // 获得数据，更新到界面中。
    WebClientBean.getData(function (map) {
        var list = document.getElementsByTagName("input");
        for (var i = 0; i < list.length && list[i]; i++) {
            // 判断是否为文本框
            if (list[i].type == "text") {
                var attr = list[i].attributes["dataattribute"];
                if (attr) {
                    var v = map[attr.nodeValue];
                    if (v) {
                        list[i].value = v;
                    } else {
                        // list[i].value = '';
                    }
                }
            }
        }
    });
    dwr.engine.setAsync(true); // 重新设置成异步
}

function checkFieldValid(field) {
    var $field = $(field);
    if ($field.is("select:hidden")) {
        var $parent = $field.parent();
        if ($parent.css("position") != "position" || $parent.css("position") != "fixed") {
            $parent.css("position", "relative").addClass("form_td_select_simulate_hidden");
        }
    }
    var format = $(this).attr("format");

    if (format && format.length > 0 && $field.val() != "") {
        $field.val(accounting.unformat($field.val()));
    }

    var passValidation = !$field.validationEngine("validate", {
        focusFirstField: true,// TODO 没有效果
        maxErrorsPerField: 1,
        showOneMessage: true
    });
    $parent && $parent.removeClass("form_td_select_simulate_hidden");

    if (!passValidation) {
        $("html,body").animate({
            scrollTop: $(".formError").offset().top - 55
        }, 100);
    }
    return passValidation;
}

function checkPassValidation(me, e) {
    // 获取最近的表单
    var mainForm = $(me).closest("form");
    var passValidation = true;
    var validateElements = $("*[class *= 'validate[']", mainForm);
    validateElements.each(function () {
        if ($(this).is("select:hidden")) {
            var $parent = $(this).parent();
            if ($parent.css("position") != "position" || $parent.css("position") != "fixed") {
                $parent.css("position", "relative").addClass("form_td_select_simulate_hidden");
            }
        }
        var format = $(this).attr("format");

        if (format && format.length > 0 && $(this).val() != "") {
            $(this).val(accounting.unformat($(this).val()));
        }

        passValidation = !$(this).validationEngine("validate", {
            focusFirstField: true,// TODO 没有效果
            maxErrorsPerField: 1,
            showOneMessage: true
        });
        $parent && $parent.removeClass("form_td_select_simulate_hidden");
        return passValidation;
    });
    if (!passValidation) {
        $("html,body").animate({
            scrollTop: $(".formError").offset().top - 55
        }, 100);
    }
    return passValidation;
}

function duplicate(me,e){
    WebClientBean.duplicate(jx_appNameType, {
        callback: function (data) {
            if (data!=null) {
                showJboDetail(data);
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });    
}

// 点保存前事件
function beforeSave(me, e) {
    debug("beforeSave...");
    return true;
}
/**
 * 执行保存数据
 *
 * @param me
 * @param e
 */
function save(me, e) {
	if ($(me).attr("disabled") == 'disabled') {
        return;
    }	
	$(me).attr("disabled", "disabled");
    // 点保存前事件
    if (!beforeSave(me, e)) {
    	$(me).removeAttr("disabled");
        return;
    }
    var passValidation = checkPassValidation(me, e);
    if (passValidation) {
        // dwr.engine.setAsync(false); // 设置成同步
        submitData();// 提交所有数据
        // 执行保存
        WebClientBean.save(jx_appNameType, {
            callback: function (jbos) {
                // 检查是否定义了保存成功的回调函数
                var aSave = $(me).attr("afterSave");
                if (aSave != null && aSave != "") {
                    // 如果定义了，则先执行回调函数。
                    var afterSaveFun = eval(aSave);
                    new afterSaveFun(me, e);
                    // 检查执行回调函数之后是否继续。
                    if ($(me).attr("afterSaveContinue") != "Y") {
                        // 不用继续
                    	$(me).removeAttr("disabled");
                        return;
                    }
                }
                var flag = getUrlParam('flag');
                var myurl = window.location.href;

                // 如果是插件
                if (myurl.indexOf(".action") > 0 && myurl.indexOf("/app.action") < 0) {
                    if ("add" == flag) {
                        myurl = myurl.replace("flag=add", "uid=" + eval(jbos.jbo.uidValue));
                    }
                } else {
                    myurl = "app.action?app=" + jx_appName;
                    if ('add' == flag) {
                        // 重定向到具体的页面。
                        myurl = myurl + "&uid=" + eval(jbos.jbo.uidValue);
                    } else {
                        myurl = myurl + "&uid=" + getUrlParam("uid");
                    }
                }

                dwr.engine.setAsync(false);
                // 将信息放到后台保存起来
                WebClientBean.saveMessage(jx_appNameType, getLangString("jxcommon.saveSuccess"), {
                    callback: function () {
                        saveCallback(me,e,myurl);
                    },
                    errorHandler: errorHandler,
                    exceptionHandler: exceptionHandler
                });
                dwr.engine.setAsync(true); // 重新设置成异步
                $(me).removeAttr("disabled");
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
        // dwr.engine.setAsync(true); // 设置成异步
    }
    $(me).removeAttr("disabled");
}

function saveCallback(me,e,url) {
	if ($(me).attr("refresh")!='false'){
		window.location.href = encodeURI(url);
	}else{
		//显示保存成功的信息
		showToolbarMsg(getLangString("jxcommon.saveSuccess"));
	}
}
/**
 * 上一条记录
 *
 * @param me
 * @param e
 */
function previous(me, e) {
    dwr.engine.setAsync(false); // 设置成同步

    WebClientBean.previous(jx_appNameType, function (data) {
        if ("" != data) {
            showJboDetail(data);
        } else {
            alert(getLangString("jxcommon.notprevious"));
        }
    });
    dwr.engine.setAsync(true);
}

/**
 * 下一条记录
 *
 * @param me
 * @param e
 */
function next(me, e) {
    dwr.engine.setAsync(false); // 设置成同步
    WebClientBean.next(jx_appNameType, function (data) {
        if ("" != data) {
            showJboDetail(data);
        } else {
            alert(getLangString("jxcommon.notnext"));
        }
    });
    dwr.engine.setAsync(true);
}
// 上一条，下一条，根据uid，刷新地址栏的url
function showJboDetail(uid) {
    var url = window.location.href;

    var uidIdx = url.indexOf("?uid=");
    if (uidIdx < 0) {
        uidIdx = url.indexOf("&uid=");
    }

    if (uidIdx > 0) {
        // 见将url分成2部分。
        var prefix = url.substring(0, uidIdx + 1);
        var changeStr = url.substring(uidIdx, url.length);

        // 再来判断changeStr后半
        var tailIdx = changeStr.indexOf("&");
        var tailFix = "";
        if (tailIdx > 0) {
            tailFix = changeStr.substring(tailIdx, changeStr.length);
        }

        window.location.href = prefix + "uid=" + uid + tailFix;
    }
}

/**
 * *删除应用程序列表，无确认删除对话框
 */
function delListNoConfirm(me, e) {
    var chk_value = "";
    var tbid = $(me).attr("fragmentid");
    var chkname = "ck_" + tbid;
    $('input[name="' + chkname + '"]:checked').each(function () {
        chk_value += $(this).val() + ",";
    });
    if (chk_value.length == 0) {
        alert(getLangString("jxcommon.chooseRecordToDel"));
        return;
    }
    // 执行删除
    WebClientBean.delList($('#' + tbid).attr("jboname"), chk_value, {
        callback: function (data) {
            getTableData('div_' + tbid, e);
            if (!data) {
                alert(getLangString("jxcommon.delFail"));
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
}
/**
 * 在主页面删除当前记录
 *
 * @param me
 * @param e
 */
function del(me, e) {
    var confirmsg = $('<div id="delConfirm"></div>');
    confirmsg.attr("title", getLangString("jxcommon.delConfirmTitle"));
    confirmsg.html(getLangString("jxcommon.delConfirmMsg"));

    $(confirmsg).dialog(
        {
            resizable: false,
            height: 180,
            modal: true,
            buttons: [
                {
                    text: getLangString("jxcommon.confirm"),
                    click: function () {
                        // 执行删除
                        var jboname = $('#jboname').val();
                        var uid = $("#uid").val();

                        WebClientBean.delList(jboname, uid, {
                            callback: function (data) {
                                if (!data) {
                                    alert(getLangString("jxcommon.delFail"));
                                }
                                // 检查是否有回调函数
                                var aDel = $(me).attr("afterDel");
                                if (aDel != null && aDel != "") {
                                    // 如果有则执行
                                    var afterDelFun = eval(aDel);
                                    new afterDelFun(me, e);
                                    if ($(me).attr("afterDelContinue") != "Y") {
                                        // 如果不用继续，则直接退出。
                                        return;
                                    }
                                }
                                $("span[mxevent='gotoapp']").click();
                            },
                            errorHandler: errorHandler,
                            exceptionHandler: exceptionHandler
                        });
                        $(this).dialog("close");
                    }
                },
                {
                    text: getLangString("jxcommon.cancle"),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
}

/**
 * *删除应用程序列表，有确认删除对话框
 */
function dellist(me, e) {
    var chk_value = "";
    // 支持TAB多表
    var tbids = $(me).attr("fragmentid").split(",");
    for (var i = tbids.length - 1; i >= 0; i--) {
        if ($("#" + tbids[i]).length == 0) {
            tbids.splice(i, 1);
            continue;
        }
        var chkname = "ck_" + tbids[i];
        $('input[name="' + chkname + '"]:checked').each(function () {
            chk_value += $(this).val() + ",";
        });
    }
    if (chk_value.length == 0) {
        alert(getLangString("jxcommon.chooseRecordToDel"));
        return;
    }
    var confirmsg = $('<div id="delConfirm"></div>');
    confirmsg.attr("title", getLangString("jxcommon.delConfirmTitle"));
    confirmsg.html(getLangString("jxcommon.delConfirmMsg"));

    $(confirmsg).dialog(
        {
            resizable: false,
            height: 180,
            modal: true,
            buttons: [
                {
                    text: getLangString("jxcommon.confirm"),
                    click: function () {
                        // 执行删除
                        var jboname = $('#' + tbids[0]).attr("jboname");

                        WebClientBean.delList(jboname,
                            chk_value, {
                                callback: function (data) {
                                    if (tbids.length == 1) {
                                        getTableData('div_' + tbids[0], e);
                                    } else {
                                        window.location.reload();
                                    }
                                    if (!data) {
                                        alert(getLangString("jxcommon.delFail"));
                                    }
                                },
                                errorHandler: errorHandler,
                                exceptionHandler: exceptionHandler
                            });

                        $(this).dialog("close");
                    }
                },
                {
                    text: getLangString("jxcommon.cancle"),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
}

/**
 * *删除应用程序列表，有确认删除对话框
 */
function delOwer(me, e) {
    var chk_value = "";
    var tbid = $(me).attr("fragmentid");
    var chkname = "ck_" + tbid;
    $('input[name="' + chkname + '"]:checked').each(function () {
        chk_value += $(this).val() + ",";
    });
    if (chk_value.length == 0) {
        alert(getLangString("jxcommon.chooseRecordToDel"));
        return;
    }
    dwr.engine.setAsync(false); // 设置成同步
    var jboname = $('#' + tbid).attr("jboname");
    WebClientBean.checkJboOwer(jboname, chk_value, {
        callback: function (data) {
            if (data) {
                delList(me, e);
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    dwr.engine.setAsync(true); // 设置成同步
}

/**
 * * 弹出高级查询对话框
 */
function search(me, e) {
    if (me) {
        var fragmentid = me.attributes["fragmentid"].nodeValue;
        if (fragmentid) {
            var typeValue = 'list-search';
            if (me.attributes["apptype"]) {
                typeValue = me.attributes["apptype"].nodeValue;
            }
            fragmentid = "div_" + fragmentid;
            node = document.getElementById(fragmentid);
            if (node) {
                var urlValue = "app.action";

                // pushbutton自定义URL
                if (undefined != $(me).attr("url")) {
                    urlValue = $(me).attr("url");
                }
                appDialog(jx_appName, typeValue, fragmentid, urlValue);
            }
        }
    }
}

/**
 * * 执行高级查询
 */
function searchOk(me, e) {
    getTableData($("#fromid").val(), e, appDialogClose);
}
/**
 * * 查询全部数据
 */
function searchAll(me, e) {
    var funs = [function () {
        searchClear(me, e);
        searchQueue();
    }, function () {
        searchOk(me, e);
        searchQueue();
    }];
    var searchQueue = function () {
        $(document).dequeue("mySearch");
    };
    $(document).queue("mySearch", funs);
    searchQueue();
}

/**
 * * 清除查询框中的所有内容
 */
function searchClear(me, e) {
    // 使用jQuery选择器查找输入框并清空值
    $("input:text").val("");
    var selects = $("select");
    var len = selects.length;
    for (var j = 0; j < len; j++) {
        var select = selects.get(j);
        var nulloption = $("option[value='']", select);
        if (nulloption && nulloption.length > 0) {
            nulloption.attr("selected", true);
        } else {
            // selects.get(j).selectedIndex = 0;// 设置每个下拉列表选中第一个option
            // 如果没有请选择，清空后默认加上请选择 需要做国际化
            var op = getLangString("jxcommon.selectoption");
            $(selects.get(j)).append("<option selected value=''>" + op + "</option>");
        }
        $(select).trigger("change"); // select2清除显示
    }

    var chks = $("input[type='checkbox']");
    $.each(chks, function (idx, chk) {
        $(chk).removeAttr("checked");
    });

    WebClientBean.searchClear(jx_appNameType, '', {
        callback: function () {
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
}

function inputOnChange(me, e) {
    if (me == null)
        return;
    var $input = $(me);
    $input.attr("changed", "1");

    var lenid = $("#len" + me.id);
    if (lenid.length > 0) {
        // 针对textbox的
        var maxvalue = $(lenid).attr("maxvalue");
        if (maxvalue != null) {
            if (maxvalue - JxUtil.getByteLen(me.value) < 0) {
                me.value = JxUtil.getByteVal(me.value, maxvalue);
                $(lenid).val(0);
            } else {
                $(lenid).val(maxvalue - JxUtil.getByteLen(me.value));
            }

        }
    } else {
        var meClass = $input.attr("class");

        if (meClass && meClass.indexOf("custom[date") < 0) {
            // 针对普通的inputbox
            var length = $input.attr("length");
            if (length) {
                if (length - JxUtil.getByteLen(me.value) < 0) {
                    me.value = JxUtil.getByteVal(me.value, length);
                }
            }
        }
    }

    setExpandSourceRowText($input, $input.val());

    var im = $input.attr("inputmode");
    if (im == "QUERYIMMEDIATELY" || im == "QUERY") {
        e = $.event.fix(e || window.event);
        if (e.keyCode == 13 || $input.is(".hasDatepicker")) {
            inputQueryOnBlur(me, e);
        }
    }else if (bIsSafari && $input.attr("type")=='checkbox'){
        tableInputOnBlur(me, e);
    }
}

function checkOnChange(me, e) {
    if (me == null)
        return;
    $(me).attr("changed", "1");
    var tValue = $(me).attr("checked") == "checked" ? 1 : 0;
    if ($(me).attr("inputmode") == "QUERY") {
        dwr.engine.setAsync(false); // 设置成同步
        if (me == null) {
            return;
        }

        if (me.readOnly) {
            return;
        }

        if ($(me).attr("changed") == "0") {
            return;// 没有变化，不需要做任何事。
        } else {
            $(me).attr("changed", "0");
        }

        var cause = $(me).attr("dataattribute");
        if (cause==undefined || cause==''){
        	return;
        }
        cause += $(me).attr("cause");
        WebClientBean.inputQueryOnBlur(jx_appNameType, '', cause, tValue, {
            callback: function () {
                if ($(me).attr("inputmode") == "QUERYIMMEDIATELY") {
                    getTableData('div_' + $(me).attr("fragmentid"), e);
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });

        dwr.engine.setAsync(true); // 设置成同步
    } else {
        var cv = $(me).attr("chk");// 选中的值
        var nv = $(me).attr("notchk");// 未选中的值
        var v = $(me).val();// 当前值
        if (v == cv) {
            $(me).val(nv);
        } else {
            $(me).val(cv);
        }
        if (bIsSafari){
            inputOnBlur(me,e);
        }
    }
}
function inputOnFocus(me, e) {
    if (me == null) return;

    if (me.readOnly && $(me).attr("datatype") && "date" != $(me).attr("datatype")) {
        return;
    }

    var format = $(me).attr("format");
    if (format && format.length > 0 && $(me).val() != "") {
        var numberVal = accounting.unformat($(me).val());
        $(me).val(numberVal);
    }

}
/**
 * * 主表输入框失去焦点时执行。 * 将数据提交到后台的SESSION中。
 */
function inputOnBlur(me, e) {
    if (me == null) {
        return;
    }
    if (me.readOnly && $(me).attr("datatype") && "date" != $(me).attr("datatype")) {
        return;
    }

    var changed = $(me).attr("changed");
    if (changed == "0" || changed == null || changed == '') {
        return;// 没有变化，不需要做任何事。
    }
    var dt = $(me).attr("dataattribute");
    if (dt == undefined || dt==''){
    	return;
    }
    // 对当前的元素进行校验，返回false则是表示校验通过
    if (!$(me).validationEngine('validate')) {
        var meValue = $(me).val();

        var format = $(me).attr("format");
        if (format && format.length > 0 && meValue != "") {
            var accountVal = accounting.formatMoney(meValue);
            $(me).val(accountVal);
        }

        $(me).attr("changed", "0");
        // 修改描述控件的值
        var desidv = "#" + me.id + "_DESC";
        if ($(desidv).length == 0) {
            try {
                desidv = $(desidv, parent.document);
            } catch (e) {

            }
        }
        var relationship = $(me).closest("form").attr("relationship");
        relationship = (relationship == undefined ? "" : relationship);

        WebClientBean.inputOnBlur2(jx_appNameType, $(me).attr("dataattribute"),
            meValue, $(desidv).attr("dataattribute"), relationship, {
                callback: function (jbo) {
                    if (null != jbo) {
                        setData($(me).closest("table[class='table_edit']"), jbo, "");
                    }
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
    }
}

/**
 * * TABLE行中的输入框失去焦点执行的操作
 */
function tableInputOnBlur(me, e) {
    if (me == null) {
        return;
    }
    if (me.readOnly && $(me).attr("datatype") && "date" != $(me).attr("datatype")) {
        return;
    }
    var changed = $(me).attr("changed");
    if (changed == "0" || changed == null || changed == '') {
        return;// 没有变化，不需要做任何事。
    }
    $(me).attr("changed", "0");
    var attributeName = $(me).attr("dataattribute");
    var trid = me.id.substring(0, me.id.length - attributeName.length - 1);
    var tableid = trid.substring(0, trid.lastIndexOf("_"));
    trid = "tr_" + trid;
    // Jboname
    var jboname = $('#' + tableid).attr("jboname");
    var relationship = $('#' + tableid).attr("relationship");
    var uid = $('#' + trid).attr('uid');
    var value = $(me).val();
    // checkbox要特殊处理
    if ((me.tagName == 'input' || me.tagName == 'INPUT') && $(me).attr("type") == "checkbox") {
        value = $(me).attr("checked") == "checked" ? 1 : 0;
    }
    WebClientBean.tableInputOnBlur(jx_appNameType, attributeName,
        value, jboname, relationship, uid, {
            callback: function (jbo) {
                if (null != jbo) {
                    setData($(me).closest("tr"), jbo, "TABLE");
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
}

function inputQueryOnBlur(me, e) {
    if (me == null) {
        return;
    }
    if (me.readOnly) {
        return;
    }
    var $field = $(me);
    var tValue = $.trim($field.val());
    dwr.engine.setAsync(false); // 设置成同步

    if (($field.attr("changed") == "0") && !$field.is("select")) {
        return;// 没有变化，不需要做任何事。
    } else {
        $field.attr("changed", "0");
    }

    var attribute = $field.attr("dataattribute");
    if (attribute==undefined || attribute==''){
    	return;
    }
    var cause = $field.attr("cause");
    if (cause) {
        if (attribute.indexOf(".") != -1) {
            // 当为PS_PGC_MATERIAL_PLANSG_SERVICE_CONTRACT_ID.SERVICE_CONTRACT_NAME查询时,对relationship做处理
            WebClientBean.getRelationShipCause(jx_appNameType, attribute, cause, {
                callback: function (data) {
                    cause = data;
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
        } else {
            cause = attribute + cause;
        }
    } else {
        return;
    }

    WebClientBean.inputQueryOnBlur(jx_appNameType, '', cause, tValue, {
        callback: function () {
            if ($field.attr("inputmode") == "QUERYIMMEDIATELY") {
                getTableData('div_' + $field.attr("fragmentid"), e);
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });

    dwr.engine.setAsync(true); // 设置成同步
}

/**
 * 点击标签时，执行查询。
 *
 * @param me
 * @param e
 */
function labelsQuery(me, e) {
    if (me == null) {
        return;
    }
    var relationship = $(me).closest("form").attr("relationship");
    relationship = (relationship == undefined ? "" : relationship);
    var tb = $(me).parent();
    while (tb != null) {
        if ($(tb).attr("fragment") != null) {
            break;
        }
        tb = tb.parent();
    }
    var dt = $(me).attr("dataattribute");
    if (dt == undefined || dt==''){
    	return;
    }
    WebClientBean.inputQueryOnBlur(jx_appNameType, relationship
        , $(me).attr("dataattribute") + $(me).attr("cause")
        , $(me)[0].innerText, {
            callback: function () {
                getTableData($(tb)[0].id, e, null, null, 2);
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
}

/**
 * * 下拉选择对话框，值发生变化时执行的操作
 */
function selectChange(me, e) {
    var $select = $(me);
    if ($select.attr("inputmode")=='DISPLAY'){
        return;
    }
    if ($select.is("select:hidden")) {
        var $parent = $select.parent();
        if ($parent.css("position") != "position" || $parent.css("position") != "fixed") {
            $parent.css("position", "relative").addClass("form_td_select_simulate_hidden");
        }
    }
    var isInvalid = $select.validationEngine('validate');
    $parent && $parent.removeClass("form_td_select_simulate_hidden");
    if (isInvalid) {
        return false;
    }

    setExpandSourceRowText($select, $select.find("option:selected").text());
    var dt=$(me).attr("dataattribute");
    if (dt==undefined || dt==''){
    	return;
    }
    var im = $select.attr("inputmode");
    if (im == 'DISPLAY') {
        return;
    } else if (im == "QUERYIMMEDIATELY" || im == "QUERY") {
        dwr.engine.setAsync(false); // 设置成同步
        var workflowid = $select.attr("workflowid");
        var tableid = $select.attr("tableid");
        if (workflowid != null && tableid != null && workflowid != ""
            && tableid != "") {
            workflowQueryOnBlur(me);
        } else {
            inputQueryOnBlur(me, e);
        }
        // 处理触发器

        doPartialTriggers(me, e);
        /*
         * if (im == "QUERYIMMEDIATELY") { getTableData('div_' +
         * $(me).attr("fragmentid"), e); }
         */
        dwr.engine.setAsync(true); // 重新设置成异步
    } else {
        dwr.engine.setAsync(false);
        WebClientBean.inputOnBlur(jx_appNameType, $(me).attr("dataattribute"),
            me.value, {
                callback: function (jbo) {
                    if (jbo) {
                        var refresh = ($(me).attr("refresh")||"").toLowerCase();
                        if (null != jbo && refresh !='false') {
                            setData($select.closest("table[class='table_edit']"), jbo, "");
                        }
                    } else {
                        alert(getLangString("jxcommon.submitdataerror") + "JS-selectChange-inputOnBlur");
                    }
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
        doPartialTriggers(me, e);
        dwr.engine.setAsync(true);
    }
}
/**
 * 表格中的select控件发生变化
 *
 * @param me
 * @param e
 */
function tableSelectChange(me, e) {
    // 为了解决IE8下面的BUG，不能直接$(me).closest方法

    var table = $(me).parent().closest("table");
    var tr = $(me).parent().closest("tr");

    var uid = tr.attr("uid");
    var jboname = table.attr("jboname");
    var relationship = table.attr("relationship");
    var dataattribute = $(me).attr("dataattribute");
    if (dataattribute==undefined || dataattribute==''){
    	return;
    }
    // 一定要用me.value啊，不能使用$(me).val()，不兼容IE10以下的浏览器
    // 用me.value不支持多选，改为遍历
    var selval = new Array();
    $('option:selected', $(me)).each(function () {
        selval.push($(this).val());
    });
    dwr.engine.setAsync(false); // 设置成同步
    WebClientBean.tableInputOnBlur(jx_appNameType, dataattribute,
        selval.toString(), jboname, relationship, uid, {
            callback: function (jbo) {
                if (null != jbo) {
                    setData(tr, jbo, "TABLE");
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });

    // 处理触发器
    doPartialTriggers(me, e);

    dwr.engine.setAsync(true); // 设置成同步
}

// 处理触发器
function doPartialTriggers(me, e, dv, dn) {

    var partialTriggers = $(me).attr("partialTriggers");
    if (partialTriggers) {

        var trigs = partialTriggers.split(',');
        var i;
        for (i = 0; i < trigs.length; i++) {
            var trigTarget = $("#" + trigs[i]);

            // 支持表格中的select触发，id是随机生成，用同行的dataattribute定位目标
            if (trigTarget.length == 0) {
                trigTarget = $("[dataattribute='" + trigs[i].toUpperCase() + "']", $(me).closest("tr"));
            }

            // 需要判断触发对象的类型，分为select和form表单
            var tagName = '';
            if (trigTarget.length > 0){
                tagName= trigTarget[0].tagName;
            }
            if ("select" == tagName || "SELECT" == tagName) {
                var partialCause = trigTarget.attr("partialCause");
                if (partialCause != null) {
                    if ($(me).find("option:selected").val() == "") {
                        partialCause = "";// 获得最新的查询条件
                    } else {
                        partialCause = partialCause.replace(/\?/g, $(me).find(
                            "option:selected").val());// 获得最新的查询条件
                    }
                    var attrs = new Array();
                    attrs[0] = trigTarget.attr('displayvalue');
                    attrs[1] = trigTarget.attr('displayname');
                    var dv = attrs[0].toLowerCase();
                    var dn = attrs[1].toLowerCase();
                    dwr.engine.setAsync(false); // 设置成同步
                    WebClientBean.getJsonData(
                        trigTarget.attr('jboname'),
                        trigTarget.attr('relationship') || '',
                        (trigTarget.attr('wherecause') || '')
                        + partialCause,
                        trigTarget.attr('orderby'),
                        attrs,
                        {
                            callback: function (data) {
                                trigTarget.empty();
                                var opts = trigTarget.attr("options");
                                if (opts != null && opts.length > 0) {
                                    var optslist = opts.split(',');
                                    for (var m = 0; m < optslist.length; m++) {
                                        var ov = optslist[m].split(':');
                                        var ov1 = optslist[m];
                                        var ov2 = optslist[m];
                                        if (ov.length >= 2) {
                                            ov1 = ov[0];
                                            ov2 = ov[1];
                                        }
                                        var no = "<option value='"
                                            + ov1 + "'>" + ov2
                                            + "</option>";
                                        trigTarget.append(no);
                                    }
                                }
                                var options = eval('({items: [' + data
                                    + ']})');
                                for (var j = 0; j < options.items.length; j++) {
                                    var newOpts = "<option value='"
                                        + options.items[j][dv]
                                        + "'>"
                                        + options.items[j][dn]
                                        + "</option>";
                                    trigTarget.append(newOpts);
                                }
                                trigTarget.val(null).trigger("change");
                            },
                            errorHandler: errorHandler,
                            exceptionHandler: exceptionHandler
                        });
                    dwr.engine.setAsync(true); // 设置成同步
                    inputQueryOnBlur(trigTarget, e);
                    doPartialTriggers(trigTarget, e);
                }
            } else if ("form" == tagName || "FORM" == tagName) {
                // 处理整个表单,其实对应的是一条JBO信息
                var clause = $(me).attr("partialCause");
                var value = $(me).val();

                WebClientBean.getJboByCause($("#jboname").val(), clause, value, {
                    callback: function (jbo) {
                        if (null != jbo) {
                            var tableEdit = $(".table_edit", trigTarget);
                            setData(tableEdit, jbo, "");
                        }
                    },
                    errorHandler: errorHandler,
                    exceptionHandler: exceptionHandler
                });

            }
        }
    }
}

/**
 * 获得Table的数据 * divid :将表格加载到哪个DIV中,即fragment标签的ID * e:event事件 *
 * callback:回调函数，执行完成加载表格之后需要执行的函数。 * cCloums:需要显示哪些字段，为空则为默认字段 *
 * loadType:1：加载缓存数据，2：重新查询数据加载。3：表格保留分页，翻页信息等。
 */
function getTableData(divid, e, callback, cColumns, loadType) {
    if (divid == null || divid == '' || divid == 'div_') {
        callback();
        return;
    }
    var isparent = false;// DIV在本窗口（FALSE）在父窗口（TRUE）
    var mydiv = $('#' + divid);// 得到DIV对象
    if (mydiv == null || mydiv.length == 0) {
        // 当前的DOM中没有找到DIV,在父窗口中找
        mydiv = $('#' + divid, parent.document);
        isparent = true;
    }
    if (mydiv == null || mydiv.length == 0) {
        isparent = false;
        mydiv = $('#div_' + divid);
    }
    if (mydiv == null || mydiv.length == 0) {
        mydiv = $('#div_' + divid, parent.document);
        isparent = true;
    }

    if (mydiv) {
        if ((mydiv.attr("id")) == undefined) {
            if (null != callback) {
                callback();
            }
            return;
        }
        var tableId = (mydiv.attr("id")).substring(4);// 表格的ID为DIVID去掉前4个字符
        var urlValue = mydiv.attr("url");// 加载表格的URL地址
        var typeValue = mydiv.attr("type");// 加载表格的JSP页面
        var appname = null;// 应用程序名
        var apptype = null;// 应用程序类型
        var pagenumid = null;// 当前页
        var topageid = null;// 去到第几页
        var pagecoutid = null;// 总页数
        var oderbyid = null;// 排序信息
        var pagesizeid = null;// 页面大小
        var jboname = null;
        var relationship = null;

        if (isparent && parent != null) {
            appname = parent.jx_appName;
            apptype = parent.jx_appType;
            pagenumid = $("#" + tableId + "_pagenum", parent.document);// 当前页
            topageid = $("#" + tableId + "_topage", parent.document);// 去到第几页
            pagecoutid = $("#" + tableId + "_pagecount", parent.document);// 总页数
            oderbyid = $("#" + tableId + "_pagesort", parent.document);// 排序信息
            pagesizeid = $("#" + tableId + "_pagesize", parent.document);// 页面大小
            jboname = $("#" + tableId, parent.document).attr("jboname");
            relationship = $("#" + tableId, parent.document).attr("relationship");
        } else {
            appname = jx_appName;
            apptype = jx_appType;
            pagenumid = $("#" + tableId + "_pagenum");// 当前页
            topageid = $("#" + tableId + "_topage");// 去到第几页
            pagecoutid = $("#" + tableId + "_pagecount");// 总页数
            oderbyid = $("#" + tableId + "_pagesort");// 排序信息
            pagesizeid = $("#" + tableId + "_pagesize");// 页面大小
            jboname = $("#" + tableId).attr("jboname");
            relationship = $("#" + tableId).attr("relationship");
        }
        var topage = 1;
        if (topageid.length > 0) {
            topage = topageid.val();
            // modify by cxm topage > pagecoutid.val 都为string比较，结果不一定正确
            // 比如"2" >"12"，使用减号操作强制转化为int后做操作即可
            if (!isInteger(topage) || (topage - pagecoutid.val()) > 0
                || topage < 0) {
                topage = pagecoutid.val();
            }
        }
        if (pagenumid.length > 0) {
            pagenumid.val(topage);
        }

        if (urlValue == null || urlValue == '') {
            urlValue = "app.action";
        }

        if (cColumns == null || cColumns == '') {
            var tableth = null;
            if (isparent && parent != null) {
                thead = tableth = $('th', $("#" + tableId + " thead:first",
                    parent.document));
            } else {
                tableth = $('th:last', $("#" + tableId + " thead:first"));
            }
            if (tableth.length > 2) {
                cColumns = "";
                $.each(tableth, function () {
                    var th = $(this).attr('dataattribute');
                    if (th != undefined) {
                        if (th.length > 0)
                            cColumns = cColumns + th + ",";
                    }
                });
            }
        } else if (cColumns == 'init') {
            cColumns = '';
        }
        // 使用load方法加载html内容
        loadingMask(mydiv, mydiv.find("tr").length);
        mydiv.load(urlValue, {
            app: appname,
            type: typeValue,
            apptype: apptype,
            pagenum: topage == -1 ? 1 : topage,
            pagesize: pagesizeid.val() == -1 ? 20 : pagesizeid.val(),
            orderby: oderbyid.val(),
            custColumns: cColumns,
            jboname: jboname,
            relationship: relationship,
            url: urlValue,
            loadType: loadType
        }, function (response, status, xhr) {
            loadingUnmask(mydiv);
            if (status == "error") {
                mydiv.html("<div class='text-center' style='color:#f30;padding:20px 0;'>loading error</div>");
            } else {
                var $tbody = $("tbody", mydiv);
                $("tr[tobeadd='true']", mydiv).each(function () {
                    $(this).find("select").removeAttr("disabled");
                    $tbody.prepend(this);
                });

                $("tr[toBeDel='true']", mydiv).each(function () {
                    delrow($(this).find("[mxevent='delrow']"));
                });
                bindTableEvent(tableId);

                if (callback != null) {
                    callback();
                }
                // 当应用为附件,设置内容在iframe框架中自动增长
                if (appname == "attachment") {
                    resetIframeHeight(divid);
                }
            }
        });
    }
}


function afterFragmentLoad(tableId) {
}
/**
 * * 转到应用程序
 */
function gotoapp(me, e) {
    var appvalue = $(me).attr("appName");
    var typevalue = $(me).attr("appType");
    var appurl = $(me).attr("url");
    if (appurl == null) {
        appurl = "app.action?app=" + appvalue + "&type=" + typevalue + "&fromUid=" + $("#uid").val();
    } else if ($(me).attr("localparams") == 'Y') {
        appurl = appurl + location.search;
    }
    var paramid = $(me).attr("paramid");
    if (paramid != null && paramid != "") {
        appurl = appurl + $("#" + paramid).val();
    }
    if ($(me).attr("context") != null) {
        appurl = contextPath + appurl;
    }
    var target = $(me).attr("target");

    dwr.engine.setAsync(false); // 设置成同步

    WebClientBean.checkDataChange(jx_appNameType, function (data) {
        if (data) {
            var confirmsg = $('<div id="delConfirm"></div>');
            confirmsg.attr("title", getLangString("jxcommon.dataChangeConfirmTitle"));
            confirmsg.html(getLangString("jxcommon.dataChangeConfirmMsg"));

            $(confirmsg).dialog(
                {
                    resizable: false,
                    width: 320,
                    height: 200,
                    modal: true,
                    buttons: [
                        {
                            text: getLangString("jxcommon.confirm"),
                            click: function () {
                                $(this).dialog("close");
                                WebClientBean.rollback(jx_appNameType, {
                                    callback: function (data) {
                                        if (target == null) {
                                            location.href = appurl;
                                        } else {
                                            windowopen(appurl, target);
                                        }
                                    },
                                    errorHandler: errorHandler,
                                    exceptionHandler: exceptionHandler
                                });
                            }
                        },
                        {
                            text: getLangString("jxcommon.cancle"),
                            click: function () {
                                $(this).dialog("close");
                            }
                        }
                    ]
                });
        } else {
            if (target == null) {
                location.href = appurl;
            } else {
                windowopen(appurl, target);
            }
        }
    });
    dwr.engine.setAsync(true);
}

/**
 * * 打开应用程序对话框 * app ：应用程序名 appType:应用程序类型 fromid：关闭对话框时，需要修改的控件ID
 * urlValue:自定义URL路径 w :width 对话框的宽度 h :height 对话框的高度
 */
function appDialog(app, appType, fromid, urlValue, w, h, beforeDialogClose, title) {

    var dialogCenterPoint = $("<div id='dialogCenterPoint' class='dialog_center_point'></div>");
    $(document.body).append(dialogCenterPoint);

    var page = "";
    if (urlValue == null || urlValue == '') {
        urlValue = "app.action";
        page = urlValue + "?app=" + app + "&type=" + appType + "&fromid=" + fromid;
    } else if ("" == app && "" == appType) {
        page = urlValue + "?fromid=" + fromid;
    } else if (urlValue.indexOf("?") < 0) {
        page = urlValue + "?app=" + app + "&type=" + appType + "&fromid=" + fromid;
    } else {
        page = urlValue + "&app=" + app + "&type=" + appType + "&fromid=" + fromid;
    }
    if (page.indexOf("?") > 0 && page.indexOf("=") > 0 && page.indexOf("&") > 0) {
        var temppara = "";
        var urlarr = page.split("?");
        var dialogurl = urlarr[0];
        var parastr = urlarr[1].split("&");
        for (var x = 0; x < parastr.length; x++) {
            var paraarr = parastr[x].split("=");
            var pname = paraarr[0];
            var pvalue = paraarr[1];
            if (x == 0) {
                pname = 'fromUid' == pname ? pname : pname.toLowerCase();
                temppara = "?" + pname + '=' + pvalue;
            } else {
                pname = 'fromUid' == pname ? pname : pname.toLowerCase();
                temppara = temppara + "&" + pname + '=' + pvalue;
            }
        }
        page = dialogurl + temppara;
    }

    var width = 100, height = 150;

    $(".dialog").remove();

    // 对话框层
    var $dialog = $("<div class='dialog'></div>");
    $dialog.attr("id", fromid + "div");
    // 等待层
    var $waitting = $("<div class='dialog_waitting'></div>");

    // 内容层
    var $content = $("<iframe frameBorder='0' style='overflow:visible' width='100%' onload='appdialogIframeLoaded(this)' height='100%'></iframe>");
    $dialog.append($waitting).append($content);
    $content.attr("id", fromid + "frame").attr("src", page);
    fixIEProcessBar();
    if (w > 0) {
        width = w;
    }
    if (h > 0) {
        height = h;
    }
    if (isMobile()){
        width="98%";
    }
    $content.attr("pwidth", width);
    $content.attr("pheight", height);
    var options = {
        autoOpen: false,
        modal: true,
        height: height,
        width: width,
        title: title ? title : "",
        resizable: true,
        draggable: true,
        beforeClose: beforeDialogClose == null ? dialogBeforeClose : beforeDialogClose,
        close: dialogClose,
        resize: function (event, ui) {
            if (isMobile()){
                $content.attr("width", "96%");
            }else{
                $content.attr("width", ui.size.width - 30);
            }
            
        }
    };


    $dialog.dialog(options);

    $dialog.dialog('open');

    // 强制设置居中显示
    $dialog.dialog('option', 'position', {my: 'center', at: 'center', of: dialogCenterPoint});
    $dialog.prev().find("button").blur();
}

function fixIEProcessBar() {
    // when multi iframes ,the processbar show loading forever;
    if (/msie/.test(navigator.userAgent.toLowerCase())) {
        var $fixIframe = $("<iframe style='display:none'/>").appendTo(top.document.body);
        $fixIframe[0].contentDocument.write("");
        $fixIframe[0].contentWindow.close();
    }
}

function appdialogIframeLoaded(me, page) {
    var iframeId = me.id;
    var dialogId = iframeId.substring(0, iframeId.length - 5);

    var appdialogDiv = $("#" + dialogId + "div");
    var body = me.contentDocument.body;
    var width = body.scrollWidth;
    var height = body.scrollHeight + 60;
    var pwidth = +$(me).attr("pwidth");
    var pheight = +$(me).attr("pheight");
    height = pheight == 150 ? height : pheight;

    width = pwidth > width ? pwidth : width;
    me.setAttribute("width", width + 10);
    width = width + 30;

    appdialogDiv.dialog({
        "width": width,
        "height": height
    });
    me.contentWindow.initWidth = width;
    me.contentWindow.initHeight = height;

    $(me).show();
    $(".dialog_waitting", $(me).parent()).hide();
    // for resizeDialogWhenFrame
    me.contentWindow.$currentDialog = appdialogDiv;

}

function dialogBeforeClose(event, ui) {
}
/* 对话框关闭 */
function dialogClose(event, ui) {
    $("#dialogCenterPoint").remove();

    var target = event.target;
    var targetId = target.id;
    if (targetId.length > 0) {
        targetId = targetId.substring(4); // 去掉前面div_
        targetId = targetId.substring(0, targetId.length - 3);// 去掉后面div

        if ($("#" + targetId).length > 0 && $("#" + targetId)[0].tagName == "TABLE") {
            loadDataTable(targetId);
        }
    }
}

/**
 * *关闭应用程序对话框
 */
function appDialogClose(me, e) {
    try {
        // 如果在当前页面无法获取nid元素，则从父页面获取nid
        var nid = "#fromid";
        if ($(nid).length == 0) {
            nid = $(nid, parent.document);
        }
        // 在其他系统调用该页面的时候，无法跨域访问，直接将该页面close掉就可以了。
        var divid = $(nid).val();
        if (divid && undefined != divid) {
            var ld = window.parent.$('#' + divid + 'div');
            ld.dialog('close');
            // ld.remove(); 这句会造成后面的js线程中断。
        } else {
            if (JxUtil.isTopWindow()) {
                window.opener = null;
                window.close();
            } else {
                var $dialog = window.parent.$('.dialog');
                if ($dialog.length) {
                    $dialog.dialog('close');
                }
            }
        }
    } catch (err) {
        debug("appDialogClose : " + err);
    }
}

function lookupAction(lookupapp, fromid, w, h) {
    w = w || 0;
    h = h || 0;
    // 直接写struts访问地址，用于OSGI
    if (lookupapp.toLowerCase().indexOf(".action") > 0) {
        appDialog(jx_appName, jx_appType, fromid, lookupapp, w, h);
    } else if (lookupapp.indexOf("?") > 0) {
        var params = lookupapp.split("?");
        if (params.length > 1) {
            appDialog(params[0], "lookup_list&" + params[1], fromid, null, w, h);
        }
    } else {
        appDialog(lookupapp, "lookup_list", fromid, null, w, h);
    }
}
/**
 * 目前有2中类型的弹窗 1：（编辑页面的字段lookup和表格中的字段lookup）；
 * 2：table模式（多选模式），针对整个table的弹窗操作（目前仅支持tablebutton标签)
 *
 * @param me
 * @param e
 */
function lookup(me, e) {
    var tag = $(me).attr("tag");
    if (tag && tag == "tablebutton") {
        var table = $(me).closest("table");
        var dtTableHeadWrap = table.closest(".dataTables_scrollHead");
        var allbox = $("input[type='checkbox'][name='allbox']", table);
        if (dtTableHeadWrap.length) {
            // 没有id说明是经过了datatable的处理
            table = dtTableHeadWrap.next().find("table");
        }
        if (allbox && allbox.length > 0) {
            var checkedJbos = $("input[type='checkbox'][name!='allbox']:checked", table);
            if (checkedJbos && checkedJbos.length > 0) {
                var uids = "";
                $.each(checkedJbos, function (idx, jbo) {
                    uids += $(jbo).val();
                    if (idx < checkedJbos.length - 1) {
                        uids += ",";
                    }
                });

                var fromId = $("#fromid").val();
                var pFrom = $("#" + fromId, parent.document);
                if (pFrom && pFrom.length > 0) {
                    var pTable = pFrom.closest("table");
                    var pJboname = pTable.attr("jboname");
                    var pRelationship = pTable.attr("relationship");
                    dwr.engine.setAsync(false); // 重新设置成异步
                    WebClientBean.lookup(jx_appNameType, table.attr("jboname"), table.attr("relationship"), uids,
                        parent.jx_appNameType, pJboname, pRelationship, {
                            callback: function (data) {
                                if ("success" == data) {

                                    var nid = "#fromid";
                                    if ($(nid).length == 0) {
                                        nid = $(nid, parent.document);
                                    }
                                    var divid = $(nid).val();
                                    var ld = window.parent.$('#' + divid + 'div');
                                    ld.dialog('close');

                                    parent.window.getTableData("div_" + pTable.attr("id"), null, null, null, 1);

                                    ld.remove();
                                }
                            },
                            errorHandler: errorHandler,
                            exceptionHandler: exceptionHandler
                        });
                    dwr.engine.setAsync(true); // 重新设置成异步

                }

            } else {
                alert(getLangString("jxcommon.notchoose"));
            }
        } else {
            debug("非多选的窗口，你干嘛非要用这种实现？ 请用默认实现方式!");
            return;
        }
    } else {
        // 默认模式 模式1
        var dataValue = $(me).attr('tid');
        var secTr = $(me).closest("tr"); // 获得选择行的tr
        var nid = "#fromid";
        if ($(nid).length == 0) {
            nid = $(nid, parent.document);
        }
        var dataattribute = $(nid).val();
        var divid = $(nid).val();
        var ld = window.parent.$('#' + divid + 'div');
        ld.dialog('close');
        var toid = '#' + divid;
        if ($(toid).length == 0) {
            toid = $(toid, parent.document);
        }

        $(toid).val(dataValue);
        $(toid).attr("changed", "1");
        // 设置第二个字段的
        var desc = $("#" + divid + "_DESC");
        if (desc.length == 0) {
            desc = $("#" + divid + "_DESC", parent.document);
        }

        if (desc.length != 0) {
            var dataattribute = desc.attr("dataattribute");
            if (dataattribute.indexOf(".") > 0) {
                dataattribute = dataattribute.split(".")[1];
            }
            // 获取desc
            var descValue = "";
            $("td", secTr).each(function (idx, dom) {
                if ($(this).attr("dataattribute") == dataattribute) {
                    descValue = $(this).attr("title");
                    return;
                }
            });
            desc.val(descValue);

        }
        var error = desc.closest("form").find(".formErrorContent");
        if (undefined != error)
            error.parent().remove();
        $(toid).focus();
        $(toid).blur();
        ld.remove();
    }
}
/**
 * 清除lookup的值
 *
 * @param me
 * @param e
 */
function lookupClear(me, e) {
    var inputs = $(me).siblings("input");

    $.each(inputs, function (idx, input) {
        $(input).focus();
        $(input).val("");
        $(input).attr("changed", 1);
        $(input).blur();
    });

    $(me).focus();
}


// 打开报表应用程序
function report(me, e) {
    var an = $(me).attr("appname");
    var tgid = $(me).attr("target");
    if (tgid != null && tgid.length > 0) {
        $("form:first").attr("target", tgid);
    }
    $("form:first").attr("action", reportUrl + an).submit();
}

/**
 * *选择工作流待办、已办下拉列表
 */
function selectAppInboxStatus(me, e) {
    dwr.engine.setAsync(false); // 设置成同步
    var wfstatus = $(me).val();
    var tbid = $(me).attr("fragmentid");
    if (wfstatus == '1' || wfstatus == '2') {
        $('#n_' + me.id).css('display', ''); // 任务选择列表
        $('#l_' + me.id).css('display', '');// “任务”两字
    } else {
        $('#n_' + me.id).css('display', 'none');// 任务选择列表
        $('#l_' + me.id).css('display', 'none'); // “任务”两字
    }
    // $('#n_'+me.id).val("");
    if (wfstatus == '1') {
        $('#r_n_' + me.id).css('display', '');// 发送工作流按钮
    } else {
        $('#r_n_' + me.id).css('display', 'none');// 发送工作流按钮
    }
    WebClientBean.selectAppInboxStatus(jx_appNameType, wfstatus, $(me).attr(
        "statusColumn"), $(me).attr("creatorColumn"), $(me).attr(
        "transactorColumn"), $(me).attr("backflagColumn"), {
        callback: function (data) {
            getTableData($(me).attr("fragmentId"));
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });

    dwr.engine.setAsync(true); // 重新设置成异步
}

/**
 * *选择工作流任务节点下拉列表
 */
function selectAppInboxNode(me, e) {
    dwr.engine.setAsync(false); // 设置成同步
    var v = $(me).val();
    WebClientBean.selectAppInboxNode(jx_appNameType, v, {
        callback: function () {
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    getTableData($(me).attr("fragmentId"));
    dwr.engine.setAsync(true); // 重新设置成异步
}

/**
 * 查看流程信息
 *
 * @param me
 * @param e
 * @param uid
 */
function showWorkflowInfo(me, e, uid) {

}

/**
 * *打开发送工作流对话框
 */
function routeDialog(me, e) {
    var chk_value = [];
    var tbid = $(me).attr("fragmentid");
    var sendid = me.id.substr(2);
    var chkname = "ck_" + tbid;
    $('input[name="' + chkname + '"]:checked').each(function () {
        chk_value.push($(this).val());
    });
    if (chk_value.length == 0) {
        alert(getLangString("jxcommon.chooseRecordToRoute"));
        return;
    }
    var fromJboname = $('#' + tbid).attr("jboname");
    var backflag = "1";// 是否可以回退
    if ($('#' + sendid).val() == $('#' + sendid).attr("firstActTaskId")) {
        backflag = "0";
    }

    var status = $("#status").val();
    var nextStatus = $("#nextStatus").val();
    dwr.engine.setAsync(false); // 设置成同步
    WebClientBean.getAppWorkflowParam(jx_appNameType, {
        // 返回一个json对象
        callback: function (data) {
            if (data) {
                var myurl = 'app.action?fromUid=' + chk_value + '&fromApp=' + jx_appName
                    + '&fromAppType=' + jx_appType + '&fromJboname=' + fromJboname
                    + '&backflag=' + backflag;
                appDialog('workflow', 'workflow', tbid, myurl, "auto", "300px");
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    dwr.engine.setAsync(true); // 重新设置成异步
}
// 点击流转前事件
function beforeRouteme(me, e) {
    return true;
}
/**
 * * 在主界面中发送工作流，弹出发送工作流按钮
 */
function routeme(me, e) {
    // 点击流转前事件
    if (!beforeRouteme(me, e)) {
        return;
    }
    // 流转之前，验证并保存数据
    var passValidation = checkPassValidation(me, e);
    if (passValidation) {
        passValidation = false;
        dwr.engine.setAsync(false); // 设置成同步
        submitData();// 提交所有数据
        // 执行保存
        WebClientBean.save(jx_appNameType, {
            callback: function (jbos) {
                // 检查是否定义了保存成功的回调函数
                var aSave = $(me).attr("afterSave");
                if (aSave != null && aSave != "") {
                    // 如果定义了，则先执行回调函数。
                    var afterSaveFun = eval(aSave);
                    new afterSaveFun(me, e);
                    // 检查执行回调函数之后是否继续。
                    if ($(me).attr("afterSaveContinue") != "Y") {
                        // 不用继续
                        return;
                    }
                }
                if (jbos != null) {
                    passValidation = true;
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
        dwr.engine.setAsync(true); // 设置成异步
    } else {
        return;
    }
    if (!passValidation) {
        return;
    }
    // 流程相关
    var uid = $('#uid').val();
    if (uid == null || uid == '' || uid == 'undefined') {
        alert(getLangString("jxcommon.route.error1"));
        return;
    }
    var jboname = $('#jboname').val();
    if (jboname == null || jboname == '' || jboname == 'undefined') {
        alert(getLangString("jxcommon.route.error2"));
    }

    var status = $("#status").val();
    var nextStatus = $("#nextStatus").val();

    var instanceid = $("#instanceid").val();
    var bpmInstanceId = "";
    var idx = instanceid.indexOf("#");
    if (idx > 0) {
        bpmInstanceId = instanceid.substring(0, idx);
    } else {
        bpmInstanceId = instanceid;
    }
    if (bpmInstanceId!='' && bpmInstanceId.length>2 && bpmInstanceId.indexOf("JXBPM.")>=0){
    	//健新科技自己的工作流
        var myurl = contextPath +'/jxbpm/route.action?fromUid=' + uid + '&fromApp=' + jx_appName
        	+ '&fromAppType=' + jx_appType + '&fromJboname=' + jboname
        	+ '&instanceid=' + bpmInstanceId;
	    var fromId = me.id;
	    if (fromId == undefined || fromId == "") {
	        fromId = "workflow";
	    }
	    appDialog(jx_appName, jx_appType, fromId, myurl, 650, 150, null, getLangString("jxcommon.routeme.title"));
    }else{
        dwr.engine.setAsync(false); // 设置成同步
        WebClientBean.getAppWorkflowParam(jx_appNameType, {
            // 返回一个json对象
            callback: function (data) {
                if (data != "") {
                    var myurl = contextPath +'/app.action?fromUid=' + uid + '&fromApp=' + jx_appName
                        + '&fromAppType=' + jx_appType + '&fromJboname=' + jboname
                        + '&instanceid=' + bpmInstanceId;
                    var fromId = me.id;
                    if (fromId == undefined || fromId == "") {
                        fromId = "workflow";
                    }
                    appDialog('workflow', 'workflow?engine=' + data, fromId, myurl, 650, 150, null, getLangString("jxcommon.routeme.title"));
                } else {
                    alert(getLangString("jxcommon.loadbpmconfigerror"));
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
        dwr.engine.setAsync(true); // 重新设置成异步    	
    }
}

/*
 * 点击流程对话框中的发送按钮
 */
function routeCommon(me, e) {
    if ($(me).attr("disabled") == "disabled") {
        return;
    }
    var checkAction = $('input[name="action"]:checked');
    var action = checkAction.val();
    var checkActionId = checkAction.attr("id");
    var actionLabel = $("label[for='" + checkActionId + "']").text();

    if (action == undefined) {
        alert(getLangString("jxcommon.bpm.needaction"));
        return;
    }

    var selUsers = $("select", $("div[useraction]:visible"));
    var actUsers = new Array();
    if (selUsers.length) {
        $('option:selected', selUsers).each(function () {
            actUsers.push($(this).val());
        });
        if (!actUsers.length) {
            alert(getLangString("jxcommon.bpm.needuser"));
            return;
        }
    }

    var required = $('#noteRequired').css('display');
    var note = $("#note").val();
    if (required!=undefined && required != 'none') {
        if (note == '') {
            alert(getLangString("jxcommon.bpm.neednote"));
            return;
        }
    }
    if (note==undefined){
    	note='';
    }
    $(me).attr("disabled", "disabled");
    note = actionLabel + ";" + note;
    dwr.engine.setAsync(false); // 设置成同步
    var appName = $('#fromApp').val();// 应用程序名
    var appType = $("#fromAppType").val();// 应用程序类型
    var jboname = $('#fromJboname').val();
    var uid = $('#fromUid').val();
    var tousers = "";
    var option = "";
    $("input[name='tousers']:checked").each(function () {
        tousers += $(this).val() + ",";
    });

    tousers += actUsers;

    WebClientBean.routeCommon(appName + "." + appType, jboname, uid, action, note, tousers, option, {
        callback: function (data) {
            // msgTip(data, getLangString("jxcommon.workflow.nexter.title"));
            alert(data);
            // 发送完工作流后，先在后台将当前的jbo数据刷新一下，然后在刷新当前页面
            WebClientBean.reloadCurrentJboData({
                callback: function () {
                    var href = parent.location.href;
                    if (href.indexOf("flag=add") > -1) {
                        href = href.replace(/flag=add/, "uid=" + uid);
                    }
                    window.parent.location.href = href;
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    var fid = $("#fromid").val();
    if (fid == null || fid == '') {
        appDialogClose(me, e);
    } else {
        getTableData("div_" + $("#fromid").val(), e, appDialogClose);
    }
    dwr.engine.setAsync(true); // 重新设置成异步

}

/**
 * * 发送工作流 * agree 参见：JxConstant.WORKFLOW_ROUTE_XXX * toActId 发送到指定的节点
 */
function route(me, e, agree, toActId) {
    if (agree == undefined && toActId == undefined) {
        routeme(me, e);
        return;
    }
    var required = $('#noteRequired').css('display');
    if (required != 'none') {
        if ($('#note').val() == '') {
            alert(getLangString("jxcommon.bpm.neednote"));
            return;
        }
    }
    dwr.engine.setAsync(false); // 设置成同步
    var appName = $('#fromApp').val();// 应用程序名
    var jboname = $('#fromJboname').val();
    var uids = $('#fromUid').val();
    var tousers = "";
    var option = "";
    $("input[name='tousers']:checked").each(function () {
        tousers += $(this).val() + ",";
    });

    // 解析wftParam
    // 查找form
    option += "[";
    var wftParamForm = $("form[id^='wftParamForm_']");
    $.each(wftParamForm, function (idx, dom) {
        var tempJson = "{";
        var key = $("#wftParamKey", dom).val();
        tempJson = tempJson + "'key':'" + key + "',";
        var jboname = $("#jboname", dom).val();
        tempJson = tempJson + "'jboname':'" + jboname + "'";

        // 解析attr和value
        tempJson = tempJson + ",'attr':[";
        var formEditTr = $(".table_edit tr", dom);

        $.each(formEditTr, function (tdidx, tddom) {
            tempJson += "{";
            var attribute = $(".form_td_content input", tddom).attr("dataattribute");
            var value = $(".form_td_content input", tddom).val();

            tempJson = tempJson + "'attribute':'" + attribute + "',";
            tempJson = tempJson + "'value':'" + value + "'";
            tempJson += "}";
            if (tdidx < formEditTr.length - 1) {
                tempJson = tempJson + ",";
            }
        });

        tempJson = tempJson + "]";
        // attr属性关闭了

        tempJson += "}";
        option = option + tempJson;
        if (idx < wftParamForm.length - 1) {
            option += ",";
        }
    });
    option += "]";

    WebClientBean.route(appName, jboname, uids, toActId, agree, $('#note')
        .val(), tousers, option == "[]" ? "" : option, {
        callback: function (data) {
            alert(data);
            // 发送完工作流后，先在后台将当前的jbo数据刷新一下，然后在刷新当前页面
            WebClientBean.reloadCurrentJboData({
                callback: function () {
                    window.parent.location.reload();
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    var fid = $("#fromid").val();
    if (fid == null || fid == '') {
        appDialogClose(me, e);
    } else {
        getTableData("div_" + $("#fromid").val(), e, appDialogClose);
    }
    dwr.engine.setAsync(true); // 重新设置成异步
}

// 发送工作流，同意
function routeOk(me, e) {
    route(me, e, 1, null);
}

// 发送工作流，不同意-退回到申请人
function routeBack(me, e) {
    route(me, e, 2, null);
}

// 发送工作流，不同意-退回到上一节点
function routeBackLast(me, e) {
    route(me, e, 4, null);
}

// 发送工作流，终止流程
function routeCancel(me, e) {
    route(me, e, 8, null);
}

/* 新增函数 2015-05-25 开始 */
/* 新增函数 2015-05-25 开始 */
function expexcel(me, e) {
    dwr.engine.setAsync(false);
    WebClientBean.preExpExcel(jx_appNameType, {
        callback: function (data) {
            if (data > 0) {
                var confirmsg = $('<div id="delConfirm"></div>');
                confirmsg.attr("title", getLangString("jxcommon.tip"));
                var html = getLangString("jxcommon.expexcelmsgstart") + $.trim(data) + getLangString("jxcommon.expexcelmsgend");
                confirmsg.html(html);
                $(confirmsg).dialog(
                    {
                        resizable: false,
                        width: 320,
                        height: 200,
                        modal: true,
                        buttons: [
                            {
                                text: getLangString("jxcommon.confirm"),
                                click: function () {
                                    $(this).dialog("close");
                                    expExcel();
                                }
                            },
                            {
                                text: getLangString("jxcommon.cancle"),
                                click: function () {
                                    $(this).dialog("close");
                                }
                            }
                        ]
                    });
            } else {
                expExcel();

            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });

    dwr.engine.setAsync(true); // 重新设置成异步
}

/**
 * 正式导出成为Excel
 */
function expExcel() {
    // $("body",
    // window.parent.document).mask(getLangString("jxcommon.expexcelexpExcel"),"");
    // $("body",
    // window.parent.document).mask(getLangString("jxcommon.expexcelexpExcel"),"",function(){
    // dwr.engine.setAsync(false);
    var title = $("#appName", window.parent.document).text();
    // 这是为了兼容ie
    var titlehtml = $("#appName", window.parent.document).html();
    if (!title) {
        if (title.length <= 0) {
            title = titlehtml;
        }
    }
    WebClientBean.expExcel(
        jx_appNameType,
        title, {
            callback: function (data) {
                // dwr.engine.setAsync(true); // 重新设置成异步
                $("body", window.parent.document).unmask();
                dwr.engine.openInDownload(data);
            },
            async: false,
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
}
/* 新增函数 2015-05-25 结束 */

// //////////////////////////////
// 导出Excel
function exportExcel(tbid) {
    if (bIsIE) {
        if ((sUserAgent.indexOf("msie 6.0") > 0)
            || (sUserAgent.indexOf("msie 7.0") > 0)) {
            exportByServer(tbid, 'excel');
        } else {
            exportExcelForIE(tbid);
        }
    } else {
        exportByServer(tbid, 'excel');
    }
}

// 导出Word
function exportWord(tbid) {
    if (bIsIE) {
        if ((sUserAgent.indexOf("msie 6.0") > 0)
            || (sUserAgent.indexOf("msie 7.0") > 0)) {
            exportByServer(tbid, 'word');
        } else {
            exportWordForIE(tbid);
        }
    } else {
        exportByServer(tbid, 'word');
    }
}

function exportByServer(tbid, filetype) {
    var context = $('#div_' + tbid).html();
    // if (filetype=='excel'){
    context = context.replace($('#foot_' + tbid).html(), "");
    // }
    var form = $("<form method='post' action='./export' style='display:none' enctype='application/x-www-form-urlencoded;charset=utf-8'><textarea name='context'>"
        + context
        + "<textarea/><input type='hidden' name='filetype' value='"
        + filetype + "'></form>");
    form.submit();
}

// 导出Excel,只为IE浏览器
function exportExcelForIE(tbid) {
    window.clipboardData.setData("Text", $('#div_' + tbid).html().replace(
        $('#foot_' + tbid).html(), ""));
    try {
        var ExApp = new ActiveXObject("Excel.Application");
        var ExWBk = ExApp.workbooks.add();
        var ExWSh = ExWBk.worksheets(1);
        ExApp.DisplayAlerts = false;
        ExApp.visible = true;
    } catch (e) {
        exportByServer(tbid);
        return false;
    }
    ExWBk.worksheets(1).Paste;
}

// 导出Word,只为IE浏览器
function exportWordForIE(tbid) {
    var divid = eval('div_' + tbid);
    var oWD = new ActiveXObject("Word.Application");
    var oDC = oWD.Documents.Add("", 0, 1);
    var oRange = oDC.Range(0, 1);
    var sel = document.body.createTextRange();
    sel.moveToElementText(divid);
    sel.select();
    sel.execCommand("Copy");
    oRange.Paste();
    oWD.Application.Visible = true;
    // window.close();
}

// 打开附件链接
function attachment(me, e) {
    var uid = $(me).attr('uid');
    var upath = $(me).attr("upath");
    var type = $(me).attr("type");
    var vfolder = $(me).attr("vfolder");
    var url = upath + "&cols=" + $(me).attr("cols") + "&iw=" + $(me).attr("imgwidth") + "&ih=" + $(me).attr("imgheight") + "&it=" + $(me).attr("imgtype");
    if (undefined != vfolder) {
        url = url + "&vfolder=" + vfolder;
    }

    if (type == 'IMGLIST') {
        // 显示图片列表
        appDialog('attachment', 'imglist', uid, url, 700, 150);
    } else {
        // 直接显示列表
        appDialog('attachment', 'list', uid, url, 700, 150);
    }
}

/**
 * *弹出表格自定义列对话框
 */
function custcolumn(tbid) {
    if (tbid == null || tbid == '') {
        return;
    }
    if ((sUserAgent.indexOf("msie 6.0") > 0)
        || (sUserAgent.indexOf("msie 7.0") > 0)) {
        alert("对不起，您的浏览器版本太低，不支持此功能。");
        $('#' + tbid).css('display', 'none');
        return;
    }
    var jboname = $('#' + tbid).attr("jboname");
    var relationship = $('#' + tbid).attr("relationship");
    appDialog('customcolumn', 'list', tbid, 'app.action?objectname=' + jboname
        + '&relationship=' + relationship, "auto", "400px");
}

/**
 * *将表格全屏
 */
function tableFullScreen(tbid) {
    $("#div_" + tbid).dialog({
        height: 'auto',
        width: '100%',
        resizable: true,
        modal: true
    });
}

/**
 * 删除并显示提示对话框，确定后直接删除
 *
 * @param me
 * @param e
 */
function delrowwithconfirm(me, e) {
    var delid = $(me).attr("id");
    var srcTr = $(me).closest("tr");
    var trid = srcTr.attr("id");
    var srcTable = $(me).closest("table");
    var tableid = srcTable.attr("id");


    var confirmsg = $('<div id="delConfirm"></div>');
    confirmsg.attr("title", getLangString("jxcommon.delConfirmTitle"));
    confirmsg.html(getLangString("jxcommon.delConfirmMsg"));
    $(confirmsg).dialog({
        resizable: false,
        height: 180,
        modal: true,
        buttons: [
            {
                text: getLangString("jxcommon.confirm"),
                click: function () {

                    WebClientBean.delRow(jx_appNameType, srcTable.attr('jboname'), srcTable.attr('relationship'),
                        srcTr.attr("uid"), true, {
                            callback: function (data) {
                                if (data) {
                                    WebClientBean.save(jx_appNameType, function () {
                                        getTableData("div_" + tableid);
                                    });
                                }
                            }
                        });

                    $(this).dialog("close");
                }
            },
            {
                text: getLangString("jxcommon.cancle"),
                click: function () {
                    $(this).dialog("close");
                }
            }
        ]
    });
}

/**
 * * 删除子记录，只是标记删除，不是真删除。
 */
function delrow(me, e) {
    var delid = $(me).attr("id");
    var srcTr = $(me).closest("tr");
    var trid = srcTr.attr("id");
    var srcTable = $(me).closest("table");
    var tableid = srcTable.attr("id");

    var flag = $(me).attr('flag'); // 1表示需要恢复，0表示需要删除

    // 如果有expandtype，现将expandtype收起来
    if (srcTable.attr("expandtype") != undefined) {
        if (srcTr.attr("expand") == "1") {
            $("td:first span", srcTr).click();
        }
    }
    dwr.engine.setAsync(false);
    if (flag == '0') {
        WebClientBean.delRow(jx_appNameType, srcTable.attr('jboname'), srcTable.attr('relationship'),
            srcTr.attr("uid"), true, {
                callback: function (data) {
                    if (data) {
                        if ($("#" + trid).attr("tobeadd") == "true") {
                            var tr = $("#" + trid).prev();
                            if (!tr.length) tr = $("#" + trid).next();
                            if (tr.length) {
                                $("#" + trid).remove();
                                tr.trigger("click");
                            } else {
                                getTableData("div_" + $("#" + trid).closest("table").attr("id"), null, null, null, 1);
                            }
                        } else {
                            // 克隆当前行
                            var delTr = $("#" + trid).clone();
                            srcTr.css("display", "none");
                            delTr.find("script").remove();
                            delTr.attr("id", srcTr.attr("id") + "_del");
                            srcTr.before(delTr);
                            // 将删除的行里面的所有控件修改为只读，去掉lookup图标
                            // TODO : 还有日期时间选择控件没有去掉，需要补充
                            $("input, select, checkbox, img", delTr).each(function (idx, dom) {
                                if ($(dom).hasClass("lookupicon")) {
                                    $(dom).remove();
                                }
                                $(dom).attr("disabled", "disabled").attr("readonly", "readonly").removeAttr("required");
                                var element = $(dom);

                                var elementClass = element.attr("class");
                                if (undefined != elementClass) {
                                    $.each(elementClass.split(" "), function (idx, value) {
                                        if (undefined != value && value.indexOf("validate") >= 0) {
                                            element.removeClass(value);
                                        }
                                    });
                                }
                            });

                            // 添加删除的横线条
                            var ot = $("td:first", delTr).html();
                            var i1 = ot.indexOf("<script");
                            var i2 = ot.indexOf("</script>");
                            if (i1 > 0 && i2 > 0) {
                                ot = ot.substring(0, i1) + ot.substring(i2 + 9);
                            }
                            var mywidth = delTr[0].clientWidth - $("td:last", delTr)[0].clientWidth;
                            $("td:first", delTr).html("<div style='position: relative;z-index:16;' class='rowDelLine'>"
                                + ot
                                + "<div style='width:"
                                + mywidth
                                + "px;position:absolute;top:50%;border-bottom:solid 1px #000'></div></div>");
                            // $("td:first", delTr).attr('oldtext', ot);

                            // 修改删除标识
                            $("#" + delid).attr('flag', '1');
                            // 修改删除图标
                            var unDelImg = $("#" + delid).attr("src").replace("delrow", "undelrow");
                            $("#" + delid).attr("src", unDelImg).removeAttr("disabled").find(".select2").css({"z-index": 15});
                        }
                    } else {
                        alert(getLangString("jxcommon.delFail"));
                    }

                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
    } else {
        WebClientBean.delRow(
            jx_appNameType,
            srcTable.attr('jboname'),
            srcTable.attr('relationship'),
            srcTr.attr("uid"),
            false,
            {
                callback: function (data) {
                    if (data) {
                        // 为了解决IE的兼容性BUG，在使用jquery.html(content)，content中不能有资源引用。
                        srcTr.next().show().trigger("click");
                        srcTr.remove();
                    } else {
                        alert(getLangString("jxcommon.undelFail"));
                    }
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
    }
    dwr.engine.setAsync(true);
}

/**
 * * 新增记录行
 */
function addrow(me, e) {
    addRowAndSetValue(me, e, null);
}

/**
 * 添加行，并设定默认值。
 *
 * @param me
 * @param e
 * @param data
 *            JSON格式的默认值
 */
function addRowAndSetValue(me, e, data) {
    var tableid = me.id.substring(0, me.id.length - $('#' + me.id).attr('mxevent').length - 1);
    var table = $("#" + tableid);
    if ($(me).attr('enabled') == '0') {
        alert(getLangString("jxcommon.addFail"));
        return;
    }
    $(me).attr('enabled', '0');
    // 处理expandtype的表格
    var hasExpand = table.attr("expandtype") == undefined ? false : true;
    if (hasExpand) {
        // 如果存在expandtype
        var exTr = $("tr[expand='1']", table);
        $("td:first span", exTr).click(); // 将expandtype收起来
    }
    WebClientBean.addRow(jx_appNameType, table.attr('jboname'), table.attr('relationship'), data,
        {
            callback: function (jbo) {
                if (jbo == null) {
                    alert(getLangString("jxcommon.addFail"));
                } else {
                    getTableData("div_" + tableid, null, null, null, 1);
                    // 可以新增下一条了。
                    $(me).attr('enabled', '1');
                }
            },
            errorHandler: errorHandler,
            exceptionHandler: exceptionHandler
        });
}

/**
 * * 新增主记录
 */
function add(me, e) {
    WebClientBean.canJboSetAdd(jx_appNameType, {
        callback: function (canAdd) {
            if (canAdd) {
                var addurl = $(me).attr("addurl");
                if (addurl == null) {
                    addurl = "./app.action";
                }
                if (addurl.indexOf("?") > 0) {
                    addurl += "&";
                } else {
                    addurl += "?";
                }
                addurl += "flag=add&app=" + jx_appName;
                window.location.href = addurl;
            } else {
                alert(getLangString("jxcommon.addFail"));
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
}

/**
 * * 渲染CKEditor * myid 需要渲染的控件ID号
 */
function renderCKEditor(myid) {
    if (myid == null) {
        return;
    }
    var me = $("#" + myid);
    if (me && me[0]) {
        CKEDITOR.config.language = userLangeCode;
        CKEDITOR.config.filebrowserImageUploadUrl = contextPath + "/ckedituploadimg.action";
        var tag = $('#' + myid)[0].tagName;
        if (tag == 'DIV' || $('#' + myid).attr('readonly') == 'readonly') {
            // 只读
            CKEDITOR.disableAutoInline = true;
            CKEDITOR.config.allowedContent = true;
            var ckedt = CKEDITOR.inline(myid, {
                readOnly: true,
                toolbar: []
            });
        } else {
            var renderExtends = $('#' + myid).attr('renderExtends');
            if ("" == renderExtends) {
                renderExtends = "BASE";// 当randerType没传值默认为BASE风格
            }
            // 可编辑
            var ckedt = CKEDITOR.replace(myid, {
                toolbar: renderExtends,
                on: {
                    'change': function (evt) {
                        $('#' + myid).attr('changed', '1');
                        ckedt.updateElement();
                        $('#' + myid).blur();
                    },
                    'blur': function (evt) {
                        ckedt.updateElement();
                        $('#' + myid).blur();
                    },
                    'key': function (evt) {
                        //
                    }
                }
            });
        }
    }

}

/**
 * * 在保存之前提交所有数据，虽然本应该已经全部提交，但仍然有一部份顽固份子，没有正确提交。
 */
function submitData() {
    // $("[render='CKEDITOR']").blur();
}

function href(me, e) {
    var ps = $(me).attr("params");
    if (ps != null && ps != '') {
        var target = $(me).attr("target");
        if (target != null && target != '') {
            windowopen(ps, target);
        } else {
            location.href = ps;
        }
    }
}

/**
 * 将jbo中的值赋给给parent内部的所有元素
 *
 * @param parent
 * @param jbo
 * @param type
 *            类型（扩展使用）
 */
function setData(parent, jbo, type) {
    /* 主表单条数据赋值 */
    setFormData(parent, jbo, type);
    /* 子表重新刷新，刷新赋值 */
    $("table[relationship!=''][jboname!='']", parent).each(function (idx, dom) {
        var relationship = $(this).attr("relationship");
        var pjboname = $("#jboname", parent.closest("form")).val();
        pjboname = pjboname.toUpperCase();
        var children = null;
        if (jbo.children) {
            children = jbo.children[pjboname + "." + relationship];
            if (typeof(children) == "undefined" || children == null) {
                children = jbo.children[pjboname + "." + relationship + "." + jbo.uidValue];
            }
        }
        if (null != children && null != children.jbo) {
            var ctable = pjboname + "." + relationship;
            // TODO : 这里获取table的id可能有问题
            if ($(this).attr("id") != undefined) {
                /* 可编辑的子表才会有id */
                getTableData("div_" + $(this).attr("id"), null, null, null, 1);
            } else {
                try {
                    var parentdiv = $(this).closest("td").children("div").eq(0);
                    getTableData($(parentdiv).attr("id"), null, null, null, 1);
                } catch (e) {
                    debug("子表" + ctable + "刷新失败");
                }
            }
        }
    });

}

/**
 * 将jbo中的值赋给parent内部的元素
 *
 * @param parent
 * @param jbo
 * @param type
 */
function setFormData(parent, jbo, type) {
    // 给input,textarea,textbox,label,checkbox,select赋值
    $("select[dataattribute!=''],input[type='text'][dataattribute!=''],textarea[dataattribute!=''],input[type='textbox'][dataattribute!=''],label[dataattribute!=''],input[type='checkbox'][dataattribute!='']", parent).each(function (idx, dom) {
        // 尚能输入字符提示不处理
        if ($(this).attr("class") && $(this).attr("class").indexOf("form_label_tip") >= 0) {
            return;
        }

        var dataattribute = $(this).attr("dataattribute");
        var tagName = this.tagName;

        var tempValue = null;
        // 如果是relationship的字段则需要详细分析
        if (undefined != dataattribute && dataattribute.indexOf(".") > 0) {
            var dataAttrs = dataattribute.split(".");
            // TODO : 这里获取jboname可以能有问题
            var jboname = $("#jboname", parent.closest("form")).val();
            if (type == "TABLE") {
                jboname = parent.closest("table[jboname!='']").attr("jboname");
            }
            jboname = jboname.toUpperCase();
            var children = null;
            if (jbo.children) {
                children = jbo.children[jboname + "." + dataAttrs[0]];
                if (typeof(children) == "undefined" || children == null) {
                    children = jbo.children[jboname + "." + dataAttrs[0] + "." + jbo.uidValue];
                }
            }
            if (null != children && null != children.jbo) {
                tempValue = children.jbo.datas[dataAttrs[1]];
            } else {
                // 关系刷新后可能是null，后面不会处理null值，导致第一次数据没有清空
                tempValue = "";
            }
        } else {
            // 不是relationship字段，直接设值
            tempValue = jbo.datas[dataattribute];
        }

        if (null != tempValue && undefined != tempValue) {
            if (tempValue && (tempValue.toString().indexOf("GMT") > 0 || tempValue.toString().indexOf("UTC") > 0)) {
                var typeClass = $(this).attr("class");
                // TODO : 日期控件的css如果是空的，则不处理
                if (typeClass && typeClass != undefined) {
                    var dateType = "";
                    if (typeClass.indexOf("custom['date']") >= 0) {
                        dateType = "date";
                    } else if (typeClass.indexOf("custom[dateTime]") >= 0) {
                        dateType = "dateTime";
                    }
                    tempValue = JxUtil.formatDate(tempValue.toString(), dateType);
                }
            }
        }

        if (undefined != tempValue && null != tempValue && "null" !== tempValue) {
            if ((typeof($(this).closest("table").attr("relationship")) == "undefined") || type == "TABLE") {
                var format = $(this).attr("format");
                // "" != tempValue判断
                if (format && format.length > 0 && "" != tempValue) {
                    tempValue = accounting.formatMoney(tempValue);
                }
                // 解析标签并且赋值
                if (tagName == "LABEL") {
                    $(this).text(tempValue);
                } else if ($(this).attr("type") == "checkbox") {
                    if ("1" == tempValue || 1 == tempValue) {
                        $(this).attr("checked", "checked");
                    } else {
                        $(this).removeAttr("checked");
                    }
                    $(this).val(tempValue);
                } else if (tagName == "SELECT") {
                    // var nulloption = $("option[value='']", $(this));
                    // if (!nulloption.length) {
                    // //如果没有请选择，加上请选择
                    // var op = getLangString("jxcommon.selectoption");
                    // $(this).append("<option selected value=''>" + op +
                    // "</option>");
                    // }
                    // tempValue = '';
                    // if($(this).val() != tempValue){
                    // $(this).val(tempValue);
                    // if ($(this).next(".select2").length) {
                    // //$(this).select2();
                    // checkFieldValid(this);
                    // }
                    // }
                } else {
                    $(this).val(tempValue);
                }
            } else {
                // 子表元素则不处理
            }
        }

    });
}

/**
 * * 处理dhtmlxGantt控件
 */
function dhtmlxGantt(me, e) {
    var columns = $(me).attr("columns");
    if ("TSEP" == columns) {
        gantt.config.columns = [
            {name: "text", label: "任务", tree: true, width: '*'},
            {name: "start_date", label: "开始时间", align: "right", width: "70"},
            {name: "end_date", label: "结束时间", align: "right", width: "70"},
            {name: "progress", label: "完成进度", align: "right", width: "40"}
        ];
        gantt.config.grid_width = 500;
    } else if ("TSE" == columns) {
        gantt.config.columns = [
            {name: "text", label: "任务", tree: true, width: '*'},
            {name: "start_date", label: "开始时间", align: "right", width: "70"},
            {name: "end_date", label: "结束时间", align: "right", width: "70"}
        ];
    } else if ("TE" == columns) {
        gantt.config.columns = [
            {name: "text", label: "任务", tree: true, width: '*'},
            {name: "end_date", label: "结束时间", align: "right", width: "70"}
        ];
    } else if ("NONE" == columns) {
        gantt.config.columns = [];
        gantt.config.grid_width = 2;
    } else {
        gantt.config.columns = [
            {name: "text", label: "任务", tree: true, width: '*'}
        ];
    }
    var ro = $(me).attr('dReadonly');
    if ('true' == ro) {
        gantt.config.readonly = true;
        gantt.config.drag_lightbox = false;
        gantt.attachEvent("onBeforeLightbox", function (id) {
            return false;
        });
    }
    var scale = $(me).attr("scale");
    if (scale == 'year') {
        gantt.config.scale_unit = "year";
        gantt.config.step = 1;
        gantt.config.scale_height = 50;
        gantt.config.subscales = [
            {unit: "year", step: 1, date: "%Y"}
        ];
    } else if (scale == 'month') {
        gantt.config.scale_unit = "month";
        gantt.config.step = 1;
        gantt.config.scale_height = 50;
        gantt.config.subscales = [
            {unit: "year", step: 1, date: "%Y"}
        ];
    } else if (scale == 'week') {
        gantt.config.scale_unit = "week";
        gantt.config.step = 1;
        gantt.config.scale_height = 80;
        gantt.config.subscales = [
            {unit: "month", step: 1, date: "%M"},
            {unit: "year", step: 1, date: "%Y"}
        ];
    } else if (scale == 'day') {
        gantt.config.scale_unit = "month";
        gantt.config.step = 1;
        gantt.config.scale_height = 80;
        gantt.config.date_scale = "%Y年%F";
        var weekScaleTemplate = function (date) {
            var dateToStr = gantt.date.date_to_str("%d %M");
            var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
            return dateToStr(date) + " - " + dateToStr(endDate);
        };
        gantt.config.subscales = [
            {unit: "week", step: 1, template: weekScaleTemplate},
            {unit: "day", step: 1, date: "%D"}
        ];
    }

    var myid = $(me).attr("id");
    gantt.config.fit_tasks = true;
    gantt.config.api_date = "%Y-%n-%d";
    gantt.config.date_grid = "%Y-%n-%d";
    gantt.config.sort = true;

    var myid = $(me).attr("id");
    gantt.init(myid);
}

/**
 * 展开treetable
 *
 * @param me
 * @param e
 */
function openTreeTable(me, e) {
    var tr = $(me).closest("tr");
    var table = $(me).closest("table[rootparent!='']");
    var tid = -1;
    if (tr) {
        // 获取当前行的记录主键id
        tid = tr.attr("uid");
        if (!table || tid < 0) {
            return;
        }

        // 如果是关闭状态，将其展开
        if ($("td:first span", tr).attr("treeStatus") == "close") {
            $("td:first span", tr).attr("treeStatus", "open");
            $("td:first span", tr).removeClass("treeclose").addClass("treeopen");
        } else {
            closeTreeTable(tr);
            return;
        }
    }

    var jboname = table.attr("jboname");
    var rootparent = table.attr("rootparent");
    var wherecause = rootparent.split(":");
    WebClientBean.getJboListByCause(jboname, wherecause[0] + "=?", tid, {
        callback: function (jbolist) {
            if (null != jbolist) {
                if (jbolist.length == 0) {
                    $(me).removeClass("treeopen").addClass("treechild");
                    $(me).removeAttr("onclick");
                    return;
                }

                for (var i = 0, l = jbolist.length; l > i; l--) {
                    var jbo = jbolist[l - 1];
                    var trClone = tr.clone();
                    // 添加uid，pid属性
                    trClone.attr("uid", jbo.uidValue).attr("pid", tid);

                    // 添加样式，添加左边宽度，显示层级
                    $("td:first span", trClone).removeClass("treeopen").addClass("treeclose").attr("treeStatus", "close");
                    var marginLeft = $("td:first span", tr).css("margin-left").replace("px", "");
                    $("td:first span", trClone).css("margin-left", parseInt(marginLeft) + 20);
                    // 给克隆的数据赋值
                    setData(trClone, jbo, "TABLE");
                    trClone.insertAfter(tr);
                }
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });

}

/**
 * 关闭treetable
 *
 * @param tr
 */
function closeTreeTable(tr) {
    var table = $(tr).closest("table");
    var uid = $(tr).attr("uid");

    var subtr = $("tr[pid='" + uid + "']", table);
    var subtrCount = subtr.length;

    if (subtrCount > 0) {
        $("td:first span", tr).attr("treeStatus", "close");
        $("td:first span", tr).removeClass("treeopen").addClass("treeclose");
        // 有子集
        for (var i = 0; i < subtrCount; i++) {
            closeTreeTable(subtr[i]);
            $(subtr[i]).remove();
        }
    } else {
        $(tr).remove();
    }
}

/**
 * 表格的onclick事件
 *
 * @param tr
 */
function selectTableTr(tr, e) {
    e = $.event.fix(e || window.event);
    if (!e.target.nodeName.toUpperCase().match(/^(a|input)$/i)) {
        // 判断是单选还是多选
        var $tr = $(tr);
        var fragment = $tr.closest(".fragment-mode");
        var $checkbox = $tr.find("input:checkbox");
        if ($checkbox.length) {
            var $allbox = $("input[type='checkbox'][name='allbox']", fragment);
            if ($allbox && $allbox.length > 0) {
                $("tr[class='trSelected']", table).removeClass("trSelected");
            }
            var checked = $checkbox[0].checked;
            $checkbox.prop("checked", !checked);
            ckOneSelectHandler($checkbox[0], $checkbox.attr("index"));
        }
        $tr.toggleClass("trSelected");
    }
}

/**
 * 执行excuteJboSetMethod,执行jboset中的方法 excujboname:对应的jbo那么
 * excumethod:对应的jbo中要执行的方法 excuparams:对应的传入参数，字符串类型 fmsg:失败后的消息提醒 smsg:成功后的消息提醒
 * callback:回调函数，执行成功后执行的函数
 */
function excuteJboSetMethod(excujboname, excumethod, excuparams, fmsg, smsg, callback) {
    if (excujboname == '' || excujboname == undefined || excujboname == null) {
        alert("请配置jboname");
        return false;
    }

    if (excumethod == '' || excumethod == undefined || excumethod == null) {
        alert("请配置jbo method");
    }
    /**
     * 当返回的消息为true,说明调用的消息没有消息返回， 当返回的消息为false,说明后台程序出错
     * 当返回的消息不为true,也不为false时，调用方法体返回的消息信息
     */
    WebClientBean.excuteJboSetMethod(excujboname, excumethod, excuparams, {
        callback: function (data) {
            if (data == 'true') {
                if (smsg != '') {
                    msgTip(smsg, '');
                }
            } else if (data == 'false') {
                if (fmsg != '') {
                    msgTip(fmsg, '');
                }
            } else {
                msgTip(data, '');
            }
            if (callback != null) {
                callback(data);
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
}

/**
 * 获取返回jboset中方法中返回的值,类似于ajax直接调用action方法，然后获取返回结果，返回结果为字符串类型，如果数据是数组，处理成json格式数据
 * 方便用户自定义操作处理
 *
 * @param excujboname
 * @param excumethod
 * @param excuparams
 */
function getJboSetMethodReturnValue(excujboname, excumethod, excuparams) {
    if (excujboname == '' || excujboname == undefined || excujboname == null) {
        alert("请配置jboname");
        return false;
    }

    if (excumethod == '' || excumethod == undefined || excumethod == null) {
        alert("请配置jbo method");
    }
    dwr.engine.setAsync(false); // 设置成同步
    var retValue = '';
    /**
     * 当返回的消息为true,说明调用的消息没有消息返回， 当返回的消息为false,说明后台程序出错
     * 当返回的消息不为true,也不为false时，调用方法体返回的消息信息
     */
    WebClientBean.excuteJboSetMethod(excujboname, excumethod, excuparams, {
        callback: function (data) {
            retValue = data;
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    dwr.engine.setAsync(true); // 重新设置成异步
    return retValue;

}

/**
 * dashboard页面的plus按钮
 *
 * @param me
 */
function showDashboardLet(me) {
    var href = $(me).attr("href");
    var target = $(me).attr("target");
    windowopen(href, target);
}

/**
 * 替换window.open()方法
 *
 * @param url
 * @param target
 */
function windowopen(url, target) {
    var newA = document.createElement("a");
    newA.id = 'gg' + parseInt(100 * Math.random());
    newA.target = target;
    newA.href = url;
    document.body.appendChild(newA);
    newA.click();
    document.body.removeChild(newA);
}

/*
 * 获取mainapp中元素属性
 */
function getMainAppAttributeValue(attributename) {
    dwr.engine.setAsync(false); // 设置成同步
    var attributevalue = '';
    WebClientBean.getData(function (map) {
        for (var prop in map) {
            if (map.hasOwnProperty(prop)) {
                if (prop == attributename) {
                    attributevalue = map[prop];
                }
            }
        }
    });
    dwr.engine.setAsync(true); // 重新设置成异步
    return attributevalue;
}

/**
 * 文件上传
 */
function importFile(me, e) {
    var relationship = $(me).attr("relationship");
    var fragmentid = $(me).attr("fragmentid");
    // 如果是从表
    if (null == relationship || undefined == relationship) {
        relationship = $("#" + fragmentid).attr("relationship");
    }
    var url = "app.action?relationship=" + (undefined == relationship ? "" : relationship) + "&appNameType=" + jx_appNameType;

    appDialog("impfile", "impfile", "impfile", url, "auto", 200, function (event, ui) {
        getTableData("div_" + fragmentid.toLowerCase(), e, null);
    });
}

/**
 * tab 完成创建
 *
 * @param event
 * @param ui
 */
function tabCreate(event, ui) {
}

/**
 * 激活tab之前触发的事件
 *
 * @param event
 * @param ui
 */
function beforeActivate(event, ui) {
}

function beforeLoad(event, ui) {

}

function tabActivate(event, ui) {

}

/**
 * tab页面切换前出发的事件
 */
function beforeTabLoad(event, ui) {
    ui.panel.html("load... ");

    ui.jqXHR.error(function () {
        ui.panel.html("Couldn't load this tab. ");
    });
}

function tabLoaded(event, ui) {
}

// tabgroup 和 tab的工具类
var tabUtil = {

    getTabGroup: function (id) {
        return $('#' + id).tabs();
    },
    getSelectedTabIndex: function (id) {
        return this.getTabGroup(id).tabs("option", "active");
    },
    getSelectedTabLink: function (id) {
        return $(".ui-tabs-active a", "#" + id);
    },
    loadTab: function (id, index, url) {
        var oldUrl = this.getSelectedTabLink(id).attr("href");
        this.getSelectedTabLink(id).attr("href", url);
        this.getTabGroup(id).data("loaded", false);
        this.getTabGroup(id).tabs('load', index);
        this.getSelectedTabLink(id).attr("href", oldUrl);
    },
    changeTab: function (id, index, newUrl) {
        var tab = $($("a[class='ui-tabs-anchor']", $("#" + id))[index]);
        var oldUrl = tab.attr("href");
        tab.attr("href", newUrl);
        this.getTabGroup(id).tabs("option", "active", index);
        // this.getTabGroup(id).tabs('load', index);
        this.getTabGroup(id).tabs("refresh");
        tab.attr("href", oldUrl);
    }
};

/**
 * 获取文件个数
 *
 * @param jboName
 * @param fromuid
 * @param code
 * @returns {String}
 */
function getAttachFileSize(jboName, fromuid, code) {
    dwr.engine.setAsync(false); // 设置成同步
    var retValue = '';
    WebClientBean.getAttachmentFileSize(jboName, fromuid, code, {
        callback: function (data) {
            retValue = data;
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
    dwr.engine.setAsync(true); // 重新设置成异步
    return retValue;
}

function sessionTimeout(loginPage) {
    alert(getLangString("jxcommon.sessionTimeout"));
    if (window) {
        window.top.location.href = loginPage + "/loginAction.do";
    }
}

/**
 * 将参数放到DataQuery中去,后台处理
 */
function inputQueryParams(params, value, callback) {
    WebClientBean.inputQueryOnBlur(jx_appNameType, '', params, value, {
        callback: function () {
            if (callback != null) {
                callback();
            }
        },
        errorHandler: errorHandler,
        exceptionHandler: exceptionHandler
    });
}

function initUILang(lang) {
}

function showToolbarMsg(msg) {
    if (msg.length > 0) {
        var toolbarMsg = $("#appMsg");
        if (toolbarMsg.length == 0) {
            toolbarMsg = $("#appMsg", parent.document);
        }

        if (toolbarMsg && toolbarMsg.length > 0) {
            toolbarMsg.text(msg).show();
            setTimeout(function () {
                toolbarMsg.hide().text("");
            }, 2000);
        } else {
            alert(msg);
        }
    }
}

function logout() {
    parent.window.location.href = "/jxweb/login/logout.jsp";
}

/**
 * tree标签中的树完成初始化后的操作
 *
 * @param zTree
 *            当前初始化的zTreeObj对象
 */
function afterTreeInit(zTree) {
}

function refresh(me, e) {
    location.reload();
}

/**
 * 刷新附件列表 attachmentid附件id
 *
 * @param attachmentid
 */
function refreshAttlist(attachmentid) {
    var src = document.getElementById("iframeAtt_" + attachmentid).src;
    // 刷新附件列表 使新生成或上传的附件显示出来
    $("iframeAtt_" + attachmentid).attr('src', $("iframeAtt_" + attachmentid).attr('src'));
    document.getElementById("iframeAtt_" + attachmentid).src = src;
}

/**
 * 重置iframe框架高度，iframe高度自动增长
 */
// FIXME : 当页面有多个附件table 的时候，会执行多次
function resetIframeHeight(divid) {
    var iframes = $("iframe[name^='iframeAtt_']", parent.document);
    $.each(iframes, function (idx, dom) {
        var div = $(dom).contents().find("div[id='" + divid + "']");
        if (div.length > 0) {
            var obj = dom;
            // obj.height = document.body.clientHeight;
            if (obj && !window.opera) {
                if (obj.contentDocument && obj.contentDocument.body.offsetHeight) { // 如果用户的浏览器是NetScape
                    obj.height = obj.contentDocument.body.offsetHeight;
                } else if (obj.Document && obj.document.body.scrollHeight) { // 如果用户的浏览器是IE
                    obj.height = obj.document.body.clientHeight;
                }
            }
        }
    });
}

/**
 * 判断字符串是否为空(null or '')
 *
 * @param {String}
 *            val
 * @return true if value is null or length=0
 * @type Boolean
 */
function isNull(val) {
    if (val == null) {
        return true;
    }
    if (val.length == 0)
        return true;
    return false;
}

/**
 * 判断字符串是否为空串
 *
 * @param {String}
 *            val
 * @return true if value only contains spaces or is null
 * @type Boolean
 */
function isBlank(val) {
    if (val == null) {
        return true;
    }
    for (var i = 0; i < val.length; i++) {
        if ((val.charAt(i) != ' ') && (val.charAt(i) != "\t")
            && (val.charAt(i) != "\n")) {
            return false;
        }
    }
    return true;
}

/**
 * 判断字符num是否为数字
 *
 * @param {String}
 *            num - digit '0'~'9'
 * @return true if value is a 1-character digit
 * @type Boolean
 */
function isDigit(num) {
    if (num != null || num.length == 1) {
        var string = "1234567890";
        if (string.indexOf(num) != -1) {
            return true;
        }
    }
    return false;
}

/**
 * isInteger(value)
 *
 * @return true if value contains all digits
 * @type Boolean
 */
function isInteger(val) {
    for (var i = 0; i < val.length; i++) {
        if (!isDigit(val.charAt(i))) {
            return false;
        }
    }
    return true;
}


/**
 * isNumeric(value)
 *
 * @return true if value contains a positive float value
 * @type Boolean
 */
function isNumeric(val) {
    var dp = false;
    for (var i = 0; i < val.length; i++) {
        if (!isDigit(val.charAt(i))) {
            if (val.charAt(i) == '.') {
                if (dp == true) {
                    return false;
                } // already saw a decimal point
                else {
                    dp = true;
                }
            } else {
                return false;
            }
            if (val.charAt(i) == '-') {
                return false;
            }
        }
    }
    return true;
}


function initSql(me, e) {
    $(me).attr('disabled', "true");
    $("#result").show();
    $("#result").html(getLangString("jxcommon.wait"));
    var curl = $(me).attr("url");
    if (curl == "" || curl == undefined) {
        curl = "config_sqlInstall.action?" + window.location.search;
    }
    var fn = $(me).attr("filename");
    if (fn == null || fn == undefined) {
        fn = $("#fileName").val();
    }
    var mid = $(me).attr("msgid");
    if (mid == "" || mid == undefined) {
        mid = "result";
    }
    $.ajax({
        type: "GET",
        url: curl,
        data: {
            fileName: fn
        },
        dataType: "html",
        cache: false,
        success: function (data, textStatus) {
            $("#" + mid).html(data);
            $("#" + mid).show();
            prettyPrint();
            $(me).removeAttr("disabled");
        },
        error: function (request, textStatus, e) {
            $("#" + mid).html(request + "," + textStatus + "," + e);
            $("#" + mid).show();
            prettyPrint();
            $(me).removeAttr("disabled");
        }
    });
}

/**
 * * 加载完毕之后，执行的操作，请将此函数放到最后。
 */
$(function () {
    // 隐藏搜索框
    var uid = getUrlParam("uid");
    var flag = getUrlParam("flag");

    if (uid && uid > 0 || uid != "") {
        $(".appbar-menu").html('');
    }
    if (flag && flag != "") {
        $(".appbar-menu").html('');
    }
    // toolbar按钮的图标变化
    $('.toolbar_btn').on("mouseenter", function () {
        var img = $(this).find('img')[0];
        if (img) {
            img.src = img.src.replace(".png", "_hover.png");
        }
    }).on("mouseleave", function () {
        var img = $(this).find('img')[0];
        if (img) {
            img.src = img.src.replace("_hover.png", ".png");
        }
    });
});
/**
 * 小写金额转大写金额
 *
 * @param dValue
 *            小写金额
 * @param maxDec
 *            小数位数（会四舍五入）
 * @returns {string}
 * @constructor
 */
function AmountInWords(dValue, maxDec) {
    // 验证输入金额数值或数值字符串：
    dValue = dValue.toString().replace(/,/g, "");
    dValue = dValue.replace(/^0+/, "");      // 金额数值转字符、移除逗号、移除前导零
    if (dValue == "") {
        return "零元整";
    }      // （错误：金额为空！）
    else if (isNaN(dValue)) {
        return "错误：金额不是合法的数值！";
    }

    var minus = "";                             // 负数的符号“-”的大写：“负”字。可自定义字符，如“（负）”。
    var CN_SYMBOL = "";                         // 币种名称（如“人民币”，默认空）
    if (dValue.length > 1) {
        if (dValue.indexOf('-') == 0) {
            dValue = dValue.replace("-", "");
            minus = "负";
        }   // 处理负数符号“-”
        if (dValue.indexOf('+') == 0) {
            dValue = dValue.replace("+", "");
        }                 // 处理前导正数符号“+”（无实际意义）
    }

    // 变量定义：
    var vInt = "";
    var vDec = "";               // 字符串：金额的整数部分、小数部分
    var resAIW;                                 // 字符串：要输出的结果
    var parts;                                  // 数组（整数部分.小数部分），length=1时则仅为整数。
    var digits, radices, bigRadices, decimals;  // 数组：数字（0~9——零~玖）；基（十进制记数系统中每个数字位的基是10——拾,佰,仟）；大基（万,亿,兆,京,垓,杼,穰,沟,涧,正）；辅币（元以下，角/分/厘/毫/丝）。
    var zeroCount;                              // 零计数
    var i, p, d;                                // 循环因子；前一位数字；当前位数字。
    var quotient, modulus;                      // 整数部分计算用：商数、模数。

    // 金额数值转换为字符，分割整数部分和小数部分：整数、小数分开来搞（小数部分有可能四舍五入后对整数部分有进位）。
    var NoneDecLen = (typeof(maxDec) == "undefined" || maxDec == null || Number(maxDec) < 0 || Number(maxDec) > 5);     // 是否未指定有效小数位（true/false）
    parts = dValue.split('.');                      // 数组赋值：（整数部分.小数部分），Array的length=1则仅为整数。
    if (parts.length > 1) {
        vInt = parts[0];
        vDec = parts[1];           // 变量赋值：金额的整数部分、小数部分

        if (NoneDecLen) {
            maxDec = vDec.length > 5 ? 5 : vDec.length;
        }                                  // 未指定有效小数位参数值时，自动取实际小数位长但不超5。
        var rDec = Number("0." + vDec);
        rDec *= Math.pow(10, maxDec);
        rDec = Math.round(Math.abs(rDec));
        rDec /= Math.pow(10, maxDec);  // 小数四舍五入
        var aIntDec = rDec.toString().split('.');
        if (Number(aIntDec[0]) == 1) {
            vInt = (Number(vInt) + 1).toString();
        }                           // 小数部分四舍五入后有可能向整数部分的个位进位（值1）
        if (aIntDec.length > 1) {
            vDec = aIntDec[1];
        } else {
            vDec = "";
        }
    }
    else {
        vInt = dValue;
        vDec = "";
        if (NoneDecLen) {
            maxDec = 0;
        }
    }
    if (vInt.length > 44) {
        return "错误：金额值太大了！整数位长【" + vInt.length.toString() + "】超过了上限——44位/千正/10^43（注：1正=1万涧=1亿亿亿亿亿，10^40）！";
    }

    // 准备各字符数组 Prepare the characters corresponding to the digits:
    digits = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");         // 零~玖
    radices = new Array("", "拾", "佰", "仟");                                              // 拾,佰,仟
    bigRadices = new Array("", "万", "亿", "兆", "京", "垓", "杼", "穰", "沟", "涧", "正"); // 万,亿,兆,京,垓,杼,穰,沟,涧,正
    decimals = new Array("角", "分", "厘", "毫", "丝");                                     // 角/分/厘/毫/丝

    resAIW = "";  // 开始处理

    // 处理整数部分（如果有）
    if (Number(vInt) > 0) {
        zeroCount = 0;
        for (i = 0; i < vInt.length; i++) {
            p = vInt.length - i - 1;
            d = vInt.substr(i, 1);
            quotient = p / 4;
            modulus = p % 4;
            if (d == "0") {
                zeroCount++;
            }
            else {
                if (zeroCount > 0) {
                    resAIW += digits[0];
                }
                zeroCount = 0;
                resAIW += digits[Number(d)] + radices[modulus];
            }
            if (modulus == 0 && zeroCount < 4) {
                resAIW += bigRadices[quotient];
            }
        }
        resAIW += "元";
    }

    // 处理小数部分（如果有）
    for (i = 0; i < vDec.length; i++) {
        d = vDec.substr(i, 1);
        if (d != "0") {
            resAIW += digits[Number(d)] + decimals[i];
        }
    }

    // 处理结果
    if (resAIW == "") {
        resAIW = "零" + "元";
    }     // 零元
    if (vDec == "") {
        resAIW += "整";
    }             // ...元整
    resAIW = CN_SYMBOL + minus + resAIW;            // 人民币/负......元角分/整
    return resAIW;
}

function DateAdd(interval, number, date) {
    /*
     * 功能:日期添加 参数:interval,字符串表达式，表示要添加的时间间隔. 参数:number,数值表达式，表示要添加的时间间隔的个数.
     * 参数:date,时间对象. 返回:新的时间对象. var now = new Date(); var newDate = DateAdd(
     * "d",5,now); --------------- DateAdd(interval,number,date)
     * -----------------
     */
    switch (interval) {
        case   "y"   :
        {
            date.setFullYear(date.getFullYear() + number);
            return date;
            break;
        }
        case   "q"   :
        {
            date.setMonth(date.getMonth() + number * 3);
            return date;
            break;
        }
        case   "m"   :
        {
            date.setMonth(date.getMonth() + number);
            return date;
            break;
        }
        case   "w"   :
        {
            date.setDate(date.getDate() + number * 7);
            return date;
            break;
        }
        case   "d"   :
        {
            date.setDate(date.getDate() + number);
            return date;
            break;
        }
        case   "h"   :
        {
            date.setHours(date.getHours() + number);
            return date;
            break;
        }
        case   "m"   :
        {
            date.setMinutes(date.getMinutes() + number);
            return date;
            break;
        }
        case   "s"   :
        {
            date.setSeconds(date.getSeconds() + number);
            return date;
            break;
        }
        default   :
        {
            date.setDate(d.getDate() + number);
            return date;
            break;
        }
    }
}

function select2AjaxSelectTag(id, displayvalue, dataattribute, displayname, ajaxurl, selectedDisplay, placeholder) {
    var $select = $("#" + id);
    var valueOrAttr = (displayvalue || dataattribute || "").toLowerCase();
    var place = {};
    displayname = (displayname || "description").toLowerCase();
    ajaxurl = ajaxurl.replace(/['"]/g, "");
    var formatRepo = function (repo) {
        if (repo.loading) return repo.text;
        var mark = repo[valueOrAttr] + '-' + repo[displayname];
        return mark;
    };
    var formatRepoSelection = function (repo) {
        return repo[displayname];
    };
    place[valueOrAttr] = -1;
    place.id = -1;
    place[displayname] = placeholder;
    $select.select2({
        placeholder: place,
        allowClear: true,
        ajax: {
            url: ajaxurl,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    quickSearchCause: params.term,
                    render: 'json',
                    jsonhead: '[{"total_count":"count"},{"fixText":"\\"items\\":"}]',
                    startStr: '{',
                    endStr: '}'
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                var da = data.items;
                var dalen = da.length;
                if (dalen > 0) {
                    if (da[0].id == undefined) {
                        for (i = 0; i < dalen; i++) {
                            da[i].id = da[i][valueOrAttr];
                        }
                    }
                }
                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * 20) < data.total_count
                    }
                };
            },
            cache: true
        },
        initSelection: function (element, callback) {
            var data = {};
            var $selection = $select.find("option:selected");
            data[valueOrAttr] = $selection.val();
            data.id = $selection.val();
            data[displayname] = $selection.text();
            callback(data);
        },
        escapeMarkup: function (markup) {
            return markup;
        }, // let our custom formatter work
        minimumInputLength: 1,
        templateResult: formatRepo, // omitted for brevity, see the source of
                                    // this page
        templateSelection: formatRepoSelection // omitted for brevity, see the
        // source of this page
    });
}

function select2CustomSelectTag(id, placeholder, allowclear) {
    $("#" + id).select2({
        placeholder: placeholder || "Select an option",
        allowClear: allowclear
    });
}

/**
 * 初始化js 金钱格式化工具
 */
function initAccountingPlugin() {
    window.accounting = window.accounting || {};
    accounting.settings = {
        currency: {
            symbol: "￥",   // 默认的货币符号 '$'
            format: "%s%v", // 输出控制: %s = 符号, %v = 值或数字 (can be object: see
                            // below)
            decimal: ".",  // 小数点分隔符
            thousand: ",",  // 千位分隔符
            precision: 2   // 小数位数
        },
        number: {
            precision: 0,  // 精度，默认的精度值为0
            thousand: ",",
            decimal: "."
        }
    };
}

/**
 * 执行Action中的方法
 *
 * @param me
 * @param e
 */
function executActionMethod(me, e) {
    var actionurl = $(me).attr('url');
    if (actionurl == undefined || actionurl == '') {
        return;
    }
    if ($(me).attr('disabled')=='true'){
    	return;
    }
    $(me).attr('disabled', "true");
    var msgid = $(me).attr("msgid");
    if (msgid != undefined && msgid != '') {
        $("#" + msgid).hide();
    }
    var callback = $(me).attr("callback");
    $.ajax({
        type: "POST",
        url: actionurl,
        dataType: "html",
        data: $('*').serialize(),
        cache: false,
        success: function (data, textStatus) {
            if (msgid != undefined && msgid != '' && data != '') {
                $("#" + msgid).show();
                $("#" + msgid).html(data);
            }
            if (callback != undefined && callback != '') {
                try {
                    eval(callback);
                } catch (exception) {
                }
            }
            $(me).removeAttr("disabled");
        },
        error: function (request, textStatus, e) {
            if (msgid != undefined && msgid != '') {
                $("#" + msgid).show();
                $("#" + msgid).html(request + "," + textStatus + "," + e);
            }
            $(me).removeAttr("disabled");
        }
    });

}

function executeaction(me,e){
	executActionMethod(me,e);
}
// 动态设置必填或只读
function setRequiredOrNot(me, isrequired, type) {
    var $me = $(me);
    var a = function (ee) {
        $me.parent().prev().attr("class", "form_td_label required");
        $me.parent().children().each(function (i, e) {
            $(e).show();
        });
    };
    var setRequied = function (ee) {
        $me.removeAttr("readonly");
        $me.attr("required", "required");
    };
    var setReadOnly = function (ee) {
        $me.attr("readonly", "readonly");
        $me.removeAttr("required");
    };
    if (isrequired) {
        if ('select' == type) {
            $me.parent().parent().prev().attr("class", "form_td_label required");
            $me.removeAttr("disabled");
            var selectclass = $me.attr("selectclass");
            if (typeof(selectclass) != "undefined") {
                $me.attr("class", selectclass);
            }
            setRequied(me);
        } else if ('date' == type) {
            a(me);
            var dateclass = $me.attr("dateclass");
            if (typeof(dateclass) != "undefined") {
                $me.attr("class", dateclass);
            }
            setRequied(me);
        } else if ('lookup' == type) {
            var $lookup = $("#" + ($me.attr("id") + "_DESC"));
            // $(me).parent().prev().attr("class","form_td_label required");
            $me.parent().children().each(function (i, e) {
                if ($(e).attr("id") != $lookup.attr("id")) {
                    $(e).show();
                }
            });
            var lookupclass = $lookup.attr("lookupclass");
            if (typeof(lookupclass) != "undefined") {
                $lookup.attr("lookupclass", lookupclass);
                $lookup.attr("class", lookupclass);
            }
            $me.removeAttr("readonly");
            $me.removeAttr("style");
            $me.parent().parent().find(".form_td_label").addClass("required");
        } else {
            a(me);
            var oldclass = $me.attr("oldclass");
            if (typeof(oldclass) != "undefined") {
                $me.attr("class", oldclass);
            }
            setRequied(me);
        }
    } else {
        if ('select' == type) {
            $me.parent().parent().prev().attr("class", "form_td_label");
            $me.attr("selectclass", $me.attr("class"));
            $me.attr("disabled", "disabled");
            setReadOnly(me);
            $me.removeAttr("class");
        } else if ('date' == type) {
            $me.parent().prev().attr("class", "form_td_label");
            $me.parent().children().each(function (i, e) {
                if ($(e).attr("id") != $me.attr("id")) {
                    $(e).hide();
                }
            });
            var dateclass = $me.attr("dateclass");
            if (typeof(dateclass) == "undefined") {
                $me.attr("dateclass", $me.attr("class"));
            }
            setReadOnly(me);
            $me.attr("class", "form_td_100");
        } else if ('lookup' == type) {
            var $lookup = $("#" + ($me.attr("id") + "_DESC"));
            $me.parent().prev().attr("class", "form_td_label");
            $me.parent().children().each(function (i, e) {
                if ($(e).attr("id") != $lookup.attr("id")) {
                    $(e).hide();
                }
            });
            var lookupclass = $lookup.attr("lookupclass");
            if (typeof(lookupclass) == "undefined") {
                $lookup.attr("lookupclass", $lookup.attr("class"));
            }
            setReadOnly($lookup);
            $lookup.attr("class", "form_td_multipart_readonly");
        } else {
            $me.parent().prev().attr("class", "form_td_label");
            $me.parent().children().each(function (i, e) {
                if ($(e).attr("id") != $me.attr("id")) {
                    $(e).hide();
                }
            });
            var oldclass = $me.attr("oldclass");
            if (typeof(oldclass) == "undefined") {
                $me.attr("oldclass", $me.attr("class"));
            }
            $me.attr("class", "form_td_100");
            setReadOnly(me);
        }
    }
}

/**
 * 手动处理table标签BUG问题， 当table标签是子表时，手动控制只读，并隐藏title按钮
 *
 * @param $table
 */
function setChildTableReadOnly($table) {
    var $dataTr = $("tbody tr", $table);
    var $thead = $("thead tr", $table);
    if ($thead.length > 0) {
        // 隐藏title按钮
        $("thead>tr:first", $table).hide();
        $("thead>tr:first", $table).next().hide();
    }
    if ($dataTr.length > 0) {
        // 隐藏输入框，加入span 文本过多时现实title显示
        // 有的input需要显示出来，请自己在界面中给该input的父td加上useful属性
        $("input", $table).each(function (idx, ele) {
            var useful = $(this).closest("td").attr("useful");
            var type = $(this).attr("type");
            if ('checkbox' == type) {
                $(this).attr("disabled", "disabled");
            } else {
                if (typeof(useful) == "undefined") {
                    $(this).closest("td").children().hide();
                    var val = $(this).val();
                    $(this).closest("td").append("<span title='" + val + "'>" + val + "</span>");
                }
            }
        });
        // 隐藏操作标题
        $("thead>tr>th", $table).each(function (idx, ele) {
            var dataattribute = $(this).attr("dataattribute");
            if (undefined == dataattribute || "" == dataattribute) {
                $(this).hide();
            }
        });
        // 隐藏操作按钮
        $("tbody>tr>td", $table).each(function (idx, ele) {
            var dataattribute = $(this).attr("dataattribute");
            var tdSelects = $("select", $(this));
            if (undefined == dataattribute || "" == dataattribute) {
                if (!(tdSelects && tdSelects.length > 0)) {
                    $(this).hide();
                }
            }
        });
    }
}

/**
 * jquery遮罩
 */
(function () {
    initAccountingPlugin();
    $.extend($.fn, {
        /**
         * fn为回调函数 可选
         */
        mask: function (msgobj, maskDivClass, fn) {
            this.unmask();
            // 参数
            var op = {
                opacity: 0.8,
                z: 10000,
                bgcolor: '#ccc'
            };
            var original = $(document.body);
            var position = {top: 0, left: 0};
            if (this[0] && this[0] !== window.document) {
                original = this;
                position = original.position();
            }
            // 创建一个 Mask 层，追加到对象中
            var maskDiv = $('<div id="maskdivgen" class="maskdivgen">&nbsp;</div>');

            var maskWidth = original.outerWidth();
            if (!maskWidth) {
                maskWidth = original.width();
            }
            var maskHeight = original.outerHeight();
            if (!maskHeight) {
                maskHeight = original.height();
            }
            maskDiv.css({
                position: 'absolute',
                top: position.top,
                left: position.left,
                'z-index': op.z,
                width: maskWidth,
                height: maskHeight,
                'background-color': op.bgcolor,
                opacity: 0
            });
            maskDiv.appendTo(original);
            // 重新设置大小
            $(window).resize(function () {
                var maskDiv = $("#maskdivgen");
                var maskWidth = original.outerWidth();
                if (!maskWidth) {
                    maskWidth = original.width();
                }
                var maskHeight = original.outerHeight();
                if (!maskHeight) {
                    maskHeight = original.height();
                }
                maskDiv.css({height: maskHeight + 'px', width: maskWidth + 'px'});
                var msgDiv = $("#msgDiv");
                var widthspace = (maskDiv.width() - msgDiv.width());
                var scrolltop = $(document).scrollTop();
                var usefulHeight = window.screen.availHeight;
                msgDiv.css({left: (widthspace / 2) + 'px', top: (usefulHeight / 2 + scrolltop - 150) + 'px'});
            });
            // 跟随滚动条
            $(window).scroll(function () {
                var maskDiv = $("#maskdivgen");
                var msgDiv = $("#msgDiv");
                var widthspace = (maskDiv.width() - msgDiv.width());
                var scrolltop = $(document).scrollTop();
                var usefulHeight = window.screen.availHeight;
                msgDiv.css({left: (widthspace / 2) + 'px', top: (usefulHeight / 2 + scrolltop - 150) + 'px'});
            });
            if (maskDivClass) {
                maskDiv.addClass(maskDivClass);
            }

            if (msgobj) {
                var isString = Object.prototype.toString.call(msgobj) === "[object String]";

                if (isString) {
                    var msgDiv = $('<div id="msgDiv"><div style="line-height:24px;border:#a3bad9 1px solid;background:white;padding:2px 10px 2px 10px">' + msgobj + '</div></div>');
                    msgDiv.css({
                        position: "absolute",
                        border: "#6593cf 1px solid",
                        padding: "2px;background:#ccca"
                    });
                    msgDiv.appendTo(maskDiv);
                    var widthspace = (maskDiv.width() - msgDiv.width());
                    var scrolltop = $(document).scrollTop();
                    var usefulHeight = window.screen.availHeight;
                    var heightspace = (maskDiv.height() - msgDiv.height());
                    msgDiv.css({
                        cursor: 'wait',
                        top: (usefulHeight / 2 + scrolltop - 150),
                        left: (widthspace / 2)
                    });
                } else if (msgobj.element) {
                    var msgDiv = $(msgobj.element);
                    maskDiv.html(msgDiv);
                } else {
                    var msgDiv = $('<div id="msgDiv"></div>');
                    msgDiv.css({
                        position: "absolute",
                        border: "#6593cf 1px solid",
                        padding: "2px;background:#ccca"
                    });
                    msgobj.appendTo(msgDiv);
                    msgDiv.appendTo(maskDiv);
                    var widthspace = (maskDiv.width() - msgDiv.width());
                    var scrolltop = $(document).scrollTop();
                    var usefulHeight = window.screen.availHeight;
                    var heightspace = (maskDiv.height() - msgDiv.height());
                    msgDiv.css({
                        cursor: 'wait',
                        top: (usefulHeight / 2 + scrolltop - 150),
                        left: (widthspace / 2)
                    });
                }
            }
            maskDiv.fadeIn('fast', function () {
                // 淡入淡出效果
                if (jQuery.isFunction(fn)) {
                    $(this).fadeTo('slow', op.opacity, fn);
                } else {
                    $(this).fadeTo('slow', op.opacity);
                }

            });
        },
        unmask: function () {
            var original = $(document.body);
            if (this[0] && this[0] !== window.document) {
                original = $(this[0]);
            }
            original.find("> div.maskdivgen").fadeOut('slow', 0, function () {
                $(this).remove();
            });
        },
        queueMask: function (strArr, delay) {
            $(this).mask(strArr[0]);
            var p = $("#msgDiv").find("div").width(140);
            var arr = [];
            for (var i = 0; i < strArr.length; i++) {
                (function (index) {
                    arr.push(function () {
                        p.html(strArr[index]);
                        setTimeout(function () {
                            if (index == strArr.length - 1) {
                                p.queue("mx", arr);
                            }
                            p.dequeue("mx");
                        }, delay);
                    });
                })(i);
            }
            p.queue("mx", arr);
            p.dequeue("mx");
        },
        dequeueMask: function () {
            $("#msgDiv").find("div").queue("mx", []);
            $(this).unmask();
        }
    });
})();

/**
 * 查询窗口中回车执行查询
 */
$(function () {
    $("body").keydown(function (event) {
        var searchBtn = $("span[mxevent='searchOk']");
        if (searchBtn.length == 0) return;
        if (!searchBtn.is(':visible')) return;
        if (event.keyCode == "13") {
            $(':focus').blur();
            searchBtn.click();
        }
    });
});

// layout tag
function createLayout(layoutId) {
    var $layoutContainer = $("#" + layoutId);
    var options = getLayoutOptions($layoutContainer);
    options.applyDemoStyles = false;
    $layoutContainer.layout(options);
}

function getLayoutOptions($container) {
    var options = {};
    $container.find(">.ui-layout-pane").each(function () {
        var $this = $(this);
        var region = $this.attr("region");
        options[region] = {};
        if (region != "center") {
            if ($this.attr("size")) {
                options[region].size = $this.attr("size");
            }
            if ($this.attr("minSize")) {
                options[region].minSize = $this.attr("minSize");
            }
            if ($this.attr("maxSize")) {
                options[region].maxSize = $this.attr("maxSize");
            }
            options[region].resizable = !($this.attr("resizable") == "false");
            options[region].closable = !($this.attr("closable") == "false");
            options[region].spacing_open = parseInt($this.attr("space"));
            if ($this.attr("status") == "close") {
                options[region].initClosed = true;
            } else if ($this.attr("status") == "hidden") {
                options[region].initHidden = true;
            }
        }
    });
    return options;
}
// 当ui.layout一开始是隐藏的时候，在显示的时候需要进行resizeAll
function resizeLayout($parent, beforeResize) {
    var layout = $parent.find(".ui-layout-container.panel").data("layout");
    if (layout) {
        if ($.isFunction(beforeResize)) {
            beforeResize();
        }
        layout.resizeAll();
    }
}

function loadingMask(element, isFloat) {
    if (isFloat) {
        // 适合有一定高度的
        element.mask({
            element: '<div class="loading-mask float-loading-mask"><span class="loading"></span></div>'
        });
    } else {
        // 适合高度为0的
        element.html('<div class="loading-mask empty-loading-mask"><span class="loading"></span></div>');
    }
}

function loadingUnmask(element) {
    var loading = element.find(".loading-mask");
    if (loading.is(".empty-loading-mask")) {
        loading.remove();
    } else {
        element.unmask();
    }
}

// tree标签加载数据前进行预处理
function processTreeNode(treeNode) {
    return treeNode;
}