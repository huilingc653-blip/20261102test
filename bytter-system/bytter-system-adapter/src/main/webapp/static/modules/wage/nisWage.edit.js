bt.use(['form', 'layedit', 'moneyType', 'datatable', 'btUpload', 'dateType', 'selectTable','selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var selectType = bt.selectType.init();
    /**
     * 选择日期回调
     */
    dateCallBack = function (data, $elem) {
        renderBudget();
    }
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    var showWagesVerify = false;
    var wagesVerify = $("#wagesVerify").val();
    var creator = $("#creator").val();
    var user = $("#user").val();
    var createBy = $("#createBy").val();
    if("1" == wagesVerify || ("1" == creator && user == createBy)){
        showWagesVerify = true;
    }
    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/wage/niswagedetail/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "wageDtlNo", bSortable: true, className: 'center'},
            /*{data: "userName", bSortable: true},*/
            {data: "workNo", bSortable: true},
            {data: "idTypeName", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("idType", full.idType);
                }
            },
            {data: "idNo", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankCode", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "wages", bSortable: true,className: 'textRight', visible: showWagesVerify,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.wages);
                }
            },
            /*{data: "provName", bSortable: true},
            {data: "cityName", bSortable: true},*/
            {
                data: "difBank", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisBillhead.difBank", full.difBank);
                }
            },
            {
                data: "areaSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisBillhead.areaSign", full.areaSign);
                }
            },
            {data: "purpose", bSortable: true, className: 'center'},
            {
                data: "voucherStat", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    //高亮显示交易失败的明细
                    if (full.voucherStat == "2" || full.voucherStat == "9") {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("remind");
                    }
                    return bt.dict("bisExc.voucherStat", full.voucherStat);
                }
            },
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if (full.voucherStat != "0") {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                }
                return html;
            }
            }
        ]
    });

    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/wage/niswage/getPayerAcc",
        urldata: {corpId: $("#corpId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payerAccDiv').text(data.bankAcc);
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccNameDiv').text(data.accName);
                $('#payerAccName').val(data.accName);
                $('#payerBankDiv').text(data.bankName);
                $('#payerBank').val(data.bankName);
                $("#curDiv").text(data.curName);
                $('#curId').val(data.curId);
                $('#bifCode').val(data.bifCode);
                //资金计划
                renderBudget();
            } else {
                $('input.text').val('');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        if($("#isOpenLimit").val()=="1") {
            bankAccLimit(data);
        }else {
            checkBalance(data);
        }
        return false;
    });

    //单位限额
    function corpLimit(data,msg){
        //获取单位本位币换算汇率
        var rate = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, corpId: data.field.corpId},
            dataType: 'json',
            success: function (result) {
                rate = bt.money.mul(data.field.wages,result,2)
            }
        });
        rate = parseFloat(rate);

        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,id:data.field.id, wishPayDate: data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 1) {
            msg += '<br>'+bt.lang('alert.over.daylimit.corp')//日累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        } else if(result === 2){
            msg += '<br>'+bt.lang('alert.over.monthlimit.corp')//月累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        }

        if(msg.length>0){
            bt.loading.hide();
            parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info')},function (index) {
                checkBalance(data);
            });
        }else{
            checkBalance(data);
        }
    }

    //账号限额
    function bankAccLimit(data){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: data.field.wages, payerAcc: data.field.payerAcc,id:data.field.id, wishPayDate: data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        var msg='';
        if (result=== 1) {
            msg = bt.lang('alert.over.paylimit')//单笔支付限额
        } else if(result === 2){
            msg = bt.lang('alert.over.daylimit')//日累计支付限额
        }else if(result === 3){
            msg = bt.lang('alert.over.monthlimit')//月累计支付限额
        }
        corpLimit(data,msg);
    }

    /**
     * 校验账户余额
     * @param data
     * @returns {boolean}
     */
    function checkBalance(data) {
        //代发工资总额
        var wages = moneyFun.toNumber(moneyType.get("wages").val());
        var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var balance = moneyFun.toNumber(moneyType.get("balance").val());//账户余额
        //付款金额大于账户余额
        var flag = false;
        if (wages > balance) {
            if (bankaccOver == "1") {
                flag = true;
            } else if (bankaccOver == "2") {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
                return false;
            }
        }
        if (flag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function (index) {
                bt.loading.show();
                submit(data);
            });
        } else {
            submit(data);
        }
    }

    //提交
    function submit(data){
        //判断是否挂接资金计划
        var wages = moneyFun.toNumber(moneyType.get("wages").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != wages) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }
        //判断期望支付日期需大于等于系统当前日期且大于等于工资发放日期
        var wageDay = dateType.get("wageDay").val();//工资发放日期
        var wishPayday = dateType.get("wishPayday").val();//期望支付日期
        if (bt.date.compare(wageDay, wishPayday) > 0) {
            bt.loading.hide();
            layer.alert(bt.lang("nis.wage.message.wishpayday.mismatch"), {icon: 2});
            return false;
        }
        //证书签名
        var signDataMsg = signData("代发工资批量提交", 7);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        var action = "/wage/niswage/modify";
        $.post(basePath + action, $('#searchForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
                parent.layer.closeAll();
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

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $("#corpId").val();
        var itemCode = $('input[name="payItem"]').val();
        var processDate = dateType.get("wishPayday").val();
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "400",
            billNo: $("#wageNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: processDate,
            curId: $("#curId").val(),
            type: 'select',
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断代发工资是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            $genOtherHtml();
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        renderBudget();
    }
    renderBudget();

    update = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if(data){
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.update.data'),  basePath + "/wage/niswagedetail/update?id=" + data.id);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/wage/niswagedetail/update?id=" + data.id,
                end: function () {
                    datatable && datatable.draw();
                }
            });
        }
    }
});
