var rowNum = 0;//用来记录行
var moneyType="";
var dateType="";
var dateFun;
var surplusMoney="";//用于计算用的剩余金额
var surplusMoneyFixed="";//固定的剩余金额（即除去已还金额，剩余的金额）
var loanDate="";//发放日期
var repayDate="";//还款日期
var loanMoney="";//发放金额
var loanRate="";//利率
var isRepay="0";//初始化时，若为1：已还，则设置不可编辑
var repayType= $("#repayType").val();//还款方式为其它
var rateBase=360;//利息测算基数
var repayCycel="";
var columns=[
    {data:"prediceRepayDate",
        render:function(data, row){
            rowNum=row;
            isRepay=data.isRepay=="1"?"disabled":"";
            if(isNull(data.prediceRepayDate)){
                return '<input '+isRepay+' name="repayMents['+rowNum+'].prediceRepayDate" data-biz-name = "prediceRepayDate_'+rowNum+'" id="prediceRepayDate_'+rowNum+'" type="text"  class="layui-input bt-biz-dateType" data-min="'+loanDate+'" data-max="'+repayDate+'" data-type="date" data-callback="clickEvent"/>';
            }else{
                return '<input '+isRepay+' name="repayMents['+rowNum+'].prediceRepayDate" data-biz-name = "prediceRepayDate_'+rowNum+'" id="prediceRepayDate_'+rowNum+'" type="text" value="'+data.prediceRepayDate+'" class="layui-input bt-biz-dateType" data-min="'+loanDate+'" data-max="'+repayDate+'" data-type="date" data-callback="clickEvent"/>';
            }
        }
    },
    {data:"repaymentMoney", render:function(data, row){
            return '<input '+isRepay+' divName="repaymentMoney" class="layui-input bt-input-inline  bt-biz-moneyType" value="'+(data.repaymentMoney==null?"":moneyFun.format(data.repaymentMoney))+'"  type="text" data-decimal="2" id="repaymentMoney_'+rowNum+'" data-callback="bt.calMoney" data-biz-name="repaymentMoney_'+rowNum+'" data-min="0" data-max="'+loanMoney+'" data-name="repayMents['+rowNum+'].repaymentMoney"/>';
        }
    },
    {data:"repaymentIst", render:function(data, row){
            if( repayType == "07"){
                return '<input  divName="repaymentIst" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-callback="bt.calMoney" value="'+(data.repaymentIst==null?"":moneyFun.format(data.repaymentIst))+'" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/>';
            }else{
                return '<input disabled divName="repaymentIst" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentIst_'+rowNum+'"  type="text" value="'+(data.repaymentIst==null?"":moneyFun.format(data.repaymentIst))+'" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/>';
            }

        }
    },
    {data:"currentRepayMoney", render:function(data, row){
            return '<input disabled divName="currentRepayMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" value="'+(data.currentRepayMoney==null?"":moneyFun.format(data.currentRepayMoney))+'" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney"/>';
        }
    },
    {data:"surplusMoney", render:function(data, row){
            return '<input  value="'+(data.surplusMoney==null?"":moneyFun.format(data.surplusMoney))+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney" readonly= "true"/>';
        }
    },
    {data:"isRepay", render:function(data, row){
            return bt.dict('yn_state',data.isRepay)+'<input name="repayMents['+rowNum+'].isRepay" value="'+data.isRepay+'"  type="hidden"/>';
        }
    },
    {data:"remark", render:function(data, type, full, meta){
            return '<input '+isRepay+' name="repayMents['+rowNum+'].remark" id="remark" value="'+(data.remark==null?"":data.remark)+'" class="layui-input" type="text"/>';
        }
    },
    {data:null, render:function(data,row){
            return '<button '+isRepay+' data-type="del" type="button" name="itemOrder" value="'+rowNum+'" class="layui-btn layui-btn-mini" ><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button>';
        }
    }
];

