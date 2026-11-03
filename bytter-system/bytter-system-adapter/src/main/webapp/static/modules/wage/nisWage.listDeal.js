var datatable;
bt.use(['form','datatable','select','dateType','corp','money','moneyType','dicts','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
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
            'url': basePath+"/wage/niswage/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:wage:nisWage:dealApply'),
                text: bt.lang('common.button.batchBack'),
                action: function ( e, dt, node, config ) {
                	 var data = dt.rows('.selected').data();
                	 backBatch(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        otherExport:{ // 其他导出
            text:bt.lang('nis.button.exportSelect'), // 按钮文本
            callback:function(processing){ // 其他导出执行方法
                var data = datatable.rows('.selected').data();
                exports(data);
                processing(false)
            }
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"wageNo",bSortable:true,className:'center'},
            {data:"wageTitle",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"wages",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.wages);
                }
            },
            {data:"wageDay",bSortable:true,className:'center'},
            {data:"wishPayday",bSortable:true,className:'center'},
            {data:"addWord",bSortable:true},
            {
                data: "successNum", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDetail(this,0)">' + full.successNum + '</a>';
                    return html;
                }
            },
            {
                data: "failNum", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDetail(this,2)">' + full.failNum + '</a>';
                    return html;
                }
            },
            {
                data: "waitNum", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDetail(this,1)">' + full.waitNum + '</a>';
                    return html;
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:wage:nisWage:dealApply',full.corpId)){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="back(this)">'+bt.lang("common.button.back")+'</a><span class="text-explode">|</span>';
                }
            	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum:'7',
                    dataName:'wages'
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

/**
 * 导入功能
 */
exports = function (data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
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
    var url = basePath + "/wage/niswage/exportDtl?ids=" + ids + "&opType=listDeal";
    window.location.href = url;
};

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswage/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswage/view/" + data.id
    });
}

/**
 * 查看
 * @param id
 */
function viewDetail(el,opType) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswagedetail/view/" + data.id + "/"+opType);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswagedetail/view/" + data.id + "/"+opType
    });
}

function back(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
        $.post(basePath + "/wage/niswage/back/" + data.id, function(result){
        	datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//批量打回
function backBatch(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchBack'),function(){
     	$.post(basePath + "/wage/niswage/batchBack",{ids:ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
 	});
}
