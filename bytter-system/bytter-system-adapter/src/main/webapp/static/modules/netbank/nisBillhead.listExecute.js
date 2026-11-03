var datatable;
bt.use(['form','datatable','select','dateType','corp','money','moneyType','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
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
            'url': basePath+"/netbank/nisbillhead/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:netBank:nisBillHead:executeApply'),
            	text: bt.lang('common.button.batchBack'),
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
                 	parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
	                 	$.post(basePath + "/netbank/nisbillhead/batchBack",{ids:ids}, function(result){
	                        datatable.draw();
	                        parent.layer.alert(result.msg, {icon: result.success?1:2});
	                    },'JSON');
                 	});
                }
            },{
                hide:bt.isnotResource('netbank:netBank:nisBillHead:executeApply'),
                text: bt.lang('common.button.batchSend'),
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
                        if(rowData[i].sysVoucherCode != '33') {
                            if (!rowData[i].downAccId || rowData[i].downAccId == "") {
                                layer.alert(bt.lang('nis.message.execute.downAcc.null', [rowData[i].billNo]));
                                return;
                            }
                            if(!rowData[i].downAccPrent || rowData[i].downAccPrent == ""){
                                layer.alert(bt.lang('nis.message.execute.downAccPrent.null', [rowData[i].billNo]));
                                return;
                            }
                        }
                    }
                    parent.layer.confirm(bt.lang('common.message.confirm.send'),function(){
                        $.post(basePath + "/netbank/nisbillhead/batchSend",{ids:ids}, function(result){
                            datatable.draw();
                            parent.layer.alert(result.msg, {icon: result.success?1:2});
                        },'JSON');
                    });
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },columns: [
            {data:"billNo",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"voucherName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payerBank",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.payMoney);
                }
            },
            {data:"downAcc",bSortable:false},
            {data:"downAccName",bSortable:false},
            {data:"downAccPrent",bSortable:false},
            {data:"downAccNamePrent",bSortable:false},
            {data:"payeeAcc",bSortable:true},
            {data:"payeeAccName",bSortable:true},
            {data:"payeeBankCode",bSortable:true},
            {data:"payeeBank",bSortable:true},
            {data:"wishPayday",bSortable:true},
            {data:"busTypeName",bSortable:true},
            {data:"bursarBillNo",bSortable:true},
            {data:"purpose",bSortable:true},
            {data:"addWord",bSortable:true},
            {data:"billFrom",bSortable:true,
                render: function ( data, type, full, meta ) {

                    return bt.dict("nisBillhead.billFrom",full.billFrom);
                }
            },
            {data:"status",bSortable:true,
                render: function ( data, type, full, meta ) {

                    return bt.dict("nisBillhead.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:netBank:nisBillHead:executeApply',full.corpId)){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="sendColumn(this)">'+bt.lang("common.button.send")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="backColumn(this)">'+bt.lang("common.button.back")+'</a><span class="text-explode">|</span>';
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	//html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum:'8',
                    dataName:'payMoney'
                }
            ]
        }
    });
    //下拉框
    $.each($("div[type='list']"),function(i,item){
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
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.id
    });
}
//发送
function sendColumn(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/netbank/nisbillhead/send?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nisbillhead/send?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
//打回
function backColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
        $.post(basePath + "/netbank/nisbillhead/back/" + data.id, function(result){
        	datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}