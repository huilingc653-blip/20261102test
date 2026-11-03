bt.use(['form', 'layedit', 'moneyType', 'accebillView', 'btUpload', 'printType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var accebillView = bt.accebillView.init();
    bt.accebillViewParams = function () {
        accebillView.set({"id": $("#billId").val()});
        return true;
    }

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        isExpand: true,
        gid: $("#attachmentId").val()
    });
    bt.printType.init();

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#wishPayday").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            settlType: $("#settlType").val(),
            purpose:  $("#purpose").val(),
            wishPayday: $("#wishPayday").val(),
            payerAcc: $("#payerAcc").val(),
            payeeAccName: $("#payeeAccName").val(),
            payeeAcc: $("#payeeAcc").val(),
            payeeBank: $("#payeeBank").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    }

    //showIsPayed 显示已付款标识
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        $("#isPayedTitle").show();
        $("#isPayed").show();
    } else {
        $("#isPayedTitle").hide();
        $("#isPayed").hide();
    }

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var expFbsin = $("#expFbsin").val();
        if (budgetSel == "1" && (expFbsin == "" || expFbsin == "1")) {
            //判断付款方式是否挂资金计划
            var isBudget = $("#isBudget").val();
            if (isBudget == "1") {
                $genOtherHtml(corpId);
            } else {
                $("#tagItemDetail").html("");
            }
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/netbank/nismessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();
});


/**
 * 打印电子回单
 * @param id
 */
function print(id) {
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/netbank/nisbillhead/print?id=" + id
    });
}
/**
 * 打印历史电子回单
 * @param id
 */
function print(id) {
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/netbank/nisbillhead/printHis?id=" + id
    });
}

function view(id) {
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + id
    });
}

function viewAcceBillInfo(type,id,vvid,payId,poolStatus) {
    var data={"type":type,"id":id,"vvid":vvid,"payId":payId};
    if(data.type==0){
        viewAcceBillEmitView(data);
    }else{
        // 票据池查询界面
        if (poolStatus == "1") {
            viewAcceBillPoolView(data)
        } else {
            viewAcceBillReceiveView(data);
        }
    }
}

function viewAcceBillPoolView(data) {
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/billPool/pdtst/view/" + data.id
    });
}

function viewAcceBillEmitView(data){
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + data.id + "/" + data.payId
    });
}

function viewAcceBillReceiveView(data){
    layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId="+data.vvid+"&accId="+data.id,
    });
}
