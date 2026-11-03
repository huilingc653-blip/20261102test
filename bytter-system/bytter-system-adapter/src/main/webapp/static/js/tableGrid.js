/**
 * jqGrid表格
 *
 * 周亮
 * 2017年8月10日
 * 0.0.1 表格实现
 * 0.0.2 包装完成后的方法
 * 0.0.3 新增回车查询功能
 * 0.0.3 解决所有行被选中时，全选框不会选中的问题；解决某页被全选中，切换页码时还会被选中
 */
layui.define(['form','jquery', 'gridExtend'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form();
    var layer = layui.layer;
    var tableGrid = function () {
        this.config = {
            elem: undefined
        }
    };

    tableGrid.prototype.v = '0.0.1';

    tableGrid.prototype.hasFrozen = function(options){
        var flag = false;
        if(options.colModel){
            for(var i=0;i<options.colModel.length;i++){
                var model = options.colModel[i];
                if(model.frozen){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    };

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    tableGrid.prototype.init = function (options) {
        if (options.elem === undefined) {
            throwError('tableGrid Error:请配置参数elem,此参数为jQuery选择器!');
        }
        var elemName = options.elem.substring(1,options.elem.length);
        var jqGridPager = elemName + "_jqGridPager";
        var jqGridPagerSelect = "#"+jqGridPager;

        var that = this;

        var tableGridOption = {
            url: options.url,
            datatype: "json",
            mtype: "post",
            ajaxGridOptions: {
                contentType: 'application/json',
            },
            iconSet: "fontAwesome",
            serializeGridData: function (data) {
                return JSON.stringify(data);
            },
            recordtext:bt.lang('common.button.view')+' {0} - {1} '+bt.lang('common.table.records')+','+bt.lang('common.table.total')+' {2}'+bt.lang('common.table.item'),//"查看 {0} - {1} 条记录，共 {2} 条",
            emptyrecords:bt.lang('common.table.noData') ,//"暂时没有数据",
            loadtext:bt.lang('sys.notification.loading'),//"数据加载中...",
            pgtext: bt.lang('table.fbsBudgetYear.the')+" {0} of {1}",//"第 {0} of {1}",
            rowNum:15,
            rowList: [15, 30, 60, 100, 200, 500],
            //是否显示行号
            rownumbers: true,
            //行号宽度
            rownumWidth:40,
            height:470,
            //表格自适应父级容器的宽度
            autowidth: true,
            sortable: true,
            //是否显示多选列
            multiselect: true,
            multiPageSelection: true,
            //多选列的宽度
            multiselectWidth: 35,
            viewrecords: true,
            //默认启用斑马纹
            altRows: true,
            //右侧滚动条默认宽度
            scrollOffset: 10,
            shrinkToFit:!that.hasFrozen(options),
            beforeRequest:function(){
                //清除头部分页和页码
                $(options.elem+"_toppager_center").html('');
                $(options.elem+"_toppager_right").html('');
                if($.isFunction(options.beforeRequest)){
                    options.beforeRequest();
                }
            },
            gridComplete: function() {
                $('.ui-jqgrid-bdiv').scrollTop(0);
                if($.isFunction(options.gridComplete)){
                    options.gridComplete();
                }
            },
            loadComplete:function(data){
                //清除底部按钮
                $(jqGridPagerSelect+"_left").html('');
                //默认主键为ID，当查找到字段中有key作为主键时，采用数组中第一次出现的字段的名字作为主键名
                var pk;
                for(var i=0;i<options.colModel.length;i++){
                    var temp = options.colModel[i];
                    if(temp.key){
                        pk = temp.name;
                        break;
                    }
                }
                //设置真实数据
                tableGridDom.setOriginalData(data, pk);
                //若传入加载完成方法，则调用
                if($.isFunction(options.loadComplete)){
                    options.loadComplete(data);
                }
                var width = $(".tableGridWrapper").width();
                tableGridDom.setGridWidth(width);
                $(options.elem).jqGrid('resetSelection');//解决第n页被选中，切换页码时，会出现原有页码被选中的问题

            },
            onSelectRow:function(rowId,status){ //解决所有行点击被选中时，全选框不被选中
                var $jq = $(options.elem),id = $jq.attr('id')
                if ($jq.jqGrid('getDataIDs').length == $jq.jqGrid('getGridParam', 'selarrrow').length){
                    $('#gview_'+id).find('th .cbox').prop("checked",true)
                }
            },
            //onPaging:function(){
            //   var forzenDivHeight =  $(".frozen-bdiv.ui-jqgrid-bdiv").height();
            //    var tableHeight = $(".ui-jqgrid-view > .ui-jqgrid-bdiv > div").height();
            //    if( tableHeight < forzenDivHeight){
            //        $(".frozen-bdiv.ui-jqgrid-bdiv").addClass('test');
            //    }else{
            //        $(".frozen-bdiv.ui-jqgrid-bdiv").removeClass('test');
            //    }
            //
            //}
        };


        tableGridOption.pager = jqGridPagerSelect;
        tableGridOption.toppager = true;

        $.extend(tableGridOption, options);

        var tableGridDom = $(options.elem);
        if(!tableGridDom.parent().hasClass("tableGridWrapper")){
            tableGridDom.wrap('<div class="tableGridWrapper">');
        }
        tableGridDom.after('<div id="'+jqGridPager+'"></div>');
        tableGridDom.jqGrid(tableGridOption)
            .jqGrid("setGridWidth")
            .jqGrid("setFrozenColumns")
            .navGrid(jqGridPagerSelect,{
                edit: $.isFunction(options.editFunc),
                add: $.isFunction(options.addFunc),
                del: $.isFunction(options.delFunc),
                search: false,
                refresh: true,
                view: false,
                cloneToTop:true,
                addfunc:function(){
                    if($.isFunction(options.addFunc)){
                        var rowId = tableGridDom.grid('getGridParam','selrow');
                        var rowData = tableGridDom.grid('getRowData',rowId);
                        options.addFunc(rowData || null);
                    }
                },
                editfunc:function(rowId){
                    if($.isFunction(options.editFunc)){
                        var rowData = tableGridDom.grid('getOriginalData',rowId);
                        options.editFunc(rowData);
                    }
                },
                delfunc:function(rowIds){
                    if($.isFunction(options.delFunc)){
                        var rowData = [];
                        if(rowIds.length){
                            for(var i=0;i<rowIds.length;i++){
                                rowData.push(tableGridDom.grid('getOriginalData',rowIds[i]));
                            }
                        }
                        options.delFunc(rowData);
                    }
                }
            });


        if($.isFunction(options.uploadFunc)){
            tableGridDom.grid("customButton",{
                title:bt.lang('common.button.imports'),//"导入",
                buttonicon:"fa-upload",
                onClickButton: function(){
                    options.uploadFunc();
                }
            });
        }
        if($.isFunction(options.downloadFunc)){
            tableGridDom.grid("customButton",{
                title:bt.lang('common.button.exports'),//"导出",
                buttonicon:"fa-download",
                onClickButton: function(){
                    options.downloadFunc();
                }
            });
        }

        var showOrHideCol = elemName + "_showOrHide";
        tableGridDom.grid("customButton",{
            title:bt.lang('common.table.showOrHideColumns'),//"显示/隐藏列",
            buttonicon:"fa-eye",
            onClickButton: function(){
                var colModelData = tableGridDom.getColModel();
                var colModel = tableGridOption.colModel;
                var html = '<div class="layui-form show-or-hide-col"><ul>';
                for(var i=0;i<colModel.length;i++){
                    for(var j=0;j<colModelData.length;j++) {
                        var cm = colModel[i];
                        var cmd = colModelData[j];
                        if(cm.name && cmd.name && cm.name === cmd.name && !cm.key){
                            html += '<li>';
                            html += '<input type="checkbox" title="' + cm.label + '" value="' + cm.name + '" lay-filter="' + showOrHideCol + '" ';
                            if(cmd.frozen){
                                html += ' disabled ';
                            }
                            if(!cmd.hidden){
                                html += ' checked ';
                            }
                            html += ' lay-skin="primary">';
                            html += '</li>';
                        }
                    }
                }
                html+='</ul></div>';
                layer.open({
                    type: 1,
                    title:bt.lang('common.table.showOrHideColumns'),//"显示/隐藏列",
                    area: ['715px', '220px'],
                    content: html,
                    success: function(layero, index) {
                        form.render('checkbox');
                    }
                });

            }
        });
        form.on('checkbox('+showOrHideCol+')',function(data){
            if(data.elem.checked){
                tableGridDom.showCol(data.value);
            }else{
                tableGridDom.hideCol(data.value);
            }
        });


        tableGridDom.grid("navSeparatorAdd");

        that.bindSearch(tableGridDom);

        $(window).unbind("resize").bind("resize", function () {
            var width = $(".tableGridWrapper").width();
            tableGridDom.setGridWidth(width);
        });

        return tableGridDom;
    };


    tableGrid.prototype.bindSearch = function(table){
        $('button[lay-filter="search"]').attr('type','submit');//设置回车可查询功能 liuyj
        form.on('submit(search)', function(data){
            table.grid('setGridParam',{
                postData:data.field,
                page:1
            }).trigger("reloadGrid");
            return false;
        });
    }

    function throwError(msg) {
        throw new Error(msg);
    }

    var tableGrid = new tableGrid();
    var obj = {
        init: function (option) {
            return tableGrid.init(option);
        }
    };
    //输出test接口
    exports('tableGrid', obj);
});
