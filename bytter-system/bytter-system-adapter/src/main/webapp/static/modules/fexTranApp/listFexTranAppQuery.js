var form, dicts, selectType, corp, dateType;
bt.use(["jquery", "datatable", "dicts","corp","dateType","selectType"], function () {
    $ = bt.jquery;
    form = bt.form();
    dicts = bt.dicts.init();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();

    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder: true,
        ordering: false,
        ajax: {
            "url": basePath + "/foreign/fexTranApp/dataTableQuery"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        }
        , columns: [
            {
                data: "billNo",
                className: "center"
            },
            {
                data: "corpName",
                className: "center"
            },
            {
                data: "appDate",
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
                    if (bt.isResource("foreign:fex:tranApp:view")) {
                        if (isEmpty(html)) {
                            html += "<a href=\"#\" onclick=\"view('" + full.id + "')\">" + bt.lang("common.button.view") + "</a>";
                        } else {
                            html += split;
                            html += "<a href=\"#\" onclick=\"view('" + full.id + "')\">" + bt.lang("common.button.view") + "</a>";
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
 * 查看
 * @param id
 */
function view(id) {
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/foreign/fexTranApp/view?id=" + id+"&flag=1");
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
 * 根据交易类型获取产品
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function getPro(data,text,selectedData,beforeValue) {
    selectType.get("proId").attr({
        "data-value": "",
        "data-url": basePath + "/foreign/fexproduct/listProduct/" + selectedData.value
    });
    selectType.render();
    form.renderIcon();
}