var rowMR = 0;
var comprowMR = 0;
var singrowMR = 0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyTypes;
var corpInit;
var selectTree;
var parameters;
var corpId;
var dateTypes;
var dicts;
var form;
var ratioType;
var curId;
var isHangBail;
var isGuarantee;
var creditContractControl;
bt.use(['form', 'layedit', 'btdate', 'bttable', 'moneynum', 'element', 'dicts', 'select', 'moneyType', 'dateType', 'corp', 'selectType', 'money', 'bankInfo', 'selectTree', 'rateType', 'bondAccInfo', 'ratioType', 'btUpload','conSelect','conView'], function () {
    var $ = bt.jquery;
    form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    rateTypes = bt.rateType.init();//利率
    bondAccInit = bt.bondAccInfo.init();//保证金账号初始化
    moneyTypes = bt.moneyType.init();//金额
    dateTypes = bt.dateType.init();
    ratioType = bt.ratioType.init();

    isHangBail = $("#isHangBail").val();<!-- 授信是否挂保证金参数设置-->
    isGuarantee = $("#isGuarantee").val();<!-- 授信是否挂担保参数设置-->
    creditContractControl = $("#creditContractControl").val();<!-- 授信合同是否必须控制-->

    var moneyFun = bt.moneyType.getFun();
    fun = bt.moneyType.getFun();
    parameters = {};

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    // 合同附件上传
    var contractGid = $("#contractAnnexName").val();
    var contractUp = bt.btUpload.init({
        elem: "#contractUploader",
        isExpand: true,
        auto: false,
        gid: contractGid
    });

    /**
     * 合同挂接组件初始化
     */
    bt.conSelect.init();
    bt.conView.init();
    dicts = bt.dicts.init();//字典

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }


    //检查合同号是否重复
    $("#contractNo").blur(function () {
        var contractNo = $(this).val();
        if (bt.empty(contractNo)) {
            return;
        }
        $.post(basePath + "/cms/rat/ratbkration//checkContractNo", {
            contractNo: contractNo
        }, function (data) {
            if (data && "0" == data) {
                layer.alert(bt.lang("table.ratbkration.contractNoRepeat"));
                $("#contractNo").val("");
            }
        }, 'JSON');
    });

    //授信模式修改变更单位树
    bt.changeCorp = function (data) {
        var ratMode = data.value;
        corpInit = bt.corp.init();
        bt.changeRatCorp(ratMode);
        if("1" == ratMode){
            selectTypes.get("oldRatCode").attr("data-disabled",false);
        }else {
            selectTypes.get("oldRatCode").attr("");
            selectTypes.get("oldRatCode").attr("data-disabled",true);
        }
        form.render();
        /*if("1" == ratMode){
            corpInit.get("corpId").setUrl("all");
        } else {
            corpInit.get("corpId").setUrl("rat");
        }*/
        //corpInit.get("corpId").render();
    }


    //修改授信单位选择项
    bt.changeRatCorp = function (ratMode) {
        //重置可用余额
        var className = "";
        var checktype = "";
        var html = "";
        $("#ratCorpDiv").html(html);
        className = "selectTreeDemo";
        checktype = "radio";
        html = '<div class="layui-input-inline"><div class="' + className + '" data-width="300px;"></div></div>';
        $("#ratCorpDiv").html(html);
        //下拉树
        $.each($("." + className), function (i, item) {
            selectTree = bt.selectTree.initGetCurrent({
                url: basePath + '/cms/rat/ratbkration/listCorp?ratModel=' + ratMode,
                elem: ".selectTreeDemo",
                name: "corpId",
                selected: $(item).attr("defaultVal"),
                checkType: checktype,
                required: true,
                clickCallBack: bt.corpCallback,////选中单位后的回调函数，用于挂接包证金的时候，保证金账号更加单位进行筛选
                setting: {//该setting为ztree的setting，可参考http://www.treejs.cn/v3/api.php的API
                    data: {
                        simpleData: {
                            pIdKey: "parentId"
                        }
                    },
                    check: {
                        chkboxType: {"Y": "s", "N": "s"}
                    }
                },
                beforeInit: function (nodes) {
                    for (k in nodes) {
                        nodes[k].name = "(" + nodes[k].code + ")" + nodes[k].name;
                    }
                    return nodes;
                }
            });
        });
        form.render("select");
    }


    bt.setBankCode = function (data) {
        var bankTypeVal = "";
        if (data && data != undefined) {
            bankTypeVal = data;
        } else {
            bankTypeVal = layui.jquery("#bankType").find("option:selected").val()
        }
        var typeid = bt.jquery.trim(bankTypeVal);
        if (!bt.empty(typeid)) {
            bt.jquery("[data-name='bankCode']").data("url", basePath + "/cms/provide/loaninfo/queryBtBankByType/" + typeid);
        } else {
            bt.jquery("[data-name='bankCode']").data("url", basePath + "/cms/provide/loaninfo/queryBtBankByType/null");
        }
        var bank = selectTypes.get("bankCode").reset();
    }

    //设置其他授信品种不可选
    bt.setItemCode = function (data, elem, bv) {
        var Othis = elem.othis.parent('[data-link="true"]');
        var v = elem.value;
        var doms = $('[data-link="true"]');
        bt.each(doms, function () {
            var othat = $(this);
            if (!othat.is(Othis)) {
                othat.find('select option[value="' + v + '"]').prop("disabled", true);
                othat.find('select option[value="' + bv + '"]').prop("disabled", false);
            }
        })
        form.render("select");
    }
    //新增品种行设置已勾选的品种不可选择
    bt.setNewItemDisable = function (rowIndex) {
        var doms = $('[data-link="true"]');
        var disItem = "";
        bt.each(doms, function (i, item) {
            var othat = $(this);
            var dataObj = $(othat).find("option:selected").val();
            if (!bt.empty(dataObj)) {
                //$("#"+rowIndex + "itemCode").find('select option[value="'+dataObj+'"]').prop("disabled",true);
                disItem += dataObj + ",";
            }
        })
        disItem = bt.jquery.trim(disItem);
        if (disItem.length > 1) {
            disItem = disItem.substr(0, disItem.length - 1);
            $("#" + rowIndex + "itemCode").attr("data-itemdisabled", disItem);
            form.render("select");
        }
    }
    //删除品种行重新设置所有行该品种可选择
    bt.resetDelItem = function (rowIndex) {
        var itemCode = selectTypes.get("rationDtls[" + rowIndex + "].itemCode").val();
        itemCode = bt.jquery.trim(itemCode);
        if (itemCode.length == 0) {
            return;
        }
        var doms = $('[data-link="true"]');
        bt.each(doms, function () {
            var othat = $(this);
            othat.find('select option[value="' + itemCode + '"]').prop("disabled", false);
        })
        form.render("select");
    }

    //检查最高额度与授信额度关系
    bt.checkMaxMoney = function (data, el) {
        var dataId = $(el).attr("data-id");
        var moneyIndex = dataId.indexOf("maxMoney");
        var rowIndex = dataId.substr(0, moneyIndex);
        var moneyIndexVal = moneyType.get(compRowIndex + "money").val();
        if (bt.empty(moneyIndexVal)) {
            layer.alert(bt.lang("table.ratbkrationdetail.moneymsg"));
            moneyType.get(moneyIndexVal + "maxMoney").val(0);
            return false;
        }
        //最高额度不能大于授信额度
        if (bt.money.sub(moneyIndexVal, data) < 0) {
            layer.alert(bt.lang("table.ratbkrationdetail.maxMoneyOut"));
            moneyType.get(rowIndex + "maxMoney").val(0);
            return false;
        }
    }

    bt.calMoney = function (data, el) {
        bt.calSumMoney();
    }
    //综合授信设置
    bt.calCompMoney = function (data, el) {
        //设置综合授信额度一致
        $.each($("input[data-id$='maxMoney']"), function (i, item) {
            var maxIndex = $(item).attr("rowindex");
            moneyType.get(maxIndex + "maxMoney").val(data);
        });

        bt.calSumMoney();
    }

    //循环计算授信额度
    bt.calSumMoney = function () {
        var sumMoney = 0;
        $.each($("input[id$='money']"), function (i, item) {
            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        moneyType.get("moneysum").val(bt.money.format(sumMoney));
        if (isHangBail == "1") {//授信基础参数设置挂接保证金
            //计算保证金
            setBailMoney();
        }
    }

    //新增单品种事件
    $("#addSingleTr").click(function () {
        rowMR++;
        singrowMR++;
        if (singrowMR - copmRatSize > 0) {
            return layer.msg(bt.lang('rat.bkration.itemCode.isnull'));
        }
        var html = '<tr>';
        html += '<td>' + bt.dict("RatModel.ItemFlag", '0') + '</td>';
        html += '<td><input id="itemFlag' + rowMR + '" name="rationDtls[' + rowMR + '].itemFlag" type="hidden" value="0">'
            + '<input name="rationDtls[' + rowMR + '].itemOrder" type="hidden" value="' + rowMR + '">'
            + '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls[' + rowMR + '].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
            + 'data-valuefield="id" data-textfield="name" id="' + rowMR + 'itemCode" data-name="rationDtls[' + rowMR + '].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';
        html += '<td>'//额度类型
            + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-group="rat.creditType" data-defaultvalue="1" data-selecttext="false" data-biz-name="rationDtls[' + rowMR + '].creditType"  '
            + 'data-name="rationDtls[' + rowMR + '].creditType"></div></div></td>';
        html += '<td><input type="text" data-name="rationDtls[' + rowMR + '].money" id="' + rowMR + 'money" data-biz-name="' + rowMR + 'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calMoney"></td>';
        html += '<input type="hidden" id="assurepercent' + rowMR + '" name="rationDtls[' + rowMR + '].assurepercent" >'
            + '<input type="hidden" id="procedureFeeRate' + rowMR + '" name="rationDtls[' + rowMR + '].procedureFeeRate" >'
            + '<input type="hidden" id="bondMoney' + rowMR + '" name="rationDtls[' + rowMR + '].bondMoney" >'
            + '<input type="hidden" id="procedureFee' + rowMR + '" name="rationDtls[' + rowMR + '].procedureFee" >'
        html += '<td>'
            + '</td>';
        html += '<td><a class="" href="#" data-row="' + rowMR + '" onclick="openAssure(this, ' + rowMR + ')">' + bt.lang("common.ration.limit.set") + '</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
            + '<button data-type="del" type="button" class="layui-btn layui-btn-mini">' + bt.lang("common.button.delete") + '</button>'
            + '</td>';
        html += '</tr>';
        if (rowMR - 1 > 0 && $("#singleTable tbody tr:last").length > 0) {
            $("#singleTable tbody tr:last").after(html);
        } else {
            $("#singleTable tbody").append(html);
        }
        initSelectCode(rowMR);
        bt.dicts.init();
        return false;
    });
    //新增综合品种事件
    $("#addCompTr").click(function () {
        if (comprowMR - copmRatSize >= 0) {
            return layer.msg(bt.lang('rat.bkration.itemCode.isnull'));
        }
        rowMR++;
        comprowMR++;
        var html = '<tr>';
        var cols = "";
        var buttonId = "";
        var colFlag = true;
        var compMoney = "";

        if (comprowMR - 1 > 0 && $("#compTable tbody tr:last").length > 0) {
            colFlag = false;
            compMoney = moneyType.get(compRowIndex + "money").val();
        } else {
            cols = "rowspan='" + copmRatSize + "'";
            buttonId = " id='compButton' ";
            compRowIndex = rowMR;//记录综合品种列Index
        }
        if (colFlag) {
            html += '<td ' + cols + '>' + bt.dict("RatModel.ItemFlag", '1') + '</td>';
        }

        html += '<td><input id="itemFlag' + rowMR + '" name="rationDtls[' + rowMR + '].itemFlag" type="hidden" value="1">'
            + '<input name="rationDtls[' + rowMR + '].itemOrder" type="hidden" value="' + rowMR + '">'
            + '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="rationDtls[' + rowMR + '].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
            + 'data-valuefield="id" data-textfield="name" id="' + rowMR + 'itemCode" data-name="rationDtls[' + rowMR + '].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';
        html += '<td>'//额度类型
            + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-group="rat.creditType" data-defaultvalue="1" data-selecttext="false" data-biz-name="rationDtls[' + rowMR + '].creditType"  '
            + 'data-name="rationDtls[' + rowMR + '].creditType"></div></div></td>';
        if (colFlag) {
            html += '<td ' + cols + '><input type="text" data-name="rationDtls[' + rowMR + '].money" id="' + rowMR + 'money" data-biz-name="' + rowMR + 'money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.calCompMoney"></td>';
        }
        html += '<td><input type="text" data-name="rationDtls[' + rowMR + '].maxMoney" id="' + rowMR + 'maxMoney" data-biz-name="' + rowMR + 'maxMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" rowindex="' + rowMR + '" value="' + compMoney + '" data-callback="bt.checkMaxMoney">';
        html += '</td>';
        html += '<input type="hidden" id="assurepercent' + rowMR + '" name="rationDtls[' + rowMR + '].assurepercent" >'
            + '<input type="hidden" id="procedureFeeRate' + rowMR + '" name="rationDtls[' + rowMR + '].procedureFeeRate" >'
            + '<input type="hidden" id="bondMoney' + rowMR + '" name="rationDtls[' + rowMR + '].bondMoney" >'
            + '<input type="hidden" id="procedureFee' + rowMR + '" name="rationDtls[' + rowMR + '].procedureFee" >'
        html += '<td><a class="" href="#" data-row="' + rowMR + '" onclick="openAssure(this, ' + rowMR + ')">' + bt.lang("common.ration.limit.set") + '</a>&nbsp;&nbsp;&nbsp;&nbsp;\t'
            + '<button data-type="del" ' + buttonId + '" type="button" class="layui-btn layui-btn-mini">' + bt.lang("common.button.delete") + '</button>'
            + '</td>';
        html += '</tr>';

        if (rowMR - 1 > 0 && $("#compTable tbody tr:last").length > 0) {
            $("#compTable tbody tr:last").after(html);
        } else {
            $("#compTable tbody").append(html);
        }
        if (!colFlag) {
            $("#compButton").hide();
        }

        initSelectCode(rowMR);
        bt.dicts.init();
        return false;
    });


    var initRationTable = function () {
        //生成列表
        bt.bttable.init({
            scrollX: false,
            scrollY: false,
            clickTrSelected: false,
            elem: "#singleTable",
            url: basePath + "/cms/rat/ratbkration/initRationDtl?ratCode=" + $("#id").val() + "&opType=add",
            buttons: [],
            columns: [
                {
                    data: "itemCodeFlag",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<div class="bt-biz-dict" data-group="RatModel.ItemFlag" data-name="rationDtls.itemFlag" data-type="select" data-callback="bt.getBkItemCode" data-defaultvalue="0">'
                                + '<input name="rationDtls[' + row + '].itemOrder" type="hidden" value="' + row + '"></div>';
                        } else {
                            reInfo = bt.dict("RatModel.ItemFlag", data.itemFlag) + '<input name="rationDtls.itemFlag" type="hidden" value="' + data.itemFlag + '">';
                            +'<input name="rationDtls[' + row + '].itemOrder" type="hidden" value="' + row + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "itemCode",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<div class="selectCode' + row + '"></div>';
                        } else {
                            reInfo = data.itemCode + '<input name="rationDtls.itemCode" type="hidden" value="' + data.itemCode + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "money",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<input type="text" data-name="rationDtls.money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false">';
                        } else {
                            reInfo = data.money + '<input name="rationDtls.money" type="hidden" value="' + data.money + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "openMoney",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<input type="text" data-name="rationDtls.openMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false">';
                        } else {
                            reInfo = data.openMoney + '<input name="rationDtls.openMoney" type="hidden" value="' + data.openMoney + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "maxMoney",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<input type="text" data-name="rationDtls.maxMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false">';
                        } else {
                            reInfo = data.openMoney + '<input name="rationDtls.maxMoney" type="hidden" value="' + data.maxMoney + '">';
                        }
                        return reInfo;
                    }
                },
                //操作
                {
                    data: null, render: function (data, row) {
                        var reInfo = '';
                        if (row > 0) {
                            reInfo = '<button data-type="del" class="layui-btn layui-btn-mini">' + bt.lang("common.button.delete") + '</button>';
                        }
                        return reInfo;
                    }
                }
            ],
            delRowCallback: function (data, $tr) {
                var rowIndex = $tr.find("input[name$='itemOrder']").val();
                bt.resetDelItem(rowIndex);
                //删除后更新其他行的授信品种选择
                $tr.remove();
                bt.calSumMoney();
                singrowMR--;
            },
            tableInitCallback: function () {
                initSelectCode();
                bt.dicts.init();
            }
        });
        rowMR = 0;
    }

    var initCompRationTable = function () {
        //生成列表
        bt.bttable.init({
            scrollX: false,
            scrollY: false,
            clickTrSelected: false,
            elem: "#compTable",
            url: basePath + "/cms/rat/ratbkration/initRationDtl?ratCode=" + $("#id").val() + "&opType=add",
            columns: [
                {
                    data: "itemCodeFlag",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<div class="bt-biz-dict" data-group="RatModel.ItemFlag" data-name="rationDtls.itemFlag" data-type="select" data-callback="bt.getBkItemCode" data-defaultvalue="0"></div>'
                                + '<input name="rationDtls[' + row + '].itemOrder" type="hidden" value="' + row + '">';
                        } else {
                            reInfo = bt.dict("RatModel.ItemFlag", data.itemFlag) + '<input name="rationDtls.itemFlag" type="hidden" value="' + data.itemFlag + '">'
                                + '<input name="rationDtls[' + row + '].itemOrder" type="hidden" value="' + row + '">';

                        }
                        return reInfo;
                    }
                },
                {
                    data: "itemCode",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<div class="selectCode' + row + '"></div>';
                        } else {
                            reInfo = data.itemCode + '<input name="rationDtls.itemCode" type="hidden" value="' + data.itemCode + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "money",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<input type="text" data-name="rationDtls.money" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false">';
                        } else {
                            reInfo = data.money + '<input name="rationDtls.money" type="hidden" value="' + data.money + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "openMoney",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<input type="text" data-name="rationDtls.openMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false">';
                        } else {
                            reInfo = data.openMoney + '<input name="rationDtls.openMoney" type="hidden" value="' + data.openMoney + '">';
                        }
                        return reInfo;
                    }
                },
                {
                    data: "maxMoney",
                    render: function (data, row) {
                        var reInfo;
                        if (row > 0) {
                            reInfo = '<input type="text" data-name="rationDtls.maxMoney" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false">';
                        } else {
                            reInfo = data.openMoney + '<input name="rationDtls.maxMoney" type="hidden" value="' + data.maxMoney + '">';
                        }
                        return reInfo;
                    }
                },
                //操作
                {
                    data: null, render: function (data, row) {
                        var reInfo = '';
                        if (row > 0) {
                            reInfo = '<button data-type="del" class="layui-btn layui-btn-mini">' + bt.lang("common.button.delete") + '</button>';
                        }
                        return reInfo;
                    }
                }
            ],
            delRowCallback: function (data, $tr) {
                var rowIndex = $tr.find("input[name$='itemOrder']").val();
                bt.resetDelItem(rowIndex);
                //删除后更新其他行的授信品种选择
                $tr.remove();
                //金额合计减少
                bt.calSumMoney();
                comprowMR--;

                if ($("#compTable tbody tr").length == 1) {
                    $("#compButton").show();
                }

            },
            tableInitCallback: function () {
                initSelectCode();
                bt.dicts.init();
            }
        });
        comprowMR = 0;
        rowMR = 0;
    }

    var initSelectCode = function (row) {
        bt.dicts.init();
        bt.moneyType.init();
        //设置新增的品种不可选已勾选的品种
        bt.setNewItemDisable(row);
        bt.selectType.init();
    }

    initRationTable();
    initCompRationTable();
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    //初始化单位
    corpInit = bt.corp.init();
    var bankInfo=bt.bankInfo.init();//联行号
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }

    //下拉框
    $.each($("div[field='curCode']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });

    //下拉框
    $.each($("div[field='bankType']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            required: true,
            url: basePath + "/basic/business/btbanktype/json",
            change: function (data, text) {
                bt.setBankCode(data.value);
            }
        });
    });

    //检查每项授信品种是否有输入额度
    var checkItemMoney = function () {
        var success = true;
        $.each($("input[id$='money']"), function (i, item) {
            if (bt.empty($(item).val())) {
                success = false;
                return false;
            }
        });
        return success;
    };

    var checkBefore = function () {
        var corpId = selectTree.val();
        if (bt.empty(corpId)) {
            layer.alert(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
            return false;
        }
        var bankId = $("#bankCode").val();//银行
        if (bt.empty(bankId)) {
            //选择银行
            layer.alert(bt.lang("cms.provide.loaninfo.bankIdnull"), {icon: 2});
            return false;
        }
        if (singrowMR == 0 && comprowMR == 0) {
            layer.alert(bt.lang("table.ratbkrationdetail.customItemOne"));
            return false;
        }

        var moneySum = moneyType.get("moneysum").val();
        if (moneySum == 0) {
            layer.alert(bt.lang("table.ratbkrationdetail.itemmoneynull"));
            return false;
        }

        //综合品种要大于等于2条
        // if (comprowMR - 1 == 0) {
        //     layer.alert(bt.lang("table.ratbkrationdetail.compItemOne"));
        //     return false;
        // }

        if (!checkItemMoney()) {
            layer.alert(bt.lang("table.ratbkrationdetail.itemmoneynull"));
            return false;
        }
        if (isHangBail == "1") {//授信参数设置挂接保证金
            var isHookBond = dicts.get("isHookBond").val();
            if (isHookBond == "1") {//只有挂接保证金时才做相应的判断
                //判断授信单位与保证金账户单位是否一致
                var ratCorpId = $("#corpId").val();
                var bondCorpId = $("#bondCorpId").val();
                if (ratCorpId != bondCorpId) {
                    bt.loading.hide(); //关闭加载中
                    layer.alert(bt.lang("cms.rat.input.checkCorp"), {icon: 2});
                    return false;
                }

                //判断授信银行与保证金账户银行是否一致
                var ratBankAcc = $("#bankCode").val();
                var bondBankAcc = $("#bondBankId").val();
                if (ratBankAcc != bondBankAcc) {
                    bt.loading.hide(); //关闭加载中
                    layer.alert(bt.lang("cms.rat.input.checkBank"), {icon: 2});
                    return false;
                }

                //判断授信币别与保证金账户币别是否一致
                var ratCurId = selectTypes.get("curCode").val();
                var bondCurId = $("#bondCurId").val();
                if (ratCurId != bondCurId) {
                    bt.loading.hide(); //关闭加载中
                    layer.alert(bt.lang("cms.rat.input.checkCurId"), {icon: 2});
                    return false;
                }

                // 判断保证金的金额是否大于当前保证金账户的可用余额
                var applyMoney = moneyFun.toNumber($("#applyMoney").val());//保证金金额(就是项目的占用金额)
                var accAvailMoney = moneyFun.toNumber($("#accAvailMoney").val());//当前保证金账户的可用余额
                var leftMoney = bt.money.sub(accAvailMoney, applyMoney);
                if (leftMoney < 0) {
                    //如果保证金金额大于当前保证金账户的可用余额,那么应该提示用户
                    layer.alert(bt.lang("fcbond.check.applyMoney"), {icon: 2});
                    bt.loading.hide(); //关闭加载中
                    return false;
                }
            }
        }
        if (isGuarantee == "1") {//授信参数设置挂接担保合同
            if (!checkGuaMsg()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 暂存和提交数据
     *
     * @param data
     * @param url
     * @returns {boolean}
     */
    function submitData(data, url) {
        data.field.attachfilepath = up.getGroupId();//设置附件ID
        data.field.contractAttachfilepath = contractUp.getGroupId();//设置合同附件id
        var relateCon = $("#relateCon").val();
        var conType = $("#conType").val();
        if (relateCon == "1") {
            // 设置挂接合同类型id
            if (conType == "0") {
                var conTypeId = $("#conTypeId").val();
                if (null != conTypeId || undefined == conTypeId || " " == conTypeId){
                    conTypeId = getConTypeRat(data.field.corpId);
                }
                if (typeof conTypeId === "boolean"){
                    bt.loading.hide(); //关闭加载中
                    return false;
                }
                if (conTypeId == null || conTypeId == undefined || conTypeId == " ") {
                    return false;
                }
                data.field.conTypeId = conTypeId;
            } else {
                var conId = $("#conId").val();
                if (null == conId || undefined == conId || "" == conId) {
                    layer.alert(bt.lang("common.table.selectConstract"));
                    return false;
                }
            }
        }
        data.field.curName = selectTypes.get('curCode').getData().obj.curName
        bt.loading.show();//显示加载中
        $.post(basePath + url, data.field, function (result) {
            bt.loading.hide(); //关闭加载中
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    var webObj = bt.navtab.getParentDom();
                    webObj.search();
                    bt.navtab.closeCurrent();
                }
            }
        }, 'JSON');
    }

    //监听暂存
    form.on('submit(temp)', function (data) {
        if (!checkBefore()) {
            bt.loading.hide(); //关闭加载中
            return false;
        }

        var url = "/cms/rat/ratbkration/save";
        // 选择的银行信息中属性“授信合同是否必须”为是，则该字段为必填，否则不必填
        var creditContractRequired = $("#creditContractRequired").val();
        if (!bt.empty(creditContractRequired) && creditContractRequired === "1") {
            // 必填时，还需要根据参数：授信合同必须控制 来控制：0-不控制，1-提醒不控制，2-控制
            if (bt.empty(contractUp.getGroupId())) {
                if (creditContractControl === '1') {
                    //提醒不控制
                    layer.confirm(bt.lang("table.ratBkration.contractAttachFile.noUpload"), function (index) {
                        submitData(data, url)
                    });
                } else if (creditContractControl === '2') {
                    //请上传合同附件
                    layer.alert(bt.lang("table.ratBkration.contractAttachFilenull"), {icon: 2});
                    return false;
                } else {
                    submitData(data, url)
                }
            } else {
                submitData(data, url)
            }
        } else {
            submitData(data, url)
        }
        return false;
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        if (!checkBefore()) {
            bt.loading.hide(); //关闭加载中
            return false;
        }

        var url = "/cms/rat/ratbkration/submit";
        // 选择的银行信息中属性“授信合同是否必须”为是，则该字段为必填，否则不必填
        var creditContractRequired = $("#creditContractRequired").val();
        if (!bt.empty(creditContractRequired) && creditContractRequired === "1") {
            // 必填时，还需要根据参数：授信合同必须控制 来控制：0-不控制，1-提醒不控制，2-控制
            if (bt.empty(contractUp.getGroupId())) {
                if (creditContractControl === '1') {
                    //提醒不控制
                    layer.confirm(bt.lang("table.ratBkration.contractAttachFile.noUpload"), function (index) {
                        submitData(data, url)
                    });
                } else if (creditContractControl === '2') {
                    //请上传合同附件
                    layer.alert(bt.lang("table.ratBkration.contractAttachFilenull"), {icon: 2});
                    return false;
                } else {
                    submitData(data, url)
                }
            } else {
                submitData(data, url)
            }
        } else {
            submitData(data, url)
        }
        return false;
    });

    bt.changeRatCorp("1");
});

