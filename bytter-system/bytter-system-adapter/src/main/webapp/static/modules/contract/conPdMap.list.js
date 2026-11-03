var $,form,dataTable;
bt.use(['form','datatable','select','dateType','dicts','conType'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    dataTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/contract/conPdMap/datatable"
        },buttons: [
            // 新增、列隐藏
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }
        ]
        ,columns: [
            {data:"pdCode",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("CON_PRODUCT_TYPE",full.pdCode);
                }
            },
            {data:"pdCode",bSortable:true,className:'center'},
            {data:"typeName",bSortable:true,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html= '<a  href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html+='<a href="#" data-row="'+meta.row+'" onclick="deleteData(this)">'+bt.lang("common.button.delete")+'</a>';
                    return html;
                }
            }
        ]
    });
    bt.dicts.init();
    bt.conType.init();
});

update = function(el){
    var data = dataTable.row(layui.jquery(el).data("row")).data();
    var params='?id='+data.id+"&pdCode="+data.pdCode+"&typeId="+data.typeId;
    parent.layer.open({
        title:bt.lang('common.button.edit'),
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/contract/conPdMap/update"+params,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
};
add = function(){
    parent.layer.open({
        title:bt.lang('common.button.add'),
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/contract/conPdMap/add",
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
};

deleteData = function(el){
    var data = dataTable.row(layui.jquery(el).data("row")).data();
    var action="/contract/conPdMap/delete";
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + action, {'id':data.id}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
};