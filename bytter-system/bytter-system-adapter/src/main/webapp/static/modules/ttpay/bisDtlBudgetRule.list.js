var table;
var natualCurrency;
var selectType;
var othertable;
var showTable ="";
var dateType;
var exchangeRate;
var control;
bt.use(['form', 'datatable', 'dateType', 'moneyType', 'dicts', 'selectType', 'itemType', 'bank', 'corp','element','moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    moneyType=bt.moneyType.init();
    selectType = bt.selectType.init();
    var element = layui.element();
    bt.bank.init({
        elem: '.bank',
    })
    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }
    element.on('tab(paymentblending)', function(data){
        $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
    });
    var dicts = bt.dicts.init();

    /**
     * 选择单位回调
     * @param id
     */
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        var layid =  $("#intTab").find(".layui-this").attr('lay-id');
        // 重新商户号
        if("inner1" == layid ){
            var busTypeUrl = basePath + "/ttpay/bisdtlclaim?getMchId&cdSign=002&corpId=" + id;
            bt.selectType.get("eqs_mchId").attr("data-url", busTypeUrl);
        }else{
            var busTypeUrl = basePath + "/ttpay/bisdtlclaim?getMchId&cdSign=003&corpId=" + id;
            bt.selectType.get("eqs_othermchId").attr("data-url", busTypeUrl);
        }
    };
    bt.corp.init();

    /**
     * 重新组装参数
     * @param param
     * @param key
     * @returns {string}
     */
    var parseParam = function (param, key) {
        var paramStr = "";
        if (param instanceof String || param instanceof Number || param instanceof Boolean) {
            paramStr += "&" + key + "=" + encodeURIComponent(param);
        } else {
            $.each(param, function (i) {
                var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                paramStr += '&' + parseParam(this, k);
            });
        }
        return paramStr.substr(1);
    };

    /**
     * 弹出资金计划页面
     * @return
     */
    $genOtherHtml = function (el,tableName) {
        showTable = tableName;
        var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
        var row = layui.jquery(el).data("row");
        var itemCode = "";
        var sign = 1;
        if (data.cdSign == "0") {//根据收支确定科目
            sign = 1;
        } else {
            sign = -1;
        }
        var param = {
            "corpId": data.corpId,
            "itemCode": itemCode,
            "sign" : sign,
            "defItemCode": itemCode,
            "processDate": dateType.get("processDate" + row).val(),
            "type": 'open',
            "date": (new Date()).getTime(),
            "budget": $("#budget" + row).val(),
            "rowNum": row,
            "detailControl": true,
            "curId": data.curId ? data.curId : "",
            "wishPayday": dateType.get("processDate" + row).val(),
            "payeeAcc": data.payAcc ? data.payAcc : "",
        };
        var url = basePath + "/budget/fbsBudgetProcess/getOpenItemDetail";
        parent.layer.open({
            type: 2,
            area: ['1000px', '530px'],
            maxmin: true,
            content: url + "?" + parseParam(param),
            end: function () {
            }
        });
    };

    var selectTree;
    var itemType;
    var moneyFun = bt.moneyType.getFun();

    var appTableColumns = [
        {
            data: "trandTime", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.id)) {
                    html = '<input type="hidden" id="id" name="id" value=""><span class="" name="trandTime" data-defaultvalue="' + full.trandTime + '">' + full.trandTime + '</span>';
                } else {
                    html = '<input type="hidden" id="id" name="id" value="' + full.id + '"><span class="" name="trandTime" data-defaultvalue="' + full.trandTime + '">' + full.trandTime + '</span>';
                }
                return html;
            }
        },
        {
            data: "corpName", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                return '<input type="hidden" id="corpId" name="corpId" value="' + full.corpId + '"><span class="" name="transTime" data-defaultvalue="' + full.corpName + '">' + full.corpName + '</span>';
            }
        },
        {
            data: "appId", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.mchId)) {
                    html = '<span class="" name="mchId" data-defaultvalue="">' + full.mchId + '</span>';
                } else {
                    html = '<span class="" name="mchId" data-defaultvalue="">' + full.mchId + '/'+full.appId+'</span>';
                }
                return html;
            }
        },
        {
            data: "serialNo", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.serialNo)) {
                    html += '<span class="" name="serialNo" data-defaultvalue=""></span>';
                } else {
                    html += '<span class="" name="serialNo" data-defaultvalue="' + full.serialNo + '">' + full.serialNo + '</span>';
                }
                return html;
            }
        },
        {
            data: "trandNo", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.trandNo)) {
                    html += '<span class="" name="serialNo" data-defaultvalue=""></span>';
                } else {
                    html += '<span class="" name="serialNo" data-defaultvalue="' + full.trandNo + '">' + full.trandNo + '</span>';
                }
                return html;
            }
        },
        {
            data: "payAcc", bSortable: true,className: 'center'
        },
        {
            data: "tradeType", bSortable: true,
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.tradeType)) {
                    return '<span class="" name="tradeType" data-defaultvalue="">' + bt.dict("TTP_WX_TRADE_TYPE", full.tradeType) + '</span>';
                } else {
                    return '<span class="" name="tradeType" data-defaultvalue="' + full.tradeType + '">' + full.tradeType + '</span>';
                }
                return html;
            }
        },
        {
            data: "trandMoney", bSortable: false,
            render: function (data, type, full, meta) {
                return '<input type="hidden" id="money" name="money" value="' + full.trandMoney + '"/><input type="hidden" id="cdSign" name="cdSign" value="' + full.cdSign + '"/>' +
                    '<span class="" name="trandMoney" data-defaultvalue="' + full.trandMoney + '">' + moneyFun.format(full.trandMoney) + '</span>';
            }
        },
        {
            data: "fbsStatus", bSortable: true,
            render: function (data, type, full, meta) {
                return '<input type="hidden" id="fbsStatus" name="fbsStatus" value="' + full.fbsStatus + '"><span class="" name="fbsStatus" data-defaultvalue="' + full.fbsStatus + '">' + bt.dict("ttpay.fbsStatus", full.fbsStatus) + '</span>';
            }
        },
        {
            data: "busType", bSortable: false,className:'select-column',
            render: function (data, type, full, meta) {
                if (full.fbsStatus == 1 ) {
                    return full.busTypeName;
                } else {
                    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign="+full.cdSign+"&corpId=" + full.corpId;
                    var html = '';
                    html += '<div class="bt-biz-selectType" data-name="busType" data-biz-name="busType' + meta.row + '" data-defaultvalue="' + full.busType + '" ';
                    html += 'data-url="' + busTypeUrl + '" data-valuefield="busId" data-textfield="busTypeName" data-search="true" data-beforeinit="busBefore"  data-full="' + full.id + '" data-row="' + meta.row + '"></div>';
                    return html;
                }
            }
        },
        {
            data: "processDate", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            render: function (data, type, full, meta) {
                if (full.fbsStatus == 1) {
                    return full.processDate;
                } else {
                    var html = '<div class="layui-inline">';
                    if (isNull(full.processDate)) {
                        html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-btns= "clear,now,confirm" value="' + full.trandTime + '">';
                    } else {
                        html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-btns= "clear,now,confirm" value="' + full.processDate + '">';
                    }
                    html += '</div>'
                    return html;
                }
            }
        },
        {
            data: "itemInfo", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            render: function (data, type, full, meta) {
                if (full.fbsStatus == 1) {
                    if (!full.itemInfo) {
                        return '';
                    }
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this,\'appTable\')">' + bt.lang("common.button.view") + '</a>';
                } else {
                    var html = '<div class="layui-input-inline">';
                    itemFormat(full.itemInfo, meta.row);
                    if (isNull(full.item)) {
                        html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '">'
                    } else {
                        html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '" value="' + full.itemInfo + '">'
                    }
                    html += '</div>';
                    html += '<div class="layui-input-inline">'
                    html += '<input type="hidden" id="budget' + meta.row + '"  name="budget" ><button class="layui-btn" data-row="' + meta.row + '" onclick="$genOtherHtml(this,\'appTable\')" type="button">' + bt.lang('common.button.browse') + '</button>';
                    html += '</div>'
                    return html;
                }
            }
        },
        {
            data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                var html = '';
                if (full.fbsStatus == 1) {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="recover(this,\'appTable\')">' + bt.lang("common.button.repeal") + '</a>';//恢复
                } else if (full.fbsStatus == 2) {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="revivification(this,\'appTable\')">' + bt.lang("common.button.repeal") + '</a>';//还原
                } else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sure(this,\'appTable\')">' + bt.lang("common.button.sure") + '</a>';//确认
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this,\'appTable\')">' + bt.lang("common.button.cancel") + '</a>';//取消
                }
                html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this,\'appTable\')">' + bt.lang("common.button.view") + '</a>';//查看
                return html;
            }
        }
    ]
    // 如果不等于，那么就不需要业务类别busType
    if($("#isClaimBusType").val() != 1){
        appTableColumns.splice(9,1);
    }
    //生成列表
    table = bt.datatable.initMultiTables({
        elem: "#appTable",
        select: false,
        selectCss: false,
        clickRowSelected: false,
        checkbox: true,
        resize: false,
        isInitData:false,
        ignoreButtons:['state'],// 忽略的表单默认按钮 state取消状态保存按钮
        searchForm: {
            formElem: "#searchForm1",
            formFilter: "search1"
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/ttpay/bisdtlclaim/datatable"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        buttonsLayout: true,
        columns: appTableColumns,
        summary: {
            selectSum: true,
            sumList: [
                {
                    colNum: '7',
                    dataName: 'trandMoney'
                }
            ]
        },
        btInitComplete: function (settings, json) {
            dateType.render();
        },
        btDrawCallback: function () {
            dateType.render();
            dicts.render();
            selectType = bt.selectType.init();
        },
        btClickRowCallback: function (data) {
            var allSelectEdData = bt.datatable.getSelectData("#appTable");
            if (!allSelectEdData.length) {
                $("#itemDiv").hide();
                $("#box").hide();
                $("#budgetProcessDataTable").hide();
                hideItemDetail();
                return;
            }
            var isItem = true;//true 代表单位与收支标志一致，其它都为false;
            natualCurrency = true;
            var fbsStatus = true;
            if(allSelectEdData[0].fbsStatus !=0){
                fbsStatus = false;
            }
            for (var i = 1; i < allSelectEdData.length; i++) {
                if (allSelectEdData[0].cdSign != allSelectEdData[i].cdSign) {
                    isItem = false;
                    layer.alert(bt.lang('common.message.cdSign.accordance'), {icon: 2});//收支类型必须一致
                    break;
                }
                if (allSelectEdData[0].corpId != allSelectEdData[i].corpId) {
                    isItem = false;
                    layer.alert(bt.lang('common.message.corp.accordance'), {icon: 2});//单位必须一致
                    break;
                }

                if (allSelectEdData[i].fbsBudget != 0) {
                    $("#box").hide();
                    hideItemDetail();
                }
                if (allSelectEdData[0].fbsStatus != allSelectEdData[i].fbsStatus) {
                    fbsStatus = false;
                    break
                }
            }
            control = allSelectEdData[0].control;
            if (isItem) { //单位与收支标志一致 才执行
                if (!fbsStatus) {
                    $("#box").hide();
                    hideItemDetail();
                } else {
                    $("#box").show();
                }
                $("#itemDiv").show();
                var itemCode = "";
                if (!selectTree) {
                    $("#corpId").val(allSelectEdData[0].corpId);
                    var sign;
                    if (allSelectEdData[0].cdSign == "0") {//根据收支确定科目
                        sign = 1;
                    } else {
                        sign = -1;
                    }
                    $("#itemInfo").data("corpid", allSelectEdData[0].corpId).data("sign", sign).data("defaultvalue", itemCode);
                    if($("#budgetSel").val() == 1) {
                        if (!itemType) {
                            itemType = bt.itemType.init();
                        } else {
                            itemType.get("itemInfo").setUrl({
                                corpId: allSelectEdData[0].corpId,
                                itemCode: itemCode,
                                sign:sign,
                            });
                        }
                    }
                    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign="+allSelectEdData[0].cdSign+"&corpId=" +allSelectEdData[0].corpId;
                    var html = "<div class=\"bt-biz-selectType\" data-name=\"batchBusType\" data-biz-name=\"batchBusType\" data-defaultvalue=\"null\" data-url=\""+busTypeUrl+"\"";
                    html += " data-valuefield=\"busId\" data-textfield=\"busTypeName\" data-search=\"true\" data-beforeinit=\"busBefore\" ></div>";
                    $("#batchBusTypeDiv").html(html);
                    selectType = bt.selectType.init();
                }
            } else {
                $("#itemDiv").hide();
                $("#budgetProcessDataTable").hide();
                $("#batchBusTypeDiv").html("");
            }
        }
    });

    /**
     * 隐藏科目明细
     */
    function hideItemDetail() {
        var row = 0;
        $('#projectDetail' + row).parents("tr").remove();
        $("#projectDetailSum" + row).remove();
    }

    var dtlColIds = [];
    bt.each(dtlColSels, function (i, col) {
        var columnId = col;
        dtlColIds.push(columnId);
    });

    /**
     * 选择科目后的回调
     * @param obj   对象
     * @param name
     */
    itemChange = function (obj, name) {
        if (isNull(obj)) {
            $("#budgetProcessDataTable").hide();
            return;
        }
        $("#budgetProcessDataTable").show();
        $("#itemCodeText").text(obj.code);
        $("#itemNameText").text(obj.itemName);
        $("#itemName").val(obj.itemName);
        var param = {
            corpId: $("#corpId").val(),
            itemCode: obj.code,
            processDate: dateType.get("processDateBatch").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: basePath + "/budget/fbsBudgetProcess/genItemInfo",
            method: 'POST',
            data: param,
            async: false,
            dataType: 'json',
            success: function (data) {
                if (data && data.obj) {
                    $("#itemBalanceText").text(bt.money.format(data.obj.itemMoney));
                    $("#superItemBalanceText").text(bt.money.format(data.obj.superItemMoney));
                    $("#topItemBalanceText").text(bt.money.format(data.obj.topItemMoney));
                    exchangeRate = data.obj.exchangeRate;
                    if ($("#dtlColSel").val()) {
                        var row = 0;
                        if ($('#projectDetailTable' + row)) {
                            $('#projectDetailTable' + row).DataTable().destroy();
                        }
                        $('#projectDetail' + row).parents("tr").remove();
                        $("#projectDetailSum" + row).remove();
                        var html = '<tr><td colspan="5" id="projectDetail0"></td></tr>';
                        $("#budgetProcessDataTable").find("tbody >  tr[row=0]").after(html);
                        initProjectDetailHtml(obj.code, 0);
                    }
                }
                form.render();
            }
        });
    }
    itemInfoInit = function(ztree){
        if(ztree.getCheckedNodes(true).length >0) {
            itemChange(ztree.getCheckedNodes(true)[0]);
        }
    }

    /**
     * 选择科目后的回调
     * @param obj   对象
     * @param name
     */
    otheritemChange = function (obj, name) {
        if (isNull(obj)) {
            $("#otherbudgetProcessDataTable").hide();
            return;
        }
        $("#otherbudgetProcessDataTable").show();
        $("#otheritemCodeText").text(obj.code);
        $("#otheritemNameText").text(obj.itemName);
        $("#otheritemName").val(obj.itemName);
        var param = {
            corpId: $("#corpId").val(),
            itemCode: obj.code,
            processDate: dateType.get("otherprocessDateBatch").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: basePath + "/budget/fbsBudgetProcess/genItemInfo",
            method: 'POST',
            data: param,
            async: false,
            dataType: 'json',
            success: function (data) {
                if (data && data.obj) {
                    $("#otheritemBalanceText").text(bt.money.format(data.obj.itemMoney));
                    $("#othersuperItemBalanceText").text(bt.money.format(data.obj.superItemMoney));
                    $("#othertopItemBalanceText").text(bt.money.format(data.obj.topItemMoney));
                    exchangeRate = data.obj.exchangeRate;
                    if ($("#dtlColSel").val()) {
                        var row = 0;
                        if ($('#otherprojectDetailTable' + row)) {
                            $('#otherprojectDetailTable' + row).DataTable().destroy();
                        }
                        $('#otherprojectDetail' + row).parents("tr").remove();
                        $("#otherprojectDetailSum" + row).remove();
                        var html = '<tr><td colspan="5" id="projectDetail0"></td></tr>';
                        $("#otherbudgetProcessDataTable").find("tbody >  tr[row=0]").after(html);
                        otherinitProjectDetailHtml(obj.code, 0);
                    }
                }
                form.render();
            }
        });
    }
    otheritemInfoInit = function (ztree) {
        if(ztree.getCheckedNodes(true).length >0) {
            otheritemChange(ztree.getCheckedNodes(true)[0]);
        }
    }


    /**
     * 加载计划明细数据
     */
    function initProjectDetailHtml(itemCode, row) {
        if (!itemCode) {
            return;
        }
        var html = '<div><table id="projectDetailTable0"  class="layui-table" ></table></div>';
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
        columns.push({
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
        });
        bt.each(dtlColSels, function (i, col) {
            var columnId = col;
            if (columnId == "curId") {
                columnId = "curName";
            }
            columns.push({
                data: columnId, bSortable: false, className: 'center', title: name
            });
        });
        $("#projectDetail" + row).html(html);
        var data = {
            corpId: $("#corpId").val(),
            itemCode: itemCode,
            "processDate": dateType.get("processDateBatch").val(),
            "curId": $("#curId").val(),
            "wishPayday": dateType.get("processDateBatch").val(),
            "payerAcc": $("#payerAcc").val(),
            "payeeAccName": $("#payeeAccName").val(),
            "payeeAcc": $("#payeeAcc").val(),
            "payeeBank": $("#payeeBank").val(),
            "dtlColIds": dtlColIds,
        };
        //生成列表
        bt.datatable.initMultiTables({
            elem: "#projectDetailTable" + row,
            scrollY: '100px',
            dom: "rt",
            buttonsLayout: false,
            autoWidth: true,
            resize: false,
            ignoreButtons:['state'],// 忽略的表单默认按钮 state取消状态保存按钮
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
                        if (natualCurrency) {
                            // 人民币时，找折合人民币
                            if (data.bsuperNatualMoney) {
                                sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                sumMoney = bt.money.add(sumMoney, data.bsuperNatualMoney);
                            } else {
                                sumMoney = bt.money.add(sumMoney, data.natualMoney);
                            }
                        } else {
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
                genItemInfoByDetail(itemCode, row, sumMoney);
                if (!json.data.length) {
                    $('#projectDetailTable' + row).DataTable().destroy();
                    $('#projectDetail' + row).parents("tr").remove();
                }
            }
        });
    }

    /**
     * 加载计划明细数据
     */
    function otherinitProjectDetailHtml(itemCode, row) {
        if (!itemCode) {
            return;
        }
        var html = '<div><table id="otherprojectDetailTable0"  class="layui-table" ></table></div>';
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
        columns.push({
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
        });
        bt.each(dtlColSels, function (i, col) {
            var columnId = col;
            if (columnId == "curId") {
                columnId = "curName";
            }

            columns.push({
                data: columnId, bSortable: false, className: 'center', title: name
            });
        });
        $("#otherprojectDetail" + row).html(html);
        var data = {
            corpId: $("#corpId").val(),
            itemCode: itemCode,
            "processDate": dateType.get("otherprocessDateBatch").val(),
            "curId": $("#curId").val(),
            "wishPayday": dateType.get("otherprocessDateBatch").val(),
            "payerAcc": $("#payerAcc").val(),
            "payeeAccName": $("#payeeAccName").val(),
            "payeeAcc": $("#payeeAcc").val(),
            "payeeBank": $("#payeeBank").val(),
            "dtlColIds": dtlColIds,
        };
        //生成列表
        bt.datatable.init({
            elem: "#otherprojectDetailTable" + row,
            scrollY: '100px',
            dom: "rt",
            buttonsLayout: false,
            autoWidth: true,
            resize: false,
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
                        if (natualCurrency) {
                            // 人民币时，找折合人民币
                            if (data.bsuperNatualMoney) {
                                sumMoney = bt.money.add(sumMoney, data.natualMoney);
                                sumMoney = bt.money.add(sumMoney, data.bsuperNatualMoney);
                            } else {
                                sumMoney = bt.money.add(sumMoney, data.natualMoney);
                            }
                        } else {
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
                genItemInfoByDetail(itemCode, row, sumMoney);
                if (!json.data.length) {
                    $('#otherprojectDetailTable' + row).DataTable().destroy();
                    $('#otherprojectDetail' + row).parents("tr").remove();
                }
            }
        });
    }

    /**
     *  根据明细获取计划执行金额
     * @param itemCode
     * @param index
     */
    function genItemInfoByDetail(itemCode, index, sumMoney) {
        if (!$("#corpId").val() || itemCode.length == 0) {
            return;
        }
        var param = {
            corpId: $("#corpId").val(),
            itemCode: itemCode,
            "processDate": dateType.get("processDateBatch").val(),
            "curId": $("#curId").val(),
            "wishPayday": dateType.get("processDateBatch").val(),
            "payerAcc": $("#payerAcc").val(),
            "payeeAccName": $("#payeeAccName").val(),
            "payeeAcc": $("#payeeAcc").val(),
            "payeeBank": $("#payeeBank").val(),
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
                html += "</tr>";
                $('#projectDetail' + index).parents("tr").before(html);
            }
        });
    };

    var otherTableColumns =[
        {
            data: "trandTime", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.id)) {
                    html = '<input type="hidden" id="id" name="id" value=""><span class="" name="trandTime" data-defaultvalue="' + full.trandTime + '">' + full.trandTime + '</span>';
                } else {
                    html = '<input type="hidden" id="id" name="id" value="' + full.id + '"><span class="" name="trandTime" data-defaultvalue="' + full.trandTime + '">' + full.trandTime + '</span>';
                }
                return html;
            }
        },
        {
            data: "corpName", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                return '<input type="hidden" id="corpId" name="corpId" value="' + full.corpId + '"><span class="" name="transTime" data-defaultvalue="' + full.corpName + '">' + full.corpName + '</span>';
            }
        },
        {
            data: "appId", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.mchId)) {
                    html = '<span class="" name="mchId" data-defaultvalue="">' + full.mchId + '</span>';
                } else {
                    html = '<span class="" name="mchId" data-defaultvalue="">' + full.mchId + '/'+full.appId+'</span>';
                }
                return html;
            }
        },
        {
            data: "serialNo", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.serialNo)) {
                    html += '<span class="" name="serialNo" data-defaultvalue=""></span>';
                } else {
                    html += '<span class="" name="serialNo" data-defaultvalue="' + full.serialNo + '">' + full.serialNo + '</span>';
                }
                return html;
            }
        },
        {
            data: "trandNo", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.trandNo)) {
                    html += '<span class="" name="serialNo" data-defaultvalue=""></span>';
                } else {
                    html += '<span class="" name="serialNo" data-defaultvalue="' + full.trandNo + '">' + full.trandNo + '</span>';
                }
                return html;
            }
        },
        {
            data: "payAcc", bSortable: true,className: 'center'
        },
        {
            data: "tradeType", bSortable: true,
            render: function (data, type, full, meta) {
                var html = "";
                if (isNull(full.tradeType)) {
                    return '<span class="" name="tradeType" data-defaultvalue="">' + bt.dict("TTP_WX_TRADE_TYPE", full.tradeType) + '</span>';
                } else {
                    return '<span class="" name="tradeType" data-defaultvalue="' + full.tradeType + '">' + full.tradeType + '</span>';
                }
                return html;
            }
        },
        {
            data: "trandMoney", bSortable: false,
            render: function (data, type, full, meta) {
                return '<input type="hidden" id="money" name="money" value="' + full.trandMoney + '"/><input type="hidden" id="cdSign" name="cdSign" value="' + full.cdSign + '"/>' +
                    '<span class="" name="trandMoney" data-defaultvalue="' + full.trandMoney + '">' + moneyFun.format(full.trandMoney) + '</span>';
            }
        },
        {
            data: "fbsStatus", bSortable: true,
            render: function (data, type, full, meta) {
                return '<input type="hidden" id="fbsStatus" name="fbsStatus" value="' + full.fbsStatus + '"><span class="" name="fbsStatus" data-defaultvalue="' + full.fbsStatus + '">' + bt.dict("ttpay.fbsStatus", full.fbsStatus) + '</span>';
            }
        },
        {
            data: "busType", bSortable: false,className:'select-column', visible:$("#isClaimBusType").val() == 1,
            render: function (data, type, full, meta) {
                if (full.fbsStatus == 1 ) {
                    return full.busTypeName;
                } else {
                    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign="+full.cdSign+"&corpId=" + full.corpId;
                    var html = '';
                    html += '<div class="bt-biz-selectType" data-name="busType" data-biz-name="busType' + meta.row + '" data-defaultvalue="' + full.busType + '" ';
                    html += 'data-url="' + busTypeUrl + '" data-valuefield="busId" data-textfield="busTypeName" data-search="true" data-beforeinit="busBefore"  data-full="' + full.id + '" data-row="' + meta.row + '"></div>';
                    return html;
                }
            }
        },
        {
            data: "processDate", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            render: function (data, type, full, meta) {
                if (full.fbsStatus == 1) {
                    return full.processDate;
                } else {
                    var html = '<div class="layui-inline">';
                    if (isNull(full.processDate)) {
                        html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-btns= "clear,now,confirm" value="' + full.trandTime + '">';
                    } else {
                        html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-btns= "clear,now,confirm" value="' + full.processDate + '">';
                    }
                    html += '</div>'
                    return html;
                }
            }
        },
        {
            data: "itemInfo", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            render: function (data, type, full, meta) {
                if (full.fbsStatus == 1) {
                    if (!full.itemInfo) {
                        return '';
                    }
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this,\'otherTable\')">' + bt.lang("common.button.view") + '</a>';
                } else {
                    var html = '<div class="layui-input-inline">';
                    itemFormat(full.itemInfo, meta.row);
                    if (isNull(full.item)) {
                        html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '">'
                    } else {
                        html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '" value="' + full.itemInfo + '">'
                    }
                    html += '</div>';
                    html += '<div class="layui-input-inline">'
                    html += '<input type="hidden" id="budget' + meta.row + '"  name="budget" ><button class="layui-btn" data-row="' + meta.row + '" onclick="$genOtherHtml(this,\'otherTable\')" type="button">' + bt.lang('common.button.browse') + '</button>';
                    html += '</div>'
                    return html;
                }
            }
        },
        {
            data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                var html = '';
                if (full.fbsStatus == 1) {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="recover(this,\'otherTable\')">' + bt.lang("common.button.repeal") + '</a>';//恢复
                } else if (full.fbsStatus == 2) {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="revivification(this,\'otherTable\')">' + bt.lang("common.button.repeal") + '</a>';//还原
                } else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sure(this,\'otherTable\')">' + bt.lang("common.button.sure") + '</a>';//确认
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this,\'otherTable\')">' + bt.lang("common.button.cancel") + '</a>';//取消
                }
                html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this,\'otherTable\')">' + bt.lang("common.button.view") + '</a>';//查看
                return html;
            }
        }
    ];
    // 如果不等于，那么就不需要业务类别busType
    if($("#isClaimBusType").val() != 1){
        otherTableColumns.splice(9,1);
    }
    othertable = bt.datatable.initMultiTables({
        elem: "#otherTable",
        select: false,
        selectCss: false,
        clickRowSelected: false,
        checkbox: true,
        resize: false,
        isInitData:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/ttpay/bisdtlclaim/datatable"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        buttonsLayout: true,
        columns: otherTableColumns,
        summary: {
            selectSum: true,
            sumList: [
                {
                    colNum: '7',
                    dataName: 'trandMoney'
                }
            ]
        },
        btInitComplete: function (settings, json) {
            dateType.render();
        },
        btDrawCallback: function () {
            dateType.render();
            dicts.render();
            selectType = bt.selectType.init();
        },
        btClickRowCallback: function (data) {
            var allSelectEdData = bt.datatable.getSelectData("#otherTable");
            if (!allSelectEdData.length) {
                $("#otheritemDiv").hide();
                $("#otherbox").hide();
                $("#otherbudgetProcessDataTable").hide();
                hideItemDetail();
                return;
            }
            var isItem = true;//true 代表单位与收支标志一致，其它都为false;
            natualCurrency = true;
            var fbsStatus = true;
            if(allSelectEdData[0].fbsStatus !=0){
                fbsStatus = false;
            }
            for (var i = 1; i < allSelectEdData.length; i++) {
                if (allSelectEdData[0].cdSign != allSelectEdData[i].cdSign) {
                    isItem = false;
                    layer.alert(bt.lang('common.message.cdSign.accordance'), {icon: 2});//收支类型必须一致
                    break;
                }
                if (allSelectEdData[0].corpId != allSelectEdData[i].corpId) {
                    isItem = false;
                    layer.alert(bt.lang('common.message.corp.accordance'), {icon: 2});//单位必须一致
                    break;
                }

                if (allSelectEdData[i].fbsBudget != 0) {
                    $("#otherbox").hide();
                    hideItemDetail();
                }
                if (allSelectEdData[0].fbsStatus != allSelectEdData[i].fbsStatus) {
                    fbsStatus = false;
                    break
                }
            }
            control = allSelectEdData[0].control;
            if (isItem) { //单位与收支标志一致 才执行
                if (!fbsStatus) {
                    $("#otherbox").hide();
                    hideItemDetail();
                } else {
                    $("#otherbox").show();
                }
                $("#otheritemDiv").show();
                var itemCode = "";
                if (!selectTree) {
                    $("#corpId").val(allSelectEdData[0].corpId);
                    var sign;
                    if (allSelectEdData[0].cdSign == "0") {//根据收支确定科目
                        sign = 1;
                    } else {
                        sign = -1;
                    }
                    $("#otheritemInfo").data("corpid", allSelectEdData[0].corpId).data("sign", sign).data("defaultvalue", itemCode);
                    if($("#budgetSel").val() == 1) {
                        if (!itemType) {
                            itemType = bt.itemType.init();
                        } else {
                            itemType.get("otheritemInfo").setUrl({
                                corpId: allSelectEdData[0].corpId,
                                itemCode: itemCode,
                                sign:sign,
                            });
                        }
                    }
                    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign="+allSelectEdData[0].cdSign+"&corpId=" +allSelectEdData[0].corpId;
                    var html = "<div class=\"bt-biz-selectType\" data-name=\"batchBusType\" data-biz-name=\"otherBatchBusType\" data-defaultvalue=\"null\" data-url=\""+busTypeUrl+"\"";
                    html += " data-valuefield=\"busId\" data-textfield=\"busTypeName\" data-search=\"true\" data-beforeinit=\"busBefore\" ></div>";
                    $("#otherbatchBusTypeDiv").html(html);
                    selectType = bt.selectType.init();
                }
            } else {
                $("#otheritemDiv").hide();
                $("#otherbudgetProcessDataTable").hide();
                $("#otherbatchBusTypeDiv").html("");
            }
        }
    });

});

