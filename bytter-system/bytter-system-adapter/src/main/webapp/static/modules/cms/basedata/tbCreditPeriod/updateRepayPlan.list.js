var unRepayMoney;
bt.use(['form', 'element', 'moneyType', 'dateType',  'dicts', 'btImport', 'selectType'], function () {
    $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dateFun = dateType.getFun();

    initVariable();

    $.ajax({
        type : 'POST',
        url : basePath + "/cms/basedata/repaymentschedule/getRepayMentScheduleDtl",
        data : {
            planId: $("#planId").val(),
            repayType: $("#orig_repayType").val(),
        },
        async : false,
        dataType : 'json',
        success : function(result) {
            var repayBeginDate;
            if ($("#repayBeginDate").length > 0) {
                repayBeginDate = dateType.get("repayBeginDate").val();
            } else {
                repayBeginDate = $("#repayMinDate").val();
            }
            var html = "";
            unRepayMoney=$("#loanMoney").val();
            $.each(result, function (index, item) {
                var trsNum = $("#repayPlanTB tbody tr").length;
                html += createTr(index+trsNum, item, repayBeginDate, $("#repayDate").val(), unRepayMoney, item.hasdelbtn);
                rowNum = index;
            });
            $("#repayPlanTB tbody").html(html);
            // 重新初始化组件
            reInit();
            // 计算合计金额
            calSum();
        }
    });

    /**
     * 保存
     */
    form.on('submit(save)', function (data) {
        bt.loading.show();
        if (!verification()) {
            bt.loading.hide();
            return false;
        }
        var action = "/cms/basedata/updaterepaymentschedule/updateRepayPlan";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.navtab.closeCurrent();
            }
        }, 'JSON');
        return false;
    });
});

//导出
exportExcel = function () {
    var url = basePath + "/cms/basedata/repaymentschedule/exportBudget";
    var formData = {
       id:$("#id").val()
    }
    DownLoadFile({url:url,data:formData,method:'post'});
}

//导入初始化
importExcel = function () {
    up = bt.btImport.init({
        code: "70000003",
        titleRows: 0,
        headRows: 1,
        success: function (head, data, select) {
            var repayBeginDate;
            if ($("#repayBeginDate").length > 0) {
                repayBeginDate = dateType.get("repayBeginDate").val();
            } else {
                repayBeginDate = $("#repayMinDate").val();
            }
            var html = "";
            deleteUnRepay();//删除未还
            var trsNum = $("#repayPlanTB tbody tr").length;
            $.each(select, function (index, item) {
                html += createTr(index+trsNum, item, repayBeginDate, $("#repayDate").val(), unRepayMoney, true);
                rowNum = index;
            });
            $("#repayPlanTB tbody").append(html);
            // 重新初始化组件
            reInit();
            // 计算合计金额
            calSum();
        }
    });
};


/**
 * 还款方式改变事件
 * @param data
 */
// function repayTypeChange(data){
//     if(isInit){
//         repayTypeOld = $("#repayType").data("defaultvalue");
//     }else{
//         repayTypeOld=repayType;
//  }
//     isInit = false;
//     repayType=data.value;//为repayType赋值
//  //等额本金
//  if(repayType=="02"){
// 	 principal=1;
//  }else{
// 	 principal=0;
//  }
//    //其它
//    if(repayType=="03"||isNull(repayType)){
//
//    	$("#repayBeginDate").attr("disabled","true");
//    	 dicts.get('repayCycel').attr({
//    		 'data-value':'',
//    		 'data-disabledValue':'0,1,2,3'
//    	 });
//    	 setOther();//设置数据
//    	return;
//    }
//    clearData();//清空未还数据(还款方式除了其他的时候)
//    //还本付息
// if(repayType=="01"){
// 	$("#repayBeginDate").attr("disabled","true");
// 	var payDate;
// 	if(isNull(zqDate)){
// 		payDate = repayDate;
// 	}else{
// 		payDate = zqDate;
// 	}
// 	var payMoney= surplusMoneyFixed;
// 	dicts.get('repayCycel').attr({
//   		 'data-value':3,
//   		 'data-disabledValue':'0,1,2'
//   	 });
// 	$("#disposable").attr("checked",true);
// 	addOneOffPayment(payDate,payMoney);
// 	return;
// }
//
// //等额本金和等额本息
// $("#repayBeginDate").removeAttr("disabled");
// dicts.get('repayCycel').attr({
// 	'data-value':'0',
// 	'data-disabledValue':'3'
//  });
// repayCycel="0";
// dicts.render();
// loadData();
// }

