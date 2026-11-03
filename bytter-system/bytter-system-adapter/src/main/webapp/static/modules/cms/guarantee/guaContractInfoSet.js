var dateTypes, moneyTypes, selectTypes, dicts,corp;
var rownum = 0,mulselects;
var guaCode;//担保方式对应code
var selectPre;//前一次选择的担保方式数组
var pDids="";//抵押物id
bt.use([ 'form','jquery', 'checkboxs', 'corp', 'dateType',
    'moneyType', 'selectType', 'dicts', 'bankInfo',
    'btUpload', 'selectTable', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();// 时间
    moneyTypes = bt.moneyType.init();// 金额
    selectTypes = bt.selectType.init();// 下拉框
    dicts = bt.dicts.init();// 字典
    bt.bankInfo.init();// 联行号
    corp = bt.corp.init();
    moneyFun = bt.moneyType.getFun();
    mulselects = bt.mulselect.init();
    selectPre = new Array();
    var count = $("#WayCount").val();
    for (var k = 0; k < count; k++) {
        selectPre.push($("#index" + (k + 1)).val())
    }
    pDids=$("#pDids").val();
});
function createHtml(guaWay) {
    var html = '<div id="div' + guaWay.id + '">';
    html += '<label>' + guaWay.name + '</label>';
    html += '<div class="layui-inline">';
    if (guaWay.isPledge == '1') {//担保方式的是否抵质押字段为是
        html += '<input type="text" id="' + guaWay.code + 'money" data-name="guaDtls[' + guaWay.code + '].money"  value="'+$("#money"+guaWay.code).val()+'" readOnly="true" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + guaWay.code + 'money"  lay-verify="required">';
        html += '<input type="hidden" id="' + guaWay.code + 'wayCodes" name="guaDtls[' + guaWay.code + '].wayCodes" value="' + guaWay.id + '" >';
        html += '<input type="hidden" id="' + guaWay.code + 'pledgeIds" name="guaDtls[' + guaWay.code + '].pledgeIds"  value="'+$("#pledgeIds"+guaWay.code).val()+'">';
        html += '<input type="hidden" id="' + guaWay.id + 'pDYIds"  value="'+$("#pDids"+guaWay.code).val()+'">';
        html += '</div>';
        html += '<div class="layui-inline">';
        html += '<button class="layui-btn" type="button"  data-code="' + guaWay.code + '"   data-pledgeProperty="' + guaWay.pledgeProperty + '" ';
        html += 'onclick="selectGuaPledge(this)">'
            + bt.lang("common.button.select") + '</button>';
        html += '</div>';
    } else {
        html += '<input type="text" id="' + guaWay.code + 'money" data-name="guaDtls[' + guaWay.code + '].money"   value="'+$("#money"+guaWay.code).val()+'" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + guaWay.code + 'money" lay-verify="required">';
        html += '<input type="hidden" id="' + guaWay.code + 'wayCodes" name="guaDtls[' + guaWay.code + '].wayCodes" value="' + guaWay.id + '">';
        html += '</div>';
    }
    html += '</div>';
    $("#guaMoney").append(html);
    bt.moneyType.init();//金额
}


/**判断字符串是否为空*/
bt.empty = function(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "|| ms =="undefined"){
		return true;
	}
	return false;
}
//选择抵质押物清单
function selectGuaPledge(elem){
    var pledgeProperty=$(elem).data('pledgeproperty');
    guaCode=$(elem).data('code');
    var params = beforeSelect(pledgeProperty);
    if(bt.empty(params)){
        return;
    }
    openSelectPage(params);
}
/**
 * 选择前判断必须的参数是否为空
 * @param params
 */
function beforeSelect(pledgeAttr){
	var corpId = $("#corpId").val()==undefined?bt.corp.init().get("corpId").val():$("#corpId").val(); 
	if(bt.empty(corpId) || corpId == 0){
		//选择单位
		parent.layer.msg(bt.lang("gua.contract.checkCorpInfo"), {icon: 2});
		return "";
	}
	var curId = $("#curId").val();//币别
	/*if(bt.empty(curId)){
		//选择币别
		parent.layer.msg(bt.lang("gua.contract.checkCurIdInfo"), {icon: 2});
		return "";
	}*/
	var startDate = dateTypes.get("startDate").val();//担保开始日期
	var endDate = dateTypes.get('endDate').val();//担保结束日期
	if(bt.empty(startDate)||bt.empty(endDate)){
		parent.layer.msg(bt.lang("gua.contract.checkDateInfo"), {icon: 2});
		return "";
	}
	var guaId = $("#guaId").val();
    var pledgeMsg=$("#"+guaCode+"pledgeIds").val();
    if(bt.empty(pledgeMsg)){
        pledgeMsg="";
    }
    var params = {
        corpId : corpId,
        curId : curId,
        startDate : startDate.replace(" ", ""),
        endDate : endDate.replace(" ", ""),
        pledgeAttr : pledgeAttr,
        pDids:pDids,
        // pZids:$("#pZids").val(),
        pledgeMsg:pledgeMsg,
        guaId:guaId
    }
	return params;
}
/**
 * 打开相应的页面
 * @param params
 */
