var dateType = {};
var itemType = {};
var moneyType = {};
var fromObj = {};
var dicts;
bt.use(['form', 'select', 'dicts', 'jquery', 'dateType', 'element', 'itemType', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    fromObj = form;
    dicts = bt.dicts.init();
    dateType = bt.dateType.init();
    itemType = bt.itemType.init();
    moneyType = bt.moneyType.init();

    //初始化资金计划
    processInit();

});

/**
 * 进行签收操作
 * @param id 签收表ID
 * @param msgtype 签收类型
 * @param type 0同意，1不同意
 */
var sign = function (id, msgtype, type) {
    if (!fromObj.verifyFun('#fromVerify')) {
        return false;
    }
    var showTitle = "";
    var sendUrl = "";
    if (type == 0) {
        showTitle = bt.lang('common.message.confirm.consent');
        sendUrl = "/bill/accebill/abbillreceivebiz/consent";
    } else {
        showTitle = bt.lang('common.message.confirm.noConsent');
        sendUrl = "/bill/accebill/abbillreceivebiz/noConsent";
    }
    var dshnrcd, dshnrrsn, cdtratgs, cdtratgagcy, cdtratgduedt, accptncagrmtnb, newDshnrcd;
    if (msgtype == '002') {
        accptncagrmtnb = $("#accptncagrmtnb").val();
        cdtratgs = $("#cdtratgs").val();
        cdtratgagcy = $("#cdtratgagcy").val();
        cdtratgduedt = dateType.get("cdtratgduedt").val();
    }
    dshnrcd = $("select[name='dshnrcd']").val();
    dshnrrsn = $("#dshnrrsn").val();
    newDshnrcd = $("select[name='newDshnrcd']").val();
    if(dshnrcd){
    	dshnrcd=dshnrcd.trim();
    }
    if(dshnrrsn){
    	dshnrrsn=dshnrrsn.trim();
    }
    if (type == 1 && (msgtype == '020' || msgtype == '021')) {
        if (!dshnrcd || dshnrcd == ' ') {
            layer.alert(bt.lang('common.message.confirm.selectDshnr'), {icon: 7});
            return;
        } else if (dshnrcd == 'DC09' && (!dshnrrsn || dshnrrsn == ' ')) {
            layer.alert(bt.lang('common.message.confirm.selectDshnrrsn'), {icon: 7});
            return;
        }
    }

    //资金计划数据
    var budgetList = [];
    //资金计划科目代码信息
    var itemCodeStr = "";
    //对内标志
    var insideSing = "";
    //判断对内,对外标志
    if (msgtype == '003' || msgtype == '010') {
        insideSing = dicts.get("insideSing").val();
        //判断单位是否为同一网点下
        var corpId = $("#corpId").val();
        var recCorpId = $("#recCorpId").val();
        if (insideSing == '1') {
            if(!corpId){
                if(msgtype=='010'){
                //背书人账号或名称判断为非内部单位,不能选择对内背书!
                    layer.alert(bt.lang('accbill.receivebiz.endose.notInnerCorp'), {icon: 7});
                    return;
                }else{
                //出票人账号或名称判断为非内部单位,收票方式不能为对内!
                    layer.alert(bt.lang('accbill.receivebiz.receive.notInnerCorp'), {icon: 7});
                    return;
                }
            }
            var flag = isSameNetByCorpId(corpId, recCorpId);
            if (!flag) {
                if (msgtype == '010') {
                    //对内背书时,背书人与被背书人账号或者名称必须为同一网点下的单位!
                    layer.alert(bt.lang('accbill.receivebiz.innerEndose.isSameNet'), {icon: 7});
                    dicts.get("insideSing").val(0);
                    return;
                } else {
                    //收票方式为对内时,出票人与收票人账号或者名称必须为同一网点下的单位!
                    layer.alert(bt.lang('accebill.receivebiz.innerReceive.isSameNet'), {icon: 7});
                    dicts.get("insideSing").val(0);
                    return;
                }
            }
        }
    }
    if (type == 0 && (msgtype == '003' || msgtype == '010')) {
        var billMoney = bt.money.toNumber($("#billMoney").val());
        var sumMoney = bt.money.toNumber($("#sumMoney").val());
        //校验票据金额与执行金额总金额是否相等
        if (sumMoney && billMoney != sumMoney) {
            layer.alert(bt.lang("common.accebill.fbsBudget.errorMessage"), {icon: 7});
            return;
        }
        var trs = $("#budgetProcessDataTable").children("div[row]");
        if (trs && trs.length) {
            $(trs).each(function (m, n) {
                var i = $(n).attr("row");
                var budgetObj = {};
                budgetObj['itemCode'] = itemType.get("itemCode" + i).val();
                budgetObj['itemName'] = $("#itemName" + i).val();
                budgetObj['processMoney'] = moneyType.get("processMoney" + i).val();
                if (itemCodeStr) {
                    itemCodeStr = itemCodeStr + ";" + budgetObj['itemCode'];
                } else {
                    itemCodeStr = budgetObj['itemCode'];
                }
                budgetList.push(budgetObj);
            })
        }
    }
    var signData = null;
    if(msgtype === '002'){
         signData=JSON.stringify({
            "id": id,
            "msgtype": msgtype,
            "dshnrcd": dshnrcd,
            "dshnrrsn": dshnrrsn,
            "accptncagrmtnb": accptncagrmtnb,
            "cdtratgs": cdtratgs,
            "cdtratgagcy": cdtratgagcy,
            "cdtratgduedt": cdtratgduedt,
            "insideSing": insideSing,
            "itemCodeStr": itemCodeStr,
            "budgetList": budgetList,
            "newDshnrcd": newDshnrcd,
            "origNo": $('#origNo').val(),
            "payerCdtRatgs": $('#payerCdtRatgs').val(),
            "payerCdtRatgGgcy": $('#payerCdtRatgGgcy').val(),
        });
    }else {
        signData=JSON.stringify({
            "id": id,
            "msgtype": msgtype,
            "dshnrcd": dshnrcd,
            "dshnrrsn": dshnrrsn,
            "accptncagrmtnb": accptncagrmtnb,
            "cdtratgs": cdtratgs,
            "cdtratgagcy": cdtratgagcy,
            "cdtratgduedt": cdtratgduedt,
            "insideSing": insideSing,
            "itemCodeStr": itemCodeStr,
            "budgetList": budgetList,
            "newDshnrcd": newDshnrcd,
            "origNo": $('#origNo').val(),
            "payerCdtRatgs": $('#payerCdtRatgs').val(),
            "payerCdtRatgGgcy": $('#payerCdtRatgGgcy').val(),
            "payerCdtRatgDuedt": dateType.get('payerCdtRatgDuedt').val(),
        });
    }


    if(type == 0){
        if($("#billType").val()=='10'){
            submitSignBankAcceptance(signData,sendUrl,showTitle);
        }else{
            submitSignExternalCorp(signData,sendUrl,showTitle);
        }
    }else{
        submitSign(signData,sendUrl,showTitle);
    }
}

