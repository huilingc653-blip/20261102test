var billTable = {};
bt.use(['form', 'datatable', 'btdate', 'select', 'dateType', 'dicts', 'conType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.conType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[6,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/contract/condraft/datatable"
        },
        buttons: [
            //新增、导出
            {
                text: bt.lang('common.button.add'),
                hide: bt.isnotResource('con:condraft:resource:op'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "conName", bSortable: true},
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "typeName", bSortable: true},
            {data: "conNo", bSortable: true},
            {
                data: "signTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_SIGN_TYPE", full.signType);
                }
            },
            {data: "draftDate", bSortable: true},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "statusName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_DRAFT_STATUS", full.status);
                }
            },
            {data: "backMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (data.status == 0 || data.status == -1 || data.status == -2) {//暂存、审批打回、-2签署打回
                        if (bt.isResource('con:condraft:resource:op', full.corpId)) {
                            html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                        }
                        if (bt.isResource('con:condraft:resource:op', full.corpId)) {
                            html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                        }
                    }
                    return html;
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2
        }
    });
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
});

//新增跳转
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/contract/condraft/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/contract/condraft/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//更新跳转
function update(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var rid = data.rid;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/contract/condraft/edit/" + rid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/contract/condraft/edit/" + rid,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//删除数据
function deleteColumn(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var rid = data.rid;
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/contract/condraft/delete", {"rid": rid}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//查看详细
function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var rid = data.rid;

    /*if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abemitbill/view/" + aid);
        return;
    }
*/
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/contract/conCommon/conView?recordId=" + rid
    });
}
