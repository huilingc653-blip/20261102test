var dateType;
var dicts;
var moneyTypes;
var curr;
bt.use(['form','btdate','money','laypage','dicts','dateType','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    dateType = bt.dateType.init();
    dicts = bt.dicts.init();//字典
    moneyTypes=bt.moneyType.init();
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "yyyy-MM-dd",
            istime: false
        });
    });


    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    /**设置查询条件参数*/
    bt.setParam = function() {
        var param = new Object();
        var ratDate = dateType.get("ratDate").val();//估计授信时间
        var ratMode =dicts.get("ratMode").val();//授信模式
        var contractNo = $("#contractNo").val();//合同号
        var gMoney=$("#gmoney").val();
        var lMoney=$("#lmoney").val();
        param["start"] = bt.empty(curr) ? "" : curr;
        param["ratDate"] = bt.empty(ratDate) ? "" : ratDate;
        param["ratMode"] = bt.empty(ratMode) ? "" : ratMode;
        param["contractNo"] = bt.empty(contractNo) ? "" : contractNo;
        param["gMoney"] = bt.empty(gMoney) ? "" : gMoney;
        param["lMoney"]=bt.empty(lMoney)?"":lMoney;
        return parseParam(param);
    }
    
    bt.laypage({
		cont: 'page',
		pages: $("#total").val(),
		curr:$("#curr").val(),
		skip:true,
		jump:function(obj, first){
			if(!first){
                curr = obj.curr;
                var param=bt.setParam();
				location.href = basePath + "/cms/rat/ratguachange/changelist?"+param;
			}
		}
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });
    
    $("#search").click(function(){
    	//search();
    });
    
    //监听重置
	resetCode = function(){
	    var inputs = $('.rCode').find('input');
	    inputs.each(function(){
	        $(this).val("");
	    })
        dicts.get("ratMode").val("");//授信模式
    }
});

function search(){
	var param = "";
	var ratDate = dateType.get("ratDate").val();
	//param = "start="+$("#curr").val() +"&contractNo="+$("#contractNo").val()+"&ratDate="+ratDate;
    var param=bt.setParam();
	location.href = basePath + "/cms/rat/ratguachange/changelist?"+param;
}

function view(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.view'),basePath + "/cms/rat/ratbkration/view/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkration/view/" + id
    });
}
function guaChange(id){
    $("#businessId").val(id);
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('rat.button.changeGua'),basePath + "/cms/rat/ratguachange/changeGuaContract/" +id);
        return;
    }
}



//json转url参数
var parseParam = function(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};
