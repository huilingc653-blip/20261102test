var corp, dicts, moneyType, item, dateType, rateType, ratioType, dateFmt, isDisCountCredit, isCredit,
    isDisThirdBillCredit;
var datatable = {};
var idsValArray = [];
bt.use(['form', 'radioType', 'datatable', 'moneyType', 'dateType', 'corp', 'select', 'dicts', 'rateType', 'ratioType'
    , 'btImport', 'date', 'itemType', 'accountTable', 'bank', 'mulselect', 'bankInfo'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    rateType = bt.rateType.init();
    ratioType = bt.ratioType.init();
    item = bt.itemType.init();
    bt.mulselect.init();
    dateFmt = bt.date;
    bt.accountTable.init({urldata: {accountType: '2,3',}});
    bt.bank.init({
        elem: '.bank'
    });
    bt.bankInfo.init();
    //挂授信标识
    isDisCountCredit = $("#isDisCountCredit").val();
    isDisThirdBillCredit = $("#isDisThirdBillCredit").val();
    isCredit = $("#isCredit").val();
    isNotDiscountApply = $("#isNotDiscountApply").val();

    importExcel = function () {
        //证书签名
        var signDataMsg = signData("贴现办理导入", 9);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        up = bt.btImport.init({
            code: "20000005",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head, data, select) {
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abBillDiscountManager/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }

    //生成列表
    datatable = bt.datatable.render({
        elem: "#appTable",
        dom: 'Brtlip',
        resize: false,
        colReorder: true,
        checkbox: true,
        mulselect: true,
        clickRowSelected: false,
        scrollY: '400px',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abBillDiscountManager/datatable"
        },
        buttons: [
            {
                text: bt.lang('bill.accebill.update.discountManager'),
                hide: bt.isnotResource('bill:accebill:abBillDiscountManager:apply'),
                action: function (e, dt, node, config) {
                    var selectData = getSelectData(); //获取选中数据
                    submitBatch(selectData);
                }
            },
            {
                text: bt.lang('common.button.back'),
                hide: $("#isNotDiscountApply").val() == 'true',
                action: function (e, dt, node, config) {   //打回
                    var selectData = getSelectData(); //获取选中数据
                    discountBack(selectData);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "bankAcc", bSortable: true},
            {data: "busCorpName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "rpdmkName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="rpdmkName' + full.accId + '" name="rpdmkName">' + data + '</div>'
                    } else {
                        html = '<div id="rpdmkName' + full.accId + '" name="rpdmkName"></div>'
                    }
                    return html;
                }
            },
            {
                data: "discountDate", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="discountDate' + full.accId + '" name="discountDate">' + data + '</div>'
                    } else {
                        html = '<div id="discountDate' + full.accId + '" name="discountDate"></div>'
                    }
                    return html;
                }
            },
            {
                data: "paydate", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (full.status == 11 || full.status == 94) {
                        html = '<div  name="paydateName" id="paydate' + full.accId + '">&nbsp;&nbsp;&nbsp' + full.paydateName + '</div>'
                    } else {
                        html = '<div id="paydateNameDiv'+full.accId+'"><div   name="paydateName" id="paydateName'+full.accId+'">&nbsp;&nbsp;&nbsp' + bt.dict("AB_PAY_DATE", '0')+'</div></div>';
                        html += '<input type="hidden"  id="paydate'+full.accId+'" name="paydate" value="0"/>'
                        //html = '<div class="bt-biz-dict" data-width="85px" data-type="select" data-placeholder="" data-biz-name="paydate' + full.accId + '" data-group="AB_PAY_DATE" data-defaultvalue="' + (data || '0') + '" data-name="paydate" data-callback="changeFn"></div>';
                    }
                    return html;
                }
            },
            {
                data: "deferdate", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data == '0') {
                        html = '<div id="deferdate' + full.accId + '" name="deferdate">0</div>';
                    } else {
                        html = '<div id="deferdate' + full.accId + '" name="deferdate">' + (data || '') + '</div>';
                    }
                    return html;
                }
            },
            {
                data: "daynum", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data == '0') {
                        html = '<div id="daynum' + full.accId + '" name="daynum">0</div>';
                    } else {
                        html = '<div id="daynum' + full.accId + '" name="daynum">' + (data || '') + '</div>';
                    }
                    html += '<input type="hidden"  id="buyerinterestRatio' + full.accId + '" name="buyerinterestRatio"></input>'//买方付息比率
                    return html;
                }
            },
            {
                data: "interest", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0") {
                        html = '<div class="changeful"  id="interest' + full.accId + '" name="interest">' + bt.money.format(data) + '</div>'
                    } else {
                        html = '<div class="changeful"  id="interest' + full.accId + '" name="interest"></div>'
                    }
                    return html;
                }
            },
            {
                data: "interestRates", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0") {
                        html = '<div id="interestRates' + full.accId + '" name="interestRates">' + bt.rateType.getFun().format(full.interestRates, 'percent', 2, ',') + '</div>'
                    } else {
                        html = '<div id="interestRates' + full.accId + '" name="interestRates"></div>'
                    }
                    return html;
                }
            },
            {
                data: "rpdintrstRate", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0") {
                        html = '<div id="rpdintrstRate' + full.accId + '" name="rpdintrstRate">' + bt.rateType.getFun().format(full.rpdintrstRate, 'percent', 2, ',') + '</div>'
                    } else {
                        html = '<div id="rpdintrstRate' + full.accId + '" name="rpdintrstRate"></div>'
                    }
                    return html;
                }
            },
            {
                data: "buyerinterestMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html = '';
                    if (data || data == "0") {
                        html += '<div class="changeful"  id="buyerinterestMoney' + full.accId + '" name="buyerinterestMoney">' + bt.money.format(data) + '</div>'
                    } else {
                        html += '<div class="changeful"  id="buyerinterestMoney' + full.accId + '" name="buyerinterestMoney"></div>'
                    }

                    //html+='<input type="hidden"  id="buyerinterestSing'+full.accId+'"  name="buyerinterestSing"></input>';//是否买方付息
                    return html;
                }
            },
            {
                data: "realMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0") {
                        html = '<div class="changeful"  id="realMoney' + full.accId + '" name="realMoney">' + bt.money.format(data) + '</div>'
                    } else {
                        html = '<div class="changeful"  id="realMoney' + full.accId + '" name="realMoney"></div>'
                    }
                    return html;
                }
            },
            {
                data: "rpdopendt", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="rpdopendt' + full.accId + '" name="rpdopendt">' + data + '</div>'
                    } else {
                        html = '<div id="rpdopendt' + full.accId + '" name="rpdopendt"></div>'
                    }
                    return html;
                }
            },
            {
                data: "rpdduedt", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="rpdduedt' + full.accId + '" name="rpdduedt">' + data + '</div>'
                    } else {
                        html = '<div id="rpdduedt' + full.accId + '" name="rpdduedt"></div>'
                    }
                    return html;
                }
            },
            {
                data: "bankendrsmtmk", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (full.status == 11 || full.status == 94) {
                        html = '<div   name="bankendrsmtmkName" id="bankendrsmtmk'+full.accId +' ">&nbsp;&nbsp;&nbsp' + full.bankendrsmtmkName + '</div>';
                    } else {
                        html = '<div id="bankendrsmtmkNameDiv'+full.accId+'"><div   name="bankendrsmtmkName" id="bankendrsmtmkName'+full.accId+'">&nbsp;&nbsp;&nbsp' + bt.dict("AB_BANEM", 'EM00')+'</div></div>';
                        html += '<input type="hidden"  id="bankendrsmtmk'+full.accId+'" name="bankendrsmtmk" value="EM00"/>'
                    }
                    return html;
                }
            },
            {
                data: "aoaccninfAc", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="aoaccninfAc' + full.accId + '" name="aoaccninfAc">' + data + '</div>'
                    } else {
                        html = '<div id="aoaccninfAc' + full.accId + '" name="aoaccninfAc"></div>'
                    }
                    return html;
                }
            },
            {
                data: "aoaccninfId", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="aoaccninfId' + full.accId + '" name="aoaccninfId">' + data + '</div>'
                    } else {
                        html = '<div id="aoaccninfId' + full.accId + '" name="aoaccninfId"></div>'
                    }
                    return html;
                }
            },
            {
                data: "aoaccninfName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="aoaccninfName' + full.accId + '" name="aoaccninfName">' + data + '</div>'
                    } else {
                        html = '<div id="aoaccninfName' + full.accId + '" name="aoaccninfName"></div>'
                    }
                    return html;
                }
            },
            {
                data: "invcNb", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (full.status == 11 || full.status == 94) {
                        html = '<div   name="invcNb" id="invcNb' + full.accId + '" >&nbsp;&nbsp;&nbsp' + (data || '') + '</div>';
                    } else {
                        html = '<input class="layui-input" style="width:120px;" value="' + (data || '') + '" name="invcNb" id="invcNb' + full.accId + '"  data-maxlength="250"></input>';
                    }
                    return html;
                }
            },
            {
                data: "txlCtrctNb", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (full.status == 11 || full.status == 94) {
                        html = '<div  name="txlCtrctNb" id="txlCtrctNb' + full.accId + '" >&nbsp;&nbsp;&nbsp' + (data || '') + '</div>';
                    } else {
                        html = '<input class="layui-input" style="width:120px;" name="txlCtrctNb" value="' + (data || '') + '"  id="txlCtrctNb' + full.accId + '"  data-maxlength="250"></input>';
                    }
                    return html;
                }
            },
            {
                data: "sttlmmkName", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (data) {
                        html = '<div id="sttlmmkName' + full.accId + '" name="sttlmmkName">' + data + '</div>'
                    } else {
                        html = '<div id="sttlmmkName' + full.accId + '" name="sttlmmkName"></div>'
                    }
                    return html;
                }
            },
            {data: "sendSignName", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "briefIdName", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},//38
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},//40
            {
                data: "blStdAmt", bSortable: true, render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd == 1) {
                        return full.blStdAmt;
                    } else {
                        return "";
                    }
                }
            },
            {data: "blRmtrUnnIdNo", bSortable: true},//42
            {data: "rmk", bSortable: true},
            {
                data: "transMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1' && $("#isNotDiscountApply").val() == 'true') {
                        return '<input  style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                            'value="' + bt.money.format(full.transMoney) + '" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_' + full.accId + '" data-key="' + full.accId + '" data-name="transMoney_' + full.accId + '" data-biz-name="transMoney_' + full.accId + '" />';
                    }
                    return bt.money.format(full.transMoney)+'<input  style="display: none" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                        'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                        'data-id="transMoney_'+full.accId+'" data-key="'+full.accId+'" data-name="transMoney_'+full.accId+'" data-biz-name="transMoney_'+full.accId+'" />';
                }
            },
            {
                data: "attachmentNum",
                bSortable: false,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="attachmentView(this)">' + bt.lang("bill.accebill.attachment.num", [(full.attachmentNum || 0)]) + '</a>';
                    return html;
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html+='<span class="text-explode">|</span>';
                    // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.accId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            },
        ],
        /*fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        //勾选事件
        btClickRowCallback: function (data) {
            var selectData = bt.datatable.getSelectData("#appTable");

            //判断系统是否挂资金计划
            var budgetSel = $("#budgetSel").val();
            if (budgetSel == "1") {
                //判断贴现挂接资金计划科目是否为空，不为空则挂资金计划
                var discountItem = $("#discountItem").val();
                if (discountItem != "") {
                    //当选择多条数据后不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                    if (selectData.length > 1) {
                        layer.alert(bt.lang('bill.accebill.billdiscount.isselectdatabudget'), {icon: 7});
                        $("#tagItemDetail").html("");
                        //全反选，重新加载表格
                        datatable.draw();
                        $('.checkAll').prop('checked',false);
                    } else if(selectData.length == 1){
                        isBudget(selectData[0].corpId, "011");
                    }
                } else {
                    $("#tagItemDetail").html("");
                }
            }

            if (selectData.length > 1) {
                $('#discountCredit').hide();
                $('#rationTr').hide();
                dicts.get('ratmodel').val('0');
                hiddenRatmodel();
            }

            if (selectData.length == 1) {
                if ((selectData[0].billType == 11 && isDisCountCredit != 1)
                    || (isBillTypeTP(bt.datatable.getSelectData()[0].billType) && isDisThirdBillCredit != 1)
                    || selectData[0].billType == 10) {
                    dicts.get('ratmodel').val('0');
                    $('#discountCredit').hide();
                    $('#rationTr').hide();
                    hiddenRatmodel();
                } else {
                    $('#discountCredit').show();
                    //根据承兑人名称查询是否是内部单位，不是则置灰承兑人授信单位类型选择
                    loadIsCorpByCorpName(bt.datatable.getSelectData()[0].acceptor);
                }
            }
            //数据选中后，校验长度
            if (data[0].isSelect) {
                batchSetCol();
                //入账账号和行号是否必填
                isSetRequired();
                checkAInfAc();
                changeMoney();
                changeFn();

                $('#invcNb' + data[0].accId).attr("lay-verify", "strlength");
                $('#txlCtrctNb' + data[0].accId).attr("lay-verify", "strlength");
            } else {
                $('#invcNb' + data[0].accId).attr("lay-verifyenterStockCal", "");
                $('#txlCtrctNb' + data[0].accId).attr("lay-verify", "");
                restore(data);
            }
            // //入账账号和行号是否必填
            // isSetRequired();
            // checkAInfAc();
            // changeFn();
            if ($("#isNotDiscountApply").val() == 'true') {
                isSameNet();
                setVerify();
            }
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            bt.moneyType.init();
            dicts.render();
            moneyType.render();
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 3
        },
        summary: {
            pageSum: true,
            selectSum: true,
            outerData: false,
            sumList: [
                {
                    colNum: '11',
                    dataName: 'billMoney'
                },
                {
                    colNum: '20',
                    dataName: 'interest'
                },
                {
                    colNum: '23',
                    dataName: 'buyerinterestMoney'
                },
                {
                    colNum: '24',
                    dataName: 'realMoney'
                },
                {
                    colNum: '43',
                    dataName: 'transMoney'
                },
            ]
        }
    });

    //取消选中复原数据
    function restore(data) {
        var str = JSON.stringify(data);
        var accId = data[0].accId;
        //贴现种类
        if (data[0].rpdmkName == null) {
            $("#rpdmkName" + accId).text("");
        } else {
            $("#rpdmkName" + accId).text(data[0].rpdmkName);
        }
        //清算方式
        if (data[0].sttlmmkName == null) {
            $("#sttlmmkName" + accId).text("");
        } else {
            $("#sttlmmkName" + accId).text(data[0].sttlmmkName);
        }
        //入账账户
        if (data[0].aoaccninfId == null) {
            $("#aoaccninfId" + accId).text("");
        } else {
            $("#aoaccninfId" + accId).text(data[0].aoaccninfId);
        }
        //入账户名
        if (data[0].aoaccninfName == null) {
            $("#aoaccninfName" + accId).text("");
        } else {
            $("#aoaccninfName" + accId).text(data[0].aoaccninfName);
        }
        //入账行号
        if (data[0].aoaccninfAc == null) {
            $("#aoaccninfAc" + accId).text("");
        } else {
            $("#aoaccninfAc" + accId).text(data[0].aoaccninfAc);
        }
        //赎回利率
        if (data[0].rpdintrstRate == null) {
            $("#rpdintrstRate" + accId).text("");
        } else {
            $("#rpdintrstRate" + accId).text(bt.rateType.getFun().format(data[0].rpdintrstRate, 'percent', 4, ','));
        }
        //贴现天数
        if (data[0].daynum == null) {
            $("#daynum" + accId).text("");
        } else {
            $("#daynum" + accId).text(data[0].daynum);
        }
        //贴现利息
        if (data[0].interest == null) {
            $("#interest" + accId).text("");
        } else {
            $("#interest" + accId).text(bt.moneyType.getFun().format(data[0].interest));
        }
        //贴现利率
        if (data[0].interestRates == null) {
            $("#interestRates" + accId).text("");
        } else {
            $("#interestRates" + accId).text(bt.rateType.getFun().format(data[0].interestRates, 'percent', 4, ','));
        }
        //贴现日期
        if (data[0].discountDate == null) {
            $("#discountDate" + accId).text("");
        } else {
            $("#discountDate" + accId).text(data[0].discountDate);
        }
        $("#rpdopendt" + accId).text(dateFmt.format(data[0].rpdopendt, "yyyy-mm-dd"));//赎回开放日
        $("#rpdduedt" + accId).text(dateFmt.format(data[0].rpdduedt, "yyyy-mm-dd"));//赎回截止日
        $("#buyerinterestRatio" + accId).val(data[0].buyerinterestRatio);//买方付息比率
        $("#buyerinterestMoney" + accId).text(bt.moneyType.getFun().format(data[0].buyerinterestMoney, "##,###.00"));//买方付息金额
        $("#realMoney" + accId).text(bt.moneyType.getFun().format(data[0].realMoney, "##,###.00"));//实付金额
    }


    //是否启用线上清算
    $.post(basePath + '/bill/accebill/abvoucherinfoVice/isEOnlinePay', function (result) {
        if (!result.success) {
            dicts.get("sttlmmk").attr({"data-filterData": "SM00"});
        }
    }, 'json')

    form.on('radio(ratmodel)', function (data) {
        var selectObj = bt.selectType.init();
        hiddenRatmodel();
    });

    $(".changeClass").change(function () {
        batchSetCol();
    })

    //银行
    bt.bailBankInfoCallback = function (data, dom) {
        $("#recNm2").val(data.bankName);
        $("#recAcctsvcr2").val(data.bankCode);
        $("#recAcctId2").val(0);
    }

    mulselect();

});

// 表格输入框离开事件，更新表格汇总功能
var timeId = null
$("#appTable").on("blur",".trans-money-input",function () {
    var that = this
    if(timeId){
        clearTimeout(timeId);
        timeId = null
    }
    timeId = setTimeout(function () {
        var key = $(that).data("key");
        var tableData = $("#appTable").DataTable().rows().data()
        if(key && tableData && tableData.length){
            for (var i = 0; i < tableData.length; i++) {
                if (tableData[i].accId === key) {
                    tableData[i].transMoney = $("#transMoney_" + key).val();
                    changeFn();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)

})

//是否挂资金计划，挂则列表只能单选，否则可以多选
function mulselect() {
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        //判断贴现挂接资金计划科目是否为空，不为空则挂资金计划
        var discountItem = $("#discountItem").val();
        if (discountItem != "") {
            datatable.attr({mulselect: false});
        } else {
            datatable.attr({mulselect: true});
        }
    }
}

//是否隐藏授信
function hiddenRatmodel() {
    if ($('input:radio[name="ratmodel"]:checked').val() == "0") {
        //选择授信为否
        $("#rationTr").css('display', 'none');
        $("#occupyMoneyText").css('display', 'none');
        $("#occupyMoney").css('display', 'none');
        $("#itemCodeText").css('display', 'none');
        $("#itemCodeValue").css('display', 'none');
        $("#itemCodeTd").css('display', 'none');
        $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
    } else {
        $("#rationTr").css('display', '');
        $("#occupyMoneyText").css('display', '');
        $("#occupyMoney").css('display', '');
        $("#itemCodeText").css('display', '');
        $("#itemCodeValue").css('display', '');
        $("#itemCodeTd").css('display', '');
        $('[data-biz-name="ratItemCode"]').find('select').attr('lay-verify', 'required');
    }
    form.renderIcon();
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId
    });
}

//批量提交
function submitBatch(selectData) {
    var rowData = selectData;
    var len = rowData.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //批量提交权限控制
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abBillDiscountManager:apply', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
    }
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    if (!bt.form().verifyFun('#appTable')) {  //校验长度
        return false;
    }
// 新系统出票且分包时，交易金额重新赋值
    for (var i = 0; i < len; i++) {
        if (rowData[i].newStmIssuofpprInd === '1' && rowData[i].blgrdbagcrclPrmtInd === '1') {
            // 判断交易金额是否为空
            var transMoney = $("#transMoney_" + rowData[i].accId).val()
            if (!transMoney) {
                layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                return;
            } else {
                rowData[i].transMoney = transMoney;
            }
        }
    }
    var recType, recAcctId, recNm, recAcctsvcr;
    //贴入机构
    var insideSing = dicts.get("recType").val();
    //启用直接贴现办理时
    if ($("#isNotDiscountApply").val() == 'true') {
        var flag = checkRec(rowData);
        if (!flag) {
            return;
        }
        recType = dicts.get("recType").val();
        if (recType == '1') {
            if (!isSameNet()) {     //单位是否属于同一网点
                return;
            }
            recAcctId = $("#recAcctId1").val();
            recNm = $("#recNm1").val();
            recAcctsvcr = $("#recAcctsvcr1").val();
        }
        if (recType == '0') {
            recAcctId = $("#recAcctId2").val();
            recNm = $("#recNm2").val();
            recAcctsvcr = $("#recAcctsvcr2").val();
        }
    }

    //校验提交数据
    if (!checkSelectData(rowData)) {
        return;
    }
    //授信判断
    if (!checkRelateRat()) {
        bt.loading.hide();
        return false;
    }
    //判断是否挂资金计划,检查金额
    if(!checkBudgetSumMoney()){
        bt.loading.hide();
        return false;
    }

    //证书签名
    var signDataMsg = signData("贴现办理提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        url = basePath + "/bill/accebill/abBillDiscountManager/batchSubmit";
        bt.use(['moneyType'], function () {
            var accIds, transMoneys, vouIds,viceIds;
            var abAccrecordsList = [];
            var urationDTO = [];
            var applyDtls = [];
            var applyDtl = {};
            var abDiscountObj = {};
            var abDiscountList = [];
            var abAccrecordObj = {};
            for (var i = 0; i < len; i++) {
                var accId = rowData[i].accId;
                if (accIds) {
                    accIds = accIds + "," + accId;
                } else {
                    accIds = accId;
                }
                var vouId = rowData[i].vouId;
                if (vouIds) {
                    vouIds = vouIds + "," + vouId;
                } else {
                    vouIds = vouId;
                }
                var viceId = rowData[i].viceId;
                if (viceIds) {
                    viceIds = viceIds + "," + viceId;
                } else {
                    viceIds = viceId;
                }
                if (transMoneys) {
                    transMoneys = transMoneys + "," + rowData[i].transMoney;
                } else {
                    transMoneys = rowData[i].transMoney;
                }
                //获取批量设置要素
                //var buyerinterestSing=$("#buyerinterestSing"+accId).val();   //是否买方付息
                var buyerinterestRatio = $("#buyerinterestRatio" + accId).val(); //买方付息比率
                var paydate = $("#paydate" + accId).val();                  //划款天数
                if (paydate == undefined) {
                    paydate = 0;
                }
                var deferdate = $("#deferdate" + accId).text();                     //顺延天数
                var daynum = $("#daynum" + accId).text();                           //贴现天数
                var interest = bt.moneyType.getFun().toNumber($("#interest" + accId).text());                       //贴现利息
                var buyerinterestMoney = bt.moneyType.getFun().toNumber($("#buyerinterestMoney" + accId).text());   //买方付息金额
                var realMoney = bt.moneyType.getFun().toNumber($("#realMoney" + accId).text());                     //实付金额
                var bankendrsmtmk = $("#bankendrsmtmk" + accId).val();      //是否转让标记
                var invcNb = $("#invcNb" + accId).val();                           //发票号
                var txlCtrctNb = $("#txlCtrctNb" + accId).val();                   //交易合同号
                //获取表单要数
                var rpdmkSelect = $("#rpdmk").find("select").val();                    //贴现种类
                var rpdopendtSelect = $("#rpdopendt").val();                           //赎回开放日
                var rpdduedtSelect = $("#rpdduedt").val();                             //赎回截止日
                var interestRatesSelect = $("#interestRates").val().substring(0, $("#interestRates").val().indexOf('.')+5);                   //贴现利率
                var aoaccninfIdSelect = $("#aoaccninfId").val();                       //入账账户
                var aoaccninfNameSelect = $("#aoaccninfName").val();                       //入账账户
                var aoaccninfAcSelect = $("#aoaccninfAc").val();                       //入账行号
                var businessDate = $("#businessDate").val();                     //业务办理日期
                var discountDateSelect = $("#discountDate").val();                     //贴现日期
                var rmk = $("#rmk").val();                                       //备注
                var rpdintrstRateSelect = $("#rpdintrstRate").val().substring(0, $("#rpdintrstRate").val().indexOf('.') + 5);				   //赎回利率
                var sttlmmkSelect = $("#sttlmmk").find("select").val();//清算方式
                var rpdmk, discountDate, rpdopendt, rpdduedt, interestRates, rpdintrstRate, sttlmmk,
                    aoaccninfId, aoaccninfName, aoaccninfAc;
                //不启用直接贴现办理
                if ($("#isNotDiscountApply").val() == 'false') {
                    //贴现种类
                    if (rpdmkSelect == ' ') {
                        rpdmk = rowData[i].rpdmk;
                    } else {
                        rpdmk = rpdmkSelect;
                    }
                    //清算方式
                    if (sttlmmkSelect == ' ') {
                        sttlmmk = rowData[i].sttlmmk;
                    } else {
                        sttlmmk = sttlmmkSelect;
                    }
                    //贴入人开户行号
                    recAcctsvcr = rowData[i].recAcctsvcr;
                    //贴入人名称
                    recNm = rowData[i].recNm;
                    //贴入人账号
                    recAcctId = rowData[i].recAcctId;
                    //赎回开放日
                    if (rpdmk == 'RM01' && (rpdopendtSelect == "" || rpdopendtSelect == null)) {
                        rpdopendt = rowData[i].rpdopendt;
                    } else if (rpdmk == 'RM01' && rpdopendtSelect != "" && rpdopendtSelect != null) {
                        rpdopendt = rpdopendtSelect;
                    }
                    //赎回截止日
                    if (rpdmk == 'RM01' && (rpdduedtSelect == "" || rpdduedtSelect == null)) {
                        rpdduedt = rowData[i].rpdduedt;
                    } else if (rpdmk == 'RM01' && rpdduedtSelect != "" && rpdduedtSelect != null) {
                        rpdduedt = rpdduedtSelect;
                    }
                    //贴现利率
                    if (interestRatesSelect == "" || interestRatesSelect == null) {
                        interestRates = rowData[i].interestRates;
                    } else {
                        interestRates = interestRatesSelect;
                    }
                    //入账账户
                    if (aoaccninfIdSelect == "" || aoaccninfIdSelect == null) {
                        aoaccninfId = rowData[i].aoaccninfId;
                    } else {
                        aoaccninfId = aoaccninfIdSelect;
                    }
                    //入账户名
                    if (aoaccninfNameSelect == "" || aoaccninfNameSelect == null) {
                        aoaccninfName = rowData[i].aoaccninfName;
                    } else {
                        aoaccninfName = aoaccninfNameSelect;
                    }
                    //入账行号
                    if (aoaccninfAcSelect == "" || aoaccninfAcSelect == null) {
                        aoaccninfAc = rowData[i].aoaccninfAc;
                    } else {
                        aoaccninfAc = aoaccninfAcSelect;
                    }
                    //贴现日期
                    if (discountDateSelect == "" || discountDateSelect == null) {
                        discountDate = rowData[i].discountDate;
                    } else {
                        discountDate = discountDateSelect;
                    }
                    //赎回利率
                    if (rpdmk == 'RM01' && (rpdintrstRateSelect == "" || rpdintrstRateSelect == null)) {
                        rpdintrstRate = rowData[i].rpdintrstRate;
                    } else if (rpdmk == 'RM01' && (rpdintrstRateSelect != "" || rpdintrstRateSelect != null)) {
                        rpdintrstRate = rpdintrstRateSelect;
                    }
                } else {
                    rpdmk = rpdmkSelect;
                    discountDate = discountDateSelect;
                    rpdopendt = rpdopendtSelect;
                    rpdduedt = rpdduedtSelect;
                    interestRates = interestRatesSelect;
                    rpdintrstRate = rpdintrstRateSelect;
                    sttlmmk = sttlmmkSelect;
                    aoaccninfId = aoaccninfIdSelect;
                    aoaccninfName = aoaccninfNameSelect;
                    aoaccninfAc = aoaccninfAcSelect;
                }
                abAccrecordObj = {
                    bankendrsmtmk: bankendrsmtmk, reqAcctId: rowData[i].bankAcc, recAcctsvcr: recAcctsvcr,
                    recAcctId: recAcctId, recNm: recNm, businessDate: businessDate, rmk: rmk, sttlmmk: sttlmmk, businessIsOver:dicts.get('businessIsOver').val()
                };
                abDiscountObj = {
                    paydate: paydate,
                    deferdate: deferdate,
                    daynum: daynum,
                    interest: interest,
                    buyerinterestMoney: buyerinterestMoney,
                    realMoney: realMoney,
                    invcNb: invcNb,
                    txlCtrctNb: txlCtrctNb,
                    rpdmk: rpdmk,
                    sttlmmk: sttlmmk,
                    rpdopendt: rpdopendt,
                    rpdduedt: rpdduedt,
                    interestRates: interestRates,
                    aoaccninfId: aoaccninfId,
                    aoaccninfName: aoaccninfName,
                    aoaccninfAc: aoaccninfAc,
                    discountDate: discountDate,
                    buyerinterestRatio: buyerinterestRatio,
                    rpdintrstRate: rpdintrstRate
                };  //buyerinterestSing:buyerinterestSing
                abAccrecordsList.push(abAccrecordObj);
                abDiscountList.push(abDiscountObj);
            }
            //资金计划数据
            var budgetList=[];
            //资金计划科目代码信息
            var itemCodeStr="";
            var trs = $("#budgetProcessDataTable").children("div[row]");
            if(trs&&trs.length){
                $(trs).each(function(m,n){
                    var i=$(n).attr("row");
                    var budgetObj={};
                    budgetObj['itemCode']=item.get("itemCode"+i).val();
                    budgetObj['itemName']=$("#itemName"+i).val();
                    budgetObj['processMoney']=moneyType.get("processMoney"+i).val();
                    budgetObj['exchangeRate']=moneyType.get("exchangeRate"+i).val();
                    budgetObj['processCurMoney']=moneyType.get("processCurMoney"+i).val();
                    if(itemCodeStr){
                        itemCodeStr=itemCodeStr+";"+budgetObj['itemCode'];
                    }else{
                        itemCodeStr=budgetObj['itemCode'];
                    }
                    budgetList.push(budgetObj);
                });
            }

            //银行授信单位类型
            var ratBankCorpType = $('input:radio[name="ratBankCorpType"]:checked').val();
            var ratBankCorpId = $("#ratBankCorpId").val();
            if (isCredit == "1" && dicts.get('ratmodel').val() == "1"
                && (isDisCountCredit == "1" || isDisThirdBillCredit == "1")) {
                var corpId = bt.datatable.getSelectData()[0].corpId;//corpId
                //当选择授信单位是贴现申请人时
                if (ratBankCorpType == "0") {
                    corpId = bt.datatable.getSelectData()[0].corpId;
                    //当选择授信单位是承兑人时
                } else {
                    //根据承兑人名称查询是否是内部单位
                    corpId = ratBankCorpId;
                }
                var curCode = $("#curCode").val();   //curCode
                var itemFlag = $("#itemFlag").val(); //itemFlag
                var ratCode = $("#ratCode").val();//ratCode
                var ratItemCode = bt.selectType.init().get("ratItemCode").val();//ratItemName
                var beginDate = bt.datatable.getSelectData()[0].startDate;//beginDate
                var occupyMoney = $("#occupyMoney").val();//money
                var applyDtlsLength = $("#applyDtlsLength").val();
                var itemCode, itemMoney;
                if (itemFlag == 1) {//综合授信
                    itemCode = $("#itemCode").val();
                    itemMoney = $("#itemMoney").val();
                    applyDtl = {itemCode: itemCode, itemMoney: itemMoney};
                    applyDtls.push(applyDtl);
                }
                urationDTO = {
                    corpId: corpId, curCode: curCode, itemFlag: itemFlag, ratCode: ratCode,
                    ratItemCode: ratItemCode, beginDate: beginDate, money: occupyMoney, applyDtls: applyDtls
                };
            } else {
                urationDTO = {};
            }
            bt.loading.show();//显示加载中
            bt.jquery.ajax({
                type: 'post',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify({
                    "accIds": accIds,    //流水id
                    "vouIds": vouIds, //主表id
                    "viceIds": viceIds, //主表id
                    "transMoneys": transMoneys,
                    "abAccrecordsList": abAccrecordsList, //新增贴现办理流水信息
                    "abDiscountList": abDiscountList,      //新增贴现办理信息,
                    "urationDTO": urationDTO,   //银行授信信息
                    "recNm": recNm,
                    "recAcctsvcr": recAcctsvcr,
                    "recAcctId": recAcctId,
                    "insideSing": insideSing,
                    "ratBankCorpType": ratBankCorpType,
                    "ratBankCorpId": ratBankCorpId,
                    "budgetList":budgetList
                }),
                asycn: false,
                url: url,
                success: function (result) {
                    if (isCredit == "1" && dicts.get('ratmodel').val() == "1"
                        && (isDisCountCredit == "1" || isDisThirdBillCredit == "1")) {
                        bt.selectType.init().get("ratItemCode").val(null);
                        setRationBtn();
                    }
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        bt.datatable.refreshCurrentPage();
                    }
                    bt.loading.hide(); //关闭加载中
                },
                dataType: 'json'
            });
        })
    })
}

//判断是否挂资金计划,检查金额
function checkBudgetSumMoney(){
    var rowData = getSelectData();
    var len = rowData.length;
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        if ($('#budgetDiv').css('display') != 'none') { 	//处理任何条件下不管有没有上报金额都会提示校验金额与资金计划总金额一致
            //判断贴现挂接资金计划科目是否为空，不为空则挂资金计划
            var discountItem = $("#discountItem").val();
            if (discountItem != "") {
                //当选择多条数据后贴现时不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                if (len == "1") {
                    var sumMoney = bt.moneyType.get("sumMoney").val();
                    //获取选择的票据金额
                    var money = rowData[0].transMoney;
                    // 校验金额与资金计划总金额一致
                    if (sumMoney != money) {
                        layer.alert(bt.lang("accebill.message.budgetMoney"), {icon: 2});
                        return false;
                    }
                }
            }
        }
    }
    return true;
}


function checkRec(rowData) {
    var origNo, checkRecNos, checkRecAndReqNos;
    var recType = dicts.get("recType").val();
    if (recType == '1') {  //贴到结算中心
        var recCorpId = $("#recCorpId").val();
        for (var i = 0; i < rowData.length; i++) {
            origNo = rowData[i].origNo;
            if (rowData[i].billFlag == '11') { //电票只能贴现到银行
                if (checkRecNos) {
                    checkRecNos = checkRecNos + origNo + "<br>";
                } else {
                    checkRecNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            if (rowData[i].corpId == recCorpId) {//贴入人，贴出人不能为同一单位
                if (checkRecAndReqNos) {
                    checkRecAndReqNos = checkRecAndReqNos + origNo + "<br>";
                } else {
                    checkRecAndReqNos = "<br>" + origNo + "<br>";
                }

            }
        }
    }
    if (checkRecNos) {
        layer.alert(bt.lang("bill.accbill.abbillDiscountApply.EleMustToBank", [checkRecNos]), {icon: 7});
        return false;
    }
    if (checkRecAndReqNos) {
        layer.alert(bt.lang("bill.accbill.abbillDiscountApply.checkRecAndReq", [checkRecAndReqNos]), {icon: 7});
        return false;
    }
    return true;
}

//获取选中的所有数据
function getSelectData() {
    var selectData;
    bt.use('datatable', function () {
        selectData = bt.datatable.getSelectData("#appTable");
    })
    return selectData;
}

//判断是否选择了数据
function isSelectData() {
    var selectData = getSelectData();
    if (!selectData.length) {
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectData'), {icon: 7});
        return false;
    }
    return true;
}


//选择电票时，入账行号，账号必填
//选择纸票时，可填可不填
function isSetRequired() {
    var billFlag = dicts.get("billFlag").val();
    var selectData = getSelectData();
    if (!selectData.length) {
        $("#aoaccninfAc").attr({"lay-verify": "strlength", "readonly": false});
        $("#aoaccninfId").attr({"lay-verify": "strlength"});
        form.renderIcon();
        return;
    }
    for (var i = 0; i < selectData.length; i++) {
        if (selectData[i].billFlag == '11') {
            //不启用直接贴现办理
            if ($("#isNotDiscountApply").val() == 'false') {
                if (selectData[i].aoaccninfAc == "" || selectData[i].aoaccninfAc == null) {
                    $("#aoaccninfAc").attr({"lay-verify": "required|strlength", "readonly": true});
                    $("#aoaccninfId").attr({"lay-verify": "required|strlength"});
                }
            } else {
                $("#aoaccninfAc").attr({"lay-verify": "required|strlength", "readonly": true});
                $("#aoaccninfId").attr({"lay-verify": "required|strlength"});
            }
            form.renderIcon();
            return;
        }
    }
    //票据都为纸票
    $("#aoaccninfAc").attr({"lay-verify": "strlength", "readonly": false});
    $("#aoaccninfId").attr({"lay-verify": "strlength"});
    form.renderIcon();
}

//重新查询
function clickSearch() {
    $("#aoaccninfAc").attr({"lay-verify": "strlength"});
    $("#aoaccninfId").attr({"lay-verify": "strlength"});
    form.renderIcon();
}

//批量设置列值
function batchSetCol() {
    $ = bt.jquery, form = bt.form();
    var selectData = getSelectData();
    if (selectData.length) {
        var paydate = dicts.get("paydate").val();
        var bankendrsmtmk = dicts.get("bankendrsmtmk").val();
        var invcNb = $("#invcNb").val();
        var txlCtrctNb = $("#txlCtrctNb").val();
        var buyerinterestRatio = $("#buyerinterestRatio").val();
        //var buyerinterestSing=dicts.get("buyerinterestSing").val();
        var selectData = getSelectData();
        for (var i = 0; i < selectData.length; i++) {
            var accId = selectData[i].accId;

            //划款天数
            $("#paydate" + accId).val(paydate);
            selectData[i].paydate = paydate;
            var htl = '<div   name="paydateName" id="paydateName' + accId + '">&nbsp;&nbsp;&nbsp' + bt.dict("AB_PAY_DATE", paydate) + '</div>';
            $("#paydateNameDiv" + accId).html(htl);

            //不得转让标记
            $("#bankendrsmtmk" + accId).val(bankendrsmtmk);
            selectData[i].bankendrsmtmk = bankendrsmtmk;
            var htl = '<div   name="bankendrsmtmkName" id="bankendrsmtmkName' + accId + '">&nbsp;&nbsp;&nbsp' + bt.dict("AB_BANEM", bankendrsmtmk) + '</div>';
            $("#bankendrsmtmkNameDiv" + accId).html(htl);

            //发票号码
            /*if (!invcNb) {
                layer.alert(bt.lang("bill.accebill.abBillDiscountManager.setInvcNb"), {icon: 7});
                //return;
            }*/
            $("#invcNb" + accId).val(invcNb);
            selectData[i].invcNb = invcNb;

            //交易合同号
            /*if (!txlCtrctNb) {
                layer.alert(bt.lang("bill.accebill.abBillDiscountManager.setTxlCtrctNb"), {icon: 7});
                //return;
            }*/
            $("#txlCtrctNb" + accId).val(txlCtrctNb);
            selectData[i].txlCtrctNb = txlCtrctNb;

            //买方付息比率
            if (!buyerinterestRatio) {
                layer.alert(bt.lang("bill.accebill.abBillDiscountManager.setRatio"), {icon: 7});
                //return;
            }
            /*if(buyerinterestSing=='1'&&buyerinterestRatio<1){
               layer.alert(bt.lang("bill.accebill.abBillDiscountManager.setRatioRule"),{icon: 7});
               return;
             }*/
            $('#buyerinterestRatio' + accId).val(buyerinterestRatio);
            selectData[i].buyerinterestRatio = buyerinterestRatio;
        }
        changeFn();
    }
}