/**
 * 加载数据
 */
function initVariable() {
    var billCode = $("#billCode").val();
    var id = $("#id").val();
    var type = $("#type").val();
    var src = ''
    //设在ifram的src
    if (type == "0") {
        src = basePath + "/cms/provide/loansend/viewSend/" + billCode;
    } else if (type == "1") {
        src= basePath + "/cms/loan/cmsloansend/view/" + id + "?param=1";
    } else if (type == "2") {
        src= basePath + "/cms/precative/loanrepay/view?sendCode=" + billCode + "&type=type";
    } else if (type == "0") {
        src= basePath + "/cms/precative/loanrepay/view?sendCode=" + billCode + "&type=type";
    }

    var html = '<iframe id="loanRepayIframe" scrolling="no" src="'+src+'" style="width: 100%; height: 620px;border: 0px;overflow-y: hidden;" name="loanRepayIframe"></iframe>'
    $("#loanRepayIframe1").html(html);
    $("#loanRepayIframe").on("load", function() {
        setTimeout(function () {
            var height = window.frames['loanRepayIframe'].$('body').height();
            $("#loanRepayIframe").height(height+30)
        },300)
    });
}


/**
 * 关闭窗口
 */
doClose = function () {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}


/**
 * 提交时验证
 */
function verification() {


    var repayTypeData = selectType.get("repayType").getData().obj;
    if (repayTypeData) {
        var capitalFrequencyType = repayTypeData.capitalFrequencyType || repayTypeData.capitalfrequencytype;
        // 随借随还时不判断本金
        if (capitalFrequencyType != "N") {
            //统计还款本金
            if (!moneyFun.toNumber(moneyType.get("currentRepayMoneySum").val())) {
                parent.layer.alert(bt.lang("cms.message.repaymentMoney.zero"));//还款本金和还款利息不能同时为零
                return false;
            }
            var repaymentMoneySum = moneyFun.toNumber($("#repaymentMoneySum").val());
            var loanMoney = moneyFun.toNumber($("#loanMoney").val());
            if (Math.round(repaymentMoneySum * 100) != Math.round(loanMoney * 100)) {
                parent.layer.alert(bt.lang("common.message.repayment_Principal"));//还款金额与发放金额不等
                return false;
            }
        }
    }else {
        parent.layer.alert(bt.lang("cms.precative.repaytype.not.null"));//还款方式不能为空
        return false;
    }

    return true;
}

/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == " " || data == "" || data == undefined || data == null) ? true : false;
}


/**
 * 本金利息计算
 */
