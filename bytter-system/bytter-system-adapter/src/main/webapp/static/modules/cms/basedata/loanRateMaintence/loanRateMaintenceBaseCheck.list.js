bt.use(['form','datatable','moneyType','dateType','selectType','dicts','rateType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    bt.dateType.init();//时间
    bt.selectType.init();//时间
    dict=bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        checkbox:true,
        ajax: {
            'url': basePath+"/rate/maintence/datatable"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
             {
                text: bt.lang('common.button.check'),
                action: function ( e, dt, node, config ) {
                	check(e, dt, node, config);
                }
            },{
                text: bt.lang('common.button.back'),
                action: function ( e, dt, node, config ) {
                	back(e, dt, node, config);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
			{data:"baseRateName"},
			{data:"creditPeriodName"},
            {data:"timeOfTakeEffect"},
            {data:"loanClassName",render: function(data, type, full, meta){
                    var loanClassName = bt.dict("cms.LoanClass",full.loanClass);
                    return loanClassName;
                },
            },
            {data:"period",render: function ( data, type, full, meta ) {
            	var per = "";
            	if(full.period == null){
            		per = "N";
            	}else if(full.period == 0){
            		per = ""
            	}else{
            		per = full.period;
            	}
                return per+full.periodCycleName+"";
            }},
            {data:"rateFromWay",bSortable:true,
                render: function ( data, type, full, meta ) {
                    var rateFromWay = bt.dict("baseRate.fromWay",full.rateFromWay);
                    return rateFromWay;
                }
            },
            {data:"rate",render: function ( data, type, full, meta ){
            	return bt.rateType.getFun().format(full.rate);
            }},
            {data:"rateUpLimit",render: function ( data, type, full, meta ){
                    var unit = "";
                    if (full.baseRateCode == "lpr") {
                        unit = "BP";
                    } else {
                        unit = "%";
                    }
                    return moneyFun.format(full.rateUpLimit, {decimal: 2}) + unit;
            }},
            {data:"rateDownLimit",render: function ( data, type, full, meta ){
                    var unit = "";
                    if (full.baseRateCode == "lpr") {
                        unit = "BP";
                    } else {
                        unit = "%";
                    }
                    return moneyFun.format(full.rateDownLimit, {decimal: 2}) + unit;
            }},
            {data:"timeOfLastModifier"}
        ]
    });
});

function check(e, dt, node, config){
	var data = dt.rows('.selected').data();
    var len = data.length;
    var rowData = data.splice(0,len);
    var ids = [];
    for(var i = 0; i < rowData.length; i++){
        ids.push(rowData[i].id);
    }
    if(ids.length>0){
    	parent.layer.confirm(bt.lang('common.message.confirm.check'),function(){
            $.post(basePath + "/cms/loanrate/maintence/check",{ids:ids}, function(result){
            	bt.navtab.refreshCurrentTable();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
    }else{
    	parent.layer.alert(bt.lang('common.message.confirm.selectData'));
    }
}

function back(e, dt, node, config){
	var data = dt.rows('.selected').data();
    var len = data.length;
    var rowData = data.splice(0,len);
    var ids = [];
    for(var i = 0; i < rowData.length; i++){
        ids.push(rowData[i].id);
    }
    if(ids.length>0){
    	parent.layer.prompt({title: bt.lang("common.message.config.please"),formType: 2},function(value, index, elem){
    		parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
    	        $.post(basePath + "/cms/loanrate/maintence/back",{ids:ids,backScribe:value},function(result){
    	        	bt.navtab.refreshCurrentTable();
    	            parent.layer.alert(result.msg, {icon: result.success?1:2});
    	        },'JSON');
    	    });
    		parent.layer.close(index);
    	});
    }else{
    	parent.layer.alert(bt.lang('common.message.confirm.selectData'));
    }
}