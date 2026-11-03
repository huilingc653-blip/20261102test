var billTable = {}, dicts,item,moneyType;
var idsValArray=[];
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'dicts','itemType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    moneyType =bt.moneyType.init();
    bt.dateType.init();
    dicts = bt.dicts.init();
    item=bt.itemType.init();
    bt.mulselect.init();

    //生成列表
    billTable = bt.datatable.render({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillcollectionapply/datatable"
        },
        buttons: [
            //提交、导入、下载
            {
                text: bt.lang('bill.collection.Application'),
                hide: bt.isnotResource('bill:accebill:collectionapply:submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submit(data);
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	exports(data);
                }
            },*/
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
            {data: "busCorpName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {
                data: "isOverdue", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isOverdue);
                }
            },
            {data: "sendSignName", bSortable: true},
            {data: "statusName", bSortable: true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            // {
            //     data: "transMoney", bSortable: true, className: 'textRight', width: 120,
            //     render: function (data, type, full, meta) {
            //         if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1') {
            //             return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType" ' +
            //                 'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
            //                 'data-id="transMoney_'+full.vid+'" data-name="'+full.vid+'" data-biz-name="transMoney_'+full.vid+'" />';
            //         }
            //         return bt.money.format(full.transMoney);
            //     }
            // },
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
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        // fnDrawCallback:function(){
        //     idsValArray=new Array();
        //     setSumMoney();
        // },
        btClickRowCallback:function(data){
            var selectData = bt.datatable.getSelectData("#appTable");

            //判断系统是否挂资金计划
            var budgetSel = $("#budgetSel").val();
            if (budgetSel == "1") {
                //判断托收挂接资金计划科目是否为空，不为空则挂资金计划
                var collectionItem = $("#collectionItem").val();
                if (collectionItem != "") {
                    //当选择多条数据后不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                    if (selectData.length > 1) {
                        layer.alert(bt.lang('bill.accebill.billcollection.isselectdatabudget'), {icon: 7});
                        $("#tagItemDetail").html("");
                        //全反选，重新加载表格
                        billTable.draw();
                        $('.checkAll').prop('checked',false);
                    } else if(selectData.length == 1){
                        isBudget(selectData[0].corpId, "020");
                    }
                } else {
                    $("#tagItemDetail").html("");
                }
            }
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            // bt.moneyType.init();
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 2
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '11',
                    dataName: 'billMoney'
                }
            ]
        }
    });

    //是否启用线上清算
    $.post(basePath + '/bill/accebill/abvoucherinfoVice/isEOnlinePay', function (result) {
        if (!result.success) {
            dicts.get("sttlmmk").attr({"data-filterData": "SM00"});
        }
    }, 'json')

    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    var dd = bt.btdate.init({
        elem: ".date-single",
        // labelname:bt.lang('table.abVoucherinfo.Business.date'),
        name: "businessdate"
    });
    dd.setValue({value: new Date().Format("yyyy-MM-dd")})

    mulselect();
});


//是否挂资金计划，挂则列表只能单选，否则可以多选
function mulselect() {
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        //判断托收挂接资金计划科目是否为空，不为空则挂资金计划
        var collectionItem = $("#collectionItem").val();
        if (collectionItem != "") {
            billTable.attr({mulselect: false});
        } else {
            billTable.attr({mulselect: true});
        }
    }
}

//托收申请
function submit(data) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var $ = bt.jquery;
    len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    for (i = 0; i < len; i++) {
        if (!$("#isOverdueRmk").val() && rowData[i].isOverdue == 1) {
            //提交票据中包含逾期票据，请填写逾期托收说明!
            parent.layer.alert(bt.lang('accebill.abbillcollectionapply.message.isOverdue'));
            return;
        }
        if (dicts.get('sttlmmk').val() == " " && rowData[i].billFlag == 11) {
            //提交票据中包含电票，请选择清算方式!
            parent.layer.alert(bt.lang('accebill.abbillcollectionapply.message.sttlmmk'));
            return;
        }
    }

    //判断是否挂资金计划,检查金额
    if(!checkBudgetSumMoney()){
        bt.loading.hide();
        return false;
    }
    //证书签名
    var signDataMsg = signData("托收申请提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit.abbillcollectionapply'), function () {//确定提交托收申请吗?
        var vids, vvids, aids;
        for (i = 0; i < len; i++) {
            if (vids) {
                vids = vids + "," + rowData[i].vid;
            } else {
                vids = rowData[i].vid;
            }
            if (vvids) {
                vvids = vvids + "," + rowData[i].vvid;
            } else {
                vvids = rowData[i].vvid;
            }
            if (aids) {
                aids = aids + "," + rowData[i].aid;
            } else {
                aids = rowData[i].aid;
            }
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

       /* $.post(basePath + "/bill/accebill/abbillcollectionapply/collectionApply", {
            "vids": vids, "vvids": vvids, "aids": aids,
            "budgetList":budgetList,
            "rmk": $("#rmk").val(), "isOverdueRmk": $("#isOverdueRmk").val(), "sttlmmk": dicts.get('sttlmmk').val(),
            "businessDate": $("[name=businessdate]").val()
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
*/
        bt.loading.show();//显示加载中
        bt.jquery.ajax({
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                "vids": vids,
                "vvids": vvids,
                "aids": aids,
                "budgetList":budgetList,
                "rmk": $("#rmk").val(),
                "isOverdueRmk": $("#isOverdueRmk").val(),
                "sttlmmk": dicts.get('sttlmmk').val(),
                "businessDate": $("[name=businessdate]").val()
            }),
            asycn: false,
            url: basePath + "/bill/accebill/abbillcollectionapply/collectionApply",
            success: function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
                bt.loading.hide(); //关闭加载中
            },
            dataType: 'json'
        });

    });
}


//判断是否挂资金计划,检查金额
function checkBudgetSumMoney(){
    var rowData  = bt.datatable.getSelectData("#appTable");
    var len = rowData.length;
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        if ($('#budgetDiv').css('display') != 'none') { 	//处理任何条件下不管有没有上报金额都会提示校验金额与资金计划总金额一致
            //判断托收挂接资金计划科目是否为空，不为空则挂资金计划
            var collectionItem = $("#collectionItem").val();
            if (collectionItem != "") {
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

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.aid
    });
}

//附件
function attachmentView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var isOverdue = '020';
    if (data.isOverdue == 1) {
        isOverdue = '021';
    }
    var params = {
        "id": data.aid,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.vvid || null),
        "briefId": isOverdue,
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

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        var budgetSel = $("#budgetSel").val();
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].id)<0){
                    if (budgetSel == "1") {
                        idsValArray = []
                    }
                    idsValArray.push(datas[i].id);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].id);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};



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
    var itemCode = $('input[name="collectionItem"]').val();
    var defItemCode = $('input[name="defItemCode"]').val();
    var processDate = $("[name=businessdate]").val();
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
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}
