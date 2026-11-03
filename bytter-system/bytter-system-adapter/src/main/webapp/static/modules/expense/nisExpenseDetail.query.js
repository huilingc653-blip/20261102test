bt.use(['form', 'datatable', 'dateType', 'selectType', 'corp', 'moneyType', 'dicts', 'bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //业务类型选择
    busTypeCallBack = function (data) {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=" + data.bustype);
    }
 opTypeCallBack = function (data) {
               var busTypeValue =selectType.get("busType").val();
               var opTypeValue =selectType.get("opType").val();
               if (opTypeValue!=""&&busTypeValue=="") {
                 parent.layer.alert(bt.lang('common.message.busTypeChose'));
                 $('select[name="eqs_opType"]').val("");
                  return false;
                  }
    }
    var selectType = bt.selectType.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/expense/nisexpensedetail/datatable"
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
            {data: "expenseId", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "expenseDate", bSortable: true},
            {
                data: "expenseMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.expenseMoney);
                }
            },
            {data: "accName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "bankCode", bSortable: true, className: 'center'},
            {data: "busTypeName", bSortable: true, className: 'center'},
            {data: "opTypeName", bSortable: true, className: 'center'},
            {
                data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.billNo != "" && full.billNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewNisBillhead(this)">' + full.billNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "purpose", bSortable: true},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisExpenseDetail.status", full.status);
                }
            },
            {
                data: "voucherStat", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    //避免误导：如果费用报销明细交易状态是3-准备发送且状态为100-已汇总前（如0-暂存/-1-打回/-2删除/11-待审批/95-审批通过），则不显示交易状态
                    if ((full.status < 100 && full.voucherStat == "3") || full.excVoucherStat == null || full.excVoucherStat == "") {
                        return "";
                    }else{
                        if(full.excVoucherStat == "6"){
                            //指令表的指令状态为6-待发送时，明细状态取指令表状态
                            return bt.dict("bisExc.voucherStat", full.excVoucherStat);
                        }else {
                            return bt.dict("bisExc.voucherStat", full.voucherStat);
                        }
                    }
                }
            },
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                    //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum: '3',
                    dataName: 'expenseMoney'
                }
            ]
        },
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.voucherStat == "9" || aData.voucherStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
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

    /**
     * 重置
     */
    resetSelect = function () {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=02");
    }
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/expense/nisexpensedetail/view/" + data.id
    });
}

function viewNisBillhead(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.billId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.billId
    });
}

//重置
function resetSelect() {
    $("#likes_opType").html('<option value=""></option>');
    form.render('select');
}