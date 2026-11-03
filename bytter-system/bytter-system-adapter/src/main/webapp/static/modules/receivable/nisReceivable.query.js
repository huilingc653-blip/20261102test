bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    //单位回调函数
    bt.corpCallback = function (id) {
        //获取单位业务类别
        $.ajax({
            url: basePath + "/netbank/niscorpbus?getNisCorpBus",
            method: 'POST',
            data: {corpId: id, cdSign: '0'},
            dataType: 'json',
            success: function (data) {
                $("#busType").empty();
                $("#busType").append("<option value=' '>" + bt.lang("common.placeholder.select") + "</option>");
                if (data && data.obj && data.obj.length > 0) {
                    $.each(data.obj, function (index, item) {
                        $("#busType").append("<option value=" + item.busId + ">" + item.busTypeName + "</option>");
                    });
                } else {
                    $("#busType").append("<option value=''></option>");
                }
                form.render('select');
            }
        });
    }
    //获取单位业务类别
    $.ajax({
        url: basePath + "/netbank/niscorpbus?getNisCorpBusByCdSign",
        method: 'POST',
        data: {cdSign: '0'},
        dataType: 'json',
        success: function (data) {
            $("#busType").empty();
            $("#busType").append("<option value=' '>" + bt.lang("common.placeholder.select") + "</option>");
            if (data && data.obj && data.obj.length > 0) {
                $.each(data.obj, function (index, item) {
                    $("#busType").append("<option value=" + item.busId + ">" + item.busTypeName + "</option>");
                });
            } else {
                $("#busType").append("<option value=''></option>");
            }
            form.render('select');
        }
    });
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'asc'],[12, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/receivable/nisreceivable/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "busTypeName", bSortable: true},
            {data: "busBillNo", bSortable: true,className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (data != null) {
                        html = '<a href="#" data-row="' + meta.row + '" onclick="viewDtl(this)">' + data + '</a>';
                    }
                    return html;
                }},
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
            {data: "transTime", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisReceivable.status", full.status);
                }
            },
            {
                data: null,
                bSortable: false,
                anCells: true,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: false,
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
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/receivable/nisreceivable/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/receivable/nisreceivable/view/" + data.id
    });
}

function viewDtl(el) {
    var data = layui.jquery('#appTable').DataTable().row(layui.jquery(el).data('row')).data();
    if(data.sysBusType === '4' || data.sysBusType === '5'){//银行理财
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/chowmatistic/chobankfinance/view/" + data.billId
        });
    }else if(data.sysBusType === '6'){//理财赎回到账
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/chowmatistic/choproductredeem/view/" + data.billId
        })
    }else if(data.sysBusType === '7'){//贴现到账
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebill/abreceivebill/view?accId=" + data.billId
        })
    }else if(data.sysBusType === '8'){//托收到账
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebill/abreceivebill/view?accId=" + data.billId
        })
    }else if(data.sysBusType === '9'){//借款发放
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/provide/loansend/viewLoanInfo?id=" + data.billId
        })
    }else if(data.sysBusType === '10'){//信用证到期收款
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/international/credit/arrivalletter/viewArrovalInfo?flag=0&id=" + data.billId
        })
    }else if(data.sysBusType === '11' || data.sysBusType === '12' || data.sysBusType === '13' || data.sysBusType === '14'){//外汇
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexcashflow/view/"+ data.billId
        })
    }
}