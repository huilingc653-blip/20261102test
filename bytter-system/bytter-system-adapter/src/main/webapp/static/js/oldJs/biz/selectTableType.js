/**
 * 组件：下拉表格业务组件
 * 刘勇军
 * 2017年8月24日
 * 0.0.1 首次发布
 * 0.0.2 tab时，表格隐藏
 * 0.0.3 修复多个重置时，出现的显示  隐藏的问题
 * 0.0.4 修改选渲染表头再渲染表体
 * 0.0.5 解决表格套表格隐藏的问题
 * 0.0.6 解决多个表格点击翻页选中的问题
 * 0.0.7 统一下拉表格的分页内容
 * 0.0.8 添加下拉表格左右定位
 * 0.0.9 添加清除按钮
 * 0.1.0 新增加 data-clear参数，默认为添加清除按钮，设置data-clear ="false"，代表不需要添加
 * 0.1.1 新增 valueFromTable 参数用于设置下拉表格是否可以填写表格外的值，默认不支持；下拉表格值的输入有三种方法，点击、backspace删除、回车
 * 0.1.2 addDelIcon 方法添加回调执行
 * 0.1.3 更改分页组件，更改上下固定位置代码
 * 0.1.3 新增加selectTableType.css文件
 * 0.1.4 新增加rebuild方法，用于表格参数重置；重新setUrl 方法，解决表格数据加载慢时，出现无表头的问题
 * 0.1.5 新增val方用，用于获取或设置下框值的方法
 * 0.1.6 增加数据加载样式，删除信息栏中多少页信息
 * 0.1.7 新增国际化
 */

