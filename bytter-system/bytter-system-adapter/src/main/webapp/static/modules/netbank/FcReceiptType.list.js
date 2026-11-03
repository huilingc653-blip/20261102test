bt.use(['form','datatable','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        query: "search",
        checkbox:false,
        order: [[0,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/receipttype/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"receiptCode",bSortable:true,className:"center"},
            {data:"receiptName",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:"validSign",bSortable:true,className:"center",
                render: function ( data, type, full, meta ) {
                    return bt.dict("valid_sign",full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    html += '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });

    function add(){
        parent.layer.open({
            type: 2,
            area: ['850px', '500px'],
            maxmin: true,
            content: basePath + "/netbank/receipttype/add",
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }

    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/receipttype/view/" + data.id
        });
    }

    update = function (el){
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '500px'],
            maxmin: true,
            content: basePath + "/netbank/receipttype/update/"  + data.id,
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }

    del = function (el){
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/netbank/receipttype/delete/" + data.id, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    if (result.success) {
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }, 'JSON');
        });

    }
});