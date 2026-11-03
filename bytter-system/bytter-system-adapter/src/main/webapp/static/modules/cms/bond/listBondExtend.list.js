bt.use(['form', 'datatable', 'btdate', 'dateType', 'selectType', 'moneyType', 'rateType'], function () {
    var dateType = bt.dateType.init();
    bt.selectType.init()
    $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var registerId = $("#registerId").val();
    var isView = $("#isView").val();
    //生成列表
    if (isView == '0') {
        datatable = bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            order: [[1, 'asc']],
            colReorder: true,
            ajax: {
                'url': basePath + "/cms/bond/extend/datatable/" + registerId
            },
            buttons: [
                {
                    text: bt.lang('common.button.add'),
                    hide: bt.isnotResource('cms:bond:register:add'),
                    action: function (e, dt, node, config) {
                        add();
                    }
                },
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            }, columns: [
                {data: "extendApplyDate", className: 'center', bSortable: true},
                {data: "bondIssueNum", className: 'center', bSortable: true},
                {data: "extendCode", className: 'center', bSortable: true},
                {data: "extendEndDate", className: 'center', bSortable: true},
                {
                    data: "extendDeadline", className: 'center', render: function (data, type, full, meta) {
                        return data !== null ? data + bt.dict("remainTerm.unit",full.deadlineUnit) : ''
                    }
                },
                {
                    data: "basicPoint", className: 'textRight', render: function (data, type, full, meta) {
                        return basicPoint = bt.rateType.getFun().format(full.basicPoint);
                    }
                },
                {
                    data: "extendRate", className: 'textRight', render: function (data, type, full, meta) {
                        return extendRate = bt.rateType.getFun().format(full.extendRate);
                    }
                },
                {data: "rmk", bSortable: true},
                {data: "statusName", bSortable: true,
                    render: function ( data, type, full, meta ) {
                        if(full.status == 95){
                            return "审批通过";
                        } else {
                            return bt.dict("AB_BILL_EMITBILL_STATUS",full.status);
                        }
                    }
                },
                {
                    data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                        var html = ''
                        if (full.status !== 11 && full.status !== 95) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a> ';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a> ';
                        }
                        return html;
                    }
                }
            ]
        });
    } else {//查看
        datatable = bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            order: [[1, 'asc']],
            colReorder: true,
            ajax: {
                'url': basePath + "/cms/bond/extend/datatable/" + registerId
            },

            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            }
            , columns: [
                {data: "extendApplyDate", className: 'center', bSortable: true},
                {data: "bondIssueNum", className: 'center', bSortable: true},
                {data: "extendCode", className: 'center', bSortable: true},
                {data: "extendEndDate", className: 'center', bSortable: true},
                {
                    data: "extendDeadline", className: 'center', render: function (data, type, full, meta) {
                        return data !== null ? data + bt.dict("remainTerm.unit",full.deadlineUnit) : ''
                    }
                },
                {
                    data: "basicPoint", className: 'textRight', render: function (data, type, full, meta) {
                        return basicPoint = bt.rateType.getFun().format(full.basicPoint);
                    }
                },
                {
                    data: "extendRate", className: 'textRight', render: function (data, type, full, meta) {
                        return extendRate = bt.rateType.getFun().format(full.extendRate);
                    }
                },
                {data: "rmk", bSortable: true},
                {data: "statusName", bSortable: true,
                    render: function ( data, type, full, meta ) {
                        if(full.status == 95){
                            return "审批通过";
                        } else {
                            return bt.dict("AB_BILL_EMITBILL_STATUS",full.status);
                        }
                    }}
            ]
        });

    }

});

function add(el) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/bond/extend/edit?registerId=" + $('#registerId').val());
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/bond/extend/edit?registerId=" + $('#registerId').val(),
        end: function () {
            datatable && datatable.draw();
            bt.navtab.closeCurrent();
            bt.navtab.refreshParent();
        }
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab("债券展期", basePath + "/cms/bond/extend/edit?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/extend/edit?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/cms/bond/extend/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