//设置是否买方付息
/*function setBuyerinterestSing(){
	$ = bt.jquery,form = bt.form();
	//选择是,设置比率为1
	var buyerinterestSing=dicts.get("buyerinterestSing").val();
	if(buyerinterestSing=='1'){
		rateType.get("buyerinterestRatio").val("1");
	}
}*/

//设置贴现方式
function setRpdmk() {
    var rpdmk = $("#rpdmk").find("select").val();  //贴现方式
    if (rpdmk == 'RM00') {
        $('#rpdopendt').attr({"lay-verify": "", "readonly": true});
        $('#rpdduedt').attr({"lay-verify": "", "readonly": true});
        $('#rpdopendt').val('');
        $('#rpdduedt').val('');
        dateType.get('rpdopendt').reset();
        dateType.get('rpdduedt').reset();
    } else if (rpdmk == 'RM01') {
        //不启用直接贴现办理
        if ($("#isNotDiscountApply").val() == 'false') {
            $('#rpdopendt').attr({"lay-verify": "", "readonly": false});
            $('#rpdduedt').attr({"lay-verify": "", "readonly": false});
        } else {
            $('#rpdopendt').attr({"lay-verify": "required", "readonly": false});
            $('#rpdduedt').attr({"lay-verify": "required", "readonly": false});
        }
        dateType.get('rpdopendt').reset();
        dateType.get('rpdduedt').reset();
    }
    setDiscountData();
    form.renderIcon();
}

