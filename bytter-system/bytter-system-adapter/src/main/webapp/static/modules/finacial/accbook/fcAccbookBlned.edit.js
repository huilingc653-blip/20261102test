bt.use(['element', 'form','layedit','btdate','moneynum'], function(){
	//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
	var $ = bt.jquery,form = bt.form();
	var element = layui.element();

	 //监听Tab切换，
	/*
	element.tabChange('tab(newiframe)', function() {
		alert('qq3');
	});*/

	 
	var newVoucherIsLoaded = false;
	
	//监听Tab切换，
	form.on('checkbox', function(data) {
		var addNewCheck = data.elem.checked;
		if("true" == addNewCheck || true == addNewCheck){
			//加载录入新凭证页面
			if(!newVoucherIsLoaded){
				newVoucherIsLoaded = true;
				bt.jquery("#newiframe").attr("src", basePath + '/finacial/accbook/fcaccbook/update?opType=newRush&id=' + $("#newId").val());
			}
			bt.jquery("#newiframe").show(1000);
		} else {
			bt.jquery("#newiframe").hide(1000);
		}
	});
	
	// 监听控制content高度
    function init() {
    	initNewVoucher();
        // 起始判断收缩还是展开
        
        // 选项卡高度
        var cardTitleHeight = $(document).find(".layui-tab[lay-filter='card2'] ul.layui-tab-title").height();
        // 需要减去的高度
        var height = $(window).height() - $('.layui-header').height() - cardTitleHeight;
        // 设置高度
        $(document).find(".layui-tab[lay-filter='card2'] div.layui-tab-content").height(height - 100);
    }
	
    //初始化新录入凭证页面
    function initNewVoucher(){
    	var addNewVoucher = bt.jquery("#addNewVoucher").prop("checked");//判断是否选中
    	if(addNewVoucher){
    		newVoucherIsLoaded = true;
			bt.jquery("#newiframe").attr("src", basePath + '/finacial/accbook/fcaccbook/update?opType=newRush&id=' + $("#newId").val());
			bt.jquery("#newiframe").show(1000);
    	}
    }
    
 	// 初始化
    init();
    
    var getReqParams = function(){
    	
    	var params = $("form:first").serialize();
        var form1 = bt.jquery("#rushiframe").contents().find("form:first");
        //form1.verifyFun("#rushtallyDate");
        //form1.verifyFun("#rushinterestStartDate");
        //form1.verifyFun("#desReason");
        
        //form.verifyFun(bt.jquery("#rushiframe").contents().find(".requr"));
        
        params += "&"+ form1.serialize();
        
        var addNewVoucher = bt.jquery("#addNewVoucher").prop("checked");//判断是否选中
        if(addNewVoucher){
        	var form2 = bt.jquery("#newiframe").contents().find("form:first");
        	//凭证录入界面数据校验
        	//form.verifyFun(bt.jquery("#newiframe").contents().find(".requr"));
        	
        	params += "&addNewVoucher=true&" + form2.serialize();
        }
        return params;
    }
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/accbook/fcaccbookblned/save";
        $.post(basePath + action, getReqParams(),function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParentTable();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/accbook/fcaccbookblned/submit";
        $.post(basePath + action, getReqParams(),function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParentTable();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //返回
    form.on('submit(revert)', function(data){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.closeCurrent();
        }
        return false;
    });
    
    
});
