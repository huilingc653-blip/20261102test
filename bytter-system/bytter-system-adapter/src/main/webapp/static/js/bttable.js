/**
 * 普通table组件
 * 本组件依赖jquery
 * 刘勇军
 * 2017年6月5日
 * 0.0.1 普通table
 * 0.0.2 优化checkboxs
 * 0.0.3 增加scroll参数
 * 0.0.4 增加selectCss参数用于设置选中时，是否有样式
 * 0.0.5 添加data参数，用于直接传表格参数
 * 0.0.6 添加reload参数，重新加载表格数据
 * 0.0.7 添加reload参数，重新加载表格数据
 * 0.0.8 重新加载时，先清除table内容，再去获取内容
 * 0.0.9 添加className参数
 * 0.0.10 添加下拉表格验证可功能
 * 0.0.11 添加解绑事件
 * 0.0.12 解决多个add按纽的事件冲突问题
 * 0.0.13 新增下拉表格的内容验证
 * 0.0.14 如果是下拉表格，取消layui-form class
 * 0.0.15 返回数据为null时，重置为空对像
 * 0.0.16 添加了键盘上下操作事件
 * 0.0.17 行内编辑新增加返回id参数
 * 0.0.18 数据支持对象
 * 0.0.19 行内注册事件条件进行了过滤
 * 0.0.20 新增加clickTrSelected ,设置点击行是否被选中
 * 0.0.21 新增attr方法 ,暂时只能支持mulselect
 * 0.0.22 有内置添加按纽时，通过判断表格高度是否达到了参数scroll，动态设置overflorw-Y的值.
 * 0.0.23 添加others 按钮.
 * 0.0.24 新增draw重绘方法.
 * 0.0.25 新增headerCheckbox参数，用于设置表头是否需要checkbox.
 * 0.0.26 新增加清空按钮
 * 0.0.27 新增setSelectedRow getSelectedData 方法
 * 0.0.28 新增formRenderInTable 默认为false，全页面绘制表单；true 代码只在表格内绘制表单
 */

