var rowMR=0;  
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var corpInit;
var selectTree;
bt.use(['form','layedit','btdate','bttable','moneynum','dicts','select','moneyType','dateType','corp', 'selectType','money','conSelect','conView'], function(){
    var $ = bt.jquery,form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框

	/**
	 * 合同挂接组件初始化
	 */
	bt.conSelect.init();
	bt.conView.init();

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
    	$.post(basePath + "/cms/rat/ratcpration/checkContractNo", {
    		contractNo : contractNo,
    		ratCode : $("#id").val()
		}, function(data) {
			if(data && "0" == data){
				layer.alert(bt.lang("table.ratbkration.contractNoRepeat"));
				$("#contractNo").val("");
			}
		}, 'JSON');
    });	

  
    
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
    
    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='money']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("moneysum").val(bt.money.format(sumMoney));
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
			+ '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls['+rowMR+'].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=1" '
			+ 'data-valuefield="id" data-textfield="name" id="'+rowMR+'itemCode" data-name="rationDtls['+rowMR+'].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';
		html+='<td><input type="text" data-name="rationDtls['+rowMR+'].money" id="'+rowMR+'money" data-biz-name="'+rowMR+'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calMoney"></td>';
		html+='<td></td>';
		html+='<td>'
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
			           			+ '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls['+rowIndex+'].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=1" '
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
	            	   var reInfo = '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
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
	    compRowIndex = $("#compRowIndex").val();
    }
    
    
    var initSelectCode=function(row){
    	bt.dicts.init();
    	bt.moneyType.init();
    	//设置新增的品种不可选已勾选的品种
    	bt.setNewItemDisable(row);
    	bt.selectType.init();
    }
    
    initRationTable();
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    //初始化单位
    corpInit = bt.corp.init();
    
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
    
    //检查每项授信是否输入品种名称
    var checkItemName = function () {
        var success = true;
        $.each($("input[class$='layui-input layui-unselect']"), function(i,item){
            if (bt.empty($(item).val())) {
                success = false;
                return false;
            }
        });
        return success;
    };
    
    var checkBefore = function(){
    	var corpId = $("#corpId").val();
    	if(bt.empty(corpId)){
    		layer.alert(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
    		return false;
    	}

    	if(singrowMR == 0){
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
        
        if (!checkItemName()){
        	layer.alert(bt.lang("table.ratbkrationdetail.customItemOne"));
            return false;
        }
    	return true;
    }
    

    
    //监听提交或暂存
    form.on('submit', function(data){
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
    	var action = "/cms/rat/ratcpration/tempOrSubmit";
    	bt.loading.show();//显示加载中
		data.field.curName = selectTypes.get('curCode').getData().obj.curName
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            bt.loading.hide(); //关闭加载中
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


//单位回调函数
bt.corpCallback = function(id) {
	$("#corpId").val(id);
	/**
	 *合同挂接改变单位时清空数据
	 */
	var relateCon = $("#relateCon").val();
	var conType = $("#conType").val();
	if (relateCon == "1" && conType == "1") {
		clearConInfo();
	}
	//获取单位信息
/*	$.ajax({
		url : basePath + "/sys/corp.do?getSysCorp",
		method : 'POST',
		data : {
			id : id
		},
		dataType : 'json',
		success : function(data) {
			if (data.obj) {
				if (data.obj.userAccountCode) {
					$("#corpId").val(data.obj.userAccountCode);
				}
			}
			form.render();
		}
	});*/
};


/**
 * 合同类型选择回调函数
 * @param conTypeId
 */
function setConTypeCallback(conTypeId){
	$("#conTypeId").val(conTypeId);
	$("#submit").trigger('click');
}