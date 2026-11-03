var billTable = {};
var idsValArray=[];
var itemType = {};
var moneyType = {};
var dicts;
bt.use(['form', 'select', 'datatable', 'dicts', 'btdate', 'moneyType','itemType', 'mulselect', 'corp', 'element', 'bank', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();
    itemType = bt.itemType.init();
    bt.accountTable.init({urldata: {accountType: '2,3',electricBill:'1'}});
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillreceivebiz/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('bill:accebill:abBillReceivebiz:merch:sign') && bt.isnotResource('bill:accebill:abBillReceivebiz:bank:sign') && bt.isnotResource('bill:accebill:abBillReceivebiz:other:sign'),
                text: bt.lang('common.button.playInfo'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    playInfo(data);
                }
            }, {
                hide: bt.isnotResource('bill:accebill:abBillReceivebiz:merch:sign') && bt.isnotResource('bill:accebill:abBillReceivebiz:bank:sign') && bt.isnotResource('bill:accebill:abBillReceivebiz:other:sign'),
                text: bt.lang('common.button.consent'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSign(data, 0);
                }
            }, {
                hide: bt.isnotResource('bill:accebill:abBillReceivebiz:merch:sign') && bt.isnotResource('bill:accebill:abBillReceivebiz:bank:sign') && bt.isnotResource('bill:accebill:abBillReceivebiz:other:sign'),
                text: bt.lang('common.button.noConsent'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSign(data, 1);
                }
            }, {
                hide: bt.isnotResource('bill:accebill:abBillReceivebiz:recall'),
                text: bt.lang('common.button.batchRecall'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSign(data, -3);
                }
            }
        ]
        , columns: [
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.applyCorpName != null && full.applyCorpName != "") {
                        return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                    }else{
                        return '(' + full.corpCode + ')' + full.corpName;
                    }
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "billTypeName", bSortable: true},
            {data: "signerType", bSortable: true},
            {data: "origNo", bSortable: true},
            {data: "bankendrsmtmkName", bSortable: true},
            {data: "endorsementCount", bSortable: true, render: function (data, type, full, meta) {
                    if (data == null) {
                        data = 0;
                    }
                    var html = '<a class="" href="#" data-row="' + meta.row + '" ' +
                        'onclick="endorsementCountDtl(\'' + full.id + '\')">' + data + '</a>';
                    return html;
                }
            },
            {data: "bussdt", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "payer", bSortable: true},
            {data: "payee", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "payerbankName", bSortable: true},
            {data: "acceptorbankName", bSortable: true},
            {data: "reqNm", bSortable: true},
            {data: "recNm", bSortable: true},
            {data: "recAcctId", bSortable: true},
            {data: "recBankTypeName", bSortable: true},
            {data: "sttlmmkName", bSortable: true},
            {data: "statusName", bSortable: true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="businessDtlView(this)">' + bt.lang("common.button.businessDtl") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.lastid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        btClickRowCallback:function(data){
            //判断系统是否挂资金计划
            var isBudgetPlan = $("#isBudgetPlan").val();
            if (isBudgetPlan == "1") {
                //判断收票挂接资金计划科目是否为空，不为空则挂资金计划
                var signItem = $("#signItem").val();
                var selectData = bt.datatable.getSelectData("#appTable");
                // 签收类型为背书申请010，提示收票003才需要挂接资金计划
                if (signItem !== "" && (data[0].msgtype === '003' || data[0].msgtype === '010')) {
                    var corpId = []
                    for (i = 0; i < selectData.length; i++) {
                        if (corpId.indexOf(selectData[i].recCorpId) === -1) {
                            corpId.push(selectData[i].recCorpId)
                        }
                    }
                    isBudget(corpId.join(','), "6001");

                } else {
                    $("#budgetDiv").hide();
                    $("#tagItemDetail").html("");
                }
            }
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 1
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '11',
                    dataName: 'billMoney'
                }
            ]
        },//每行回调函数
        fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if([-2].indexOf(aData.status)>=0){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
    bt.btdate.init({
        elem: "#startDate",
        startname: "ged_startDate",
        endname: "led_startDate"
    });
    bt.btdate.init({
        elem: "#endDate",
        startname: "ged_endDate",
        endname: "led_endDate"
    });
    bt.btdate.init({
        elem: "#bussdt",
        startname: "ged_bussdt",
        endname: "led_bussdt"
    });
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    bt.corp.init();
    bt.bank.init({elem: '.bank'});
});

// 如果签收类型只为背书和收票，则为true，若只为其他签收类型也为true
function judgeMsgType(data) {

    var flag = true
    var tag = true
    for (var i = 0; i < data.length; i++) {
        if (data[i].msgtype == '003' || data[i].msgtype == '010') {
            flag = false
        } else {
            tag = false
        }
    }
    return !flag && !tag
}

/**
 * 进行批量签收操作
 * @param data 选择的数据类型
 * @param type 0批量同意，1批量不同意，-3已撤回
 */
