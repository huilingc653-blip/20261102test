var rowMR=0;
var comprowMR=0;
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var corpInit;
var selectTree;
var parameters;
var corpId;
var moneyTypes;
var form;
var ratioType;
var dateTypes;
var dicts;
var isHangBail;
var isGuarantee;
var creditContractControl;
bt.use(['form','layedit','btdate','bttable','moneynum','dicts','select','moneyType','dateType','corp', 'selectType','money','bankInfo','bondAccInfo','rateType','ratioType','btUpload','conSelect','conView'], function(){
    var $ = bt.jquery;
    form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    bondAccInit = bt.bondAccInfo.init();//保证金账号的初始化
    rateTypes = bt.rateType.init();//利率
    fun = bt.moneyType.getFun();
    var moneyFun = bt.moneyType.getFun();
    moneyTypes = bt.moneyType.init();//金额
    ratioType= bt.ratioType.init();
    dateTypes=bt.dateType.init();
    parameters = {};

    isHangBail=$("#isHangBail").val();//获取参数设置是否挂接保证金
    isGuarantee=$("#isGuarantee").val();//获取参数设置是否挂接担保
	creditContractControl = $("#creditContractControl").val();<!-- 授信合同是否必须控制-->

	/**判断字符串是否为空*/
	bt.empty = function(ms){
		if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
			return true;
		}
		return false;
	}

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem : "#uploader",
        isExpand : true,
        auto : false,
        gid : gid
    });

	// 合同附件上传
	var contractGid = $("#contractAnnexName").val();
	var contractUp = bt.btUpload.init({
		elem: "#contractUploader",
		isExpand: true,
		auto: false,
		gid: contractGid
	});

	var creditContractRequired = $("#creditContractRequired").val()
	if (!bt.empty(creditContractRequired) && creditContractRequired === '1') {
		$("#contractAttachSpan").show();
	} else {
		$("#contractAttachSpan").hide();
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


	var conSelect = bt.conSelect.init();
	var conView = bt.conView.init();

	bt.conSelectParams=function(){
		var corpId = corpInit.get("").val();
		return true;
	}

	bt.conViewParams=function(){
		var conId = $("#conId").val();
		conView.set({"id": conId});
		return true;
	}
	bt.conSelectCallback = function(datas,dom){
		var data = datas.data;
		if (null != data && data != undefined && data != "") {
			$("#conId").val(datas.id);
			$("#contractNo").val(datas.conNo);
			//datas.id 合同ID
			//datas.conName 合同名称
			//datas.conNo 合同代码
		}
	}

    dicts = bt.dicts.init();//字典
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
				clickCallBack:bt.corpCallback,////选中单位后的回调函数，用于挂接包证金的时候，保证金账号更加单位进行筛选
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
	            }
			});
		});
    	form.render("select");
    }


    //页面加载后判断是否挂接了保证金,没有则隐藏填写的保证金信息
    var isHook = $("#isHookBond").val();
    if(isHangBail=="1"){
        if(isHook=="0"||isHook==""){
            $(".isShow").hide();
            $("#applyMoney").removeAttr('lay-verify');
            $("#bankAcc").removeAttr('lay-verify');
            dicts.get("isHookBond").attr({ 'data-defaultvalue':0});
            form.renderIcon();
        }else{
            $(".isShow").show();
            $("#applyMoney").attr('lay-verify','required')
            $("#bankAcc").attr('lay-verify','required')
            dicts.get("isHookBond").attr({ 'data-defaultvalue':1});
            form.renderIcon();
        }
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
    		moneyType.get(moneyIndexVal+"maxMoney").val(0);
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
    	bt.calSumMoney();
    }

    //综合授信设置
    bt.calCompMoney = function(data){
    	//设置综合授信额度一致
    	$.each($("input[data-id$='maxMoney']"), function(i,item){
    		var maxIndex = $(item).attr("rowindex");
    		moneyType.get(maxIndex+"maxMoney").val(data);
        });

    	bt.calSumMoney();
    }

    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='money']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("moneysum").val(bt.money.format(sumMoney));

    	if(isHangBail=="1"){
            //计算保证金
            setBailMoney();
		}
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
			+ 'data-valuefield="id" data-textfield="name" id="'+rowMR+'itemCode" data-name="rationDtls['+rowMR+'].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';
        html += '<td>'//额度类型
            + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-group="rat.creditType" data-defaultvalue="1" data-selecttext="false" data-biz-name="rationDtls[' + rowMR + '].creditType"  '
            + 'data-name="rationDtls[' + rowMR + '].creditType"></div></div></td>';
		html+='<td><input type="text" data-name="rationDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calMoney"></td>';
        html+='<input type="hidden" id="assurepercent'+rowMR+'" name="rationDtls['+rowMR+'].assurepercent" >'
            + '<input type="hidden" id="procedureFeeRate'+rowMR+'" name="rationDtls['+rowMR+'].procedureFeeRate" >'
            + '<input type="hidden" id="bondMoney'+rowMR+'" name="rationDtls['+rowMR+'].bondMoney" >'
            + '<input type="hidden" id="procedureFee'+rowMR+'" name="rationDtls['+rowMR+'].procedureFee" >'
        html+='<td></td>';
        html += '<td><a class="" href="#" data-row="'+rowMR+'" onclick="openAssure(this, '+rowMR+')">'+bt.lang("common.ration.limit.set")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
			+'<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
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
			+ 'data-valuefield="id" data-textfield="name" id="'+rowMR+'itemCode" data-name="rationDtls['+rowMR+'].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';
        html += '<td>'//额度类型
            + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-group="rat.creditType" data-defaultvalue="1" data-selecttext="false" data-biz-name="rationDtls[' + rowMR + '].creditType"  '
            + 'data-name="rationDtls[' + rowMR + '].creditType"></div></div></td>';
		if(colFlag){
			html+='<td ' + cols + '><input type="text" data-name="rationDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calCompMoney"></td>';
		}
		html+='<td><input type="text" data-name="rationDtls['+rowMR+'].maxMoney" id="'+rowMR+'maxMoney" data-biz-name="'+rowMR+'maxMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" rowindex="'+rowMR+'" value="'+compMoney+'" data-callback="bt.checkMaxMoney">';
        html+='<input type="hidden" id="assurepercent'+rowMR+'" name="rationDtls['+rowMR+'].assurepercent" >'
            + '<input type="hidden" id="procedureFeeRate'+rowMR+'" name="rationDtls['+rowMR+'].procedureFeeRate" >'
            + '<input type="hidden" id="bondMoney'+rowMR+'" name="rationDtls['+rowMR+'].bondMoney" >'
            + '<input type="hidden" id="procedureFee'+rowMR+'" name="rationDtls['+rowMR+'].procedureFee" >'
		html+='</td>';
        html += '<td><a class="" href="#" data-row="'+rowMR+'" onclick="openAssure(this, '+rowMR+')">'+bt.lang("common.ration.limit.set")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
			+'<button data-type="del" ' + buttonId + '" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
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

   //初始化保证金额度单
   // 根据授信单位去查找保证金相关的信息
	parameters.corpId= $("#corpId").val();
	parameters.bankCode=$("#bankCode").val();
    parameters.curId=$("#curCode").val();
	bt.bondAccInfo.set(parameters);

    //初始化单品种table
    var initRationTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:false,
	        scrollY:false,
	        clickTrSelected : false,
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
                           reInfo += '<td>'//额度类型
                               + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-selecttext="false" data-group="rat.creditType" data-biz-name="rationDtls[' + rowIndex + '].creditType"  '
                               + 'data-name="rationDtls[' + rowIndex + '].creditType" data-defaultvalue="'+ data.creditType + '"></div></div></td>';
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
	            	   data:"maxMoney",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
	            		   var assurepercent = data.assurepercent;
	            		   var procedureFeeRate = data.procedureFeeRate;
	            		   var bondMoney = data.bondMoney;
                           var procedureFee = data.procedureFee;
	            		   if(assurepercent == null || assurepercent == undefined){
                               assurepercent= "";
	            		   }
	            		   if(procedureFeeRate == null || procedureFeeRate == undefined){
                               procedureFeeRate= "";
	            		   }
	            		   if(bondMoney == null || bondMoney == undefined){
                               bondMoney= "";
	            		   }
                           if(procedureFee == null || procedureFee == undefined){
                               procedureFee= "";
                           }
	            		   var reInfo = '<input type="hidden" id="assurepercent'+rowIndex+'" name="rationDtls['+rowIndex+'].assurepercent" value="' + assurepercent + '">'
			           			+ '<input type="hidden" id="procedureFeeRate' + rowIndex +'" name="rationDtls['+rowIndex+'].procedureFeeRate" value="' + procedureFeeRate + '">'
                               + '<input type="hidden" id="bondMoney' + rowIndex +'" name="rationDtls['+rowIndex+'].bondMoney" value="' + bondMoney + '">'
			           			+ '<input type="hidden" id="procedureFee' + rowIndex + '" name="rationDtls['+rowIndex+'].procedureFee" value="' + procedureFee + '">';
	            		   return reInfo;
	            	   }
	               },
	               //操作
	               {data:null,render:function(data,row){
	            	   var rowIndex = data.itemOrder;
	            	   var reInfo = '<a class="" href="#" data-row="'+rowIndex+'" onclick="openAssure(this, '+rowIndex+')">'+bt.lang("common.ration.limit.set")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t' +
						   '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
	                   return reInfo;
	               }}
		       ],
		       delRowCallback:function(data,$tr){
		    	   var rowIndex = $tr.find("input[name$='itemOrder']").val();
		    	   bt.resetDelItem(rowIndex);
		    	   //删除后更新其他行的授信品种选择
		           $tr.remove();
		           bt.calSumMoney();
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
	        clickTrSelected : false,
	    	elem: "#compTable",
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
	            	   data:"maxMoney",
	            	   render:function(data,row){
	            		   var rowIndex = data.itemOrder;
                           var reInfo = '<input type="hidden" id="assurepercent'+rowIndex+'" name="rationDtls['+rowIndex+'].assurepercent" value="' + assurepercent + '">'
                               + '<input type="hidden" id="procedureFeeRate' + rowIndex +'" name="rationDtls['+rowIndex+'].procedureFeeRate" value="' + procedureFeeRate + '">'
                               + '<input type="hidden" id="bondMoney' + rowIndex +'" name="rationDtls['+rowIndex+'].bondMoney" value="' + bondMoney + '">'
                               + '<input type="hidden" id="procedureFee' + rowIndex + '" name="rationDtls['+rowIndex+'].procedureFee" value="' + procedureFee + '">';
                           return reInfo;
	            	   }
	               },
	               //操作
	               {data:null,render:function(data,row){
	            	   var rowIndex = data.itemOrder;
	            	   var reInfo='<a class="" href="#" data-row="'+rowMR+'" onclick="openAssure(this, '+rowMR+')">'+bt.lang("common.ration.limit.set")+'</a>&nbsp;&nbsp;&nbsp;&nbsp;\t' +
						   '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
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
    var bankInfo=bt.bankInfo.init();//联行号
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }

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

        if (!checkItemMoney()){
            layer.alert(bt.lang("table.ratbkrationdetail.itemmoneynull"));
            return false;
        }

        if(isHangBail=="1"){//授信参数设置挂接保证金
            var isHookBond=dicts.get("isHookBond").val();
            if(isHookBond=="1"){//只有挂接保证金时才做相应的判断
                //判断授信单位与保证金账户单位是否一致
                var ratCorpId=$("#corpId").val();
                var bondCorpId=$("#bondCorpId").val();
                if(ratCorpId!=bondCorpId){
                    bt.loading.hide(); //关闭加载中
                    layer.alert(bt.lang("cms.rat.input.checkCorp"),{icon:2});
                    return false;
                }

                //判断授信银行与保证金账户银行是否一致
                var ratBankAcc=$("#bankCode").val();
                var bondBankAcc=$("#bondBankId").val();
                if(ratBankAcc!=bondBankAcc){
                    bt.loading.hide(); //关闭加载中
                    layer.alert(bt.lang("cms.rat.input.checkBank"),{icon:2});
                    return false;
                }

                //判断授信币别与保证金账户币别是否一致
                var ratCurId=$("#curCode").val();
                var bondCurId=$("#bondCurId").val();
                if(ratCurId!=bondCurId){
                    bt.loading.hide(); //关闭加载中
                    layer.alert(bt.lang("cms.rat.input.checkCurId"),{icon:2});
                    return false;
                }

                // 判断保证金的金额是否大于当前保证金账户的可用余额
                var applyMoney = moneyFun.toNumber($("#applyMoney").val());//保证金金额(就是项目的占用金额)
                var accAvailMoney=moneyFun.toNumber($("#accAvailMoney").val());//当前保证金账户的可用余额
                var applyMoneyPre;
                var editFlag=$("#editFlag").val();
                if(editFlag=="1"){//修改数据,保证金余额需加上修改之前占用的金额
                    applyMoneyPre=moneyFun.toNumber($("#applyMoneyPre").val());
                    accAvailMoney=bt.money.add(applyMoneyPre,accAvailMoney);
                }
                var leftMoney=bt.money.sub(accAvailMoney,applyMoney);
                if(leftMoney<0){
                    //如果保证金金额大于当前保证金账户的可用余额,那么应该提示用户
                    layer.alert(bt.lang("fcbond.check.applyMoney"),{icon:2});
                    bt.loading.hide(); //关闭加载中
                    return false;
                }
            }
		}

		if(isGuarantee=="1"){//授信参数设置挂接担保合同
            //检查选择挂接担保时是否选择担保合同
            var isHangGua=dicts.get("isHangGuarantee").val();
            if(isHangGua=="1"){
                var contractCodes=$("#guaContractCodes").val();
                var contractIds=$("#contractIds").val();
                if(bt.empty(contractCodes)||bt.empty(contractIds)){
                    layer.alert(bt.lang("cms.rat.input.checkGuaContract"),{icon:2});
                    return false;
                }
            }
		}
    	return true;
    }

	/**
	 * 暂存和提交数据
	 *
	 * @param data
	 * @param url
	 * @returns {boolean}
	 */
    function submitData(data, url) {
		data.field.attachfilepath = up.getGroupId();//设置附件ID
		data.field.contractAttachfilepath = contractUp.getGroupId();//设置合同附件id
		var relateCon = $("#relateCon").val();
		var conType = $("#conType").val();
		if (relateCon == "1") {
			// 设置挂接合同类型id
			if (conType == "0") {
				var conTypeId = $("#conTypeId").val();
				if (null != conTypeId || undefined == conTypeId || " " == conTypeId){
					conTypeId = getConTypeRat(data.field.corpId);
				}
				if (typeof conTypeId === "boolean"){
					bt.loading.hide(); //关闭加载中
					return false;
				}
				if (conTypeId == null || conTypeId == undefined || conTypeId == " ") {
					return false;
				}
				data.field.conTypeId = conTypeId;
			} else {
				var conId = $("#conId").val();
				if (null == conId || undefined == conId || "" == conId) {
					layer.alert(bt.lang("common.table.selectConstract"));
					return false;
				}
			}
		}
		data.field.curName = selectTypes.get('curCode').getData().obj.curName
		bt.loading.show();//显示加载中
		$.post(basePath + url, data.field, function (result) {
			bt.loading.hide(); //关闭加载中
			parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
			if (result.success) {
				if (!bt.navtab) {
					parent.layer ? parent.layer.closeAll() : layer.closeAll();
				} else {
					var webObj = bt.navtab.getParentDom();
					webObj.search();
					bt.navtab.closeCurrent();
				}
			}
		}, 'JSON');
	}

    //监听暂存
    form.on('submit(temp)', function(data){
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
		var url = "/cms/rat/ratbkration/editSave";
		// 选择的银行信息中属性“授信合同是否必须”为是，则该字段为必填，否则不必填
		var creditContractRequired = $("#creditContractRequired").val();
		if (!bt.empty(creditContractRequired) && creditContractRequired === "1") {
			// 必填时，还需要根据参数：授信合同必须控制 来控制：0-不控制，1-提醒不控制，2-控制
			if (bt.empty(contractUp.getGroupId())) {
				if (creditContractControl === '1') {
					//提醒不控制
					layer.confirm(bt.lang("table.ratBkration.contractAttachFile.noUpload"), function (index) {
						submitData(data, url)
					});
				} else if (creditContractControl === '2') {
					//请上传合同附件
					layer.alert(bt.lang("table.ratBkration.contractAttachFilenull"), {icon: 2});
					return false;
				} else {
					submitData(data, url)
				}
			} else {
				submitData(data, url)
			}
		} else {
			submitData(data, url)
		}
        return false;
    });

    //监听提交
    form.on('submit(submit)', function(data){
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}

		var url = "/cms/rat/ratbkration/editSubmit";
		// 选择的银行信息中属性“授信合同是否必须”为是，则该字段为必填，否则不必填
		var creditContractRequired = $("#creditContractRequired").val();
		if (!bt.empty(creditContractRequired) && creditContractRequired === "1") {
			// 必填时，还需要根据参数：授信合同必须控制 来控制：0-不控制，1-提醒不控制，2-控制
			if (bt.empty(contractUp.getGroupId())) {
				if (creditContractControl === '1') {
					//提醒不控制
					layer.confirm(bt.lang("table.ratBkration.contractAttachFile.noUpload"), function (index) {
						submitData(data, url)
					});
				} else if (creditContractControl === '2') {
					//请上传合同附件
					layer.alert(bt.lang("table.ratBkration.contractAttachFilenull"), {icon: 2});
					return false;
				} else {
					submitData(data, url)
				}
			} else {
				submitData(data, url)
			}
		} else {
			submitData(data, url)
		}
    	return false;
    });

    //bt.setBankCode(bt.jquery("#bankTypeOri").val());
    bt.changeCorp();

    if(isHangBail=="1"){
        //页面加载后如果没有挂接保证金则隐藏填写信息
        if(dicts.get("isHookBond").val()=="0"){
            $(".isShow").hide();
        }
	}



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

