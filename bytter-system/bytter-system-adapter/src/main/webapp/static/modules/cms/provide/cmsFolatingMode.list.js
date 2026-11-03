var select;
var form;
var dicts;
bt.use(['form','jquery','corp','dateType','moneyType','rateType','datatable','dicts','selectType'], function(){
	$ = bt.jquery,form = bt.form();
	dicts=bt.dicts.init();
   rateTypes=bt.rateType.init();
	dateTypes=bt.dateType.init();
	moneyTypes=bt.moneyType.init();//金额
	moneyFun=bt.moneyType.getFun();
	selectTypes = bt.selectType.init();//下拉框
	 //生成列表
    billTable= bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/provide/cmsFloatingMode/datatable?id="+$("#id").val()
        },
        columns: [
                   {data:"floatingModeName",bSortable:true},
                   {data:"enterDate",bSortable:true},
        ],

    });
    //监听提交
    form.on('submit', function(data){
        var action = "/cms/provide/cmsFloatingMode/save";
        bt.loading.show();
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide();
        	parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
            		location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
            	} else {
            		if(data.field.id == ""){
            			bt.navtab.refreshCurrent();
            		}else{
            			bt.navtab.refreshParent();
            			bt.navtab.closeCurrent();
            		}
            	}
            }
        },'JSON');
        return false;
    });

});

/**
 * 当浮点方式为即时浮动时，设置floatingModeName为浮点方式
 */
function even(){
var floatWay = dicts.get("floatingMode").val();
	var floatingMode = $("select[name='floatingMode']").find("option:selected").text();
if(floatWay=="02") {
	$("#floatingModeName").val(floatingMode);
}else if(floatWay=="01"){
	var fixedRate=moneyFun.toNumber($("input[data-name='fixedRate']").val());
	$("#floatingModeName").val(floatingMode+"--"+fixedRate+"%");
}
}
/**浮动方式选择月、季、半年、年生产对应的详情选择 用来计算利率生效日期*/
function setNextDetail(){
	var floatWay = dicts.get("floatingMode").val();
	var postfix = "";
	var htl = '';
	switch(floatWay){
	case "01":show(true,false,"");even();break;
	case "02":show(false,false,"");even();break;
	case "03":htl=show(false,true,"Month");break;
	case "04":htl=show(false,true,"Season");break;
	case "05":htl=show(false,true,"HY");break;
	case "06":htl=show(false,true,"Year");break;
	}
	$("#nextDetail").html("");
	$("#nextDetail").html(htl);

	dicts.render();
	//disabledLoanRate(flag);//暂时不需要控制固定利率不能选择浮动方式
}
/**
 * 当浮点方式变更时，一些属性也相应的更改
 * @param a
 * @param b
 * @param postfix
 * @returns {String}
 */
function show(a,b,postfix){
	var htl="";
	/*if(a){
		$("input[data-name='fixedRate']").attr("lay-verify","required");
		$("#fixed").show();
		$("#point").hide();
	}else{
		$("input[data-name='fixedRate']").val("");
		$("input[data-name='fixedRate']").removeAttr("lay-verify");
		$("#fixed").hide();
		$("#point").show();
	}*/
	if(a){
		showFixed();
	}else{
		hideFixed();
	}
	if(b){
		htl = '<div class="bt-biz-dict" data-required="true" data-name="floatModeNext" data-type="select" data-biz-name="floatModeNext'+postfix+'" data-callback="test"  data-group="Cms.FloatWay'+ postfix +'" ></div>';
		$("select[name='floatModeNext']").attr("lay-verify","required");
	}else{
		htl="";
	}
	return htl;
}

function showFixed() {
	$("#fixedRateLabel").show();
	$("#fixedRateInput").show();
	$("#rateQuotedPriceDate").show();
}


function hideFixed() {
	$("#fixedRateLabel").hide();
	$("#fixedRateInput").hide();
	$("#rateQuotedPriceDate").hide();
}

/**
 * 当离开固定利率文本框之后，设置floatingModeName为浮点方式+固定利率的值
 */
function setFloatingMode(){
	var floatingMode=$("select[name='floatingMode']").find("option:selected").text();
	var fixedRate=moneyFun.toNumber($("input[data-name='fixedRate']").val());
	$("#floatingModeName").val(floatingMode+"--"+fixedRate+"%");
}
/**
 * 当浮点方式二级菜单变更时，设置floatingModeName为浮点方式+二级菜单方式
 * @param data
 */
function test(data){
	var floatModeNext=$("select[name='floatModeNext']").find("option:selected").text();
	var floatingMode=$("select[name='floatingMode']").find("option:selected").text();
	$("#floatingModeName").val(floatingMode+"--"+floatModeNext);
}

doClose = function(){
	if(bt.navtab){
		bt.navtab.closeCurrent();
	}
}

