bt.use(['form', 'dateType', 'datatable', 'moneyType', 'selectType', 'bank', 'corp','mulselect','dicts'], function () {
    $ = bt.jquery;
    bt.dateType.init();
    bt.selectType.init();
    var moneyType=bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.corp.init();
    bt.mulselect.init();
    var dicts = bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

    var selectTree;

    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/ttpay/tpPayment/dataTableApprove"
        },
        buttons: [{
            extend: "colvis",
            text: bt.lang('common.button.theadHide'),
            columns: ':not(:first):not(:last)'
        }],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true},
            {data: "busTitle", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }},
            {data: "payeeAccName", bSortable: true,},
            {data: "payeeAcc", bSortable: true},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {data: "payTypeName", bSortable: true, className: 'center'},
            {data: "statusName", bSortable: true, className: 'center'},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "busTypeName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if ($("#flowStatus").val() == 'todo') {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                }
                if ($("#flowStatus").val() == 'track') {
                    if ((full.status >= 11 && full.status < 95)) {
                        // 审批人可主动撤回
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    }
                }
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
                return html;
            }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        },
        btInitComplete: function (settings, json) {
            var flowStatus = $("#flowStatus").val();
            // 加载单位
            selectTree = bt.selectTree.init({
                url: basePath + "/ttpay/tpPayment/treeByCorpIds/" + flowStatus,
                elem: ".demo-selectTree-normal",
                name: 'ins_corpId',
                id: 'id',
                pId: 'pId',
                checkType: "check",
                // param:JSON.stringify(data),
                required: false,
                clickCallBack: function (id) {
                }
            });
        },
    });

    initCorp = function () {
        selectTree.setUrl(basePath + "/ttpay/tpPayment/treeByCorpIds");
    }

    bt.workFlow.beforeSubmit = function(submitData) {
        if(submitData.workflowPass=="1") {
            var rowData = bt.datatable.getSelectData("#appTable");
            var billNos = "";
            var nowDate = bt.date.format(new Date(), 'yyyy-MM-dd');
            for (var i = 0; i < rowData.length; i++) {
                var wishPayday = bt.date.format(rowData[i].wishPayday, 'yyyy-MM-dd');
                if (bt.date.compare(wishPayday, nowDate) > 0) {
                    billNos = billNos + rowData[i].billNo + ",";
                }
            }
            if (billNos != "") {
                billNos = billNos.substring(0, (billNos.length - 1));
                bt.loading.hide();
                parent.layer.confirm(bt.lang('nis.message.wishpayday.pass', [billNos]), function () {
                    bt.workFlow.submit(submitData, true);
                }, function () {
                });
            } else {
                return true;
            }
        }else{
            bt.workFlow.submit(submitData, true);
        }
    }

    /**
     * 流程撤回 - 审批人可主动撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            parent.layer.confirm(bt.lang('common.message.confirm.batchRevoke'), function () {
                $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                    console.log('result',result)
                    if(result.success){
                        bt.datatable.refreshCurrentPage();
                        var msg = result.msg;
                        var obj = result.obj;
                        var length = obj.length;
                        if (length) {
                            //对报错信息进行转化，显示每个报错信息有多少条
                            for (var a = 0; a < length; a++) {
                                if (obj[a].corpName) {
                                    obj[a].errorMsg = [];
                                    var messageList = obj[a].messageList.sort();
                                    for (var i = 0; i < messageList.length;) {
                                        var count = 0;
                                        for (var j = i; j < messageList.length; j++) {
                                            if (messageList[i] == messageList[j]) {
                                                count++;
                                            }
                                        }
                                        obj[a].errorMsg.push([messageList[i].replace(/_/g, '.').toLowerCase(), count]);
                                        i += count;
                                    }
                                }
                            }
                            //拼装报错信息
                            for (var i = 0; i < length; i++) {
                                if (obj[i].corpName) {
                                    msg += obj[i].corpName + '单位有' + obj[i].errorNum + '条撤回失败,原因:';
                                    for (var k = 0; k < obj[i].errorMsg.length; k++) {
                                        msg += obj[i].errorMsg[k][1] + '条' + bt.lang(obj[i].errorMsg[k][0])
                                    }
                                }
                            }
                        }
                        parent.layer.alert(msg, {icon: result.success ? 1 : 2});
                    }else{
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                }, 'JSON');
            });
        }
    }
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/ttpay/tpPayment/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/tpPayment/view/" + data.id
    });
}

/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/ttpay/tpPayment/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/tpPayment/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
