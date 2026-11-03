bt.use(['form','datatable','dicts','btImport'], function(){
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
            'url': basePath+"/netbank/bankAccUse/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('netbank:bankAccUse:edit'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
            {
                text: bt.lang('common.button.imports'),
                hide:bt.isnotResource('netbank:bankAccUse:edit'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            }
        ]
        ,columns: [
            {data:"useCode",bSortable:true,className:"center"},
            {data:"useName",bSortable:true},
            {data:"useRmk",bSortable:true},
            {data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
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
            content: basePath + "/netbank/bankAccUse/add",
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }
    update = function (el){
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '500px'],
            maxmin: true,
            content: basePath + "/netbank/bankAccUse/update/"  + data.id,
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }

    del = function (el){
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/netbank/bankAccUse/delete/" + data.id, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    if (result.success) {
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }, 'JSON');
        });

    }

    /**
     * 导入功能
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000087",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/netbank/bankAccUse/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    };
});