var BUDGET_PROCESS_SPLIT = "!-!";

/**
 * 资金计划回调
 * @param sumMoney
 * @param datas
 * @param rowNum
 */
function budgetProcessCallback(sumMoney, datas, rowNum) {
    var it = "";
    var temp = "";
    var data = layui.jquery("#appTable").DataTable().row(rowNum).data();
    if(!data){
        data = layui.jquery("#otherTable").DataTable().row(rowNum).data();
    }
    if (Math.abs(bt.money.toNumber(data.trandMoney)) !== bt.money.toNumber(sumMoney)) {
        layer.alert(bt.lang('common.message.money.noEqual'), {icon: 7});
        return;
    }
    $(datas).each(function (i, item) {
        it = item.split(BUDGET_PROCESS_SPLIT);
        if (i == 0) {
            temp += "(" + it[0] + ")" + "" + it[1];
        } else {
            temp = temp + ";" + "(" + it[0] + ")" + "" + it[1];
        }
    });
    var itemCode = $("#itemCode" + rowNum).val(temp);
    $("#budget" + rowNum).val(datas);
}

function itemFormat(temp, rowNum) {
    if (temp != null && temp != "" && typeof(temp) != "undefined") {
        var tempStr = "";
        var arr = temp.split(",");
        $(arr).each(function (i, item) {
            it = item.split(BUDGET_PROCESS_SPLIT);
            if (i == 0) {
                tempStr += "(" + it[0] + ")" + "" + it[1];
            } else {
                tempStr = tempStr + ";" + "(" + it[0] + ")" + "" + it[1];
            }
        });
        var itemCode = $("#itemCode" + rowNum).val(tempStr);
        $("#budget" + rowNum).val(temp);
    }
}

