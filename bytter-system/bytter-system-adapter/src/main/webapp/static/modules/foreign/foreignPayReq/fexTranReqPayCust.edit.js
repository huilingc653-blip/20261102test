var dict,corp,moneyType;
var setcorpBank;
var allExternalAccCorp;
var isSeleTemplate = false;
var isBankPayType = false;
/* 交易对手关联客商是否境外：null=未按客商判定（此时按"国家是否中国"决定省市/地区），
   true/false=已按客商的境内(1)/境外(0)属性判定。境外客商的客商资料里存的是"国家+地区"，
   省/市要用 sys_region，二者不能混，页面显隐口径必须跟随客商属性。
   编辑页未重选客商时用隐藏域 #cotpIsNative（后台按客商主数据带出）。 */
var cotpIsOuterCorp = null;
bt.use(['form', 'layedit', 'dicts', 'selectType', 'moneyType', 'element', 'selectTable', 'dateType', 'corp', 'btUpload', 'region', 'allExternalBankAcc','radioType', 'rateType'], function () {
  var $ = bt.jquery, form = bt.form();
  layui.element()
  bt.rateType.init();

  // 加载模块配置
  loadTemplateConfig('','',true);
  // 注册模块选择
  $('#templateBtn').click(function () {
    parent.layer.open({
      title:'选择模版',
      type: 2,
      area: ['1000px', '700px'],
      maxmin: true,
      content: basePath + '/foreign/fexTranConfigTemplate/fexConfigTemplateChoose',
    });
  })

  var dateType = bt.dateType.init();
  //省回调函数
  bt.provCallback = function (value, othis, options) {
    $('#cotpProv').val(value);
    $('#cotpCity').val("");
  };
  // //市回调函数
  bt.cityCallback = function (value, othis, options) {
    $('#cotpCity').val(value);
  };
  region = bt.region.init();

  bt.cotpCountryCallBack = function (data) {
    setAddress(data.id, data.namezh, '');
  }

  bt.generalTradeAmountCallback = function (data) {
    setRptGateEntId();
  }

  bt.feedAmountCallback = function (data) {
    setRptGateEntId();
  }

  bt.otherTradeAmountCallback = function (data) {
    setRptGateEntId();
  }


  bt.borderTradeAmountCallback = function (data) {
    setRptGateEntId();
  }

  allExternalAccCorp = bt.allExternalBankAcc.init();//客商账号组件初始化
  bt.cotpExternalAccBeforeClick = function () {
    if(!isSeleTemplate && !$('#templateId').val() && !isBankPayType){
      allExternalAccCorp.set({
        "type": "innerBankAcc,outerBankAcc,corpBankAcc", //"type": "payPal",
      });
    }
    return true;
  }

  //交易对手银行账户
  bt.cotpExternalAccCallback = function (data, dom) {
    if (!isEmpty(data.type)) {
      // 默认中国的国家ID
      var chinaId = $('#chinaId').val();
      //客商是境外(含境外网点账户)时，"交易对手所在国家&地区"走"国家+地区"，不用省/市
      cotpIsOuterCorp = (data.type === "outerBankAcc")
          || (data.type === "corpBankAcc" && data.isDomesticBank == "0");
      if (data.type === "innerBankAcc") {
        $("#oppId").val(data.externalCorpId);
        $("#cotpName").val(data.ecName);
        $("#cotpEnName").val(data.ecNameEn);
        $("#cotpBankName").val(data.bank);
        $("#cotpBankAcc").val(data.externalAcc);
        //这个对应 交易对手银行账户ID
        // $("#cotpAccId").val(data.id);
        $("#cotpAccName").val(data.externalAccName);
        $("#cotpAccCurId").val(data.curId); //账户币种
        if (isNotEmpty(data.englishCode)) {
          $("#cotpAccCurNameDiv").text(data.englishCode);
        }
        $("#cotpAdrr").val(data.address); //交易对手地址
        /* if (isNotEmpty(data.address)) {
             $("#cotpAdrrDiv").text(data.address);
         }*/
        /*$("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
        $("#cotpProv").val(data.externalPro);
        $("#cotpCity").val(data.externalCity);*/
        selectType.get('cotpCountry').val(chinaId);
        $('#addressDiv').hide();
        $('#addrDiv').show();
        selectType.get("cotpAddress").attr({"data-value": '', "data-required": false});
        region.get('payeeAddr').set([{
              name: 'cotpProv',
              defaultValue: data.externalPro,
              required: true
            }, {
              name: 'cotpCity',
              defaultValue: data.externalCity,
              required: true
            }]
        );
        $("#cotpProvName").val(data.externalProName);
        $("#cotpCityName").val(data.externalCityName);
        /*if (isNotEmpty(data.country)) {
            $("#cotpCountryDiv").text(data.country);
        }
        if (isNotEmpty(data.externalProName)) {
            $("#cotpProvDiv").text(data.externalProName);
        }
        if (isNotEmpty(data.externalCityName)) {
            $("#cotpCityDiv").text(data.externalCityName);
        }
        $("#countryId").val(data.countryId);//交易对手银行所在国家地区&地点
        if (isNotEmpty(data.bankCountry)) {
            $("#nameZhBankDiv").text(data.bankCountry);
        }*/
        //交易对手银行所在国家地区&地点
        selectType.get('countryId').val(data.chinaId);
        $("#cotpSwiftCode").val(data.swiftCode);
        if (isNotEmpty(data.swiftCode)) {
          $("#cotpSwiftCodeDiv").text(data.swiftCode);//交易对手银行SWIFT CODE
        }
        $("#localClearNo").val(data.localClearNo);
        if (isNotEmpty(data.localClearNo)) {
          $("#localClearNoDiv").text(data.localClearNo);//本地清算号
        }
        // $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
        $("#cotpAgentBank").val(data.intermediaryBank);
        $("#cotpAgentBankAcc").val(data.intermediaryBankAcc);
        $("#cotpAgentBankAddr").val(data.intermediaryBankAddress);
        if (isNotEmpty(data.intermediaryBank)) {
          // $("#cotpAgentBankDiv").text(data.intermediaryBank);//交易对手代理行
        }
        if (isNotEmpty(data.intermediaryBankAddress)) {
          // $("#cotpAgentBankAddrDiv").text(data.intermediaryBankAddress);//交易对手代理行地址
        }
        if (isNotEmpty(data.intermediaryBankAcc)) {
          // $("#cotpAgentBankAccDiv").text(data.intermediaryBankAcc);//交易对手代理行账户
        }
        if (isNotEmpty(data.ecName)) {
          $("#srcCotpTd").text(data.ecName);
        }
        if (isNotEmpty(data.bank)) {
          $("#srcCotpBankTd").text(data.bank);
        }
        //现金流信息
        if (isNotEmpty(data.externalAcc)) {
          $("#srcCotpBankAccTd").text(data.externalAcc);
        }
        if (isNotEmpty(data.externalAccName)) {
          $("#srcCotpAccNameTd").text(data.externalAccName);
        }
      } else if (data.type === "outerBankAcc") {
        $("#oppId").val(data.externalCorpId);
        $("#cotpName").val(data.ecName);
        $("#cotpEnName").val(data.ecNameEn);
        $("#cotpBankName").val(data.bank);
        $("#cotpBankAcc").val(data.externalAcc);
        //这个对应 交易对手银行账户ID
        // $("#cotpAccId").val(data.id);
        $("#cotpAccName").val(data.externalAccName);
        $("#cotpAccCurId").val(data.curId); //账户币种
        if (isNotEmpty(data.englishCode)) {
          $("#cotpAccCurNameDiv").text(data.englishCode);
        }
        $("#cotpAdrr").val(data.address1); //交易对手地址
        /*if (isNotEmpty(data.address)) {
            $("#cotpAdrrDiv").text(data.address);
        }*/
        /*$("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
        // $("#cotpProv").val(data.outExternalPro);
        // $("#cotpCity").val(data.outExternalCity);
        // $("#cotpProvName").val(data.outExternalProName);
        // $("#cotpCityName").val(data.outExternalCityName);
        $("#cotpProv").val('');
        $("#cotpCity").val('');*/
        selectType.get('cotpCountry').val(data.countryId);
        $('#addressDiv').show();
        $('#addrDiv').hide();
        var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.countryId;
        selectType.get("cotpAddress").attr({"data-value": data.address, "data-url": url, "data-required": true});
        region.get('payeeAddr').set([{
              name: 'cotpProv',
              defaultValue: '',
              required:false
            }, {
              name: 'cotpCity',
              defaultValue: '',
              required:false
            }]
        );
        $("#cotpProvName").val('');
        $("#cotpCityName").val('');
        $("#cotpProvName").removeAttr("lay-verify");
        $("#cotpCityName").removeAttr("lay-verify");
        /*$("#cotpProvDiv").text('');
        $("#cotpCityDiv").text('');
        if (isNotEmpty(data.country)) {
            $("#cotpCountryDiv").text(data.country);
        }*/
        // if (isNotEmpty(data.outExternalProName)) {
        //     $("#cotpProvDiv").text(data.outExternalProName);
        // }
        // if (isNotEmpty(data.outExternalCityName)) {
        //     $("#cotpCityDiv").text(data.outExternalCityName);
        // }
        /*$("#countryId").val(data.bankOutCountryId);//交易对手银行所在国家地区&地点
        if (isNotEmpty(data.bankCountry)) {
            $("#nameZhBankDiv").text(data.bankCountry);
        }*/
        //交易对手银行所在国家地区&地点
        selectType.get('countryId').val(data.bankOutCountryId);
        $("#cotpSwiftCode").val(data.swiftCode);
        if (isNotEmpty(data.swiftCode)) {
          $("#cotpSwiftCodeDiv").text(data.swiftCode);//交易对手银行SWIFT CODE
        }
        $("#localClearNo").val(data.localClearNo);
        if (isNotEmpty(data.localClearNo)) {
          $("#localClearNoDiv").text(data.localClearNo);//本地清算号
        }
        $("#cotpBankAdrr").val(data.bankAddress);
        if (isNotEmpty(data.bankAddress)) {
          // $("#cotpBankAdrrDiv").text(data.bankAddress);//交易对手银行地址
        }
        $("#cotpAgentBank").val(data.intermediaryBank);
        $("#cotpAgentBankAcc").val(data.intermediaryBankAcc);
        $("#cotpAgentBankAddr").val(data.intermediaryBankAddress);
        $("#cotpAgentSwiftCode").val(data.intermediaryBankSwiftCode);
        if (isNotEmpty(data.intermediaryBank)) {
          // $("#cotpAgentBankDiv").text(data.intermediaryBank);//交易对手代理行
        }
        if (isNotEmpty(data.intermediaryBankAddress)) {
          // $("#cotpAgentBankAddrDiv").text(data.intermediaryBankAddress);//交易对手代理行地址
        }
        if (isNotEmpty(data.intermediaryBankAcc)) {
          // $("#cotpAgentBankAccDiv").text(data.intermediaryBankAcc);//交易对手代理行账户
        }
        if (isNotEmpty(data.ecName)) {
          $("#srcCotpTd").text(data.ecName);
        }
        if (isNotEmpty(data.bank)) {
          $("#srcCotpBankTd").text(data.bank);
        }
        //现金流信息
        if (isNotEmpty(data.externalAcc)) {
          $("#srcCotpBankAccTd").text(data.externalAcc);
        }
        if (isNotEmpty(data.externalAccName)) {
          $("#srcCotpAccNameTd").text(data.externalAccName);
        }
      } else if (data.type === "corpBankAcc") {
        $("#oppId").val(data.corpId);
        $("#cotpName").val(data.corpName);
        $("#cotpEnName").val(data.corpNameEn);
        $("#cotpBankName").val(data.bankName);
        $("#cotpBankAcc").val(data.bankAcc);
        //这个对应 交易对手银行账户ID
        // $("#cotpAccId").val(data.id);
        $("#cotpAccName").val(data.accName);
        $("#cotpAccCurId").val(data.curId); //账户币种
        if (isNotEmpty(data.curName)) {
          $("#cotpAccCurNameDiv").text(data.curName);
        }
        $("#cotpAdrr").val(data.addressEn); //交易对手地址
        /*if (isNotEmpty(data.addressEn)) {
            $("#cotpAdrrDiv").text(data.addressEn);
        }*/
        /*$("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
        $("#cotpProv").val('');
        $("#cotpCity").val('');
        $("#cotpProvName").val('');
        $("#cotpCityName").val('');
        $("#cotpProvDiv").text('');
        $("#cotpCityDiv").text('');
        // $("#cotpProv").val(data.bankProv);
        // $("#cotpCity").val(data.bankCity);
        // $("#cotpProvName").val(data.bankProvName);
        // $("#cotpCityName").val(data.bankCityName);
        if (isNotEmpty(data.countryName)) {
            $("#cotpCountryDiv").text(data.countryName);
        }
        // if (isNotEmpty(data.bankProvName)) {
        //     $("#cotpProvDiv").text(data.bankProvName);
        // }
        // if (isNotEmpty(data.bankCityName)) {
        //     $("#cotpCityDiv").text(data.bankCityName);
        // }*/
        /*$("#countryId").val(data.bankCountryId);//交易对手银行所在国家地区&地点
        if (isNotEmpty(data.bankCountryName)) {
            $("#nameZhBankDiv").text(data.bankCountryName);
        }*/
        selectType.get('countryId').val(chinaId);
        //境内
        if (isNotEmpty(data.isDomesticBank) && data.isDomesticBank == "1") {
          selectType.get('cotpCountry').val(chinaId);
          $('#addressDiv').hide();
          $('#addrDiv').show();
          selectType.get("cotpAddress").attr({"data-value": '', "data-required": false});
          region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: data.accProvince,
                required: true
              }, {
                name: 'cotpCity',
                defaultValue: data.accCity,
                required: true
              }]
          );

          $("#cotpProvName").val(data.accProvinceName);
          $("#cotpCityName").val(data.accCityName);
        } else if (isNotEmpty(data.isDomesticBank) && data.isDomesticBank == "0") {
          selectType.get('cotpCountry').val(data.countryId);
          $('#addressDiv').show();
          $('#addrDiv').hide();
          var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.accProvince;
          selectType.get("cotpAddress").attr({"data-value": data.accCity, "data-url": url, "data-required": true});
          region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: '',
                required: false
              }, {
                name: 'cotpCity',
                defaultValue: '',
                required: false
              }]
          );
          $("#cotpProvName").val('');
          $("#cotpCityName").val('');
          $("#cotpProvName").removeAttr("lay-verify");
          $("#cotpCityName").removeAttr("lay-verify");
          if (isNotEmpty(data.bankCode)) {
            $("#cotpSwiftCodeDiv").text(data.bankCode);//交易对手银行SWIFT CODE
            $("#cotpSwiftCode").val(data.bankCode);
          }
        }
        $("#localClearNo").val(data.localClearNo);
        if (isNotEmpty(data.localClearNo)) {
          $("#localClearNoDiv").text(data.localClearNo);//本地清算号
        }
        $("#cotpBankAdrr").val(data.bankAddress);
        if (isNotEmpty(data.bankAddress)) {
          // $("#cotpBankAdrrDiv").text(data.bankAddress);//交易对手银行地址
        }
        // if (isNotEmpty(data.intermediaryBank)) {
        //     $("#cotpAgentBankDiv").text(data.intermediaryBank);//交易对手代理行
        // }
        // if (isNotEmpty(data.intermediaryBankAddress)) {
        //     $("#cotpAgentBankAddrDiv").text(data.intermediaryBankAddress);//交易对手代理行地址
        // }
        // if (isNotEmpty(data.intermediaryBankAcc)) {
        //     $("#cotpAgentBankAccDiv").text(data.intermediaryBankAcc);//交易对手代理行账户
        // }
        if (isNotEmpty(data.corpName)) {
          $("#srcCotpTd").text(data.corpName);
        }
        if (isNotEmpty(data.bankName)) {
          $("#srcCotpBankTd").text(data.bankName);
        }
        //现金流信息
        if (isNotEmpty(data.bankAcc)) {
          $("#srcCotpBankAccTd").text(data.bankAcc);
        }
        if (isNotEmpty(data.accName)) {
          $("#srcCotpAccNameTd").text(data.accName);
        }
      }  else if (data.type === "payPal") {
        $("#oppId").val(data.id);
        $("#cotpName").val(data.ecName);
        $("#cotpAdrr").val(data.addressEn); //交易对手地址
        if (isNotEmpty(data.addressEn)) {
          $("#cotpAdrrDiv").text(data.addressEn);
        }
        // $("#cotpProv").val(data.prov);
        // $("#cotpCity").val(data.city);
        // $("#cotpProvName").val(data.provName);
        // $("#cotpCityName").val(data.cityName);
        if (isNotEmpty(data.countryName)) {
          $("#cotpCountryDiv").text(data.countryName);
        }


        selectType.get('cotpCountry').val(data.countryId); //交易对手所在国家地区&地点
        var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.countryId;
        selectType.get("cotpAddress").attr({"data-value": data.city, "data-url": url, "data-required":true});
        region.get('payeeAddr').set([{
              name: 'cotpProv',
              defaultValue: '',
              required:false
            }, {
              name: 'cotpCity',
              defaultValue: '',
              required:false
            }]
        );
        $("#cotpEnName").val(data.ecNameEn);


        // if (isNotEmpty(data.provName)) {
        //     $("#cotpProvDiv").text(data.provName);
        // }
        // if (isNotEmpty(data.cityName)) {
        //     $("#cotpCityDiv").text(data.cityName);
        // }

        $("#email").val(data.email);
        $("#isdCode").val(data.countryCode);
        $("#phoneCode").val(data.tel);

        if (isNotEmpty(data.countryCode) && isNotEmpty(data.tel)) {
          $("#mobnValue").val(data.countryCode+"-"+data.tel);
        } else {
          $("#mobnValue").val(data.tel);
        }

        if(isNotEmpty(data.email)) {
          $("#cotpBankAcc").val(data.email);
          $("#srcCotpBankAccTd").text(data.email);
        } else if(isNotEmpty(data.countryCode) && isNotEmpty(data.tel)) {
          $("#cotpBankAcc").val(data.countryCode+data.tel);
          $("#srcCotpBankAccTd").text(data.countryCode+data.tel);
        }
        if (isNotEmpty(data.ecName)) {
          $("#srcCotpTd").text(data.ecName);
        }
        $("#srcCotpBankTd").text('');
        $("#srcCotpAccNameTd").text('');
      } else if (data.type === "fps") {
        $("#oppId").val(data.id);
        $("#cotpName").val(data.ecName);
        $("#cotpAdrr").val(data.addressEn); //交易对手地址
        if (isNotEmpty(data.addressEn)) {
          $("#cotpAdrrDiv").text(data.addressEn);
        }

        if (isNotEmpty(data.countryName)) {
          $("#cotpCountryDiv").text(data.countryName);
        }

        selectType.get('cotpCountry').val(data.countryId); //交易对手所在国家地区&地点
        var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.countryId;
        selectType.get("cotpAddress").attr({"data-value": data.city, "data-url": url, "data-required":true});
        region.get('payeeAddr').set([{
              name: 'cotpProv',
              defaultValue: '',
              required:false
            }, {
              name: 'cotpCity',
              defaultValue: '',
              required:false
            }]
        );
        $("#cotpEnName").val(data.ecNameEn);

        $("#fpsId").val(data.fpsId);
        $("#email").val(data.email);
        $("#isdCode").val(data.countryCode);
        $("#phoneCode").val(data.tel);
        $("#aiIn").val(data.creditCard);
        $("#hkId").val(data.hkId);

        if (isNotEmpty(data.countryCode) && isNotEmpty(data.tel)) {
          $("#mobnValue").val(data.countryCode+"-"+data.tel);
        } else {
          $("#mobnValue").val(data.tel);
        }

        if (isNotEmpty(data.fpsId)) {
          $("#cotpBankAcc").val(data.fpsId);
          $("#srcCotpBankAccTd").text(data.fpsId);
        } else if(isNotEmpty(data.email)) {
          $("#cotpBankAcc").val(data.email);
          $("#srcCotpBankAccTd").text(data.email);
        } else if(isNotEmpty(data.countryCode) && isNotEmpty(data.tel)) {
          $("#cotpBankAcc").val(data.countryCode+data.tel);
          $("#srcCotpBankAccTd").text(data.tel);
        } else if(isNotEmpty(data.creditCard)) {
          $("#cotpBankAcc").val(data.creditCard);
          $("#srcCotpBankAccTd").text(data.creditCard);
        } else if(isNotEmpty(data.hkId)) {
          $("#cotpBankAcc").val(data.hkId);
          $("#srcCotpBankAccTd").text(data.hkId);
        }

        $("#cotpBankName").val('');
        $("#cotpBankNameDiv").text('');
        $("#cotpAccName").val('');
        $("#cotpAccCurId").val(''); //账户币种
        $("#cotpAccCurNameDiv").text('');

        $("#countryId").val('');//交易对手银行所在国家地区&地点
        // $("#payeeEnCode").val('');
        $("#nameZhBankDiv").text('');
        $("#cotpSwiftCode").val('');
        $("#cotpSwiftCodeDiv").text('');//交易对手银行SWIFT CODE
        $("#localClearNo").val('');
        $("#localClearNoDiv").text('');//本地清算号
        $("#cotpBankAdrr").val('');
        $("#cotpBankAdrrDiv").text('');

        if (isNotEmpty(data.ecName)) {
          $("#srcCotpTd").text(data.ecName);
        }
        $("#srcCotpBankTd").text('');
        $("#srcCotpAccNameTd").text('');
      }
    } else {
      $("#oppId").val('');
      $("#cotpName").val('');
      $("#cotpEnName").val('');
      $("#cotpBankName").val('');
      $("#cotpBankAcc").val('');
      $("#cotpAccCurId").val(''); //账户币种
      $("#cotpAccCurNameDiv").text('');
      $("#cotpAdrr").val(''); //交易对手地址
      /*$("#cotpAdrrDiv").text('');*/
      /*$("#cotpCountry").val('');//交易对手所在国家地区&地点
      $("#cotpProv").val('');
      $("#cotpCity").val('');
      $("#cotpCountryDiv").text('');
      $("#cotpProvDiv").text('');
      $("#cotpCityDiv").text('');
      $("#countryId").val('');//交易对手银行所在国家地区&地点
      $("#nameZhBankDiv").text('');*/
      selectType.get('cotpCountry').val('');
      region.get('payeeAddr').set([{
            name: 'cotpProv',
            defaultValue: ''
          }, {
            name: 'cotpCity',
            defaultValue: ''
          }]
      );
      selectType.get('countryId').val('');
      $("#cotpSwiftCodeDiv").text('');//交易对手银行SWIFT CODE
      $("#localClearNoDiv").text('');//本地清算号
      // $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
      // $("#cotpAgentBankDiv").text('');//交易对手代理行
      // $("#cotpAgentBankAddrDiv").text('');//交易对手代理行地址
      // $("#cotpAgentBankAccDiv").text('');//交易对手代理行账户
    }

    //获取支付方式
    renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val());
  }

  //相应金额2回调
  bt.tranAmt1Callback = function(data) {
    // 招商云
    setZSYBankExtend();
  }

  //相应金额2回调
  bt.tranAmt2Callback = function (data) {
    var tranAmt2 = moneyType.get("tranAmt2").val();
    if (tranAmt2) {
      //相应金额2有值，则交易编码2和交易附言2必填
      moneyType.get('tranAmt2').attr('lay-verify', 'required|number2');
      $('#tranName2').attr('lay-verify', 'tableData|required');
      $('#tranEnglishCode2').attr('lay-verify', 'required');
      $('#tranAddWord2').attr('lay-verify', 'required');
      //同步币种
      $("#tranCurId2").val($("#tranCurId1").val());
      $("#tranEnglishCode2").val($("#tranEnglishCode1").val());
    } else {
      if (!$('#tranName2').val() && !$('#tranAddWord2').val()) {
        moneyType.get('tranAmt2').attr('lay-verify', 'number2');
        $('#tranName2').attr('lay-verify', 'tableData');
        $('#tranEnglishCode2').removeAttr("lay-verify");
        $('#tranAddWord2').removeAttr("lay-verify");
        //同步币种
        $("#tranCurId2").val('');
        $("#tranEnglishCode2").val('');
      }
    }
    // 招商云
    setZSYBankExtend();
    renderIcon();
  }
  //交易附言2回调
  $('#tranAddWord2').on('blur', function () {
    var tranAddWord2 = $('#tranAddWord2').val();
    if (tranAddWord2) {
      //交易附言2有值，则交易编码2和相应金额2必填
      moneyType.get('tranAmt2').attr('lay-verify', 'required|number2');
      $('#tranName2').attr('lay-verify', 'tableData|required');
      $('#tranEnglishCode2').attr('lay-verify', 'required');
      $('#tranAddWord2').attr('lay-verify', 'required');
      //同步币种
      $("#tranCurId2").val($("#tranCurId1").val());
      $("#tranEnglishCode2").val($("#tranEnglishCode1").val());
    } else {
      if (!moneyType.get('tranAmt2').val() && !$('#tranAddWord2').val()) {
        moneyType.get('tranAmt2').attr('lay-verify', 'number2');
        $('#tranName2').attr('lay-verify', 'tableData');
        $('#tranEnglishCode2').removeAttr("lay-verify");
        $('#tranAddWord2').removeAttr("lay-verify");
        //同步币种
        $("#tranCurId2").val('');
        $("#tranEnglishCode2").val('');
      }
    }
    renderIcon();
  })

  /**
   * 设置地点
   * @param data
   * @param elem
   * @param bv
   */
  function setAddress(countryId, countryName, cotpAddress) {
    var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + countryId;
    var requiredAddress = false;//设置地点是否必填
    var requiredAddr = false;//设置省市是否必填
    //汇款类型=1-中国大陆账户跨境汇款/2-境外账户汇款
    var remitType = bt.dicts.get("remitType").val();
    var bankTypeName = $("#srcBankTd").text();
    /* 交易对手是境外客商时，客商资料里的省市存的是"国家/地区"ID，只能走"国家+地区"；
       未重选过客商时用隐藏域 #cotpIsNative（后台按客商主数据带出）判定。 */
    var isOuterCorp = (cotpIsOuterCorp === null)
        ? ($('#cotpIsNative').val() === '0')
        : cotpIsOuterCorp;
    if (countryName == "中国" && !isOuterCorp) {
      $('#addressDiv').hide();
      $('#addrDiv').show();
      if (bankTypeName == "中国银行" && remitType == "2") {
        //中国银行 且 境外账户汇款 必填
        requiredAddr = true;
      }
    } else {
      $('#addressDiv').show();
      $('#addrDiv').hide();
      if (bankTypeName == "中国银行" && remitType == "2") {
        //中国银行 且 境外账户汇款 必填
        requiredAddress = true;
      }
    }
    selectType.get("cotpAddress").attr({
      "data-value": cotpAddress ? cotpAddress : '',
      "data-url": url,
      "data-required": requiredAddress
    });
    region.get('payeeAddr').set([{
      name: 'cotpProv',
      defaultValue: '',
      required: requiredAddr
    }, {
      name: 'cotpCity',
      defaultValue: '',
      required: requiredAddr
    }]);
    renderIcon();
    form.render();
  }

  //汇款类型回调函数
  bt.remitTypeCallback = function (data) {
    setReportInfo();
    var remitType = bt.dicts.get("remitType").val();
    var countryId = "CN";
    if ($('#payerIsDomesticBank').val() == "0") {
      // 境外账户
      countryId = $('#payerCountryId').val();
    }
    var bankTypeId = bt.selectType.get('corpBank').val();
    var bifCode = $('#payerBifCode').val();

    // 招商云
    setZSYBankExtend();
    renderIcon();
  }

  function setReportInfo() {
    //汇款类型=2-境外账户汇款时，“主体单位联系人”、“主体单位联系电话”为非必填
    var remitType = bt.dicts.get("remitType").val();
    if (remitType == "2") {
      //汇款类型=2-境外账户汇款时，“其它信息”非必填，并默认为折叠状态
      // $("#reportInfoDiv").removeClass("layui-show");
      $("#reportInfoDiv").addClass("layui-hide")
      $("#reportInfoDiv").addClass("ignore-verify");
      //隐藏被代收/代付单位 和  加急标志
      // $("#isQuickTr").hide();
      $("#isQuickTr").addClass('layui-hide');
      // $("#payCorpIdTitleDiv").hide();
      $("#payCorpIdTitleDiv").closest('.form-line').addClass('layui-hide')
      // $("#payCorpIdDiv").hide();
      // $("#reportInfoItemDiv").hide();
      $("#reportInfoItemDiv").addClass("layui-hide")
    } else {
      // $("#reportInfoDiv").addClass("layui-show");
      $("#reportInfoDiv").removeClass("layui-hide");
      $("#reportInfoDiv").removeClass("ignore-verify");
      // $("#isQuickTr").show();
      $("#isQuickTr").removeClass('layui-hide');
      // $("#payCorpIdTitleDiv").show();
      //$("#payCorpIdDiv").show();
      $("#payCorpIdTitleDiv").closest('.form-line').removeClass('layui-hide')
     // $("#reportInfoItemDiv").show();
      $("#reportInfoItemDiv").removeClass("layui-hide");
      $("input[data-name='tranAmt1']").attr("lay-verify", "required");

      var tranCurId1 = $("#tranCurId1").val();
      if (!tranCurId1){
        $("#tranCurId1").val($("#curIdDef").val());
        $("#tranEnglishCode1").val($("#englishCodeDef").val());
      }
    }

    //工商银行个性化
    var bankTypeName = $("#srcBankTd").text();
    setBranchCodeForGsbank(bankTypeName);
    renderIcon();
  }

  //付款类型回调函数
  bt.payTypeCallback = function (data) {
    //付款类型=1-预付货款时，最迟装运日期为必填项
    var payType = bt.dicts.get("payType").val();
    if (payType == "1") {
      $("input[name='shipDate']").attr("lay-verify", "required");
    } else {
      $("input[name='shipDate']").removeAttr("lay-verify");
    }
    //招商云
    setZSYBankExtend();
    renderIcon();
  }
  //国内外费用承担标志回调函数
  bt.costSignCallback = function (data) {
    //收付方向 0-收款 1-付款
    var cdSign = bt.dicts.get('cdSign').val();
    //国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA
    var costSign = bt.dicts.get("costSign").val();
    if ((cdSign == "0" && costSign == "2") || (cdSign == "1" && costSign == "1")) {
      $("input[name='costAcc']").attr("lay-verify", "required");
    } else {
      $("input[name='costAcc']").removeAttr("lay-verify");
    }
    setReportInfo();
    renderIcon();
    //现金流
    if (cdSign == "0") {
      $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('payMoney').val()));
      // $("#contractRateTr").hide();
      $("#contractRateTr").addClass('layui-hide');
    } else {
      $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('payMoney').val()));
      //$("#contractRateTr").show();
      $("#contractRateTr").removeClass('layui-hide');
    }
    //计算汇率折算金额
    calPayMoneyConvert();
    renderBudget();
  }
  /**
   * 汇款方式回调
   */
  bt.remitMethodCallBack = function (data, $elem) {
    var remitMethod = dict.get("remitMethod").val();

  }
  dict = bt.dicts.init();

  var up = bt.btUpload.init({
    elem: "#uploader",
    auto: false,
    gid: $("#attachmentId").val()
  });
  //单位回调函数
  bt.corpCallback = function (id) {
    //获取单位信息
    $.ajax({
      url: basePath + "/sys/corp/getSysCorp",
      method: 'POST',
      data: {id: id},
      dataType: 'json',
      success: function (data) {
        if (data.obj) {
          if (data.obj.userAccountCode) {
            $('#corpNameEn').val(data.obj.nameEn);
            $('#corpAddrEn').val(data.obj.addressEn);
            $('#corpAddr').val(data.obj.address);
            //清空主体单位联系人信息
            $('#corpContact').val("");
            $('#corpContactTel').val("");
            //重新加载主体单位联系人
            corpContactInit.get("corpContact").reload({corpId: id});
            //加载资金计划
            renderBudget();

            $("#srcCorpTd").text(data.obj.name);
          }
        }
        form.render();
      }
    });
    // 重新设置用途
    initPurpose();
    // 招商云
    setZSYBankExtend();
    //获取支付方式
    renderBankAcc("0", id, selectType.get("curId").val());
    //重新加载源账户信息
    corpBankAccInit.get("corpBankAcc").reload({
      corpId: id,
      curId: bt.selectType.get('curId').val(),
      bankType: bt.selectType.get('corpBank').val()
      //countryId: $("#payerCountryId").val()
    });
    costAccInit.get("costAcc").reload({bankType: bt.selectType.get('corpBank').val(), corpId: id});
  };

  function initPurpose() {
    var corpId = corp.get("corpId").val();
    var curId = selectType.get("curId").val();
    var countryId = $("#payerCountryId").val();
    var bankTypeId = selectType.get("corpBank").val();
    var purposeUrl = basePath + "/basic/business/btpurpose/getByListForFex?sysBusType=6&corpId=" + corpId
        + "&curId=" + curId + "&countryId=" + countryId + "&bankTypeId=" + bankTypeId;
    bt.selectType.get("remitUse").attr("data-url", purposeUrl);
  }


  /**
   * 获取支付方式
   * @param busType 业务类型：0主体单位，1交易对手
   * @param corpId 单位/交易对手
   * @param curId 币种
   * @param proId 产品
   */
  function renderBankAcc(busType, corpId, curId) {
    var oppId = $("#oppId").val();
    if (isEmpty(oppId)) {
      oppId = null;
    }
    if (busType != "" && corpId != "") {
      $.ajax({
        url: basePath + "/foreign/fexcorpbankacc?getBankAcc",
        method: 'POST',
        async: false,
        data: {
          busType: busType,
          tranType: "4",
          corpId: corpId,
          curId: curId,
          cashFlowType: "0",
          cdSign: bt.dicts.get('cdSign').val(),
          oppCorpId: oppId
        },
        dataType: 'json',
        success: function (data) {
          if (data.obj && data.obj.bankAcc) {
            selectType.get('corpBank').val(data.obj.bankType);
            $('#corpAccId').val(data.obj.accId);
            $('#corpBankAcc').val(data.obj.bankAcc);
            $('#corpAccName').val(data.obj.accName);
            $('#corpAccCurId').val(data.obj.curId);

            //账户类别
            if (data.obj.accType) {
              $('#accType').val(data.obj.accType);
              $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.obj.accType));
            }

            // 设置支付类别
            var bankPayTypeId = bt.selectType.get('bankPayTypeId').val();
            bt.selectType.get('bankPayTypeId').attr({
              "data-value": bankPayTypeId,
              "data-url": basePath + "/basic/business/btbanktype/listByCondition?bankType="+data.obj.bankTypeId+"&bifCode="+data.obj.bifCode
                  +"&bankCode="+data.obj.bankCode+"&isDomesticBank="+data.obj.isDomesticBank
            });

            // 设置模板
            setTemplate(bankPayTypeId);

            //费用支出账户
            $('#costAccId').val(data.obj.accId);
            $('#costAcc').val(data.obj.bankAcc);
            $("#srcBankTd").text(data.obj.bankTypeName);
            $("#srcAccNameTd").text(data.obj.accName);
            $("#srcBankAccTd").text(data.obj.bankAcc);
            if (data.obj.voucherName) {
              $("#srcPayTypeTd").text(data.obj.voucherName);
            } else {
              $("#srcPayTypeTd").text("");
            }
            $("#corpAccNameDiv").text(data.obj.accName);
            $('#corpAccCurNameDiv').text(data.obj.curName);
          } else {
            // 无匹配账户时清空主体单位银行账户
            /*$('#corpAccCurNameDiv').text('');
            $('#corpAccNameDiv').text('');
            $('#corpAccId').val('');
            $('#corpBankAcc').val('');
            $('#corpAccName').val('');
            $('#corpAccCurId').val('');*/
          }
        }
      });
    }
  }

  //主体账户信息
  var corpBankAccInit = bt.selectTable.init({
    elem: "#corpBankAccTable",
    url: basePath + "/foreign/fextranreq/getCorpBankAcc",
    urldata: {corpId: $("#corpIdDef").val(), curId: $("#curIdDef").val(), bankType: $("#corpBankDef").val()},
    scrollX: true,
    scrollY: "200px",
    searchInput: "#corpBankAcc",
    paging: true,
    columns: [
      {data: "bankAcc",},
      {data: "accName",},
      {data: "bankName",},
      {data: "curEnglishCode",}
    ],
    clickCallback: function (data) {
      if (data) {
        $('#corpBankAcc').val(data.bankAcc);
        $('#corpAccId').val(data.id);
        $('#corpAccName').val(data.accName);
        $('#corpAccCurId').val(data.curId);
        $("#corpAccCurNameDiv").text(data.curEnglishCode);
        $('#corpId').val(data.corpId);
        $("#corpAccNameDiv").text(data.accName);
        $("#corpNameDiv").text(data.corpName);
        selectType.get('corpBank').val(data.bankTypeId);
        $('#payerBifCode').val(data.bifCode);
        //境内外账号标志
        $('#payerIsDomesticBank').val(data.isDomesticBank);
        //本地清算号
        if (data.localClearNo) {
          $("#payerLocalClearNoDiv").text(data.localClearNo);
        } else {
          $("#payerLocalClearNoDiv").text('');
        }
        //现金流信息
        $("#srcBankTd").text(data.bankTypeName);
        $("#srcAccNameTd").text(data.accName);
        $("#srcBankAccTd").text(data.bankAcc);
        $("#srcCorpTd").text(data.corpName);
        //国家地区信息
        $('#payerCountryId').val(data.prov);
        $('#payerCountryNameZh').val(data.provName);
        $('#payerEnCode').val(data.countryEnCode);


        //账户类别
        $('#accType').val(data.accType);
        $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.accType));

        var corpId = corp.get("corpId").val();
        var curId = selectType.get("curId").val();
        //var countryId = $("#payerCountryId").val();
        var bankTypeId = selectType.get("corpBank").val();
        var purposeUrl = basePath + "/basic/business/btpurpose/getByListForFex?sysBusType=6&corpId=" + corpId
            + "&curId=" + curId + "&countryId=" + $("#payerCountryId").val() + "&bankTypeId=" + bankTypeId;

        //加载资金计划
        renderBudget();
        //默认设置费用支出账户
        $('#costAccId').val(data.id);
        $('#costAcc').val(data.bankAcc);
        //重新加载费用支出账号
        costAccInit.get("costAcc").reload({bankType: data.bankTypeId, corpId: data.corpId});

        //工商银行个性化
        setBranchCodeForGsbank(data.bankTypeName);
        //银行支付类型
        var countryId = "CN";
        if (data.isDomesticBank == "0") {
          // 境外账户
          countryId = data.prov;
        }
        //汇款类型
        var remitType = bt.dicts.get("remitType").val();
        //计算汇率折算金额
        calPayMoneyConvert();

        // 设置支付类别
        var bankPayTypeId = bt.selectType.get('bankPayTypeId').val();
        bt.selectType.get('bankPayTypeId').attr({
          "data-value": bankPayTypeId,
          "data-url": basePath + "/basic/business/btbanktype/listByCondition?bankType="+data.bankTypeId+"&bifCode="+data.bifCode
              +"&bankCode="+data.bankCode+"&isDomesticBank="+data.isDomesticBank
        });

        // 招商云
        setZSYBankExtend();

        // 设置模板
        setTemplate(bankPayTypeId);
      } else {
        $('input.text').val('');
        $('#corpAccCurNameDiv').text('');
        $('#corpAccNameDiv').val('');
      }
    },handleDeleteCallback:function (){
      $('#corpAccCurNameDiv').text('');
      $('#corpAccNameDiv').val('');
          $("#corpAccId").val("");
          $("#corpBankAcc").val("");
          $("#corpAccName").html("");
          corpBankAccInit.get("corpBankAcc").rebuild({
              urldata: {
                  curId: selectType.get('curId').val(),
                  bankType: selectType.get('corpBank').val(),
                  //countryId:$("#payerCountryId").val(),
                  "corpId":corp.get("corpId").val()
              },
              columns: [
                  {data:"bankAcc"},
                  {data:"accName"},
                  {data:"bankName"},
                  {data:"curEnglishCode"}
              ]
          });
    }
  });


  //起运国下拉表格
  var originCountryInit = bt.selectTable.init({
    elem: "#originCountryTable",
    url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
    searchInput: "#originCountryName",
    scrollX: "600px",
    scrollY: "300px",
    columns: [
      {data: "code",},
      {data: "nameZh",},
      {data: "nameEn",},
    ],
    clickCallback: function (data, $tr) {
      $("input[name='originCountryName']").val(data.nameZh);
      $("input[name='originCountry']").val(data.id);
    },
  });

  //办理机构
  var branchCodeInit = bt.selectTable.init({
    elem: "#branchCodeTable",
    url: basePath + "/basic/business/bisBankBranch/listAll",
    searchInput: "#branchCode",
    scrollX: true,
    scrollY: "200px",
    valueFromTable: false,
    columns: [
      {data: "branchCode",},
      {data: "branchName",},
    ],
    clickCallback: function (data) {
      if (data) {
        $('#branchCode').val(data.branchCode);
        $('#branchName').val(data.branchName);
      } else {
        $('input.text').val('');
      }
    }
  });

  //费用支出账户信息
  var costAccInit = bt.selectTable.init({
    elem: "#costAccTable",
    url: basePath + "/foreign/fextranreq/getCostAcc",
    urldata: {bankType: $("#corpBankDef").val(), corpId: $("#corpIdDef").val()},
    scrollX: true,
    scrollY: "200px",
    searchInput: "#costAcc",
    paging: true,
    columns: [
      {data: "bankAcc",},
      {data: "accName",},
      {data: "bankName",},
      {data: "curName",}
    ],
    clickCallback: function (data) {
      if (data) {
        $('#costAccId').val(data.id);
        $('#costAcc').val(data.bankAcc);
        bt.selectType.get('costCurId').val(data.curId);
      } else {
        $('input.text').val('');
      }
    }
  });

  //交易编码
  var tranCodeInit1 = bt.selectTable.init({
    elem: "#tranCodeTable1",
    url: basePath + "/foreign/fextraninfo/list",
    searchInput: "#tranName1",
    scrollX: true,
    scrollY: "300px",
    columns: [
      {data: "tranCode",},
      {data: "tranName",}
    ],
    clickCallback: function (data) {
      if (data) {
        $('#tranName1').val(data.tranName);
        $('#tranCode1').val(data.tranCode);


        var tranCurId1 = $("#tranCurId1").val();
        if(!tranCurId1){
          $("#tranCurId1").val($("#curIdDef").val());
          $("#tranEnglishCode1").val($("#englishCodeDef").val());
        }
        //获取交易附言
        $.ajax({
          url: basePath + "/foreign/fexAddWord/getFexAddWord",
          method: 'POST',
          data: {tranCode: data.tranCode, cdSign: bt.dicts.get("cdSign").val()},
          dataType: 'json',
          success: function (data) {
            if (data.obj) {
              $('#tranAddWord1').val(data.obj);
            } else {
              $('#tranAddWord1').val('');
            }
            form.render();
          }
        });
      } else {
        $('input.text').val('');
        $('#tranAddWord1').val('');
      }
      // 招商云
      setZSYBankExtend();
    }
  });


  /**
   * 招商云-新增要素
   */
  function setZSYBankExtend(initSign) {
    //汇款类型=中国大陆账户跨境汇款&交易币种=人民币&单位账户的银行类别=招行&银行接口代码=招商云时
    var remitType = bt.dicts.get("remitType").val();
    var curId = $("#curIdDef").val();
    if (initSign) {
      if (selectType.get("curId").val()) {
        curId = selectType.get("curId").val();
      }
    } else {
      curId = selectType.get("curId").val();
    }
    var rmbId = $('#rmbId').val();
    var payerBifCode = $('#payerBifCodeStr').val();
    if (remitType == "1" && curId == rmbId && isReportVisibleField("goodsAmount")) {
      var tranCode1 = $('#tranCode1').val();
      var tranCode2 = $('#tranCode2').val();
      //如果交易编码1或交易编码2的首位为1
      if ((tranCode1 && tranCode1.substring(0, 1) == '1') || (tranCode2 && tranCode2.substring(0, 1) == '1')) {
        setTranCode(1, true);
        if (!initSign) {
          //货物贸易金额 默认值设置
          var goodsAmount = 0;
          if (tranCode1.substring(0, 1) == '1') {
            var tranAmt1 = moneyType.get("tranAmt1").val();
            goodsAmount = moneyFun.add(goodsAmount, tranAmt1);
          }
          if (tranCode2.substring(0, 1) == '1') {
            var tranAmt2 = moneyType.get("tranAmt2").val();
            goodsAmount = moneyFun.add(goodsAmount, tranAmt2);
          }
          moneyType.get("goodsAmount").val(goodsAmount);
        }
        //付款类型=1-预付货款
        var payType = bt.dicts.get("payType").val();
        if (payType == "1") {
          setPayType(true);
        } else {
          $('.payType1 input').removeAttr("lay-verify");
          $('.payType1').hide();
          setPayType(false);
        }

        var rptGateEntId = bt.corp.get("rptGateEntId").val();
        var rptGateType = dict.get("rptGateType").val();
        if (rptGateEntId) {
          bt.corp.get("rptGateEntId").val(rptGateEntId);
        }
        if (rptGateType) {
          dict.get("rptGateType").val(rptGateType);
        }
      } else {
        setTranCode(1, false);
        moneyType.get("goodsAmount").val(0);
      }

      //如果交易编码1或交易编码2的首位为1
      if ((tranCode1 && tranCode1.substring(0, 1) == '2') || (tranCode2 && tranCode2.substring(0, 1) == '2')) {
        setTranCode(2, true);
      } else {
        $('.tranCode2 input').removeAttr("lay-verify");
        setTranCode(2, false);
      }

      //如果交易编码1或交易编码2的首位为1
      if ((tranCode1 && tranCode1.substring(0, 1) == '3') || (tranCode2 && tranCode2.substring(0, 1) == '3')) {
        setTranCode(3, true);
      } else {
        $('.tranCode3 input').removeAttr("lay-verify");
        setTranCode(3, false);
      }

      //如果交易编码1或交易编码2的首位为1
      if ((tranCode1 && tranCode1.substring(0, 1) == '4') || (tranCode2 && tranCode2.substring(0, 1) == '4')
          || (tranCode1 && tranCode1.substring(0, 1) == '3' && tranCode1.substring(0, 3) != '322')
          || (tranCode2 && tranCode2.substring(0, 1) == '3' && tranCode2.substring(0, 3) != '322')) {
        setTranCode(4, true);
      } else {
        $('.tranCode4 input').removeAttr("lay-verify");
        setTranCode(4, false);
      }

      //如果交易编码1或交易编码2的首位为6
      if ((tranCode1 && tranCode1.substring(0, 1) == '6') || (tranCode2 && tranCode2.substring(0, 1) == '6')) {
        setTranCode(6, true);
      } else {
        $('.tranCode6 input').removeAttr("lay-verify");
        setTranCode(6, false);
      }
    } else {
      moneyType.get("goodsAmount").val('');
      moneyType.get('generalTradeAmount').val('');
      moneyType.get('feedAmount').val('');
      moneyType.get('otherTradeAmount').val('');
      moneyType.get('borderTradeAmount').val('');
      bt.corp.get("rptGateEntId").setRequired(false);
      dict.get("rptGateType").attr({"data-required": false});
      setTranCode(1, false);
      setTranCode(2, false);
      setTranCode(3, false);
      setTranCode(4, false);
      setTranCode(6, false);
    }

    setRptGateEntId();
    renderIcon();
  }

  var tranCodeInit2 = bt.selectTable.init({
    elem: "#tranCodeTable2",
    url: basePath + "/foreign/fextraninfo/list",
    searchInput: "#tranName2",
    scrollX: true,
    scrollY: "300px",
    columns: [
      {data: "tranCode",},
      {data: "tranName",}
    ],
    clickCallback: function (data) {
      if (data) {
        $('#tranName2').val(data.tranName);
        $('#tranCode2').val(data.tranCode);
        $('#tranName2').attr('lay-verify', 'tableData|required');
        moneyType.get('tranAmt2').attr('lay-verify', 'required|number2');
        $('#tranEnglishCode2').attr('lay-verify', 'required');
        $('#tranAddWord2').attr('lay-verify', 'required');
        //同步币种
        $("#tranCurId2").val($("#tranCurId1").val());
        $("#tranEnglishCode2").val($("#tranEnglishCode1").val());
        //获取交易附言
        $.ajax({
          url: basePath + "/foreign/fexAddWord/getFexAddWord",
          method: 'POST',
          data: {tranCode: data.tranCode, cdSign: bt.dicts.get("cdSign").val()},
          dataType: 'json',
          success: function (data) {
            if (data.obj) {
              $('#tranAddWord2').val(data.obj);
            } else {
              $('#tranAddWord2').val('');
            }
            form.render();
          }
        });
      } else {
        $('input.text').val('');
        $('#tranCode2').val('');
        if (!$('#tranName2').val() && !$('#tranAddWord2').val()) {
          moneyType.get('tranAmt2').attr('lay-verify', 'number2');
          $('#tranName2').attr('lay-verify', 'tableData');
          $('#tranEnglishCode2').removeAttr("lay-verify");
          $('#tranAddWord2').removeAttr("lay-verify");
          //同步币种
          $("#tranCurId2").val('');
          $("#tranEnglishCode2").val('');
        }
      }
      // 招商云
      setZSYBankExtend();
      renderIcon();
    },
    blurInputCallback: function () {
      $('input.text').val('');
      $('#tranCode2').val('');
      if (!$('#tranName2').val() && !$('#tranAddWord2').val()) {
        moneyType.get('tranAmt2').attr('lay-verify', 'number2');
        $('#tranName2').attr('lay-verify', 'tableData');
        $('#tranEnglishCode2').removeAttr("lay-verify");
        $('#tranAddWord2').removeAttr("lay-verify");
        //同步币种
        $("#tranCurId2").val('');
        $("#tranEnglishCode2").val('');
      }
      renderIcon();
    }
  });

  //主体单位联系人
  var corpContactInit = bt.selectTable.init({
    elem: "#corpContactTable",
    url: basePath + "/foreign/fexlinkmaninfo/listByCorpId",
    urldata: {corpId: $("#corpIdDef").val()},
    searchInput: "#corpContact",
    scrollX: true,
    scrollY: "200px",
    valueFromTable: false,
    columns: [
      {data: "payerName",},
      {data: "payerTel",},
    ],
    clickCallback: function (data) {
      if (data) {
        $('#corpContact').val(data.payerName);
        $('#corpContactTel').val(data.payerTel);
      } else {
        $('input.text').val('');
      }
    }
  });

  //交易地点下拉表格
  var tranAddrInit = bt.selectTable.init({
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

  //产品回调函数
  bt.proCallback = function (data) {
    //获取支付方式
    renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val());
  };

  //币别回调函数
  bt.curCallback = function (data) {
    $("#tranCurId1").val(data.id);
    $("#tranEnglishCode1").val(data.englishcode);
    var tranCode2 = $('#tranCode2').val();
    if (tranCode2 && tranCode2 != "") {
      $("#tranCurId2").val(data.id);
      $("#tranEnglishCode2").val(data.englishcode);
    } else {
      $("#tranCurId2").val('');
      $("#tranEnglishCode2").val('');
    }
    renderBudget();
    //现金流信息
    $("#srcCurTd").text(data.englishcode);
    //获取支付方式
    renderBankAcc("0", $("input[name='corpId']").val(), data.id);
    //计算汇率折算金额
    calPayMoneyConvert();
    form.render();
    // 重新设置用途
    initPurpose();
    // 招商云
    setZSYBankExtend();
  };

  /**
   * 汇率折算金额计算
   */
  calPayMoneyConvert = function () {
    //交易币种
    var curId = selectType.get("curId").val();
    //主体单位账户币种
    var corpAccCurId = $("#corpAccCurId").val();
    //金额
    var payMoney = moneyType.get("payMoney").val();
    var cdSign = dict.get('cdSign').val();
    if (cdSign == "1" && isNotEmpty(curId) && isNotEmpty(corpAccCurId)) {
      if (curId == corpAccCurId) {
        moneyType.get("payMoneyConvert").val(payMoney);
        return;
      }
      $.ajax({
        "url": basePath + "/basic/business/btcurrencypair/getByCurId",
        data: {
          curId1: corpAccCurId,
          curId2: curId
        },
        type: "post",
        dataType: "json",
        async: false,
        success: function (data) {
          if (data.success) {
            if (null == data.obj) {
              parent.layer.alert(bt.lang("fex.tranApp.currencyPair"), {icon: 2});
              moneyType.get("contractRate").val("");
              moneyType.get("payMoneyConvert").val("");
            } else {
              var contractRate = moneyType.get("contractRate").val();
              if (isNotEmpty(contractRate)) {
                var baseCurId = data.obj.baseCurId;
                if (baseCurId == corpAccCurId) {//主体单位账号币种为标准
                  moneyType.get("payMoneyConvert").val(bt.money.mul(payMoney, contractRate, 2));
                } else if (baseCurId == curId) {//交易币种为标准
                  moneyType.get("payMoneyConvert").val(bt.money.div(payMoney, contractRate, 2));
                } else {
                  moneyType.get("payMoneyConvert").val("");
                }
              }
            }
          }
        }
      });
    } else {
      moneyType.get("payMoneyConvert").val("");
    }
  }

  /**
   * 为空判断
   * @param stringData
   * @returns {boolean}
   */
  function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 非空判断
   * @param stringData
   * @returns {boolean}
   */
  function isNotEmpty(stringData) {
    return !isEmpty(stringData)
  }

  //主体单位银行回调函数
  bt.corpBankCallback = function (data) {
    //重新加载源账户信息
    corpBankAccInit.get("corpBankAcc").reload({
      corpId: $("input[name='corpId']").val(),
      curId: bt.selectType.get('curId').val(),
      bankType: bt.selectType.get('corpBank').val()
    });

    costAccInit.get("costAcc").reload({bankType: bt.selectType.get('corpBank').val(), corpId: $("input[name='corpId']").val()});

    $('#corpBankAcc').val('');
    $('#corpAccId').val('');
    $('#corpAccName').val('');
    $('#corpAccCurNameDiv').text('');
    $('#corpAccNameDiv').val('');

    //现金流信息
    $("#srcBankTd").text(data.typename);
    $("#srcAccNameTd").text('');
    $("#srcBankAccTd").text('');
    $("#srcCorpTd").text('');
    var remitMethod = "0,1";
    if (data.remitmethod) {
      remitMethod = remitMethod + "," + data.remitmethod;
    }
    bt.dicts.get("remitMethod").attr({"data-displayData": remitMethod});
    form.render();
    // 重新设置用途
    initPurpose();
    //工商银行个性化
    setBranchCodeForGsbank(data.typename);
    //计算汇率折算金额
    calPayMoneyConvert();
  };

  setcorpBank = function (data, countryId){
    //重新加载源账户信息
    corpBankAccInit.get("corpBankAcc").reload({
      corpId: $("input[name='corpId']").val(),
      curId: bt.selectType.get('curId').val(),
      bankType: bt.selectType.get('corpBank').val()
      //countryId:countryId || ""
    });
    //现金流信息
    $("#srcBankTd").text(data.typename);
    $("#srcAccNameTd").text('');
    $("#srcBankAccTd").text('');
    $("#srcCorpTd").text('');
    var remitMethod = "0,1";
    if (data.remitmethod) {
      remitMethod = remitMethod + "," + data.remitmethod;
    }
    bt.dicts.get("remitMethod").attr({"data-displayData": remitMethod});
    form.render();
    // 重新设置用途
    initPurpose();
    //工商银行个性化
    setBranchCodeForGsbank(data.typename);
    //计算汇率折算金额
    calPayMoneyConvert();
  }

  //工商银行个性化
  function setBranchCodeForGsbank(bankTypeName) {
    var remitType = bt.dicts.get("remitType").val();
    //汇款类型=中国大陆账户跨境汇款/境外账户汇款
    if ((remitType == "1" || remitType == "2") && bankTypeName && bankTypeName.indexOf("工商银行") != -1) {
      $("input[name='corpNameEn']").attr("lay-verify", "required");
    } else {
      $("input[name='corpNameEn']").removeAttr("lay-verify");
    }
    renderIcon();
  }


  /**
   *
   * 根据支付类型和银行账号获取魔板
   *
   */
  function setTemplate(bankPayTypeId){
    var corpBankAcc = $("#corpBankAcc").val();
    var bankPayType = bt.selectType.get('bankPayTypeId').val() || bankPayTypeId;
    if (!corpBankAcc || !bankPayType){
      if(!$('#templateId').val()){
        initFormShow();
      }
      return false;
    }
    // 补充接口请求返回值给templateId，templateName
    $.ajax({
      url: basePath + '/foreign/fexTranConfigTemplate/getTemplateByBankAcc',
      type: 'POST',
      data: {
        bankAcc: corpBankAcc,
        payTypeId: bankPayType
      },
      success: function(response) {
        if (response) { // 根据实际返回码调整
          var jsonObj;
          if(typeof response  == 'object'){
            jsonObj = response;
          }else{
            jsonObj = JSON.parse(response);
          }
          if(jsonObj.success){
            if(jsonObj.obj){
              var templateId = jsonObj.obj.id;
              var templateName = jsonObj.obj.templateName;
              var versionNo = jsonObj.obj.versionNo;

              $('#templateName').val(templateName).attr('title', templateName);
              $('#templateId').val(templateId);
              $('#versionNo').val(versionNo);

              loadTemplateConfig(templateId, versionNo);
            }else {
              initFormShow();
              $('#templateName').val('').attr('title', '');
              $('#templateId').val('');
              $('#versionNo').val('');
            }
          }
        }
      }
    });
  }

  //交易对手回调函数
  function setCotp(data, flag) {
    //交易对手所在国家地区
    // setAddress(data.countryid, data.countryname,data.address);
    // if(data.countryid){
    //     //$("#nameZhDiv").text(data.countryname);
    //     // selectType.get('cotpCountry').val(data.countryid);
    //     setTimeout(function () {
    //         // selectType.get('cotpAddress').val(data.address);
    //     }, 500)
    //     region.get('payeeAddr').set([{
    //         name: 'cotpProv',
    //         defaultValue: data.prov
    //     }, {
    //         name: 'cotpCity',
    //         defaultValue: data.city
    //     }]);
    // } else {
    //     //$("#nameZhDiv").text('');
    //     selectType.get('cotpCountry').val('');
    //     setTimeout(function () {
    //         selectType.get('cotpAddress').val('');
    //     }, 500)
    //     region.get('payeeAddr').set([{
    //         name: 'cotpProv',
    //         defaultValue: ''
    //     }, {
    //         name: 'cotpCity',
    //         defaultValue: ''
    //     }]);
    // }
    // //交易对手银行地址
    // if(data.addressen){
    //     $("#cotpAdrrDiv").text(data.addressen);
    // } else {
    //     $("#cotpAdrrDiv").text('');
    // }
    // //现金流信息
    // $("#srcCotpTd").text(data.cotpname);
    // if (flag) {
    //     //交易对手银行账户
    //     setCotpBankAcc();
    // }
    // //获取支付方式
    // renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),selectType.get("proId").val());
  }

  bt.cotpCallback = function (data) {
    setCotp(data, true);
  };

  //交易对手银行回调函数
  function setCotpBank(data, flag) {
    //交易对手银行所在国家地区
    if (data.countryname) {
      $("#nameZhBankDiv").text(data.countryname);
    } else {
      $("#nameZhBankDiv").text('');
    }
    $("#countryId").val(data.countryid);
    if (flag) {
      //交易对手银行账户
      setCotpBankAcc();
    }
    //现金流信息
    $("#srcCotpBankTd").text(data.cotpname);
    //交易对手银行Swift Code(交易对手银行账户-BIC Code-fexCotpBankAcc.bicCode)
    if (data.biccode) {
      $("#cotpSwiftCodeDiv").text(data.biccode);
    } else {
      $("#cotpSwiftCodeDiv").text('');
    }
    //交易对手银行本地清算号
    if (data.localclearno) {
      $("#localClearNoDiv").text(data.localclearno);
    } else {
      $("#localClearNoDiv").text('');
    }
  }

  bt.cotpBankCallback = function (data) {
    setCotpBank(data, true);
  };

  //交易对手银行账户
  function setCotpBankAcc() {
    // var cotpId = selectType.get("cotpId").val();
    // var bankId = selectType.get("cotpBank").val();
    // /*selectType.get("cotpBankAcc").attr({"data-value": "",
    //     "data-url": basePath + "/foreign/fexcotpbankacc/select?cotpId=" + cotpId  + "&bankId=" + bankId});
    // selectType.render();*/
    // cotpBankAccInit.get("cotpBankAcc").reload({cotpId: cotpId, bankId: bankId});
    form.render();
  }

  //交易对手银行账户回调函数
  /*bt.cotpBankAccCallback = function (data) {
      selectType.get("cotpId").val(data.cotpid);
      //反写交易对手信息
      setCotp(selectType.get("cotpId").getSelectedData(), false);
      selectType.get("cotpBank").val(data.bankid);
      //反写交易对手银行信息
      setCotpBank(selectType.get("cotpBank").getSelectedData(), false);
      //交易对手银行地址(交易对手银行账号-地址-fexCotpBankAcc.bankAddr)
      if(data.bankaddr) {
          $("#cotpBankAdrrDiv").text(data.bankaddr);
      } else {
          $("#cotpBankAdrrDiv").text('');
      }
      //交易对手代理行(交易对手银行账户-中转行-fexCotpBankAcc.tranBank)
      if(data.tranbank) {
          $("#cotpAgentBankDiv").text(data.tranbankname);
      } else {
          $("#cotpAgentBankDiv").text('');
      }
      //交易对手代理行地址(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAddr)
      if(data.tranbankaddr) {
          $("#cotpAgentBankAddrDiv").text(data.tranbankaddr);
      } else {
          $("#cotpAgentBankAddrDiv").text('');
      }
      //交易对手在代理行的账户(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAcc)
      if(data.tranbankacc) {
          $("#cotpAgentBankAccDiv").text(data.tranbankacc);
      } else {
          $("#cotpAgentBankAccDiv").text('');
      }
      $("#cotpAccId").val(data.id);
      $("#cotpAccName").val(data.accname);
      $("#cotpAccCurNameDiv").text(data.curname);

      //现金流信息
      $("#srcCotpBankAccTd").text(data.bankacc);
      $("#srcCotpAccNameTd").text(data.accname);
  };*/

  //银行自定义配置初始化前回调
  customBeforeInit = function (data, arguments) {
    var show = "(" + arguments.customCode + ")" + arguments.customName;
    return show;
  }

  bt.bankPayTypeCallback = function (data) {
    if(data && data.customcode){
      isBankPayType = true;
    } else {
      isBankPayType = false;
    }

    $("#bankPayType").val(data.customcode);
    $("#bankPayTypeName").val(data.customname);
    // 设置模板
    setTemplate(data.id);
    setTemplate(data.id);
    // 重置客商账号组件
    resetExternalAccCorp(data.customcode);
    renderIcon();
  }

  /**
   * 现金流项目
   * @param data
   */
  bt.cashItemCallback = function (data) {
    if (data.itemname) {
      $("#srcCashItemTd").text(data.itemname);
    } else {
      $("#srcCashItemTd").text('');
    }
  }
  var selectType = bt.selectType.init();

  resetExternalAccCorp();

  //结算日期回调函数
  settleDateCallback = function (data, $elem) {
    $("#srcSettleDateTd").text(dateType.get('settleDate').val());
    renderBudget();
  };

  //金额回调函数
  bt.payMoneyCallback = function (data) {
    //根据收付标志显示金额
    var cdSign = dict.get('cdSign').val();
    if (cdSign == "0") {
      $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('payMoney').val()));
    } else {
      $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('payMoney').val()));
    }
    //计算汇率折算金额
    calPayMoneyConvert();
  };

  //合同汇率回调函数
  bt.contractRateCallback = function (data) {
    //计算汇率折算金额
    calPayMoneyConvert();
  };

  // 被代收单位回调
  bt.payCorpCallback = function (data) {
    var corpId = $("input[name='corpId']").val();
    if(corpId == data.id){
      bt.corp.get("payCorpId").val('');
      layer.alert(bt.lang("fex.foreign.message.payCorp.same"), {icon: 2});
    }
  };

    //初始化单位
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();

  //监听提交
  form.on('submit(submit)', function (data) {
    //控制重复提交

    if ($(data.elem).parent().hasClass('isSubmiting')) {
      bt.loading.hide();
      return false;
    }

    $("#status").val($(this).attr("value"));
    data.field.attachmentId=up.getGroupId();
      $("#attachmentId").val(attachmentId);
    //支付类型为FPS时，交易对手账户与FPS ID二者至少填一个
    var bankPayType = $("#bankPayType").val();
    if ("FPS" == bankPayType || "fps" == bankPayType) {
      if (isEmpty($("#cotpBankAcc").val()) && isEmpty($("#fpsId").val())) {
        bt.loading.hide();
        layer.alert(bt.lang("fex.fexTranReq.message.fps.cotpBankAcc.or.fpsId"), {icon: 2});
        return false;
      }
    }
    //判断是否挂接资金计划
    var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
    if ($("input[name='sumMoney']").length) {
      var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
      // 校验金额与资金计划总金额一致
      if (sumMoney != payMoney) {
        bt.loading.hide();
        layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
        return false;
      }
    }

    //汇款类型=2-境外账户汇款时，其他信息隐藏无需校验
    var remitType = bt.dicts.get("remitType").val();
    if (remitType != "2") {
      //校验交易编码
      var tranCode1 = $('#tranCode1').val();
      var tranCode2 = $('#tranCode2').val();
      var tranAmt1 = moneyType.get("tranAmt1").val();
      var tranAmt2 = moneyType.get("tranAmt2").val();
      if (tranCode2 != "") {
        //交易编码1和交易编码2不允许相同
        if (tranCode1 == tranCode2) {
          bt.loading.hide();
          layer.alert(bt.lang("fex.foreign.message.trancode.same"), {icon: 2});
          return false;
        }
        //相应金额2不能为空
        var tranAmt2 = moneyType.get("tranAmt2").val();
        if (tranAmt2 == null) {
          bt.loading.hide();
          layer.alert(bt.lang("fex.foreign.message.tranamt2.null"), {icon: 2});
          return false;
        }
        //交易附言2不能为空
        var tranAddWord2 = $('#tranAddWord2').val();
        if (!tranAddWord2) {
          bt.loading.hide();
          layer.alert(bt.lang("fex.foreign.message.tranaddword2.null"), {icon: 2});
          return false;
        }
      }
      if (!tranAmt2) {
        // 相应金额2未填写时，默认0
        tranAmt2 = 0;
      }
      //校验相应金额1和相应金额2之和 是否与 付款金额一致
      var tranAmt = moneyFun.add(tranAmt1, tranAmt2);
      if (tranAmt != payMoney) {
        bt.loading.hide();
        layer.alert(bt.lang("fex.foreign.message.tranAmt.mismatch"), {icon: 2});
        return false;
      }
    }

    // 如果汇率类型=中国大陆账户跨境汇款&交易币种=人民币&单位账户的银行类别=招行&银行接口代码=招商云&货物贸易金额！=0，
    // 校验预付货款项下金额+一般贸易金额+进料加工金额+其他贸易金额+边境贸易金额+海关特殊监管区域及保税监管场所进出境物流货物金额+离岸转手买卖金额+其他金额=货物贸易金额
    var tranCode1 = $('#tranCode1').val();
    var tranCode2 = $('#tranCode2').val();
    var curId = bt.selectType.get('curId').val();
    var rmbId = $('#rmbId').val();
    var payerBifCode = $('#payerBifCodeStr').val();
    if (remitType == "1" && curId == rmbId && isReportVisibleField("goodsAmount")
        && ((tranCode1 && tranCode1.substring(0, 1) == '1') || (tranCode2 && tranCode2.substring(0, 1) == '1'))) {
      var goodsAmount = moneyType.get('goodsAmount').val();//货物贸易金额
      var totalAmount = 0;
      var advanceChargeAmount = moneyType.get('advanceChargeAmount').val();//预付货款项下金额
      totalAmount = moneyFun.add(totalAmount,advanceChargeAmount);
      var generalTradeAmount = moneyType.get('generalTradeAmount').val();//一般贸易金额
      totalAmount = moneyFun.add(totalAmount,generalTradeAmount);
      var feedAmount = moneyType.get('feedAmount').val();//进料加工金额
      totalAmount = moneyFun.add(totalAmount,feedAmount);
      var otherTradeAmount = moneyType.get('otherTradeAmount').val();//其他贸易金额
      totalAmount = moneyFun.add(totalAmount,otherTradeAmount);
      var borderTradeAmount = moneyType.get('borderTradeAmount').val();//边境贸易金额
      totalAmount = moneyFun.add(totalAmount,borderTradeAmount);
      var customAmount = moneyType.get('customAmount').val();//海关特殊监管区域及保税监管场所进出境物流货物金额
      totalAmount = moneyFun.add(totalAmount,customAmount);
      var offshoreAmount = moneyType.get('offshoreAmount').val();//离岸转手买卖金额
      totalAmount = moneyFun.add(totalAmount,offshoreAmount);
      var otherAmount = moneyType.get('otherAmount').val();//其他金额
      totalAmount = moneyFun.add(totalAmount,otherAmount);
      if (goodsAmount != totalAmount) {
        bt.loading.hide();
        parent.layer.alert(bt.lang("fex.goodsAmount.diff"), {icon: 2});
        return false;
      }
    }

    bt.loading.show();
    //增加已提交的标志
    $(data.elem).parent().addClass('isSubmiting');



    //证书签名
    //证书签名
    var encrypt = $("#encrypt").val(); // cfca证书
    if (encrypt == 2 || encrypt == 1) { // 1 天威证书
      signData("外汇交易提交", 11, function () {
        var action = "/foreign/fextranreq/modify";
        var corpAccCurId = $("#corpAccCurId").val();//主体单位账号币种
        var cotpAccCurId = $("#cotpAccCurId").val();//交易对手账号币种
        var curId = selectType.get("curId").val();//交易币种
        if (corpAccCurId != cotpAccCurId) {
          //跨币种交易，是否继续?
          bt.loading.hide();
          parent.layer.confirm(bt.lang('foreign.message.confirm.cur'), function (index) {
            bt.loading.show();
            // $('#inputForm').serialize()
            $.post(basePath + action, getFormFields(data.field), function (result) {
              bt.loading.hide();
              parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
                parent.layer.closeAll();
                //解除已提交的标志
                $(data.elem).parent().removeClass('isSubmiting');
                if (result.success) {
                  if (bt.navtab) {
                    bt.navtab.closeCurrent();
                  } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                  }
                }
              }, function () {
                //取消已提交标志
                $(data.elem).parent().removeClass('isSubmiting');
              })
            }, 'JSON');
          }, function () {
            //取消已提交标志
            $(data.elem).parent().removeClass('isSubmiting');
          });
        } else if (corpAccCurId == cotpAccCurId && corpAccCurId != curId) {
          //交易币种与交易主体单位/交易对手账号币种不一致，是否继续?
          bt.loading.hide();
          parent.layer.confirm(bt.lang('foreign.message.confirm.cur1'), function (index) {
            bt.loading.show();
            //  $('#inputForm').serialize()
            $.post(basePath + action,getFormFields(data.field), function (result) {
              bt.loading.hide();

              parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
                parent.layer.closeAll();
                //解除已提交的标志
                $(data.elem).parent().removeClass('isSubmiting');
                if (result.success) {
                  if (bt.navtab) {
                    bt.navtab.closeCurrent();
                  } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                  }
                }
              }, function () {
                //取消已提交标志
                $(data.elem).parent().removeClass('isSubmiting');
              })
            }, 'JSON');
          }, function () {
            //取消已提交标志
            $(data.elem).parent().removeClass('isSubmiting');
          });
        } else {
          // $('#inputForm').serialize()
          $.post(basePath + action, getFormFields(data.field), function (result) {
            bt.loading.hide();
            parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
              parent.layer.closeAll();
              //解除已提交的标志
              $(data.elem).parent().removeClass('isSubmiting');
              if (result.success) {
                if (bt.navtab) {
                  bt.navtab.closeCurrent();
                } else {
                  parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
              }
            })
          }, 'JSON');
        }
      })
    } else {
      var signDataMsg = signData("外汇交易提交", 11);
      if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
      }

      var action = "/foreign/fextranreq/modify";
      var corpAccCurId = $("#corpAccCurId").val();//主体单位账号币种
      var cotpAccCurId = $("#cotpAccCurId").val();//交易对手账号币种
      var curId = selectType.get("curId").val();//交易币种
      if (corpAccCurId != cotpAccCurId) {
        //跨币种交易，是否继续?
        bt.loading.hide();
        parent.layer.confirm(bt.lang('foreign.message.confirm.cur'), function (index) {
          bt.loading.show();
          // $('#inputForm').serialize()
          $.post(basePath + action, getFormFields(data.field), function (result) {
            bt.loading.hide();
            parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
              parent.layer.closeAll();
              //解除已提交的标志
              $(data.elem).parent().removeClass('isSubmiting');
              if (result.success) {
                if (bt.navtab) {
                  bt.navtab.closeCurrent();
                } else {
                  parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
              }
            })
          }, 'JSON');
        }, function () {
          //取消已提交标志
          $(data.elem).parent().removeClass('isSubmiting');
        });
      } else if (corpAccCurId == cotpAccCurId && corpAccCurId != curId) {
        //交易币种与交易主体单位/交易对手账号币种不一致，是否继续?
        bt.loading.hide();
        parent.layer.confirm(bt.lang('foreign.message.confirm.cur1'), function (index) {
          bt.loading.show();
          // $('#inputForm').serialize()
          $.post(basePath + action, getFormFields(data.field), function (result) {
            bt.loading.hide();
            parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
              parent.layer.closeAll();
              //解除已提交的标志
              $(data.elem).parent().removeClass('isSubmiting');
              if (result.success) {
                if (bt.navtab) {
                  bt.navtab.closeCurrent();
                } else {
                  parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
              }
            }, function () {
              //取消已提交标志
              $(data.elem).parent().removeClass('isSubmiting');
            })
          }, 'JSON');
        }, function () {
          //取消已提交标志
          $(data.elem).parent().removeClass('isSubmiting');
        });
      } else {
        //  $('#inputForm').serialize()
        $.post(basePath + action, getFormFields(data.field), function (result) {
          bt.loading.hide();
          parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
            parent.layer.closeAll();
            //解除已提交的标志
            $(data.elem).parent().removeClass('isSubmiting');
            if (result.success) {
              if (bt.navtab) {
                bt.navtab.closeCurrent();
              } else {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
              }
            }
          }, function () {
            //取消已提交标志
            $(data.elem).parent().removeClass('isSubmiting');
          })
        }, 'JSON');
      }
    }

    return false;
  });

  /**
   * 计划明细控制项
   */
  var dtlColSels = [];
  if ($("#dtlColSel").val()) {
    bt.each($("#dtlColSel").val().split(","), function (i, col) {
      dtlColSels[col] = col;
    });
  }

  /**
   * 生成公共的属性
   * @return
   */
  $genOtherHtml = function () {
    var corpId = $("#corpIdDef").val();
    if ($('input[name="corpId"]').val()) {
      corpId = $('input[name="corpId"]').val();
    }
    var curId = $("#curIdDef").val();
    if (selectType.get("curId").val()) {
      curId = selectType.get("curId").val();
    }
    var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
    var param = {
      corpId: corpId,
      itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
      billNo: $("#billNo").val(),
      businessName: $("#businessName").val(),
      processSysname: $("#processSysname").val(),
      processDate: dateType.get("settleDate").val(),
      type: 'select',
      detailControl: true,
      curId: curId,
      wishPayday: dateType.get("settleDate").val(),
      payerAcc: $("#corpBankAcc").val(),
      payeeAcc: $("#cotpBankAcc").val(),
      date: (new Date()).getTime()
    };
    $.ajax({
      url: url,
      method: 'POST',
      data: param,
      async: true,
      success: function (json) {
        $("#tagItemDetail").html("");
        $("#tagItemDetail").html(json);
      }
    });
  };
  renderBudget();

  /**
   * 资金计划挂接
   */
  function renderBudget() {
    //判断外币汇款是否挂接资金计划
    var budgetSel = $("#budgetSel").val();
    var cdSign = $("#cdSignDef").val();
    if (bt.dicts.get('cdSign').val()) {
      cdSign = bt.dicts.get('cdSign').val();
    }
    if (budgetSel == "1" && cdSign == "1") {
      $genOtherHtml();
    } else {
      $("#tagItemDetail").html("");
    }
  }

  /**
   * 重置按钮回调
   */
  resetCallBack = function () {
    renderBudget();
    //$("#nameZhDiv").text('');
    // $("#cotpAdrrDiv").text('');
    // $("#corpAccNameDiv").text('');
    // $("#cotpBankAdrrDiv").text('');
    // $("#cotpSwiftCodeDiv").text('');
    // $("#cotpAgentBankDiv").text('');
    // $("#cotpAgentBankAddrDiv").text('');
    // $("#cotpAgentBankAccDiv").text('');

    $("#corpAccNameDiv").text('');
    $("#oppId").val('');
    $("#cotpName").val('');
    $("#cotpNameEn").val('');
    $("#cotpBankName").val('');
    $("#cotpBankAcc").val('');
    $("#cotpAccCurId").val(''); //账户币种
    $("#cotpAccCurNameDiv").text('');
    $("#cotpAdrr").val(''); //交易对手地址
    /*$("#cotpAdrrDiv").text('');*/
    $("#cotpCountry").val('');//交易对手所在国家地区&地点
    $("#cotpProv").val('');
    $("#cotpCity").val('');
    $("#cotpCountryDiv").text('');
    $("#cotpProvDiv").text('');
    $("#cotpCityDiv").text('');
    $("#countryId").val('');//交易对手银行所在国家地区&地点
    $("#nameZhBankDiv").text('');
    $("#cotpSwiftCodeDiv").text('');//交易对手银行SWIFT CODE
    $("#localClearNoDiv").text('');//本地清算号
    // $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
    // $("#cotpAgentBankDiv").text('');//交易对手代理行
    // $("#cotpAgentBankAddrDiv").text('');//交易对手代理行地址
    // $("#cotpAgentBankAccDiv").text('');//交易对手代理行账户
  }

  /**
   * 生成自定义信息
   * @return
   */
  $genCustHtml = function () {
    var id = $('#id').val() || $('#cloneId').val();
    if (!id) {
      return false;
    }
    var url = basePath + "/foreign/fextranreqCust/updateCustInfo/" + id + "?timestamp=" + new Date().getTime();
    $.ajax({
      url: url,
      method: 'GET',
      async: false,
      success: function (json) {
        $("#custMsg").html("");
        $("#custMsg").html(json);
        bt.dateType.init();
        renderIcon();
        form.render();
      }
    });
  }
  $genCustHtml();
  dict = bt.dicts.init();
  //初始化单位
  corp = bt.corp.init();
  moneyType = bt.moneyType.init();
  moneyFun = bt.moneyType.getFun();
  // 初始化汇款类型对应报文信息显隐与校验
  setReportInfo();
  // 初始化根据付款类型设置最迟装运日期必填（未隐藏时才必填）
  if (bt.dicts.get("payType").val() == "1" && $("input[name='shipDate']").is(':visible')) {
    $("input[name='shipDate']").attr("lay-verify", "required");
    renderIcon();
  }
  // 招商云
  setZSYBankExtend(true);
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
  if ($("#editPayMoneyFlag").val() == "1") {
    bt.moneyType.get("payMoney").val(data);
  }
}

