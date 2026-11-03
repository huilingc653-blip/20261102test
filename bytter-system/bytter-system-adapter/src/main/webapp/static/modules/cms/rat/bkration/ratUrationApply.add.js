var rowMR = 0;
var selectTypes;
var moneyType;
var ratioType;
var fun;
var datatable;
var parameters;
var exChangeMoneyRate;
var exChangeBondMoneyRate;
var isGuarantee;
var isHangGuarantee;
var mulselects;
var corpId;
var ratCode;
var $;
var form,dicts;
bt.use(['form', 'select', 'layedit', 'btdate', 'bttable', 'moneynum', 'dicts', 'select', 'moneyType', 'dateType', 'corp', 'selectType', 'money', 'ratioType', 'checkboxs', 'datatable2', 'mulselect', 'btUpload', 'btImport'], function () {
    $ = bt.jquery;
    form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    ratCode = $("#ratCode").val();
    corpId = $("#corpId").val();
    fun = bt.moneyType.getFun();
    parameters = new Object();
    exChangeMoneyRate = 1;// 占用金额汇率
    exChangeBondMoneyRate = 1;// 保证金额汇率


    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        return typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ";

    }

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //综合设置占用额度不可编辑
    bt.setMoneyEditable = function (flag, rowIndex) {
       /* if (flag == "0") {
            $("input[data-id='" + rowIndex + "money']").removeAttr("readonly");
        } else {
            $("input[data-id='" + rowIndex + "money']").attr("readonly", "readonly");
        }*/
        $("input[data-id='" + rowIndex + "money']").removeAttr("readonly");
    }

    //设置保证金比例利率
    bt.setAssure = function (rowIndex, data) {
        //moneyType.get(rowIndex + "assurepercent").val(data.assurepercent);
        //moneyType.get(rowIndex + "interestrate").val(data.interestrate);
    }

    bt.calUMoney = function (data, el) {
        var dataId = $(el).attr("data-id");
        var index = dataId.indexOf("maxMoney");
        var rowIndex = dataId.substr(0, index);
        var itemOrder = dataId.substr(index);
        var money = moneyType.get(rowIndex + "money").val();
        bt.setMoney(rowIndex);
    }

    bt.setMoney = function (rowIndex) {
        var sumMoney = 0;
        $.each($("input[data-id^='" + rowIndex + "maxMoney']"), function (i, item) {
            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        moneyType.get(rowIndex + "money").val(sumMoney);
    }

    //检查最高额度与授信额度关系
    bt.checkMaxMoney = function (data, el) {
        var dataId = $(el).attr("data-id");
        var moneyIndex = dataId.indexOf("maxMoney");
        var rowIndex = dataId.substr(0, moneyIndex);
        var moneyIndexVal = moneyType.get(compRowIndex + "money").val();
        if (bt.empty(moneyIndexVal)) {
            layer.alert(bt.lang("table.ratbkrationdetail.moneymsg"));
            moneyType.get(rowIndex + "maxMoney").val(0);
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
        var rowIndex = $(el).attr("rowindex");
        //var moneyIndex = dataId.indexOf("money");
        //var rowIndex = dataId.substr(0, moneyIndex);
    }

    bt.setMaxMoney = function (data, el) {
        var dataId = $(el).attr("data-id");
        var index = dataId.indexOf("money");
        var rowIndex = dataId.substr(0, index);
        var itemFlag = $("#itemFlag" + rowIndex).val();
        if (itemFlag == '1') {
            var itemOrder = $("#itemFlag" + rowIndex).attr("itemOrder");
            var money = moneyType.get(rowIndex + "money").val();
            moneyType.get(rowIndex + "maxMoney" + itemOrder).val(money);
            bt.setMoney(rowIndex);
        } else {
            return;
        }

    }
    //循环计算授信额度
    bt.calSumMoney = function () {
        var sumMoney = 0;
        $.each($("input[id$='money']"), function (i, item) {
            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        moneyType.get("moneysum").val(bt.money.format(sumMoney));
    }
    //循环计算敞口授信额度
    bt.calSumOpenMoney = function () {
        var sumMoney = 0;
        $.each($("input[id$='openMoney']"), function (i, item) {
            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        moneyType.get("openMoneysum").val(bt.money.format(sumMoney));
    }

    bt.setDays = function (data, el) {
        var rowIndex = $(el).attr("rowindex");
        var beginDate = bt.dateType.init().get(rowIndex + "beginDate").val();
        if (bt.empty(beginDate)) {
            return;
        }
        var endDate = bt.dateType.init().get(rowIndex + "endDate").val();
        if (bt.empty(endDate)) {
            return;
        }
        var days = getBetweenDay(beginDate, endDate) - (-1);
        $("#" + rowIndex + "days").val(days);
    }

    //生成列表
    datatable = bt.datatable2.init({
        elem: "#singleTable1",
        scrollY: '430px',
        resize: false,
        colReorder: false,
        ordering: false,
        lengthMenu: [500],//每页显示多少条
        fixedColumns: {
            rightColumns: 1
        },
    });

    //新增行事件
    $("#add").click(function () {
        var beginDate = $("#beginDate").val();

        var endDate = $("#endDate").val();
        var disCode = $("#disCode").val();

        var ratCurId = $("#curCode").val();//授信币种

        var isOpenRat = $("#isOpenRat").val();//是否启用授信敞口

        isGuarantee = $("#isGuarantee").val();//参数设置是否挂接担保
        isHangGuarantee = $("#isHangGuarantee").val();//授信是否挂接担保

        rowMR++;
        datatable.addRow(
            ['<td>' + rowMR + '</td>',
                // 占用授信品种
                '<td><div class="select_width"><div class="bt-biz-selectType" data-name="applyDtls[' + rowMR + '].itemCode" id="' + rowMR + 'itemCode" data-method="post" data-url="' + basePath + '/cms/rat/raturationapply/getUrationItem?disCode=' + disCode + '" '
                + 'data-valuefield="itemCode" data-textfield="itemName" id="' + rowMR + 'itemCode" data-biz-name="' + rowMR + 'itemCode" data-search="false"  data-link="true"  data-callback="bt.setSysItem" ></div></div><span id="sysItemCode' + rowMR + '"></span></td>',
                // 授信类型
                '<td><input type="text"  id="' + rowMR + 'itemFlag" maxlength="20" class="layui-input" disabled="disabled" style="width: 100%"></td>',
                // 业务品种
                '<td ><div id="billNameSpan' + rowMR + '"><div class="layui-input-inline"><select name="applyDtls[' + rowMR + '].billName" id="' + rowMR + 'billName"   lay-search="" lay-filter="type">'
                + '<option value=""></option></select></div></div></td>',
                // 是否启用授信敞口
                '<td><div class="layui-inline"><div class="bt-biz-dict" data-type="radio" data-group="sf_yn" data-defaultvalue="0" data-name="applyDtls[' + rowMR + '].isOpenRat" data-biz-name="' + rowMR + 'isOpenRat" onclick="changeOpenRat(' + rowMR + ')"></div></div></td>',
                // 业务币种
                '<td ><div class=layui-input-inline">' + '<input type="hidden" id="' + rowMR + 'initCurCode" value="">' +
                '<div class="bt-biz-selectType" data-biz-name="' + rowMR + 'initCurCode" data-url="' + basePath + '/cms/rat/raturationapply/getBillCurList?ratCurId=' + ratCurId + '" data-valuefield="id" data-defaultvalue="' + ratCurId + '" data-textfield="curName" data-required="true" data-callback="bt.initCurIdCallback"' +
                'data-name="applyDtls[' + rowMR + '].initCurCode" data-search="true" data-row="' + rowMR + '"></div></div></td>',
                // 业务金额
                '<td><input type="hidden" id="' + rowMR + 'exchangeMoney"><input type="hidden" id="' + rowMR + 'exchangeBondMoney">' +
                '<input type="text" data-name="applyDtls[' + rowMR + '].billMoney" id="' + rowMR + 'billMoney" data-biz-name="' + rowMR + 'billMoney" class="layui-input bt-biz-moneyType" rowindex="' + rowMR + '" data-decimal="2" data-min="0" data-minus="false" lay-verify="required" data-row="' + rowMR + '"  data-callback="bt.billMoneyCallback"></td>',
                // 保证金币种
                '<td ><div id="bondCurId' + rowMR + '"><div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="' + rowMR + 'bondCurId" data-url="' + basePath + '/cms/rat/raturationapply/getBondCurList?billCurId=' + ratCurId + '&corpId=' + corpId + '"  data-valuefield="id" data-defaultvalue="' + ratCurId + '" data-textfield="curName" data-required="true" ' +
                'data-name="applyDtls[' + rowMR + '].bondCurId" data-search="true"  data-callback="bt.bondCurIdCallback" data-row="' + rowMR + '"></div></div></div></td>',
                // 保证金比例
                '<td><div class="layui-input-inline"><input type="text" data-name="applyDtls[' + rowMR + '].assurepercent" value="0" data-decimal="2" data-max="100" class="layui-input bt-biz-ratioType" id="' + rowMR + 'assurepercent" data-biz-name="' + rowMR + 'assurepercent" data-callback="bt.setBailMoney" data-row="' + rowMR + '" data-width="170px"></div></td>',
                // 保证金金额
                '<td><div class="layui-input-inline"><input type="text" id="' + rowMR + 'bondMoney" name="applyDtls[' + rowMR + '].bondMoney" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + rowMR + 'bondMoney" data-callback="bt.setBailMoneyRate" data-row="' + rowMR + '"></div></td>',
                // 占用授信金额
                '<td><input type="text" data-name="applyDtls[' + rowMR + '].money" id="' + rowMR + 'money" data-biz-name="' + rowMR + 'money" class="layui-input bt-biz-moneyType" rowindex="' + rowMR + '" data-decimal="2" data-min="0" data-minus="false"   lay-verify="required" readonly="readonly"></td>', //data-callback="bt.setMaxMoney"
                // 担保方式
                '<td><div class="layui-form-item"> <div class="layui-inline"><div class="selectDemo bt-biz-mulselectType" data-biz-name="' + rowMR + 'guaWay" data-url="' + basePath + '/cms/guarantee/guaContractInfo/json" data-method="get" data-valuefield="code" data-textfield="name" data-name="applyDtls[' + rowMR + '].guaWay" data-row="' + rowMR + '" data-required="required" data-defaultvalue="' + $("#guaWay").val() + '"></div></div></div>',//data-callback="setGuaWays"
                '<td><input type="text" name="applyDtls[' + rowMR + '].beginDate" lay-verify="required" data-min="' + beginDate + '" data-initmax="' + endDate + '" id="' + rowMR + 'beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="' + rowMR + 'beginDate" rowindex="' + rowMR + '" data-endid="' + rowMR + 'endDate" data-callback="bt.setDays" lay-verify="required" ></td>',
                '<td><input type="text" name="applyDtls[' + rowMR + '].endDate" lay-verify="required" id="' + rowMR + 'endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="' + rowMR + 'endDate" rowindex="' + rowMR + '" data-callback="bt.setDays" lay-verify="required" ></td>',
                '<td><input type="text" name="applyDtls[' + rowMR + '].days" id="' + rowMR + 'days" readonly="readonly" class="layui-input" lay-verify="required" ></td>',
                '<td><input type="text" name="applyDtls[' + rowMR + '].contactNo" lay-verify="required"  id="' + rowMR + 'contactNo" maxlength="50" class="layui-input"></td>',
                '<td><input type="text" name="applyDtls[' + rowMR + '].lcNo" id="' + rowMR + 'lcNo" maxlength="32" class="layui-input"></td>',
                '<td><input type="text" name="applyDtls[' + rowMR + '].billNo" id="' + rowMR + 'billNo" maxlength="32" class="layui-input"></td>',
                '<td><input type="text" name="applyDtls[' + rowMR + '].remark" id="' + rowMR + 'remark" maxlength="100" class="layui-input remark"></td>',
                '<td>&nbsp;&nbsp;&nbsp;&nbsp;\t'
                + '<button  type="button" class="layui-btn layui-btn-mini remove_row">' + bt.lang("common.button.delete") + '</button>'
                + '</td>'
            ]
        )
        if (isOpenRat === "0") {//不启用授信敞口
            datatable.setColumnShow(4, false);//隐藏授信敞口选择列
        }

        if (isGuarantee === "0") {//参数设置不挂接担保
            datatable.setColumnShow(11, false);//隐藏担保方式列
        }

        initSelectCode(rowMR);
        return false;
    });

    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();


    function checkBefore() {
        // 相同占用授信品种不能提交
        var $trs = $('#singleTable1').find('tbody tr');// 提交的占用数据条数

        if ($trs.length <= 0) {
            layer.alert(bt.lang("cms.rat.add.item.null"));
            return false;
        }
        if ($trs.length === 1 && $($trs[0]).find('td:eq(0)').html() === '暂无数据') {
            layer.alert(bt.lang("cms.rat.add.item.null"));
            return false;
        }
        return true;
    }

    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();//显示加载中
        if (!checkBefore()) {
            bt.loading.hide(); //关闭加载中
            return false;
        }
        data.field.attachmentId = up.getGroupId();//设置附件ID
        var action = "/cms/rat/raturationapply/save";
        $.post(basePath + action, data.field, function (result) {
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
        return false;
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();//显示加载中
        if (!checkBefore()) {
            bt.loading.hide(); //关闭加载中
            return false;
        }
        data.field.attachmentId = up.getGroupId();//设置附件ID
        var action = "/cms/rat/raturationapply/submit";
        $.post(basePath + action, data.field, function (result) {
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
        return false;
    });

    bt.close = function () {
        if (!bt.navtab) {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
        } else {
            //bt.navtab.refreshParent();
            bt.navtab.closeCurrent();
        }
    }

    //修改授信品种改变业务名称
    bt.setSysItem = function (typeVal, elem, bv, wrapObj) {
        //异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
        var dataId = $(wrapObj).attr("id");
        var moneyIndex = dataId.indexOf("itemCode");
        var rowIndex = dataId.substr(0, moneyIndex);
        var customId = typeVal.itemcode;
        var itemFlag = typeVal.itemflag;

        var htmlspan = '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="' + rowIndex + 'billName" data-url="' + basePath + '/cms/rat/ratBusinessRace/getBillByCustomId?customId=' + customId + '"  data-valuefield="id" data-textfield="raceName" data-defaultvalue="first"' +
            'data-name="applyDtls[' + rowIndex + '].billName" data-search="true"></div></div>';
        $("#billNameSpan" + rowIndex).empty();
        $("#billNameSpan" + rowIndex).html(htmlspan);
        bt.selectType.init();
        form.render("select");

        var html = '';
        if ("0" == itemFlag) {
            html = '<input type="hidden" name="applyDtls[' + rowIndex + '].itemFlag" id="itemFlag' + rowIndex + '" value="0">'
                + '<input type="hidden" data-name="applyDtls[' + rowIndex + '].applyDtls[' + typeVal.itemorder + '].maxMoney" data-biz-name="' + rowIndex + 'maxMoney" data-id="' + rowIndex + 'maxMoney' + typeVal.itemorder + '" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false"/>'
                + '<input type="hidden"  data-biz-name="' + rowIndex + 'itemMaxMoney" data-id="' + rowIndex + 'itemMaxMoney' + typeVal.itemorder + '" class="layui-input bt-biz-moneyType" data-decimal="2" value="' + typeVal.money + '" data-minus="false"/>'
                + '<input type="hidden" name="applyDtls[' + rowIndex + '].ratCreditType"  value="' + typeVal.credittype + '">';
        } else {
            html += '<input type="hidden" name="applyDtls[' + rowIndex + '].itemFlag"  id="itemFlag' + rowIndex + '" itemOrder="' + typeVal.itemorder + '" value="1">'
                + '<input type="hidden" name="applyDtls[' + rowIndex + '].applyDtls[' + typeVal.itemorder + '].itemCode" rowIndex="' + rowIndex + '" itemorder="' + typeVal.itemorder + '" id="' + rowIndex + 'itemCode' + typeVal.itemorder + '" value="' + typeVal.itemcode + '">'
                + '<input type="hidden" data-name="applyDtls[' + rowIndex + '].applyDtls[' + typeVal.itemorder + '].maxMoney" data-biz-name="' + rowIndex + 'maxMoney" data-id="' + rowIndex + 'maxMoney' + typeVal.itemorder + '" class="layui-input bt-biz-moneyType" data-decimal="2"  data-minus="false"/>'
                + '<input type="hidden" data-biz-name="' + rowIndex + 'itemMaxMoney" data-id="' + rowIndex + 'itemMaxMoney' + typeVal.itemorder + '" class="layui-input bt-biz-moneyType" data-decimal="2" value="' + typeVal.maxmoney + '" data-minus="false"/>'
                + '<input type="hidden" name="applyDtls[' + rowIndex + '].ratCreditType"  value="' + typeVal.credittype + '">';

        }

        if ("0" === itemFlag) {
            bt.setMoneyEditable("0", rowIndex);
            $("#sysItemCode" + rowIndex).html(html);
            $("#" + rowIndex + "itemFlag").val(bt.dict("RatModel.ItemFlag", "0"));
        } else {
            //综合品种
            $("#sysItemCode" + rowIndex).html(html);
            $("#" + rowIndex + "itemFlag").val(bt.dict("RatModel.ItemFlag", "1"));
        }
        //获取授信品种的调剂额度
        bt.moneyType.init();
        form.render('select');
        bt.selectType.init();
    }

    // 业务币种change事件，设置保证金币别
    bt.initCurIdCallback = function (data, elem, bv, wrapObj) {

        var ratCurCode = $("#curCode").val();
        var rowIndex = $(wrapObj).data("row");
        // 业务币种切换时，清空业务金额、保证金金额、占用授信金额
        moneyType.get(rowIndex + "billMoney").val("");//业务金额
        moneyType.get(rowIndex + "bondMoney").val("")//保证金金额
        moneyType.get(rowIndex + "money").val("")//占用授信金额

        if (bt.empty(data.id)) {
            exChangeMoneyRate = 1;
            return false;
        }

        $("#" + rowIndex + "initCurCode").val(data.id);
        var html = '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="' + rowIndex + 'bondCurId" data-url="' + basePath + '/cms/rat/raturationapply/getBondCurList?billCurId=' + data.id + '&corpId=' + corpId + '"  data-valuefield="id" data-defaultvalue="first" data-textfield="curName" data-required="true" ' +
            'data-name="applyDtls[' + rowIndex + '].bondCurId" data-search="true"  data-callback="bt.bondCurIdCallback" data-row="' + rowIndex + '"></div></div>';
        $("#bondCurId" + rowIndex).empty();
        $("#bondCurId" + rowIndex).html(html);
        bt.selectType.init();

        if (data.id !== ratCurCode) {//业务币别与授信币别不一致，需要查询对应的折算汇率
            $.post(basePath + "/cms/rat/raturationapply/getNearCurRat", {
                curId: data.id,//业务币别
                convertCurId: ratCurCode,//授信币别
                date: $("#beginDate").val()//授信开始时间
            }, function (retData) {
                exChangeMoneyRate = retData;
            }, 'JSON');
        } else {
            exChangeMoneyRate = 1;
        }

        //业务币别发生变化时重新设置保证金金额
        var billMoney = moneyType.get(rowIndex + "billMoney").val();//业务金额
        var bondMoney = moneyType.get(rowIndex + "bondMoney").val();//保证金金额
        if ((!bt.empty(billMoney)) && (!bt.empty(bondMoney))) {
            setBondMoney(rowIndex);
        }

    }


    /**根据业务金额和保证金比例设置保证金金额*/
    bt.setBailMoney = function (data, elem) {
        var rowIndex = $(elem).data("row");
        setBondMoney(rowIndex);
    }

    // 业务金额change事件
    bt.billMoneyCallback = function (data, elem) {
        var rowIndex = $(elem).data("row");
        var isOpenRat = dicts.get(rowIndex + "isOpenRat").val(); // 是否启用银行授信敞口
        var exChangeMoney = fun.mul(exChangeMoneyRate, data);//币别*汇率
        var exChangeBondMoney = fun.mul(exChangeBondMoneyRate, data);//币别*汇率
        $("#" + rowIndex + "exchangeMoney").val(exChangeMoney);//设置占用授信转换金额
        $("#" + rowIndex + "exchangeBondMoney").val(exChangeBondMoney);//设置保证金转换金额
        var bondPercent = $("#" + rowIndex + "assurepercent").val();//保证金比例
        if (!bt.empty(bondPercent)) {
            //setBondMoney(rowIndex);//设置保证金金额
            var billCurId = selectTypes.get(rowIndex + "initCurCode").val();//业务币种
            var bondCurId = selectTypes.get(rowIndex + "bondCurId").val();//保证金币种
            var billMoney;
            if (billCurId === bondCurId) {
                billMoney = moneyType.get(rowIndex + "billMoney").val();//业务金额
            } else {
                billMoney = $("#" + rowIndex + "exchangeBondMoney").val();//业务金额(进行过汇率转换的)
            }

            if (bt.empty(billMoney)) {
                return;
            }
            if (bondPercent !== 0) {//设置保证金金额
                // 占用金额，减去保证金金额
                if (isOpenRat === "1"){
                    exChangeMoney = fun.sub(exChangeMoney, fun.mul(billMoney, bondPercent));
                }
                moneyType.get(rowIndex + "bondMoney").val(fun.format(fun.mul(billMoney, bondPercent), {
                    separator: "",
                    decimal: 2
                }));
            } else {
                moneyType.get(rowIndex + "bondMoney").val('');
            }
        }

        // 占用授信金额
        moneyType.get(rowIndex + "money").val(fun.format(exChangeMoney, {
            separator: "",
            decimal: 2
        }));

        var itemFlag = $("#itemFlag" + rowIndex).val();
        if (itemFlag === "1") {//综合品种
            moneyType.get(rowIndex + "maxMoney").val(exChangeMoney, {
                separator: "",
                decimal: 2
            });
        }
    }

    /**根据保证金金额设置保证金比例*/
    bt.setBailMoneyRate = function (data, elem) {
        var rowIndex = $(elem).data("row");
        var isOpenRat = dicts.get(rowIndex + "isOpenRat").val(); // 是否启用银行授信敞口
        var billCurId = selectTypes.get(rowIndex + "initCurCode").val();//业务币种
        var bondCurId = selectTypes.get(rowIndex + "bondCurId").val();//保证金币种
        var billMoney;
        if (billCurId === bondCurId) {
            billMoney = moneyType.get(rowIndex + "billMoney").val();//业务金额
        } else {
            billMoney = $("#" + rowIndex + "exchangeBondMoney").val();//业务金额(进行过汇率转换的)
        }

        var bondMoney = data;
        if (bt.empty(billMoney) || bt.empty(bondMoney)) {
            return;
        }

        if (bt.money.sub(bondMoney, billMoney) > 0) {//判断保证金金额是否大于业务金额
            if (isOpenRat === "1"){
                moneyType.get(rowIndex + "money").val(0);
            }
            moneyType.get(rowIndex + "bondMoney").val(billMoney);
            ratioType.get(rowIndex + "assurepercent").val(fun.format(1));
            parent.layer.msg(bt.lang("cms.rat.apply.checkBondMoney"), {icon: 2});
            return;
        }

        if (!bt.empty(bondMoney) && bondMoney !== 0) {
            ratioType.get(rowIndex + "assurepercent").val(fun.div(bondMoney, billMoney, 8));
            if (isOpenRat === "1"){
                moneyType.get(rowIndex + "money").val(fun.format(fun.sub(billMoney,bondMoney), {
                    separator: "",
                    decimal: 2
                }));
            }
        }

    }

    //删除某一行
    $("table").on('click', '.remove_row', function () {
        var index = $(this).parents('tr').data('dt-row');
        var $tr = $("#singleTable1").find('tbody tr:eq(' + index + ')');
        if ($tr[0]) {
            datatable.removeRow($tr);
        }
    })

});

//设置保证金金额
function setBondMoney(rowIndex) {
    var billCurId = selectTypes.get(rowIndex + "initCurCode").val();//业务币种
    var bondCurId = selectTypes.get(rowIndex + "bondCurId").val();//保证金币种
    var bondPercent = $("#" + rowIndex + "assurepercent").val();//保证金比例
    var isOpenRat = dicts.get(rowIndex + "isOpenRat").val(); // 是否启用银行授信敞口
    var billMoney;
    if (billCurId === bondCurId) {
        billMoney = moneyType.get(rowIndex + "billMoney").val();//业务金额
    } else {
        billMoney = $("#" + rowIndex + "exchangeBondMoney").val();//业务金额(进行过汇率转换的)
    }

    if (bt.empty(billMoney)) {
        return;
    }
    if (!bt.empty(bondPercent) && bondPercent !== 0) {//设置保证金金额
        moneyType.get(rowIndex + "bondMoney").val(fun.format(fun.mul(billMoney, bondPercent), {
            separator: "",
            decimal: 2
        }));
        if (isOpenRat === "1"){
            moneyType.get(rowIndex + "money").val(fun.format(fun.sub(billMoney, fun.mul(billMoney, bondPercent)), {
                separator: "",
                decimal: 2
            }));
        }else{
            moneyType.get(rowIndex + "money").val(fun.format(billMoney, {
                separator: "",
                decimal: 2
            }));
        }
    } else {
        moneyType.get(rowIndex + "bondMoney").val('');
    }
}

/**
 * 得到两个日期之间的天数
 * @param startDate
 * @param endDate
 */
function getBetweenDay(startDate, endDate) {
    var date1 = bt.date.get(startDate);
    var date2 = bt.date.get(endDate);
    var btt = date2 - date1;
    return btt / (1000 * 60 * 60 * 24);
}

closeTab = function () {
    bt.close();
}

//调整是否启用敞口授信
function changeOpenRat(rowIndex) {
    setBondMoney(rowIndex);
}

function resetTab() {
    location.href = basePath + "/cms/rat/raturationapply/add?ratCode=" + ratCode + "&corpId=" + corpId
}

//json转url参数
var parseParam = function (param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function (i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};

function initSelectCode(row) {
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    mulselects = bt.mulselect.init();
    ratioType = bt.ratioType.init();
    bt.selectType.init();
    $('[data-biz-name="' + row + 'money"]').attr("readonly", "readonly");
    // 担保方式
    if (isHangGuarantee == "1") {//授信挂担保，显示银行授信录入的担保方式，不能修改
        mulselects.get(row + "guaWay").attr({
            'data-disabled': true,
            'data-itemsdisabled': $("#allGuaWayCode").val()
        });//设置下拉框禁用
    }
}

var applyDtlsData = []
bt.setApplyDtlsData = function () {
    return applyDtlsData;
}

/**
 * 导入功能
 */
function importExcel() {
    up = bt.btImport.init({
        code: "70000067",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        params: {
            ratCode: $("#ratCode").val(),
            disCode: $("#disCode").val(),
            ratCurId: $("#curCode").val(),
            corpId: $("#corpId").val(),
        },
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            var beginDate = $("#beginDate").val();
            var endDate = $("#endDate").val();

            var isOpenRat = $("#isOpenRat").val();//是否启用授信敞口

            isGuarantee = $("#isGuarantee").val();//参数设置是否挂接担保
            isHangGuarantee = $("#isHangGuarantee").val();//授信是否挂接担保

            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                applyDtlsData = item.itemDisList;
                rowMR++;
                datatable.addRow(
                    ['<td>' + rowMR + '</td>',
                        // 占用授信品种
                        '<td><div class="select_width"><div class="bt-biz-selectType" data-name="applyDtls[' + rowMR + '].itemCode" id="' + rowMR + 'itemCode" data-getInitData="bt.setApplyDtlsData" '
                        + 'data-valuefield="itemCode" data-textfield="itemName" id="' + rowMR + 'itemCode" data-biz-name="' + rowMR + 'itemCode" data-search="false" data-link="true" data-callback="bt.setSysItem" data-defaultvalue="' + item.itemCode + '"></div></div><span id="sysItemCode' + rowMR + '"></span></td>',
                        // 授信类型
                        '<td><input type="text" id="' + rowMR + 'itemFlag" maxlength="20" class="layui-input" value="' + item.itemFlagName + '" disabled="disabled" style="width: 100%"></td>',
                        // 业务品种
                        '<td><div id="billNameSpan' + rowMR + '"><div class="layui-input-inline"><select name="applyDtls[' + rowMR + '].billName" id="' + rowMR + 'billName" lay-search="" lay-filter="type">'
                        + '<option value=""></option></select></div></div></td>',
                        // 是否启用授信敞口
                        '<td><div class="layui-inline"><div class="bt-biz-dict" data-type="radio" data-group="sf_yn" data-defaultvalue="' + item.isOpenRat + '" data-name="applyDtls[' + rowMR + '].isOpenRat" data-biz-name="' + rowMR + 'isOpenRat" onclick="changeOpenRat(' + rowMR + ')"></div></div></td>',
                        // 业务币种
                        '<td ><div class=layui-input-inline">' + '<input type="hidden" id="' + rowMR + 'initCurCode" value="">' +
                        '<div class="bt-biz-selectType" data-biz-name="' + rowMR + 'initCurCode" data-url="' + basePath + '/cms/rat/raturationapply/getBillCurList?ratCurId=' + item.initCurCode + '" data-valuefield="id" data-defaultvalue="' + item.initCurCode + '" data-textfield="curName" data-required="true" data-callback="bt.initCurIdCallback"' +
                        'data-name="applyDtls[' + rowMR + '].initCurCode" data-search="true" data-row="' + rowMR + '"></div></div></td>',
                        // 业务金额
                        '<td><input type="hidden" id="' + rowMR + 'exchangeMoney"><input type="hidden" id="' + rowMR + 'exchangeBondMoney">' +
                        '<input type="text" data-name="applyDtls[' + rowMR + '].billMoney" id="' + rowMR + 'billMoney" data-biz-name="' + rowMR + 'billMoney" class="layui-input bt-biz-moneyType" rowindex="' + rowMR + '" data-decimal="2" data-min="0" data-minus="false" lay-verify="required" data-row="' + rowMR + '" value="' + item.billMoney + '" data-callback="bt.billMoneyCallback"></td>',
                        // 保证金币种
                        '<td ><div id="bondCurId' + rowMR + '"><div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="' + rowMR + 'bondCurId" data-url="' + basePath + '/cms/rat/raturationapply/getBondCurList?billCurId=' + item.bondCurId + '&corpId=' + corpId + '"  data-valuefield="id" data-defaultvalue="' + item.bondCurId + '" data-textfield="curName" data-required="true" ' +
                        'data-name="applyDtls[' + rowMR + '].bondCurId" data-search="true"  data-callback="bt.bondCurIdCallback" data-row="' + rowMR + '"></div></div></div></td>',
                        // 保证金比例
                        '<td><div class="layui-input-inline"><input type="text" data-name="applyDtls[' + rowMR + '].assurepercent" value="' + item.assurepercent + '" data-decimal="2" data-max="100" class="layui-input bt-biz-ratioType" id="' + rowMR + 'assurepercent" data-biz-name="' + rowMR + 'assurepercent" data-callback="bt.setBailMoney" data-row="' + rowMR + '" data-width="170px"></div></td>',
                        // 保证金金额
                        '<td><div class="layui-input-inline"><input type="text" id="' + rowMR + 'bondMoney" name="applyDtls[' + rowMR + '].bondMoney" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + rowMR + 'bondMoney" value="' + item.bondMoney + '" data-callback="bt.setBailMoneyRate" data-row="' + rowMR + '"></div></td>',
                        // 占用授信金额
                        '<td><input type="text" data-name="applyDtls[' + rowMR + '].money" id="' + rowMR + 'money" value="' + item.money + '" data-biz-name="' + rowMR + 'money" class="layui-input bt-biz-moneyType" rowindex="' + rowMR + '" data-decimal="2" data-min="0" data-minus="false" lay-verify="required" readonly="readonly"></td>', //data-callback="bt.setMaxMoney"
                        // 担保方式
                        '<td><div class="layui-form-item"> <div class="layui-inline"><div class="selectDemo bt-biz-mulselectType" data-biz-name="' + rowMR + 'guaWay" data-url="' + basePath + '/cms/guarantee/guaContractInfo/json" data-method="get" data-valuefield="code" data-textfield="name" data-name="applyDtls[' + rowMR + '].guaWay" data-row="' + rowMR + '" data-required="required" data-defaultvalue="' + item.guaWay + '"></div></div></div></td>',//data-callback="setGuaWays"
                        // 开始日期
                        '<td><input type="text" value="' + item.beginDate + '" name="applyDtls[' + rowMR + '].beginDate" lay-verify="required" data-min="' + beginDate + '" data-initmax="' + endDate + '" id="' + rowMR + 'beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="' + rowMR + 'beginDate" rowindex="' + rowMR + '" data-endid="' + rowMR + 'endDate" data-callback="bt.setDays" lay-verify="required" ></td>',
                        // 预计结束日期
                        '<td><input type="text" value="' + item.endDate + '" name="applyDtls[' + rowMR + '].endDate" lay-verify="required" id="' + rowMR + 'endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="' + rowMR + 'endDate" rowindex="' + rowMR + '" data-callback="bt.setDays" lay-verify="required" ></td>',
                        '<td><input type="text" value="' + item.days + '" name="applyDtls[' + rowMR + '].days" id="' + rowMR + 'days" readonly="readonly" class="layui-input" lay-verify="required" ></td>',
                        '<td><input type="text" value="' + item.contactNo + '" name="applyDtls[' + rowMR + '].contactNo" lay-verify="required"  id="' + rowMR + 'contactNo" maxlength="50" class="layui-input"></td>',
                        '<td><input type="text" value="' + item.lcNo + '" name="applyDtls[' + rowMR + '].lcNo" id="' + rowMR + 'lcNo" maxlength="32" class="layui-input"></td>',
                        '<td><input type="text" value="' + item.billNo + '" name="applyDtls[' + rowMR + '].billNo" id="' + rowMR + 'billNo" maxlength="32" class="layui-input"></td>',
                        '<td><input type="text" value="' + item.remark + '" name="applyDtls[' + rowMR + '].remark" id="' + rowMR + 'remark" maxlength="100" class="layui-input remark"></td>',
                        '<td>&nbsp;&nbsp;&nbsp;&nbsp;\t'
                        + '<button  type="button" class="layui-btn layui-btn-mini remove_row">' + bt.lang("common.button.delete") + '</button>'
                        + '</td>'
                    ]
                )

                if (isOpenRat === "0") {//不启用授信敞口
                    datatable.setColumnShow(4, false);//隐藏授信敞口选择列
                }

                if (isGuarantee === "0") {//系统参数设置不挂接担保
                    datatable.setColumnShow(11, false);//隐藏担保方式列
                }

                var billNameSpan = '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="' + rowMR + 'billName" data-url="' + basePath + '/cms/rat/ratBusinessRace/getBillByCustomId?customId=' + item.itemCode + '"  data-valuefield="id" data-textfield="raceName" data-defaultvalue="' + item.billName + '" data-name="applyDtls[' + rowMR + '].billName" data-search="true"></div></div>'
                $("#billNameSpan" + rowMR).empty();
                $("#billNameSpan" + rowMR).html(billNameSpan);

                var html = '';
                var itemFlag = item.itemFlag;
                if ("0" === itemFlag) {
                    // 单品种
                    html = '<input type="hidden" name="applyDtls[' + rowMR + '].itemFlag" id="itemFlag' + rowMR + '" value="0">'
                        + '<input type="hidden" data-name="applyDtls[' + rowMR + '].applyDtls[' + item.itemOrder + '].maxMoney" data-biz-name="' + rowMR + 'maxMoney" data-id="' + rowMR + 'maxMoney' + item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false"/>'
                        + '<input type="hidden"  data-biz-name="' + rowMR + 'itemMaxMoney" data-id="' + rowMR + 'itemMaxMoney' + item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" value="' + item.money + '" data-minus="false"/>'
                        + '<input type="hidden" name="applyDtls[' + rowMR + '].ratCreditType"  value="' + item.creditType + '">';
                } else {
                    // 多品种
                    html += '<input type="hidden" name="applyDtls[' + rowMR + '].itemFlag"  id="itemFlag' + rowMR + '" itemOrder="' + item.itemOrder + '" value="1">'
                        + '<input type="hidden" name="applyDtls[' + rowMR + '].applyDtls[' + item.itemOrder + '].itemCode" rowIndex="' + rowMR + '" itemorder="' + item.itemOrder + '" id="' + rowMR + 'itemCode' + item.itemOrder + '" value="' + item.itemCode + '">'
                        + '<input type="hidden" data-name="applyDtls[' + rowMR + '].applyDtls[' + item.itemOrder + '].maxMoney" data-biz-name="' + rowMR + 'maxMoney" data-id="' + rowMR + 'maxMoney' + item.itemOrder + '" value="' + item.money + '" class="layui-input bt-biz-moneyType" data-decimal="2"  data-minus="false"/>'
                        + '<input type="hidden" data-biz-name="' + rowMR + 'itemMaxMoney" data-id="' + rowMR + 'itemMaxMoney' + item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" value="' + item.maxMoney + '" data-minus="false"/>'
                        + '<input type="hidden" name="applyDtls[' + rowMR + '].ratCreditType"  value="' + item.creditType + '">';
                }
                $("#sysItemCode" + rowMR).empty();
                $("#sysItemCode" + rowMR).html(html);

                form.render('select');

                initSelectCode(rowMR);

            }

            form.render("select");
        }
    });
}






