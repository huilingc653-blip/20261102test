bt.use(['form','datatable','btdate'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        ordering:false,
        checkbox:true,order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/finacial/check/fccompareacc/datatable"
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
            {data:"checkType",bSortable:true},
            {data:"orderNo",bSortable:true},
            {data:"voucherNo",bSortable:true},
            {data:"tallyDate",bSortable:true},
            {data:"compAcc",bSortable:true},
            {data:"curCode",bSortable:true},
            {data:"redblueSign",bSortable:true},
            {data:"debitMoney",bSortable:true},
            {data:"creditMoney",bSortable:true},
            {data:"balance",bSortable:true},
            {data:"summaryContent",bSortable:true},
            {data:"lineNo",bSortable:true},
            {data:"compareSign",bSortable:true},
            {data:"bankId",bSortable:true},
            {data:"billNo",bSortable:true},
            {data:"interestStartDate",bSortable:true},
            {data:"adjustDate",bSortable:true},
            {data:"importDate",bSortable:true},
            {data:"importType",bSortable:true},
            {data:"dataSource",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"importUser",bSortable:true},
            {data:"checkDate",bSortable:true},
            {data:"checkUser",bSortable:true},
            {data:"updateBy",bSortable:true},
            {data:"updateTime",bSortable:true},
            {data:"createBy",bSortable:true},
            {data:"createTime",bSortable:true},
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
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/fccompareacc/add",
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
        content: basePath + "/finacial/check/fccompareacc/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/fccompareacc/update?id=" + data.id,
        end:function(){
            location.reload();
        }
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/finacial/check/fccompareacc/delete/" + data.id, function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}