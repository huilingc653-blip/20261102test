var dict, selectType, accountTable,moneyType,dateType;

// 选择单位回调
receiptCorpCallback = function (id) {
    // 重新根据单位ID加载账户表格
    accountTable.get("receipt_payAccNo").setCorp(id);
}
dtlCorpCallback = function (id) {
    // 重新根据单位ID加载账户表格
    accountTable.get("dtl_payAccNo").setCorp(id);
}
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect'], function () {
    var laydate = layui.laydate;
    $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });

    selectType = bt.selectType.init();

    accountTable = bt.accountTable.init();

    bt.corp.init();

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
    var receipt_Id = $("#receipt_Id").val();
    var initdate = false;
    if(receipt_Id != ""){
        initdate = true;
    }
    var appTableReceipt = bt.datatable.initMultiTables({
        elem: "#appTableReceipt",
        checkbox: true,
        mulselect: true,
        selectCss: true,
        jumpPageSelect: true,//开启跨页选择
        isInitData: initdate,
        searchForm: {
            formElem: "#receiptSearchForm",
            formFilter: "receiptSearch",
            callback:function(data){ //点击查询按钮的回调方法
                clearFrom();
                $("#eqs_id").val("#");
                getDtlDate();
            }
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/receipt/bisbankreceiptblending/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptBlending:apply'),
                text: bt.lang('common.button.auto.paymentblending'),
                className: 'aotuButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = dt.rows().data();
                        auto(data);
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptBlending:apply'),
                text: bt.lang('common.button.manual.paymentblending'),
                className: 'manualButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = bt.datatable.getJumpSelectedData("appTableReceipt");
                        manual(data);
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptBlending:apply'),
                text: bt.lang('common.button.cancel.paymentblending'),
                className: 'cancelButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = bt.datatable.getJumpSelectedData("appTableReceipt");
                        cancel(data);
                    }
                }
            },{
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptBlending:apply'),
                text: "无需勾对",
                action: function (e, dt, node, config) {
                    var data = bt.datatable.getJumpSelectedData("appTableReceipt");
                    needNot(data);
                }
            },
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "blendSignName", bSortable: true},
            {data: "paymentBlendingDate", bSortable: true},
            {data: "corpName", bSortable: false},
            {data: "charageDate", bSortable: true},
            {
                data: "transAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.transAmt);
                }
            },
            {data: "payAccNo", bSortable: true},
            {data: "payAccName", bSortable: true},
            {data: "payBankName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "cdSignName", bSortable: true},
            {data: "payeeAccNo", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankName", bSortable: true},
            {data: "receiptCheckCode", bSortable: true},
            {data: "receiptNo", bSortable: true},
            {data: "usefor", bSortable: true}
        ],
        btClickRowCallback: function (data1) {
            var data = bt.datatable.getSelectData("#appTableReceipt");
            var bankAccTypeStr = '';
            var blendSign = dict.get('receipt_blendSign').val();
            $('input[name="eqs_matchingRule"]:checked').each(function () {
                bankAccTypeStr += $(this).val() + ",";
            });
            if(data.length >1 && bankAccTypeStr.indexOf("1") != -1 && blendSign === '0'){
                parent.layer.alert(bt.lang('bankacc.message.receipt.rules.money'), {icon: 2});
                clearFrom();
                $("#eqs_id").val("#");
                getDtlDate();
                return false;
            }
            if(data.length >1 && blendSign === '0'){
                var payerAcc = data[0].payAccNo;
                var payeeAcc = data[0].payeeAcc;
                var curName = data[0].curName;
                var cdSign = data[0].cdSign;
                for(var i =1;i<data.length;i++){
                    if(payerAcc != data[i].payAccNo || payeeAcc !=data[i].payeeAcc || curName!=data[i].curName || cdSign != data[i].cdSign){
                        parent.layer.alert(bt.lang('bankacc.message.receipt.rules.standard'), {icon: 2});
                        clearFrom();
                        $("#eqs_id").val("#");
                        getDtlDate();
                        return false;
                    }
                }
            }
            if (data1[0].isSelect == true) {
                billTableInit(data1[0]);
            }else if(data.length == 0){
                clearFrom();
                $("#eqs_id").val("#");
                getDtlDate();
            }
        },
        btDrawCallback: function (setting) { //回调方法
            var blendSign = dict.get('receipt_blendSign').val();
            $('.aotuButton').removeClass('layui-disabled');
            $('.manualButton').removeClass('layui-disabled');
            $('.cancelButton').removeClass('layui-disabled');
            if (blendSign === '0') {//未勾兑
                $('.cancelButton').addClass('layui-disabled');
            }
            if (blendSign === '1') {//已勾兑
                $('.aotuButton').addClass('layui-disabled');
                $('.manualButton').addClass('layui-disabled');
            }
            $("#eqs_id").val("#");
            bt.datatable.refreshTable("appTableBill");
        }
    });
});
var tbDtl = null;

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
            parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.receipt'), {icon: 2});
            return false;
        }
        ids += "" + data[i].id + ",";
    }
    bt.loading.show();
    $.ajax({
        url: basePath + "/receipt/bisbankreceiptblending?auto",
        method: 'POST',
        data: {ids: ids, bankAccTypeStr: bankAccTypeStr},
        dataType: 'json',
        success: function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                bt.datatable.refreshTable("appTableReceipt");
                clearFrom();
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
        parent.layer.alert(bt.lang('bankacc.message.NoReceiptBlending.bill'), {icon: 2});
        return false;
    }
    //电子回单id
    var receiptId="";
    for (var i = 0; i < data.length; i++) {
        if (data[i].blendSign == '1') {
            parent.layer.alert(bt.lang('bankacc.message.NoReceiptBlending.bill'), {icon: 2});
            return false;
        }
        if(i === 0){
            receiptId += data[i].id;
        }else {
            receiptId += "," + data[i].id;
        }
    }
    //明细id
    if (tbDtl) {
        var table = $('#appTableDtl').DataTable();
        var billData = table.rows('.selected').data();
        if (billData.length == 0) {
            parent.layer.alert(bt.lang('bankacc.message.receiptBlending.dtl'), {icon: 2});
            return false;
        }
        var dtlId = "";
        for (var j = 0; j < billData.length; j++) {
            if(j === 0){
                dtlId += billData[j].id;
            }else {
                dtlId += "," + billData[j].id;
            }
        }
        bt.loading.show();
        $.ajax({
            url: basePath + "/receipt/bisbankreceiptblending?manual",
            method: 'POST',
            data: {receiptId: receiptId, dtlId: dtlId},
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success == true) {
                    parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTableReceipt");
                    $("#eqs_id").val("#");
                    bt.datatable.refreshTable("appTableDtl");
                }
            }
        });
    } else {
        parent.layer.alert(bt.lang('bankacc.message.receiptBlending.dtl'), {icon: 2});
        return false;
    }
}

