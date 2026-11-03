bt.use(['form', 'layedit', 'moneyType', 'datatable', 'btUpload', 'printType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        isExpand: true,
        gid: $("#attachmentId").val()
    });
    bt.printType.init();

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
        scrollY: '',
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
            // {data: "difBankName", bSortable: true, className: 'center'},
            // {data: "areaSignName", bSortable: true, className: 'center'},
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
                data: "voucherStatName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    //高亮显示交易失败的明细
                    if (full.voucherStat == "2" || full.voucherStat == "9") {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("remind");
                    }
                    return full.voucherStatName;
                }
            },
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true}
        ]
    });

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#wishPayday").val(),
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
    }

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断代发工资是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            $genOtherHtml(corpId);
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/netbank/nismessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();

    /**
     * 查看
     * @param id
     */
    viewDetail = function (wageId,opType) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswagedetail/view/" + wageId + "/"+opType);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/wage/niswagedetail/view/" + wageId + "/"+opType
        });
    }
});