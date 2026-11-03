bt.use(['form','datatable','dateType','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    //时间
    bt.dateType.init();
    bt.mulselect.init();
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
            'url': basePath+"/creditRating/scoringGrade/getGradeDataTables"
        },
        buttons:[
            {
                hide: bt.isnotResource('cms:loaninfo:apply:edit'),
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
            {data:"scoringGradeCode",bSortable:true,className:"center"},
            {data:"scoringGradeName",bSortable:true,className:"center"},
            {data:"institutionTypeName",bSortable:true,className:"center"},
            {data:"validSignName",bSortable:true,className:"center"},
            {data:"scoringGradeDescription",bSortable:true,className:"center"},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('creditRating:grade:edit')){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                    }
                    if(bt.isResource('creditRating:grade:delete')){
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
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/creditRating/scoringGrade/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/creditRating/scoringGrade/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        btnAlign: 'c',
        btn: [bt.lang("common.button.close")],
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/creditRating/scoringGrade/view?id=" + data.id,
        yes: function(index, layero){
            parent.layer.close(index);
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/creditRating/scoringGrade/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/creditRating/scoringGrade/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/creditRating/scoringGrade/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

