var moneyType,moneyFun,selectType,bankAccTable;
bt.use(['form', 'rateType', 'moneyType','dateType','selectTable','selectType'], function () {
  var $ = bt.jquery, form = bt.form();
  moneyFun = bt.moneyType.getFun();
  bt.rateType.init();
  bt.dateType.init();

  // 第一行操作金额回调
  bt.firstMoneyCallback = function(data) {
    var firstMoney = data;
    var busDirection = $("#busDirection").val();//买卖方向
    var updownDirection = $("#updownDirection").val();//涨跌方向
    var contractRate = $("#contractRate").val();//执行汇率
    var secondMoney = 0;
    $("#opMoney").val(firstMoney);
    //如果货币对相等
    if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
      secondMoney = moneyFun.mul(firstMoney,contractRate);
    } else {
      secondMoney = moneyFun.div(firstMoney,contractRate,2);
    }

    if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
      moneyType.get("sellMoney").val(secondMoney);
    } else {
      moneyType.get("buyMoney").val(secondMoney);
    }
    calculateBal(firstMoney);

    // 结算损益
    calculateFexProfitLoss();
   // moneyType.render();
  }
  // 第二行操作金额回调
  bt.secondMoneyCallback = function(data) {
    var secondMoney = data;
    var busDirection = $("#busDirection").val();
    var updownDirection = $("#updownDirection").val();//涨跌方向
    var contractRate = $("#contractRate").val();
    var firstMoney = 0;
    if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
      firstMoney = moneyFun.div(secondMoney,contractRate,6);
    } else {
      firstMoney = moneyFun.mul(secondMoney,contractRate);
    }
    $("#opMoney").val(firstMoney);
    if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
      moneyType.get("buyMoney").val(firstMoney);
    } else {
      moneyType.get("sellMoney").val(firstMoney);
    }
    calculateBal(firstMoney);
    // 结算损益
    calculateFexProfitLoss();
    // moneyType.render();
  }

  bt.spotRateCallback = function () {
    // 结算损益
    calculateFexProfitLoss();
  }

  // 结算金额回调
  bt.settleMoneyCallback = function () {
    // 计算损益
    calculateFexProfitLossDeliveryType1();
  }

  // 结算币种回调
  bt.settleCurCallback = function (data) {
    bankAccTable.get("bankAcc").reload({curId: data.id, corpId: $('#corpId').val(),bankTypeId: $('#corpBank').val()});
    calculateFexProfitLoss();
  }
  // 操作日记账汇率回调
  bt.recordRateCallback = function () {
    calculateFexProfitLoss();
  }

  // 损益金额回调
  bt.profitLossMoneyCallback = function () {
    calculateFexLifeCycleConvertLossMoney();
  }

  // 折本位币汇率回调
  bt.convertRateCallback = function () {
    calculateFexLifeCycleConvertLossMoney();
  }

  moneyType = bt.moneyType.init();
  selectType = bt.selectType.init();

  // if ("1" == busDirection) {
  //   moneyType.get("sellMoney").val(secondMoney);
  // } else {
  //   moneyType.get("buyMoney").val(secondMoney);
  // }
  //calculateBal(firstMoney);

  if ("1" == $("#deliveryType").val()) {
    var curId = $("#settleCurId").val() || $("#buyCurId").val();
    //结算账号
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
  } else {
    //收款账号
    var payeeAccTable = bt.selectTable.init({
      elem: "#payeeAccBankAccTable",
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
          //$('#curId').val(data.curId);
        }else{
          $('input.text').val('');
        }
      }
    });
    //付款账号
    var payerAccTable = bt.selectTable.init({
      elem: "#payerAccBankAccTable",
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
          //$('#curId').val(data.curId);
        }else{
          $('input.text').val('');
        }
      }
    });
  }




  //监听提交
  form.on('submit(submit)', function (data) {
    bt.loading.show();
    $("#status").val($(this).attr("value"));
    data.field.lifeStatus = $(this).attr("value");

    var submitData = data.field;
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
  bt.profitLossCurCallback = function (data){
    showOrHideBaseCur()
  }
})

function calculateFexProfitLoss() {
  if("1" == $("#deliveryType").val()) {
    calculateSettleMoney();
  } else {
    calculateFexProfitLossDeliveryType();
  }
}

/**
 * 计算损益(无本金交割)
 */