// 绘制表单必填*
function renderIcon() {
  var vElem = $(".layui-tab-content").find('*[lay-verify],*[data-required = "true"]');//如果data-required = true 也需要设置必须项
  // $(".layui-tab-content").find('.ignore-required-icon .required').remove();
  $(".layui-tab-content").find('.required').remove();
  layui.each(vElem, function (_, item) {
    var $this = $(this);
    //如果父元素有class .ignore-required-icon return
    if ($(this).parents('.ignore-required-icon').length === 1 || $(this).hasClass('ignore-required-icon')) {
      return;
    }
    var formLabel = $this.closest(".form-input").siblings(".form-label");
    var ver = $this.attr('lay-verify') !== undefined ? $this.attr('lay-verify') : $this.data('required');
    if ((typeof ver === 'string' && ver.indexOf("required") != -1) || (ver == "true" || ver === true)) {
      var text = formLabel.html();
      if (text && text.indexOf("*") == -1) {
        formLabel.prepend('<span class="required">*</span>')
      }
    } else {
      formLabel.find('.required').remove();
    }
  })
}

// 模块选择回调方法 根据配置更新表单
// 模板选择回调方法 根据配置更新表单
function updateFormByConfig(row) {
  row = row || {};

  isSeleTemplate = true;

  // 1. 获取行数据
  var templateName = row.templateName || '';
  var id = row.id || '';
  var versionNo = row.versionNo || '';

  // 2. 获取页面表单值
  var oldCorpBank = $('[name="corpBank"]').val() || '';
  var corpAccCurId = $('[name="corpAccCurId"]').val() || '';
  var corpAccId = $('[name="corpAccId"]').val() || '';
  var accType = $('[name="accType"]').val() || '';
  var oldTemplateId = $('#templateId').val() || '';
  var corpBankAcc = $('#corpBankAcc').val() || '';
  var oldBankPayType = $('[name="bankPayTypeId"]').val() || '';
  var corpAccCurName = $('#corpAccCurNameDiv').text()
  var oldPayerCountryId = $("#payerCountryId").val();
  var payerCountryId = row.countryId;

  // 3. 赋值页面
  $('#templateName').val(templateName).attr('title', templateName);
  $('#templateId').val(id);
  $('#versionNo').val(versionNo);
  $("#payerCountryId").val(row.countryId);

  // 设置支付类别
  bt.selectType.get('bankPayTypeId').attr({
    "data-value": "",
    "data-url": basePath + "/basic/business/btbanktype/listByCondition?bankType="+row.bankTypeId+"&bifCode="+row.bifId
        +"&countryId="+row.countryId
  });
  var bankTypeId = row.bankTypeId || ''
  bt.selectType.get('corpBank').val(bankTypeId);


  var url = basePath + "/foreign/fextranreqCust/getTemplate?"
      + "templateId=" + id
      + "&oldTemplateId=" + oldTemplateId
      + "&corpBankAcc=" + corpBankAcc
      + "&bankPayType=" + row.payTypeId
      + "&versionNo=" + versionNo
      + "&corpBank=" + bankTypeId
      + "&corpAccCurId=" + corpAccCurId
      + "&corpAccCurName=" + corpAccCurName
      + "&corpAccId=" + corpAccId
      + "&accType=" + accType
      + "&templateName=" + encodeURI(templateName);

  // 4. 发送请求
  $.get(url, function (result) {
    var res;
    if(typeof result  == 'object'){
      res = result;
    }else{
      res = JSON.parse(result);
    }
    if (!res || !res.success || !res.obj) {
      return;
    }

    var obj = res.obj.fexTranReq;
    var templateId = obj.templateId || '';
    var versionNo = obj.versionNo || '';
    var corpAccId = obj.corpAccId || '';
    var newCorpBankAcc = obj.corpBankAcc || ''
    var corpBank = obj.corpBank || ''
    var corpAccCurId = obj.corpAccCurId || ''
    var corpAccCurName = obj.corpAccCurName || ''

    if(templateId){
      $('#templateId').val(templateId);
      $('#versionNo').val(versionNo);
    } else {
      $('#templateName').val('').attr('title', '');
      $('#templateId').val('');
      $('#versionNo').val('');
      $("#payerCountryId").val('');
    }

    // 支付类型 bankPayType 主体单位银行 corpBank 账户币种 corpAccCurId / corpAccCurNameDiv 账户名称 corpBankAcc/corpAccId
    // 账户类别 accType，以上值要以后台值为准
    bt.selectType.get('bankPayTypeId').val(obj.bankPayTypeId);
    $("#bankPayType").val(obj.bankPayType);
    $("#bankPayTypeName").val(obj.bankPayTypeName);
    bt.selectType.get('corpBank').val(corpBank);
    $('[name="corpAccCurId"]').val(corpAccCurId);
    $('#corpAccCurNameDiv').text(corpAccCurName);

    // 根据最新的newCorpBankAcc 进行赋值,如果新旧值没有变化，就不会改变，如果新值为空字符串，则清空了，必填项要重新选择
    $('[name="corpBankAcc"]').val(newCorpBankAcc);
    $('[name="corpAccId"]').val(corpAccId);
    if(!newCorpBankAcc){
      $('#corpAccCurNameDiv').text('');
      $('#corpAccNameDiv').val('');
    }

    if(oldPayerCountryId && (oldPayerCountryId != payerCountryId)){
      $('[name="corpBankAcc"]').val('');
      $('[name="corpAccId"]').val('');
      if(!newCorpBankAcc){
        $('#corpAccCurNameDiv').text('');
        $('#corpAccNameDiv').val('');
      }
    }

    if(oldCorpBank && (oldCorpBank != corpBank)){
      $('[name="corpBankAcc"]').val('');
      $('[name="corpAccId"]').val('');
      if(!newCorpBankAcc){
        $('#corpAccCurNameDiv').text('');
        $('#corpAccNameDiv').val('');
      }
    }


    // 重置银行类型
    if(res.obj.bankType){
      setcorpBank(res.obj.bankType, row.countryId);
    }

    // 重置客商账号组件
    if (obj.bankPayType){
      resetExternalAccCorp(obj.bankPayType);
    }

    // 更新模板
    if (templateId && versionNo) {
      loadTemplateConfig(templateId, versionNo,true);
    }
  });
}

