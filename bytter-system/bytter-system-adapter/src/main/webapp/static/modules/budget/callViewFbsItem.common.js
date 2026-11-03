bt.use(['form', 'moneyType','datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var corpId = $('#myCorpId').val();

    /**
     * 汇总
     * @param value
     * @param data
     * @returns {boolean}
     */
    callSumProcessMoney = function () {
        // 汇总执行金额
        var trs = $("#budgetProcessDataTable").children("div[row]");
        // 遍历行数据
        var sumMoney = 0;
        trs.each(function (index, tr) {
            var rowIndex = $(tr).attr("row");
            var processMoney = bt.moneyType.get("processMoney" + rowIndex).val();
            var itemMoney = bt.money.toNumber(processMoney);
            sumMoney = bt.money.add(sumMoney, itemMoney);
        });
        bt.moneyType.get("sumMoney").val(sumMoney);
        // 执行金额汇总后的回调
        if (typeof sumMoneyCallBack == "function") {
            sumMoneyCallBack(sumMoney);
        }
    };

    var rowTr = $("#budgetProcessDataTable").find("div[row]").length;


    var detailDict = bt.dictGroup("fbsBudget.detailCol");
    var detailCol = {};
    bt.each(detailDict, function (i, temp) {
        detailCol[temp.name] = temp.code;
    });
    var dtlColSel = $("#myDtlColSel").val().replace('[', '').replace(']', '').replace(/\s+/g, "").split(",");
    var dtlColIds = [];
    bt.each(dtlColSel, function (i, col) {
        //var name = bt.dict("fbsBudget.detailCol", col);
        //var columnId = detailCol[name];
        dtlColIds.push(col);
    });

    function init() {
        // 重新汇总
        callSumProcessMoney();
        for (var i = 0; i < rowTr; i++) {
            if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
                initProjectDetailHtml($("#initItemCode"+ i).val(), i);
            }
        }
        // 计划执行组件加载完后回调
        if (typeof fbsItemTableInitCallback == "function") {
            fbsItemTableInitCallback();
        }
    }
    init();

    /**
     * 加载计划明细数据
     */
    function initProjectDetailHtml(itemCode, row) {
        if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
            $('#projectDetail' + row).show();
            var html = '<div><table id="projectDetailTable' + row + '"  class="layui-table" ></table></div>';
            var columns = [];
            columns.push({
                data: "bMoney",
                bSortable: false,
                className: 'textRight',
                title: bt.lang('table.fbsBudgetDetail.detailMoney'),
                render: function (data, type, full, meta) {
                    if (full.bsuperMoney) {
                        return bt.money.format(bt.money.add(full.bMoney, full.bsuperMoney));
                    } else {
                        return bt.money.format(full.bMoney);
                    }
                }
            });
            /*columns.push({
                data: "natualMoney",
                bSortable: false,
                className: 'textRight',
                title: bt.lang('table.fbsBudgetDetail.natualMoney'),
                render: function (data, type, full, meta) {
                    if (full.bsuperNatualMoney) {
                        return bt.money.format(bt.money.add(full.natualMoney, full.bsuperNatualMoney));
                    } else {
                        return bt.money.format(full.natualMoney);
                    }
                }
            });*/
            bt.each(dtlColSel, function (i, columnId) {
                var name = bt.dict("fbsBudget.detailCol", columnId);
                //var columnId = detailCol[name];
                if (columnId == "curId") {
                    columnId = "curName";
                }
                if (columnId == "settlType") {
                    columnId = "settlTypeName";
                }
                columns.push({
                    data: columnId, bSortable: false, className: 'center', title: name
                });
            });
            $("#projectDetail" + row).html(html);
            var data = {
                corpId: corpId,
                itemCode: itemCode,
                processDate: $("#myProcessDate").val(),
                billNo: $('#myBillNo').val(),
                billId: $('#myBillId').val(),
                "curId": $("#myCurId").val(),
                "settlType": $("#mySettlType").val(),
                "corpName": $("#myCorpName").val(),
                "purpose": $("#myPurpose").val(),
                "wishPayday": $("#myWishPayday").val(),
                "payerAcc": $("#myPayerAcc").val(),
                "payeeAccName": $("#myPayeeAccName").val(),
                "payeeAcc": $("#myPayeeAcc").val(),
                "payeeBank": $("#myPayeeBank").val(),
                "contract": $("#myContract").val(),
                "dtlColIds": dtlColIds,
            };
            //生成列表
            bt.datatable.init({
                elem: "#projectDetailTable" + row,
                scrollY: '100px',
                dom: "rt",
                buttonsLayout: false,
                autoWidth: true,
                colReorder:false,
                ajax: {
                    url: basePath + "/budget/fbsbudgetdetail/getProjectDetailInfo",
                    data: function (d) {
                        d.formdata = data;
                        return JSON.stringify(d);
                    }
                },
                columns: columns,
                btInitComplete: function (setting, json) {
                    var sumMoney = 0;
                    if (json.data.length) {
                        bt.each(json.data, function (i, data) {
                            if($("#isRMB").val()){
                                // 人民币时，找折合人民币
                                if (data.bsuperNatualMoney) {
                                    sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                    sumMoney = bt.money.add(sumMoney, data.bsuperNatualMoney);
                                } else {
                                    sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                }
                            }else {
                                // 非人民币，直接找本身上报金额
                                if (data.bsuperMoney) {
                                    sumMoney = bt.money.add(sumMoney, data.bMoney);
                                    sumMoney = bt.money.add(sumMoney, data.bsuperMoney);
                                } else {
                                    sumMoney = bt.money.add(sumMoney, data.bMoney);
                                }
                            }
                        });
                    }
                    // genItemInfoByDetail(itemCode, row, sumMoney);
                    if (!json.data.length) {
                        $('#projectDetailTable' + row).DataTable().destroy();
                        $('#projectDetail' + row).remove();
                    }
                }
            });
        }
    }

    /**
     *  根据明细获取计划执行金额
     * @param itemCode
     * @param index
     */
    function genItemInfoByDetail(itemCode, index, sumMoney) {
        var processDate = $("#myProcessDate").val();
        if (corpId.length == 0 || itemCode.length == 0 || processDate.length == 0) {
            return;
        }
        var param = {
            corpId: corpId,
            itemCode: itemCode,
            processDate: processDate,
            billNo: $('#myBillNo').val(),
            billId: $('#myBillId').val(),
            "curId": $("#myCurId").val(),
            "settlType": $("#mySettlType").val(),
            "corpName": $("#myCorpName").val(),
            "purpose": $("#myPurpose").val(),
            "wishPayday": $("#myWishPayday").val(),
            "payerAcc": $("#myPayerAcc").val(),
            "payeeAccName": $("#myPayeeAccName").val(),
            "payeeAcc": $("#myPayeeAcc").val(),
            "payeeBank": $("#myPayeeBank").val(),
            "contract": $("#myContract").val(),
            "dtlColIds": dtlColIds.toString(),
        };
        $.ajax({
            url: basePath + "/budget/fbsBudgetProcess/genItemInfoByDetail",
            method: 'POST',
            data: param,
            async: false,
            dataType: 'json',
            success: function (data) {
                $("#projectDetailSum" + index).remove();
                var html = "<div class='line line_border' id='projectDetailSum" + index + "'>";
                html += "<div class='x2 x4-left textRight'>" + bt.lang('table.fbsBudgetProcess.detailsummoney') + "：<span id='detailSumMoney" + index + "'>" + moneyFun.format(sumMoney) + "</span></div>";
                html += "<div class='x2 x4-left textRight'>" + bt.lang('table.fbsBudgetProcess.detailprocesssummoney') + "：<span id='detailProcessSumMoney" + index + "'>" + moneyFun.format(data.obj) + "</span></div>";
                var detailBalance = moneyFun.sub(moneyFun.toNumber(sumMoney), moneyFun.toNumber(data.obj));
                html += "<div class='x2 x4-left textRight'>" + bt.lang('table.fbsBudgetProcess.detailbalance') + "：<span id='detailBalance" + index + "'>" + moneyFun.format(detailBalance) + "</span></div>";
                html += "</div>";
                $('#projectDetail' + index).before(html);
            }
        });
    };
});