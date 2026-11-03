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
            url: basePath+"/bpm/model/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function () {
                    parent.layer.open({
                        type: 2,
                        area: ['350px', '250px'],
                        content: basePath + "/bpm/model/view"
                    });
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
            {data:"createTime",bSortable:false},
            {data:"lastUpdateTime",bSortable:true},
            {data:"version",bSortable:false},
            {data:"category",bSortable:false},
            {data:"metaInfo",bSortable:false},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '';
                //编辑
                html += '<a class="model-edit" href="javascript:void(0);" data-row="'+meta.row+'">'+bt.lang("common.button.edit")+'</a>';
                html += '<span class="text-explode">|</span>';
                //部署
                html += '<a class="model-deploy" href="javascript:void(0);" data-row="'+meta.row+'" >'+bt.lang("common.button.deploy")+'</a>';
                html += '<span class="text-explode">|</span>'
                //BPMN
                html += '<a class="export-xml" href="javascript:void(0);" data-row="'+meta.row+'">BPMN</a>';
                html += '<span class="text-explode">|</span>';
                //JSON数据
                html += '<a class="export-json" href="javascript:void(0);" data-row="'+meta.row+'">JSON</a>';
                html += '<span class="text-explode">|</span>';
                //删除
                html += '<a class="model-delete" href="javascript:void(0);" data-row="'+meta.row+'">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ]
    });

    table.on('click', '.export-xml,.export-json', function (event) {
        var data = table.row($(this).data('row')).data();
        var type = $(this).hasClass("export-xml")?'bpmn':'json';
        window.open(basePath + "/bpm/model/export/"+data.id+"/"+type);
        event.stopPropagation();
    });

    table.on('click', '.model-edit', function (event) {
        var data = table.row($(this).data('row')).data();
        window.open(basePath + "/flowable-ui/modeler.html?modelId="+data.id);
        event.stopPropagation();
    });

    table.on('click', '.model-deploy', function (event) {
        var data = table.row($(this).data('row')).data();
        $.post(basePath + "/bpm/model/deploy/" + data.id, function(result){
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        event.stopPropagation();
    });

    table.on('click', '.model-bpmn', function (event) {
        var data = table.row($(this).data('row')).data();
        event.stopPropagation();
    });

    table.on('click', '.model-json', function (event) {
        var data = table.row($(this).data('row')).data();
        event.stopPropagation();
    });

    table.on('click', '.model-delete', function (event) {
        var data = table.row($(this).data('row')).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
            $.post(basePath + "/bpm/model/delete/" + data.id, function(result){
                table.draw();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
        event.stopPropagation();
    });



});
