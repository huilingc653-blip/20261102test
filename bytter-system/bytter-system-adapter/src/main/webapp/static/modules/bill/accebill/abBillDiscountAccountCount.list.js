var corp,dicts,moneyType,dateType;
bt.use(['form','datatable','moneyType','dateType','corp','select','dicts','date'], function(){
    $ = bt.jquery,form = bt.form();
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    dateType=bt.dateType.init();
});

//查询
function selectData() {
    //查询条件
    var statistical = dicts.get("statistical").val();
    var gbusinessDate = $("#gbusinessDate").val();
    var lbusinessDate = $("#lbusinessDate").val();
    //请选择统计方式!
    if(!$.trim(statistical)){
       layer.alert(bt.lang("accbill.discountaccountcount.statistical.isnotnull"),{icon:7});
       return;
    }
    //请选择日期范围!
    if(!($.trim(gbusinessDate)&&$.trim(lbusinessDate))){
        layer.alert(bt.lang("accbill.discountaccountcount.date.isnotnull"),{icon:7});
        return;
    }
    //日期范围不能超出30天(月)
    if(statistical=='1'){
    if(bt.date.getBetweenDay(gbusinessDate,lbusinessDate)+1>30){
        layer.alert(bt.lang("accebill.abbillCollectionAccountReport.dayOut"),{icon:7});
        return;
     }
    }else{
        if(bt.date.getBetweenMonth(gbusinessDate,lbusinessDate)>12){
            layer.alert(bt.lang("accebill.abbillCollectionAccountReport.monthOut"),{icon:7});
            return;
        }
    }
    var billFlag = dicts.get("billFlag").val();
    var billType = dicts.get("billType").val();
    var corpId=corp.get("corpId").val();
    var dimensions = dicts.get("dimensions").val();
    var recNm = window.encodeURI(window.encodeURI($("#recNm").val()));
    location.replace(basePath + "/bill/accebill/abbilldiscountaccountCount/list?statistical=" + statistical + "&gbusinessDate=" + gbusinessDate + "&lbusinessDate="
        + lbusinessDate + "&isQuery=" + "1" + "&recNm=" + recNm + "&billFlag=" + billFlag + "&billType=" + billType+ "&corpId=" + corpId+"&dimensions="+dimensions);

}

function resetDate() {
    var statistical = dicts.get("statistical").val();
    $('#dateDiv').html("");
    var html="";
    if(statistical=="0"){
         html = '<div class="layui-inline">'
            +'<input data-required="true" id="gbusinessDate" type="text" name="ged_businessDate" class="layui-input bt-input-inline bt-biz-dateType" data-endid="lbusinessDate"'
            +' data-type="month" data-biz-name="gbusinessDate"  />'
            +'<span class="date-mid">~</span>'
            +'<input type="text" name="led_businessDate" class="layui-input bt-input-inline bt-biz-dateType" id="lbusinessDate"'
            + ' data-type="month" data-biz-name="lbusinessDate"  />'
            +'</div>';
    }else{
        html = '<div class="layui-inline">'
            +'<input type="text" data-required="true" id="gbusinessDate" name="ged_businessDate" class="layui-input bt-input-inline bt-biz-dateType" data-endid="lbusinessDate"'
            +' data-type="date" data-biz-name="gbusinessDate"  />'
        +'<span class="date-mid">~</span>'
        +'<input type="text" name="led_businessDate" class="layui-input bt-input-inline bt-biz-dateType" id="lbusinessDate"'
        + ' data-type="date" data-biz-name="lbusinessDate"  />'
        +'</div>';
    }
    $('#dateDiv').html(html);
    bt.dateType.init();

}

function resetQuery(){
    dicts.get("statistical").val("0");
    corp.get("corpId").val("");
    dicts.get("billFlag").val(" ");
    dicts.get("billType").val(" ");
    $("#gbusinessDate").val("");
    $("#lbusinessDate").val("");
    $("#recNm").val("");
    dict.get("dimensions").val("0");
    resetDate();
}