var dateTypes, moneyTypes, selectTypes, dicts,corp,moneyFun,mulselects;
var guaCode;//担保方式对应code
var selectPre;//前一次选择的担保方式数组
var pDids="";
var corp;
var form;
var status; // 在对接合同管理的时候用来判断是暂存还是提交，来执行提交还是暂存
var rowIndex = 0;
bt.use([ 'form', 'jquery', 'checkboxs', 'corp', 'dateType','conSelect','conView',
			'moneyType', 'selectType', 'dicts', 'bankInfo',
			'btUpload', 'selectTable','mulselect' ],function() {
					$ = bt.jquery, form = bt.form(), fun = bt.moneyType
							.getFun();
					dateTypes = bt.dateType.init();// 时间
					moneyTypes = bt.moneyType.init();// 金额
					selectTypes = bt.selectType.init();// 下拉框
					dicts = bt.dicts.init();// 字典
					bt.bankInfo.init();// 联行号
					corp = bt.corp.init();
					moneyFun = bt.moneyType.getFun();
					mulselects=bt.mulselect.init();
					selectPre=new Array();
                    conSelectInit = bt.conSelect.init(); // 合同挂接组件初始化
                    conView = bt.conView.init(); //  合同查看组件初始化
					var count=$("#WayCount").val();
					for(var k=0;k<count;k++){
                        selectPre.push($("#index"+(k+1)).val())
                    }

                    pDids=$("#pDids").val();
					// 附件处理
					var gid = $("#annexName").val();
					var up = bt.btUpload.init({
						elem : "#uploader",
						isExpand : true,
						auto : false,
						gid : gid
					});

					// 监听提交
					form.on('submit', function(data) {
                        bt.loading.show();//显示加载中
						// 担保额度校验，当担保额度为空或者为0时，提示用户检查担保额度
						var action = "/cms/guarantee/guaExtContractInfo/tempOrSubmit";
						data.field.status = $(this).attr("value");
						data.field.attachmentId = up.getGroupId();
                        // 如果担保对接了合同
                        if($("#relateCon").val()==1){
                            if ($("#conType").val()==1 && bt.empty($("#conId").val())){ // 业务选择合同
                                bt.loading.hide(); //关闭加载中
                                layer.msg(bt.lang('cms.conInfo.unSelect'));
                                return false;
                            }
                            if ($("#conType").val()==0){ // 业务生成合同
                                var corpId = $("#corpId").val();
                                var conTypeId = $("#conTypeId").val();
                                if (bt.empty(conTypeId)){
                                    conTypeId = getConTypeExtGua(corpId);
                                }
                                if (typeof conTypeId === "boolean"){
                                    bt.loading.hide(); //关闭加载中
                                    status = data.field.status; // 对接合同管理的状态标识，用于区分业务是提交还是暂存
                                    return false;
                                }
                                data.field.conTypeId = conTypeId;
                                $("#conTypeId").val("");
                            }
                        }
                        $.post(basePath + action,data.field,function(result){
                            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                            bt.loading.hide(); //关闭加载中
                            if(result.success){
                                if(!bt.navtab){
                                    parent.layer?parent.layer.closeAll():layer.closeAll();
                                } else {
                                    bt.navtab.closeCurrent();
                                    bt.navtab.refreshParentTable();
                                }
                            }
                        },'JSON');
					return false;
		});

    initGuaWayDiv();
});
function createHtml(guaWay) {
    var html = '<div id="div' + guaWay.id + '">';
    html += '<label>' + guaWay.name + '</label>';
    html += '<div class="layui-inline">';
    if (guaWay.isPledge == '1') {//担保方式的是否抵质押字段为是
        html += '<input type="text" id="' + guaWay.code + 'money" data-name="guaDtls[' + rowIndex + '].money"  value="'+$("#money"+guaWay.code).val()+'" readonly="readonly" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + guaWay.code + 'money"  lay-verify="required">';
        html += '<input type="hidden" id="' + guaWay.code + 'wayCodes" name="guaDtls[' + rowIndex + '].wayCodes" value="' + guaWay.id + '" >';
        html += '<input type="hidden" id="' + guaWay.code + 'pledgeIds" name="guaDtls[' + rowIndex + '].pledgeIds"  value="'+$("#pledgeIds"+guaWay.code).val()+'">';
        html += '<input type="hidden" id="' + guaWay.id + 'pDYIds"  value="'+$("#pDids"+guaWay.code).val()+'">';
        html += '</div>';
        html += '<div class="layui-inline">';
        html += '<button class="layui-btn" type="button"  data-code="' + guaWay.code + '" data-pledgeProperty="' + guaWay.pledgeProperty + '" ';
        html += 'onclick="selectGuaPledge(this)">'
            + bt.lang("common.button.select") + '</button>';
        html += '</div>';
    } else {
        html += '<input type="text" id="' + guaWay.code + 'money" data-name="guaDtls[' + rowIndex + '].money" value="'+$("#money"+guaWay.code).val()+'" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + guaWay.code + 'money" lay-verify="required">';
        html += '<input type="hidden" id="' + guaWay.code + 'wayCodes" name="guaDtls[' + rowIndex + '].wayCodes" value="' + guaWay.id + '">';
        html += '</div>';
    }
    html += '</div>';
    $("#guaMoney").append(html);
    rowIndex++;
    bt.moneyType.init();//金额
}