function setItemCode(data, elem, bv) {
    bt.setItemCode(data, elem, bv)
}

/**根据银行类型获取银行支行信息*/
function loadBtBankByType(data) {
    bt.setBankCode(data);
    var bankTypeVal = selectTypes.get("bankType").val();
    var typeid = bt.jquery.trim(bankTypeVal);
    if (!bt.empty(typeid)) {
        bt.jquery("[data-name='bankCode']").data("url", basePath + "/cms/provide/loaninfo/queryBtBankByType/" + typeid);
    } else {
        bt.jquery("[data-name='bankCode']").data("url", basePath + "/cms/provide/loaninfo/queryBtBankByType/null");
    }
    var bank = selectTypes.get("bankCode").reset();
}

//银行账号选择
function bankinfoCallback(data, dom) {
    $("#shouBankName").val(data.bankName);
    $("#bankName").val(data.bankName);
    $("#bankTypeName").val(data.bankTypeName);
    $("#bankCode").val(data.bankCode);//联行号
    $("#bankType").val(data.bankTypeId);//银行类别
    $("#bankId").val(data.id);//联行号id
    //重新根据授信单位去查找保证金相关的信息
    parameters.corpId = corpId;
    parameters.bankCode = $("#bankCode").val();
    parameters.curId = selectTypes.get("curCode").val();
    bt.bondAccInfo.set(parameters);
    useRatCode();

    // 选择的银行信息中属性“授信合同是否必须”为是，则该字段为必填，否则不必填
    var creditContractRequired = data.creditContractRequired;
    if (!bt.empty(creditContractRequired) && creditContractRequired === '1') {
        $("#contractAttachSpan").show();
        $("#creditContractRequired").val("1");
    } else {
        $("#contractAttachSpan").hide();
        $("#creditContractRequired").val("0");
    }
}

