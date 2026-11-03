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
    bt.setItemType = function(typeVal, elem, bv, wrapObj) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	var dataId = $(wrapObj).attr("id");
    	var moneyIndex = dataId.indexOf("type");
    	var rowIndex = dataId.substr(0, moneyIndex);

    	$.post(basePath + "/cms/rat/raturationapply/getUrationItem", {
    		disCode : disCode,
			sysItemCode : typeVal.code
		}, function(data) {
			$("#"+ rowIndex +"itemCode").empty();
			moneyType.get(rowIndex+"money").val("0");
			if(data && data.length>0){
				var itemFlag = data[0].itemFlag;
				if("0" == itemFlag){
					bt.setMoneyEditable("0", rowIndex);
					//bt.setAssure(rowIndex, data[0]);
					var html = '<input type="hidden" name="applyDtls[' + rowIndex + '].itemFlag" id="itemFlag' + rowIndex + '" value="0">'
							+ '<td><input type="hidden" name="applyDtls[' + rowIndex + '].itemOrder" value="'+data[0].itemOrder+'">';
					html += '<div class="layui-input-inline"><select lay-verify="required" name="applyDtls['+rowIndex+'].itemCode" id="'+rowIndex+'itemCode"  lay-search="" lay-filter="itemCode">'
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
							//bt.setAssure(rowIndex, data[0]);
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
				//form.render();
				form.render('select');
			}else{
				var html = '<input type="hidden" name="' + rowIndex + 'itemFlag" id="itemFlag' + rowIndex + '" value="0">';
				html += '<div class="layui-input-inline"><select name="'+rowIndex+'itemCode" id="'+rowIndex+'itemCode"  lay-search="" lay-filter="itemCode">'
						+ '<option value=""></option></select></div>';
				$("#itemCodeTd" + rowIndex).html(html);
				//bt.setAssure(rowIndex, {"assurepercent" : "0","interestrate" : "0"});
			}
			form.render();
            //设置新增的品种不可选已勾选的品种
            bt.setItemCode(data,elem,bv);
		}, 'JSON');
    }
    
    //设置保证金比例利率
    bt.setAssure = function(rowIndex, data) {
    	//moneyType.get(rowIndex + "assurepercent").val(data.assurepercent);
    	//moneyType.get(rowIndex + "interestrate").val(data.interestrate);
    }
    
    //设置其他授信品种不可选
    bt.setItemCode = function(data,elem,bv){
        var Othis = elem.othis.parent('[data-link="true"]');
        var v = elem.value;
        var doms = $('[data-link="true"]');
        bt.each(doms,function(){
            var othat = $(this);
            if(!othat.is(Othis)){
              //  othat.find('select option[value="'+v+'"]').prop("disabled",true);
              //  othat.find('select option[value="'+bv+'"]').prop("disabled",false);
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
    	//bt.calIndexOpenMoney(rowIndex);
    }
    
    bt.setMoney = function(rowIndex){
    	var sumMoney = 0;
    	$.each($("input[data-id^='"+rowIndex+"maxMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get(rowIndex+"money").val(sumMoney);
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
    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='money']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("moneysum").val(bt.money.format(sumMoney));
    }
    //循环计算敞口授信额度
    bt.calSumOpenMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='openMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("openMoneysum").val(bt.money.format(sumMoney));
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
    	var disCode = $("#disCode").val();
    	rowMR++;
		var html='<tr><input type="hidden" id="rowSign'+rowMR+'" value="'+rowMR+'">';
		html +='<td><div class="layui-inline"><div class="bt-biz-selectType" data-required="true" data-name="applyDtls['+rowMR+'].type" data-method="post" data-url="' + basePath + '/cms/rat/cprationapply/getCpRationDisSysItem?disCode='+disCode+'" '
			+ 'data-valuefield="code" data-textfield="name" id="'+rowMR+'type" data-biz-name="'+rowMR+'type" data-search="false"  data-link="true" data-callback="bt.setItemType"></div></td>';
		html += '<td ><span id="itemCodeTd'+rowMR+'"><div class="layui-input-inline"><select lay-verify="required" name="applyDtls['+rowMR+'].itemCode" id="'+rowMR+'itemCode" lay-search="" lay-filter="itemCode">'
			+ '<option value=""></option></select></div></span></td>';
        html += '<td><input type="text" name="applyDtls['+rowMR+'].beginDate" lay-verify="required" data-min="'+beginDate+'" data-initmax="'+endDate+'" id="'+rowMR+'beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="'+rowMR+'beginDate" rowindex="'+rowMR+'" data-endid="'+rowMR+'endDate" data-callback="bt.setDays"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].endDate" lay-verify="required" id="'+rowMR+'endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="'+rowMR+'endDate" rowindex="'+rowMR+'" data-callback="bt.setDays"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].days" id="'+rowMR+'days" readonly="readonly" class="layui-input"></td>';
		html+='<td><input type="text" data-name="applyDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" rowindex="'+rowMR+'" data-decimal="2" data-min="0" data-minus="false" ></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].contactNo" lay-verify="required" id="'+rowMR+'contactNo" maxlength="50" class="layui-input"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].lcNo" id="'+rowMR+'lcNo" maxlength="32" class="layui-input"></td>';
		html += '<td><input type="text" name="applyDtls['+rowMR+'].billNo" id="'+rowMR+'billNo" maxlength="32" class="layui-input"></td>';
		html+='<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;\t'
			+ '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
			+ '</td>';
		html+='</tr>';
        html += '<tr><td align="right">'+bt.lang("table.ratBkration.remark")+'</td><td colspan="8" >' +
			'<input type="text" name="applyDtls['+rowMR+'].remark" id="'+rowMR+'remark" maxlength="100" class="layui-input remark"></td></tr>';
        $("#singleTable tbody:first").append(html);
		initSelectCode(rowMR);
		return false;
    });
    
    var initSelectCode=function(row){
    	bt.dicts.init();
    	bt.moneyType.init();
    	bt.dateType.init();
        //设置新增的品种不可选已勾选的品种
        bt.setNewItemDisable(row);
    	bt.selectType.init();
    }

    //新增品种行设置已勾选的品种不可选择
    bt.setNewItemDisable = function(rowIndex){
        var doms = $('[data-link="true"]');
        var disItem = "";
        bt.each(doms, function(i,item){
            var othat = $(this);
            var dataObj = $(othat).find("option:selected").val();
            if(!bt.empty(dataObj)){
                //$("#"+rowIndex + "itemCode").find('select option[value="'+dataObj+'"]').prop("disabled",true);
                disItem += dataObj + ",";
            }
        })
        disItem = bt.jquery.trim(disItem);
        if(disItem.length > 1){
            disItem = disItem.substr(0, disItem.length - 1);
            $("#"+rowIndex + "type").attr("data-itemdisabled", disItem);
            form.render("select");
        }
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
                $($tr).next().remove();//先删除下一行(备注)
		        $tr.remove();
		    },
		    tableInitCallback:function(){}
	    });
    }
    initRationTable();
    
    
    function checkBefore(){
    	
    	return true;
    }
    
    //监听暂存或提交
    form.on('submit', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	var nm = $(this).attr("id");    
        if(nm == "submit"){
        	data.field.saveType = "1";//提交
        }else{
        	data.field.saveType = "0"; //暂存
        }
        var action = "/cms/rat/cprationapply/tempOrSubmit";
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
    
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	//bt.navtab.refreshParent();
        	bt.navtab.closeCurrent();
        }
    }
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

closeTab = function(){
	bt.close();
}