//取消勾兑
function cancel(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('bankacc.message.receiptBlending.bill'), {icon: 2});
        return false;
    }
    var receiptId="";
    for (var i = 0; i < data.length; i++) {
        if (data[i].blendSign == '0') {
            parent.layer.alert(bt.lang('bankacc.message.receiptBlending.bill'), {icon: 2});
            return false;
        }
        if(i === 0){
            receiptId += data[i].id;
        }else {
            receiptId += "," + data[i].id;
        }
    }
    if (tbDtl) {
        // var table = $('#appTableDtl').DataTable();
        // var billData = table.rows('.selected').data();
        // if (billData.length == 0) {
        //     parent.layer.alert(bt.lang('bankacc.message.receiptBlending.dtl'), {icon: 2});
        //     return false;
        // }
        bt.loading.show();
        $.ajax({
            url: basePath + "/receipt/bisbankreceiptblending?cancel",
            method: 'POST',
            data: {receiptId: receiptId},
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success == true) {
                    parent.layer.alert(bt.lang('bankacc.message.cancel.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTableReceipt");
                    $("#eqs_id").val("#");
                    bt.datatable.refreshTable("appTableDtl");
                }
            }
        });
    } else {
        parent.layer.alert(bt.lang('bankacc.message.receiptBlending.dtl'), {icon: 2});
        return false;
    }
}

