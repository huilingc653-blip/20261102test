bt.use(['form','datatable','btdate','dateType','selectType','corp'], function(){
    var dateType = bt.dateType.init();
    bt.selectType.init()
    bt.corp.init()
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        order: [[1,'asc']],
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/bond/register/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide: bt.isnotResource('cms:bond:register:add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        }
        ,columns: [
            {data:"bondTypeName",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"bondCode",bSortable:true},
            {data:"bondFullName",bSortable:true},
            {data:"bondShortName",bSortable:true},
            {data:"bondDeadline",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return data + bt.dict("remainTerm.unit", full.deadlineUnit);
                }
            },
            {data:"bondCurName",bSortable:true},
            {data:"issueCurTotal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.issueCurTotal);
                }
            },
            {data:"issueRmbTotal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.issueRmbTotal);
                }
            },
            {data:"purpose",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:"statusName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_EMITBILL_STATUS",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = ''
                    if (full.status === 0 || full.status === -1) {
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a> ';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a> ';
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    });
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/bond/register/view/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/register/view/" + data.id
        });
    }
}
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/bond/register/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/bond/register/add",
        end:function(){
            datatable && datatable.draw();
            bt.navtab.closeCurrent();
            bt.navtab.refreshParent();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/bond/register/update/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/register/update/" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/bond/register/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
