var corp, dicts, moneyType, dateType, form, dateFmt, rateType;
var idsValArray = [];
bt.use(['form', 'datatable', 'moneyType', 'dateType', 'corp', 'mulselect', 'rateType', 'select', 'dicts', 'bankInfo', 'externalCorpBankAcc', 'accountTable', 'jquery', 'element', 'bank', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    rateType = bt.rateType.init();
    bt.externalCorpBankAcc.init();
    dateFmt = bt.date;
    bt.bankInfo.init();
    bt.mulselect.init();
    bt.accountTable.init({urldata: {accountType: '2,3'}});
    bt.bank.init({
        elem: '.bank'
    })

    importExcel = function () {
        //证书签名
        var signDataMsg = signData("贴现申请导入", 9);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        up = bt.btImport.init({
            code: "20000002",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head, data, select) {
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abBillDiscountApply/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abBillDiscountApply/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.submit'),
                hide: bt.isnotResource('bill:accebill:abBillDiscountApply:apply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                hide: $("#isNotDiscountApply").val() == 'true',
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true,
                className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "sendSignName", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "briefIdName", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},
            {data: "blStdAmt", bSortable: true,render: function (data, type, full, meta) {
                    if(full.newStmIssuofpprInd == 1){
                        return full.blStdAmt;
                    }else {
                        return "";
                    }
                }},
            {data: "blRmtrUnnIdNo", bSortable: true},
            {data: "rmk", bSortable: true},
            {
                data: "transMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    full.transMoney=full.billMoney;
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1') {
                        return '<input  style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.accId+'" data-key="'+full.accId+'" data-name="transMoney_'+full.accId+'" data-biz-name="transMoney_'+full.accId+'" />';
                    }
                    return bt.money.format(full.transMoney)+'<input  style="display: none" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                        'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                        'data-id="transMoney_'+full.accId+'" data-key="'+full.accId+'" data-name="transMoney_'+full.accId+'" data-biz-name="transMoney_'+full.accId+'" />';
                }
            },
            {
                data: "attachmentNum",
                bSortable: false,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="attachmentView(this)">' + bt.lang("bill.accebill.attachment.num", [(full.attachmentNum || 0)]) + '</a>';
                    return html;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    //html += '<span class="text-explode">|</span>';
                    // html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.accId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    html += '<div id="deferdate' + full.accId + '" name="deferdate" style="display: none">0</div>';
                    return html;
                }
            }
        ],
        btClickRowCallback: function (data) {
            isSameNet();
            setVerify();
            opIdsValArray(data);
            if (data[0].isSelect) {
                changeMoney();
            }
        },
        btDrawCallback: function (setting) {
            bt.moneyType.init();
            dicts.render();
            moneyType.render();
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 3
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '11',
                    dataName: 'billMoney'
                },
                {
                    colNum: '23',
                    dataName: 'blStdAmt'
                },
                {
                    colNum: '27',
                    dataName: 'transMoney'
                },
            ]
        }
    });

    //银行
    bt.bailBankInfoCallback = function (data, dom) {
        $("#recNm2").val(data.bankName);
        $("#recAcctsvcr2").val(data.bankCode);
        $("#recAcctId2").val(0);
    }

    });


//查询时设置属性
function enterStockCall() {
    var $ = bt.jquery, form = bt.form();
    var enterStock = dicts.get("enterStock").val();
    //触发查询功能
    $("#searchBtn").click();
    //单位库存
    if (enterStock == '10') {
        //默认为银行
        $("#netTr1").hide();
        $("#netTr2").hide();
        $("#netTr3").hide();

        $("#bankTr1").show();
        $("#bankTr2").show();
        dicts.get("recType").attr({"data-disabledValue": ""});
    }
    //中心代管
    if (enterStock == '11') {

        $("#netTr1").show();
        $("#netTr2").show();
        $("#netTr3").show();

        $("#bankTr1").hide();
        $("#bankTr2").hide();

        //隐藏银行radio
        dicts.get("recType").attr({"data-disabledValue": "0"});
        dicts.get("recType").val(1);
    }
    //银行托管
    if (enterStock == '12') {
        $("#netTr1").hide();
        $("#netTr2").hide();
        $("#netTr3").hide();

        $("#bankTr1").show();
        $("#bankTr2").show();

        dicts.get("recType").attr({"data-disabledValue": "1"});
        dicts.get("recType").val(0);
    }

    $('#recAcctId1').val('');
    $('#recNm1').val('');
    $('#recAcctsvcr1').val('');
    $('#recAcctId2').val('');
    $('#recNm2').val('');
    $('#recAcctsvcr2').val('');
    $('#center').val('');
    $("#recCorpId").val('');
    netCallback('0');
}

