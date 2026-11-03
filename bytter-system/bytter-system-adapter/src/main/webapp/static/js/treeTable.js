/**
 * 树表功能
 * 本组件依赖layui的from，以及jquery
 * 周亮
 * 2017年4月25日
 * 0.0.1 树形表格基础功能实现
 * 0.0.2　增加elem可以传入jq对象,并添加initialState属性,即展开子节点
 */
layui.define(['jquery','treeTableCore'],function(exports){
    var $  = layui.jquery;
    var TreeTable = function () {
        this.config ={
            elem:undefined
        }
    }


    TreeTable.prototype.v = '0.0.1';

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    TreeTable.prototype.init = function (options){
        if (options.elem === undefined) {
            throwError('TreeTable Error:请配置参数elem,此参数为jQuery选择器!');
        }

        var that = this;
        $.extend(true, that.config, options);
        var _config = that.config;

        var table;
        if ( typeof _config.elem === 'object' ) {
            table = _config.elem;
            _config.initialState = 'expandAll'
        }else {
            table = $(_config.elem)
        }
        _config.expandable = true;
        table.treetable(_config);
        if ( typeof _config.elem !== 'object' ) {
            table.treetable('expandAll');
        }
        return table;
    }

    function throwError(msg) {
        throw new Error(msg);
    };

    var treeTable = new TreeTable();
    var obj = {
        init: function(option){
            return treeTable.init(option);
        }
    };
    //输出test接口
    exports('treeTable', obj);
});
