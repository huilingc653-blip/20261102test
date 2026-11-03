bt.use(['form','btdate','money','dateType','corp','dicts','selectType','treeTable','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }

    //监听查询
    form.on('submit(search)', function(data){
    	var curCode = bt.selectType.init().get("curCode").val();
    	if(bt.empty(curCode)){
    		parent.layer.msg(bt.lang("nis.pay.message.cur.null"), {icon: 2});
    		return false;
    	}
    	// var beginDate = bt.dateType.init().get("beginDate").val();
    	// var endDate = bt.dateType.init().get("endDate").val();
    	// if(bt.empty(beginDate) || bt.empty(endDate)){
    	// 	parent.layer.msg(bt.lang("cms.rat.ratUration.ratDate.null"), {icon: 2});
    	// 	return false;
    	// }

		var action =basePath + "/cms/rat/ratcprationcount/rationCountQuery";
        $("#searchForm").attr("action",action);
        $("#searchForm").submit();
    });
    bt.dateType.init();
    bt.selectType.init();
    bt.corp.init();
    bt.dicts.init();
    bt.mulselect.init();//单位实现多选
    bt.treeTable.init({elem:"#appTable",column:0});

    //监听重置
	resetCode = function(){
	    var inputs = $('.rCode').find('input');
	    inputs.each(function(i, item){
	        $(this).val("");
	    });
	    $("#corpId").val(" ");
	    // document.getElementById("bankCode").options[0].selected = true;
	    bt.selectType.init().get("moneyType").val("1");
	    // bt.selectType.init().get("itemCode").val(" ");
	    bt.selectType.init().get("curCode").val($("#curId").val());
			bt.dicts.init().get("moneyType").val("1");
	    var nowdate = bt.date.format(new Date(), 'yyyy-MM-dd');
	    bt.dateType.init().get("beginDate").val(nowdate);
	    bt.dateType.init().get("endDate").val(nowdate);
	}
});



