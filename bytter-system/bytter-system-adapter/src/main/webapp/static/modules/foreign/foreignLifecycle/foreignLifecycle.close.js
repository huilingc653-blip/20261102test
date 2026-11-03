var moneyFun,moneyType,selectType,rateType,dateType;
bt.use(['form', 'rateType', 'moneyType','dateType', 'selectType', 'selectTable','layer'], function () {
  var $ = bt.jquery, form = bt.form();
  var layer = bt.layer;
  var tip_index9 = 0;
  $('#direction-desc').mouseenter(function () {
    tip_index9 = layer.tips(bt.lang('指原合约的方向'), '#direction-desc', {time: 30000});
  }).mouseleave(function () {
    layer.close(tip_index9);
  });

  //交易地点下拉表格
  bt.selectTable.init({
    elem: "#tranAddrTable",
    url: basePath + "/basic/business/syscountrycust/listAllSysCountryArea",
    searchInput: "#tranAddr",
    scrollX: "900px",
    scrollY: "300px",
    columns: [
      {data: "code",},
      {data: "nameZh",},
      {data: "nameEn",},
    ],
    clickCallback: function (data, $tr) {
      $("#tranAddrId").val(data.id);
      $("input[name='tranAddr']").val(data.nameZh);
    },
  });

  var curId = $("#settleCurId").val() || $("#buyCurId").val();
  var bankAccTable = bt.selectTable.init({
    elem: "#bankAccTable",
    url: basePath+"/foreign/fextranreq/getBoodBankAcc",
    urldata: {curId: curId, corpId: $('#corpId').val(),bankTypeId: $('#corpBank').val()},
    scrollX: true,
    scrollY:"200px",
    searchInput:"#bankAcc",
    paging:true,
    columns: [
      {data:"bankAcc",},
      {data:"accName",},
      {data:"bankName",},
      {data:"curName",}
    ],
    clickCallback:function(data, row, inputElem){
      if(data){
        inputElem.val(data.bankAcc);
        $('#bankAcc').val(data.bankAcc);
      }else{
        $('input.text').val('');
      }
    }
  });


  // 第一行操作金额回调
  bt.firstMoneyCallback = function(data) {
      var firstMoney = data;
      $("#opMoney").val(firstMoney);
      var busDirection = $("#busDirection").val();
      var contractRate = $("#contractRate").val();
      var secondMoney = 0;
      if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
        secondMoney = moneyFun.mul(firstMoney,contractRate);
      } else {
        secondMoney = moneyFun.div(firstMoney,contractRate,2);
      }
      if ("1" == $("#tranType").val()) {
        if ("1" == busDirection) {
          moneyType.get("sellMoney").val(secondMoney);
        } else {
          moneyType.get("buyMoney").val(secondMoney);
        }
      } else {
        var updownDirection = $("#updownDirection").val();//涨跌方向
        if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
          moneyType.get("sellMoney").val(secondMoney);
        } else {
          moneyType.get("buyMoney").val(secondMoney);
        }
      }


      calculateBal(firstMoney);

    // 计算结算金额
    if("1" == $("#tranType").val()) { // 期权不需要计算结算币种
      calculateSettleMoney();
    }
  }
  // 第二行操作金额回调
  bt.secondMoneyCallback = function(data) {
    var secondMoney = data;
    var busDirection = $("#busDirection").val();
    var contractRate = $("#contractRate").val();
    var firstMoney = 0;
    if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
      firstMoney = moneyFun.div(secondMoney,contractRate,6);
    } else {
      firstMoney = moneyFun.mul(secondMoney,contractRate);
    }
    $("#opMoney").val(firstMoney);
    if ("1" == $("#tranType").val()) {
        if ("1" == busDirection) {
          moneyType.get("buyMoney").val(firstMoney);
        } else {
          moneyType.get("sellMoney").val(firstMoney);
        }
    } else {
      var updownDirection = $("#updownDirection").val();//涨跌方向
      if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
          moneyType.get("buyMoney").val(firstMoney);
        } else {
          moneyType.get("sellMoney").val(firstMoney);
        }
    }

    calculateBal(firstMoney);

    // 计算结算金额
    if("1" == $("#tranType").val()) { // 期权不需要计算结算币种
      calculateSettleMoney();
    }
  }

  // 结算币种回调
  bt.settleCurCallback = function (data) {
    var settleCur = selectType.get("curId").val();
    var profitLossCur = $("#profitLossCur").val();
    if (profitLossCur && profitLossCur == settleCur) {
      moneyType.get("recordRate").attr("lay-verify","");
      $(".recordRateTd").hide();
    } else {
      moneyType.get("recordRate").attr("lay-verify","required");
      $(".recordRateTd").show();
    }

    if (settleCur == $("#buyCurId").val()) {
      $('#bankAcc').val($("#corpBankAcc").val());
    } else {
      $('#bankAcc').val($("#srcBankAcc").val());
    }
    // 计算结算金额
    if("1" == $("#tranType").val()) { // 期权不需要计算结算币种
      calculateSettleMoney();
    }

    bankAccTable.get("bankAcc").reload({curId: settleCur, corpId: $('#corpId').val(),bankTypeId: $('#corpBank').val()});
  }

  // 结算金额回调
  bt.settleMoneyCallback = function (data) {
    var settleCur = selectType.get("curId").val();
    calculateFexLifeCycleProfitLoss(settleCur);
  }



  // 平仓汇率回调
  bt.closeRateCallback = function (data) {
    // 计算结算金额
    if("1" == $("#tranType").val()) { // 期权不需要计算结算币种
      calculateSettleMoney();
    }
  }
  // 操作日记账汇率回调
  bt.recordRateCallback = function () {
    var settleCur = selectType.get("curId").val();
    calculateFexLifeCycleProfitLoss(settleCur);
  }

  // 平盘损益回调
  bt.profitLossMoneyCallback = function () {
    calculateFexLifeCycleConvertLossMoney();
  }

  // 折本位币汇率回调
  bt.convertRateCallback = function () {
    calculateFexLifeCycleConvertLossMoney();
  }

  // 结算日期回调
  bt.settleDateCallback = function(){
    checkDate("settleDate");
  }

  dateType = bt.dateType.init();
  rateType = bt.rateType.init();
  selectType = bt.selectType.init();
  moneyType = bt.moneyType.init();
  moneyFun = bt.moneyType.getFun();

  initAmountDetailValidation();
  init();



  //监听提交
  form.on('submit(submit)', function (data) {
    bt.loading.show();
    //$("#status").val($(this).attr("value"));
    data.field.lifeStatus = $(this).attr("value");

    var submitData = data.field;
    delete submitData.currencyPairName;
    delete submitData.quotationCurId;
    delete submitData.quotationMethod;
    delete submitData.quotationReferCurId
    delete submitData.tranCurPairBaseId;
    delete submitData.tranCurPairReferId;
    delete submitData.profitLossCurCode;
    var action = "/foreign/fexTradingLifecycle/updateLifecycle";
    $.ajax({
      "url": basePath + action,
      data: JSON.stringify(submitData),
      type:"post",
      dataType:"json",
      contentType: "application/json",
      async: false,
      success:function (result) {
        bt.loading.hide();
        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
          parent.layer.closeAll();
          if (result.success) {
            if (bt.navtab) {
              bt.navtab.closeCurrent();
            } else {
              parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
          }
        })
      }
    });
    return false;
  });

});

