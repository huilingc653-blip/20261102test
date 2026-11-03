bt.use(['form', 'moneyType', 'datatable', 'corp', 'selectType', 'bank', 'btImport', 'dicts','money'], function () {
    var $ = bt.jquery, form = bt.form();
    var mt = bt.moneyType.init();
    bt.corp.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

    importExcel = function () {
        up = bt.btImport.init({
            code: "10000009",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/inneraccount/fcinitacc/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        location.reload();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }
    //生成列表
    var initM = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true, order: [[1, 'desc']],
        scrollY: '400px',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcinitacc/tableInit"
        },
        buttons: [{
            hide: bt.isnotResource('netbank:expense:nisExpenseDetail:touchApply'),
            text: bt.lang('common.button.imports'),
            action: function (e, dt, node, config) {
                importExcel();
            }
        }]
        , columns: [
            {
                data: "account", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.account + '<input id="' + meta.row + 'account"  name="fcInitAccs[' + meta.row + '].account" type="hidden" value="' + full.account + '">';
                }
            },
            {
                data: "accName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = '<input id="' + meta.row + 'balanceWay"  name="fcInitAccs[' + meta.row + '].balanceWay" type="hidden" value="' + full.balanceWay + '">';
                    html += '<input id="' + meta.row + 'id"  name="fcInitAccs[' + meta.row + '].id" type="hidden" value="' + full.id + '">';
                    return full.accName + html;
                }
            },
            {
                data: "curId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.curName + '<input id="' + meta.row + 'curId"  type="hidden" value="' + full.curId + '">';
                }
            },
            {
                data: "netId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.netName + '<input id="' + meta.row + 'netId"  type="hidden" value="' + full.netId + '">';
                }
            },
            {
                data: "debitMoney", bSortable: false, width: "200px",
                render: function (data, type, full, meta) {
                    if (full.balanceWay == 1) {
                        return '<input type="text" data-name="fcInitAccs[' + meta.row + '].debitMoney"   id="' + meta.row + 'debitMoney" value="' + (full.balance == null ? 0 : full.balance) + '"  class="layui-input bt-biz-moneyType" data-minus="true" autocomplete="off"/>';
                    } else {
                        return '';
                    }
                }
            },
            {
                data: "creditMoney", bSortable: false, width: "200px",
                render: function (data, type, full, meta) {
                    if (full.balanceWay == -1) {
                        return '<input type="text" data-name="fcInitAccs[' + meta.row + '].creditMoney"  id="' + meta.row + 'creditMoney" value="' + (full.balance == null ? 0 : full.balance) + '" class="layui-input bt-biz-moneyType" data-minus="true" autocomplete="off"/>';
                    } else {
                        return '';
                    }
                }
            },
            {
                data: "cumulation", bSortable: false, width: "200px",
                render: function (data, type, full, meta) {
                    return '<input type="text" data-name="fcInitAccs[' + meta.row + '].cumulation"  id="' + meta.row + 'cumulation" value="' + (full.cumulation == null ? 0 : full.cumulation) + '" class="layui-input bt-biz-moneyType" data-minus="true" autocomplete="off"/>';
                }
            },
            {
                data: "yearBalance", bSortable: false, width: "200px",
                render: function (data, type, full, meta) {
                    return '<input type="text" data-name="fcInitAccs[' + meta.row + '].yearBalance"  id="' + meta.row + 'yearBalance" value="' + (full.yearBalance == null ? 0 : full.yearBalance) + '" class="layui-input bt-biz-moneyType" data-minus="true" autocomplete="off"  lay-verify="required"/>';
                }
            },
            {
                data: "yearTotal", bSortable: false, width: "200px",
                render: function (data, type, full, meta) {
                    return '<input type="text" data-name="fcInitAccs[' + meta.row + '].yearTotal"  id="' + meta.row + 'yearTotal" value="' + (full.yearTotal == null ? 0 : full.yearTotal) + '"class="layui-input bt-biz-moneyType" data-minus="true"  autocomplete="off"  lay-verify="required"/>';
                }
            },
            {data: "accStartDate", bSortable: true},
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('inneraccount.fcInitStatus', full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:innerAccount:fcInitAcc:apply',full.corpId) && (full.status == "0" || full.status == "-1")) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: false,
            allSum: false,
            selectSum: true,
            outerData: true,//利用表格输入的数据进行汇总
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5',
                    dataName: 'debitMoney'
                },
                {
                    colNum: '6',
                    dataName: 'creditMoney'
                }
            ]
        }, btInitComplete: function (setting, json) {
            mt.render();
        },
        btDrawCallback: function (setting, json) {
            mt.render();
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var accState = $(this).attr("value");
        var dt = initM.rows('.selected').data();
        var len = dt.length;
        if (len < 1) {
            bt.loading.hide();
            layer.msg(bt.lang('table.fcBankBill.msg.chooseLeastOne'));
            $(this).removeClass('layui-btn-disabled')
            return false;
        }
        var indexs = initM.rows('.selected').indexes();
        var rowData = dt.splice(0, len);
        var obj = new Object();
        obj.status = accState;
        var curId = "", netId = "", accStartDate = "";
        var flag = false, flag2 = false, flag3 = false;
        var arrDebitMoney = []; //借方总额
        var arrCreditMoney = [];//贷方总额
        for (var i = 0; i < rowData.length; i++) {
            obj["fcInitAccs[" + i + "].account"] = $("#" + indexs[i] + "account").val();
            obj["fcInitAccs[" + i + "].balanceWay"] = $("#" + indexs[i] + "balanceWay").val();
            obj["fcInitAccs[" + i + "].debitMoney"] = $("#" + indexs[i] + "debitMoney").val();
            obj["fcInitAccs[" + i + "].creditMoney"] = $("#" + indexs[i] + "creditMoney").val();
            obj["fcInitAccs[" + i + "].cumulation"] = $("#" + indexs[i] + "cumulation").val();
            obj["fcInitAccs[" + i + "].yearBalance"] = $("#" + indexs[i] + "yearBalance").val();
            obj["fcInitAccs[" + i + "].yearTotal"] = $("#" + indexs[i] + "yearTotal").val();
            obj["fcInitAccs[" + i + "].id"] = $("#" + indexs[i] + "id").val();
            arrDebitMoney.push(bt.money.toNumber($("#" + indexs[i] + "debitMoney").val()));
            arrCreditMoney.push(bt.money.toNumber($("#" + indexs[i] + "creditMoney").val()));
            if (curId == "") {
                curId = $("#" + indexs[i] + "curId").val();
            } else {
                if (curId != $("#" + indexs[i] + "curId").val()) {
                    flag = true;
                    break;
                }
            }

            if (netId == "") {
                netId = $("#" + indexs[i] + "netId").val();
            } else {
                if (netId != $("#" + indexs[i] + "netId").val()) {
                    flag2 = true;
                    break;
                }
            }
            if (accStartDate == "") {
                accStartDate = rowData[i].accStartDate;
            } else {
                if (accStartDate != rowData[i].accStartDate) {
                    flag3 = true;
                    break;
                }
            }
        }

        if (flag && accState == "11") {
            bt.loading.hide();
            layer.msg(bt.lang('请选择同币别账号！'));
            $(this).removeClass('layui-btn-disabled')
            return false;
        }
        if (flag2 && accState == "11") {
            bt.loading.hide();
            layer.msg(bt.lang('请选择同网点账号！'));
            $(this).removeClass('layui-btn-disabled')
            return false;
        }
        if (flag3 && accState == "11") {
            bt.loading.hide();
            layer.msg(bt.lang('请选择同启用日期账号！'));
            $(this).removeClass('layui-btn-disabled')
            return false;
        }
        var debitMoney='';
        var creditMoney='';
        for(var i=0;i<arrCreditMoney.length;i++){
            creditMoney = bt.money.add(creditMoney,arrCreditMoney[i])
        }
        for(var i=0;i<arrDebitMoney.length;i++){
            debitMoney = bt.money.add(debitMoney,arrDebitMoney[i])
        }
        if (debitMoney != creditMoney && accState == "11") {
            bt.loading.hide();
            layer.msg(bt.lang('借贷总额不相等！'));
            $(this).removeClass('layui-btn-disabled')
            return false;
        }

        var action = "/inneraccount/fcinitacc/saveOrUpdate";
        $.post(basePath + action, obj, function (result) {
            bt.loading.hide();
            if (result.success) {
                initM.draw();
            }
            parent.layer ? parent.layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            }) : layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            });
            initM.draw();
        }, 'JSON');
        $(this).removeClass('layui-btn-disabled')
        return false;
    });

});

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/inneraccount/fcinitacc/delete/" + data.id, function (result) {
            // location.reload();
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}