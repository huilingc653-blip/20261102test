var datatable;
bt.use(['form', 'datatable', 'dateType', 'select', 'corp', 'money', 'moneyType', 'bank', 'btImport', 'upload', 'dicts', 'selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();//下拉框
    bt.bank.init({
        elem: '.bank',
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bond/fcbondInfo/datatable1"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bond:fcbondback:apply'),//应用权限
                text: '编辑利率',
                action: function (e, dt, node, config) {
                    batchEditInterestRate(e, dt, node, config);
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
        },
        columns: [
            {data: "corpName", bSortable: true, className: 'textCenter'},
            {data: "bankAcc", bSortable: true, className: 'textCenter'},
            {data: "accName", bSortable: true, className: 'textCenter'},
            {data: "bankName", bSortable: true, className: 'textCenter'},
            {data: "curName", bSortable: true, className: 'textCenter'},
            {
                data: "balMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="balMoneyCall(this)">' + bt.money.format(full.balMoney) + '</a>';
                    return html;
                }
            },
            {
                data: "applyMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="applyMoneyCall(this)">' + bt.money.format(full.applyMoney) + '</a>';
                    return html;
                }
            },
            {
                data: "availMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.availMoney);
                }
            },
            {
                data: "backInterest", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.backInterest);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    // if(bt.isResource('netbank:expense:nisExpenseDetail:touchApply',full.corpId)){
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});


//查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/bond/fcbondInfo/viewacc/" + data.bondId);
        return;
    }
}

function balMoneyCall(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fc.bond.trans.dtl'), basePath + "/bond/fcbondInfo/BondTransDtl/" + data.bondId);
        return;
    }

}

function applyMoneyCall(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.fcBond.itemInfo'), basePath + "/bond/fcbondInfo/BondApplyDtl/" + data.bondId + "/null");
        return;
    }

}

// 编辑利率
function batchEditInterestRate(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    var len = data.length;
    if (len < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var bondIds = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (bondIds === "") {
            bondIds += rowData[i].bondId;
        } else {
            bondIds += "," + rowData[i].bondId;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '450px'],
        maxmin: true,
        title: '批量编辑利率',
        content: basePath + "/bond/fcbond/editFcBondInterestRate?bondIds=" + bondIds,
    });
    return false;
}

