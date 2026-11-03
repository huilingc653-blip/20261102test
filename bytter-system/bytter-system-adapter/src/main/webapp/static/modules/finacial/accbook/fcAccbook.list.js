bt.use(['form','datatable','btdate','moneynum'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:false,order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/finacial/accbook/fcaccbook/datatable"
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
            {data:"tallyDate",bSortable:true},
            {data:"interestStartDate",bSortable:true},
            {data:"voucherType",bSortable:true},
            {data:"emergencySign",bSortable:true},
            {data:"affixAmount",bSortable:true},
            {data:"busOriType",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ]
    });
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            //bt.moneynum.setnum({elem:$(item),decimal:$(item).attr('decimal')*1});
        }
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
});
function add(){
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbook/add",
        end:function(){
            location.reload();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbook/view?id=" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbook/update?id=" + data.id,
        end:function(){
            location.reload();
        }
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/finacial/accbook/fcaccbook/delete/" + data.id, function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}