//根据币别查询保证金账户
function curinfoCallback(data) {
    $("#curId").val(data.id);
    parameters.corpId = corpId;
    parameters.bankCode = $("#bankCode").val();
    parameters.curId = data.id;
    bt.bondAccInfo.set(parameters);
//重新选择币别时，清空担保信息，重新选择担保合同
    resetGuaMsg();//清空担保信息
    $("#guaBtn").hide();
    useRatCode();
}

//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data, dom) {
    $("#bondId").val(data.bondId);//保证金Id
    $("#bondMoney").val(data.bondMoney);//保证金账户余额
    $("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
    $("#bankAcc").val(data.bankAcc);//保证金账户
    $("#bondCorpId").val(data.corpId);
    $("#bondBankId").val(data.bankCode);
    $("#bondCurId").val(data.curId);
}

//可结转授信编号查询
function useRatCode(){
    var corpId=$("#corpId").val();
    var bankType=$("#bankType").val();
    var curCode=selectTypes.get("curCode").val();
    if(!bt.empty(corpId)&&!bt.empty(bankType)&&!bt.empty(curCode)){
        selectTypes.get("oldRatCode").attr({
            "data-url": basePath + "/cms/rat/ratbkration/json?corpId="+corpId+"&bankType="+bankType+"&curCode="+curCode
        })
    }
}
//根据授信单位去查找保证金相关的信息
bt.corpCallback = function (id) {
    corpId = id;
    $("#corpId").val(id);
    $("#corpName").val(id);

    parameters.corpId = corpId;
    parameters.bankCode = $("#bankCode").val();
    parameters.curId = selectTypes.get("curCode").val();
    bt.bondAccInfo.set(parameters);
//结转授信得判断
    useRatCode();
    //重新选择单位时，清空担保信息，重新选择担保合同
    resetGuaMsg();//清空担保信息
    $("#guaBtn").hide();

    /**
     *合同挂接改变单位时清空数据
     */
    var relateCon = $("#relateCon").val();
    var conType = $("#conType").val();
    if (relateCon == "1" && conType == "1") {
        clearConInfo();
    }


};

