var datatable;
bt.use(['form','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable=bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[0, 'asc']],
        ajax: {
            'url': basePath+"/cms/credit/loantype/datatable"
        },
        buttons: [
            {
            	hide:bt.isnotResource('cms:basedata:loan:add'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }
        ]
        ,columns: [
            {data:"code",bSortable:true,className:"center"},
            {data:"name",bSortable:true,className:"center"},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            if(bt.isResource('cms:basedata:loan:edit')){
            	var html= '';
            	if(full.isDefault != 1 && full.status == 0){
                 html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                 	if(bt.isResource('cms:basedata:loan:delete')){
                 		 html +='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                 	}
                 	return html;
            }else if(full.status == -2){
                	var a = bt.lang("cms.basedata.loan.delete");
                	return a;
            }else{
            		return null;
                }
            }}}
        ]
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/credit/loantype/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/credit/loantype/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/credit/loantype/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/credit/loantype/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        $.post(basePath + "/cms/credit/loantype/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
          
        },'JSON');
}