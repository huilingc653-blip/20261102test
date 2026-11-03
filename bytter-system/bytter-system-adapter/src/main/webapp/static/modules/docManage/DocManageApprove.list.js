var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

        //客商选择
        bt.bankinfoCallback = function (data, dom) {
//            $("#payerBankNo").val(data.bankCode);
//            $("#payerBankName").val(data.bankName);
        };

    importExcel = function () {
        up = bt.btImport.init({
            code: "10000062",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/accountReceivable/docManage/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'asc'],[13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/accountReceivable/docManage/datatableApprove"
        },
        buttons: [
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
            {data: "code", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "batchBusTypeName", bSortable: true},
            {
                data: "source", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.docSource", full.source);
                }
            },
            {
                data: "voucherType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("account.receivable.doc.voucherType", full.voucherType);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisReceivable.status", full.status);
                }
            },
            {data: "receiptDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "agingDate", bSortable: true},
            {
                data: "receivableMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.receivableMoney);
                }
            },
            {data: "curName", bSortable: true},
            {data: "externalName", bSortable: true},
             {
                data: "externalType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            {data: "contractCode", bSortable: true},

            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                 if ($("#flowStatus").val() == 'todo') {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                }
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
                return html;
            }
            }
        ]
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

bt.corpCallback = function (id) {
    // 重新设置业务类别
    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
    bt.selectType.get("eqs_busType").attr("data-url", busTypeUrl);
    form.render()
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/accountReceivable/docManage/view?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/docManage/view?id=" + data.id
    });
}
/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/accountReceivable/docManage/viewDocApprove?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/docManage/viewDocApprove?id=" + data.id + "&taskId=" + data.taskId
    });
}
//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