bt.use(['form','element','dateType','moneyType','date','bttable','dicts','btImport'], function(){
	$ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
	bt.dicts.init();
	moneyType=bt.moneyType.init();
	//加载数据
	initVariable();
	  //生成列表
    var noVal = repayType == "03"//还款方式为其他不生成默认数据
	bt.bttable.init({
        elem: "#repayPlanTB",
        mulselect:false,
        checkbox: false,
        url: basePath+"/cms/basedata/repaymentschedule/getRepayMentScheduleByBillCode?billCode="+$("#billCode").val()+"&planId="+$("#planId").val(),
        buttons: [
              {
                  text: '<i class="layui-icon">&#xe654;</i>'+bt.lang('common.button.add'),
                  action: function ( e, dt, node, config ) {
                      add();
                  }
              },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                    exportExcel();
                }
            },
        ],
        columns:columns,
            delRowCallback:function(data,$tr){
	           $tr.remove();
	           MoneyCalculation();
	        },
	        tableInitCallback:function(data){
                rowNum = data.length;
                addFun();
                if (repayType != "07") {
                    MoneyCalculation();
                }
                sumMoney();
           }
    });
	//导出
    exportExcel=function(){
        var url = basePath + "/cms/basedata/repaymentschedule/exportBudget?billCode="+$("#billCode").val()+"&planId="+$('#planId').val();
        window.location.href = url;
    }
//导入初始化
    importExcel= function() {
        var surplusMoney=$("#loanMoney").val();
        up = bt.btImport.init({
            code: "70000003",
            titleRows: 0,
            headRows: 1,
            params:{
              repayTypeId:$("#repayType").val(),
            },
            success: function (head,data,select) {
                for (let i = 0; i < select.length; i++) {
                    surplusMoney=surplusMoney-select[i].repaymentMoney
                    addImport(select[i],surplusMoney);
                    sumMoney();
                }
            }
        });
    };
    form.on('submit(save)', function(data){
    	bt.loading.show();
    	if(verification() == false){
    		bt.loading.hide();
    		return false;
    	}
        var action = "/cms/basedata/repaymentschedule/save";
            $.post(basePath + action,data.field,function(result){
            	bt.loading.hide();
            	parent.layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                  var webObj = bt.navtab.getParentDom();
                  //给父级选项卡页面中id为test的input赋值;
                  webObj.bt.jquery("#planId").val(result.obj);
                  webObj.bt.jquery("#planStatus").val("0");
                  bt.navtab.closeCurrent();
                }
            },'JSON');

        return false;
    });

});

bt.use(['dateType'], function() {
	dateType = bt.dateType.init();
	dateFun=dateType.getFun();
bt.fun = function(value){
	MoneyCalculation();
}
})
//全局方法
function clickEvent(value){
    if (repayType === "07") {
        checkDateByCustomRepay();
    } else {
        bt.fun(value);
    }
}

function addFun(){
	//重新渲染datatype和moneyType
	 dateType.render();
	 moneyType.render();
	 //+1为了以防id重复
	 rowNum++;
}
/**
 * 初始化数据
 */
function initVariable(){
	loanDate=$("#loanDate").val();//发放日期
	repayDate=$("#repayDate").val();//还款日期
	loanMoney=$("#loanMoney").val();//发放金额
	loanRate=Number($("#loanRate").val());//利率
	surplusMoney=Number(loanMoney);//剩余本金（初始化为发放金额）
	surplusMoneyFixed=Number(loanMoney);//剩余本金（初始化为发放金额）
	if(!isNull($("#rateBase").val())){
		rateBase=Number($("#rateBase").val());
	}
	$("#repayBeginDate").attr("disabled","true");
}


/**
 * 各金额的合计
 */
function sumMoney(){
	//计算还款本金
	var repaymentMoneySum=0;
	$("input[divName^='repaymentMoney']").each(function(i){
		repaymentMoneySum = bt.money.add(repaymentMoneySum, $(this).val());
	});
	moneyType.get("repaymentMoneySum").val(repaymentMoneySum);
	//计算利息
	var repaymentIstSum=0;
	$("input[divName^='repaymentIst']").each(function(i){
		repaymentIstSum = bt.money.add(repaymentIstSum, $(this).val());
	});
	moneyType.get("repaymentIstSum").val(repaymentIstSum);

	//计算还款金额
	var currentRepayMoneySum=0;
	$("input[divName^='currentRepayMoney']").each(function(i){
		currentRepayMoneySum = bt.money.add(currentRepayMoneySum, $(this).val());
	});
	moneyType.get("currentRepayMoneySum").val(currentRepayMoneySum);
}


//获取两个日期之间的天数
function getDays(strDateStart,strDateEnd){
	return dateFun.getBetweenDay(strDateStart,strDateEnd);
	}