var commonCompnonets = ['bt-biz-selectType', 'bt-biz-corp', 'bt-biz-dict','div'];
var specialGroups = ["corpInfo", "cotpInfo"]
var specialRquired = {
    corpBankAcc:'required|tableData',
    tranName1:'required|tableData'
}
var newLayoutHmtl = {}
var reportInfoRequiredFields = [];
var reportInfoVisibleFields = [];
// 更新基础信息
function updateLayout(baseGroup,beforeInit) {
    var fields = baseGroup.children; // 子字段列表
    var fragment = document.createDocumentFragment();
    var $target = $('#rasBackQuery'); // 交易详细信息容器

    if(fields){
        fields.forEach(function (item) {
            if(item.children && item.children.length){
                updateLayout(item,beforeInit)
            }else {
                var componentName = item.dataClass; // 组件名称
                var childCode = item.fieldCode;
                var isLength = item.isLength;
                var isLimitedDict = item.isLimitedDict;
                var isRequired = item.isRequired;
                var isVisible = item.isVisible;
                var fieldDefVal = item.fieldDefVal || '';
                var $childDom = $target.find('.form-line[data-name="' + childCode + '"]');
                if (!$childDom.length) return;

                // 如果lable 文字超过15个字时，将添加title属性
                var lableTitle = $childDom.find('.form-label').text();
                if(lableTitle && lableTitle.length > 15){
                    $childDom.find('.form-label').attr('title',lableTitle)
                }

                var isDict = componentName === 'bt-biz-dict';
                if(['bt-biz-selectType', 'bt-biz-dict'].indexOf(componentName) !== -1){
                  if (isRequired) {
                    $childDom.find('.bt-biz-dict').attr('data-required', 'true');
                  } else {
                    $childDom.find('.bt-biz-dict').removeAttr('data-required');
                  }
                }
                var isFormElem = commonCompnonets.indexOf(componentName) === -1 // 是否为表单 input / select /
                if(specialGroups.indexOf(baseGroup.fieldCode) === -1){
                    // 1. 先找到目标元素
                    var $formLine = $target.find('.form-line[data-name="' + childCode + '"]');

                    // 2. 如果元素不存在，直接跳过
                    if (!$formLine.length) return;

                    // 3. 获取父元素
                    var $parent = $formLine.parent();

                    // 4. 如果父元素不是 top-col，就包裹一层
                    if (!$parent.hasClass('top-col')) {
                        $formLine.wrap('<div class="top-col"></div>');
                        $childDom = $formLine.parent(); // 包裹后重新取父级
                    } else {
                        $childDom = $parent; // 已经是 top-col
                    }
                }

                if(isVisible ){
                    $childDom.removeClass('layui-hide');
                    if(baseGroup.fieldCode === 'reportInfo'){
                      reportInfoVisibleFields.push(childCode)
                    }
                }else{
                    $childDom.addClass('layui-hide')
                }
                if(isRequired){
                    if(!$childDom.find('.required')[0]){
                        $childDom.find('.form-label').prepend('<span class="required">*</span>')
                    }

                  // 当为申报信息时，如果为必填，则缓存起来；
                  if(baseGroup.fieldCode === 'reportInfo'){
                    reportInfoRequiredFields.push(childCode)
                  }
                }else {
                    $childDom.find('.required').remove();
                }
                // 表单元素 input：文本字段用 data-maxlength + strlength（汉字算2个字符），数字字段仍用 maxlength
                if(isFormElem){
                    var $fieldInput = $childDom.find('input,textarea').first();
                    if($fieldInput.length){
                        if(item.fieldCode==='contractNo'||item.fieldCode==='foreignContractNo'||item.fieldCode==='busNo'
                            ||item.fieldCode==='declareNo' ||item.fieldCode==='invoiceNo' || item.fieldCode==='corpContactTel'){
                            $fieldInput.removeAttr('maxlength').attr('data-maxlength', 50);
                            var verify = $fieldInput.attr('lay-verify') || '';
                            if (verify.indexOf('strlength') === -1) {
                                $fieldInput.attr('lay-verify', verify ? (verify + '|strlength') : 'strlength');
                            }
                        }else   if(item.fieldCode === 'contractRate'){
                            $fieldInput.attr('maxlength',7)
                        }else   if(item.fieldCode === 'rptGateDate'){
                            $fieldInput.attr('maxlength',9)
                        }else {
                            if(isLength && item.fieldLength){
                                $fieldInput.removeAttr('maxlength').attr('data-maxlength', item.fieldLength);
                                var verify = $fieldInput.attr('lay-verify') || '';
                                if (verify.indexOf('strlength') === -1) {
                                    $fieldInput.attr('lay-verify', verify ? (verify + '|strlength') : 'strlength');
                                }
                            }
                        }
                    }
                }

                if(beforeInit){ // 未初始化时
                    if(isLimitedDict && item.fieldLimitedDict){
                        $childDom.find('.'+componentName).data('displayData',item.fieldLimitedDict)
                    }

                    if(isDict || componentName === 'bt-biz-corp' || componentName === 'bt-biz-selectType'){
                        isRequired = isRequired?true:false;
                        var $targetDiv = $childDom.find('.form-input> div');
                        if($targetDiv.find('*')[0]){ // 如果元素里面含有子元素，直接找childCode
                          $targetDiv.find('[data-name="'+childCode+'"]').data('required',isRequired)
                        }else{
                          $targetDiv.data('required',isRequired)
                        }

                    }

                }else{ // 初始化之后更改参数
                    var dictRestAttr = null;
                    if(isDict && isLimitedDict && item.fieldLimitedDict){
                        dictRestAttr = {
                            'data-displayData':item.fieldLimitedDict
                        }
                    }
                    // 必填
                    var requiredCode = specialRquired[childCode];
                    if(requiredCode){ // 下拉表格
                        var $specialInput = $childDom.find('[name="'+childCode+'"]');
                        setLayVerifyWithStrlength($specialInput, isRequired, requiredCode);
                    }else if(isDict){
                        if(!dictRestAttr){
                            dictRestAttr = {}
                        }
                        dictRestAttr['data-required'] = isRequired?'true':'false'
                        dictRestAttr['data-defaultvalue'] = fieldDefVal
                        var bizName = $childDom.find('.'+componentName).data('biz-name')
                        if(bizName){
                            bt.dicts.get(bizName).attr(dictRestAttr);
                        }

                    }else if(componentName === 'bt-biz-corp'){
                        var corpInput = $childDom.find('[name="'+childCode+'"]').siblings('.select-tree-input');
                        if(isRequired){
                            corpInput.attr('lay-verify','required');
                        }else{
                            corpInput.removeAttr('lay-verify');
                        }
                    }else if(componentName === 'bt-biz-selectType'){
                        // 只处理必填，默认值暂时不处理
                        var bizName = $childDom.find('.'+componentName).data('biz-name')
                        if(bizName){
                            bt.selectType.get(bizName).setAttr({
                                'data-required':isRequired?true:false
                            })
                        }
                    }
                }
                if(isFormElem){ // 如果是表单 input / select；设置必填时保留 strlength（配合 data-maxlength）
                    var $formInput = $childDom.find('[name="'+childCode+'"]');
                    if (!$formInput.length) {
                        $formInput = $childDom.find('input,textarea').first();
                    }
                    setLayVerifyWithStrlength($formInput, isRequired, 'required');
                }
                if ($childDom.length) {
                    fragment.append($childDom[0]);
                }
            }
        })
    }

    // $('#'+baseGroup.fieldCode).html('')
   // $('#'+baseGroup.fieldCode).append(fragment)
    newLayoutHmtl[baseGroup.fieldCode] = fragment;
}
/*
*
* 是否控制长度	：isLength  fieldLength
是否限制枚举值：isLimitedDict   fieldLimitedDict
默认值：fieldDefVal
是否必填：isRequired
是否可见： isVisible
* beforeInit 是否初始化完成
* */
function loadTemplateConfig(templateId,vNo,beforeInit) {
  var id = templateId || $('#templateId').val();
  var versionNo = vNo || $('#versionNo').val();
  if(id){
    var url = basePath + '/foreign/fexTranReqConfig/getTemplateConfigByAereTree/'+id;
    if(versionNo){
      url = url + '/' + versionNo;
    }
    $.ajaxSettings.async = false;
    bt.loading.show();
    $.get(url, function (result) {
      console.log(result)
      if (result.success) {
        var datas = result.obj && result.obj || []
        datas.forEach(function (item) {
          if(item.fieldCode !== 'otherInfo'){
              // custInfo --> otherInfo
              if(item.fieldCode === 'custInfo'){
                  item.fieldCode = 'otherInfo'
              }
            updateLayout(item,beforeInit)
          }else if(item.fieldCode === 'otherInfo'){
            updateCustomInfo(item.children,beforeInit)
          }
        })
          for(var key in newLayoutHmtl){
              $('#'+key).html('');
              $('#'+key).append(newLayoutHmtl[key])
          }
          newLayoutHmtl = {};
      }
    }, 'JSON');
    $.ajaxSettings.async = true;
    bt.loading.hide();
  }else{
    // 如果没有模块ID，就初始化的与新增页面一样的
    initFormShow();
  }
}


