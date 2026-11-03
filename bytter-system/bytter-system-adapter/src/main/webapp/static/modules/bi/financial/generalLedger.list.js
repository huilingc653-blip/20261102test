bt.use(['form', 'datatable', 'dateType', 'selectType', 'corp', 'jquery', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    beforeInitSubject = function (nodes) {
        var newNodes = [];
        for (k in nodes) {
            var node = nodes[k];
            node.subjectNameText = "(" + node.subjectCode + ") " + node.subjectName;
            newNodes.push(nodes[k]);
        }
        return newNodes;
    }

    var selectType = bt.selectType.init();
    bt.corp.init();
    var dicts = bt.dicts.init();

    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: false,
        checkbox: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bi/financial/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {
                data: "netName", className: 'center', bSortable: false
            },
            {
                data: "curName", className: 'center', bSortable: false
            },
            {
                data: "ledgerDate", className: 'center', bSortable: false,
                render: function (data, type, full, meta) {
                    const startDate = dateType.get("beginDate").val() + "-01";
                    const endDate = dateType.get("endDate").val();
                    let endDateSplit = endDate.split("-");
                    const lastDay = new Date(endDateSplit[0], endDateSplit[1], 0).getDate();
                    let lastDate = endDate + "-" + lastDay;
                    return startDate + "~" + lastDate;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

    view = function (el) {
        var subjectId = selectType.get("subjectId").getData();
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var param = {
            "netId": data.netId,
            "netName": data.netName,
            "curId": data.curId,
            "curName": data.curName,
            "balanceWay": subjectId.obj ? subjectId.obj.balanceWay : "",
            "subjectId": subjectId.obj ? subjectId.obj.id : "",
            "showDetailSubject": dicts.get("showDetailSubject").val(),
            "beginDate": dateType.get("beginDate").val(),
            "endDate": dateType.get("endDate").val(),
        };
        var url = basePath + "/bi/financial/viewDetailLedger";
        url += "?showDetailSubject=" + param.showDetailSubject + "&beginDate=" + param.beginDate + "&endDate=" + param.endDate;
        url += "&subjectId=" + param.subjectId;
        layer.open({
            type: 2,
            id: "viewDetailLedger",
            area: ['100%', '100%'],
            maxmin: true,
            closeBtn: 1,
            content: url,
            success: function (layero, index) {
                $(layero).find("iframe").contents().find("#netId").val(param.netId);
                $(layero).find("iframe").contents().find("#netName").html(param.netName);
                $(layero).find("iframe").contents().find("#curId").val(param.curId);
                $(layero).find("iframe").contents().find("#curName").html(param.curName);
                $(layero).find("iframe").contents().find("#balanceWay").val(param.balanceWay);
                $(layero).find("iframe").contents().find("#subjectId").val(param.subjectId);
                $(layero).find("iframe").contents().find("#showDetailSubject").val(param.showDetailSubject);
                $(layero).find("iframe").contents().find("#beginDate").val(param.beginDate);
                $(layero).find("iframe").contents().find("#endDate").val(param.endDate);
            }
        });
    }
});
