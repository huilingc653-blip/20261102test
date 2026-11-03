var rowMR = 0;
var comprowMR = 0;
var singrowMR = 0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var rateTypes;
var disItem;
bt.use(['form', 'layedit', 'bttable', 'moneynum', 'dicts', 'select', 'moneyType', 'mulselect', 'corp', 'selectType', 'mulselect', 'money', 'rateType','btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    $('.bt-biz-selectType select').addClass('initSelect');
    fun = moneyFun = bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率

    disItem = '';
    refreshrDisItem();

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //
    $("body").on('click', '.remove_row', function (data) {
        var bizName = $(this).parents('tr').find('.bt-biz-moneyType').data('biz-name');
        var codeBizName = $(this).parents('tr').find('.bt-biz-selectType').data('biz-name');
        var index = bizName.substr(0,1);
        var flag = $(this).parents('tr').find('#itemFlag'+index).attr('value');
        var money = moneyType.get(bizName).val();
        var sMoney = moneyType.get("moneysum").val();
        if(flag != "1") {
            var sumMoney = sMoney - money;
            moneyType.get("moneysum").val(bt.money.format(sumMoney));
        }
        $(this).parents('tr').remove()
        disItem ='';
        setNewItemDisable(codeBizName);
    })

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //设置分配金额和剩余额度
    bt.calMoney = function (data, el) {
        var dataId = $(el).attr("data-id");
        var moneyIndex = dataId.indexOf("money");
        var rowIndex = dataId.substr(0, moneyIndex);
        var compItemSize = $("#compItemSize").val();
        var compRowIndex = $("#compItemIndex").val();
        if(compItemSize==0){
            compItemSize = comprowMR;
        }
        if(compRowIndex==0){
            compRowIndex = singrowMR;
        }
        var itemSize = compRowIndex - (0 - compItemSize);
        var singleSign = $("."+rowIndex+"singleSign").val();
        var isMinus = data.toString().substr(0,1);
        if(singleSign == 0){
            if("-" == isMinus){
                var remainMoney = $("."+rowIndex+"singLeftMoney").val();
                var total = (data-0) +(remainMoney - 0);
                if(total <= 0){
                    layer.alert(bt.lang("table.ratbkrationAdd.moneyError"));
                    moneyType.get(rowIndex + "money").val("0");
                }
            }
            bt.calSumMoney();
            return;
        }

        for (var i = compRowIndex; i < itemSize; i++) {
            if(bt.checkMaxMoney(i)==0){
                data = 0;
            }
            bt.calSumMoney();
            if (rowIndex == i) {
                bt.setMaxReadOnly(data);
            }
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

    bt.calMaxMoney = function (data, el) {
        var dataId = $(el).attr("data-id");
        var moneyIndex = dataId.indexOf("maxMoney");
        var rowIndex = dataId.substr(0, moneyIndex);
        var singItemSize = $("#singItemSize").val();
        var compRowIndex = $("#compItemIndex").val();
        if(compRowIndex == 0){
            compRowIndex = singItemSize-0+1;
        }
        var moneyIndexVal = moneyType.get(compRowIndex + "money").val();//授信新追加额度
        var maxMoneyIndexVal = moneyType.get(rowIndex + "maxMoney").val();
        var lMoney = $("."+rowIndex+"leftMoney").val();//添加按钮增加的行合同剩余额度
        var leftMoney = $("#leftMoney").val();//合同剩余额度

        if (lMoney != undefined && lMoney == 0) {//新增的数据
            if(!bt.empty(moneyIndexVal)) {
                var total = moneyIndexVal - 0 + (leftMoney - 0);
                if(bt.money.sub(maxMoneyIndexVal,total) > 0){
                    layer.alert(bt.lang("table.ratbkrationAdd.total.judge"));
                }else{
                    return;
                }
                moneyType.get(rowIndex + "maxMoney").val(moneyIndexVal);
                return;
            }
        }

        var isMinus = moneyIndexVal.substr(0,1);//是否追减
        if(isMinus == "-"){
            if((maxMoneyIndexVal-0) < (moneyIndexVal-0)){
                layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
                moneyType.get(rowIndex + "maxMoney").val(moneyIndexVal);
                return;
            }
        }else{
            if(maxMoneyIndexVal < 0){
                moneyType.get(rowIndex + "maxMoney").val(moneyIndexVal);
            }
        }

        if (bt.empty(moneyIndexVal)){
            layer.alert(bt.lang("table.ratbkrationAdd.moneynull"));
            moneyType.get(rowIndex + "maxMoney").val("0");
        }

        if (bt.money.sub(maxMoneyIndexVal, moneyIndexVal) > 0 && maxMoneyIndexVal > 0) {
            layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
            moneyType.get(rowIndex + "maxMoney").val(moneyIndexVal);
        }
    }

    bt.checkMaxMoney = function (rowIndex) {
        var singItemSize = $("#singItemSize").val();
        var compRowIndex = $("#compItemIndex").val();
        if(compRowIndex == 0){
            compRowIndex = singItemSize-0+1;
        }
        var moneyIndexVal;
        var moneyIndexObj = $("#" + compRowIndex + "money");
        if (moneyIndexObj && moneyIndexObj.val()) {
            moneyIndexVal = moneyType.get(compRowIndex + "money").val();
        }
        var maxMoneyIndexVal;
        var maxMoneyIndexObj = $("#" + rowIndex + "maxMoney");
        if (maxMoneyIndexObj && maxMoneyIndexObj.val()) {
            maxMoneyIndexVal = moneyType.get(rowIndex + "maxMoney").val();
        }

        var isMinus = moneyIndexVal.substr(0,1);//是否追减
        if(isMinus == "-"){
            var remainMoney = $("."+rowIndex+"remainMoney").val();
            var total = (moneyIndexVal-0) +(remainMoney - 0);
            if(total <= 0){
                layer.alert(bt.lang("table.ratbkrationAdd.moneyError"));
                //moneyType.get(compRowIndex + "money").val("0");
                return 0;
            }
        }

        if (bt.empty(moneyIndexVal)) {
            layer.alert(bt.lang("table.ratbkrationAdd.moneynull"));
            moneyType.get(rowIndex + "maxMoney").val("0");
        }
        if (bt.money.sub(maxMoneyIndexVal, moneyIndexVal) > 0 && maxMoneyIndexVal > 0) {
            layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
            moneyType.get(rowIndex + "maxMoney").val(moneyIndexVal);
        }
    }

    //设置敞口最高额度不可编辑
    bt.setMaxReadOnly = function (compMoney) {
        var addFlag = true;
        if (compMoney - 0 < 0) {//追减
            addFlag = false;
        }

        $.each($("input[data-id$='maxMoney']"), function (i, item) {
            //if (addFlag) {//追加
                $(item).removeAttr("readonly");
          /*  } else {
                $(item).attr("readonly", "readonly");
            }*/
            var rowindex = $(item).attr("rowindex");
            moneyType.get(rowindex + "maxMoney").val(compMoney);
        });
    }

    bt.dicts.init();
    moneyType = bt.moneyType.init();

    function checkBefore() {


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
        var action = "/cms/rat/ratbkrationadd/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide(); //关闭加载中
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    var webObj = bt.navtab.getParentDom();
                    setTimeout(function () {
                        webObj.search();
                        bt.navtab.closeCurrent();
                    },200)
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
        var action = "/cms/rat/ratbkrationadd/submit";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide(); //关闭加载中
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    var webObj = bt.navtab.getParentDom();
                    setTimeout(function () {
                        webObj.search();
                        bt.navtab.closeCurrent();
                    },200)
                }
            }
        }, 'JSON');
        return false;
    });

    //监听返回
    form.on('submit(back)', function (data) {
        parent.layer ? parent.layer.closeAll() : layer.closeAll();
        return false;
    });

    bt.close = function () {
        if (!bt.navtab) {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
        } else {
            bt.navtab.closeCurrent();
        }
    }

    rowMR = $("#itemSize").val();
    singrowMR = $("#singItemSize").val();
    comprowMR = $("#compItemSize").val();
    compRowIndex = $("#compRowIndex").val();

    //新增单品种事件
    $("#addSingleTr").click(function () {
        rowMR++;
        singrowMR++;
        // if(singrowMR - copmRatSize > 0){
        //     return false;
        // }
        var selets = $('.itemCode');
        bt.each(selets, function (i, item) {
            var bizName = $(this).data("biz-name");
            if (bizName) {
                var v = selectTypes.get(bizName).val();
                if (!bt.empty(v)) {
                    disItem += v+',';
                }
            }
        })

        var html = '<tr>';
        html += '<td align="center">' + bt.dict("RatModel.ItemFlag", '0') + '</td>';
        html += '<td align="center"><input id="itemFlag' + rowMR + '" name="rationDtls[' + rowMR + '].itemFlag" type="hidden" value="0">'
            + '<input name="rationDtls[' + rowMR + '].itemOrder" type="hidden" value="' + rowMR + '">'
            + '<div class="layui-inline"><div class="bt-biz-selectType itemCode" data-biz-name="rationDtls[' + rowMR + '].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
            + 'data-valuefield="id" data-textfield="name" id="' + rowMR + 'itemCode" data-name="rationDtls[' + rowMR + '].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';
        html += '<td>'//额度类型
            + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-group="rat.creditType" data-defaultvalue="1" data-selecttext="false" data-biz-name="rationDtls[' + rowMR + '].creditType"  '
            + 'data-name="rationDtls[' + rowMR + '].creditType"></div></div></td>';
        html += '<td align="right">0.00</td><td align="right">0.00</td>';

        html += '<td  align="right"><input type="text" data-name="rationDtls[' + rowMR + '].money" id="' + rowMR + 'money" data-biz-name="' + rowMR + 'money"' +
            'class="layui-input bt-biz-moneyType" data-min="0" value="0" data-decimal="2" data-minus="true" data-callback="bt.calMoney"></td>'
        html += '<td></td><td></td>'
        html += '<td><button  type="button" class="layui-btn layui-btn-mini remove_row">' + bt.lang("common.button.delete") + '</button>'
            + '</td>';
        html += '</tr>';
        if (rowMR - 1 > 0 && $("#ratTable tbody tr.single").length > 0) {
            $("#ratTable tbody tr:last").after(html);
        } else {
            $("#ratTable tbody").append(html);
        }

        initSelectCode(rowMR, 'rationDtls[' + rowMR + '].itemCode');
        bt.dicts.init();
        return false;
    });
    //新增综合品种事件
    $("#addCompTr").click(function () {
        // if(comprowMR - copmRatSize >= 0){
        //     return false;
        // }
        rowMR++;
        comprowMR++;
        var html = '<tr class="compRat">';
        var cols = "";
        var buttonId = "";
        var colFlag = true;
        var compMoney = "";

        setRowSpan();

        var selets = $('.itemCode');
        bt.each(selets, function (i, item) {
            var bizName = $(this).data("biz-name");
            if (bizName) {
                var v = selectTypes.get(bizName).val();
                if (!bt.empty(v)) {
                    disItem += v+',';
                }
            }
        })

        if (comprowMR - 1 > 0 && $("#aTable tbody tr").length > 0) {
            colFlag = false;
            //compMoney = moneyType.get(compRowIndex + "money").val();
        } else {
            cols = "rowspan='" + comprowMR + "'";
            buttonId = " id='compButton' ";
            compRowIndex = rowMR;//记录综合品种列Index
        }
        if (colFlag) {
            html += '<td ' + cols + ' class="change_rowspan">' + bt.dict("RatModel.ItemFlag", '1') + '</td>';
        }

        //第二行
        html += '<td align="center"><input id="itemFlag' + rowMR + '" name="rationDtls[' + rowMR + '].itemFlag" type="hidden" value="1">'//授信类型
            + '<input name="rationDtls[' + rowMR + '].itemOrder" type="hidden" value="' + rowMR + '">'
            + '<div class="layui-inline"><div class="bt-biz-selectType itemCode" data-biz-name="rationDtls[' + rowMR + '].itemCode" data-url="' + basePath + '/cms/rat/ratitemcustom/getAllRatItem?type=0" '
            + 'data-valuefield="id" data-textfield="name" id="' + rowMR + 'itemCode" data-name="rationDtls[' + rowMR + '].itemCode" data-search="true" data-link="true" data-callback="setItemCode"></div></div></td>';//授信品种
        html += '<td>'//额度类型
            + '<div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-group="rat.creditType" data-defaultvalue="1" data-selecttext="false" data-biz-name="rationDtls[' + rowMR + '].creditType"  '
            + 'data-name="rationDtls[' + rowMR + '].creditType"></div></div></td>';
        if(comprowMR >1){
            html += '<td align="right">0.00 <input class="' + rowMR + 'leftMoney" type="hidden" value="0.00"></td>'
        }
        //录入时没有添加综合
        if(comprowMR==1){
            var leftMoney = $("#leftMoney").val();
            html += '<td align="right" class="change_rowspan" ' + cols + '>0.00<input id="leftMoney" type="hidden" value="0.00" class="' + rowMR + 'leftMoney"></td>'
            html += '<td align="right" class="change_rowspan" ' + cols + '>0.00</td>'
            html += '<td align="right" class="change_rowspan" ' + cols + '>' +
               '<input type="text" data-name="rationDtls[' + rowMR + '].money" id="' + rowMR + 'money" data-biz-name="' + rowMR + 'money"' +
                'class="layui-input bt-biz-moneyType" data-min="0" value="0" data-decimal="2" data-minus="true" data-callback="bt.calMoney">'+
                '</td>'
            html += '<td align="right">0.00</td>'
        }
        html += '<td align="right"><input type="text" data-name="rationDtls[' + rowMR + '].maxMoney" data-id="' + rowMR + 'maxMoney" rowindex="' + rowMR + '" data-biz-name="' + rowMR + 'maxMoney"' +
            'class="layui-input bt-biz-moneyType" data-min="0" value="0" data-decimal="2" data-minus="true" data-callback="bt.calMaxMoney"></td>'

        html += '<td><button type="button" class="layui-btn layui-btn-mini remove_row">' + bt.lang("common.button.delete") + '</button>'
            + '</td>';
        html += '</tr>';

        if (rowMR - 1 > 0 && $("#aTable tbody tr.compRat").length > 0) {
            $("#aTable tbody tr:last").after(html);
        } else {
            $("#aTable tbody").append(html);
        }
        if (!colFlag) {
            $("#compButton").hide();
        }

        initSelectCode(rowMR, 'rationDtls[' + rowMR + '].itemCode');
        bt.dicts.init();
        return false;
    });

    var initSelectCode = function (row, bizName) {
        bt.dicts.init();
        bt.moneyType.init();
        //设置新增的品种不可选已勾选的品种
        addNewItemDisable(bizName);

    }

    var setRowSpan = function () {
        var tds = $('.compRat').find('td');
        bt.each(tds, function () {
            if ($(this).hasClass('change_rowspan')) {
                var rowspan = $(this).attr('rowspan');
                rowspan = rowspan ? rowspan - 0 + 1 : 1;
                $(this).attr('rowspan', rowspan)
            }
        })
    }

});

