var datatable;
bt.use(['form','datatable','selectType','dateType','dicts','bank','corp'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    bt.corp.init();

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
            'url': basePath+"/bdlink/bisquerycmd/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:bdLink:bisQueryCmd:sendApply'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                hide:bt.isnotResource('netbank:bdLink:bisQueryCmd:sendApply'),
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
                 	parent.layer.confirm(bt.lang('common.message.confirm.bisquerycmd.resend'),function(){
	                 	$.post(basePath + "/bdlink/bisquerycmd/batchResend",{ids:ids}, function(result){
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
        },
        columns: [
            
            {data:"voucherNo",bSortable:true,className:'center'},
            {data:"bifName",bSortable:true,className:'center'},
            {data: "corpName", bSortable: true},
            {data:"bankAcc",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"curName",bSortable:true},
            {data: "transName", bSortable: true, className: 'center'},
            {data:"startTime",bSortable:true,className:'center'},
            {data:"endTime",bSortable:true,className:'center'},
            {data:"sendTime",bSortable:true,className:'center'},
            {data:"returnTime",bSortable:true,className:'center'},
            {data:"firstReturnMsg",bSortable:true,className:'center'},
            {data:"nextReturnMsg",bSortable:true,className:'center'},
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "cmdStatName", bSortable: true, className: 'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:bdLink:bisQueryCmd:sendApply',full.corpId)){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="resendColumn(this)">'+bt.lang("common.button.resend")+'</a>';
                }
                return html;
            }}
        ]
    });
});
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/bdlink/bisquerycmd/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisquerycmd/add",
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
        content: basePath + "/bdlink/bisquerycmd/view/" + data.id
    });
}
//重发
function resendColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.bisquerycmd.resend'),function(){
        $.post(basePath + "/bdlink/bisquerycmd/resend/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