var submitSignBankAcceptance=function(signData,sendUrl,showTitle){
    bt.loading.show();
    //对黑白名单进行验证
    var reData=bankAcceptanceCheck({"name":$("#acceptor").val(),"corpId":$("#recCorpId").val()});
    bt.loading.hide();
    if(reData.reCode=='3'){
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
    }else if(reData.reCode=='2'){
        parent.layer.confirm(reData.msg,function(){
            submitSignExternalCorp(signData,sendUrl,showTitle);
        });
    }else{
        submitSignExternalCorp(signData,sendUrl,showTitle);
    }
}

var submitSignExternalCorp=function(signData,sendUrl,showTitle){
    bt.loading.show();
    //对黑白名单进行验证
    var bwName=$("#payer").val();
    if($("#reqNm").val() && bwName!=$("#reqNm").val()){
        bwName+=","+$("#reqNm").val();
    }
    var reData=externalCorpCheck({"name":bwName,"corpId":$("#recCorpId").val()});
    bt.loading.hide();
    if(reData.reCode=='3'){
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
    }else if(reData.reCode=='2'){
        parent.layer.confirm(reData.msg,function(){
            submitSign(signData,sendUrl,showTitle);
        });
    }else{
        submitSign(signData,sendUrl,showTitle);
    }
}