//判断是否触发计算数值函数
function changeFn() {
    culculate();
    setDiscountData();
}

//设置贴现种类、清算方式、入账行号、入账账户到列表显示
function setDiscountData() {
    //贴现方式、清算方式
    var rpdmkName, sttlmmkName;
    var rpdmkSelect = $("#rpdmk").find("select").val();
    var sttlmmkSelect = $("#sttlmmk").find("select").val();
    var aoaccninfIdSelect = $("#aoaccninfId").val();                       //入账账户
    var aoaccninfNameSelect = $("#aoaccninfName").val();                   //入账户名
    var aoaccninfAcSelect = $("#aoaccninfAc").val();                       //入账行号
    var rpdintrstRateSelect = $("#rpdintrstRate").val();//赎回利率

    if (rpdmkSelect != ' ') {
        rpdmkName = $('[data-biz-name="rpdmk"]').find("select option:selected").text()
    }
    if (sttlmmkSelect != ' ') {
        sttlmmkName = $('[data-biz-name="sttlmmk"]').find("select option:selected").text()
    }
    var tableData = getSelectData();
    if (!tableData.length) {
        return;
    }
    for (var i = 0; i < tableData.length; i++) {
        var accId = tableData[i].accId;
        if (rpdmkName) {
            $("#rpdmkName" + accId).text(rpdmkName);//贴现种类
        }
        if (sttlmmkName) {
            $("#sttlmmkName" + accId).text(sttlmmkName);//清算方式
        }
        //入账账户
        if (aoaccninfIdSelect != "" && aoaccninfIdSelect != null) {
            $("#aoaccninfId" + accId).text(aoaccninfIdSelect);
        }
        //入账户名
        if (aoaccninfNameSelect != "" && aoaccninfNameSelect != null) {
            $("#aoaccninfName" + accId).text(aoaccninfNameSelect);
        }
        //入账行号
        if (aoaccninfAcSelect != "" && aoaccninfAcSelect != null) {
            $("#aoaccninfAc" + accId).text(aoaccninfAcSelect);
        }
        //赎回利率
        if (rpdintrstRateSelect != "" && rpdintrstRateSelect != null) {
            $("#rpdintrstRate" + accId).text(bt.rateType.getFun().format(rpdintrstRateSelect, 'percent', 4, ','));
        }
    }
}

