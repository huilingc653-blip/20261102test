bt.use(['form', 'moneyType', 'itemType', 'datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    var corpId = $('#myCorpId').val();
    var myItemCode = $('#myItemCode').val();
    var myDefItemCode = $('#myDefItemCode').val();
    var myProcessDate = $('#myProcessDate').val();
    var moneyFun = bt.moneyType.getFun();

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

    /**
     * 选择科目后的回调
     * @param obj   对象
     * @param name
     */
    itemChange = function (obj, name) {
        var row = name.substring(8, name.length);
        $('#projectDetail' + row).hide();
        if (checkUnique(obj.code, row)) {
            layer.alert(bt.lang('fbs.message.itemname.unique'), {icon: 2}, function () {
                layer.closeAll();
                bt.itemType.get("itemCode" + row).val("");
                $("#itemName" + row).val("");
                if ($('#projectDetailTable' + row)) {
                    $('#projectDetailTable' + row).DataTable().destroy();
                }
                $('#projectDetail' + row).html('');
                $("#projectDetailSum" + row).remove();
            });
        } else {
            $("#itemName" + row).val(obj.itemName);
            $genItemInfo(obj.code, row);
            if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
                $('#projectDetail' + row).show();
                initProjectDetailHtml(obj.code, row);
            }
            // 将执行金额清零
            bt.moneyType.get("processMoney" + row).val(0);
            // 重新汇总
            bt.callSumProcessMoney();
        }
    }

    /**
     * 科目树初始化完后的回调事件
     * @param ztree
     */
    initItemComplete = function (ztree) {
        var nodes = ztree.getSelectedNodes();
        if (nodes.length == 0) {
            nodes = ztree.getCheckedNodes();
        }
        if (nodes[0] != null) {
            var row = $("input[name=\"itemCode\"][value=" + nodes[0].code + "]").parents("div[row]").attr("row");
            $("#itemName" + row).val(nodes[0].itemName);
            // 重新汇总
            bt.callSumProcessMoney(bt.moneyType.get("processMoney"+ row).val(), bt.moneyType.get("processMoney"+ row));
        }
    }
    var itemType = bt.itemType.init();

    /**
     * 汇总
     * @param value
     * @param data
     * @returns {boolean}
     */
    bt.callSumProcessMoney = function (value, data) {
        if (typeof value != "undefined") {
            // 当前执行金额所在第几行
            var iptRow = data.attr("iptRow");

            // 汇率校验
            var exchangeRate = bt.moneyType.get("exchangeRate" + iptRow).val();
            if(!exchangeRate){
                layer.alert(bt.lang('budget.message.exchangeRate.null'), {icon: 2}, function () {
                    layer.closeAll();
                    bt.moneyType.get("processMoney" + iptRow).val(0);
                });
                return false;
            }

            //执行金额（折合本身币别）
            var processMoney = bt.moneyType.get("processMoney" + iptRow).val();
            var processCurMoney = bt.money.mul(processMoney, exchangeRate);
            bt.moneyType.get("processCurMoney" + iptRow).val(processCurMoney);
            $("#processCurMoneyDiv" + iptRow).text(moneyFun.format(processCurMoney));
            value = processCurMoney;

            // 科目校验
            var curItemCode = itemType.get("itemCode" + iptRow).val();
            if ("" == curItemCode) {
                layer.alert(bt.lang('fbs.message.itemcode.null'), {icon: 2}, function () {
                    layer.closeAll();
                    bt.moneyType.get("processMoney" + iptRow).val(0);
                });
                return false;
            }
            var fbsItemBalance = $("#fbsItemBalance").val();
            // 当前填写金额对应科目
            var item = itemType.get("itemCode" + iptRow).data()[0];
            if ("true" == $("#myControlBudget").val()) {
                // 校验计划执行规则为5-先执行后控制
                var processRule = $("#processRule" + iptRow).val();
                if(processRule != "5") {
                    // 校验执行金额填写
                    if (item.sign == -1 && item.isControl == 1) {
                        // zuoy 根据明细金额校验 2018-11-19
                        if ($("#detailBalance" + iptRow).text()) {
                            var detailBalance = bt.money.toNumber($("#detailBalance" + iptRow).text());
                            if (detailBalance < value) {
                                layer.alert(bt.lang('fbs.message.detailmoney.gt.detailbalance'), {icon: 2}, function () {
                                    layer.closeAll();
                                    $('[data-id ="processMoney' + iptRow + '"] ').addClass("layui-form-danger");
                                    $('[data-id ="processMoney' + iptRow + '"] ').focus();
                                    bt.moneyType.get("processMoney" + iptRow).val(0);
                                });
                                return false;
                            }
                        }
                        if (fbsItemBalance == 0) {
                            if (bt.money.toNumber($("#itemBalance" + iptRow).val()) < value) {
                                layer.alert(bt.lang('fbs.message.detailmoney.gt.itembalance'), {icon: 2}, function () {
                                    layer.closeAll();
                                    $('[data-id ="processMoney' + iptRow + '"] ').addClass("layui-form-danger");
                                    $('[data-id ="processMoney' + iptRow + '"] ').focus();
                                    bt.moneyType.get("processMoney" + iptRow).val(0);
                                });
                                return false;
                            }
                        } else if (fbsItemBalance == 1) {
                            if (bt.money.toNumber($("#topItemBalance" + iptRow).val()) < value) {
                                layer.alert(bt.lang('fbs.message.detailmoney.gt.topitembalance'), {icon: 2}, function () {
                                    layer.closeAll();
                                    $('[data-id ="processMoney' + iptRow + '"] ').addClass("layui-form-danger");
                                    $('[data-id ="processMoney' + iptRow + '"] ').focus();
                                    bt.moneyType.get("processMoney" + iptRow).val(0);
                                });
                                return false;
                            }
                        } else if (fbsItemBalance == 2) {
                            if (bt.money.toNumber($("#superItemBalance" + iptRow).val()) < value) {
                                layer.alert(bt.lang('fbs.message.detailmoney.gt.superitembalance'), {icon: 2}, function () {
                                    layer.closeAll();
                                    $('[data-id ="processMoney' + iptRow + '"] ').addClass("layui-form-danger");
                                    $('[data-id ="processMoney' + iptRow + '"] ').focus();
                                    bt.moneyType.get("processMoney" + iptRow).val(0);
                                });
                                return false;
                            }
                        }
                    }
                }
            } else {
                // zuoy 根据明细金额校验 2018-11-19
                if ($("#detailBalance" + iptRow).text()) {
                    var detailBalance = bt.money.toNumber($("#detailBalance" + iptRow).text());
                    if (detailBalance < value) {
                        $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").attr("data-gt-balance", curItemCode);
                    } else {
                        $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").removeAttr("data-gt-balance");
                    }
                } else {
                    if (fbsItemBalance == 0) {
                        if (bt.money.toNumber($("#itemBalance" + iptRow).val()) < value) {
                            $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").attr("data-gt-balance", curItemCode);
                        } else {
                            $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").removeAttr("data-gt-balance");
                        }
                    } else if (fbsItemBalance == 1) {
                        if (bt.money.toNumber($("#topItemBalance" + iptRow).val()) < value) {
                            $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").attr("data-gt-balance", curItemCode);
                        } else {
                            $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").removeAttr("data-gt-balance");
                        }
                    } else if (fbsItemBalance == 2) {
                        if (bt.money.toNumber($("#superItemBalance" + iptRow).val()) < value) {
                            $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").attr("data-gt-balance", curItemCode);
                        } else {
                            $("#budgetProcessDataTable").find("div[row=" + iptRow + "]").removeAttr("data-gt-balance");
                        }
                    }
                }
            }
        }

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
    var bmt = bt.moneyType.init();

    /**
     * 加载余额
     * @param itemCode
     * @param index
     */
    $genItemInfo = function (itemCode, index) {
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
            curId: $('#myCurId').val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: basePath + "/budget/fbsBudgetProcess/genItemInfo",
            method: 'POST',
            data: param,
            async: false,
            dataType: 'json',
            success: function (data) {
                if (data) {
                    $("#processRule" + index).val(data.obj.processRule);
                    bmt.get("itemBalance" + index).val(data.obj.itemMoney);
                    bmt.get("superItemBalance" + index).val(data.obj.superItemMoney);
                    bmt.get("topItemBalance" + index).val(data.obj.topItemMoney);
                    bmt.get("exchangeRate" + index).val(data.obj.exchangeRate);
                } else {
                    bmt.get("itemBalance" + index).val(0);
                    bmt.get("superItemBalance" + index).val(0);
                    bmt.get("topItemBalance" + index).val(0);
                }
                bmt.render();
            }
        });
    };
    var rowTr = $("#budgetProcessDataTable").find("div[row]").length;
    /**
     * 新增行
     * @param rowMR
     */
    add = function () {
        rowMR = rowTr;
        var html = ' <div class="line" row="' + rowMR + '">';
        html += '<div class="x10 line line_border">';
        html += '<div class="x2">';
        html += '   <div class="layui-inline">';
        html += '       <div class="bt-biz-itemType" data-type="selectTree" data-tree-type="radio" id="itemCode' + rowMR + '" data-name="itemCode" data-biz-name="itemCode' + rowMR + '" ';
        html += '       data-callbackData="itemChange"  data-corpId="' + corpId + '" data-itemCode="' + myItemCode + '" data-required="true" data-processDate="' + myProcessDate + '"></div>'
        html += '   </div>';
        html += '   <input type="hidden" id="itemName' + rowMR + '" name="itemName" autocomplete="off" class="layui-input"/>';
        html += '   <input type="hidden" id="processRule' + rowMR + '" name="processRule" autocomplete="off" class="layui-input"/>';
        html += '</div>';
        html += '<div class="x2"><input type="text" id="itemBalance' + rowMR + '" data-name="itemBalance" data-biz-name="itemBalance' + rowMR + '" readonly="readonly" class="layui-input bt-biz-moneyType" data-minus="true"/></div>';
        html += '<div class="x2"><input type="text" id="superItemBalance' + rowMR + '" data-name="superItemBalance" data-biz-name="superItemBalance' + rowMR + '" readonly="readonly" class="layui-input bt-biz-moneyType" data-minus="true"/></div>';
        html += '<div class="x2"><input type="text" id="topItemBalance' + rowMR + '" data-name="topItemBalance" data-biz-name="topItemBalance' + rowMR + '" readonly="readonly" class="layui-input bt-biz-moneyType" data-minus="true"/></div>';
        html += '<div class="x2"><input type="text" id="processMoney' + rowMR + '" data-name="processMoney" data-callback="bt.callSumProcessMoney" data-biz-name="processMoney' + rowMR + '" lay-verify="required" iptRow="' + rowMR + '" class="layui-input bt-biz-moneyType ignore-required-icon" data-minus="true"/></div>';
        html += '<div class="x2"><input type="text" id="exchangeRate' + rowMR + '" data-name="exchangeRate" data-biz-name="exchangeRate' + rowMR + '" lay-verify="number2" readonly="readonly" class="layui-input bt-biz-moneyType" data-budget="true" data-decimal="4" data-minus="false" data-type="percent" data-min="0.0001" data-max="100"/></div>';
        html += '</div>';
        html += '<div class="x2 line line_border">';
        html += '<div class="x6 textRight"><input type="hidden" id="processCurMoney' + rowMR + '" data-name="processCurMoney" data-biz-name="processCurMoney' + rowMR + '" class="layui-input bt-biz-moneyType" data-minus="true"/><div id="processCurMoneyDiv' + rowMR + '"></div></div>';
        if(rowMR == 0){
            html += '<div class="x6"></div>';
        }else {
            html += '<div class="x6"><a href="javascript:void(0)" onclick="del(' + rowMR + ')">' + bt.lang("common.button.delete") + '</a></div>';
        }
        html += '</div>';
        html += '</div>';
        if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
            html += ' <div class="table_grid" id="projectDetail' + rowMR + '" style="display: none"></div>';
        }
        $("#budgetProcessDataTable").append(html);
        rowTr++;
        bmt.render();
        itemType.render();
    }

    function init() {
        for (var i = 0; i < rowTr; i++) {
            if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
                initProjectDetailHtml($("#initItemCode"+ i).val(), i);
            }
        }
        if(rowTr == 0){
            add();
        }
        // 计划执行组件加载完后回调
        if (typeof fbsItemTableInitCallback == "function") {
            fbsItemTableInitCallback();
        }
    }
    init();

    /**
     * 删除
     * @param el
     */
    del = function (row) {
        $("#budgetProcessDataTable").find("div[row=" + row + "]").remove();
        bt.callSumProcessMoney();
        if ($('#projectDetailTable' + row)) {
            $('#projectDetailTable' + row).DataTable().destroy();
        }
        $('#projectDetail' + row).remove();
        $("#projectDetailSum" + row).remove();
    }

    /**
     * 检查科目是否重复
     * @param itemCode
     */
    function checkUnique(itemCode, curRow) {
        var item = $("input[name=\"itemCode\"][value=" + itemCode + "]");
        if (item.length > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 加载计划明细数据
     */
    function initProjectDetailHtml(itemCode, row) {
        if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
            $('#projectDetail' + row).show();
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
            // columns.push({
            //     data: "natualMoney",
            //     bSortable: false,
            //     className: 'textRight',
            //     title: bt.lang('table.fbsBudgetDetail.natualMoney'),
            //     render: function (data, type, full, meta) {
            //         if (full.bsuperNatualMoney) {
            //             return bt.money.format(bt.money.add(full.natualMoney, full.bsuperNatualMoney));
            //         } else {
            //             return bt.money.format(full.natualMoney);
            //         }
            //     }
            // });
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
            if ($('#projectDetailTable' + row).length) {
                $('#projectDetailTable' + row).DataTable().destroy();
            }
            var html = '<div><table id="projectDetailTable' + row + '"  class="layui-table" ></table></div>';
            $("#projectDetail" + row).html(html);
            var data = {
                corpId: corpId,
                itemCode: itemCode,
                processDate: myProcessDate,
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
                ajax: {
                    url: basePath + "/budget/fbsbudgetdetail/getProjectDetailInfo",
                    data: function (d) {
                        d.formdata = data;
                        return JSON.stringify(d);
                    }
                },
                columns: columns,
                btInitComplete: function (setting, json) {
                    $("#projectDetailSum" + row).remove();
                    var sumMoney = 0;
                    if (json.data.length) {
                        bt.each(json.data, function (i, data) {
                            if (!$("#isRMB").val()) {
                                // 非人民币时，找折合人民币
                                if (data.bsuperNatualMoney) {
                                    sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                    sumMoney = bt.money.add(sumMoney, data.bsuperNatualMoney);
                                } else {
                                    sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                }
                            } else {
                                // 人民币，直接找本身上报金额
                                if (data.bsuperMoney) {
                                    sumMoney = bt.money.add(sumMoney, data.bMoney);
                                    sumMoney = bt.money.add(sumMoney, data.bsuperMoney);
                                } else {
                                    sumMoney = bt.money.add(sumMoney, data.bMoney);
                                }
                            }
                        });
                    }
                    if(json.recordsTotal >= 1){
                        genItemInfoByDetail(itemCode, row, sumMoney);
                    }
                    if (!json.data.length) {
                        $('#projectDetailTable' + row).DataTable().destroy();
                        $('#projectDetail' + row).html('');
                        $('#projectDetail' + row).hide();
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


