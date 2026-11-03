var table;
var $genOtherHtml;
var natualCurrency;
bt.use(['form', 'datatable', 'dateType', 'select', 'moneyType', 'dicts', 'date', 'itemType'], function () {
    $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    bt.dicts.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }

    /**
     * 重新组装参数
     * @param param
     * @param key
     * @returns {string}
     */
    var parseParam = function (param, key) {
        var paramStr = "";

        // 处理 null 值（可根据需求决定是否忽略或转为空字符串）
        if (param === null) {
            paramStr += "&" + key + "=";
        }
        // 判断基本类型（包括对应的包装对象）
        else if (typeof param === 'string' || typeof param === 'number' || typeof param === 'boolean' || param instanceof String || param instanceof Number || param instanceof Boolean) {
            paramStr += "&" + key + "=" + encodeURIComponent(param);
        }
        // 处理数组或对象
        else {
            $.each(param, function (i, val) {   // 使用第二个参数 val 代替 this
                var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                paramStr += '&' + parseParam(val, k);
            });
        }
        return paramStr.substr(1);
    };

    /**
     * 弹出资金计划页面
     * @return
     */
    $genOtherHtml = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var row = layui.jquery(el).data("row");
        var itemCode = "";
        if (data.cdSign == "0") {//根据收支确定科目
            itemCode = "200";
        } else {
            itemCode = "400";
        }
        var param = {
            corpId: data.corpId,
            itemCode: itemCode,
            defItemCode: itemCode,
            processDate: dateType.get("processDate" + row).val(),
            type: 'open',
            date: (new Date()).getTime(),
            budget: $("#budget" + row).val(),
            rowNum: row,
            "detailControl": true,
            "curId": data.curId ? data.curId : "",
            "settlType": 0,
            "purpose": "",
            "wishPayday": dateType.get("processDate" + row).val(),
            "payerAcc": data.blnctrAcc ? data.blnctrAcc : "",
            "payeeAccName": data.oppAccName ? data.oppAccName : "",
            "payeeAcc": data.oppAccNo ? data.oppAccNo : "",
            "payeeBank": data.oppAccBank ? data.oppAccBank : "",
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
    //生成列表
    table = bt.datatable.init({
        elem: "#appTable",
        //dom: 'Brtlip',
        select: false,
        selectCss: false,
        clickRowSelected: false,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcaccdetailbudgetmatch/datatable"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        buttonsLayout: true,
        columns: [
            {
                data: "tallyDate", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.id)) {
                        html = '<input type="hidden" id="id" name="id" value=""><span class="" name="tallyDate" data-defaultvalue="' + full.tallyDate + '">' + full.tallyDate + '</span>';
                    } else {
                        html = '<input type="hidden" id="id" name="id" value="' + full.id + '"><span class="" name="tallyDate" data-defaultvalue="' + full.tallyDate + '">' + full.tallyDate + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "blnctrAcc", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.blnctrAcc)) {
                        html = '<span class="" name="blnctrAcc" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="blnctrAcc" data-defaultvalue="' + full.blnctrAcc + '">' + full.blnctrAcc + '</span>';
                    }
                    return html;
                }
            },
            {data: "accName", bSortable: true},
            {
                data: "oppAccNo", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.oppAccNo)) {
                        html = '<span class="" name="oppAccNo" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="oppAccNo" data-defaultvalue="' + full.oppAccNo + '">' + full.oppAccNo + '</span>';
                    }
                    return html;
                }
            },
            {data: "oppAccName", bSortable: true},
            {
                data: "cdSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="cdSignTemp" name="cdSignTemp" value="' + full.cdSign + '"><span class="" name="cdSign" data-defaultvalue="' + full.cdSign + '">' + bt.dict("btBankAccDtl.cdSign", full.cdSign) + '</span>';
                }
            },
            {
                data: "debitMoney", bSortable: true, className: "textRight", render: function (data, type, full, meta) {
                    return '<input type="hidden" id="debitMoney" name="debitMoney" value="' + full.debitMoney + '">' + moneyFun.format(full.debitMoney);
                }
            },
            {
                data: "creditMoney",
                bSortable: true,
                className: "textRight",
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="creditMoney" name="creditMoney" value="' + full.creditMoney + '">' + moneyFun.format(full.creditMoney);
                }
            },

            {
                data: "voucherNo", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = '<input type="hidden" id="curExrate" name="curExrate" value="' + full.curExrate + '">';
                    html += '<input type="hidden" id="dayCloseSign" name="dayCloseSign" value="' + full.dayCloseSign + '">' + full.voucherNo;
                    return html;
                }
            },
            {data: "orderNo", bSortable: true,},
            {
                data: "fbsStatus", bSortable: false,
                render: function (data, type, full, meta) {
                    if (isNull(full.fbsStatus) || full.fbsStatus == 0) {
                        full.fbsStatus = "0"
                    } else if (full.fbsStatus == 1) {
                        full.fbsStatus = "1"
                    } else {
                        full.fbsStatus = "2"
                    }
                    return '<span class="" name="fbsStatus" data-defaultvalue="' + full.fbsStatus + '">' + bt.dict("bisAccDtlBudgetRule.fbsStatus", full.fbsStatus) + '</span>';
                }
            },
            {
                data: "processDate", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.fbsStatus == 0) {
                        var html = '<div class="layui-inline">';
                        if (isNull(full.processDate)) {
                            html += '<input type="text" name="processDate" style="width: 110px;" data-biz-name="processDate' + meta.row + '" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-today="true">';
                        } else {
                            html += '<input type="text" name="processDate" style="width: 110px;" data-biz-name="processDate' + meta.row + '" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" value="' + full.processDate + '">';
                        }
                        html += '</div>'
                        return html;
                    } else {
                        return full.processDate;
                    }
                }
            },
            {
                data: "itemInfo", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.fbsStatus == 0) {
                        var html = '<div class="layui-input-inline">';
                        itemFormat(full.itemInfo, meta.row);
                        html += '<input type="hidden" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" value="' + full.itemInfo + '">'
                        html += '<input type="text" id="itemCodeText' + meta.row + '" name="itemCodeText" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '" value="' + full.itemInfoText + '">'
                        html += '</div>';
                        html += '<div class="layui-input-inline">'
                        html += '<input type="hidden" id="budget' + meta.row + '"  name="budget" ><button class="layui-btn" data-row="' + meta.row + '" onclick="$genOtherHtml(this)" type="button">' + bt.lang('common.button.browse') + '</button>';
                        html += '</div>'
                        return html;
                    } else {
                        if (!full.itemInfo) {
                            return '';
                        }
                        return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    }
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:innerAccount:fcAccDetailBudgetMatch:apply',full.corpId)) {
                        if (full.fbsStatus == 1) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="recover(this)">' + bt.lang("common.button.recover") + '</a>';//恢复
                        } else if (full.fbsStatus == 2) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="revivification(this)">' + bt.lang("common.button.revivification") + '</a>';//还原
                        } else {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sure(this)">' + bt.lang("common.button.sure") + '</a>';//确认
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';//删除
                        }
                    }
                    return html;
                }
            }
        ],
        summary: {
            selectSum: true,
            sumList: [
                {
                    colNum: '7',
                    dataName: 'debitMoney'
                },
                {
                    colNum: '8',
                    dataName: 'creditMoney'
                }
            ]
        },
        btInitComplete: function (settings, json) {
            dateType.render();
        },
        btDrawCallback: function () {
            dateType.render();
        },
        btClickRowCallback: function (data) {
            var allSelectEdData = bt.datatable.getSelectData("#appTable");
            if (!allSelectEdData.length) {
                $("#itemDiv").hide();
                $("#box").hide();
                $("#budgetProcessDataTable").hide();
                return;
            }
            var isItem = true;//true 代表单位与收支标志一致，其它都为false;
            var selectCurId = allSelectEdData[0].curId;
            natualCurrency = true;
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

                if (dtlColSels["payerAcc"]) {
                    if (allSelectEdData[0].blnctrAcc != allSelectEdData[i].blnctrAcc) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeracc.accordance'), {icon: 2});//付款账户必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeAccName"]) {
                    if (allSelectEdData[0].oppAccName != allSelectEdData[i].oppAccName) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeeaccname.accordance'), {icon: 2});//收款账户名必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeAcc"]) {
                    if (allSelectEdData[0].oppAccNo != allSelectEdData[i].oppAccNo) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeeacc.accordance'), {icon: 2});//收款账户必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeBank"]) {
                    if (allSelectEdData[0].oppAccBank != allSelectEdData[i].oppAccBank) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeebank.accordance'), {icon: 2});//收款开户行必须一致
                        break;
                    }
                }

                if (allSelectEdData[i].fbsBudget != 0) {
                    $("#box").hide();
                }
                if (selectCurId.indexOf(allSelectEdData[i].curId) == -1) {
                    selectCurId += "," + allSelectEdData[i].curId;
                }
            }
            if (isItem) { //单位与收支标志一致 才执行
                if (data[0].fbsStatus != 0) {
                    $("#box").hide();
                } else {
                    $("#box").show();
                }
                $("#itemDiv").show();
                var itemCode = "";
                if (allSelectEdData[0].isSelect) {
                    if (allSelectEdData[0].cdSign == "0") {
                        itemCode = "200";
                    } else {
                        itemCode = "400";
                    }
                }
                if (!selectTree) {
                    $("#corpId").val(allSelectEdData[0].corpId);
                    $("#itemInfo").data("corpid", allSelectEdData[0].corpId).data("itemcode", itemCode).data("defaultvalue", itemCode);
                    if (selectCurId.indexOf($("#natualCurId").val()) == -1) {
                        natualCurrency = false;
                    }
                    $("#curId").val(allSelectEdData[0].curId);
                    $("#payerAcc").val(allSelectEdData[0].blnctrAcc);
                    $("#payeeAccName").val(allSelectEdData[0].oppAccName);
                    $("#payeeAcc").val(allSelectEdData[0].oppAccNo);
                    $("#payeeBank").val(allSelectEdData[0].oppAccBank);
                    if (!itemType) {
                        itemType = bt.itemType.init();
                    } else {
                        itemType.get("itemInfo").setUrl({
                            corpId: allSelectEdData[0].corpId,
                            itemCode: itemCode,
                        });
                        return false;
                    }
                }
            } else {
                $("#itemDiv").hide();
                $("#budgetProcessDataTable").hide();
            }
        }
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
        $("#isControl").val(obj.isControl);
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


    /**
     * 加载计划明细数据
     */
    function initProjectDetailHtml(itemCode, row) {
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
            if (columnId == "settlType") {
                columnId = "settlTypeName";
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
            "settlType": 0,
            "wishPayday": dateType.get("processDateBatch").val(),
            "payerAcc": $("#payerAcc").val(),
            "payeeAccName": $("#payeeAccName").val(),
            "payeeAcc": $("#payeeAcc").val(),
            "payeeBank": $("#payeeBank").val(),
            "dtlColIds": $("#dtlColSel").val(),
        };
        //生成列表
        bt.datatable.init({
            elem: "#projectDetailTable" + row,
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
                    $('#projectDetailTable' + row).DataTable().destroy();
                    $('#projectDetail' + row).parents("tr").remove();
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
            "settlType": 0,
            "wishPayday": dateType.get("processDateBatch").val(),
            "payerAcc": $("#payerAcc").val(),
            "payeeAccName": $("#payeeAccName").val(),
            "payeeAcc": $("#payeeAcc").val(),
            "payeeBank": $("#payeeBank").val(),
            "dtlColIds": $("#dtlColSel").val(),
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
    if (data.cdSign == "0") {//收入 跟贷方发生额比较
        if (bt.money.toNumber(data.creditMoney) !== bt.money.toNumber(sumMoney)) {
            layer.alert(bt.lang('common.message.money.noEqual'), {icon: 7});
            return;
        }
    } else {//支出 跟借方发生额比较
        if (bt.money.toNumber(data.debitMoney) !== bt.money.toNumber(sumMoney)) {
            layer.alert(bt.lang('common.message.money.noEqual'), {icon: 7});
            return;
        }
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
    $("#itemCodeText" + rowNum).val(temp);
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
        $("#itemCodeText" + rowNum).val(tempStr);
        $("#budget" + rowNum).val(temp);
    }
}

function isNull(data) {
    if (data == null || data == "null" || data == "" || data == undefined) {
        return true;
    } else {
        return false;
    }
}

/**
 * 确认
 * @param el
 */
function sure(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var row = layui.jquery(el).data("row");
    data.budget = $("#budget" + row).val();
    data.fbsStatus = (data.fbsStatus).toString();
    data.processDate = bt.dateType.get("processDate" + row).val();
    if (data.budget.length < 1) {
        layer.alert(bt.lang('table.bisAccDtlBudgetRule.selectBudget'), {icon: 7});
        return;
    }
    var action = "/inneraccount/fcaccdetailbudgetmatch/sure"
    $.post(basePath + action, data, function (result) {
        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
        bt.loading.hide(); //关闭加载中
        if (result.success) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                var webObj = bt.navtab.getCurrentDom();
                webObj.jQuery("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }
    }, 'JSON');
    return false;
}

/**
 * 恢复
 */
function recover(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.recover'), function (result) {
        $.post(basePath + "/inneraccount/fcaccdetailbudgetmatch/business/" + data.id + "/H" + "/" + data.dayCloseSign, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                var webObj = bt.navtab.getCurrentDom();
                webObj.jQuery("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 还原
 */
function revivification(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.revivification'), function (result) {
        $.post(basePath + "/inneraccount/fcaccdetailbudgetmatch/business/" + data.id + "/R" + "/" + data.dayCloseSign, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                var webObj = bt.navtab.getCurrentDom();
                webObj.jQuery("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 删除
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/inneraccount/fcaccdetailbudgetmatch/business/" + data.id + "/D" + "/" + data.dayCloseSign, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                var webObj = bt.navtab.getCurrentDom();
                webObj.jQuery("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccdetailbudgetmatch/view/" + data.id
    });
}

/**
 * 批量选中数据
 */
var selectData = function () {
    var trs = $("#appTable").find("tr.selected");
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
                var text = $input.val();
                var name = $input.attr("name");
                formObj[name] = text;
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
function batchSure() {
    var selectDataTemp = selectData();
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
            totalMoney = bt.money.add(totalMoney, bt.money.mul(item.debitMoney, item.curExrate));
        } else {
            totalMoney = bt.money.add(totalMoney, item.debitMoney);
        }
    });
    var itemCodeTemp = bt.itemType.get("itemInfo").val();
    var itemNameTemp = $("#itemName").val();
    var processDateBatch = bt.dateType.get("processDateBatch").val();
    if (itemCodeTemp.length < 1) {
        layer.alert(bt.lang('table.bisAccDtlBudgetRule.selectBudget'), {icon: 7});
        return;
    }
    // 当前填写金额对应科目
    var item = bt.itemType.get("itemInfo").data()[0];
    var iptRow = 0;
    var fbsItemBalance = $("#fbsItemBalance").val();
    // 校验执行金额填写
    if (item.sign == -1 && item.isControl == 1) {
        // zuoy 根据明细金额校验 2018-11-19
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
                if (bt.money.toNumber($("#itemBalance" + iptRow).val()) < totalMoney) {
                    layer.alert(bt.lang('fbs.message.detailmoney.gt.itembalance.batch'), {icon: 2}, function () {
                        layer.closeAll();
                    });
                    return false;
                }
            } else if (fbsItemBalance == 1) {
                if (bt.money.toNumber($("#topItemBalance" + iptRow).val()) < totalMoney) {
                    layer.alert(bt.lang('fbs.message.detailmoney.gt.topitembalance.batch'), {icon: 2}, function () {
                        layer.closeAll();
                    });
                    return false;
                }
            } else if (fbsItemBalance == 2) {
                if (bt.money.toNumber($("#superItemBalance" + iptRow).val()) < totalMoney) {
                    layer.alert(bt.lang('fbs.message.detailmoney.gt.superitembalance.batch'), {icon: 2}, function () {
                        layer.closeAll();
                    });
                    return false;
                }
            }
        }
    }

    var action = "/inneraccount/fcaccdetailbudgetmatch/batch"
    var data = {
        "ids": ids,
        "type": "Y",
        "itemCode": itemCodeTemp,
        "itemName": itemNameTemp,
        "processDate": processDateBatch
    };
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + action, data, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                var webObj = bt.navtab.getCurrentDom();
                webObj.jQuery("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量删除
 */
function batchDelete() {
    var selectDataTemp = selectData();
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
    var action = "/inneraccount/fcaccdetailbudgetmatch/batch"
    var data = {"ids": ids, "type": "D", "itemCode": null, "itemName": null, "processDate": null};
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + action, data, function (result) {
            if (result.success) {
                bt.datatable.refreshCurrentPage();
                var webObj = bt.navtab.getCurrentDom();
                webObj.jQuery("#box").hide();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

