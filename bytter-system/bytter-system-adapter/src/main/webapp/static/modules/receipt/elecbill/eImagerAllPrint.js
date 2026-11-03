bt.use(['form','moneyType','printType'], function(){
    bt.moneyType.init();
//            bt.printType.init();
});
var getAllowOnce=function(size){
    if(!size){
        size=1;
    }
    var allowOnceArr;
    bt.jquery.ajax({
        type: "GET",
        url: basePath + "/allowOnce?size="+size,
        dataType:"json",
        async: false,
        success: function(result){
            if(result){
                allowOnceArr = result;
            }
        }
    });
    return allowOnceArr;
};
function prints() {
    var printType = $("#printType").val();
    var LODOP=getLodop();
    var allowOnce=getAllowOnce();
    $('input[name="fileName"]').each(function(i){
        var  fileName = $(this).val();
        var srcs = basePath + "/download/attachment?id=" + fileName + "&_allow_once_=" + allowOnce;
        $("#imger"+$(this).attr("alt")).attr("src", srcs);
    })


    LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Auto-Width");
    document.getElementById('buttonDiv').style.display = 'none';
    //LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.getElementById("print_doc1").innerHTML);
    var printHtmlPre = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>";
    printHtmlPre+=$('head').html();
    printHtmlPre+="</head>"
    if(printType == "2") {
        var printDocLength = $('div[name="print_doc1"]').length;
        var printHtml = "";
        $('div[name="print_doc1"]').each(function (i) {
            var no = i+1;
            if(no%2 == 0){
                printHtml = printHtml + $(this).html();
            } else {
                printHtml = $(this).html()+'</br></br></br>';
            }
            if(no%2 == 0 || no == printDocLength) {
                //A4一页两份（纵向）
                LODOP.SET_PRINT_PAGESIZE(3, 0, 0, "A4");
                LODOP.ADD_PRINT_HTM(0, 10, "100%", "100%", printHtmlPre + printStyle + '<body>' + printHtml + '</body>');
                LODOP.NewPageA();
            }
        })
    } else {
        $('div[name="print_doc1"]').each(function (i) {
            if (printType == "1") {
                //A4一页一份（横向）
                LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
            } else if (printType == "3") {
                //A5一页一份（横向）
                LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A5");
            }
            LODOP.ADD_PRINT_HTM(0, 10, "100%", "100%", printHtmlPre + printStyle + '<body>' + $(this).html() + '</body>');
            LODOP.NewPageA();
        })
    }
    // LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.documentElement.innerHTML);
    document.getElementById('buttonDiv').style.display = '';
    // LODOP.PREVIEW();
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

        if(LODOP.PRINTA()){
            printSuccess();
        }else{
            layer.msg(bt.lang('common.message.print.failure'));
            return;
        }
    }



};


function printSuccess() {
    var corpIds = "";
    var billNos = "";
    $('input[name="corpId"]').each(function(i){
        if (corpIds == "") {
            corpIds += $(this).val();
        } else {
            corpIds += "," + $(this).val();
        }
    })
    $('input[name="billNo"]').each(function(i){
        if (billNos == "") {
            billNos += $(this).val();
        } else {
            billNos += "," + $(this).val();
        }
    })
    var param={
        corpIds:corpIds,
        billNos:billNos,
    };
    var url=basePath + "/receipt/billPrint/BatchPrints";
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
