var rowNum=$("#rowIndex").val();
var addRow;
var dicts;
bt.use(['form', 'layedit', 'dateType','corp',"selectTableType", 'selectTable', 'bank', 'rateType','bankInfo',
    'moneyType', 'externalCorpBankAcc', 'btUpload', 'dicts','bttable','selectType', 'inputExternalCorpBankAcc'], function () {
         var $ = bt.jquery, form = bt.form();
         dicts =bt.dicts.init()
         $("#tagItemDetailDiv").hide();
         var timeStamp = Date.parse(new Date());
         var dtlColSels = [];
         $('#timeStamp').val(timeStamp);
         var up = bt.btUpload.init({ elem: "#uploader", auto: false, gid: '' });
         var up2 = bt.btUpload.init({ elem: "#uploader2", auto: false, gid: '' });
        if ($("#dtlColSel").val()) {
            bt.each($("#dtlColSel").val().split(","), function (i, col) {
                dtlColSels[col] = col;
            });
        }
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
                                $("#accCorpId").val(data.obj.userAccountCode);
                                bankAccInit.get("payeeAcc").reload({corpId: $("#accCorpId").val()});
                            }
                        }
                        form.render();
                    }
                });
            if(id){
                var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
                bt.selectType.get("batchBusType").attr("data-url", busTypeUrl);
            }
           //清空工作表
           $("#payerBankNo").val("");
           $("#payerBankName").val("");
           $("#externalName").val("");
           $("#externalCode").val("");
           $("#payerAccNo").val("");
           $("#payerAccName").val("");
           $('#payeeAcc').val("");
           $('#payeeAccName').val("");
           $('#payeeBankName').val("");
           }





        //业务种类初始化前回调
        busBefore = function (data,arguments) {
            var show = "("+arguments.busType+")" + arguments.busTypeName;
            return show;
        }

        //合同单号
        contractCodeBefore = function (data,arguments) {
            var show = "("+arguments.conNo+")" + arguments.conName;
            return show;
        }


//  本方账号
   var bankAccInit = bt.selectTable.init({
        elem: ".bankAccTable",
        url: basePath + "/accountReceivable/docManage/getPayerAcc",
        urldata: {corpId: $("#accCorpId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: ".payeeAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payeeAcc').val(data.bankAcc);
                $('#payeeAccName').val(data.accName);
                $('#payeeBankName').val(data.bankName);
                if (dtlColSels["payeeAcc"] || (data.accName && dtlColSels["payeeAccName"]) || (data.bankName && dtlColSels["payeeBankName"])) {
                    renderBudget();
                }
            } else {
                $('input.text').val('');
            }
        }
    });


       /** 业务类别回调
       * @param data
       */
      busTypeCallback = function (data) {
          $("#batchBusType").val(data);
          renderBudget();
      }
      curCallback = function (data){
       $("#curId").val(data.id);
        renderBudget();
      }

    /**
     * 供应商查询
     * @param value
     */
    seachExternalCorpBankAcc = function (value) {
        value = encodeURI(value);
        var url = basePath + "/sys/externalCorp/listByInputExternalCorp?corpId=" + corp.get("corpId").val() + "&seach=" + value;
        inputExternalCorpBankAcc.get("payerAccNo").setUrl({url: url});
    }
     /**
     * 供应商查询选择回调
     * @param data
     * @param dom
     *
     */
    inputExternalCorpCallback = function (value) {
        if (value) {
            var data = value.data;
            $("#payerAccNo").val(data.externalAcc);
            $("#payerAccName").val(data.name);
            $("#payerBankNo").val(data.bankSourceCode);
            $("#payerBankName").val(data.bank);
            $("#externalName").val(data.name);
            $("#externalCode").val(data.code);
        }
    }

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") { // 选择客商信息
            $("#payerAccNo").val(data.externalAcc);
            var name = data.name;
            if(data.externalAccName){
                name = data.externalAccName;
            }
            inputExternalCorpBankAcc.get('payerAccName').val(name);
            $("#payerBankNo").val(data.bankSourceCode);
            $("#payerBankName").val(data.bank);
            $("#externalName").val(data.name);
            $("#externalCode").val(data.code);
            dicts.get('externalType').val(data.externalType);
        }else{
            $("#payerAccNo").val(data.bankAcc);
            inputExternalCorpBankAcc.get('payerAccName').val(data.accName);
            $("#payerBankNo").val(data.bankCode);
            $("#payerBankName").val(data.bankName);
            $("#externalName").val(data.name);
            $("#externalCode").val(data.code);
            dicts.get('externalType').val(data.type);
        }
    };
