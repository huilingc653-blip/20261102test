function viewBill(reqId) {
    var fexTranTypeCode = null;
    var tranType = $("#tranType").val();
    if ("2" == tranType) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingReq/viewOption/" + reqId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['950px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexTradingReq/viewOption/" + reqId
        });
    } else{
        if ($("#isSwapTran").val()==='1') {
            fexTranTypeCode = "swap";
        } else if($("#fexTranType").val()==='0'){
            fexTranTypeCode = "spot";
        } else if($("#fexTranType").val()==='1'){
            fexTranTypeCode = "forward";
        }
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingReq/view/" +fexTranTypeCode+ "/" + reqId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['950px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexTradingReq/view/" +fexTranTypeCode+ "/" + reqId
        });
    }
}


function checkDate(dataSource){
    var tranAddrId = $("#tranAddrId").val();
    var proId = $("#proId").val();
    var settleDate = dateType.get("settleDate").val();
    if ("closeSettleDate" == dataSource) {
        settleDate = dateType.get("closeSettleDate").val();
    }
    if(tranAddrId && proId && settleDate){
        $.ajax({
            "url": basePath + "/foreign/fexTradingLifecycle/checkWorkDate",
            data:{
                tranAddrId:tranAddrId,
                proId:proId,
                settleDate:settleDate
            },
            type:"post",
            dataType:"json",
            async: false,
            success:function (data) {
                if (data.success) {
                    if (false == data.obj) {
                        if ("settleDate" == dataSource) {
                            dateType.get("settleDate").val('');
                        } else if ("closeSettleDate" == dataSource) {
                            dateType.get("closeSettleDate").val('');
                        }
                        parent.layer.alert(bt.lang("fex.settle.date.not.work"), {icon: 7});
                    }
                }
            }
        });
    }
}