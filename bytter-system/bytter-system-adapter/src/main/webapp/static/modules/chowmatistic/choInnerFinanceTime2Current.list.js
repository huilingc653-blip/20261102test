var datatable;
bt.use(['form','datatable','select','corp','dicts','moneyType','rateType', 'selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rateType = bt.rateType.getFun();
    var selectType = bt.selectType.init();
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
            'url': basePath+"/chowmatistic/choinnerfinance/datatable"
        },
        buttons: [
            {
            	hide:bt.isnotResource('cho:innerFinanceT2C:choInnerFinance:apply'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
            	hide:bt.isnotResource('cho:innerFinanceT2C:choInnerFinance:apply'),
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
                 	 $.post(basePath + "/chowmatistic/choinnerfinance/batchDelete/",{ids:ids}, function(result){
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
            {data:"corpName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"dayLimit",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return full.dayLimit+bt.dict("pricing.fixedTerm", full.dateUnit);
                }
            },
            {data:"payDate",bSortable:true,className:'center'},
            {data:"purpose",bSortable:true},
            {
                data: "relatedBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.relatedBillNo != "" && full.relatedBillNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewRelatedBill(this)">' + full.relatedBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '';
            	if(bt.isResource('cho:innerFinanceT2C:choInnerFinance:apply',full.corpId)){
	                html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
	                <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
            	}
            	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ]
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/chowmatistic/choinnerfinance/add/"+$("#busType").val());
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/choinnerfinance/add/"+$("#busType").val(),
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
        content: basePath + "/chowmatistic/choinnerfinance/view/" + data.id
    });
}
function viewRelatedBill(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/choinnerfinance/view/" + data.relatedBillId
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/chowmatistic/choinnerfinance/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/chowmatistic/choinnerfinance/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/chowmatistic/choinnerfinance/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}