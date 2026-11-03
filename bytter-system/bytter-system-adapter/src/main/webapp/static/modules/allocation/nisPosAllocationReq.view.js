bt.use(['form','moneyType','capNumber','btUpload','printType'], function(){
    var $ = bt.jquery,form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    // 金额大写
    bt.capNumber.init({
        elem:'#money',
        linkElemDiv:"#moneyUp"
    });
    // 初始化收款人信息
    $.ajax({
        url: basePath + "/bankacc/btbankacc?getBankAccById",
        method: 'POST',
        data: {id: $("#payeeAccId").val(), curId: $("#curId").val()},
        dataType: 'json',
        success: function(result){
            var data = result.obj;
            initFcAccBalance(data.bankAcc, "payeeFcAccBalance");
            $("#payeeProvCity").text(data.provName + "  " + data.cityName);
        }
    });
    // 初始化付款人信息
    $.ajax({
        url: basePath + "/bankacc/btbankacc?getBankAccById",
        method: 'POST',
        data: {id: $("#payerAccId").val(), curId: $("#curId").val()},
        dataType: 'json',
        success: function(result){
            var data = result.obj;
            initFcAccBalance(data.bankAcc, "payerFcAccBalance");
            $("#payerProvCity").text(data.provName + "  " + data.cityName);
        }
    });
    bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete:false,
        hasUpload:false,
        gid: $("#attachmentId").val()
    });

    bt.printType.init();

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml= function() {
        var url=basePath + "/netbank/nismessage/list/"+$('#id').val()+"/"+$("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url:url,
            method: 'GET',
            async:false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();
})

/**
 * 设置内部账户余额
 * @param bankAcc
 * @param initId
 */
function initFcAccBalance(bankAcc, initId){
    var $ = bt.jquery;
    var moneyFun = bt.moneyType.getFun();
    // 加载付款账户单位
    $.ajax({
        url: basePath + "/inneraccount/fconlineledger?getByBankAcc",
        method: 'POST',
        data: {bankAcc: bankAcc, curId: $("#curId").val()},
        dataType: 'json',
        success: function(result){
            var data = result.obj;
            if(null == data){
                $("#" + initId).text("0.00");
            }else{
                $("#" + initId).text(moneyFun.format(data.checkBalance));
            }
        }
    });
}