//以下是计算项目期限
function computePrTerm() {
    var startDate = $.trim(dateTypes.get("begindate").val());
    var endDate = $.trim(dateTypes.get("enddate").val());
    var PrTerm;
    var month;
    if (bt.empty(startDate) || bt.empty(endDate)) {
        PrTerm = "";
    } else {
        var termUnit = $.trim(dicts.get("termUnit").val());
        var month = dataSubtract(startDate, endDate, "M");
        if (!bt.empty(termUnit)) {
            PrTerm = dataSubtract(startDate, endDate, termUnit);
            if (termUnit == "D") {
                //PrTerm = dataSubtract(startDate,endDate,"D");
            }
        } else {
            //为空或者null默认月处理
            dicts.get("termUnit").val("M");
            PrTerm = dataSubtract(startDate, endDate, "M");
        }
    }
    // moneyTypes.get("PrTerm").val(PrTerm);//设置借款期限
    //重新选择时间时，清空担保信息，重新选择担保合同
    resetGuaMsg();//清空担保信息
    $("#guaBtn").hide();

}


/**计算data1 与 data2 相差多少param单位 data为字符串*/
function dataSubtract(data1, data2, param) {
    var dates;
    if (param == "D") {
        dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime())) / (1000 * 60 * 60 * 24);
    } else {
        var start = new Date(data1);
        var end = new Date(data2);

        var addMonth = end.getMonth() - start.getMonth();//月份差

        var startLastDay = mGetDate(start.getYear(), start.getMonth());//开始时间当前月总共多少天
        var endLastDay = mGetDate(end.getYear(), end.getMonth());//结束时间当前月总共多少天

        if (startLastDay == start.getDate()) {//开始日期月末最后一天
            // if (endLastDay == end.getDate()) {//结束日期月末最后一天
            //     addMonth++;//月份+1
            // } else {
                if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            // }
        } else {
            if (endLastDay == end.getDate()) {//结束日期月末最后一天
                addMonth++;//月份+1
            } else {
                if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            }
        }

        dates = (end.getYear() - start.getYear()) * 12 + addMonth;
    }
    return dates;
}

