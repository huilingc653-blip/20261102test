var datatable={},mulselect;
bt.use(['form','datatable','corp','dateType','element','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.dateType.init();
    mulselect=bt.mulselect.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
//        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bis/checkReason/datatable"
        },
        buttons: [
            //新增、批量删除
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('bis:check:reason:query'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"reasonCode",bSortable:true},
            {data:"reasonName",bSortable:true},
            {data:"bankTypeName",bSortable:true},
            {data:"appScopeName",bSortable:true},
            {data:"validSignName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                    html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                    html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    });

});

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bis/checkReason/add");
        return;
    }
}

function update(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bis/checkReason/edit/"+data.id);
        return;
    }
}

function view(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/bis/checkReason/view/"+data.id);
        return;
    }
}



function deleteColumn(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bis/checkReason/btDel",{"ids":id}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}