//重置时设置属性
function resetSet() {
    var $ = bt.jquery, form = bt.form();
    $("#searchBtn").click();
    dicts.get("recType").attr({"data-disabledValue": ""});

    $("#netTr1").hide();
    $("#netTr2").hide();
    $("#netTr3").hide();

    $("#bankTr1").show();
    $("#bankTr2").show();
    dicts.get("recType").attr({"data-disabledValue": ""});

    $('#recAcctId1').val('');
    $('#recNm1').val('');
    $('#recAcctsvcr1').val('');
    $('#recAcctId2').val('');
    $('#recNm2').val('');
    $('#recAcctsvcr2').val('');
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId
    });
}

//批量提交
function submitBatch(data) {

    var recType, recAcctId, recNm, recAcctsvcr;
    var len = data.length;
    var rowData = data.splice(0, len);
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //批量提交权限控制
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abBillDiscountApply:apply', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
    }
    //校验必填项
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var flag = checkRec(rowData);
    if (!flag) {
        return;
    }
    // 新系统出票且分包时，交易金额重新赋值
    for (var i = 0; i < len; i++) {
        if (rowData[i].newStmIssuofpprInd === '1' && rowData[i].blgrdbagcrclPrmtInd === '1') {
            // 判断交易金额是否为空
            var transMoney = $("#transMoney_" + rowData[i].accId).val()
            if (!transMoney) {
                layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                return;
            } else {
                rowData[i].transMoney = transMoney;
            }
        }
    }
    //贴入机构
    recType = dicts.get("recType").val();
    if (recType == '1') {
        if (!isSameNet()) {     //单位是否属于同一网点
            return;
        }
        recAcctId = $("#recAcctId1").val();
        recNm = $("#recNm1").val();
        recAcctsvcr = $("#recAcctsvcr1").val();
    }
    if (recType == '0') {
        recAcctId = $("#recAcctId2").val();
        recNm = $("#recNm2").val();
        recAcctsvcr = $("#recAcctsvcr2").val();
    }
    //证书签名
    var signDataMsg = signData("贴现申请提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        url = basePath + "/bill/accebill/abBillDiscountApply/batchSubmit";
        var accIds,transMoneys;
        //贴入机构
        var insideSing = dicts.get("recType").val();
        //业务办理日期
        var businessDate = $("#businessDate").val();
        var rmk = $("#rmk").val();

        //贴现明细信息
        var abDiscountObj = {};
        var abDiscountList = [];
        var sttlmmk = $("#sttlmmk").find("select").val();//清算方式
        //获取表单要数
        var rpdmk = $("#rpdmk").find("select").val();                    //贴现种类
        var rpdopendt = $("#rpdopendt").val();                           //赎回开放日
        var rpdduedt = $("#rpdduedt").val();                             //赎回截止日
        var interestRates = $("#interestRates").val().substring(0, $("#interestRates").val().indexOf('.')+5);                   //贴现利率
        var interestRates = $("#interestRates").val();                   //贴现利率
        var aoaccninfId = $("#aoaccninfId").val();                       //入账账户
        var aoaccninfName = $("#aoaccninfName").val();                   //入账户名
        var aoaccninfAc = $("#aoaccninfAc").val();                       //入账行号
        var discountDate = $("#discountDate").val();                     //贴现日期
        var rpdintrstRate = $("#rpdintrstRate").val().substring(0, $("#interestRates").val().indexOf('.')+5);
        var rpdintrstRate = $("#rpdintrstRate").val();
        var invcNb = $("#invcNb").val();                           //发票号
        var txlCtrctNb = $("#txlCtrctNb").val();
        for (var i = 0; i < len; i++) {
            var accId = rowData[i].accId;
            if (accIds) {
                accIds = accIds + "," + accId;
            } else {
                accIds = accId;
            }
            if (transMoneys) {
                transMoneys = transMoneys + "," + rowData[i].transMoney;
            } else {
                transMoneys = rowData[i].transMoney;
            }
            //获取批量设置要素
            var deferdate = $("#deferdate" + accId).text();                     //顺延天数
            //赎回利率
            abDiscountObj = {
                deferdate: deferdate,
                rpdmk: rpdmk,
                sttlmmk: sttlmmk,
                rpdopendt: rpdopendt,
                rpdduedt: rpdduedt,
                interestRates: interestRates,
                aoaccninfId: aoaccninfId,
                aoaccninfName: aoaccninfName,
                aoaccninfAc: aoaccninfAc,
                discountDate: discountDate,
                invcNb: invcNb,
                txlCtrctNb: txlCtrctNb,
                rpdintrstRate: rpdintrstRate
            };
            abDiscountList.push(abDiscountObj);
        }
        /*$.post(url, {
            "accIds": accIds,
            "businessDate": businessDate,
            "recNm": recNm,
            "recAcctsvcr": recAcctsvcr,
            "recAcctId": recAcctId,
            "insideSing": insideSing,
            "rmk": rmk,
            "sttlmmk":sttlmmk,    //清算方式
            "abDiscountList":abDiscountList      //新增贴现办理信息,
        }, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');*/
        bt.loading.show();//显示加载中
        bt.jquery.ajax({
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                "accIds": accIds,
                "transMoneys": transMoneys,
                "businessDate": businessDate,
                "recNm": recNm,
                "recAcctsvcr": recAcctsvcr,
                "recAcctId": recAcctId,
                "insideSing": insideSing,
                "rmk": rmk,
                "sttlmmk": sttlmmk,    //清算方式
                "abDiscountList": abDiscountList      //新增贴现办理信息,
            }),
            asycn: false,
            url: url,
            success: function (result) {
                bt.loading.hide(); //关闭加载中
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            dataType: 'json'
        });
    })
}

