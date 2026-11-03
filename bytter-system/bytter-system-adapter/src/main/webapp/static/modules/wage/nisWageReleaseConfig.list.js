bt.use(['form', 'datatable', 'corp', 'selectType', 'dateType', 'dicts', 'btImport', 'mulselect', 'accountTable'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    bt.dateType.init(); //日期
    bt.corp.init();
    bt.selectType.init();
    bt.mulselect.init();

    bt.accountTable.init({
        urldata: {
            url: basePath + "/wage/wageReleaseConfig/getBankAccQuery"
        },
        showCallback:function (inputElem) {
            // 重新根据单位ID加载账户表格
            const bizName = inputElem.data('biz-name');
            const ids = $('[name="ins_corpId"]').val()
            bt.accountTable.get(bizName).reload({
                urldata: {
                    url: basePath + "/wage/wageReleaseConfig/getBankAccQuery?corpIds=" + ids
                }
            });
        },
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        ajax: {
            'url': basePath + "/wage/wageReleaseConfig/datatable"
        },
        searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },
        buttons: [
            {
                hide: bt.isnotResource('wage:release:config:add'), //新增权限
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importNisWageReleaseConfig();
                }
            },
            {
                hide: bt.isnotResource('wage:release:config:add'), //新增权限
                text: bt.lang('批量设置金额'),
                action: function (e, dt, node, config) {
                    const elements = dt.rows('.selected').data();
                    batchSetWageReleaseConfig(elements);
                }
            },
            {
                hide: bt.isnotResource('wage:release:config:add'), //新增权限
                text: bt.lang('批量确认'),
                action: function (e, dt, node, config) {
                    const elements = dt.rows('.selected').data();
                    batchConfirm(elements, 1)
                }
            },
            {
                hide: bt.isnotResource('wage:release:config:add'), //新增权限
                text: bt.lang('批量反确认'),
                action: function (e, dt, node, config) {
                    const elements = dt.rows('.selected').data();
                    batchConfirm(elements, 0)
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
            { data: "corpCode", bSortable: false, className: 'center' },
            { data: "corpName", bSortable: false, className: 'center' },
            { data: "bankAcc", bSortable: false, className: 'center' },
            { data: "bankAccName", bSortable: false, className: 'center' },
            { data: "bankName", bSortable: false, className: 'center' },


            { data: "lastMonthReleaseAmount", sortable: false, className: "textRight",
                render: function(data, type, full, meta) { return bt.money.format(full.lastMonthReleaseAmount); }
            },
            { data: "releaseAmount", sortable: false, className: "textRight",
                render: function(data, type, full, meta) { return bt.money.format(full.releaseAmount); }
            },
            {
                data: "status", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nis.wage.release.status", data);
                }
            },
            { data: "confirmName", bSortable: false, className: 'center' },
            { data: "confirmTime", bSortable: false, className: 'center' },


            { data: "generateRequire", sortable: false,
                render: function(data, type, full, meta) { return full.generateRequire == '1' ? "是" : "否" }
            },
            { data: "generateTime", bSortable: false, className: 'center' },
            { data: "totalReleaseMoney", sortable: false, className: "textRight",
                render: function(data, type, full, meta) { return bt.money.format(full.totalReleaseMoney); }
            },


            { data: null, bSortable: false, className: 'center', render: function(data, type, full, meta) {
                    const elements = [];

                    const currentYearMonth = new Date().toISOString().slice(0, 7); // "2025-11"
                    // 本月暂存数据
                    if (data.releaseDate.startsWith(currentYearMonth) && data.status === 0) {
                        // 拥有编辑权限
                        if (bt.isResource('wage:release:config:edit')) {
                            elements.push('<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a>')
                        }
                        // 拥有删除权限
                        if (bt.isResource('wage:release:config:delete')) {
                            elements.push('<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>')
                        }
                    }

                    return elements.join(" ");
                }
            }
        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '5',
                    dataName: 'lastMonthReleaseAmount'
                },
                {
                    colNum: '6',
                    dataName: 'releaseAmount'
                }
            ]
        },
    });
});

// 编辑
function edit(el){
   const data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
   bt.navtab.addNavtab(bt.lang('修改数据'),
       basePath + "/wage/wageReleaseConfig/edit?id=" + data.id + "&releaseAmount=" + data.releaseAmount);
}

function del(el){
    const data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    doDelete([data.id])
}

// 批量删除
function batchDelete(e, dt, node, config) {
    const data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    const ids = [];
    const rowData = data.splice(0, data.length);
    for (let i = 0; i < rowData.length; i++) {
        ids.push(rowData[i].id);
    }
    doDelete(ids);
}

function doDelete(ids) {
    if (!ids || ids.length === 0) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function(){
        bt.loading.show();
        $.post(basePath + "/wage/wageReleaseConfig/deleteByIds?ids=" + ids.join(","), function(result){
            bt.loading.hide();
            bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function batchConfirm(elements, status) {
    if (!elements.length) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    const ids = [];
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        if (!validElement(element)) return;
        ids.push(element.id);
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/wage/wageReleaseConfig/batchConfirm", {
            ids: ids.join(","),
            status: status
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function validElement(element) {
    if (!element) return;
    // 校验是否是当月
    const currentYearMonth = new Date().toISOString().slice(0, 7); // "2025-11"
    if (!element.releaseDate.startsWith(currentYearMonth)) {
        layer.alert("非本月数据，不允许进行修改", {icon: 7});
        return false;
    }

    // 校验是否生成需求清单
    if (element.generateRequire === 1) {
        layer.alert("已生成需求清单，不允许进行修改", {icon: 7});
        return false;
    }
    return true;
}

function accountCallBack(data) {
  $("#bankAcc").val(data.bankAcc);
}

// 导入
function importNisWageReleaseConfig() {
    up = bt.btImport.init({
        code: "202511240001",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/wage/wageReleaseConfig/saveImport",
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

function batchSetWageReleaseConfig(elements) {
    if (!elements.length) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    const ids = [];
    for (let i = 0; i < elements.length; i++) {
        if (elements[i].status === 1) {
            layer.alert("已确认的数据，不允许进行修改", {icon: 7});
            return;
        }
        ids.push(elements[i].id);
    }
    bt.navtab.addNavtab(bt.lang('批量设置金额'), basePath + "/wage/wageReleaseConfig/batchSetWageRelease?ids=" + ids);
}
