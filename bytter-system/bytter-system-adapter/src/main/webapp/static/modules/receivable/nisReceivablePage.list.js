var datatable;
var moneyFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();
    moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc'],[13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/receivable/nisreceivable/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.select'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSelect(data);
                }
            }
        ],
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "busTypeName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppBankCode", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "transTime", bSortable: true, className: 'center'}
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
                    dataName: 'amt'
                }
            ]
        }
    });

});

//选择
function batchSelect(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var amtTotal = 0;
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "" && amtTotal == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        amtTotal = moneyFun.add(amtTotal,rowData[i].amt);
    }

    var amt = moneyFun.toNumber($("#amt").val());
    var bankSerialNo = $("#bankSerialNo").val();
    if(bt.money.sub(amt, amtTotal) != 0) {
        layer.alert(bt.lang("common.nisReceivable.amt.errorMessage"));
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/receivable/nisreceivable/batchSelect", {ids: ids,bankSerialNo:bankSerialNo}, function (result) {
            parent.layer.closeAll();
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshParentWithoutDatatable();
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');

    });
    return false;
}
