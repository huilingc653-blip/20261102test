bt.use(['form','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [0,'desc'],
        paging: false,
        ajax: {
            url: basePath+"/app/rest/models",
            method: 'get',
            contentType:'application/json; charset=UTF-8',
            dataType: 'json',
            data: function(aoData){
                var sort = "modifiedDesc";
                if(aoData.order && aoData.order.length>0){
                    if(aoData.columns[aoData.order[0].column].data=='lastUpdated'){
                        sort = "modified"+replaceStr(aoData.order[0].dir);
                    }else{
                        sort = aoData.columns[aoData.order[0].column].data+replaceStr(aoData.order[0].dir);
                    }
                }
                if($("input[name='filterText']").val()){
                    aoData.filterText = $("input[name='filterText']").val();
                }
                aoData.filter = "processes";
                aoData.modelType = 0;
                aoData.sort = sort;
                return aoData;
            }
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function () {
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
            {data:"name",bSortable:true},
            {data:"key",bSortable:false},
            {data:"description",bSortable:false},
            {data:"lastUpdated",bSortable:true},
            {data:"version",bSortable:false},
            {data:"createdBy",bSortable:false},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="deployment(this)">部署</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ]
    });
    form.on('submit(search)', function(data){
        datatable.draw();
        return false;
    });
});
function replaceStr(str){
    str = str.toLowerCase();
    var reg = /\b(\w)|\s(\w)/g;
    return str.replace(reg,function(m){
        return m.toUpperCase()
    });
}
function add(){
    parent.layer.open({
        type: 2,
        area: ['600px', '350px'],
        maxmin: true,
        content: basePath + "/bpm/model/de/create"
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
            var data = $(layero).find("iframe")[0].contentWindow.formData();
            if(data){
                $.ajax({
                    url: basePath + "/bpm/model/de/create",
                    method: 'POST',
                    data: JSON.stringify(data),
                    contentType:'application/json; charset=UTF-8',
                    dataType: 'json',
                    processData: false,
                    success: function(result){
                        layer.closeAll();
                        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/flowable-ui/index.html#/editor/" + result.id);
                    },
                    error:function(err){
                        console.log(err);
                    }
                });
            }
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bpm/actdemodel/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/flowable-ui/index.html#/editor/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/flowable-ui/index.html#/editor/" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bpm/model/de/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}