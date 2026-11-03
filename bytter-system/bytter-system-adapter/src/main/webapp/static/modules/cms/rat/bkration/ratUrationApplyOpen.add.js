var rowMR=0;
var selectTypes;
var moneyType;
var rowArray = {};
bt.use(['form','layedit','btdate','bttable','moneynum','dicts','select','moneyType','dateType','corp', 'selectType','money'], function(){
    var $ = bt.jquery,form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    var ratCode = $("#ratCode").val();
    var disCode = $("#disCode").val();
	var corpId = $("#corpId").val();
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    //综合设置占用额度不可编辑
    bt.setMoneyEditable = function(flag, rowIndex){
    	if(flag == "0"){
    		$("input[data-id='"+rowIndex+"money']").removeAttr("readonly");
    	} else {
    		$("input[data-id='"+rowIndex+"money']").attr("readonly", "readonly");
    	}
    }
    
    //通过
    bt.setItemType = function(data, elem, bv, wrapObj) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	var dataId = $(wrapObj).attr("id");
    	var moneyIndex = dataId.indexOf("type");
    	var rowIndex = dataId.substr(0, moneyIndex);

    	$.post(basePath + "/cms/rat/raturationapply/getUrationItem", {
    		disCode : disCode,
			sysItemCode : data.code
		}, function(data) {
			$("#"+ rowIndex +"itemCode").empty();
			if(data && data.length>0){
				var itemFlag = data[0].itemFlag;
				if("0" == itemFlag){
					bt.setMoneyEditable("0", rowIndex);
					bt.setAssure(rowIndex, data[0]);
					var html = '<input type="hidden" name="applyDtls[' + rowIndex + '].itemFlag" id="itemFlag' + rowIndex + '" value="0">'
							+ '<td><input type="hidden" name="applyDtls[' + rowIndex + '].itemOrder" value="'+data[0].itemOrder+'">';
					html += '<div class="layui-input-inline"><select name="applyDtls['+rowIndex+'].itemCode" id="'+rowIndex+'itemCode" onchange="bt.selectItemCode(this)" lay-search="" lay-filter="itemCode">'
						 + '<option value=""></option></select></div>';
					$("#itemCodeTd" + rowIndex).html(html);
					$("#"+ rowIndex +"itemCode").val(data[0].itemCode);
					//单品种
					$.each(data,function(index,item){
						$("#"+ rowIndex +"itemCode").append("<option value="+item.itemCode+">" + item.itemName + "</option>");
					});
					var itemCode = data[0].itemCode;
					$("select[id='"+ rowIndex +"itemCode']").val(itemCode);
					moneyType.get(rowIndex+"money").attr("data-max", data[0].money);
				} else {
					bt.setMoneyEditable("1", rowIndex);
					//综合品种
					var html = '<input type="hidden" name="applyDtls[' + rowIndex + '].itemFlag" id="itemFlag' + rowIndex + '" value="1">';
					html += '<table class="layui-table" width="100%" style="margin:0px">';
					html += '<tr>' +
							'<th>' + bt.lang("table.ratBkrationDetail.itemCodeFlag") + '</th>' +
							'<th>' + bt.lang("table.ratBkrationDetail.itemCode") + '</th>' +
							'<th><font color="red">*</font>' + bt.lang("table.ratUrationApply.money") + '</th>' +
							'</tr>';
					var compsize = data.length;
					$.each(data,function(index,item){
						if(index == 0){
							bt.setAssure(rowIndex, data[0]);
							html += '<tr>' +
								'<td rowspan="'+compsize+'">'+ bt.dict("RatModel.ItemFlag", item.itemFlag) +
								'<td><input type="hidden" name="applyDtls[' + rowIndex + '].applyDtls['+item.itemOrder+'].itemOrder" id="' + rowIndex + 'itemOrder" value="'+item.itemOrder+'">' +
								'<input type="hidden" name="applyDtls[' + rowIndex + '].applyDtls['+item.itemOrder+'].itemCode" rowIndex="'+rowIndex+'" itemOrder="'+item.itemOrder+'" id="'+rowIndex+'itemCode'+item.itemOrder+'" value="'+item.itemCode+'">' + item.itemName + '</td>' +
								'<td align="right"><input type="text" data-name="applyDtls[' + rowIndex + '].applyDtls['+item.itemOrder+'].maxMoney" data-biz-name="'+rowIndex+'maxMoney' + item.itemOrder + '" data-id="'+rowIndex+'maxMoney' 
								+ item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" data-max="'+item.maxMoney+'" data-minus="false" data-callback="bt.calUMoney"></td>' +
								'</tr>';
						} else {
							html += '<tr>' +
								'<td><input type="hidden" name="applyDtls[' + rowIndex + '].applyDtls['+item.itemOrder+'].itemOrder" value="'+item.itemOrder+'">' +
								'<input type="hidden" name="applyDtls[' + rowIndex + '].applyDtls['+item.itemOrder+'].itemCode" rowIndex="'+rowIndex+'" itemOrder="'+item.itemOrder+'" id="'+rowIndex+'itemCode'+item.itemOrder+'" value="'+item.itemCode+'">' + item.itemName + '</td>' +
								'<td align="right"><input type="text" data-name="applyDtls[' + rowIndex + '].applyDtls['+item.itemOrder+'].maxMoney" data-biz-name="'+rowIndex+'maxMoney' + item.itemOrder + '" data-id="'+rowIndex+'maxMoney' 
								+ item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" data-max="'+item.maxMoney+'" data-minus="false" data-callback="bt.calUMoney"></td>' +
								'</tr>';
						}
						
					});
					html += '</table>';
					$("#itemCodeTd" + rowIndex).html(html);
				}
				//获取授信品种的调剂额度
				var itemCode = data[0].itemCode;
				//bt.getItemMoney(ratCode, corpId, itemFlag, itemCode);
				bt.moneyType.init();
				form.render();
			}else{
				var html = '<input type="hidden" name="' + rowIndex + 'itemFlag" id="itemFlag' + rowIndex + '" value="0">';
				html += '<div class="layui-input-inline"><select name="'+rowIndex+'itemCode" id="'+rowIndex+'itemCode" onchange="bt.selectItemCode(this)" lay-search="" lay-filter="itemCode">'
						+ '<option value=""></option></select></div>';
				$("#itemCodeTd" + rowIndex).html(html);
				bt.setAssure(rowIndex, {"assurepercent" : "0","interestrate" : "0"});
			}
			form.render();
		}, 'JSON');
    }
    
    //设置保证金比例利率
    bt.setAssure = function(rowIndex, data) {
    	moneyType.get(rowIndex + "assurepercent").val(data.assurepercent);
    	moneyType.get(rowIndex + "interestrate").val(data.interestrate);
    }
    
    //设置其他授信品种不可选
    bt.setItemCode = function(data,elem,bv){
        var Othis = elem.othis.parent('[data-link="true"]');
        var v = elem.value;
        var doms = $('[data-link="true"]');
        bt.each(doms,function(){
            var othat = $(this);
            if(!othat.is(Othis)){
                othat.find('select option[value="'+v+'"]').prop("disabled",true);
                othat.find('select option[value="'+bv+'"]').prop("disabled",false);
            }
        })
        form.render("select");
    }
    
    
    bt.calUMoney = function(data, el) {
    	var dataId = $(el).attr("data-id");
    	var index = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, index);
    	var itemOrder = dataId.substr(index);
    	var money = moneyType.get(rowIndex+"money").val();
    	bt.setMoney(rowIndex);
    	bt.calIndexOpenMoney(rowIndex);
    }
    
    bt.setMoney = function(rowIndex){
    	var sumMoney = 0;
    	$.each($("input[data-id^='"+rowIndex+"maxMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get(rowIndex+"money").val(sumMoney);
    }
    
    //根据授信额度和保证金比例费率计算敞口额度
    bt.calIndexOpenMoney = function(rowIndex) {
    	var itemFlag = $("#itemFlag"+rowIndex).val();
    	var moneyIndexVal = moneyType.get(rowIndex+"money").val();
    	if("0" == itemFlag){
    		var assurepercent = moneyType.get(rowIndex + "assurepercent").val();
    		//授信额度*（1-保证金比例/100）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(assurepercent, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	} else {
    		var interestrate = moneyType.get(rowIndex + "interestrate").val();
    		//授信额度*（1-利率/费率）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(interestrate, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	}
    }
    
    //检查最高额度与授信额度关系
    bt.checkMaxMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	var moneyIndexVal = moneyType.get(compRowIndex+"money").val();
    	if(bt.empty(moneyIndexVal)){
    		layer.alert(bt.lang("table.ratbkrationdetail.moneymsg"));
    		moneyType.get(rowIndex+"maxMoney").val(0);
    		return false;
    	}
    	//最高额度不能大于授信额度
    	if(bt.money.sub(moneyIndexVal, data) < 0){
    		layer.alert(bt.lang("table.ratbkrationdetail.maxMoneyOut"));
    		moneyType.get(rowIndex+"maxMoney").val(0);
    		return false;
    	}
    }
    
    bt.calMoney = function(data, el){
    	var rowIndex = $(el).attr("rowindex");
    	//var moneyIndex = dataId.indexOf("money");
    	//var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calIndexOpenMoney(rowIndex);
    }
    
    bt.setDays = function(data, el) {
    	var rowIndex = $(el).attr("rowindex");
    	var beginDate = bt.dateType.init().get(rowIndex+"beginDate").val();
    	if(bt.empty(beginDate)){
    		return;
    	}
    	var endDate = bt.dateType.init().get(rowIndex+"endDate").val();
    	if(bt.empty(endDate)){
    		return;
    	}
    	var days = getBetweenDay(beginDate, endDate) - (-1);
    	$("#" + rowIndex + "days").val(days);
    }
    
    
    //新增单品种事件
    $("#add").click(function(){
    	var beginDate = $("#beginDate").val();
    	var endDate = $("#endDate").val();
    	rowMR++;
		var html='<tr><input type="hidden" id="rowSign'+rowMR+'" value="'+rowMR+'">';
		html +='<td><div class="layui-inline"><div class="bt-biz-selectType" data-name="applyDtls['+rowMR+'].type" data-method="post" data-url="' + basePath + '/cms/rat/raturationapply/getRationDisSysItem?disCode='+disCode+'" '
			+ 'data-valuefield="code" data-textfield="name" id="'+rowMR+'type" data-biz-name="'+rowMR+'type" data-search="false" data-required="true" data-callback="bt.setItemType"></div></td>';
		html += '<td id="itemCodeTd'+rowMR+'"><div class="layui-input-inline"><select name="applyDtls['+rowMR+'].itemCode" id="'+rowMR+'itemCode" onchange="bt.selectItemCode(this)" lay-search="" lay-filter="itemCode">'
			+ '<option value=""></option></select></div></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].beginDate" lay-verify="required" data-min="'+beginDate+'" data-initmax="'+endDate+'" id="'+rowMR+'beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="'+rowMR+'beginDate" rowindex="'+rowMR+'" data-endid="'+rowMR+'endDate" data-callback="bt.setDays"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].endDate" lay-verify="required" id="'+rowMR+'endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="'+rowMR+'endDate" rowindex="'+rowMR+'" data-callback="bt.setDays"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].days" lay-verify="required" id="'+rowMR+'days" readonly="readonly" class="layui-input"></td>';
		html+='<td><input type="text" data-name="applyDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" rowindex="'+rowMR+'" data-decimal="2" data-min="0" data-minus="false" data-callback="bt.calMoney"></td>';
		html+='<td><input type="text" data-name="applyDtls['+rowMR+'].assurepercent" id="'+rowMR+'assurepercent" data-biz-name="'+rowMR+'assurepercent" rowindex="'+rowMR+'" class="layui-input bt-biz-moneyType" data-decimal="2" data-min="0" data-minus="false" data-callback="bt.calMoney"></td>';
		html+='<td><input type="text" data-name="applyDtls['+rowMR+'].interestrate" id="'+rowMR+'interestrate" data-biz-name="'+rowMR+'interestrate" rowindex="'+rowMR+'" class="layui-input bt-biz-moneyType" data-decimal="6" data-minus="false" data-callback="bt.calMoney"></td>';
		html+='<td><input type="text" data-name="applyDtls['+rowMR+'].openMoney" id="'+rowMR+'openMoney" data-biz-name="'+rowMR+'openMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" readonly="readonly"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].contactNo" lay-verify="required" id="'+rowMR+'contactNo" maxlength="50" class="layui-input"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].lcNo" id="'+rowMR+'lcNo" maxlength="32" class="layui-input"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].billNo" id="'+rowMR+'billNo" maxlength="32" class="layui-input"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].remark" id="'+rowMR+'remark" maxlength="100" class="layui-input"></td>';
		html+='<td>&nbsp;&nbsp;&nbsp;&nbsp;\t'
			+ '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
			+ '</td>';
		html+='</tr>';
		$("#singleTable tbody:first").append(html);
		initSelectCode(rowMR);
		return false;
    });
    
    var initSelectCode=function(row){
    	bt.dicts.init();
    	bt.moneyType.init();
    	bt.dateType.init();
    	bt.selectType.init();
    }
    
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    
    //初始化删除按钮
    var initRationTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:false,
	        scrollY:false,
	        clickTrSelected : false,
	    	elem: "#singleTable",
	    	//url: basePath+"/cms/rat/ratbkration/initRationDtl?opType=add",
	    	buttons: [
	    	          ],
	    	columns:[],            
		    delRowCallback:function(data,$tr){
		    	//删除后更新其他行的授信品种选择
		        $tr.remove();
		    },            
		    tableInitCallback:function(){}
	    });
    }
    initRationTable();
    
    function checkBefore(){
    	
    	return true;
    }
    
    bt.setParam = function(){
    	var params = {};
    	params.ratCode = ratCode;
    	params.disCode = disCode;
    	params.corpId = corpId;
    	params.curCode = $("#curCode").val();
    	params.applyDtls = {};
    	//循环列
    	var applyDtls = [];
    	var index = 0;
		for(var i = 1; i <= rowMR; i++) {
			index = i;
			var applyDtl = {};
			var rowSignPut = $("#rowSign"+ i);
			if(rowSignPut){
				var rowSign = rowSignPut.val();
			}
			if(bt.empty(rowSign)){
				continue;
			}
			applyDtl.type = selectTypes.get(i+"type").val();
			var itemFlag = $("#itemFlag"+i).val();
			applyDtl.itemFlag = itemFlag;
			applyDtl.itemCode = [];
			applyDtl.itemOrder = [];
			applyDtl.maxMoney = [];
			if(itemFlag == 0){
				var itemCode = $("#itemCode"+i).val();
				applyDtl.itemCode.push(itemCode);
			} else {
				$.each($("input[name='"+i+"itemOrder']"), function(i, item) {
					var itemOrder = $(item).val();
					applyDtl.itemOrder.push(itemOrder);
					var itemCode = $("#"+i+"itemCode"+itemOrder).val();
					applyDtl.itemCode.push(itemCode);
					var maxMoney = moneyType.get(i+"maxMoney"+itemOrder).val();
					applyDtl.maxMoney.push(bt.money.toNumber(maxMoney));
				});
			}
			applyDtl.beginDate = bt.dateType.init().get(i+"beginDate").val();
			applyDtl.endDate = bt.dateType.init().get(i+"endDate").val();
			applyDtl.money = moneyType.get(i+"money").val();
			applyDtl.openMoney = moneyType.get(i+"openMoney").val();
			applyDtl.interestRate = moneyType.get(i+"interestrate").val();
			applyDtl.assurepercent = moneyType.get(i+"assurepercent").val();
			applyDtl.contactNo = $("#"+ i + "contactNo").val();
			applyDtl.lcNo = $("#"+ i + "lcNo").val();
			applyDtl.billNo = $("#"+ i + "billNo").val();
			applyDtl.remark = $("#"+ i + "remark").val();
			applyDtls.push(applyDtl);
		}
		params.applyDtls = applyDtls;
    	return params;
    }
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	
        var action = "/cms/rat/raturationapply/save";
        $.post(basePath + action, data.field,function(result){
        	bt.loading.hide(); //关闭加载中
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
                	webObj.search();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	
        var action = "/cms/rat/raturationapply/submit";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
                	webObj.search();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    
});

function setItemCode(data,elem,bv){
    bt.setItemCode(data,elem,bv)
}

/**
 * 得到两个日期之间的天数
 * @param startDate
 * @param endDate
 */
function getBetweenDay(startDate,endDate){
	var date1 = bt.date.get(startDate);
	var date2 = bt.date.get(endDate);
	var btt = date2 - date1;
	return btt/(1000*60*60*24);
}