bt.define(['jquery', 'form'], function (exports) {
    "use strict"
    var $ = bt.jquery, form = bt.form();
    var Table = function () {
        this.config = {
            elem: '',
            checkbox: false,
            mulselect: true,
            type: "POST",
            url: '',
            checkChildCallback: '',
            clickCallback: '',
            tableInitCallback: '',
            delRowCallback: '',
            reviewRowCallback: '',
            editRowCallback: '',
            selectCss: true,
            urldata: '',
            clickTrSelected: true,//,设置点击行是否被选中
            valueFromTable: true,//下拉表格时才启动，设置下拉框input内容必须取表格内容
            // scrollX:true,
            scrollY: '',//容器出现滚动条高度
            headerCheckbox: false, //设置表头是否要checkbox,false代码不要，其它值与checkbox共同决定是否有checkbox
            // data:[]
            // colDrage:true,
            // checkAllCallback:'',
            formRenderInTable:false // 默认为false，全页面绘制表单；true 代码只在表格内绘制表单

        };
        this.firstRow = {};
        this.num;
        this.addFlag = false,
            this.tableElem = '',
            this.select = "";
        this.tableData;
        this.reloadFlag = 0;
        this.addButton = false;//判断表格是否内置的添加行的按钮，目的是：true 代表通过添加行达到一定高度时，添加Y滚动条；false 代表设置固定高度添加Y滚动条。
    }

    Table.prototype.v = '0.0.21';

    Table.prototype.set = function (ops) {
        var that = this;
        $.extend(true, that.config, ops);
        return that
    }

    Table.prototype.init = function (render) {
        var that = this;
        var _config = that.config;
        this.num = parseInt(Math.random() * 1000000);
        this.select = this.config.selectCss ? "selected selectedCss" : "selected";
        if (typeof (_config.elem) !== 'string' && typeof (_config.elem) !== 'object') {
            throwError('table error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }

        var $container;

        if (typeof (_config.elem) === 'string') {
            $container = $('' + _config.elem + '');
            that.tableElem = _config.elem.substr(1);
            this.num = that.tableElem + that.num;//拼接表格id
        }

        if (typeof (_config.elem) === 'object') {
            $container = _config.elem;
        }
        if ($container.length === 0) {
            throwError('bttable error:找不到elem参数配置的容器，请检查.');
        }
        if (!_config.columns) {
            throwError('bttable error:找不到columns参数，请检查.');
        }
        $container.addClass("layui-table layui-form");

        var headerCheckbox = $container.find('#checkAll');//查询表头checkAll元素，如果有，就不用添加
        if (_config.checkbox && !headerCheckbox.length) { //
            if (_config.headerCheckbox !== false) {
                $container.find('thead th:first-child').before('<th width="30px" id="checkAll" style="padding: 6px 10px;"><input type="checkbox" name="checkAll" lay-filter="checkAll' + this.num + '" lay-skin="primary" ></th>');
            } else {//如果设置了表头不需要checkbox，就只要添加一个th就可以了
                $container.find('thead th:first-child').before('<th width="30px" id="checkAll"></th>');
            }
        }

        if (headerCheckbox.length && headerCheckbox.find('[type="checkbox"]').length) {
            headerCheckbox.find('[type="checkbox"]')[0].checked = false;
        }

        if (!_config.mulselect && headerCheckbox.length > 0) {
            throwError('bttable error:mulselect参数false时，不能设置表头checkboxs，请检查.');
        }

        if (!_config.clickTrSelected) {
            that.addFlag = true;
        }
        if(_config.checkbox){
            $container.attr('lay-filter','bttable_filter'+that.num)
        }

        $(function () {
            that.scrollCss();
            //生成table页面
            that.getData(render);

            if (!render && _config.checkbox && _config.headerCheckbox !== false) {
                that.checkboxToggleAll();
            }

            if (_config.buttons) {
                that.clickButtons()
            }

            that.addOverflow()//表格添加overflow-Y属性


            //托拽功能
            // if(_config.colDrage){
            // 	bt.use('colResize',function(){
            // 		$container.colResizable({
            // 			minWidth:30,//设置最小宽度
            // 			disabledColumns:[0,1],//禁止托拽功能
            // 		});
            // 	})
            // }
        })
    }

    //getDate获取表格数据
    Table.prototype.getData = function (render) {
        var that = this, _config = that.config;
        if (_config.data && typeof _config.data === "object" && _config.data.length !== 0) {
            that.appendHtmlCallback(_config.data);
            if (that.config.SelectTable) {
                $(_config.elem).removeClass("layui-form")
            }
            that.doRowCallback(_config.data);
        } else {
            //解决无数据时下拉框不显示的问题
            if ($(_config.elem).find("tbody").length == 0 && _config.SelectTable) {
                var cols = $(_config.elem).find("thead tr th").length;
                $(_config.elem).append('<tbody><tr><td colspan="' + cols + '" class="" align="center"><img src="'+basePath+'/static/v3.2/images/pic_blank.png" class="empty-pic"><div class="empty-text">' + bt.lang("common.msg.temporarily_no_data") + '</div></td></tr></tbody>');
            }

            if (_config.searchInput && !render) {
                that.search();
                that.verifySelectTable();
                that.deleteValue();
            }
            //结束
            if (_config.url) {
                if(_config.contentType === 'application/json'){
                    $.ajax({
                        type: _config.type,
                        url: _config.url,
                        data: JSON.stringify(_config.urldata),
                        dataType: 'json',
                        contentType: "application/json",
                        success: function (result, status, xhr) {
                            that.ajaxSuccess(result)
                        },
                    });
                }else{
                    $.ajax({
                        type: _config.type,
                        url: _config.url,
                        data: _config.urldata,
                        dataType: 'json',
                        success: function (result, status, xhr) {
                            that.ajaxSuccess(result)
                        },
                    });
                }
            } else {
                that.doRowCallback();
            }
        }
    }

    Table.prototype.ajaxSuccess = function (result){
        var that = this,_config = that.config,data;
        if (result === null) {
            data = [];
        } else if (result.data !== undefined) {
            data = result.data;
        } else {
            data = result;
        }
        that.appendHtmlCallback(data);
        if (!that.addFlag) {
            that.clickRow(data);
        }
        //注册点击事件
        if (_config.checkbox && that.addFlag) {
            that.checkboxBodyCheck();
        }
        if (that.config.SelectTable) {
            $(_config.elem).removeClass("layui-form")
        }
        //点击行回调
        that.doRowCallback(data);
    }
    //异步追加html之后的各种回调
    Table.prototype.appendHtmlCallback = function (data, draw) { //外部重绘时，会传入draw标识
        var that = this, _config = that.config;
        var html = that.getHtml(data);
        if ($(_config.elem).children("tbody").length !== 0) {
            if (data.length) { //有数据时
                $(_config.elem).children("tbody").html(html);
            } else {　//无数据时
                // $(_config.elem).children("tbody").find('td').text('暂无数据');
                if (_config.SelectTable) {
                    var colSpan = _config.columns.length;
                    $(_config.elem).children("tbody").html('<tr><td colspan="' + colSpan + '" align="center">' + bt.lang("common.msg.temporarily_no_data") + '</td></tr>');
                } else { // 无数据时，清空表格
                    $(_config.elem).children("tbody").html(html);
                }
            }

            // that.getHeadHtml()
        }
        // 只绘制表格内的复选框
        if(that.formRenderInTable){
            form.render("checkbox",'bttable_filter'+that.num);
        }else {
            form.render("checkbox");
        }
        if (!draw && _config.tableInitCallback && typeof _config.tableInitCallback === "function") {
            _config.tableInitCallback(data);
            // if(_config.searchInput){
            //     that.search();
            //     // that.selectTableKeyUp();
            //     that.verifySelectTable();
            //     that.setSelectAttr();
            // }
        }
        if (_config.searchInput && !draw) {
            // that.search();
            // that.selectTableKeyUp();
            // that.verifySelectTable();
            that.setSelectAttr();
        }
        if (draw && _config.tableDrawCallback && typeof _config.tableDrawCallback === "function") {
            _config.tableDrawCallback(data);
        }
        that.tableData = data;
    }
    //getDate获取表格数据
    Table.prototype.getHtml = function (data) {
        if (!data) {
            return '';
        }
        var that = this;
        var _config = this.config;
        var columns = _config.columns;
        if ($(_config.elem).find("tbody").length == 0) {
            $(_config.elem).append('<tbody></tbody>');
        }
        var rowCount = data.length,
            row = '',
            footer = '',
            columnLen;
        var num = parseInt(Math.random() * 1000000);
        for (var i = 0; i < rowCount; i++) {
            var rowId = i;
            var dataObj = data[i];
            row += '<tr row ="' + i + '" data-id = "' + dataObj["id"] + '">';
            if (_config.checkbox) {
                row += '<td class = "center" style="width:40px;"><input type="checkbox" name="checkChildren" lay-filter="checkChildren' + this.num + '" lay-skin="primary" class="checkchild" ';
                if (dataObj["id"] !== undefined) {
                    row += 'data-id = "' + dataObj["id"] + '"';
                }
                row += '></td>';
            }

            layui.each(columns, function (j, v) {
                var data = v.data;
                var render = v.render;
                var className = v.className;
                if (j == 0) {
                    row += '<td data-id="' + dataObj["id"] + '"'
                } else {
                    row += '<td '
                }

                if (className && typeof className === "string") {
                    row += 'class ="' + className + '" '
                }

                if (data && !render) {
                    row += ' name="' + data + '" title ="' + dataObj[data] + '" >' + dataObj[data] + '</td>';
                }
                ;

                if (render && typeof render === "function") {
                    var html = "" ;
                    var html_ = render(dataObj, rowId);
                    if(html_){
                        html = html_.toString();
                    }
                    if (html && html.indexOf("data-type") !== -1) {
                        that.addFlag = true;
                    }
                    row += ' name="' + data + '" data-id="' + dataObj["id"] + '">' + html + '</td>';
                }
            })
            row += '</tr>';
        }
        return row;
    }
    Table.prototype.getHeadHtml = function () {
        var that = this;
        var $table = $(that.config.elem);
        var header = $table.children("thead").html();
        var tableid = that.config.elem.substring(1);
        var html = '<table id="' + tableid + '-scrollheader" style="width:100%;" class = "layui-table" >'
        html += "<thead>";
        html += header;
        html += "</thead>";
        $table.parent("div").before(html);
        $table.find("thead").hide();
        var $tableheader = $("#" + tableid + "-scrollheader")
        that.getHeadWidth($tableheader)

    }
    Table.prototype.getHeadWidth = function ($tableheader) {
        var that = this;
        var $table = $(that.config.elem);
        var tds = $table.find("tbody tr").eq(0).find("td");
        var headertds = $tableheader.find("thead tr th ")
        var width = [];
        bt.each(tds, function () {
            width.push($(this).width())
        })
        bt.each(headertds, function (i, v) {
            $(this).width(width[i])
        })
    }

    Table.prototype.clickRow = function (data) {
        var that = this;
        var _config = that.config;
        var $table = $(_config.elem);
        var select = that.select;
        $table.find("tbody tr").off('click').on("click", function (e) {
            var othis = $(this);
            //单选时
            if (!_config.mulselect) {
                othis.siblings("tr").removeClass(select);
                othis.siblings("tr").find("input").prop("checked", false)
            }
            othis.toggleClass(select);
            if (_config.checkbox) {
                if (othis.hasClass(select)) {
                    othis.find("input").prop("checked", "checked");
                } else {
                    othis.find("input").prop("checked", false)
                }
                $table.find("thead input").prop("checked", $table.find("input.checkchild").length == $table.find("input.checkchild:checked").length ? true : false);
                // 只绘制表格内的复选框
                if(that.formRenderInTable){
                    form.render("checkbox",'bttable_filter'+that.num);
                }else {
                    form.render("checkbox");
                }
            }
            if (_config.clickCallback && typeof _config.clickCallback === "function") {
                var selectedRowsData = that.updateDataSelect();
                if (that.config.SelectTable) {
                    var id = othis.attr("row");
                    _config.clickCallback(that.tableData[id], othis);
                    if (_config.valueFromTable) {
                        var $searchInput = othis.parents('.table-scroll').siblings('.bt-biz-accountTable');//获取输入框
                        var searchInputValue = $searchInput.val();//获取值
                        $searchInput.data('beforevalue', searchInputValue);//保存值
                    }
                } else {
                    _config.clickCallback(that.tableData, othis, selectedRowsData);
                }
            }
            //如果是下拉表格时，点击行后，清除掉被中样式
            if (that.config.SelectTable) {
                $table.parent().hide();
                $(this).removeClass(select);
            }
        })
    };

    //选中数据获取
    Table.prototype.rowData = function ($tr) {
        var _config = this.config;
        var select = this.select;
        var selected = $tr ? $tr : $(_config.elem).find('tbody tr.' + select + '');
        var rowData = [];
        if (selected.length === 0) {
            return rowData
        }
        layui.each(selected, function (i, select) {
            var tds = $(select).find('td');
            var data = {};
            layui.each(tds, function (i, td) {
                var name = $(td).attr("name");
                var text = $(td).text();
                if (name != undefined) {
                    data[name] = text;
                }
            })
            rowData.push(data)
        });
        return rowData
    };

    //选中row id
    Table.prototype.updateDataSelect = function () {
        var _config = this.config,
            select = this.select,
            selected = $(_config.elem).find('tbody tr.selected'),
            data = this.tableData;
        //重置选中参数为false;
        this.setSelectAttr();
        var selectedRowsData = [];
        if (selected.length !== 0 && data.length > 0) {
            bt.each(selected, function (j, s) {
                var rowId = $(s).attr("row");
                data[rowId].select = true;
                selectedRowsData.push(data[rowId])
            })
        }
        return selectedRowsData;
    };

    //给数据添加一个初始化的select属性
    Table.prototype.setSelectAttr = function () {
        var data = this.tableData;
        if (data && data.length > 0) {
            bt.each(data, function (i, v) {
                v.select = false
            })
        }
    };
    //全选设置
    Table.prototype.checkboxToggleAll = function () {
        var that = this,
            select = that.select,
            checkAllCallback = that.config.checkAllCallback;
        form.on('checkbox(checkAll' + this.num + ')', function (data) {
            var $elem = $(data.elem).parents("thead").siblings("tbody");
            $elem.find('input.checkchild').prop("checked", this.checked);
            var trs = $elem.find('tr');
            // 只绘制表格内的复选框
            if(that.formRenderInTable){
                form.render("checkbox",'bttable_filter'+that.num);
            }else {
                form.render("checkbox");
            }
            if (this.checked) {
                trs.addClass(select);
            } else {
                trs.removeClass(select);
            }
            that.updateDataSelect();
            if (checkAllCallback && typeof checkAllCallback === "function") {
               checkAllCallback(that.tableData)
            }
        });
    };

    //给tbodaycheckbox复选框
    Table.prototype.checkboxBodyCheck = function () {
        var that = this;
        var elem = that.config.elem,
            select = that.select,
            checkChildCallback = that.config.checkChildCallback;
        var filter = $(elem).find('tbody input[type ="checkbox"]').attr('lay-filter');
        form.on('checkbox(checkChildren' + that.num + ')', function (data) {

            var parent = $(data.elem).parents("tbody").siblings().find("th input");
            var checkboxs = $(data.elem).parents("tr").siblings("tr").find("input");
            //单选时
            if (!that.config.mulselect) {
                layui.each(checkboxs, function (i, v) {
                    v.checked = false;
                })
                $(data.elem).parents("tr").siblings("tr").removeClass(select)
            }
            parent.each(function (index, item) {
                item.checked = $(data.elem).parents("tbody").find("input.checkchild").length == $(data.elem).parents("tbody").find("input.checkchild:checked").length ? true : false;
            });
            if (data.elem.checked) {
                $(data.elem).parents("tr").addClass(select)
            } else {
                $(data.elem).parents("tr").removeClass(select)
            }
            // 只绘制表格内的复选框
            if(that.formRenderInTable){
                form.render("checkbox",'bttable_filter'+that.num);
            }else {
                form.render("checkbox");
            }
            //设置
            var selectedRowsData = that.updateDataSelect();

            if (checkChildCallback && typeof checkChildCallback === "function") {
                var $tr = $(data.elem).parent().parent("tr")
                checkChildCallback(that.tableData, $tr, selectedRowsData)
            }
        });
    }

    //下拉表格的搜索功能;
    Table.prototype.search = function () {
        var that = this;
        var $table = $(that.config.elem);
        if (that.config.valueFromTable) {
            var v = $.trim($(that.config.searchInput).val());
            if (v !== '') {
                $(that.config.searchInput).data('beforevalue', v);
            }
        }

        // 下拉表格的显示隐藏  控制下拉表格点击时不根据已选账户加载数据  zuoy 2017-11-6
        if (that.config.SelectTable) {
            $(that.config.searchInput).off("click").on("click", function (e) {
                //定位
                var tableScroll = $(this).siblings('.table-scroll');//下拉表格jq对象
                var top = $(this).offset().top + $(this).outerHeight() + 5 - $(window).scrollTop();
                var tableScrollHeight = tableScroll.outerHeight();
                if (top + tableScrollHeight > $(window).height() && top >= tableScrollHeight) {
                    tableScroll.removeClass("select-table");
                    tableScroll.addClass("select-table-up");
                } else {
                    tableScroll.removeClass("select-table-up");
                    tableScroll.addClass("select-table");
                }
                $table.show();
                $(this).siblings("div").show();
            });
        }
        $(this.config.searchInput).off("input propertychange focus").on("input propertychange focus", function (e) {
            var searchTxt = $(this).val();
            $table.show();
            $table.find("tbody tr")
                .removeClass("selectedCss")
                .removeClass('isShow')
                .hide()
                .filter(":contains('" + searchTxt + "')")
                .show()
                .addClass('isShow');

            if (that.config.SelectTable) {
                $(this).siblings("div").show();
            }
            ;
            that.selectTableKeydown()
        })

        if (that.config.SelectTable) {
            $(document).on('click', 'body', function (e) {
                var target = e.target;
                if ($(target).closest(that.config.searchInput).length == 0) {
                    if (!$table.parent("div").is(":hidden")) { //判断下拉框是否可见
                        var $searchInput = $(that.config.searchInput);
                        var v = $.trim($searchInput.val());
                        var beforeValue = $searchInput.data('beforevalue');
                        if ($table.find("tbody tr.isShow").length === 1) {//判断下拉框tr是否可见，如果可见，说明找到了对应的值
                            if (that.config.valueFromTable) {
                                var rowid = $table.find("tbody tr.isShow").attr('row');
                                if (beforeValue !== v) {
                                    if (v === '') { //清空时调用
                                        that.config.clickCallback({}, $table.find("tbody tr.isShow"));
                                    } else {
                                        $searchInput.val('');
                                    }
                                    $searchInput.data('beforevalue', '');//关闭下拉框时，把值赋值给beforevalue
                                }
                            }
                        } else {
                            if (that.config.valueFromTable) {
                                if (v === '' && beforeValue !== v) { //当无输入值，且上一个值不为空时，启用回调；
                                    that.config.clickCallback({}, $searchInput);
                                    $searchInput.data('beforevalue', v);//更新beforevalue;
                                } else {
                                    $searchInput.val($searchInput.data('beforevalue'));//非空值，且
                                }
                            }
                        }
                        $table.parent("div").hide();
                    }
                    ;
                }
            })
        }
    }
    //下拉表格的键盘事件；
    Table.prototype.selectTableKeydown = function () {
        var that = this;
        var $table = $(that.config.elem);
        var index = -1;
        $(this.config.searchInput).off('keydown').on('keydown', function (event) {
            var trs = $table.find("tbody tr:visible");
            var container = $table.parent();
            var flag = container[0].scrollHeight > container[0].clientHeight;
            var trHeight = $table.find('tr').height();//行高
            // 检验到第几行时才滚动
            var stage = Math.floor(container[0].clientHeight / trHeight) - 2;
            if (event.keyCode == 38) {  //向上38，向下40，回车13
                if (index <= 0) {
                    // index = trs.length;
                } else {
                    index--;
                }
                $(trs[index]).addClass("selectedCss");
                if ((trs.length - index) > stage) {
                    var totop = container.scrollTop() - trHeight;
                    that.scrolly(container, $(trs[index]), flag, totop)
                }
                $(trs[index + 1]).removeClass("selectedCss");

            }
            //向下
            else if (event.keyCode == 40) {//向下
                if (index >= trs.length - 1) {
                    // index = -1;
                } else {
                    index++;
                }
                $(trs[index]).addClass("selectedCss");
                if (index > stage) {
                    var top = (index - stage) * trHeight;
                    that.scrolly(container, $(trs[index]), flag, top)
                }
                $(trs[index - 1]).removeClass("selectedCss");
            }
            //回车
            if (event.keyCode == 13) {//回车
                if (index >= 0) {
                    if (that.config.clickCallback && typeof that.config.clickCallback === "function") {
                        var othis = $(trs[index]);
                        var rowid = othis.attr('row');
                        that.config.clickCallback(that.tableData[rowid], othis);
                        $(trs[index]).removeClass("selectedCss");
                        if (that.config.valueFromTable) {
                            othis.parents('.table-scroll').siblings('.bt-biz-accountTable').data('beforevalue', othis.parents('.table-scroll').siblings('.bt-biz-accountTable').val());//关闭下拉框时，把值保存在beforevalue中
                        }
                    }
                }
                index = -1;
                $table.parent().hide();
                $(this).blur();
                event.preventDefault ? event.preventDefault() : (event.returnValue = false);
            }

        })
    };
    //手动触发滚动条滚动
    Table.prototype.scrolly = function (container, tolocation, flag, top) {
        var step = 0;
        if (flag) {
            // container.animate({
            //  		// scrollTop: tolocation.offset().top - container.offset().top + container.scrollTop()
            //  		scrollTop: top,
            // 		}, 100);//2秒滑动到指定位置
            container.scrollTop(top)
        }
    }

    //验证输入框的值是否是下拉表格的数据
    Table.prototype.verifySelectTable = function () {
        var that = this;
        form.verify({
            //模糊匹配数据
            tableData: function (value, item) {
                if ($.trim(value) !== '') { //value为空时，不校验
                    var $table = $(item).siblings("div").find("table");
                    var tr = $table.find("tbody tr").filter(":contains('" + value + "')");
                    if (tr.length == 0) {
                        return bt.lang("sys.form.verify.mustBeInTable")//"输入的值不在下拉表格中"
                    }
                }

            },
            //完全匹配数据
            tableDataEquals: function (value, item) {
                var $table = $(item).siblings("div").find("table");
                // var tr = $table.find("tbody tr").filter(":contains('"+value+"')");
                var td = $table.find("tbody tr").find("[title='" + value + "']");
                if (td.length == 0) {
                    return bt.lang('sys.form.verify.mustBeInTable')//"输入的值不在下拉表格中"
                }
            },
        });
    }

    //行内编辑查看/删除/编辑回调
    Table.prototype.doRowCallback = function (data) {
        var that = this;
        var del = that.config.delRowCallback;
        var reviewRowCallback = that.config.reviewRowCallback;
        var edit = that.config.editRowCallback;
        var $table = $(that.config.elem);
        var scrollY = that.config.scrollY.toString();
        var scrollYnumber = 0;
        if (scrollY && scrollY.indexOf('px') > -1) {
            scrollYnumber = scrollY.split('px')[0];//获取scrollY数字部分
        }
        $(that.config.elem).off('click').on('click', 'button[data-type],a[data-type]', function (e) { //添加了解绑点击，解决表格多次渲染时的问题
            e.stopPropagation();
            var type = $(this).data("type");
            var tds = $(this).parents("tr").find("td");
            var currentTr = $(this).parent().parent("tr");
            var id = currentTr.data('id');
            var rowData = {};
            layui.each(tds, function (i, td) {
                var name = $(td).attr("name");
                var text = $(td).text();
                if (name != undefined) {
                    rowData[name] = text;
                }
            })

            if (type == "del") {
                if (del && typeof del === "function") {
                    // del(rowData,$(this).parents("tr"))
                    del(data, currentTr, id);
                    var tableHeight = $table.height();
                    var overflowY = $table.parent(".table-scroll").css('overflow-Y') == 'auto';
                    if (scrollYnumber > tableHeight && overflowY && that.addButton) {
                        $table.parent(".table-scroll").css({
                            "height": "auto",
                            "overflow-Y": "visible",
                        });
                        that.addButton = false;
                    }
                }
            }
            if (type == "review") {
                if (reviewRowCallback && typeof reviewRowCallback === "function") {
                    reviewRowCallback(data, currentTr, id)
                }

            }
            if (type == "edit") {
                if (edit && typeof edit === "function") {
                    edit(data, currentTr, id)
                }
            }
        })
    };
    Table.prototype.clickButtons = function () {
        var that = this;
        var buttons = that.config.buttons;
        var $table = $(that.config.elem);
        var select = that.select;
        if (buttons) {
            var html = '';
            var scrollY = that.config.scrollY.toString();
            var scrollYnumber = 0;
            if (scrollY && scrollY.indexOf('px') > -1) {
                scrollYnumber = scrollY.split('px')[0];//获取scrollY数字部分
            }
            layui.each(buttons, function (i, button) {
                var text = button.text;
                var type = '';
                if (text && (text.indexOf("del") > -1 || text.indexOf(bt.lang("common.button.delete")) > -1)) {
                    type = "del";
                    if (button.action && typeof button.action === "function") {
                        $table.parent().parent().on("click", 'button#del' + that.num + i, function (e) {
                            var trs = $table.find('tr.' + select + '');
                            button.action($(this), trs);
                            return false
                        })
                    }
                } else if (text && (text.indexOf("add") > -1 || text.indexOf(bt.lang('common.button.add')) > -1)) {
                    type = "add";
                    if (button.action && typeof button.action === "function") {
                        that.addButton = true;
                        // var addOverflow = false; //判定有无overflowY属性,false代表没有，true代表有
                        $table.parent().parent().on("click", 'button#add' + that.num + i, function (e) {
                            button.action($(this), that.firstRow.html);
                            var tableHeight = $table.height();
                            if (scrollYnumber < tableHeight && that.addButton && scrollYnumber) { //当scrolly高度小于表格高度，同时表格无overflowY属性，需要添加此属性
                                $table.parent(".table-scroll").css({
                                    "height": scrollY,
                                    "overflow-Y": "auto",
                                });
                                that.addButton = false;

                            }
                            return false;
                        })
                    }
                } else {
                    type = 'others';
                    if (button.action && typeof button.action === "function") {
                        $table.parent().parent().on("click", 'button#others' + that.num + i, function (e) {
                            button.action($(this), $table);
                            return false;
                        })
                    }
                }
                var isHide = button.hide ? 'layui-hide' : '';
                html += '<button type = "button" data-type="' + type + '" class="layui-btn layui-btn-small ' + isHide + '" id ="' + type + that.num + i + '">' + text + '</button>';

            })

            if ($table.parent(".table-scroll").siblings(".tableButtons").length == 0) {
                $table.parent(".table-scroll").before('<div class="layui-block tableButtons"></div>');
            }
            $table.parent(".table-scroll").siblings(".tableButtons").html(html);
        }

    }
    Table.prototype.scrollCss = function () {
        var that = this;
        var $table = $(that.config.elem);
        if ($table.parent(".table-scroll").length == 0) {
            $table.wrap('<div class="table-scroll ' + (that.config.SelectTable ? 'selecttablecss' : '') + '"  style="' + (that.config.SelectTable ? 'max-height:300px' : '') + '"></div');
        }
        $table.find("thead tr th").css({
            "min-Width": "30px",
        })

    }

    //设置输入框的删除按钮功能
    Table.prototype.deleteValue = function () {
        var that = this;
        var _config = that.config;
        var inputElem = $(_config.searchInput);
        //var width =inputElem.width();
        if (!inputElem.siblings('a').find('.bt-selectTableType-close')[0]) {
            var icon = '<a href="javascript:void(0);" class="delIcon"><i class="layui-icon bt-selectTableType-close" style="right: 4px">ဆ</i></a>';
            inputElem.after(icon);
        }
        inputElem.parent().find('.delIcon').off('click').on('click', function (e) {
            bt.stope(e);
            var $currentInput = $(this).siblings('input');
            $currentInput.val('');
            $currentInput.trigger('focus');
            $('.select-tablecss').hide();

            var tableScroll = $currentInput.siblings('.table-scroll');//下拉表格jq对象
            var top = $currentInput.offset().top + $currentInput.outerHeight() + 5 - $(window).scrollTop();
            var tableScrollHeight = tableScroll.outerHeight();
            if (top + tableScrollHeight > $(window).height() && top >= tableScrollHeight) {
                tableScroll.removeClass("select-table");
                tableScroll.addClass("select-table-up");
            } else {
                tableScroll.removeClass("select-table-up");
                tableScroll.addClass("select-table");
            }

            $currentInput.siblings('.select-tablecss').show();
        })
    }

    //表格添加overflowY的属性
    Table.prototype.addOverflow = function () {
        var that = this;
        var $table = $(that.config.elem);
        var scrollYItem = that.config.scrollY || that.config.scrolly;
        var scrollXItem = that.config.scrollX || that.config.scrollX;
        if (scrollYItem && typeof scrollYItem === "string" && scrollYItem.indexOf("px") > -1) {
            if (!that.addButton) {//如果表格无内置添加行按钮
                $table.parent(".table-scroll").css({
                    "height": scrollYItem,
                    "overflow-Y": "auto",
                })
            }
        }
        if (!that.addButton) {
            if (scrollXItem) {
                $table.parent(".table-scroll").css({
                    "overflow-x": "auto",
                    // "width":"200px",
                })
            }
        }
    }
    //重新加载回调方法
    Table.prototype.reload = function (ops) {
        var that = this;
        that.set(ops);
        that.init('reload');//添加参数，重新加载数据
    }

    //重置像mulselect方法
    Table.prototype.attr = function (obj) {
        var that = this;
        if (!obj) {
            return "";
        }
        var _config = that.config;
        for (var key in obj) {
            _config[key] = obj[key]
        }
    }

    Table.prototype.draw = function (ops) { //重绘方法
        var that = this;
        that.set(ops);
        var _config = that.config;
        var draw = 'draw';
        if (_config.url) {
            $.ajax({
                type: _config.type,
                url: _config.url,
                data: _config.urldata,
                dataType: 'json',
                success: function (result, status, xhr) {
                    var data;
                    if (result === null) {
                        data = [];
                    } else if (result.data !== undefined) {
                        data = result.data;
                    } else {
                        data = result;
                    }
                    that.appendHtmlCallback(data, draw);
                },
            });
        }
    }
    /*
    * 设置行的选中
    * checkedArray Array数组对象 {id:'xxxxx',checked:true}
    * */
    Table.prototype.setSelectedRow = function (checkedArray) {
        var that = this
        if(checkedArray && checkedArray.length){
            var currentRow
            for (var i = 0; i < checkedArray.length; i++) {
                 currentRow = $('tr[data-id="'+checkedArray[i].id+'"]')
                currentRow.find('input.checkchild').prop("checked", checkedArray[i].checked);
                if(checkedArray[i].checked){
                    currentRow.addClass(that.select)
                }else{
                    currentRow.removeClass(that.select)
                }
            }
            // 多选时
            if(that.config.mulselect){
                var checkAll = currentRow.parents("tbody").siblings().find("th input")
                checkAll.each(function (index, item) {
                    item.checked = currentRow.parents("tbody").find("input.checkchild").length == currentRow.parents("tbody").find("input.checkchild:checked").length ? true : false;
                });
            }
            // 只绘制表格内的复选框
            if(that.formRenderInTable){
                form.render("checkbox",'bttable_filter'+that.num);
            }else {
                form.render("checkbox");
            }
            that.updateDataSelect();
        }
    }
    // 获取选中的据
    Table.prototype.getSelectedData = function () {
        return this.updateDataSelect();
    }

    Table.prototype.getDate = function () {
        var that = this;
        return  that.tableData;
    }

    function throwError(msg) {
        throw new Error(msg);
    }


    var obj = {
        init: function (ops) {
            var tbl = new Table();
            tbl.set(ops);
            tbl.init();
            return tbl;
        }
    }

    exports('bttable', obj);
}).addcss('../../../css/bttable.css', 'bttablecss');
