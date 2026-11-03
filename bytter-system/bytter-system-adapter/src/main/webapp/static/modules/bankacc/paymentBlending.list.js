var dict, selectType, accountTable,moneyType,corp;

// 选择单位回调
corpCallback = function (id) {
    // 重新根据单位ID加载账户表格
    accountTable.get("likes_bankAcc").setCorp(id);
}

// 选择单位回调
corpCallback2 = function (id) {
    // 重新根据单位ID加载账户表格
    accountTable.get("eqs_payerAcc").setCorp(id);
}


/**
 * 结算类型选择回调
 */
settlTypeBatchCallback = function () {
    if ($("#dtlColSel").val() && dtlColSels["settlType"]) {
        var row = 0;
        if ($('#projectDetailTable' + row)) {
            $('#projectDetailTable' + row).DataTable().destroy();
        }
        $('#projectDetail' + row).parents("tr").remove();
        $("#projectDetailSum" + row).remove();
        var html = '<tr><td colspan="5" id="projectDetail0"></td></tr>';
        $("#budgetProcessDataTable").find("tbody >  tr[row=0]").after(html);
        initProjectDetailHtml(bt.itemType.get("itemInfo").val(), 0);
    }
}

bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect'], function () {
    var laydate = layui.laydate;
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });

    selectType = bt.selectType.init();

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
        selectType.get("eqs_curId").val(data.curId);
    }

    payerAccCallBack = function (data) {
        $("#eqs_payerAcc").val(data.bankAcc);
        selectType.get("eqs_curId").val(data.curId);
    }

    accountTable = bt.accountTable.init();

    corp = bt.corp.init();

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

    var statusValue = '';
    var eqs_id = $("#eqs_id").val();
    var initdate = false;
    if(eqs_id != ""){
        initdate = true;
    }
    var appTableDtl = bt.datatable.initMultiTables({
        elem: "#appTableDtl",
        checkbox: true,
        mulselect: true,
        selectCss: true,
        jumpPageSelect: false,//开启跨页选择
        isInitData: initdate,
        searchForm: {
            formElem: "#searchForm2",
            formFilter: "search2"
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/paymentblending/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:paymentBlending:apply'),
                text: bt.lang('common.button.auto.paymentblending'),
                className: 'aotuButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = dt.rows().data();
                        auto(data);
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:paymentBlending:apply'),
                text: bt.lang('common.button.manual.paymentblending'),
                className: 'manualButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = dt.rows('.selected').data();
                        manual(data);
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:paymentBlending:apply'),
                text: bt.lang('common.button.cancel.paymentblending'),
                className: 'cancelButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = dt.rows('.selected').data();
                        cancel(data);
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:paymentBlending:apply'),
                text: bt.lang('common.button.manual.scrap'),
                className: 'scrapButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        scrap();
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:paymentBlending:apply'),
                text: bt.lang('common.button.cancel.scrap'),
                className: 'cancelScrapButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        cancelScrap();
                    }
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "blendSignName", bSortable: true},
            {data: "paymentBlendingDate", bSortable: true},
            {data: "transTime", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "uses", bSortable: true}
        ],
        btClickRowCallback: function (data) {
            if (data[0].isSelect == true) {
                billTableInit(data[0]);
            }
        },
        btDrawCallback: function (setting) { //回调方法
            var blendSign = dict.get('blendSign').val();
            $('.aotuButton').removeClass('layui-disabled');
            $('.manualButton').removeClass('layui-disabled');
            $('.cancelButton').removeClass('layui-disabled');
            $('.scrapButton').removeClass('layui-disabled');//作废
            $('.cancelScrapButton').removeClass('layui-disabled');
            if (blendSign === '0') {//未勾兑
                $('.cancelButton').addClass('layui-disabled');
            }
            if (blendSign === '1') {//已勾兑
                $('.aotuButton').addClass('layui-disabled');
                $('.manualButton').addClass('layui-disabled');
            }
            $("#eqs_bankSerialNo").val("#");
            bt.datatable.refreshTable("appTableBill");
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bankacc/paymentblending/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    })

    /**付款明细勾兑交易时间 begin*/
    var endDate = laydate.render({
        elem: '#endDate',//选择器
        type: 'date',//日期类型
        min: "1970-1-1",//默认最小值
        btns: ['clear'],
        done: function (value, date) {//点击确认后回调
            startDate.config.max = {
                year: date.year,
                month: date.month - 1,
                date: date.date,
                hours: 0,
                minutes: 0,
                seconds: 0
            }
        }
    });
    var startDate = laydate.render({
        elem: '#startDate',
        type: 'date',
        max: "2099-12-31",//默认最大值
        btns: ['clear'],
        done: function (value, date) {//点击确认后回调
            endDate.config.min = {
                year: date.year,
                month: date.month - 1,
                date: date.date,
                hours: 0,
                minutes: 0,
                seconds: 0
            };
        }
    });
    /** 付款明细勾兑交易时间 end*/
});
var tblBill = null;