function batchSign(data, type) {
    if (type === 0 && !bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var showTitle = "";
    var sendUrl = "";
    var rowData = data.splice(0, len);
    if (type == 0) {
        if ($("#isBudgetPlan").val() == "1" && judgeMsgType(rowData)) {
            layer.alert(bt.lang('accebill.abbillreceivebiz.message.msgtypeCheck'), {icon: 7});
            return
        }
        showTitle = bt.lang('common.message.confirm.batchConsent');
        sendUrl = "/bill/accebill/abbillreceivebiz/batchConsent";
    } else if (type == 1) {
        showTitle = bt.lang('common.message.confirm.batchNoConsent');
        sendUrl = "/bill/accebill/abbillreceivebiz/batchNoConsent";
    } else if (type == -3) {
        showTitle = bt.lang('common.message.confirm.batchRecall');
        sendUrl = "/bill/accebill/abbillreceivebiz/batchRecall";
    }
    var ids = [], msgtypes = [], dshnrcd, dshnrrsn, stop = false;
    var bwDataParams = [];
    var bwAcceptorDataParams = [];
    for (i = 0; i < len; i++) {
        if (type == -3) {
            if (bt.isnotResource('bill:accebill:abBillReceivebiz:recall', rowData[i].recCorpId)) {
                layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
                return;
            }
        } else {
            // if(bt.isnotResource('bill:accebill:abBillReceivebiz:sign',rowData[i].recCorpId)){
            if ((rowData[i].billType == '10' && bt.isnotResource('bill:accebill:abBillReceivebiz:bank:sign', rowData[i].recCorpId))
                || (rowData[i].billType == '11' && bt.isnotResource('bill:accebill:abBillReceivebiz:merch:sign', rowData[i].recCorpId))
                || (rowData[i].billType != '10' && rowData[i].billType != '11' && bt.isnotResource('bill:accebill:abBillReceivebiz:other:sign', rowData[i].recCorpId))) {
                layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
                return;
            }
        }
        var isBudgeJudge = $("#isBudgetPlan").val() == "1"
        // 只有第一次判断金额是否一致
        if (i === 0 && isBudgeJudge && $("input[name='sumMoney']").length) {
            //资金计划数据
            var budgetList = [];
            //资金计划科目代码信息
            var itemCodeStr = "";
            var selectData = bt.datatable.getSelectData("#appTable");
            var billMoney = 0;
            for (var j = 0; j < selectData.length; j++) {
                billMoney += selectData[j].billMoney * 100
            }
            billMoney /= 100
            if (type == '0' && (rowData[i].msgtype == '003' || rowData[i].msgtype == '010')) {
                var sumMoney = bt.moneyType.get("sumMoney").val();
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
        }
        ids.push(rowData[i].id)
        if (isBudgeJudge) {
            msgtypes.push(rowData[i].msgtype)
        }
        dshnrcd = $("select[name='dshnrcd']").val();
        dshnrrsn = $("#dshnrrsn").val();
        var newDshnrcd = $("select[name='newDshnrcd']").val();
        if (dshnrcd) {
            dshnrcd = dshnrcd.trim();
        }
        if (dshnrrsn) {
            dshnrrsn = dshnrrsn.trim();
        }
       /* if (type == 1 && !stop && (rowData[i].msgtype == '020' || rowData[i].msgtype == '021')) {
            stop = true;
            if (!dshnrcd || (dshnrcd == ' ' && newDshnrcd == ' ') ) {
                layer.alert(bt.lang('common.message.confirm.selectDshnr'), {icon: 7});
                return;
            } else if (dshnrcd == 'DC09' && (!dshnrrsn || dshnrrsn == ' ')) {
                layer.alert(bt.lang('common.message.confirm.selectDshnrrsn'), {icon: 7});
                return;
            }
        }*/
        var bwName = rowData[i].payer;
        if (rowData[i].reqNm && rowData[i].reqNm != bwName) {
            bwName += "," + rowData[i].reqNm;
        }
        bwDataParams.push({"name": bwName, "appNo": rowData[i].origNo, "corpId": rowData[i].recCorpId});
        if (rowData[i].billType == '10') {
            bwAcceptorDataParams.push({
                "name": rowData[i].acceptor,
                "appNo": rowData[i].origNo,
                "corpId": rowData[i].recCorpId
            });
        }
    }
    var signData = JSON.stringify({"id": ids.join(','), "dshnrcd": dshnrcd,"newDshnrcd": newDshnrcd, "dshnrrsn": dshnrrsn,
        "itemCodeStr": itemCodeStr, "budgetList": budgetList, "msgtype": msgtypes.join(',')});

    if (type == 0) {
        if (bwAcceptorDataParams.length > 0) {
            submitSignBankAcceptance(signData, sendUrl, showTitle, bwAcceptorDataParams, bwDataParams);
        } else {
            submitSignExternalCorp(signData, sendUrl, showTitle, bwDataParams);
        }
    } else {
        submitSign(signData, sendUrl, showTitle);
    }
}

var submitSignBankAcceptance = function (signData, sendUrl, showTitle, bwAcceptorDataParams, bwDataParams) {
    bt.loading.show();
    //对黑白名单进行验证
    var reData = bankAcceptanceCheckBatch({"params": bwAcceptorDataParams});
    bt.loading.hide();
    if (reData.reCode == '3') {
        parent.layer ? parent.layer.alert(reData.msg, {icon: 2}) : layer.alert(reData.msg, {icon: 2});
    } else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            submitSignExternalCorp(signData, sendUrl, showTitle, bwDataParams);
        });
    } else {
        submitSignExternalCorp(signData, sendUrl, showTitle, bwDataParams);
    }
}