function init() {
  initAmountDetailCurrency();
  var firstMoney = $("#firstMoney").val();
  calculateBal(firstMoney);
}

// 交易金额明细币种按交易货币对固定展示：第一行左币，第二行右币
function initAmountDetailCurrency() {
  if (!isForwardTransaction()) {
    return;
  }
  var currencyPair = $("#tranCurPairIdName").val() || $("input[name='currencyPairName']").val();
  if (!currencyPair || currencyPair.indexOf("/") < 0) {
    return;
  }
  var currencies = currencyPair.split("/");
  if (currencies.length < 2) {
    return;
  }
  $("#firstMoney").closest("tr").children("td").first().text($.trim(currencies[0]));
  $("#secondMoney").closest("tr").children("td").first().text($.trim(currencies[1]));
}

// 远期、期权交易操作金额必填且必须大于0，余额由操作金额自动计算
function initAmountDetailValidation() {
  if (!isForwardTransaction() && !isOptionTransaction()) {
    return;
  }
  $("#firstMoney, #secondMoney").attr({"lay-verify": "required", "data-min": "0.01"});
  $("#firstBal, #secondBal").attr("lay-verify", "required");
}

function isForwardTransaction() {
  return "1" == $("#tranType").val() && "1" == $("#fexTranType").val();
}

function isOptionTransaction() {
  return "2" == $("#tranType").val();
}