//自动勾兑
function auto(data) {
    var bankAccTypeStr = '';
    var ids = '';
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
    })
    if (bankAccTypeStr.trim() == "") {
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.matchingrules'), {icon: 2});
        return false;
    }
    for (var i = 0; i < data.length; i++) {
        if (data[i].blendSign == '1') {
            parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
            return false;
        }
        ids += "" + data[i].id + ",";
    }
    bt.loading.show();
    $.ajax({
        url: basePath + "/bankacc/paymentblending?auto",
        method: 'POST',
        data: {ids: ids, bankAccTypeStr: bankAccTypeStr},
        dataType: 'json',
        success: function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                bt.datatable.refreshTable("appTableDtl");
                $("#eqs_bankSerialNo").val("#");
                bt.datatable.refreshTable("appTableBill");
            } else {
                parent.layer.alert(bt.lang('bankacc.message.notDate'), {icon: result.success ? 1 : 2});
            }
        }
    });

}

//手工勾兑
function manual(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
        return false;
    }
    var dtlId =  "";
    for (var i = 0; i < data.length; i++) {
        if (data[i].blendSign == '1') {
            parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
            return false;
        }
        if(isNull(dtlId)){
            dtlId = data[i].id;
        }else{
            parent.layer.alert(bt.lang('只能选择一笔交易明细数据'), {icon: 2});
            return false;
        }
    }
    if (tblBill) {
        var table = $('#appTableBill').DataTable();
        var billData = table.rows('.selected').data();
        if (billData.length == 0) {
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'), {icon: 2});
            return false;
        }
        var billId="";
        for (var j = 0; j < billData.length; j++) {
            if (isNull(billId)){
                billId = billData[j].id;
            }else{
                parent.layer.alert(bt.lang('只能选择一笔付款单数据'), {icon: 2});
                return false;
            }

        }
        var isFrom = billData[0].isFrom;
        bt.loading.show();
        $.ajax({
            url: basePath + "/bankacc/paymentblending?manual",
            method: 'POST',
            data: {dtlId: dtlId, billId: billId, isFrom: isFrom},
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success == true) {
                    parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTableDtl", true);
                    $("#eqs_bankSerialNo").val("#");
                    bt.datatable.refreshTable("appTableBill", true);
                }
            }
        });
    } else {
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'), {icon: 2});
        return false;
    }
}