function mGetDate(year, month) {
    var d = new Date(year, month + 1, 0);
    return d.getDate();
}

/**设置保证金比例*/
function setBailMoneyRate() {
    var moneysum = moneyTypes.get("moneysum").val();
    var bondPercent = rateTypes.get("bondPercent").val();
    var applyMoney = moneyTypes.get("applyMoney").val();
    var isHookBond = dicts.get('isHookBond').val();

    if (bt.empty(moneysum) || (isHookBond == "0") || (isHookBond == null)) {
        return;
    }

    if (bt.money.sub(applyMoney, moneysum) > 0) {//判断保证金金额是否大于授信金额
        parent.layer.msg(bt.lang("cms.rat.input.checkBondMoney"), {icon: 2});
        moneyTypes.get("applyMoney").val(moneysum);
        rateTypes.get("bondPercent").val('100');
        return;
    }

    if (!bt.empty(applyMoney) && applyMoney != 0) {
        rateTypes.get("bondPercent").val(fun.div(applyMoney, moneysum, 8));
    }
}

/**设置保证金*/
function setBailMoney() {
    var moneysum = moneyTypes.get("moneysum").val();
    var bondPercent = rateTypes.get("bondPercent").val();
    var applyMoney = moneyTypes.get("applyMoney").val();
    var isHookBond = dicts.get('isHookBond').val();

    if (bt.empty(moneysum) || (isHookBond == "0") || (isHookBond == null)) {
        return;
    }
    if (!bt.empty(bondPercent) && bondPercent != 0) {
        moneyTypes.get("applyMoney").val(fun.format(fun.mul(moneysum, bondPercent), {separator: "", decimal: 2}));
    }
}

