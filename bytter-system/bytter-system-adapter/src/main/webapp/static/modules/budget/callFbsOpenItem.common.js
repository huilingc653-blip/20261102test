bt.use(['form', 'bttable', 'moneyType', 'itemType', 'datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    var corpId = $('#myCorpId').val();
    var myItemCode = $('#myItemCode').val();
    var myDefItemCode = $('#myDefItemCode').val();
    var myProcessDate = $('#myProcessDate').val();
    var moneyFun = bt.moneyType.getFun();
    var BUDGET_PROCESS_SPLIT = "!-!";
    //是否支持多个科目
    var isMultiItem = $("#isMultiItem").val();
    /**
     * 选择科目后的回调
     * @param obj   对象
     * @param name
     */
    itemChange = function (obj, name) {
        var row = name.substring(8, name.length);
        $('#projectDetailTr' + row).show();
        if(checkUnique(obj.code, row)){
            $('#projectDetailTr' + row).hide();
            layer.alert(bt.lang('fbs.message.itemname.unique'), {icon: 2},function () {
                layer.closeAll();
                bt.itemType.get("itemCode" + row).val("");
                $("#itemName" + row).val("");
                if ($('#projectDetailTable' + row)) {
                    $('#projectDetailTable' + row).DataTable().destroy();
                }
                $('#projectDetail' + row).html('');
                $("#projectDetailSum" + row).remove();
            });
        }else {
            $("#itemName" + row).val(obj.itemName);
            $genItemInfo(obj.code, row);
            if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
                initProjectDetailHtml(obj.code, row);
            }
            // 将执行金额清零
            bt.moneyType.get("processMoney" + row).val(0);
            // 重新汇总
            callSumProcessMoney();
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
        if(nodes[0]){
            var row = $("input[value=" + nodes[0].code + "]").parents("tr").attr("row");
            $("#itemName" + row).val(nodes[0].itemName);
            if (myDefItemCode != "" && row == 0) {
                $genItemInfo(myDefItemCode, row);
            }
        }
    }
    var itemType = bt.itemType.init();;

    callSumProcessMoney = function (value, data) {
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
            var curItemCode = itemType.get("itemCode" + iptRow ).val();
            if("" == curItemCode){
                layer.alert(bt.lang('fbs.message.itemcode.null'), {icon: 2},function () {
                    layer.closeAll();
                    bt.moneyType.get("processMoney" + iptRow).val(0);
                });
                return false;
            }
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
                        var fbsItemBalance = $("#fbsItemBalance").val();
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
        var trs = $("#budgetProcessDataTable").children("tbody").children("tr[row]");
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
    var rowTr = 0;
    addBudget = function(rowMR, budget){
        var html = '<tr row=' + rowMR + '>';
        html += '<td>';
        html += '   <div class="layui-inline">';
        if(budget){
            html += '       <div class="bt-biz-itemType" data-type="selectTree" id="itemCode' + rowMR + '" data-tree-type="radio" data-name="itemCode" data-biz-name="itemCode' + rowMR + '" data-defaultvalue="' + budget[0] + '" data-callbackData="itemChange"  data-corpId="' + corpId + '" data-itemCode="' + myItemCode + '" data-processDate="' + myProcessDate + '" data-required="true"></div>';
        }else{
            html += '       <div class="bt-biz-itemType" data-type="selectTree" id="itemCode' + rowMR + '" data-tree-type="radio" data-name="itemCode" data-biz-name="itemCode' + rowMR + '" ';
            if (myDefItemCode != "" && rowMR == 0) {
                html += ' data-defaultvalue="' + myDefItemCode + '"';
            }
            html += 'data-initComplete="initItemComplete" data-callbackData="itemChange"  data-corpId="' + corpId + '" data-itemCode="' + myItemCode + '" data-processDate="' + myProcessDate + '" data-required="true"></div>';
        }
        html += '   </div>';
        html += '   <input type="hidden" id="itemName' + rowMR + '" name="itemName" autocomplete="off" class="layui-input"/>';
        html += '   <input type="hidden" id="processRule' + rowMR + '" name="processRule" autocomplete="off" class="layui-input"/>';
        html += '</td>';
        html += '<td class="textRight"><input type="text" style="width: 120px" id="itemBalance' + rowMR + '" data-name="itemBalance" data-biz-name="itemBalance' + rowMR + '" readonly="readonly" class="layui-input bt-biz-moneyType" data-minus="true"/></td>';
        html += '<td class="textRight"><input type="text" style="width: 120px" id="superItemBalance' + rowMR + '" data-name="superItemBalance" data-biz-name="superItemBalance' + rowMR + '" readonly="readonly" class="layui-input bt-biz-moneyType" data-minus="true"/></td>';
        html += '<td class="textRight"><input type="text" style="width: 120px" id="topItemBalance' + rowMR + '" data-name="topItemBalance" data-biz-name="topItemBalance' + rowMR + '" readonly="readonly" class="layui-input bt-biz-moneyType" data-minus="true"/></td>';
        if(budget) {
            html += '<td class="textRight"><input type="text" style="width: 120px" id="processMoney' + rowMR + '" data-name="processMoney" data-callback="callSumProcessMoney" data-biz-name="processMoney' + rowMR + '" value="' + budget[2] + '"  iptRow="' + rowMR + '" class="layui-input bt-biz-moneyType" data-minus="true"/></td>';
            html += '<td class="textRight"><input type="text" style="width: 120px" id="exchangeRate' + rowMR + '" data-name="exchangeRate" data-biz-name="exchangeRate' + rowMR + '"  class="layui-input bt-biz-moneyType" value="' + budget[3] + '" readonly="readonly" data-budget="true" data-decimal="4" data-minus="false" data-type="percent" data-min="0.0001" data-max="100"/></td>';
            html += '<td class="textRight"><input type="hidden" style="width: 120px" id="processCurMoney' + rowMR + '" data-name="processCurMoney" data-biz-name="processCurMoney' + rowMR + '"  value="' + budget[4] + '" class="layui-input bt-biz-moneyType" data-minus="true" /><div id="processCurMoneyDiv' + rowMR + '">' + moneyFun.format(budget[4]) + '</div></td>';
        }else{
            html += '<td class="textRight"><input type="text" style="width: 120px" id="processMoney' + rowMR + '" data-name="processMoney" data-callback="callSumProcessMoney" data-biz-name="processMoney' + rowMR + '"  iptRow="' + rowMR + '" class="layui-input bt-biz-moneyType" data-minus="true"/></td>';
            html += '<td class="textRight"><input type="text" style="width: 120px" id="exchangeRate' + rowMR + '" data-name="exchangeRate" data-biz-name="exchangeRate' + rowMR + '"  class="layui-input bt-biz-moneyType" readonly="readonly" data-budget="true" data-decimal="4" data-minus="false" data-type="percent" data-min="0.0001" data-max="100"/></td>';
            html += '<td class="textRight"><input type="hidden" style="width: 120px" id="processCurMoney' + rowMR + '" data-name="processCurMoney" data-biz-name="processCurMoney' + rowMR + '"  class="layui-input bt-biz-moneyType" data-minus="true" /><div id="processCurMoneyDiv' + rowMR + '"></div></td>';
        }
        if(isMultiItem != "0") {
            if (rowMR == 0) {
                html += '<td></td>';
            } else {
                html += '<td><a href="javascript:void(0)" onclick="del(this)">' + bt.lang("common.button.delete") + '</a></td>';
            }
        }
        html += '</tr>';
        if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
            html += '<tr id="projectDetailTr' + rowMR + '" style="display: none"><td colspan="8" id="projectDetail' + rowMR + '"></td></tr>';
        }
        $("#budgetProcessDataTable >tbody").append(html);
        rowTr++;
        // 重新初始化金额及科目
        bmt.render();
        itemType.render();
        if(budget){
            // 重新汇总
            callSumProcessMoney();
            // 初始化余额信息
            $genItemInfo(budget[0], rowMR);
            if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
                initProjectDetailHtml(budget[0], rowMR);
            }
        }
    }

    if(isMultiItem == "0") {
        //生成列表
        bt.bttable.init({
            elem: "#budgetProcessDataTable",
            scrollY: "500px",
            columns: []
        });
    } else {
        //生成列表
        bt.bttable.init({
            elem: "#budgetProcessDataTable",
            scrollY: "500px",
            buttons: [{
                text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
                action: function ($trs) {
                    addBudget(rowTr);
                }
            }],
            columns: []
        });
    }

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
            curId:$("#myCurId").val(),
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
                    $("#itemName" + index).val(data.obj.itemName);
                    $("#processRule" + index).val(data.obj.processRule);
                    bmt.get("itemBalance" + index).val(data.obj.itemMoney);
                    bmt.get("superItemBalance" + index).val(data.obj.superItemMoney);
                    bmt.get("topItemBalance" + index).val(data.obj.topItemMoney);
                    bmt.get("exchangeRate" + index).val(data.obj.exchangeRate);
                }
                form.render();
            }
        });
    };

    /**
     * 保存
     */
    onBudgetSave = function (){
        var rowNum = $("#rowNum").val();
        var sumMoney = bt.moneyType.get("sumMoney").val();
        if(sumMoney == 0){
            layer.alert(bt.lang("fbs.budget.message.processmoney.gtzero"), {icon: 2});
            return false;
        }
        var datas = [];
        $('form').find('input[name="itemCode"]').each(function(index){
            if($(this).val()){
                datas.push($(this).val() + BUDGET_PROCESS_SPLIT + $("#itemName" + index).val() + BUDGET_PROCESS_SPLIT + bt.moneyType.get("processMoney" + index).val() + BUDGET_PROCESS_SPLIT + bt.moneyType.get("exchangeRate" + index).val() + BUDGET_PROCESS_SPLIT + bt.moneyType.get("processCurMoney" + index).val());
            }
        });
        var webObj = bt.navtab.getCurrentDom();
        if(datas.length){
            webObj.budgetProcessCallback(sumMoney, datas, rowNum);
        }else{
            webObj.budgetProcessCallback(0, datas, rowNum);
        }
        parent.layer?parent.layer.closeAll():layer.closeAll();
    }

    $("#saveBudget").on("click",function() {
        onBudgetSave();
    });


    /**
     * 检查科目是否重复
     * @param itemCode
     */
    function checkUnique(itemCode, curRow) {
        var item = $("input[value=" + itemCode + "]");
        var tr = $(item).closest("tr:not([row=" + curRow + "])");
        return tr.length;
    }

    /**
     * 删除
     * @param el
     */
    del = function (el) {
        var tr = $(el).parent("td").parent("tr");
        tr.remove();
        callSumProcessMoney();
        var row = $(tr).attr("row");
        if ($('#projectDetailTable' + row)) {
            $('#projectDetailTable' + row).DataTable().destroy();
        }
        $('#projectDetail' + row).parents("tr").remove();
        $("#projectDetailSum" + row).remove();
    }

    /**
     * 初始化之前选中的执行科目信息
     */
    function initBudget(){
        if($("#budget").val()) {
            var tr = $("#budgetProcessDataTable").find('tr[row="0"]');
            tr.remove();
            var budgets = $("#budget").val().split(",");
            if(budgets.length == 1){
                var obj = budgets[0].split(BUDGET_PROCESS_SPLIT);
                addBudget(0, obj);
            }else{
                for (k in budgets) {
                    var obj = budgets[k].split(BUDGET_PROCESS_SPLIT);
                    addBudget(k, obj);
                }
            }
        }else{
            // 初始化第一行
            addBudget(0);
        }
    }



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
     * 加载计划明细数据
     */
    function initProjectDetailHtml(itemCode, row) {
        if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
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
                "corpId": corpId,
                "itemCode": itemCode,
                "processDate": myProcessDate,
                "billNo": $('#myBillNo').val(),
                "billId": $('#myBillId').val(),
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
                    if (json.data && json.data.length) {
                        bt.each(json.data, function (i, data) {
                            if(!$("#isRMB").val()){
                                // 非人民币时，找折合人民币
                                if (data.bsuperNatualMoney) {
                                    sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                    sumMoney = bt.money.add(sumMoney, data.bsuperNatualMoney);
                                } else {
                                    sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                }
                            }else {
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
            curId: $("#myCurId").val(),
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
                var html = "<tr class='detail_tr' id='projectDetailSum" + index + "' >";
                html += "<td colspan='2'></td>";
                html += "<td class='textRight'>" + bt.lang('table.fbsBudgetProcess.detailsummoney') + "：<span id='detailSumMoney" + index + "'>" + moneyFun.format(sumMoney) + "</span></td>";
                html += "<td class='textRight'>" + bt.lang('table.fbsBudgetProcess.detailprocesssummoney') + "：<span id='detailProcessSumMoney" + index + "'>" + moneyFun.format(data.obj) + "</span></td>";
                var detailBalance = moneyFun.sub(moneyFun.toNumber(sumMoney), moneyFun.toNumber(data.obj));
                html += "<td class='textRight'>" + bt.lang('table.fbsBudgetProcess.detailbalance') + "：<span id='detailBalance" + index + "'>" + moneyFun.format(detailBalance) + "</span></td>";
                html += "<td colspan='3'></td>";
                html += "</tr>";
                $('#projectDetail' + index).parents("tr").before(html);
            }
        });
    };

    initBudget();

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //获取数据行信息
        var trs = $('#budgetProcessDataTable').find('tbody tr');
        if (trs[0]){
            bt.each(trs, function () {
                if ($(this).attr('row')!==undefined){
                    resetItemAndProcessMoney($(this).attr('row'));
                }
            })
        }
    }

    /**
     * 清空科目代码 和 执行金额数据 栏数据
     * */
    function resetItemAndProcessMoney (row){
        bt.itemType.get("itemCode"+row).val("");
        $("input[name='itemCode']").val('');
        bt.moneyType.init().get("processMoney"+row).val('');
    }
});



