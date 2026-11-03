bt.use(['form','datatable','select','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
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
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bankacc/btbankaccapp/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:bankAcc:btBankAccApp:apply'),
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
            {data:"accNo",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"curName",bSortable:true},

            // {data:"curId",bSortable:true,
            //     render:function (data,type,full,meta) {
            //         return full.curName;
            //     }
            // },
            {data:"isDomesticBankName",bSortable:true},
            {data:"accTypeName",bSortable:true},
            {data:"attributeName",bSortable:true},
            {data:"natureName",bSortable:true},
            {data:"bankTypeName",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"status",bSortable:true,
                render:function (data,type,full,meta) {
                    return bt.dict("netbank.statusAll",full.status);
                }
            },
            {data:"provName",bSortable:true},
            {data:"cityName",bSortable:true},
          
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html='';
                if(bt.isResource('netbank:bankAcc:btBankAccApp:apply',full.corpId)) {
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ]
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/bankacc/btbankaccapp/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccapp/add",
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
        content: basePath + "/bankacc/btbankaccapp/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/bankacc/btbankaccapp/update?id=" + data.id);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccapp/update?id=" + data.id,
        end:function(){
            location.reload();
        }
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bankacc/btbankaccapp/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
