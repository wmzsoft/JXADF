/**
 * @desc : jx 国际化处理 js处理
 * @author : cxm
 * @date : 2014/8/19
 */

function loadLanguage(lang) {
    var browserLang;
    if (lang && lang.length > 0) {
        browserLang = lang;
    } else {
        browserLang = $.i18n.browserLang();
    }

    $.i18n.properties({
        name: 'lang',// 资源文件名称
        path: '/jxweb/i18n/',// 资源文件所在目录路径
        mode: 'both',// 模式：变量或 Map
        language: browserLang,// 对应的语言
        cache: false,
        encoding: 'UTF-8',
        callback: function () {// 回调方法
            initUILang(browserLang);
        }
    });
}

/**
 * 获取值
 */
function getLangString(key) {
    var result = $.i18n.prop(key);
    if (result == "["+key+"]" && parent != window) {
        result = parent.$.i18n.prop(key);
    }
    return result;
}

/**
 ** OSGi 的JS语言包
 ** 详细使用方式参见：http://wiki.jxtech.net/pages/viewpage.action?pageId=21266779
 */
function getI18n(key) {
    return osgiI18n[key];
}