//银行账号选择
function bankinfoCallback(data,dom){
	$("#shouBankName").val(data.bankName);
	$("#bankName").val(data.bankName);
	$("#bankTypeName").val(data.bankTypeName);
	$("#bankCode").val(data.bankCode);//联行号
	$("#bankType").val(data.bankTypeId);//银行类别
	//重新根据授信单位去查找保证金相关的信息
	parameters.corpId=corpId;
	parameters.bankCode=$("#bankCode").val();
    parameters.curId= selectTypes.get("curCode").val();
	bt.bondAccInfo.set(parameters);
	useRatCode();

	// 选择的银行信息中属性“授信合同是否必须”为是，则该字段为必填，否则不必填
	var creditContractRequired = data.creditContractRequired;
	if (!bt.empty(creditContractRequired) && creditContractRequired === '1') {
		$("#contractAttachSpan").show();
		$("#creditContractRequired").val("1");
	} else {
		$("#contractAttachSpan").hide();
		$("#creditContractRequired").val("0");
	}
}

//可结转授信编号查询
function useRatCode(){
	var corpId=$("#corpId").val();
	var bankType=$("#bankType").val();
	var curCode=$("#curCode").val();
	var ratCode=$("#ratCode").val();
	if(!bt.empty(corpId)&&!bt.empty(bankType)&&!bt.empty(curCode)){
		selectTypes.get("oldRatCode").attr({
			"data-url": basePath + "/cms/rat/ratbkration/json?corpId="+corpId+"&bankType="+bankType+"&curCode="+curCode+"&ratCode="+ratCode
		})
	}
}

