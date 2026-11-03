/**
 * 获取参数值
 * @param operation 操作 [0-利息清单、1-计息凭证、2-积数清单、3-打印记账凭证、4-打印利息通知单]，默认空时为利息清单、计息凭证、积数清单
 * @returns {{}}
 */
function getParams(operation) {
    var param = {};
    var netKey = "netId";//网点参数key名称，利息清单、计息凭证、积数清单参数网点名称key=netId
    var accTypeKey = "accType";//账户类型参数key名称，利息清单、计息凭证、积数清单、打印利息通知单参数账户类型名称key=accType
    if (operation == undefined || operation == null) operation = '';
    if (operation == '0' || operation == '3' || operation == '4') {
        netKey = 'netCode';//打印记账凭证、打印利息通知单参数网点名称key=netCode
    }
    if (operation == '4') {
        accTypeKey = "accTypeCode";//打印利息通知单参数账户类型名称key=accTypeCode
        param["tallyDate"] = $("#tallyDate").val();//记账日期，打印利息通知单需要此参数
        param["blnInstDate"] = $("#endDate").val();//止息日期，打印利息通知单需要此参数
    }
    param["affixAmount"] = $("#affixAmount").val();//附件张数
    param["id"] = $("#id").val();//ID
    param[netKey] = $("#netId").val();//网点
    param[accTypeKey] = $("#accType").val();//账户类型 01-企业活期 02-银行活期 03-放款[内部贷款] 04-银行借款 05-企业定期 08-通知存款
    param["opType"] = $("#opType").val();//操作类型
    if (operation != '3' || operation != '4') {
        param["curr"] = 1;
        param["poolSign"] = $("#poolSign").val();//资金池标识 0-非资金池；1-资金池
        //param["interestSign"]=$("#interestSign").val();//计息标志
        //param["curId:$("#curId").val();//币别ID
    }
    param["account"] = $("#account").val();//内部账户
    param["generateMode"] = $("#generateMode").val()//产生方式 P-批量计息 I-个别计息
    return param;
}

// 监听控制content高度
function init() {
    // 起始判断收缩还是展开
    initTabJX();
    initTabPZ();
    initTabJS();
    // 选项卡高度
    var cardTitleHeight = $(document).find(".layui-tab[lay-filter='card2'] ul.layui-tab-title").height();
    // 需要减去的高度
    var height = $(window).height() - $('.layui-header').height() - cardTitleHeight;
    // 设置高度
    $(document).find(".layui-tab[lay-filter='card2'] div.layui-tab-content").height(height - 200);
}

//利息清单
function initTabJX() {
    bt.jquery("#lxqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/interestBill?" + $.param(getParams()));
    bt.jquery("#lxqdiframe").show(1000);
}

//计息凭证
function initTabPZ() {
    bt.jquery("#jxpziframe").attr("src", basePath + "/finacial/interest/single/noti/toLoadInterestVoucher?" + $.param(getParams()));
    bt.jquery("#jxpziframe").show(1000);
}

//积数清单
function initTabJS() {
    bt.jquery("#jsqdiframe").attr("src", basePath + "/finacial/interest/single/noti/accuBill?" + $.param(getParams()));
    bt.jquery("#jsqdiframe").show(1000);
}

//计息/提交
function calcInterest(opType, opTips) {
    var tallyDate = dateType.get("tallyDate").val();//记账日期
    var endInstDate = dateType.get("endDate").val();//止息日期
    var pageFlag = $("#pageFlag").val();
    layer.confirm(bt.lang("finacial.interest.OpInterest", new Array(opTips)), {
        icon: 3,
        btn: [bt.lang("common.button.confirm"), bt.lang("common.button.cancel")]
    }, function (index, layero) {
        bt.loading.show();
        $.ajax({
            type: "POST",
            url: basePath + "/finacial/interest/single/noti?doInterestaccrualByBatch",
            data: {
                "opType": opType,
                "netId": $("#netId").val(),
                "accType": $("#accType").val(),
                "tallyDate": tallyDate,//记账日期
                "endDate": endInstDate,//止息日期
                "account": $("#account").val(),//账户
                "money": moneyType.get("money").val(),//取款金额
                "affixAmount": $("#affixAmount").val(),//附件张数
                "corpId": $("#corpId").val(),//客户单位
                "pageFlag": pageFlag,//界面标志 0-新增 1-修改
                "id": $("#id").val()//数据ID
            },
            dataType: "json",
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        bt.navtab.refreshParent();
                        if (pageFlag == '1') {
                            init();
                        } else {
                            var fisId = result.params;
                            if(fisId) {
                                $("#id").val(fisId[0]);
                            }
                            init();
                        }
                        if("submit" == opType){
                            if (bt.navtab) {
                                bt.navtab.closeCurrent();
                            } else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                    }
                })
            }
        });
        //layer.close(index);
    }, function () {
    });
}

/**
 * 导出利息清单
 */
function doExportInterestBill() {
    // var url = basePath + "/finacial/interest/fcinterest/doExportInterestBill?opType=" + $("#opType").val()
    //     + "&netCode=" + $("#netId").val() + "&accTypeCode=" + dicts.get("accType").val().trim() + "&poolSign=1";
    var paramsStr = $.param(getParams('0'));
    var url = basePath + "/finacial/interest/fcinterest/doExportInterestBill?" + paramsStr;
    layer.confirm(bt.lang("finacial.interest.ExportPage"), {
        btn: [bt.lang("common.button.page"), bt.lang("common.button.notpage"), bt.lang("common.button.cancel")] //按钮
    }, function () {//分页
        location.href = url + "&impType=1";
    }, function () {//不分页
        location.href = url + "&impType=0";
    }, function () {
    });
}

//打印计息凭证
function doBatchPrint() {
    var url = basePath + "/finacial/interest/single/noti/doBatchPrint?" + $.param(getParams('3'));
    parent.layer.open({
        title: bt.lang("common.button.preview"),
        type: 2,
        area: ['75%', '50%'],
        maxmin: true,
        content: url,
        end: function () {
        }
    });
}

//打印利息通知单
function doPrintRateNotice() {
    var endDate;
    var pageFlag = $("#pageFlag").val();//界面标志 0-新增 1-修改 2-复核 3-查看
    if (pageFlag == undefined || pageFlag == null) pageFlag = '0';
    if ('0' == pageFlag || '1' == pageFlag) {
        endDate = dateType.get("endDate").val();
    } else {
        endDate = $("#endDate").val();
    }
    if (!endDate || endDate.trim() == "") {
        layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon: 5});//请输入止息日期！
        return;
    }
    var url = basePath + "/finacial/interest/fcinterest/doPrintRateNotice?" + $.param(getParams('4'));
    parent.layer.open({
        title: bt.lang("common.button.preview"),
        type: 2,
        area: ['65%', '50%'],
        maxmin: true,
        content: url,
        end: function () {
        }
    });
}