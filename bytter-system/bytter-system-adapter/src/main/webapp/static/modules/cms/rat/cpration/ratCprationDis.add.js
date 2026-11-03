var rowMR=0; 
var comprowMR=0;
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var selectTree;
bt.use(['form','layedit','bttable','moneynum','dicts','select','moneyType','mulselect','corp', 'selectType','mulselect','money','selectTree'], function(){
    var $ = bt.jquery,form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }

    var unUserMoney=$("#unUserMoney").val();
    if(unUserMoney<=0){
        layer.alert(bt.lang("table.ratcprationDis.checkLeftMoney"));
	}
    
    /**获取二次授信单位**/
    $.each($(".selectTreeDemo"),function(i,item){
		selectTree = bt.selectTree.initGetCurrent({
			url: basePath + '/cms/rat/ratcprationdis/listNetCorp?netId=' + $("#ratCorpId").val(),
			elem:".selectTreeDemo",
			name: "corpId",
			selected: $("#disCorpId").val(),
			setting:{
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
    
    //设置可读不可读
    bt.setMoneyRead = function(readSign) {
    	$.each($(".bt-biz-moneyType"), function(i,item){
    		if(readSign == "1"){
    			$(item).attr("readonly", "readonly");
    		} else {
    			var dataName = $(item).attr("data-name");
    			if(dataName != "moneysum"){
    				$(item).removeAttr("readonly");
    			}
    		}
        });
    }
    
    //重置分配额度和剩余额度
    bt.resetUnuseMoney = function(){
       	$.each($("input[data-id$='money']"), function(i,item){
    		var rowIndex = $(item).attr("rowindex");
    		bt.calLeftMoney(rowIndex);
    		
    		var rowId = $(item).attr("data-id");
    		if(rowId.indexOf("leftMaxMoney") != -1){
    			bt.calLeftMaxMoney(rowIndex);
    		}
        });
    	
    }

    //计算剩余额度
    bt.calLeftMoney = function(rowIndex) {
    	var unUserMoney = 0; 
    	var disMoney = 0;
    	var moneyIndex = $("#" + rowIndex + "money");
    	if(moneyIndex.val()){
    		disMoney = moneyType.get(rowIndex+"money").val();
    		unUserMoney = $("#"+ rowIndex+"unUserMoney").val(); 
    	}
        $("#" + rowIndex + "moneySpan").html(bt.money.format(bt.money.sub(unUserMoney, disMoney)));
    }

    //设置分配金额和剩余额度
    bt.calMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calLeftMoney(rowIndex);
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
    //设置分配金额和剩余额度
    bt.calMaxMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.checkMaxMoney(data, rowIndex);
    	bt.calLeftMaxMoney(rowIndex);
    }
 
    
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    //初始化单位
    bt.corp.init();
    bt.mulselect.init();
    
    function checkBefore(){
    	var corpId = "";
        corpId = selectTree.val();
        if(bt.empty(corpId)){
            layer.alert(bt.lang("table.ratbkrationDis.chooseCorpId"));
            return false;
        }
        var unUserMoney=$("#unUserMoney").val();
        if(unUserMoney<=0){
            layer.alert(bt.lang("table.ratcprationDis.checkLeftMoney"));
            return false;
        }

        var sumMoney;
        $.each($("input[id$='money']"), function(i,item){
            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        if(sumMoney<=0){
            layer.alert(bt.lang("table.ratcprationDis.checkDisMoney"));
            return false;
        }
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
    	var action = "/cms/rat/ratcprationdis/tempOrSubmit";
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
    
    //监听返回
    form.on('submit(back)', function(data){
    	parent.layer ? parent.layer.closeAll() : layer.closeAll();
    	return false;
    });
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.closeCurrent();
        }
    }
    bt.resetUnuseMoney();
});

closeTab = function(){
	bt.close();
}


