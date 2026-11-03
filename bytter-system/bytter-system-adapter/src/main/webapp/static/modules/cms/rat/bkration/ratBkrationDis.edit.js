var rowMR=0;
var comprowMR=0;
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var selectTree;
bt.use(['form','layedit','bttable','moneynum','dicts','select','moneyType','mulselect','corp', 'selectType','mulselect','money','btUpload'], function(){
    var $ = bt.jquery,form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //修改二次授信单位选择项
    bt.changeRatCorp = function(disWay) {//重置可用余额
    	var className = "";
    	var checktype = "";
    	var html = "";
    	var disCorpId = $("#disCorpId").val();
    	if("1" == disWay) {
    		className = "bt-biz-selectType";
    		html = '<div class="layui-input-inline"><div class="' + className + '" data-biz-name="corpId"'
    		+ ' data-url="' + basePath + '/cms/rat/ratbkrationdis/listNetCorp?netId=' + $("#ratCorpId").val() + '&disWay=' 
    		+ disWay + '" data-valuefield="id" data-textfield="name" data-name="corpId" data-biz-name="corpId" data-defaultvalue="' + $("#ratCorpId").val() + '" ></div></div>';
    	} else if ("2" == disWay){//总额共享
    		className = "selectTreeDemo";
    		checktype = "check";
    		html = '<div class="layui-input-inline"><div class="'+className+'" data-width="400px;"></div></div>';
    	} else if ("3" == disWay){//金额共享
    		className = "selectTreeDemo";
    		checktype = "radio";
    		html = '<div class="layui-input-inline" ><div class="'+className+'" data-width="400px;"></div></div>';
    	}
    	
    	$("#ratCorpDiv").html(html);
    	if("1" == disWay ) {
    		bt.selectType.init();//下拉框
    	} else if ("2" == disWay || "3" == disWay){//总额共享
    		//下拉树
    		$.each($("." + className),function(i,item){
    			selectTree = bt.selectTree.initGetCurrent({
    				url: basePath + '/cms/rat/ratbkrationdis/listNetCorp?netId=' + $("#ratCorpId").val() + '&disWay='+ disWay,
    				elem:".selectTreeDemo",
    				name: "corpId",
    				selected: disCorpId,
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
    	            }
    			});
    		});
    	}
    	form.render("select");
    }
    
    
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
    	$.each($(".rationHidden"), function(i,item){
    		var rowIndex = $(item).attr("index");
    		//bt.calLeftMoney(rowIndex);
    		var rowId = $(item).attr("id");
    		if(rowId.indexOf("leftMaxMoney") != -1){
    			bt.calLeftMaxMoney(rowIndex);
    		}
        });
    }
    
    //金额共享,设置所有的金额为0
    bt.setDisMoneyShare = function(){
    	$.each($(".bt-biz-moneyType"), function(i,item){
    		var biz_name = $(item).attr("data-biz-name");
    		moneyType.get(biz_name).val("0.00");
        });
    }
    
    //设置所有的金额为各自剩余的金额
    bt.setDisMoneyOwn = function(){
    	$.each($(".bt-biz-moneyType"), function(i,item){
    		var biz_name = $(item).attr("data-biz-name");
    		moneyType.get(biz_name).val($(item).attr("oldValue"));
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
    	if ($("#disWay").val() != '2') {
            $("#" + rowIndex + "moneySpan").html(bt.money.format(bt.money.sub(unUserMoney, disMoney)));
        }else{
            $("#" + rowIndex + "moneySpan").html(bt.money.format(0));
		}
    }
    
    bt.calMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calLeftMoney(rowIndex);
    	bt.calSumMoney();
    }
    //综合授信设置
    bt.calCompMoney = function(data, el){
    	//设置综合授信额度一致
    	$.each($("input[data-id$='maxMoney']"), function(i,item){
    		var maxIndex = $(item).attr("rowindex");
    		moneyType.get(maxIndex+"maxMoney").val(data);
    		bt.calLeftMaxMoney(maxIndex);
        });
    	
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calLeftMoney(rowIndex);
    	bt.calSumMoney();
    }

    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[data-id$='money']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("moneysum").val(bt.money.format(sumMoney));
    };
    
    //设置分配金额和剩余额度
    bt.calMaxMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.checkMaxMoney(data, rowIndex);
    	bt.calLeftMaxMoney(rowIndex);
    }
    //计算剩余最高额度
    bt.calLeftMaxMoney = function(rowIndex) {
    	var unUserMoney = 0;
    	var disMoney = 0;
    	var moneyIndex = $("#" + rowIndex + "maxMoney");
    	if(moneyIndex.val()){
    		disMoney = moneyType.get(rowIndex+"maxMoney").val();
    	} else {
    		moneyType.get(rowIndex+"maxMoney").val("0");
    		disMoney = 0;
    	}
    	unUserMoney = $("#"+ rowIndex+"leftMaxMoney").val();
    	$("#"+rowIndex +"maxMoneySpan").html(bt.money.format(bt.money.sub(unUserMoney, disMoney)));
    }
    
    //检查最高额度与授信额度关系
    bt.checkMaxMoney = function(data, rowIndex){
    	var compRowIndex = $("#compItemIndex").val();
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
    
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    //初始化单位
    bt.corp.init();
    bt.mulselect.init();
    
    function checkBefore(){
    	var disWay = bt.dicts.init().get("distributeWay").val();
    	var corpId = "";
    	if("1" == disWay) {
    		corpId = selectTypes.get("corpId").val();
    		if(bt.empty(corpId)){
    			layer.alert(bt.lang("table.ratbkrationDis.chooseCorpId"));
    			return false;
    		}
    	} else {
    		corpId = selectTree.val();
    		if(bt.empty(corpId)){
				layer.alert(bt.lang("table.ratbkrationDis.chooseCorpId"));
				return false;
    		}
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
        data.field.attachmentId = up.getGroupId();//设置附件ID
        var action = "/cms/rat/ratbkrationdis/editSave";
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
        data.field.attachmentId = up.getGroupId();//设置附件ID
    	var action = "/cms/rat/ratbkrationdis/editSubmit";
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
    bt.changeRatCorp($("#disWay").val());

    if ($("#disWay").val() === '1') {
        bt.setMoneyRead(1);
		$("#distributeView").hide();
    } else if ($("#disWay").val() === '2') {
		bt.setMoneyRead(1);
		$("._maxMoney").removeAttr("readonly");
		$("#distributeView").show();
	} else if ($("#disWay").val() === '3') {
		bt.setMoneyRead(0);
		$("#distributeView").hide();
	}
    
    //检查额度是否超过剩余额度
    bt.checkDisMoney = function(){
    	var checkFlag = true;
        if ($("#disWay").val() == '2'){
            return checkFlag;
        }
    	$.each($("input[data-id$='money']"), function(i,item){
    		var rowIndex = $(item).attr("rowindex");
            var money = moneyType.get(rowIndex+"money").val();
    		var leftMoney = $("#"+ rowIndex +"unUserMoney").val();
    		//额度不能大于授信剩余额度
        	if(bt.money.sub(money, leftMoney) > 0){
        		layer.alert(bt.lang("cms.rat.dis.money.enough"));
        		checkFlag = false;
        		return false;
        	}
        });
    	return checkFlag;
    }
    //显示分配单位和剩余额度
    bt.checkDisMoney();
    bt.resetUnuseMoney();
    bt.calSumMoney();
});

closeTab = function(){
	bt.close();
}

// 总额共享方式已分配的单位查看
function distributeView() {
	layer.open({
		type: 2,
		title: '总额共享已分配单位',
		area: ['1000px','500px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratbkrationdis/distributeWayView/" + $("#ratCode").val() + "/" + $("#ratCorpId").val(),
		end : function(){

		}
	});
}