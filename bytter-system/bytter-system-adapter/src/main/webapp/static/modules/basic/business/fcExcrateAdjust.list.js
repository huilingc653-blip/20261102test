var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType', 'dicts','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    bt.dateType.init();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[3, 'desc'], [1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/fcexcrateadjust/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    batchSubmit(e, dt, node, config);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "typeName", bSortable: true, className: "center"},
            {data: "curName", bSortable: true, className: "center"},
            {data: "referCurName", bSortable: false, className: "center"},
            {data: "preTime", bSortable: true, className: "center",},
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
                data: "status", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("fcExcrateAdjust.status", full.status);
                }
            },
            {data: "rmk", bSortable: false, className: "textRight"},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if ((full.status === 0 || full.status === -1) && $("#loginName").val() === full.createBy) {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

    function add() {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/basic/business/fcexcrateadjust/add");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/fcexcrateadjust/add",
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }

    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/fcexcrateadjust/view/" + data.id
        });
    }

    update = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/basic/business/fcexcrateadjust/update?id=" + data.id);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/basic/business/fcexcrateadjust/update?id=" + data.id,
                end: function () {
                    bt.datatable.refreshCurrentPage();
                }
            });
        }
    }

    deleteColumn = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/basic/business/fcexcrateadjust/delete/" + data.id, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };

    /**
     * 批量提交
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    function batchSubmit(e, dt, node, config) {
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
        parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
            $.post(basePath + "/basic/business/fcexcrateadjust/batchSubmit/", {ids: ids}, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            }, 'JSON');
        });
    }

});


