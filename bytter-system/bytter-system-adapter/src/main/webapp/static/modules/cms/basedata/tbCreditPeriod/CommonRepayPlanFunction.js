var zqRate=0;//展期利率
var zqDate="";//展期日期
var remainDate;//已还的最后一笔日期加一天

/**
 * 导入还款计划(其他)
 */
function addImport(o,surplusMoney) {
	var currentRepayMoney=o.repaymentMoney+o.repaymentIst;
	var html = '<tr>';
	// 03 其他-含息 06 其他-不含息 07 自定义
	if (repayType != "03" && repayType != "06" && repayType != "07") {
		return;
	}
	//如果remainDate为空，证明没有已还数据，起始日期的计算为loanDate,反之就为remainDate(用来设置最小日期)
	var showRemainDate;
	if (isNull(remainDate)) {
		showRemainDate = dateFun.format(dateFun.addDay(loanDate, 1));
	} else {
		showRemainDate = remainDate;
	}
	//如果zqDate为空，证明没有已还数据，起始日期的计算为loanDate,反之就为zqDate(用来设置最大日期)
	var showZqDate;
	if (isNull(zqDate)) {
		showZqDate = repayDate;
	} else {
		showZqDate = zqDate;
	}
	html += '<td><input  name="repayMents[' + rowNum + '].prediceRepayDate" value="' + o.prediceRepayDate + '"  data-biz-name="prediceRepayDate_' + rowNum + '" id="prediceRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + showRemainDate + '" data-max="' + showZqDate + '" data-type="date" lay-verify="required" data-callback="clickEvent"/></td>';
	html += '<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" value="' + o.repaymentMoney + '" divName="repaymentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repaymentMoney_' + rowNum + '" id="repaymentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + loanMoney + '" value="0" data-name="repayMents[' + rowNum + '].repaymentMoney"/></td>';
	if (repayType === "07") {
		html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" value="'+ o.repaymentIst +'" data-callback="bt.calMoney" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" id="repaymentIst_' + rowNum + '" data-min="0"  data-name="repayMents[' + rowNum + '].repaymentIst" value="0"/></td>';
	} else {
		html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" value="'+ o.repaymentIst +'" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" id="repaymentIst_' + rowNum + '" data-min="0"  data-name="repayMents[' + rowNum + '].repaymentIst"/></td>';
	}
	html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" value="'+ currentRepayMoney +'" divName="currentRepayMoney" data-biz-name="currentRepayMoney_' + rowNum + '" type="text" data-decimal="2" id="currentRepayMoney_' + rowNum + '" data-min="0" data-name="repayMents[' + rowNum + '].currentRepayMoney"/></td>';
	html += '<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="surplusMoney" value="'+ surplusMoney +'" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" id="surplusMoney_' + rowNum + '" data-min="0" data-name="repayMents[' + rowNum + '].surplusMoney"/></td>';
	html += '<td name="isRepay">' + bt.lang("common.sc_yn.no") + '<input name="repayMents[' + rowNum + '].isRepay" value="0"  type="hidden"/></td>';
	html += '<td><input name="repayMents[' + rowNum + '].remark"  id="remark" class="layui-input" value="'+ o.remark +'" type="text"/></td>';
	html += '<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
	html += '</tr>';
	$("#repayPlanTB tbody").append(html);
	addFun();
};
    /**
     * 新增还款计划(其他)
     */
	function add(){
        var html='<tr>';
		// 03 其他-含息 06 其他-不含息 07 自定义
        if(repayType != "03" && repayType != "06" && repayType != "07") {
            return;
        }
        //如果remainDate为空，证明没有已还数据，起始日期的计算为loanDate,反之就为remainDate(用来设置最小日期)
        var showRemainDate;
        if(isNull(remainDate)){
            showRemainDate = dateFun.format(dateFun.addDay(loanDate, 1));
        }else{
            showRemainDate = remainDate;
        }
        //如果zqDate为空，证明没有已还数据，起始日期的计算为loanDate,反之就为zqDate(用来设置最大日期)
        var showZqDate;
        if(isNull(zqDate)){
            showZqDate = repayDate;
        }else{
            showZqDate = zqDate;
        }
        html+='<td><input  name="repayMents['+rowNum+'].prediceRepayDate" data-biz-name="prediceRepayDate_'+rowNum+'" id="prediceRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType" data-min="'+showRemainDate+'" data-max="'+showZqDate+'" data-type="date" lay-verify="required" data-callback="clickEvent"/></td>';
        html+='<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repaymentMoney_'+rowNum+'" id="repaymentMoney_'+rowNum+'" data-min="0" lay-verify="required" data-max="'+loanMoney+'" value="0" data-name="repayMents['+rowNum+'].repaymentMoney"/></td>';
		if (repayType === "07") {
        	html+='<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" data-callback="bt.calMoney" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst" value="0"/></td>';
		} else {
        	html+='<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/></td>';
		}
        html+='<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="currentRepayMoney" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney"/></td>';
        html+='<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="surplusMoney" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney"/></td>';
        html+='<td name="isRepay">'+bt.lang("common.sc_yn.no")+'<input name="repayMents['+rowNum+'].isRepay" value="0"  type="hidden"/></td>';
        html+='<td><input name="repayMents['+rowNum+'].remark" id="remark" class="layui-input" type="text"/></td>';
        html+='<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button></td>';
        html+='</tr>';
        $("#repayPlanTB tbody").append(html);
        addFun();
    };

	/**
	 * 新增还款计划(还本付息)
	 * @param payDate 日期数组
	 * @param payMoney 金钱数组
	 */
	function addOneOffPayment(payDate,payMoney){
		 var html='';
		 var obj;
		 var headDate;//计算开始日期（判断remainDate，空位loanDate，反之取remainDate）
		 if(isNull(remainDate)){
			headDate=loanDate;
		 }else{
			 headDate = dateFun.format(dateFun.addDay(remainDate, -1));
		 }
		 obj=calculationOneOffPayment(payDate,payMoney,headDate);
			  html+='<tr class="dataRow">';
			  html+='<td><input readonly="true" value="'+payDate+'" name="repayMents['+rowNum+'].prediceRepayDate" id="prediceRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType" data-min="'+loanDate+'" data-max="'+payDate+'" data-type="date" lay-verify="required" /></td>';
			  html+='<td><input  readonly="true" divName="repaymentMoney" value="'+payMoney+'" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repaymentMoney_'+rowNum+'" data-min="0" lay-verify="required" data-max="'+loanMoney+'" data-name="repayMents['+rowNum+'].repaymentMoney"/></td>';
			  html+='<td><input readonly="true" divName="repaymentIst" value="'+obj['repaymentIst']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/></td>';
			  html+='<td><input readonly="true" divName="currentRepayMoney" value="'+obj['currentRepayMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney"/></td>';
			/*  html+='<td></td>';
			  html+='<td></td>';*/
			  html+='<td><input readonly="true" value="'+obj['surplusMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" readonly="true" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney"/></td>';
			  html+='<td name="isRepay">'+bt.dict('yn_state',"0")+'<input readonly="true" name="repayMents['+rowNum+'].isRepay" value="0"  type="hidden"/></td>';
			  html+='<td><input  name="repayMents['+rowNum+'].remark" id="remark" class="layui-input" type="text"/></td>';
			  html+='</tr>';
			  rowNum++;
		  $("#repayPlanTB tbody").append(html);
		  addFun();
	};

	/**
	 * 新增还款计划（等额本金）
	 * @param payDate 日期数组
	 * @param payMoney 金钱数组
	 */
	function addEqualPrincipal(payDate,payMoney, payInterest){

        var html='';
        var obj;
        surplusMoney = surplusMoneyFixed;
        var lastDate = loanDate;//上一笔日期,默认赋值为贷款发放开始日期
		if(!isNull(remainDate)){
			lastDate = dateFun.format(dateFun.addDay(remainDate, -1));
		}
		//console.log("lastDate------"+lastDate);
		//console.log("remainDate-------"+remainDate);
        $.each(payDate, function(i,val){
            if(i == (payDate.length - 1) ) {//最后一次还款
              obj = calculationEqualPrincipal(lastDate,payDate[i],payMoney[i],true, payInterest);
            }else{
              obj = calculationEqualPrincipal(lastDate,payDate[i],payMoney[i],false, payInterest);
            }
            html+='<tr class="dataRow">';
            html+='<td><input disabled value="'+val+'" name="repayMents['+rowNum+'].prediceRepayDate" id="prediceRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType" data-min="'+loanDate+'" data-max="'+repayDate+'" data-type="date" lay-verify="required" /></td>';
            html+='<td><input  disabled divName="repaymentMoney" value="'+obj['repaymentMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repaymentMoney_'+rowNum+'" data-min="0" lay-verify="required" data-max="'+loanMoney+'" data-name="repayMents['+rowNum+'].repaymentMoney"/></td>';
            html+='<td><input disabled divName="repaymentIst" value="'+obj['repaymentIst']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/></td>';
            html+='<td><input disabled divName="currentRepayMoney" value="'+obj['currentRepayMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney"/></td>';
            /*  html+='<td></td>';
            html+='<td></td>';*/
            html+='<td><input disabled value="'+obj['surplusMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" readonly="true" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney"/></td>';
            html+='<td name="isRepay">'+bt.dict('yn_state',"0")+'<input readonly="true" name="repayMents['+rowNum+'].isRepay" value="0"  type="hidden"/></td>';
            html+='<td><input  name="repayMents['+rowNum+'].remark" id="remark" class="layui-input" type="text"/></td>';
            html+='</tr>';
            rowNum++;
            lastDate=payDate[i];
        });
        $("#repayPlanTB tbody").append(html);
        addFun();
	};


	/**
	 * 新增还款计划（等额本息）
	 * @param payDate
	 */
	function addEqualPrincipalInterest(payDate){
		var qs=0;
		var f1=0;//辅助列1
		var f2=1;//辅助列2
		var sumf2=0;//辅助列2的合计
		var html='';
		var obj;
		var currentRepayMoney=0;//每期的还款金额
		surplusMoney = surplusMoneyFixed;
		var lastDate;
		var lastDateFixed;
		//当remainDate为空时，取还款开始日期，反之，取上一笔已还数据日期
		if(!isNull(remainDate)){
			lastDateFixed = dateFun.format(dateFun.addDay(remainDate, -1));
		}else{
			lastDateFixed = loanDate;//上一笔日期,默认赋值为开始日期
		}
		lastDate=lastDateFixed;
		 $.each(payDate, function(i,val){
			 //console.log('------------------------------------');
			 var dateStr1=dateFun.format(payDate[i], 'yyyy-MM-dd');
			 //console.log('payDate一:'+dateStr1);
			 //console.log('repayDate一:'+repayDate);
			 if(dateFun.compare(dateFun.get(payDate[i]),dateFun.get(repayDate))<=0){//当前日期在还款日期之前，采用loanRate
				 var days=getDaysDate(lastDate,payDate[i]);
					qs=Number(1+days*loanRate/rateBase).toFixed(10);//对应1+期间利息
					//console.log('days一:'+days);
				}else{
					if(dateFun.compare(dateFun.get(lastDate),dateFun.get(repayDate))>=0){//利率取zqRate
						var days=getDaysDate(lastDate,payDate[i]);
						qs=Number(1+days*zqRate/rateBase).toFixed(10);//对应1+期间利息
						//console.log('days二:'+days);
					}else{//分段处理
						var daysone=getDaysDate(lastDate,repayDate);
						var daystwo=getDaysDate(repayDate,payDate[i]);
						qs=Number(1+daysone*loanRate/rateBase+daystwo*zqRate/rateBase).toFixed(10);
						//console.log('daysone:'+daysone);
						//console.log('daystwo:'+daystwo);
					}

				}
			 	f1=Number(1/qs).toFixed(10);//辅助列2
				f2=bt.moneyType.getFun().mul(Number(f1), Number(f2), 9);//辅助列4
				sumf2=Number(Number(sumf2)+Number(f2)).toFixed(10);//辅助列4的合计
				lastDate=payDate[i];
	  });
		currentRepayMoney=(surplusMoneyFixed/sumf2).toFixed(2);
		//因为前面计算currentRepayMoney使得lastDate改变，所以需要重新赋值
		lastDate=lastDateFixed;
		$.each(payDate, function(i,val){
				  if(i==(payDate.length-1)){
					  obj=calculationEqualPrincipalAndInterest(lastDate,payDate[i],currentRepayMoney,true);
				  }else{
					  obj=calculationEqualPrincipalAndInterest(lastDate,payDate[i],currentRepayMoney,false);
				  }
			  html+='<tr class="dataRow">';
			  html+='<td><input disabled value="'+val+'" name="repayMents['+rowNum+'].prediceRepayDate" id="prediceRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType" data-min="'+loanDate+'" data-max="'+repayDate+'" data-type="date" lay-verify="required" /></td>';
			  html+='<td><input  disabled divName="repaymentMoney" value="'+obj['repaymentMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repaymentMoney_'+rowNum+'" data-min="0" lay-verify="required" data-max="'+loanMoney+'" data-name="repayMents['+rowNum+'].repaymentMoney"/></td>';
			  html+='<td><input disabled divName="repaymentIst" value="'+obj['repaymentIst']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/></td>';
			  html+='<td><input disabled divName="currentRepayMoney" value="'+obj['currentRepayMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney"/></td>';
			/*  html+='<td></td>';
			  html+='<td></td>';*/
			  html+='<td><input disabled value="'+obj['surplusMoney']+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" readonly="true" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney"/></td>';
			  html+='<td name="isRepay">'+bt.dict('yn_state',"0")+'<input readonly="true" name="repayMents['+rowNum+'].isRepay" value="0"  type="hidden"/></td>';
			  html+='<td><input  name="repayMents['+rowNum+'].remark" id="remark" class="layui-input" type="text"/></td>';
			  html+='</tr>';
			  rowNum++;
			  lastDate=payDate[i];
		  });
		  $("#repayPlanTB tbody").append(html);
		  addFun();
	};

/**
 * 计算剩余本金，利息，本期还款金额(其他)
 * lastPrediceRepayDateId 上一次还款日
 * prediceRepayDate_id    本次还款日
 */
function calculation(lastPrediceRepayDateId, prediceRepayDate_id){
	var id = prediceRepayDate_id.split("_")[1];
	//var repaymentMoney=$("#repaymentMoney_"+id).val();
	var repaymentMoney = moneyType.get("repaymentMoney_"+id).val();
	//获取现在利息值确定要不要主动计算
	var repaymentIstbefor= moneyType.get("repaymentIst_"+id).val();
	var prediceRepayDate = $("#prediceRepayDate_"+id).val();//预计还款日
	//上一笔日期
	var lastDate;
	if(lastPrediceRepayDateId == "-1"){
		lastDate = loanDate;
	}else{
		lastDate=$("#"+lastPrediceRepayDateId).val();
	}

	if(dateFun.compare(dateFun.get(lastDate),dateFun.get(prediceRepayDate))>0){
		parent.layer.alert(bt.lang("common.message.prediceRepayDate"));//预计还款日期不能小于上一笔日期
		var getCurrentDate = dateType.get("prediceRepayDate_"+id).val("");
	}
	if(surplusMoney <= 0){
        moneyType.get("repaymentMoney_"+id).val(surplusMoney);
        moneyType.get("repaymentIst_"+id).val(0);
        moneyType.get("surplusMoney_"+id).val(0);
        moneyType.get("currentRepayMoney_"+id).val(0);
	    return;
    }

	//如果预计还款日或者还款本金有一项为空则跳过计算
	if(isNull(repaymentMoney) || isNull(prediceRepayDate)) {
		return;
	}
	var surplusMoneyOld = Number(surplusMoney);
    var sign = false;//最后一次还款
    var payInterest = true;//还本时还息
    if("06" == repayType) {
        payInterest = false;//还本时不付息
    }

	//剩余本金
	if(Number(surplusMoney) <= Number(repaymentMoney)){
		moneyType.get("repaymentMoney_"+id).val(surplusMoney);
    	repaymentMoney = Number(surplusMoney);
    	moneyType.get("surplusMoney_"+id).val(0);
    	surplusMoney = 0;
        sign = true;//最后一次还款
	}else{
		surplusMoney = (surplusMoney-Number(repaymentMoney)).toFixed(2);
		moneyType.get("surplusMoney_"+id).val(surplusMoney);
	}
	var repaymentIst=null;
    if(divName=="repaymentMoney"+id){
		repaymentIst= commonCalPayIst(lastDate, prediceRepayDate, repayDate, repaymentMoney, sign, payInterest);
	}else{
		repaymentIst=repaymentIstbefor;
	}
	moneyType.get("repaymentIst_"+id).val(repaymentIst);
	//计算本期还款金额
	var currentRepayMoney=(Number(repaymentMoney)+Number(repaymentIst)).toFixed(2);
	moneyType.get("currentRepayMoney_"+id).val(currentRepayMoney);
}

/**
 * 计算剩余本金，利息，本期还款金额（还本付息）
 */
function calculationOneOffPayment(prediceRepayDate,repaymentMoney,headDate){

	if(isNull(zqDate)){//非展期
		//计算利息
		var days=getDaysDate(headDate,prediceRepayDate);
		var repaymentIst=(surplusMoneyFixed*days*loanRate/Number(rateBase)).toFixed(2);
		//计算本期还款金额
		var currentRepayMoney=(Number(repaymentMoney)+Number(repaymentIst)).toFixed(2);
		//剩余本金
	    surplusMoney=(Number(surplusMoneyFixed)-Number(repaymentMoney)).toFixed(2);

	    var obj={repaymentIst:repaymentIst,currentRepayMoney:currentRepayMoney,surplusMoney:surplusMoney};
	    return obj;
	}else{//展期
		var repaymentIst;
		//分段计算利息
		if(dateFun.compare(dateFun.get(headDate),dateFun.get(repayDate))>=0){
			var days=getDaysDate(headDate,zqDate);
			repaymentIst=(surplusMoneyFixed*days*zqRate/Number(rateBase)).toFixed(2);
		}else{
			var daysone=getDaysDate(headDate,repayDate);
			var daysTwo=getDaysDate(repayDate,zqDate);
			repaymentIst=bt.moneyType.getFun().add((surplusMoneyFixed*(loanRate/Number(rateBase))*daysone),
					(surplusMoneyFixed*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
		}
		//计算本期还款金额
		var currentRepayMoney=(Number(repaymentMoney)+Number(repaymentIst)).toFixed(2);
		//剩余本金
	    surplusMoney=(Number(surplusMoneyFixed)-Number(repaymentMoney)).toFixed(2);
	    var obj={repaymentIst:repaymentIst,currentRepayMoney:currentRepayMoney,surplusMoney:surplusMoney};
	    return obj;
	}
}

/**
 * 计算剩余本金，利息，本期还款金额(等额本金)
 * repayDate: 到期日期
 * lastDate: 上次还款日
 * prediceRepayDate: 本次还款日
 * sign: 最后一次还款
 * payInterest: 是否还息
 */
function calculationEqualPrincipal(lastDate, prediceRepayDate, repaymentMoney, sign, payInterest){
    var repaymentIst;//利息
    var currentRepayMoney;//本金

	if(dateFun.compare(dateFun.get(prediceRepayDate), dateFun.get(repayDate)) <= 0) {
	    if(sign){//最后一次还款
	        if(payInterest){//还本时归还利息
                var days = getDaysDate(lastDate, prediceRepayDate);
                repaymentIst = (surplusMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                var days = getDaysDate(loanDate, prediceRepayDate);//本次日期-发放日期
                repaymentIst = (loanMoney * loanRate / rateBase * days).toFixed(2);//发放金额计算
            }
        } else {
            if(payInterest){//还本时归还利息
                var days = getDaysDate(lastDate, prediceRepayDate);
                repaymentIst = (surplusMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息
                repaymentIst = (0).toFixed(2);//发放金额计算
            }
        }
	} else {
        if(dateFun.compare(dateFun.get(lastDate), dateFun.get(repayDate)) >= 0) {
            if(sign){//最后一次还款
                if(payInterest){//还本时归还利息
                    var days = getDaysDate(lastDate, prediceRepayDate);
                    repaymentIst = (surplusMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, prediceRepayDate);
                    repaymentIst = bt.moneyType.getFun().add((loanMoney * (loanRate/Number(rateBase))*daysone),
                        (loanMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                }
            } else {
                if(payInterest){//还本时归还利息
                    var days = getDaysDate(lastDate, prediceRepayDate);
                    repaymentIst = (surplusMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    repaymentIst = (0).toFixed(2);//发放金额计算
                }
            }
        } else {
            if(sign){//最后一次还款
                if(payInterest){//还本时归还利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, prediceRepayDate);
                    repaymentIst = bt.moneyType.getFun().add((surplusMoney * (loanRate/Number(rateBase))*daysone),
                        (surplusMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, prediceRepayDate);
                    repaymentIst = bt.moneyType.getFun().add((loanMoney * (loanRate/Number(rateBase))*daysone),
                        (loanMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                }
            } else {
                if(payInterest){//还本时归还利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, prediceRepayDate);
                    repaymentIst = bt.moneyType.getFun().add((surplusMoney * (loanRate/Number(rateBase))*daysone),
                        (surplusMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    repaymentIst = (0).toFixed(2);//发放金额计算
                }
            }
        }
	}
	if(sign){
    	repaymentMoney = Number(surplusMoney);
    	//计算本期还款金额
    	currentRepayMoney = (Number(repaymentMoney) + Number(repaymentIst)).toFixed(2);
    	surplusMoney = 0;
    }else{
    	//计算本期还款金额
    	currentRepayMoney = (Number(repaymentMoney) + Number(repaymentIst)).toFixed(2);
    	//剩余本金
        surplusMoney = (Number(surplusMoney) - Number(repaymentMoney)).toFixed(2);
    }
    var obj = {
        repaymentIst : repaymentIst,
        currentRepayMoney : currentRepayMoney,
        surplusMoney : surplusMoney,
        repaymentMoney : repaymentMoney
    };
    return obj;
}

/**
 * 公共利息测算方法: 如果还本时不还息，则中间的还款都不计算利息，只有最后一次还款才计算所有的利息
 * @param lastReapyDate     上一次还款日
 * @param currentReapyDate  本次还款日
 * @param endReapyDate      到期日
 * @param curRepayMoney     当前还款金额
 * @param sign              是否最后一次还款
 * @param payInterest       还款时是否还息
 * @returns {number}        返回还息金额
 */
function commonCalPayIst(lastReapyDate, currentReapyDate, endReapyDate, curRepayMoney, sign, payInterest){
    var repayInterest = 0.00;
    if(dateFun.compare(dateFun.get(currentReapyDate), dateFun.get(endReapyDate)) <= 0) {
        if(sign){//最后一次还款
            if(payInterest){//还本时归还利息
                var days = getDaysDate(lastReapyDate, currentReapyDate);
                repayInterest = (curRepayMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                var days = getDaysDate(loanDate, currentReapyDate);//本次日期-发放日期
                repayInterest = (loanMoney * loanRate / rateBase * days).toFixed(2);//发放金额计算
            }
        } else {
            if(payInterest){//还本时归还利息
                var days = getDaysDate(lastReapyDate, currentReapyDate);
                repayInterest = (curRepayMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息
                repayInterest = (0).toFixed(2);//发放金额计算
            }
        }
    } else {
        if(dateFun.compare(dateFun.get(lastReapyDate), dateFun.get(endReapyDate)) >= 0) {
            if(sign){//最后一次还款
                if(payInterest){//还本时归还利息
                    var days = getDaysDate(lastReapyDate, currentReapyDate);
                    repayInterest = (curRepayMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((loanMoney * (loanRate/Number(rateBase))*daysone),
                        (loanMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                }
            } else {
                if(payInterest){//还本时归还利息
                    var days = getDaysDate(lastReapyDate, currentReapyDate);
                    repayInterest = (curRepayMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    repayInterest = (0).toFixed(2);//发放金额计算
                }
            }
        } else {
            if(sign){//最后一次还款
                if(payInterest){//还本时归还利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((curRepayMoney * (loanRate/Number(rateBase))*daysone),
                        (curRepayMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((loanMoney * (loanRate/Number(rateBase))*daysone),
                        (loanMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                }
            } else {
                if(payInterest){//还本时归还利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((curRepayMoney * (loanRate/Number(rateBase))*daysone),
                        (curRepayMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    repayInterest = (0).toFixed(2);//发放金额计算
                }
            }
        }
    }
    return repayInterest;
}

/**
 * 计算剩余本金，利息，本期还款本金(等额本息)
 */
function calculationEqualPrincipalAndInterest(lastDate,prediceRepayDate,currentRepayMoney,sign){
	var repaymentIst;
	var repaymentMoney;
	if(dateFun.compare(dateFun.get(prediceRepayDate),dateFun.get(repayDate))<=0){
		var days=getDaysDate(lastDate,prediceRepayDate);
		repaymentIst=(surplusMoney*loanRate/rateBase*days).toFixed(2);
	}else{
		if(dateFun.compare(dateFun.get(lastDate),dateFun.get(repayDate))>=0){
			var days=getDaysDate(lastDate,prediceRepayDate);
			repaymentIst=(surplusMoney*zqRate/rateBase*days).toFixed(2);
		}else{
			var daysone=getDaysDate(lastDate,repayDate);
			var daysTwo=getDaysDate(repayDate,prediceRepayDate);
			repaymentIst=bt.moneyType.getFun().add((surplusMoney*(loanRate/Number(rateBase))*daysone),
					(surplusMoney*(zqRate/Number(rateBase))*daysTwo)).toFixed(2);
		}
	}
	 if(sign){
		 	repaymentMoney=Number(surplusMoney);
	    	//计算本期还款金额
	    	currentRepayMoney=(Number(repaymentMoney)+Number(repaymentIst)).toFixed(2);
	    	surplusMoney=0;
	    }else{
	    	//计算本金
	    	repaymentMoney=(Number(currentRepayMoney)-Number(repaymentIst)).toFixed(2);
	    	//剩余本金
	        surplusMoney=(Number(surplusMoney)-Number(repaymentMoney)).toFixed(2);
	    }
    var obj={repaymentIst:repaymentIst,currentRepayMoney:currentRepayMoney,surplusMoney:surplusMoney,repaymentMoney:repaymentMoney};
    return obj;
}

/**
 * 计算剩余本金，利息，本期还款金额(等额本金)
 */
//function calculationEqualPrincipal(prediceRepayDate,repaymentMoney,sign){
////	console.log("************************************");
////	console.log("prediceRepayDate--"+prediceRepayDate);
////	console.log("repaymentMoney--"+repaymentMoney);
////	console.log("surplusMoney--"+surplusMoney);
////	console.log("loanRate--"+loanRate);
//	var repaymentIst;
//	var currentRepayMoney;
//	//等额本金对于最后一笔数据的处理
//	 if(sign){
//	    	repaymentMoney=Number(surplusMoney);
//	    	//计算利息
//	    	repaymentIst=(surplusMoney*loanRate/Number(timeParms)).toFixed(2);
//	    	//计算本期还款金额
//	    	currentRepayMoney=(Number(repaymentMoney)+Number(repaymentIst)).toFixed(2);
//	    	surplusMoney=0;
//	    }else{
//	    	//计算利息
//	    	repaymentIst=(surplusMoney*loanRate/Number(timeParms)).toFixed(2);
//	    	//计算本期还款金额
//	    	currentRepayMoney=(Number(repaymentMoney)+Number(repaymentIst)).toFixed(2);
//	    	//剩余本金
//	        surplusMoney=(Number(surplusMoney)-Number(repaymentMoney)).toFixed(2);
//	    }
//
//
//    var obj={repaymentIst:repaymentIst,currentRepayMoney:currentRepayMoney,surplusMoney:surplusMoney,repaymentMoney:repaymentMoney};
//    return obj;
//}

/**
 * 根据预计还款日和预计还款本金，算出利息，本次还款本金，剩余本金（其他：循环每一行，之后调用calculation方法计算）
 */
function MoneyCalculation(){
	surplusMoney=Number(surplusMoneyFixed);
	var trs = $("#repayPlanTB").children("tbody").children("tr");
	var lastPrediceRepayDateId="-1";//上一笔还款本金id
	// 遍历行数据
	trs.each(function(index, tr) {

		var prediceRepayDate_id="";//预计还款日期
		var tds = $(tr).find("td");
		tds.each(function(i, td) {
			var $input = $(td).find("input");
			if ($input.length) {
				if(!isNull($input.attr("id"))&&$input.attr("id").indexOf("prediceRepayDate")>=0){
					prediceRepayDate_id = $input.attr("id");
				}
//				if(!isNull($input.attr("data-id"))&&$input.attr("data-id").indexOf("repaymentMoney")>=0){
//					repaymentMoney_id=$input.attr("data-id");
//				}

			}
		}
		)
		var isRepay=$(tr).find("td[name^='isRepay']").find("input").val();
		if(isRepay!="0"){
			lastPrediceRepayDateId = prediceRepayDate_id;
		}else{
			calculation(lastPrediceRepayDateId, prediceRepayDate_id);
			lastPrediceRepayDateId = prediceRepayDate_id;
		}
	})
	sumMoney();
}
/**
 * 根据还款开始日期和还款日期、还款周期计算出日期数组
 */
function getDates(){
	var beginDate=dateFun.get($('#repayBeginDate').val());//开始日期
	var endDate;
	if(isNull(zqDate)){
		endDate=dateFun.get($('#repayDate').val());//还款日期
	}else{
		endDate=zqDate;
	}
	var timeSpan;
	switch(repayCycel)
	{
	case '0':
		timeSpan=1;timeParms=12;break;
	case '1':
		timeSpan=3;timeParms=4;break;
	case '2':
		timeSpan=6;timeParms=2;break;
	default: timeSpan="1";
	}
	var dates = splitByMonth(beginDate, endDate, timeSpan, "0");
	return dates;
}
/**
 * 根据还款开始日期和还款日期、还款周期计算出日期数组(还款计划修改中)
 */
//function getDatesUpdate(){
//	var beginDate=new Date($('#repayBeginDate').val());//开始日期
//	var repayDate=new Date($("#repayDate").val());//还款日期
//	var timeSpan;
//	switch(repayCycel)
//	{
//	case '0':
//		timeSpan=1;timeParms=12;break;
//	case '1':
//		timeSpan=3;timeParms=4;break;
//	case '2':
//		timeSpan=6;timeParms=2;break;
//	default: timeSpan="1";
//	}
//	var dates=splitByMonth(beginDate,repayDate,timeSpan,"1");
//	return dates;
//}

/**
 * 计算两个日期间相隔的（月，季，半年）
 * @param start 开始日期
 * @param end 结束日期
 * @param size 参数
 * @param type 1：还款计划修改
 * @returns
 */
function splitByMonth(start, end, size,type){
	start = dateFun.get(start);
	end = dateFun.get(end);

	if(!start || !end || !size || size < 1){
		return null;
	};

	var ary = [];
    ary.push(start);
	// if(type=="1"){
	// 	ary.push(start);
	// }
	var temp = start;
	var day = start.getDate();
	if(addMonth(temp, size, day).getTime() > end.getTime()){
        ary.push(end);
    }else{
        while(addMonth(temp, size, day).getTime() < end.getTime()){
            var newDate = addMonth(temp, size, day);
            ary.push(newDate);
            temp = newDate;
        };
        ary.push(end);
    };

	return ary;
}

function addMonth(date, size, oriDay){
    date = dateFun.get(date);
    if(!date){
        return null;
    };
    size = size - 0; //转数字
    var y = date.getFullYear();
    var m = date.getMonth();
    var nextY = y;
    var nextM = m;
    //如果当前月+要加上的月>11 这里之所以用11是因为 js的月份从0开始
    if ((m + size)> 11) {
        nextY = y + 1;
        nextM = parseInt(m + size) - 12;
    } else {
        nextM = date.getMonth() + size
    }
    var daysInNextMonth = daysInMonth(nextY, nextM);
    var day = oriDay;
    if (day > daysInNextMonth) {
        day = daysInNextMonth;
    }
    var newdate = new Date(nextY, nextM, day);
    return newdate;
}

function daysInMonth(year, month) {
    if (month == 1) {
        if (year % 4 == 0 && year % 100 != 0){
            return 29;
        } else {
            return 28;
        }
    } else if (month == 3 || month == 5 || month == 7 || month == 10) {
        return 30;
    } else{
        return 31;
    }
};


/**
 * 获取两个日期之间的天数(天数)
 * @param strDateStart(Date)
 * @param strDateEnd(Date)
 * @returns
 */
function getDaysDate(strDateStart,strDateEnd){
	var dateStr1=dateFun.format(strDateStart, 'yyyy-MM-dd');
	var dateStr2=dateFun.format(strDateEnd, 'yyyy-MM-dd');
//	strDateStart=dateFun.get(dateStr1);
//	strDateEnd=dateFun.get(dateStr2);
// 	if(repayCycel === "0"){
//         return days = 30;
// 	}else if(repayCycel === "1" ){
//         return days = 90;
// 	}else if(repayCycel === "2"){
//         return days = 180;
// 	}else{
//         return days=dateFun.getBetweenDay(dateStr1,dateStr2)
// 	}
    var date1 = bt.date.get(strDateStart);
    var date2 = bt.date.get(strDateEnd);
    var btt = date2 - date1;
    return btt/(1000*60*60*24);
}



