bt.use(['form', 'layedit', 'moneyType', 'printType','element', 'btUpload','money'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.printType.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        isExpand: true,
        gid: $("#attachmentId").val()
    });

    $.ajaxSettings.async = false;
    var id = $("#id").val();
    $('#id').val(id);
    $.post(basePath+'/foreign/fextranreqCust/viewJson/'+id,function(result){
        if(result.success){
            var obj = result.obj;
            setHiddenFormValue(obj);
            var templateId = obj.fexTranReqPage.templateId;
            var versionNo = obj.fexTranReqPage.versionNo;
            if(templateId && versionNo){
                $.post(basePath+'/foreign/fexTranReqConfig/getTemplateConfigByAereTree/'+templateId+'/'+versionNo,function(result){
                    if(result.success){
                        console.log(result.obj)
                        createHtml(result.obj,obj);
                    }
                },'JSON');
            } else if (templateId){
                $.post(basePath+'/foreign/fexTranReqConfig/getTemplateConfigByAereTree/'+templateId ,function(result){
                    if(result.success){
                        console.log(result.obj)
                        createHtml(result.obj,obj);
                    }
                },'JSON');
            }

        }

    },'JSON');
    $.ajaxSettings.async = true;
    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#settleDate").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            wishPayday: $("#settleDate").val(),
            payerAcc: $("#corpBankAcc").val(),
            payeeAccName: $("#cotpAccName").val(),
            payeeAcc: $("#cotpBankAcc").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    }

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var cdSign = $("#cdSign").val();
        if (budgetSel == "1" && cdSign=="1") {
            $genOtherHtml(corpId);
        } else {
            $("#tagItemDetail").html("");
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/foreign/fexmessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();

    /**
     * 生成自定义信息
     * @return
     */
    $genCustHtml = function () {
        var url = basePath + "/foreign/fextranreqCust/viewCustInfo/" + $('#id').val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#custMsg").html("");
                $("#custMsg").html(json);
                form.render();
            }
        });
    }
    $genCustHtml();

});

function view(id) {
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/view/" + id
    });
}
/**
 * 给页面所有隐藏 input 批量赋值
 * @param {Object} res - 接口完整返回数据
 */
