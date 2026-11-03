var moneyFun;
var rateType;
bt.use(['form', 'corp', 'element', 'btdate', 'select', 'dicts', 'dateType', 'moneyType', 'printType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    var element = layui.element();
    moneyFun = bt.moneyType.getFun();
    rateType = bt.rateType.getFun();
    corpCall = function (ele) {//网点回调
        $("#netId").val(ele.value);
        location.href = basePath + "/finacial/interest/fcinterest/listBatchInterest?netId=" + ele.value + "&opType=" + $("#opType").val() + "&accType=" + dicts.get('accType').val();
    }
    accTypeCall = function (ele) {//开户种类回调
        location.href = basePath + "/finacial/interest/fcinterest/listBatchInterest?netId=" + $("#netId").val() + "&opType=" + $("#opType").val() + "&accType=" + ele.value;
    }
    bt.corp.init();
    dateType = bt.dateType.init();
    var dicts = bt.dicts.init();
    var params = "";

    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            placeholder: {value: "", text: ""},
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")

        });
    });

    // 初始化
    init();

    // 监听控制content高度
    function init() {
        // 起始判断收缩还是展开
        initParam();
        initTabJX();
        // 选项卡高度
        var cardTitleHeight = $(document).find(".layui-tab[lay-filter='card2'] ul.layui-tab-title").height();
        // 需要减去的高度
        var height = $(window).height() - $('.layui-header').height() - cardTitleHeight;
        // 设置高度
        $(document).find(".layui-tab[lay-filter='card2'] div.layui-tab-content").height(height - 200);
    }

    //初始化参数
    function initParam() {
        var accType = "";
        if (dicts.get('accType').val()) {
            accType = dicts.get('accType').val().trim();
        }
        params = "netId=" + $("#netId").val() + "&accType=" + accType + "&opType=" + $("#opType").val() + "&curr=1"
    }
    var timer = null;
    element.on('tab(card2)', function (arg1) {
        var layId = $(arg1.elem.context).attr('lay-id');
        var iframe = $('#' + layId).find('iframe')[0];
        if (!iframe) {
            var src = '';
            if (layId === 'lxqd') {
                src = basePath + "/finacial/interest/fcinterest/interestBill?" + params
            } else if (layId === 'jxpz') {
                $("#loading").removeClass("layui-hide");
                src = basePath + "/finacial/interest/fcinterest/toLoadInterestVoucher?" + params
            } else if (layId === 'jsqd') {
                src = basePath + "/finacial/interest/fcinterest/accuBill?" + params
            } else if (layId === 'bzkqd') {
                if ("initList" === $("#opType").val()) {
                    src = basePath + "/finacial/interest/fcinterest/lessPayBill?" + params
                }
            }
            var html = '<iframe src="' + src + '" frameborder="0" style="width: 100%; height: 100%"></iframe>'
            $('#' + layId).append(html);
        }
        // 解决子页面报错时，未能关闭加载动画
        if(timer){
            clearTimeout(timer);
            timer = null
        }
        if($("#tabContent").find(".layui-show .layui-layer-loading")[0]){
            timer = setTimeout(function () {
                $("#tabContent").find(".layui-show .layui-layer-loading").remove()
            },1000)
        }

    });

    //利息清单
    function initTabJX() {
        bt.jquery("#lxqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/interestBill?" + params);
        bt.jquery("#lxqdiframe").show(1000);
    }

    //计息凭证
    function initTabPZ() {
        bt.jquery("#jxpziframe").attr("src", basePath + "/finacial/interest/fcinterest/toLoadInterestVoucher?" + params);
        bt.jquery("#jxpziframe").show(1000);
    }

    //积数清单
    function initTabJS() {
        bt.jquery("#jsqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/accuBill?" + params);
        bt.jquery("#jsqdiframe").show(1000);
    }

    //不足款清单
    function initTabBUK() {
        bt.jquery("#bzkqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/lessPayBill?" + params);
        bt.jquery("#bzkqdiframe").show(1000);
    }

    form.on('submit(calcInterest)', function (data) {
        $calcInterest("interest", bt.lang("table.fcAcc.interestSignOk"));//计息
        return false;
    });

    // 提交 应该删除内容
    form.on('submit(calcSubmit)', function (data) {
        $calcInterest("submit", bt.lang("common.button.submit"));//提交
        return false;
    });

    form.on('submit(doCheck)', function (data) {
        $checkInterest("check", bt.lang("common.button.check"));//复核
        return false;
    });
    form.on('submit(doBack)', function (data) {
        $checkInterest("back", bt.lang("common.button.back"));//打回
        return false;
    });

    // 应该删除内容
    form.on('submit(doDeleteByBatch)', function (data) {
        $doDeleteByBatch();//删除
        return false;
    });

    form.on('submit(doExportInterestBill)', function (data) {
        $doExportInterestBill();//导出利息清单
        return false;
    });

    form.on('submit(doBatchPrint)', function (data) {
        $doBatchPrint();//打印记账凭证
        return false;
    });

    form.on('submit(doPrintRateNotice)', function (data) {
        $doPrintRateNotice();//打印利息通知单
        return false;
    });

    // 重置 iframe
    function resetFrame(){
        initParam()
        // 删除iframe标签，点击页签时重新加载
        $("#tabContent").find(".layui-tab-item iframe").remove();
        var currentTabId = $("#tabContent").find(".layui-show").attr("id");
        var src = '';
        if (currentTabId === 'lxqd') {
            src = basePath + "/finacial/interest/fcinterest/interestBill?" + params
        } else if (currentTabId === 'jxpz') {
            $("#loading").removeClass("layui-hide");
            src = basePath + "/finacial/interest/fcinterest/toLoadInterestVoucher?" + params
        } else if (currentTabId === 'jsqd') {
            src = basePath + "/finacial/interest/fcinterest/accuBill?" + params
        } else if (currentTabId === 'bzkqd') {
            if ("initList" === $("#opType").val()) {
                src = basePath + "/finacial/interest/fcinterest/lessPayBill?" + params
            }
        }
        var html = '<iframe src="' + src + '" frameborder="0" style="width: 100%; height: 100%"></iframe>'
        $('#' + currentTabId).append(html);

    }

    $doBatchPrint = function () {
        var url = basePath + "/finacial/interest/fcinterest/doBatchPrint?opType=" + $("#opType").val() + "&netCode=" + $("#netId").val() + "&accTypeCode=" + dicts.get("accType").val().trim();
        parent.layer.open({
            title: bt.lang("common.button.preview"),
            type: 2,
            area: ['75%', '50%'],
            maxmin: true,
            content: url,
            end: function () {
            }
        });
    }

    $doPrintRateNotice = function () {
        if (!dateType.get("blnInstDate").val() || dateType.get("blnInstDate").val().trim() == "") {
            layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon: 5});//请输入止息日期！
            return;
        }
        var url = basePath + "/finacial/interest/fcinterest/doPrintRateNotice?opType=" + $("#opType").val() + "&netCode=" + $("#netId").val() + "&accType="
            + dicts.get("accType").val().trim() + "&tallyDate=" + dateType.get("tallyDate").val() + "&blnInstDate=" + dateType.get("blnInstDate").val();
        parent.layer.open({
            title: bt.lang("common.button.preview"),
            type: 2,
            area: ['65%', '50%'],
            maxmin: true,
            content: url,
            end: function () {
            }
        });
    }
    var opTypestr = 'P';
    $doDeleteByBatch = function () {
        layer.confirm(bt.lang("finacial.interest.confirmMsg.deleteInterestInfo"), {//您确定要删除利息信息吗？
            icon: 3,
            btn: [bt.lang("common.button.confirm"), bt.lang("common.button.cancel")]
        }, function (index, layero) {
            bt.loading.show();
            $.ajax({
                type: "POST",
                url: basePath + "/finacial/interest/fcinterest/doDeleteByBatch",
                data: {
                    "opTypestr": opTypestr,
                    "opType": $("#opType").val(),
                    "netCode": $("#netId").val(),
                    "accTypeCode": dicts.get("accType").val().trim(),
                },
                dataType: "json",
                success: function (rs) {
                    bt.loading.hide();
                    layer.msg(bt.lang("finacial.interest.InterestDelete"), {icon: 1});//删除成功
                    init();

                    // 清空原来的数据
                    resetFrame();
                    //bt.navtab.refreshCurrent();
                }
            });
            layer.close(index);
        }, function () {
        });
    }

    doPrintInterestBill = function () {
        var printObj = bt.lodop.init();
        setTimeout(function () {
            LODOP = printObj.lodop;//获取lodop打印对象
            if (!LODOP) {
                return;
            }
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头 取网点名称
            LODOP.ADD_PRINT_TEXT('2mm', '0', '100%', 100, $('#netName').val());
            LODOP.SET_PRINT_STYLEA(0, "FontSize", 22);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            getAllDetailHtml(LODOP);

            // 制表日期
            LODOP.ADD_PRINT_HTM('97%', '3%', 300, 120, "<font style='font-size:12px;font-family: 宋体' format='0'><span>" + bt.lang('com.table.makeDate') + ':' + getNowFormatDate() + "</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            // 制表人
            LODOP.ADD_PRINT_HTM('97%', '33%', 300, 120, "<font style='font-size:12px;font-family: 宋体' format='0'><span>" + bt.lang('table.fcInstrateAdjust.checkerCode') + ':' + "</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            // 制表人
            LODOP.ADD_PRINT_HTM('97%', '66%', 300, 120, "<font style='font-size:12px;font-family: 宋体' format='0'><span>" + bt.lang('table.cmsRatInfo.TabPerson') + ':' + $("#userName").val() + "</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.PREVIEW();
            return false;
        }, 2000);
        return false;
    }

    getAllDetailHtml = function (LODOP) {
        var accType = "";
        if (dicts.get('accType').val()) {
            accType = dicts.get('accType').val().trim();
        }

        var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all" />';
        style += '<link rel="stylesheet" href="' + basePath + '/static/css/common.css" />';
        // padding 上 右 下 左
        style += '<style>'
            + ' table th {  border:1px solid #000!important; height:30px;font-family:宋体;font-size: 12px!important;padding: 6px 10px 6px 18px;font-weight: 400;}'
            + ' table td {  border:1px solid #000!important; font-size: 12px!important;font-family:宋体;line-height: 24px!important;padding: 5px 10px 5px 8px;min-height: 20px!important;}'
            + ' .width80 {width: 80px;} .width50 {width: 50px;}  .width150 {width: 150px;}'
            + ' .center {text-align:center}  .textRight{text-align:right;}'
            + ' </style>';

        var thhead = "<thead><tr>";
        thhead += "<th style='text-align: center;width: 80px;'>" + bt.lang('table.fcAccuBalance.account') + "</th>";
        thhead += "<th style='text-align: center;width: 180px;'>" + bt.lang('table.fcAcc.accName') + "</th>";
        thhead += "<th style='text-align: center;width: 80px;'>" + bt.lang('table.fcInterestDtlTemp.interestStartDate') + "</th>";
        thhead += "<th style='text-align: center;width: 80px;'>" + bt.lang('table.fcInterestDtlTemp.blnInstDate') + "</th>";
        thhead += "<th style='text-align: center;width: 80px;'>" + bt.lang('table.fcInterestDtlTemp.days') + "</th>";
        thhead += "<th style='text-align: center;width: 180px;'>" + bt.lang('table.fcInterestDtlTemp.cumulation') + "</th>";
        thhead += "<th style='text-align: center;width: 80px;'>" + bt.lang('table.fcInterestDtlTemp.interestRate') + "</th>";
        thhead += "<th style='text-align: center;width: 180px;'>" + bt.lang('table.fcInterestDtlTemp.interest') + "</th>";
        thhead += "<th style='text-align: center;width: 80px;'>" + bt.lang('table.fcInterestDtlTemp.rateType') + "</th>";
        //thhead += "<th style='text-align: center;'>"+ bt.lang('table.fcInterestDtlTemp.interestType') + "</th>";
        thhead += "</tr></thead>";

        // 获取科目信息
        var typeData = getAccTypeData();
        if (typeData && typeData.length) {
            var pageIndex = 1;
            for (var index in typeData) {
                var accTypeCode = typeData[index].ACCTYPE;
                // 循环打印所有利息清单
                var param = {
                    "netId": $("#netId").val(),
                    "accType": accTypeCode,
                    "opType": $("#opType").val(),
                    "curr": 1,
                    "poolSign": '0',
                    "interestSign": typeData[index].INTERESTSIGN,
                    "curId": typeData[index].CURID
                };
                var curName = typeData[index].CURNAME;
                $.ajax({
                    url: basePath + "/finacial/interest/fcinterest/interestBillQuery",
                    method: 'POST',
                    data: param,
                    async: false,
                    success: function (result) {
                        var resultData = result;
                        var data = resultData.interestDtls;
                        if (data && data.length) {
                            // 组织当前科目页面数据
                            //var obj= document.getElementById("lxqdiframe").contentWindow;
                            //var ifmObj = obj.document.getElementById("detailThead");
                            var html = "<table>" + thhead;
                            var detailHtml = getDetailHtml(resultData);
                            html += detailHtml;
                            html += "</table>";

                            // 获取科目名称在哪几个打印页显示
                            var num = Math.ceil((data.length - 2) / 2 / 8);
                            var tempPageIndex = pageIndex;
                            for (var j = 2; j <= num; j++) {
                                pageIndex++;
                                tempPageIndex += "," + pageIndex;
                            }

                            LODOP.ADD_PRINT_TEXT('16mm', '0', '100%', 100, bt.dict("InnerAccount.AccType", accTypeCode) + bt.lang('table.interet.lxqd'));
                            LODOP.SET_PRINT_STYLEA(0, "FontSize", 16);
                            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
                            // 打印项在纸张中水平居中
                            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
                            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
                            LODOP.SET_PRINT_STYLEA(0, "PageIndex", tempPageIndex);

                            LODOP.ADD_PRINT_TEXT('26mm', '3%', '100%', 100, bt.lang('table.fcChargeType.curCode') + ":" + curName);
                            LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
                            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
                            // 文本框里 内容对于文本框
                            LODOP.SET_PRINT_STYLEA(0, "Alignment", 1);
                            LODOP.SET_PRINT_STYLEA(0, "Horient", 1);
                            LODOP.SET_PRINT_STYLEA(0, "PageIndex", tempPageIndex);

                            // 表格数据
                            var printHtml = '<head>' + style + '</head><body>' + html + '</body>';
                            LODOP.ADD_PRINT_TABLE('30mm', '0mm', 'RightMargin:3mm', "BottomMargin:9mm", printHtml);
                            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
                            // 强制分页
                            LODOP.NEWPAGEA();
                            pageIndex++;
                        }
                    }
                });
            }
        }
    }

    function getAccTypeData() {
        var accType = "";
        if (dicts.get('accType').val()) {
            accType = dicts.get('accType').val().trim();
        }
        var param = {
            "netId": $("#netId").val(),
            "accType": accType,
            "opType": $("#opType").val(),
            "curr": 1
        };
        $.ajax({
            url: basePath + "/finacial/interest/fcinterest/istBillAccType",
            method: 'POST',
            data: param,
            async: false,
            success: function (result) {
                data = result;
            }
        });
        return data;
    }

    function getDetailHtml(result) {
        var data = result.interestDtls;//数据
        var interestTot = result.interestTot;//合计
        var voucherInfo = result.voucherInfo;//凭证借贷
        var interestRateType = result.interestRateType;//利率类型

        var html = "";
        for (var index in data) {
            var row = data[index];
            html += "<tr>";
            html += "   <td style='text-align: center;'>" + row.account + "</td>";
            html += "   <td style='text-align: center;'>" + row.accName + "</td>";
            html += "   <td style='text-align: center;'>" + bt.date.format(row.interestStartDate, "yyyy-MM-dd") + "</td>";
            html += "   <td style='text-align: center;'>" + bt.date.format(row.blnInstDate, "yyyy-MM-dd") + "</td>";
            html += "   <td style='text-align: center;'>" + row.days + "</td>";
            html += "   <td style='text-align: center;'>" + moneyFun.format(row.cumulation) + "</td>";
            html += "   <td style='text-align: center;'>" + rateType.format(row.interestRate) + "</td>";
            html += "   <td style='text-align: right;'>" + moneyFun.format(row.interest) + "</td>";
            html += "   <td style='text-align: center;'>" + bt.dict("interest.type", row.interestType) + "</td>";
            //html += "   <td align='center width80'>"+bt.dict("interest.sign", row.interestSign)+"</td>";
            //html += "   <td align='center width80'>"+bt.dict("interest.busStatus", row.busStatus)+"</td>";
            html += "</tr>";
        }
        html += "<tr>";
        html += "<td colspan='6'></td><td></td>";
        html += "<td style='text-align: center;'>" + moneyFun.format(interestTot) + "</td>";
        html += "<td></td>";
        html += "</tr>";

        html += "<tr>";
        html += "<td colspan='5' rowspan='2'></td>";
        html += "<td rowspan='2'>" + bt.lang('table.interet.vourcherfl') + "</td>";
        html += "<td colspan='3'>" + voucherInfo[0] + "</td>";
        html += "</tr>";

        html += "<tr>";
        html += "<td colspan='3'>&nbsp;&nbsp;&nbsp;&nbsp;" + voucherInfo[1] + "</td>";
        html += "</tr>";
        return html;
    }

    $doExportInterestBill = function () {
        var url = basePath + "/finacial/interest/fcinterest/doExportInterestBill?opType=" + $("#opType").val() + "&netCode=" + $("#netId").val() + "&accTypeCode=" + dicts.get("accType").val().trim();
        layer.confirm(bt.lang("finacial.interest.ExportPage"), {
            btn: [bt.lang("common.button.page"), bt.lang("common.button.notpage"), bt.lang("common.button.cancel")] //按钮
        }, function () {//分页
            location.href = url + "&impType=1";
        }, function () {//不分页
            location.href = url + "&impType=0";
        }, function () {
        });
    }

    $calcInterest = function (opType, opTips) {
        var tallyDate = dateType.get("tallyDate").val();
        var endInstDate = dateType.get("blnInstDate").val();
        if (!checkTallyDate()) {
            return false;
        }
        if (!endInstDate || endInstDate.trim() == "") {
            layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon: 5});//请输入止息日期！
            return;
        }
        var dayCloseDate = $("#dayCloseDate").val();
        var closeDateEnd = bt.date.addDay(dayCloseDate, 1);

        if (bt.date.compare(endInstDate, closeDateEnd) > 0) {//止息日期应小于等于日结后日期
            layer.alert(bt.lang("finacial.interest.endInstDatelessCloseDate", new Array(bt.date.format(closeDateEnd))), {icon: 5});
            return false;
        }

        layer.confirm(bt.lang("finacial.interest.OpInterest", new Array(opTips)), {
            icon: 3,
            btn: [bt.lang("common.button.confirm"), bt.lang("common.button.cancel")]
        }, function (index, layero) {
            bt.loading.show();
            $.ajax({
                type: "POST",
                url: basePath + "/finacial/interest/fcinterest?doInterestaccrualByBatch",
                data: {
                    "opTypestr": opType,
                    "netCode": $("#netId").val(),
                    "accTypeCode": dicts.get("accType").val().trim(),
                    "tallyDate": tallyDate,
                    "endInstDate": endInstDate
                },
                dataType: "json",
                success: function (rs) {
                    if (rs.success) {
                        bt.loading.hide();
                        layer.msg(rs.obj, {icon: 1});
                        init();
                        // 清空iframe数据
                        resetFrame();
                    } else {
                        bt.loading.hide();
                        if ("notBalance" == rs.obj) {
                            $calcInterest_notBalance(opType, tallyDate, endInstDate, htmlEscape(rs.msg));
                        } else {
                            layer.alert(rs.msg, {icon: 5});
                            init();
                        }
                    }
                    //bt.navtab.refreshCurrent();
                }
            });
            //layer.close(index);
        }, function () {
        });

    }


    $calcInterest_notBalance = function(opType,tallyDate,endInstDate,msg){
    	layer.confirm(msg, {
    		  icon: 3,
    		  area: '500px',
    		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
    		}, function(index, layero){
    			bt.loading.show();
    			$.ajax({
  	    		type: "POST",
  	    		url: basePath+"/finacial/interest/fcinterest/doInterestaccrualByBatch",
  	    		data: {
  	    			"opTypestr":opType,
  	    			"netCode" : $("#netId").val(),
  					"accTypeCode" : dicts.get("accType").val().trim(),
  					"tallyDate" : tallyDate,
  					"endInstDate" : endInstDate,
  					"IsHoldOn":true
  	    		},
  	    		dataType : "json",
  	    		success: function(rs){
  	    			if(rs.success){
  	    				bt.loading.hide();
  	    				layer.msg(rs.obj, {icon:1});
  	    			}else{
  	    				bt.loading.hide();
  	    				layer.alert(rs.msg, {icon:5});
  	    			}
  	    			init();
  	    		}
  	    	});
    		}, function(){
    		});
    }

    $checkInterest = function (opType, opTips) {
        var tallyDate = dateType.get("tallyDate").val();
        var endInstDate = dateType.get("blnInstDate").val();
        if ("check" == opType) {
            if (!checkTallyDate()) {
                return false;
            }
            if (!endInstDate || endInstDate.trim() == "") {
                layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon: 5});//请输入止息日期！
                return;
            }
            var dayCloseDate = $("#dayCloseDate").val();
            var closeDateEnd = bt.date.addDay(dayCloseDate, 1);

            if (bt.date.compare(endInstDate, closeDateEnd) > 0) {//止息日期应小于等于日结后日期
                layer.alert(bt.lang("finacial.interest.endInstDatelessCloseDate", new Array(bt.date.format(closeDateEnd))), {icon: 5});
                return false;
            }
        }
        layer.confirm(bt.lang("finacial.interest.OpInterest", new Array(opTips)), {
            icon: 3,
            btn: [bt.lang("common.button.confirm"), bt.lang("common.button.cancel")]
        }, function (index, layero) {
            bt.loading.show();
            $.ajax({
                type: "POST",
                url: basePath + "/finacial/interest/fcinterest?doProcessInterestCheck",
                data: {
                    "opTypestr": opType,
                    "netCode": $("#netId").val(),
                    "accTypeCode": dicts.get("accType").val().trim(),
                    "tallyDate": tallyDate,
                    "endInstDate": endInstDate
                },
                dataType: "json",
                success: function (rs) {
                    if (rs.success) {
                        bt.loading.hide();
                        layer.msg(bt.lang("finacial.interest.OpInterest.state", new Array(opTips)), {icon: 1});
                    } else {
                        bt.loading.hide();
                        layer.alert(rs.msg, {icon: 5});
                    }
                    init();
                    // 清空原来的数据
                    resetFrame();
                    //bt.navtab.refreshCurrent();
                }
            });
        }, function () {
        });


    }


    function getDayCloseDate() {
        var netCode = $("#netId").val();
        if (!netCode) {
            return;
        }
        $.ajax({
            type: "POST",
            url: basePath + "/finacial/interest/fcinterest?getDayCloseDate",
            data: {"netCode": netCode},
            dataType: "json",
            success: function (rs) {
                $("#dayCloseDate").val(rs.obj);
            }
        });
    }


    /**
     * 检查记账日期
     * @returns
     */
    function checkTallyDate() {
        var tallyDate = dateType.get("tallyDate").val();
        if (!tallyDate || tallyDate.trim() == "") {
            layer.alert(bt.lang("fin.outbook.tally.tallydate.empty"), {icon: 5});//请选择记账日期！
            return false;
        }
        var dayCloseDate = $("#dayCloseDate").val();
        if (dateCompareTo(tallyDate, dayCloseDate) >= 1) {
            layer.alert(bt.lang("finacial.interest.msg.tallyDateLessCloseDate"), {icon: 5});//记账日期应不小于日结日期！
            return false;
        }
        return true;
    }

    // 日期比较大小
    function dateCompareTo(date1, date2) {
        var d, d1, d2, t1, t2;
        d = date1.split("-");
        d1 = new Date(parseInt(d[0], 10), parseInt(d[1], 10) - 1, parseInt(d[2], 10));
        t1 = d1.getTime();
        d = date2.split("-");
        d2 = new Date(parseInt(d[0], 10), parseInt(d[1], 10) - 1, parseInt(d[2], 10));
        t2 = d2.getTime();
        if (t1 > t2) return 0;
        if (t1 == t2) return 1;
        if (t1 < t2) return 2;
    }

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }

});