//确定网点后回调
function netCallback(id, name) {
    //判断是否需要重新加载账户控件
    if ($("#recCorpId").val() && $("#recCorpId").val() == id) {
        return;
    }
    bt.accountTable.get("recAcctId1").reload({
        urldata: {
            corpId: id,
            electricBill: "1",
            permission: "",
            ignorePermission: "true"
        }
    });
    $("#recCorpId").val(id);
    $("#recNm1").val(name);
}

function recAcctIdClick(data) {
    var $ = bt.jquery, form = bt.form();
    $("#recAcctId1").val(data.bankAcc);
    $("#recNm1").val(data.accName);
    $("#recAcctsvcr1").val(data.bankCode);
}


//选择贴入机构时触发
function selectRecType() {
    var $ = bt.jquery, form = bt.form();
    var recType = dicts.get("recType").val();
    //结算中心
    if (recType == '1') {
        $("#netTr1").show();
        $("#netTr2").show();
        $("#netTr3").show();

        $("#bankTr1").hide();
        $("#bankTr2").hide();
    }
    //银行
    if (recType == '0') {
        $("#netTr1").hide();
        $("#netTr2").hide();
        $("#netTr3").hide();

        $("#bankTr1").show();
        $("#bankTr2").show();
    }
    $('#recAcctId1').val('');
    $('#recNm1').val('');
    $('#recAcctsvcr1').val('');
    $('#recAcctId2').val('');
    $('#recNm2').val('');
    $('#recAcctsvcr2').val('');
    $('#center').val('');
    $("#recCorpId").val('');
    netCallback('0');

    if (recType == '1') {
        isSameNet();
    }
}