//验证日期
function calculationDateFn() {
    var rpdmkSelect = $("#rpdmk").find("select").val();    //贴现方式
    var discountDateSelect = $("#discountDate").val();     //贴现申请日期
    var rpdopendtSelect = $("#rpdopendt").val();           //赎回开放日
    var rpdduedtSelect = $("#rpdduedt").val();             //赎回截止日
    var interestRatesSelect = $("#interestRates").val();   //贴现利率
    //比较申请日与截止日
    discountDateSelect = dateFmt.get(discountDateSelect)
    rpdopendtSelect = dateFmt.get(rpdopendtSelect);
    rpdduedtSelect = dateFmt.get(rpdduedtSelect);
    //如果贴现日期为空，刚取列表数据的贴现日期
    //如果有勾选数据则循环判断选择的数据的贴现日期与贴现赎回日期
    var tableData = getSelectData();
    if (!tableData.length) {
        return;
    }
    for (var i = 0; i < tableData.length; i++) {
        var rpdmk, discountDate, rpdopendt, rpdduedt, interestRates;
        //不启用直接贴现办理
        if ($("#isNotDiscountApply").val() == 'false') {
            //贴现种类
            if (rpdmkSelect == ' ') {
                rpdmk = tableData[i].rpdmk;
            } else {
                rpdmk = rpdmkSelect;
            }
            //贴现日期
            if (discountDateSelect == "" || discountDateSelect == null) {
                discountDate = dateFmt.get(tableData[i].discountDate);
            } else {
                discountDate = discountDateSelect;
            }
            //贴现利率
            if (interestRatesSelect == "" || interestRatesSelect == null) {
                interestRates = tableData[i].interestRates;
            } else {
                interestRates = interestRatesSelect;
            }
            //赎回开放日
            if (rpdmk == 'RM01' && (rpdopendtSelect == "" || rpdopendtSelect == null)) {
                rpdopendt = dateFmt.get(tableData[i].rpdopendt);
            } else if (rpdmk == 'RM01' && rpdopendtSelect != "" && rpdopendtSelect != null) {
                rpdopendt = rpdopendtSelect;
            }
            //赎回截止日
            if (rpdmk == 'RM01' && (rpdduedtSelect == "" || rpdduedtSelect == null)) {
                rpdduedt = dateFmt.get(tableData[i].rpdduedt);
            } else if (rpdmk == 'RM01' && rpdduedtSelect != "" && rpdduedtSelect != null) {
                rpdduedt = rpdduedtSelect;
            }
        } else {
            rpdmk = rpdmkSelect;
            discountDate = discountDateSelect;
            rpdopendt = rpdopendtSelect;
            rpdduedt = rpdduedtSelect;
            interestRates = interestRatesSelect;
        }
        //赎回式
        if (rpdmk == 'RM01' && discountDate && rpdopendt && rpdduedt && interestRates) {
            if (dateFmt.compare(rpdduedt, rpdopendt) > 0 && dateFmt.compare(rpdduedt, discountDate) > 0 &&
                dateFmt.compare(rpdopendt, discountDate) >= 0) {     //贴现赎回开放日必须大于申请日期
                if (culDeferdate(dateFmt.format(rpdduedt, "yyyy-mm-dd")) == -1) {
                    layer.alert(bt.lang('bill.accebill.abBillDiscountManager.dateError', [dateFmt.format(rpdduedt, "yyyy-mm-dd")]), {icon: 7});
                    return;
                }
            } else {
                //赎回截止日必须大于赎回开放日!
                if (dateFmt.compare(rpdduedt, rpdopendt) <= 0) {
                    layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comDueToOpenMsg'), {icon: 7});
                    return;
                }
                if (dateFmt.compare(rpdopendt, discountDate) < 0) {
                    layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comRpdopendtMsg'), {icon: 7});
                    return;
                }
                if (dateFmt.compare(rpdduedt, discountDate) <= 0) {
                    layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comRpdduedtMsg'), {icon: 7});
                    return;
                }
            }
        }
    }
}

