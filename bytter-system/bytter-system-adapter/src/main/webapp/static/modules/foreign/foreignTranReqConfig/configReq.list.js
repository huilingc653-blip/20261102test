var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();


    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexTranReqConfig/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "sortOrder", bSortable: true, className: 'center'},
            {data: "fieldCode", bSortable: true, className: 'center'},
            {data: "fieldName", bSortable: false, className: 'center'},
            {data: "defaultValue", bSortable: false, className: 'center'},
            {data: "isRequired", bSortable: false,className:"center",
                render:function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isRequired);
                }
            },
            {data: "isVisible", bSortable: false,className:"center",
                render:function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isVisible);
                }
            },
            {data: "isEditable", bSortable: false,className:"center",
                render:function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isEditable);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<div style="min-width: 200px;">';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a></div>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexTranReqConfig/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranReqConfig/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexTranReqConfig/update/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexTranReqConfig/update/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function clone(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexTranReqConfig/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranReqConfig/update/" + data.id
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTranReqConfig/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranReqConfig/view/" + data.id
    });
}


//批量删除
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/foreign/fexTranReqConfig/batchDelete", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}




