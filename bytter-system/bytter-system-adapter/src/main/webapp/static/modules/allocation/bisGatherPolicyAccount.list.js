var datatable;
bt.use(['form','datatable','select','dicts','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [[3,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/allocation/bisgatherpolicy/accountDatatable"
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
            {data:"bankAcc",bSortable:false,className:'center'},
            {data:"corpName",bSortable:false},
            {data:"bankName",bSortable:false,className:'center'},
            {data:"priority",bSortable:true,className:'center'},
            {data:"isDown",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isDown);
                }
            },
            {data:"status",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("status_yn",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    });

});
function add(){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/bisgatherpolicy/addAccount",
        end:function(){
            bt.navtab.refreshCurrentTable();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/bisgatherpolicy/viewAccount/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/bisgatherpolicy/updateAccount?id=" + data.id,
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/allocation/bisgatherpolicy/deleteAccount/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
