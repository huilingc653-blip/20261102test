var table;
var natualCurrency;
var selectType;
var othertable;
var showTable ="";
var dateType;
var exchangeRate;
bt.use(['form', 'datatable', 'dateType', 'moneyType', 'dicts', 'accountTable', 'selectType', 'itemType', 'bank', 'corp','element'], function () {
    var $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    var element = layui.element();
    bt.moneyType.init();
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
    /**
     * 结算类型选择回调
     */
    settlTypeBatchCallback = function () {
        if ($("#dtlColSel").val() && dtlColSels["settlType"]) {
            var row = 0;
            if ($('#projectDetailTable' + row)) {
                $('#projectDetailTable' + row).DataTable().destroy();
            }
            $('#projectDetail' + row).parents("tr").remove();
            $("#projectDetailSum" + row).remove();
            var html = '<tr><td colspan="5" id="projectDetail0"></td></tr>';
            $("#budgetProcessDataTable").find("tbody >  tr[row=0]").after(html);
            initProjectDetailHtml(bt.itemType.get("itemInfo").val(), 0);
        }
    }
    var dicts = bt.dicts.init();


    /**
     * 其他付款流水本方账户回调
     * @param data
     */
    accountOtherCallBack = function (data) {
        $("#likes_otherBankAcc").val(data.bankAcc);
    }

    /**
     * 收款流水本方账户回调
     * @param data
     */
    bt.accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
    }

    var accountTable = bt.accountTable.init();
    /**
     * 选择单位回调
     * @param id
     */
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        var layid =  $("#intTab").find(".layui-this").attr('lay-id');
        if("inner1" == layid ){
            accountTable.get("likes_bankAcc").setCorp(id);
        }else{
            accountTable.get("likes_otherBankAcc").setCorp(id);
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
        var settlType = dicts.get("settlType" + row).val();
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
            "settlType": settlType ? settlType : 0,
            "purpose": data.remark ? data.remark : "",
            "wishPayday": dateType.get("processDate" + row).val(),
            "payerAcc": data.bankAcc ? data.bankAcc : "",
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
    /**
     * 弹出收款单页面
     * @return
     */
    $genNisReceivable = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var row = layui.jquery(el).data("row");
        var isClaimBusType = $("#isClaimBusType").val()
        var busType = "";//业务类别
        if(isClaimBusType == 1){
            busType = selectType.get("busType"+row).getData();
        }
        var bankSerialNo = data.bankSerialNo;//银行流水号
        var bankAcc = data.bankAcc;//本方账号
        var oppAccNo = data.oppAccNo;//对方账号
        var curId = data.curId;//币别
        var id = data.id;//id
        var amt = data.amt;
        var url = "/receivable/nisreceivable/receivablePage?bankAcc="+bankAcc+"&oppAccNo="+oppAccNo+"&curId="+curId+"&amt="+amt+"&bankSerialNo=";
        //如果bankSerialNo 为空。则更新为id
        if(isNull(bankSerialNo) || bankSerialNo.trim() == ""){
            bankSerialNo = id;
            $.ajax({
                url: basePath + "/bankacc/paymentblending?updateDtlBankSerialNo",
                method: 'POST',
                data: {id: id},
                dataType: 'json',
                success: function (data) {

                }
            });
        }
        url = url+bankSerialNo;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.select'), basePath + url);
            return;
        }
    }
    //业务类型选择后
    toChooseBillId = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var row = layui.jquery(el).data("row");
        var id = data.id;
        var url = "";
        if(selectType.get("busType"+row).getData()) {
            var sysbustype = selectType.get("busType"+row).getData().obj.sysBusType;
            if (sysbustype == 4) {
                url = basePath + "/chowmatistic/chobankfinance/mulSelectBankFinancePage?corpId=" + data.corpId + "&busType=11&row=" + row + "&id=" + id;
            }
            if (sysbustype == 5) {
                url = basePath + "/chowmatistic/chobankfinance/mulSelectBankFinancePage?corpId=" + data.corpId + "&busType=13&row=" + row + "&id=" + id;
            }
            if (sysbustype == 6) {
                url = basePath + "/chowmatistic/choproductredeem/select?corpId=" + data.corpId + "&row=" + row + "&id=" + id;
            }
            if (sysbustype == 7) {
                url = basePath + "/bill/accebill/abBillDiscountAccount/listDiscountAccount?corpId=" + data.corpId + "&row=" + row + "&id=" + id;
            }
            if (sysbustype == 8) {
                url = basePath + "/bill/accebill/abbillcollectionaccount/listCollectionAccount?corpId=" + data.corpId + "&row=" + row + "&id=" + id;
            }
            if (sysbustype == 9) {
                url = basePath + "/cms/provide/loansend/proLoanSendData?id=" + id + "&row=" + row;
            }
            if (sysbustype == 10) {
                url = basePath + "/international/credit/incomingletter/selectIncoming?row=" + row + "&id=" + id;
            }
            if (sysbustype == 11 || sysbustype == 12 || sysbustype == 13 || sysbustype == 14) {
                var tranType;
                if(sysbustype == 11){
                    tranType = 1;
                }else if(sysbustype == 12){
                    tranType = 2;
                }else if(sysbustype == 13){
                    tranType = 3;
                }else if(sysbustype == 14){
                    tranType = 4;
                }

                url = basePath + "/foreign/fexcashflow/choose?row=" + row + "&id=" + id+"&tranType="+tranType;
            }
            if (url != "") {
                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: url,
                    end: function () {

                    }
                });
            }
        }else{
            layer.alert(bt.lang('nis.pay.message.bustype.null'), {icon: 7});
        }
    }
    toViewBillId = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var row = layui.jquery(el).data("row");
        var id = $("#billId"+row).val();
        var url = "";
        if(id!="null" && id != "") {
            if (selectType.get("busType" + row).getData()) {
                var sysbustype = selectType.get("busType" + row).getData().obj.sysBusType;
                if (sysbustype == 4 || sysbustype == 5) {
                    url = basePath + "/chowmatistic/chobankfinance/view/" + id;
                }
                if (sysbustype == 6) {
                    url = basePath + "/chowmatistic/choproductredeem/view/" + id;
                }
                if (sysbustype == 7 || sysbustype == 8) {
                    url = basePath + "/bill/accebill/abreceivebill/view?accId=" + id;
                }
                if (sysbustype == 9) {
                    url = basePath + "/cms/provide/loansend/viewLoanInfo?id=" + id;
                }
                if (sysbustype == 10) {
                    url = basePath + "/international/credit/arrivalletter/viewArrovalInfo?flag=0&id=" + id;
                }
                if (sysbustype == 11 || sysbustype == 12 || sysbustype == 13 || sysbustype == 14) {
                    url = basePath + "/foreign/fexcashflow/view/"+id;
                }
                if (url != "") {
                    parent.layer.open({
                        type: 2,
                        area: ['850px', '530px'],
                        maxmin: true,
                        content: url,
                        end: function () {

                        }
                    });
                }
            } else {
                layer.alert(bt.lang('nis.pay.message.bustype.null'), {icon: 7});
            }
        }
    }
    var selectTree;
    var itemType;
    var otherItemType;
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    table = bt.datatable.initMultiTables({
        elem: "#appTable",
        select: false,
        selectCss: false,
        clickRowSelected: false,
        checkbox: true,
        resize: false,
        isInitData:false,
        searchForm: {
            formElem: "#searchForm1",
            formFilter: "search1"
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/bisaccdtlbudgetrule/datatable"
        },
        buttons: [
            {
                hide: $("#isClaimBusType").val() == 0,
                text: bt.lang('table.bisAccDtlClaim.button.autoClaim'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoRule(data);
                }
            },
            {
                hide: $("#isClaimBusType").val() == 0,
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoBatchSubmit(data);
                }
            },
            {
                hide: $("#isEnableMerge").val() == 0,
                text: bt.lang('common.button.merge'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    merge(data);
                }
            },
            {
                hide: $("#isEnableMerge").val() == 0,
                text: bt.lang('common.button.mergecancel'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    mergeCancel(data);
                }
            },
            {
                /*hide: $("#isClaimBusType").val() == 0,*/
                text: bt.lang('common.button.batchRecover'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchRecover(data);
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        buttonsLayout: true,
        columns: [
            {
                data: "transTime", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.id)) {
                        html = '<input type="hidden" id="id" name="id" value=""><span class="" name="transTime" data-defaultvalue="' + full.transTime + '">' + full.transTime + '</span>';
                    } else {
                        html = '<input type="hidden" id="id" name="id" value="' + full.id + '"><span class="" name="transTime" data-defaultvalue="' + full.transTime + '">' + full.transTime + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="corpId" name="corpId" value="' + full.corpId + '"><span class="" name="transTime" data-defaultvalue="' + full.corpName + '">' + full.corpName + '</span>';
                }
            },
            {
                data: "bankAcc", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.bankAcc)) {
                        html = '<input type="hidden" id="accNo" name="accNo" value="' + full.accNo + '"><span class="" name="bankAcc" data-defaultvalue=""></span>';
                    } else {
                        html = '<input type="hidden" id="accNo" name="accNo" value="' + full.accNo + '"><span class="" name="bankAcc" data-defaultvalue="' + full.bankAcc + '">' + full.bankAcc + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "accName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.accName)) {
                        html = '<span class="" name="accName" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="accName" data-defaultvalue="' + full.accName + '">' + full.accName + '</span>';
                    }
                    return html;
                }
            },
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
            {
                data: "oppAccName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.oppAccName)) {
                        html = '<span class="" name="oppAccName" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="oppAccName" data-defaultvalue="">' + full.oppAccName + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "curName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<input type="hidden" id="curExrate" name="curExrate" value="' + full.curExrate + '">';
                    if (isNull(full.curName)) {
                        html += '<span class="" name="curName" data-defaultvalue=""></span>';
                    } else {
                        html += '<span class="" name="curName" data-defaultvalue="' + full.curName + '">' + full.curName + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "cdSignName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="cdSignTemp" name="cdSignTemp" value="' + full.cdSign + '"><span class="" name="cdSign" data-defaultvalue="' + full.cdSign + '">' + bt.dict("btBankAccDtl.cdSign", full.cdSign) + '</span>';
                }
            },
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="money" name="money" value="' + full.amt + '"/><span class="" name="amt" data-defaultvalue="' + full.amt + '">' + moneyFun.format(full.amt) + '</span>';
                }
            },
            {
                data: "remark", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.remark)) {
                        html = '<span class="" name="remark" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="remark" data-defaultvalue="' + full.remark + '">' + full.remark + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "fbsStatusName", bSortable: false,
                render: function (data, type, full, meta) {
                    return '<span class="" name="fbsStatus" data-defaultvalue="' + full.fbsStatus + '">' + bt.dict("bisAccDtlBudgetRule.fbsStatus", full.fbsStatus) + '</span>';
                }
            },
            {data: "busTypeName", bSortable: true, visible:$("#isClaimBusType").val() == 1},
            {data: "billNo", bSortable: true},
            {data: "settlType", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("settlType", full.settlType);
                }
            },
            {data: "processDate", bSortable: true,visible:$("#budgetSel").val() == 1},
            // {
            //     data: "busType", bSortable: true,className:'select-column', visible:$("#isClaimBusType").val() == 1,
            //     render: function (data, type, full, meta) {
            //         var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + full.corpId;
            //         var html = '';
            //         html += '<div class="bt-biz-selectType" data-name="busType" data-biz-name="busType' + meta.row + '" data-defaultvalue="' + full.busType + '" ';
            //         html += 'data-url="' + busTypeUrl + '" data-valuefield="busId" data-textfield="busTypeName" data-search="true" data-beforeinit="busBefore"  data-full="'+full.id+'" data-row="'+meta.row+'"></div>';
            //         html += '<input type="hidden" name="dtlId" id="dtlId'+meta.row+'" value="'+full.id+'"/>';
            //         html += '<input type="hidden" name="billId" id="billId'+meta.row+'"  value="'+full.billId+'"/> ';
            //         if (full.fbsStatus == 1) {
            //             if(full.billId == "null" || full.busType == '' || full.billId == null || full.busType == null){
            //                 html += '<button class="layui-btn layui-btn-disabled" data-row="' + meta.row + '" onclick="toViewBillId(this)" type="button">' + bt.lang('common.button.view') + '</button>';
            //             }else {
            //                 html += '<button class="layui-btn" data-row="' + meta.row + '" onclick="toViewBillId(this)" type="button">' + bt.lang('common.button.view') + '</button>';
            //             }
            //         }else if(full.fbsStatus == 2){
            //
            //         }else {
            //             html += '<button class="layui-btn" data-row="' + meta.row + '" onclick="toChooseBillId(this)" type="button">' + bt.lang('common.button.select') + '</button>';
            //             html += '<button class="layui-btn" data-row="' + meta.row + '" onclick="toViewBillId(this)" type="button">' + bt.lang('common.button.view') + '</button>';
            //         }
            //         return html;
            //     }
            // },
            // {
            //     data: "billNo", bSortable: false, width: 100, className: 'center',
            //     render: function (data, type, full, meta) {
            //         if(isNull(full.fbsStatus) || full.fbsStatus == 0) {
            //             if(isNull(full.billNo)){
            //                 full.billNo = "";
            //             }
            //             var html = '';
            //             html += '<input type="text" name="billNo" data-biz-name="billNo' + meta.row + '" readonly="readonly"  class="layui-input bt-input-inline " value="'+full.billNo+'" placeholder="'+bt.lang('table.choBankFinance.bankBillNo')+'">';
            //             html += '&nbsp;<button class="layui-btn" data-row="' + meta.row + '" onclick="$genNisReceivable(this)" type="button">' + bt.lang('common.button.select') + '</button>';
            //             return html;
            //         }else{
            //             return full.billNo;
            //         }
            //     }
            // },
            // {
            //     data: "settlType", bSortable: false, width: 100, className: 'center',
            //     render: function (data, type, full, meta) {
            //         if (full.fbsStatus == 1 || full.fbsStatus == 2) {
            //             return bt.dict("settlType", full.settlType);
            //         } else {
            //             return '<div class="bt-biz-dict" data-type="select" data-group="settlType" data-name="settlType" data-biz-name="settlType' + meta.row + '" data-defaultvalue="' + full.settlType + '"></div>';
            //         }
            //     }
            // },
            // {
            //     data: "processDate", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            //     render: function (data, type, full, meta) {
            //         if (isNull(full.fbsStatus) || full.fbsStatus == 0) {
            //             var html = '<div class="layui-inline">';
            //             if (isNull(full.processDate)) {
            //                 html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" data-today="true" class="layui-input bt-input-inline bt-biz-dateType" data-type="date">';
            //             } else {
            //                 html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" value="' + full.processDate + '">';
            //             }
            //             html += '</div>'
            //             return html;
            //         } else {
            //             return full.processDate;
            //         }
            //     }
            // },
            // {
            //     data: "itemInfo", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            //     render: function (data, type, full, meta) {
            //         if (isNull(full.fbsStatus) || full.fbsStatus == 0) {
            //             var html = '<div class="layui-input-inline">';
            //             itemFormat(full.itemInfo, meta.row);
            //             if (isNull(full.item)) {
            //                 html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '">'
            //             } else {
            //                 html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '" value="' + full.itemInfo + '">'
            //             }
            //             html += '</div>';
            //             html += '<div class="layui-input-inline">'
            //             html += '<input type="hidden" id="budget' + meta.row + '"  name="budget" ><button class="layui-btn" data-row="' + meta.row + '" onclick="$genOtherHtml(this,\'appTable\')" type="button">' + bt.lang('common.button.browse') + '</button>';
            //             html += '</div>'
            //             return html;
            //         } else {
            //             return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this,\'appTable\')">' + bt.lang("common.button.view") + '</a>';
            //         }
            //     }
            // },
            {
                data: "rmk", bSortable: false, width: 100, className: 'center',
                render: function (data, type, full, meta) {
                    /*if(isNull(full.fbsStatus) || full.fbsStatus == 0) {
                        var html = '';
                        if (isNull(full.rmk)) {
                            full.rmk = "";
                        }
                        html += '<input type="text" name="rmk" id="rmk' + meta.row + '" style="width: 110px;"  class="layui-input" placeholder="' + bt.lang("table.fbsBudgetPeriod.rmk") + '" value="'+full.rmk+'">';
                        return html;
                    }else{*/
                        return full.rmk;
                    /*}*/
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bankAcc:bisAccDtlBudgetRule:apply',full.corpId)) {
                        if (full.fbsStatus == 1) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="recover(this,\'appTable\')">' + bt.lang("common.button.recover") + '</a>';//恢复
                        } else if (full.fbsStatus == 2) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="revivification(this,\'appTable\')">' + bt.lang("common.button.revivification") + '</a>';//还原
                        } else {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sureView(this,\'appTable\')">' + bt.lang("common.button.sure") + '</a>';//确认
                            if ($("#isEnableSplit").val() == 1) {
                                if (full.splitId && full.splitId != "") {
                                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="splitCancel(this,\'appTable\')">' + bt.lang("common.button.splitcancel") + '</a>';
                                }else {
                                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="split(this,\'appTable\')">' + bt.lang("common.button.split") + '</a>';
                                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this,\'appTable\')">' + bt.lang("common.button.delete") + '</a>';//删除
                                }
                            }
                        }
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this,\'appTable\')">' + bt.lang("common.button.view") + '</a>';//查看
                    }
                    return html;
                }
            }
        ],
        summary: {
            selectSum: true,
            sumList: [
                {
                    colNum: '12',
                    dataName: 'amt'
                }
            ]
        },
        // btInitComplete: function (settings, json) {
        //     dateType.render();
        // },
        // btDrawCallback: function () {
        //     dateType.render();
        //     dicts.render();
        //     selectType = bt.selectType.init();
        // },
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
                    if (allSelectEdData[0].bankAcc != allSelectEdData[i].bankAcc) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeracc.accordance'), {icon: 2});//本方账户必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeAccName"]) {
                    if (allSelectEdData[0].oppAccName != allSelectEdData[i].oppAccName) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeeaccname.accordance'), {icon: 2});//对方账户名必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeAcc"]) {
                    if (allSelectEdData[0].oppAccNo != allSelectEdData[i].oppAccNo) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeeacc.accordance'), {icon: 2});//对方账户必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeBank"]) {
                    if (allSelectEdData[0].oppAccBank != allSelectEdData[i].oppAccBank) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeebank.accordance'), {icon: 2});//对方开户行必须一致
                        break;
                    }
                }

                if (allSelectEdData[i].fbsBudget != 0) {
                    $("#box").hide();
                    hideItemDetail();
                }
                if (selectCurId.indexOf(allSelectEdData[i].curId) == -1) {
                    selectCurId += "," + allSelectEdData[i].curId;
                }
            }
            if (isItem) { //单位与收支标志一致 才执行
                if (data[0].fbsStatus != 0 && data[0].fbsStatus != null) {
                    $("#box").hide();
                    hideItemDetail();
                } else {
                    $("#box").show();
                }
                $("#itemDiv").show();
                var itemCode = "";
                if (!selectTree) {
                    $("#corpId").val(allSelectEdData[0].corpId);
                    $("#itemInfo").data("corpid", allSelectEdData[0].corpId).data("sign", "1").data("defaultvalue", itemCode);
                    if (selectCurId.indexOf($("#natualCurId").val()) == -1) {
                        natualCurrency = false;
                    }
                    $("#curId").val(allSelectEdData[0].curId);
                    $("#selectCurId").val($("#selectCurId").val() + "," + allSelectEdData[0].curId);
                    $("#payerAcc").val(allSelectEdData[0].bankAcc);
                    $("#payeeAccName").val(allSelectEdData[0].oppAccName);
                    $("#payeeAcc").val(allSelectEdData[0].oppAccNo);
                    $("#payeeBank").val(allSelectEdData[0].oppAccBank);
                    if($("#budgetSel").val() == 1) {
                        if (!itemType) {
                            itemType = bt.itemType.render($("#itemDiv"));
                        } else {
                            itemType.get("itemInfo").setUrl({
                                corpId: allSelectEdData[0].corpId,
                                itemCode: itemCode,
                                sign:"1",
                            });
                        }
                    }
                    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" +allSelectEdData[0].corpId;
                    var html = "<div class=\"bt-biz-selectType\" data-name=\"batchBusType\" data-biz-name=\"batchBusType\" data-defaultvalue=\"null\" data-url=\""+busTypeUrl+"\"";
                    html += " data-valuefield=\"busId\" data-textfield=\"busTypeName\" data-search=\"true\" data-beforeinit=\"busBefore\" data-callback=\"busChoose\"></div>";
                    $("#batchBusTypeDiv").html(html);
                    selectType = bt.selectType.init();
                }
            } else {
                $("#itemDiv").hide();
                $("#budgetProcessDataTable").hide();
                $("#batchBusTypeDiv").html("");
            }
        },
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bankacc/bisaccdtlbudgetrule/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
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
            if (columnId == "settlType") {
                columnId = "settlTypeName";
            }
            columns.push({
                data: columnId, bSortable: false, className: 'center', title: name
            });
        });
        $("#projectDetail" + row).html(html);
        var settlType = dicts.get("settlTypeBatch").val();
        var data = {
            corpId: $("#corpId").val(),
            itemCode: itemCode,
            "processDate": dateType.get("processDateBatch").val(),
            "curId": $("#curId").val(),
            "settlType": settlType ? settlType : 0,
            "wishPayday": dateType.get("processDateBatch").val(),
            "payerAcc": $("#payerAcc").val(),
            "payeeAccName": $("#payeeAccName").val(),
            "payeeAcc": $("#payeeAcc").val(),
            "payeeBank": $("#payeeBank").val(),
            "dtlColIds": dtlColIds,
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
            if (columnId == "settlType") {
                columnId = "settlTypeName";
            }
            columns.push({
                data: columnId, bSortable: false, className: 'center', title: name
            });
        });
        $("#otherprojectDetail" + row).html(html);
        var settlType = dicts.get("othersettlTypeBatch").val();
        var data = {
            corpId: $("#corpId").val(),
            itemCode: itemCode,
            "processDate": dateType.get("otherprocessDateBatch").val(),
            "curId": $("#curId").val(),
            "settlType": settlType ? settlType : 0,
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
            "settlType": dicts.get("settlTypeBatch").val(),
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
            'url': basePath + "/bankacc/bisaccdtlbudgetrule/datatable"
        },
        buttons:[
            {
                hide: $("#isClaimBusType").val() == 0,
                text: bt.lang('table.bisAccDtlClaim.button.autoClaim'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoRuleOther(data);
                }
            },
            {
                hide: $("#isClaimBusType").val() == 0,
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoBatchSubmitOther(data);
                }
            },
            {
                /*hide: $("#isClaimBusType").val() == 0,*/
                text: bt.lang('common.button.batchRecover'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchRecover(data);
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        buttonsLayout: true,
        columns: [
            {
                data: "transTime", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.id)) {
                        html = '<input type="hidden" id="id" name="id" value=""><span class="" name="transTime" data-defaultvalue="' + full.transTime + '">' + full.transTime + '</span>';
                    } else {
                        html = '<input type="hidden" id="id" name="id" value="' + full.id + '"><span class="" name="transTime" data-defaultvalue="' + full.transTime + '">' + full.transTime + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="corpId" name="corpId" value="' + full.corpId + '"><span class="" name="transTime" data-defaultvalue="' + full.corpName + '">' + full.corpName + '</span>';
                }
            },
            {
                data: "bankAcc", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.bankAcc)) {
                        html = '<input type="hidden" id="accNo" name="accNo" value="' + full.accNo + '"><span class="" name="bankAcc" data-defaultvalue=""></span>';
                    } else {
                        html = '<input type="hidden" id="accNo" name="accNo" value="' + full.accNo + '"><span class="" name="bankAcc" data-defaultvalue="' + full.bankAcc + '">' + full.bankAcc + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "accName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.accName)) {
                        html = '<span class="" name="accName" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="accName" data-defaultvalue="' + full.accName + '">' + full.accName + '</span>';
                    }
                    return html;
                }
            },
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
            {
                data: "oppAccName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.oppAccName)) {
                        html = '<span class="" name="oppAccName" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="oppAccName" data-defaultvalue="">' + full.oppAccName + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "curName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<input type="hidden" id="curExrate" name="curExrate" value="' + full.curExrate + '">';
                    if (isNull(full.curName)) {
                        html += '<span class="" name="curName" data-defaultvalue=""></span>';
                    } else {
                        html += '<span class="" name="curName" data-defaultvalue="' + full.curName + '">' + full.curName + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "cdSignName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="cdSignTemp" name="cdSignTemp" value="' + full.cdSign + '"><span class="" name="cdSign" data-defaultvalue="' + full.cdSign + '">' + bt.dict("btBankAccDtl.cdSign", full.cdSign) + '</span>';
                }
            },
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="money" name="money" value="' + full.amt + '"/><span class="" name="amt" data-defaultvalue="' + full.amt + '">' + moneyFun.format(full.amt) + '</span>';
                }
            },
            {
                data: "remark", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if (isNull(full.remark)) {
                        html = '<span class="" name="remark" data-defaultvalue=""></span>';
                    } else {
                        html = '<span class="" name="remark" data-defaultvalue="' + full.remark + '">' + full.remark + '</span>';
                    }
                    return html;
                }
            },
            {
                data: "fbsStatusName", bSortable: false,
                render: function (data, type, full, meta) {
                    return '<span class="" name="fbsStatus" data-defaultvalue="' + full.fbsStatus + '">' + bt.dict("bisAccDtlBudgetRule.fbsStatus", full.fbsStatus) + '</span>';
                }
            },
            {data: "busTypeName", bSortable: true, visible:$("#isClaimBusType").val() == 1},
            {data: "settlTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("settlType", full.settlType);
                }
            },
            {data: "processDate", bSortable: true,visible:$("#budgetSel").val() == 1},
            // {
            //     data: "busTypeName", bSortable: true,className:'select-column', visible:$("#isClaimBusType").val() == 1,
            //     render: function (data, type, full, meta) {
            //         if (full.fbsStatus == 1 || full.fbsStatus == 2) {
            //             return full.busTypeName;
            //         } else {
            //             var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + full.corpId;
            //             var html = '';
            //             html += '<div class="bt-biz-selectType" data-name="busType" data-biz-name="busType' + meta.row + '" data-defaultvalue="' + full.busType + '" ';
            //             html += 'data-url="' + busTypeUrl + '" data-valuefield="busId" data-textfield="busTypeName" data-search="true" data-beforeinit="busBefore"  data-full="' + full.id + '" data-row="' + meta.row + '"></div>';
            //             return html;
            //         }
            //     }
            // },
            // {
            //     data: "settlTypeName", bSortable: false, width: 100, className: 'center',
            //     render: function (data, type, full, meta) {
            //         if (full.fbsStatus == 1 || full.fbsStatus == 2) {
            //             return full.settlTypeName;
            //         } else {
            //             return '<div class="bt-biz-dict" data-type="select" data-group="settlType" data-name="settlType" data-biz-name="settlType' + meta.row + '" data-defaultvalue="' + full.settlType + '"></div>';
            //         }
            //     }
            // },
            // {
            //     data: "processDate", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            //     render: function (data, type, full, meta) {
            //         if (full.fbsStatus == 1 || full.fbsStatus == 2) {
            //             return full.processDate;
            //         } else {
            //             var html = '<div class="layui-inline">';
            //             if (isNull(full.processDate)) {
            //                 html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-btns= "clear,now,confirm" value="' + full.transTime + '">';
            //             } else {
            //                 html += '<input type="text" name="processDate" data-biz-name="processDate' + meta.row + '" style="width: 110px;" class="layui-input bt-input-inline bt-biz-dateType" data-type="date" data-btns= "clear,now,confirm" value="' + full.processDate + '">';
            //             }
            //             html += '</div>'
            //             return html;
            //         }
            //     }
            // },
            // {
            //     data: "itemInfo", bSortable: false, className: 'center',visible:$("#budgetSel").val() == 1,
            //     render: function (data, type, full, meta) {
            //         if (full.fbsStatus == 1 || full.fbsStatus == 2) {
            //             if (!full.itemInfo) {
            //                 return '';
            //             }
            //             return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this,\'otherTable\')">' + bt.lang("common.button.view") + '</a>';
            //         } else {
            //             var html = '<div class="layui-input-inline">';
            //             itemFormat(full.itemInfo, meta.row);
            //             if (isNull(full.item)) {
            //                 html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '">'
            //             } else {
            //                 html += '<input type="text" id="itemCode' + meta.row + '" name="itemCode" class="layui-input" readonly="readonly" placeholder="' + bt.lang("table.bisAccDtlBudgetRule.selectBudget") + '" value="' + full.itemInfo + '">'
            //             }
            //             html += '</div>';
            //             html += '<div class="layui-input-inline">'
            //             html += '<input type="hidden" id="budget' + meta.row + '"  name="budget" ><button class="layui-btn" data-row="' + meta.row + '" onclick="$genOtherHtml(this,\'otherTable\')" type="button">' + bt.lang('common.button.browse') + '</button>';
            //             html += '</div>'
            //             return html;
            //         }
            //     }
            // },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bankAcc:bisAccDtlBudgetRule:apply',full.corpId)) {
                        if (full.fbsStatus == 1) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="recover(this,\'otherTable\')">' + bt.lang("common.button.recover") + '</a>';//恢复
                        } else if (full.fbsStatus == 2) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="revivification(this,\'otherTable\')">' + bt.lang("common.button.revivification") + '</a>';//还原
                        } else {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sureView(this,\'otherTable\')">' + bt.lang("common.button.sure") + '</a>';//确认
                            if ($("#isEnableSplit").val() == 1) {
                                if (full.splitId && full.splitId != "") {
                                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="splitCancel(this,\'otherTable\')">' + bt.lang("common.button.splitcancel") + '</a>';
                                }else {
                                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="split(this,\'otherTable\')">' + bt.lang("common.button.split") + '</a>';
                                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this,\'otherTable\')">' + bt.lang("common.button.delete") + '</a>';//删除
                                }
                            }
                        }
                    }
                    if (html != '') {
                        html += '<span class="text-explode">|</span>'
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this,\'otherTable\')">' + bt.lang("common.button.view") + '</a>';//查看
                    return html;
                }
            }
        ],
        summary: {
            selectSum: true,
            sumList: [
                {
                    colNum: '9',
                    dataName: 'amt'
                }
            ]
        },
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bankacc/bisaccdtlbudgetrule/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        },
        btInitComplete: function (settings, json) {
            dateType = bt.dateType.init();
            dateType.render();
        },
        btDrawCallback: function () {
            dateType = bt.dateType.init();
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
            var selectCurId = allSelectEdData[0].curId;
            $("#otherControl").val(allSelectEdData[0].control);
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
                    if (allSelectEdData[0].bankAcc != allSelectEdData[i].bankAcc) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeracc.accordance'), {icon: 2});//本方账户必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeAccName"]) {
                    if (allSelectEdData[0].oppAccName != allSelectEdData[i].oppAccName) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeeaccname.accordance'), {icon: 2});//对方账户名必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeAcc"]) {
                    if (allSelectEdData[0].oppAccNo != allSelectEdData[i].oppAccNo) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeeacc.accordance'), {icon: 2});//对方账户必须一致
                        break;
                    }
                }
                if (dtlColSels["payeeBank"]) {
                    if (allSelectEdData[0].oppAccBank != allSelectEdData[i].oppAccBank) {
                        isItem = false;
                        layer.alert(bt.lang('bisaccdtl.message.payeebank.accordance'), {icon: 2});//对方开户行必须一致
                        break;
                    }
                }

                if (allSelectEdData[i].fbsBudget != 0) {
                    $("#otherbox").hide();
                    hideItemDetail();
                }
                if (selectCurId.indexOf(allSelectEdData[i].curId) == -1) {
                    selectCurId += "," + allSelectEdData[i].curId;
                }
            }
            if (isItem) { //单位与收支标志一致 才执行
                if (data[0].fbsStatus != 0 && data[0].fbsStatus != null) {
                    $("#otherbox").hide();
                    hideItemDetail();
                } else {
                    $("#otherbox").show();
                }
                $("#otheritemDiv").show();
                var itemCode = "";
                if (!selectTree) {
                    $("#corpId").val(allSelectEdData[0].corpId);
                    $("#otheritemInfo").data("corpid", allSelectEdData[0].corpId).data("sign", "-1").data("defaultvalue", itemCode);
                    if (selectCurId.indexOf($("#natualCurId").val()) == -1) {
                        natualCurrency = false;
                    }
                    $("#curId").val(allSelectEdData[0].curId);
                    $("#selectCurId").val($("#selectCurId").val() + "," + allSelectEdData[0].curId);
                    $("#payerAcc").val(allSelectEdData[0].bankAcc);
                    $("#payeeAccName").val(allSelectEdData[0].oppAccName);
                    $("#payeeAcc").val(allSelectEdData[0].oppAccNo);
                    $("#payeeBank").val(allSelectEdData[0].oppAccBank);
                    if($("#budgetSel").val() == 1) {
                        if (!otherItemType) {
                            otherItemType = bt.itemType.render($('#otheritemDiv'));
                        } else {
                            otherItemType.get("otheritemInfo").setUrl({
                                corpId: allSelectEdData[0].corpId,
                                itemCode: itemCode,
                                sign:"-1",
                            });
                        }
                    }
                    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" +allSelectEdData[0].corpId;
                    var html = "<div class=\"bt-biz-selectType\" data-name=\"batchBusType\" data-biz-name=\"otherBatchBusType\" data-defaultvalue=\"null\" data-url=\""+busTypeUrl+"\"";
                    html += " data-valuefield=\"busId\" data-textfield=\"busTypeName\" data-search=\"true\" data-beforeinit=\"busBefore\" data-callback=\"otherbusChoose\"></div>";
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
    var data = layui.jquery("#otherTable").DataTable().row(rowNum).data();
    if (bt.money.toNumber(data.amt) !== bt.money.toNumber(sumMoney)) {
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
    if(tableName != 'otherTable') {
        data.rmk = $("#rmk" + row).val();
        data.billId = $("#billId"+row).val();
    }
    data.fbsStatus = (data.fbsStatus).toString();
    if($("#budgetSel").val() == 1) {
        data.budget = $("#budget" + row).val();
        data.processDate = dateType.get("processDate" + row).val();
        if (data.budget.length < 1) {
            layer.alert(bt.lang('table.bisAccDtlBudgetRule.selectBudget'), {icon: 7});
            return;
        }
    }
    data.settlType = bt.dicts.get("settlType" + row).val();
    var action = "/bankacc/bisaccdtlbudgetrule/sure"
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
function sureView(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    var id = data.id;
    var url = basePath+"/bankacc/bisaccdtlbudgetrule/sureView?id="+id+"&tableName="+tableName;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.sure'), url);
        return;
    }
}

/**
 * 恢复
 */
function recover(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('table.message.confirm.recover'), function (result) {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/business/" + data.id + "/H", function (result) {
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
    parent.layer.confirm(bt.lang('table.dtl.message.confirm.revivification'), function (result) {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/business/" + data.id + "/R", function (result) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/business/" + data.id + "/D", function (result) {
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
    /*if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bankacc/bisaccdtlbudgetrule/view/" + data.id);
        return;
    }*/
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccdtlbudgetrule/view/" + data.id
    });
}

/**
 * 查看
 * @param el
 */
function viewDtl(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bankacc/bisaccdtlbudgetrule/viewDtl/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccdtlbudgetrule/viewDtl/" + data.id
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
    var billNos = "";
    var rmks = "";
    $(selectDataTemp).each(function (i, item) {
        if (i == 0) {
            ids += item.id;
            if(tableName != 'otherTable') {
                billNos+=item.billNo;
                if(item.rmk) {
                    rmks += item.rmk.trim();
                }else{
                    rmks += "";
                }
            }
        } else {
            ids += "," + item.id;
            if(tableName != 'otherTable') {
                billNos += ";" + item.billNo;
                if(item.rmk) {
                    rmks += "," + item.rmk.trim();
                }else{
                    rmks += "";
                }
            }
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
    var settlTypeBatch = "";
    if(tableName == 'otherTable') {
        settlTypeBatch = bt.dicts.get("othersettlTypeBatch").val();
    }else{
        settlTypeBatch = bt.dicts.get("settlTypeBatch").val();
    }
    var itemCodeTemp = "";
    var itemNameTemp = "";
    var processDateBatch = "";
    if($("#budgetSel").val() == 1) {
        if (tableName == 'otherTable') {
            itemCodeTemp = bt.itemType.get("otheritemInfo").val();
            itemNameTemp = $("#otheritemName").val();
            processDateBatch = bt.dateType.get("otherprocessDateBatch").val();
            settlTypeBatch = bt.dicts.get("othersettlTypeBatch").val();
        }else{
            itemCodeTemp = bt.itemType.get("itemInfo").val();
            itemNameTemp = $("#itemName").val();
            processDateBatch = bt.dateType.get("processDateBatch").val();
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
        var otherControl = $("#otherControl").val();
        // 校验执行金额填写
        if (item.sign == -1 && item.isControl == 1 && otherControl=='true') {
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
    var action = "/bankacc/bisaccdtlbudgetrule/batch"
    var data = {
        "ids": ids,
        "type": "Y",
        "itemCode": itemCodeTemp,
        "itemName": itemNameTemp,
        "processDate": processDateBatch,
        "settlType": settlTypeBatch,
        "billNos":billNos,
        "batchBusType":batchBusType,
        "rmks":rmks
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
    var action = "/bankacc/bisaccdtlbudgetrule/batch"
    var data = {"ids": ids, "type": "D", "itemCode": null, "itemName": null, "processDate": null,"settlType":null,"billNos":null,"batchBusType":null,"rmks":null};
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
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
//业务种类选择后回调
busChoose = function (data) {
    if($("#budgetSel").val() == 1){
        bt.itemType.get("itemInfo").val(data.itemcode);
        if(bt.itemType.get("itemInfo").data().length > 0) {
            itemChange(bt.itemType.get("itemInfo").data()[0]);
        }
    }

};
//业务种类选择后回调
otherbusChoose = function (data) {
    if($("#budgetSel").val() == 1){
        bt.itemType.get("otheritemInfo").val(data.itemcode);
        if(bt.itemType.get("otheritemInfo").data().length > 0) {
            otheritemChange(bt.itemType.get("otheritemInfo").data()[0]);
        }
    }

};
//自动匹配
function autoRule(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var corpIds="";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if(rowData[i].fbsStatus != 1 && rowData[i].fbsStatus != 2) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }else{
            layer.alert("已确认的数据不能再匹配!", {icon: 7});
            return;
        }
    }
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            corpIds += rowData[i].corpId;
        } else {
            corpIds += "," + rowData[i].corpId;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/autoRule", {
            ids: ids,corpIds: corpIds
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//自动匹配后批量提交
function autoBatchSubmit(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        if ($("#isClaimBusType").val() == 1) {
            if (rowData[i].busType == "" || rowData[i].busType == null) {
                layer.alert(bt.lang('nis.netbank.message.bustypename.null'), {icon: 7});
                return;
            }
        }
        if($("#budgetSel").val() == 1 ){
            if(rowData[i].itemInfo == "" || rowData[i].itemInfo == null){
                layer.alert(bt.lang('nis.assign.message.itemcode.null'), {icon: 7});
                return;
            }
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/autoBatchSubmit", {
            ids: ids
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//批量恢复
function batchRecover(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if(rowData[i].fbsStatus == 1 || rowData[i].fbsStatus == 3) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }else{
            layer.alert(bt.lang('nis.netbank.message.issure'), {icon: 7});
            return;
        }
    }
    var corpIds = "";
    for (var i = 0; i < rowData.length; i++) {
        if(rowData[i].fbsStatus == 1 || rowData[i].fbsStatus == 3) {
            if (corpIds == "") {
                corpIds += rowData[i].corpId;
            } else {
                corpIds += "," + rowData[i].corpId;
            }
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/batchRecover", {
            ids: ids,corpIds:corpIds
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//自动匹配后批量提交
function autoBatchSubmitOther(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        if ($("#isClaimBusType").val() == 1) {
            if (rowData[i].busType == "" || rowData[i].busType == null) {
                layer.alert(bt.lang('nis.netbank.message.bustypename.null'), {icon: 7});
                return;
            }
        }
        if($("#budgetSel").val() == 1 ){
            if(rowData[i].itemInfo == "" || rowData[i].itemInfo == null){
                layer.alert(bt.lang('nis.assign.message.itemcode.null'), {icon: 7});
                return;
            }
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/autoBatchSubmit", {
            ids: ids
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//拆分
function split(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        $.ajax({
            url: basePath + "/bankacc/bisaccdtlbudgetrule?isMergeBill",
            method: 'POST',
            async: false,
            data: {
                id: data.id
            },
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    layer.alert(result.msg, {icon: 2});
                    return;
                }
                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: basePath + "/bankacc/bisaccdtlbudgetrule/split?id=" + data.id,
                    end: function () {
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }
        });
    }
}
//撤销拆分
function splitCancel(el,tableName) {
    var data = layui.jquery("#"+tableName).DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.splitcancel'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/splitCancel/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//合并
function merge(data) {
    var len = data.length;
    if (len < 2) {
        layer.alert(bt.lang('netbank.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.merge'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/merge", {ids: ids}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//撤销合并
function mergeCancel(data) {
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.mergecancel'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/mergeCancel", {ids: ids}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}



 function busTypeCallback(data,row) {
        if(data){
            $("#billId"+row).val(data.id);
        }
     parent.layer.closeAll();
 }

//自动匹配
function autoRuleOther(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/bankacc/bisaccdtlbudgetrule/autoRuleOther", {
            ids: ids
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
