var initBankAcc,uploader;
bt.use(['form','layedit','selectTable','moneynum','btdate','select','money','dicts','bttable','dateType','selectType','moneyType',
	'externalCorpBankAcc','externalCorpBankAccFex','btUpload','bankInfo','swiftInfo','bankAccDomestic','bankAccAbroad'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.selectType.init();
	var dict = bt.dicts.init();
    bt.moneynum.init({elem:$('[data-type="int"]'),decimal:0,minus:false,callback : function(data){
    }});
	uploader=bt.btUpload.init({
		elem: "#uploader",
		auto: false,
		isExpand: true,
		gid: $("#attachmentId").val()
	});
    
    //时间
	$.each($("div[type='date']"),function(i,item){
		bt.btdate.init({
			elem: "[field='"+$(item).attr("field")+"']",
			name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
			istime: false,
			'lay-verify': "required",
			choose:function(data){
		    	$("input[name='accbook." + $(item).attr("field") + "'").val(data);
			}
		});
	});
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref"),
        });
    });
    
    var checkBefore = function(){
    	
    	
    }
    
  //监听提交
    form.on('submit(temp)', function(data){
		data.field["accbook.attachmentId"] = uploader.getGroupId();
    	bt.loading.show();//显示加载中
        var action = "/finacial/accbook/fcaccbook/save";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
		data.field["accbook.attachmentId"] = uploader.getGroupId();
    	bt.loading.show();//显示加载中
        var action = "/finacial/accbook/fcaccbook/submit";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
    
    var addBookTr = function(){
		var account = '<td><div class="layui-inline"><input type="text" name="bookDtls[' + rowMR + '].account" id="account' + rowMR + '" class="layui-input text input-normal" lay-verify = "required|tableData">'
					+ '<table id="example' + rowMR + '">'
					+ '	<thead>'
					+ '    	<tr>'
					+ '        	<th>' + bt.lang("table.fcAccbookDtl.account")+ '</th>'
					+ '            <th>' + bt.lang("table.fcAccbookDtl.accName")+ '</th>'
					+ '            <th>' + bt.lang("table.fcAccbookDtl.subjectCode")+ '</th>'
					+ '    	</tr>'
					+ '    </thead>'
					+ '</table>'
					+ '</div></td>';
		var html='<tr row="'+rowMR+'">';
		html+='<td><div class="selectCode' + rowMR + '"></div></td>';
		html+= account;
		html+='<td><div id="accName' + rowMR + '">&nbsp;</div>'
		+ '<input id="baccName' + rowMR + '" name="bookDtls[' + rowMR + '].accName" type="hidden"></td>';
		html+='<td><input type="text" name="bookDtls[' + rowMR + '].origVoucherNo" class="layui-input input-normal" maxlength="32" /></td>';
		html+='<td><input type="text" data-name="debitMoney_' + rowMR + '" alisaname="debitMoney_' + rowMR + '" data-type="precent" decimal="2" minus="true" class="layui-input input-normal" maxlength="20" /> '
		+ '<input id="bookDtls[' + rowMR + '].debitMoney" name="bookDtls[' + rowMR + '].debitMoney" type="hidden" ></td>';
		html+='<td ><input type="text" data-name="creditMoney_' + rowMR + '" alisaname="creditMoney_' + rowMR + '" data-type="precent" decimal="2" minus="true" class="layui-input input-normal" maxlength="20" /> '
		+ '<input id="bookDtls[' + rowMR + '].creditMoney" name="bookDtls[' + rowMR + '].creditMoney" type="hidden" ></td>';
		html += '<td><span id="cashFlowItem' + rowMR + '"></span></td>';
		if(rowMR - 1 > 0){
		html+='<td><button type="button" data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
		} else {
		html+='<td></td>';
		}
		html+='</tr>';
		$("#printMRTable>tbody").append(html);
		bt.dicts.init();
		initdataTableMoney(rowMR);
		initSelectCode(rowMR);
		initSelectAccount(rowMR);
		rowMR++;
    }
    
    var rowMR=0;
    var rowFmtDtl=0;
    var initPrintMRTable=function(){
	    //生成列表
	    bt.bttable.init({
	    	elem: "#printMRTable",
	    	url: basePath+"/finacial/accbook/fcaccbook/getFcAccBookDtls",
	    	scrollX : true,
	    	scrollY : "800px",
	    	selectCss:false,
	    	buttons: [
	    	          {
	    	        	  text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
	    	        	  action:function($button){
	    	        		  addBookTr();
	    	        	  }
	    	          }
	    	          ],
		      columns:[
		               {
		            	   data:"summaryContent",
		            	   render:function(data, row){
		            		   var reInfo='<div class="selectCode' + rowMR + '"></div>';
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"account",
		            	   render:function(data,row){
		            		   var reInfo = '<div class="layui-inline"><input type="text" name="bookDtls[' + rowMR + '].account" id="account' + rowMR + '" class="layui-input text" lay-verify = "tableData">'
								 + '<table id="example' + rowMR + '">'
								 + '	<thead>'
								 + '    	<tr>'
								 + '        	<th>' + bt.lang("table.fcAccbookDtl.account")+ '</th>'
								 + '            <th>' + bt.lang("table.fcAccbookDtl.accName")+ '</th>'
								 + '            <th>' + bt.lang("table.fcAccbookDtl.subjectCode")+ '</th>'
								 + '    	</tr>'
								 + '    </thead>'
								 + '</table></div>';
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"accName",
		            	   render:function(data,row){
		            		   var reInfo='<div id="accName' + row + '">' + data.accName + '</div>'
		            		   	+ '<input id="baccName' + row + '" name="bookDtls[' + row + '].accName" type="hidden" value="'+data.accName+'">';
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"origVoucherNo",
		            	   render:function(data,row){
		            		   var reInfo='<input type="text" name="bookDtls[' + row + '].origVoucherNo" class="layui-input" maxlength="32" />';
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"debitMoney",
		            	   render:function(data,row){
		            		   var reInfo ='<input type="text" data-name="bookDtls[' + rowMR + '].debitMoney" name="bookDtls[' + rowMR + '].debitMoney" alisaname="debitMoney_' + rowMR + '" data-type="precent" decimal="2" minus="true" class="layui-input" maxlength="20" /> '
		    	        			  + '';
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"creditMoney",
		            	   render:function(data,row){
		            		   var reInfo ='<input type="text" data-name="bookDtls[' + rowMR + '].creditMoney" name="bookDtls[' + rowMR + '].creditMoney" alisaname="creditMoney_' + rowMR + '" data-type="precent" decimal="2" minus="true" class="layui-input" maxlength="20" /> '
		    	        			  + '';
		            		   return reInfo;
		            	   }
		               },
		               {data:null,render:function(data,row){
		            	   var reInfo='';
            			   reInfo='<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
		                   return reInfo;            
		               }}
		       ],            
		       delRowCallback:function(data,$tr){
		    	   calcDelDr($tr.attr("row"));//重新计算金额合计   = 原值  - 删行的值
		           $tr.remove();
		       },            
		       tableInitCallback:function(){  
		    	   initSelectCode();
		    	   bt.dicts.init();
		    	   // 默认增加两行数据
				   addBookTr();
				   addBookTr();
	          }
	    });
	    rowMR=0;
    }
    
    var initSelectCode=function(row){
    	var cls="";
    	if(row!= undefined){
    		cls=row
    	}
    	if($(".selectCode"+cls).length>0){
    		bt.select.init({
    			elem:'.selectCode'+cls,
    			name:"bookDtls["+cls+"].summaryContent",
    			valueField:"summaryContent",
    			textField:"summaryContent",
    			search: true,
    			required: true,
    			inputValue:true,
    			disabled: false,
    			url:basePath+"/finacial/paramconfig/btsummary/json"
    		});
    	}
    }
    
    var initSelectAccount = function(row){
    	var cls="";
    	if(row!= undefined){
    		cls=row
    	}
    	bt.selectTable.init({
    	   	 elem: "#example"+cls,
    	   	 url: basePath + "/finacial/accbook/fcaccbook/getTallyAccs",
    	   	 scrollX: true,
    	   	 paging:true,
    	   	 scrollY:"200px",
    	   	 searchInput:"#account"+cls,
    	   	 columns: [
	             {data:"blnctrAcc",},
	             {data:"accName",},
	             {data:"subjectName",}
    	   	 ],
    	   	 clickCallback:function(data){
    	   		 //设置账户名称
    	   		$("#account"+cls).val(data.blnctrAcc);
    	   		$("#accName"+cls).text(data.accName);
    	   		$("#baccName"+cls).val(data.accName);
				 /**
				  * 设置现金流项目
				  */
				 setCashFlowItem(cls);
    	   	 }
    	});
    	
    }
    
    var initdataTableMoney=function(row){
		var cls="";
        if(row!= undefined){
        	cls=row
        }
    	//格式化数字
        $.each($('[data-type="precent"]'),function(i,item){
            if($(item).attr("decimal")!=""){
            	var alisaname = $(item).attr("alisaname");
             	if(undefined != alisaname){
             		alisaname = alisaname.split("_");
             	}
             	var mindex;
             	if(alisaname.length == 2){
         	    	mindex = alisaname[1];
             	} else {
         	    	mindex = $(item).attr("index");
             	}
            	if(mindex - row < 0){
            		return;
            	}
                bt.moneynum.init({elem:$(item),decimal:2,minus:true,callback : function(data){
                	calcTot(item, data);
                }});
            }
        });
    }
    
    /*
     * 先还原原先列的借贷方金额
     */
    var calcDelDr = function(rowIndex){
     	//重新计算金额合计   = 原值  - 删行的值
        var mindex = rowIndex;
     	var data_name = "[alisaname='creditMoney_" + mindex + "']";
     	//判断贷方是否已录入金额
     	var creditMoneyi = layui.jquery(data_name).val();
     	creditMoneyi = bt.money.toNumber(creditMoneyi);
     	if(null == creditMoneyi || creditMoneyi - 0 == 0){
     	} else {
     		//减掉录入的金额
     		delTrTot(creditMoneyi, "creditTot");
     		return;
     	}
     	
     	var data_name = "[alisaname='debitMoney_" + mindex + "']";
     	//判断借方是否已录入金额
     	var debitMoneyi = layui.jquery(data_name).val();
     	creditMoneyi = bt.money.toNumber(debitMoneyi);
     	if(null == debitMoneyi || debitMoneyi - 0 == 0){
     	} else {
     		//减掉录入的金额
     		delTrTot(debitMoneyi, "debitTot");
     		return;
     	}
     }
    
     /**
      * 删除行后更新合计值
      */
    var delTrTot = function(data, telId){
		var $tel = $("#" + telId);//贷方增加合计
		//计算合计值 = 原值 + 新值 - 旧值
		var totMoney = bt.money.sub($tel.attr("val"), data);
		$("#" + telId).attr("val", totMoney);//
		$("#" + telId).text(bt.money.format(totMoney));
    }
     
     /**
      * 计算合计值
      */
    var setTot = function(item, data, telId){
    	var oldVal = $(item).attr("oldVal");
     	var newVal = data;
     	if(oldVal === undefined){
     		oldVal = 0;				
     	}
     	if(newVal === '') {
     		newVal = 0;
     	} else {
     		newVal = bt.money.toNumber(newVal);
     	}
     	
     	var $tel = $("#" + telId);//贷方增加合计
     	//计算合计值 = 原值 + 新值 - 旧值
     	var totMoney = bt.money.add($tel.attr("val"), data, -1*oldVal);
     	
 		$("#" + telId).attr("val", totMoney);//
 		$("#" + telId).text(bt.money.format(totMoney));
 		$(item).attr("oldVal", bt.money.toNumber(data));
     	$(item).attr("value", bt.money.toNumber(data));
     }
     
    var calcTot = function(item, data){
    	if(null == data){
    		data = 0;
    	}
    	var oldVal = $(item).attr("oldVal");
     	var newVal = data;
     	if(oldVal === undefined){
     		oldVal = 0;				
     	}
     	if(newVal === '') {
     		newVal = 0;
     	} else {
     		newVal = bt.money.toNumber(newVal);
     	}
     	//获取发生额，计算合计值
 		var alisaname = $(item).attr("alisaname");
     	if(undefined != alisaname){
     		alisaname = alisaname.split("_");
     	}
     	var mindex;
     	if(alisaname.length == 2){
 	    	mindex = alisaname[1];
     	} else {
 	    	mindex = $(item).attr("index");
     	}
     	var name = alisaname[0];
     	
     	if(name.indexOf("debitMoney") != -1){//借方金额
     		var data_name = $("input[name='bookDtls[" + mindex + "].creditMoney']");
     		//判断贷方是否已录入金额
     		var creditMoneyi = layui.jquery(data_name).val();
     		creditMoneyi = bt.money.toNumber(creditMoneyi);
     		if(null == creditMoneyi || creditMoneyi - 0 == 0){
     		} else {
     			$("input[data-id='debitMoney_"+mindex+"']").val("0");
     			$("input[id='debitMoney_"+mindex+"']").val("0.00");
     			$(item).val("0.00");
     			return;
     		}
     		$("input[name='bookDtls[" + mindex + "].debitMoney']").val(data);
     		$("#debitMoney_" + mindex).val(data);
     		//计算合计值
     		setTot(item, data, "debitTot");
     	} else if(name.indexOf("creditMoney") != -1){//贷方金额
     		var data_name = $("input[name='bookDtls[" + mindex + "].debitMoney']");
     		//判断借方是否已录入金额
     		var creditMoneyi = layui.jquery(data_name).val();
     		creditMoneyi = bt.money.toNumber(creditMoneyi);
     		if(null == creditMoneyi || creditMoneyi - 0 == 0){
     		} else {
     			$("input[data-id='creditMoney_"+mindex+"']").val("0");
     			$("input[id='creditMoney_"+mindex+"']").val("0.00");
     			$(item).val("0.00");
     			return;
     		}
     		$("input[name='bookDtls[" + mindex + "].creditMoney']").val(data);
     		$("#creditMoney_" + mindex).val(data);
     		//计算合计值
     		setTot(item, data, "creditTot");
     	}
     	
     	if(mindex - 0 == 0){//第一行
     		if(name.indexOf("debitMoney") != -1){//借方金额
     			var data_name = "[alisaname='creditMoney_1']";
         		//判断贷方是否已录入金额
         		var creditMoney0 = layui.jquery(data_name).val();
         		creditMoney0 = bt.money.toNumber(creditMoney0);
				var eItem = $(data_name);
				eItem.val(bt.money.format(data));
				eItem.attr("data-value", bt.money.format(data));
				var creditMoney1 = $("input[name='bookDtls[1].creditMoney']");
				if(creditMoney1 && $("#creditMoney_1")){
					$("input[name='bookDtls[1].creditMoney']").val(data);
					$("#creditMoney_1").val(data);
					if(creditMoney1.val()){
						//计算合计值
						setTot(eItem, data, "creditTot");
					}
				}
     		} else if(name.indexOf("creditMoney") != -1){//贷方金额
     			var data_name = "[alisaname='debitMoney_1']";
         		//判断贷方是否已录入金额
         		var debitMoney0 = layui.jquery(data_name).val();
         		debitMoney0 = bt.money.toNumber(debitMoney0);
				var eItem = $(data_name);
				eItem.val(bt.money.format(data));
				eItem.attr("data-value", bt.money.format(data));
				var debitMoney1 = $("input[name='bookDtls[1].debitMoney']");
				if(debitMoney1 && $("#debitMoney_1").val()){
					$("input[name='bookDtls[1].debitMoney']").val(data);
					$("#debitMoney_1").val(data);
					if(debitMoney1.val()){
						//计算合计值
						setTot(eItem, data, "debitTot");
					}
				}
     		}
			/**
			 * 设置现金流项目
			 */
			setCashFlowItem(1);
     	}

		/**
		 * 设置现金流项目
		 */
		setCashFlowItem(mindex);
    };


    initPrintMRTable();
    bt.dateType.init();

	var multiIframes = $('#multiIframes').data('flag');//多级iframe
	bt.bankAccDomestic.init({multiIframes:multiIframes});
	bt.bankAccAbroad.init({multiIframes:multiIframes});
	bt.swiftInfo.init({multiIframes:multiIframes});
	var bankInfo = bt.bankInfo.init({multiIframes:multiIframes});
	bt.bankInfoBeforeClick = function () {
		/*bankInfo.set({
			"joinBankType": true,
		});*/
		return true;
	}

	//客商选择-境内
	var externalCorpBankAcc = bt.externalCorpBankAcc.init({multiIframes:multiIframes});
	bt.externalCorpBankAccParams = function () {
		externalCorpBankAcc.set({
			type: "0"
		});
		return true;
	}

	//客商选择-境外
	var externalCorpBankAccFex = bt.externalCorpBankAccFex.init({multiIframes:multiIframes});
	bt.externalCorpBankAccFexParams = function () {
		externalCorpBankAccFex.set({
			type: "0"
		});
		return true;
	}

	//账号选择回调
	bt.externalCorpCallback = function (data, dom) {
		// 选择客商信息
		$("#accName").val(data.name);
		$("#acc").val(data.externalAcc);
		$("#accBank").val(data.bank);
	};

	//类型-单位/银行
	bt.swiftInfoCallback = function (data, dom) {//境外
		$("#accName").val(data.bankName);
	};

	bt.bankinfoCallback = function (data, dom) {//境内
		$("#accName").val(data.bankName);
	}

	//类型-客商为否
	bt.swiftTypeCallback = function (data, dom) {//境外
		$("#accBank").val(data.bankName);
		$("#bankTypeAcc").val(data.swiftCode);
		$("#externalProv").val(data.nameZh);//国家、地区
		$("#externalCity").val(data.cityName);//市
	};

	bt.bankTypeCallback = function (data, dom) {//境内
		$("#accBank").val(data.bankName);
		$("#bankTypeAcc").val(data.bankCode);
		$("#externalProv").val(data.prov);
		$("#externalCity").val(data.city);
	}

	bt.bankAccCallback = function (data, dom) {//银行账号境内
		$("#accName").val(data.corpName);
		$("#acc").val(data.bankAcc);
		$("#accBank").val(data.bankName);
	}

	externalInit();
});

function otherInfoChange(data) {
	formatValue();
	$("#acc").attr("readonly","readonly");
	$("#accName").attr("readonly","readonly");
	$("#accBank").attr("readonly","readonly");
	var externalSign = $("#externalSign").val();
	var val = data.value;
	if(val == "1"){//客商
		$(".corp").hide();
		$(".bank").hide();
		if(externalSign == 1){//白名单
			$(".ke").show();
		}else {
			$(".keBankType").show();
			$("#accName").removeAttr("readonly");
			$("#acc").removeAttr("readonly");
		}
	}else if(val == "2"){//网点/单位
		$(".corp").show();
		$(".bank").hide();
		$(".ke").hide();
		$(".keBankType").hide();
	}else {//银行
		$(".bank").show();
		$(".ke").hide();
		$(".corp").hide();
		$(".keBankType").hide();
		$("#acc").removeAttr("readonly");
		$("#accBank").removeAttr("readonly");
	}
	var dict = bt.dicts.init();
	dict.get("typeSign").val("1");
	isDomesticBankChange({value:"1"});
}

function isDomesticBankChange(data){
	formatValue();
	var val = data.value;
	var dict = bt.dicts.init();
	var otherType = dict.get("externalType").val();
	var externalSign = $("#externalSign").val();
	if(otherType == "1"){//客商
		if(val == "1"){//境内
			if(externalSign == 1) {
				$("#domestic").show();
				$("#abroad").hide();
			}else {
				$(".isSwiftNType").show();
				$(".isSwiftYType").hide();
			}
		}else {
			if(externalSign == 1) {
				$("#domestic").hide();
				$("#abroad").show();
			}else {
				$(".isSwiftNType").hide();
				$(".isSwiftYType").show();
			}
		}
	}else if(otherType == "2"){//网点/单位
		if(val == "1"){//境内
			$(".bankDomestic").show();
			$(".bankAbroad").hide();
		}else {
			$(".bankDomestic").hide();
			$(".bankAbroad").show();
		}
	}else {//银行
		if(val == "1"){//境内
			$(".isSwiftN").show();
			$(".isSwiftY").hide();
		}else {
			$(".isSwiftN").hide();
			$(".isSwiftY").show();
		}
	}
}

function formatValue(){
	$("#accName").val("");
	$("#acc").val("");
	$("#accBank").val("");
}

function externalInit(){
	var externalSign = $("#externalSign").val();//白名单参数
	if(externalSign == 0){
		$("#accName").removeAttr("readonly");
		$("#acc").removeAttr("readonly");
		$(".keBankType").show();
		$(".ke").hide();
	}
}

/**
 * 设置现金流项目
 * @param rowMR
 */
function setCashFlowItem(rowMR) {
	var fcAcc = $("#account" + rowMR).val();
	var debitMoney = $("#debitMoney_" + rowMR).val();
	var creditMoney = $("#creditMoney_" + rowMR).val();
	var jsonData = {};
	jsonData.fcAcc = fcAcc;
	if (Number(debitMoney) > 0 || Number(debitMoney) < 0) {
		jsonData.debit = "1";
	}else if (Number(creditMoney) > 0 || Number(creditMoney) < 0){
		jsonData.credit = "1";
	}
	if (isNotEmpty(fcAcc) && (isNotEmpty(jsonData.debit) || isNotEmpty(jsonData.credit))) {
		$.ajax({
			url: basePath + "/finacial/paramconfig/voucherCash/getFcVoucherCash",
			type: "POST",
			data: jsonData,
			dataType: "json",
			success: function (data) {
				var html = "";
				if (isNotEmpty(data) && data.success) {
					if (isNotEmpty(data.obj)) {
						data = data.obj;
						if (Number(debitMoney) > 0 || Number(debitMoney) < 0) {
							html += '<input type="hidden" value="' + data.debitItem + '" name="bookDtls[' + rowMR + '].cashFlux">';
							html += data.debitItemName;
						} else {
							html += '<input type="hidden" value="' + data.creditItem + '" name="bookDtls[' + rowMR + '].cashFlux">';
							html += data.creditItemName;
						}
					}
				}
				$("#cashFlowItem" + rowMR).html(html);
			}
		});
	}
}

/**
 * 判断未空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
	if (null == value || value == undefined || value == "" || value == " ") {
		return true;
	} else {
		return false;
	}
}

/**
 *判断非空
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
	return !isEmpty(value);
}


