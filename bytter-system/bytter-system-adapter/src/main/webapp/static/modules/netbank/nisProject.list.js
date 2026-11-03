bt.use(['form','upload','datatable','select','btImport','dicts'],function () {
    bt.dicts.init();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder: true,
        order: [[6,'asc'],[0,'asc']],
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/nisproject/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }

        ],
        columns: [
            {data:"projectCode",bSortable:true,className:'center'},
            {data:"projectNameShow",bSortable:true,className:'center'},
            {data:"projectCdSignName",bSortable:true,className:'center'},
            {data:"matchAttrName",bSortable:true,className:'center'},
            {data:"matchContent",bSortable:true,className:'center'},
            {data:"matchRelationName",bSortable:true,className:'center'},
            {data:"projectSort",bSortable:true,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}
        ],
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/netbank/nisproject/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    })
});

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + '/netbank/nisproject/add');
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px','530px'],
        maxmin: true,
        content: basePath + '/netbank/nisproject/add',
        end:function () {
            table && table.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisproject/view/" + data.id
    });
}

function update(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    if (data){
        if (bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + '/netbank/nisproject/update?id=' + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px','530px'],
            manmin: true,
            content: basePath + "/netbank/nisproject/update?id=" + data.id,
            end:function () {
                table && table.draw();
            }
        })
    }
}

function del(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function () {
        $.post(basePath + "/netbank/nisproject/delete/" + data.id,function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg,{icon: result.success?1:2});
        },'JSON');
    })
}