//根据授信单位去查找保证金相关的信息
bt.corpCallback=function(id){
	corpId=id;
    $("#corpId").val(id);
	parameters.corpId=corpId;
	parameters.bankCode=$("#bankCode").val()
    parameters.curId= selectTypes.get("curCode").val();
	bt.bondAccInfo.set(parameters);
	useRatCode();
    //重新选择单位时，清空担保信息，重新选择担保合同
    $("#guaContractCodes").val("");
    $("#contractIds").val("");
    $("#guaBtn").hide();

	/**
	 *合同挂接改变单位时清空数据
	 */
	var relateCon = $("#relateCon").val();
	var conType = $("#conType").val();
	if (relateCon == "1" && conType == "1") {
		clearConInfo();
	}
}

//如果要挂保证金则显示要填写的信息，否则隐藏
bt.checkback= function(obj){
	if(obj.value =='1'){
		$('.fcbond').show();
	}else{
		$('.fcbond').hide();
	}
}

//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data,dom){
	$("#bondId").val(data.bondId);//保证金Id
	$("#bondMoney").val(data.bondMoney);//保证金账户余额
	$("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
	$("#bankAcc").val(data.bankAcc);//保证金账户
	$("#bankId").val(data.id);//银行id
    $("#bondCorpId").val(data.corpId);
    $("#bondBankId").val(data.bankCode);
    $("#bondCurId").val(data.curId);
    $("#applyMoneyPre").val("");
}