//取消勾兑
function cancel(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
        return false;
    }
    var dtlId;
    for (var i = 0; i < data.length; i++) {
        if (data[i].blendSign == '0') {
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
            return false;
        }
        dtlId += "," + data[i].id;
    }
    if (tblBill) {
        /*var table = $('#appTableBill').DataTable();
        var billData = table.rows('.selected').data();
        if (billData.length == 0) {
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'), {icon: 2});
            return false;
        }
        var billId;
        for (var j = 0; j < billData.length; j++) {
            billId += "," + billData[j].id;
        }
        var isFrom = billData[0].isFrom;*/
        bt.loading.show();
        $.ajax({
            url: basePath + "/bankacc/paymentblending?cancel",
            method: 'POST',
            data: {dtlId: dtlId},
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success == true) {
                    parent.layer.alert(bt.lang('bankacc.message.cancel.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTableDtl");
                    $("#eqs_bankSerialNo").val("#");
                    bt.datatable.refreshTable("appTableBill");
                }
            }
        });
    } else {
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'), {icon: 2});
        return false;
    }
}

//作废
function scrap() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.manual.scrap'), basePath + "/bankacc/paymentblending/scrap/0");
        return;
    }
}

//取消作废
function cancelScrap() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.cancel.scrap'), basePath + "/bankacc/paymentblending/scrap/2");
        return;
    }
}

//根据所选明细获取匹配的付款单信息
function billTableInit(data) {
    var bankAccTypeStr = '';
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
    })
    clearForm();
    $("#eqs_bankSerialNo").val(data.id);
    if (data.blendSign != '1') {
        if (bankAccTypeStr.indexOf("1") != -1) {
            moneyType.get('geg_payMoney').val(data.amt);
            moneyType.get('leg_payMoney').val(data.amt);
        }
        if (bankAccTypeStr.indexOf("2") != -1) {
            $("#eqs_payerAcc").val(data.bankAcc);
        }
        if (bankAccTypeStr.indexOf("3") != -1) {
            if(data.oppAccNo == null || data.oppAccNo == ""){
                $("#eqs_payeeAcc").val("");
            }else {
                $("#eqs_payeeAcc").val(data.oppAccNo);
            }
        }
        if (bankAccTypeStr.indexOf("4") != -1) {
            var time = data.transTime;
            time = time.substring(0, time.indexOf(" "));
            $("#wishPayday").val(time);
        }
        if (bankAccTypeStr.indexOf("5") != -1) {
            $("#eqs_curId").val(data.curId);
        }
    }
    var blendSign = dict.get('blendSign').val();
    if (null == tblBill) {
        tblBill = bt.datatable.initMultiTables({
            elem: "#appTableBill",
            checkbox: true,
            mulselect: true,
            selectCss: true,
            jumpPageSelect: false,//开启跨页选择
            searchForm: {
                formElem: "#searchFormBill",
                formFilter: "searchBill"
            },
            order: [[1, 'desc']],
            ajax: {
                'url': basePath + "/bankacc/paymentblending/billdatatable"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            columns: [
                {data: "billNo", bSortable: true},
                {data: "payerAcc", bSortable: true},
                {data: "payerAccName", bSortable: true},
                {data: "curName", bSortable: true},
                {
                    data: "payMoney", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.payMoney);
                    }
                },
                {data: "voucherName", bSortable: true},
                {data: "payeeAcc", bSortable: true},
                {data: "payeeAccName", bSortable: true},
                {
                    data: "busType",
                    bSortable: true,
                    className: 'select-column',
                    render: function (data, type, full, meta) {
                        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + full.corpId;
                        var html = '';
                        if(full.isFrom != "fex_cash_flow" && full.isFrom != "nis_allocation_req") {
                            html += '<div class="bt-biz-selectType" data-name="busType" data-biz-name="busType' + meta.row + '" data-defaultvalue="' + full.busType + '" ';
                            html += 'data-url="' + busTypeUrl + '" data-valuefield="busId" data-textfield="busTypeName" data-search="true" data-beforeinit="busBefore"></div>';
                        }else{
                            var type = full.busType
                            if(full.busType == null){
                                type = '';
                            }
                            html += type;
                        }
                        return html;
                    }
                },
                {data: "purpose", bSortable: true},
                {
                    data: "null", bSortable: true, className: 'center', render: function (data, type, full, meta) {
                        var html = '';
                        if(full.isFrom != "fex_cash_flow" && full.isFrom != "nis_allocation_req") {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="toSave(\'' + full.id + '\',\'' + meta.row + '\',\'' + full.isFrom + '\')">' + bt.lang("common.button.save") + '</a>';
                        }
                        html += '&nbsp;<a class="" href="#" data-row="' + meta.row + '" onclick="toView(\'' + full.id + '\',\'' + full.isFrom + '\')">' + bt.lang("common.button.view") + '</a>';
                        if (full.bankSerialNo != null) {
                            html += '&nbsp;<a class="" href="#" data-row="' + meta.row + '" onclick="toDtlList(\'' + full.id + '\',\'' + full.isFrom + '\')">' + bt.lang("common.button.cancel.paymentblending") + '</a>';
                        }
                        return html;
                    }
                }
            ],
            btDrawCallback: function () {
                selectType = bt.selectType.init();
                checkRow(0, true, $("#appTableBill"));
            },

        });
    } else {
        bt.datatable.refreshTable("appTableBill");
    }
}