/**基准利率加载完成后 加载利率周期*/
function loanRepayCycle(){

	/*var rateType = selectTypes.get("referenceRateType").getData();
	if(!bt.empty(rateType)){
		var code = rateType.obj.code;
		if(code=="lpr"){
			$("#numUnit").removeClass("input-percent");
			$("#numUnit").addClass("input-bp");
			dicts.get("pointsProportionSelect").val("01");
			$("#inputMark").text("BP");
		}else {
			$("#numUnit").removeClass("input-bp");
			$("#numUnit").addClass("input-percent");
			dicts.get("pointsProportionSelect").val("02");
			$("#inputMark").text("%");
		}
	}*/
	var referenceRateType = selectTypes.get("referenceRateType").val();//基准利率类型
	var curId = $("#curId").val();//为空默认录入日期
	var sendDate = $("#sendDate").val();//为空默认录入日期
	selectTypes.get("repayCycle").attr(
		{
			"data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + sendDate + "&curId=" + curId + "&baseId=" + referenceRateType
		}
	);

}

function updatePointsAndQuotedPriceDate(){
	var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
	var ratedownlimit=0;
	var rateuplimit=0;

	if(data && data.ratedownlimit){
		ratedownlimit=data.ratedownlimit
	}
	if(data && data.rateuplimit){
		rateuplimit=data.rateuplimit
	}
	var pointsProportionTextObj = bt.moneyType.init().get("pointsText");
	//设置text的输入范围
	pointsProportionTextObj.attr("data-min",ratedownlimit);
	pointsProportionTextObj.attr("data-max",rateuplimit);

	// 更新利率类型的生效日期 start
	var repayCycleData = selectTypes.get("repayCycle").getData();
	var timeOfTakeEffect = '';

	if(repayCycleData) {
		if(repayCycleData.obj && repayCycleData.obj.timeOfTakeEffect){
			timeOfTakeEffect=repayCycleData.obj.timeOfTakeEffect;
		}
	}
	var floatWay = dicts.get("floatingMode").val();

	if(floatWay && floatWay != "01" ){//非固定利率 报价日期的默认值为 利率调整生效日期匹配之前 最近的一个有效期
		var rateEntriesEffectDate=dateTypes.get("enterDate").val();
		if(rateEntriesEffectDate && rateEntriesEffectDate!='' && data.alleffectdatearray && data.alleffectdatearray.length >0){
			var i=0
			for(;i<data.alleffectdatearray.length;i++){
				if(rateEntriesEffectDate == data.alleffectdatearray[i]){
					timeOfTakeEffect=data.alleffectdatearray[i];
					break
				}else if(rateEntriesEffectDate < data.alleffectdatearray[i] && i>0){
					timeOfTakeEffect=data.alleffectdatearray[i-1];
					break;
				}
			}
			if(i == data.alleffectdatearray.length){
				timeOfTakeEffect=data.alleffectdatearray[i-1];
			}
		}
	}
	// var rateData = $("#rateQuotedPriceDate");
	// rateData.val(timeOfTakeEffect);
	dateTypes.get("rateQuotedPriceDate").val(timeOfTakeEffect);
	// 更新利率类型的生效日期 end
	getRate();
	even();//填入名称 floatingModeName
}

//获取利率
function getRate(){
	var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
	if(!data || !data.id){//利率周期不存在，则利率为0
		rateTypes.get("fixedRate").val(0);
		return;
	}
	var choseDate=dateTypes.get("rateQuotedPriceDate").val();
	if(!data.effectdate2loanratemaintence || !data.effectdate2loanratemaintence[choseDate]){
		parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
		return ;
	}
	var baseRate=data.effectdate2loanratemaintence[choseDate].rate;
	// if(bt.empty(data.rate)){
	// 	parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
	// 	return ;
	// }

	var pointsProportionSelect = dicts.get("pointsProportionSelect").val();//浮动比例或点数类型
	var pointsProportionText = moneyTypes.get("pointsText").val();//浮动点数或比例值
	var floatWay = dicts.get("floatingMode").val();
	if(pointsProportionText != ""){//点数比例不为空
		if(pointsProportionSelect=="01"){//点数
			//rateTypes.get("plRate").val(data.rate+(pointsProportionText/100));//使用百分比  不使用万分比
			//基础利率+浮动的点数*0.01%
			rateTypes.get("fixedRate").val(moneyFun.format(baseRate + pointsProportionText * (0.01 / 100), {decimal: 4}));
			//setInterestFee();//自动计算综合利率

		}else{
			//比例 基准利率+浮动的比例*基准利率
			//rateTypes.get("plRate").val((data.rate)*(1+(pointsProportionText/100)));
			rateTypes.get("fixedRate").val(moneyFun.format(baseRate + (pointsProportionText / 100) * baseRate, {decimal: 4}));
			//setInterestFee();//自动计算综合利率

		}
		if(floatWay=="01"){
			setFloatingMode();
		}
	}
}