function calculateFexProfitLossDeliveryType1(){
  var profitLossCur = selectType.get("profitLossCur").val();
  var settleMoney = $("#settleMoney").val();
  var recordRate = moneyType.get('recordRate').val();
  var quotationCurId = $("#quotationCurId").val();
  var quotationReferCurId = $("#quotationReferCurId").val();
  var settleCurId = selectType.get("curId").val();
  if (!recordRate) {
    return;
  }
  var calculateForm = {
    profitLossCur: profitLossCur,
    settleMoney: settleMoney,
    recordRate: recordRate,
    quotationCurId: quotationCurId,
    quotationReferCurId:quotationReferCurId,
    settleCur: settleCurId,
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
 * 计算损益(本金交割)
 */
function calculateFexProfitLossDeliveryType(){
  var busDirection = $("#busDirection").val();
  var buyCurId = $("#buyCurId").val();
  var spotRate = moneyType.get("spotRate").val();
  var contractRate = $("#contractRate").val();
  var profitLossCur = selectType.get("profitLossCur").val();
  var convertCurType = $("#convertCurType").val();
  var isShowConvert = null;
  if (convertCurType) {
    isShowConvert = "Y";
  }
  if (!spotRate) {
    return;
  }
  var calculateData = {
    tranCurPairBaseId: $("#tranCurPairBaseId").val(),tranCurPairReferId:$("#tranCurPairReferId").val(),
    quotationCurId:$("#quotationCurId").val(),quotationReferCurId:$("#quotationReferCurId").val(),
    contractRate:contractRate,spotRate:spotRate,profitLossCur:profitLossCur,corpId:$("#corpId").val(),
    buyCurId:buyCurId,buyMoney:moneyType.get("buyMoney").val(),sellMoney:moneyType.get("sellMoney").val(),
    busDirection:busDirection,isShowConvert:isShowConvert,tranType: "2",updownDirection: $("#updownDirection").val()
  }

  $.ajax({
    "url": basePath + "/foreign/fexProfit/lossManage/calculateFexProfitLoss",
    data: JSON.stringify(calculateData),
    type:"post",
    dataType:"json",
    contentType: "application/json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
        if ("Y" == calculateData.isShowConvert) {
          moneyType.get('convertLossMoney').val(result.obj.convertLossMoney);
        }
      }
    }
  });
}



// 计算结算金额
function calculateSettleMoney(){
  var spotRate = moneyType.get("spotRate").val();
  var settleCurId = selectType.get("curId").val();
  if (!spotRate || !settleCurId) {
    return
  }
  $.ajax({
    "url": basePath + "/foreign/fexTradingLifecycle/calculateSettleMoney",
    data:{
      tranCurPairIdName: $("#tranCurPairIdName").val(),
      tranCurPairBaseId: $("#tranCurPairBaseId").val(),
      quotationMethod:$("#quotationMethod").val(),
      buyMoney: moneyType.get("buyMoney").val(),
      sellMoney: moneyType.get("sellMoney").val(),
      buyCurId: $("#buyCurId").val(),
      sellCurId: $("#sellCurId").val(),
      contractRate: $("#contractRate").val(),
      rate: spotRate,
      settleCurId: settleCurId,
      busDirection:$("#busDirection").val(),
      tranType: "2",
      updownDirection: $("#updownDirection").val()
    },
    type:"post",
    dataType:"json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get("money").val(result.obj);
        // 计算损益
        calculateFexProfitLossDeliveryType1();
      }
    }
  });
}

/**
 * 折本位币显示隐藏
 */
function showOrHideBaseCur(){
  //获取单位本位币
  var corpCurId = $("#corpCurId").val();
  //获取损益币种
  var closeLossCurId = selectType.get('closeLossCurId').val();
  //如果单位的本位币不等于损益币种时，显示；否则，隐藏
  if (corpCurId!=closeLossCurId){
    moneyType.get('baseCurMoney').removeClass('layui-hide');
  }else{
    moneyType.get('baseCurMoney').addClass('layui-hide');
  }
  //更新默认值
  getBaseConvertInfo()
}
/**
 * 获取折本位币报价方式和折本位币汇率
 */
function getBaseConvertInfo(){
  //获取单位本位币
  var corpCurId = $("#corpCurId").val();
  //获取损益币种
  var closeLossCurId = selectType.get('closeLossCurId').val();
  $.ajax({
    "url": basePath + "/foreign/fexrateinfo/getBaseCurFexRate",
    data:{
      curId: corpCurId,
      referCurId: closeLossCurId
    },
    type:"post",
    dataType:"json",
    async: false,
    success:function (data) {
      if (data.success) {
        if (data.obj){
          $("#convertCurId").val(data.obj.cur);
          $("#convertCurReferId").val(data.obj.referCur);
          $("#convertCurType").val(data.obj.curEnCode+'/'+data.obj.referCurEnCode);
          //如果期权费币种=单位本位币，取买入价否则取卖出价
          if (corpCurId == premiumCur) {
            bt.moneyType.get("convertRate").val(data.obj.purchaseRate);
          }else{
            bt.moneyType.get("convertRate").val(data.obj.sellRate);
          }
        }
      }
    }
  });
}

function calculateBal(firstMoney){
  var positionMoney = $("#positionMoney").val();
  var busDirection = $("#busDirection").val();
  var updownDirection = $("#updownDirection").val();//涨跌方向
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
  if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
    moneyType.get("buyBal").val(firstBal);
    moneyType.get("sellBal").val(secondBal);
  } else {
    moneyType.get("buyBal").val(secondBal);
    moneyType.get("sellBal").val(firstBal);
  }

}


// 计算折本位币生命周期损益
function calculateFexLifeCycleConvertLossMoney(){
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