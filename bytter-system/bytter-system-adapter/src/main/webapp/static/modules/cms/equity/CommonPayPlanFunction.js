var zqRate=0;//展期利率
var zqDate="";//展期日期
var remainDate;//已还的最后一笔日期加一天

/**
 * 判断是否为空 true：空 false:不为空
 * @param data
 */
function isNull(data){
	return (data == "" || data == undefined || data == null) ? true : false;
}
/**
 * 导入还款计划(其他)
 */
function addImport(o,surplusMoney) {
	var num=0;
	var isMore=true;
	var invMoney=$("#invMoney").val();
	var accounted=0;
	if(surplusMoney>0){
		if(o.payMoney<invMoney) {
			 accounted = moneyFun.div(o.payMoney , invMoney,4);
		}else {
			 accounted=1;
		}
	}else {
		o.payMoney=maxPayMoney;
		accounted = moneyFun.div(o.payMoney , invMoney,4);
	}

	$("input[divName^='payMoney']").each(function(i){
		num++;
	});
	//出资方式，当为1现金时，可多条明细，当2实物时，只能一条,资产名称必填
	var invWay = $("#invWay").val();
	if(invWay=="1"){
		isMore=true;
	}else if(invWay=="2"){
		isMore=false;
	}
	if(!isMore && num>0){
		layer.alert(bt.lang('cms.equity.addPlanDetailError'), {icon: 7});
		return;
	}
	var html='<tr>';
	html+='<td><input type="text" id="equityCode_'+rowNum+'" name="detailDtl['+rowNum+'].equityCode" value="'+$("#equityCode").val()+'" readonly="readonly" class="layui-input" /></td>';
	html+='<td><input name="detailDtl['+rowNum+'].payDate" data-biz-name="payDate_'+rowNum+'" id="payDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" value="'+o.payDate+'"/></td>';
	html+='<td><input divName="payMoney" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="payMoney_'+rowNum+'" data-callback="bt.calMoney" lay-verify="required" data-biz-name="payMoney_'+rowNum+'" data-min="0" data-max="'+maxPayMoney+'" data-name="detailDtl['+rowNum+'].payMoney" ';
	if(!isMore){
		html+=' disabled value="'+moneyFun.format(invMoney)+'" ';
	}else {
		html+=' value="'+moneyFun.format(o.payMoney)+'" ';
	}
	html+='/></td>';
	html+='<td><input disabled divName="rePayMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="rePayMoney_'+rowNum+'" type="text" data-decimal="2" id="rePayMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].rePayMoney"';
	if(!isMore){
		html+=' value="0" ';
	}else {
		html+=' value="'+surplusMoney+'" ';
	}
	html+='/></td>';
	html+='<td align="center"><input disabled divName="accounted" class="layui-input   bt-biz-rateType" data-biz-name="accounted_'+rowNum+'"  type="text"   id="accounted_'+rowNum+'"   data-name="detailDtl['+rowNum+'].accounted"';
	if(!isMore){
		html+=' value="1" ';
	}else {
	html+=' value="'+accounted+'" ';
	}
	html+='/></td>';
	html+='<td><input type="text" id="assName_'+rowNum+'" name="detailDtl['+rowNum+'].assName"  class="layui-input" value="'+o.assName+'"';
	if(!isMore){
		html+=' lay-verify="required" ';
	}
	html+='/></td>';
	html+='<td><input type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isPay" value="1"';
	if(o.isPay=="1"){
		html+=' checked ';
	}
	html+='/></td>';
	html+='<td><input name="detailDtl['+rowNum+'].rmk" id="rmk"  class="layui-input" type="text" value="'+o.rmk+'"/></td>';
	html+='<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button></td>';
	html+='</tr>';
	$("#planDetailTB tbody").append(html);
	addFun();
	sumMoney();
};
    /**
     * 新增还款计划(其他)
     */
	function add(){
		var num=0;
		var isMore=true;
		var invMoney=$("#invMoney").val();
		$("input[divName^='payMoney']").each(function(i){
			num++;
		});
		//出资方式，当为1现金时，可多条明细，当2实物时，只能一条,资产名称必填
		var invWay = $("#invWay").val();
		if(invWay=="1"){
			isMore=true;
		}else if(invWay=="2"){
			isMore=false;
		}
		if(!isMore && num>0){
			layer.alert(bt.lang('cms.equity.addPlanDetailError'), {icon: 7});
			return;
		}
        var html='<tr>';
        html+='<td><input type="text" id="equityCode_'+rowNum+'" name="detailDtl['+rowNum+'].equityCode" value="'+$("#equityCode").val()+'" readonly="readonly" class="layui-input" /></td>';
        html+='<td><input name="detailDtl['+rowNum+'].payDate" data-biz-name="payDate_'+rowNum+'" id="payDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';
        html+='<td><input divName="payMoney" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="payMoney_'+rowNum+'" data-callback="bt.calMoney" lay-verify="required" data-biz-name="payMoney_'+rowNum+'" data-min="0" data-max="'+maxPayMoney+'" data-name="detailDtl['+rowNum+'].payMoney" ';
		if(!isMore){
			html+=' disabled value="'+moneyFun.format(invMoney)+'" ';
		}else{
			html+=' value="0" ';
		}
		html+='/></td>';
        html+='<td><input disabled divName="rePayMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="rePayMoney_'+rowNum+'" type="text" data-decimal="2" id="rePayMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].rePayMoney"';
		if(!isMore){
			html+=' value="0" ';
		}else{
			html+=' value="'+moneyFun.format(maxPayMoney)+'" ';
		}
		html+='/></td>';
        html+='<td align="center"><input disabled divName="accounted" class="layui-input   bt-biz-rateType" data-biz-name="accounted_'+rowNum+'"  type="text"   id="accounted_'+rowNum+'"   data-name="detailDtl['+rowNum+'].accounted"';
		if(!isMore){
			html+=' value="1" ';
		}
		html+='/></td>';
        html+='<td><input type="text" id="assName_'+rowNum+'" name="detailDtl['+rowNum+'].assName"  class="layui-input" ';
		if(!isMore){
			html+=' lay-verify="required" ';
		}
		html+='/></td>';
        html+='<td><input type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isPay" value="1" /></td>';
        html+='<td><input name="detailDtl['+rowNum+'].rmk" id="rmk"  class="layui-input" type="text"/></td>';
        html+='<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button></td>';
        html+='</tr>';
        $("#planDetailTB tbody").append(html);
        addFun();
		sumMoney();
    };