//无需勾对
function needNot(data) {
    if (data.length == 0) {
        parent.layer.alert("请选择数据！", {icon: 2});
        return false;
    }
    var receiptId="";
    for (var i = 0; i < data.length; i++) {
        if(i === 0){
            receiptId += data[i].id;
        }else {
            receiptId += "," + data[i].id;
        }
    }
    $.ajax({
        url: basePath + "/receipt/bisbankreceiptblending?needNot",
        method: 'POST',
        data: {receiptId: receiptId},
        dataType: 'json',
        success: function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert("操作成功！", {icon: result.success ? 1 : 2});
                bt.datatable.refreshTable("appTableReceipt");
                $("#eqs_id").val("#");
                bt.datatable.refreshTable("appTableDtl");
            }
        }
    });
}
//清除查询列表
function clearFrom(){
    moneyType.get("dtl_startAmt").val("");
    moneyType.get("dtl_endAmt").val("");
    $("#dtl_oppAccNo").val("");
    $("#dtl_bankAcc").val("");
    dateType.get("dtl_startDate").val("");
    dateType.get("dtl_endDate").val("");
    selectType.get("dtl_curId").val("");
    $("#eqs_id").val("");
    $("#eqs_cdSign").val("");
}
//根据所选银行电子回单获取匹配的银行流水信息
function billTableInit(data) {
    var bankAccTypeStr = '';
    var cdSign = data.cdSign;
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
    });
    clearFrom();
    dict.get("dtl_cdSign").val(cdSign);
    $("#eqs_cdSign").val(cdSign);
    var blendSign = data.blendSign;
    if (blendSign === '1') {//已勾兑
        $("#eqs_id").val(data.dtlId);
    }
    if (data.blendSign != '1') {
        if (bankAccTypeStr.indexOf("1") != -1) {
            moneyType.get("dtl_startAmt").val(data.transAmt);
            moneyType.get("dtl_endAmt").val(data.transAmt);
        }
        if (bankAccTypeStr.indexOf("2") != -1) {
            if(cdSign == "0"){
                $("#dtl_oppAccNo").val(data.payAccNo);
            }else {
                $("#dtl_bankAcc").val(data.payAccNo);
            }
        }
        if (bankAccTypeStr.indexOf("3") != -1) {
            if(cdSign == "0"){
                $("#dtl_bankAcc").val(data.payeeAccNo);
            }else {
                $("#dtl_oppAccNo").val(data.payeeAccNo);
            }
        }
        if (bankAccTypeStr.indexOf("4") != -1) {
            var time = data.charageDate;
            if(time.length>10){
                time = time.substr(0,10);
            }
            dateType.get("dtl_startDate").val(time);
            dateType.get("dtl_endDate").val(time);
        }
        if (bankAccTypeStr.indexOf("5") != -1) {
            selectType.get("dtl_curId").val(data.curId);
        }
    }
    getDtlDate();
}

//获取流水表格数据
function getDtlDate() {
    if (null == tbDtl) {
        tbDtl = bt.datatable.initMultiTables({
            elem: "#appTableDtl",
            checkbox: true,
            mulselect: true,
            selectCss: true,
            jumpPageSelect: false,//开启跨页选择
            searchForm: {
                formElem: "#searchFormDtl",
                formFilter: "dtlSearch"
            },
            order: [[1, 'desc']],
            ajax: {
                'url': basePath + "/receipt/bisbankreceiptblending/dtlDatatable"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            columns: [
                {data: "corpName", bSortable: true},
                {data: "transTime", bSortable: true},
                {
                    data: "amt", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.amt);
                    }
                },
                {data: "bankAcc", bSortable: true},
                {data: "accName", bSortable: true},
                {data: "curName", bSortable: true},
                {data: "cdSign", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("btBankAccDtl.cdSign",full.cdSign);
                    }
                },
                {data: "oppAccNo", bSortable: true},
                {data: "oppAccName", bSortable: true},
                {data: "remark", bSortable: true},
                {data: "uses", bSortable: true}
            ],
            btDrawCallback: function () {
                selectType = bt.selectType.init();
                checkRow(0, true, $("#appTableDtl"));
            },

        });
    } else {
        bt.datatable.refreshTable("appTableDtl");
    }
}

//根据所选电子回单获取匹配的银行流水信息
function getDtl() {
    var data = bt.datatable.getSelectData("#appTableReceipt");
    var bankAccTypeStr = '';
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
    });
    if(data.length >1 && bankAccTypeStr.indexOf("1") != -1){
        parent.layer.alert(bt.lang('bankacc.message.receipt.rules.money'), {icon: 2});
        clearFrom();
        $("#eqs_id").val("#");
        getDtlDate();
        return false;
    }
    if(data.length >1){
        var payerAcc = data[0].payAccNo;
        var payeeAcc = data[0].payeeAcc;
        var charageDate = data[0].charageDate;
        var curName = data[0].curName;
        var cdSign = data[0].cdSign;
        for(var i =1;i<data.length;i++){
            if(payerAcc != data[i].payAccNo || payeeAcc !=data[i].payeeAcc || charageDate!=data[i].charageDate || curName!=data[i].curName
            || cdSign != data[i].cdSign){
                parent.layer.alert(bt.lang('bankacc.message.receipt.rules.standard'), {icon: 2});
                clearFrom();
                $("#eqs_id").val("#");
                getDtlDate();
                return false;
            }
        }
    }
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
