bt.use(['form', 'datatable', 'dicts', 'dateType', 'selectType', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.moneyType.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        ignoreButtons: ['colvis', 'output', 'print'],
        colReorder: true,
        checkbox: true,
        selectCss: true,
        clickRowSelected:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/receipt/bisbankreceiptimport/accDtldatatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptImport:apply'),
                text: bt.lang('common.button.sure'),
                className: 'manualButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        //var data = $('#appTable').DataTable().rows('.selected').data();
                        var dtlData = dt.rows('.selected').data();
                        manual(dtlData);
                    }
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "corpName", className: 'center', bSortable: false},
            {data: "transTime", className: 'center', bSortable: false},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "bankAcc", className: 'center', bSortable: false},
            {data: "accName", className: 'center', bSortable: false},
            {data: "curName", className: 'center', bSortable: false},
            {data: "cdSignName", className: 'center', bSortable: false},
            {data: "oppAccNo", className: 'center', bSortable: false},
            {data: "oppAccName", className: 'center', bSortable: false},
            {data: "remark", className: 'center', bSortable: false},
            {data: "uses", className: 'center', bSortable: false}
        ]
    });
});

function manual(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
        return false;
    }
    if (data.length > 1) {
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.overMax'), {icon: 2});
        return false;
    }
    //电子回单id
    var receiptId=$("#billId").val();
    if (data[0].receiptBlendSign == '1') {
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
        return false;
    }
    var dtlId = data[0].id;

    bt.loading.show();
    $.ajax({
        url: basePath + "/receipt/bisbankreceiptimport.do?blendDtl",
        method: 'POST',
        data: {receiptId: receiptId, dtlId: dtlId},
        dataType: 'json',
        success: function (result) {
            bt.loading.hide();
            if (result.success == true) {
                //parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                //bt.datatable.refreshCurrentPage();
                parent.bt.datatable.refreshCurrentPage();
                bt.navtab.getCurrentDom().returnMsg(bt.lang('bankacc.message.paymentblending.success'));
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
        }
    });

}

//取消勾兑
function cancel(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
        return false;
    }
    var dtlId = "";
    for (var i = 0; i < data.length; i++) {
        if (data[i].receiptBlendSign != '1') {
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
            return false;
        }
        if (dtlId == "") {
            dtlId += data[i].id;
        } else {
            dtlId += "," + data[i].id;
        }
    }

    bt.loading.show();
    $.ajax({
        url: basePath + "/receipt/bisbankreceiptimport.do?cancel",
        method: 'POST',
        data: {dtlId: dtlId},
        dataType: 'json',
        success: function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert(bt.lang('bankacc.message.cancel.paymentblending.success'), {icon: result.success ? 1 : 2});
                bt.datatable.refreshCurrentPage();
                parent.bt.datatable.refreshCurrentPage();
                //parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
        }
    });

}