/**设置保证金比例*/
function setBailMoneyRate(){
	var moneysum = moneyTypes.get("moneysum").val();
	var bondPercent = ratioType.get("bondPercent").val();
	var applyMoney = moneyTypes.get("applyMoney").val();
	var isHookBond =  dicts.get('isHookBond').val();

	if(bt.empty(moneysum)||(isHookBond=="0")||(isHookBond==null)){
		return ;
	}

    if(bt.money.sub(applyMoney,moneysum) > 0){//判断保证金金额是否大于授信金额
        parent.layer.msg(bt.lang("cms.rat.input.checkBondMoney"), {icon: 2});
        moneyTypes.get("applyMoney").val(moneysum);
        rateTypes.get("bondPercent").val('100');
        return ;
    }

	if(!bt.empty(applyMoney) && applyMoney != 0){
        ratioType.get("bondPercent").val(fun.div(applyMoney,moneysum,8));
	}
}
/**设置保证金*/
function setBailMoney(){
	var moneysum = moneyTypes.get("moneysum").val();
	var bondPercent = ratioType.get("bondPercent").val();
	var applyMoney = moneyTypes.get("applyMoney").val();
	var isHookBond =  dicts.get('isHookBond').val();

	if(bt.empty(moneysum)||(isHookBond=="0")||(isHookBond==null)){
		return ;
	}
	if(!bt.empty(bondPercent) && bondPercent !=0){
		moneyTypes.get("applyMoney").val(fun.format(fun.mul(moneysum,bondPercent),{separator: "",decimal: 2}));
	}
}

