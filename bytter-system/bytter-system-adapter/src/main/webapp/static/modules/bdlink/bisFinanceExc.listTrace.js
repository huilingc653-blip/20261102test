var datatable;
bt.use(['form','datatable','dateType','selectType','moneyType','dicts','bank','corp'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bdlink/bisfinanceexc/datatable"
        },
        buttons: [
            {
            	hide:bt.isnotResource('netbank:bdLink:bisFinanceExc:traceApply'),
                text: bt.lang('common.button.batchResend'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(len<1){
                    	layer.msg(bt.lang('common.message.confirm.selectData'));
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,len);
                 	for(var i=0;i<rowData.length;i++){
                 		if (ids == "") {
    						ids += rowData[i].id;
    					} else {
    						ids += "," + rowData[i].id;
    					}
                 	}
                 	parent.layer.confirm(bt.lang('common.message.confirm.resend'),function(){
	                 	$.post(basePath + "/bdlink/bisfinanceexc/batchResend",{ids:ids}, function(result){
	                        datatable.draw();
	                        parent.layer.alert(result.msg, {icon: result.success?1:2});
	                    },'JSON');
                 	});
                }
            },{
            	hide:bt.isnotResource('netbank:bdLink:bisFinanceExc:traceApply'),
                text: bt.lang('common.button.batchDefeat'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(len<1){
                    	layer.msg(bt.lang('common.message.confirm.selectData'));
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,len);
                 	for(var i=0;i<rowData.length;i++){
                 		if (ids == "") {
    						ids += rowData[i].id;
    					} else {
    						ids += "," + rowData[i].id;
    					}
                 	}
                 	parent.layer.confirm(bt.lang('common.message.confirm.defeat'),function(){
	                 	$.post(basePath + "/bdlink/bisfinanceexc/batchDefeat",{ids:ids}, function(result){
	                        datatable.draw();
	                        parent.layer.alert(result.msg, {icon: result.success?1:2});
	                    },'JSON');
                 	});
                }
            },{
            	hide:bt.isnotResource('netbank:bdLink:bisFinanceExc:traceApply'),
                text: bt.lang('common.button.batchReturn'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(len<1){
                    	layer.msg(bt.lang('common.message.confirm.selectData'));
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,len);
                 	for(var i=0;i<rowData.length;i++){
                 		if (ids == "") {
    						ids += rowData[i].id;
    					} else {
    						ids += "," + rowData[i].id;
    					}
                 	}
                    parent.layer.prompt({
                        title: bt.lang('table.bisExc.backMsg'),
                        formType: 2
                    },function(backMsg){
	                 	$.post(basePath + "/bdlink/bisfinanceexc/batchBack",{ids:ids,backMsg:backMsg}, function(result){
                            datatable && datatable.draw();
                            parent.layer?parent.layer.closeAll():layer.closeAll();
	                        parent.layer.alert(result.msg, {icon: result.success?1:2});
	                    },'JSON');
                 	});
                }
            /*},{
                hide:bt.isnotResource('netbank:bdLink:bisFinanceExc:traceApply'),
                text: bt.lang('common.button.batchRecover'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(len<1){
                    	layer.msg(bt.lang('common.message.confirm.selectData'));
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,len);
                 	for(var i=0;i<rowData.length;i++){
                 		if (ids == "") {
    						ids += rowData[i].id;
    					} else {
    						ids += "," + rowData[i].id;
    					}
                 	}
                 	parent.layer.confirm(bt.lang('common.message.confirm.recover'),function(){
	                 	$.post(basePath + "/bdlink/bisfinanceexc/batchRecover",{ids:ids}, function(result){
	                        datatable.draw();
	                        parent.layer.alert(result.msg, {icon: result.success?1:2});
	                    },'JSON');
                 	});
                }*/
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
            
            {data:"voucherNo",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true},
            {data: "busTypeName", bSortable: true, className: 'center'},
            {data:"bifName",bSortable:true,className:'center'},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payerBank",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"dayLimit",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return full.dayLimit+full.dateUnitName;
                }
            },
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.payMoney);
                }
            },
            {data:"reqDate",bSortable:true,className:'center'},
            {data:"cbsBillNo",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                	var html = "";
                	if(full.cbsBillNo!="" && full.cbsBillNo!=null){
                		html = '<a class="" href="#" data-row="'+meta.row+'" onclick="viewBill(this)">'+full.cbsBillNo+'</a>';
                	}
                	return html;
                }
            },
            {data: "voucherStatName", bSortable: true, className: 'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:bdLink:bisFinanceExc:traceApply',full.corpId)){
	                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="resendColumn(this)">'+bt.lang("common.button.resend")+'</a><span class="text-explode">|</span>';
	                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="defeatColumn(this)">'+bt.lang("common.button.defeat")+'</a><span class="text-explode">|</span>';
	                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="backColumn(this)">'+bt.lang("common.button.return")+'</a><span class="text-explode">|</span>';
	                /*html += '<a class="" href="#" data-row="'+meta.row+'" onclick="recoverColumn(this)">'+bt.lang("common.button.recover")+'</a><span class="text-explode">|</span>';*/
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(full.cbsBillId && full.cbsBillId != ""){
                	html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="viewBill(this)">'+bt.lang("common.button.viewBusBill")+'</a>';
                }
                return html;
            }}
        ],
        summary:{
            pageSum:false,
            allSum:true,
            selectSum:true,
            format:{
            	decimal:2
            },
            sumList:[
                {
                    colNum:'11',
                    dataName:'payMoney'
                }
            ]
        }
    });
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisfinanceexc/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisfinanceexc/view/" + data.id
    });
}
//重发
function resendColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.resend'),function(){
        $.post(basePath + "/bdlink/bisfinanceexc/resend/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//作废
function defeatColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.defeat'),function(){
        $.post(basePath + "/bdlink/bisfinanceexc/defeat/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//退回
function backColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.prompt({
        title: bt.lang('table.bisExc.backMsg'),
        formType: 2
    },function(backMsg){
        $.post(basePath + "/bdlink/bisfinanceexc/back", {id:data.id,backMsg:backMsg}, function(result){
            datatable && datatable.draw();
            parent.layer?parent.layer.closeAll():layer.closeAll();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//恢复
/*function recoverColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.recover'),function(){
        $.post(basePath + "/bdlink/bisfinanceexc/recover/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}*/
//查看业务单据详情
function viewBill(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var cbsBillId = data.cbsBillId;
    if (cbsBillId.indexOf("_D") > -1) {
        cbsBillId = cbsBillId.replace("_D", "");
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/chowmatistic/chobankfinance/view/" + cbsBillId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/chobankfinance/view/" + cbsBillId
    });
}