//计算数值
function culculate() {
    bt.use(['date', 'dateType', 'moneyType'], function () {
        $ = bt.jquery, form = bt.form();
        var tableData = getSelectData();
        if (!tableData.length) {
            return;
        }

        var rpdmkSelect = $("#rpdmk").find("select").val(); //贴现方式
        var discountDateSelect = $("#discountDate").val();   //贴现申请日期
        var rpdopendtSelect = $("#rpdopendt").val();          //赎回开放日
        var rpdduedtSelect = $("#rpdduedt").val();            //赎回截止日
        var interestRatesSelect = $("#interestRates").val(); //贴现利率
        var comDisToStartNos;
        var comDisToEndNos;
        var comOpenToStartNos;
        var comDueToEndNos;
        var dateErrorNos;
        var culEndDate = '';                    //贴现利息计算截止日
        //格式化日期
        discountDateSelect = dateFmt.get(discountDateSelect);
        rpdopendtSelect = dateFmt.get(rpdopendtSelect);
        rpdduedtSelect = dateFmt.get(rpdduedtSelect);
        for (var i = 0; i < tableData.length; i++) {
            var rpdmk, discountDate, rpdopendt, rpdduedt, interestRates;
            //不启用直接贴现办理
            if ($("#isNotDiscountApply").val() == 'false') {
                //贴现种类
                if (rpdmkSelect == ' ') {
                    rpdmk = tableData[i].rpdmk;
                } else {
                    rpdmk = rpdmkSelect;
                }
                //贴现日期
                if (discountDateSelect == "" || discountDateSelect == null) {
                    discountDate = dateFmt.get(tableData[i].discountDate);
                } else {
                    discountDate = discountDateSelect;
                }
                //贴现利率
                if (interestRatesSelect == "" || interestRatesSelect == null) {
                    interestRates = tableData[i].interestRates;
                } else {
                    interestRates = interestRatesSelect;
                }
                if (rpdmk == 'RM01') {
                    calculationDateFn();
                }
                //赎回开放日
                if (rpdmk == 'RM01' && (rpdopendtSelect == "" || rpdopendtSelect == null)) {
                    rpdopendt = dateFmt.get(tableData[i].rpdopendt);
                } else if (rpdmk == 'RM01' && rpdopendtSelect != "" && rpdopendtSelect != null) {
                    rpdopendt = rpdopendtSelect;
                }
                //赎回截止日
                if (rpdmk == 'RM01' && (rpdduedtSelect == "" || rpdduedtSelect == null)) {
                    rpdduedt = dateFmt.get(tableData[i].rpdduedt);
                } else if (rpdmk == 'RM01' && rpdduedtSelect != "" && rpdduedtSelect != null) {
                    rpdduedt = rpdduedtSelect;
                }
                //买断式 如果贴现日期或贴现利率为空则退出
                if (rpdmk == 'RM00' && (discountDate == null || discountDate == "" || interestRates == null || interestRates == "")) {
                    continue;
                }
            } else {
                rpdmk = rpdmkSelect;
                discountDate = discountDateSelect;
                rpdopendt = rpdopendtSelect;
                rpdduedt = rpdduedtSelect;
                interestRates = interestRatesSelect;
            }
            var accId = tableData[i].accId;
            var origNo = tableData[i].origNo;                    //票据号
            var billMoney = parseFloat(tableData[i].billMoney); //票据金额
            var transMoney = parseFloat(tableData[i].transMoney); //票据金额 //TODO 金额
            var paydate = $("#paydate" + accId).val();    //划款天数
            var startDate = dateFmt.get(tableData[i].startDate);   //出票日期
            var endDate = dateFmt.get(tableData[i].endDate);       //到期日期
            var buyerinterestRatio = $("#buyerinterestRatio" + accId).val();//买方付息比率
            //贴现申请日期不能小于开票日期
            if (dateFmt.compare(startDate, discountDate) > 0) {
                if (comDisToStartNos) {
                    comDisToStartNos = comDisToStartNos + origNo + "<br>";
                } else {
                    comDisToStartNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            //贴现申请日期必须小于到期日期
            if (dateFmt.compare(endDate, discountDate) <= 0) {
                if (comDisToEndNos) {
                    comDisToEndNos = comDisToEndNos + origNo + "<br>";
                } else {
                    comDisToEndNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            //判断贴现种类
            if (rpdmk == "RM01") { //RM01 回购式
                //贴现赎回开放日不能小于开票日期
                if (dateFmt.compare(startDate, rpdopendt) > 0) {
                    if (comOpenToStartNos) {
                        comOpenToStartNos = comOpenToStartNos + origNo + "<br>";
                    } else {
                        comOpenToStartNos = "<br>" + origNo + "<br>";
                    }
                    continue;
                }
                //贴现赎回截止日必须小于到期日期
                if (dateFmt.compare(endDate, rpdduedt) <= 0) {
                    if (comDueToEndNos) {
                        comDueToEndNos = comDueToEndNos + origNo + "<br>";
                    } else {
                        comDueToEndNos = "<br>" + origNo + "<br>";
                    }
                    continue;
                }
                //culEndDate = rpdduedt;  //赎回截止日为贴现利息计算截止日
                culEndDate = endDate;  //赎回截止日为贴现利息计算截止日   modify zouxs 2024.01.05 赎回的贴现利息：(票据到期日期-贴现办理日期)*票面金额*贴现利率/100
            } else {                    //买断式
                culEndDate = endDate;   //到期日期为贴现利息计算截止日
                //日历维护
                if (culDeferdate(dateFmt.format(culEndDate, "yyyy-mm-dd")) == -1) {
                    if (dateErrorNos) {
                        dateErrorNos = dateErrorNos + origNo + "<br>";
                    } else {
                        dateErrorNos = "<br>" + origNo + "<br>";
                    }
                    continue;
                }
            }

            //格式化日期
            culEndDate = dateFmt.format(culEndDate, "yyyy-mm-dd");
            discountDate = dateFmt.format(discountDate, "yyyy-mm-dd");//贴现办理日
            var culdays = bt.dateType.getFun().getBetweenDay(discountDate, culEndDate);//计算日期之差
            var deferdate = culDeferdate(culEndDate);  //顺延天数
            if(deferdate == -1){
                deferdate = 0;
            }

            var daynum = culdays + parseInt(paydate) + parseInt(deferdate);
            var interest = (bt.moneyType.getFun().format((daynum * transMoney * 100 * parseFloat(interestRates*10000) / 360)/1000000, {decimal: 2})).replace(/,/g, "");

            $("#deferdate" + accId).text(deferdate);//顺延天数
            $("#daynum" + accId).text(daynum);//贴现天数
            $("#interest" + accId).text(bt.moneyType.getFun().format(interest));//贴现利息
            $("#interestRates" + accId).text(bt.rateType.getFun().format(interestRates, 'percent', 4, ','));//贴现利率
            $("#discountDate" + accId).text(discountDate);//贴现日期
            $("#rpdopendt" + accId).text(dateFmt.format(rpdopendt, "yyyy-mm-dd"));//赎回开放日
            $("#rpdduedt" + accId).text(dateFmt.format(rpdduedt, "yyyy-mm-dd"));//赎回截止日
            //判断是否设值买方付息,买方付息比率不填默认为0
            if (!buyerinterestRatio) {
                buyerinterestRatio = 0.000000;
                $("#buyerinterestRatio" + accId).val(buyerinterestRatio);//买方付息比率
                //$("#buyerinterestSing"+accId).val("0");//是否买方付息 否
            }
            if (interest && transMoney) {
                var buyerinterestMoney = (interest * buyerinterestRatio).toFixed(2);
                var realMoney = (parseFloat(transMoney) - parseFloat(interest) + parseFloat(buyerinterestMoney)).toFixed(2);
                $("#buyerinterestMoney" + accId).text(bt.moneyType.getFun().format(buyerinterestMoney, "##,###.00"));//买方付息金额
                $("#realMoney" + accId).text(bt.moneyType.getFun().format(realMoney, "##,###.00"));//实付金额
            }
        }
        bt.datatable.updateSummary('#appTable');
        //重新绘制表格
        $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();

        if (comDisToStartNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDisToStartMsg", [comDisToStartNos]), {icon: 7});
            return;
        }

        if (comDisToEndNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDisToEndMsg", [comDisToEndNos]), {icon: 7});
            return;
        }

        if (comOpenToStartNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comOpenToStartMsg", [comOpenToStartNos]), {icon: 7});
            return;
        }

        if (comDueToEndNos) {
            layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDueToEndMsg", [comDueToEndNos]), {icon: 7});
            return;
        }

        if (dateErrorNos) {
            layer.alert(bt.lang('bill.accebill.abBillDiscountManager.dateErrorMsg', [dateErrorNos]), {icon: 7});

        }
    });
}

//计算假日顺延天数
function culDeferdate(culEndDate) {
    $ = bt.jquery, form = bt.form();
    var deferdate;
    var url = basePath + '/bill/accebill/abBillDiscountManager/getDeferdate';
    $.ajax({
        type: 'post',
        data: {"endTime": culEndDate},
        async: false,
        url: url,
        cache: false,
        success: function (result) {
            if (result.success) {
                bt.use(['date', 'dateType'], function () {
                    var workDay = dateFmt.format(dateFmt.get(result.obj), "yyyy-mm-dd");
                    deferdate = bt.dateType.getFun().getBetweenDay(culEndDate, workDay);
                })
            } else {
                deferdate = -1;      //返回null,返回-1
            }
        },
        dataType: 'json'
    });
    return deferdate;
}

//填写入账行号时，校验开户行是否一致
function clickAInfAc(fieldName) {
    //获取选择数据
    var selectData = getSelectData();
    var len = selectData.length;
    if (!len) {
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectDiscountBill'), {icon: 7});
        return;
    }
    var obj = checkSameCode(fieldName, selectData);
    if (!obj.flag && fieldName == 'recAcctsvcr') {
        //所选电票贴入人开户行号不一致，无法设置贴入人行号为入账行号
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectSameAcctsvcr'), {icon: 7});
        return;
    }
    if (!obj.flag && fieldName == 'bankCode') {
        //所选电票贴出人开户行号不一致，无法设置贴出人行号为入账行号
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectSameBankCode'), {icon: 7});
        return;
    }
    $("#aoaccninfAc").val(obj.code);
    if (fieldName == 'recAcctsvcr') {
        $("#aoaccninfAc").attr("opType", "accRec");
    }
    if (fieldName == 'bankCode') {
        $("#aoaccninfAc").attr("opType", "accReq");
    }
    setDiscountData();
}

//变更选票信息时，检验入账行号填写是否正确
function checkAInfAc(message) {
    var selectData = getSelectData();
    var len = selectData.length;
    var checkFlag = true;
    if (!len) {
        return;
    }
    var aoaccninfAc = $("#aoaccninfAc").val();
    var opType = $("#aoaccninfAc").attr("opType");
    var obj = {};
    //校验贴出人行号
    if (aoaccninfAc && opType == 'accReq') {
        obj = checkSameCode("bankCode", selectData);
        if (!obj.flag) {
            //所选电票贴出人开户行号不一致，不能设置贴出行号为入账行号！
            layer.alert(bt.lang('bill.accebill.abBillDiscountManager.canNotSetBankCode'), {icon: 7});
            checkFlag = false;
            return;
        }
    }
    //校验贴入人行号
    if (aoaccninfAc && opType == 'accRec') {
        obj = checkSameCode("recAcctsvcr", selectData);
        if (!obj.flag) {
            //所选电票贴入人开户行号不一致，不能设置贴入行号为入账行号！
            layer.alert(bt.lang('bill.accebill.abBillDiscountManager.canNotSetAcctsvcr'), {icon: 7});
            checkFlag = false;
            return;
        }
    }
    //校验通过返回true
    return checkFlag;
}

//贴出人或贴入人开户行号是否一致
function checkSameCode(fieldName, selectData) {
    var code = "";
    var selCode;
    var obj = {};
    var flag = true;
    var selCode;
    obj['flag'] = flag;
    obj['code'] = code;
    for (var i = 0; i < selectData.length; i++) {
        if (selectData[i].billFlag == '11') {
            //贴出人开户行号
            if (fieldName == "bankCode") {
                if (selectData[i].bankCode == "" || selectData[i].bankCode == null) {
                    selCode = selectData[i].reqAcctsvcr;
                } else {
                    selCode = selectData[i].bankCode;
                }
            }
            if (fieldName == "recAcctsvcr") {
                //判断是否直接贴现办理
                if ($("#isNotDiscountApply").val() == 'true') {
                    //贴入机构
                    var recType = dicts.get("recType").val();
                    //贴入行号
                    var recAcctsvcr = "";
                    if (recType == '1') {
                        if (!isSameNet()) {     //单位是否属于同一网点
                            return;
                        }
                        recAcctsvcr = $("#recAcctsvcr1").val();
                    }
                    if (recType == '0') {
                        recAcctsvcr = $("#recAcctsvcr2").val();
                    }
                    selCode = recAcctsvcr;
                } else {
                    selCode = selectData[i].recAcctsvcr;
                }
            }
            if (code && code != selCode) {
                flag = false;
                obj['flag'] = flag;
                break;
            }
            code = selCode;//赋值
            obj['code'] = code;
        }
    }
    return obj;
}

//批量提交时校验
function checkSelectData(selectData) {
    $ = bt.jquery, form = bt.form();
    var tableData = selectData;
    var len = tableData.length;
    var rpdmkSelect = $("#rpdmk").find("select").val(); //贴现方式
    var businessDate = $("#businessDate").val();//业务办理日期
    var discountDateSelect = $("#discountDate").val(); //贴现申请日期
    var rpdopendtSelect = $("#rpdopendt").val();    //赎回开放日
    var rpdduedtSelect = $("#rpdduedt").val();    //赎回截止日
    //比较申请日,开放日,截止日
    discountDateSelect = dateFmt.get(discountDateSelect);
    rpdopendtSelect = dateFmt.get(rpdopendtSelect);
    rpdduedtSelect = dateFmt.get(rpdduedtSelect);

    var comDisToStartNos;
    var comDisToEndNos;
    var comOpenToStartNos;
    var comDueToEndNos;
    var buyerMoneyNos;
    var dateErrorNos;
    for (var i = 0; i < tableData.length; i++) {
        var rpdmk, discountDate, rpdopendt, rpdduedt;
        if (!tableData[i].buyerinterestRatio) {
            layer.alert('买方付息比例不能为空！若多笔数据请选中数据后点击“批量设置”按钮', {icon: 7});
            return false;
        }
        //不启用直接贴现办理
        if ($("#isNotDiscountApply").val() == 'false') {
            //贴现种类
            if (rpdmkSelect == ' ') {
                rpdmk = tableData[i].rpdmk;
            } else {
                rpdmk = rpdmkSelect;
            }
            //贴现日期
            if (discountDateSelect == "" || discountDateSelect == null) {
                discountDate = dateFmt.get(tableData[i].discountDate);
            } else {
                discountDate = discountDateSelect;
            }
            //赎回开放日
            if (rpdmk == 'RM01' && (rpdopendtSelect == "" || rpdopendtSelect == null)) {
                rpdopendt = dateFmt.get(tableData[i].rpdopendt);
            } else if (rpdmk == 'RM01' && rpdopendtSelect != "" && rpdopendtSelect != null) {
                rpdopendt = rpdopendtSelect;
            }
            //赎回截止日
            if (rpdmk == 'RM01' && (rpdduedtSelect == "" || rpdduedtSelect == null)) {
                rpdduedt = dateFmt.get(tableData[i].rpdduedt);
            } else if (rpdmk == 'RM01' && rpdduedtSelect != "" && rpdduedtSelect != null) {
                rpdduedt = rpdduedtSelect;
            }
        } else {
            rpdmk = rpdmkSelect;
            discountDate = discountDateSelect;
            rpdopendt = rpdopendtSelect;
            rpdduedt = rpdduedtSelect;
        }
        //赎回式时校验
        if (rpdmk == 'RM01') {
            if (dateFmt.compare(rpdduedt, rpdopendt) <= 0) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comDueToOpenMsg'), {icon: 7});
                return;
            }
            if (dateFmt.compare(rpdopendt, discountDate) < 0) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comRpdopendtMsg'), {icon: 7});
                return;
            }
            if (dateFmt.compare(rpdduedt, discountDate) <= 0) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.comRpdduedtMsg'), {icon: 7});
                return;
            }
            if (culDeferdate(dateFmt.format(rpdduedt, "yyyy-mm-dd")) == -1) {
                layer.alert(bt.lang('bill.accebill.abBillDiscountManager.dateError', [dateFmt.format(rpdduedt, "yyyy-mm-dd")]), {icon: 7});
                return;
            }
        }
        var origNo = tableData[i].origNo//票据号
        var billMoney = parseFloat(tableData[i].billMoney)//票据金额
        var endDate = dateFmt.get(tableData[i].endDate);   //票据到期日
        var startDate = dateFmt.get(tableData[i].startDate);
        var buyerinterestMoney = $("#buyerinterestMoney" + tableData[i].accId).text();
        if (buyerinterestMoney == "") {
            buyerinterestMoney = "0.00";
        }

        //贴现申请日期不能小于开票日期
        if (dateFmt.compare(startDate, discountDate) > 0) {
            if (comDisToStartNos) {
                comDisToStartNos = comDisToStartNos + origNo + "<br>";
            } else {
                comDisToStartNos = "<br>" + origNo + "<br>";
            }
            continue;
        }
        //贴现申请日期必须小于到期日期
        if (dateFmt.compare(endDate, discountDate) <= 0) {
            if (comDisToEndNos) {
                comDisToEndNos = comDisToEndNos + origNo + "<br>";
            } else {
                comDisToEndNos = "<br>" + origNo + "<br>";
            }
            continue;
        }
        if (rpdmk == "RM01") { //赎回式
            //贴现赎回开放日不能小于开票日期
            if (dateFmt.compare(startDate, rpdopendt) > 0) {
                if (comOpenToStartNos) {
                    comOpenToStartNos = comOpenToStartNos + origNo + "<br>";
                } else {
                    comOpenToStartNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
            //贴现赎回截止日必须小于到期日期
            if (dateFmt.compare(endDate, rpdduedt) <= 0) {
                if (comDueToEndNos) {
                    comDueToEndNos = comDueToEndNos + origNo + "<br>";
                } else {
                    comDueToEndNos = "<br>" + origNo + "<br>";
                }
                continue;
            }
        } else {//买断式
            //到期日期是否已经在日历中维护
            if (culDeferdate(dateFmt.format(endDate, "yyyy-mm-dd")) == -1) {
                if (dateErrorNos) {
                    dateErrorNos = dateErrorNos + origNo + "<br>";
                } else {
                    dateErrorNos = "<br>" + origNo + "<br>";
                }
                continue;
            }

        }

        //是否填写买方付息比率利息
        if (!buyerinterestMoney) {
            if (buyerMoneyNos) {
                buyerMoneyNos = buyerMoneyNos + origNo + "<br>";
            } else {
                buyerMoneyNos = "<br>" + origNo + "<br>";
            }

        }

    }
    //如果是电票业务校验入账行号
    if (!checkAInfAc()) {
        return;
    }
    if (buyerMoneyNos) {
        layer.alert(bt.lang("bill.accebill.abBillDiscountManager.buyerMoneyMsg", [buyerMoneyNos]), {icon: 7});
        return false;
    }

    if (comDisToStartNos) {
        layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDisToStartMsg", [comDisToStartNos]), {icon: 7});
        return false;
    }

    if (comDisToEndNos) {
        layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDisToEndMsg", [comDisToEndNos]), {icon: 7});
        return false;
    }

    if (comOpenToStartNos) {
        layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comOpenToStartMsg", [comOpenToStartNos]), {icon: 7});
        return false;
    }

    if (comDueToEndNos) {
        layer.alert(bt.lang("bill.accebill.abBillDiscountManager.comDueToEndMsg", [comDueToEndNos]), {icon: 7});
        return false;
    }

    if (dateErrorNos) {
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.dateErrorMsg', [dateErrorNos]), {icon: 7});
        return false;
    }
    return true;
}

//贴现办理打回
function discountBack(selectData) {
    var rowData = selectData;
    var len = rowData.length;
    var accIds;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    for (var i = 0; i < len; i++) {
        var accId = rowData[i].accId;
        if (accIds) {
            accIds = accIds + "," + accId;
        } else {
            accIds = accId;
        }
    }
    $.ajax({
        type: 'post',
        data: JSON.stringify({accIds: accIds}),
        url: basePath + '/bill/accebill/abBillDiscountManager/batchBack',
        contentType: "application/json",
        dataType: 'json',
        async: false,
        success: function (result) {
            layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }
    });

}

function attachmentView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.accId,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.viceId || null),
        "briefId": '011',
        "isNotOp": isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}


//选择授信额度
function beforeRation() {

    if (bt.datatable.getSelectData().length < 1) {
        //未选中数据
        parent.layer.msg(bt.lang("common.message.confirm.selectData"), {icon: 2});
        return "";
    }
    if (bt.datatable.getSelectData().length > 1) {
        //多条数据的判断
        parent.layer.msg(bt.lang("common.message.confirm.selectBatchdate"), {icon: 2});
        return "";
    }
    //银票不允许贴现挂授信
    if (bt.datatable.getSelectData()[0].billType == 10) {
        parent.layer.msg(bt.lang("common.message.confirm.selectCommercial"), {icon: 2});
        return "";
    }
    var billType = bt.datatable.getSelectData()[0].billType;
    // 商票贴现挂银行授信模式未开启，无法挂接！
    if (billType == 11 && isDisCountCredit != "1") {
        parent.layer.msg(bt.lang("accebill.message.isClose.billType11"), {icon: 2});
        return "";
    }
    // 第三方票据贴现挂银行授信模式未开启，无法挂接银行授信！
    if (isBillTypeTP(billType) && isDisThirdBillCredit != '1') {
        parent.layer.msg(bt.lang("accebill.message.isClose.billTypeTb"), {icon: 2});
        return "";
    }

    var itemCode = bt.selectType.init().get("ratItemCode").val();//授信品种
    var curId = $("#curCode").val();//币别
    if (curId == null) {
        parent.layer.msg(bt.lang("accebill.message.curid.isnotselect"), {icon: 2});
        return "";
    }
    //启用直接贴现办理时
    if ($("#isNotDiscountApply").val() == 'true') {
        var recType = dicts.get("recType").val();
        //当类型为结算中心时，只支持贴现机构到银行
        if (recType == '1') {
            parent.layer.msg(bt.lang("accebill.message.banks.only"), {icon: 2});
            return "";
        }
    } else {
        //只支持贴现机构到银行
        if (bt.datatable.getSelectData()[0].insideSing != 0) {
            parent.layer.msg(bt.lang("accebill.message.banks.only"), {icon: 2});
            return "";
        }
    }

    var businessDate = $("#discountDate").val();//业务办理日期
    if (businessDate == "") {
        if (bt.datatable.getSelectData()[0].discountDate) {
            businessDate = bt.datatable.getSelectData()[0].discountDate;
        } else {
            parent.layer.msg(bt.lang("accebill.message.discountdate.isnotnull"), {icon: 2});
            return "";
        }
    }

    //银行授信单位类型
    var ratBankCorpType = $('input:radio[name="ratBankCorpType"]:checked').val();
    var corpId;
    var bankCode;
    //当选择授信单位是贴现申请人时
    if (ratBankCorpType == "0") {
        corpId = bt.datatable.getSelectData()[0].corpId;
        bankCode = bt.datatable.getSelectData()[0].recAcctsvcr;
        //启用直接贴现办理时
        if ($("#isNotDiscountApply").val() == 'true') {
            var recType = dicts.get("recType").val();
            if (recType == '1') {
                bankCode = $("#recAcctsvcr1").val();
            }
            if (recType == '0') {
                bankCode = $("#recAcctsvcr2").val();
            }
        }
        //当选择授信单位是承兑人时
    } else {
        //根据承兑人名称查询是否是内部单位
        corpId = $("#ratBankCorpId").val();
        //获取承兑人行号
        bankCode = bt.datatable.getSelectData()[0].acceptorbankno;
    }

    var params = {
        corpId: corpId,
        curCode: curId,
        bankCode: bankCode,
        ratItemCode: itemCode.trim(),
        beginDate: businessDate,
    }
    return params;
}

//选择授信
function chooseRatCode(params) {
    // bt.selectType.init().get("ratItemCode").val(null);
    setRationBtn();
    var params = beforeRation();
    if (params == "") {
        return;
    }
    openRation(params);
}

//查看授信
function viewRatCode() {
    var params = beforeRation();
    if (bt.empty(params)) {
        return;
    }
    viewRationDtl(params);
}

//根据承兑人名称查询是否是内部单位，不是则置灰承兑人授信单位类型选择
function loadIsCorpByCorpName(corpName) {
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abreceivebill/getCorpIdByName",
        data: {"name": corpName},
        async: false,
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                $("#ratBankCorpId").val(result.obj);
                dicts.get("ratBankCorpType").attr({"data-disabledValue": ""});
            } else {
                $("#ratBankCorpId").val("");
                dicts.get("ratBankCorpType").attr({"data-disabledValue": "1"});
            }
        }
    });
}

