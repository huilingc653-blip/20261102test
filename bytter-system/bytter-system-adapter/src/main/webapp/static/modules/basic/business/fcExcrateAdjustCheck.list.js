var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType','dateType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dateType.init();
    bt.dicts.init();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[3, 'desc'],[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/fcexcrateadjust/datatableCheck"
        },
        buttons: [
            {
                text: bt.lang('common.button.batchCheck'),
                action: function (e, dt, node, config) {
                    batchCheck(e, dt, node, config);
                }
            },
            {
                text: bt.lang('common.button.batchBack'),
                action: function (e, dt, node, config) {
                    batchBack(e, dt, node, config);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "typeName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "referCurName", bSortable: false},
            {data: "preTime", bSortable: true},
            {
                data: "oldExchangeRate", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    if(!full.oldExchangeRate){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: full.decimalPlaces
                    }
                    return moneyFun.format(full.oldExchangeRate, opt);
                }
            },
            {
                data: "newExchangeRate", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var opt = {
                        separator: ",",
                        decimal: full.decimalPlaces
                    }
                    return moneyFun.format(full.newExchangeRate, opt);
                }
            },
            {
                data: "foreignExchangePurchase", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    if(!full.foreignExchangePurchase){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: full.decimalPlaces
                    }
                    return moneyFun.format(full.foreignExchangePurchase, opt);
                }
            },
            {
                data: "foreignExchangeSell", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    if(!full.foreignExchangeSell){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: full.decimalPlaces
                    }
                    return moneyFun.format(full.foreignExchangeSell, opt);
                }
            },
            {
                data: "foreignExchangeMiddle", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    if(!full.foreignExchangeMiddle){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: full.decimalPlaces
                    }
                    return moneyFun.format(full.foreignExchangeMiddle, opt);
                }
            },
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("fcExcrateAdjust.status", full.status);
                }
            },
            {data: "rmk", bSortable: false},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="check(this)">' + bt.lang("common.button.check") + '</a>';
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="backColumn(this)">' + bt.lang("common.button.back") + '</a>';
                    return html;
                }
            }
        ]
    });

    /**
     * 复核
     * @param el
     */
    check = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.check'), function () {
            $.post(basePath + "/basic/business/fcexcrateadjust/check/", {id: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    /**
     * 打回
     * @param el
     */
    backColumn = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.back'), function () {
            $.post(basePath + "/basic/business/fcexcrateadjust/back/", {id: data.id}, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            }, 'JSON');
        });
    }

    /**
     * 批量打回
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    function batchBack(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
            return;
        }
        var ids = "";
        var rowData = data.splice(0, data.length);
        for (var i = 0; i < rowData.length; i++) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }
        parent.layer.confirm(bt.lang('common.message.confirm.batchBack'), function () {
            $.post(basePath + "/basic/business/fcexcrateadjust/batchBack/", {ids: ids}, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            }, 'JSON');
        });
    }

    /**
     * 批量复核
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    function batchCheck(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
            return;
        }
        var ids = "";
        var rowData = data.splice(0, data.length);
        for (var i = 0; i < rowData.length; i++) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }
        parent.layer.confirm(bt.lang('common.message.confirm.batchCheck'), function () {
            $.post(basePath + "/basic/business/fcexcrateadjust/batchCheck/", {ids: ids}, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            }, 'JSON');
        });
    }
});