//如果选择了否,清空填写的保证金占用信息
bt.changeFcBond = function (isHookFcBond) {
    if (isHookFcBond.value == "0") {
        $(".isShow").hide();
        $("#applyMoney").removeAttr('lay-verify');
        $("#bankAcc").removeAttr('lay-verify');
        moneyTypes.get("applyMoney").val("");
        rateTypes.get("bondPercent").val("");
        $("#bankAcc").val("");
        $("#bondContract").val("");
    } else {
        $(".isShow").show();
        $("#applyMoney").attr('lay-verify', 'required')
        $("#bankAcc").attr('lay-verify', 'required')
    }
    form.renderIcon();
}


//提示未选择银行
function checkCorp() {
    var bankCode = $("#bankCode").val();
    if (bt.empty(bankCode)) {
        layer.alert(bt.lang("table.fcBond.bank.null"), {icon: 2});
        return false;
    } else {
        return true;
    }
}


function openAssure(el, rowIndex) {
    var assurepercent = bt.jquery("#assurepercent" + rowIndex).val();
    var procedureFeeRate = bt.jquery("#procedureFeeRate" + rowIndex).val();
    var bondMoney = bt.jquery("#bondMoney" + rowIndex).val();
    var procedureFee = bt.jquery("#procedureFee" + rowIndex).val();

    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        //content : html,
        content: basePath + "/cms/rat/ratbkration/setAssure?rowIndex=" + rowIndex + "&assurepercent=" + bt.jquery("#assurepercent" + rowIndex).val()
        + "&procedureFeeRate=" + bt.jquery("#procedureFeeRate" + rowIndex).val() + "&bondMoney=" + bt.jquery("#bondMoney" + rowIndex).val()
        + "&procedureFee=" + bt.jquery("#procedureFee" + rowIndex).val(),
        end: function () {
            for (var i = 0; i < arguments.length; i++) {
                //console.log(arguments[i]);
            }
        }
    });
}