closeTab = function () {
    bt.close();
}

//判断追加的金额是否大于当前保证金账户的可用余额
bt.checkBondMoney = function () {
    // 判断保证金的金额是否大于当前保证金账户的可用余额
    var addBondMoney = moneyFun.toNumber($("#bondAddMoney").val());//保证金金额(就是项目的占用金额)
    var accAvailMoney = moneyFun.toNumber($("#accAvailMoney").val());//当前保证金账户的可用余额
    if (addBondMoney > accAvailMoney) {//如果保证金金额大于当前保证金账户的可用余额,那么应该提示用户
        //bt.loading.hide(); //关闭加载中
        layer.alert(bt.lang("fcBond.bondAdd.addErro"), {icon: 2});
    }
}

function setItemCode(data, elem, bv) {
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
    bt.form().render("select");

}

function setNewItemDisable(name) {
   refreshrDisItem();
    var allSelects = $(".itemCode");
    bt.each( allSelects,function () {
         var bizName = $(this).data('biz-name');//default_itemCode
         if( !name && bizName !== name){
            selectTypes.get(bizName).attr("data-itemdisabled", disItem)
        }
    })
    bt.selectType.init();
}

function refreshrDisItem() {
    var itemCodeInput = $('.default_itemCode');
    bt.each(itemCodeInput, function () {
        var v = $.trim($(this).val());
        if (v !== '') {
            disItem += v+',';
        }
    })
}

function addNewItemDisable(name,sign) {
    if (disItem.length > 1) {
        if(sign == 1){
            form.render("select");
        }else{
            $('[data-biz-name="' + name + '"').attr("data-itemdisabled", disItem);
        }
    }
    bt.selectType.init();
}