/**
 * 计算剩余金额，占用利率
 * lastEquityCodeIdId 上一次
 * equityCode_id    本次
 */
function calculation(lastEquityCodeId, equityCode_id){
	var id = equityCode_id.split("_")[1];
	//缴付金额
	var payMoney = moneyType.get("payMoney_"+id).val();
	if(surplusPayMoney<= 0){
        moneyType.get("payMoney_"+id).val(surplusPayMoney);
        moneyType.get("rePayMoney_"+id).val(0);
	    return;
    }
	//剩余本金
	if(Number(surplusPayMoney) <= Number(payMoney)){
		moneyType.get("payMoney_"+id).val(surplusPayMoney);
		payMoney = Number(surplusPayMoney);
    	moneyType.get("surplusMoney_"+id).val(0);
    	surplusPayMoney= 0;
	}else{
		surplusPayMoney= (surplusPayMoney-Number(payMoney)).toFixed(2);
		moneyType.get("rePayMoney_"+id).val(surplusPayMoney);
	}
	//moneyType.get("repaymentIst_"+id).val(repaymentIst);
}


/**
 * 根据本次缴付金额算出剩余缴付金额，本次占比（其他：循环每一行，之后调用calculation方法计算） */
function MoneyCalculation(){
	var trs = $("#planDetailTB").children("tbody").children("tr");
	var lastEquityCodeId="-1";//上一笔还款本金id
	// 遍历行数据
	trs.each(function(index, tr) {

		var equityCode_id="";//融资编码
		var tds = $(tr).find("td");
		tds.each(function(i, td) {
			var $input = $(td).find("input");
			if ($input.length) {
				if(!isNull($input.attr("id"))&&$input.attr("id").indexOf("equityCode")>=0){
					equityCode_id = $input.attr("id");
				}
			}
		}
		)
		var isPay=$(tr).find("td[name^='isPay']").find("input").val();
		if(isPay=="1"){
			calculation(lastEquityCodeId, equityCode_id);
			lastEquityCodeId = equityCode_id;
		}else{
			lastEquityCodeId = equityCode_id;
		}
	})
	sumMoney();
}


/**
 * 各金额的合计
 */
function sumMoney(){
	var invMoney=$("#invMoney").val();
	var payMoneySum=0;
	var $payMoneyInputs = $("input[divName^='payMoney']");
	$payMoneyInputs.each(function(i){
		payMoneySum = bt.money.add(payMoneySum, $(this).val());
	});
	moneyType.get("payMoneySum").val(payMoneySum);
	bt.rateType.get("accountedSum").val(moneyFun.div(parseFloat(payMoneySum), parseFloat(invMoney), 4))
	maxPayMoney=moneyFun.sub(parseFloat(invMoney),parseFloat(payMoneySum));
	surplusPayMoney=maxPayMoney;
	$payMoneyInputs.each(function(i){
		var $input = $(this);
		$input.attr("data-max", surplusPayMoney).removeClass("init");
		$input.siblings('input[type="hidden"]').remove();
	});
	moneyType.render('set');
}