//保存业务类型
function toSave(id, busType, isFrom) {
    var type = selectType.get("busType" + busType).getData();
    $.ajax({
        url: basePath + "/bankacc/paymentblending?toSaveBillBusType",
        method: 'POST',
        data: {id: id, busType: type.value, isFrom: isFrom},
        dataType: 'json',
        success: function (result) {
            if (result.success == true) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.datatable.refreshTable("appTableBill");
            }
        }
    });
}

function toView(id, from) {
    var url = "";
    if (from == 'nis_wage') {
        url = basePath + "/wage/niswage/view/" + id
    } else if (from == "nis_billhead") {
        url = basePath + "/netbank/nisbillhead/view/" + id
    } else if(from == "fex_cash_flow"){
        url = basePath + "/foreign/fexcashflow/view/" + id
    } else if(from == "nis_allocation_req"){
        url = basePath + "/allocation/nisallocationreq/view/" + id
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: url
    });
}

function toDtlList(id, from) {
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/paymentblending/view/" + id + "/" + from
    });
}

function clearForm() {
    moneyType.get('geg_payMoney').val("");
    moneyType.get('leg_payMoney').val("");
    $("#eqs_payerAcc").val("");
    $("#eqs_payeeAcc").val("");
    $("#wishPayday").val("");
    $("#eqs_curId").val("");
}

//根据所选明细获取匹配的付款单信息
function getPayBill() {
    var data = bt.datatable.getSelectData("#appTableDtl");
    if (data.length > 0) {
        billTableInit(data[0]);
    }
}

function checkRow(rowIndex, checked, elem) {
    var tables = elem.parents('.DTFC_ScrollWrapper').find('table');
    //获取页面贮存的实例
    var currentTableList = elem.data('table');

    for (var i = 0; i < tables.length; i++) {
        var clickTr = $(tables[i]).find("tbody tr:eq(" + rowIndex + ") ");
        if (!clickTr.length) {
            continue;
        }
        checked ? clickTr.addClass(currentTableList.selectedStyle) : clickTr.removeClass(currentTableList.selectedStyle);
        if (clickTr.find("td:first-child input").length != 0) {
            clickTr.find('input[type="checkbox"]').prop("checked", checked);
        }
    }
    form.render('checkbox');
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
function chearBill() {
    corp.get("ins_corpId").val("");
    $("#eqs_payerAcc").val("");
    $("#likes_payerAccName").val("");
    $("#eqs_payeeAcc").val("");
    $("#likes_payeeAccName").val("");
    moneyType.get("geg_payMoney").val("");
    moneyType.get("leg_payMoney").val("");

}
function isNull(data) {
    if (data == null || data == "null" || data == "" || data == undefined) {
        return true;
    } else {
        return false;
    }
}
