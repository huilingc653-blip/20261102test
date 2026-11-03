bt.use(['form','datatable','btdate'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:true,order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/fccarry/fccarry/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: '<i class="layui-icon">&#xe642;</i> '+bt.lang('common.button.edit'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    layer.msg(JSON.stringify(rowData));
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"carryType",bSortable:true},
            {data:"netId",bSortable:true},
            {data:"carryTime",bSortable:true},
            {data:"operatorCode",bSortable:true},
            {data:"voucherStartDate",bSortable:true},
            {data:"voucherEndDate",bSortable:true},
            {data:"carryAcc",bSortable:true},
            {data:"restoreCarryTime",bSortable:true},
            {data:"restoreCarryOptor",bSortable:true},
            {data:"createTime",bSortable:true},
            {data:"createBy",bSortable:true},
            {data:"updateTime",bSortable:true},
            {data:"updateBy",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ]
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/fccarry/fccarry/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/fccarry/fccarry/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/fccarry/fccarry/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/fccarry/fccarry/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/fccarry/fccarry/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/fccarry/fccarry/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}