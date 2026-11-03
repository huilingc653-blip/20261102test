var rowMR=0;
var comprowMR=0;
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var corpInit;
var selectTree;
bt.use(['form','layedit','btdate','bttable','moneynum','dicts','select','moneyType','dateType','corp', 'selectType','money','bankInfo'], function(){
    var $ = bt.jquery,form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    //检查合同号是否重复
    $("#contractNo").blur(function(){
    	var contractNo = $(this).val();
    	if(bt.empty(contractNo)){
    		return;
    	}
    	$.post(basePath + "/cms/rat/ratbkration//checkContractNo", {
    		contractNo : contractNo,
    		ratCode : $("#id").val()
		}, function(data) {
			if(data && "0" == data){
				layer.alert(bt.lang("table.ratbkration.contractNoRepeat"));
				$("#contractNo").val("");
			}
		}, 'JSON');
    });	
    
    //授信模式修改变更单位树
    bt.changeCorp = function(data){
    	var ratMode;
    	if(bt.empty(data)){
    		ratMode = $("#ratModeTemp").val();
    	} else {
    		ratMode = data.value;
    	}
    	corpInit = bt.corp.init();
    	bt.changeRatCorp(ratMode);
    	/*if("1" == ratMode){
    		corpInit.get("corpId").setUrl("all");
    	} else {
    		corpInit.get("corpId").setUrl("rat");
    	}*/
    	//corpInit.get("corpId").render();
    }
    
    //修改授信单位选择项
    bt.changeRatCorp = function(ratMode) {
    	//重置可用余额
    	var className = "";
    	var checktype = "";
    	var html = "";
    	className = "selectTreeDemo";
    	checktype = "radio";
    	html = '<div class="layui-input-inline"><div class="'+className+'" data-width="300px;"></div></div>';
    	$("#ratCorpDiv").html(html);
		//下拉树
		$.each($("." + className),function(i,item){
			selectTree = bt.selectTree.initGetCurrent({
				url: basePath + '/cms/rat/ratbkration/listCorp?ratModel=' + ratMode,
				elem:".selectTreeDemo",
				name: "corpId",
				selected: $("#corpIdTemp").val(),
				checkType:checktype,
				setting:{//该setting为ztree的setting，可参考http://www.treejs.cn/v3/api.php的API
	                data:{
	                    simpleData: {
	                    	pIdKey: "parentId"
	                    }
	                },
	                check : {
	                	chkboxType: { "Y": "s", "N": "s" }
	                }
	            },
	            beforeInit:function(nodes){
	                for(k in nodes){
	                    nodes[k].name = "(" + nodes[k].code + ")" + nodes[k].name;
	                }   
	                return nodes;
	            },
	            initComplete : function(){
	            	
	            }
			});
		});
    	form.render("select");
    }
    
    bt.setBankCode = function(data){
    	var bankTypeVal = "";
    	if(data && data != undefined){
    		bankTypeVal = data;
    	} else {
    		bankTypeVal = layui.jquery("#bankType").find("option:selected").val();
    	}
    	var typeid = bt.jquery.trim(bankTypeVal);
    	if(!bt.empty(typeid)){
    		bt.jquery("[data-name='bankCode']").data("url",basePath+"/cms/provide/loaninfo/queryBtBankByType/"+typeid);
    	}else{
    		bt.jquery("[data-name='bankCode']").data("url",basePath+"/cms/provide/loaninfo/queryBtBankByType/null");
    	}
    	var bank = selectTypes.get("bankCode").reset();
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
    		$("#"+rowIndex + "itemCode").attr("data-itemdisabled", disItem);
    		form.render("select");
    	}
    }
    //删除品种行重新设置所有行该品种可选择
    bt.resetDelItem = function(rowIndex){
    	var itemCode = selectTypes.get("rationDtls[" + rowIndex + "].itemCode").val();
    	itemCode = bt.jquery.trim(itemCode);
    	if(itemCode.length == 0){
    		return;
    	}
        var doms = $('[data-link="true"]');
        bt.each(doms,function(){
            var othat = $(this);
            othat.find('select option[value="'+itemCode+'"]').prop("disabled",false);
        })
        form.render("select");
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
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calIndexOpenMoney(rowIndex);
    	
    	bt.calSumMoney();
    }
    //综合授信设置
    bt.calCompMoney = function(data, el){
    	//设置综合授信额度一致
    	$.each($("input[data-id$='maxMoney']"), function(i,item){
    		var maxIndex = $(item).attr("rowindex");
    		moneyType.get(maxIndex+"maxMoney").val(data);
        });
    	
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calIndexOpenMoney(rowIndex);
    	bt.calSumMoney();
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
    //根据授信额度和保证金比例费率计算敞口额度
    bt.calIndexOpenMoney = function(rowIndex) {
    	var itemFlag = $("#itemFlag"+rowIndex).val();
    	var moneyIndexVal = moneyType.get(rowIndex+"money").val();
    	if("0" == itemFlag){
    		var assurepercent = $("#assurepercent"+rowIndex).val();
    		//授信额度*（1-保证金比例/100）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(assurepercent, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	} else {
    		var interestrate = $("#interestrate"+rowIndex).val();
    		//授信额度*（1-利率/费率）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(interestrate, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	}
    	bt.calSumOpenMoney();
    }
    //新增单品种事件
    $("#addSingleTr").click(function(){
    	rowMR++;
    	singrowMR++;
    	if(singrowMR - copmRatSize > 0){
			return false;
		}
		var html='<tr>';
		html +='<td>'+bt.dict("RatModel.ItemFlag", '0')+'</td>';
		html += '<td><input id="itemFlag'+rowMR+'" name="rationDtls['+rowMR+'].itemFlag" type="hidden" value="0">'
			+ '<input name="rationDtls['+rowMR+'].itemOrder" type="hidden" value="' + rowMR + '">'
			+ '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls['+rowMR+'].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
			+ 'data-valuefield="id" data-textfield="name" id="'+rowMR+'itemCode" data-name="rationDtls['+rowMR+'].itemCode" data-search="false" data-link="true" data-callback="setItemCode"></div></div></td>';
		html+='<td><input type="text" data-name="rationDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calMoney"></td>';
		html+='<td><input type="text" data-name="rationDtls['+rowMR+'].openMoney" id="'+rowMR+'openMoney" data-biz-name="'+rowMR+'openMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" readonly="readonly"></td>';
		html+='<td>'
			+ '<input type="hidden" id="assurecondition'+rowMR+'" name="rationDtls['+rowMR+'].assurecondition" >'
			+ '<input type="hidden" id="assurepercent'+rowMR+'" name="rationDtls['+rowMR+'].assurepercent" >'
			+ '<input type="hidden" id="interestrate'+rowMR+'" name="rationDtls['+rowMR+'].interestrate" >'
			+ '</td>';
		html+='<td><a class="" href="#" data-row="'+rowMR+'" onclick="openAssure(this, '+rowMR+')">'+bt.lang("common.ration.openMoney")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
			+ '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
			+ '</td>';
		html+='</tr>';
		if(rowMR - 1 > 0 && $("#singleTable tbody tr:last").length > 0){
			$("#singleTable tbody tr:last").after(html);
		} else {
			$("#singleTable tbody").append(html);
		}
		initSelectCode(rowMR);
		bt.dicts.init();
		return false;
    });
    //新增综合品种事件
    $("#addCompTr").click(function(){
    	if(comprowMR - copmRatSize >= 0){
    		return false;
    	}
    	rowMR++;
    	comprowMR++;
		var html='<tr>';
		var cols = "";
		var buttonId = "";
		var colFlag = true;
		var compMoney = "";
		
		if(comprowMR - 1 > 0 && $("#compTable tbody tr:last").length > 0){
			colFlag = false;
			compMoney = moneyType.get(compRowIndex+"money").val();
		} else {
			cols = "rowspan='" + copmRatSize + "'";
			buttonId = " id='compButton' ";
			compRowIndex = rowMR;//记录综合品种列Index
		}
		if(colFlag){
			html+='<td ' + cols + '>'+bt.dict("RatModel.ItemFlag", '1')+'</td>';
		}

		html += '<td><input id="itemFlag'+rowMR+'" name="rationDtls['+rowMR+'].itemFlag" type="hidden" value="1">'
			+ '<input name="rationDtls['+rowMR+'].itemOrder" type="hidden" value="' + rowMR + '">'
			+ '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls['+rowMR+'].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
			+ 'data-valuefield="id" data-textfield="name" id="'+rowMR+'itemCode" data-name="rationDtls['+rowMR+'].itemCode" data-search="false" data-link="true" data-callback="setItemCode"></div></div></td>';
		if(colFlag){
			html+='<td ' + cols + '><input type="text" data-name="rationDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calCompMoney"></td>';
			html+='<td ' + cols + '><input type="text" data-name="rationDtls['+rowMR+'].openMoney" id="'+rowMR+'openMoney" data-biz-name="'+rowMR+'openMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" readonly="readonly"></td>';
		}
		html+='<td><input type="text" data-name="rationDtls['+rowMR+'].maxMoney" id="'+rowMR+'maxMoney" data-biz-name="'+rowMR+'maxMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" rowindex="'+rowMR+'" value="'+compMoney+'" data-callback="bt.checkMaxMoney">';
		html+='<input type="hidden" id="assurecondition'+rowMR+'" name="rationDtls['+rowMR+'].assurecondition" >'
			+ '<input type="hidden" id="assurepercent'+rowMR+'" name="rationDtls['+rowMR+'].assurepercent" >'
			+ '<input type="hidden" id="interestrate'+rowMR+'" name="rationDtls['+rowMR+'].interestrate" >'
			+ '</td>';
		html+='<td>';
		if(colFlag){
			html+='<a class="" href="#" data-row="'+rowMR+'" onclick="openAssure(this, '+rowMR+')">'+bt.lang("common.ration.openMoney")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t';
		}
		html += '<button data-type="del" ' + buttonId + '" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
			+ '</td>';
		html+='</tr>';
		
		if(rowMR - 1 > 0 && $("#compTable tbody tr:last").length > 0){
			$("#compTable tbody tr:last").after(html);
		} else {
			$("#compTable tbody").append(html);
		}
		if(!colFlag){
			$("#compButton").hide();
		}
		
		initSelectCode(rowMR);
		bt.dicts.init();
		return false;
    });
    
    bt.setGuaInfo = function(data){
    	if("1" == data.value){
    		$("#guaTr").show(100);
    	} else {
    		$("#guaTr").hide(100);
    	}
    }
    
    //初始化单品种table
    var initRationTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:false,
	        scrollY:false,
	    	elem: "#singleTable",
	    	url: basePath+"/cms/rat/ratbkration/initRationDtl?ratCode="+$("#id").val()+"&itemFlag=0&opType=edit",
	    	buttons: [
	    	          ],
	    	columns:[
	               {
	            	   data:"itemFlag",
	            	   render:function(data, row){
	            		   var reInfo = bt.dict("RatModel.ItemFlag", '0');
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"itemCode",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo;
	            		   reInfo = '<input id="itemFlag'+rowIndex+'" name="rationDtls['+rowIndex+'].itemFlag" type="hidden" value="0">'
			           			+ '<input name="rationDtls['+rowIndex+'].itemOrder" type="hidden" value="' + rowIndex + '">'
			           			+ '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls['+rowIndex+'].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
			           			+ 'data-valuefield="id" data-textfield="name" id="'+rowIndex+'itemCode" data-name="rationDtls['+rowIndex+'].itemCode" data-defaultvalue="' 
			           			+ data.itemCode + '" data-itemdisabled="' + data.disItemCodes + '" data-search="false" data-link="true" data-callback="setItemCode"></div></div>';
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"money",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo = '<input type="text" data-name="rationDtls['+rowIndex+'].money" id="'+rowIndex+'money" data-biz-name="'
		            		   	+ rowIndex + 'money" class="layui-input bt-biz-moneyType" value="' + data.money 
		            		   	+ '" data-decimal="2" data-minus="false" data-callback="bt.calMoney">';
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"openMoney",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo = '<input type="text" data-name="rationDtls['+rowIndex+'].openMoney" id="'+rowIndex+'openMoney" data-biz-name="'
		            		   	+ rowIndex + 'openMoney" class="layui-input bt-biz-moneyType" value="' + data.openMoney 
		            		   	+ '" data-decimal="2" data-minus="false" readonly="readonly" >';
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"maxMoney",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var assurepercent = data.assurepercent;
	            		   var interestrate = data.interestrate;
	            		   var assurecondition = data.assurecondition;
	            		   if(assurecondition == null || assurecondition == undefined){
	            			   assurecondition= "";
	            		   }
	            		   if(assurepercent == null || assurepercent == undefined){
	            			   assurepercent= "";
	            		   }
	            		   if(interestrate == null || interestrate == undefined){
	            			   interestrate= "";
	            		   }
	            		   var reInfo = '<input type="hidden" id="assurecondition'+rowIndex+'" name="rationDtls['+rowIndex+'].assurecondition" value="' + assurecondition + '">'
			           			+ '<input type="hidden" id="assurepercent' + rowIndex +'" name="rationDtls['+rowIndex+'].assurepercent" value="' + assurepercent + '">'
			           			+ '<input type="hidden" id="interestrate' + rowIndex + '" name="rationDtls['+rowIndex+'].interestrate" value="' + interestrate + '">';
	            		   return reInfo;
	            	   }
	               },
	               //操作
	               {data:null,render:function(data,row){
	            	   var rowIndex = data.itemOrder;
	            	   var reInfo='<a class="" href="#" data-row="'+rowIndex+'" onclick="openAssure(this, '+rowIndex+')">'+bt.lang("common.ration.openMoney")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
	       					+ '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
	                   return reInfo;            
	               }}
		       ],            
		       delRowCallback:function(data,$tr){
		    	   var rowIndex = $tr.find("input[name$='itemOrder']").val();
		    	   bt.resetDelItem(rowIndex);
		    	   //删除后更新其他行的授信品种选择
		           $tr.remove();
		           bt.calSumMoney();
		           bt.calSumOpenMoney();
		           singrowMR--;
		       },            
		       tableInitCallback:function(){  
		    	   initSelectCode();
		    	   bt.dicts.init();
	           }
	    });
	    rowMR = $("#itemSize").val();
	    singrowMR = $("#singItemSize").val();
	    comprowMR = $("#compItemSize").val();
	    compRowIndex = $("#compRowIndex").val();
    }
    
    //初始化综合品种table
    var initCompRationTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:false,
	        scrollY:false,
	    	elem: "#compTable",
	    	//url: basePath+"/cms/rat/ratbkration/initRationDtl?ratCode=&opType=add",
	    	//url: basePath+"/cms/rat/ratbkration/initRationDtl?ratCode="+$("#id").val()+"&itemFlag=1&opType=edit",
	    	buttons: [
	    	          ],
	        columns:[
	               {
	            	   data:"itemFlag",
	            	   render:function(data, row){
	            		   var reInfo = bt.dict("RatModel.ItemFlag", '1');
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"itemCode",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo;
	            		   reInfo = '<input id="itemFlag'+rowIndex+'" name="rationDtls['+rowIndex+'].itemFlag" type="hidden" value="1">'
			           			+ '<input name="rationDtls['+rowIndex+'].itemOrder" type="hidden" value="' + rowIndex + '">'
			           			+ '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls['+rowIndex+'].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
			           			+ 'data-valuefield="id" data-textfield="name" id="'+rowIndex+'itemCode" data-name="rationDtls['+rowIndex+'].itemCode" data-defaultvalue="' 
			           			+ data.itemCode + '" data-itemdisabled="' + data.disItemCodes + '" data-search="false" data-link="true" data-callback="setItemCode"></div></div>';
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"money",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo = '<input type="text" data-name="rationDtls['+rowIndex+'].money" id="'+rowIndex+'money" data-biz-name="'
		            		   	+ rowIndex + 'money" class="layui-input bt-biz-moneyType" value="' + data.money 
		            		   	+ '" data-decimal="2" data-minus="false" data-callback="bt.calMoney">';
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"openMoney",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo = '<input type="text" data-name="rationDtls['+rowIndex+'].openMoney" id="'+rowIndex+'openMoney" data-biz-name="'
		            		   	+ rowIndex + 'openMoney" class="layui-input bt-biz-moneyType" value="' + data.openMoney 
		            		   	+ '" data-decimal="2" data-minus="false" readonly="readonly" >';
	            		   return reInfo;
	            	   }
	               },
	               {
	            	   data:"maxMoney",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var reInfo = '<input type="hidden" id="assurecondition'+rowIndex+'" name="rationDtls['+rowIndex+'].assurecondition" >'
			           			+ '<input type="hidden" id="assurepercent' + rowIndex +'" name="rationDtls['+rowIndex+'].assurepercent" >'
			           			+ '<input type="hidden" id="interestrate' + rowIndex + '" name="rationDtls['+rowIndex+'].interestrate" >';
	            		   return reInfo;
	            	   }
	               },
	               //操作
	               {data:null,render:function(data,row){
	            	   var rowIndex = data.itemOrder;
	            	   var reInfo='<a class="" href="#" data-row="'+rowIndex+'" onclick="openAssure(this, '+rowIndex+')">'+bt.lang("common.ration.openMoney")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
	       					+ '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
	                   return reInfo;            
	               }}
		        ],
	    	delRowCallback:function(data,$tr){
	    		var rowIndex = $tr.find("input[name$='itemOrder']").val();
	    		bt.resetDelItem(rowIndex);
	    		//删除后更新其他行的授信品种选择
	    		$tr.remove();
	    		//金额合计减少
	    		bt.calSumMoney();
	    		bt.calSumOpenMoney();
	    		comprowMR--;
	    		
	    		if($("#compTable tbody tr").length == 1){
	    			$("#compButton").show();
	    		}
	    		
	        }, 
	        tableInitCallback:function(){  
	        	initSelectCode();
		    	bt.dicts.init();
	        }
	    });
	    //comprowMR = 0;
	    //rowMR = 0;
	}
    
    var initSelectCode=function(row){
    	bt.dicts.init();
    	bt.moneyType.init();
    	//设置新增的品种不可选已勾选的品种
    	bt.setNewItemDisable(row);
    	bt.selectType.init();
    }
    
    initRationTable();
    initCompRationTable();
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    //初始化单位
    corpInit = bt.corp.init();
    //bt.changeCorp();
    bt.bankInfo.init();//联行号
    
    //下拉框
    $.each($("div[field='curCode']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    
    //下拉框
    $.each($("div[field='bankType']"),function(i,item){
    	bt.select.init({
			elem: "[field='"+$(item).attr("field")+"']",
			name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
			search: true,
			required: true,
			url:basePath+"/basic/business/btbanktype/json",
			change:function(data,text){
				bt.setBankCode(data.value);
	        }
		});
    });

    //检查每项授信品种是否有输入额度
    var checkItemMoney = function () {
        var success = true;
        $.each($("input[id$='money']"), function(i,item){
            if (bt.empty($(item).val())) {
                success = false;
                return false;
            }
        });
        return success;
    };
    
    var checkBefore = function(){
    	var corpId = selectTree.val();
    	if(bt.empty(corpId)){
    		layer.alert(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
    		return false;
    	}
    	var bankId = $("#bankCode").val();//银行
    	if(bt.empty(bankId)){
    		//选择银行
    		layer.alert(bt.lang("cms.provide.loaninfo.bankIdnull"), {icon: 2});
    		return false;
    	}
    	if(singrowMR == 0 && comprowMR == 0){
    		layer.alert(bt.lang("table.ratbkrationdetail.customItemOne"));
    		return false;
    	}
    	
    	var moneySum = moneyType.get("moneysum").val();
    	if(moneySum == 0){
    		layer.alert(bt.lang("table.ratbkrationdetail.itemmoneynull"));
    		return false;
    	}
    	
    	//综合品种要大于等于2条
    	if(comprowMR - 1 == 0){
    		layer.alert(bt.lang("table.ratbkrationdetail.compItemOne"));
    		return false;
    	}
        if (!checkItemMoney()){
            layer.alert(bt.lang("table.ratbkrationdetail.itemmoneynull"));
            return false;
        }
    	return true;
    }
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	//综合品种要大于等于2条
    	if(comprowMR - 1 == 0){
    		bt.loading.hide(); //关闭加载中
    		layer.alert(bt.lang("table.ratbkrationdetail.compItemOne"));
    		return false;
    	}
    	
        var action = "/cms/rat/ratbkration/editSave";
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
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	//综合品种要大于等于2条
    	if(comprowMR - 1 == 0){
    		bt.loading.hide(); //关闭加载中
    		layer.alert(bt.lang("table.ratbkrationdetail.compItemOne"));
    		return false;
    	}
    	
    	var action = "/cms/rat/ratbkration/editSubmit";
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
    
    //bt.setBankCode(bt.jquery("#bankTypeOri").val());
});

function setItemCode(data,elem,bv){
    bt.setItemCode(data,elem,bv)
}

/**根据银行类型获取银行支行信息*/
function loadBtBankByType(data){
	bt.setBankCode(data);
	var bankTypeVal = selectTypes.get("bankType").val();
	var typeid = bt.jquery.trim(bankTypeVal);
	if(!bt.empty(typeid)){
		bt.jquery("[data-name='bankCode']").data("url",basePath+"/cms/provide/loaninfo/queryBtBankByType/"+typeid);
	}else{
		bt.jquery("[data-name='bankCode']").data("url",basePath+"/cms/provide/loaninfo/queryBtBankByType/null");
	}
	var bank = selectTypes.get("bankCode").reset();
}


/**
 * 计算敞口金额
 */
function getAssure(rowIndex, assurecondition, assurepercent, interestrate){
	bt.jquery("#assurecondition"+rowIndex).val(assurecondition);
	bt.jquery("#assurepercent"+rowIndex).val(assurepercent);
	bt.jquery("#interestrate"+rowIndex).val(interestrate);
	
	bt.calIndexOpenMoney(rowIndex);
}

//后续设置成div设置保证金比例
function openAssure(el, rowIndex) {
	layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkration/setAssure?rowIndex=" + rowIndex + "&assurecondition="+bt.jquery("#assurecondition"+rowIndex).val()
        	+ "&assurepercent="+bt.jquery("#assurepercent"+rowIndex).val() + "&interestrate="+bt.jquery("#interestrate"+rowIndex).val(),
        end : function(index){
        },
        success : function(){
        	
        }
    });
}

//银行账号选择
function bankinfoCallback(data,dom){
	$("#shouBankName").val(data.bankName);
	$("#bankCode").val(data.bankCode);//联行号
	$("#bankType").val(data.bankTypeId);//银行类别
}
