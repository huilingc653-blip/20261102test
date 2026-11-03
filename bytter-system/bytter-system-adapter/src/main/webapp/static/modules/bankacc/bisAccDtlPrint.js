bt.use(['form','moneyType','printType'],function () {
    bt.moneyType.init();
});

function prints() {
    var LODOP = getLodop();
    document.getElementById("buttonDiv").style.display = 'none';
    LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Width:90%");
    LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.documentElement.innerHTML);
    document.getElementById("buttonDiv").style.display = '';
    if (LODOP.CVERSION){
        CLODOP.On_Return=function(TaskID,Value){
            if(Value>0){
                printSuccess();
            }else{
                layer.msg(bt.lang('common.message.print.failure'));
                return;
            }
        };
        LODOP.PREVIEW();
    }else{
        var _val=LODOP.PREVIEW();
        if(_val>0){
            printSuccess();
        }else{
            layer.msg(bt.lang('common.message.print.failure'));
            return;
        }
    }
}

function printSuccess() {
    var param={
        corpId:$("#corpId").val(),
        billNo:$("#billNo").val(),
        date:(new Date()).getTime()
    };
    var url=basePath + "/receipt/billPrint/prints";
    $.ajax({
        url: url, method: 'POST', data: param, async:false, dataType: 'json',
        success: function (data) {
            if (data && data.success) {
                layer.msg(bt.lang('common.message.print.success'));
            }
        }
    });
}

function getStatusValue(ValueType,ValueIndex,oResultOB){
    LODOP=getLodop();
    if (LODOP.CVERSION) {
        LODOP.On_Return=function(TaskID,Value){oResultOB=Value;};
    }
    var strResult=LODOP.GET_VALUE(ValueType,ValueIndex);
    if (!LODOP.CVERSION) {
        return strResult;
    } else {
        return "";
    }
};

var closeForms = function () {
    parent.layer ? parent.layer.closeAll() : layer.closeAll();
};