//    单独选择客商名称
bt.externalCorpNameCallback = function (data, dom) {
        if (typeof data.netId === "undefined") { // 选择客商信息
            $("#externalName").val(data.name);
            $("#externalCode").val(data.code);

            $("#payerAccNo").val(data.externalAcc);
            $("#payerAccName").val(data.accName);
            $("#payerBankNo").val(data.bankSourceCode);
            $("#payerBankName").val(data.bankName);

            dicts.get('externalType').val(data.externalType);
        }else{
            $("#externalName").val(data.name);
            $("#externalCode").val(data.code);

            $("#payerAccNo").val(data.externalAcc);
            $("#payerAccName").val(data.name);
            $("#payerBankNo").val(data.bankSourceCode);
            $("#payerBankName").val(data.bank);
            dicts.get('externalType').val(data.type);
        }
    };



    var inputExternalCorpBankAcc = bt.inputExternalCorpBankAcc.init();
    var externalCorpBankAcc = bt.externalCorpBankAcc.init();

    bt.externalCorpBankAccParams = function () {
        //type 空是所有 0客商 1单位
        externalCorpBankAcc.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
            type: "",showType:"1"
        });
        return true;
    }

    bt.externalCorpBankAccParams2 = function () {
        //type 空是所有 0客商 1单位
        externalCorpBankAcc.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
            type: "4",showType:"1",showCorp:"0", "flag": "1",
            "externalAcc": $("#payerAccNo").val()
        });
        return true;
    }

    /**
     * 供应商查询
     * @param value
     */
    seachExternalCorpBankAcc = function (value) {
        value = encodeURI(value);
        var url = basePath + "/sys/externalCorp/listByInputExternalCorp?corpId=" + corp.get("corpId").val() + "&seach=" + value;
        inputExternalCorpBankAcc.get("oppAccName").setUrl({url: url});
    }

    //开户行选择
    bt.bankinfoCallback = function (data, dom) {
        $("#payerBankNo").val(data.bankCode);
        $("#payerBankName").val(data.bankName);
    };

    bt.externalTypeCallback = function(data){
        initSupplier(data.value);
    };
    //日期控制
    bt.agingDateCallBack = function(data){
        dateType.get("endDate").attr("data-min",data);
    }

    function initSupplier(value) {
        if (value == 0){
            selectTypes.get("supplierId").attr("data-required",false);
            $("#supplierId").removeAttr('data-required');
            $(".showSupplierId").hide();
        }else {
            $(".showSupplierId").show();
            selectTypes.get("supplierId").attr("data-required",true);
        }
        form.renderIcon();
    }

    addRow();

    $(document).on('click', ".delete-bankAcc", function () {
        var tr = $(this).parents('tr');
        var len = $('#rowBody tr').length;
        tr.remove();
        if(len == 1){
            rowNum = 0;
            addRow();
        }
    });
/**
     * 选择日期回调
     */
    dateCallBack = function (data, $elem) {
        renderBudget();
    }



 /**
     * 初始化列表
     */
    var initEditTable=function(){
        bt.bttable.init({
            scrollX:false,
            scrollY:false,
            elem: "#editTable",
            fixedColumns: {
                rightColumns:1
            },
            buttons: [
            ],
            columns:[],

        });
    }
    initEditTable();


  /**
     * 收款账户值改变后回调
     */
    $("#payeeAcc").change(function () {
        if (dtlColSels["payeeAcc"]) {
            renderBudget();
        }
    });

 /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //判断付款方式是否挂资金计划
            var budgetSel = $("#budgetSel").val();
            if (budgetSel == "1") {
                $("#tagItemDetailDiv").show();
                $genOtherHtml();
            } else {
                $("#tagItemDetail").html("");
            }
//            moneyType.get("receivableMoney").val(0);
        }
    }

   /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $('input[name="corpId"]').val();
        var itemCode = $('input[name="payItem"]').val();
        var defItemCode = $('#defItemCode').val();
        var processDate = dateType.get("receiptDate").val();
        if(!corpId || !processDate){
            return false;
        }
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            corpId: corpId,
            itemCode: defItemCode ? defItemCode : "200",
            defItemCode: defItemCode,
            processDate: processDate,
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            wishPayday: processDate,
            payerAcc: $("#payerAccNo").val(),
            payeeAccName: $("input[name=payerAccName]").val(),
            payeeAcc: $("#payeeAcc").val(),
            payeeBank: $("#payeeAccName").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            }
        });
    };


     /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        $("#tagItemDetailDiv").hide();
        renderBudget();

    }

        //初始化
        var selectType = bt.selectType.init();
        var corp = bt.corp.init();
        var dateType = bt.dateType.init();
        var moneyType = bt.moneyType.init();
        bt.bank.init({elem: '#payerAccNo'});
        bt.bankInfo.init();