//关闭窗口
doClose = function(){
	if(bt.navtab){
		bt.navtab.closeCurrent();
	}
}


/**
 * 提交时验证
 */
function verification(){
	//统计还款本金
	var sumMoney = 0;
	var flag = false;
	$.each($("input[id*='repaymentMoney_']"), function(i,item){
	    if($(item).val() <= 0){
            var dataId = $(item).attr("id");
            if(-1 != dataId.indexOf("repaymentMoney_")){
                var moneyInd = dataId.substr(dataId.indexOf("_") + 1);
                if(moneyType.get("repaymentIst_"+moneyInd).val() <= 0 ){
                    flag = true;
                }
            }
        }
		sumMoney = bt.money.add(sumMoney, $(item).val());
    });
	if(flag) {
        parent.layer.alert(bt.lang("cms.message.repaymentMoney.zero"));//还款本金和还款利息不能同时为零
        return false;
    }
	if(Number(sumMoney)!=Number(loanMoney)){
		parent.layer.alert(bt.lang("common.message.repayment_Principal"));//还款金额与发放金额不等
		return false;
	}
	return true;
}

/**
* 判断是否为空 true：空 false:不为空
* @param data
*/
function isNull(data){
return (data == "" || data == undefined || data == null) ? true : false;
}
/**
 * 预计还款本金回调事件
 */
var divName = null
bt.calMoney = function(data,el){
    var rowNum = el.parents("tr").find("input[divName^='repaymentMoney']").data("id").split("_")[1];
    divName = $(el).attr("divName")+rowNum;
    // if (repayType === "07") {
    //     if(divName==='repaymentIst'+rowNum && Number(moneyType.get("repaymentMoney_"+rowNum).val())<Number(moneyType.get("repaymentIst_"+rowNum).val())){
    //         var minmoney=moneyType.get("repaymentMoney_"+rowNum).val();
    //         moneyType.get("repaymentIst_"+rowNum).val(minmoney);
    //     }
	//     MoneyCalculation();
    // }else{
    //     MoneyCalculation();
    // }
    MoneyCalculation();
}

/**
 * 自定义金额计算
 * @param trEle
 */
function calCustomMoney(trEle){
    // 行下标
    var rowNum = trEle.find("input[divName^='repaymentMoney']").data("id").split("_")[1];
    if (!isNull(rowNum)) {
        var repaymentMoney = moneyType.get("repaymentMoney_"+rowNum).val();
        var repaymentIst = moneyType.get("repaymentIst_"+rowNum).val();
        if (!isNull(repaymentMoney) && !isNull(repaymentIst)) {
            moneyType.get("currentRepayMoney_"+rowNum).val(moneyFun.add(repaymentMoney, repaymentIst));
            var allRepayMoney = 0;
            // 遍历本金
            trEle.prevAll().each(function (index, item) {
                allRepayMoney = moneyFun.add(allRepayMoney, $(this).find("input[divName^='repaymentMoney']").val());
            });
            moneyType.get("surplusMoney_"+rowNum).val(moneyFun.sub(surplusMoney, moneyFun.add(allRepayMoney, repaymentMoney)));
            sumMoney();
        }
    }
}

/**
 *日期检查，即下一个还款日期不能小于上一个还款日期
 */
function checkDateByCustomRepay(){
    var trs = $("#repayPlanTB").children("tbody").children("tr");
    var lastPrediceRepayDateId="";//上一笔还款日期id
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
                }
            }
        )
        var isRepay=$(tr).find("td[name^='isRepay']").find("input").val();
        if(isRepay!="0"){
            lastPrediceRepayDateId = prediceRepayDate_id;
        }else{
            var id = prediceRepayDate_id.split("_")[1];
            var prediceRepayDate = $("#prediceRepayDate_"+id).val();//预计还款日
            //上一笔日期
            var lastDate;
            if(isNull(lastPrediceRepayDateId)){
                lastDate = loanDate;
            }else {
                lastDate = dateType.get(lastPrediceRepayDateId).val()
            }
            if(dateFun.compare(dateFun.get(lastDate),dateFun.get(prediceRepayDate))>0){
                parent.layer.alert(bt.lang("common.message.prediceRepayDate"));//预计还款日期不能小于上一笔日期
                dateType.get("prediceRepayDate_"+id).val("");
            }
            lastPrediceRepayDateId = prediceRepayDate_id;
        }
    })
}
