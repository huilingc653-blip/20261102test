var datatable;
bt.use(['form', 'datatable', 'select', 'selectTable', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'bank','mulSelectBankFinancePage','choProductRedeem'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/chowmatistic/chobankfinance/datatable"
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
            {data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.status == -1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "corpName", bSortable: true},
            {
                data: "busType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("choBankFinance.busType", full.busType);
                }
            },
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "dayLimit", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return full.dayLimit + bt.dict("choBankFinance.dateUnit", full.dateUnit);
                }
            },
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "payeeAcc", bSortable: true},
            {data:"isOnline",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isOnline);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisBillhead.status", full.status);
                }
            },
            {
                data: "voucherStat", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bisExc.voucherStat", full.voucherStat);
                }
            },
            {data: "createBy", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                    //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    if (full.excId && full.excId != "") {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewExc(this)">' + bt.lang("common.button.viewBisExc") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/chowmatistic/chobankfinance/getBankAcc",
        urldata: {corpId: $("input[name='corpId']").val(), busType: 10},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccId').val(data.id);
                $('#curId').val(data.curId);
            } else {
                $('input.text').val('');
            }
        }
    });

    //单位回调函数
    bt.corpCallback = function (id) {
        //重新加载付款账号
        payerAccInit.get("payerAcc").reload({corpId: id, busType: $("#busType").val()});

        //获取单位用途
        $.ajax({
            url: basePath + "/basic/business/btpurpose?getPurpose",
            method: 'POST',
            data: {corpId: id, sysBusType: 4},
            dataType: 'json',
            success: function (data) {
                $("#purpose").empty();
                $("#purpose").append("<option value=' '></option><option value='inputText'></option>");
                if (data && data.obj && data.obj.length > 0) {
                    $.each(data.obj, function (index, item) {
                        $("#purpose").append("<option value=" + item.purpose + ">" + item.purpose + "</option>");
                    });
                } else {
                    $("#purpose").append("<option value=''></option>");
                }
                form.render();
            }
        });
    }

    //初始化单位
    bt.corp.init();

    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/increment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }


    // var mulSelect = bt.choProductRedeem.init();
    // mulSelect.set({
    //     "corpId": '402880425b8abcfb015b8ac16e980000',
    // });
    // bt.choProductRedeemCallback = function (data, dom) {
    //     console.log(data)
    //     //点击选择后
    //     $("#payeeSwiftCode").val(data.id)
    // };
    //
    // var mulSelect2 = bt.mulSelectBankFinancePage.init();
    // mulSelect2.set({
    //     "corpId": '402880425b8abcfb015b8ac16e980000',
    //     "busType": '11',//11定期转活期，13通知转活期
    // });
    // bt.bankFinanceCallback = function (data, dom) {
    //     console.log(data)
    //     //点击选择后
    //     $("#bankFinance").val(data.id)
    // };


    bt.dicts.init();
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/chobankfinance/view/" + data.id
    });
}

//指令查看
function viewExc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisfinanceexc/view/" + data.excId
    });
}