//
    //监听提交
    form.on('submit(submit)', function (data) {
        if(data.field.receiptDate > data.field.agingDate || data.field.agingDate > data.field.endDate){
            parent.layer.msg('单据日期应早于或等于账龄起算日，账龄起算日应早于或等于到期日期。', {icon: 2});
            return false;
        }
        data.field.status=$(this).attr("value");
        var submitStatus=$(this).attr("value");
        bt.loading.show();
        data.field.contractAttachmentId = up2.getGroupId();
        data.field.attachmentId = up.getGroupId();
        var action = "/accountReceivable/docManage/save";
        $.post(basePath + action,data.field, function (result) {
              bt.loading.hide();
              parent.layer.alert(result.success?(submitStatus=="11"?bt.lang("submitted.successfully.pending.approval"):result.msg):result.msg,
               {icon: result.success ? 1 : 2}, function () {
              parent.layer.closeAll();
              if (result.success) {
                  if (!bt.navtab) {
                      location.reload();
                      parent.layer ? parent.layer.closeAll() : layer.closeAll();
                  } else {
                      bt.navtab.refreshParent();
                      bt.navtab.closeCurrent();
                  }
              }
              })
          }, 'JSON');
        return false;
    });

    showDesc();
    function showDesc() {
        var tip_index1 = 0;
        $('#invoiceCode-desc').mouseenter(function () {
            tip_index1 = layer.tips(bt.lang('8位或20位的数字或者字母'), '#invoiceCode-desc', {time: 30000});
        }).mouseleave(function () {
            layer.close(tip_index1);
        });
        $('#taxincAmt-desc').mouseenter(function () {
            tip_index1 = layer.tips(bt.lang('如为购车发票，则传价税合计'), '#taxincAmt-desc', {time: 30000});
        }).mouseleave(function () {
            layer.close(tip_index1);
        });
        $('#contractCode-desc').mouseenter(function () {
            tip_index1 = layer.tips(bt.lang('若合同附件上有编号，请严格按照附件上的填写，否则审核时会被退回'), '#contractCode-desc', {time: 30000});
        }).mouseleave(function () {
            layer.close(tip_index1);
        });
    }
    $('.inputMoney').on('blur',checkInputMoney);
});

    /**
      * 设置业务类型
      */
       function setBusTypeCallBack() {
          // if(bt.isResource('netbank:basicdata:nisbustype:apply')){
              if (bt.navtab) {
                  bt.navtab.addNavtab(bt.lang('业务种类维护'), basePath + "/netbank/nisbustype/list?init='1'" );
                  return;
              }
              parent.layer.open({
                  type: 2,
                  area: ['950px', '530px'],
                  maxmin: true,
                  content: basePath + "/netbank/nisbustype/list"
              });
          // }else {
          //     layer.alert("无功能权限，请联系管理员在基础平台分配权限。", {icon: 2, title:bt.lang('暂无权限访问')});
          // }
       }

    /**
      * 设置客商资料
      */
       function setExternalCallBack() {
        if(bt.isResource('sys:external:corp:view')){
           if (bt.navtab) {
               bt.navtab.addNavtab(bt.lang('客商资料'), basePath + "/sys/externalCorp/index" );
               return;
           }
           parent.layer.open({
               type: 2,
               area: ['950px', '530px'],
               maxmin: true,
               content: basePath + "/sys/externalCorp/index"
           });
            }else {
              layer.alert("无功能权限，请联系管理员在基础平台分配权限。", {icon: 2, title:bt.lang('暂无权限访问')});
          }
       }
    /**
      * 设置交易合同
      */
       function setContractCallBack() {
       if(bt.isResource('con:conregister:resource:op')){
           if (bt.navtab) {
               bt.navtab.addNavtab(bt.lang('合同登记'), basePath + "/contract/conregister/list" );
               return;
           }
           parent.layer.open({
               type: 2,
               area: ['950px', '530px'],
               maxmin: true,
               content: basePath + "/contract/conregister/list"
           });
            }else {
              layer.alert("无功能权限，请联系管理员在基础平台分配权限。", {icon: 2, title:bt.lang('暂无权限访问')});
          }
       }

       /**
        * 计划执行输入金额汇总后的回调
        * @param data
        */
       function sumMoneyCallBack(data) {
           bt.moneyType.get("receivableMoney").val(data);
       }

