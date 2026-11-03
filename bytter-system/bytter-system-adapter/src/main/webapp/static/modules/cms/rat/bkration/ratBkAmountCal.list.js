var corpInit;
var dateType;
var selectType;
var dicts;
var moneyType;
var moneyFun,mulSelect;
bt.use(['form','bttable','btdate','datatable','corp','moneyType','laypage','dateType','selectType',
    'date','dicts','jquery','element','selectTable','page','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    dateType=bt.dateType.init();
    selectType=bt.selectType.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    corpInit = bt.corp.init();
    mulSelect=bt.mulselect.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    //监听提交
    form.on('submit(search)', function(data) {
        search();
    });

    //导出表格
    form.on('submit(excel)', function(data) {
        if(!checkTables()){
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }else if(checkTables()){
            location.href=basePath + "/cms/report/ratBkAmountCalculate/excel?"+getParam();
        }
    });

    //监听重置
    resetCode = function(){
        var inputs = $('.rCode').find('input');
        inputs.each(function(i, item){
            $(this).val("");
        });

        var selects = $('.rCode').find('.bt-biz-selectType,.bt-biz-dict');
        bt.each(selects,function () {
            $(this).find('select').val(' ');
        })
        form.render('select');
        $("#bankCode").val("");
        corpInit.get("corpId").val("");
        $('#itemCode').data('defaultvalue','');
        mulSelect.get("itemCode").reset();
        mulSelect.get("curCode").reset();
        selectType.get("curCode").val("");
        dicts.get("isConvertCur").val(0);
        selectType.get("convertCurId").val("");
        var nowdate = bt.date.format(new Date(), 'yyyy-MM-dd');
        bt.dateType.init().get("calEndDate").val(nowdate);
        isConvertCallBack();
    }

});

function search(){
    location.href = basePath + "/cms/report/ratBkAmountCalculate/queryBkAmountCalData?"+getParam();
}


var getParam = function(){
    var itemCode=$("input[name='itemCode']").val();
    var isConvertCur=dicts.get('isConvertCur').val();
    var curCode;
    if(isConvertCur=="1"){
        curCode=$("input[name='curCode']").val();
    }else{
        curCode=selectType.get('curCode').val();
    }
    var param="";
    param +="&bankCode="+$("#bankCode").val()+"&corpId="+corpInit.get('corpId').val()+
        "&itemCode="+itemCode+"&curCode="+curCode+
        "&isConvertCur="+isConvertCur+"&convertCurId="+selectType.get('convertCurId').val()+
        "&exRateType="+selectType.get('exRateType').val()+
        "&calEndDate="+dateType.get('calEndDate').val()+"&searchFlag=1";
    return param;
}

/**表格不为空验证*/
var checkTables = function () {
    var  isreturn =true;
    //获取所有tr
    var trs=$("#appTable").find("tr");
    if(trs.length<=2){
        isreturn =false;
    }
    return isreturn;
}

/**
 * 是否折算本位币回调
 */
function isConvertCallBack() {
    $(".showCur").html("");
    var isConvert = dicts.get("isConvertCur").val();
    var mulCur='<div class="selectDemo bt-biz-mulselectType" data-biz-name="curCode" data-method="get" data-defaultvalue="all" \n' +
        ' data-url="'+ basePath+'/basic/business/btcurrency/json" data-valuefield="id" data-textfield="curName" data-required="true"\n' +
        ' data-name="curCode" data-callback="checkConvertCurCallBack"></div>';
    var singleCur= '<div class="bt-biz-selectType" data-biz-name="curCode" data-url="'+ basePath+'/basic/business/btcurrency/json"' +
        ' data-valuefield="id" data-textfield="curName" data-name="curCode" ></div>';

    if ("1" == isConvert) {//是否折合币种选择是，授信币种可多选
        selectType.get("convertCurId").attr({"data-disabled": false, "data-required": true});
        selectType.get("exRateType").attr({"data-value":"","data-required":true, "data-disabled": false});
        $(".showCur").html(mulCur);
        bt.mulselect.init();
    } else {//是否折合币种选择否，授信币种可单选
        selectType.get("convertCurId").attr({"data-disabled": true, "data-required": false});
        selectType.get("exRateType").attr({"data-value":"","data-required":false, "data-disabled": true});
        $(".showCur").html(singleCur);
        bt.selectType.init();
    }
    form.renderIcon();
}

/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var curIds=$("input[name='curCode']").val();//选择的授信币种
    var convertCurId = selectType.get("convertCurId").val();//选择的折合币种
    if(bt.empty(convertCurId)||bt.empty(curIds)){
        return;
    }
    var exRateType=selectType.get("exRateType").val();//汇率类型
    if (!bt.empty(convertCurId)) {
        $.ajax({
            url: basePath + "/cms/report/ratBkAmountCalculate/checkExchangeRate",
            type:"post",
            data:{
                "curIds":curIds,
                "convertCurId": convertCurId,
                "exRateType":exRateType
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("convertCurId").val("");
                }
            }
        });
    }

}