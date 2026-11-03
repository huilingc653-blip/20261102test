var $, form, moneyType, moneyFun, laydate;
bt.use(['form', 'datatable', 'dicts', 'btImport', 'moneyType', 'corp', 'dateType', 'laydate'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    laydate = layui.laydate;

    // 日期控件
    laydate.render({
        elem: '#years', //指定元素
        type: 'year' //只提供年列表选择
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        resize: false,
        checkbox: false, order: [1, 'desc'],
        ajax: {
            'url': basePath + "/cms/rat/yearFinPlanRegister/datatable"
        },
        searchForm: {
            formFilter: "search",
            formElem: "#searchForm"
        },
        buttons: [
            {
                hide: bt.isnotResource('cms:rat:yearFinPlanRegister:apply'),//应用权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('cms:rat:yearFinPlanRegister:apply'),//应用权限
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importYearFinPlan();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: false, className: 'center'},
            {data: "years", bSortable: false, className: 'center'},
            {
                data: "yearPurchaseAmount", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.yearPurchaseAmount);
                }
            },
            {
                data: "mergeInvestmentAmount", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.mergeInvestmentAmount);
                }
            },
            {
                data: "solveMeasure", bSortable: false, className: 'center',
            },
            {
                data: "existDifficulty", bSortable: false, className: 'center',
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('cms:rat:yearFinPlanRegister:apply')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>\
                	    <a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

// 新增
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/rat/yearFinPlanRegister/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/yearFinPlanRegister/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

// 导入年度融资规划
function importYearFinPlan() {
    up = bt.btImport.init({
        code: "70000065",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/cms/rat/yearFinPlanRegister/saveImport",
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
}

// 查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/yearFinPlanRegister/view/" + data.id
    });
}

// 编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/rat/yearFinPlanRegister/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/rat/yearFinPlanRegister/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

// 删除
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/cms/rat/yearFinPlanRegister/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}