function checkRec(rowData) {
    var origNo, checkRecNos, checkRecAndReqNos;
    var recType = dicts.get("recType").val();
    if (recType == '1') {  //贴到结算中心
        var recCorpId = $("#recCorpId").val();
        for (var i = 0; i < rowData.length; i++) {
            origNo = rowData[i].origNo;
            if (rowData[i].billFlag == '11') { //电票只能贴现到银行
                if (checkRecNos) {
                    checkRecNos = checkRecNos + origNo + "<br>";
                } else {
                    checkRecNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            if (rowData[i].corpId == recCorpId) {//贴入人，贴出人不能为同一单位
                if (checkRecAndReqNos) {
                    checkRecAndReqNos = checkRecAndReqNos + origNo + "<br>";
                } else {
                    checkRecAndReqNos = "<br>" + origNo + "<br>";
                }

            }
        }
    }
    if (checkRecNos) {
        layer.alert(bt.lang("bill.accbill.abbillDiscountApply.EleMustToBank", [checkRecNos]), {icon: 7});
        return false;
    }
    if (checkRecAndReqNos) {
        layer.alert(bt.lang("bill.accbill.abbillDiscountApply.checkRecAndReq", [checkRecAndReqNos]), {icon: 7});
        return false;
    }
    return true;
}


//获取选中的所有数据
function getSelectData() {
    var selectData;
    bt.use('datatable', function () {
        selectData = bt.datatable.getSelectData("#appTable");
    })
    return selectData;
}

//判断所选单位是否在同一网点下
function isSameNet() {
    //票据贴现到网点进行判断
    var recType = dicts.get("recType").val();
    if (recType == '0') {
        return;
    }
    var selectData = getSelectData();
    if (!selectData.length) {
        netCallback('0');
        return;
    }
    var corpIds;
    var corpNames;
    for (var i = 0; i < selectData.length; i++) {
        if (corpIds) {
            corpIds = corpIds + "," + selectData[i].corpId;
            corpNames = corpNames + "," + selectData[i].corpName;
        } else {
            corpIds = selectData[i].corpId;
            corpNames = selectData[i].corpName;
        }
    }
    var listSysCorp;
    //获取所选数据的单位信息
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abBillDiscountApply/listSysCorp",
        data: {corpIds: corpIds, corpNames: corpNames},
        async: false,
        success: function (result) {
            if (result.success) {
                listSysCorp = result.obj;
            } else {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        },
        dataType: 'json'
    });
    if (!listSysCorp.length) {           //无法获取单位信息,请检查数据!
        //layer.alert(bt.lang('bill.accbill.abbillDiscountApply.corp.isnotexist'),{icon:7});
        return;
    }
    var firstParentId;
    for (var i = 0; i < listSysCorp.length; i++) {
        if (i == 0) {
            firstParentId = listSysCorp[i].parentId;
        } else {
            if (firstParentId != listSysCorp[i].parentId) {   //贴现到结算中心时,所选单位必须属于同一直属网点
                layer.alert(bt.lang('bill.accbill.abbillDiscountApply.corp.notSameNet'), {icon: 7});
                return;
            }
        }
    }
    if (!firstParentId) {         //所选单位为顶级网点,只能贴现到银行!
        layer.alert(bt.lang('bill.accbill.abbillDiscountApply.topCorp.toBank'), {icon: 7});
        return;
    }
    //获取对应网点信息
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abBillDiscountApply/getSysCorpById",
        data: {id: firstParentId},
        async: false,
        success: function (result) {
            if (result) {
                $("#center").val(result.name);
                //$("#recNm1").val(result.name);
                //判断网点是否相同
                netCallback(result.id, result.name);
            }
        },
        dataType: 'json'
    });
    return true;
}

/**
 * 判断是纸票流程还是电票流程
 * @returns {Number} 0:纸票 1:电票
 */
function checkBillFlag() {
    var data = bt.datatable.getSelectData("#appTable");
    var len = data.length;
    var rowData = data.splice(0, len);
    var sign = 0;//处理标志 0:纸票处理标志 1:电票处理标志
    for (i = 0; i < len; i++) {
        if ("11" == rowData[i].billFlag) {//billFlag=11.则设置sign=1
            sign = 1;
            break;
        }
    }
    if (len == 0) {//若没选择数据，则按电票处理（即选择单位不赋值给质权人名称）
        sign = 1;
    }
    return sign;
}

