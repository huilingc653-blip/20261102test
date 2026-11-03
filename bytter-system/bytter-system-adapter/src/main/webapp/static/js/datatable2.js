/**
 * 表格组件
 * 本组件依赖由第三方插件datatables
 * liuyj
 * 2019年6月27日
 * 0.0.1 非服务端可表格组件
 */

layui.define(['datatablecore','jquery','form','fixedColumns','datatableColReorder'], function(exports) {
    "use strict";
    var $ = layui.jquery,
        layer = layui.layer,
        laypage = layui.laypage,
        form = layui.form();
    var TableList = function(){
        this.initConfig = { //初始默认配置
            elem:undefined,//容器
            autoWidth:true,
            serverSide:false,//开启服务器模式; false 代表前端排序
            checkbox:false,//添加首列checkbox
            scrollX: true,
            dom:'rtp',
            processing: true,
            colReorder:true,//固定列
            // scrollY:'430px',//设置表格固定高度
            lengthMenu:[30, 45, 50,100,500],//每页显示多少条
            // fixedColumns: true,
            // fixedColumns: {
            // leftColumns:2,
            // rightColumns:3
            // },
        }
        this.table = null;
        this.config = null
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
        sEmptyTable:bt.lang('common.msg.temporarily_no_data') ,//"暂无数据",
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

    /**
     * 参数设置
     * @param {Object} options  isMultiTables 标识是否页面多表格功能,如果是，则将实例存贮在页面中
     *
     */
    TableList.prototype.set = function(options,isMultiTables){
        var that = this;
        var config = that.config;
        // 获取产品的肤色
        // if(store.get('web-skin') != 'light-skin'){
        //     $(options.elem).addClass('table-dark')
        // }
        that.config = $.extend(true,{},that.initConfig,options);//解决同一个页面多个表格调用的问题
        //that.config.dom ='rt<"wrapper datatable-page">',
        that.config.dom ='rt';
        // 初始化右侧固定列信息
        // that.initFixedRightColumns()
        return that
    };
    //表格初始化
    TableList.prototype.init = function(){
        var that = this;
        var _config = that.config;

        var elem = _config.elem,$elem = $(elem);
        $elem.removeClass('layui-table');
        //表格初始化

        _config.language = lang;
        that.config.table =  $elem.DataTable(_config);
        return that.config.table;
    }

    //初始化右侧固定列
    TableList.prototype.initFixedRightColumns = function(){
        // 配置了fixedColumns，同时fixedColumns.rightColumns无配置时 或者未配置 fixedColumns
        if((this.config.fixedColumns && !this.config.fixedColumns.rightColumns) || !this.config.fixedColumns ){
            var columns = this.config.columns;
            var lastIndex = columns.length - 1;
            if(columns[lastIndex].data === null){ //当最后一个数据data值为null时，判定此列为操作列
                if(!this.config.fixedColumns){
                    this.config.fixedColumns = {};
                }
                this.config.fixedColumns.rightColumns = 1;
            }
        }
    }
    /**
     * 新增行
     * ops Array or Object  行数据
     *
     * */

    TableList.prototype.addRow = function( ops ){
        this.config.table.row.add(ops).draw()
    }
    /**
     * 删除行方法
     * $tr 删除行的jquery对象
     * */
    TableList.prototype.removeRow = function( $tr ){
        this.config.table.row($tr).remove().draw(false)
    }

    /**
     * 列显示或隐藏方法
     * index 列索引
     * flag  Boolean  true 代表显示，false 代表隐藏
     * */
    TableList.prototype.setColumnShow = function (index,flag) {
        var column = this.config.table.column( index );
        column.visible( flag );
    }

    function throwError(msg) {
        throw new Error(msg);
    };

    var obj = {
        init:function(ops){
            var tble = new TableList();
            tble.set(ops);
            tble.init();
            return tble
        },

    }

    exports("datatable2",obj)
}).addcss('../../../css/datatable.css','datatablecss');

