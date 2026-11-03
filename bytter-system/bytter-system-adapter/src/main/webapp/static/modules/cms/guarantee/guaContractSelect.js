var dateTypes, moneyTypes, selectTypes, dicts,corp;
var mulselects;
var isGuarantee;
var viewFlag;//查看页面标志
bt.use(['form', 'jquery', 'checkboxs', 'corp', 'dateType',
    'moneyType', 'selectType', 'dicts',
    'btUpload', 'selectTable', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form(), fun = bt.moneyType
        .getFun();
    dateTypes = bt.dateType.init();// 时间
    moneyTypes = bt.moneyType.init();// 金额
    selectTypes = bt.selectType.init();// 下拉框
    dicts = bt.dicts.init();// 字典
    corp = bt.corp.init();
    moneyFun = bt.moneyType.getFun();
    mulselects = bt.mulselect.init();

    isGuarantee=$("#isGuarantee").val();<!-- 业务模块是否挂担保参数设置-->

    viewFlag=$("#viewFlag").val();
    if(viewFlag=="1"){
        $("#guaBtn").show();
        $("#guaranteeRmk").attr("readonly","readonly");
    }

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    if(isGuarantee=="1"){
        //页面加载后判断是否挂接担保,没有则隐藏填写的相关信息
        var isHangGuarantee = $("#isHangGuarantee").val();
        if(isHangGuarantee=="0"){
            $(".isHangGua").hide();
            dicts.get("isHangGuarantee").attr({ 'data-defaultvalue':0});
            $("#guaContractCodes").removeAttr("lay-verify");
            form.renderIcon();
        }else if(isHangGuarantee=="1"||bt.empty(isHangGuarantee)){
            $(".isHangGua").show();
            dicts.get("isHangGuarantee").attr({ 'data-defaultvalue':1});
            $("#guaContractCodes").attr("lay-verify", "required");
            form.renderIcon();
        }
    }

    if(isGuarantee=="1"){
        //页面加载后如果没有挂接担保则隐藏填写信息
        if(dicts.get("isHangGuarantee").val()=="0"){
            $(".isHangGua").hide();
        }
        // else{
        //     var contractComb=$("#contractIds").val();
        //     if(contractComb!=null||contractComb!=""){
        //         var arr=contractComb.split(";");
        //         $("#guaContractCodes").val(arr[1]);//担保合同号
        //     }
        // }
    }

    /**设置担保合同条件参数*/
    bt.setParam = function () {
        var param = new Object();
        setOtherParam(param);//选择担保合同前设置其他参数（corpId 网点，startDate 开始日期，endDate，结束日期，curId 币别）
        // var contractComb = $("#contractIds").val();
        var contractIds=$("#contractIds").val();
        // if (!bt.empty(contractComb)) {
        //     var arr = contractComb.split(";");
        //     contractIds = arr[0];//担保合同id
        // }
        param["contractIds"] = bt.empty(contractIds) ? "" : contractIds;
        return parseParam(param);
    }

});


//是否挂接担保控制
bt.setGuaInfo = function(data){
    if("1" == data.value){
        $(".isHangGua").show();
        $("#guaContractCodes").attr("lay-verify", "required");
    } else {
        $("#guaContractCodes").removeAttr("lay-verify");
        $(".isHangGua").hide();
        $("#guaBtn").hide();
        resetGuaMsg();
    }
    form.renderIcon();
}
//跳转到选择担保合同页面
function addGuaContract() {
    if(selectBefore()){//校验网点
        var param = bt.setParam();
        top.guaContractParentWindow = window;
        top.layer.open({
            type: 2,
            area: ['980px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/guaContractInfo/addGuaContract?" + param,
            end: function () {
                delete top.guaContractParentWindow;
            }
        });
    }
}


//担保合同选择完成之后回写数据
function updateInfo(contractIds, contractCodes,guaWayNames,guaWayCodes) {
    resetGuaMsg();//清空担保信息
    //设置担保合同号
    if (!bt.empty(contractCodes)) {
        $("#guaContractCodes").val(contractCodes);
    }
    //设置担保合同id值
    if (!bt.empty(contractIds)) {
        var idArr=contractIds.split(",")
        var html="";
        for(var j=0;j<idArr.length;j++){
            html += '<input type="hidden" name="contractIds[' + j + ']"  value="'+idArr[j]+'">';
        }
        $("#guaContractSpan").html(html);
        // $("#contractIds").val(contractIds + ";" + contractCodes);
        $("#contractIds").val(contractIds);
    }
    //设置担保方式值
    if (!bt.empty(guaWayNames)) {
        var guaWayArr=new Array();
        var guaWay=new Array();
        guaWayNames=guaWayNames.substring(0, guaWayNames.length - 1);
        guaWayArr=guaWayNames.split("、");//去除最后一个顿号并分割
        guaWay=unique1(guaWayArr);
        var guaWays="";
        for(var i=0;i<guaWay.length;i++){
            if (i == 0) {
                guaWays = guaWay[i];
            } else {
                guaWays += "," + guaWay[i];
            }
        }
        $("#guaWayNames").val(guaWays);
    }

    //设置担保方式Code
    if (!bt.empty(guaWayCodes)) {
        var guaWayArr=new Array();
        var guaWay=new Array();
        guaWayArr=guaWayCodes.split(",");
        guaWay=unique1(guaWayArr);
        var wayCodes="";
        for(var i=0;i<guaWay.length;i++){
            if (guaWay[i] != null && guaWay[i] != "" && guaWay[i] != undefined && guaWay[i] != " ") {
                if (i == 0) {
                    wayCodes = guaWay[i];
                } else {
                    wayCodes += "," + guaWay[i];
                }
            }
        }
        $("#guaWay").val(wayCodes);
    }


    $("#guaBtn").show();
}

function unique1(array) {
    for (var i=array.length-1; i>=0; i--)
    {
        var targetNode = array[i];
        for (var j=0; j<i; j++)
        {
            if(targetNode == array[j]){
                array.splice(i,1);
                break;
            }
        }
    }
    return array;
}


    /**
 * 查看担保合同单
 * @param params
 */
function viewGuaContract() {
    var param = bt.setParam();
    layer.open({
        type: 2,
        area: ['980px', '430px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaContractInfo/getContractList?" + param,
        end: function () {

        }
    });
}

//重置担保相关信息
function resetGuaMsg(){
    $("#guaContractCodes").val("");
    $("#contractIds").val("");
    $("#guaWay").val("");
    $("#guaWayNames").val("");
    $("#guaContractSpan").html("");
    $("#guaranteeRmk").val("");
}

function checkGuaMsg() {
    //检查选择挂接担保时是否选择担保合同
    var isHangGua = dicts.get("isHangGuarantee").val();
    if (isHangGua == "1") {
        var contractCodes = $("#guaContractCodes").val();
        var contractIds = $("#contractIds").val();
        if (bt.empty(contractCodes) || bt.empty(contractIds)) {
            layer.alert(bt.lang("cms.rat.input.checkGuaContract"), {icon: 2});
            return false;
        }
    }
    return true;
}

//json转url参数
var parseParam = function (param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function (i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};