/**检查授信是否符合要求*/
function checkRelateRat() {
    //等于1 开启授信
    if (isCredit == "1" && dicts.get('ratmodel').val() == "1"
        && (isDisCountCredit == "1" || isDisThirdBillCredit == '1')) {
        // 银票不能挂授信
        if (bt.datatable.getSelectData()[0].billType == 10) {
            parent.layer.msg(bt.lang("common.message.confirm.selectCommercial"), {icon: 2});
            return false;
        }
        if (bt.datatable.getSelectData().length > 1) {
            //多条数据的判断
            parent.layer.msg(bt.lang("common.message.confirm.selectBatchdate"), {icon: 2});
            return "";
        }
        var billType = bt.datatable.getSelectData()[0].billType;
        // 商票贴现挂银行授信模式未开启，无法挂接银行授信！
        if (billType == 11 && isDisCountCredit != "1") {
            parent.layer.msg(bt.lang("accebill.message.isClose.billType11"), {icon: 2});
            return false;
        }
        // 第三方票据贴现挂银行授信模式未开启，无法挂接银行授信！
        if (isBillTypeTP(billType) && isDisThirdBillCredit != '1') {
            parent.layer.msg(bt.lang("accebill.message.isClose.billTypeTb"), {icon: 2});
            return false;
        }
        var itemMoney = $("#itemMoney").val();
        if (!itemMoney) {
            parent.layer.msg(bt.lang("accebill.message.relateRat.isnotselect"), {icon: 2});
            return false;
        } else if (Number(bt.datatable.getSelectData()[0].billMoney) > Number(itemMoney)) {
            parent.layer.msg(bt.lang("accebill.message.itemMoney.balToosmall"), {icon: 2});
            return false;
        }
        if ($("#rationBtn").css("display") == "none") {
            parent.layer.msg(bt.lang("accebill.message.relateRat.isnotselect"), {icon: 2});
            return false;
        }
        return true;
    } else {
        return true;
    }
}

