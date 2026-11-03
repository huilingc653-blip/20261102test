bt.use(['form', 'datatable', 'dateType', 'selectType', 'dicts', 'moneyType', 'accountTable','corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.selectType.init();
    bt.corp.init();
    accountPayerCallBack = function (data) {
        $("#likes_payerAcc").val(data.bankAcc);
    }
    accountPayeeCallBack = function (data) {
        $("#likes_payeeAcc").val(data.bankAcc);
    }
    bt.accountTable.init();
    //生成列表
    var htmlx =
        '<tr class="title">'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.billNo')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.type')+'</th>'+
            '<th colspan="4">'+bt.lang('table.nisAllocationReq.payerAcc')+'</th>'+
            '<th colspan="4">'+bt.lang('table.nisAllocationReq.payeeAcc')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.curId')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.wishPayday')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.purpose')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.money')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.createBy')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.createDate')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.status')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.bisExc.voucherStat')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.billFrom')+'</th>'+
        '</tr>'+
        '<tr class="title">'+
            '<th>'+bt.lang('table.nisAllocationReq.acc')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.corp')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.accName')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.bankName')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.acc')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.corp')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.accName')+'</th>'+
            '<th style="border-right: none;">'+bt.lang('table.nisAllocationReq.bankName')+'</th>'+
        '</tr>';

    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false,
        checkbox: false,
        order: [[0,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/allocation/nisallocationreq/datatable"
        },
        exportFile: {
            customPrint: {
                header: function () {
                    var printHtml = htmlx;
                    return printHtml;
                },
            },
        },
        customExportAll:function(params, processing, data){
            var exportUrl = basePath + "/allocation/nisallocationreq/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
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
            {data: "billNo", bSortable: true},
            {data: "typeName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerCorpName", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBankName", bSortable: false},
            {data: "payeeAcc", bSortable: true},
            {data: "payeeCorpName", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankName", bSortable: false},
            {data: "curName", bSortable: true},
            {data: "wishPayday", bSortable: true},
            {data: "purpose", bSortable: true},
            {
                data: "money", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.money);
                }
            },
            {data: "createByName", bSortable: true},
            {data: "createDate", bSortable: true},
            { data: "statusName", bSortable: true},
            {data: "voucherStatName", bSortable: true},
            {data: "billFrom", bSortable: false, render: function (data, type, full, meta) {
                    return bt.dict("nisAllocationReq.billFrom", full.billFrom);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                    //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    if ((full.bisExcId && full.bisExcId != "") || (full.bisGatherCmdId && full.bisGatherCmdId != "")) {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewBisExc(this)">' + bt.lang("common.button.viewBisExc") + '</a>';
                    }
                    return html;
                }
            }
        ],
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.voucherStat == "9" || aData.voucherStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
        // summary: {
        //     allSum: true,
        //     sumList: [
        //         {
        //             colNum: '10',
        //             dataName: 'money'
        //         }
        //     ]
        // }
    });


    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/nisallocationreq/view/" + data.id
    });
}

//指令查看
function viewBisExc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var path = "/bdlink/bisexc/view/" + data.bisExcId;
    if (data.type == "10" || data.type == "14") {
        path = "/bdlink/bisgathercmd/view/" + data.bisGatherCmdId;
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '530px'],
        maxmin: true,
        content: basePath + path
    });
}