function openSelectPage(params) {
	var param = parseParam(params);
	 if(bt.navtab){
         bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/cms/guarantee/guaContractInfo/toPage?" +param);
         return;
     }
}
//json转url参数
var parseParam = function(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};
/**
 * 页面回调函数
 */
var updateInfo = function(data,type,totalMoney,pIds){
    if(moneyFun.toNumber(totalMoney)>0){
        moneyTypes.get(guaCode+"money").val(totalMoney);
        $("#"+guaCode+"pledgeIds").val(data);
        var guaId=$("#"+guaCode+"wayCodes").val();
        if(type=="0"){
            pDids+=pIds;
            $("#"+guaId+"pDYIds").val(pIds);
        }

    }
    if(moneyFun.toNumber(totalMoney)<=0){
        moneyTypes.get(guaCode+"money").val("");
        $("#"+guaCode+"pledgeIds").val("");
    }
}
/**
 * 拼接担保方式的id,用于担保关联担保方式表
 */
var guaWayIds = function(){
	var guaId = "";
	$('#guaTable #guaMoney div').each(function(i){ 
			guaId+= $(this).attr('id').val();
	});
}
/**
 * 点击融资主体后的回到函数
 */
/*bt.corpCallbackDate=function(data){
	$("#debtorPerson").val(data.id);
	$("#debtorPersonName").val(data.name);
	bt.reSelect();
}*/
/**
 *重新选择币别后执行的回调函数
 */
/*bt.curCallBack = function(data){
	$("#curCode").val(data.id);
	bt.reSelect();
}*/

/**
 *重新选择担保时间后执行的回调函数
 */
bt.dateChange = function(){
	//bt.reSelect();
}
/**
 * 用户重新选择单位,币别,担保时间后
 * 将选择的担保方式和内容清空
 */
bt.reSelect = function(){
    $("#guaMoney").empty();
    $("#pDids").val("");
    $("#pZids").val("");
    $("#guaId").val("");
}

//选择担保方式回调事件
bt.changeGuaWay = function(data) {
    var selectNew=new Array();//选中的最新数据
    var selectChange =new Array();//改变前后变化的数据
    $.each(data,function(i){
        selectNew[i]=data[i].id;
    });

    selectChange=getDif(selectNew,selectPre);
    function getDif(a,b){
        var array1=a.length>=b.length?a:b;
        var array2=a.length>=b.length?b:a;
        return array1.concat(array2).filter(function(v, i, arr) {
            return arr.indexOf(v) === arr.lastIndexOf(v);
        });
    }

    if(selectPre.length>data.length){//选择减少
        $.each(selectChange,function(i){
            var guaDiv=$("#guaMoney").find('#div' + selectChange[i]);
            if(guaDiv.length>0){
                var pd=$("#"+selectChange[i]+"pDYIds").val();
                if(!bt.empty(pd)){
                    pDids=pDids.replace(pd,"");
                }
                guaDiv.remove();
            }
        });
    }else if(selectPre.length<data.length){//选择增多
        $.each(data,function(i){
            var guaDiv=$("#guaMoney").find('#div' + data[i].id);
            if(guaDiv.length>0){
                return true;
            }
            createHtml(data[i])
        });
    }
    selectPre.splice(0,selectPre.length);
    selectPre=selectNew;
}

//初始化担保方式事件
bt.initGuaWay = function(data) {
    var guaWaysValue = $("#guaWays").val();
    if(guaWaysValue.length){
        var arr=new Array();
        arr = guaWaysValue.split(",");
        $.each(data,function(i){
            for(var j=0;j<arr.length;j++)
            {
                var str1 =data[i].id;
                var str2 = arr[j];
                if(str1==str2){
                    createHtml(data[i]);
                }
            }
        });
    }
}
