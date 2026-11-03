var select;
var form;
var  moneyFun ;
var rateType;
var moneyType;
bt.use(['form','jquery','corp','dateType','dicts','rateType','datatable','moneyType'], function(){
	$ = bt.jquery,form = bt.form();
   dicts = bt.dicts.init();//字典	
   moneyFun = bt.moneyType.getFun();
   bt.moneyType.init();
   bt.dateType.init();
   rateType = bt.rateType.init();
	moneyType= bt.moneyType.init();
	form.on('select(cycle)', function(data){
		 loadData($("#repayCycel").val(data.value))
	 });
	 //生成列表
    billTable= bt.datatable.init({
        elem: "#appTable",
        colReorder: true,      
        order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/provide/cmsprovideloanrate/datatable?billCode="+$("#billCode").val()
        },
        columns: [
                  {data:"plRate",className:'textRight',
                  	render: function ( data, type, full, meta ) {
                  		return moneyFun.format(full.plRate*100,{separator: ",",decimal: 4})+" %"
                      }
                  },
                  {data:"deferredRate",className:'textRight',
                  	render: function ( data, type, full, meta ) {
                  		return moneyFun.format(full.deferredRate*100,{separator: ",",decimal: 4})+" %"
                      }
                  },
                  {data:"overdueRate",className:'textRight',
                  	render: function ( data, type, full, meta ) {
                  		return moneyFun.format(full.overdueRate*100,{separator: ",",decimal: 4})+" %"
                      }
                  },                  
                   {data:"enterDate",bSortable:true},                
        ],
       
    });
    //监听提交
    form.on('submit(save)', function(data){
		var plRate = rateType.get("plRate").val();
		var deferredRate = rateType.get("deferredRate").val();
		var overdueRate = rateType.get("overdueRate").val();
		// 必填一项
		if (isEmpty(plRate) && isEmpty(deferredRate) && isEmpty(overdueRate)) {
			layer.alert(bt.lang("must.input.oneItem"), {icon: 2});
			return false;
		}

        var action = "/cms/provide/cmsprovideloanrate/save";
        bt.loading.show();
        $.post(basePath + action,data.field,function(result){
        	 bt.loading.hide();
        	parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
            		location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
            	} else {
            		if(data.field.id == ""){
            			bt.navtab.refreshCurrent();
            		}else{
            			bt.navtab.refreshParent();
            			bt.navtab.closeCurrent();
            		}
            	}
            }
        },'JSON');
        return false;
    });

});
bt.use(['selectType'], function() {
 select = bt.selectType.init();
//回调方法逻辑部分
 bt.text = function(data){
	 $("#expiryDayDiv").hide();
	 switch(data.value)
		{
		case '01':
			setDisplay(true,false);break;
		case '02':
			setDisplay(true,true);break;
		case '03':
			setDisplay(true,false);break;
		case '04':
			setDisplay(true,true);break;
		case '05':
			setDisplay(false,false);break;
		default: setDisplay(true,true);	    
		}
 }
});

function setDisplay(expiryDay,withholdingDay){
	if(!expiryDay){
		$("#expiryDayDiv").hide();
		var getCurrentSelect = select.get("expiryDay");
		 $("select[name='expiryDay']").removeAttr("lay-verify");
		 getCurrentSelect.val("0");
	}else{
		$("#expiryDayDiv").show();
		$("select[name='expiryDay']").attr("lay-verify","required");
	}
	if(!withholdingDay){
		$("#withholdingDayDiv").hide();
		var getCurrentSelect = select.get("withholdingDayDiv");
		getCurrentSelect.val("0");
		var p=getCurrentSelect.val();
		$("select[name='withholdingDay']").removeAttr("lay-verify");
		$("#id").attr("data-required","false");
	}else{
		$("#withholdingDayDiv").show();
		$("select[name='withholdingDay']").attr("lay-verify","required");
		$("#id").attr("data-required","true");
	}
	
	form.render();
	form.renderIcon();
}
//回调方法
function test(data){
bt.text(data)
}
doClose = function(){
	if(bt.navtab){
		bt.navtab.closeCurrent();
	}
}

function isEmpty(value) {
	if (value === null || value === undefined || value === "" || value === " ") {
		return true;
	} else {
		return false;
	}
}


/**根据plRate + 上浮BP点数/10000 */
function setDeferredRate(){
	var originalPLRate = $("#originalPLRate").val() ;//原始借款利率
	var deferredPoints = moneyType.get("deferredPoints").val();
	if(!deferredPoints){
		deferredPoints="0.00";
		moneyType.get("deferredPoints").val(deferredPoints);
	}
	var deferredRate=moneyFun.format(parseFloat(originalPLRate)+parseFloat(deferredPoints/10000), {decimal: 6});
	rateType.get("deferredRate").val(deferredRate);
}

/**根据基础利率 + 上浮BP点数/10000 */
function setPLRate(){
	var originalPointsProportion = $("#originalPointsProportionText").val() ;//原始PB点数
	var originalPLRate = $("#originalPLRate").val() ;//原始借款利率

	if(!originalPointsProportion){
		originalPointsProportion="0.00";
	}
	if(!originalPLRate){
		originalPLRate="0.00";
	}
	var benchmarkRate=moneyFun.format(parseFloat(originalPLRate)-parseFloat(originalPointsProportion/10000), {decimal: 6});
	var newPointsProportion = moneyType.get("newPointsProportionText").val() ;
	if(!newPointsProportion){
		newPointsProportion="0.00";
		moneyType.get("newPointsProportionText").val(newPointsProportion);
	}
	var plRate=moneyFun.format(parseFloat(benchmarkRate)+parseFloat(newPointsProportion/10000), {decimal: 6});
	rateType.get("plRate").val(plRate);
}


/**根据plRate * (1+上浮百分比) */
function setOverdueRate(){
	var originalPLRate = $("#originalPLRate").val() ;//原始借款利率
	var overduePercent = moneyType.get("overdueProportion").val();
	if(!overduePercent){
		overduePercent="0.00";
		moneyType.get("overdueProportion").val(overduePercent);
	}
	var overdueRate=moneyFun.format(parseFloat(originalPLRate)*parseFloat(1+overduePercent/100), {decimal: 6});
	rateType.get("overdueRate").val(overdueRate);
}