//如果选择了否,清空填写的保证金占用信息
bt.changeFcBond=function(isHookFcBond){
	if(isHookFcBond.value=="0"){
		$(".isShow").hide();
		$("#applyMoney").removeAttr('lay-verify');
		$("#bankAcc").removeAttr('lay-verify');
		moneyTypes.get('applyMoney').val("");
        ratioType.get("bondPercent").val("");
		$("#bankAcc").val("");
		$("#bondContract").val("");
	}else{
		$(".isShow").show();
		$("#applyMoney").attr('lay-verify','required')
		$("#bankAcc").attr('lay-verify','required')
	}
	form.renderIcon();
}


function openAssure(el, rowIndex) {
    var assurepercent = bt.jquery("#assurepercent" + rowIndex).val();
    var procedureFeeRate = bt.jquery("#procedureFeeRate" + rowIndex).val();
    var bondMoney = bt.jquery("#bondMoney" + rowIndex).val();
    var procedureFee = bt.jquery("#procedureFee" + rowIndex).val();

    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        //content : html,
        content: basePath + "/cms/rat/ratbkration/setAssure?rowIndex=" + rowIndex + "&assurepercent=" + bt.jquery("#assurepercent" + rowIndex).val()
        + "&procedureFeeRate=" + bt.jquery("#procedureFeeRate" + rowIndex).val() + "&bondMoney=" + bt.jquery("#bondMoney" + rowIndex).val()
        + "&procedureFee=" + bt.jquery("#procedureFee" + rowIndex).val(),
        end: function () {
            for (var i = 0; i < arguments.length; i++) {
                //console.log(arguments[i]);
            }
        }
    });
}


