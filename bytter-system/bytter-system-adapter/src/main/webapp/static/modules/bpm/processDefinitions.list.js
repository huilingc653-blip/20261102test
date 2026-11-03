bt.use(['form','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:false,
        order: [0,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            url: basePath+"/bpm/processDefinitions/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"process.id",bSortable:true},
            {data:"process.deploymentId",bSortable:false},
            {data:"process.name",bSortable:false},
            {data:"process.key",bSortable:false},
            {data:"process.version",bSortable:false},
            {data:"process.resourceName",bSortable:false,className:'center',render: function (data, type, full, meta) {
                return '<a target="_blank" href="'+basePath+'/bpm/processDefinitions/resource/read?processDefinitionId='+full.process.id+'&resourceType=xml">'+full.process.resourceName+'</a>';
            }},
            {data:"process.diagramResourceName",bSortable:false,className:'center',render: function (data, type, full, meta) {

                return '<a target="_blank" href="'+basePath+'/bpm/processDefinitions/resource/read?processDefinitionId='+full.process.id+'&resourceType=image">'+full.process.diagramResourceName+'</a>';
            }},
            {data:"deployment.deploymentTime",bSortable:false},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="model-status" href="javascript:void(0);" data-row="'+meta.row+'" >'+(full.process.suspended?bt.lang("table.processDefinitions.active"):bt.lang("table.processDefinitions.suspend"))+'</a>';
                    html += '<span class="text-explode">|</span>';
                if(!full.process.suspended){
                    html += '<a class="start" href="#" data-row="'+meta.row+'" onclick="startProcess(this)">'+bt.lang("table.processDefinitions.start")+'</a>';
                    html += '<span class="text-explode">|</span>';
                }

                //配置
                html += '<a class="model-setting" href="javascript:void(0);" data-row="'+meta.row+'" >配置</a>';
                html += '<span class="text-explode">|</span>';

                html += '<a class="definition-delete" href="javascript:void(0);" data-row="'+meta.row+'">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ]
    });

    table.on('click', '.model-setting', function (event) {
        var data = table.row($(this).data('row')).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '330px'],
            content: basePath + "/bpm/processDefinitions/setting/" + data.process.id,
        });
        event.stopPropagation();
    });

    table.on('click', '.model-status', function (event) {
        var data = table.row($(this).data('row')).data();
        $.post(basePath + (data.process.suspended?"/bpm/processDefinitions/update/active/":"/bpm/processDefinitions/update/suspend/") + data.process.id, function(result){
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        event.stopPropagation();
    });

    table.on('click', '.start', function (event) {
        var data = table.row($(this).data('row')).data();
        $.post(basePath + "/bpm/processDefinitions/startProcess/" + data.process.key, function(result){
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        event.stopPropagation();
    });
    table.on('click', '.definition-delete', function (event) {
        var data = table.row($(this).data('row')).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
            $.post(basePath + "/bpm/processDefinitions/delete/" ,{id:data.process.deploymentId}, function(result){
                table.draw();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
        event.stopPropagation();
    });

});

function startProcess(el){

}
function deleteProcess(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){

    }
}