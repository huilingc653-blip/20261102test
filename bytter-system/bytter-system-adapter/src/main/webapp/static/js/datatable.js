/**
 * 表格组件
 * 本组件依赖由第三方插件datatables
 * liuyj
 * 2017年4月19日
 * 0.0.1 基础表格
 * 0.0.2 加入checkbox单选多选
 * 0.0.3 增加删除、添加、刷新等功能
 * 0.0.4 添加跳转页面功能
 * 0.0.5 支持过滤筛选功能
 * 0.0.6 支持行合并功能
 * 0.0.7 支持下拉表格
 * 0.0.8 支持小计、汇总，选择汇总
 * 0.0.9 支持单选。多选，及选择颜色
 * 0.0.10 修改点击返回数据
 * 0.0.11 第一列复选框判断显示，要求数据中添加一个参数checkboxHide
 * 0.0.12 修复有btInitComplete时，无法点击的问题
 * 0.0.13 新增加表格汇总的方法
 * 0.0.14 拽回调函数,添加重新添加汇总
 * 0.0.15 默认支持打印功能,data-btns=""参数
 * 0.0.16 添加冻结及修改托拽默认参数
 * 0.0.17 添加buttons hide参数，用于设置按钮的隐藏
 * 0.0.18 修复冻结列被托拽
 * 0.0.19 添加方法，用于获取表格中被选中的数据
 * 0.0.20 添加表头行合并时，checkbox也合并
 * 0.0.21 新增render()方法
 * 0.0.22 新增draw()方法
 * 0.0.23 新增fixedLeftToggleCheck方法,解决固定列全选切换
 * 0.0.24 跳转输入框注册只能输入正整数
 * 0.0.25 新增加jumpPageSelect参数，用于开启跨页选择功能
 * 0.0.26 新增加分类汇总功能
 * 0.0.27 新增加分类汇总功能 convertMoney参数，用于元与万元转换
 * 0.0.28 添加excel导出功能
 * 0.0.29 添加本位币转换汇总功能
 * 0.0.30 优化页码跳转时的输入内容不能大于总页码；解决刷新页面时，页码都是为0的设置
 * 0.0.31 解决ie浏览器在每页有100条数据时，出现表格底部内容未显示的问题
 * 0.0.32 重新写print当前页的方法
 * 0.0.33 添加表格状态保存服务器的功能
 * 0.0.34 新增加表格刷新方法
 * 0.0.35 新增加不打印操作列信息及打印JS从footer中导入
 * 0.0.36 新增双表头参数doubleHeader
 * 0.0.37 配置国际化
 * 0.0.38 更改表格底部
 * 0.0.39 新增加查询回调方法
 * 0.0.40 内容优化
 * 0.0.41 新增加汇总参数outerDate,传为true，代表汇总的数据是外部输入的数据
 * 0.0.42 更新选中样式进行统一管理，汇总显示列无复选框放在第一列，有放在第二列
 * 0.0.43 添加汇总title属性
 * 0.0.44 先table 先hide  再show
 * 0.0.45 优化全选全不选时，回调返回数据添加isSelect属性
 * 0.0.46 解决pages为NaN数据的问题
 * 0.0.47 解决打印所有时，无内容打印出null的问题
 * 0.0.46 新增加ignoreButtons:['colvis','output','print']设置，用于忽略部分默认按钮;并在分页上添加了一个className datatable-page
 * 0.0.48 解决分类汇总列的字段重写时的导出问题
 * 0.0.49 scrollY 参数在没有汇总时，改成300px
 * 0.0.50 设置表格最小高度为360px
 * 0.0.51 初始化有隐藏列时，汇总行要设置隐藏td
 * 0.0.52 解决列隐藏信息保存后，隔天不生效的问题
 * 0.0.53 新增加suffix用于汇总特殊后缀要求，如百分比
 * 0.0.54 新增加money用于汇总非货币列汇兑，默认为true，设置false，将不对汇总进行千分格式化
 * 0.0.55 新增加expression回调方法用于自定义汇总，其参数为当前行每列的数据
 * 0.0.56 在汇总页面上添加分页字段信息data-sumpage-text，用于导出所有的分页汇总
 * 0.0.57 分页 更改
 * 0.0.57 新增 destroy:true（销毁之前页面表格对像）,columns[0].title属性，设置表头
 * 0.0.58 当没有分页时，将显示条数更改为500条
 * 0.0.59 添加批量撤回功能
 * 0.0.60 新增加clickRowSelected，用于设置点击行内容选中参数
 * 0.0.61 动态数据汇总时，动态变化的数据容器div添加一个className changeful，与outerData true配合使用。
 * 0.0.62 新增加初始化右侧固定列参数
 * 0.0.63 表格复选框filter动态生成
 * 0.0.64 构造函数中，新增isMultiTables参数，用于判断是否需要在页面上存贮实例对象
 * 0.0.65 新增加pageSizeOptions参数，用于自定义每页显示多少条设置
 * 0.0.66 新增加getSelectRowIndexes方法，用于获取选中行的行索引；优化onCheckbox方法，支持参数为数组，解决多次调用oncheckbox慢的问题
 * 0.0.67　新增加updateSummary　更新表格汇总数据的方法
 * 0.0.68 新增加参数exportFile 用于输出文档设置，其中printTitle 设置打印文档标题；outputTitle　设置导出文档名称及标题　
 * 0.0.69 新增查询按钮自定义方法customizeFormSearchFunction，常用于查询按钮根据条件，渲染不同的表格
 * 0.0.70 添加国际化
 * 0.0.71 解决可编辑表格托拽时，组件未被初始化的问题
 * 0.0.72 新增自定义导出excel文件名称
 * 0.0.73 新增取消默认状态保存按钮参数ignoreButtons['state']
 * 0.0.74 新增insertInformation方法，用于在底部信息栏中插入信息，返回html字符串
 * 0.0.75 insertInformation参数可以传入boolean值，默认为false,true代码从后台取值
 * 0.0.76 新增customPrint参数，用于设置打印换行及自定义打印功能
 * 0.0.77 新增打印方向参数，0横向打印、１为纵向打印
 * 0.0.78 新增createSummaryText参数，用于自定义汇总名称，另外表格无数据时，只提醒不打印
 * 0.0.79 新增加customDom,自定义表格布局参数
 * 0.0.80 新增customSum参数，用于自定义汇总
 * 0.0.81 新增selectSumCallback 选择行汇总回调方法，用于选择自定义汇总
 * 0.0.82 新增notAdjustColumns参数，用于优化表格绘制效果，取消重新计算列宽的方法,默认 值为false
 * 0.0.83 新增otherExport 参数，其他导出功,如 选中行明细导出
 * 0.0.84 新增initQueryParams 参数，用于重置查询条件功能，必须返回新的查询参数
 */