function calRepayPlan() {
    var params = {};
    var repayBeginDate;
    if ($("#repayBeginDate").length > 0) {
        $("#repayBeginDate").removeClass("layui-form-danger");
        repayBeginDate = dateType.get("repayBeginDate").val();
        if (!repayBeginDate) {
            layer.msg(bt.lang(" cms.RepaymentSchedule.noSelectBeginDate"),{icon: 5});
            $("#repayBeginDate").focus().addClass("layui-form-danger")
            return false;
        }
    }
    if(isNull(selectType.get("repayType").val())){
        $("[data-biz-name='repayType']").find("select").removeClass("layui-form-danger")
        parent.layer.msg(bt.lang("cms.provide.loaninfo.repaytypeempty"), {icon: 2});
        $("[data-biz-name='repayType']").find("select").addClass("layui-form-danger");
        $("[data-biz-name='repayType']").find("input").focus()
        return false;
    }
    params.repayTypeId = selectType.get("repayType").val();
    params.beginDate = repayBeginDate;
    params.sendDate = $("#loanDate").val();
    params.endDate = $("#repayDate").val();
    params.money = unRepayMoney;
    params.loanRate = $("#loanRate").val();
    params.rateBase = $("#rateBase").val();
    bt.loading.show();
    $.post(basePath + "/cms/provide/cmsprovideinterest/calInterest", params, function (data) {
        var html = "";
        if(!repayBeginDate){
            params.beginDate= $("#repayMinDate").val();
        }
        deleteUnRepay();//删除未还
        var trsNum = $("#repayPlanTB tbody tr").length;
        $.each(data, function (index, item) {
            html += createTr(index+trsNum, item, params.beginDate, params.endDate, params.money, false);
            rowNum = index;
        });
        $("#repayPlanTB tbody").append(html);
        // 重新初始化组件
        reInit();
        // 计算合计金额
        calSum();
    }, "JSON");
}



/**
 * 构建表格行
 * @param rowNum 行号
 * @param item 行数据对象
 * @returns {string} 返回行数据
 */
function createTr(rowNum, item, beginDate, endDate, money,operate) {
    var html = "";
    var prediceRepayDateIsEdit = item.prediceRepayDateIsEdit ? "" : "disabled";
    var payMoney=item.paymentMoneyIsEdit ? "" : 0;
    var paymentMoneyIsEdit = item.paymentMoneyIsEdit ? "" : "disabled";
    var payIst=item.inerestIsEdit ? "":  0;
    var inerestIsEdit = item.inerestIsEdit ? "" : "disabled";
    item.isRepay=isNull(item.isRepay) ? 0:  item.isRepay;
    if(item.isRepay=="1"){//已还则不能操作
        prediceRepayDateIsEdit = "disabled";
        paymentMoneyIsEdit = "disabled";
        inerestIsEdit = "disabled";
        operate = false;
        unRepayMoney=moneyFun.sub(unRepayMoney,item.repaymentMoney);
    }
    html += '<tr data-rownum="' + rowNum + '">';
    html += '<td><div><input ' + prediceRepayDateIsEdit + ' value="' + (isNotEmpty(item.prediceRepayDate) ? item.prediceRepayDate : '') + '" name="repayMents[' + rowNum + '].prediceRepayDate" data-biz-name="prediceRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" /></div></td>';
    html += '<td><input ' + paymentMoneyIsEdit + ' value="' + (isNotEmpty(item.repaymentMoney) ? item.repaymentMoney : payMoney) + '" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" data-id="repaymentMoney_' + rowNum + '" data-biz-name="repaymentMoney_' + rowNum + '" data-min="0" ' + (item.paymentMoneyIsEdit ? 'lay-verify="required"' : '') + ' data-max="' + money + '" data-name="repayMents[' + rowNum + '].repaymentMoney" data-callback="calSum"/></td>';
    html += '<td><input ' + inerestIsEdit + ' value="' + (isNotEmpty(item.repaymentIst) ? item.repaymentIst : payIst) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="repaymentIst_' + rowNum + '" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" data-min="0" ' + (inerestIsEdit ? 'lay-verify="required"' : '') + ' data-name="repayMents[' + rowNum + '].repaymentIst" data-callback="calSum"/></td>';
    html += '<td><input disabled value="' + (isNotEmpty(item.currentRepayMoney) ? item.currentRepayMoney : '') + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="currentRepayMoney_' + rowNum + '" data-biz-name="currentRepayMoney_' + rowNum + '" type="text" data-decimal="2" data-min="0" data-name="repayMents[' + rowNum + '].currentRepayMoney" data-callback="calSum"/></td>';
    html += '<td><input disabled value="' + (isNotEmpty(item.surplusMoney) ? item.surplusMoney : '') + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="surplusMoney_' + rowNum + '" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" readonly="true" data-min="0" data-name="repayMents[' + rowNum + '].surplusMoney"/></td>';
    html += '<td name="isRepay">' + bt.dict('yn_state', item.isRepay) + '<input readonly="true" name="repayMents[' + rowNum + '].isRepay" id="isRepay_'+ rowNum +'" value="'+item.isRepay+'"  type="hidden"/></td>';
    html += '<td><input name="repayMents[' + rowNum + '].remark" id="remark" class="layui-input" type="text"/></td>';
    if (operate) {
        html += '<td><button class="layui-btn layui-btn-small" type="button" onclick="delRow(' + rowNum + ')">删除</button></td>';
    } else {
        html += '<td></td>';
    }
    html += '</tr>';
    return html;
}