/**
 *
 * 重置客商账号组件
 *
 * @param bankPayType
 */
function resetExternalAccCorp(bankPayType){
  if(!bankPayType){
    bankPayType = $("#bankPayType").val();
  }
  var isFps = "FPS" == bankPayType || "fps" == bankPayType;
  // FPS 时交易对手账户与 FPS ID 二选一，账户本身不再单独必填
  $("#cotpBankAcc").attr("lay-verify", isFps ? "" : "required");
  if ("PAYPAL" == bankPayType || "paypal" == bankPayType){
    allExternalAccCorp.set({
      "type": "payPal",
    });
  } else if(isFps){
    allExternalAccCorp.set({
      "type": "outerBankAcc,fps",
    });
  } else {
    allExternalAccCorp.set({
      "type": "innerBankAcc,outerBankAcc,corpBankAcc",
    });
  }
  renderIcon();
}


// 创建自定义表单
function updateCustomInfo(config) {
  var html = '';
  if(config){
    var custFields = $('#templateCustFields').val();
    if(custFields){
      custFields =JSON.parse(custFields)
    }
    bt.each(config,function (i,item) {
      html += getcomponentHtml(item,custFields);
    })
  }
  $('#custInfo').html(html);
  if(html){
     initCustomInfo()
  }
}
// 根据配置创建hmtl
var requiredHtml = '<span class="required">*</span>'
function getcomponentHtml(item,customValueObj){
  var dataClass = item.dataClass; // 组件名称
  var fieldCode = item.fieldCode;
  // var isLength = item.isLength;
  var isLimitedDict = item.isLimitedDict; // 显示值
  var isRequired = item.isRequired;
  var isVisible = item.isVisible;
  var dataType = item.dataType;
  var fieldDictGroup = item.fieldDictGroup;
  var fieldName = item.fieldName;
  var fieldLength = item.fieldLength;
    if (item.fieldCode === 'contractNo' || item.fieldCode === 'foreignContractNo' || item.fieldCode === 'busNo'
        || item.fieldCode === 'declareNo' || item.fieldCode === 'invoiceNo') {
        fieldLength = 50;
    } else if (item.fieldCode === 'contractRate') {
        fieldLength = 7;
    }else if (item.fieldCode === 'rptGateDate') {
        fieldLength = 9;
    }


  var html = '<div class="top-col '
  if(!isVisible){
    html += ' layui-hide'
  }
  html += '">'
  html += '<div class="form-line" data-name="'+fieldCode+'">'
  html += '<div class="form-label">'
  if(isRequired){
    html += requiredHtml
  }
  html += fieldName
  html += '</div>'
  html += '<div class="form-input">'
  if(dataClass === 'bt-biz-dict'){
    html += '<div class="'+dataClass+'" data-name="'+fieldCode+'" data-type="'+dataType+'" data-group="'+fieldDictGroup+'" '
    if(isLimitedDict){
      html += 'data-displaydata="'+item.fieldLimitedDict +'" '
    }
    if(isRequired){
      html += 'data-required="true" '
    }
    if(customValueObj && customValueObj[fieldCode]){
      html += 'data-value="'+customValueObj[fieldCode]+'" '
    }
    html += '></div>'
  }else if(dataClass === 'bt-biz-dateType'){
    if(dataType === 'day'){
      dataType = 'date'
    }else if(dataType === 'daytime'){
      dataType = 'datetime'
    }
    html += '<input type="text" class="layui-input '+dataClass+'" name="'+fieldCode+'" data-type="'+dataType+'" '
    if(isRequired){
      html += 'lay-verify="required" '
    }
    if(customValueObj && customValueObj[fieldCode]){
      html += 'value="'+customValueObj[fieldCode]+'" '
    }
    html += '>'
  }else if(dataClass === 'bt-biz-moneyType'){
    html += '<input type="text" class="layui-input '+dataClass+'" data-name="'+fieldCode+'" '
    if(isRequired){
      html += 'lay-verify="required" '
    }
    if(customValueObj && customValueObj[fieldCode]){
      html += 'value="'+customValueObj[fieldCode]+'" '
    }

    html += '>'
  } else if(dataType === 'text'){
    html += '<input type="text" class="layui-input" name="'+fieldCode+'" '
    if(isRequired){
      html += 'lay-verify="required" '
    }
      if(fieldLength){
          html += 'maxlength="'+fieldLength+'" '
      }
    if(customValueObj && customValueObj[fieldCode]){
      html += 'value="'+customValueObj[fieldCode]+'" '
    }
    html += '>'
  }
  html += '</div></div></div>'
  return html
}