function setHiddenFormValue(res) {
    // 安全判断
    if (!res) return;
    const data = res.fexTranReqPage || {};

    // ==================== 系统参数 ====================
    // $('#id').val(data.id || '');
    $('#billNo').val(data.billNo || '');
    $('#corpId').val(data.corpId || '');
    $('#settleDate').val(data.settleDate || '');

    // 这些直接在 res 上
    $('#budgetSel').val(res.budgetSel || '');
    $('#payItem').val(res.payItem || '');
    $('#businessName').val(res.businessName || '');
    $('#processSysname').val(res.processSysname || '');
    $('#busType').val(res.busType || '');

    // ==================== 资金计划参数 ====================
    $('#dtlColSel').val(res.dtlColSel || '');

    // 账户信息
    $('#curId').val(data.curId || '');
    $('#corpBankAcc').val(data.corpBankAcc || '');
    $('#cotpAccName').val(data.cotpAccName || '');
    $('#cotpBankAcc').val(data.cotpBankAcc || '');
    $('#cdSign').val(data.cdSign || '');
}
var fieldLabelMap = {
    // ==================== baseInfoBody 基本信息 ====================
    templateId:'templateName', // 模板ID
    billNo: "billNo",               // 交易号码
    tranAppDtlNo: "tranAppDtlNo",   // 交易需求编号
    // 产品：模板字段编码为 proId（不是 product），取 VO 的产品名称，否则会显示产品ID
    proId: "proName",               // 产品
    difBank: "difBank",             // 跨行标志
    cdSign: "cdSign",               // 收付方向
    accNature: "accNature",         // 资金性质
    remitType: "remitType",         // 汇款类型
    isPayed: "isPayed",             // 是否线下付款
    corpInfo: "corpInfo",           // 主体信息
    corpId: "corpName",             // 单位
    cotpInfo: "cotpInfo",           // 对手信息
    copt: "cotpName",               // 交易对手
    corpBank: "corpBankName",       // 主体单位银行
    cotp: "cotpBankName",           // 交易对手银行
    corpBankAcc: "corpBankAcc",     // 主体账户
    bankAcc: "cotpBankAcc",         // 对手账户
    branchCode: "branchCode",       // 交易办理机构
    payeeType: "payeeType",         // 收款人类型
    cotpCurName: "corpAccEnglishCode", // 账户币种
    corpAccCurNameDiv: "corpAccEnglishCode", // 账户币种
    cotpCurName: "cotpAccEnglishCode", // 对手账户币种
    cotpAccCurNameDiv: "cotpAccEnglishCode", // 对手账户币种
    accName: "corpAccName",         // 账户名称
    corpAccNameDiv:"corpAccName", // 主体单位账户名称
    address: "cotpAdrr",            // 对手地址
     accType: "accType",             // 账户类别
    // accType:function (value){
    //     return bt.dict('btBankAcc.accType',value)
    // },
    corpAccCurId:'corpAccEnglishCode', // 主体单位账户币种
    cotpCountry: "cotpCountryName",  // 对手国家
    corpNameEn: "corpNameEn",       // 英文单位名
    cotpBankCountry: "nameZh",      // 对手银行国家
    corpAddrEn: "corpAddrEn",       // 英文地址
    cotpSwiftCode: "cotpSwiftCode", // Swift Code
    corpAddr: "corpAddr",           // 中文地址
    localClearNo: "localClearNo",   // 本地清算号
    payerLocalClearNo: "payerLocalClearNo", // 付款清算号
    cotpBankAdrr: "cotpBankAdrr",   // 对手银行地址
    corpContact: "corpContact",     // 联系人
    agentBank: "cotpAgentBank",     // 代理行
    corpContactTel: "corpContactTel", // 联系电话
    agentBankAddr: "cotpAgentBankAddr", // 代理行地址
    bursarBillNo: "bursarBillNo",   // 会计凭证号
    agentAcc: "cotpAgentBankAcc",   // 代理行账户
    bankPayTypeId: "bankPayTypeName",     // 支付类型
    bnfRelate: "bnfRelate",         // 受益人关系
    countryId:'nameZh',      // 交易对手银行所在国家地区
    cotpAccCurNameDiv:'cotpAccEnglishCode', // 对手账户币种

    // ==================== fexTranInfoBody 交易信息 ====================
    curId: "englishCode",         // 币种
    payMoney: "payMoney",           // 金额
    contractRate: "contractRate",   // 合同汇率
    payMoneyConvert: "payMoneyConvert", // 折算金额
    foreignContractNo: "foreignContractNo", // 合同号
    transTime: "tranDate",          // 交易日期
    optDate: "settleDate",          // 结算日期
    remitMethod: "remitMethod",     // 汇款方式
    cashItem: "cashItemName",           // 现金流项目
    remitUse: "remitUse",           // 汇款用途
    addWord: "addWord",             // 附言
    rmk: "rmk",                     // 英文备注
    rmkLocal: "rmkLocal",           // 本地备注

    // ==================== costInfoBody 费用信息 ====================
    costSign: "costSign",           // 费用承担
    costAcc: "costAcc",             // 费用账户
    costCurId: "costCurId",         // 费用币种
    costMoney: "costMoney",         // 费用总额

    // ==================== reportInfoBody 申报信息 ====================
    remitRoute: "remitRoute",       // 汇款去向
    shipDate: "shipDate",           // 最迟装运日
    isBonded: "isBonded",           // 是否保税
    busNo: "busNo",                 // 业务编号
    arrivalDate: "arrivalDate",     // 进口日期
    declareNo: "declareNo",         // 报关单号
    contractNo: "contractNo",       // 合同号
    invoiceNo: "invoiceNo",         // 发票号
    tranCode1: "tranName1",         // 交易编码1
    tranCode2: "tranName2",         // 交易编码2
    tranAmt1: "tranAmt1",           // 金额1
    tranAmt2: "tranAmt2",           // 金额2
    tranCurId1: "tranEnglishCode1", // 币种1
    tranCurId2: "tranEnglishCode2", // 币种2
    tranAddWord1: "tranAddWord1",   // 附言1
    tranAddWord2: "tranAddWord2",   // 附言2
    dealer: "dealer",               // 交易员
    reqDate: "createDate",          // 申请日期
    tranAddr: "tranAddr",           // 交易地点
    tranGroup: "tranGroupName",     // 交易组
    originCountry: "originCountryName", // 起运国

    // ==================== otherCustom 其他信息 ====================
    payType: "payType",             // 付款类型
    remitNature: "remitNature",     // 付汇性质
    regulatoryType: "regulatoryType", // 监管类型
    rptGateEntId: "rptGateEntName" // 报关经营单位名称
};
// 需要格式化的字段
var numberFieldType = {
    payMoney: "money",
    payMoneyConvert: "money",
    contractRate: "rate", // 6
    costMoney: "money",
    tranAmt1: "money",
    tranAmt2: "money"
};
// 根据上面关系转换一下
function getFieldValue(config, data) {
    var fieldCode = config.fieldCode;
    var code = fieldLabelMap[fieldCode] || fieldCode;
    // 注意：0 是有效值，不能用 || ''，否则 rptGateDate/checkDay 等为 0 时不显示
    var value = (data && data[code] !== null && data[code] !== undefined) ? data[code] : '';

    // 字典翻译
    if (config.fieldDictGroup) {
        // 如果值是空，直接返回
        if (value === null || value === undefined || value === '') {
            // 不处理
        } else {
            // 包含逗号，拆分循环处理
            if (value.indexOf(',') > -1) {
                var arr = value.split(',');
                var resultArr = [];
                for (var i = 0; i < arr.length; i++) {
                    var item = arr[i].trim(); // 去空格
                    resultArr.push(bt.dict(config.fieldDictGroup, item));
                }
                // 用逗号拼回去
                value = resultArr.join(',');
            } else {
                // 单个值，正常处理
                value = bt.dict(config.fieldDictGroup, value);
            }
        }
    }
    var dataClass = config.dataClass || ''
    // 金额/汇率格式化：只有 null/undefined/'' 不格式化，0 必须格式化
    var formatType = numberFieldType[code];

    if(!formatType && dataClass){
        if(dataClass.indexOf('money') !== -1){ // 金额
            formatType = 'money'
        }else if(dataClass.indexOf('rate') !== -1){ // 比例
            formatType = 'rate'
        }
    }

    if (formatType) {
        // 空值不处理
        if (value === null || value === undefined || value === '') {
            // 不格式化
        }
        // 0 或数字 → 必须格式化
        else {
            if (formatType === 'money') {
                value = bt.money.format(value);
            } else {
                if(code === 'contractRate'){
                    value = bt.money.format(value, {
                        separator: "",
                        decimal: 6
                    });
                }else if(formatType === 'rate'){
                    var num = Number(value || 0) * 100;
                    value = bt.money.format(num, {
                        separator: "",
                        decimal: 2
                    }) + '%';
                }
            }
        }
    }

    return value;
}