/**
 * 重新初始化组件
 */
function reInit() {
    dateType.render();
    moneyType.render();
    bt.loading.hide();
}

/**
 * 删除未还行
 * @param rowNum
 */
function deleteUnRepay() {
    var trs = $("#repayPlanTB tbody tr");
    $.each(trs, function (index, item) {
        var isRepay = $(item).find("input[id^='isRepay_']").val();
        if(isRepay!="1"){
            $(item).remove();
        }
    })
    // 重新计算合计
    calSum()
}

/**
 * 删除行
 * @param rowNum
 */
function delRow(rowNum) {
    $("tr[data-rownum='" + rowNum + "']").remove();
    // 重新计算合计
    calSum()
}
/**
 * 合计金额计算
 */
function calSum() {
    var trs = $("#repayPlanTB tbody tr");
    var sendMoney=moneyFun.toNumber($("#loanMoney").val());
    if (trs.length == 0) {
        moneyType.get("repaymentMoneySum").val("");
        moneyType.get("repaymentIstSum").val("");
        moneyType.get("currentRepayMoneySum").val("");
        return;
    }
    var sumRepaymentMoney = 0;
    var sumIst = 0;
    var sumCurrentRepayMoney = 0;
    var x;
    var y;
    var z;
    var rowNum;
    $.each(trs, function (index, item) {
        var $tr = $(item);
        rowNum = $tr.attr("data-rownum");
        x = getRowMoneyValue($tr, rowNum, "repaymentMoney");
        y = getRowMoneyValue($tr, rowNum, "repaymentIst");
        var repaymentMoney = moneyFun.toNumber(x);
        var repaymentIst = moneyFun.toNumber(y);
        z = moneyFun.add(repaymentMoney, repaymentIst);
        var currentRepayMoney = getRowMoneyValue($tr, rowNum, "currentRepayMoney");
        if (currentRepayMoney) {
            z = moneyFun.toNumber(currentRepayMoney);
        }else {
            setRowMoneyValue($tr, rowNum, "currentRepayMoney", z);
        }
        if (x) {
            sumRepaymentMoney = moneyFun.add(sumRepaymentMoney, repaymentMoney);
        }
        if (y) {
            sumIst = moneyFun.add(sumIst, repaymentIst);
        }
        if (z) {
            sumCurrentRepayMoney = moneyFun.add(sumCurrentRepayMoney, z);
        }
        setRowMoneyValue($tr, rowNum, "currentRepayMoney", z);
        setRowMoneyValue($tr, rowNum, "surplusMoney", moneyFun.sub(sendMoney, sumRepaymentMoney));
        moneyType.get("repaymentMoneySum").val(moneyFun.format(sumRepaymentMoney,2));
        moneyType.get("repaymentIstSum").val(moneyFun.format(sumIst,2));
        moneyType.get("currentRepayMoneySum").val(moneyFun.format(sumCurrentRepayMoney,2));
    });
}

function getRowMoneyValue($tr, rowNum, bizName) {
    return $tr.find("[data-biz-name='" + bizName + "_" + rowNum + "']").val();
}

function setRowMoneyValue($tr, rowNum, bizName, value) {
    var formattedValue = moneyFun.format(value, 2);
    $tr.find("[data-biz-name='" + bizName + "_" + rowNum + "']").val(formattedValue);
}


/**
 * 判断为空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value === "" || value === " " || value == undefined || value == null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判断非空
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}



/**
 * 新增行
 * @param capitalEdit
 * @param interestEdit
 */
