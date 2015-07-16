
/**
 * player jQuery plugin
 *
 * Copyright (c) 2009 Xing,Xiudong
 * 
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * Released under the MIT, BSD, and GPL Licenses.
 *
 * @author Xing,Xiudong  xingxiudong@gmail.com | http://blog.csdn.net/xxd851116
 * @since: 2009-08-06
 * @version 1.0 2009-08-06
 * @deprecated 1.1 Update a problem: the same index li element's show or hide is repeated when mouse over it @2009-09-23
 * @deprecated 1.2 Add row($title_bar.width($container.width() - 5 * 2);) to remove '$title_bar' left-padding and right-padding @2010-02-24
 * @version 1.3 Add showTitle attributte and rebuild parameters initialization @2012-06-19.
 */
(function($){
	$.fn.player = function(settings){
		settings = settings || {};
		// Some goloab private constant parameters.
		var defaultCSS = {
			size : {'width':'800px','height':'600px'},
			div : {'position':'relative','border':'1px solid #CCC','padding':'0px','fontSize':'12px','overflow':'hidden'},
			img	: {'position':'absolute','border':'none','z-index':'0','margin':'0px'},
			ul	: {'margin':'0px','padding':'0px','z-index':'10','position':'absolute','bottom':'2px','right':'2px','list-style-type':'none'},
			li	: {'font-family':'Arial','color':'#FFF','float':'left','border':'1px solid #eee','padding':'2px','width':'15px','height':'15px','text-align':'center','margin-left':'1px','cursor':'pointer','background-color':'#999','font-size':'100%'},
			title: {'background-color':'#eee','position':'absolute','z-index':'9','line-height':'25px','font-weight':'700','padding':'0 5px','bottom':'0'},
			liFoucs : {'background-color':'#F00','font-weight':'700'},
			liBlur  : {'background-color':'#999','font-weight':'normal'}
		}
		defaultCSS.liFoucs = $.extend({}, defaultCSS.li, defaultCSS.liFoucs);
		defaultCSS.liBlur = $.extend({}, defaultCSS.li, defaultCSS.liBlur);
		
		var options = $.extend({
			showTitle	: true, // 标题显示开关，true表示显示标题，默认为true
			width		: '', // 播放器div容器的宽度
			height		: '', // 播放器div容器的高度
			time		: 2000,  // 图片播放时间（单张图片播放至切换到下一张图片所要的时间）,单位：毫秒
			onStart		: function(){}, // 开始播放时执行的函数
			onStop		: function(){}, // 停止播放时执行的函数
			onShow		: function($) {$.show();}, // 每页图片显示时执行的函数
			onHide		: function($) {$.hide();}  // 每页图片隐藏时执行的函数
		}, defaultCSS.size, settings, {
			divCSS		: $.extend(defaultCSS.div, settings.divCSS || {}, defaultCSS.size, {width:settings.width,height:settings.height}), // 用户自定义容器样式
			imgCSS		: $.extend(defaultCSS.img, settings.imgCSS || {}, defaultCSS.size, {width:settings.width,height:settings.height}), // 用户自定义图片样式
			ulCSS		: $.extend(defaultCSS.ul, settings.ulCSS || {}), // 用户自定义图片序号容器UL样式
			liCSS		: $.extend(defaultCSS.li, settings.liCSS || {}), // 用户自定义图片序号容器LI样式
			titleCSS	: $.extend(defaultCSS.title, settings.titleCSS || {}), // 用户自定义标题样式
			focusIdxCSS	: $.extend(defaultCSS.liFoucs, settings.focusIdxCSS || {}), // 图片序号focus样式
			blurIdxCSS	: $.extend(defaultCSS.liBlur, settings.blurIdxCss || {}) // 图片序号blur样式
		}, {
			animate : $.extend({
				duration : 500,// 图片播放动画执行时间,建议此项小于time值的1/3,单位：毫秒
				method : '', // 动画效果，总共32种，默认随机
				direction : '', // 动画方向，总共8种，默认随机，zoomshow和zoomhide不支持随机。
				zoomRatio : 5 // 采用zoomin或zoomout时的最大放大级别，默认5倍，大于等于零的整数
			}, settings.animate || {})
		});

		var $container 	= this.hide().css(options.divCSS); // 获得图片播放器容器
		var $images 	= $container.find("img").hide().css(options.imgCSS); // 获得图片数组
		var lastIndex 	= $images.length - 1; // 最后一张（从左到右）图片的索引
		var prevIndex	= lastIndex; // 最后一次访问图片的索引（从0开始）
		var index = 0; // 记录当前索引值, 默认为第一张图片的索引：0
		var timer; // 定时器

		if (options.showTitle) {
			// 生成标题栏
			var $title_bar = $("<div>&nbsp;</div>").appendTo($container).css(options.titleCSS).fadeTo('fast', "0.75");
			//除去 $title_bar自身左右padding 5px
			var title_w = options.titleCSS.width ? (parseInt(options.titleCSS.width)  - 5 * 2) : ($container.width() - 5 * 2);
			$title_bar.width(title_w);
		}

		// 生成索引序号
		var $indexGroups = $("<ul></ul>").css(options.ulCSS);
		for (var i = 0; i < $images.length; i++) {
			$indexGroups.append("<li>" + (i + 1) + "</li>");
		}
		// 获取序号元素的jQuery对象数组
		var $sn = $indexGroups.appendTo($container).children("li").fadeTo('fast', "0.6");

		// 为每一个图片绑定hover事件
		$images.hover(function() {
			pause();
		}, function() {
			timer = setTimeout(run, options.time);
		});

		// 为每一个li标签绑定hover事件
		$sn.css(options.liCSS).hover(function() {
			index = $.trim($(this).text()) - 1; // 计算当前图片的索引

			if (prevIndex != index) {
				hide(prevIndex); // 隐藏上一张图片
				show(index); // 显示当前索引的图片
			}

			prevIndex = index; // 记住当前图片索引

			pause(); // 计时器停暂停运行
		}, function() {
			prevIndex = $.trim($(this).text()) - 1;
			timer = setTimeout(run, options.time);
		});

		function run() {
			// 计算index值，如果指定显示正在显示的图片，则计时器将显示下一幅图片
			index = index == prevIndex ? index + 1 : index;
			// 计算index值，超过最后索引值则重置
			index = index > lastIndex ? 0 : index;
			$container.initAnimate(options.animate);

			hide(prevIndex); // 隐藏上一幅图片
			show(index); // 显示当前图片

			prevIndex = index;
			index++;

			timer = setTimeout(run, options.time);
		}

		function show(index) {
			//$("#consle").html("index: " + index + ", prevIndex: " + prevIndex); // 测试代码
			options.onShow($images.eq(index));
			
			if (options.showTitle) {
				$title_bar.text($images.eq(index).parent("a").attr("title"));
			}

			$sn.eq(index).css(options.focusIdxCSS).fadeTo(0, "1");
		}

		function hide(index) {
			options.onHide($images.eq(index));
			$sn.eq(index).css(options.blurIdxCSS).fadeTo(1000, "0.6");
		}

		function pause() {
			options.onStop();
			clearTimeout(timer);
		}

		$container.play = function(){options.onStart();run();return $container;}
		$container.stop  = function(){options.onStop();pause();return $container;}

		// 定义动画
		$container.initAnimate = function(animate) {
			var cur_animate = $.extend({}, options.animate, animate);
			var direction = cur_animate.direction || 0;
			var method = cur_animate.method || 0;

			var directionArray = ',fix,top,right,bottom,left,zoomshow,zoomhide'.split(',');
			// http://jqueryui.com/demos/effect/easing.html
			var methodArray = 'linear,swing,easeInQuad,easeOutQuad,easeInOutQuad,easeInCubic,easeOutCubic,easeInOutCubic,easeInQuart,easeOutQuart,easeInOutQuart,easeInQuint,easeOutQuint,easeInOutQuint,easeInSine,easeOutSine,easeInOutSine,easeInExpo,easeOutExpo,easeInOutExpo,easeInCirc,easeOutCirc,easeInOutCirc,easeInElastic,easeOutElastic,easeInOutElastic,easeInBack,easeOutBack,easeInOutBack,easeInBounce,easeOutBounce,easeInOutBounce'.split(',');
			
			var r_method = !options.animate.method ? parseInt(Math.random() * 32) + 1 : options.animate.method;
			
			cur_animate.direction = typeof direction === 'number' ? directionArray[direction] : direction;
			cur_animate.method = typeof method === 'number' ? methodArray[method || r_method] : method;
			cur_animate.method = typeof $.effects === 'object' ? cur_animate.method : 'linear';

			switch(cur_animate.direction) {
				case 'fix' : // 渐渐互溶模式
					options.onShow = function($img) {return $img.fadeIn(cur_animate.duration);};
					options.onHide = function($img) {return $img.fadeOut(cur_animate.duration);};
					break;
				case 'left' : // 水平向左
					options.onShow = function($img) {
						return $img.css({"left": options.width}).show().animate({"left": "0px"}, cur_animate.duration, cur_animate.method);
					};
					options.onHide = function($img) {
						return $img.animate({"left": "-=" + options.width}, cur_animate.duration, cur_animate.method, function(){ $img.hide().css({top:'0px',left:'0px'}); });
					};
					break;
				case 'right' : // 水平向右
					options.onShow = function($img) {
						return $img.css({"left": "-" + options.width}).show().animate({"left": "0px"}, cur_animate.duration, cur_animate.method);
					};
					options.onHide = function($img) {
						return $img.animate({"left": options.width}, cur_animate.duration, cur_animate.method, function(){ $img.hide().css({top:'0px',left:'0px'}); });
					};
					break;
				case 'top' : // 垂直向上
					options.onShow = function($img) {
						return $img.css({"top": options.height}).show().animate({"top": "0px"}, cur_animate.duration, cur_animate.method);
					};
					options.onHide = function($img) {
						return $img.animate({"top": "-" + options.height}, cur_animate.duration, cur_animate.method, function(){ $img.hide().css({top:'0px',left:'0px'}); });
					};
					break;
				case 'bottom' : // 垂直向下
					options.onShow = function($img) {
						return $img.css({"top": "-" + options.height}).show().animate({"top": "0px"}, cur_animate.duration, cur_animate.method);
					};
					options.onHide = function($img) {
						return $img.animate({"top": options.height}, cur_animate.duration, cur_animate.method, function(){ $img.hide().css({top:'0px',left:'0px'}); });
					};
					break;
				case 'zoomshow' : // 缩放显示效果
					var zoomRatio = options.animate.zoomRatio;
					options.onShow = function($img) {
						return $img.css({
							'top':-((zoomRatio - 1) / 2) * $img.height(),
							'left': -((zoomRatio - 1) / 2) * $img.width(),
							'width': zoomRatio * 100 + '%',
							'height': zoomRatio * 100 + '%',
							'z-index' : '2'
						}).show().animate({width:'100%',height:'100%','top':'0','left':'0'}, cur_animate.duration, cur_animate.method, function(){
							$img.css({'z-index':'1'});// 动画执行结束后置为中间层，采用当前图片作为下一次动画执行背景
						});
						return $img;
					};
					options.onHide = function($img) {
						setTimeout(function(){ $img.css({'z-index':'0'}); }, cur_animate.duration + 1);// 一个动画执行周期后自动置为底层
						return $img.css({'z-index':'1'}); // 隐藏时置于中间层
					};
					break;
				case 'zoomhide' :  // 缩放隐藏效果
					var zoomRatio = options.animate.zoomRatio;
					options.onShow = function($img) {
						return $img.css({'z-index':'1'}).fadeIn(cur_animate.duration);// Step3：显示时置为中间层
					};
					options.onHide = function($img) {
						return $img.css({'z-index':'2'}) // Step1：隐藏时置于顶层
							.show().animate({
							'top':-((zoomRatio - 1) / 2) * $img.height(),
							'left': -((zoomRatio - 1) / 2) * $img.width(),
							'width': zoomRatio * 100 + '%',
							'height': zoomRatio * 100 + '%',
							'opacity' : '0' // 置为透明
						}, cur_animate.duration, cur_animate.method, function(){
							// Step2：动画执行后置为底层和不透明
							$img.css({width:'100%',height:'100%','top':'0','left':'0','z-index':'0','opacity':'1'});
						});
						return $img;
					};
					break;
				default : // 随机
					var r_direction = parseInt(Math.random() * 5) + 1;
					return this.initAnimate($.extend({}, options.animate, {direction:r_direction}));
			}
			return this;
		}

		return $container.initAnimate(options.animate).show();
	}
})(jQuery);