/**根据选择的业务设置必填标志
 * 选择数据包含电票则必填，不包含电票则不必填**/
function setVerify() {
    var billFlag = checkBillFlag();
    if (billFlag == 1) {
        $("#recAcctId2").attr("lay-verify", "required|strlength");
        $("#recAcctsvcr2").attr("lay-verify", "required|strlength");
    } else {
        $("#recAcctId2").attr("lay-verify", "");
        $("#recAcctsvcr2").attr("lay-verify", "");
    }
    form.renderIcon();
}

//附件
function attachmentView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.accId,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.viceId || null),
        "briefId": '6011',
        "isNotOp": isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//设置贴现方式
function setRpdmk() {
    var rpdmk = $("#rpdmk").find("select").val();  //贴现方式
    if (rpdmk == 'RM00') {
        $('#rpdopendt').attr({"lay-verify": "", "readonly": true});
        $('#rpdduedt').attr({"lay-verify": "", "readonly": true});
        $('#rpdopendt').val('');
        $('#rpdduedt').val('');
        dateType.get('rpdopendt').reset();
        dateType.get('rpdduedt').reset();
    } else {
        $('#rpdopendt').attr({"lay-verify": "required", "readonly": false});
        $('#rpdduedt').attr({"lay-verify": "required", "readonly": false});
        dateType.get('rpdopendt').reset();
        dateType.get('rpdduedt').reset();
    }
    form.renderIcon();
}

//判断是否触发计算数值函数
function changeFn() {
    var rpdmk = $("#rpdmk").find("select").val();    //贴现方式
    var discountDate = $("#discountDate").val();     //贴现申请日期
    var rpdopendt = $("#rpdopendt").val();           //赎回开放日
    var rpdduedt = $("#rpdduedt").val();             //赎回截止日
    var interestRates =$("#interestRates").val().substring(0, $("#interestRates").val().indexOf('.')+5);   //贴现利率
    //比较申请日与截止日
    discountDate = dateFmt.get(discountDate);
    rpdopendt = dateFmt.get(rpdopendt);
    rpdduedt = dateFmt.get(rpdduedt);
    //赎回式
    if (rpdmk == 'RM01' && discountDate && rpdopendt && rpdduedt && interestRates) {
        if (dateFmt.compare(rpdduedt, rpdopendt) > 0 && dateFmt.compare(rpdduedt, discountDate) > 0 &&
            dateFmt.compare(rpdopendt, discountDate) >= 0) {     //贴现赎回开放日必须大于申请日期
            if (culDeferdate(dateFmt.format(rpdduedt, "yyyy-mm-dd")) == -1) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.dateError', [dateFmt.format(rpdduedt, "yyyy-mm-dd")]), {icon: 7});
                return;
            }
            culculate();
            return;
        } else {
            //赎回截止日必须大于赎回开放日!
            if (dateFmt.compare(rpdduedt, rpdopendt) <= 0) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comDueToOpenMsg'), {icon: 7});
                return;
            }
            if (dateFmt.compare(rpdopendt, discountDate) < 0) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comRpdopendtMsg'), {icon: 7});
                return;
            }
            if (dateFmt.compare(rpdduedt, discountDate) <= 0) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comRpdduedtMsg'), {icon: 7});
                return;
            }
        }
    }
    //买断式
    if (rpdmk == 'RM00' && discountDate && interestRates) {
        culculate();

    }
}


