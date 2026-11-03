var datatable;
bt.use(['form','datatable','select','corp','dicts','selectTable','moneyType','rateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rateType = bt.rateType.getFun();
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
            'url': basePath+"/chowmatistic/chobankfinance/datatable"
        },
        buttons: [
            {
            	hide:bt.isnotResource('cho:bankFinanceC2T:choBankFinance:apply'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
            	hide:bt.isnotResource('cho:bankFinanceC2T:choBankFinance:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    if(data.length < 1){
                    	layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,data.length);
                 	for(var i=0;i<rowData.length;i++){
                 		if (ids == "") {
    						ids += rowData[i].id;
    					} else {
    						ids += "," + rowData[i].id;
    					}
                 	}
                 	parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
                 	 $.post(basePath + "/chowmatistic/chobankfinance/batchDelete/",{ids:ids}, function(result){
                         bt.datatable.refreshCurrentPage();
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
        }
        ,columns: [
            {data:"billNo",bSortable:true,className:'center'},
            {data:"payerAcc",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.payMoney);
                }
            },
            {data:"interestRate",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return rateType.format(full.interestRate);
                }
            },
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center'},
            {data:"isOnline",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isOnline);
                }
            },
            {data:"purpose",bSortable:true},
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '';
            	if(bt.isResource('cho:bankFinanceC2T:choBankFinance:apply',full.corpId)){
	                html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
	                <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
            	}
            	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ]
    });
    
    //付款账号
    var payerAccInit = bt.selectTable.init({
      	 elem: "#payerAccTable",
      	 url: basePath+"/chowmatistic/chobankfinance/getBankAcc",
      	 urldata: {corpId: $("input[name='corpId']").val(), busType: $("#busType").val()},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#payerAcc",
         paging:true,
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#payerAcc').val(data.bankAcc);
      			 $('#payerAccId').val(data.id);
      			 $('#curId').val(data.curId);
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });
    
    //单位回调函数
    bt.corpCallback = function(id){
    	//重新加载付款账号
    	payerAccInit.get("payerAcc").reload({corpId: id, busType: $("#busType").val()});
    }
    
    //初始化单位
    bt.corp.init();
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/chowmatistic/chobankfinance/add/"+$("#busType").val());
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/chobankfinance/add/"+$("#busType").val(),
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/chobankfinance/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/chowmatistic/chobankfinance/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/chowmatistic/chobankfinance/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/chowmatistic/chobankfinance/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}