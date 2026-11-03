bt.use(['form','datatable','moneyType','dateType','selectType','dicts','rateType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    bt.dateType.init();//时间
    bt.selectType.init();//时间
    bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
      //  checkbox:true,
        ajax: {
            'url': basePath+"/rate/maintence/datatable"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add(e, dt, node, config);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
			{data:"baseRateName"},
			{data:"creditPeriodName"},
            {data:"moneyTypeName"},
            {data:"timeOfTakeEffect"},
            {data:"loanClassName",
                render: function(data, type, full, meta){
                    var loanClassName = bt.dict("cms.LoanClass",full.loanClass);
                    return loanClassName;
                }
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
            {data:"timeOfLastModifier"},
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("LoanRate.Status",full.status);
                }
            },
            {data:"isValid",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("LoanRate.IsValid",full.isValid);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html;
            	if(full.status == "1" || full.status == "3" || (full.status == "2" && full.isValid == "0")){
            		html = '<a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("common.button.edit")+'</a>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a>';
                }else if(full.status == "2"){
                	html = '<a class="" href="#" data-row="'+meta.row+'" onclick="valid(this)">'+bt.lang("common.button.taboo")+'</a>';
                }
            	html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}
        ]
    });
});

function add(e, dt, node, config){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/loanrate/maintence/addNotBase");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/loanrate/maintence/addNotBase",
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
    });
}

function edit(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/loanrate/maintence/editNotBase/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/loanrate/maintence/editNotBase" + data.id,
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
    });
}

function del(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/loanrate/maintence/delete/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function valid(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.taboo'),function(){
        $.post(basePath + "/cms/loanrate/maintence/valid/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.id);
            return;
        }
	}
}