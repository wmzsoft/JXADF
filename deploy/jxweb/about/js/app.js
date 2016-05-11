(function () {
    $.extend($.fn, {
        /**
         * fn为回调函数  可选
         */
        mask: function (msgobj, maskDivClass, fn) {
            this.unmask();
            // 参数
            var op = {
                opacity: 0.8,
                z: 10000,
                bgcolor: '#333'
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
            //重新设置大小
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
            //跟随滚动条
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
        }
    });
})();
$(function () {
    var _pageUrl = encodeURIComponent(location.href);
    $("#myScrollspy").find(".share-icon").each(function () {
        var $this = $(this);
        var url = "";
        if ($this.is(".share_qq")) {
            url = "http://connect.qq.com/widget/shareqq/index.html?"
                + "title=" + _pageTitle
                + "&url=" + _pageUrl
                + "&desc=" + _pageUrl
                + "&pics="
                + "&site=";
        } else if ($this.is(".share_qzone")) {
            url = "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"
                + "title=" + _pageTitle
                + "&url=" + _pageUrl
                + "&desc=" + _pageTitle
                + "&summary=" + _pageTitle
                + "&pics="
                + "&site=";
        } else if ($this.is(".share_txwb")) {
            url = "http://v.t.qq.com/share/share.php?"
                + "url=" + _pageUrl
                + "&title=" + _pageTitle;
        } else if ($this.is(".share_xlwb")) {
            url = "http://service.weibo.com/share/share.php?"
                + "url=" + _pageUrl
                + "&title=" + _pageTitle;
        } else if ($this.is(".share_wx")) {
            $this.on("click", function (e) {
                $(document).mask({
                    element:"<div id='qrcode'></div>"
                });
                $("#qrcode").qrcode({width:128,height:128,text:_pageUrl}).css({
                    position:"fixed",
                    left:"45%",
                    top:"30%"
                });
                $("#maskdivgen").one("click",function(){
                    $(document).unmask();
                });
                e.preventDefault();
            });
            return true;
        }
        $this.attr("href", url);
    });
});

function osgia(){
    window.location.href="http://www.osgia.com";
}