function getAssure(rowIndex, assurepercent, procedureFeeRate, bondMoney,procedureFee){
    bt.jquery("#assurepercent"+rowIndex).val(assurepercent);
    bt.jquery("#procedureFeeRate"+rowIndex).val(procedureFeeRate);
    bt.jquery("#bondMoney"+rowIndex).val(bondMoney);
    bt.jquery("#procedureFee"+rowIndex).val(procedureFee);

    //bt.calIndexOpenMoney(rowIndex);不用计算
}

//根据币别查询保证金账户
function curinfoCallback(data){
    $("#curCode").val(data.id);
    parameters.corpId=corpId;
    parameters.bankCode=$("#bankCode").val();
    parameters.curId=data.id;
    bt.bondAccInfo.set(parameters);

    //重新选择币别时，清空担保信息，重新选择担保合同
    $("#guaContractCodes").val("");
    $("#contractIds").val("");
    $("#guaBtn").hide();
	useRatCode();
}

function changeDate(){
    //重新选择时间时，清空担保信息，重新选择担保合同
    $("#guaContractCodes").val("");
    $("#contractIds").val("");
    $("#guaBtn").hide();
}


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

	if("1" == ratMode){
		var corpId=$("#corpId").val();
		var bankType=$("#bankType").val();
		var curCode=$("#curCode").val();
		var ratCode=$("#ratCode").val();
		if(!bt.empty(corpId)&&!bt.empty(bankType)&&!bt.empty(curCode)){
			selectTypes.get("oldRatCode").attr({
				"data-url": basePath + "/cms/rat/ratbkration/json?corpId="+corpId+"&bankType="+bankType+"&curCode="+curCode+"&ratCode="+ratCode,
				"data-disabled":false
			})
		}else {
			selectTypes.get("oldRatCode").attr("data-disabled",false);
		}
	}else {
		selectTypes.get("oldRatCode").attr("");
		selectTypes.get("oldRatCode").attr("data-disabled",true);
	}
	form.render();
    /*if("1" == ratMode){
        corpInit.get("corpId").setUrl("all");
    } else {
        corpInit.get("corpId").setUrl("rat");
    }*/
    //corpInit.get("corpId").render();
}

function setOtherParam(param){
    var corpId = selectTree.val();//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    startDate = bt.dateType.init().get("begindate").val();//开始日期
    endDate =  bt.dateType.init().get("enddate").val();//结束日期

    var curId = bt.selectType.init().get("curCode").val();//币别
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

function selectBefore(){
    var corpId =  selectTree.val();//网点id
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return false;
    }else{
        return true;
    }
}

function calComMoney(value,elem){
    //设置综合授信额度一致
    $.each($("input[data-id$='maxMoney']"), function(i,item){
        var maxIndex = $(item).attr("rowindex");
        moneyType.get(maxIndex+"maxMoney").val(value);
    });

    bt.calSumMoney();
}


/**
 * 合同类型选择回调函数
 * @param conTypeId
 */
function setConTypeCallback(conTypeId){
	$("#conTypeId").val(conTypeId);
	$("#submit").trigger('click');
}
