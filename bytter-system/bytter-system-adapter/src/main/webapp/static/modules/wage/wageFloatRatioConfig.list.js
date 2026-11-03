var $, form, datatable;
bt.use(['form', 'datatable', 'mulselect', 'btImport', 'dicts', 'rateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.mulselect.init();
    bt.rateType.init();//利率
    dicts = bt.dicts.init();//字典

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        ajax: {
            'url': basePath + "/wage/wageFloatRatio/datatable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:wage:floatRatio:apply'),//应用权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    addWageFloatRatio();
                }
            },
            {
                hide: bt.isnotResource('netbank:wage:floatRatio:query'),//应用权限
                text: '操作记录查询',
                action: function (e, dt, node, config) {
                    operationRecords();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {
                data: "month", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.months", full.month);
                }
            },
            {
                data: "floatRate", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.rateType.getFun().format(full.floatRate, 'percent', 2, ',');
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:wage:floatRatio:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="updateWageFloatRatio(this)">' + bt.lang("common.button.edit") + '</a>';
                    }
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="delWageFloatRatio(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });


});

// 新增
function addWageFloatRatio() {
    parent.layer.open({
        type: 2,
        area: ['900px', '580px'],
        maxmin: true,
        content: basePath + "/wage/wageFloatRatio/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

// 编辑
function updateWageFloatRatio(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.open({
            type: 2,
            area: ['900px', '580px'],
            maxmin: true,
            content: basePath + "/wage/wageFloatRatio/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

// 删除
function delWageFloatRatio(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/wage/wageFloatRatio/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable && datatable.draw();
            }
        }, 'JSON');
    });
}

// 操作记录查询
function operationRecords() {
    bt.navtab.addNavtab("操作记录查询", basePath + "/wage/wageFloatRatio/listMessage");
}