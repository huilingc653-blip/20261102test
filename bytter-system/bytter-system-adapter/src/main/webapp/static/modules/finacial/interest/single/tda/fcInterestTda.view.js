var moneyType;
var rateType;
var selectType, dicts, corp, element,  accountTable;
bt.use(['form', 'corp', 'element', 'btdate', 'dicts', 'moneyType', 'printType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    element = layui.element();
    moneyType = bt.moneyType.getFun();
    rateType = bt.rateType.getFun();

    // 导出利息清单
    form.on('submit(doExportInterestBill)', function (data) {
        doExportInterestBill();
        return false;
    });

    form.on('submit(doBatchPrint)', function (data) {
        doBatchPrint();//打印记账凭证
        return false;
    });

    form.on('submit(doPrintRateNotice)', function (data) {
        doPrintRateNotice();//打印利息通知单
        return false;
    });
    form.on('submit(doCheck)',function(data){
        checkInterest("check",bt.lang("common.button.check"));//复核
        return false;
    });
    form.on('submit(doBack)',function(data){
        checkInterest("back",bt.lang("common.button.back"));//打回
        return false;
    });
    init();
});
//复核/打回
function checkInterest(opType, opTips){
    var tallyDate = $("#tallyDate").val();
    var endInstDate = $("#endDate").val();
    layer.confirm(bt.lang("finacial.interest.OpInterest",new Array(opTips)), {
        icon: 3,
        btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
    }, function(index, layero){
        bt.loading.show();
        var saveDataAry=[];
        var data = {};
        data.id=$("#id").val();
        data.opType=opType;
        data.netId=$("#netId").val();
        data.accType=$("#accType").val().trim();
        data.tallyDate=tallyDate;
        data.endDate=endInstDate;
        data.poolSign=$("#poolSign").val();
        data.account=$("#account").val();
        data.pageFlag=$("#pageFlag").val();
        data.affixAmount=$("#affixAmount").val();
        saveDataAry.push(data);
        $.ajax({
            type: "POST",
            url: basePath+"/finacial/interest/single/tda?doProcessInterestCheck",
            data: JSON.stringify(saveDataAry),
            dataType : "json",
            contentType:"application/json", // 指定这个协议很重要
            success: function(result){

                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.closeCurrent();
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }
        });
    }, function(){
    });
}