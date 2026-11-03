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
    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var expFbsin = $("#expFbsin").val();
        if (budgetSel == "1" && (expFbsin == "" || expFbsin == "1")) {
            $genOtherHtml(corpId);
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/ttpay/tpLog/listMsgData";
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            data: {quoteId: $('#id').val()},
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

function viewAcceBillInfo(type,id,vvid) {
    var data={"type":type,"id":id,"vvid":vvid};
    if(data.type==0){
        viewAcceBillEmitView(data);
    }else{
        viewAcceBillReceiveView(data);
    }
}

function viewAcceBillEmitView(data){
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + data.id
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
