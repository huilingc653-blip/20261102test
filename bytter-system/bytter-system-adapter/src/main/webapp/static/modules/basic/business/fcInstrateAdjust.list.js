var datatable;
bt.use(['form', 'datatable', 'selectType', 'corp', 'money', 'rateType', 'dicts', 'date', 'bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var select = bt.selectType.init();
    var rate = bt.rateType.getFun();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false, order: [[7, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/fcinstrateadjust/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.adjust'),
                action: function (e, dt, node, config) {
                    add();
                }
            }
        ]
        , columns: [
            {
                data: "netId", bSortable: true,
                render: function (data, type, full, meta) {
                    var poolSign = $("#poolSign").val();
                    if(poolSign === "1"){
                        return full.netPoolName;
                    }else{
                        return full.netName;
                    }
                }
            },
            {
                data: "subjectId", bSortable: true,
                render: function (data, type, full, meta) {
                    var poolSign = $("#poolSign").val();
                    if(poolSign === "1"){
                        return full.subjectPoolName;
                    }else{
                        return full.subjectName;
                    }
                }
            },
            {data: "account", bSortable: true},
            {
                data: "curId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.curName;
                }
            },
            {
                data: "interestId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.interestName;
                }
            },
            {
                data: "oldInstrate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return rate.format(full.oldInstrate, 'percent');
                }
            },
            {
                data: "newInstrate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return rate.format(full.newInstrate, 'percent');
                }
            },
            {
                data: "adjustTime", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.adjustTime, "yyyy-mm-dd")
                }
            },
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.statusName;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    if (full.status == "-1") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                        html += ' <a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                        html += ' <a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    } else {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

});

function netChange(id) {

}

function add() {
    var poolSign = $("#poolSign").val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.adjust'), basePath + "/basic/business/fcinstrateadjust/adjust/" + poolSign);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/fcinstrateadjust/adjust/" + poolSign,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var poolSign = $("#poolSign").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/basic/business/fcinstrateadjust/view/" + data.id +"/" + poolSign);
            return;
        }

        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/fcinstrateadjust/view/" + data.id + "/" + poolSign
        });
    }
}

function update(el) {
    var poolSign = $("#poolSign").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/basic/business/fcinstrateadjust/update/"+ poolSign +"?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/fcinstrateadjust/update/"+ poolSign +"?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/basic/business/fcinstrateadjust/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function beforeInit(text, data) {
    if (data.subjectCode) {
        return "(" + data.subjectCode + ")" + data.subjectName;
    }
    return text;
}

/**
 * 资金池回调
 * @param data
 */
function netIdCallback(data) {
    netIdCallback(data);
}