function getAssure(rowIndex, assurepercent, procedureFeeRate, bondMoney, procedureFee) {
    bt.jquery("#assurepercent" + rowIndex).val(assurepercent);
    bt.jquery("#procedureFeeRate" + rowIndex).val(procedureFeeRate);
    bt.jquery("#bondMoney" + rowIndex).val(bondMoney);
    bt.jquery("#procedureFee" + rowIndex).val(procedureFee);

    //bt.calIndexOpenMoney(rowIndex);不用计算
}

function setOtherParam(param){
    var corpId = $("#corpId").val();//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    if(viewFlag=="1"){
        startDate = $("#startDate").val();//开始日期
        endDate = $("#enddate").val() ;//结束日期
    }else{
        startDate = $.trim(dateTypes.get("begindate").val());//开始日期
        endDate = $.trim(dateTypes.get("enddate").val());//结束日期
    }
    var curId = $("#curId").val();//币别
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

function selectBefore(){
    var corpId = $("#corpId").val();
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("table.rat.corp.null"), {icon: 2});
        return false;
    }else{
        return true;
    }
}

/**
 * 合同类型选择回调函数
 * @param conTypeId
 */
function setConTypeCallback(conTypeId){
    $("#conTypeId").val(conTypeId);
    $("#submit").trigger('click');
}

//会出现加载问题，一开始触发会触发不了
function changeCorp (data) {
    var ratMode = data.value;
    corpInit = bt.corp.init();
    bt.changeRatCorp(ratMode);
    if("1" == ratMode){
        selectTypes.get("oldRatCode").attr("data-disabled",false);
    }else {
        selectTypes.get("oldRatCode").attr("");
        selectTypes.get("oldRatCode").attr("data-disabled",true);
    }
    form.render();
    /*if("1" == ratMode){
        corpInit.get("corpId").setUrl("all");
    } else {
        corpInit.get("corpId").setUrl("rat");
    }*/
    //corpInit.get("corpId").render();
}
