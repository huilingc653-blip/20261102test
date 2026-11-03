bt.use(['form','corp','datatable','dicts','selectTree','mulselect','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        order: [[0, 'desc']],
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData:"false"
        },
        ajax: {
            'url': basePath+"/ttpay/bussiness/datatable"
        },
        // ignoreButtons: ['print'],
        buttons: [
            {
                hide:bt.isnotResource('ttpay:shop:bussniss:add'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }
        ],
        order: [[1,'desc']],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data:"code",bSortable:true},
            {data:"shopName",bSortable:true},
            {data:"shoptypeName",bSortable:true},
            {data:"isFlag",bSortable:true,render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.isFlag);
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('ttpay:shop:bussniss:edit',full.corpId)){
                        html+='<a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("common.button.edit")+'</a>';
                        html+='<span class="text-explode">|</span>';
                    }
                    if(bt.isResource('ttpay:shop:bussniss:del',full.corpId)){
                        html+='<a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a>';
                        html+='<span class="text-explode">|</span>';
                    }
                    if(bt.isResource('ttpay:shop:bussniss:view',full.corpId)){
                        html+='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    }

                    return html;
                }}
        ]
    });
});

function edit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/ttpay/bussiness/update/" + data.id);
        return;
    }
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/bussiness/view/" + data.id
    });
}
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/ttpay/bussiness/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/bussiness/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}
//删除
function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/ttpay/bussiness/delete/" + data.id, function(result){
            bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}


