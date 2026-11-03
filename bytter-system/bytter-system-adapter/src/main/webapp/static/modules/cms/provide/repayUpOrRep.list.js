bt.use(['form', 'datatable', 'corp', 'moneyType', 'dateType','dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();

    bt.dicts.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        ajax: {
            'url': dataUrl
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
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
            {data: "corpName"},
            {data: "billCode"},
            {data: "createDate"},
            {data: "infoBillCode"},
            {data: "sendCode"},
            {data: "curName", className: "center"},
            {
                data: "repayMoney", className: "textRight", render: function (data, type, full, meta) {
                    return bt.money.format(full.repayMoney);
                }
            },
            {data: "repayDate"},
            {
                data: "onlineSign", render: function (data, type, full, meta) {
                    return bt.dict("cms.leasepay.online.sign", data);
                }
            },
            {
                data: "isAdvancedRepay",
                className:"center",
                render: function (data, type, full, meta) {
                    return bt.dict("cms.repay.isAdvancedRepay", data);
                }
            },
            {
                data: "status", render: function (data, type, full, meta) {
                    return bt.dict('cms.bizStatus', full.bizStatus);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('cms:provide:repqy:edit')) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a> ';
                        if (upRepSing == "1") {//修改
                            html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("cms.loanRepay.update") + '</a> ';
                            if (bt.isResource('cms:provide:repay:delete')) {
                                html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a> ';
                            }
                        } else {//补录
                            html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="' + meta.row + '" onclick="repair(this)">' + bt.lang("common.button.makeup") + '</a> ';
                        }
                    }
                    html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="' + meta.row + '" onclick="listMsg(this)">' + bt.lang("common.button.logInfo") + '</a>';
                    // html += '\<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7', //汇总列序号
                    dataName: 'repayMoney' //汇总列还款金额
                }
            ]
        }
    });
});


function edit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var onlineSign = "0"
    var tempOrSubmitSign = $("#tempOrSubmitSign").val();
    if ("1" == data.onlineSign) {
        onlineSign = "1";
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/provide/loanrepay/edit?id=" + data.id + "&tempOrSubmitSign=" + tempOrSubmitSign + "&onlineSign=" + onlineSign);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loanrepay/edit?id=" + data.id + "&onlineSign=" + data.onlineSign,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/cms/provide/loanrepay/viewRepay?sendBillCode=" + data.sendCode + "&repayId=" + data.id + "&show=1");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loanrepay/viewRepay?sendBillCode=" + data.sendCode + "&repayId=" + data.id + "&show=1"
    });
}

function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/cms/provide/loanrepay/delete?id=" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


function repair(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/provide/loanrepay/editRepair?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/provide/loanrepay/editRepair?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//日志列表
function listMsg(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'), basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);

        }
    }
}
