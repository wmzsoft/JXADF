/**
 * @author : cxm
 * @date : 2013-11-07
 * @desc : 表格扩展功能，目前包含以下2个功能 1，表头固定 2，列固定
 * @mind : 创建3个表格，左上角固定的列和表头(fixHeaderColumn)， 表头(fixHeaderTbale)，
 *       列(fixColumnTable)
 * @version: 1.0
 */
// 创建一个闭包
(function($) {
	// 定义内部变量
	var scrollTop = 0;
	var scrollLeft = 0;

	var srcTable;
	var opts;

	// 插件的定义
	$.fn.flextable = function(options) {
		srcTable = this;

		debug("传入的表格：");
		debug(srcTable);

		// 创建默认参数
		opts = $.extend({}, $.fn.flextable.defaults, options);
		// 遍历所有对象，并对每个对象进行操作
		return this.each(function(index) {
			var $this = $(this);
			fixTable($this, opts.column, index);
		});
	};

	// 私有函数：debug
	function debug($obj) {
		if (window.console && window.console.log)
			window.console.info($obj);
	}

	/**
	 * 刷新表格后，请空原有的数据。
	 */
	function clear(table) {
		debug("clear() : 开始清除原有的表格内容。。");
		scrollTop = 0;
		scrollLeft = 0;

		// 清理表格数据
		var tableId = table.attr("id");
		$("#" + tableId + "_fixColumnTable").remove();
		$("#" + tableId + "_fixHeaderTable").remove();
		$("#" + tableId + "_fixHeaderColumn").remove();
	}

	function refreshTable() {
		debug("refreshTable() : 开始刷新表格。。。");
		srcTable.flextable();
	}

	/**
	 * @desc 固定表格主函数
	 * @param table
	 *            需要固定的表格
	 * @param column
	 *            需要固定的列数
	 * @param index
	 *            表格ID索引
	 */
	function fixTable(table, column, index) {
		clear(table);

		var tableId = table.attr("id");
		// 创建fixColumnTable - table
		var fixColumnTable = $("<table></table>");
		var fixColumnTableId = tableId + "_fixColumnTable";
		fixColumnTable.attr("id", fixColumnTableId);
		createFixColumnTable(table, fixColumnTable, column);

		// 创建fixHeaderTbale - table
		var fixHeaderTable = $("<table></table>");
		var fixHeaderTableId = tableId + "_fixHeaderTable";
		fixHeaderTable.attr("id", fixHeaderTableId);
		createFixHeaderTable(table, fixHeaderTable);

		// 创建fixHeaderColumn - table
		var fixHeaderColumn = $("<table></table>");
		var fixHeaderColumnId = tableId + "_fixHeaderColumn";
		fixHeaderColumn.attr("id", fixHeaderColumnId);
		createFixHeaderColumn(table, fixHeaderTable, fixHeaderColumn, column);

		// 添加事件监听
		// 添加滚动事件
		$(window).scroll(function() {
			// IEbug ： 该事件中无法直接引用上面的table，fxiColumnTable, fixHeaderTable,
			// fixHeaderColumn等变量，只能通过ID重新查询出来。
			// 在chrome下面可以直接引用，为了兼容，全部改成使用ID查询
			
			var $tableHeader = $("#" + tableId).find("thead:first");
			var srcHeaderTop = $tableHeader[0].getBoundingClientRect().top;
			var srcHeaderLeft = $tableHeader[0].getBoundingClientRect().left;

			if (srcHeaderTop <= 0) {
				$("#" + fixHeaderTableId).css("display", "");
				$("#" + fixHeaderColumnId).css({
					"top" : "0"
				});
			} else {
				$("#" + fixHeaderTableId).css("display", "none");
				$("#" + fixHeaderColumnId).css({
					"top" : Util.getFixHeaderColumnSelfStyle($("#" + tableId)[0].getBoundingClientRect().top, "top")
				});
			}
			if (srcHeaderTop != scrollTop) {
				// 垂直滚动

				// 设置表头
				$("#" + fixHeaderTableId).css("position", "fixed");
				$("#" + fixHeaderTableId).css("top", 0);
				calcFixHeaderSize($tableHeader, $("#" + fixHeaderTableId));

				// 设置列
				$("#" + fixColumnTableId).css("position", "absolute");
				//改为absolute的定位，相对于父亲，left应该为0保证为最左
				$("#" + fixColumnTableId).css("left", 0);
				$("#" + fixColumnTableId).css("top", Util.getFixColumnSelfStyle($tableHeader.height(), "top"));

				// 保存起来,作为下次判断
				scrollTop = srcHeaderTop;
			} else if (srcHeaderLeft != scrollLeft) {
				// 水平滚动
				$("#" + fixHeaderTableId).css("position", "absolute");
				$("#" + fixHeaderTableId).css("top", 0 - srcHeaderTop);

				// 设置列
				$("#" + fixColumnTableId).css("position", "fixed");
				$("#" + fixColumnTableId).css("left", table.position().left);
				$("#" + fixColumnTableId).css("top", srcHeaderTop + $tableHeader.height());

				// 保存起来,作为下次判断
				scrollLeft = srcHeaderLeft;
			}
		});
		// 添加列宽改变监听事件
		// FIXME : 这个效率比较低
		/*table.resize(function() {
			debug("resize() : 改变表格大小。。。");
			var $tableHeader = $(srcTable[index]).find("thead");
			calcFixHeaderSize($tableHeader, fixHeaderTable);

			calcFixColumnTable($(srcTable[index]), fixColumnTable);

			calcFixHeaderColumn($(srcTable[index]), fixHeaderTable, fixHeaderColumn);
		});*/

	}

	/**
	 * 创建固定表头表格
	 */
	function createFixHeaderTable(table, fixHeaderTable) {
		var $tableHeader = table.find("thead");
		var $fixHeader = createFixHeader($tableHeader);
		fixHeaderTable.css({
			"display" : "none",
			"border-collapse" : "collapse",
			"padding" : "0"
		});
		table.append(fixHeaderTable.append($fixHeader));
	}

	/**
	 * 表头克隆及样式调整
	 */
	function createFixHeader($tableHeader) {
		var $fixedHeader = $tableHeader.clone(true);
		// id指定和样式修复
		$fixedHeader.attr("id", "fixHeaderTable_header").css({
			"border-collapse" : "collapse"
		});
		$fixedHeader.removeClass("ui-resizable");
		$("th", $fixedHeader).addClass("fixHeader");
		calcFixHeaderSize($tableHeader, $fixedHeader);
		return $fixedHeader;
	}

	/**
	 * 计算表头样式
	 */
	function calcFixHeaderSize($refHeader, $aimHeader) {
		debug("calcFixHeaderSize() : 调整表头样式。。。");
		// 重新设置坐标
		$aimHeader.css("left", $refHeader[0].getBoundingClientRect().left);
		// 固定fixedHeader列宽
		$aimHeader.css("width", $refHeader.width());

		// 列宽重定义
		var $srcHeaderTh = $("th", $refHeader);
		var $fixHeaderTh = $("th", $aimHeader);

		$.each($srcHeaderTh, function(idx, domObj) {
			$($fixHeaderTh[idx]).attr("width", domObj.clientWidth);
		});
	}

	/**
	 * 创建列的固定
	 */
	function createFixColumnTable(table, fixColumnTable, column) {
		if (column <= 0) {
			return;
		}

		// 默认选择列要自动带上
		column++;
		// 先找出tbody tr
		var $tr = table.find("tbody:last").find("tr");
		debug($tr);
		$.each($tr, function(tridx) {
			// 找出需要的列
			var $fixColumnTr = $("<tr></tr>");
			$fixColumnTr.attr("id", "fixColumnTr_" + tridx);
			var $td = $(this).find("td");
			$.each($td, function(tdidx) {
				if (tdidx < column) {
					$fixColumnTr.append($(this).clone(true));
					/* $(this).css("visibility", "hidden"); */
				}
			});
			fixColumnTable.append($fixColumnTr);
		});
		fixColumnTable.css({
			"background-color" : opts.bgColor,
			"border-collapse" : "collapse",
			"position" : "absolute"
		});
		calcFixColumnTable(table, fixColumnTable);
		table.append(fixColumnTable);
	}

	/**
	 * 计算列的宽度和高度及样式调整
	 */
	function calcFixColumnTable(table, fixColumnTable) {
		fixColumnTable.css({
			"top" : Util.getFixColumnSelfStyle($(table.find("thead")[0]).height(), "top"), // 表头的高度
			"left" : 0
		});
		// 获取行
		var srcTbody = $(table.find("tbody")[1]);
		var srcTr = srcTbody.find("tr");
		var fixTr = fixColumnTable.find("tr");

		$.each(fixTr, function(fixTrIdx) {
			$(this).css("height", $(srcTr[fixTrIdx]).height());

			var srcTd = $(srcTr[fixTrIdx]).find("td");
			var fixTd = $(this).find("td");

			$.each(fixTd, function(fixTdIdx, fixTdObj) {
				$(fixTdObj).css("width", Util.getFixColumnTdStyle(srcTd[fixTdIdx].clientWidth, "width"));
				$(fixTdObj).css("height", Util.getFixColumnTdStyle($(srcTd[fixTdIdx]).height(), "height"));
			});
		});
	}

	/**
	 * 创建固定的列头和行
	 */
	function createFixHeaderColumn(table, fixHeaderTable, fixHeaderColumn, column) {
		if (column <= 0) {
			return;
		}
		var $th = fixHeaderTable.find("th");
		var $tr = $("<tr></tr>");
		fixHeaderColumn.append($tr.attr("id", table.attr("id") + "_fixHeaderColumnTr"));

		column++;
		$.each($th, function(thIdx) {
			if (thIdx < column) {
				var $headerColumnTh = $(this).clone(true);
				/* $(this).css("visibility", "hidden"); */
				$headerColumnTh.attr("id", table.attr("id") + "_fixHeaderColumnTh_" + thIdx).css({
					"background-color" : 'rgb(139, 168, 203)',
					"border" : "1px solid #7C9CC3",
					"padding" : "0"
				});
				$tr.append($headerColumnTh);
			}
		});
		fixHeaderColumn.css({
			"background-color" : opts.bgColor,
			"border-collapse" : "collapse",
			"position" : "fixed"
		});
		calcFixHeaderColumn(table, fixHeaderTable, fixHeaderColumn);
		table.append(fixHeaderColumn);
	}

	function calcFixHeaderColumn(table, fixHeaderTable, fixHeaderColumn) {
		fixHeaderColumn.css({
			"top" : Util.getFixHeaderColumnSelfStyle(table.find("thead")[0].getBoundingClientRect().top <= 0 ? 0
					: table.find("thead")[0].getBoundingClientRect().top, "top"),
			"left" : Util.getFixHeaderColumnSelfStyle(table.find("thead")[0].getBoundingClientRect().left, "left")
		});

		var fixHeaderColumnTr = fixHeaderColumn.find("tr");
		// 设置tr的高度
		fixHeaderColumnTr.css("height", Util.getFixHeaderColumnTrStyle($(table.find("thead")[0]).height(), "height"));
		// 设置th的样式
		var $headerColumnTh = fixHeaderColumnTr.find("th");
		var $fixHeaderTh = fixHeaderTable.find("th");
		$.each($headerColumnTh, function(thIdx) {
			$(this).css({
				"width" : Util.getFixHeaderColumnTdStyle($($fixHeaderTh[thIdx]).width(), "width"),
				"height" : Util.getFixHeaderColumnTdStyle($($fixHeaderTh[thIdx]).height(), "height")
			});
		});

	}

	/**
	 * 工具类
	 */
	var Util = {
		/**
		 * 获取fixcolumn 的自身样式，在每个浏览器下面表现都不一样
		 */
		getFixColumnSelfStyle : function(value, attr) {
			if ($.JxUtil.browser.isIE) {
				if ("top" == attr) {
				} else if ("width" == attr) {
				} else if ("left" == attr) {
				} else if ("height" == attr) {
				}
			} else if ($.JxUtil.browser.isFF) {
				// TODO : 根据firefox的效果计算偏差
			} else if ($.JxUtil.browser.isChrome) {
				if ("top" == attr) {
				} else if ("width" == attr) {
				} else if ("left" == attr) {
				} else if ("height" == attr) {
				}
			}
			return value;
		},
		/**
		 * 获取fixcolumn 的TD样式，在每个浏览器下面表现都不一样
		 */
		getFixColumnTdStyle : function(value, attr) {
			if ($.JxUtil.browser.isIE) {
				if ("top" == attr) {
				} else if ("width" == attr) {
					value += 1;
				} else if ("left" == attr) {
				} else if ("height" == attr) {
				}
			} else if ($.JxUtil.browser.isFF) {
				// TODO : 根据firefox的效果计算偏差
			} else if ($.JxUtil.browser.isChrome) {
				if ("top" == attr) {
				} else if ("width" == attr) {
				} else if ("left" == attr) {
				} else if ("height" == attr) {
				}
			}
			return value;
		},

		/**
		 * 计算fixHeaderColumn 自己的Style（top，width，left，height）等
		 * 
		 */
		getFixHeaderColumnSelfStyle : function(value, attr) {
			if ($.JxUtil.browser.isIE) {
				if ("top" == attr) {
					value -= 0.5;
				} else if ("width" == attr) {
					value -= 1;
				} else if ("left" == attr) {
					value -= 0.5;
				} else if ("height" == attr) {

				}
			} else if ($.JxUtil.browser.isFF) {
				// TODO : 根据firefox的效果计算偏差
			} else if ($.JxUtil.browser.isChrome) {
				if ("top" == attr) {
				} else if ("width" == attr) {
				} else if ("left" == attr) {
				} else if ("height" == attr) {
					value -= 1;
				}
			}
			return value;
		},

		getFixHeaderColumnTrStyle : function(value, attr) {
			if ($.JxUtil.browser.isIE) {
				debug("调用getFixHeaderColumnTrStyle的IE修复方法：");
				if ("top" == attr) {

				} else if ("width" == attr) {
					value -= 1;
				} else if ("left" == attr) {
					value = 0;
				} else if ("height" == attr) {
				}
			} else if ($.JxUtil.browser.isFF) {
				// TODO : 根据firefox的效果计算偏差
			} else if ($.JxUtil.browser.isChrome) {
				debug("调用getFixHeaderColumnTrStyle的Chrome修复方法：");
				if ("top" == attr) {
				} else if ("width" == attr) {
					value += 1;
				} else if ("left" == attr) {
				} else if ("height" == attr) {
				}
			}
			return value;
		},

		/**
		 * 计算fixHeaderColumn td的Style（top，width，left，height）等
		 * 
		 */
		getFixHeaderColumnTdStyle : function(value, attr) {
			if ($.JxUtil.browser.isIE) {
				if ("top" == attr) {
				} else if ("width" == attr) {
					value += 1;
				} else if ("left" == attr) {
					value = 0;
				} else if ("height" == attr) {

				}
			} else if ($.JxUtil.browser.isFF) {
				// TODO : 根据firefox的效果计算偏差
			} else if ($.JxUtil.browser.isChrome) {
				if ("top" == attr) {
					
				} else if ("width" == attr) {
				} else if ("left" == attr) {

				} else if ("height" == attr) {
				}
			}
			return value;
		}
	};

	// 定义暴露setColumn函数
	$.fn.flextable.setColumn = function(number) {
		$.fn.flextable.defaults.column = number;
		srcTable.flextable();
	};
	
	/**
	 * 重新修正浮动的header
	 */
	$.fn.flextable.refreshHeader = function(srcHeader, fixHeader){
		calcFixHeaderSize(srcHeader, fixHeader);
	};

	// 插件的默认参数
	$.fn.flextable.defaults = {
		column : 0,
		bgColor : 'silver'
	};
})(jQuery);

jQuery.JxUtil = {
	browser : {
		isIE : /trident/.test(navigator.userAgent.toLowerCase()) || /msie/.test(navigator.userAgent.toLowerCase()),
		isFF : /firefox/.test(navigator.userAgent.toLowerCase()),
		isChrome : /chrome/.test(navigator.userAgent.toLowerCase())
	}
};