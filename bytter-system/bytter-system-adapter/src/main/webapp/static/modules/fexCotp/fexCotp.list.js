var form, dicts;
bt.use(["jquery", "datatable", "dicts","btImport"], function () {
    $ = bt.jquery;
    form = bt.form();
    dicts = bt.dicts.init();

    importExcel = function () {
        up = bt.btImport.init({
            code: "1000000012",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/foreign/fexCotp/saveImport",
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

    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder: true,
        ordering: false,
        ajax: {
            "url": basePath + "/foreign/fexCotp/dataTable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:fex:cotp:add'),
                text: bt.lang('fex.cotp.addGroup'),
                action: function (e, dt, node, config) {
                    add(0);
                }
            },
            {
                hide: bt.isnotResource('foreign:fex:cotp:add'),
                text: bt.lang('fex.cotp.add'),
                action: function (e, dt, node, config) {
                    add(1);
                }
            },
            {
                text: bt.lang('fex.cotp.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
        ]
        , columns: [
            {
                data: "cotpName",
                className: "center"
            },
            {
                data: "parentName",
                className: "center"
            },
            {
                data: "cotpLevel",
                className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("fex.cotp.level", data);
                }
            },
            {
                data: "shortName",
                className: "center"
            },
            {
                data: "officialName",
                className: "center"
            },
            {
                data: "status",
                className: "center",
                render:function (data, type, full, met) {
                    return bt.dict("fex.status", data);
                }
            },
            {
                data: null,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    var split = "<span class=\"text-explode\">|</span>"
                    if ((0 == full.status || -1 == full.status) && ($("#nowUserId").val() == full.createBy)) {
                        if (bt.isResource("foreign:fex:cotp:edit")) {
                            if (isEmpty(html)) {//编辑
                                html += "<a href=\"#\" onclick=\"edit('" + full.id + "')\">" + bt.lang("common.button.edit") + "</a>";
                            } else {
                                html += split;
                                html += "<a href=\"#\" onclick=\"edit('" + full.id + "')\">" + bt.lang("common.button.edit") + "</a>";
                            }
                        }
                        if (bt.isResource("foreign:fex:cotp:delete")) {
                            if (isEmpty(html)) {//删除
                                html += "<a href=\"#\" onclick=\"deleteById('" + full.id + "')\">" + bt.lang("common.button.delete") + "</a>";
                            } else {
                                html += split;
                                html += "<a href=\"#\" onclick=\"deleteById('" + full.id + "')\">" + bt.lang("common.button.delete") + "</a>";
                            }
                        }
                    }
                    if (bt.isResource("foreign:fex:cotp:view")) {
                        if (isEmpty(html)) {//查看
                            html += "<a href=\"#\" onclick=\"view('" + full.id + "')\">" + bt.lang("common.button.view") + "</a>";
                        } else {
                            html += split;
                            html += "<a href=\"#\" onclick=\"view('" + full.id + "')\">" + bt.lang("common.button.view") + "</a>";
                        }
                    }

                    if (95 == full.status) {
                        if (bt.isResource("foreign:fex:cotp:tovoid")) {
                            if (isEmpty(html)) {//作废
                                html += "<a href=\"#\" onclick=\"tovoidOrEnable('" + full.id + "',-3)\">" + bt.lang("common.button.tovoid") + "</a>";
                            } else {
                                html += split;
                                html += "<a href=\"#\" onclick=\"tovoidOrEnable('" + full.id + "',-3)\">" + bt.lang("common.button.tovoid") + "</a>";
                            }
                        }
                        if (bt.isResource("foreign:fex:cotp:edit")&& ($("#nowUserId").val() == full.createBy)) {
                            if (isEmpty(html)) {//变更
                                html += "<a href=\"#\" onclick=\"edit('" + full.id + "')\">" + bt.lang("netbank.button.change") + "</a>";
                            } else {
                                html += split;
                                html += "<a href=\"#\" onclick=\"edit('" + full.id + "')\">" + bt.lang("netbank.button.change") + "</a>";
                            }
                        }
                    }

                    if (-3 == full.status) {
                        if (bt.isResource("foreign:fex:cotp:tovoid")) {
                            if (isEmpty(html)) {//启用
                                html += "<a href=\"#\" onclick=\"tovoidOrEnable('" + full.id + "',95)\">" + bt.lang("common.button.enable") + "</a>";
                            } else {
                                html += split;
                                html += "<a href=\"#\" onclick=\"tovoidOrEnable('" + full.id + "',95)\">" + bt.lang("common.button.enable") + "</a>";
                            }
                        }
                    }
                    return html;
                }
            }
        ]
    });

    //这里填写新的js代码，请参考前端文档
});

/**
 * 新增
 * @param cotpLeve
 */
function add(cotpLeve) {
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexCotp/add?cotpLeve=" + cotpLeve);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexCotp/add?cotpLeve=" + cotpLeve,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

/**
 * 编辑
 * @param id
 */
function edit(id) {
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/foreign/fexCotp/edit?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexCotp/edit?id=" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

/**
 * 删除
 * @param id
 */
function deleteById(id) {
    parent.layer.confirm(bt.lang("table.sysTask.deletedsw")+"?",function () {
        var url = "/foreign/fexCotp/delete?id=" + id;
        bt.loading.show();
        $.post(basePath + url,function(data){
            bt.loading.hide();
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            datatable.draw();
        },'JSON');
    });
}

/**
 * 查看
 * @param id
 */
function view(id) {
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('table.query.message'), basePath + "/foreign/fexCotp/view?id=" + id+"&flag=1");
        return;
    }
    parent.layer.open({
        type: 2,
        title: bt.lang('table.query.message'),
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexCotp/view?id=" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}


/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}

/**
 * 作废，或者启用
 * @param id
 */
function tovoidOrEnable(id, status) {
    var url = "/foreign/fexCotp/tovoidOrEnable";
    bt.loading.show();
    $.post(basePath + url, {"id": id, "status": status},
        function (data) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            datatable.draw();
        },
        'JSON');
}
