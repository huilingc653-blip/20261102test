var $, form, moneyType, dateType, select, dateFun, dicts, moneyFun, bttable, selectTable;
bt.use(['form', 'layedit', 'dicts', 'selectTree', 'rateType', 'selectType', 'moneyType', 'dateType', 'corp', 'btUpload', 'bttable', 'selectTableType', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    corp = bt.corp.init();//单位
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    /**
     * 生成列表
     */
    bttable = bt.bttable.init({
        elem: "#appTable",
        mulselect: false,
        checkbox: false,
        'url': basePath + "/cms/equity/info/detailDatatable?ids=" + $("#ids").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {
                data: "equityCode", className: "center", render: function (data, row) {
                    return data.equityCode;
                }
            },
            {
                data: "financingWay", className: "center", render: function (data, row) {
                    return bt.dict("cms.equity.financingWay", data.financingWay);
                }
            },
            {
                data: "investor", className: "center", render: function (data, row) {
                    return data.investor;
                }
            },
            {
                data: "invWay", className: "center", render: function (data, row) {
                    return bt.dict("cms.equity.invWay", data.invWay);
                }
            },
            {
                data: "invMoney", className: "textRight", render: function (data) {
                    return bt.money.format(data.invMoney);
                }
            },
            {
                data: "equityPro", className: "center", render: function (data, row) {
                    return data.equityPro * 100;
                }
            },
            {
                data: "assetName", className: "center", render: function (data, row) {
                    return data.assetName;
                }
            },
            {
                data: "rmk", className: "center", render: function (data, row) {
                    return data.rmk;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" equityCode="' + data.equityCode + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';

                    return html;
                }
            }
        ]
    });
    initBankAccTable();
    //监听保存
    form.on('submit', function (data) {
        bt.loading.show();

        var nm = $(this).attr("id");
        if (nm == "submit") {
            data.field.saveType = "1";
        } else {
            data.field.saveType = "0";
        }
        data.field.uploadId = up.getGroupId();
        var action = "/cms/equity/info/tempOrSubmit";
        var corpData = corp.get('corpId').data()[0];
        var curData = select.get("curId").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
        data.field.curName = curData.curname;
        data.field.corpName = corpData.corpName;
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    if (data.field.id == "") {
                        bt.navtab.refreshCurrent();
                    } else {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    }
                }
            } else {
                bt.loading.hide();
            }

        }, 'JSON');
        return false;
    });
});

