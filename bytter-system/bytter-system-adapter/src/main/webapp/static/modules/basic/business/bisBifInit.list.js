bt.use(['form','datatable','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/basic/business/bisbifinit/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"bifCode",bSortable:true},
            {data:"name",bSortable:true},
            /*{
                data: "isDomesticBank", bSortable: true, render: function (data, type, full, meta) {
                    if (full.isDomesticBank == '1') {
                        return bt.lang("nis.sc_yn.yes");
                    } else {
                        return bt.lang("nis.sc_yn.no");
                    }
                }
            },*/
            {data:"bankTypeName",bSortable:true},
            {
                data: "isSupportBankForeign", bSortable: true, render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isSupportBankForeign);
                }
            },
            {data:"validSignName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    });
})

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/basic/business/bisbifinit/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/bisbifinit/add",
        end:function(){
            location.reload();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/bisbifinit/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/basic/business/bisbifinit/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/bisbifinit/bisbifinit/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/basic/business/bisbifinit/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}