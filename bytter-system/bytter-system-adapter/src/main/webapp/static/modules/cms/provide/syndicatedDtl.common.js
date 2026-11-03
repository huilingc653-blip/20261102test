//本期还款金额
var currentRepayMoney = 0
//本期还款利息
var repaymentIst = 0
var moneyTypes, moneyFun, rateType;
bt.use(['form', 'bttable', 'moneyType', 'datatable', 'dicts', 'bankInfo', 'rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();//字典
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();
    rateType = bt.rateType.init();

    bt.bankCodeCallback = function (data, dom) {
        var tr = dom.parents('tr');
        var rowIndex = tr.attr("row");

        $("#bankName" + rowIndex).val(data.bankName);
        $("#bankCode" + rowIndex).val(data.bankCode);
    }

    bt.bankInfo.init();//联行号

    var BUDGET_PROCESS_SPLIT = "!-!";
    var rowTr = 0;
    addSyndicatedDtl = function (rowMR, syndicateds) {
        var html = '<tr row=' + rowMR + '>';
        html += '<td>';
        html += '<div class="layui-inline">';

        if (syndicateds) {
            html += '<div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="leadingBank' + rowMR + '" data-biz-name="leadingBank' + rowMR + '" data-defaultvalue="' + syndicateds[0] + '"></div>';
            html += '</div>';

            html += '<td align="center"><div class="layui-input-inline"><input type="text" id="bankCode' + rowMR + '" name="syndicatedDtlList[' + rowMR + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input" value="' + syndicateds[2] + '"/> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
            html += '<td align="center"><div class="layui-inline"><input type="text" name="syndicatedDtlList[' + rowMR + '].bankName" id="bankName' + rowMR + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + syndicateds[1] + '"/></div></td>';
            //待还款金额
            html += '<td align="center"><input disabled class="layui-input bt-input-inline bt-biz-moneyType" divName="surplusMoney" type="text"  value="' + syndicateds[3] + '" data-decimal="2" data-biz-name="surplusMoney_' + rowMR + '" id="surplusMoney_' + rowMR + '" data-min="0" lay-verify="required" data-name="surplusMoney" /></td>';
            //本次还款金额
            html += '<td align="center"><input class="layui-input bt-input-inline bt-biz-moneyType" divName="repaymentMoney" data-callback="callSumMoney" type="text" value="' + syndicateds[4] + '" data-decimal="2" data-biz-name="repaymentMoney_' + rowMR + '" id="repaymentMoney_' + rowMR + '" data-min="0" lay-verify="required" data-name="repaymentMoney" /></td>';
            //还款本金占比%
            html += '<td align="center"><input disabled class="layui-input bt-input-inline bt-biz-rateType" divName="repaymentMoneyShare" data-biz-name="repaymentMoneyShare_' + rowMR + '" type="text" value="' + syndicateds[5] + '" data-decimal="2" data-ratetype="percent" id="repaymentMoneyShare_' + rowMR + '" data-min="0" data-max="100" data-name="repaymentMoneyShare"/></td>';
            //还款利息
            html += '<td align="center"><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" data-callback="callSumMoney" type="text" value="' + syndicateds[6] + '" data-decimal="2" data-biz-name="repaymentIst_' + rowMR + '" id="repaymentIst_' + rowMR + '" data-min="0" lay-verify="required" data-name="commitmentMoney"/></td>';
        } else {
            html += '<div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="leadingBank' + rowMR + '" data-biz-name="leadingBank' + rowMR + '" data-defaultvalue="0"></div>';
            html += '</div>';

            html += '<td align="center"><div class="layui-input-inline"><input type="text" id="bankCode' + rowMR + '" name="syndicatedDtlList[' + rowMR + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"/> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
            html += '<td align="center"><div class="layui-inline"><input type="text" name="syndicatedDtlList[' + rowMR + '].bankName" id="bankName' + rowMR + '" lay-verify="required" autocomplete="off" readonly class="layui-input"/></div></td>';
            //待还款金额
            html += '<td align="center"><input disabled class="layui-input bt-input-inline bt-biz-moneyType" divName="surplusMoney" type="text" data-decimal="2" data-biz-name="surplusMoney_' + rowMR + '" id="surplusMoney_' + rowMR + '" data-min="0" lay-verify="required" data-name="surplusMoney" /></td>';
            //本次还款金额
            html += '<td align="center"><input class="layui-input bt-input-inline bt-biz-moneyType" divName="repaymentMoney" data-callback="callSumMoney" type="text" value="0" data-decimal="2" data-biz-name="repaymentMoney_' + rowMR + '" id="repaymentMoney_' + rowMR + '" data-min="0" lay-verify="required" data-name="repaymentMoney" /></td>';
            //还款本金占比%
            html += '<td align="center"><input disabled class="layui-input bt-input-inline bt-biz-rateType" divName="repaymentMoneyShare" data-biz-name="repaymentMoneyShare_' + rowMR + '" type="text" data-decimal="2" data-ratetype="percent" id="repaymentMoneyShare_' + rowMR + '" data-min="0" data-max="100" data-name="repaymentMoneyShare"/></td>';
            //还款利息
            html += '<td align="center"><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" data-callback="callSumMoney" type="text" value="0" data-decimal="2" data-biz-name="repaymentIst_' + rowMR + '" id="repaymentIst_' + rowMR + '" data-min="0" lay-verify="required" data-name="commitmentMoney"/></td>';
        }
        html += '<td><a href="javascript:void(0)" onclick="del(this)">' + bt.lang("common.button.delete") + '</a></td>';

        html += '</tr>';
        if ($("#myDetailControl").val() && $("#myDtlColSel").val()) {
            html += '<tr id="projectDetailTr' + rowMR + '" style="display: none"><td colspan="8" id="projectDetail' + rowMR + '"></td></tr>';
        }
        $("#syndicatedDtlDataTable >tbody").append(html);
        rowTr++;
        // 重新初始化金额及科目
        moneyTypes.render();
        bt.dicts.render();
        bt.rateType.init();
    }

    //生成列表
    bt.bttable.init({
        elem: "#syndicatedDtlDataTable",
        scrollY: "500px",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($trs) {
                addSyndicatedDtl(rowTr);
            }
        }],
        columns: []
    });

    callSumMoney = function (value, data) {
        if (typeof value != "undefined") {
            //遍历表格，重新计算剩余本金
            var trs = $("#syndicatedDtlDataTable").children("tbody").children("tr");
            // 遍历行数据
            var initpayMoney = currentRepayMoney;
            trs.each(function (index, tr) {
                var rowIndex = $(this).find("input[divName^='repaymentMoney']").data("id").split("_")[1];
                var repaymentMoney = $(this).find("input[divName^='repaymentMoney']").val();
                var surplusMoney_ = moneyFun.sub(initpayMoney, repaymentMoney);
                initpayMoney = surplusMoney_;
                moneyTypes.get("surplusMoney_" + rowIndex).val(surplusMoney_);
                // 还款本金占比%
                var repaymentMoneyShare = moneyFun.div(repaymentMoney, currentRepayMoney);
                rateType.get("repaymentMoneyShare_" + rowIndex).val(repaymentMoneyShare);
            });
            sumMoney();
        }
    };

    /**
     * 将数据回写给前一个页面
     */
    $("#saveSyndicatedDtl").on("click", function () {
        var rowNum = $("#rowNum").val();
        var datas = [];
        // 校验数据牵头行只有一个为是
        var checkLeadingBank = 0;
        var checkSave = false;

        $('form').find('input[name="repaymentMoney"]').each(function (index) {
            if ($(this).val()) {
                var leadingBank = $("select[name='leadingBank" + index + "']").val();
                if (leadingBank == 1) {
                    checkLeadingBank = checkLeadingBank + 1
                }
                datas.push(leadingBank + BUDGET_PROCESS_SPLIT + $("#bankName" + index).val() + BUDGET_PROCESS_SPLIT + $("#bankCode" + index).val() + BUDGET_PROCESS_SPLIT + bt.moneyType.get("surplusMoney_" + index).val() + BUDGET_PROCESS_SPLIT + $(this).val() + BUDGET_PROCESS_SPLIT + bt.rateType.get("repaymentMoneyShare_" + index).val() + BUDGET_PROCESS_SPLIT + bt.moneyType.get("repaymentIst_" + index).val());
            }
        });

        if (checkLeadingBank <= 1) {
            var parentWindow = bt.navtab.getParentDom();
            if (parentWindow) {
                parentWindow.syndicatedCallback(datas, rowNum)
            }
            if (bt.navtab) {
                bt.navtab.closeCurrent();
            }
        } else {
            // 牵头行只能有一个，不能有多个！
            parent.layer.msg(bt.lang("cms.cmsloaninfo.leadingbank.unique"), {icon: 2});
        }
    });

    /**
     * 删除
     * @param el
     */
    del = function (el) {
        var tr = $(el).parent("td").parent("tr");
        tr.remove();
        callSumProcessMoney();
        var row = $(tr).attr("row");
        if ($('#projectDetailTable' + row)) {
            $('#projectDetailTable' + row).DataTable().destroy();
        }
        $('#projectDetail' + row).parents("tr").remove();
        $("#projectDetailSum" + row).remove();
    }

    initSyndicated();

    /**
     * 初始化之前的数据
     */
    function initSyndicated() {
        var rowNum = $("#rowNum").val();
        // 获取上一个页面的保存值和数据
        var webObj = bt.navtab.getParentDom();
        if (webObj) {
            var tr = $("#syndicatedDtlDataTable").find('tr[row="0"]');
            tr.remove();
            currentRepayMoney = webObj.document.getElementById("currentRepayMoney_" + rowNum).value;
            repaymentIst = webObj.document.getElementById("repaymentIst_" + rowNum).value;
            var syndicated = webObj.document.getElementById("syndicated" + rowNum).value;
            if (syndicated.length > 1) {
                var syndicateds = syndicated.split(",");
                if (syndicateds.length == 1) {
                    var obj = syndicateds[0].split(BUDGET_PROCESS_SPLIT);
                    addSyndicatedDtl(0, obj);
                } else {
                    for (k in syndicateds) {
                        var obj = syndicateds[k].split(BUDGET_PROCESS_SPLIT);
                        addSyndicatedDtl(k, obj);
                    }
                }
            } else {
                // 初始化第一行
                addSyndicatedDtl(0);
            }
        } else {
            // 初始化第一行
            addSyndicatedDtl(0);
        }
    }

    function sumMoney() {
        var trs = $("#syndicatedDtlDataTable").children("tbody").children("tr[row]");
        // 遍历行数据
        var sumMoney = 0;
        trs.each(function (index, tr) {
            var rowIndex = $(tr).attr("row");
            var repaymentMoney = bt.moneyType.get("repaymentMoney_" + rowIndex).val();
            var itemMoney = bt.money.toNumber(repaymentMoney);
            sumMoney = bt.money.add(sumMoney, itemMoney);
        });
        bt.moneyType.get("sumMoney").val(sumMoney);
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //获取数据行信息
        var trs = $('#syndicatedDtlDataTable').find('tbody tr');
        if (trs[0]) {
            bt.each(trs, function () {
                if ($(this).attr('row') !== undefined) {
                    resetItemAndProcessMoney($(this).attr('row'));
                }
            })
        }
    }

    /**
     * 清空银行信息 和 本次缓存金额 栏数据
     * */
    function resetItemAndProcessMoney(row) {
        bt.moneyType.init().get("surplusMoney" + row).val('');
    }


});
