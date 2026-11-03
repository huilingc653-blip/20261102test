var form, element, corp, dateType, datatable;
bt.use(["jquery", "element", "form", "datatable", "corp","dateType"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();


//监听提交
    form.on("submit(submit)", function (data) {
        if (checkDataTable()) {
            return false;
        }
        var action = "/foreign/fexTranApp/save";
        data.field.status = this.value;
        bt.loading.show();
        $.post(basePath + action, data.field, function (data) {
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
                if (!bt.navtab) {
                    bt.navtab.refreshParentTable();
                } else {
                    bt.navtab.closeCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });

    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        ordering: false,
        ignoreButtons: ["colvis", "output", "print"],
        ajax: {
            "url": basePath + "/foreign/fexTranApp/getDtlTmpDataTable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add(0);
                }
            }
        ],
        searchForm: {
            formElem: "#myform",
            formFilter: "search"
        },
        columns: [
            {
                data: "tranType",
                className: "center",
                render: function (data, type, row, meta) {
                    return bt.dict("fex.tranApp.tranType", data);
                }
            },
            {
                data: "proName",
                className: "center"
            },
            {
                data: "cotpName",
                className: "center"
            },
            {
                data: "tranDate",
                className: "center"
            },
            {
                data: "settleDate",
                className: "center"
            },
            {
                data: null,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    var split = "<span class=\"text-explode\">|</span>"
                    html += "<a href=\"#\" onclick=\"edit('" + full.id + "')\">" + bt.lang("common.button.edit") + "</a>";
                    html += split;
                    html += "<a href=\"#\" onclick=\"deleteById('" + full.id + "')\">" + bt.lang("common.button.delete") + "</a>";
                    return html;
                }
            }
        ]
    });

    //这里填写新的js代码，请参考前端文档
});

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 新增
 */
function add() {
    var corpId = corp.get("corpId").val();
    if (isEmpty(corpId)) {
        layer.alert(bt.lang("table.rat.corp.null"), {icon: 2});
        return false;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexTranApp/addDtl?corpId=" + corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranApp/addDtl?corpId=" + corpId,
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
    var corpId = corp.get("corpId").val();
    if (isEmpty(corpId)) {
        layer.alert(bt.lang("table.rat.corp.null"), {icon: 2});
        return false;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/foreign/fexTranApp/addDtl?corpId=" + corpId + "&id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranApp/addDtl?corpId=" + corpId + "&id=" + id,
        end:function(){
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
 * 保存临时数据id
 * @param id
 */
function setDtlIds(id) {
    var dtlIds = $("#dtlIds").val();
    if (isEmpty(dtlIds)) {
        $("#dtlIds").val(id);
    } else {
        $("#dtlIds").val(dtlIds+","+id);
    }
    if (datatable) {
        datatable.draw();
    }
}


/**
 * 删除明细数据
 * @param id
 */
function deleteById(id) {
    parent.layer.confirm(bt.lang("table.sysTask.deletedsw")+"?",function () {
        var url = "/foreign/fexTranApp/deleteDtlTmp?id=" + id;
        bt.loading.show();
        $.post(basePath + url,function(data){
            bt.loading.hide();
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            datatable.draw();
        },'JSON');
    });
}

/**
 * 校验是否增加明细
 */
function checkDataTable() {
    var empty = $(".dataTables_empty");
    var flag = false;
    if (empty.length > 0) {
        layer.alert(bt.lang("fex.tranApp.tranDtlNotNull"), {icon: 2});
        flag = true;
    }
    return flag;
}