var submitSignExternalCorp = function (signData, sendUrl, showTitle, bwDataParams) {
    bt.loading.show();
    //对黑白名单进行验证
    var reData = externalCorpCheckBatch({"params": bwDataParams});
    bt.loading.hide();
    if (reData.reCode == '3') {
        parent.layer ? parent.layer.alert(reData.msg, {icon: 2}) : layer.alert(reData.msg, {icon: 2});
    } else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            submitSign(signData, sendUrl, showTitle);
        });
    } else {
        submitSign(signData, sendUrl, showTitle);
    }
}

var submitSign = function (signDataStr, sendUrl, showTitle) {
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
            data: signDataStr,//将对象序列化成JSON字符串
            dataType: "json",
            async: false,
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    idsValArray=[]
                    //bt.datatable.refreshCurrentPage();
                }
                bt.datatable.refreshCurrentPage();
            },
            error: function (res) {
                bt.loading.hide();
            }
        });
    });
}

/**
 * 支付信息查询
 * @param data 选择的数据
 */
function playInfo(data) {
    var len = data.length;
    //选择的数据不能为空
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //选择的数据不能为空
    if (len >= 2) {
        layer.alert(bt.lang('common.message.confirm.selectBatchdate'), {icon: 7});
        return;
    }
    //嵌入外部页面
    parent.layer.open({
        type: 2,
        area: ['950px', '550px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillreceivebiz/playPage?billCode=" + data[0].origNo + "&acctId=" + data[0].recAcctId  + "&recId=" + data[0].id + "&msgType=01",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });


}

function logInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    console.log("data====",data)
    parent.layer.open({
        type: 2,
        area: ['550px', '330px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abLog/abLogView?ids=" + data.vvid,
        end: function () {
//        	billTable.draw();
            //bt.datatable.refreshCurrentPage();
        }
    });
}

// 背书转让详情页
function endorsementCountDtl(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abbillQuery/endorsementCountDtl?recid=" + id);

    }
}


function businessDtlView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.businessDtl') + '-' + data.origNo, basePath + "/bill/accebill/abbillreceivebiz/businessDtlView?id=" + data.id + "&msgtype=" + data.msgtype);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillreceivebiz/businessDtlView?id=" + data.id + "&msgtype=" + data.msgtype,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].id)<0){
                    idsValArray.push(datas[i].id);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].id);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};

//根据单位ID和业务类型判断是否挂预算
function isBudget(corpId, briefId) {
    $.ajax({
        type: 'POST',
        url: basePath + "/bill/accebill/abvoucherinfoVice/isBudget",
        data: {'corpId': corpId, 'briefId': briefId},
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

// 生成公共的属性
$genOtherHtml = function (corpId) {
    var $ = bt.jquery;
    var itemCode = $('input[name="signItem"]').val();
    var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
    var curId = $("#curId").val();
    var param = {
        corpId: corpId,
        itemCode: itemCode,
        defItemCode: '',
        processDate: bt.date.format(new Date(), "yyyy-MM-dd"),
        curId: curId,
        type: 'select',
        date: (new Date()).getTime(),
        isMultiItem: '0'
    };
    $.ajax({
        type: 'post',
        url: url,
        data: param,
        async: true,
        success: function (json) {
            $("#budgetDiv").show();
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
        },
    });
};

//根据查询条件发送指令
function sendCommand() {
    var payerAcc = $("#recAcctId2").val();
    var msgType = '06';
    var billType = $("input[name='ins_billTypeName']").val();
    var signType = $("input[name='ins_signerType']").val();
    $.ajax({
        type: 'POST',
        url: basePath + '/bill/accebill/abbillreceivebiz/sendCommand',
        data: {'payerAcc': payerAcc,
            'msgType': msgType,
            'billType':billType,
            'signType':signType
        },
        async: false,
        dataType: 'json',
        success:function(result){
            if(result.success==true){
                parent.layer.alert(bt.lang(result.attributes.MSG_INFO), {icon: 6});
            }else{
                parent.layer.alert(bt.lang(result.attributes.MSG_INFO), {icon: 2});
            }
        }
    });
}

//票据账户
clickCallback = function (data) {
    $('#recAcctId2').val(data.bankAcc);
}