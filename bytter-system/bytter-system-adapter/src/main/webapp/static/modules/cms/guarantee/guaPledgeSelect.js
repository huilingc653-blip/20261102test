var dateTypes, moneyTypes, selectTypes, dicts,corp;
var mulselects;
var pledgeConfig;
var pledgeMsg;
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
    pledgeMsg=$("#pledgeMsg").val();
    pledgeConfig=$("#pledgeConfig").val();<!-- 业务模块是否挂抵质押参数设置-->


    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    var guaPledgeMoney=$("#guaPledgeMoney").val();
    if(!bt.empty(guaPledgeMoney)){
        $("#viewBtn").show();
    }

    if(pledgeConfig=="1"){
        //页面加载后判断是否挂接抵质押,没有则隐藏填写的相关信息
        var isRelatePledge = $("#isRelatePledge").val();
        if(isRelatePledge=="0"||bt.empty(isRelatePledge)){
            $(".pledgeTd").hide();
            dicts.get("isRelatePledge").attr({ 'data-defaultvalue':0});
            $("input[data-id='guaPledgeMoney']").removeAttr("lay-verify");
            $("#guaPledgeMoney").removeAttr("lay-verify");
        }else if(isRelatePledge=="1"){
            $(".pledgeTd").show();
            dicts.get("isRelatePledge").attr({ 'data-defaultvalue':1});
            $("#guaPledgeMoney").attr("lay-verify", "required");
        }
        form.renderIcon();
    }


    /**设置抵质押条件参数*/
    bt.setPledgeParam = function () {
        var param = new Object();
        setOtherParam(param);//选择抵质押前设置其他参数（corpId 网点，startDate 开始日期，endDate，结束日期，curId 币别）
        // var pledgeIds=$("#pledgeIds").val();
        // param["pledgeIds"] = bt.empty(pledgeIds) ? "" : pledgeIds;
        param["pledgeMsg"] = bt.empty(pledgeMsg) ? "" : pledgeMsg;
        var contractIds=$("#contractIds").val();
        param["contractIds"] = bt.empty(contractIds) ? "" : contractIds;
        return parseParam(param);
    }

});


//是否挂接抵质押控制
bt.setPledgeInfo = function(data){
    if("1" == data.value){
        $(".pledgeTd").show();
        $("#guaPledgeMoney").attr("lay-verify", "required");
    } else {
        $("input[data-id='guaPledgeMoney']").removeAttr("lay-verify");
        $("#guaPledgeMoney").removeAttr("lay-verify");
        $(".pledgeTd").hide();
        $("#viewBtn").hide();
        resetPledgeMsg();
    }
    form.renderIcon();
}
//跳转到选择抵质押页面
function addGuaPledge() {
    if(selectBefore()){//校验网点
        var param = bt.setPledgeParam();
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/cms/guarantee/pledgeOccupy/addGuaPledgeOccupy?" + param);
            return;
        }
    }
}


//抵质押选择完成之后回写数据
function updatePledgeInfo(totalMoney,dataInfo,pledgeIds,pledgeMoney) {
    resetPledgeMsg();//清空抵质押信息
    pledgeMsg=dataInfo;
    var guaMoney=moneyFun.format(0);
    //设置抵质押号
    if (!bt.empty(totalMoney)) {
        // pledgeNos=pledgeNos.substring(0, pledgeNos.length - 1);
        guaMoney=moneyFun.format(totalMoney);
    }
    moneyTypes.get("guaPledgeMoney").val(guaMoney);
    //设置抵质押id值
    if (!bt.empty(pledgeIds)) {
        var idArr=pledgeIds.split(",");
        var moneyArr=pledgeMoney.split(";");
        var html="";
        for(var j=0;j<idArr.length-1;j++){
            html += '<input type="hidden" name="pledgeId[' + j + ']"  value="'+idArr[j]+'">';
        }
        for(var k=0;k<moneyArr.length-1;k++){
            html += '<input type="hidden" name="pledgeMoney[' + k + ']"  value="'+moneyArr[k]+'">';
        }
        $("#guaPledgeSpan").html(html);
        $("#pledgeIds").val(pledgeIds);
    }

    $("#viewBtn").show();
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
 * 查看抵质押单
 * @param params
 */
function viewGuaPledge() {
    var param = bt.setPledgeParam();
    layer.open({
        type: 2,
        area: ['980px', '430px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/pledgeOccupy/viewPledgeOccupy?" + param,
        end: function () {

        }
    });
}

//重置抵质押相关信息
function resetPledgeMsg(){
    var isPledge=dicts.get("isRelatePledge").val();
    if(!bt.empty(isPledge)&&isPledge=="1"){
        $("#guaPledgeMoney").val("");
        $("#pledgeIds").val("");
        $("#pledgeMsg").val("");
        pledgeMsg="";
        $("#guaPledgeSpan").html("");
    }
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

