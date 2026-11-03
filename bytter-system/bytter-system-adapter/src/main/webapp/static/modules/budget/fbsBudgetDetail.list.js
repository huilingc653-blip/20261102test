bt.use(['form', 'moneyType', 'select', 'dateType', 'date', 'selectType', 'btImport', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    form.render('select');
    var dateType = bt.dateType.init();
    var flag = $("#flag").val();

    //自定义验证规则
    form.verify({
        nozero: function (value, item) {
            if (value !== '' && value == 0) {
                if ("input" == flag) {
                    return bt.lang('fbs.budget.detail.message.bmoney.null');
                } else {
                    return bt.lang('fbs.budget.detail.message.btempmoney.null');
                }
            }
        }
    });

    /**
     * 填写金额后回调
     * @param value
     * @param elem
     */
    bMoneyCallBack = function (value, elem) {
        /*var row = $(elem).data("row");
        var cur = selectType.get("curId" + row).getData();
        if (cur.obj) {
            moneyType.get("tempNatualMoney" + row).val(value * cur.obj.curExrate);
        } else {
            moneyType.get("tempNatualMoney" + row).val(value);
        }*/
    }

    /**
     * 填写追加金额后回调
     * @param value
     * @param elem
     */
    btempMoneyCallBack = function (value, elem) {
        /*var row = $(elem).data("row");
        var cur = selectType.get("curId" + row).getData();
        if (cur.obj) {
            moneyType.get("tempNatualMoney" + row).val(value * cur.obj.curExrate);
        } else {
            moneyType.get("tempNatualMoney" + row).val(value);
        }*/
    }
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    /**
     * 选择币别后回调
     * @param data
     * @param elem
     * @param beforevalue
     * @param wrapObj
     */
    curCallback = function (data, elem, beforevalue, wrapObj) {
        /*var row = $(wrapObj).parents("tr").attr("row");
        var money;
        if ($("#flag").val() == "input") {
            money = moneyType.get("bMoney" + row).val();
        } else {
            money = moneyType.get("btempMoney" + row).val();
        }
        if (data.curexrate) {
            moneyType.get("tempNatualMoney" + row).val(money * data.curexrate);
        } else {
            moneyType.get("tempNatualMoney" + row).val(money);
        }*/
    }
    var selectType = bt.selectType.init();

    /**
     * 导入功能
     */
    importExcel = function () {
        var up = bt.btImport.init({
            code: "50000002",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            params: {
                itemCode: $("#itemCode").val(),
                timeType: $("#timeType").val(),
                detailId: $("#detailId").val(),
                flag: $("#flag").val(),
                beginDate: $("#beginDate").val(),
                endDate: $("#endDate").val(),
            },
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadFbsBudgetDetail?itemCode=" + $("#itemCode").val() + "&itemName=" + $("#itemName").val() + "&target=" + $("#flag").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/budget/fbsbudgetdetail/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        layer.confirm(bt.lang('table.fcBankBillTemp.importSuccess'), {
                            btn: [bt.lang('table.bisAccBal.btn.confirm')] //按钮
                        }, function (index) {
                            var webObj = bt.navtab.getParentDom();
                            if(webObj){
                                var obj=webObj.document.createElement("input");
                                obj.value="1";
                                obj.id="isImp";
                                obj.type="hidden";
                                webObj.document.body.appendChild(obj);
                            }
                            location.reload();
                        });
                    }
                });
            }
        });
    };

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/budget/fbsbudgetdetail/save";
        var itemCode = $("#itemCode").val();
        //科目明细是否必须录入
        var isWriteDetail = $("#isWriteDetail").val();
        var flag = $("#flag").val();
        var moneyTotal = 0;
        // 保存前校验可上报金额
        $('form').find('input[data-name="tempNatualMoney"]').each(function () {
            if ($(this).data('value')) {
                moneyTotal = moneyFun.add(moneyTotal, Math.abs(moneyFun.toNumber($(this).data('value'))));
            }
        });
        if (flag == 'add') {
            if ($("#canBMoney").val() && $("#canBMoney").val() < moneyTotal) {
                layer.alert(bt.lang('budget.message.btempmoney.gt.canbmoney'), {icon: 7});
                return false;
            }
        } else {
            if ($("#canBMoney").val() && $("#canBMoney").val() < moneyTotal) {
                layer.alert(bt.lang('budget.message.bmoney.gt.canbmoney'), {icon: 7});
                return false;
            }
        }
        //$("select[name='curId']").removeAttr("disabled");
        $("select[name='settlType']").removeAttr("disabled");

        var webObj = bt.navtab.getParentDom();
        if (!webObj) {
            layer.alert(bt.lang('上报页面已关闭，无法保存'), {icon: 7});
            return false;
        }
        $.post(basePath + action, $('#fbsDtlForm').serialize(), function (result) {
            if (flag == 'add') {
                var bool = false;
                try {
                    bt.each(result.attributes, function (key, v) {
                        // 年分解月 月分解周
                        bool = true;
                        var oldMoney = webObj.bt.moneyType.init().get("btempMoney" + key + "_" + itemCode).val();
                        webObj.bt.moneyType.init().get("btempMoney" + key + "_" + itemCode).val(v);
                        webObj.calItemMoney(v, webObj.$('input[data-biz-name ="btempMoney' + key + '_' + itemCode + '"]'), oldMoney);
                        if (v > 0 || isWriteDetail == "1") {
                            webObj.bt.moneyType.init().get("btempMoney" + key + "_" + itemCode).attr("readonly", "readonly");
                        } else {
                            webObj.bt.moneyType.init().get("btempMoney" + key + "_" + itemCode).removeAttr('readonly');
                        }
                    });
                }catch (e){}
                if (!bool) {
                    if (webObj.$("#btempMoney_" + itemCode).length > 0) {
                        webObj.bt.moneyType.init().get("btempMoney_" + itemCode).val(result.obj);
                        webObj.calItemMoney(result.obj, webObj.$('input[data-biz-name ="btempMoney_' + itemCode + '"]'));
                    }
                }
            } else {
                var bool = false;
                var isApprove = $("#isApprove").val();
                try {
                    bt.each(result.attributes, function (key, v) {
                        // 年分解月
                        bool = true;
                        if(isApprove == "1" && key == "1"){
                            var oldMoney = webObj.bt.moneyType.init().get("bMoney_" + itemCode).val();
                            webObj.bt.moneyType.init().get("bMoney_" + itemCode).val(v);
                            webObj.calItemMoney(v, webObj.$('input[data-biz-name ="bMoney_' + itemCode + '"]'), oldMoney);
                            if (v > 0 || isWriteDetail == "1") {
                                webObj.bt.moneyType.init().get("bMoney_" + itemCode).attr("readonly", "readonly");
                            } else {
                                webObj.bt.moneyType.init().get("bMoney_" + itemCode).removeAttr('readonly');
                            }
                        }else {
                            var oldMoney = webObj.bt.moneyType.init().get("bMoney" + key + "_" + itemCode).val();
                            webObj.bt.moneyType.init().get("bMoney" + key + "_" + itemCode).val(v);
                            webObj.calItemMoney(v, webObj.$('input[data-biz-name ="bMoney' + key + '_' + itemCode + '"]'), oldMoney);
                            if (v > 0 || isWriteDetail == "1") {
                                webObj.bt.moneyType.init().get("bMoney" + key + "_" + itemCode).attr("readonly", "readonly");
                            } else {
                                webObj.bt.moneyType.init().get("bMoney" + key + "_" + itemCode).removeAttr('readonly');
                            }
                        }
                    });
                }catch (e){}
                if (!bool) {
                    if (webObj.$("#bMoney_" + itemCode).length > 0) {
                        // 计划普通模式
                        webObj.bt.moneyType.init().get("bMoney_" + itemCode).val(result.obj);
                        if (result.obj > 0 || isWriteDetail == "1") {
                            webObj.bt.moneyType.init().get("bMoney_" + itemCode).attr("readonly", "readonly");
                        } else {
                            webObj.bt.moneyType.init().get("bMoney_" + itemCode).removeAttr('readonly');
                        }
                        webObj.calItemMoney(result.obj, webObj.$('input[data-biz-name ="bMoney_' + itemCode + '"]'));
                    } else if (webObj.$("#bMoney1_" + itemCode).length > 0) {
                        // 月滚动模式
                        webObj.bt.moneyType.init().get("bMoney1_" + itemCode).val(result.obj);
                        if (result.obj > 0 || isWriteDetail == "1") {
                            webObj.bt.moneyType.init().get("bMoney1_" + itemCode).attr("readonly", "readonly");
                        } else {
                            webObj.bt.moneyType.init().get("bMoney1_" + itemCode).removeAttr('readonly');
                        }
                        webObj.calItemMoney(result.obj, webObj.$('input[data-biz-name ="bMoney1_' + itemCode + '"]'));
                    }
                }
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                bt.loading.hide(); //关闭加载中
                if (result.success) {
                    var webObj = bt.navtab.getParentDom();
                    if(webObj && webObj.document.getElementById("saveBut")){
                        webObj.document.getElementById("saveBut").click();
                    }
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 全选
     */
    form.on('checkbox(check)', function (data) {
        var check = $(data.elem).prop("checked");
        $("input[type='checkbox']").each(function () {
            if (check) {
                $(this).prop("checked", true);
            } else {
                $(this).prop("checked", false);
            }
        });
        form.render("checkbox");
    });

    /**
     * 增加一行
     */
    addTr = function () {
        var defVestingDate = $("input[name=defVestingDate]").eq(0).val();
        var row = $("#dtlTable tbody tr:last").find(".bt-biz-moneyType").data("row");
        if (isNaN(row)) {
            row = 0;
        }
        var newRow = row + 1;
        var dtlCol = $("#dtlCol").val().replace('[', '').replace(']', '').replace(/\s+/g, "").split(",");
        var html = '<tr row="' + newRow + '">';
        html += '<td><input type="checkbox" lay-skin="primary"/><input type="hidden" name="id" /> </td>';
        html += '<td><a href="javascript:void (0)" onclick="deleteDtl(this)" id="del' + newRow + '">' + bt.lang("common.button.delete") + '</a></td>';
        html += '<td>';
        html += '(' + $("#itemCode").val() + ')' + $("#itemName").val();
        html += '</td>';
        if ($("#flag").val() == "input") {
            html += '<td>';
            html += '   <input type="text" data-name="bMoney" data-id="bMoney' + newRow + '" data-biz-name="bMoney' + newRow + '" lay-verify="required|nozero" data-row="' + newRow + '" class="layui-input bt-input-inline bt-biz-moneyType" data-callback="bMoneyCallBack">';
            html += '</td>'
        } else {
            html += '<td>';
            html += '   <input type="text" data-name="bMoney" data-id="bMoney' + newRow + '" data-biz-name="bMoney' + newRow + '" readonly="readonly" data-row="' + newRow + '" class="layui-input bt-input-inline bt-biz-moneyType" >';
            html += '</td>';
            html += '<td>';
            html += '   <input type="text" data-name="bsuperMoney" data-id="bsuperMoney' + newRow + '" readonly="readonly" data-row="' + newRow + '" class="layui-input bt-input-inline bt-biz-moneyType" >';
            html += '</td>';
            html += '<td>';
            html += '   <input type="text" data-name="btempMoney" data-id="btempMoney' + newRow + '" data-biz-name="btempMoney' + newRow + '" lay-verify="required|nozero" data-minus="true" data-row="' + newRow + '" class="layui-input bt-input-inline bt-biz-moneyType" data-callback="btempMoneyCallBack">';
            html += '</td>';
        }
        /*html += '<td>';
        html += '<input type="text" data-name="tempNatualMoney" data-id="tempNatualMoney' + newRow + '" data-biz-name="tempNatualMoney' + newRow + '" lay-verify="required" class="layui-input bt-input-inline bt-biz-moneyType" readonly="readonly" value="0">';
        html += '</td>';
        html += '<td>';
        html += '   <div class="layui-input-inline">';
        html += '       <div class="bt-biz-selectType" data-name="curId" data-biz-name="curId' + newRow + '" data-textfield="curName" data-defaultvalue="' + $("#natualCurId").val() + '" ';
        html += '           data-url="' + basePath + '/basic/business/btcurrency/json" data-valuefield="id" data-callback="curCallback" data-required="true" data-selecttext="false"></div>';
        html += ' </div>';
        html += '</td>';*/
        html += '<td>';
        html += ' <input type="text" class="layui-input bt-biz-dateType" name="vestingDate" data-type="date" data-min="' + $("#beginDate").val() + '" data-max="' + $("#endDate").val() + '" value="' + defVestingDate + '"/>';
        html += '</td>';
        bt.each(dtlCol, function (i, col) {
            if (col == "settlType") {
                html += '<td>';
                html += ' <div class="bt-biz-dict" data-type="select" data-group="settlType" data-name="settlType" data-defaultvalue="0"  data-selecttext="false" ></div>';
                html += '</td>';
            }
            if (col == "corpName") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="corpName" />';
                html += '</td>';
            }
            if (col == "purpose") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="purpose" />';
                html += '</td>';
            }
            if (col == "wishPayday") {
                html += '<td>';
                html += ' <input type="text" class="layui-input bt-biz-dateType" name="wishPayday" data-type="date"/>';
                html += '</td>';
            }
            if (col == "payerAcc") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="payerAcc" />';
                html += '</td>';
            }
            if (col == "payeeAccName") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="payeeAccName" />';
                html += '</td>';
            }
            if (col == "payeeAcc") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="payeeAcc" />';
                html += '</td>';
            }
            if (col == "payeeBank") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="payeeBank" />';
                html += '</td>';
            }
            if (col == "contract") {
                html += '<td>';
                html += ' <input type="text" class="layui-input" name="contract" />';
                html += '</td>';
            }
        });
        $("#dtlTable").append(html);
        moneyType.render();
        dateType.render();
        selectType.render();
        dicts.render();
        form.render();
        if ($("#delFlag").val()) {
            $("#del" + newRow).remove();
        }
    }

    /**
     * 删除
     * @param _this
     * @param id
     */
    deleteDtl = function (_this, id) {
        var row = $(_this).parents("tr").attr("row");
        if(moneyType.get("bMoney" + row).val() && "add" == flag){
            moneyType.get("btempMoney" + row).val(0);
            moneyType.get("tempNatualMoney" + row).val(0);
        }else {
            $(_this).parents("tr").remove();
        }
        //layer.alert(bt.lang('fbs.budget.detail.message.del'), {icon: 7});
    }
    deleteDtlForBatch = function (_this, id) {
        var row = $(_this).parents("tr").attr("row");
        if(moneyType.get("bMoney" + row).val() && "add" == flag){
            moneyType.get("btempMoney" + row).val(0);
            moneyType.get("tempNatualMoney" + row).val(0);
        }else {
            $(_this).parents("tr").remove();
        }
    }


    /**
     * 监听查看上期明细
     */
    view = function () {
        var detailId = $("#detailId").val();
        var budgetId = $("#budgetId").val();
        var flag = $("#flag").val();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudgetdetail/viewSuperDetail?detailId=" + detailId + "&budgetId=" + budgetId + "&flag=" + flag
        });
    };

    deleteALl = function () {
        var isCheck = false
        $("input[type='checkbox']").each(function () {
            if ($(this).prop("checked")) {
                isCheck = true;
            }
        });
        if (!isCheck) {
            layer.alert(bt.lang("common.message.confirm.selectData"), {icon: 7});
            return false;
        } else {
            $("input[type='checkbox']").filter(":not([disabled])").each(function () {
                if ($(this).prop("checked")) {
                    if ($(this).val() != "all") {
                        deleteDtlForBatch(this, $(this).val());
                        $(this).prop("checked", false);
                    }
                }
            })
            //layer.alert(bt.lang('fbs.budget.detail.message.del'), {icon: 7});
        }
    };

    var webObj = bt.navtab.getParentDom();
    if(webObj){
        if(webObj.$("#isImp").val()==1){
            bt.loading.show();
            setTimeout(function () {
                webObj.$("#isImp").remove();
                $("#saveBut").trigger('click');
            }, 5000);
        }
    }
});




