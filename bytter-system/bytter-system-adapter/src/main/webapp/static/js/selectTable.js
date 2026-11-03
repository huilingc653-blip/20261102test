/**
 * 联动组件
 * 本组件依赖jquery、datatable
 * 刘勇军
 * 2017年5月11日
 * 0.0.1 基础下拉表格
 * 0.0.2 修改成普通table
 */
bt.define(['jquery', 'selectTableType'], function (exports) {
    var $ = bt.jquery;
    var SelectTable = function () {
        this.config = {
            SelectTable: true,
        };
        this.tableObj
    }

    SelectTable.prototype.v = '0.0.2';

    SelectTable.prototype.set = function (ops) {
        var that = this;
        $.extend(true, that.config, ops);
        return that
    }

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    SelectTable.prototype.init = function () {
        var that = this;
        var tableElem = $(that.config.elem);
        var inputElem = that.config.searchInput;
        that.config.elem = inputElem;
        that.config.tableElem = tableElem;
        this.tableObj = bt.selectTableType.init(this.config);
    }

    SelectTable.prototype.get = function (ops) {
        var that = this;
        return that.tableObj.get(ops);
    }

    var obj = {
        init: function (ops) {
            var sTable = new SelectTable();
            sTable.set(ops);
            sTable.init();
            return sTable
        }
    };
    //输出test接口
    exports('selectTable', obj);
});