function addRow(capitalEdit,interestEdit) {
    var trs = $("#repayPlanTB tbody tr");
    rowNum = trs.length > 0 ? Number($(trs[trs.length - 1]).attr("data-rowNum")) + 1 : 0;
    var item = {};
    item.prediceRepayDateIsEdit = true;
    item.paymentMoneyIsEdit = capitalEdit;
    item.inerestIsEdit = interestEdit;
    var beginDate = "";
    if ($("#repayBeginDate").length > 0) {
        beginDate = $("#repayBeginDate").val();
    } else {
        beginDate = $("#repayMinDate").val();
    }
    var endDate = $("#repayDate").val();
    var money = unRepayMoney;
    $("#repayPlanTB tbody").append(createTr(rowNum, item, beginDate, endDate, money, true));
    // 重新初始化组件
    reInit();
}

/**
 * 还款方式选择回调
 * @param el
 * @param value
 * @param data
 */
function repayTypeCallback(data, el) {
    // 利息与本金无关
    if (data.capitalandinterest == "3") {
        // 本金时间周期/次数
        if (data.capitalfrequencytype == "D" || data.capitalfrequencytype=="C") {
            $("#calBtn").removeClass("layui-disabled");
            $("#calBtn").removeAttr("disabled", "disabled");

            if (data.interestfrequencytype == "P") {//利息有计划
                $("#addRowBtn").removeClass("layui-disabled");
                $("#addRowBtn").removeAttr("disabled", "disabled");
                $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" type=\"button\" id=\"addRowBtn\" onclick=\"addRow(false,true)\">"+bt.lang("common.button.add")+"</button>")

                $("#importBtn").removeAttr("disabled")
            } else {
                $("#addRowBtn").addClass("layui-disabled");
                $("#addRowBtn").attr("disabled", "disabled");
                $("#importBtn").attr("disabled", "disabled");
            }
        }else{
            $("#calBtn").addClass("layui-disabled");
            $("#calBtn").attr("disabled", "disabled");
            dateType.get("repayBeginDate").attr("data-disabled",true);
            // 不规律/随借随还
            if (data.capitalfrequencytype == "N" && data.interestfrequencytype == "N") {
                // 本金利息均为随借随还
                $("#addRowBtn").addClass("layui-disabled");
                $("#addRowBtn").attr("disabled", "disabled");
            } else {
                $("#addRowBtn").removeClass("layui-disabled");
                $("#addRowBtn").removeAttr("disabled", "disabled");
                if (data.capitalfrequencytype == "P" && data.interestfrequencytype != "P") {
                    $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(true,false)\">"+bt.lang("common.button.add")+"</button>")
                }else if (data.capitalfrequencytype != "P" && data.interestfrequencytype == "P") {
                    $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(false,true)\">"+bt.lang("common.button.add")+"</button>")
                }else{
                    $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(true,true)\">"+bt.lang("common.button.add")+"</button>")
                }
                $("#importBtn").removeAttr("disabled")
            }
        }
    }else if (data.capitalandinterest == "2") {
        // 等额本息
        $("#addRowBtn").addClass("layui-disabled");
        $("#addRowBtn").attr("disabled", "disabled");
        $("#calBtn").removeClass("layui-disabled");
        $("#calBtn").removeAttr("disabled", "disabled");
        $("#addRowBtn").attr("disabled", "disabled");
    } else {
        // 按剩余本金
        $("#calBtn").removeClass("layui-disabled");
        $("#calBtn").removeAttr("disabled", "disabled");
        if (data.capitalfrequencytype == "P") {
            $("#addRowBtn").removeClass("layui-disabled");
            $("#addRowBtn").removeAttr("disabled", "disabled");

            $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(true,false)\">" + bt.lang("common.button.add") + "</button>");
            $("#importBtn").removeAttr("disabled");
        }
    }
    deleteUnRepay();//去掉未还数据
    // 计算合计数据
    calSum()
}
