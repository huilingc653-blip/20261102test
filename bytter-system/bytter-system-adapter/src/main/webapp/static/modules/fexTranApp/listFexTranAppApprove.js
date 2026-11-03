var form, dicts, corp, dateType;
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
        checkbox: true,
        mulselect: true,
        ajax: {
            "url": basePath + "/foreign/fexTranApp/dataTableApprove"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        columns: [
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
                    if ($("#flowStatus").val() == "todo") {
                        html += "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + full.id + "','" + full.taskId + "')\">" + bt.lang("common.button.approve") + "</a>" + split;
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"bt.workFlow.trackFlow('" + full.processInstanceId + "')\">" + bt.lang("common.button.trackFlow") + "</a>";
                    }
                    return html;

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
 * @param taskId
 */
function view(id, taskId) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/foreign/fexTranApp/viewApprove?id=" + id + "&taskId=" + taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranApp/viewApprove?id=" + id + "&taskId=" + taskId,
        end: function () {
            datatable && datatable.draw();
        }
    });
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