addRow = function (){
    if(!rowNum){
        rowNum = 0;
    }
    var html = '<tr data-row="'+rowNum+'">';
    html+='<input type="hidden" id="rowSign'+rowNum+'" value="'+rowNum+'">';
    html+='<td><input type="text" placeholder="请输入" name="invoiceVos['+rowNum+'].number" onkeyup="invoiceNumberCheck('+rowNum+')" class="layui-input layui-input-list "data-name="invoiceVos['+rowNum+'].numbe"></td>';
    html+='<td><input type="text" placeholder="请输入" name="invoiceVos['+rowNum+'].code" class="layui-input layui-input-list" data-name="invoiceVos['+rowNum+'].code"></td>';
    html+='<td><input type="text" placeholder="请输入" name="invoiceVos['+rowNum+'].parAmt"  class="invoiceVos'+rowNum+'parAmt inputMoney layui-input layui-input-list bt-biz-moneyType" lay-verify="number2" maxlength="17" data-id ="invoiceVos'+rowNum+'parAmt" data-name="invoiceVos['+rowNum+'].parAmt"></td>';
    html+='<td><input type="text" placeholder="请输入" name="invoiceVos['+rowNum+'].taxincAmt"  class="inputMoney layui-input layui-input-list bt-biz-moneyType" lay-verify="number2" maxlength="17" data-id ="invoiceVos'+rowNum+'taxincAmt" data-name="invoiceVos['+rowNum+'].taxincAmt"></td>';
    html+='<td><input type="text" placeholder="请选择" name="invoiceVos['+rowNum+'].draDate"  class="layui-input layui-input-list bt-biz-dateType" data-type="date" placeholder="请选择" data-name="invoiceVos['+rowNum+'].draDate"data-biz-name="invoiceVos['+rowNum+'].draDate"></td>';
    html+='<td><div name="invoiceVos['+rowNum+'].type" placeholder="请选择" data-group="fp.type" class="bt-biz-dict" data-type="select" data-name="invoiceVos['+rowNum+'].type" data-callback="updateInvoiceCode" data-biz-name="invoiceVos['+rowNum+'].type"></div></td>';
    html+='<td><input type="text" name="invoiceVos['+rowNum+'].checkCode"  placeholder="请输入" class="layui-input layui-input-list" data-name="invoiceVos['+rowNum+'].checkCode"></td>';
    html+='<td><a class="addTr" href="javascript:void(0);" onclick="addRow()">新增</a><span style="color:#C70D0D"> </span><a href="javascript:void(0);"class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a></td>';
    html += '</tr>';
    $("#editTable tbody:last").append(html);
    $('.inputMoney').on('blur',checkInputMoney);
    rowNum++;
    bt.dateType.init();
    bt.moneyType.init();
    bt.dicts.init();
    return false;
}

checkInputMoney = function (){
    if(bt.money.format(this.value)==0){this.value=''}
    if(this.value.length==1){this.value = this.value.replace(/[^1-9]/g,'')}else{this.value.replace(/\\D/g,'')}
}

invoiceNumberCheck = function (index){
    var code = $('input[name="invoiceVos['+index+'].number"]').val();
    if(code && code.length >= 1){
        $('input[name="invoiceVos['+index+'].number"]').attr('lay-verify', 'invoiceCodeAssert');
        if(code.length == 8){
            $('input[name="invoiceVos['+index+'].code"]').attr('lay-verify', 'invoiceNumberAssert');
        } else if(code.length == 20){
            $('input[name="invoiceVos['+index+'].code"]').removeAttr('lay-verify');
        }
    }
}

updateInvoiceCode = function (data){
    var currentElem = data.elem;
    var selectName = currentElem.name;
    var index = selectName.substring(selectName.indexOf('[') + 1, selectName.indexOf(']'));
    var type = data.value;
    invoiceNumberCheck(index);
    $('input[name="invoiceVos['+index+'].checkCode"]').removeAttr('lay-verify');
    $('input[name="invoiceVos['+index+'].number"]').removeAttr('lay-verify');
    $('.invoiceVos'+index+'parAmt').attr('lay-verify','number2');
    if(type && (type == 3 || type == 4)){
        $('input[name="invoiceVos['+index+'].number"]').attr('lay-verify', 'invoiceCodeAssert');
        $('.invoiceVos'+index+'parAmt').attr('lay-verify', 'required|number2');
    }else if(type && type == 1){
        $('input[name="invoiceVos['+index+'].checkCode').attr('lay-verify', 'required');
    }
}