function initCustomInfo() {
  bt.dicts.init();
  bt.dateType.init();
  bt.moneyType.init();
}
// 重新组装表单数据：分离自定义字段与普通字段
function getFormFields(fields) {
  // 深拷贝原始数据，避免修改源对象
  var data = JSON.parse(JSON.stringify(fields || {}));

  // 企业自定义信息为异步加载内容，提交时重新采集，避免 Layui 未收集动态表单项
  $('#custMsg').find('input[name], select[name], textarea[name]').each(function () {
    data[this.name] = $(this).val();
  });

  // 获取自定义信息容器内所有表单元素
  var $fieldElems = $('#custInfo').find('input, select, textarea');
  var fexCustInfos = {};

  // 遍历自定义表单元素，抽取到 fexCustInfos 中
  bt.each($fieldElems, function (_, item) {
    var name = item.name;
    // 没有 name 属性直接跳过
    if (!name) return;

    // 赋值并从主数据中移除
    fexCustInfos[name] = data[name];
    delete data[name];
  });

  // 将自定义字段集合挂载到主数据
  data.fexCustInfos = fexCustInfos;
  data.status = $('#status').val();
  return data;
}

// 初始化表单展示（根据配置显示/隐藏字段）
function initFormShow() {
  var HIDE_CLASS = 'layui-hide';

  // 1. 统一隐藏容器
  var containerIds = ['costInfo', 'reportInfo', 'otherInfo', 'custInfo'];
  bt.each(containerIds, function (i, item) {
    $('#' + item).addClass(HIDE_CLASS);
  });

  // 2. 统一隐藏所有默认字段
  $('#baseInfo > .top-col, #corpInfo > .form-line, #cotpInfo > .form-line, #fexTranInfo > .top-col').addClass(HIDE_CLASS);

  // 需要显示的字段配置
  var showFields = {
    baseInfo: ['templateId','remitType','corpId','cotpName','corpBank','cotpBankName','corpBankAcc','cotpBankAcc','corpAccName','cotpEnName','corpAccCurId','cotpAccCurName','bankPayTypeId','isPayed'],
    fexTranInfo: ['curId','payMoney','tranDate','settleDate','rmkLocal']
  };

  // 3. 显示基础信息字段
  bt.each(showFields.baseInfo, function (i, item) {
    var $target = $('#baseInfo1').find('.form-line[data-name="' + item + '"]');
    $target.is('.top-col .form-line') ? $target.parent().removeClass(HIDE_CLASS) : $target.removeClass(HIDE_CLASS);
  });

  // 4. 显示交易信息字段
  bt.each(showFields.fexTranInfo, function (i, item) {
    $('#fexTranInfo').find('.form-line[data-name="' + item + '"]').parent().removeClass(HIDE_CLASS);
  });

  // 5. 支付类型标题赋值
  $('#bankPayTypeTd').html(bt.lang('table.nisBillhead.bankPayType'));

  //自定义信息清空
  $('#custInfo').html('');
}