function calculateBal(firstMoney){
  var positionMoney = $("#positionMoney").val();
  var busDirection = $("#busDirection").val();
  var firstBal = moneyFun.sub(positionMoney,firstMoney);
  var contractRate = $("#contractRate").val();
  var secondBal = 0;
  if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
    secondBal = moneyFun.mul(firstBal,contractRate);
  } else {
    secondBal = moneyFun.div(firstBal,contractRate,6);
  }
  $("#firstBal").val(firstBal);
  $("#secondBal").val(secondBal);
  if ("1" == $("#tranType").val()) {
    if ("1" == busDirection) {
      moneyType.get("buyBal").val(firstBal);
      moneyType.get("sellBal").val(secondBal);
    } else {
      moneyType.get("buyBal").val(secondBal);
      moneyType.get("sellBal").val(firstBal);
    }
  } else {
    var updownDirection = $("#updownDirection").val();//涨跌方向
    if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
      moneyType.get("buyBal").val(firstBal);
      moneyType.get("sellBal").val(secondBal);
    } else {
      moneyType.get("buyBal").val(secondBal);
      moneyType.get("sellBal").val(firstBal);
    }
  }


}

// 计算生命周期损益
function calculateFexLifeCycleProfitLoss(settleCur){
  var profitLossCur = $("#profitLossCur").val();
  var settleMoney = $("#settleMoney").val();
  var recordRate = $("#recordRate").val();
  var quotationCurId = $("#quotationCurId").val();
  var quotationReferCurId = $("#quotationReferCurId").val();
  var calculateForm = {
    profitLossCur: profitLossCur,
    settleMoney: settleMoney,
    recordRate: recordRate,
    quotationCurId: quotationCurId,
    quotationReferCurId:quotationReferCurId,
    settleCur: settleCur,
    corpId: $("#corpId").val()
  }
  if ("Y" == $("#isShowConvert").val()) {
    calculateForm.convertRate = moneyType.get('convertRate').val()
  }
  $.ajax({
    "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleProfitLoss",
    data: JSON.stringify(calculateForm),
    type:"post",
    dataType:"json",
    contentType: "application/json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
        if ("Y" == $("#isShowConvert").val()) {
          moneyType.get('convertLossMoney').val(result.obj.convertLossMoney);
        }

      }
    }
  });
}

// 计算结算金额
function calculateSettleMoney(){
  var tranCurPairBaseId = $("#tranCurPairBaseId").val();
  var closeRate = moneyType.get("closeRate").val();
  var settleCur = selectType.get("curId").val();
  if (!closeRate) {
    return
  }
  $.ajax({
    "url": basePath + "/foreign/fexTradingLifecycle/calculateSettleMoney",
    data:{
      tranCurPairIdName: $("#tranCurPairIdName").val(),
      tranCurPairBaseId: tranCurPairBaseId,
      quotationMethod:$("#quotationMethod").val(),
      buyMoney: moneyType.get("buyMoney").val(),
      sellMoney: moneyType.get("sellMoney").val(),
      buyCurId: $("#buyCurId").val(),
      sellCurId: $("#sellCurId").val(),
      contractRate: $("#contractRate").val(),
      rate: closeRate,
      settleCurId: settleCur,
      busDirection:$("#busDirection").val()
    },
    type:"post",
    dataType:"json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get("money").val(result.obj);
        // 计算损益
        calculateFexLifeCycleProfitLoss(settleCur);
      }
    }
  });
}


// 计算折本位币生命周期损益
function calculateFexLifeCycleConvertLossMoney(calculateData){
  if ("Y" != $("#isShowConvert").val()) {
    return;
  }
  var calculateForm = {
    profitLossCur: $("#profitLossCur").val(),
    convertCurType: $("#convertCurType").val(),
    profitLossMoney: moneyType.get('profitLossMoney').val(),
    convertRate: moneyType.get('convertRate').val(),
    corpId: $("#corpId").val()
  }

  $.ajax({
    "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleConvertLossMoney",
    data: JSON.stringify(calculateForm),
    type:"post",
    dataType:"json",
    contentType: "application/json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get('convertLossMoney').val(result.obj);
      }
    }
  });
}

/**
 * 查询远期汇率
 */
function getRateForEnd(){
  var calculateForm = {
    curId: $("#buyCurId").val(),
    referCurId: $("#sellCurId").val(),
    buyCurId: $("#buyCurId").val(),
    quotationCurId: $("#quotationCurId").val(),
    rateDate: dateType.get('settleDate').val()
  }
  $.ajax({
    "url": basePath + "/foreign/fexrateinfo/getRateForEnd",
    data: JSON.stringify(calculateForm),
    type:"post",
    dataType:"json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get('closeRate').val(result.obj.referenceRate);
      }
    }
  });
}