var submitSign=function(signDataSend,sendUrl,showTitle){
    //证书签名
    var signDataMsg = signData("票据签收提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(showTitle, function () {
        bt.loading.show();
        bt.jquery.ajax({
            type: "POST",
            url: basePath + sendUrl,
            data: signDataSend,//将对象序列化成JSON字符串
            dataType: "json",
            async: false,
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    var parentWindow = bt.navtab.getParentDom();
                    parentWindow.location.reload();
                    bt.navtab.refreshParentTable();
                    bt.navtab.closeCurrent();
//					parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            },
            error: function (res) {
                bt.loading.hide();
            }
        });
    });
}

/**
 * 进行票据信息查看
 * @param id 签收表ID
 * @param type front正面，back背面
 */
var billInfo = function (id, opType, accId) {
    parent.layer.open({
        title: bt.lang('common.button.billinfo'),
        type: 2,
        area: ['850px', '620px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abvoucherinfo/billEcdsInfoView?recId=" + id + "&accId=" + accId + "&origNo=" + $("#origNo").val()+"&isSign="+$("#isSign").val()+"&sbbllRngStrtSn="+$("#sbbllRngStrtSn").val()+"&sbbllRngEndSn="+$("#sbbllRngEndSn").val()
    });
}

//提示收票,背书申请挂接资金计划
function processInit() {
    var msgtype = $("#msgtype").val();
    if (msgtype == '003' || msgtype == '010') {
        var recCorpId = $("#recCorpId").val();
        renderBudget(recCorpId);
    }
};

function  isSameNetByCorpId(corpId1,corpId2){
    var flag=true;
    $.ajax({
        type: 'post',
        url: basePath+"/bill/accebill/abbillreceivebiz/isTheSameNetByCorpId",
        data: {corpId1:corpId1,corpId2:corpId2},
        async: false,
        dataType: 'json',
        success: function (result) {
            if(result.success){
                flag=true;
            }else{
                flag=false;
            }
        }
    });
    return flag;
}

/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml = function (corpId) {
    var itemCode = $("#recItem").val();
    var defItemCode = $("#defItemCode").val();
    var url;
    var param;
    var curId = $("#curId").val();
    bt.use(['date'], function () {
        //判断是否挂接过资金计划
        $.ajax({
            type: 'post',
            url: basePath + "/budget/fbsBudgetProcess/getBudgetProcesInfo",
            data: {
                "businessName": $("#businessName").val(),
                "processSysname": $("#processSysname").val(),
                "billNo": $("#origNo").val(),
                "billId": $("#vvid").val()
            },
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.length && result[0].corpId == corpId) {
                    url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
                    param = {
                        corpId: corpId,
                        itemCode: itemCode,
                        billNo: $("#origNo").val(),
                        billId: $("#vvid").val(),
                        businessName: $("#businessName").val(),
                        processSysname: $("#processSysname").val(),
                        processDate: bt.date.format(new Date(), "yyyy-MM-dd"),
                        type: 'select',
                        curId:curId,
                        date: (new Date()).getTime()
                    };

                } else {
                    url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
                    param = {
                        corpId: corpId,
                        itemCode: itemCode,
                        defItemCode: defItemCode,
                        processDate: bt.date.format(new Date(), "yyyy-MM-dd"),
                        type: 'select',
                        curId:curId,
                        date: (new Date()).getTime()
                    };
                }
            }
        });
        $.ajax({
            type: 'post',
            url: url,
            data: param,
            async: false,
            success: function (json) {
                $("#budgetDiv").show();
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            },
        });
    });
};

/**
 * 资金计划挂接
 */
function renderBudget(corpId) {
    $.ajax({
        type: 'POST',
        url: basePath + "/bill/accebill/abvoucherinfoVice/isBudget",
        data: {'corpId': corpId, 'briefId': '6001'},
        async: false,
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                if (result.attributes.isBudget) {
                    $genOtherHtml(corpId);
                } else {
                    $("#budgetDiv").hide();
                    $("#tagItemDetail").html("");
                }
            } else {
                $("#budgetDiv").hide();
                $("#tagItemDetail").html("");
            }
        }
    });
}
