var $, form, dataTable, corp;
bt.use(['form', 'datatable', 'corp', 'select', 'dateType', 'dicts', 'selectType', 'conType'], function () {
    $ = bt.jquery, form = bt.form();
    //生成列表
    dataTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/contract/conCommon/conSelectData"
        }, columns: [
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.conSelectOp(this)">' + bt.lang("common.button.select") + '</a>';
                    html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.conView(\'' + full.id + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    var corpName = full.corpName;
                    if (full.corpCode) {
                        corpName = '(' + full.corpCode + ')' + corpName;
                    }
                    return corpName;
                }
            },
            {data: "typeName", bSortable: true},
            {data: "conName", bSortable: true},
            {data: "conNo", bSortable: true},
            {
                data: "signTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_SIGN_TYPE", full.signType);
                }
            },
            {data: "draftDate", bSortable: true},
            {data: "signDate", bSortable: true},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "statusName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_INFO_STATUS", full.status);
                }
            }
        ]
    });
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    corp = bt.corp.init();
    bt.conType.init();
});

//单位回调函数
function corpCallback(data) {
    if (data) {
        if (data instanceof Array) {
            var corpId = "";
            for (j = 0, len = data.length; j < len; j++) {
                if (corpId) {
                    corpId += ",";
                }
                corpId += data[j].id;
            }
            bt.conType.get("conType").init({"corpId": corpId});
        } else {
            bt.conType.get("conType").init({"corpId": data.id});
        }
    }
}

bt.conSelectOp = function (el) {
    var data = dataTable.row(layui.jquery(el).data("row")).data();
    if (data) {
        var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
        var func = top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-conSelect']['callback'];
        if (func) {
            func(data);
        }
    }
}
bt.conView = function (conId) {
//    if(bt.navtab){
//        bt.navtab.addNavtab(bt.lang('common.button.view') ,
//        		basePath + "/contract/conCommon/conView?id=" + conId);
//        return;
//    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/contract/conCommon/conView?id=" + conId
    });
}