/**
 * 确认
 * @param el
 */
function sure(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    var row = layui.jquery(el).data("row");
    var busType ="";//业务类别
    if($("#isClaimBusType").val() == 1){
        busType = selectType.get("busType" + row).getData().value.trim();
        if(busType == "" || busType == null){
            layer.alert(bt.lang('nis.pay.message.bustype.null'), {icon: 7});
            return;
        }
    }
    data.busType = busType;
    data.fbsStatus = (data.fbsStatus).toString();
    if($("#budgetSel").val() == 1) {
        data.budget = $("#budget" + row).val();
        data.processDate = dateType.get("processDate" + row).val();
        if (data.budget.length < 1) {
            layer.alert(bt.lang('table.bisAccDtlBudgetRule.selectBudget'), {icon: 7});
            return;
        }
    }
    var action = "/ttpay/bisdtlclaim/sure"
    $.post(basePath + action, data, function (result) {
        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
        bt.loading.hide(); //关闭加载中
        if (result.success) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                $("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }
    }, 'JSON');
    return false;
}

/**
 * 恢复
 */
function recover(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.determine.repeal'), function (result) {
        $.post(basePath + "/ttpay/bisdtlclaim/business/" + data.id + "/H", function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                $("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 还原
 */
function revivification(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.determine.repeal'), function (result) {
        $.post(basePath + "/ttpay/bisdtlclaim/business/" + data.id + "/R", function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                $("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


/**
 * 删除
 */
function deleteColumn(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.cancelClaim'), function () {
        $.post(basePath + "/ttpay/bisdtlclaim/business/" + data.id + "/D", function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                $("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量删除
 */
function batchDelete(tableName) {
    var selectDataTemp = selectData(tableName);
    if (selectDataTemp.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    $(selectDataTemp).each(function (i, item) {
        if (i == 0) {
            ids += item.id;
        } else {
            ids += "," + item.id;
        }
    });
    var action = "/ttpay/bisdtlclaim/batch"
    var data = {"ids": ids, "type": "D", "itemCode": null, "itemName": null, "processDate": null,"batchBusType":null};
    parent.layer.confirm(bt.lang('common.message.confirm.cancelClaim'), function () {
        $.post(basePath + action, data, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                $("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 查看
 * @param el
 */
function view(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/bisdtlclaim/view/" + data.id
    });
}

/**
 * 查看
 * @param el
 */
function viewDtl(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/ttpay/bisdtlclaim/viewDtl/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/bisdtlclaim/viewDtl/" + data.id
    });
}

/**
 * 批量选中数据
 */
var selectData = function (tableName) {
    var trs = $("#"+tableName).find("tr.selected");
    var selectData = [];
    // 遍历行数据
    trs.each(function (index, tr) {
        var formObj = {};
        var tds = $(tr).find("td");
        tds.each(function (i, td) {
            var $span = $(td).children("span");
            var $input = $(td).find("input");
            var $select = $(td).find("select");
            if ($input.length && !$select.length) {
                bt.each($input, function () {
                    var text = $(this).val();
                    var name = $(this).attr("name");
                    if (name == undefined) {
                        name = $input.attr("data-name");
                    }
                    if (name != undefined) {
                        formObj[name] = text;
                    }
                });
            }
            if ($select.length) {
                var text = $select.val();
                var name = $select.attr("name");
                formObj[name] = text;
            }
            if ($span.length) {
                var text = $span.text();
                var name = $span.attr("name");
                formObj[name] = text;
            }
        });
        selectData.push(formObj);
    });
    return selectData;
}

/**
 * 批量确认
 */

function batchSure(tableName) {
    var selectDataTemp = selectData(tableName);
    if (selectDataTemp.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var totalMoney = 0;
    $(selectDataTemp).each(function (i, item) {
        if (i == 0) {
            ids += item.id;
        } else {
            ids += "," + item.id;
        }
        if (natualCurrency) {
            totalMoney = bt.money.add(totalMoney, bt.money.mul(item.money, exchangeRate));
        } else {
            totalMoney = bt.money.add(totalMoney, item.money);
        }
    });
    var batchBusType = "";
    if ($("#isClaimBusType").val() == 1) {
        if(tableName == 'otherTable') {
            batchBusType = selectType.get("otherBatchBusType").getData().value.trim();
        }else{
            batchBusType = selectType.get("batchBusType").getData().value.trim();
        }
        if(batchBusType.length < 1) {
            layer.alert(bt.lang('nis.pay.message.bustype.null'), {icon: 7});
            return;
        }
    }
    var itemCodeTemp = "";
    var itemNameTemp = "";
    var processDateBatch = "";
    if($("#budgetSel").val() == 1) {
        itemCodeTemp = bt.itemType.get("itemInfo").val();
        itemNameTemp = $("#itemName").val();
        processDateBatch = bt.dateType.get("processDateBatch").val();
        if (tableName == 'otherTable') {
            itemCodeTemp = bt.itemType.get("otheritemInfo").val();
            itemNameTemp = $("#otheritemName").val();
            processDateBatch = bt.dateType.get("otherprocessDateBatch").val();
        }
        if (itemCodeTemp.length < 1) {
            layer.alert(bt.lang('table.bisAccDtlBudgetRule.selectBudget'), {icon: 7});
            return;
        }
        // 当前填写金额对应科目
        if (tableName == 'otherTable') {
            var item = bt.itemType.get("otheritemInfo").data()[0];
        } else {
            var item = bt.itemType.get("itemInfo").data()[0];
        }
        var iptRow = 0;
        var fbsItemBalance = $("#fbsItemBalance").val();
        // 校验执行金额填写
        if (item.sign == -1 && item.isControl == 1 && control) {
            //支出为负数取绝对值
            totalMoney = Math.abs(totalMoney);
            if ($("#detailBalance" + iptRow).text()) {
                var detailBalance = bt.money.toNumber($("#detailBalance" + iptRow).text());
                if (detailBalance < totalMoney) {
                    layer.alert(bt.lang('fbs.message.detailmoney.gt.detailbalance.batch'), {icon: 2}, function () {
                        layer.closeAll();
                    });
                    return false;
                }
            } else {
                if (fbsItemBalance == 0) {
                    var itemMoney = bt.money.toNumber($("#itemBalanceText").text());
                    if (tableName == 'otherTable') {
                        itemMoney = bt.money.toNumber($("#otheritemBalanceText").text());
                    }
                    if (itemMoney < totalMoney) {
                        layer.alert(bt.lang('fbs.message.detailmoney.gt.itembalance.batch'), {icon: 2}, function () {
                            layer.closeAll();
                        });
                        return false;
                    }
                } else if (fbsItemBalance == 1) {
                    var itemMoney = bt.money.toNumber($("#topItemBalanceText").text());
                    if (tableName == 'otherTable') {
                        itemMoney = bt.money.toNumber($("#othertopItemBalanceText").text());
                    }
                    if (itemMoney < totalMoney) {
                        layer.alert(bt.lang('fbs.message.detailmoney.gt.topitembalance.batch'), {icon: 2}, function () {
                            layer.closeAll();
                        });
                        return false;
                    }
                } else if (fbsItemBalance == 2) {
                    var itemMoney = bt.money.toNumber($("#superItemBalanceText").text());
                    if (tableName == 'otherTable') {
                        itemMoney = bt.money.toNumber($("#othersuperItemBalanceText").text());
                    }
                    if (itemMoney < totalMoney) {
                        layer.alert(bt.lang('fbs.message.detailmoney.gt.superitembalance.batch'), {icon: 2}, function () {
                            layer.closeAll();
                        });
                        return false;
                    }
                }
            }
        }
    }
    var action = "/ttpay/bisdtlclaim/batch"
    var data = {
        "ids": ids,
        "type": "Y",
        "itemCode": itemCodeTemp,
        "itemName": itemNameTemp,
        "processDate": processDateBatch,
        "batchBusType":batchBusType,
    };
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + action, data, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                $("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


function isNull(data) {
    if (data == null || data == "null" || data == "" || data == undefined) {
        return true;
    } else {
        return false;
    }
}

function pushTable(tableName){
    bt.datatable.refreshTable(tableName);
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
};

function busTypeCallback(data,row) {
    if(data){
        $("#billId"+row).val(data.id);
    }
    parent.layer.closeAll();
}

