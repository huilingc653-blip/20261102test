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
            'url': basePath + "/netbank/nisbillhead/dataTableApprove"
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
            {data: "corpName", bSortable: true},
            {data: "voucherName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "isPayedName", bSortable: true, className: 'center', visible: setIsPayedVisible()},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankCode", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "wishPayday", bSortable: true},
            {data: "busTypeName", bSortable: true},
            {data: "bursarBillNo", bSortable: true},
            {data: "purpose", bSortable: true},
            {data: "addWord", bSortable: true},
            {data: "billFromName", bSortable: true, className: 'center'},
            {data: "statusName", bSortable: true, className: 'center', visible: setApproveDateVisible()},
            {data: "approveDate", bSortable: true, className: 'center', visible: setApproveDateVisible()},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
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
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var showHTML =  '已选笔数: '+bt.datatable.getSelectData('#appTable').length || 0;
            return '<div style="color: red" <span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        },
        btClickRowCallback:function(){
            $("#sumMoneyBillSpan").text('已选笔数: '+bt.datatable.getSelectData('#appTable').length || 0)
        },
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
                url: basePath + "/netbank/nisbillhead/treeByCorpIds/" + flowStatus,
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
        selectTree.setUrl(basePath + "/netbank/nisbillhead/treeByCorpIds");
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
                        if(!msg && result.messageEnum){
                            msg = bt.lang(result.messageEnum);
                        }
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
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.id
    });
}

function setApproveDateVisible() {
    var flowStatus = $("#flowStatus").val();
    if (flowStatus == "pass") {
        return true;
    } else {
        return false;
    }
}

function setIsPayedVisible() {
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        return true;
    } else {
        return false;
    }
}
/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/netbank/nisbillhead/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}