// 是第三方票据类型则返回true
function isBillTypeTP(billType) {
    if (billType != 10 && billType != 11) {
        return true;
    }
    return false;
}


//查询时设置属性
function enterStockCall() {
    var $ = bt.jquery, form = bt.form();
    var enterStock = dicts.get("enterStock").val();
    //触发查询功能
    $("#searchBtn").click();
    //单位库存
    if (enterStock == '10') {
        //默认为银行
        $("#netTr2").hide();

        $("#bankTr1").show();
        dicts.get("recType").attr({"data-disabledValue": ""});
    }
    //中心代管
    if (enterStock == '11') {
        $("#netTr2").show();
        $("#bankTr1").hide();
        //隐藏银行radio
        dicts.get("recType").attr({"data-disabledValue": "0"});
        dicts.get("recType").val(1);
    }
    //银行托管
    if (enterStock == '12') {
        $("#netTr2").hide();
        $("#bankTr1").show();
        dicts.get("recType").attr({"data-disabledValue": "1"});
        dicts.get("recType").val(0);
    }

    $('#recAcctId1').val('');
    $('#recNm1').val('');
    $('#recAcctsvcr1').val('');
    $('#recAcctId2').val('');
    $('#recNm2').val('');
    $('#recAcctsvcr2').val('');
    $('#center').val('');
    $("#recCorpId").val('');
    netCallback('0');
}

