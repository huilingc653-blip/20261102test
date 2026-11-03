var datatable;
var dateFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'selectType', 'element','selectTable', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();


    //单位回调函数
    bt.corpCallback = function (id) {
        //重新加载收款账号
        bankAccInit.get("bankAcc").reload({ corpId: id });
    }
    //上级单位回调函数
    bt.superiorCorpCallback = function (id) {
        //重新加载收款账号
        superBankAccInit.get("superBankAcc").reload({ corpId: id });
    }
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    dateFun = bt.dateType.getFun();
    bt.dicts.init();
    bt.mulselect.init();
    //需求账号
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/netbank/nisdemand/getBankAccQuery",
        urldata: {resource:'netbank:nisDemand:query'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#eqs_bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#eqs_bankAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });

    var superBankAccInit = bt.selectTable.init({
        elem: "#superBankAccTable",
        url: basePath + "/netbank/nisdemand/getBankAccQuery",
        urldata: {resource:'netbank:nisDemand:query'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#eqs_superBankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#eqs_superBankAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1,'desc']],
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nisdemand/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:nisDemand:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:nisDemand:allocation'),
                text: "生成下拨付款指令",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    generateAllocation(data);
                }
            }, {
                hide: bt.isnotResource('netbank:nisDemand:super'),
                text: "生成上级子账户需求",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    generateDemand(data);
                }
            }, {
                hide: bt.isnotResource('netbank:nisDemand:apply'),
                text: "批量作废",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchDefeat(data);
                }
            }, {
                hide: bt.isnotResource('netbank:nisDemand:apply'),
                text: "批量设置上级账户",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetSuperAcc(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "accName", bSortable: false},
            {data: "bankName", bSortable: false},
            {data: "demandType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisDemand.demandType",full.demandType);
                }
            },
            {data: "demandDate", bSortable: true},
            {data: "demandMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.demandMoney);
                }
            },
            {data: "payBillNo", bSortable: true},
            {data: "payBillDate", bSortable: true},
            {data: "superCorpName", bSortable: false},
            {data: "superBankAcc", bSortable: true},
            {data: "superAccName", bSortable: false},
            {data: "superBankName", bSortable: false},
            {data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisDemand.status",full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:nisDemand:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defeat(this)">作废</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'demandMoney'
                }
            ]
        }
    });
});

//批量作废
function batchDefeat(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm("确定作废？", function () {
        $.post(basePath + "/netbank/nisdemand/batchDefeat", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量选择付款账号
function batchSetSuperAcc(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var corpId = "";
    for (var i = 0; i < rowData.length; i++) {
        //校验付款单位是否一致
        if (corpId == "") {
            corpId = rowData[i].corpId;
        } else {
            if (corpId != rowData[i].corpId) {
                layer.alert("批量设置上级账户单位要相同！", {icon: 7});
                return false;
            }
        }

    }
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisdemand/toSetSuperAcc/" + corpId
    });
}

//生成下拨付款指令
function generateAllocation(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm("生成下拨付款指令？", function () {
        $.post(basePath + "/netbank/nisdemand/generateAllocation", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//生成上级子账户需求
function  generateDemand(data){
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var billNos = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        if(rowData[i].isSummary == "1") {
            if (billNos == "") {
                billNos += rowData[i].billNo;
            } else {
                billNos += "," + rowData[i].billNo;
            }
        }
    }
    if(billNos != ""){
        layer.alert("单号："+billNos+"已经汇总过不能再次汇总生成上级需求单！", {icon: 7});
        return;
    }
    parent.layer.confirm("生成上级子账户需求？", function () {
        $.post(basePath + "/netbank/nisdemand/generateDemand", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/netbank/nisdemand/add");
        return;
    }
}

function defeat(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm("确定作废？", function () {
        $.post(basePath + "/netbank/nisdemand/defeat/" + data.id , function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/nisdemand/update?id=" + data.id);
            return;
        }
    }
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisdemand/view/" + data.id);
        return;
    }
}