/**判断字符串是否为空*/
bt.empty = function(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "||ms=="undefined"){
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
	var curId = bt.selectType.init().get("curId").val();//币别
	if(bt.empty(curId)){
		//选择币别
		parent.layer.msg(bt.lang("gua.contract.checkCurIdInfo"), {icon: 2});
		return "";
	}
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

/**加载还款计划*/
function repayLan(){
    var id = $("#id").val();
    var params = "id=" +id;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/guarantee/guaExtContractInfo/toRepayPlan?" + params);
    }
}



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
bt.corpCallbackDate=function(data){
	//$("#debtorPerson").val(data.name);
	$("#corpId").val(data.id);
	bt.reSelect();
    // 清空业务对接的合同信息
    var relateCon=$("#relateCon").val();
    var conType=$("#conType").val();
    clearConInfo();
}
/**
 *重新选择币别后执行的回调函数
 */
bt.curCallBack = function(data){
	$("#curCode").val(data.id);
	bt.reSelect();
}

/**
 *重新选择担保时间后执行的回调函数
 */
bt.dateChange = function(){
	bt.reSelect();
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
    selectPre=new Array();
    var guaWayIds=mulselects.get("guaWay").val();
    if(!bt.empty(guaWayIds)){
        mulselects.get("guaWay").val({
            'defaultvalue':''});
    }else{//重置的时候
        mulselects.get("guaWay").val({
            'defaultvalue':$("#guaWays").val()});
    }


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

// bt.initGuaWay = function(data) {
//
// }
//初始化担保方式事件
function initGuaWayDiv(){
    var guaWaysValue = $("#guaWays").val();
    // mulselects.get("guaWay").val({
    //     'defaultvalue':guaWaysValue});
    if(guaWaysValue.length){
        var arr=new Array();
        arr = guaWaysValue.split(",");
        // 外币
        $.ajax({
            url: basePath+"/cms/guarantee/guaContractInfo/json",
            type: "get",
            dataType: "json",
            success: function (data) {
                $.each(data, function (i) {
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
        });
    }
}

/**
 * 合同类型选择回调函数
 * @param conTypeId
 */
function setConTypeCallback(conTypeId){
    $("#conTypeId").val(conTypeId);
    if (status ==11 ){
        $("#submit").trigger('click');
    }
    if (status == 0){
        $("#temp").trigger('click');
    }
}

//清空担保选择数据
function clearData(){
    selectPre=new Array();
    initGuaWayDiv();
}

/***
 * 是否集团内担保单位回调函数
 * @param data
 */
bt.changeType = function (data) {
    var html = '';
    if (data.value == '0'){
        html = '<input class="layui-input" id="debtorPerson" name="debtorPerson" type="text" lay-verify="required">';
        $("#debtorPersonDiv").empty();
        $("#debtorPersonDiv").html(html);
    }else if (data.value == '1'){
        html += '<div class="bt-biz-corp"';
        html += 'data-biz-type="corp"';
        html += 'data-type="selectTree"';
        html += 'data-name="debtorPerson"';
        html += 'data-required="true"';
        html += 'data-scope="all"';
        html += '</div>';
        $("#debtorPersonDiv").empty();
        $("#debtorPersonDiv").html(html);
    }
    corp.render();
}