//确定网点后回调
function netCallback(id, name) {
    //判断是否需要重新加载账户控件
    if ($("#recCorpId").val() && $("#recCorpId").val() == id) {
        return;
    }
    bt.accountTable.get("recAcctId1").reload({
        urldata: {
            corpId: id,
            electricBill: "1",
            permission: "",
            ignorePermission: "true"
        }
    });
    $("#recCorpId").val(id);
    $("#recNm1").val(name);
}

function recAcctIdClick(data) {
    var $ = bt.jquery, form = bt.form();
    $("#recAcctId1").val(data.bankAcc);
    $("#recNm1").val(data.accName);
    $("#recAcctsvcr1").val(data.bankCode);
}

//选择贴入机构时触发
function selectRecType() {
    var $ = bt.jquery, form = bt.form();
    var recType = dicts.get("recType").val();
    //结算中心
    if (recType == '1') {
        $("#netTr2").show();
        $("#bankTr1").hide();
    }
    //银行
    if (recType == '0') {
        $("#netTr2").hide();
        $("#bankTr1").show();
    }
    $('#recAcctId1').val('');
    $('#recNm1').val('');
    $('#recAcctsvcr1').val('');
    $('#recAcctId2').val('');
    $('#recNm2').val('');
    $('#recAcctsvcr2').val('');
    $('#center').val('');
    $("#recCorpId").val('');
    netCallback('0');

    if (recType == '1') {
        isSameNet();
    }
}

//判断所选单位是否在同一网点下
function isSameNet() {
    //票据贴现到网点进行判断
    var recType = dicts.get("recType").val();
    if (recType == '0') {
        return;
    }
    var selectData = getSelectData();
    if (!selectData.length) {
        netCallback('0');
        return;
    }
    var corpIds;
    var corpNames;
    for (var i = 0; i < selectData.length; i++) {
        if (corpIds) {
            corpIds = corpIds + "," + selectData[i].corpId;
            corpNames = corpNames + "," + selectData[i].corpName;
        } else {
            corpIds = selectData[i].corpId;
            corpNames = selectData[i].corpName;
        }
    }
    var listSysCorp;
    //获取所选数据的单位信息
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abBillDiscountApply/listSysCorp",
        data: {corpIds: corpIds, corpNames: corpNames},
        async: false,
        success: function (result) {
            if (result.success) {
                listSysCorp = result.obj;
            } else {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        },
        dataType: 'json'
    });
    if (!listSysCorp.length) {           //无法获取单位信息,请检查数据!
        //layer.alert(bt.lang('bill.accbill.abbillDiscountApply.corp.isnotexist'),{icon:7});
        return;
    }
    var firstParentId;
    for (var i = 0; i < listSysCorp.length; i++) {
        if (i == 0) {
            firstParentId = listSysCorp[i].parentId;
        } else {
            if (firstParentId != listSysCorp[i].parentId) {   //贴现到结算中心时,所选单位必须属于同一直属网点
                layer.alert(bt.lang('bill.accbill.abbillDiscountApply.corp.notSameNet'), {icon: 7});
                return;
            }
        }
    }
    if (!firstParentId) {         //所选单位为顶级网点,只能贴现到银行!
        layer.alert(bt.lang('bill.accbill.abbillDiscountApply.topCorp.toBank'), {icon: 7});
        return;
    }
    //获取对应网点信息
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abBillDiscountApply/getSysCorpById",
        data: {id: firstParentId},
        async: false,
        success: function (result) {
            if (result) {
                $("#center").val(result.name);
                //$("#recNm1").val(result.name);
                //判断网点是否相同
                netCallback(result.id, result.name);
            }
        },
        dataType: 'json'
    });
    return true;
}


//重置时设置属性
function resetSet() {
    var $ = bt.jquery, form = bt.form();
    dicts.get("recType").attr({"data-disabledValue": ""});
    $("#netTr2").hide();
    $("#bankTr1").show();
    $('#recAcctId1').val('');
    $('#recNm1').val('');
    $('#recAcctsvcr1').val('');
    $('#recAcctId2').val('');
    $('#recNm2').val('');
    $('#recAcctsvcr2').val('');
    $("#searchBtn").click();
    bt.datatable.refreshCurrentPage();
}

/**根据选择的业务设置必填标志
 * 选择数据包含电票则必填，不包含电票则不必填**/
function setVerify() {
    var billFlag = checkBillFlag();
    if (billFlag == 1) {
        $("#recAcctId2").attr("lay-verify", "required|strlength");
        $("#recAcctsvcr2").attr("lay-verify", "required|strlength");
    } else {
        $("#recAcctId2").attr("lay-verify", "");
        $("#recAcctsvcr2").attr("lay-verify", "");
    }
    form.renderIcon();
}

/**
 * 判断是纸票流程还是电票流程
 * @returns {Number} 0:纸票 1:电票
 */
function checkBillFlag() {
    var data = bt.datatable.getSelectData("#appTable");
    var len = data.length;
    var rowData = data.splice(0, len);
    var sign = 0;//处理标志 0:纸票处理标志 1:电票处理标志
    for (i = 0; i < len; i++) {
        if ("11" == rowData[i].billFlag) {//billFlag=11.则设置sign=1
            sign = 1;
            break;
        }
    }
    if (len == 0) {//若没选择数据，则按电票处理（即选择单位不赋值给质权人名称）
        sign = 1;
    }
    return sign;
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].id) < 0) {
                    idsValArray.push(datas[i].id);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].id);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].vouId) < 0) {
                    idsValArray.push(datas[i].vouId);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].vouId);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};



/**
 * 判断选择数据是否挂资金计划
 */
function isRenderBudget() {
    var selectData = bt.datatable.getSelectData("#appTable");
    if (selectData.length) {
        //判断票据系统是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //判断贴现挂接资金计划科目是否为空，不为空则挂资金计划
            var discountItem = $("#discountItem").val();
            if (discountItem != "") {
                //当选择多条数据后选择贴现时不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                if (selectData.length > 1) {
                    layer.alert(bt.lang('bill.accebill.billdiscount.isselectdatabudget'), {icon: 7});
                    $("#tagItemDetail").html("");
                    return false;
                } else {
                    for (i = 0; i < selectData.length; i++) {
                        //判断挂接资金计划
                        isBudget(selectData[i].corpId, "011");
                        return true;
                    }

                }
            }
        }
    }
    return true;
}


//根据单位ID和业务类型判断是否挂预算
function isBudget(corpId, briefId) {
    $.ajax({
        type: 'POST',
        url: basePath + "/bill/accebill/abvoucherinfoVice/isBudget",
        data: {'corpId': corpId, 'briefId': briefId},
        async: false,
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                if (result.attributes.isBudget) {
                    $genOtherHtml(corpId);
                } else {
                    $("#budgetDiv").hide();
                    $("#tagItemDetail").html("");
                }
            } else {
//	            	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                $("#budgetDiv").hide();
                $("#tagItemDetail").html("");
            }
        }
    });
}

/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml = function (corpId) {
    var $ = bt.jquery, form = bt.form();
    var itemCode = $('input[name="discountItem"]').val();
    var defItemCode = $('input[name="defItemCode"]').val();
    var processDate = $('input[name="businessDate"]').val();
    var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
    var curId = $("#curCode").val();
    var param = {
        corpId: corpId,
        itemCode: itemCode,
        defItemCode: defItemCode,
        processDate: processDate,
        curId: curId,
        type: 'select',
        date: (new Date()).getTime()
    };
    $.ajax({
        type: 'post',
        url: url,
        data: param,
        async: true,
        success: function (json) {
            $("#budgetDiv").show();
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            //设置执行总金额lay-verify属性
//        bt.use('moneyType',function(){
//        	bt.moneyType.init().get("sumMoney").attr("lay-verify","checkSumMoney");
//        });

        },
    });
};

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp", data);
}

//入账账户
clickCallback = function (data) {
    $('#aoaccninfId').val(data.bankAcc);
    $('#aoaccninfName').val(data.accName);
    $('#aoaccninfAc').val(data.bankCode);
}

//获取交易金额
function changeMoney(){
    bt.use(['moneyType'], function (){
        $ = bt.jquery, form = bt.form();
        var tableData = getSelectData();
        if (!tableData.length) {
            return;
        }
        var moneyDataLen = tableData.length;
        // 新系统出票且分包时，交易金额重新赋值
        for (var i = 0; i < moneyDataLen; i++) {

            if (tableData[i].newStmIssuofpprInd === '1' && tableData[i].blgrdbagcrclPrmtInd === '1') {
                // 判断交易金额是否为空
                var transMoney = $("#transMoney_" + tableData[i].accId).val()
                if (!transMoney) {
                    layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                    return;
                } else {
                    tableData[i].transMoney = transMoney;
                }
            }
        }

    });

}