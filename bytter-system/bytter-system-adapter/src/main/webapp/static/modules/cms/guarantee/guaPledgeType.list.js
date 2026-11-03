var flag="";
var datatable;

bt.use(['form','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [[0, 'asc']],
        ajax: {
            'url': basePath+"/cms/guarantee/pledgeType/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
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
            {data:"typeCode",bSortable:true,className:'center'},
            {data:"typeName",bSortable:true,className:'center'},
            {data:"typeAttr",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("guarantee.pledge.TypeAttr",full.typeAttr);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '\
                <a class="" href="#" data-row="'+meta.row+'" onclick="beforeUpdatecheckData(this)">'+bt.lang("common.button.edit")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="beforeDeletecheckData(this)">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ]
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/guarantee/pledgeType/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/pledgeType/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/guarantee/pledgeType/update?id=" + data.id);
        return;
    }
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/pledgeType/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/guarantee/pledgeType/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 判断抵质押物类型能否被修改
 * @param data
 */
function beforeUpdatecheckData(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	$.post(basePath + "/cms/guarantee/pledgeType/checkData", {
		id : data.id
	}, function(data) {
		if(data){
			 update(el);
		}else {
			parent.layer.msg(bt.lang("gua.guaPledgeType.info"), {icon: 2});
			return false;
		}
	}, 'JSON');
}

/**
 * 判断抵质押物类型能否被删除
 * @param data
 */
function beforeDeletecheckData(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	$.post(basePath + "/cms/guarantee/pledgeType/checkData", {
		id : data.id
	}, function(data) {
		if(data){
			deleteColumn(el);
		}else {
			parent.layer.msg(bt.lang("gua.guaPledgeType.info"), {icon: 2});
			return false;
		}
	}, 'JSON');
}