bt.define(['jquery', 'form', 'datatablecore'], function (exports) {
    var $ = bt.jquery;
    var form = bt.form();

    var SelectTableType = function () {
        this.config = {
            elem: "",
            url: '',
            // urldata: '',
            type: "POST",
            flag: false,//设置是否改变url信息
            async: true,
            scrollX: '600px',//由500px 改成600px
            scrollY: '',
            paging: true,
            valueFromTable:true, //新增加数据只能从表格中获取,或者在input中添加 data-value ="table" 也可以设置此功能
            urldata:{
                date:new Date().getTime()
            }
            // handleDeleteCallback:function () { //点击清除按钮的回调方法
            //
            // }
        };
        this.bizName = 'selectTable';
        this.currentObject = null;
        this.randomNumber = 0;
    }

    var lang = {
        sProcessing: "<div class='loader'>"+bt.lang('sys.notification.loading')+"</div>",//加载中...
        // sProcessing: "<div class='loader'>bt.lang('common.table.loading')</div>",//加载中...
        //sProcessing: "<div class='loader'><i class='layui-icon layui-anim layui-anim-rotate layui-anim-loop' style='font-size: 40px'></i></div>",//
        sLengthMenu:bt.lang('common.table.lengthMenu') ,//"每页 _MENU_ 项",
        // sZeroRecords: "换个搜索词试试呢？暂无数据",
        sZeroRecords:bt.lang('common.msg.temporarily_no_data') ,//"暂无数据",
        sInfo:bt.lang('common.table.info') ,//"当前显示第 _START_ 至 _END_ 项，全部 _TOTAL_ 项。",
        sInfoEmpty:bt.lang('common.table.infoEmpty') ,//"当前显示第 0 至 0 项，全部 0 项",
        sInfoFiltered:bt.lang('common.table.infoFiltered') ,//"(由 _MAX_ 项结果过滤)",
        sInfoPostFix: "",
        sSearch: "本地搜索：",
        sUrl: "",
        sEmptyTable: '<img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="table-pic-bank"><div class="empty-text">'+bt.lang('common.msg.temporarily_no_data')+'</div>',//"暂无数据",
        sLoadingRecords:bt.lang('common.table.loading'),//"载入中...",
        sInfoThousands: ",",
        oPaginate: {
            sFirst:bt.lang('common.table.firstPage'),//"首页",
            //sPrevious:bt.lang('common.table.previousPage') ,//"上一页",
            sPrevious:'< ' ,//"上一页",
            //sNext:bt.lang('common.table.nextPage') ,//"下一页",
            sNext:'> ' ,//"下一页",
            sLast: bt.lang('common.table.lastPage'),//"最后页",
            sJump:bt.lang('common.table.jump'),//"跳转至"
        },
        oAria: {
            sSortAscending:bt.lang('common.table.sortAscending'),//":以升序排列此列",
            sSortDescending:bt.lang('common.table.sortDescending') //": 以降序排列此列"
        }
    };

    SelectTableType.fn = SelectTableType.prototype;

    SelectTableType.fn.v = '0.0.8';

    /**
     * 方法入口，初始化
     */
    SelectTableType.fn.init = function (ops) {
        var that = this;
        var _config = that.config;
        $.extend(true, _config, ops);

        if (!_config.columns) {
            throwError('selectTableType error:找不到columns参数，请检查');
        }
        that.render()
    };

    SelectTableType.fn.render = function (obj,agr2) {
        var that = this;
        var elem = that.config.elem;
        var $elem = obj || $('' + elem + ':not(.init)');
        that.randomNum();
        if ($elem.length !== 0) {
            for (var i = 0; i < $elem.length; i++) {
                var elemObj = $($elem[i]);
                //给父元素添加定位；
                if (elemObj.parent('div').length === 0) {
                    elemObj.wrap('<div class ="layui-inline" > </div>');
                } else {
                    elemObj.parent('div').addClass('pl');
                }
                var inputValue = $.trim( elemObj.val() );
                // 新增下拉表格输入框的数据来源与表格参数
                if( !agr2 ){ //非reload 时
                    elemObj.data('defaultvalue',inputValue )
                }
                elemObj.data('beforevalue',inputValue );

                if( that.config.valueFromTable ){
                    if( inputValue !== '' ){
                        elemObj.data('value',inputValue);
                    }else{
                        elemObj.data('value','table');
                    }
                }

                //判断有说明是重置:无代码是新表格 同时不是原selectTable组件 that.config.SelectTable;
                if (!elemObj.siblings('.select-tablecss').find('table tbody')[0] && !that.config.SelectTable) {
                    var html = that.tableHeader();
                    elemObj.after(html).parent().addClass('pl');
                } else if (that.config.SelectTable) { //原selectTable组件
                    var borderBottom = that.config.paging ? '':'border-bottom:1px solid #d2d2d2;';
                    elemObj.after('<div class ="select-tablecss bt-biz-selectTableType" style="width:' + that.config.scrollX + ';'+ borderBottom +'"></div>');
                    that.config.tableElem.css('width', '100%').removeClass('hide');
                    // if(store.get('web-skin') != 'light-skin'){
                    //     that.config.tableElem.addClass('table-dark');
                    // }
                    that.config.tableElem.find('thead').after('<tbody></tbody>');
                    elemObj.siblings('.select-tablecss').append(that.config.tableElem);
                }
                //注册点击事件，让下拉表格显示
                if (!obj) {
                    //$('body').on('click', that.config.elem, function (event) {
                    $('body').find(that.config.elem).off('click').on('click', function (event) {
                        bt.stope(event);
                        var selectTableWrap = $(this).siblings('.select-tablecss');
                        if (selectTableWrap.find('table tbody tr').length === 0) {
                            selectTableWrap.find('table').addClass('layui-table');
                            var colspan = selectTableWrap.find('table thead th').length
                            selectTableWrap.find('table tbody').html('<tr><td colspan="'+colspan+'" align="center">'+bt.lang('sys.notification.loading')+'</td></tr> ');
                        }
                        if( $.trim($(this).val()).length && !$(this).data('beforevalue') && selectTableWrap.is(':hidden') && $(this).data('value')!== undefined){ //检验是否通过直接赋值的方式，如果是把值保存在beforevalue中
                            $(this).data('beforevalue',$.trim($(this).val()));
                        }
                        $(".select-tablecss").hide();
                        //设置下拉框显示位置
                        displayPosition($(this));
                        selectTableWrap.find('.selectedCss').removeClass('selectedCss');
                        selectTableWrap.show();
                        $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
                    });
                }
            }
            that.getData($elem);
        }
    };
    //设置下拉框显示位置
    function displayPosition(elem) {
        var win = $(window);
        var tableElem = elem.siblings('.select-tablecss');
        //左右判断方法
        var tableWidth = tableElem.outerWidth();
        var windowWidth = win.width();
        var paddingRight = parseFloat( $('body').css('padding-right') );
        var right = windowWidth - elem.offset().left - paddingRight;
        var left = 0;
        if( right < tableWidth ){
            left = - ( tableWidth - right );
            tableElem.css('left',left);
        }
        //上下位置判断方法
        var top = elem.offset().top + elem.outerHeight() + 5 - win.scrollTop()
            ,tableHeight = tableElem.outerHeight();
        if(top + tableHeight + 55> win.height() && top >= tableHeight){
            var x = tableHeight + 2;
            tableElem.css('top',-x)
        }else{
            tableElem.css('top',elem.outerHeight() + 10 + 'px')
        }
    }
    //生成表头
    SelectTableType.fn.tableHeader = function () {
        var that = this;
        var columns = that.config.columns ? that.config.columns : [];
        var headerHtml = '';
        var tableSkinClass = ''
        // 获取产品的肤色
        // if(store.get('web-skin') != 'light-skin'){
        //     tableSkinClass = 'table-dark'
        // }
        headerHtml += '<div class ="select-tablecss bt-biz-selectTableType" style="width:' + that.config.scrollX + '">';
        headerHtml += '<table  width = 100% id="table' + that.randomNumber + '" class="'+tableSkinClass+'">';
        headerHtml += '<thead><tr>';
        for (var i = 0; i < columns.length; i++) {
            var column = columns[i],
                width = column.width,
                name = column.data;
            //表头生成
            if (column.title !== undefined) {
                headerHtml += '<th style = "min-width: 30px;"';
                if (width !== undefined) {
                    if (typeOf(width) === 'string' && width.indexOf('px') !== -1) {
                        width = width.replace('px', '');
                    }
                    headerHtml += 'width = ' + width + '';
                } else {
                    width = 150;
                }
                headerHtml += 'data-data ="' + name + '"';
                headerHtml += '>';
                headerHtml += column.title;
                headerHtml += '</th>';
            }
        }
        headerHtml += '</thead></tr>';
        headerHtml += '<tbody></tbody>';
        headerHtml += '</table>';
        headerHtml += '</div>';
        return headerHtml;
    };

    //getData获取表格数据
    SelectTableType.fn.getData = function (elem) {
        var that = this, _config = that.config;
        if (_config.url) {
            $.ajax({
                type: _config.type,
                url: _config.url,
                data: _config.urldata,
                dataType: 'json',
                async: _config.async,
                success: function (result, status, xhr) {
                    var data;
                    if (result === null || !result) {
                        data = [0];
                    }else if (result.data !== undefined) {
                        data = result.data;
                    }else {
                        data = result;
                    }
                    for (var i = 0; i < elem.length; i++) {
                        that.scrollHtml(data, $(elem[i]));
                    }
                },
            });
        }
    }

    //完成页面组装
    SelectTableType.fn.scrollHtml = function (data, elem) {
        var that = this;
        var othis = elem;
        var bizName = othis.data('biz-name');
        //下拉表格输入框是否有清除按钮
        var isClearBtn = othis.data('clear');

        var $table;
        if (!that.config.SelectTable && !that.config.flag) {
            $table = $('#table' + that.randomNumber);
            that.config.tableElem = $table;
        } else {//旧的selectTable组件
            $table = that.config.tableElem;
            // if(store.get('web-skin') != 'light-skin'){
            //     $table.addClass('table-dark');
            // }
        }
        //_config.dom = 'Brtli<"jump_page">p';
        //var tableDom = that.config.paging ? 'rt<"wrapper">':'rt';//设置下拉表格的有无分页
        var tableDom = that.config.paging ? 'rtl<"jump_page">p':'rt';//设置下拉表格的有无分页
        //下拉表格参数
        var config = {
            language: lang,
            // dom: 'rtlp',
            dom: tableDom,
            ordering: false,
            serverSide: false,
            scrollX: that.config.scrollX,
            scrollY: that.config.scrollY,
            paging: that.config.paging,
            data: data,
            columns: that.config.columns,
            // pageLength:10,
            initComplete: function (settings, json) {
                elem.siblings('.select-tablecss').find('tr.selectedCss').removeClass('selectedCss');
                var table = new $.fn.dataTable.Api( settings );
                that.listenEvent(othis, data,table);
                //删除上一个版本的分页
                // $('.select-tablecss .dataTables_scroll').append($('.select-tablecss .wrapper'))
                //if(!$table.parents('.dataTables_scroll').find('.wrapper').length){
                //    if(that.config.paging){
                //        that.setTableInformation(settings,$table);//设置表格底部翻页功能
                //    }
                //    var wrapper = $table.parents('.select-tablecss').find('.wrapper');
                //    $table.parents('.dataTables_scroll').append(wrapper);
                //}
                //var html = '<span class="table_input_text">跳至</span><input type="text" class="table_input_page "/>&nbsp;&nbsp页';
                //$table.parents('.select-tablecss').find('.jump_page').html(html);
                that.jumpPage($table.parents('.select-tablecss'),settings)
                $('.dataTables_length').find('select').attr('lay-ignore','')

            },
            drawCallback: function (settings, json) {
                var wrapperTable = elem.siblings('.select-tablecss');
                wrapperTable.find('tr.selectedCss').removeClass('selectedCss');
                that.clickRow(data,othis);
                //删除上一个版本的分页
                //updatePages($table,settings);
                //解决无内容时，分页div border-top 两条线的问题
                if(!wrapperTable.find('table td.dataTables_empty').length){
                    wrapperTable.find('.wrapper').css('border-top','1px solid #d2d2d2');
                }else{
                    wrapperTable.find('.wrapper').css('border-top','none');
                }
            }
        }

        $(document).ready(function () {
            //清空原来表格内容
            $table.find('tbody').html('');
            $table.removeClass('layui-table');
            if ( !that.config.header ){
                that.config.header = $table.find('thead').html()
            }
            var table = $table.DataTable(config)
            othis.addClass('init');
            if(isClearBtn !== false ){ //下拉表格输入框是否有清除按钮 默认是添加
                that.addDelIcon(othis,data);
            }
            //如果有bizname 把url存起来
            if (bizName !== undefined) {
                elem.data('url', that.config.url);
                that.config.elem = othis;
                that.config.flag = false;
                that.config.table = table;//存贮datatable实例
                if (!window.biz) {
                    window.biz = [];
                }
                if (!window.biz[that.bizName]) {
                    window.biz[that.bizName] = [];
                }
                window.biz[that.bizName][bizName] = that.config;
            }
        })

    };


    //所有监听事件的调用
    SelectTableType.fn.listenEvent = function (elem, data, table) {
        var that = this;
        //下拉表格初始完回调
        that.initCompleteCallback(data, elem);
        //注册点击事件,点击回调方法
        that.clickRow(data, elem,table);
        //注册智能搜索功能，并添加了键盘上下选择功能
        that.search(data, elem,table);
        //注册提交验证功能，验证值是否为表格数据
        that.verifySelectTable(data, elem);
        //点击body时，关闭下拉表格
        // that.closeTable(elem);
    };

    //注册点击事件 点击回调
    SelectTableType.fn.clickRow = function (data, elem,table) {
        var that = this;
        elem.siblings('.select-tablecss').find('tbody tr ').off('click').on('click', function () {
            var tableElem = $(this).parents('.select-tablecss').find('.dataTables_scrollBody table');
            var id = tableElem.attr('id');
            var table = new $.fn.dataTable.Api( '#'+id );
            var currentData = table.row(this).data();
            elem.siblings('.select-tablecss').hide();
            if(currentData === undefined){ //表示无数据
                return
            }
            var clickCallback = that.config.clickCallback;
            var v = $.trim( elem.val() );
            if (clickCallback !== undefined && typeof clickCallback === 'function') {
                clickCallback(currentData, $(this), elem, data);
                if( elem.data('value') !== undefined ){
                    elem.data('value',elem.val()).data('beforevalue',elem.val());
                }
            }
        })
    };

    //下拉表格初始完成回调
    SelectTableType.fn.initCompleteCallback = function (data, elem) {
        var tableInitCallback = this.config.tableInitCallback;
        if (tableInitCallback !== undefined && typeof tableInitCallback === 'function') {
            tableInitCallback(data, elem)
        }
    };

    //下拉表格的搜索功能;
    SelectTableType.fn.search = function (data, elem,table) {
        var that = this;
        var tableWrap = $(elem).siblings('.select-tablecss');
        //$('body').on('click', function (event) {
        $(document).on('click','body', function (event) {
            bt.each(tableWrap,function(){
                if( !$(this).is(":hidden")){ //判断元素是否可见
                    /**
                     * data-value 有值时，代表输入的数据必须是表格中的数据,即输入时，必须点击表格行数才有效，点击body位置无效
                     * */
                    var currentInput = $(this).siblings('input:visible');// 解决有隐藏输入框的问题
                    if ( currentInput.data('value') !== undefined ){
                        if ( currentInput.data('beforevalue') !== '' ) {
                            currentInput.val( currentInput.data('beforevalue') );//还原原来的值
                        }else { //无上一次值时，直接清空
                            currentInput.val('');
                        }
                    }else{
                        currentInput.data('beforevalue',$.trim(currentInput.val()));
                    }
                }
            })
            tableWrap.hide();
        });

        $(elem).off('keyup focus').on('keyup focus', function (event) {
            if (event.keyCode !== 38 && event.keyCode !== 40) {
                table.search(this.value).draw();
                //if( $(this).siblings('.select-tablecss').is(":hidden") && event.keyCode === 8 ){
                $(this).trigger('click');
                //}
                if( event.keyCode === 8 && $(this).val() === ''){ //当使用backspace键清空内容
                    if( elem.data('value') !== undefined ){
                        elem.data('beforevalue','');//清空值
                    }
                }
                //$(this).siblings('.select-tablecss').show();
            }
            var pageChild = $(this).siblings('.select-tablecss').find('.wrapper .table-page');
            //updateRecord(table,pageChild);//更新表格下方显示起止信息条数
            //updatePageStyle(pageChild,0,table);
        });

        $(elem).siblings('.select-tablecss').on('click', function (event) {
            layui.stope(event)
        });

        var index = -1;
        //设置下拉表格支持键盘上下选择功能
        that.selectTableKeydown(index, table, elem, data);
    };

    //下拉表格的键盘事件；
    SelectTableType.fn.selectTableKeydown = function (index, table, elem, data) {
        var that = this;
        var $table = elem.siblings('.select-tablecss').find('table');
        var pageArr = [];//存贮当前页码
        elem.off('keydown').on('keydown', function (event) {
            var trs = $table.find("tbody tr:visible");
            var container = $table.parent('.dataTables_scrollBody');
            if (container.length > 0) {
                var flag = container[0].scrollHeight > container[0].clientHeight;
                var trHeight = $table.find('tbody tr').height();//行高
                // 检验到第几行时才滚动
                var stage = Math.floor(container[0].clientHeight / (trHeight) - 1);
                var pageIndex = table.page();//获取当前页码
                if (pageArr.length == 0) { //当为空时，存贮；
                    pageArr.push(pageIndex);//
                } else {
                    if (pageArr[0] !== pageIndex) { //不相等时，表示翻页了
                        pageArr[0] = pageIndex;//重置当前页码
                        index = -1;//重置页码索引
                    }
                }

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
                    that.keyboardSelect(table, index, elem, data, trs);
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
                        var top = (index - stage) * (trHeight);
                        that.scrolly(container, $(trs[index]), flag, top)
                    }
                    that.keyboardSelect(table, index, elem, data, trs);
                    $(trs[index - 1]).removeClass("selectedCss");
                }
                //回车
                if (event.keyCode == 13) {//回车
                    if (index >= 0) {
                        that.keyboardSelect(table, index, elem, data, trs);
                        if( elem.data('value') !== undefined ){
                            elem.data('value',elem.val()).data('beforevalue',elem.val());//设置值
                        }
                        $(trs[index]).removeClass("selectedCss");
                    }else{
                        if( elem.data('value') !== undefined ){
                            elem.data('value','').data('beforevalue','');//清空值
                            elem.val('');
                        }
                    }
                    index = -1;
                    $table.parents('.select-tablecss').hide();
                    $(this).blur();
                    event.preventDefault ? event.preventDefault() : (event.returnValue = false);

                    that.enterToNextInput($(this))
                }
                //tab时，表格隐藏
                if (event.keyCode == 9) {
                    elem.siblings('div').hide()
                }
            }

        })
    };

    /**
     * 回车切换至下一个input text focus
     * */
    SelectTableType.fn.enterToNextInput = function(currentElem){

        if(!currentElem.parents('.layui-form').hasClass('enter-switch')){ //当form表单中没有hasSwitch样式，代表有切换功能
            return '';
        }

        var currentRowNextInputElem = function(td,next){
            var nextTd = td?td.next('td'):next;
            if(nextTd[0]){ //1、当前行有，那就寻找td下面的input；
                var inputElem = nextTd.find('input[type="text"]');
                if(inputElem[0] && inputElem.is(':visible')){
                    inputElem.trigger('focus');
                    return ''
                }else{ //如果没有可见输入框，继续查询下一个
                    currentRowNextInputElem(nextTd)
                }
            }else{ //如果没有说明是当前行最后一个td
                if(td.parent('tr').next('tr')[0]){ //2、当前行没有，那就寻找下一行；
                    var nextRowFirstTd =  td.parent('tr').next('tr').find('td:eq(0)');
                    currentRowNextInputElem('',nextRowFirstTd)
                }else{
                    return ''
                }
            }
        }

        var currentTd = currentElem.parents('td');
        currentRowNextInputElem(currentTd);
    }

    //键盘上下键选中并赋值
    SelectTableType.fn.keyboardSelect = function (table, index, elem, data, trs) {
        var that = this;
        if (that.config.clickCallback && typeof that.config.clickCallback === "function") {
            var othis = elem;
            var currentData = table.row(trs[index]).data();
            var tds = $(trs[index]).find('td');
            that.config.clickCallback(currentData, othis, elem, data);
        }
    };
    //手动触发滚动条滚动
    SelectTableType.fn.scrolly = function (container, tolocation, flag, top) {
        var step = 0;
        if (flag) {
            container.scrollTop(top)
        }
    };

    //验证输入框的值是否是下拉表格的数据
    SelectTableType.fn.verifySelectTable = function (data, elem) {
        var that = this;
        form.verify({
            tableData: function (value, item) {
                var value = $.trim(value);
                if(value == ''){
                    return;
                }
                var tableElem = $(item).siblings('.select-tablecss').find('.dataTables_scrollBody table');
                var id = tableElem.attr('id');
                var table = new $.fn.dataTable.Api( '#'+id );
                var columns = table.columns().header();
                columns = columns.length;
                var flag = false;
                for (var i = 0; i < columns; i++) {
                    var index = table.column(i).data().indexOf(value);
                    if (index !== -1) {
                        flag = true;
                        break
                    }
                }
                if (!flag) {
                    return bt.lang('sys.form.verify.mustBeInTable') //"输入的值不在下拉表格中"
                }
            }
        });
    };

    // 添加删除按钮事件
    SelectTableType.prototype.addDelIcon = function(inputElem,data){
        var that = this;
        var _config = that.config;
        //var width =inputElem.width();
        if( !inputElem.siblings('span').find('.bt-selectTableType-close')[0] && inputElem.data('close') != false){
            //var icon = '<a href="javascript:void(0);" class="delIcon"><i class="layui-icon bt-selectTableType-close" style="right: 4px">ဆ</i></a>';
            var icon = '<span href="javascript:void(0);" class="delIcon"><i class="layui-icon bt-selectTableType-close" style="right: 4px">ဆ</i></span>';
            inputElem.after(icon);
        }
        inputElem.parent().find('.delIcon').off('click').on('click',function(e){
            bt.stope(e);
            var $currentInput = $(this).siblings('.init');
            var disabled = $currentInput.attr('disabled');
            if (disabled){
                return
            }
            $currentInput.val('');
            $currentInput.data('value','').data('beforevalue','');

            //清除数据回调试
            if( _config.handleDeleteCallback && typeof _config.handleDeleteCallback ==="function"){
                _config.handleDeleteCallback()
            }

            $currentInput.trigger('focus');
            $('.select-tablecss').hide();
            $currentInput.siblings('.select-tablecss').show();
            displayPosition($(this).siblings('input'));
            $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
        })
    };

    SelectTableType.prototype.randomNum = function () {
        var num = Math.random() * 1000000;
        num = parseInt(num, 10);
        this.randomNumber = num;
        return num;
    }


    SelectTableType.fn.setUrl = function (ops) {
        // var that = this;
        // var currentObject = this.currentObject;
        // if (currentObject === undefined) {
        //     return '';
        // }
        // if (ops !== undefined) {
        // that.config.elem = currentObject;
        // that.config = currentObject;
        // var elem = currentObject.elem;

        // if (typeof ops === 'string' && ops.indexOf('/') !== -1) {
        //     that.config.url = ops;
        // }
        // else if (typeof ops === 'string' && ops.indexOf('/') == -1) {
        //     that.config.urldata = ops;
        // }
        // else if (typeof ops === 'object') {
        // that.config.url = currentObject.data('url');
        // that.config.urldata = ops;
        // that.config.urldata.date = new Date().getTime();
        // }
        // that.config.flag = true;
        // //elem.removeClass('init').val('');//新增清空input框内容
        // elem.removeClass('init');//新增清空input框内容
        // that.config.table.destroy();//销毁datatable实例
        // elem.siblings('.select-tablecss').find('table tbody').html('');
        // that.config.SelectTable = false;
        // that.render(elem,'reload');
        // }
        //　当表格数据加载慢时，上面代码会出现无表头的问题；
        //　改用下面代码，调用新的接口

        if (ops !== undefined) {
            var param = {};
            if (typeof ops === 'string' && ops.indexOf('/') !== -1) {
                param.url = ops;
            }
            else if (typeof ops === 'string' && ops.indexOf('/') == -1) {
                param.urldata = ops;
            }
            else if (typeof ops === 'object') {
                param.urldata = ops;
                param.urldata.date = new Date().getTime();
            }
            if ( param ){
                this.rebuild(param)
            }
        }
    };
    /**
     *  重新绘制表格
     *  @param config 对象参数重置方法
     *
     * */
    SelectTableType.fn.rebuild = function (config) {
        var that = this;
        if ( !config ){
            return;
        }
        var currentObject = that.currentObject;
        if (currentObject === undefined) {
            return '';
        }
        if ( config !== undefined ) {
            that.config = currentObject;
            that.config.table.destroy();//销毁datatable实例
            var columns = config.columns;
            if ( columns && columns.length ){
                bt.each(columns,function (i,item) {
                    if ( item.visible !== undefined ){
                        item.bVisible = item.visible;
                    }
                })
                config.columns = columns;
            }

            $.extend(true, that.config, config);

            var elem = currentObject.elem;

            that.config.flag = true;
            that.randomNum();
            elem.siblings('.select-tablecss').html('<table  width = 100% id="table' + that.randomNumber + '" class="layui-table"><thead></thead><tbody></tbody></table>');

            that.config.tableElem = $('#table' + that.randomNumber)
            var currentTable = elem.siblings('.select-tablecss').find('table');
            if ( that.config.header && that.config.header.length ){
                currentTable.find('thead').html( that.config.header )
            }
            currentTable.find('tbody').html('<tr class="table_loading"><td colspan="'+currentTable.find('thead th').length+'" class="center">'+bt.lang('sys.notification.loading')+'</td></tr>');//数据正在加载中.....

            that.config.SelectTable = false;
            that.updateTable(elem,'update');
        }
    };
    //更新表格信息
    SelectTableType.fn.updateTable = function (elemObj,agr2) {

        if (elemObj.length !== 0) {
            //给父元素添加定位；
            if (elemObj.parent('div').length === 0) {
                elemObj.wrap('<div class ="layui-inline" > </div>');
            } else {
                elemObj.parent('div').addClass('pl');
            }
            var inputValue = $.trim( elemObj.val() );
            // 新增下拉表格输入框的数据来源与表格参数
            if( !agr2 ){ //非reload 时
                elemObj.data('defaultvalue',inputValue )
            }
            elemObj.data('beforevalue',inputValue );

            if( this.config.valueFromTable ){
                if( inputValue !== '' ){
                    elemObj.data('value',inputValue);
                }else{
                    elemObj.data('value','table');
                }
            }
            this.getData(elemObj);
        }
    };

    SelectTableType.prototype.jumpPage = function($table,setting){
        var that = this;
        var toolDiv = $table.find('.jump_page');
        var html = '<span class="table_input_text">'+bt.lang('table.datatable.pageTo')+'</span><input type="text" class="table_input_page "/>&nbsp;&nbsp'+bt.lang('common.table.page');
        toolDiv.html(html);
        keyupPageNumber(toolDiv);
        enterJumpPage(new $.fn.dataTable.Api( setting ),toolDiv);
    }

    //控制只能输入正整数
    function keyupPageNumber(toolsDiv){
        //$('.dataTables_wrapper .wrapper input').on('keyup',function(){
        toolsDiv.find('input').on('keyup',function(){
            if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}
        }).on('afterpaste',function(){
            if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')};
        })

    }
    //按回车键页面跳转
    function enterJumpPage(table,toolsDiv){
        toolsDiv.off('keydown','.table_input_page');
        toolsDiv.on('keydown','.table_input_page',function(e){
            e = event || windw.event;
            if(e.keyCode ===13){
                var val = parseInt($(this).val());
                if(val>=0){
                    if(val > table.page.info().pages){//大于最大页码时，清空页码
                        $(this).val('')
                    }else{
                        val--
                        table.page(val).draw('page');
                    }
                }
                $(this).blur();
            }
        })
    }

    //兼容之前selecTable reload方法
    SelectTableType.fn.reload = function (ops) {
        var that = this;
        that.setUrl(ops);
    }

    /**
     * 获取或者设置下拉框输入框值的方法
     * @param value string
     *      未传入时　代表获取值
     *      传入值时　代表设置值
     * */
    SelectTableType.fn.val = function (value) {

        if (this.currentObject === undefined) {
            return '';
        }

        if (this.currentObject.elem.length){
            if ( value !== undefined ){
                this.currentObject.elem.val(value).data('beforevalue',value);
            } else {
                return this.currentObject.elem.val();
            }
        } else {
            throwError('selectTableType error:找不到下拉框，请检查');
        }

    }

    function throwError(msg) {
        throw new Error("[bt-biz-selectTableType:" + msg);
    };

    var selectTableType;
    var obj = {
        init: function (ops) {
            selectTableType = new SelectTableType();
            selectTableType.init(ops);
            return this;
        },
        render: function () {
            selectTableType = new SelectTableType();
            selectTableType.render();
        },
        get: function (bizName) {
            selectTableType = new SelectTableType();
            var currentObj = window.biz[selectTableType.bizName][bizName];
            if (!currentObj) {
                throw new Error("[bt-biz-selectTableType:元素容器找不到，具体设置格式请参考文档API");
            }
            selectTableType.currentObject = currentObj;
            return selectTableType;
        }
    }

    exports('selectTableType', obj)
})
    .addcss('../../../css/oldCss/selectTable.css','selectTableCss');