// 是否在申报信息是必填字段
function isReportRequiredField(field){
  if (!field || typeof field !== 'string') {
    return false;
  }
  return reportInfoRequiredFields.indexOf(field) !== -1 && reportInfoVisibleFields.indexOf(field) !== -1
}

// 是否在申报信息可见字段
function isReportVisibleField(field){
  if (!field || typeof field !== 'string') {
    return false;
  }
  return reportInfoVisibleFields.indexOf(field) !== -1
}

function setRptGateEntId(){
  //报关经营单位名称&报关方式：如果“一般贸易金额”、“进料加工金额”、“其他贸易金额”、“边境贸易金额”有值时，必填
  var generalTradeAmount = moneyType.get('generalTradeAmount').val();
  var feedAmount = moneyType.get('feedAmount').val();
  var otherTradeAmount = moneyType.get('otherTradeAmount').val();
  var borderTradeAmount = moneyType.get('borderTradeAmount').val();
  if (generalTradeAmount || feedAmount || otherTradeAmount || borderTradeAmount) {
    var tranCode1 = $('#tranCode1').val();
    var tranCode2 = $('#tranCode2').val();
    if ((tranCode1 && tranCode1.substring(0, 1) == '1') || (tranCode2 && tranCode2.substring(0, 1) == '1')) {
      setRequired('rptGateEntId',isReportRequiredField('rptGateEntId'));
      setRequired('rptGateType',isReportRequiredField('rptGateType'));
    } else {
      setRequired('rptGateEntId',false);
      setRequired('rptGateType',false);
    }
  } else {
    setRequired('rptGateEntId',false);
    setRequired('rptGateType',false);
  }
  renderIcon();
}


