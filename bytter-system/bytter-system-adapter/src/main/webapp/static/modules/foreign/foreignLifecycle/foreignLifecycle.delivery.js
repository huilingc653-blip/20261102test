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
  var payeeAccTable,payerAccTable,bankAccTable;
  if ("0" == $("#deliveryType").val()) {
    //收款账户信息
    payeeAccTable = bt.selectTable.init({
      elem: "#payeeAccTable",
      url: basePath+"/foreign/fextranreq/getBoodBankAcc",
      urldata: {curId: $("#buyCurId").val(), corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
      scrollX: true,
      scrollY:"200px",
      searchInput:"#payeeAcc",
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
          $('#payeeAcc').val(data.bankAcc);
        }else{
          $('input.text').val('');
        }
      }
    });


    payerAccTable = bt.selectTable.init({
      elem: "#payerAccTable",
      url: basePath+"/foreign/fextranreq/getBoodBankAcc",
      urldata: {curId: $("#sellCurId").val(), corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
      scrollX: true,
      scrollY:"200px",
      searchInput:"#payerAcc",
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
          $('#payerAcc').val(data.bankAcc);
        }else{
          $('input.text').val('');
        }
      }
    });
  } else {
    var curId = $("#settleCurId").val() || $("#buyCurId").val();
    bankAccTable = bt.selectTable.init({
      elem: "#bankAccTable",
      url: basePath+"/foreign/fextranreq/getBoodBankAcc",
      urldata: {curId: curId, corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
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
  }


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

    if ("1" == busDirection) {
      moneyType.get("sellMoney").val(secondMoney);
    } else {
      moneyType.get("buyMoney").val(secondMoney);
    }

    calculateBal(firstMoney);
    // 本金到期交割
    if ("0" == $("#deliveryType").val()) {
      calculateFexProfitLoss()
    } else {
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
    if ("1" == busDirection) {
      moneyType.get("buyMoney").val(firstMoney);
    } else {
      moneyType.get("sellMoney").val(firstMoney);
    }
    calculateBal(firstMoney);
    // 本金到期交割
    if ("0" == $("#deliveryType").val()) {
      calculateFexProfitLoss()
    } else {
      calculateSettleMoney();
    }
  }

  // 即期汇率
  bt.spotRateCallback = function (data) {
    if ("0" == $("#deliveryType").val()) {
      calculateFexProfitLoss();
    } else {
      calculateSettleMoney();
    }
  }

  // 结算金额回调
  bt.settleMoneyCallback = function () {
    var settleCur = selectType.get("curId").val();
    // 计算损益
    calculateFexLifeCycleProfitLoss(settleCur);
  }


  // 结算币种回调
  bt.settleCurCallback = function (data) {
    var settleCur = selectType.get("curId").val();
    var profitLossCur = selectType.get("profitLossCur").val();
    if (settleCur == $("#buyCurId").val()) {
      $('#bankAcc').val($("#corpBankAcc").val());
    } else {
      $('#bankAcc').val($("#srcBankAcc").val());
    }
    if (profitLossCur == settleCur) {
      $(".recordRateTr").hide();
      moneyType.get("recordRate").removeAttr("lay-verify");
    } else {
      $(".recordRateTr").show();
      moneyType.get("recordRate").attr('lay-verify', 'required');
    }
    bankAccTable.get("bankAcc").reload({curId: settleCur, corpId: $('input[name="corpId"]').val(),bankTypeId: $('#corpBank').val()});
    calculateSettleMoney();
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
    $("#status").val($(this).attr("value"));
    data.field.lifeStatus = $(this).attr("value");

    var submitData = data.field;
    delete submitData.currencyPairName;
    delete submitData.quotationCurId;
    delete submitData.quotationMethod;
    delete submitData.quotationReferCurId
    delete submitData.tranCurPairBaseId;
    delete submitData.tranCurPairReferId;
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
  var currencyPair = $("#tranCurPairIdName").val();
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

// 远期交易操作金额必填且必须大于0，余额由操作金额自动计算
function initAmountDetailValidation() {
  if (!isForwardTransaction()) {
    return;
  }
  $("#firstMoney, #secondMoney").attr({"lay-verify": "required", "data-min": "0.01"});
  $("#firstBal, #secondBal").attr("lay-verify", "required");
}

function isForwardTransaction() {
  return "1" == $("#tranType").val() && "1" == $("#fexTranType").val();
}

function calculateProfitLoss(){

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
    secondBal = moneyFun.div(firstBal,contractRate,2);
  }
  $("#firstBal").val(firstBal);
  $("#secondBal").val(secondBal);
  if ("1" == busDirection) {
    moneyType.get("buyBal").val(firstBal);
    moneyType.get("sellBal").val(secondBal);
  } else {
    moneyType.get("buyBal").val(secondBal);
    moneyType.get("sellBal").val(firstBal);
  }

}


// 计算生命周期损益
function calculateFexLifeCycleProfitLoss(settleCur){
  var profitLossCur = selectType.get("profitLossCur").val();
  var settleMoney = $("#settleMoney").val();
  var recordRate = moneyType.get("recordRate").val();
  var quotationCurId = $("#quotationCurId").val();

  var calculateForm = {
    profitLossCur: profitLossCur,
    settleMoney: settleMoney,
    recordRate: recordRate,
    quotationCurId: quotationCurId,
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

/**
 * 计算损益
 * @param data
 */
function calculateFexProfitLoss() {
  var busDirection = $("#busDirection").val();
  var buyCurId = $("#buyCurId").val();
  var spotRate = moneyType.get("spotRate").val();
  var contractRate = $("#contractRate").val();
  var profitLossCur = selectType.get("profitLossCur").val();
  // 本金交割损益的计算基数固定为操作金额第一行，opMoney 保存其未格式化值。
  var firstOperationMoney = $("#opMoney").val();
  var calculateData = {
    tranCurPairBaseId: $("#tranCurPairBaseId").val(), tranCurPairReferId: $("#tranCurPairReferId").val(),
    quotationCurId: $("#quotationCurId").val(), quotationReferCurId: $("#quotationReferCurId").val(),
    contractRate: contractRate, spotRate: spotRate, profitLossCur: profitLossCur, corpId: $("#corpId").val(),
    // 通用接口会按左侧币种从买入/卖出金额中取值，两者统一传第一行以避免取到第二行。
    buyCurId: buyCurId, buyMoney: firstOperationMoney, sellMoney: firstOperationMoney,
    busDirection: busDirection, isShowConvert: $("#isShowConvert").val(), rateTypeId: $("#rateTypeId").val()
  }
  if ("Y" == calculateData.isShowConvert) {
    calculateData.convertRate = moneyType.get("convertRate").val();
  }
  $.ajax({
    "url": basePath + "/foreign/fexProfit/lossManage/calculateFexProfitLoss",
    data: JSON.stringify(calculateData),
    type: "post",
    dataType: "json",
    contentType: "application/json",
    async: false,
    success: function (result) {
      if (result.success) {
        moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
        if ("Y" == $("#isShowConvert").val()) {
          moneyType.get('convertLossMoney').val(result.obj.convertLossMoney);
        }
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
    profitLossCur: selectType.get("profitLossCur").val(),
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


// 计算结算金额
function calculateSettleMoney(){
  var tranCurPairBaseId = $("#tranCurPairBaseId").val();
  var spotRate = moneyType.get("spotRate").val();
  var settleCur = selectType.get("curId").val();
  if (!spotRate) {
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
      rate: spotRate,
      settleCurId: selectType.get("curId").val(),
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