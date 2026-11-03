bt.use(['form', 'datatable', 'corp', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();

    /**
     * 导入Excel
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000003",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/netbank/nispersonneldetail/saveImport",
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

    /**
     * 生成列表
     */
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nispersonneldetail/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:nispersonneldetail:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide:bt.isnotResource('netbank:nispersonneldetail:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            }, {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
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
            {data: "workNo", bSortable: true,className:'center'},
            {data: "userName", bSortable: true},
            {
                data: "sex", bSortable: true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sex", full.sex);
                }
            },
            {data: "idcard", bSortable: true,className:'center'},
            {data: "mobile", bSortable: true,className:'center'},
            {data: "corpName", bSortable: true},
            {data: "deptName", bSortable: true},
            {data: "bankCode", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:nispersonneldetail:apply',full.corpId)){
                    html+='<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html+='<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
                html+='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }
            }
        ]
    });

    /**
     * 新增
     */
    function add() {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.nisPersonnelDetail.btn.create'), basePath + "/netbank/nispersonneldetail/add");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nispersonneldetail/add",
            end: function () {
                datatable && datatable.draw();
            }
        });
    }

    /**
     * 查询
     * @param el
     */
    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nispersonneldetail/view/" + data.id
        });
    }

    /**
     * 修改
     * @param el
     */
    update = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('table.nisPersonnelDetail.btn.update'), basePath + "/netbank/nispersonneldetail/update?id=" + data.id);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/netbank/nispersonneldetail/update?id=" + data.id,
                end: function () {
                    datatable && datatable.draw();
                }
            });
        }
    }

    /**
     * 删除
     * @param el
     */
    deleteColumn = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/netbank/nispersonneldetail/delete/" + data.id, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    /**
     * 批量删除
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    function batchDelete(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
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
            $.post(basePath + "/netbank/nispersonneldetail/batchDelete/", {ids: ids}, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            }, 'JSON');
        });
    }
});
