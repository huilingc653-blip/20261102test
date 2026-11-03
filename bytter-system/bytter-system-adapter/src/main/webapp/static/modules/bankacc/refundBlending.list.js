var dict,moneyType;

accountCallBack = function (data) {
    $("#likes_bankAcc").val(data.bankAcc);
}



bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    var selectType = bt.selectType.init();



    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }

    payerAccCallBack = function (data) {
        $("#eqs_payerAcc").val(data.bankAcc);
        // selectType.get("eqs_curId").val(data.curId);
    }

    // 选择单位回调
    corpCallback2 = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("eqs_payerAcc").setCorp(id);
    }
    var accountTable = bt.accountTable.init();


    bt.corp.init();
    var selectType;

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

    var appTableDtl = bt.datatable.initMultiTables({
        elem:"#appTableDtl",
        checkbox:true,
        mulselect:false,
        selectCss:true,
        jumpPageSelect:false,//开启跨页选择
        isInitData:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        order: [[1, 'desc']],
        ajax:{
            'url':basePath+"/bankacc/refundblending/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:refundblending:apply'),
                text: bt.lang('common.button.auto.paymentblending'),
                className:'aotuButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        var data = dt.rows().data();
                        auto(data);
                    }
                }
            },{
                hide: $("#autoTrue").val() != 0 ? true : bt.isnotResource('netbank:bankAcc:refundblending:apply'),
                text: bt.lang('common.button.auto.paymentblending.true'),
                className:'aotuTrueButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        var data = dt.rows('.selected').data();
                        autoTrue(data);
                    }
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:refundblending:apply'),
                text: bt.lang('common.button.manual.paymentblending'),
                className:'manualButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        var data = dt.rows('.selected').data();
                        manual(data);
                    }
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:refundblending:apply'),
                text: bt.lang('common.button.cancel.paymentblending'),
                className:'cancelButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        var data = dt.rows('.selected').data();
                        cancel(data);
                    }
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:refundblending:apply'),
                text: bt.lang('common.button.manual.scrap'),
                className:'scrapButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ){
                        scrap();
                    }
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:refundblending:apply'),
                text: bt.lang('common.button.cancel.scrap'),
                className:'cancelScrapButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        cancelScrap();
                    }
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data: "transTime", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "amt", bSortable: true,className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "remark", bSortable: true},
            {data: "uses", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "blendSignName", bSortable: true},
            {data: "refundBlendTime", bSortable: true}
        ],
        btClickRowCallback:function(data){
            if(data[0].isSelect == true) {
                billTableInit(data[0]);
            }
        },
        btDrawCallback:function(setting){ //回调方法
            var blendSign = dict.get('blendSign').val();
            $('.aotuButton').removeClass('layui-disabled');
            $('.aotuTrueButton').removeClass('layui-disabled');
            $('.manualButton').removeClass('layui-disabled');
            $('.cancelButton').removeClass('layui-disabled');
            $('.scrapButton').removeClass('layui-disabled');//作废
            $('.cancelScrapButton').removeClass('layui-disabled');
            if(blendSign ==='0') {//未勾兑
                $('.cancelButton').addClass('layui-disabled');
            }
            if(blendSign ==='1') {//已勾兑
                $('.aotuButton').addClass('layui-disabled');
                $('.manualButton').addClass('layui-disabled');
            }
        }
    })
});
var tblBill = null;
//自动勾兑
function auto(data) {
    bt.loading.show();
    var handle = dict.get('handle').val();
    var bankAccTypeStr = '';
    var ids = '';
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
    })
    if(bankAccTypeStr.trim() == ""){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.matchingrules'), {icon: 2});
        return false;
    }
    for(var i = 0;i< data.length;i++){
        if(data[i].blendSign != '0'){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
            return false;
        }
        ids += ""+data[i].id+",";
    }
    bt.loading.hide();
    parent.layer.confirm(bt.lang('common.message.confirm.auto'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/bankacc/refundblending?auto",
            method: 'POST',
            data: {ids: ids,bankAccTypeStr:bankAccTypeStr,handle: handle},
            async:true,
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success) {
                    if (result.attributes.msg == "1") {
                        //请及时做冲账处理
                        bt.loading.hide();
                        parent.layer.alert(bt.lang('common.msg.closeDate'), {icon: 2});
                        bt.datatable.refreshTable("appTableDtl");
                        $("#eqs_bankSerialNo").val("#");
                        bt.datatable.refreshTable("appTableBill");
                        //请及时作废原单的记账凭证
                    } else if (result.attributes.msg == "2") {
                        bt.loading.hide();
                        parent.layer.alert(bt.lang('common.msg.refundblending.normal'), {icon: 2});
                        bt.datatable.refreshTable("appTableDtl");
                        $("#eqs_bankSerialNo").val("#");
                        bt.datatable.refreshTable("appTableBill");
                    }else {
                        parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                        bt.datatable.refreshTable("appTableDtl");
                        $("#eqs_bankSerialNo").val("#");
                        bt.datatable.refreshTable("appTableBill");
                    }
                }else{
                    parent.layer.alert(bt.lang('bankacc.message.notDate'), {icon: result.success?1:2});
                }
            }
        });
    });

}
//自动勾兑确认
function autoTrue(data) {
    bt.loading.show();
    if(data.length == 0){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.refundblending.true.dtl'),{icon:2});
        return false;
    }
    if(data[0].blendSign != '3'){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.refundblending.true.dtl'), {icon: 2});
        return false;
    }
    var dtlId = data[0].id;
    var handle = dict.get('handle').val();
    if(tblBill) {
        var table = $('#appTableBill').DataTable();
        var billData = table.rows('.selected').data();
        if(billData.length == 0){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'),{icon:2});
            return false;
        }
        var billId =billData[0].id;
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.autoTrue'), function () {
            bt.loading.show();
            $.ajax({
                url: basePath + "/bankacc/refundblending?autoTrue",
                method: 'POST',
                data: {dtlId: dtlId, billId: billId, handle: handle},
                async:true,
                dataType: 'json',
                success: function (result) {
                    bt.loading.hide();
                    if (result.success) {
                        if (result.attributes.msg == "1") {
                            //请及时做冲账处理
                            parent.layer.confirm(bt.lang('common.msg.closeDate'), function () {
                                bt.loading.show();
                                $.ajax({
                                    url: basePath + "/bankacc/refundblending?backNisBillhead",
                                    method: 'POST',
                                    data: {dtlId: dtlId, billId: billId, handle: handle},
                                    async:true,
                                    dataType: 'json',
                                    success: function (result) {
                                        bt.loading.hide();
                                        parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                                        bt.datatable.refreshTable("appTableDtl");
                                        $("#eqs_bankSerialNo").val("#");
                                        bt.datatable.refreshTable("appTableBill");
                                    }
                                });
                            });
                            //请及时作废原单的记账凭证
                        } else if (result.attributes.msg == "2") {
                            bt.loading.hide();
                            parent.layer.alert(bt.lang('common.msg.refundblending.normal'), {icon: 2});
                        } else {
                            bt.loading.hide();
                            parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                            bt.datatable.refreshTable("appTableDtl");
                            $("#eqs_bankSerialNo").val("#");
                            bt.datatable.refreshTable("appTableBill");
                        }
                    }
                }
            });
        });
    }else{
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'),{icon:2});
        return false;
    }
}
//手工勾兑
function manual(data) {
    bt.loading.show();
    if(data.length == 0){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'),{icon:2});
        return false;
    }
    var dtlId = data[0].id;
    var bankSerialNo = data[0].bankSerialNo;
    if(data[0].blendSign != '0'){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
        return false;
    }
    var handle = dict.get('handle').val();
    if(tblBill) {
        var table = $('#appTableBill').DataTable();
        var billData = table.rows('.selected').data();
        if(billData.length == 0){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'),{icon:2});
            return false;
        }
        var billId =billData[0].id;
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.manual'), function () {
            bt.loading.show();
            $.ajax({
                url: basePath + "/bankacc/refundblending?manual",
                method: 'POST',
                data: {dtlId: dtlId, billId: billId, bankSerialNo: bankSerialNo, handle: handle},
                async:true,
                dataType: 'json',
                success: function (result) {
                    bt.loading.hide();
                    if (result.success) {
                        if (result.attributes.msg == "1") {
                            //请及时做冲账处理
                            parent.layer.confirm(bt.lang('common.msg.closeDate'), function () {
                                bt.loading.show();
                                $.ajax({
                                    url: basePath + "/bankacc/refundblending?backNisBillhead",
                                    method: 'POST',
                                    data: {dtlId: dtlId, billId: billId, handle: handle},
                                    async:true,
                                    dataType: 'json',
                                    success: function (result) {
                                        bt.loading.hide();
                                        parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                                        bt.datatable.refreshTable("appTableDtl");
                                        $("#eqs_bankSerialNo").val("#");
                                        bt.datatable.refreshTable("appTableBill");
                                    }
                                });
                            });
                            //请及时作废原单的记账凭证
                        } else if (result.attributes.msg == "2") {
                            bt.loading.hide();
                            parent.layer.alert(bt.lang('common.msg.refundblending.normal'), {icon: 2});
                        } else {
                            bt.loading.hide();
                            parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                            bt.datatable.refreshTable("appTableDtl");
                            $("#eqs_bankSerialNo").val("#");
                            bt.datatable.refreshTable("appTableBill");
                        }
                    }else {
                        parent.layer.alert(result.msg, {icon: 2});
                    }
                }
            });
        });
    }else{
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'),{icon:2});
        return false;
    }
}
//取消勾兑
function cancel(data) {
    bt.loading.show();
    if(data.length == 0){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'),{icon:2});
        return false;
    }
    if(data[0].blendSign != '1' && data[0].blendSign != '3'){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
        return false;
    }
    var dtlId = data[0].id;
    var handle = dict.get('handle').val();
    if(tblBill) {
        var table = $('#appTableBill').DataTable();
        var billData = table.rows('.selected').data();
        if (billData.length == 0) {
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'), {icon: 2});
            return false;
        }
        var billId = billData[0].id;
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.cancelmanual'), function () {
            bt.loading.show();
            $.ajax({
                url: basePath + "/bankacc/refundblending?cancel",
                method: 'POST',
                data: {dtlId: dtlId, billId: billId, handle: handle},
                async:true,
                dataType: 'json',
                success: function (result) {
                    bt.loading.hide();
                    if (result.success) {
                        if (result.attributes.msg == "1") {
                            //勾兑后原单处理方式为打回原单，不允许取消勾兑
                            parent.layer.alert(bt.lang('common.msg.refundblending.notcancel'), {icon: result.success ? 1 : 2});
                            //请及时作废原单的记账凭证
                        } else if (result.attributes.msg != "") {
                            parent.layer.alert(bt.lang('common.msg.refundblending.notcancel.bill', [result.attributes.msg]), {icon: 2});
                        } else {
                            parent.layer.alert(bt.lang('bankacc.message.cancel.paymentblending.success'), {icon: result.success ? 1 : 2});
                            bt.datatable.refreshTable("appTableDtl");
                            $("#eqs_bankSerialNo").val("#");
                            bt.datatable.refreshTable("appTableBill");
                        }
                    }
                }
            });
        });
    }else{
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.bill'), {icon: 2});
        return false;
    }
}
//作废
function scrap() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.manual.scrap'), basePath + "/bankacc/refundblending/scrap/0");
        return;
    }
}
//取消作废
function cancelScrap() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.cancel.scrap'), basePath + "/bankacc/refundblending/scrap/2");
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
    $("#eqs_bankSerialNo").val(data.bankSerialNo);
    $("#refundBlendType").val(data.refundBlendType);
    $("#blendSign").val(data.blendSign);
    if(data.blendSign != '1') {
        if (bankAccTypeStr.indexOf("1") != -1) {
            // $("#eqg_payMoney").val(data.amt);
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
    if(null == tblBill) {
        tblBill = bt.datatable.initMultiTables({
            elem: "#appTableBill",
            checkbox: true,
            mulselect: false,
            selectCss: true,
            jumpPageSelect: false,//开启跨页选择
            searchForm: {
                formElem: "#searchFormBill",
                formFilter: "searchBill"
            },
            order: [[1, 'desc']],
            ajax: {
                'url': basePath + "/bankacc/refundblending/billdatatable"
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
                {data: "payMoney", bSortable: true,className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.payMoney);
                    }
                },
                {data: "voucherName", bSortable: true},
                {data: "payeeAcc", bSortable: true},
                {data: "payeeAccName", bSortable: true},
                {
                    data: "status", bSortable: true, className: 'center',
                    render: function (data, type, full, meta) {
                        return bt.dict("nisBillhead.status", full.status);
                    }
                },
                {data: "purpose", bSortable: true},
                {
                    data: "null", bSortable: true, className: 'center', render: function (data, type, full, meta) {
                        var html = "";
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="toView(\''+full.id+'\')">' + bt.lang("common.button.view") + '</a>';
                        return html;
                }
                }
            ],
            btDrawCallback:function(){
                selectType = bt.selectType.init();
            },

        });
    }else{
        bt.datatable.refreshTable("appTableBill");
    }
}
function clearForm() {
    moneyType.get('geg_payMoney').val("");
    moneyType.get('leg_payMoney').val("");
    $("#eqs_payerAcc").val("");
    $("#eqs_payeeAcc").val("");
    $("#wishPayday").val("");
    $("#eqs_curId").val("");
    /*$("#eqg_payMoney").val("");
    $("#eqs_payerAcc").val("");
    $("#eqs_payeeAcc").val("");
    $("#wishPayday").val("");
    $("#eqs_curId").val("");*/
}
function toView(id) {
    url = basePath + "/netbank/nisbillhead/view/" + id
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: url
    });
}
//根据所选明细获取匹配的付款单信息
function getPayBill() {
    var data  = bt.datatable.getSelectData("#appTableDtl");
    if(data.length > 0) {
        billTableInit(data[0]);
    }
}