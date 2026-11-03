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
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/receipt/bisbankreceiptdtldispose/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:receipt:bisbankreceiptdtldispose:apply'),
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
            {data:"bankTypeName",bSortable:true, className: 'center'},
            {data:"updateDate",bSortable:true, className: 'center'},
            {data:"isEnable",bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html="";
                if(full.isEnable=="1"){
                    html+='<div><input type="checkbox" name="Enable"  lay-skin="switch" lay-text="YES|NO" lay-filter="check"  vid="'+full.id+'" checked></div>';
                }else{
                    html+='<div><input type="checkbox" name="Enable"  lay-skin="switch" lay-text="YES|NO" lay-filter="check"  vid="'+full.id+'" ></div>';
                }

                    return html;
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('netbank:receipt:bisbankreceiptdtldispose:apply',full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }}
        ]
    });
    form.on('switch(check)',function(object){
        var i;

       if(object.elem.checked){
           i=1;
       }else {
           i=0;
       }
        check($(this).attr("vid"),i);
    })

    $(".buttons-collection").hide();
    $(".buttons-colvis").show();
});

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/receipt/bisbankreceiptdtldispose/savepage");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptdtldispose/savepage",
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
        content: basePath + "/receipt/bisbankreceiptdtldispose/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/receipt/bisbankreceiptdtldispose/updatepage?id=" + data.id);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptdtldispose/updatepage?id=" + data.id,
        end:function(){
            location.reload();
        }
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/receipt/bisbankreceiptdtldispose/delete/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            });
        },'JSON');
    });
}


function check(id,isEnable) {

    $.post(basePath + "/receipt/bisbankreceiptdtldispose/check?id=" + id+"&isenable="+isEnable, function(result){
        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        });
    },'JSON');
}