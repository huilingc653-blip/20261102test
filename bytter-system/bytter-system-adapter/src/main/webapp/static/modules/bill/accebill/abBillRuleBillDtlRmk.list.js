bt.use(['form', 'datatable', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },
            {
                text: bt.lang('common.button.batchDisable'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    disable(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "bankTypeName", bSortable: true, className: "center"},
            {data: "bustypeName", bSortable: true, className: "center"},
            {data: "statusName",bSortable:true, className: "center"},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    if (bt.isResource('ab:bill:resource:rulebilltlrmk:query',full.corpId)) {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                            html += '<span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/add",
        end: function () {
            appTable && appTable.draw();
        }
    });
}

//跳转到编辑页面
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/update?id=" + data.id,
            end:function(){
                appTable && appTable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        if(data.mark === "1"){
            layer.alert(bt.lang('accebill.message.error.claimaccount.validsign'), {icon: 7});
            return;
        }else{
            $.post(basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/delete/" + data.id, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if(result.success){
                    bt.navtab.refreshParent();
                    bt.navtab.refreshCurrent();
                }
            }, 'JSON');
        }
    });
}

//批量删除数据
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        var ids;
        for (var i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].id;
            } else {
                ids = rowData[i].id;
            }
        }
        $.post(basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/batchDelete/" + ids, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.navtab.refreshParent();
                bt.navtab.refreshCurrent();
            }
        }, 'JSON');
    });
}

//批量禁用数据
function disable(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    parent.layer.confirm(bt.lang('common.message.confirm.disable'), function () {
        var ids;
        for (var i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].id;
            } else {
                ids = rowData[i].id;
            }
        }
        $.post(basePath + "/bill/accebillClaim/config/ruleBillDtlRmk/disable/" + ids, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.navtab.refreshParent();
                bt.navtab.refreshCurrent();
            }
        }, 'JSON');
    });
}
