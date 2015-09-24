var JxMenu = (function(){
    var JxMenu = function (setting, menuDiv) {
        this.menuDiv = typeof menuDiv == "object" ? menuDiv : $("#" + menuDiv);
        var defaultSetting = {idKey: "app", pIdKey: "parent", menuData: null, iconBase: ""};
        this.setting = $.extend(defaultSetting, setting);
        this.initData();
        menuDiv.addClass("jxMenuDiv");

        if (langCode == "en")
            $(".popupWin").addClass("en");
    };

    var hideTimer = null;

    /**
     * json数组转换为树形数据结构
     * @param setting
     * @param sNodes  JSON菜单数组
     * @returns {Array}
     */
    JxMenu.prototype.transformToTreeData = function (setting) {
        var i, l,
            key = setting.idKey,
            parentKey = setting.pIdKey,
            sNodes = setting.menuData;
        childKey = "children";
        if (!key || key == "" || !sNodes) return [];

        var r = [];
        var tmpMap = [];
        for (i = 0, l = sNodes.length; i < l; i++) {
            tmpMap[sNodes[i][key]] = sNodes[i];
        }
        for (i = 0, l = sNodes.length; i < l; i++) {
            if (tmpMap[sNodes[i][parentKey]] && sNodes[i][key] != sNodes[i][parentKey]) {
                if (!tmpMap[sNodes[i][parentKey]][childKey])
                    tmpMap[sNodes[i][parentKey]][childKey] = [];
                tmpMap[sNodes[i][parentKey]][childKey].push(sNodes[i]);
            } else {
                r.push(sNodes[i]);
            }
        }
        return r;
    };
    /**
     * 一级菜单mouseover事件，二级菜单位置、高度处理
     * @param e
     */
    JxMenu.prototype.mouseOverEventHandler = function (e) {
        hideTimer && clearTimeout(hideTimer);
        var subMenuTop,
            borderTop = $(this).offset().top - e.data.menuDiv.position().top,
            viewHeight = $(window).height(),
            menuDivHeight = e.data.menuDiv.outerHeight(),
            scrollTop = $(document).scrollTop(),
            relaxHeight = viewHeight - ( borderTop - scrollTop ),
            subMenu = $(".subMenu", $(this)[0].parentNode),
            popupWin = $(".popupWin"),
            padding = 20,
            $children = subMenu.children();

        if ($children.length <= 0) {
            popupWin.hide();
            return false;
        }

        var popupSubMenu = $children.clone(true);
        popupSubMenu.last().css({
            "border-bottom":"none",
            "margin-bottom":"0"
        });
        subMenu.hide();
        e.data.curSubMenu = popupSubMenu;

        popupWin.removeAttr("style").html("<div class='popupWin_bg'></div><div class='popupWin_content'></div>").find(".popupWin_content").append(popupSubMenu).show();

        subMenuHeight = popupWin.height();

        if (subMenuHeight <= relaxHeight) {
            subMenuTop = borderTop;
        } else if (subMenuHeight > relaxHeight && subMenuHeight < menuDivHeight) {
            subMenuTop = scrollTop + viewHeight - subMenuHeight-5;
        } else {
            subMenuHeight = menuDivHeight - padding;
            subMenuTop = scrollTop - e.data.menuDiv.position().top;
        }
        var menuTop = subMenuTop - e.data.menuDiv.parent().position().top;
        if (menuTop < 0)
            menuTop = 0;

        popupWin.css({
            "top": menuTop,
            "left": "246px",
            "height": subMenuHeight,
            "display": "block"
        });
    };

    JxMenu.prototype.mouseOutEventHandler = function(){
        hideTimer = setTimeout(function(){
            $(".popupWin").hide();
        },20);
    };


    /**
     * 菜单dom生成
     * @param node    树形对象
     * @param parent  jquery父节点对象
     */
    JxMenu.prototype.createMenu = function (node, parent) {
        if (node != null) {
            var menuDiv = this.menuDiv;
            var isLastItemApp = false;
            for (var i = 0; i < node.length; i++) {
                if (!node[i].parent) {
                    var imgItem = $('<span class="icon-module icon-module-'+node[i].app.toLowerCase()+'"></span>');
                    var menuContainer = $('<div class="menuContainer"></div>').append(imgItem)
                        .append($('<div id="'+node[i].app+'" class="menuItem" '+(node[i].appUrl?('appUrl="'+node[i].appUrl):'')+'" app="'+node[i].app+'" appId="'+node[i].maxAppsId+'">'+node[i].description+'</div> '));
                    var topMenu = $("<div class='topMenu'></div>")
                    topMenu.append(menuContainer).appendTo(menuDiv);
                    if (node[i].children != null) {
                        this.createMenu(node[i].children, $('<div class="subMenu"></div>').appendTo(topMenu));
                    }
                }

                if (node[i].appType == "MODULE" && node[i].parent) {
                    isLastItemApp = false;
                    var menuItem = $('<dt appUrl="'+node[i].appUrl+'" app="'+node[i].app+'" appId="'+node[i].maxAppsId+'">'+node[i].description+'</dt>');
                    if(parent[0].tagName == "DL" || parent[0].tagName == "DD"){
                        parent = parent.closest(".subMenu");
                    }
                    var subMenu = $("<dl/>").append(menuItem).appendTo(parent);
                    if (node[i].children != null)
                        this.createMenu(node[i].children, subMenu);
                }

                if (node[i].appType == "APP" && parent) {
                    if (parent[0].tagName != "DL" && parent[0].tagName != "DD") {
                        parent = $("<dl/>").appendTo(parent);
                    }
                    if (!isLastItemApp) {
                        parent = $('<dd><em appUrl="'+node[i].appUrl+'" app="'+node[i].app+'" appId="'+node[i].maxAppsId+'">'+node[i].description+'</em></dd>').appendTo(parent);
                        isLastItemApp = true;
                    } else
                        $('<em appUrl="'+node[i].appUrl+'" app="'+node[i].app+'" appId="'+node[i].maxAppsId+'">'+node[i].description+'</em>').appendTo(parent);
                }
            }
        }
    };

    /**
     * 菜单点击事件处理函数
     * @param e
     */
    JxMenu.prototype.menuClickEventHandler = function (e) {
        if ($(this).attr("appUrl") != undefined && $(this).attr("appUrl") != "") {
            if (typeof e.data.onMenuClick == "function") {
                if(e.data.curSubMenu){
                    e.data.curSubMenu.toggle();
                }
                e.data.onMenuClick(e);

                $(".popupWin").hide();
            }
        }
    };


    /**
     * 菜单点击事件处理函数
     * @param e
     */
    JxMenu.prototype.menuHisBtnClickEventHandler = function (e) {
        var popWin = $(".popupWin");
        var $appMenu = $("#menuAppBar");
        popWin.css({
            top: $appMenu.position().top + $appMenu.outerHeight(),
            left: "22px",
            height: "250px"
        });

        if (!popWin.is(":visible")) {
            WebClientBean.getMenuHisData(function (data) {
                function addList(lst, title) {
                    var menuItem = $("<dt/>").text(title);
                    var dd = $("<dd/>");

                    for (var i = 0; i < lst.length; i++) {
                        $("<em/>").text(lst[i].DESCRIPTION).attr("appUrl", lst[i].APPURL).attr("app", lst[i].APP).appendTo(dd);
                    }
                    $("<dl/>").addClass('subMemuFont').append(menuItem).append(dd).appendTo(popWin);
                }

                popWin.empty();
                if (data == null) return;
                addList(data.favoriteApp, getLangString("home.myFavorite"));
                addList(data.recentApp, getLangString("home.recentVisit"));
            });
            e.data.curSubMenu = popWin;
        }

        popWin.toggle();
    };

    JxMenu.prototype.loadModuleImg = function (obj, hover) {
        var imgItem = $(obj).find(".moduleIcon");
        var menuItem = $(obj).find(".menuItem");
        var url = this.setting.iconBase + "menu/" + menuItem.attr("ID").toLowerCase() + hover + ".png";
        this.imgLoad(url, imgItem);
    };

    /**
     * 初始化菜单
     */
    JxMenu.prototype.initData = function () {
        if (this.setting.menuData == null)
            return;
        var popupWin = $(".popupWin");
        var treeData = this.transformToTreeData(this.setting);
        this.menuDiv.on("mouseenter", ".menuContainer", this, this.mouseOverEventHandler);
        this.menuDiv.on("mouseleave",".menuContainer", $.proxy(this.mouseOutEventHandler,this));
        this.menuDiv.on("click", ".menuItem, .subMenu dt, .subMenu  em", this, this.menuClickEventHandler);

        $(".visitHisBtn").on("click", null, this, this.menuHisBtnClickEventHandler);
        this.autoComplete();
        popupWin.on("click", "em", this, this.menuClickEventHandler);
        popupWin.on("mouseleave", function () {
            $(this).hide();
        }).on("mouseenter", $.proxy(function(){
            hideTimer && clearTimeout(hideTimer);
        },this));

        this.createMenu(treeData, null);
        var self = this;
    };

    JxMenu.prototype.autoComplete = function () {
        var tags = [];
        $.each(this.setting.menuData, function (key, item) {
            tags.push({
                label: item.description,
                value: item.description,
                target: "#mainMenu [appid="+item.maxAppsId+"]",
                appUrl: item.appUrl
            });
        });
        //$.fn.autocomplete依赖的$.fn.menu可能被easyui等其他库污染，所以需要重新定义
        var oldMenuFn = $.fn.menu;
        $.widget.bridge("menu", $.ui.menu);
        $(".input-txt","#menuAppBar").autocomplete({
            source:tags,
            select:function(e,ui){
                if(ui.item.appUrl){
                    $(ui.item.target).trigger("click");
                }else{
                    $(ui.item.target).trigger("mouseover");
                }
            }
        }).on("keypress",function(e){
            var $menu = $(".ui-autocomplete");
            if($menu.is(":visible") && e.keyCode == 13){
                $menu.find("a").eq(0).trigger("click");
                $(this).blur();
            }
        }).on("focusin",function(){
            $(".popupWin").hide();
            $(this).val("");
        });
        $.fn.menu = oldMenuFn;
    };

    JxMenu.prototype.imgLoad = function (url, imgItem) {
        var img = new Image();
        img.src = url;

        if (img.complete) {
            imgItem.attr("src", url);
        } else {
            img.onload = function () {
                imgItem.attr("src", url);
                img.onload = null;
            };
        }
    };

    return JxMenu;
})();