layui.define(['layer', 'datatablecore', 'jquery', 'form', 'fixedColumns', 'datatableColReorder', 'datatableButtons', 'datatableColVis', 'colResize', 'money', 'jszip', 'html5', 'lodop', 'laypage'], function (exports) {
    "use strict";
    var $ = layui.jquery,
        layer = layui.layer,
        laypage = layui.laypage,
        form = layui.form();
    var TableList = function () {
        this.initConfig = { //初始默认配置
            elem: undefined,//容器
            notAdjustColumns:false,
            autoWidth: true,
            serverSide: true,//开启服务器模式; false 代表前端排序
            checkbox: false,//添加首列checkbox
            // disableIds:[],// checkbox禁选ids
            scrollX: true,
            //dom:'rt<"wrapper">',
            dom: 'rtp',
            customDom: '',//自定义dom布局
            select: true,
            selectCss: true,//选择中默认样式
            clickRowSelected: true,//行点击选中配置，false代表不能点击选中，默认为true
            mulselect: true,
            processing: true,
            resize: true,//列托宽托窄参数
            colReorder: true,//固定列
            jumpPageSelect: false,//跨页选择
            buttonsLayout: true,//是否需要配置按钮
            deferRender: true,//控制表格的延迟渲染，可以提高初始化的速度
            // lengthMenu:[],// 每一页显示多少条信息，-1代表所有
           // scrollY: '520px',//设置表格固定高度
            scrollY: '446px',//设置表格固定高度 13行
            lengthMenu: [10, 25, 50, 100, 500],//每页显示多少条
            pageSizeOptions: [],//每页显示多少条
            ignoreButtons: [],
            exportFile: { //输出文档设置
                printTitle: '', //设置打印标题　默认无标题
                outputTitle: '',//设置导出标题　默认为父级选项卡内容
                outputFilename: '',//设置导出文件名称　默认为父级选项卡内容或标题名称
                customPrint: {
                    breakWord: [],//参数默认为空数组[{name:'bank',title:'开户银行名称',width:'120'}]， 布尔值时，代码所有列都支持换行
                    // header:function (visible,dt) { visible　参数为当前所有表格列显示情况；dt为datatable实例
                    //    return html
                    // }, 自定义表头传入,用于多表头打印
                    pageOrient: 0,//打印方向参数，横向打印0，1为纵向，
                },
                // 导出excel扩展单行信息回调方法
                // extendExcelSingleRowInfoCallback: function () {
                //     // 返回一个数组，导出excel扩展单行信息，一个数组元素标识一个单元格内容
                //     return [];
                // },
            },
            // insertInformation:function(settings){ //底部插入信息回调方法，返回html字符串,也可以传入boolean值，true代表从后台获取
            //     return html
            // },
            //自定义导出所有 主要用于要求导出上万条的需求,
            // 参数processing为启动或者关闭加载动画方法，processing（true）代表开启动画；processing(false)代表关闭动
            // customExportAll:function(params,processing){
            //    processing(false)
            // },
            // customExportSelected:function(params,processing){
            //    processing(false)
            // },
           // otherExport:{ // 其他导出
           //     text:'', // 按钮文本
           //     callback:function(processing){ // 其他导出执行方法
           //         processing(false)
           //     }
           // },
            //destroy:true,销毁页面所有表格对像
            // ignoreButtons:['colvis','output','print','state'],// 忽略的表单默认按钮 state取消状态保存按钮
            // columns:[],
            // fixedColumns: true,
            // fixedColumns: {
            // leftColumns:2,
            // rightColumns:3
            // },
            paging: true,//是否需要分页，传入false， 表格将没有分页

            // print:true,
            ajax: {
                contentType: 'application/json',
                url: undefined,
                type: 'post',
                data: function (d) {
                    return JSON.stringify(d);
                }
            },
            isInitData: true,//true代码初始化加载数据，false代表初始化不加载数据,但是点击查询按钮将会加载数据
            // searchForm:{
            //     formElem:"#id",//表单选择器，即form标签的id
            //     formFilter:"", //查询按钮的lay-filter属性
            //     customizeFormSearchFunction:function(data){ //自定义查询按钮方法，常用于一个查询按钮，根据条件渲染不同表格

            //     }
            //     callback:function(data){ //点击查询按钮的回调方法
            //     }
            //     初始化查询参数
            //      initQueryParams:function(params){
            //           return params
            //     },
            // },
            // summary:{
            //     pageSum:true,//小计功能开启
            //     selectSum:true,//已选汇总功能开启
            //     groupColumn:'curName',//分类汇总列参数
            //     createSummaryText:function(text){ return text} //创建汇总标题的自定义方法,需要有返回值
            //     position:'top', //汇总显示的位置，默认在底部，top代表在顶部显示
            //     convertMoney:'yuan',//或者'million'或者function(){} 金额转换,
            //     natualCurrency:function(){ return [obj]},//本位币计算信息,函数返回一个数组对像，其对象有两个分健currency、rate;
            //     outerData:false,// 外部数据汇总，默认为false，如果是true，代表数据是加工之后再汇总
            //     sumTextCol:2,//配置汇总文本是显示列
            //     customSum:{
            //          text:function(){return '折合人民币'}, //如果无return 或者return '' 将不会不会添加这一行
            //          sumCallback:function(currentPageData,colName){ //汇总回调，必须要有返回值
            //                 currentPageData　当前页面数据 colName　当前列名称
            //                 return 2222
            //          },
            //             renderHtml:function(html){ // 重新绘制汇总html的回调方法必须有返回值 2021/11/11 liuyj
            //                 return html
            //             }
            //     }
            //     format:{
            //         decimal: 3
            //     },
            //     sumList:[
            //         {
            //             colNum:'6',//汇总列序号
            //             dataName:'salary',//汇总列数据
            //             format:1,//自定义数据格式
            //             suffix:'%',//‰　添加后缀字符串
            //　           money:false,//非货币类型，不需要千分格式化
            //            expression:function(obj){ //自定义汇总内容 obj为各汇总列数据
            //                    console.log(obj);
            //              return obj['payMoney']
            //            },

            //            选中行汇总回调方法，返回计算结果 参数currentData 当前数据，selectedData 当前表格选中数据
            //            selectSumCallback:function(currentRowData,selectedData){
            //
            //                return '100.00',
            //            }
            //         },
            //     ]
            // },
            // btInitComplete:function(setting,json){ //初始化完成的回调
            //     console.log(json)
            // },
            // btDrawCallback:function(setting){ //初始完成的回调方法
            //
            // },
            // btClickRowCallback:function(data){//点击行或全选的回调
            //     console.log(data)
            // },
            //btHeaderCallback:function(thead, data, start, end, display){ //头部初始化回调
            //
            //},
            createdRow: function(row, data, dataIndex){
                $(row).attr('data-dt-row', dataIndex);
            },
            stateSave: true,
            stateKey:'' // 表格状态保存key
        };
        this.table = null;
        this.flag = true;
        this.config = null;
        this.selectId = [];//存贮所有选中的数据
        this.bizName = 'datatables';
        this.currentObject = null;
        this.groupSummaryName;//分类汇总字段
        this.tableStatus = null;
        this.doubleHeader = false;//是否为双表头
        this.drawItem = 0; //重绘次数
        this.selectedStyle = 'selected';//设置选中的公共样式
        this.filterValue = { //表格复选框filter参数
            allSelectFiliter: '',
            childSelectFiter: '',
        };

        this.isMultiTables = false;//标记是否页面多表格渲染,默认为false
    }

    var lang = {
        sProcessing: "<div class='loader'>" + bt.lang('sys.notification.loading') + "</div>",//加载中...
        // sProcessing: "<div class='loader'>bt.lang('common.table.loading')</div>",//加载中...
        //sProcessing: "<div class='loader'><i class='layui-icon layui-anim layui-anim-rotate layui-anim-loop' style='font-size: 40px'></i></div>",//
        sLengthMenu: bt.lang('common.table.lengthMenu'),//"每页 _MENU_ 项",
        // sZeroRecords: "换个搜索词试试呢？暂无数据",
        // sZeroRecords: bt.lang('common.msg.temporarily_no_data'),//"暂无数据",
        sZeroRecords: '<img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank">',//"暂无数据",
        sInfo: bt.lang('common.table.info'),//"当前显示第 _START_ 至 _END_ 项，全部 _TOTAL_ 项。",
        sInfoEmpty: bt.lang('common.table.infoEmpty'),//"当前显示第 0 至 0 项，全部 0 项",
        sInfoFiltered: bt.lang('common.table.infoFiltered'),//"(由 _MAX_ 项结果过滤)",
        sInfoPostFix: "",
        sSearch: "本地搜索：",
        sUrl: "",
        // sEmptyTable: bt.lang('common.msg.temporarily_no_data'),//"暂无数据",
        sEmptyTable: '<img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang('common.msg.temporarily_no_data')+'</div>',//"暂无数据",
        sLoadingRecords: bt.lang('common.table.loading'),//"载入中...",
        sInfoThousands: ",",
        oPaginate: {
            sFirst: bt.lang('common.table.firstPage'),//"首页",
            //sPrevious:bt.lang('common.table.previousPage') ,//"上一页",
            sPrevious: '< ',//"上一页",
            //sNext:bt.lang('common.table.nextPage') ,//"下一页",
            sNext: '> ',//"下一页",
            sLast: bt.lang('common.table.lastPage'),//"最后页",
            sJump: bt.lang('common.table.jump'),//"跳转至"
        },
        oAria: {
            sSortAscending: bt.lang('common.table.sortAscending'),//":以升序排列此列",
            sSortDescending: bt.lang('common.table.sortDescending') //": 以降序排列此列"
        }
    };
    var table, LODOP;//
    /**
     * 参数设置
     * @param {Object} options  isMultiTables 标识是否页面多表格功能,如果是，则将实例存贮在页面中
     *
     */
    TableList.prototype.set = function (options, isMultiTables) {
        var that = this;
        var config = that.config;
        // 获取产品的肤色
       // if(store.get('web-skin') != 'light-skin'){
       //   $(options.elem).addClass('table-dark')
       // }
        if(options.searchForm && options.searchForm.formFilter){
            var $currentFormFilter = $('[lay-filter="'+options.searchForm.formFilter+'"]')
            if(!$currentFormFilter.parents('.layui-collapse')[0]){
                $currentFormFilter.parents('form').addClass('search-border')
            }
        }

        //  $(options.elem).hide();//表格隐藏
        //添加checkbox
        if (options.checkbox) {
            options.columnDefs = setcolumnDefs(options);
            options.columns = setCheckbox(options, that);
        }
        for (var i = 0; i < options.columns.length; i++) {
            if (options.columns[i] && (options.columns[i].data == null || options.columns[i].data == "null")) {
                var className = options.columns[i].className;
                if (className) {
                    className += ' extraCol';//所有额外数据列，添加extraCol标识，用于导入列表用
                } else {
                    className = 'extraCol';
                }
                options.columns[i].className = className;
            }
        }
        ////如果没有汇总参数，表格固定高度为300px
        if (!options.summary) {
           // that.initConfig.scrollY = '300px';
            that.initConfig.scrollY = '340px';
            // that.initConfig.scrollY = '410px';
        }
        //that.config.dom ='rt';
        that.config = $.extend(true, {}, that.initConfig, options);//解决同一个页面多个表格调用的问题
        //that.config.dom ='rt<"wrapper datatable-page">',
        that.config.dom = 'rt';

        if (that.config.pageSizeOptions.length) { //当外部传了每页显示多少条时
            that.config.lengthMenu = that.config.pageSizeOptions;
        }
        // 多表头表格，关闭列托拽功能
        var $table = $(that.config.elem)
        if($table[0] && $table.find('thead tr').length > 1){
            that.config.colReorder = false
        }
        // 动态设置复选框的filter

        var number = that.randomNum();

        that.filterValue.allSelectFiliter = 'checkboxsfilter' + number;
        that.filterValue.childSelectFiter = 'checkchild' + number;
        //设置单选多选
        that.setSelectType();
        // 初始化右侧固定列信息
        if (that.config.colReorder) {
            that.initFixedRightColumns();
        }
        //设置表格按钮位置
        that.tableDom(options);
        //设置按钮
        if (that.config.buttonsLayout) {
            that.setDefaultBtn();
            //设置按钮的显示
            that.setButtonsShow();
        }
        //加载表格状态
       that.loadTableStatus();
        //向服务器保存表格状态
        that.saveTableStatus();
        //设置表格状态参数
        that.stateSaveParams();
        //设置行选中的公共样式
        that.selectStyle();
        // 判断是否是页面多表格功能
        if (isMultiTables) {
            that.isMultiTables = true;
        }

        return that
    };
    //表格初始化
    TableList.prototype.init = function () {
        var that = this;
        var _config = that.config;
        var elem = _config.elem, $elem = $(elem);
        if (elem === undefined) {
            throwError('dataTable Error:请配置参数elem,此参数为jQuery选择器!');
        }
        if(_config.isLayIgnore){
            //禁用layui多选的美化渲染功能;
            $elem.removeClass("layui-form").removeClass("layui-table");
        }else{
            //checkbox渲染需要添加layui-form类;
            $elem.addClass("layui-form").removeClass("layui-table");
        }

        $elem.width('100%');
        //表头初始化
        that.headerInit($elem);
        //添加托拽回调函数
        that.colReorderInit($elem) //设置托拽/固定列/汇总
        //搜索表格
        that.searchForm();
        //表格初始化完成回调方法注册
        that.tableInitComplete();
        //表格重绘回调方法注册
        that.tableDrawCallback();
        //表格初始化完成表头时的回调
        that.tableHeaderCallback();
        _config.language = lang;
        // 赋值columnname
        that.setColumnsName();
        //设置托宽托窄参数
        that.colResizeInit();
        if (!_config.isInitData) { //如果无初始化数据，使用新的空数据接口
            that.ajaxUrl = _config.ajax.url;
            _config.ajax.url = basePath + '/prestrain';
        }
        //存贮summary在table上
        if (_config.summary) {
            $elem.data("summary", _config.summary);
        }
        //表格初始化
        that.table = table = $elem.DataTable(_config);
        // 解决表格分段加载的问题 先隐藏，然后初始化完成时再显示
        // $elem.parents('.dataTables_wrapper').hide();
        // var tables = $elem.parents('.dataTables_wrapper').find('table');
        // tables.addClass("layui-form").removeClass("layui-table");
        // tables.show();
        // $elem.parents('.dataTables_wrapper').show();
        return table;
    }

    //初始化右侧固定列
    TableList.prototype.initFixedRightColumns = function () {
        // 配置了fixedColumns，同时fixedColumns.rightColumns无配置时 或者未配置 fixedColumns
        if ((this.config.fixedColumns && !this.config.fixedColumns.rightColumns) || !this.config.fixedColumns) {
            var columns = this.config.columns;
            var lastIndex = columns.length - 1;
            if (columns[lastIndex].data === null) { //当最后一个数据data值为null时，判定此列为操作列
                if (!this.config.fixedColumns) {
                    this.config.fixedColumns = {};
                }
                this.config.fixedColumns.rightColumns = 1;
            }
        }
    }
    //加载表格状态
    TableList.prototype.loadTableStatus = function () {
        var that = this;
        that.config.stateLoadCallback = function (settings, callback) {//表格状态获取：当本地无时，就向服务器加载过来
            var localKey = that.config.stateKey ? that.config.stateKey :'DataTables_' + settings.sInstance;
            var tableKey = that.config.stateKey ? that.config.stateKey:that.getTableKey(localKey);
            store.remove(tableKey);
            $.ajaxSetup({cache: false});
            $.ajax({
                url: basePath + '/sys/user/tableRecord/get',
                data: {tableKey: tableKey},
                async: false,
                dataType: 'json',
                success: function (json) {
                    if (!json) {
                        callback({});
                    } else {
                        if (json.msg) {
                            callback({})
                        } else {
                            json.time = Date.parse(new Date());
                            try{
                                store.set(tableKey, json);
                            }catch (e) {}
                            callback(json);
                        }
                    }
                },
                error: function () {
                    callback({});
                }
            });
        }
    }
    //保存数据状态
    TableList.prototype.saveTableStatus = function () {
        var that = this;
        that.config.stateSaveCallback = function (settings, data) {//服务器保存表格状态
            var tableKey = that.getTableKey('DataTables_' + settings.sInstance);
            try{
                store.set(tableKey, data);
            }catch (e) {}

        }
    };
    //获取tableKey，用于保存表格状态
    TableList.prototype.getTableKey = function (key) {
        var that = this;
        if(that.config.stateKey){
            return  that.config.stateKey
        }
        var pathname = self.location.pathname;
        if (basePath) {
            pathname = pathname.replace(basePath, '');
        }
        return key + '_' + pathname;
    }

    TableList.prototype.stateSaveParams = function () {
        var that = this;
        that.config.stateSaveParams = function (settings, data) {
            data.start = 0;//页码不被保存，每次刷新时，页码都是为0
        }
    }
    //组装form表单参数对象
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push($.trim(this.value) || '');//this value添加trim方法 liuyj 2018-05-23
            } else {
                o[this.name] = $.trim(this.value) || '';//this value添加trim方法 liuyj 2018-05-23
            }
        });
        return o;
    }

    //查询方法
    TableList.prototype.searchForm = function () {
        var that = this;
        var _config = that.config;
        var searchForm = _config.searchForm;
        var isFirstForm = true;
        if (typeof (searchForm) === "object") {
            _config.fnServerParams = function (aoData) {
                var params = $(searchForm.formElem).serializeObject();
                if(_config.searchForm.initQueryParams && typeof (_config.searchForm.initQueryParams) === 'function'){
                    params = _config.searchForm.initQueryParams(params);
                }
                // aoData.formdata = $(searchForm.formElem).serializeObject();
                aoData.formdata = params
            };
            var callback = searchForm.callback;
            //设置表单文本输入时，回车执行查询设置
            $('button[lay-filter="' + searchForm.formFilter + '"]').attr('type', 'submit');

            form.on('submit(' + searchForm.formFilter + ')', function (data) {
                //自定义查询按钮方法，常用于一个查询按钮，根据条件渲染不同表格
                if (searchForm.customizeFormSearchFunction && typeof (searchForm.customizeFormSearchFunction) === 'function') {
                    searchForm.customizeFormSearchFunction(data);
                    return false
                }

                var table = new $.fn.dataTable.Api(_config.elem);
                if (isFirstForm && !_config.isInitData) { //无初始化数据时，点击查询就加载数据
                    //table.ajax.url( that.ajaxUrl ).draw();

                    table.ajax.url(that.ajaxUrl);
                    table.ajax.reload(function () { //表格列宽对不齐的问题
                       if(!_config.notAdjustColumns){
                           table.columns.adjust().draw();
                       }
                    }, true);

                    isFirstForm = false;
                } else {
                    that.selectId = []
                    table.draw();
                }
                if (callback && typeof (callback) === 'function') {
                    callback(data);
                }
                return false;
            });
        }
        ;
    }

    TableList.prototype.headerInit = function ($elem) {
        var that = this;
        var _config = that.config;
        if (_config.checkbox) {
            var html = '';
            var len = $elem.find('thead tr').length;
            if (_config.mulselect) {
                var checkBoxHtml = '';
                if(!_config.isLayIgnore){
                    checkBoxHtml = '<div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div>';
                }

                if (len > 1) {
                    // 忽略美化
                    if(_config.isLayIgnore){
                        html += '<th rowspan="' + len + '" class="headerCheckAll" style="max-width: 28px;"><input type="checkbox" lay-skin="primary" lay-ignore class="checkAll">' + checkBoxHtml + '</th>';

                    }else{
                        html += '<th rowspan="' + len + '" class="headerCheckAll" style="max-width: 28px;"><input type="checkbox" lay-skin="primary" lay-filter ="' + that.filterValue.allSelectFiliter + '" class="checkAll">' + checkBoxHtml + '</th>';
                    }

                    if (_config.columns[0].className.indexOf('ckbx28') !== -1) { //解决双表头对不齐的问题
                        _config.columns[0].className = _config.columns[0].className.replace('ckbx28', '');
                    }
                } else {
                    // 忽略美化
                    if(_config.isLayIgnore){
                        html += '<th class="headerCheckAll" style="max-width: 28px;"><input type="checkbox" lay-skin="primary" lay-ignore class="checkAll">' + checkBoxHtml + '</th>';
                    }else{
                        html += '<th class="headerCheckAll" style="max-width: 28px;"><input type="checkbox" lay-skin="primary" lay-filter ="' + that.filterValue.allSelectFiliter + '" class="checkAll">' + checkBoxHtml + '</th>';
                    }
                }
            } else {
                if (len > 1) {
                    html += '<th rowspan="' + len + '"></th>';
                } else {
                    html += '<th></th>';
                }
            }
            $elem.find('thead tr th').eq(0).before(html);
        }
        that.checkDoubleHeader();
        //设置有第二列默认排序
        var order = _config.order;
        if (order && order.length) {
            if (typeof (order[0]) !== "object") {
                // _config.order = [];//清除所有专为[1, 'desc']的默认设置排序
                if (order.length === 2){ //统一处理一维参数转为二维数组
                    _config.order = [order]
                }else{
                    _config.order = [];
                }
            }
        } else {
            _config.order = [];//解决datatable默认第一列排序的问题（当第一列是复选框时不需要，
        }
        // that.setHeaderCenter();//设置表头居中
    }
    //检验表格是否有双表头，如果是双表头，则表格不设置table-layout:fixed 属性
    TableList.prototype.checkDoubleHeader = function () {
        var that = this;
        var $theadTrs = $(that.config.elem).find('thead tr');
        if ($theadTrs.length > 1) {
            that.config.resize = false;
        }
    }

    TableList.prototype.colReorderInit = function ($elem) {
        var that = this;
        var _config = that.config;
        if (_config.colReorder && typeof _config.colReorder !== "object") {
            var leftColumns = 0, rightColumns = 0;
            if (_config.fixedColumns && typeof _config.fixedColumns === "object") {
                if (_config.fixedColumns.leftColumns) {
                    leftColumns = _config.fixedColumns.leftColumns;
                } else if (_config.checkbox) {
                    leftColumns = 1;
                }
                _config.fixedColumns.leftColumns = leftColumns;
                rightColumns = _config.fixedColumns.rightColumns;
            } else {
                if (_config.checkbox) {
                    _config.fixedColumns = {
                        leftColumns: 1,
                        rightColumns: 0
                    }
                }
            }
            var obj = {
                colReorder: {
                    //回调方法
                    fnReorderCallback: function () {
                        // var ds = $elem.find("tbody tr:eq(0) td");
                        var ds = that.table.columns().dataSrc();
                        if (_config.summary) {
                            $elem.find('tbody tr.btsummary').remove();
                            that.doSumData(that.table);
                        }
                        // 解决可编辑表格托拽时，组件未被初始化的问题
                        if (_config.btDrawCallback !== undefined && typeof (_config.btDrawCallback) === "function") {
                            _config.btDrawCallback(that.table.settings()[0]);
                        }
                        if(!_config.notAdjustColumns){
                            $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
                        }
                    },
                    iFixedColumnsLeft: leftColumns,
                    iFixedColumnsRight: rightColumns,
                },
            }
            $.extend(true, _config, obj);
        }
        return obj;
    }
    //buttons按钮左上角参数布局
    TableList.prototype.tableDom = function (ops) {
        var that = this;
        var _config = that.config;
        var buttonLayout = _config.buttonsLayout;
        var btn = _config.buttons;
        var selectTable = _config.selectTable;
        // if(typeof(ops.dom) !== "string"){
        if (buttonLayout || btn) {
            // _config.dom = 'Brtlip';
            if (_config.paging) {
                //_config.dom = 'Brt<"wrapper datatable-page">';
                _config.dom = 'Brtli<"jump_page">p';
                //_config.dom = 'Blfrtip';
            } else {
                _config.dom = 'Brt'; //不需要分页
            }
        } else {
            _config.dom = _config.paging ? 'tp' : 'rt';
        }
        // }
        if (typeof (selectTable) === 'object') {
            _config.dom = 'ftp';
        }
        if (_config.dom.indexOf('p') === -1) { //　如果配置中没有分页，将每页显示多少条改成500
            _config.lengthMenu = [500, 10, 25, 50, 100];//每页显示多少条
        }
        if (_config.insertInformation) {
            _config.dom = 'Brtli<"data_summary"><"jump_page">p';
        }
        if (_config.customDom) {//自定义表格布局参数
            _config.dom = _config.customDom;
        }
    }
    //设置表格的托宽托窄
    TableList.prototype.colResizeInit = function () {
        var that = this;
        var _config = that.config;
        if (_config.resize) { // Z代表托宽托窄功能参数
            _config.dom = 'Z' + _config.dom;

            var obj = {
                colResize: {
                    //回调方法
                    resizeCallback: function () {
                        that.table.state.save();

                    }
                },
            }
            $.extend(true, _config, obj);
        }
    }
    //打印当前页
    TableList.prototype.currentPrint = function () {
        var that = this;
        var _config = that.config;
        var currentPrint = {
            text: bt.lang('common.button.printcurrent'),//'打印当前页',
            className: 'print-btn active',
            exportOptions: {
                columns: ':visible'
            },
            action: function (e, dt, node, config) {
                node.parent('.dt-button-collection').hide(300);
                var data = dt.buttons.exportData(config.exportOptions);
                var d = $(dt).find("tbody");
                if (data.body && !data.body.length) {
                    layer.alert(bt.lang('common.msg.temporarily_no_data'), {icon: 7});
                    return
                }
                var addRow = function (d, tag, flag) {
                    var str = '<tr>';
                    if (!flag) {
                        for (var i = 0, ien = d.length; i < ien; i++) {
                            str += '<' + tag + '>' + d[i] + '</' + tag + '>';
                        }
                    } else {
                        var printBreakRowArr = _config.exportFile.customPrint.breakWord;
                        for (var i = 0, ien = d.length; i < ien; i++) {
                            var width = d[i].width;
                            if (printBreakRowArr) {
                                for (var j = 0; j < printBreakRowArr.length; j++) {
                                    if (printBreakRowArr[j].title == d[i].title) {
                                        width = parseFloat(printBreakRowArr[j].width);
                                        break
                                    }
                                }
                            }
                            var padding = (width - 0 - d[i].title.length * 9) / 2 + 15;
                            str += '<' + tag + ' style="padding:0 ' + padding + 'px">' + d[i].title + '</' + tag + '>';
                        }
                    }
                    return str + '</tr>';
                };
                var html = '';
                //　配置打印标题
                if (_config.exportFile && _config.exportFile.printTitle) {
                    html += '<p style="color: #333;text-align: center;font-size:16px;font-weight: 700;">' + _config.exportFile.printTitle + '</p>'
                }

                html += '<table class="layui-table"><thead>';
                var operationCols = data.operationCols;//操作列索引数组
                var header = data.header, titleArr = data.title;
                var isBreakRowPrint = false; //是否需要设置换行打印功能
                if (!isFunction(_config.exportFile.customPrint.header)) {
                    if (titleArr.length) {
                        isBreakRowPrint = true;
                        html += addRow(titleArr, 'th', true);
                    } else {
                        var newHeader = [];//重新组件打印表头
                        for (var k = 0; k < header.length; k++) {
                            var flag = true;
                            for (var a = 0; a < operationCols.length; a++) {
                                if (operationCols[a] === k) {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                newHeader.push(header[k]);
                            }
                        }
                        html += addRow(newHeader, 'th');
                    }
                } else {
                    html += _config.exportFile.customPrint.header(dt.columns().visible(), dt);
                    isBreakRowPrint = _config.exportFile.customPrint.breakWord
                }
                html += '</thead>'
                var bodyHtml = '';
                if (operationCols.length) { //有操作列时，删除操作列信息
                    var bodyTrs = $(dt.table().node()).find("tbody tr").clone();
                    for (var k = 0; k < bodyTrs.length; k++) {
                        // 1. extraCol额外添加列 不打印
                        // 2. 如果是汇总行删除所有不显示的
                        for (var i = 0; i < $(bodyTrs[k]).find('td').length; i++) {
                            var currentTd = $(bodyTrs[k]).find('td')[i];
                            if($(currentTd).css('display') == 'none' || $(currentTd).hasClass('extraCol')){
                                $(bodyTrs[k]).find('td:eq(' + i + ')').remove();
                                i --
                            }
                        }
                        // var resetFlag = 0
                        // for (var b = 0; b < operationCols.length; b++) {
                        //     var index = operationCols[b] - resetFlag;
                        //     $(bodyTrs[k]).find('td:eq(' + index + ')').remove();
                        //     resetFlag += 1;
                        // }
                        bodyHtml += '<tr>' + $(bodyTrs[k]).html() + '</tr>'
                    }
                } else {
                    bodyHtml = $(dt.table().node()).find("tbody").html();
                }
                html += '<tbody>' + bodyHtml + '</tbody>';
                if (config.footer) {
                    html += '<thead>' + addRow(data.footer, 'th') + '</thead>';
                }
                html += '</table>';
                lodopPrint(html, isBreakRowPrint, _config.exportFile.customPrint.pageOrient);
            }
        }
        return currentPrint
    }
    //打印表格所有
    TableList.prototype.allPrint = function () {
        var that = this;
        var _config = that.config;
        var allPrint = {
            text: bt.lang('common.button.printall'),//'打印全部',
            className: 'print-btn printAll active',
            exportOptions: {
                columns: ':visible'
            },
            action: function (e, dt, node, config) {
                node.parent('.dt-button-collection').hide(300);
                var addRow = function (d, tag) {
                    return '<' + tag + '>' + d + '</' + tag + '>';
                };

                var addRow2 = function (d, tag) {
                    var str = '';
                    var printBreakRowArr = _config.exportFile.customPrint.breakWord;
                    for (var i = 0, ien = d.length; i < ien; i++) {
                        var width = d[i].width;
                        if (printBreakRowArr) {
                            for (var j = 0; j < printBreakRowArr.length; j++) {
                                if (printBreakRowArr[j].title == d[i].title) {
                                    width = parseFloat(printBreakRowArr[j].width);
                                    break
                                }
                            }
                        }
                        var padding = (width - 0 - d[i].title.length * 9) / 2 + 15;
                        str += '<' + tag + ' style="padding:0 ' + padding + 'px">' + d[i].title + '</' + tag + '>';
                    }

                    return str + '';
                };

                //更新一个数组方法
                var updateArr = function (oldArr) {
                    var newArr = [];
                    for (var i = 0; i < oldArr.length; i++) {
                        newArr.push(oldArr[i]);
                    }
                    return newArr;
                }
                var getUpdateColumns = function (dataSrc, columns) {
                    var arr = [], align = [];
                    for (var i = 0; i < dataSrc.length; i++) {
                        for (var j = 0; j < columns.length; j++) {
                            if (columns[j].data == dataSrc[i]) {
                                arr.push(columns[j].render);
                                align.push(columns[j].className)
                                break
                            }
                        }
                    }

                    return {
                        renderFn: arr,
                        align: align
                    };
                }
                var data = dt.columns().dataSrc();
                var heads = dt.columns().header();
                var v = dt.columns().visible();
                var t = dt.columns().nodes();
                var breakWord = _config.exportFile.customPrint.breakWord;
                var dataSrc = updateArr(data);
                var visible = updateArr(v);
                var header = [],
                    updatedColumns = getUpdateColumns(dataSrc, _config.columns),
                    renderFn = updatedColumns.renderFn,
                    align = updatedColumns.align
                ;
                for (var i = 0; i < heads.length; i++) {
                    header.push($(heads[i]).text());
                }
                // for (var i = 0; i < _config.columns.length; i++) {
                //     renderFn.push(_config.columns[i].render);
                // }

                if (_config.checkbox) {
                    dataSrc.splice(0, 1), header.splice(0, 1), visible.splice(0, 1), renderFn.splice(0, 1), align.splice(0, 1);
                }
                var d = $.extend(JSON.parse(dt.ajax.params()), {length: 1000,start:0});
                $.ajax({
                    url: dt.ajax.url(),
                    type: 'post',
                    data: JSON.stringify(d),
                    contentType: 'application/json',
                    success: function (data) {
                        var list = [];
                        if (data) {
                            try {
                                list = typeof data === 'string'?JSON.parse(data).data:data.data;
                            } catch (e) {
                                list = [];
                            }
                            if (!list.length) {
                                layer.alert(bt.lang('common.msg.temporarily_no_data'), {icon: 7});
                                return
                            }
                            var html = '';
                            //　配置打印标题
                            if (_config.exportFile && _config.exportFile.printTitle) {
                                html += '<p style="color: #333;text-align: center;font-size:16px;font-weight: 700;">' + _config.exportFile.printTitle + '</p>'
                            }
                            var exportData = dt.buttons.exportData(config.exportOptions);
                            var titleArr = exportData.title, isBreakRowPrint = false; //是否需要设置换行打印功能
                            html += '<table class="layui-table">';
                            html += '<thead>';
                            if (!isFunction(_config.exportFile.customPrint.header)) {
                                html += '<tr>';
                                if (!titleArr) {
                                    for (var i = 0; i < header.length; i++) {
                                        if (header[i] && dataSrc[i] && visible[i]) {
                                            html += addRow(header[i], 'th');
                                        }
                                    }
                                } else {
                                    isBreakRowPrint = true;
                                    html += addRow2(titleArr, 'th');
                                }
                                html += '</tr>';
                            } else {
                                html += _config.exportFile.customPrint.header(v, dt);
                                isBreakRowPrint = _config.exportFile.customPrint.breakWord;
                            }

                            html += '</thead>';
                            var bodyHtml = '';
                            for (var i = 0; i < list.length; i++) {
                                bodyHtml += '<tr>';
                                for (var j = 0; j < header.length; j++) {
                                    if (header[j] && dataSrc[j] && dataSrc[j] != 'null' && visible[j]) {
                                        var c = dataSrc[j], rFn = renderFn[j];
                                        var val = list[i][c];
                                        if (rFn) {
                                            var meta = {row: i, col: (_config.checkbox) ? j + 1 : j};
                                            val = rFn(val, "", list[i], meta);
                                        }
                                        if (val === null) { //解决无内容时，打印出null的问题
                                            val = '';
                                        }
                                        if (breakWord.length) {
                                            var isBreak = false;
                                            for (var b = 0; b < breakWord.length; b++) {
                                                if (breakWord[b].name && dataSrc[j] == breakWord[b].name) {
                                                    isBreak = true
                                                    bodyHtml += '<td class="' + align[j] + ' break_word">' + val + '</td>';
                                                    break
                                                }
                                            }
                                            if (!isBreak) {
                                                bodyHtml += '<td class="' + align[j] + '">' + val + '</td>';
                                            }
                                        } else {
                                            if (breakWord) {
                                                bodyHtml += '<td class="' + align[j] + ' break_word">' + val + '</td>';
                                            } else {
                                                bodyHtml += '<td class="' + align[j] + '">' + val + '</td>';
                                            }
                                        }
                                    }
                                }
                                bodyHtml += "</tr>";
                            }
                            html += '<tbody>' + bodyHtml + '</tbody>';
                            html += '</table>';
                            lodopPrint(html, isBreakRowPrint, _config.exportFile.customPrint.pageOrient);
                        }
                    },
                    error: function (e, e1, e2) {
                    }
                });
            }
        }
        return allPrint;
    }

    var lodopPrint = function (html, flag, pageOrient) {
        var printObj = bt.lodop.init();
        setTimeout(function () {
            LODOP = printObj.lodop;
            if (!LODOP) {
                return;
            }
            var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all">'
                + '<style>table th,table td { border:1.6px solid #000!important; height:34px;color:#000;border-collapse:collapse;} table th {font-weight: bold!important} .text-break{word-break: normal;white-space:normal;},.noprint {display:none} .width30 {width:29%;display:inline-block;}'
                + ' .width40 {width:39%;display:inline-block;} .textCenter,.center {text-align:center} .fontSize18 {font-size:18px;height: 30px;} .textRight{text-align:right;}'
                + ' .layui-table {margin-top:4px} table {margin:0 auto} table thead th{word-break: keep-all;white-space:nowrap;text-align: center;}';
            if (!flag) {
                style += ' table tbody td,table tfoot td{word-break: keep-all;white-space:nowrap;} '
            } else {
                if (flag.length) {
                    style += ' table tbody td,table tfoot td{word-break: keep-all;white-space:nowrap;} '
                    style += ' table tbody td.break_word,table tfoot td.break_word{word-break:normal!;white-space:normal;} '
                } else {
                    style += ' table tbody td,table tfoot td{word-break: normal;white-space:normal;} '
                }
            }
            style += '</style>';
            var html1 = '<head>' + style + '</head><body>' + html + '</body>';
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 4);
            LODOP.NewPageA();
            LODOP.ADD_PRINT_HTM('9mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html1);
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
            if (pageOrient == 1) {
                LODOP.SET_PRINT_PAGESIZE(1)  //打印默认为横向打印　0，1为纵向，
            }
            LODOP.PREVIEW();
        }, 800)
    }
    //设置默认按钮，目前只支持 print colvis
    TableList.prototype.setDefaultBtn = function () {
        var that = this;
        var _config = that.config;
        var btn = _config.buttons;
        var colvisArray = that.colvisArray();//获取隐藏列索引
        var currentPrint = that.currentPrint();
        var allPrint = that.allPrint();
        var ignoreButtons = _config.ignoreButtons;
        var print = {
            extend: 'collection',
            text: bt.lang('common.button.print'),//'打印',
            buttons: [
                currentPrint,
                allPrint
            ],
            fade: true
        };

        var colvisBtn = {
            extend: "colvis",
            text: bt.lang('common.button.theadHide'),//'列隐藏',
            columns: colvisArray,
        };
        // 选中行导出功能
        var selectedExcel = {
            extend: 'excel',
            text: bt.lang('导出选中行'),//'导出选中行',
            className: 'active',
            exportOptions: {
                modifier: {
                    page: 'current'
                },
                columns: ":visible.exportColumn",
                // type: "selectedExport",
                type: "selectedExport",
                callback:function (dtObj,body,processing) {
                    var indexes = dtObj.rows(".selected").indexes();
                    if(!indexes){
                        layer.msg(bt.lang("common.message.confirm.selectData"), {
                            time: 1000
                        });
                        return false
                    }
                    var rowData = []
                    for (var i = 0; i < indexes.length; i++) {
                        rowData.push(body[indexes[i]])
                    }
                    if(typeof _config.customExportSelected === 'function'){
                        var params = JSON.parse(dtObj.ajax.params());
                        var currentColumns = $.extend([],dtObj.settings()[0].aoColumns);
                        for (var i = 0; i < params.columns.length; i++) {
                            var col = params.columns[i];
                            for (var j = 0; j < currentColumns.length; j++) {
                                if (currentColumns[i].data == col.data){
                                    col.title = currentColumns[i].title
                                    break
                                }
                            }
                        }
                        _config.customExportSelected(params,processing,rowData)
                        return
                    }
                    return rowData
                }
            }
        }
        //当前页导出excel
        var currentExcel = {
            extend: 'excel',
            text: bt.lang('common.button.exportsCurrentPage'),//'导出当前页',
            className: 'active',
            extendExcelSingleRowInfoCallback: _config.exportFile.extendExcelSingleRowInfoCallback,
            exportOptions: {
                modifier: {
                    page: 'current'
                },
                columns: ":visible.exportColumn",
                type: "current"
            }
        }
        //所有页导出excel
        var excel = {
            extend: 'excel',
            text: bt.lang('common.button.exportsAll'), // '导出所有',
            className: 'output active',
            extendExcelSingleRowInfoCallback: _config.exportFile.extendExcelSingleRowInfoCallback,
            exportOptions: {
                modifier: {
                    page: 'current'
                },
                columns: ":visible.exportColumn",
                type: "all",
                customExportAll: _config.customExportAll
            }
        }

        if (_config.exportFile && _config.exportFile.outputTitle) {
            currentExcel.title = _config.exportFile.outputTitle;
            excel.title = _config.exportFile.outputTitle;
        }
        if (_config.exportFile && _config.exportFile.outputFilename) {
            currentExcel.filename = _config.exportFile.outputFilename;
            excel.filename = _config.exportFile.outputFilename;
        }

        //导出
        var outPut = {
            extend: 'collection',
            text: bt.lang('common.button.exports'),//'导出',
            buttons: [
                currentExcel,
                excel
            ],
            fade: true
        };

        if(_config.otherExport){
            var otherExportExcel = {
                extend: 'excel',
                text: _config.otherExport.text, // '导出所有',
                className: 'output active',
                exportOptions: {
                    modifier: {
                        page: 'current'
                    },
                    columns: ":visible.exportColumn",
                    type: "otherExport",
                    callback: _config.otherExport.callback
                }
            }
            outPut.buttons.push(otherExportExcel)
        }
        if(_config.checkbox){
            outPut.buttons.unshift(selectedExcel)
        }
        var btnText = $(_config.elem).data("btns");
        if (btnText) {
            var btnsArr = btnText.split(',');
            if (btn) {
                eachBtns(btnsArr, btn, colvisBtn)
            } else {
                _config.buttons = [];
                eachBtns(btnsArr, _config.buttons, colvisBtn);
            }
        } else {//对列隐藏 cloumns重置
            if (btn && btn.length != 0) {
                for (var i = 0; i < btn.length; i++) {
                    var button = btn[i];
                    if (button.extend == 'colvis') {
                        button.columns = colvisArray;
                        break
                    }
                }
            } else {
                _config.buttons = [];
            }
        }
        if (ignoreButtons.indexOf('print') == -1) {
            _config.buttons.push(print);
        }
        if (ignoreButtons.indexOf('output') == -1) {
            _config.buttons.push(outPut);
        }
        if (ignoreButtons.indexOf('colvis') > -1) {
            for (var i = 0; i < _config.buttons.length; i++) {
                if (_config.buttons[i].extend == 'colvis') {
                    _config.buttons.splice(i, 1);
                    break;
                }
            }
        }

        //匹量撤回按钮
        var revokeBtn = {
            hide: !matchUrl(),
            text: bt.lang("common.button.batchRevoke"),
            action: function (e, dt, node, config) {
                var d = dt.rows(".selected").data();
                var rowData = d.splice(0, d.length);
                if (!rowData.length) {
                    layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
                    return
                }
                var ids = '';

                for (var i = 0; i < rowData.length; i++) {
                    if (ids == '') {
                        ids += rowData[i].businessKey;
                    } else {
                        ids += ',' + rowData[i].businessKey;
                    }
                }

                parent.layer.confirm(bt.lang('common.message.confirm.batchRevoke'), function () {
                    $.post(basePath + "/bpm/task/batchRevoke", {ids: ids}, function (result) {
                        dt.ajax.reload(null, false);//刷新表格
                        var msg = result.msg;
                        var obj = result.obj;
                        var length = obj.length;
                        if (length) {
                            //对报错信息进行转化，显示每个报错信息有多少条
                            for (var a = 0; a < length; a++) {
                                if (obj[a].corpName) {
                                    obj[a].errorMsg = [];
                                    var messageList = obj[a].messageList.sort();
                                    for (var i = 0; i < messageList.length;) {
                                        var count = 0;
                                        for (var j = i; j < messageList.length; j++) {
                                            if (messageList[i] == messageList[j]) {
                                                count++;
                                            }
                                        }
                                        obj[a].errorMsg.push([messageList[i].replace(/_/g, '.').toLowerCase(), count]);
                                        i += count;
                                    }
                                }
                            }
                            //拼装报错信息
                            for (var i = 0; i < length; i++) {
                                if (obj[i].corpName) {
                                    msg += obj[i].corpName + '单位有' + obj[i].errorNum + '条撤回失败,原因:';
                                    for (var k = 0; k < obj[i].errorMsg.length; k++) {
                                        msg += obj[i].errorMsg[k][1] + '条' + bt.lang(obj[i].errorMsg[k][0])
                                    }
                                }
                            }
                        }
                        parent.layer.alert(msg, {icon: result.success ? 1 : 2});
                    }, 'JSON');
                });
            }
        };
        _config.buttons.push(revokeBtn);
    }

    //遍历按钮
    function eachBtns(arr, btn, colvisBtn) {
        bt.each(arr, function (i, v) {
            if (v == "colvis") {
                btn.push(colvisBtn);
            }
        })
    }

    //设置显示隐藏列数组方法 ,获取隐藏列索引,要求：第一列、固定列不能隐藏;
    TableList.prototype.colvisArray = function () {
        var that = this, _config = that.config, colvisArray = [];
        var columnsCount = _config.columns.length;//获取参数中列的数量;
        var fixedLeftColumnsCount = 1, fixedRightColumnsCount = 0;
        // debugger
        if (_config.fixedColumns) {
            fixedLeftColumnsCount = _config.fixedColumns.leftColumns; //获取左边固定列数量 并减1，代表从索引列开始不固定
            fixedRightColumnsCount = _config.fixedColumns.rightColumns;//获取右边固定列数量
            fixedRightColumnsCount = columnsCount - fixedRightColumnsCount;//获取从第fixedRightColumnsCount列开始后面所有列都固定
        }
        fixedLeftColumnsCount = fixedLeftColumnsCount ? fixedLeftColumnsCount : 1;//默认第一列不能隐藏
        fixedRightColumnsCount = fixedRightColumnsCount ? fixedRightColumnsCount : columnsCount;
        for (var i = 0; i < columnsCount; i++) {
            if (i > (fixedLeftColumnsCount - 1) && i < fixedRightColumnsCount) { //当i 小于左固件列 或者 i 小于右固定列 时，才能隐藏此索引
                if (false != _config.columns[i].visible) { // 过滤visible为false的列
                    colvisArray.push(i);
                }
            }
        }
        return colvisArray;
    }

    /*
        表格按钮功能：
    * */

    //批量撤回功能的 匹配url方法
    function matchUrl(url) {

        var href = window.location.href;
        if (href.indexOf('flowStatus=track') == -1) {
            return
        }
        var pathname = window.location.pathname;
        var urlArr = top.bt.tableUrl;
        if (!urlArr) { // 判断bt.tableUrl 是否存贮了所有需要添加批量撤回功能url集合
            //通过方法去获取
            var url = basePath + '/bpm/flowCategory/revokableUrl';
            $.get(url, function (res) {
                top.bt.tableUrl = res;
                // 匹配传入的url 为选项卡中的url
                urlArr = res;
                for (var i = 0; i < urlArr.length; i++) {
                    if (pathname.indexOf(urlArr[i]) !== -1) {
                        return true;
                    }
                }
                return false;
            }, "json")
        } else {
            for (var i = 0; i < urlArr.length; i++) {
                if (pathname.indexOf(urlArr[i]) !== -1) {
                    return true;
                }
            }
            return false;
        }
    }

    TableList.prototype.saveTableStatusOnOff = function () {
        var that = this;
        var buttonDiv = $(that.config.elem).parents('.dataTables_wrapper').children('.dt-buttons');
        var onOffBtn = '<div class="layui-inline fr layui-form"><input type="checkbox" name="close" value="1" lay-skin="switch"  lay-filter="switchSave" lay-text="ON|OFF"></div>';
        buttonDiv.append(onOffBtn);
        form.render();
        //监听指定开关
        form.on('switch(switchSave)', function (data) {
            var _this = this;
            var flag = _this.checked ? 'true' : 'false';
            var localKey = that.config.stateKey ? that.config.stateKey :'DataTables_' + $(this).parents('.dataTables_wrapper').attr('id').split('_')[0];
            var tableKey = that.getTableKey(localKey);
            // var localTableStatus = JSON.stringify(store.get(tableKey));
            var storeStatus = store.get(tableKey);
         //   if (localTableStatus && !$(this).parents('.dataTables_wrapper').find('.dataTables_empty').length) {
            if (that.config.stateKey || storeStatus) {
                if (flag == 'true') {
                    layer.confirm(bt.lang("common.table.format.needs.save"), {
                        btn: [bt.lang("common.button.save"), bt.lang("common.button.reset")], //按钮
                        // skin: 'layui-layer-lan',
                    }, function () {
                        if(storeStatus.ColReorder && storeStatus.ColReorder.length){
                            // var columns = [];
                            // for (var i = 0; i < storeStatus.ColReorder.length; i++) {
                            //     var colIndex = storeStatus.ColReorder[i]
                            //     if(colIndex === i){
                            //         columns.push(storeStatus.columns[i]);
                            //     }else{
                            //         for (var j = 0; j < storeStatus.ColReorder.length; j++) {
                            //             var colIndex2 = storeStatus.ColReorder[j]
                            //             if( colIndex2 === i ){
                            //                 columns.push(storeStatus.columns[j]);
                            //                 break
                            //             }
                            //         }
                            //     }
                            // }
                            // storeStatus.columns = columns;
                        }

                        var tableStatus = {
                            tableKey: tableKey, content: JSON.stringify(storeStatus)
                        }


                        if (tableStatus) { //
                            $.ajax({
                                url: basePath + '/sys/user/tableRecord/change',
                                dataType: 'json',
                                type: "POST",
                                data: tableStatus,
                                success:function (res) {
                                    if(res.success){
                                        localStorage.setItem(localKey, JSON.stringify(storeStatus));
                                        layer.msg(bt.lang("common.table.format.already.save"), {
                                            time: 1000
                                        });
                                    }
                                }
                            });
                        }

                    }, function () {
                        $.ajax({
                            url: basePath + '/sys/user/tableRecord/reset',
                            dataType: 'json',
                            type: "get",
                            data: {tableKey: tableKey}
                        });
                    });
                }
            }
            setTimeout(function () {
                $(_this).prop('checked', false);
                form.render('checkbox');
                flag = false;
            }, 3000)
        });
    }
    //表格完成加载绘制完成后执行此方法
    TableList.prototype.tableInitComplete = function (ops) {
        var that = this;
        var _config = that.config;
        var btInitComplete = _config.btInitComplete;
        if (_config.initComplete === undefined) {
            _config.initComplete = function (setting, json) {
                //公共的内容;
                var $table = $(_config.elem);//表格

                var tableScroll = $table.parents('.dataTables_scroll');
                var tableWrapper = $table.parents('.dataTables_wrapper');
                $('.dataTable').show();
                tableWrapper.show();
                that.clickRowCallback(setting, json);
                //向服务器获取汇总信息
                that.getAllSummary();
                if (!_config.mulselect) {
                    form.render();
                }
                //给表格注册hover样式
                if (!_config.rowsGroup || !_config.rowsGroup.length) { //全并单元格时，不能有hover事件
                    if (_config.summary && _config.summary.position && _config.summary.position == 'top') {
                    } else {
                        that.onHover();
                    }
                }
                //if(_config.resize){ //当编辑时或双表头时 为false;
                //    tableWrapper.addClass('dataTables-resize');
                //}
                if (_config.scrollY) {
                    tableWrapper.addClass('dataTables-height');
                }

                if (_config.summary) {
                    tableWrapper.addClass('dataTables-summary');
                } else {
                    tableWrapper.addClass('dataTables-normal');
                }

                if (that.config.buttonsLayout) {
                    if (that.config.ignoreButtons.indexOf('state') == -1) {
                        that.saveTableStatusOnOff(); //设置表格保存功能
                    }
                }
                if (btInitComplete !== undefined && typeof (btInitComplete) === "function") {
                    btInitComplete(setting, json);
                }
                //if(_config.fixedColumns){
                //    if(!_config.fixedColumns.rightColumns){
                //        var scrollWidth = tableScroll.width();
                //        var elemWidth = $table.width();
                //        if(scrollWidth != elemWidth){ //判断是否有滚动条，如果有，就添加padding
                //            //tableScroll.css('padding-right','1px');
                //        }
                //    }
                //}
                //if(_config.paging){
                //that.setTableInformation(setting);//设置表格底部翻页功能
                //$('.wrapper').attr('id','tablePage')
                //
                //laypage.render({
                //    elem: 'tablePage'
                //    ,count: 100
                //    ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
                //    ,jump: function(obj){
                //        console.log(obj)
                //    }
                //});

                //};
                if (_config.summary && _config.summary.sumList) { //将汇总的sumlist存在window中
                    that.saveSumlistExpression(setting, _config.summary.sumList);
                }
                //新的分页
                if (_config.paging) {
                    that.jumpPage(tableWrapper, setting);
                }
                // 如果是页面为多表格，则将实例例存贮在页面上
                if (that.isMultiTables) {
                    $table.data('table', that)
                }
            }
        }
    }

    //获取当前所有表格
    function getTables(elem) {
        var that = this;
        var topTableWrap = $(elem).parents('.dataTables_wrapper');
        return topTableWrap.find('table.dataTable');
    }

    //给表格注册hover样式
    TableList.prototype.onHover = function (elem) {
        var that = this;
        $(document).on(
            {
                mouseenter: function () {
                    var trIndex = $(this).index();
                    if ($(this).parent('thead').length) {
                        return;
                    }
                    var tables = getTables(this);
                    tables.each(function (index) {
                        $(this).find("tbody tr:eq(" + trIndex + ")").addClass("hover")
                    });
                },
                mouseleave: function () {
                    var trIndex = $(this).index();
                    var tables = getTables(this);
                    tables.each(function (index) {
                        $(this).find("tbody tr:eq(" + trIndex + ")").removeClass("hover")
                    });
                }
            }, ".dataTables_wrapper tr");
    }
    //表头渲染完成的回调试
    TableList.prototype.tableHeaderCallback = function () {
        var that = this;
        var _config = that.config;
        var btHeaderCallback = _config.btHeaderCallback;
        if (_config.headerCallback === undefined) {
            _config.headerCallback = function (thead, data, start, end, display) {
                if (_config.checkbox && _config.mulselect) {
                    if (!$(thead).find('th').eq(0).find('input').length) { //表单中没有全选框，就添加上
                        if(_config.isLayIgnore){
                            $(thead).find('th').eq(0).html('<input type="checkbox" lay-skin="primary" lay-ignore class="checkAll">').addClass('headerCheckAll');
                        }else{
                            $(thead).find('th').eq(0).html('<input type="checkbox" lay-skin="primary" lay-filter ="' + that.filterValue.allSelectFiliter + '" class="checkAll">').addClass('headerCheckAll');
                        }
                    }
                }
            }
        }
    }
    //表格重绘后执行的方法
    TableList.prototype.tableDrawCallback = function (ops) {
        var that = this;
        var _config = that.config;
        var btDrawCallback = _config.btDrawCallback;
        if (_config.drawCallback === undefined) {
            // if(btDrawCallback !==undefined && typeof(btDrawCallback) === "function"){
            _config.drawCallback = function (setting) {
                //公共的内容;
                var $table = $(_config.elem);//表格
                var tableScroll = $table.parents('.dataTables_scroll');
                var tableWrapper = $table.parents('.dataTables_wrapper');
                tableWrapper.show();
                $('.dataTable').show();
                var table = new $.fn.dataTable.Api(setting);
                if (_config.checkbox) {
                    //表格全选时，页面刷新后，全选为false
                    that.checkboxAllselectRefresh(false);
                }

                //重绘时，将汇总信息添加到页面tbody中
                var api = this.api();
                var isSummary = tableWrapper.find('.dataTables_scrollBody .btsummary').length > 0 ? true : false;
                if (!isSummary) { //当设置了rowsGroup时，会多添加一次，所以添加判断
                    that.doSumData(api);
                }
                //回调方法
                if (btDrawCallback !== undefined && typeof (btDrawCallback) === "function") {
                    btDrawCallback(setting);
                }
                if (that.config.resize) { //当编辑时或双表头时 为false;
                    tableWrapper.addClass('dataTables-resize');
                }
                //跨页选择时重绘设置
                if (that.config.jumpPageSelect) {
                    var currentPageData = table.rows().data();//获取当前页面全部数据
                    var currentPageTrsLength = currentPageData.length;//
                    currentPageData = currentPageData.splice(0, currentPageTrsLength);//截取数据
                    var selectId = that.selectId;
                    var selectedStyle = that.selectedStyle;
                    for (var i = 0; i < selectId.length; i++) {
                        var id = selectId[i].id;
                        for (var j = 0; j < currentPageData.length; j++) {
                            if (id && id == currentPageData[j].id) {
                                var input = $('[data-id="' + id + '"]');
                                input.prop('checked', true);
                                input.parents('tr').addClass(selectedStyle);
                                break
                            }
                        }
                    }

                    var checkChildren = tableScroll.find('input.checkchild');
                    var checkedChildren = tableScroll.find('input.checkchild:checked');
                    if (checkChildren.length == checkedChildren.length) {
                        that.checkboxAllselectRefresh(true);
                    }
                }
                $table.closest('.DTFC_ScrollWrapper').css('margin-bottom', '0px');
                // 无数据时，禁用全选按钮
                if(!that.isMultiTables){
                    if(table.rows().data() && table.rows().data().length){
                        $('.checkAll').prop('disabled',false)
                    }else{
                        $('.checkAll').prop('disabled',true)
                    }
                }else{ // 多表格时，需要找到对应的表格复选框设置禁用
                    var $tableContainer = $(table.table().container())
                    if($tableContainer){
                        if(table.rows().data() && table.rows().data().length){
                            $tableContainer.find('.checkAll').prop('disabled',false)
                        }else{
                            $tableContainer.find('.checkAll').prop('disabled',true)
                        }
                    }
                }

                //解决查询之后样式异常的问题
                if(!_config.notAdjustColumns){
                    form.render();
                    $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
                }
                //updatePages($table,setting);

                if (_config.resize) { //双表头/编辑表格时不设置此属性
                    if (!$table.find('.dataTables_empty').length) {
                        var cols = setting.aoColumns;
                        // var tableTds = $(_config.elem).find('tbody tr:first-child').find('td');
                        var tableTds = $table.parents('.dataTables_scrollBody').siblings('.dataTables_scrollHead').find('thead tr:first-child').find('th');
                        // if(cols && !tableTds.hasClass('dataTables_empty') && !$(_config.elem).css('table-layout') == 'fixed' ){
                        var colWidth = 0;
                        if (cols && !tableTds.hasClass('dataTables_empty') && that.drawItem === 0) {
                            bt.each(cols, function (index, col) {
                                // if(!col.sWidth || !col.sWidthOrig){
                                if (col.sWidth || !col.sWidthOrig) {
                                    colWidth += parseFloat(col.sWidth)
                                    // col.sWidthOrig = col.sWidth = $(tableTds[index]).width() + 'px';
                                    col.sWidthOrig = col.sWidth;//解决数据很少时出现水平滚动条的问题
                                }
                            })
                            setting.aoColumns = cols;
                        }
                        var tables = tableScroll.find('table');
                        if(!_config.notAdjustColumns){
                            api.tables({visible: true, api: true}).columns.adjust();
                        }

                        //clearTimeout(timer)
                        //timer =  setTimeout(function(){
                        bt.each(tables, function () {
                            if ($(this).css("table-layout") != "fixed") {
                                $(this).css(
                                    {
                                        'table-layout': 'fixed',
                                        'outline-style': 'none'
                                    }
                                );
                            }

                        })
                        tableWrapper.addClass('dataTables_colResize');
                        //},300);
                    }
                }
                //解决选不同分页时，当滚动水平滚动条时，出现的表头与表体对不齐的问题
                if (tableWrapper.find('.DTFC_RightHeadBlocker').is(':visible')) {
                    tableWrapper.find('.dataTables_scrollHeadInner').css('padding-right', '17px');
                }
                //if(that.drawItem > 0 ){
                //    adjustHeight($table,setting);
                //    //当出现垂直滚动条时，样式增加padding-right 1PX，解决滚动条外没有边框线的问题
                //    var scrollY = parseFloat(_config.scrollY);
                //    var elemHeight = $table.height();//表体高度
                //    var elemWidth = $table.width();//表体宽度
                //    var scrollWidth = tableScroll.width();
                //    if(scrollWidth < elemWidth || scrollY < elemHeight ){ //如果有水平/垂直滚动条，则需要添加样式
                //        tableScroll.css('padding-right','1px');


                //    }
                //}
                //
                //在表格底部插入信息回调方法
                if (_config.insertInformation !== undefined && typeof (_config.insertInformation) === "function") {
                    var html = _config.insertInformation(setting);
                    $(".data_summary").html(html);
                } else if (_config.insertInformation) { //true时，代表从后台返回数据中获取
                    $(".data_summary").html(setting.json.insertInformation);
                }
                //解决查询之后样式异常的问题
              //  $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
                if(table.rows().data() && table.rows().data().length){
                    setTimeout(function(){
                        if (document.createEvent) {
                            var event = document.createEvent('HTMLEvents')
                            event.initEvent('resize', true, true)
                            window.dispatchEvent(event)
                        } else if (document.createEventObject) {
                            window.fireEvent('onresize')
                        }
                    })
                }
                //清空跳转页
                var infoObj = table.page.info();
                var currentPage = infoObj.page;
                var inputPage = $(that.config.elem+"_wrapper").find(".table_input_page").val();
                if (currentPage+1!=inputPage){
                    $(".table_input_page").val("");
                }
            }
        }
    }

    //function adjustHeight(elem,setting) {
    //    var initHeight = setting.oInit.scrollY;
    //    var floatHeight = parseFloat('iniHeight');
    //    var bodyHeight = parseFloat(elem.parent().css('height'));
    //    var bodyWidth = parseFloat(elem.css('width'));
    //    var bodyWrapperWidth = parseFloat(elem.parent().css('width'));
    //    if( bodyWidth + 1 < bodyWrapperWidth ){
    //        if(( floatHeight + 18 ) === bodyHeight ){
    //            elemparent().css('height',initHeight);
    //        }
    //    }
    //}
    //function updatePages(elem,setting) {
    //    var wraper = elem.parents('.dataTables_wrapper').find('.wrapper');
    //    var table = new $.fn.dataTable.Api( setting );
    //    var infoObj = table.page.info();
    //    var pages = 1;
    //    if(infoObj.pages == 0){
    //        pages = 1;
    //    }else{
    //        if(!isNaN(infoObj.pages)){
    //            pages = infoObj.pages;
    //        }
    //    }
    //    var start = 0;
    //    var end = infoObj.end;
    //    if( end== 0 || isNaN(end)){ //无数据，start为0
    //        start = 0;
    //        end = 0;
    //    }else{
    //        start = infoObj.start - 0 + 1;
    //    }
    //    var recordsDisplay = infoObj.recordsDisplay;
    //    if( isNaN(recordsDisplay) ){
    //        recordsDisplay = 0;
    //    }
    //    wraper.find('.table-pageInput .totalPage').text( pages );
    //    wraper.find('.table-record .table-endRecord').text( infoObj.end );
    //    wraper.find('.table-record .table-totalRecord').text( recordsDisplay );
    //    //如果重新查询之后，将回到第一页，所以需要更新如下数据
    //    if( infoObj.start === 0  && wraper[0] ){
    //        wraper.find('.table-record .table-startRecord').text( start );
    //        wraper.find('.table-pageNumber').val(1);
    //        updatePageStyle(wraper.find('.table-pageInput span'),0,table);
    //    }
    //}
    ////表格底部页码初始化
    //TableList.prototype.setTableInformation = function (settings) {
    //    var that = this;
    //    var lengthMenu = that.config.lengthMenu;//每页显示多少条信息，array
    //    var table = new $.fn.dataTable.Api( settings );
    //    var isInitData = that.config.isInitData;
    //    //获取表格的当前页码(page)、所有页码(pages)、数据显示的条数(recordsDisplay)，所有条数(recordsTotal)、每页条数（length），起始行索引（start）
    //    var infoObj = table.page.info();
    //    var len = table.page.len();
    //    //var toolsDiv = $('.wrapper');
    //    var toolsDiv = $(that.config.elem).parents('.dataTables_scroll').siblings('.wrapper');
    //    var pages = 1;//总页码
    //    if( infoObj.pages != 0 && !isNaN( infoObj.pages )){
    //        pages = infoObj.pages;
    //    }
    //    var end = 0;//结束条数
    //    var recordsDisplay = 0;//表格总条数
    //    if(isInitData){
    //        if( !isNaN(infoObj.end)){ //解决后台返回的数据为NaN的问题
    //            end = infoObj.end;
    //            recordsDisplay = infoObj.recordsDisplay;
    //            if(end < 0){
    //                end = 0;
    //                recordsDisplay = 0;
    //            }
    //        }
    //    }
    //    var hoverMouse = infoObj.page === 0 ? 'firstPage':'table-jumpPage';
    //    var lastMouse = pages === 1 ? 'firstPage':'table-jumpPage';
    //    var start = 0;
    //    if( end != 0){
    //        start = infoObj.start - 0 + 1;
    //    }
    //
    //    //页码html
    //    var html = '';
    //    html += '<span class="table-page">';
    //    html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-firstPage pageLeft pages span fa fa-fw fa-step-backward spanLeft '+ hoverMouse+'" title="First Page" ></span>';
    //    html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-prevPage pageLeft pages span fa fa-fw fa-backward spanLeft '+ hoverMouse+'" title="Previous Page"></span>';
    //    html += '<span class="text-explode">|</span>'
    //    html += '<span class="table-pageInput span"><span style="margin-right: 5px">第</span>';
    //    html += '<input class="table-pageNumber" value="'+ ( infoObj.page + 1) +'" > of';
    //    html += '<span class="pd2 totalPage" style="padding-left: 5px;max-width: 45px;">' + pages +'</span></span>';
    //    html += '<span class="text-explode">|</span>';
    //    html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-nextPage pageRight pages span fa fa-fw fa-forward '+lastMouse+'" title="Next Page"></span>';
    //    html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-lastPage pageRight pages span fa fa-fw fa-step-forward '+lastMouse+'" title="Last Page"></span>';
    //    html += '<select lay-ignore class="table-length">';
    //    for(var i = 0; i < lengthMenu.length; i++){
    //        if(len === lengthMenu [i]){
    //            html += '<option selected>';
    //        }else{
    //            html += '<option>';
    //        }
    //        html += lengthMenu[i] +'</option>';
    //    }
    //    html += '</select>';
    //    html += '</span>';
    //    //数据条数html
    //    var recordHtml = '';
    //    recordHtml += '<div class="table-record fr">';
    //    recordHtml += '<span class="pd2">查看</span>';
    //    recordHtml += '<span class="table-startRecord">'+ (  isInitData ? start:0) +'</span>';
    //    recordHtml += '<span class="pd2">-</span>';
    //    recordHtml += '<span class="table-endRecord">'+end+'</span>';
    //    recordHtml += '<span class="pd2">条记录 ,</span>';
    //    recordHtml += '<span class="pd2">共</span>';
    //    recordHtml += '<span class="table-totalRecord">'+ recordsDisplay +'</span>';
    //    recordHtml += '<span class="pd2">条</span>';
    //    toolsDiv.append(recordHtml);
    //    toolsDiv.append(html).addClass('pl');
    //    that.setTableInformationEvent(settings,toolsDiv);
    //}
    ////表格底部页码初始化注册方法
    //TableList.prototype.setTableInformationEvent = function (settings,toolsDiv) {
    //    var table = new $.fn.dataTable.Api( settings );
    //    tablePageInput(toolsDiv);//保证输入的是正整数
    //    tableJumpPageEnter(table,toolsDiv); //注册回车跳转事件
    //    tableJumpPageClick(table,toolsDiv);//注册点击跳转事件
    //    tableClickLengthMenu(table,toolsDiv)//注册点击下拉框事件
    //}
    //
    ////控制只能输入正整数
    //function tablePageInput(toolsDiv){
    //    //$('.dataTables_wrapper .wrapper input').on('keyup',function(){
    //    toolsDiv.find('input').on('keyup',function(){
    //        if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}
    //    }).on('afterpaste',function(){
    //        if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')};
    //    })
    //
    //}
    ////按回车键页面跳转
    //function tableJumpPageEnter(table,toolsDiv){
    //    //$("body").on('keydown','.table-pageNumber',function(e){
    //    toolsDiv.on('keydown','.table-pageNumber',function(e){
    //        e = event || windw.event;
    //        if(e.keyCode ===13){
    //            var $this = $(this);
    //            var val = parseInt($this.val());
    //            if(val>=0){
    //                var total = $this.siblings('.totalPage').text();
    //                if( val > total){ //当输入的数字大于总页面数字时
    //                    val = total;
    //                    $this.val(val);
    //                }
    //                val = val - 1;
    //                table.page(val).draw('page');
    //                updateRecord(table,$(this));//更新表格下方显示起止信息条数
    //                updatePageStyle( $(this),val,table );//更新首页码样式
    //            }
    //            $(this).blur();
    //        }
    //    })
    //}
    ////翻页跳转
    //function tableJumpPageClick(table,toolsDiv) {
    //    //$('.dataTables_wrapper .wrapper').on('click','.table-jumpPage',function () {
    //    toolsDiv.on('click','.table-jumpPage',function () {
    //        var title = $(this).attr('title');
    //        var currentPage = $(this).siblings('.table-pageInput').find('input').val() - 0 - 1;
    //        var totalPage =  $(this).siblings('.table-pageInput').find('.totalPage').text() - 0 - 1;
    //        var pageNumber = 0;
    //        switch (title){
    //            case 'First Page':
    //                pageNumber = 0;
    //                break;
    //            case 'Previous Page':
    //                pageNumber = currentPage - 1;
    //                break;
    //            case 'Next Page':
    //                if(currentPage < totalPage){
    //                    pageNumber = currentPage + 1;
    //                }else{
    //                    pageNumber = currentPage;
    //                }
    //                break;
    //            case 'Last Page':
    //                pageNumber = totalPage;
    //                break;
    //            default:
    //                break;
    //        }
    //        table.page(pageNumber).draw('page');
    //        $(this).siblings('.table-pageInput').find('input').val( pageNumber + 1);
    //        updatePageStyle( $(this),pageNumber,table );//更新首页码样式
    //        updateRecord(table,$(this));//更新表格下方显示起止信息条数
    //    })
    //}
    ////点击下拉框选择每页显示多少条数据
    //function tableClickLengthMenu(table,toolsDiv) {
    //    //$('.dataTables_wrapper .wrapper').on('change','.table-length',function () {
    //    toolsDiv.on('change','.table-length',function () {
    //        var value = $(this).val();
    //        // var table = new $.fn.dataTable.Api( settings );
    //        table.page.len( value ).draw();
    //        $(this).siblings('.table-pageInput').find('input').val( 1 );//重新选择时，回到第一列车
    //        updateRecord(table,$(this));//更新表格下方显示起止信息条数
    //        updatePageStyle($(this),0,table);
    //    })
    //}
    ////更新表格下方显示起止信息条数
    //function updateRecord(table,elem) {
    //    //获取表格的当前页码(page)、所有页码(pages)、数据显示的条数(recordsDisplay)，所有条数(recordsTotal)、每页条数（length），起始行索引（start）
    //    var infoObj = table.page.info();
    //    var wrapper = elem.parents('.wrapper');
    //    if( infoObj ){
    //        var pages = 0;
    //        if( infoObj.pages == 0 ){
    //            pages = 1;
    //        }else{
    //            pages = infoObj.pages;
    //        }
    //        var start = 0;
    //        if(infoObj.start == 0){
    //            start = 1;
    //        }else{
    //            start = infoObj.start - 0 + 1;
    //        }
    //        var end = 0;
    //        if(infoObj.end == 0){
    //            end = 0;
    //            start = 0;
    //        }else{
    //            end = infoObj.end;
    //        }
    //        wrapper.find('.table-record .table-startRecord').text( start );
    //        wrapper.find('.table-record .table-endRecord').text( end );
    //        wrapper.find('.table-pageInput .totalPage').text( pages );
    //        wrapper.find('.table-record .table-totalRecord').text( infoObj.recordsDisplay );
    //    }
    //}
    ////更新首页或前一页的样式
    //function updatePageStyle(elem,number,table) {
    //    var wrapper = elem.parents('.wrapper');
    //    var jump = 'table-jumpPage';
    //    var nonJump = 'firstPage';
    //    number += 1;
    //    var infoObj = table.page.info();
    //    var pages = infoObj.pages;
    //    wrapper.find('.pages').addClass(nonJump).removeClass(jump);
    //    if(pages === 1){
    //        return;
    //    }else{
    //        if( number !== 1 ){
    //            wrapper.find('.pageLeft').removeClass(nonJump).addClass(jump);
    //            if( number !== pages ){
    //                wrapper.find('.pageRight').removeClass(nonJump).addClass(jump);
    //            }
    //        }else{
    //            if(pages != 0){ //表格无数据时，右边的向下键也禁用
    //                wrapper.find('.pageRight').removeClass(nonJump).addClass(jump);
    //            }
    //        }
    //    }
    //}
    //重新渲染全选按钮
    TableList.prototype.checkboxAllselectRefresh = function (status) {
        var that = this, _config = that.config;
        var elem = _config.elem;
        var parent = $(elem).parents('.dataTables_scroll').find('.checkAll');
        parent.each(function (index, item) {
            item.checked = status;
        });
        //当出现左边固定列/多选时,清除全选复选框
        if (_config.fixedColumns && _config.checkbox && _config.mulselect) {
            $(elem).parents(".DTFC_ScrollWrapper").find('.DTFC_LeftWrapper .DTFC_LeftHeadWrapper .checkAll').prop('checked', status);
        }
    }
    //将　汇总中的expression方法存在window中，此方法在导有所有汇总时要用
    TableList.prototype.saveSumlistExpression = function (settings, expression) {
        var that = this;
        var tableKey = that.getTableKey('DataTables_' + settings.sInstance);
        if (!window.sumCallback) {
            window.sumCallback = [];
        }
        if (!window.sumCallback[tableKey]) {
            window.sumCallback[tableKey] = [];
        }
        $.each(expression, function (i, item) {
            if (item.expression) {
                window.sumCallback[tableKey][item.dataName] = {expression: item.expression};
            }
        })
    }
    //页面数据汇总处理 isUpdate 是从updateSummary的标识，有此标识，代表汇总html不用添加
    TableList.prototype.doSumData = function (api, isUpdate) {
        var _config = this.config, that = this;
        //添加汇总html
        if (_config.summary && typeof (_config.summary) === 'object') {
            var summary = _config.summary;
            var $table = $(_config.elem);
            var api = api;
            var money = bt.money;
            var options = {
                //decimal: summary.format !== undefined ? 2 : summary.format;
                decimal: (summary.format !== undefined && summary.format.decimal !== undefined) ? summary.format.decimal : 2
            };
            var groupColumn = summary.groupColumn;//分类汇总字段列
            var sumList = summary.sumList;
            var position = summary.position;
            var convertMoney = summary.convertMoney;//万元或者元参数
            var outerData = summary.outerData;
            var natualCurrency = summary.natualCurrency;//将结果转换成本位货参数
            var colsNamesArr = api.columns().dataSrc();//拿到每一列的数据名称;
            var customSum = summary.customSum;
            for (var k = 0; k < sumList.length; k++) {//更新sumlist汇总列清单列索引
                //列托动后，刷新列的index
                for (var a = 0; a < colsNamesArr.length; a++) {
                    if (sumList[k].dataName == colsNamesArr[a]) {
                        sumList[k].colNum = a;
                    }
                }
            }
            if (!groupColumn) { //非分类汇总
                if (!isUpdate) { //　true时，页面不添加html
                    var sumHtml = that.getSumHtml([], api);
                    if (position != undefined && position == 'top') {
                        $table.find("tbody").prepend(sumHtml);
                    } else {
                        $table.find("tbody").append(sumHtml);
                    }
                }
                bt.each(sumList, function (i, v) {
                    var total, pageTotal, valueForPage, valueForselected, valueForAll;
                    if (!v.expression) {
                        if (!outerData) {
                            total = api
                                .column(v.colNum)
                                .data()
                                .reduce(function (a, b) {
                                    return intVal(a) + intVal(b);
                                }, 0);

                            pageTotal = api
                                .column(v.colNum, {page: 'current'})
                                .data()
                                .reduce(function (a, b) {
                                    return intVal(a) + intVal(b);
                                }, 0);
                            i = 0;
                            //金额转换成万元或者元
                            if (typeof (convertMoney) === 'function') {
                                pageTotal = money.convertMoney(pageTotal, convertMoney());
                                total = money.convertMoney(total, convertMoney());
                            } else if (typeof (convertMoney) === 'string') {
                                pageTotal = money.convertMoney(pageTotal, convertMoney);
                                total = money.convertMoney(total, convertMoney);
                            }
                            if (v.suffix == '%') {//百分比时，不能大于100
                                valueForPage = valueForPage > 100 ? 100 : valueForPage;
                                valueForselected = valueForselected > 100 ? 100 : valueForPage;
                                valueForAll = valueForAll > 100 ? 100 : valueForPage;
                            }
                            if (v.money != false) { //非货币汇总不需要格式化
                                valueForPage = money.format(pageTotal, options);
                                valueForselected = money.format(0, options);
                                valueForAll = money.format(total, options);
                            } else {
                                valueForPage = pageTotal;
                                valueForselected = 0;
                                valueForAll = total;
                            }
                            //others:'%'//‰千分百分汇总
                            if (v.suffix) { //如果有特殊要求后缀
                                valueForPage = valueForPage + v.suffix;
                                valueForselected = valueForselected + v.suffix;
                                valueForAll = valueForAll + v.suffix;
                            }
                            $table.find("tbody td.pageSum" + v.dataName + i).html(valueForPage).attr('title', valueForPage);//添加title属性
                            $table.find("tbody td.selectSum" + v.dataName + i).html(money.format(0, options)).attr('title', valueForselected);
                            $table.find("tbody td.allSum" + v.dataName + i).html(money.format(total, options)).attr('title', valueForAll);

                        } else { //当配置了此参数，代表汇总数据需要从页面上取才可以汇总，因为需要汇总的数据进行了二次加工,暂时不做本位币的转换
                            var vName = v.dataName;
                            var sum = 0;
                            var col = v.colNum;
                            var trs = $table.find('.selected');
                            bt.each(trs, function (i, tr) { //选中小计
                                var td = $(tr).find('td:eq(' + col + ')');
                                if (td.find('input').length || td.find('.changeful').length) { //changeful样式的标签内的内容是多变的，计算时要重新获取
                                    var outerValue = 0;
                                    if (td.find('input').length) {
                                        outerValue = td.find('input').val();
                                    } else if (td.find('.changeful').length) {
                                        outerValue = td.find('.changeful').text();
                                    }
                                    sum = sum + (intVal(money.toNumber(outerValue)) - 0);//对数据进行格式化
                                } else {
                                    sum = sum + (intVal(td.text()) - 0);//对数据进行格式化
                                }
                            })
                            //本页总计
                            var allTrs = $table.find('tbody tr:not(.btsummary)');
                            var pageSum = 0;
                            var isRefreshPageSum = false;
                            bt.each(allTrs, function (i, tr) { //选中小计
                                var td = $(tr).find('td:eq(' + col + ')');
                                if (td.find('input').length || td.find('.changeful').length) {

                                    isRefreshPageSum = true;
                                    var outerSumValue = 0;
                                    if (td.find('input').length) {
                                        outerSumValue = td.find('input').val();
                                    } else if (td.find('.changeful').length) {
                                        outerSumValue = td.find('.changeful').text();
                                    }
                                    pageSum = pageSum + (intVal(money.toNumber(outerSumValue)) - 0);//对数据进行格式化
                                } else {
                                    pageSum = pageSum + (intVal(td.text()) - 0);//对数据进行格式化
                                }
                            })
                            /**
                             * 汇总设置了outerData 为true时，初始化时，页面上无input或者　无className changeful，就会取td.text()，这些数据都会参加小计汇总
                             * 当 isUpdate　为true时，代表调试了手动刷新updateSummary方法，刷新汇总数据时，只对出现 input 或 changeful的列进行刷新
                             * */
                            if (!isUpdate || isRefreshPageSum) {
                                pageSum = money.format(pageSum, options);
                                $(that.config.elem).find("tbody .pageSum" + v.dataName + '0').text(pageSum).attr('title', pageSum);
                            }
                        }
                    } else { //自定义汇总数据
                        var sumObj = {};
                        var tds = $table.find("tbody tr.pageSum0").find('.sumtd');
                        $.each(tds, function () {
                            var name = $(this).data('sum-name');
                            sumObj[name] = $(this).text();
                        })
                        valueForPage = v.expression(sumObj);//自定义
                        $table.find("tbody tr.pageSum0").find('.' + v.dataName).text(valueForPage).attr('title', valueForPage)
                    }
                })
            } else {//分类汇总
                var currenPageData = api.rows().data();
                var groupColumnIndex;
                for (var i = 0; i < colsNamesArr.length; i++) {
                    if (colsNamesArr[i] == groupColumn) {
                        groupColumnIndex = i;
                    }
                }
                var nodes = api.columns(groupColumnIndex).nodes();
                var groupColText = [];//分尖汇总列文本
                for (var i = 0; i < nodes[0].length; i++) {
                    groupColText.push($(nodes[0][i]).text())
                }

                var groupColData = api.columns(groupColumnIndex).data();//分类汇总页的数据名字
                //数组去重,得到需要汇总的定段
                groupColData = doDouble(groupColData[0]);
                groupColText = doDouble(groupColText)
                that.groupSummaryName = groupColData;
                if (!isUpdate) { //手动更新合计时，不用重新加天html
                    var sumHtml = that.getSumHtml(groupColText, api);//生成计算html
                    if (position != undefined && position == 'top') {
                        $table.find("tbody").prepend(sumHtml);
                    } else {
                        $table.find("tbody").append(sumHtml);
                    }
                }
                for (var k = 0; k < sumList.length; k++) {//按照sumlist汇总列清单进行遍历
                    var courtName = sumList[k].dataName;//计算列的字段名
                    var courtIndex = sumList[k].colNum;//计算列的索引
                    var colFormat = sumList[k].format;//当前列数据格式
                    if (!sumList[k].expression) { //非自定义汇总数据
                        var numFormat;
                        if (colFormat !== undefined) {
                            numFormat = {
                                decimal: colFormat
                            };
                        } else {
                            numFormat = options
                        }
                        var natualCurrencySum = 0;//本位币计算结果初始化为0
                        //遍历获取分类数值对象
                        var sumObj = getGroupData(groupColumn, courtName, currenPageData, groupColData, $table, courtIndex);//每一列的数据
                        //分类汇总
                        for (var key in sumObj) {
                            var sum = sumObj[key].reduce(function (a, b) {
                                return intVal(a) + intVal(b);
                            }, 0);
                            //金额转换成万元或者元
                            if (typeof (convertMoney) === 'function') {
                                sum = money.convertMoney(sum, convertMoney());
                            } else if (typeof (convertMoney) === 'string') {
                                sum = money.convertMoney(sum, convertMoney);
                            }
                            //本位币转换：将日元或美元转换成人民币
                            if (natualCurrency && typeof (natualCurrency) === 'function') {
                                var natual = natualCurrency();
                                if (natual) {
                                    var natualCurrencyFlag = true;//标识是否需要本位币换算;
                                    for (var i = 0; i < natual.length; i++) {
                                        var item = natual[i];
                                        if (key == item.currency) {
                                            var rate = item.rate;
                                            if (rate) {
                                                natualCurrencyFlag = false;//由于已经换算了本位币，所以标识重置为false;
                                                natualCurrencySum += money.mul(sum, rate);
                                            }
                                            break;
                                        }
                                    }
                                    if (natualCurrencyFlag) {//不用换本位币的直接相加
                                        natualCurrencySum += sum;
                                    }
                                }
                            }
                            for (var i = 0; i < groupColData.length; i++) {
                                if (key == groupColData[i]) { //按照i进行列赋值
                                    var sumForPage, sumForSelected;
                                    if (!sumList[k].expression) {
                                        if (sumList[k].suffix == '%') {//百分比时，不能大于100
                                            sumForPage = sumForPage > 100 ? 100 : sumForPage;
                                            sumForSelected = sumForSelected > 100 ? 100 : sumForSelected;
                                        }
                                        if (sumList[k].money != false) {
                                            sumForPage = money.format(sum, numFormat);
                                            sumForSelected = money.format(0, numFormat);
                                        } else {
                                            sumForPage = sum;
                                            sumForSelected = 0;
                                        }
                                        if (sumList[k].suffix) { //如果有特殊要求后缀
                                            sumForPage = sumForPage + sumList[k].suffix;
                                            sumForSelected = sumForSelected + sumList[k].suffix;
                                        }
                                        $table.find("tbody td.pageSum" + courtName + i).html(sumForPage).attr('title', sumForPage);//添加title属性
                                        $table.find("tbody td.selectSum" + courtName + i).html(sumForSelected).attr('title', sumForSelected);//添加title属性
                                    }
                                }
                            }
                            //给本位币内容赋值
                            var sumForCurrency = money.format(natualCurrencySum, numFormat);
                            $table.find("tbody td.natualCurrency" + courtName).html(sumForCurrency).attr('title', sumForCurrency);
                        }
                    } else { //自定义汇总内容
                        for (var i = 0; i < groupColData.length; i++) {
                            var sumObj = {};
                            var tds = $table.find('tbody .pageSum' + i).find('.sumtd');
                            $.each(tds, function () {
                                var name = $(this).data('sum-name');
                                sumObj[name] = $(this).text();
                            })
                            var sumForPage = sumList[k].expression(sumObj);//根据自定义回调方法返回值
                            $table.find("tbody").find('.pageSum' + i).find('.' + courtName).text(sumForPage).attr('title', sumForPage)
                        }
                    }
                }
            }
            for (var k = 0; k < sumList.length; k++) {
                if (customSum) { //自定汇总
                    var rowsData = api.rows().data();
                    rowsData = rowsData.splice(0, rowsData.length);
                    if (isFunction(customSum.sumCallback)) {
                        var sumForCustom = customSum.sumCallback(rowsData, sumList[k].dataName);
                        $table.find("tbody").find('.customSum0').find('.' + sumList[k].dataName).text(sumForCustom).attr('title', sumForCustom)
                    }
                }
            }
        }
    }

    //数组去重方法
    function doDouble(arr) {
        var res = [arr[0]];
        for (var i = 1; i < arr.length; i++) {
            var isOnly = false;
            for (var j = 0; j < res.length; j++) {
                if (arr[i] === res[j]) {
                    isOnly = false;
                    break;
                } else {
                    isOnly = true;
                }
            }
            if (isOnly) {
                res.push(arr[i]);
            }
        }
        return res;
    }

    //转number
    function intVal(i) {
        if (typeof i === 'string') { //当数字被格式化时
            i = i.replace(/,/g, '')
        }
        if ($.isNumeric(i)) { //其它非数字字符不处理
            return i - 0;
        } else {
            return 0;
        }

//        return typeof i === 'string' ?
//            i.replace(/[\$,]/g, '')*1 :
//            typeof i === 'number' ?
//                i : 0;
    }

    /**
     表格数据分类汇总
     @param {String} groupColName 数据的分类字段名称
     @param {String} keyName 汇总数值字段名称
     @param {Array} data 数据
     @param {Array} groupArray 分类汇总字段数组
     return {Object} 分类整理的数组对象
     */
    function getGroupData(groupColName, keyName, data, groupArray, $table, colIndex) {
        var sumObj = {};//分类存贮数值
        //遍历获取分类数值
        for (var j = 0; j < groupArray.length; j++) {
            var groupKey = groupArray[j];
            sumObj[groupKey] = [];
            var length = data.length;
            if (!length) {
                sumObj[groupKey].push(0)
            }
            for (var i = 0; i < length; i++) {
                var rowData = data[i];
                if (rowData[groupColName] == groupKey) {
                    //var num = $table.find('tbody tr:eq('+i+')').find('td:eq('+colIndex+')').text();
                    var num = rowData[keyName];//解决隐藏列汇总的问题
                    sumObj[groupKey].push(num);
//                    sumObj[groupKey].push(rowData[keyName])
                }
            }
        }
        return sumObj;
    }

    //设置汇总的html
    TableList.prototype.getSumHtml = function (groupNameArray, api) {
        var that = this;
        var _config = that.config,
            summary = _config.summary,
            sumList = summary.sumList,
            groupColumn = summary.groupColumn;//分类汇总参数
        var natualCurrency = summary.natualCurrency;//将结果转换成本位货参数
        var position = summary.position;
        var groupText = '';//分类汇总文本
        var thsLen = $(_config.elem).find('thead tr th').length;
        var footHtml = '', trhtml = '', sumArr = [];
        var isCheckbox = _config.checkbox; //是否第一列为chekbox
        var sumTextCol = summary.sumTextCol;//汇总文本列信息
        var colsNamesArr = api.columns().dataSrc();
        var colsVisible = api.columns().visible();//获取列的显示状态
        if (summary.selectSum) {
            sumArr.push("selectSum")
        }
        if (summary.pageSum) {
            sumArr.push("pageSum")
        }
        if (summary.allSum) {
            sumArr.push("allSum")
        }
        if (summary.customSum && isFunction(summary.customSum.text) && summary.customSum.text()) {
            sumArr.push("customSum")
        }
        if (!groupColumn) {
            groupNameArray = [0] //代表无分类汇总
        }

        //遍历生成汇总html
        for (var j = 0; j < groupNameArray.length; j++) {
            groupText = groupNameArray[j] ? groupNameArray[j] : '';
            bt.each(sumArr, function (i, v) {
                var isPageSum = false;//分页小计标识，应用于导出所有的分页小计
                if (v == "customSum") { //当为自定义汇总时，只添加一次且添加在最后面
                    if (j !== (groupNameArray.length - 1)) {
                        return
                    } else {
                        trhtml += '<tr class="btsummary  ' + v + '0">';
                    }
                } else {
                    trhtml += '<tr class="btsummary  ' + v + j + '">';
                }

                //for(var i = 0; i < colsNamesArr.length;i++){
                for (var i = 0; i < colsVisible.length; i++) {
                    var flag = true;
                    if (sumTextCol === undefined && !isCheckbox && i === 0 || sumTextCol !== undefined && i == sumTextCol || sumTextCol === undefined && isCheckbox && i === 1) {
                        var sumName = '';
                        if (v == "selectSum") {
                            if (isFunction(summary.createSummaryText)) { //如果有自定义汇总题目的
                                sumName = summary.createSummaryText(groupText, v);
                            } else {
                                sumName = (groupText + bt.lang('table.datatable.subtotalSelected'));
                            }
                        } else if (v == "pageSum") {
                            if (isFunction(summary.createSummaryText)) { //如果有自定义汇总题目的
                                sumName = summary.createSummaryText(groupText, v);
                            } else {
                                sumName = (groupText + bt.lang('table.adjustDetail.moneySum'));
                            }
                            isPageSum = true;//设置为true
                        } else if (v == "allSum") {
                            if (isFunction(summary.createSummaryText)) { //如果有自定义汇总题目的
                                sumName = summary.createSummaryText(groupText, v);
                            } else {
                                sumName = (groupText + bt.lang('table.fbsBudget.collect'));
                            }
                        } else if (v == "customSum") {
                            sumName = summary.customSum.text();
                        }
                        if (colsVisible[i]) { //汇总文本列出现隐藏,也要给加上display　none
                            trhtml += '<td align="center" >' + sumName + '</td>';
                        } else {
                            trhtml += '<td align="center" style="display:none">' + sumName + '</td>';
                        }
                    } else {
                        for (var a = 0; a < sumList.length; a++) {
                            //var dataName = sumList[a].dataName;
                            //if( dataName == colsNamesArr[i]){
                            //    var  vName = v + dataName +j+' '+ 'textRight';
                            //    trhtml +='<td class="'+ vName + ' '+ dataName +'"></td>';
                            //    flag = false;
                            //    break;
                            //}
                            if (sumList[a].colNum && sumList[a].colNum === i) {
                                var vName = sumList[a].money == false ? v + sumList[a].dataName + j + ' ' + 'center' : v + sumList[a].dataName + j + ' ' + 'textRight';
                                sumList[a].suffix = sumList[a].suffix || '';
                                sumList[a].money = sumList[a].money == false ? false : true;
                                var expression = sumList[a].expression ? 'expression' : '';
                                if (colsVisible[i]) {
                                    trhtml += '<td class=" sumtd ' + vName + ' ' + sumList[a].dataName + '"  data-suffix="' + sumList[a].suffix + '"  data-money="' + sumList[a].money + '" data-sum-name="' + sumList[a].dataName + '" data-sum-callback="' + expression + '"';
                                } else {//汇总列被隐藏时的问题
                                    trhtml += '<td class="sumtd ' + vName + ' ' + sumList[a].dataName + '" style="display:none;" data-suffix="' + sumList[a].suffix + '"  data-money="' + sumList[a].money + '" data-sum-name="' + sumList[a].dataName + '" data-sum-callback="' + expression + '"';
                                }

                                if (!isPageSum) {
                                    trhtml += '></td>'
                                } else { //如果有分页汇总，在html中添加分页汇总的内容，应用于导入所有数据时的汇总
                                    trhtml += 'data-sumpage-text ="' + groupText + '"></td>'
                                }
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if (isCheckbox && i === 0) {
                                trhtml += '<td style="width: 26px;height:18px;" class="extraCol"></td>';
                            } else {
                                if (colsVisible[i]) {
                                    if (colsNamesArr[i] == "null" || colsNamesArr[i] === null) {
                                        trhtml += '<td class="extraCol"></td>';
                                    } else {
                                        trhtml += '<td></td>';
                                    }
                                } else {
                                    if (colsNamesArr[i]) {
                                        trhtml += '<td style="display:none;"></td>';
                                    } else {
                                        trhtml += '<td style="display:none;" class="extraCol"></td>';
                                    }
                                }
                            }
                        }
                    }
                }
                trhtml += '</tr>';
            })
            footHtml += trhtml;
            trhtml = '';
        }
        //生成本位币html
        if (natualCurrency && typeof (natualCurrency) === 'function' && natualCurrency()) {
            var currencyHtml = '<tr class="btsummary">';
            for (var i = 0; i < colsVisible.length; i++) {
                var flag = true;
                if ((i === 0 && !_config.checkbox) || (_config.checkbox && i === 2) ) {
                    currencyHtml += '<td align="center">' + bt.lang('table.datatable.subtotalBaseCurrency') + '</td>';
                } else {
                    bt.each(sumList, function (index, item) {
                        var vName = 'natualCurrency' + item.dataName + ' ' + 'textRight';
                        if (i == item.colNum) {
                            currencyHtml += '<td class="' + vName + '"></td>';
                            flag = false;
                        }
                    })
                    if (flag) {
                        currencyHtml += '<td></td>';
                    }
                }
            }
            currencyHtml += '</tr>';
            if (position && position == 'top') {
                footHtml = currencyHtml + footHtml;
            } else {
                footHtml += currencyHtml;
            }
        }

        if(_config.summary && _config.summary.customSum && _config.summary.customSum.renderHtml){
            footHtml = _config.summary.customSum.renderHtml(footHtml)
        }
        return footHtml;
    }

    TableList.prototype.getTableHtml = function () {
        var that = this;
        var _config = that.config;
        var html = $(_config.elem).html();
        return html;
    }

    //设置选中数据汇总的方法 isUpdate是updateSummary方法传过来的标识，代表小计不用计算
    TableList.prototype.selectSumData = function (data, $table, isUpdate, currentRowData) {
        var sumList = this.config.summary.sumList, that = this;
        var groupColumn = this.config.summary.groupColumn;
        var money = bt.money;
        var options = that.config.summary.format;
        var outerData = this.config.summary.outerData;
        var convertMoney = this.config.summary.convertMoney;//金额转换成万元或者元
        if (!groupColumn) {
            bt.each(sumList, function (index, item) {
                var sumForSelect;
                var selectSumCallback = item.selectSumCallback; //选中汇总时回调方法
                if (selectSumCallback && typeof selectSumCallback === 'function') {
                    sumForSelect = selectSumCallback(currentRowData, data);
                } else {
                    var vName = item.dataName;
                    var sum = 0;
                    if (outerData) { //当配置了此参数，代表汇总数据需要从页面上取才可以汇总，因为需要汇总的数据进行了二次加工。
                        var col = item.colNum;
                        var trs = $table.find('.selected');
                        bt.each(trs, function (i, tr) { //选中小计
                            var td = $(tr).find('td:eq(' + col + ')');
                            if (td.find('input').length || td.find('.changeful').length) { //changeful样式的标签内的内容是多变的，计算时要重新获取
                                var outerValue = 0;
                                if (td.find('input').length) {
                                    outerValue = td.find('input').val();
                                } else if (td.find('.changeful').length) {
                                    outerValue = td.find('.changeful').text();
                                }
                                sum = sum + (intVal(money.toNumber(outerValue)) - 0);//对数据进行格式化
                            } else {
                                sum = sum + (intVal(td.text()) - 0);//对数据进行格式化
                            }
                        })
                        //本页总计
                        if (!isUpdate) { //　isUpdate为true时，不用计算小计
                            var allTrs = $table.find('tr:not(.btsummary)');
                            var pageSum = 0;
                            var isRefreshPageSum = false;
                            bt.each(allTrs, function (i, tr) { //选中小计
                                var td = $(tr).find('td:eq(' + col + ')');
                                if (td.find('input').length || td.find('.changeful').length) {

                                    isRefreshPageSum = true;
                                    var outerSumValue = 0;
                                    if (td.find('input').length) {
                                        outerSumValue = td.find('input').val();
                                    } else if (td.find('.changeful').length) {
                                        outerSumValue = td.find('.changeful').text();
                                    }
                                    pageSum = pageSum + (intVal(money.toNumber(outerSumValue)) - 0);//对数据进行格式化
                                } else {
                                    pageSum = pageSum + (intVal(td.text()) - 0);//对数据进行格式化
                                }
                            })
                            if (isRefreshPageSum) {
                                pageSum = money.format(pageSum, options);
                                $(that.config.elem).find("tbody .pageSum" + item.dataName + '0').text(pageSum).attr('title', pageSum);
                            }
                        }
                    } else {
                        bt.each(data, function (i, v) {
                            sum = sum + (v[vName] - 0);
                        })
                    }
                    index = 0;
                    //金额转换成万元或者元
                    if (typeof (convertMoney) === 'function') {
                        sum = money.convertMoney(sum, convertMoney());
                    } else if (typeof (convertMoney) === 'string') {
                        sum = money.convertMoney(sum, convertMoney);
                    }
                    sumForSelect = money.format(sum, options);
                }
                index = 0;
                $(that.config.elem).find("tbody .selectSum" + item.dataName + index).text(sumForSelect).attr('title', sumForSelect);
                var rightFixedCol = $(that.config.elem).parents('.dataTables_scroll').siblings('.DTFC_RightWrapper');
                if (rightFixedCol.length > 0) {
                    rightFixedCol.find('.DTFC_RightBodyWrapper').find("tbody .selectSum" + item.dataName + index).text(sumForSelect).attr('title', sumForSelect);
                }
            })
        } else {
            var groupArray = that.groupSummaryName;
            for (var j = 0; j < sumList.length; j++) {
                var courtName = sumList[j].dataName;//计算列的字段名
                var courtIndex = sumList[j].colNum;//计算列的索引
                //获取选择数据分类
                var sumObj = getGroupData(groupColumn, courtName, data, groupArray, $table, courtIndex);
                //分类汇总
                for (var key in sumObj) {
                    var sum = sumObj[key].reduce(function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0);
                    //金额转换成万元或者元
                    if (typeof (convertMoney) === 'function') {
                        sum = money.convertMoney(sum, convertMoney());
                    } else if (typeof (convertMoney) === 'string') {
                        sum = money.convertMoney(sum, convertMoney);
                    }
                    for (var i = 0; i < groupArray.length; i++) {
                        if (key == groupArray[i]) {
                            var valueForSelect = money.format(sum, options);
                            $(that.config.elem).find("tbody .selectSum" + courtName + i).html(valueForSelect).attr('title', valueForSelect);
                        }
                    }
                }
            }
        }
    }

    //表格点击的方法
    TableList.prototype.clickRowCallback = function (setting, json) {
        this.checkboxToggleAll(setting, json);
        this.selectTableShow(setting, json);
        if (this.config.clickRowSelected) { //行点击参数为true才会有点击事件
            this.clickRow(setting, json);
        } else {
            this.clickChildCheckBox(setting, json);
        }
    };

    // 新增忽略美化时的
    TableList.prototype.changeCheckboxCallback = function(settings,$this){
        var _config = this.config, _that = this;
        var elem = _config.elem;
        var rowIndex = $this.parents('tr').data('dt-row');
        var oThat = $(elem).find('tbody tr:eq(' + rowIndex + ')');
        var that = oThat[0];
        var table = new $.fn.dataTable.Api(setting);


      //点击禁用行时，直接return
      if (oThat.find("td:first-child input.trdisabled").length == 1 || oThat.hasClass('btsummary')) {
        // layer.msg('此行信息不能选择');
        return;
      }
      //点击右固定列时，判断是否有禁用行
      var rightFixedWrapper = oThat.parents('.DTFC_RightWrapper');
      if (rightFixedWrapper.length) {
        var rowIndex = oThat.index();
        var tr = $(_config.elem).find('tbody tr:eq(' + rowIndex + ')');
        if (tr.find("td:first-child input.trdisabled").length == 1) {
          // layer.msg('此行信息不能选择');
          return;
        }
      }
      //设置行选中方法
      _that.rowSelected(oThat);

      //支持翻页功能时
      if (_config.jumpPageSelect) {
        var doTrData = table.rows(oThat).data();
        doTrData = doTrData[0];
        var selectId = _that.selectId;
        if (oThat.hasClass('selected')) {
          selectId.push(doTrData);
        } else {
          for (var i = 0; i < selectId.length; i++) {
            if (doTrData.id == selectId[i].id) {
              selectId.splice(i, 1);
              break;
            }
          }
        }
        _that.setWindowValue(oThat.parents("table"), selectId)
      }
      var data = table.rows(that).data();
      var index = table.row(that).index();
      var dataArr = [];
      var selectData = data[0];
      if (oThat.hasClass('selected')) {
        if (selectData) {
          selectData.isSelect = true;
        }
      } else {
        if (selectData) {
          selectData.isSelect = false;
        }
      }
      dataArr.push(selectData);
      var selectDatas = table.rows(".selected").data();
      if (_config.summary) {
        selectDatas = selectDatas.splice(0, selectDatas.length)
        _that.selectSumData(selectDatas, $(_config.elem), false, dataArr);
      }
      var btClickRowCallback = _config.btClickRowCallback;
      if (typeof (btClickRowCallback) === "function") {
        btClickRowCallback(dataArr);
      }
    }

    TableList.prototype.clickChildCheckBox = function (setting, josn) {
        var _config = this.config, _that = this;
        var elem = _config.elem;
        var selectTable = _config.selectTable;

        if(_config.isLayIgnore){
              $(_config.elem).parents(".dataTables_wrapper").on('click','.checkchild',function(){
                  _that.changeCheckboxCallback(setting,$(this));
              })
          return false
        }

        form.on('checkbox(' + _that.filterValue.childSelectFiter + ')', function (data) {
            var rowIndex = data.othis.parents('tr').data('dt-row');
            var oThat = $(elem).find('tbody tr:eq(' + rowIndex + ')');
            var that = oThat[0];
            var table = new $.fn.dataTable.Api(setting)

            //点击禁用行时，直接return
            if (oThat.find("td:first-child input.trdisabled").length == 1 || oThat.hasClass('btsummary')) {
                // layer.msg('此行信息不能选择');
                return;
            }
            //点击右固定列时，判断是否有禁用行
            var rightFixedWrapper = oThat.parents('.DTFC_RightWrapper');
            if (rightFixedWrapper.length) {
                var rowIndex = oThat.index();
                var tr = $(_config.elem).find('tbody tr:eq(' + rowIndex + ')');
                if (tr.find("td:first-child input.trdisabled").length == 1) {
                    // layer.msg('此行信息不能选择');
                    return;
                }
            }
            //设置行选中方法
            _that.rowSelected(oThat);

            //支持翻页功能时
            if (_config.jumpPageSelect) {
                var doTrData = table.rows(oThat).data();
                doTrData = doTrData[0];
                var selectId = _that.selectId;
                if (oThat.hasClass('selected')) {
                    selectId.push(doTrData);
                } else {
                    for (var i = 0; i < selectId.length; i++) {
                        if (doTrData.id == selectId[i].id) {
                            selectId.splice(i, 1);
                            break;
                        }
                    }
                }
                _that.setWindowValue(oThat.parents("table"), selectId)
            }
            var data = table.rows(that).data();
            var index = table.row(that).index();
            var dataArr = [];
            var selectData = data[0];
            if (oThat.hasClass('selected')) {
                if (selectData) {
                    selectData.isSelect = true;
                }
            } else {
                if (selectData) {
                    selectData.isSelect = false;
                }
            }
            dataArr.push(selectData);
            var selectDatas = table.rows(".selected").data();
            if (_config.summary) {
                selectDatas = selectDatas.splice(0, selectDatas.length)
                _that.selectSumData(selectDatas, $(_config.elem), false, dataArr);
            }
            var btClickRowCallback = _config.btClickRowCallback;
            if (typeof (btClickRowCallback) === "function") {
                btClickRowCallback(dataArr);
            }
            form.render('checkbox');
        })

    }

    TableList.prototype.clickRow = function (setting, json) {
        var _config = this.config, _that = this;
        var elem = _config.elem;
        var selectTable = _config.selectTable;
        //注销行点击事件，解决表格销毁之后的问题
        $(_config.elem).off('click', 'tbody tr');
        $(_config.elem).on('click', 'tbody tr', function (event) {

            var that = this;
            var oThat = $(that);
            // 如果点击是无数据行，则不执行
            if (oThat.find('.dataTables_empty')[0]) {
                return;
            }
            var table = new $.fn.dataTable.Api(setting)
            //点击禁用行时，直接return
            if (oThat.find("td:first-child input.trdisabled").length == 1 || oThat.hasClass('btsummary')) {
                // layer.msg('此行信息不能选择');
                return;
            }
            //点击右固定列时，判断是否有禁用行
            var rightFixedWrapper = oThat.parents('.DTFC_RightWrapper');
            if (rightFixedWrapper.length) {
                var rowIndex = oThat.index();
                var tr = $(_config.elem).find('tbody tr:eq(' + rowIndex + ')');
                if (tr.find("td:first-child input.trdisabled").length == 1) {
                    // layer.msg('此行信息不能选择');
                    return;
                }
            }
            //设置行选中方法
            _that.rowSelected(oThat);

            //支持翻页功能时
            if (_config.jumpPageSelect) {
                var doTrData = table.rows(oThat).data();
                doTrData = doTrData[0];
                var selectId = _that.selectId;
                if (oThat.hasClass('selected')) {
                    selectId.push(doTrData);
                } else {
                    for (var i = 0; i < selectId.length; i++) {
                        if (doTrData.id == selectId[i].id) {
                            selectId.splice(i, 1);
                            break;
                        }
                    }
                }
                _that.setWindowValue(oThat.parents("table"), selectId)
            }
            var data = table.rows(that).data();
            var index = table.row(that).index();
            var dataArr = [];
            var selectData = data[0];
            if (oThat.hasClass('selected')) {
                if (selectData) {
                    selectData.isSelect = true;
                }
            } else {
                if (selectData) {
                    selectData.isSelect = false;
                }
            }
            dataArr.push(selectData);
            var selectDatas = table.rows(".selected").data();
            if (_config.summary) {
                selectDatas = selectDatas.splice(0, selectDatas.length)
                _that.selectSumData(selectDatas, $(_config.elem), false, dataArr);
            }
            var btClickRowCallback = _config.btClickRowCallback;
            if (typeof (btClickRowCallback) === "function") {
                btClickRowCallback(dataArr);
            }

            if(!_config.isLayIgnore){
                form.render('checkbox');
            }
        });
    };

    //在固定列时，设置固定列的选中样式
    TableList.prototype.rowSelected = function (elem) {
        var that = this;
        var rowIndex = elem.index();
        var mulselect = that.config.mulselect;
        var tables = elem.parents('.DTFC_ScrollWrapper').find('table');
        if (!tables.length) {
            tables = elem.parents('.dataTables_scrollBody').find('table');
        }
        var selectedStyle = that.selectedStyle;
        for (var i = 0; i < tables.length; i++) {
            var clickTr = $(tables[i]).find("tbody tr:eq(" + rowIndex + ") ");
            if (!clickTr.length) {
                continue;
            }
            that.toggleSelectedClass(clickTr);
            if (!mulselect) {
                var trs = clickTr.siblings("tr").removeClass(selectedStyle);
                trs.find("input.checkchild:checked").prop("checked", false);
            }
            if (that.config.checkbox && clickTr.find("td:first-child input").length != 0) {
                clickTr.find('input[type="checkbox"]').prop("checked", clickTr.hasClass("selected"));
            }
        }
        //根据选中的内容，切换全选状态
        if (that.config.checkbox) {
            checkAll($(that.config.elem));
        }
    }

    function checkAll(elem) {
        var checked = elem.find("input.checkchild:not('.trdisabled')").length == $(elem).find("input.checkchild:checked").length ? true : false;
        elem.parents('.dataTables_scrollBody').siblings('.dataTables_scrollHead').find('table thead th input.checkAll ').prop('checked', checked);
        var leftWrapper = elem.parents('.dataTables_scroll').siblings('.DTFC_LeftWrapper');
        if (leftWrapper.length) {
            leftWrapper.find('.DTFC_LeftHeadWrapper table thead th  input.checkAll').prop('checked', checked);
        }
    }

    //切换选中的className方法
    TableList.prototype.toggleSelectedClass = function (elem) {
        elem.toggleClass(this.selectedStyle)
    };
    //表格保存时
    TableList.prototype.saveTableCallback = function (event) {
        var that = this;
        var _config = that.config;
        var rows = $(_config.elem).find('.edittr');
        rows.each(function (i, row) {
            var tds = row.checkchild('td');
            tds.each(function (j, td) {
                var td = $(td);
                if (td.has('input')) {
                    var txt = td.children("input").val();
                    td.html(txt);
                    dt.cell(td).data(txt);//修改DataTables对象的数据
                }
            })
        })
        var tableData = dt.rows(rows).data();
        var data = tableData.splice(0, tableData.length)
        _config.saveCallback(data)
    };

    //新增忽略美化时的 全选的业务逻辑
    TableList.prototype.changeCheckAll = function(checkAllElem){
        var _that = this;
        var _config = this.config, elem = _config.elem;
        var table = _that.table;
        var obj = checkAllElem;
        var $tr = $(elem).find('tbody tr');
        var wrapper = $(elem).parents('.dataTables_wrapper');
        var selectedStyle = _that.selectedStyle;
        var allChildrenCheckboxs = wrapper.find('input.checkchild:not(.trdisabled)')
        allChildrenCheckboxs.prop("checked", checkAllElem.checked);

        if (obj.checked) {
            allChildrenCheckboxs.parents('tr').addClass(selectedStyle)
        } else {
            wrapper.find('tbody tr').removeClass(selectedStyle)
        }
        //设置固定列的全选全不选事件
        if (_config.fixedColumns && obj.checked) {
            var fixedRightTrs = $(checkAllElem).parents(".DTFC_LeftWrapper").siblings(".DTFC_RightWrapper").find("table tbody tr:not(.btsummary)");
            if (fixedRightTrs.length !== 0) {
                var allAbleTrs = $(elem).find('tbody tr').find('input.checkchild:not(.trdisabled)');
                // 如果没有禁选，就直接添加选中样式或取消样式
                if(allAbleTrs.length && allAbleTrs.length === fixedRightTrs.length){
                    fixedRightTrs.addClass(selectedStyle)
                }else{
                    bt.each(fixedRightTrs, function () {
                        var othis = $(this);
                        if (othis.attr("role") == "row") {
                            var rowid = othis.data("dt-row");
                            if (_that.config.checkbox && $(elem).find('tbody tr:eq(' + rowid + ')').find("td:first-child input:not(.trdisabled)").length != 0) {
                                _that.setSelectedClass(othis, obj.checked)
                            }
                        }
                    })
                }
            }
        }

        if (_config.jumpPageSelect) {//支持翻页功能时
            //  var doTrData = table.rows().data();
            var selectId = _that.selectId;
            if (obj.checked) {
                var doTrData = table.rows('.selected').data();
                doTrData = doTrData.splice(0, doTrData.length);
                if (selectId.length === 0) {
                    selectId = selectId.concat(doTrData);
                } else {
                    for (var j = 0; j < doTrData.length; j++) {
                        for (var i = 0; i < selectId.length; i++) {
                            if (doTrData[j].id != selectId[i].id) {
                                selectId.push(doTrData[j]);
                                break
                            }
                        }
                    }
                }
            } else {
                var doTrData = table.rows().data();
                for (var j = 0; j < doTrData.length; j++) {
                    for (var i = 0; i < selectId.length; i++) {
                        if (doTrData[j].id == selectId[i].id) {
                            selectId.splice(i, 1);
                            break
                        }
                    }
                }
            }
            _that.selectId = selectId;
            _that.setWindowValue($(this).parents("table"), selectId)
        }
        var data = table.rows('.selected').data();
        var btClickRowCallback = _config.btClickRowCallback;

        if (typeof (btClickRowCallback) === "function") {
            // var data = json.data;
            var currentData = table.rows('.selected').data();
            currentData = currentData.splice(0, currentData.length)
            btClickRowCallback(currentData);
        }

        if (_config.summary) {
            _that.selectSumData(data.splice(0, data.length), $(elem));
        }
    }

    //checkbox全选全不选
    TableList.prototype.checkboxToggleAll = function (setting, json) {
        var _that = this;
        var _config = this.config;//
        var  elem = _config.elem;
        // 忽略美化
        if(_that.config.isLayIgnore){
            $(_config.elem).parents(".dataTables_wrapper").on('click','.checkAll',function(){
                _that.changeCheckAll(this);
            })
            return false
        }

        form.on('checkbox(' + _that.filterValue.allSelectFiliter + ')', function (data) {

            var table = _that.table;
            var obj = this;
            var $tr = $(elem).find('tbody tr');
            var wrapper = $(elem).parents('.dataTables_wrapper');
            var selectedStyle = _that.selectedStyle;
            wrapper.find('input.checkchild:not(.trdisabled)').prop("checked", this.checked);
            form.render('checkbox');
            if (obj.checked) {
                bt.each($tr, function () {
                    if ($(this).find("td:first-child input:not(.trdisabled)").length != 0) {
                        $(this).addClass(selectedStyle);
                    }
                })
            } else {
                $tr.removeClass(selectedStyle);
            }
            //设置固定列的全选全不选事件
            if (_config.fixedColumns) {
                var fixedLeftTrs = $(data.elem).parents(".DTFC_LeftHeadWrapper").siblings(".DTFC_LeftBodyWrapper").find("table tbody tr");
                var fixedRightTrs = $(data.elem).parents(".DTFC_LeftWrapper").siblings(".DTFC_RightWrapper").find("table tbody tr");
                //遍历左边固定部分
                if (fixedLeftTrs.length !== 0) {
                    bt.each(fixedLeftTrs, function () {
                        var othat = $(this);
                        if (othat.find('input[type="checkbox"]:not(.trdisabled)').length !== 0 && othat.attr("role") == "row") {
                            _that.setSelectedClass(othat, obj.checked)
                        }
                    });
                }
                if (fixedRightTrs.length !== 0) {
                    bt.each(fixedRightTrs, function () {
                        var othis = $(this);
                        if (othis.attr("role") == "row") {
                            var rowid = othis.data("dt-row");
                            if (_that.config.checkbox && $(elem).find('tbody tr:eq(' + rowid + ')').find("td:first-child input:not(.trdisabled)").length != 0) {
                                _that.setSelectedClass(othis, obj.checked)
                            }
                        }
                    })
                }
            }
            // if (_config.jumpPageSelect) {//支持翻页功能时//
            //     _config.jumpPageSelect = false;
            //      }

            if (_config.jumpPageSelect) {//支持翻页功能时
                //  var doTrData = table.rows().data();
                var selectId = _that.selectId;
                if (this.checked) {
                    var doTrData = table.rows('.selected').data();
                    doTrData = doTrData.splice(0, doTrData.length);
                    if (selectId.length === 0) {
                        selectId = selectId.concat(doTrData);
                    } else {
                        for (var j = 0; j < doTrData.length; j++) {
                            for (var i = 0; i < selectId.length; i++) {
                                if (doTrData[j].id != selectId[i].id) {
                                    selectId.push(doTrData[j]);
                                    break
                                }
                            }
                        }
                    }
                } else {
                    var doTrData = table.rows().data();
                    for (var j = 0; j < doTrData.length; j++) {
                        for (var i = 0; i < selectId.length; i++) {
                            if (doTrData[j].id == selectId[i].id) {
                                selectId.splice(i, 1);
                                break
                            }
                        }
                    }
                }
                _that.selectId = selectId;
                _that.setWindowValue($(this).parents("table"), selectId)
            }
            var data = table.rows('.selected').data();
            var btClickRowCallback = _config.btClickRowCallback;

            if (typeof (btClickRowCallback) === "function") {
                // var data = json.data;
                var currentData = table.rows().data();
                currentData = currentData.splice(0, currentData.length)
                for (var i = 0; i < currentData.length; i++) {
                    if (this.checked) { //全选
                        var isSelectedRow = $(elem).find('tbody tr:eq(' + i + ')').hasClass('selected');
                        if (isSelectedRow) {//行非禁用时设置选中属性；
                            currentData[i].isSelect = true;
                        } else {//行禁用时设置选中属性；
                            currentData[i].isSelect = false;
                        }
                    } else { //全不选
                        currentData[i].isSelect = false;
                    }
                }
                btClickRowCallback(currentData);
            }

            if (_config.summary) {
                _that.selectSumData(data.splice(0, data.length), $(elem));
            }
        });
    };
    //设置选中时的tr样式及选中状态
    TableList.prototype.setSelectedClass = function (elem, status) {
        var that = this;
        var selectedStyle = that.selectedStyle;
        if (status) {
            elem.addClass(selectedStyle);
        } else {
            elem.removeClass(selectedStyle);
        }
    };

    //表格汇总获取total数据
    TableList.prototype.getAllSummary = function () {
        var that = this;
        var _config = that.config;
        var summary = _config.summary;
        if (summary !== undefined) {
            var url = summary.url;
            var data = {};
            if (summary && typeof (_config) === 'object') {
                var sumList = summary.sumList;
                for (var j = 0; j < sumList.length; j++) {
                    var dataName = sumList[j].dataName;
                    data[dataName] = "all";
                }
            }
            if (url !== undefined) {
                $.post(
                    url,
                    data,
                    function (result) {
                        for (var key in data) {
                            $('.allSum' + key).html(result[key])
                        }
                    }
                )
            }
        }
    };

    //下拉表格显示与隐藏
    TableList.prototype.selectTableShow = function () {
        var that = this;
        var _config = that.config;
        var selectTable = _config.selectTable;
        if (typeof (selectTable) === 'object') {
            $(selectTable.inputElem).parents("td").css("position", "relative");
            //table显示
            $(selectTable.inputElem).on('click focus', function (event) {
                event.stopPropagation();
                $(".selectTableDiv").hide();
                $(this).siblings('div').show();
            })
        }
    }

    TableList.prototype.setColumnsName = function () {
        var that = this;
        var _config = that.config;
        var columnDefs = [];
        if (_config.columns !== undefined) {
            for (var i = 0; i < _config.columns.length; i++) {
                if (_config.columns[i].data && _config.columns[i].data != 'null') {
                    var column = {
                        name: _config.columns[i].data,
                        targets: i
                    };
                    if (_config.columns[i].visible !== undefined) { //解决配置了隐藏列显示的问题
                        column.visible = _config.columns[i].visible;
                    }
                    if (_config.columns[i].data && _config.columns[i].data !== "id") {
                        column.className = "exportColumn";
                    }
                    columnDefs.push(column);
                }
            }
        }
        _config.columnDefs = columnDefs;
    };

    //给表头添加居中样式
    // TableList.prototype.setHeaderCenter = function () {
    //     var _config = this.config;
    // var header = $(_config.elem).find('thead th');
    // for(var i = 0;i < header.length;i++){
    //     $(header[i]).css('text-align','center')
    // }
    // var trs = $(_config.elem).find('thead tr');
    // for(var i = 0;i < trs.length;i++){
    //     $(trs[i]).css('align','center')
    // }
    // }

    //单选多选设置
    TableList.prototype.setSelectType = function () {
        var that = this, _config = that.config;
        var s = _config.mulselect;
        if (s) {
            _config.select = {
                style: 'multi'
            }
        }
    }

    //设置表格上方按钮的显示隐藏,用于设置权限
    TableList.prototype.setButtonsShow = function () {
        var that = this;
        var btns = that.config.buttons;
        if (btns) {
            for (var i = 0; i < btns.length; i++) {
                var btn = btns[i];
                if (btns[i].hide) {
                    delete that.config.buttons[i]
                }
            }
        }
    }

    //获取选中的数据
    TableList.prototype.attr = function (obj) {
        var that = this;
        if (!obj) {
            return '';
        }
        var _config = that.config;
        for (var key in obj) {
            _config[key] = obj[key];
        }
        that.clearSelected();
    }
    //刷新表格当前页
    TableList.prototype.draw = function (tableId) {
        var that = this;
        var tableObj = that.getCurrentWebTableObject(tableId);
        tableObj.ajax.reload(null, false);
    }
    //重新加载
    TableList.prototype.reload = function () {
        var that = this;
        var tableObj = that.getCurrentWebTableObject();
        tableObj.ajax.reload();
    }

    TableList.prototype.clearSelected = function () {
        var that = this;
        var $elem = $(that.config.elem);
        var scrollDiv = $elem.parents('.dataTables_scroll');
        var leftWrapper = scrollDiv.siblings('.DTFC_LeftWrapper');
        var selectedStyle = that.selectedStyle;
        $elem.find('tbody tr').removeClass(selectedStyle);
        //清除固定列的选中事件
        if (that.config.fixedColumns) {
            //左边固定列选中样式
            leftWrapper.find('tbody tr').removeClass(selectedStyle);
            leftWrapper.find('.checkAll').prop('checked', false);//清除左边固定列表头的选中
            var leftCheckboxs = leftWrapper.find('.checkchild');
            bt.each(leftCheckboxs, function (i, item) {
                item.checked = false;
            })
            //右边固定列选中样式
            scrollDiv.siblings('.DTFC_RightWrapper').find('tbody tr').removeClass(selectedStyle);
        }
        var checkboxs = $elem.find('tbody tr .checkchild');
        bt.each(checkboxs, function (i, item) {
            item.checked = false;
        })
        form.render('checkbox');
    }
    //存贮数据
    TableList.prototype.setWindowValue = function (jThis, obj) {
        var that = this;
        var bizName = jThis.data('biz-name'.toLowerCase());
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            window.biz[that.bizName] = [{bizName: bizName, data: obj}];
        }
    }
    //根据bizname获取存贮在window中的对象
    TableList.prototype.get = function (name) {
        var that = this;
        var currentObj = window.biz && window.biz[that.bizName];
        if (currentObj) {
            for (var i = 0; i < currentObj.length; i++) {
                if (currentObj[i] && currentObj[i].bizName == name) {
                    that.currentObject = currentObj[i];
                    break
                }
            }
        }
        // if (!currentObj) {
        //     throwError(bizName + "尚未配置data-biz-name属性！")
        // }
        return that;
    }
    //获取存贮在window中的数据
    TableList.prototype.getAllSelectedData = function () {
        var that = this;
        var currentObj = that.currentObject;
        if (!currentObj) {
            return
        }
        var selected = currentObj.data;
        return selected;
    }

    //设置表格默认宽度
    TableList.prototype.initColWidth = function (ops) {
        var that = this;
        var columns = ops.columns;
        if (columns) {
            for (var i = 0; i < columns.length; i++) {
                var col = columns[i];
                if (col.width == undefined) {
                    columns[i].width = '120px'
                }
            }
        }
        return columns
    }

    //给首列添加checkbox参数
    function setCheckbox(options, config) {
        var columns = options.columns;
        var checkboxObj = {
            data: "id",
            width: "28px",
            className: "center ckbx28 extraCol datatable-checkbox",
            orderable: false,
            render: function (data, type, full, meta) {
                var checkBoxHtml = ''
                if(!options.isLayIgnore){
                    checkBoxHtml = '<div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div>'
                }
                if (full.checkboxHide && full.checkboxHide == "1") {
                    return '<input type="checkbox"  class="checkchild trdisabled" lay-skin="primary" data-id="' + data + '"  disabled="" />';
                } else {
                    // 如果在禁用清单中的，也将禁用
                    if(options.disableIds && options.disableIds.indexOf(data) !== -1){
                        return '<input type="checkbox"  class="checkchild trdisabled" lay-skin="primary" data-id="' + data + '"  disabled="" />';
                    }
                    return '<input type="checkbox"  class="checkchild" lay-skin="primary" data-id="' + data + '" lay-filter ="' + config.filterValue.childSelectFiter + '"/>' + checkBoxHtml;
                }
            }
        };
        columns.unshift(checkboxObj);
        if (options.summary) {
            var sumList = options.summary.sumList;
            bt.each(sumList, function (i, v) {
                var colNum = v.colNum, dataName = v.dataName;
                columns[colNum].className += ' ' + dataName
            })
        }
        return columns
    }

    //给首列添加checkbox时columnDefs列数加1处理;
    function setcolumnDefs(options) {
        var columnDefs = options.columnDefs;
        if (columnDefs === undefined) return;
        for (var i = 0; i < columnDefs.length; i++) {
            columnDefs[i].targets = columnDefs[i].targets + 1;
        }
        return columnDefs
    }

    //获取当前页码Datatable对象
    TableList.prototype.getCurrentWebTableObject = function (tableId) {
        var that = this;
        if (tableId) {
            return new $.fn.dataTable.Api('#' + tableId);
        } else {
            return $.fn.dataTable.tables({visible: true, api: true});
        }
    }
    //表格刷新，回到第一页
    TableList.prototype.refreshTable = function (tableId) {
        var that = this;
        var tableObj = that.getCurrentWebTableObject(tableId);
        tableObj.ajax.reload();
    }
    //兼容原来datatable row的方法
    TableList.prototype.row = function (rowIndex) {
        var that = this;
        var tableObj = that.getCurrentWebTableObject();
        return tableObj.row(rowIndex);
    };
    //兼容原来datatable on的方法 event 事件名称；elem 范围,callback 回调
    TableList.prototype.on = function (event, elem, callback) {
        var that = this;
        if (that.table) {
            that.table.on(event, elem, callback);
        }
    }

    //设置选中行的样式,当无复选框时，不添加背景色
    TableList.prototype.selectStyle = function (event, elem, callback) {
        var that = this;
        if (that.config.checkbox && that.config.selectCss) {
            that.selectedStyle = 'selectedCss selected';
        }
    }

    TableList.prototype.jumpPage = function ($table, setting) {
        var that = this;
        var toolDiv = $table.find('.jump_page');
        var html = '<span class="table_input_text">' + bt.lang('table.datatable.pageTo') + '</span><input type="text" class="table_input_page "/>&nbsp;&nbsp' + bt.lang('common.table.page');
        toolDiv.html(html);
        keyupPageNumber(toolDiv);
        enterJumpPage(new $.fn.dataTable.Api(setting), toolDiv);
    }

    /**
     * 设置行的选中
     * 参数 tableId　为表格的id
     * 参数  obj
     *     1、object 是一个对象，包括key　代表数据中的字段名称；value　代表数据字段中的key的值；checked 代表行是否被选中;rowIndex:列索引（可选）（{ key:'id',value:'122223333',checked:true} / { key:'id',value:['122223333','2566899999'],checked:true}）
     *     ２、Array 是一个对象数组，对象参数如上，但是rowIndex必填
     */
    TableList.prototype.onCheckRow = function (tableId, obj) {
        if (!obj) {
            return
        }
        var that = this;
        var tableId = tableId.indexOf('#') !== 0 ? tableId : tableId.replace('#', '');
        var datatableObj = this.getCurrentWebTableObject(tableId);//获取指定tableId的datatable对象
        var $table = $("#" + tableId);
        if (Array.isArray(obj)) { //判断是数组时，对数组进行遍历
            bt.each(obj, function (i, v) {
                if (v.rowIndex !== undefined) {
                    if (Array.isArray(v.rowIndex)) {
                        bt.each(v.rowIndex, function (indexValue, dataRowIndex) {
                            that.checkRow(dataRowIndex, v.checked, $table, datatableObj)
                        })
                    } else {
                        that.checkRow(v.rowIndex, v.checked, $table, datatableObj)
                    }
                } else {
                    var data = datatableObj.rows().data();
                    bt.each(data, function (index, item) {
                        if (Array.isArray(v.value)) {
                            bt.each(v.value, function (indexValue, dataValue) {
                                if (item[v.key] == dataValue) {
                                    that.checkRow(index, v.checked, $table, datatableObj)
                                }
                            })
                        } else {
                            if (item[v.key] == v.value) {
                                that.checkRow(index, v.checked, $table, datatableObj)
                            }
                        }
                    })
                }
            })
        } else {
            if (obj.rowIndex !== undefined) {
                that.checkRow(obj.rowIndex, obj.checked, $table, datatableObj)
            } else {
                var data = datatableObj.rows().data();
                bt.each(data, function (index, item) {
                    if (Array.isArray(obj.value)) {
                        bt.each(obj.value, function (indexValue, dataValue) {
                            if (item[obj.key] == dataValue) {
                                that.checkRow(index, obj.checked, $table, datatableObj)
                            }
                        })
                    } else {
                        if (item[obj.key] == obj.value) {
                            that.checkRow(index, obj.checked, $table, datatableObj)
                        }
                    }
                })
            }
        }
        checkAll($table);//根据选中的内容，切换全选状态
        form.render('checkbox');
        this.updateSummary(tableId);
    }

    /**
     * 选中指定行
     * */
    TableList.prototype.checkRow = function (rowIndex, checked, elem, datatableObj) {
        var that = this;
        var tables = elem.parents('.DTFC_ScrollWrapper').find('table');
        //获取页面贮存的实例
        var currentTableList = elem.data('table');
        currentTableList = currentTableList ? currentTableList : this;

        for (var i = 0; i < tables.length; i++) {
            var clickTr = $(tables[i]).find("tbody tr:eq(" + rowIndex + ") ");
            if (!clickTr.length) {
                continue;
            }
            checked ? clickTr.addClass(currentTableList.selectedStyle) : clickTr.removeClass(currentTableList.selectedStyle);
            if (clickTr.find("td:first-child input").length != 0) {
                clickTr.find('input[type="checkbox"]').prop("checked", checked);
            }

        }
        //支持翻页功能时
        if (currentTableList.config.jumpPageSelect) {
            var bizName = elem.data('biz-name'.toLowerCase());
            if (bizName) {
                var selectId = [];
                var currentObj = window.biz && window.biz[that.bizName];
                if (currentObj) {
                    for (var i = 0; i < currentObj.length; i++) {
                        if (currentObj[i] && currentObj[i].bizName == bizName) {
                            selectId = currentObj[i].data;
                            break
                        }
                    }
                }
                var rowIndexData = datatableObj.rows(rowIndex).data()[0];
                var isHasId = false; //当前数据是否在selectId中
                for (var i = 0; i < selectId.length; i++) {
                    if (rowIndexData.id == selectId[i].id) {
                        if (!checked) {
                            selectId.splice(i, 1);
                        }
                        isHasId = true;
                        break;
                    }
                }
                if (!selectId.length || (!isHasId && checked)) {
                    selectId.push(rowIndexData);
                }
                that.selectId = selectId;
                that.setWindowValue(elem, selectId)
            }
        }
    }

    /**
     * 更新表格汇总
     * */
    TableList.prototype.updateSummary = function (tableId) {
        var tableId = tableId.indexOf('#') !== 0 ? tableId : tableId.replace('#', '');
        var datatableObj = this.getCurrentWebTableObject(tableId);//获取指定tableId的datatable对象
        if (this.config && this.config.summary) {
            this.doSumData(datatableObj, true);
            var selectDatas = datatableObj.rows(".selected").data();
            selectDatas = selectDatas.splice(0, selectDatas.length)
            this.selectSumData(selectDatas, $('#' + tableId), true);
        }
    }

    TableList.prototype.randomNum = function () {
        var num = Math.random() * 1000000;
        num = parseInt(num, 10);
        this.randomNumber = num;
        return num;
    }

    //控制只能输入正整数
    function keyupPageNumber(toolsDiv) {
        //$('.dataTables_wrapper .wrapper input').on('keyup',function(){
        toolsDiv.find('input').on('keyup', function () {
            if (this.value.length == 1) {
                this.value = this.value.replace(/[^1-9]/g, '')
            } else {
                this.value = this.value.replace(/\D/g, '')
            }
        }).on('afterpaste', function () {
            if (this.value.length == 1) {
                this.value = this.value.replace(/[^1-9]/g, '0')
            } else {
                this.value = this.value.replace(/\D/g, '')
            }
            ;
        })

    }

    //按回车键页面跳转
    function enterJumpPage(table, toolsDiv) {
        toolsDiv.on('keydown', '.table_input_page', function (e) {
            e = event || windw.event;
            if (e.keyCode === 13) {
                var val = parseInt($(this).val());
                if (val >= 0) {
                    if (val > table.page.info().pages) {//大于最大页码时，清空页码
                        $(this).val('')
                    } else {
                        val--
                        table.page(val).draw('page');
                    }
                }
                $(this).blur();
            }
        })
    }

    function throwError(msg) {
        throw new Error(msg);
    };

    function isFunction(fun) {
        if (fun == undefined) {
            return false
        }
        return fun && typeof (fun) === 'function'
    }

    var tble = new TableList();

    var obj = {
        init: function (ops) {
            tble.set(ops);
            var tableObj = tble.init();
            return tableObj;
        },
        initMultiTables: function (ops) { //针对两个以上表格的初始化方法
            var tl = new TableList();
            tl.set(ops, 'initMultiTables');
            var tableObj = tl.init();
            return tableObj;
        },
        getSelectData: function (elem) { //获取选中数据
            var t;
            if (elem) {
                t = $(elem).DataTable();
            } else {
                t = tble.table;
            }
            if (!t) {
                return null;
            }
            var d = t.rows(".selected").data();
            var data = d.splice(0, d.length);
            return data
        },
        getSelectRowIndexes: function (elem) {　//获取选中行的索引
            var t;
            if (elem) {
                t = $(elem).DataTable();
            } else {
                t = tble.table;
            }
            if (!t) {
                return null;
            }
            var d = t.rows(".selected").indexes();
            return d.splice(0, d.length);
        },
        render: function (ops) {
            tble.set(ops);
            tble.init();
            return tble;
        },
        getJumpSelectedData: function (bizname) {
            tble.get(bizname);
            return tble.getAllSelectedData() || []
        },
        refreshCurrentPage: function (tableId) {
            var tble = new TableList();
            tble.draw(tableId);
        },
        refreshTable: function (tableId) {
            var tble = new TableList();
            tble.refreshTable(tableId);
        },
        /**
         * 参数 obj　
         *    1、object类型　是一个对象，包括key　代表数据中的字段名称；value　代表数据字段中的key的值；checked 代表行是否被选中　rowIndex　行索引
         *    2、Array 类型　是一个对象数组，对象中参数同上
         * */
        onCheckRow: function (tableId, obj) {
            tble.onCheckRow(tableId, obj)
        },
        /**
         * 更新汇总结果
         *  参数：tableId 汇总表格的id号
         * */
        updateSummary: function (tableId) {
            tble.updateSummary(tableId)
        }

    }

    exports("datatable", obj)
}).addcss('../../../css/datatable.css', 'datatablecss');