/**
 * 导入功能
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "70000021",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/cms/equity/info/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var ids = $("#ids").val()
                        if (ids == "") {
                            ids = result.obj
                        } else {
                            ids = ids + "," + result.obj
                        }
                        bttable.reload({url: basePath + "/cms/equity/info/detailDatatable?ids=" + ids});
                        bt.jquery("#ids").val(ids);
                        console.log(ids)
                    }
                }
            });
        }
    });
};

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/equity/info/addDetail");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/info/addDetail",
        end: function () {
            location.reload();
        }
    });
}

function update(el) {
    var equity = $(el).attr("equityCode")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/equity/info/updateDetail/" + equity);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/info/updateDetail/" + equity,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function del(el) {
    var id = $(el).attr("id")
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        bt.loading.show();
        $.post(basePath + "/cms/equity/info/deleteDetail/" + id, function (result) {
            bt.loading.hide();
            var ids = $("#ids").val()
            var idArray = ids.split(',')
            for (var i = 0; i < idArray.length; i++) {
                if (idArray[i] == id) {
                    idArray.splice(i, 1)
                }
            }
            ids = idArray.toString()
            $("#ids").val(ids)
            bttable.reload();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function addTr() {
    var tab = document.getElementById("dataTable");
    trHtml(tab);
    var lastRow = $("#dataTable tbody").find("tr:last").data('row');
    rateType = bt.rateType.init();//利率
    moneyType = bt.moneyType.init();
}

trHtml = function (tab) {
    var rowsCount = tab.rows.length;
    var row = rowsCount - 1;
    var trHtml = '<tr data-row="' + row + '">';
    trHtml += '<td align="center"><div class="layui-inline"> <div class="bt-biz-selectType" data-biz-name="costTypeDtl[' + row + ']costCode" data-url="' + basePath + '/cms/dfCostType/getValidDfCostType"' +
        ' data-valuefield="id" data-textfield="costName" id="costCode' + row + '" data-name="costTypeDtl[' + row + '].costTypeId" data-search="true" id="costCode' + row + '" ' +
        ' data-link="true" data-callback="setItemCode"></div></div></td>';
    trHtml += '<td align="center"><input type="text" id="rate' + row + '" name="costTypeDtl[' + row + '].rate" data-name="costTypeDtl[' + row + '].rate" class="layui-input bt-biz-rateType" data-biz-name="costTypeDtl[' + row + ']rate" data-callback="setInterestFee1" ></td>';
    trHtml += '<td align="center"><input type="text" id="advanceFees' + row + '" data-name="costTypeDtl[' + row + '].advanceFees"  autocomplete="off" data-biz-name="costTypeDtl[' + row + ']advanceFees" data-callback="setInterestFee4" class="layui-input bt-biz-moneyType"></td>';

    trHtml += '<td align="center"><a href="javascript:void(0);" title="#springMessage("common.delTr")" onClick="remoTr(this.parentElement.parentElement.rowIndex)"><i class="layui-icon">&#xe640;</i></a></td>';

    trHtml += '</tr>';
    $("#dataTable tbody").append(trHtml);
    initSelectCode(row);
    bt.selectType.init();
}

var initSelectCode = function (row) {
    bt.rateType.init();
    bt.moneyType.init();
    //设置新增的费用类型行不可选已勾选的
    setNewItemDisable(row);
    bt.selectType.init();
}

function setInterestFee1(data, elem) {
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyType.get("money").val();//租赁金额
    var consultantFee = moneyFun.mul(plMoney, data);//预计费用
    moneyType.get("costTypeDtl[" + rowIndex + "]advanceFees").val(consultantFee);
    //summation();
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
    form.render("select");
}

function setInterestFee4(data, elem) {
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyType.get("money").val();//租赁金额
    var rate = data / plMoney;//顾问费
    rateTypes.get("costTypeDtl[" + rowIndex + "]rate").val(rate);
}

/**选择帐号的下拉列表初始化*/
function initBankAccTable() {
    selectTable = bt.selectTableType.init({
        elem: ".account",
        type: 'get',
        paging: true,
        url: basePath + "/cms/equity/info/queryPaymentAccListByCoprId?corpId=" + $("#corpId1").val(),
        columns: [
            {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
            {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")},
            {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
            {"data": "curName", title: bt.lang("international.payment.curName")},
        ],
        clickCallback: function (severData, data) {
            $("#account").val(severData.bankAcc);
            $("#accName").val(severData.accName);
            $("#bankName").val(severData.bankName);
            select.get("curId").val(severData.curId);
        }
    });


}

function BankAccTable() {
    selectTable.get("account").setUrl(basePath + "/cms/equity/info/queryPaymentAccListByCoprId?corpId=" + bt.corp.init().get("corpId").val())
}

//新增费用类型行设置已勾选的不可选择
function setNewItemDisable(rowIndex) {
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
        $("#costCode" + rowIndex).attr("data-itemdisabled", disItem);
        form.render("select");
    }
}

function remoTr(rowIndex) {
    var tab = document.getElementById("dataTable");
    tab.deleteRow(rowIndex);
    console.log(rowIndex);
    //循环修改元素的名字
    for (var i = rowIndex; i < (tab.rows.length - 1); i++) {
        var tempTr = tab.rows.item(i);
        for (var x = 0; x < tempTr.cells.length; x++) {
            var tempTd = tempTr.cells.item(x);
            var strHTML = tempTd.innerHTML;
            console.log(strHTML);
            while (strHTML.indexOf("[" + (i - 1) + "]") > 0) {
                strHTML = strHTML.replace("[" + (i - 1) + "]", "["
                    + (i - 2) + "]");
            }
            while (strHTML.indexOf("[" + (i) + "]") > 0) {
                strHTML = strHTML.replace("[" + (i) + "]", "[" + (i - 1)
                    + "]");
            }
            tempTd.innerHTML = strHTML;
        }
    }
    return false;
}
