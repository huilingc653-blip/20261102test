bt.use(['form','datatable','dateType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    //时间
    bt.dateType.init();
    dicts = bt.dicts.init();//字典

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        colReorder: true,
        ordering:false,
        ajax: {
            'url': basePath+"/creditRating/computationalModel/getModelDataTables"
        },
        buttons:[
            {
                hide: bt.isnotResource('creditRating:model:add'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"modelCode",bSortable:true,className:"center"},
            {data:"modelName",bSortable:true,className:"center"},
            // {data:"modelFormulaName",bSortable:true,className:"center"},
            {data:"validSignName",bSortable:true,className:"center"},
            {data:"modelDescription",bSortable:true,className:"center"},
            {data:"createDate",bSortable:true,className:"center"},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('creditRating:model:add')){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="copy(this)">'+bt.lang("common.button.clone")+'</a>'
                    }
                    if(bt.isResource('creditRating:model:edit')){
                        if(html != ''){
                            html +=' <span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                    }
                    if(bt.isResource('creditRating:model:delete')){
                        html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a>';
                    }
                    html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';

                    return html;
                }}
        ],

    });
});

function add(el){

    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/creditRating/computationalModel/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/creditRating/computationalModel/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/creditRating/computationalModel/view?modelId=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/creditRating/computationalModel/view?modelId=" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function copy(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.clone'),basePath + "/creditRating/computationalModel/copy?modelId=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/creditRating/computationalModel/copy?modelId=" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/creditRating/computationalModel/update?modelId=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/creditRating/computationalModel/update?modelId=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/creditRating/computationalModel/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

