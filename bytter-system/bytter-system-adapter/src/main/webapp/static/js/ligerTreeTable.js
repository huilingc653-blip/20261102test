/**
 * 可冻结列的树表格
 * 本组件依赖ligerUi 及jquery
 * liuyj
 * 2018年6月25日
 * 0.0.1 封装组件
 * 0.0.2 新增加托宽功能；新增时间输入框靠右
 */
layui.define(['jquery','form','ligerBase','ligerGrid','ligerResizable'], function(exports){

    var TreeTable = function(){
        this.config = {
            elem:'',//id选择器
            width: '100%',
            //height: '97%',
            rownumbers: false,//是否显示行序号
            frozenRownumbers:false,
            enabledSort:false,//排序功能关闭
            usePager:false,//是否分页
            colDraggable:true,//是否允许表头拖拽
            rowDraggable:true,//是否允许行拖拽
            onRendered:function(){
                $('.l-panel').find('.bt-biz-moneyType').css('float','right');
            }
            //columns: [
            //    { display: '部门名', name: 'name', width: 250, align: 'left', frozen: true },
            //    { display: '部门标示', name: 'id', width: 150, type: 'int', align: 'left',
            //        render : function (record, rowindex, value, column) {
            //                  //this     这里指向grid
            //                   //record   行数据
            //                   //rowindex 行索引
            //                   //value    当前的值，对应record[column.name]
            //                  //column   列信息
            //                 return value;  //返回此单元格显示的HTML内容(一般根据value和row的内容进行组织)
            //                }},
            //    { display: '部门标示2', name: 'id', width: 150, type: 'int', align: 'left' },
            //    { display: '部门标示3', name: 'id', width: 150, type: 'int', align: 'left' },
            //    { display: '部门标示4', name: 'id', width: 150, type: 'int', align: 'left' },
            //    { display: '部门描述', name: 'remark', width: 150, align: 'left' }
            //],
            //data:{
            //    Rows : [
            //        { id: '01', name: "企划部",   remark: "1989-01-12",
            //            children: [
            //                { id: '0101', name: "企划分部一", remark: "企划分部一"
            //                },
            //                { id: '0102', name: "企划分部二", remark: "企划分部二", children:
            //                    [
            //                        {
            //                            id: '010201', name: "企划分部二 A组", remark: "企划分部二 A组", children:
            //                            [
            //                                { id: '01020101', name: "企划分部二 A组1分队", remark: "企划分部二 A组1分队" },
            //                                { id: '01020102', name: "企划分部二 A组2分队", remark: "企划分部二 A组2分队" }
            //                            ]
            //                        },
            //                        { id: '010202', name: "企划分部二 B组", remark: "企划分部二 B组" }
            //                    ]
            //                },
            //                { id: '0103', name: "企划分部三", remark: "企划分部三" }
            //            ]
            //        },
            //        { id: '02', name: "研发部", remark: "研发部" },
            //        { id: '03', name: "产品部", remark: "产品部" }
            //    ]
            //}
            //,
            //tree: { columnName: 'name' }
        };
        this.ligerObj = null;
        this.bizName = 'ligerTree'
    }

    /**
     * 参数设置
     * @param {Object} options
     */
    TreeTable.prototype.set = function(options){
        var that = this;
        $.extend(true, that.config, options);
        if(!this.config.resize){ //列托宽功能是否开启
            that.config.colDraggable = false;//是否允许表头拖拽
            that.config.rowDraggable = false;//是否允许行拖拽
            $(that.config.elem).addClass('notResize');//添加禁用托宽功能
        }
        return that
    }

    TreeTable.prototype.init = function(options){
        this.ligerObj = $(this.config.elem).ligerGrid(this.config);
        this.setWindowValue($(this.config.elem));
    }

    TreeTable.prototype.setWindowValue = function(jThis) {
        var that = this;
        var bizName = jThis.data('biz-name'.toLowerCase());
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            window.biz[that.bizName][bizName] = {ligerObj:this.ligerObj};
        }
    }

    var treeTable = new TreeTable();

    var obj = {
        init:function(ops){
            treeTable.set(ops);
            treeTable.init();
            return this;
        },
        initMulTreeTable:function(ops){ // 初始化多颗树表格
            var treeTable1 = new TreeTable();
            treeTable1.set(ops);
            treeTable1.init();
            return this;
        },
    }

    //输入口
    exports('ligerTreeTable', obj);
});
