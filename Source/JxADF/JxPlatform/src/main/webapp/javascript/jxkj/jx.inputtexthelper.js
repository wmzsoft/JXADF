(function ($) {
    function TextHelper(target) {
        this.$target = $(target);
        this.selector = "input[type=text]";
        this.init();
    }

    TextHelper.prototype.init = function () {
        this.initElement();
        this.initEvents();
    };
    TextHelper.prototype.initElement = function () {
        this.$view = $(".plugin-input-text-helper-view");
        if (!this.$view.length) {
            this.$view = $('<div class="plugin-input-text-helper-view"></div>').appendTo("body");
        }
    };
    TextHelper.prototype.initEvents = function () {
        $(document).on("focusin mouseenter", this.selector, $.proxy(function (e) {
            this.$target = $(e.target);
            this.$view.css({
                "font-size": this.$target.css("font-size"),
                "font-family": this.$target.css("font-family"),
                "word-spacing": this.$target.css("word-spacing"),
                "height": this.$target.height(),
                "line-height": this.$target.css("line-height")
            }).html(this.$target.val());
            this.position();
        }, this)).on("keyup", this.selector, $.proxy(function (e) {
            this.$target = $(e.target);
            this.$view.html(this.$target.val());
            this.position();
        }, this)).on("focusout mouseleave", this.selector, $.proxy(function () {
            if($(this.selector).filter("focus"))
            this.$view.css({
                left: -9999,
                top: -9999,
                right:"auto"
            });
        }, this));
    };
    TextHelper.prototype.position = function () {
        var options = {};
        var inputWidth = this.$target.outerWidth();
        var winWidth = $(window).width();
        var viewWidth = this.$view.width();
        if (inputWidth < viewWidth) {
            var offset = this.$target.offset();
            if (offset.left + viewWidth > winWidth) {
                options.right = winWidth - offset.left - inputWidth;
                options.left = "auto";
            } else {
                options.left = offset.left;
                options.right = "auto";
            }
            options.top = offset.top - this.$view.outerHeight() - 2;
        } else {
            options.left = -9999;
            options.top = -9999;
            options.right = "auto";
        }
        this.$view.css(options);
    };

    $.inputTextHelper = function () {
        new TextHelper();
    };

    $(function () {
        $.inputTextHelper();
    });
})(jQuery);