//计算数值
function culculate() {
    bt.use(['date', 'dateType', 'moneyType'], function () {
        $ = bt.jquery, form = bt.form();
        var tableData = getSelectData();
        if (!tableData.length) {
            return;
        }
        var rpdmk = $("#rpdmk").find("select").val();  //贴现方式
        var discountDate = $("#discountDate").val();   //贴现申请日期
        var rpdopendt = $("#rpdopendt").val();          //赎回开放日
        var rpdduedt = $("#rpdduedt").val();            //赎回截止日
        var comDisToStartNos;
        var comDisToEndNos;
        var comOpenToStartNos;
        var comDueToEndNos;
        var dateErrorNos;
        var culEndDate = '';                    //贴现利息计算截止日
        for (var i = 0; i < tableData.length; i++) {
            //格式化日期
            discountDate = dateFmt.get(discountDate);
            rpdopendt = dateFmt.get(rpdopendt);
            rpdduedt = dateFmt.get(rpdduedt);
            var accId = tableData[i].accId;
            var origNo = tableData[i].origNo;                    //票据号
            var startDate = dateFmt.get(tableData[i].startDate);   //出票日期
            var endDate = dateFmt.get(tableData[i].endDate);       //到期日期
            //贴现申请日期不能小于开票日期
            if (dateFmt.compare(startDate, discountDate) > 0) {
                if (comDisToStartNos) {
                    comDisToStartNos = comDisToStartNos + origNo + "<br>";
                } else {
                    comDisToStartNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            //贴现申请日期必须小于到期日期
            if (dateFmt.compare(endDate, discountDate) <= 0) {
                if (comDisToEndNos) {
                    comDisToEndNos = comDisToEndNos + origNo + "<br>";
                } else {
                    comDisToEndNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            //判断贴现种类
            if (rpdmk == "RM01") { //赎回式
                //贴现赎回开放日不能小于开票日期
                if (dateFmt.compare(startDate, rpdopendt) > 0) {
                    if (comOpenToStartNos) {
                        comOpenToStartNos = comOpenToStartNos + origNo + "<br>";
                    } else {
                        comOpenToStartNos = "<br>" + origNo + "<br>";
                    }
                    continue;
                }
                //贴现赎回截止日必须小于到期日期
                if (dateFmt.compare(endDate, rpdduedt) <= 0) {
                    if (comDueToEndNos) {
                        comDueToEndNos = comDueToEndNos + origNo + "<br>";
                    } else {
                        comDueToEndNos = "<br>" + origNo + "<br>";
                    }
                    continue;
                }
                culEndDate = rpdduedt;  //赎回截止日为贴现利息计算截止日
            } else {                    //买断式
                culEndDate = endDate;   //到期日期为贴现利息计算截止日
                //日历维护
                if (culDeferdate(dateFmt.format(culEndDate, "yyyy-mm-dd")) == -1) {
                    if (dateErrorNos) {
                        dateErrorNos = dateErrorNos + origNo + "<br>";
                    } else {
                        dateErrorNos = "<br>" + origNo + "<br>";
                    }
                    continue;
                }
            }

            //格式化日期
            culEndDate = dateFmt.format(culEndDate, "yyyy-mm-dd");
            discountDate = dateFmt.format(discountDate, "yyyy-mm-dd");
            var deferdate = culDeferdate(culEndDate);  //顺延天数
            $("#deferdate" + accId).text(deferdate);//顺延天数
        }
        bt.datatable.updateSummary('#appTable');
        //重新绘制表格
        $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();

        if (comDisToStartNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDisToStartMsg", [comDisToStartNos]), {icon: 7});
            return;
        }

        if (comDisToEndNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDisToEndMsg", [comDisToEndNos]), {icon: 7});
            return;
        }

        if (comOpenToStartNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comOpenToStartMsg", [comOpenToStartNos]), {icon: 7});
            return;
        }

        if (comDueToEndNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDueToEndMsg", [comDueToEndNos]), {icon: 7});
            return;
        }

        if (dateErrorNos) {
            layer.alert(bt.lang('bill.accebill.abBillDiscountManager.dateErrorMsg', [dateErrorNos]), {icon: 7});

        }
    });
}


//填写入账行号时，校验开户行是否一致
function clickAInfAc(fieldName) {
    //获取选择数据
    var selectData = getSelectData();
    var len = selectData.length;
    if (!len) {
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectDiscountBill'), {icon: 7});
        return;
    }
    var obj = checkSameCode(fieldName, selectData);
    if (!obj.flag && fieldName == 'recAcctsvcr') {
        //所选电票贴入人开户行号不一致，无法设置贴入人行号为入账行号
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectSameAcctsvcr'), {icon: 7});
        return;
    }
    if (!obj.flag && fieldName == 'bankCode') {
        //所选电票贴出人开户行号不一致，无法设置贴出人行号为入账行号
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectSameBankCode'), {icon: 7});
        return;
    }
    $("#aoaccninfAc").val(obj.code);
    if (fieldName == 'recAcctsvcr') {
        $("#aoaccninfAc").attr("opType", "accRec");
    }
    if (fieldName == 'bankCode') {
        $("#aoaccninfAc").attr("opType", "accReq");
    }
}

//计算假日顺延天数
function culDeferdate(culEndDate) {
    $ = bt.jquery, form = bt.form();
    var deferdate;
    var url = basePath + '/bill/accebill/abBillDiscountManager/getDeferdate';
    $.ajax({
        type: 'post',
        data: {"endTime": culEndDate},
        async: false,
        url: url,
        success: function (result) {
            if (result.success) {
                bt.use(['date', 'dateType'], function () {
                    var workDay = dateFmt.format(dateFmt.get(result.obj), "yyyy-mm-dd");
                    deferdate = bt.dateType.getFun().getBetweenDay(culEndDate, workDay);
                })
            } else {
                deferdate = -1;      //返回null,返回-1
            }
        },
        dataType: 'json'
    });
    return deferdate;
}

//贴出人或贴入人开户行号是否一致
function checkSameCode(fieldName, selectData) {
    var code = "";
    var selCode;
    var obj = {};
    var flag = true;
    obj['flag'] = flag;
    obj['code'] = code;
    //贴入机构
    var recType = dicts.get("recType").val();
    //贴入行号
    var recAcctsvcr = "";
    if (recType == '1') {
        if (!isSameNet()) {     //单位是否属于同一网点
            return;
        }
        recAcctsvcr = $("#recAcctsvcr1").val();
    }
    if (recType == '0') {
        recAcctsvcr = $("#recAcctsvcr2").val();
    }
    for (var i = 0; i < selectData.length; i++) {
        if (selectData[i].billFlag == '11') {
            //贴出人开户行号
            if (fieldName == "bankCode") {
                if (selectData[i].bankCode == "" || selectData[i].bankCode == null) {
                    selCode = selectData[i].reqAcctsvcr;
                } else {
                    selCode = selectData[i].bankCode;
                }
            }
            if (fieldName == "recAcctsvcr") {
                selCode = recAcctsvcr;
            }
            if (code && code != selCode) {
                flag = false;
                obj['flag'] = flag;
                break;
            }
            code = selCode;//赋值
            obj['code'] = code;
        }
    }
    return obj;
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].accId) < 0) {
                    idsValArray.push(datas[i].accId);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].accId);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

var timeId = null
$("#appTable").on("blur",".trans-money-input",function () {
    var that = this
    if(timeId){
        clearTimeout(timeId);
        timeId = null
    }
    timeId = setTimeout(function () {
        var key = $(that).data("key");
        var tableData = $("#appTable").DataTable().rows().data()
        if(key && tableData && tableData.length){
            for (var i = 0; i < tableData.length; i++) {
                if(tableData[i].accId === key){
                    tableData[i].transMoney = $("#transMoney_"+key).val();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)
})

//获取交易金额
function changeMoney(){
    bt.use(['moneyType'], function (){
        $ = bt.jquery, form = bt.form();
        var tableData = getSelectData();
        if (!tableData.length) {
            return;
        }
        var moneyDataLen = tableData.length;
        // 新系统出票且分包时，交易金额重新赋值
        for (var i = 0; i < moneyDataLen; i++) {
            if (tableData[i].newStmIssuofpprInd === '1' && tableData[i].blgrdbagcrclPrmtInd === '1') {
                // 判断交易金额是否为空
                var transMoney = $("#transMoney_" + tableData[i].accId).val()
                if (!transMoney) {
                    layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                    return;
                } else {
                    tableData[i].transMoney = transMoney;
                }
            }
        }

    });

}
//入账账户
clickCallback = function (data) {
    $('#aoaccninfId').val(data.bankAcc);
    $('#aoaccninfName').val(data.accName);
    $('#aoaccninfAc').val(data.bankCode);
}