bt.use(['form','datatable','select','date'], function(){
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
            'url': basePath+"/scheduler/systask/datatable"
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
                    if(rowData && rowData.length>0){
                        update(rowData[0]);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"taskname",bSortable:true},
            {data:"active",width:"80px",className:'center',bSortable:true, render: function(data) {
                return bt.dict("scheduler.status",data);
            }},
            {data:"period",width:"80px",className:'center',bSortable:true, render: function(data) {
                return bt.dict("scheduler.type",data);
            }},
            {data:"startdtm",width:"150px",className:'center',bSortable:true, render: function(data) {
                if(null == data || "" == data){
                    return "";
                }
                return bt.date.format(data, "yyyy-MM-dd hh:mm:ss");
            }},
            {data:"enddtm",width:"150px",className:'center',bSortable:true, render: function(data) {
                if(null == data || "" == data){
                    return "";
                }
                return bt.date.format(data, "yyyy-MM-dd hh:mm:ss");
            }},
            /*{
                data: "validSign", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.validSign);
                }
            },*/
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="javascript:;" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="javascript:;" data-row="'+meta.row+'" onclick="update(layui.jquery(\'#appTable\').DataTable().row(layui.jquery(this).data(\'row\')).data())">'+bt.lang("common.button.edit")+'</a>\
                <a class="" href="javascript:;" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.dict("scheduler.status",full.active?0:1)+'</a>\
                <a class="" href="javascript:;" data-row="'+meta.row+'" onclick="runNow(this);">'+bt.lang("task.button.runnow")+'</a>\
                <a class="" href="javascript:;" data-row="'+meta.row+'" onclick="runhistory(this)">'+bt.lang("task.button.history")+'</a>';
                return html;
            }}
        ]
    });
    bt.select.init({
        elem:'.selectactive',
        name:'eqi_active',
        valueField: "code",
        textField:"name",
        placeholder:{value:'',text:'请选择'},
        url:basePath+"/sys/dict/dictlist?"+encodeURI("code[]=scheduler.status"),
        change:function(data,text){
        }
    });
});
function runhistory(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('task.button.history'),basePath + "/scheduler/systaskaudit/list/"+data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/scheduler/systaskaudit/list/"+data.id
        });
    }
}
function runNow(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.runNow'),function(){
        bt.loading.show();
        $.post(basePath + "/scheduler/systask/runnow/" + data.id, function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/scheduler/systask/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/scheduler/systask/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['800px', '400px'],
        maxmin: true,
        content: basePath + "/scheduler/systask/view/" + data.id
    });
}
function update(data){
    //var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/scheduler/systask/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/scheduler/systask/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        parent.layer.confirm(data.active?bt.lang('common.message.confirm.taboo'):bt.lang('common.message.confirm.enable'),function(){
            $.post(basePath + "/scheduler/systask/status/" + data.id, function(result){
                datatable && datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
    }
}