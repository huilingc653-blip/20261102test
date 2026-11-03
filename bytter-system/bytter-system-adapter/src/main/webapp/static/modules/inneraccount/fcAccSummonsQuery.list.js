bt.use(['form', 'datatable', 'corp', 'selectType', 'dateType', 'dicts', 'money'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.dateType.init();
    bt.dicts.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        order: [[1, 'desc']],
        pageSizeOptions: [10,20,50,100],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox: true,
        ajax: {
            'url': basePath + "/inneraccount/fcaccsummons/datatable"
        },
        buttons: [
            {
                text: bt.lang('print.internal.summonsVoucher'),
                action: function (e, dt, node, config) {
                    batchPrint(e, dt, node, config);
                }
            },
        ],
        columns: [
            {data: "voucherNo", bSortable: true},
            {
                data: "sumMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.sumMoney);
                }
            },
            {
                data: "voucherType", bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("summons.voucherType", full.voucherType);
                }
            },
            {data: "voucherName", bSortable: true},
            {data: "tallyDate", bSortable: true},
            {data: "summaryContent", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.voucherType) && full.voucherType != "40" && full.voucherType != "50"){
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this,1)">' + bt.lang("common.button.print") + '</a>';
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="view(this,0)">' + bt.lang("common.button.view") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
});

function view(el,viewSign) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var params;
    if (bt.empty(data.voucherType)) {
        layer.alert(bt.lang('table.fcAcc.summonsError'), {icon: 7});
        return;
    } else {
        params = "orderNo=" + data.orderNo + "&voucherType=" + data.voucherType + "&viewSign=" + viewSign
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccsummons/view?" + params
    });
}

/**
 * 批量打印
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPrint(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var orderNos = "";
    var voucherTypes = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if(!bt.empty(rowData[i].voucherType) && (rowData[i].voucherType == "10" || rowData[i].voucherType == "20" || rowData[i].voucherType == "30")){
            if (orderNos == "") {
                orderNos = rowData[i].orderNo;
                voucherTypes = rowData[i].voucherType;
            }else{
                orderNos += "," + rowData[i].orderNo;
                voucherTypes += "," + rowData[i].voucherType;
            }
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccsummons/batchPrint?orderNos=" + orderNos+"&voucherTypes="+voucherTypes
    });
}