// 公共 TD 拼接
function buildTd(label, value, labelWidth, valueWidth, labelColspan, valueColspan) {
    label = label || '';
    // 注意：0 是有效值，不能用 || ''
    value = (value === null || value === undefined) ? '' : value;
    labelColspan = labelColspan || 1;
    valueColspan = valueColspan || 1;
    return '<td class="textRight" width="' + labelWidth + '" colspan="' + labelColspan + '">' + label + '</td>' +
        '<td width="' + valueWidth + '" colspan="' + valueColspan + '">' + value + '</td>';
}
var newLayoutHmtl = {};
// 主渲染（已全部替换为 $.each，修复循环问题）
function createHtml(config, data) {
    var fexData = data.fexTranReqPage || {};
    fexData.dealer = data.dealer || ''
    var newLayoutHmtl = {}; // 防止未定义报错

    // 第一层循环 → 替换成 $.each
    $.each(config, function (i1, item1) {
        // ========================= baseInfo =========================
        if (item1.fieldCode === 'baseInfo' && item1.children && item1.children.length) {
            var html = '';
            var flag = 0;
            var consumedBaseInfoIndexes = {};

            // 替换成 $.each
            $.each(item1.children, function (i2, item2) {
                if (consumedBaseInfoIndexes[i2]) return true;
                // jQuery：return true = 跳过当前项，继续循环 ✅
                if (!item2.isVisible) return true;

                var fieldCode = item2.fieldCode;
                if (fieldCode === 'cotpInfo') return true;

                // 普通字段
                if (fieldCode !== 'corpInfo') {
                    var val = getFieldValue(item2, fexData);
                    if (flag === 0) {
                        html += '<tr>';
                    }
                    html += buildTd(item2.fieldName, val, '20%', '30%', 2, 1);
                    flag++;

                    if (flag === 2) {
                        html += '</tr>';
                        flag = 0;
                    }
                }

                // 主体账户跨行信息
                else if (fieldCode === 'corpInfo' && item2.children && item2.children.length) {
                    if (flag === 1) {
                        html += '<td class="textRight" width="20%" colspan="2"></td>';
                        html += '<td width="30%"></td></tr>';
                        flag = 0;
                    }

                    // 可显示的主体单位信息
                    var visibleCorpInfoList = getVisibleFields(item2.children);

                    // 可显示的对手账户信息
                    var cotpInfoItem = item1.children[i2 + 1];
                    var visibleCotpInfoList = getVisibleFields(cotpInfoItem.children);
                    var trailingBaseInfoList = [];

                    // 主体字段少于对手字段时，用后续普通字段补齐左侧空位，避免产生空行。
                    var corpInfoGap = visibleCotpInfoList.length - visibleCorpInfoList.length;
                    if (corpInfoGap > 0) {
                        for (var nextIndex = i2 + 2; nextIndex < item1.children.length && trailingBaseInfoList.length < corpInfoGap; nextIndex++) {
                            var nextItem = item1.children[nextIndex];
                            if (!nextItem.isVisible || nextItem.fieldCode === 'corpInfo' || nextItem.fieldCode === 'cotpInfo') {
                                continue;
                            }
                            trailingBaseInfoList.push(nextItem);
                            consumedBaseInfoIndexes[nextIndex] = true;
                        }
                    }

                    // 获取两组数据最大行数，用于循环渲染
                    var maxRow = Math.max(visibleCorpInfoList.length, visibleCotpInfoList.length);

                    for (var r = 0; r < maxRow; r++) {
                        html += '<tr>';
                        var item3 = visibleCorpInfoList[r];
                        var cotpItem = visibleCotpInfoList[r] || {};

                        // 主体单位信息分组标题只跨自身字段，后续普通字段可占用剩余行的完整左半区。
                        if (r === 0 && visibleCorpInfoList.length) {
                            html += '<td align="center" width="5%" rowspan="' + visibleCorpInfoList.length + '" class="rowSpan">' + (item2.fieldName || '') + '</td>';
                        }
                        if (item3) {
                            if (item3.fieldCode === 'accType' && !item3.fieldDictGroup) {
                                item3.fieldDictGroup = 'btBankAcc.accType';
                            }
                            html += buildTd(item3.fieldName, getFieldValue(item3, fexData), '15%', '30%');
                        } else {
                            var trailingItem = trailingBaseInfoList[r - visibleCorpInfoList.length];
                            if (trailingItem) {
                                html += buildTd(trailingItem.fieldName, getFieldValue(trailingItem, fexData), '20%', '30%', 2, 1);
                            } else {
                                html += buildTd('', '', '20%', '30%', 2, 1);
                            }
                        }

                        // 对手账户信息
                        if (r === 0 && visibleCotpInfoList.length) {
                            html += '<td align="center" width="5%" rowspan="' + visibleCotpInfoList.length + '" class="rowSpan">' + (cotpInfoItem.fieldName || '') + '</td>';
                        }
                        if(cotpItem.fieldCode){
                            var codeValue = getFieldValue(cotpItem, fexData);
                            if (cotpItem.fieldCode === 'cotpCountry') {
                                codeValue = fexData.cotpCountryName || '';
                                //国家=中国且单据存的是省/市时才拼省/市，否则拼"地区"（境外客商的国家/地区口径）
                                if (codeValue === 'China' && (fexData.cotpProv || fexData.cotpCity)) {
                                    codeValue += (fexData.cotpProvName || '') + (fexData.cotpCityName || '');
                                } else {
                                    codeValue += (fexData.cotpAddressName || '');
                                }
                            } else if (cotpItem.fieldCode === 'payCorpId') {
                                codeValue = fexData.payCorpName || '';
                            }

                            html += buildTd(cotpItem.fieldName, codeValue, '15%', '30%');
                        }else {
                            html += buildTd('', '', '20%', '30%', 2, 1);
                        }
                        html += '</tr>';
                    }
                }
            });

            if (flag === 1) {
                html += '<td width="20%" colspan="2"></td><td width="30%"></td></tr>';
            }
            newLayoutHmtl['baseInfoBody'] = html
        }

        // ========================= fexTranInfo =========================
        else if (item1.fieldCode === 'fexTranInfo') {
            var html = '';
            var flag = 0;

            $.each(item1.children, function (i2, item2) {
                if (!item2.isVisible) return true;

                var fieldCode = item2.fieldCode;
                fieldCode = fieldLabelMap[fieldCode] || fieldCode;
                var val = getFieldValue(item2, fexData);

                if (flag === 0) {
                    if (fieldCode === 'contractRate' && fexData.cdSign == '0') {
                        html += '<tr style="display: none">';
                    } else {
                        html += '<tr>';
                    }
                }

                if (flag < 2) {
                    if (fieldCode === 'rmk' || fieldCode === 'rmkLocal') {
                        html += buildTd(item2.fieldName, val, '20%', '30%', 1, 3);
                        flag = 2;
                    } else {
                        html += buildTd(item2.fieldName, val, '20%', '30%');
                        flag++;
                    }
                }

                if (flag === 2) {
                    html += '</tr>';
                    flag = 0;
                }
            });

            if (flag === 1) {
                html += '<td></td><td></td></tr>';
            }
            newLayoutHmtl['fexTranInfoBody'] = html
        }

        // ========================= costInfo =========================
        else if (item1.fieldCode === 'costInfo') {
            var html = '';
            var flag = 0;

            $.each(item1.children, function (i2, item2) {
                if (!item2.isVisible) return true;
                if (item2.fieldCode === 'costCurId') {
                    item2.fieldCode = 'cotpAccCurName'
                }

                var val = getFieldValue(item2, fexData);
                if (flag === 0) html += '<tr>';
                html += buildTd(item2.fieldName, val, '20%', '30%');
                flag++;

                if (flag === 2) {
                    html += '</tr>';
                    flag = 0;
                }
            });

            if (flag === 1) {
                html += '<td></td><td></td></tr>';
            }
            newLayoutHmtl['costInfoBody'] = html
        }

        // ========================= reportInfo =========================
        else if (item1.fieldCode === 'reportInfo') {
            var html = '';
            var flag = 0;

            $.each(item1.children, function (i2, item2) {
                if (!item2.isVisible) return true;

                var fieldCode = item2.fieldCode;
                fieldCode = fieldLabelMap[fieldCode] || fieldCode;
                var val = getFieldValue(item2, fexData);

                if (fieldCode === 'dealer') {
                    val = data.dealer || '';
                }

                if (flag === 0) html += '<tr>';
                html += buildTd(item2.fieldName, val, '20%', '30%');
                flag++;

                if (flag === 2) {
                    html += '</tr>';
                    flag = 0;
                }
            });

            if (flag === 1) {
                html += '<td></td><td></td></tr>';
            }
            $('#reportInfoBody').html(html);
        }
            // custInfo --> otherInfo
            // otherInfo -> custInfo
        // ========================= otherInfo =========================
        else if (item1.fieldCode === 'otherInfo') {
            var html = '';
            var flag = 0;
            var custFields = $('#templateCustFields').val();
            if (custFields) {
                custFields = JSON.parse(custFields)
            } else {
                custFields = {}
            }

            item1.children && $.each(item1.children, function (i2, item2) {
                if (!item2.isVisible) return true;
                var val = getFieldValue(item2, custFields);
                var fieldName = item2.fieldName;

                if (flag === 0) html += '<tr>';
                html += buildTd(fieldName, val, '20%', '30%');
                flag++;

                if (flag === 2) {
                    html += '</tr>';
                    flag = 0;
                }
            });

            if (flag === 1) {
                html += '<td></td><td></td></tr>';
            }

            $('#customInfoBody').html(html);
            // custInfo --> otherInfo
        }else if(item1.fieldCode === 'custInfo'){
            var html = '';
            var flag = 0;
            if(item1.children){
                $.each(item1.children, function (i2, item2) {
                    if (!item2.isVisible) return true;

                    var fieldCode = item2.fieldCode;
                    fieldCode = fieldLabelMap[fieldCode] || fieldCode;
                    var val = getFieldValue(item2, fexData);

                    if (flag === 0) html += '<tr>';
                    html += buildTd(item2.fieldName, val, '20%', '30%');
                    flag++;

                    if (flag === 2) {
                        html += '</tr>';
                        flag = 0;
                    }
                });

                if (flag === 1) {
                    html += '<td></td><td></td></tr>';
                }
                $('#otherInfoBody').html(html);
            }

        }
    });

    for (var key in newLayoutHmtl) {
        $('#' + key).html(newLayoutHmtl[key]);
    }
}
// corpInfo 主体账号信息 cotpInfo 对手信息
function getVisibleFields(list){
    var newList = [];
    $.each(list,function (i,item){
        if(item && item.isVisible){
            newList.push(item)
        }
    })
    return newList;
}