function setPayType(isShow){
  var tranCodeEles = $(".payType1");
  for (var i=0; i < tranCodeEles.length; i++){
    var $tranCodeEle = $(tranCodeEles[i]);
    var $input = $tranCodeEle.find("input");
    var dataName = $input.data("name");
    if(isReportVisibleField(dataName) && isShow){
      $tranCodeEle.show();
    } else {
      $tranCodeEle.hide();
    }
  }
}

function setTranCode(clsSuffix, isShow) {
  var tranCodeEles = $(".tranCode" + clsSuffix);
  for (var i = 0; i < tranCodeEles.length; i++) {
    var $tranCodeEle = $(tranCodeEles[i]);
    var eleId = $tranCodeEle.attr("id")
    var $input = $tranCodeEle.find("input");
    var dataName = $input.data("name");
    if ("rptGateEntId" === eleId || "rptGateType" === eleId){
      if(isReportVisibleField(eleId) && isShow){
        $tranCodeEle.show();
      } else {
        $tranCodeEle.hide();
      }
    }
    else if (isReportVisibleField(dataName) && isShow) {
      $tranCodeEle.show();
    } else {
      $tranCodeEle.hide();
    }
   if(dataName === 'rptGateDate'){
          $input.attr("maxlength", 9);
      }
  }
}


function setRequired(name,isRequired){
  var currentElm = $('[name="'+name+'"]');
  if(currentElm[0]){
    if(currentElm.parent('div').hasClass('bt-biz-dict')){ // dict
      currentElm.parent('div').data('required',isRequired)
    }else if(currentElm.siblings('.select-tree-input')[0]){ // corp
      currentElm = currentElm.siblings('.select-tree-input');
    }
    setLayVerifyWithStrlength(currentElm, isRequired, 'required');
  }
}

/**
 * 设置必填校验时保留 data-maxlength 对应的 strlength，避免只剩 data-maxlength 却无校验
 */
function setLayVerifyWithStrlength($el, isRequired, baseVerify) {
  if (!$el || !$el.length) return;
  var hasMaxLen = $el.attr('data-maxlength') || $el.data('maxlength');
  if (isRequired) {
    var verify = baseVerify || 'required';
    if (hasMaxLen && (verify + '').indexOf('strlength') === -1) {
      verify = verify + '|strlength';
    }
    $el.attr('lay-verify', verify);
  } else if (hasMaxLen) {
    $el.attr('lay-verify', 'strlength');
  } else {
    $el.removeAttr('lay-verify');
  }
}
